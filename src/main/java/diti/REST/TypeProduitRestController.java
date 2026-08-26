package diti.REST;


import diti.entity.TypeProduit;
import diti.exception.ApiError;
import diti.service.TypeProduitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/typeproduit")
@Tag(name = "Types de produit", description = "Gestion des types de produit")
@ApiResponse(responseCode = "500", description = "Erreur interne (code ERREUR_INTERNE)",
        content = @Content(schema = @Schema(implementation = ApiError.class)))
public class TypeProduitRestController {


    @Autowired
    private TypeProduitService typeProduitService;


    @Operation(summary = "Lister tous les types de produit")
    @ApiResponse(responseCode = "200", description = "Liste des types de produit")
    @GetMapping
    public List<TypeProduit> getList(){
        return typeProduitService.findAll();
    }

    @Operation(summary = "Creer un type de produit")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Type de produit cree"),
            @ApiResponse(responseCode = "400", description = "Donnees invalides (code VALIDATION_ERROR ou MALFORMED_JSON)",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Un type porte deja ce libelle (code TYPE_PRODUIT_DEJA_EXISTANT)",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public String save(@Valid @RequestBody TypeProduit typeProduit){
        typeProduitService.save(typeProduit);
        return "type produit ajoute avec succes";
    }

    @Operation(summary = "Supprimer un type de produit",
            description = "Echoue en 409 si des produits referencent encore ce type.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Type de produit supprime"),
            @ApiResponse(responseCode = "404", description = "Type inexistant (code TYPE_PRODUIT_INTROUVABLE)",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Type encore utilise par des produits (code CONFLIT_DONNEES)",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable UUID id){
        typeProduitService.delete(id);
        return "type produit supprime avec succes";
    }


    @Operation(summary = "Recuperer un type de produit par son id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Type de produit trouve"),
            @ApiResponse(responseCode = "400", description = "Id non conforme au format UUID (code INVALID_PARAMETER)",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Type inexistant (code TYPE_PRODUIT_INTROUVABLE)",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public TypeProduit getById(@PathVariable UUID id){
        return typeProduitService.findById(id);
    }


}
