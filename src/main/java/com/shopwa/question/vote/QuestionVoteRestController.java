package com.shopwa.question.vote;

import com.shopwa.ControllerHelper;
import com.shopwa.entity.Customer;
import com.shopwa.vote.VoteResult;
import com.shopwa.vote.VoteType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class QuestionVoteRestController {

	@Autowired private ControllerHelper controllerHelper;
	
	@Autowired private QuestionVoteService service;
	
	@PostMapping("/vote_question/{id}/{type}")
	public VoteResult voteQuestion(@PathVariable(name = "id") Integer questionId,
			@PathVariable(name = "type") String type, HttpServletRequest request) {
		
		Customer customer = controllerHelper.getAuthenticatedCustomer(request);
		if (customer == null) {
			return VoteResult.fail("You must login to vote the question.");
		}
		
		VoteType voteType = VoteType.valueOf(type.toUpperCase());
		
		return service.doVote(questionId, customer, voteType);		
	}
	
}
