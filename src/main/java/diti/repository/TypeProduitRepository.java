package diti.repository;

import diti.entity.TypeProduit;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeProduitRepository extends JpaRepository<TypeProduit, UUID> {

    boolean existsByLibelleIgnoreCase(String libelle);
}
