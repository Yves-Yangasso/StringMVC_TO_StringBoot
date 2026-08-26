package diti.service;

import diti.entity.Produit;
import java.util.List;
import java.util.UUID;

public interface ProductService {

    void save(Produit product);

    List<Produit> findAll();

    List<Produit> findAll(int page, int size);

    Produit findById(UUID id);

    void delete(UUID id);
}
