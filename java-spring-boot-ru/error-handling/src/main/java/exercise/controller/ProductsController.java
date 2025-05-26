package exercise.controller;

import exercise.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

import exercise.model.Product;
import exercise.repository.ProductRepository;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping(path = "")
    public List<Product> index() {
        return productRepository.findAll();
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // BEGIN
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product indexById(@PathVariable String id) {
        var result = productRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found."));
        return result;
    }

    @PutMapping("/{id}") // Обновление страницы
    public Product update(@PathVariable String id, @RequestBody Product data) {
        var existingProduct = productRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found."));

        existingProduct.setPrice(data.getPrice());
        existingProduct.setTitle(data.getTitle());
        productRepository.save(data);

        return data;
    }
    // END

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable long id) {
        productRepository.deleteById(id);
    }
}
