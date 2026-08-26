package diti.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class ProduitDTO {

    // Genere par le serveur : un id envoye par le client est ignore.
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    @NotBlank(message = "Le libelle est obligatoire")
    @Size(min = 2, max = 100, message = "Le libelle doit contenir entre {min} et {max} caracteres")
    private String libelle;

    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit etre superieur a 0")
    private double prix;

    @NotNull(message = "Le type est obligatoire")
    private UUID typeProduitId;

    private String typeProduitLibelle;

    public ProduitDTO() {
    }

    public ProduitDTO(UUID id, String libelle, double prix, UUID typeProduitId, String typeProduitLibelle) {
        this.id = id;
        this.libelle = libelle;
        this.prix = prix;
        this.typeProduitId = typeProduitId;
        this.typeProduitLibelle = typeProduitLibelle;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public UUID getTypeProduitId() {
        return typeProduitId;
    }

    public void setTypeProduitId(UUID typeProduitId) {
        this.typeProduitId = typeProduitId;
    }

    public String getTypeProduitLibelle() {
        return typeProduitLibelle;
    }

    public void setTypeProduitLibelle(String typeProduitLibelle) {
        this.typeProduitLibelle = typeProduitLibelle;
    }

    @Override
    public String toString() {
        return "ProduitDTO{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                ", prix=" + prix +
                ", typeProduitId=" + typeProduitId +
                '}';
    }
}
