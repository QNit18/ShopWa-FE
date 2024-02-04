package com.shopwa.product;

import com.shopwa.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.enabled = true AND " +
            "(p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%) " +
            "ORDER BY p.name ASC"
    )
    Page<Product> listByCategory(Integer categoryId, String categoryIDMatch, Pageable pageable);

    Product findByAlias(String alias);

    @Query(value = "SELECT * FROM products WHERE enabled = true AND " +
            "MATCH(name, short_description, full_description) AGAINST (?1)",
            nativeQuery = true)
    Page<Product> search(String keyword, Pageable pageable);

    @Query("UPDATE Product p set p.averageRating = coalesce((select AVG(r.rating) FROM Review r where r.product.id=?1),0)," +
            " p.reviewCount=(select COUNT(r.id) from Review r where r.product.id=?1) " +
            "where p.id=?1")
    @Modifying
    void updateReviewCountAndAverageRating(Integer productId);

    @Query("SELECT p FROM Product p WHERE p.enabled=true AND p.brand.id=?1")
    Page<Product> listByBrand(Integer brandId, Pageable pageable);
}
