package diti.repository;

import diti.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Produit, Long> {
}
