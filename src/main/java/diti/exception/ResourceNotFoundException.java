package diti.exception;

import java.util.UUID;

public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public static ResourceNotFoundException produit(UUID id) {
        return new ResourceNotFoundException(
                ErrorCode.PRODUIT_INTROUVABLE,
                "Produit introuvable avec l'id " + id
        );
    }

    public static ResourceNotFoundException typeProduit(UUID id) {
        return new ResourceNotFoundException(
                ErrorCode.TYPE_PRODUIT_INTROUVABLE,
                "Type de produit introuvable avec l'id " + id
        );
    }
}
