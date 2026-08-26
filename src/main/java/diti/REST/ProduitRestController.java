package diti.REST;

import diti.dto.ProduitDTO;
import diti.exception.ApiError;
import diti.mapper.ProduitMapper;
import diti.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produit")
@Tag(name = "Produits", description = "Gestion des produits")
@ApiResponse(responseCode = "500", description = "Erreur interne (code ERREUR_INTERNE)",
        content = @Content(schema = @Schema(implementation = ApiError.class)))
public class ProduitRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProduitMapper produitMapper;

    @Operation(summary = "Lister les produits", description = "Retourne les produits page par page, tries par id.")
    @ApiResponse(responseCode = "200", description = "Liste des produits")
    @ApiResponse(responseCode = "400", description = "Parametre de pagination invalide (code INVALID_PARAMETER)",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @GetMapping
    public List<ProduitDTO> getList(
            @Parameter(description = "Numero de page, commence a 0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Nombre d'elements par page") @RequestParam(defaultValue = "5") int size) {

        return productService.findAll(page, size)
                .stream()
                .map(produitMapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Creer un produit")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produit cree"),
            @ApiResponse(responseCode = "400", description = "Donnees invalides (code VALIDATION_ERROR ou MALFORMED_JSON)",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Type de produit inexistant (code TYPE_PRODUIT_INTROUVABLE)",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Un produit porte deja ce libelle (code PRODUIT_DEJA_EXISTANT)",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public String save(@Valid @RequestBody ProduitDTO produitDTO) {
        productService.save(produitMapper.toEntity(produitDTO));
        return "produit ajoute avec succes";
    }

    @Operation(summary = "Supprimer un produit")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produit supprime"),
            @ApiResponse(responseCode = "404", description = "Produit inexistant (code PRODUIT_INTROUVABLE)",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        productService.delete(id);
        return "produit supprime avec succes";
    }

    @Operation(summary = "Recuperer un produit par son id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produit trouve"),
            @ApiResponse(responseCode = "400", description = "Id non conforme au format UUID (code INVALID_PARAMETER)",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Produit inexistant (code PRODUIT_INTROUVABLE)",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public ProduitDTO getById(@PathVariable UUID id) {
        return produitMapper.toDto(
                productService.findById(id)
        );
    }
}
