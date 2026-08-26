package diti.service;

import diti.entity.TypeProduit;
import java.util.List;
import java.util.UUID;

public interface TypeProduitService {

    void save(TypeProduit typeProduit);

    List<TypeProduit> findAll();

    TypeProduit findById(UUID id);

    void delete(UUID id);
}
