package diti.service;

import diti.entity.TypeProduit;
import java.util.List;

public interface TypeProduitService {

    void save(TypeProduit typeProduit);

    List<TypeProduit> findAll();

    TypeProduit findById(Long id);

    void delete(Long id);
}
