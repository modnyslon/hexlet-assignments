package exercise.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;

import java.util.List;

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import exercise.exception.ResourceNotFoundException;

// BEGIN
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentsController {

    @Autowired
    private final CommentRepository commentRepository;

    // Получить все комментарии
    @GetMapping
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    // Получить комментарий по ID
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable long id) {
        return commentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found"));
    }

    // Создать новый комментарий
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        Comment savedComment = commentRepository.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    // Обновить комментарий
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable long id,
            @RequestBody Comment commentDetails) {

        return commentRepository.findById(id)
                .map(comment -> {
                    comment.setBody(commentDetails.getBody());
                    comment.setPostId(commentDetails.getPostId());
                    return ResponseEntity.ok(commentRepository.save(comment));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Удалить комментарий
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        commentRepository.deleteById(id);
    }
}
// END
