package diti.service.impl;

import diti.entity.Produit;
import diti.exception.ResourceNotFoundException;
import diti.repository.ProductRepository;
import diti.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Override
    public void save(Produit product) {
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
    public Produit findById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produit introuvable avec l'id " + id
                ));
    }

    @Override
    public void delete(Long id) {
        repository.findById(id).ifPresent(repository::delete);
    }
}
