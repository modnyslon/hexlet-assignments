package exercise;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import exercise.model.Post;
import lombok.Setter;

@SpringBootApplication
@RestController
public class Application {
    // Хранилище добавленных постов
    @Setter
    private static List<Post> posts = Data.getPosts();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/posts") // Список страниц
    public ResponseEntity<List<Post>> index(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit
    ) {
        var result = posts.stream()
                .skip((page - 1) * limit)
                .limit(limit)
                .toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);
    }

    @PostMapping("/posts") // Создание страницы
    public ResponseEntity<Post> create(@RequestBody Post Post) {
        posts.add(Post);

        return ResponseEntity.status(HttpStatus.CREATED).body(Post);
    }

    @GetMapping("/posts/{id}") // Вывод страницы
    public ResponseEntity<Optional<Post>> show(@PathVariable String id) {
        var Post = posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        if (Post.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Post);
    }

    @PutMapping("/posts/{id}") // Обновление страницы
    public ResponseEntity<Post> update(@PathVariable String id, @RequestBody Post data) {
        var maybePost = posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (maybePost.isPresent()) {
            var Post = maybePost.get();
            Post.setId(data.getId());
            Post.setTitle(data.getTitle());
            Post.setBody(data.getBody());
        } else {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/posts/{id}") // Удаление страницы
    public void destroy(@PathVariable String id) {
        posts.removeIf(p -> p.getId().equals(id));
    }
}
