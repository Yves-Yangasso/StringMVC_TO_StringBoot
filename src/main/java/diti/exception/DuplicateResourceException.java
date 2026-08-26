package diti.exception;

public class DuplicateResourceException extends ApiException {

    public DuplicateResourceException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public static DuplicateResourceException produit(String libelle) {
        return new DuplicateResourceException(
                ErrorCode.PRODUIT_DEJA_EXISTANT,
                "Un produit nomme '" + libelle + "' existe deja"
        );
    }

    public static DuplicateResourceException typeProduit(String libelle) {
        return new DuplicateResourceException(
                ErrorCode.TYPE_PRODUIT_DEJA_EXISTANT,
                "Un type de produit nomme '" + libelle + "' existe deja"
        );
    }
}
