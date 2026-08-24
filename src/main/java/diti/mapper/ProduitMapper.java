package diti.mapper;

import diti.dto.ProduitDTO;
import diti.entity.Produit;
import diti.entity.TypeProduit;
import diti.exception.ResourceNotFoundException;
import diti.service.TypeProduitService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ProduitMapper {

    @Autowired
    protected TypeProduitService typeProduitService;

    @Mapping(source = "typeProduit.id", target = "typeProduitId")
    @Mapping(source = "typeProduit.libelle", target = "typeProduitLibelle")
    public abstract ProduitDTO toDto(Produit produit);

    @Mapping(source = "typeProduitId", target = "typeProduit")
    public abstract Produit toEntity(ProduitDTO dto);

    protected TypeProduit mapTypeProduit(Long typeProduitId) {
        if (typeProduitId == null) {
            return null;
        }

        TypeProduit typeProduit = typeProduitService.findById(typeProduitId);

        if (typeProduit == null) {
            throw new ResourceNotFoundException(
                    "Type de produit introuvable avec l'id " + typeProduitId
            );
        }

        return typeProduit;
    }
}
