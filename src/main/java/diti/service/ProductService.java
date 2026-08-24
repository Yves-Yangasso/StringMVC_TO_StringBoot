package diti.service;

import diti.entity.Produit;
import java.util.List;

public interface ProductService {

    void save(Produit product);

    List<Produit> findAll();

    List<Produit> findAll(int page, int size);

    Produit findById(Long id);

    void delete(Long id);
}
