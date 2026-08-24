package diti.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "type_produits")
public class TypeProduit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le libelle est obligatoire")
    @Size(min = 2, max = 100, message = "Le libelle doit contenir entre {min} et {max} caracteres")
    private String libelle;

    public TypeProduit() {
    }

    public TypeProduit(Long id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return "TypeProduit{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                '}';
    }
}
