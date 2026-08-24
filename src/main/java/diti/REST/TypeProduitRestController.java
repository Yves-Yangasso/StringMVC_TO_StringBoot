package diti.REST;


import diti.entity.TypeProduit;
import diti.service.TypeProduitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/typeproduit")
public class TypeProduitRestController {


    @Autowired
    private TypeProduitService typeProduitService;


    @GetMapping
    public List<TypeProduit> getList(){
        return typeProduitService.findAll();
    }

    @PostMapping
    public String save(@Valid @RequestBody TypeProduit typeProduit){
        typeProduitService.save(typeProduit);
        return "type produit ajoute avec succes";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        typeProduitService.delete(id);
        return "type produit supprime avec succes";
    }


    @GetMapping("/{id}")
    public TypeProduit getById(@PathVariable Long id){
        return typeProduitService.findById(id);
    }


}
