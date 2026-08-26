package diti.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Corps de reponse unique pour toutes les erreurs de l'API.
 */
@Schema(name = "ApiError", description = "Reponse renvoyee pour toute erreur de l'API")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ApiError(

        @Schema(description = "Date et heure de l'erreur", example = "2026-08-26T14:32:10.123+02:00")
        OffsetDateTime timestamp,

        @Schema(description = "Statut HTTP", example = "404")
        int status,

        @Schema(description = "Code d'erreur metier, stable et exploitable par le client",
                example = "PRODUIT_INTROUVABLE")
        ErrorCode code,

        @Schema(description = "Message lisible expliquant l'erreur",
                example = "Produit introuvable avec l'id 42")
        String message,

        @Schema(description = "Chemin de la requete a l'origine de l'erreur", example = "/api/produit/42")
        String path,

        @Schema(description = "Detail des champs invalides (present uniquement en cas d'erreur de validation)")
        List<FieldError> errors
) {

    public static ApiError of(ErrorCode code, String message, String path) {
        return new ApiError(OffsetDateTime.now(), code.getStatus().value(), code, message, path, List.of());
    }

    public static ApiError of(ErrorCode code, String message, String path, List<FieldError> errors) {
        return new ApiError(OffsetDateTime.now(), code.getStatus().value(), code, message, path, errors);
    }

    @Schema(name = "FieldError", description = "Champ en erreur lors de la validation")
    public record FieldError(

            @Schema(description = "Nom du champ concerne", example = "libelle")
            String field,

            @Schema(description = "Raison du rejet", example = "Le libelle est obligatoire")
            String message,

            @Schema(description = "Valeur rejetee", example = "")
            Object rejectedValue
    ) {
    }
}
