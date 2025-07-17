package com.example.prak.service;

import com.example.prak.repository.CityRepository;
import com.example.prak.repository.ProductRepository;
import com.example.prak.repository.model.Image;
import com.example.prak.repository.model.Product;
import com.example.prak.repository.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserService userService;


    public ProductService(ProductRepository productRepository, UserService userService) {
        this.productRepository = productRepository;
        this.userService = userService;
    }

    public void save(Product newProduct, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        Image image1;
        Image image2;
        Image image3;
        if(file1.getSize() != 0) {
            image1 = imageToEntity(file1);
            image1.setPreview(true);
            newProduct.addImageToProduct(image1);
        }
        if(file2.getSize() != 0) {
            image2 = imageToEntity(file2);
            newProduct.addImageToProduct(image2);
        }
        if(file3.getSize() != 0) {
            image3 = imageToEntity(file3);
            newProduct.addImageToProduct(image3);
        }
        Product productFromDb = productRepository.save(newProduct);
        productFromDb.setPreviewImageId(productFromDb.getImages().get(0).getId());
        productRepository.save(productFromDb);
    }

    private Image imageToEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setType(file.getContentType());
        image.setSize(file.getSize());
        image.setData(file.getBytes());
        return image;
    }

    public Iterable<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> searchProducts(String name, Long cityId, Long categoryId) {
        if ((name == null || name.isBlank())
                && cityId == null
                && categoryId == null) {
            return (List<Product>) productRepository.findAll();
        }
        return productRepository.searchProducts(name, cityId, categoryId);
    }

    public List<Product> getAllByName(String name) {
        if (name != null) return productRepository.findByNameContainingIgnoreCase(name);
        else return (List<Product>) getAll();
    }

    public List<Product> getAllByCity(Long cityId) {
        return productRepository.findAllByCity_Id(cityId);
    }

    public List<Product> getAllByCategory(Long categoryId) {
        return productRepository.findAllByCategory_Id(categoryId);
    }

    public void delete(Product product) {
        productRepository.delete(product);
    }

    public List<Product> getAllByAuthor(User user) {
        return productRepository.findAllByUserId(user.getId());
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}
