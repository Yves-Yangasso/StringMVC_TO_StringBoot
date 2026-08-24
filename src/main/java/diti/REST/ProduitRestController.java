package diti.REST;

import diti.dto.ProduitDTO;
import diti.mapper.ProduitMapper;
import diti.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produit")
public class ProduitRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProduitMapper produitMapper;

    @GetMapping
    public List<ProduitDTO> getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return productService.findAll(page, size)
                .stream()
                .map(produitMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public String save(@Valid @RequestBody ProduitDTO produitDTO) {
        productService.save(produitMapper.toEntity(produitDTO));
        return "produit ajoute avec succes";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "produit supprime avec succes";
    }

    @GetMapping("/{id}")
    public ProduitDTO getById(@PathVariable Long id) {
        return produitMapper.toDto(
                productService.findById(id)
        );
    }
}
