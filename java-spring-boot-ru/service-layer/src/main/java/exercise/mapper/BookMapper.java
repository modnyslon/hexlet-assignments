package exercise.mapper;

import exercise.dto.BookCreateDTO;
import exercise.dto.BookDTO;
import exercise.dto.BookUpdateDTO;
import exercise.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class BookMapper {

    // BEGIN
    @Mapping(source = "authorId", target = "author") //, qualifiedByName = "idToAuthor"
    public abstract Book map(BookCreateDTO dto);

    @Mapping(source = "author.id", target = "authorId") //, qualifiedByName = "authorToId"
    @Mapping(source = "author.firstName", target = "authorFirstName") //, qualifiedByName = "authorToFirstName"
    @Mapping(source = "author.lastName", target = "authorLastName") //, qualifiedByName = "authorToLastName"
    public abstract BookDTO map(Book model);
    // END

    @Mapping(target = "author", source = "authorId")
    public abstract void update(BookUpdateDTO dto, @MappingTarget Book model);
}
