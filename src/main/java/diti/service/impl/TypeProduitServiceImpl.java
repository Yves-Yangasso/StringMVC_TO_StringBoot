package diti.service.impl;


import diti.entity.TypeProduit;
import diti.exception.DuplicateResourceException;
import diti.exception.ResourceNotFoundException;
import diti.repository.TypeProduitRepository;
import diti.service.TypeProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TypeProduitServiceImpl implements TypeProduitService {

    @Autowired
    private TypeProduitRepository repository ;

    @Override
    public void save(TypeProduit typeProduit) {
        if (repository.existsByLibelleIgnoreCase(typeProduit.getLibelle())) {
            throw DuplicateResourceException.typeProduit(typeProduit.getLibelle());
        }
        repository.save(typeProduit);
    }

    @Override
    public List<TypeProduit> findAll() {
        return repository.findAll();
    }

    @Override
    public TypeProduit findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.typeProduit(id));
    }

    @Override
    public void delete(UUID id) {
        repository.delete(findById(id));
    }
}
