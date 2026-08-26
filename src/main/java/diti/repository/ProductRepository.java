package diti.repository;

import diti.entity.Produit;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Produit, UUID> {

    boolean existsByLibelleIgnoreCase(String libelle);
}
