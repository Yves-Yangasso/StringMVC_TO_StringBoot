package diti.exception;

import org.springframework.http.HttpStatus;

/**
 * Catalogue des erreurs de l'API : un code stable expose au client,
 * associe a un statut HTTP et a un message par defaut.
 */
public enum ErrorCode {

    // 400
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Les donnees envoyees sont invalides"),
    MALFORMED_JSON(HttpStatus.BAD_REQUEST, "Le corps de la requete est illisible ou mal forme"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Un parametre de la requete est invalide"),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "Un parametre obligatoire est absent"),

    // 404
    PRODUIT_INTROUVABLE(HttpStatus.NOT_FOUND, "Produit introuvable"),
    TYPE_PRODUIT_INTROUVABLE(HttpStatus.NOT_FOUND, "Type de produit introuvable"),
    ENDPOINT_INTROUVABLE(HttpStatus.NOT_FOUND, "Cette ressource n'existe pas"),

    // 405 / 415
    METHODE_NON_AUTORISEE(HttpStatus.METHOD_NOT_ALLOWED, "Methode HTTP non supportee pour cette ressource"),
    MEDIA_TYPE_NON_SUPPORTE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Format de contenu non supporte"),

    // 409
    PRODUIT_DEJA_EXISTANT(HttpStatus.CONFLICT, "Un produit portant ce libelle existe deja"),
    TYPE_PRODUIT_DEJA_EXISTANT(HttpStatus.CONFLICT, "Un type de produit portant ce libelle existe deja"),
    CONFLIT_DONNEES(HttpStatus.CONFLICT, "L'operation viole une contrainte d'integrite des donnees"),

    // 500
    ERREUR_INTERNE(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur interne est survenue");

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
