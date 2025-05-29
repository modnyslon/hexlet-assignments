package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import exercise.model.Contact;
import exercise.repository.ContactRepository;
import exercise.dto.ContactDTO;
import exercise.dto.ContactCreateDTO;

@RestController
@RequestMapping("/contacts")
public class ContactsController {

    @Autowired
    private ContactRepository contactRepository;

    // BEGIN
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactDTO create(@RequestBody ContactCreateDTO postData) {
        var post = toEntity(postData); // Сначала в Entity
        contactRepository.save(post);
        var postDTO = toDTO(post); // Потом в DTO
        return postDTO;
    }

    private ContactDTO toDTO(Contact post) {
        var dto = new ContactDTO();
        dto.setId(post.getId());
        dto.setFirstName(post.getFirstName());
        dto.setLastName(post.getLastName());
        dto.setPhone(post.getPhone());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        return dto;
    }

    private Contact toEntity(ContactCreateDTO postDto) {
        var post = new Contact();
        post.setFirstName(postDto.getFirstName());
        post.setLastName(postDto.getLastName());
        post.setPhone(postDto.getPhone());
        return post;
    }
    // END
}
