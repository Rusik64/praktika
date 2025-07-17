package com.example.prak.repository;

import com.example.prak.repository.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findAllByCity_Id(Long cityId);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findAllByCategory_Id(Long categoryId);

    @Query("""
    SELECT p
    FROM Product p
    WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:cityId IS NULL OR p.city.id = :cityId)
      AND (:categoryId IS NULL OR p.category.id = :categoryId)
""")
    List<Product> searchProducts(@Param("name") String name, @Param("cityId") Long cityId, @Param("categoryId") Long categoryId);

    List<Product> findAllByUserId(Long id);
}
