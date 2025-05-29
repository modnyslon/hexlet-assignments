package exercise.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;

// BEGIN
@SpringBootTest
@AutoConfigureMockMvc
// END
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;

    private Task testTask;

    @BeforeEach
    public void setUp() {
        testTask = new Task();
        testTask.setTitle("Test task - " + faker.book().title());
        testTask.setDescription("Test description - " + faker.book().title());
        taskRepository.save(testTask);
    }

    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    // BEGIN
    @Test
    public void testShow() throws Exception {
        mockMvc.perform(get("/tasks/{id}", testTask.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(om.writeValueAsString(testTask)))
                .andExpect(jsonPath("$.id").value(testTask.getId()))
                .andExpect(jsonPath("$.title").value(testTask.getTitle()))
                .andExpect(jsonPath("$.description").value(testTask.getDescription()));
    }

    @Test
    public void testShowNegative() throws Exception {
        var result = mockMvc.perform(get("/tasks/{id}", 100))
                .andExpect(status().isNotFound())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Task with id 100 not found");
    }

    @Test
    public void testCreate() throws Exception {

        var request = post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testTask));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(testTask.getTitle()))
                .andExpect(jsonPath("$.description").value(testTask.getDescription()))
                .andReturn();

        var actualTask = taskRepository.findById(testTask.getId()).get();
        assertThat(actualTask.getCreatedAt()).isNotNull();
        assertThat(actualTask.getUpdatedAt()).isNotNull();
    }

    @Test
    public void testUpdate() throws Exception {
//        Task task = new Task();
        var task = taskRepository.save(testTask);

        // Подготавливаем обновленные данные
        Map<String, String> updateData = new HashMap<>();
        updateData.put("title", "Updated title");
        updateData.put("description", "Updated description");

        var request = put("/tasks/{id}", task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(updateData));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var actualTask = taskRepository.findById(testTask.getId()).get();

        assertThat(actualTask.getTitle()).isEqualTo(updateData.get("title"));
        assertThat(actualTask.getDescription()).isEqualTo(updateData.get("description"));
    }

    @Test
    public void testUpdateNegative() throws Exception {

        var request = put("/products/{id}", 100)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testTask));

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDelete() throws Exception {

        mockMvc.perform(delete("/tasks/{id}", testTask.getId()))
                .andExpect(status().isOk());

        assertThat(taskRepository.findById(testTask.getId())).isEmpty();
    }


}
