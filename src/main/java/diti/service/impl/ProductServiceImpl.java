package diti.service.impl;

import diti.entity.Produit;
import diti.exception.DuplicateResourceException;
import diti.exception.ResourceNotFoundException;
import diti.repository.ProductRepository;
import diti.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Override
    public void save(Produit product) {
        if (repository.existsByLibelleIgnoreCase(product.getLibelle())) {
            throw DuplicateResourceException.produit(product.getLibelle());
        }
        repository.save(product);
    }

    @Override
    public List<Produit> findAll() {
        return repository.findAll(Sort.by("id"));
    }


    @Override
    public List<Produit> findAll(int page, int size) {

        if (page < 0) {
            page = 0;
        }

        if (size <= 0) {
            size = 5;
        }

        return repository.findAll(PageRequest.of(page, size, Sort.by("id")))
                .getContent();
    }

    @Override
    public Produit findById(UUID id) {

        return repository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.produit(id));
    }

    @Override
    public void delete(UUID id) {
        repository.delete(findById(id));
    }
}
