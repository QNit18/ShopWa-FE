package com.shopwa.review.vote;

import com.shopwa.ControllerHelper;
import com.shopwa.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class VoteReviewRestController {

    @Autowired private ReviewVoteService service;
    @Autowired private ControllerHelper helper;

    @PostMapping("/vote_review/{id}/{type}")
    public VoteResult voteReview(@PathVariable(name = "id")Integer reviewId,
                                 @PathVariable(name = "type") String type,
                                 HttpServletRequest request){

        Customer customer = helper.getAuthenticatedCustomer(request);

        if (customer == null) {
            return VoteResult.fail("You mus login to vote the review");
        }
        VoteType voteType = VoteType.valueOf(type.toUpperCase());

        return service.doVote(customer, reviewId, voteType);
    }
}
