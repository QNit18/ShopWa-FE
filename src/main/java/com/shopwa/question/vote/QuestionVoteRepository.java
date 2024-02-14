package com.shopwa.question.vote;

import com.shopwa.entity.QuestionVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionVoteRepository extends JpaRepository<QuestionVote, Integer> {

	@Query("SELECT qv FROM QuestionVote qv WHERE qv.question.id = ?1 AND qv.customer.id = ?2")
	public QuestionVote findByQuestionAndCustomer(Integer questionId, Integer customerId);
	
	@Query("SELECT qv FROM QuestionVote qv WHERE qv.question.product.id = ?1 AND qv.customer.id = ?2")
	public List<QuestionVote> findByProductAndCustomer(Integer productId, Integer customerId);

}
