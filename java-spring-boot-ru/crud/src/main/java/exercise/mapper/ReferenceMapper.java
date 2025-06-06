package exercise.mapper;

import exercise.exception.ResourceNotFoundException;
import exercise.model.Category;
import exercise.repository.CategoryRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;

import exercise.model.BaseEntity;
import jakarta.persistence.EntityManager;

// BEGIN
@Mapper(componentModel = "spring")  // Интеграция со Spring
public abstract class ReferenceMapper {

    @Autowired
    private CategoryRepository categoryRepository;  // Для загрузки Category по ID

    // Преобразование categoryId → Category (используется при создании Product)
    @Named("idToCategory")
    public Category idToCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    // Преобразование Category → categoryId (используется при маппинге в DTO)
    @Named("categoryToId")
    public Long categoryToId(Category category) {
        return category != null ? category.getId() : null;
    }

    // Преобразование Category → categoryName (используется при маппинге в DTO)
    @Named("categoryToName")
    public String categoryToName(Category category) {
        return category != null ? category.getName() : null;
    }
}
// END
