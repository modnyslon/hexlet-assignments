package exercise.mapper;

import exercise.dto.TaskCreateDTO;
import exercise.dto.TaskDTO;
import exercise.dto.TaskUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.model.Task;
import exercise.model.User;
import exercise.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Mapping(target = "assignee.id", source = "assigneeId")
    public abstract Task map(TaskCreateDTO dto);
    @Mapping(source = "assignee.id", target = "assigneeId")
    public abstract TaskDTO map(Task model);

//    @Mapping(target = "assignee.id", source = "assigneeId")
//    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);
    @Autowired
    private UserRepository userRepository;  // Добавляем репозиторий для загрузки User

    @Mapping(target = "assignee", expression = "java(getUserById(dto.getAssigneeId()))")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task task);

    protected User getUserById(Long assigneeId) {
        if (assigneeId == null) {
            return null;  // Если assigneeId не задан, оставляем assignee как null
        }
        return userRepository.findById(assigneeId)  // Иначе загружаем User из базы
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + assigneeId));
    }


}
