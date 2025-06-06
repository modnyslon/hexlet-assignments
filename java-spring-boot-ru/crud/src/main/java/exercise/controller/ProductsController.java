package exercise.controller;

import java.util.List;

import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.mapper.ProductMapper;
import exercise.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryRepository categoryRepository;
    // BEGIN
    // GET /products – список всех товаров
    @GetMapping("")
    public List<ProductDTO> index() {
        var products = productRepository.findAll();
        return products.stream()
                .map(productMapper::map)
                .toList();
    }

    // GET /products/{id} – просмотр конкретного товара
    @GetMapping("/{id}")
    public ProductDTO show(@PathVariable Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
        return productMapper.map(product);
    }

    // POST /products – создание нового товара
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@Valid @RequestBody ProductCreateDTO productData) {
        // Проверяем, существует ли категория
        if (productData.getCategoryId() != null && !categoryRepository.existsById(productData.getCategoryId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category with id " + productData.getCategoryId() + " does not exist");
        }

        var product = productMapper.map(productData);
        productRepository.save(product);
        return productMapper.map(product);
    }

    // PUT /products/{id} – обновление товара
    @PutMapping("/{id}")
    public ProductDTO update(@PathVariable Long id, @Valid @RequestBody ProductUpdateDTO productData) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        // Проверяем, существует ли новая категория (если она указана)
        if (productData.getCategoryId() != null && !categoryRepository.existsById(productData.getCategoryId().get())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category with id " + productData.getCategoryId() + " does not exist");
        }

        var updatedCategory = categoryRepository.findById(productData.getCategoryId().get())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Category with id " + productData.getCategoryId() + " does not exist"));

        productMapper.update(productData, product);
        product.setCategory(updatedCategory);
        productRepository.save(product);
        return productMapper.map(product);
    }

    // DELETE /products/{id} – удаление товара
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product with id " + id + " not found");
        }
        productRepository.deleteById(id);
    }
    // END
}
