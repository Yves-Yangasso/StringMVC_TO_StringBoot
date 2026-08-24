package diti.service.impl;


import diti.entity.TypeProduit;
import diti.repository.TypeProduitRepository;
import diti.service.TypeProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeProduitServiceImpl implements TypeProduitService {

    @Autowired
    private TypeProduitRepository repository ;

    @Override
    public void save(TypeProduit typeProduit) {
        repository.save(typeProduit);
    }

    @Override
    public List<TypeProduit> findAll() {
        return repository.findAll();
    }

    @Override
    public TypeProduit findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.findById(id).ifPresent(repository::delete);
    }
}
