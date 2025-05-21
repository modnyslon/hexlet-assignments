package exercise.controller.users;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import exercise.model.Post;
import exercise.Data;

// BEGIN
@RestController
@RequestMapping("/api")
public class PostsController {

    @Setter
    private static List<Post> posts = Data.getPosts();

    @GetMapping("/users/{id}/posts")
    public ResponseEntity<List<Post>> show(@PathVariable String id) {
        List<Post> userPosts = posts.stream()
                .filter(p -> String.valueOf(p.getUserId()).equals(id))
                .collect(Collectors.toList());

        if (userPosts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userPosts);
    }

    @PostMapping("users/{id}/posts")
//    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Post> create(
            @PathVariable String id,
            @RequestBody Post data
    ) {
        Post newPost = new Post();
        newPost.setUserId(Integer.parseInt(id));
        newPost.setTitle(data.getTitle());
        newPost.setBody(data.getBody());
        newPost.setSlug(data.getSlug());
        posts.add(newPost);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newPost);
    }
}
// END
