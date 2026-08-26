package diti.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

/**
 * Traduit toute exception remontant des controleurs en {@link ApiError}.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** Exceptions metier : le code d'erreur porte deja le statut HTTP. */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApiException(ApiException ex, HttpServletRequest request) {
        return build(ex.getErrorCode(), ex.getMessage(), request);
    }

    /** @Valid sur un @RequestBody. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex,
                                                     HttpServletRequest request) {

        List<ApiError.FieldError> champs = ex.getBindingResult().getFieldErrors().stream()
                .map(erreur -> new ApiError.FieldError(
                        erreur.getField(),
                        erreur.getDefaultMessage(),
                        erreur.getRejectedValue()))
                .toList();

        return ResponseEntity
                .status(ErrorCode.VALIDATION_ERROR.getStatus())
                .body(ApiError.of(
                        ErrorCode.VALIDATION_ERROR,
                        ErrorCode.VALIDATION_ERROR.getDefaultMessage(),
                        request.getRequestURI(),
                        champs));
    }

    /** Contraintes portees par les parametres de methode (@Validated). */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex,
                                                              HttpServletRequest request) {

        List<ApiError.FieldError> champs = ex.getConstraintViolations().stream()
                .map(violation -> new ApiError.FieldError(
                        violation.getPropertyPath().toString(),
                        violation.getMessage(),
                        violation.getInvalidValue()))
                .toList();

        return ResponseEntity
                .status(ErrorCode.VALIDATION_ERROR.getStatus())
                .body(ApiError.of(
                        ErrorCode.VALIDATION_ERROR,
                        ErrorCode.VALIDATION_ERROR.getDefaultMessage(),
                        request.getRequestURI(),
                        champs));
    }

    /** JSON absent, tronque ou avec un type incompatible. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadableBody(HttpMessageNotReadableException ex,
                                                         HttpServletRequest request) {
        log.debug("Corps de requete illisible sur {}", request.getRequestURI(), ex);
        return build(ErrorCode.MALFORMED_JSON, ErrorCode.MALFORMED_JSON.getDefaultMessage(), request);
    }

    /** /api/produit/abc alors que l'id attendu est un UUID. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                       HttpServletRequest request) {

        String typeAttendu = ex.getRequiredType() == null ? "inconnu" : ex.getRequiredType().getSimpleName();
        String message = "Le parametre '%s' doit etre de type %s (valeur recue : %s)"
                .formatted(ex.getName(), typeAttendu, ex.getValue());

        return build(ErrorCode.INVALID_PARAMETER, message, request);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingParameter(MissingServletRequestParameterException ex,
                                                           HttpServletRequest request) {
        return build(ErrorCode.MISSING_PARAMETER,
                "Le parametre '" + ex.getParameterName() + "' est obligatoire",
                request);
    }

    /** URL qui ne correspond a aucun endpoint. */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleNoResource(NoResourceFoundException ex,
                                                     HttpServletRequest request) {
        return build(ErrorCode.ENDPOINT_INTROUVABLE,
                "Aucune ressource pour " + request.getMethod() + " " + request.getRequestURI(),
                request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                             HttpServletRequest request) {
        return build(ErrorCode.METHODE_NON_AUTORISEE,
                "La methode " + ex.getMethod() + " n'est pas supportee sur cette ressource",
                request);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiError> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                HttpServletRequest request) {
        return build(ErrorCode.MEDIA_TYPE_NON_SUPPORTE,
                "Le type de contenu " + ex.getContentType() + " n'est pas supporte",
                request);
    }

    /**
     * save() a fait un merge sur une ligne absente : typiquement un client qui envoie
     * un id inexistant. Filet de securite, les id sont deja ignores en entree.
     */
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiError> handleStaleObject(ObjectOptimisticLockingFailureException ex,
                                                      HttpServletRequest request) {
        log.warn("Entite modifiee ou absente sur {}", request.getRequestURI(), ex);
        return build(ErrorCode.CONFLIT_DONNEES,
                "La ressource visee n'existe plus ou a ete modifiee entre-temps",
                request);
    }

    /** Contrainte SQL violee : cle etrangere encore referencee, unicite, NOT NULL... */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex,
                                                        HttpServletRequest request) {
        log.warn("Violation d'integrite sur {}", request.getRequestURI(), ex);
        return build(ErrorCode.CONFLIT_DONNEES, ErrorCode.CONFLIT_DONNEES.getDefaultMessage(), request);
    }

    /** Filet de securite : rien de l'exception d'origine n'est expose au client. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception ex, HttpServletRequest request) {
        log.error("Erreur non geree sur {}", request.getRequestURI(), ex);
        return build(ErrorCode.ERREUR_INTERNE, ErrorCode.ERREUR_INTERNE.getDefaultMessage(), request);
    }

    private ResponseEntity<ApiError> build(ErrorCode code, String message, HttpServletRequest request) {
        return ResponseEntity
                .status(code.getStatus())
                .body(ApiError.of(code, message, request.getRequestURI()));
    }
}
