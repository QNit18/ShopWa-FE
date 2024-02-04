package com.shopwa.review.vote;

import com.shopwa.entity.ReviewVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewVoteRepository extends JpaRepository<ReviewVote, Integer> {

    @Query("SELECT v FROM ReviewVote v WHERE v.review.id = ?1 AND v.customer.id = ?2")
    ReviewVote findByReviewAndCustomer(Integer reviewId, Integer customerId);

    @Query("SELECT v FROM ReviewVote v WHERE v.review.product.id = ?1 AND v.customer.id = ?2")
    List<ReviewVote> findByProductAndCustomer(Integer productId, Integer customerId);
}
