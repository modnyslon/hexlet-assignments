package exercise.mapper;

import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

// BEGIN
@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ReferenceMapper.class, JsonNullableMapper.class}
)
public abstract class ProductMapper {

    @Mapping(source = "categoryId", target = "category", qualifiedByName = "idToCategory")
    public abstract Product map(ProductCreateDTO dto);

    @Mapping(source = "category", target = "categoryId", qualifiedByName = "categoryToId")
    @Mapping(source = "category", target = "categoryName", qualifiedByName = "categoryToName")
    public abstract ProductDTO map(Product model);

    public abstract void update(ProductUpdateDTO dto, @MappingTarget Product model);

}
// END
