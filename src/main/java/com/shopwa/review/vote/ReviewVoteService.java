package com.shopwa.review.vote;

import com.shopwa.entity.Customer;
import com.shopwa.entity.Review;
import com.shopwa.entity.ReviewVote;
import com.shopwa.review.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ReviewVoteService {

    @Autowired private ReviewVoteRepository reviewVoteRepo;
    @Autowired private ReviewRepository reviewRepo;

    public VoteResult undoVote(ReviewVote vote, Integer reviewId, VoteType voteType) {
        reviewVoteRepo.delete(vote);
        reviewRepo.updateVoteCount(reviewId);
        Integer voteCount = reviewRepo.getVoteCount(reviewId);

        return VoteResult.success("You have unvoted " + voteType + " that review.", voteCount );
    }

    public VoteResult doVote(Customer customer, Integer reviewId, VoteType voteType) {
        Review review = null;

        try {
            review = reviewRepo.findById(reviewId).get();
        } catch (NoSuchElementException ex){
            return VoteResult.fail("The Review Id " + reviewId + " no longer exists");
        }

        ReviewVote vote = reviewVoteRepo.findByReviewAndCustomer(reviewId, customer.getId());

        if (vote!=null) {
            if (vote.isUpVoted() && voteType.equals(VoteType.UP) ||
            vote.isDownVoted() && voteType.equals(VoteType.DOWN)){
                return undoVote(vote, reviewId, voteType);
            } else if (vote.isUpVoted() && voteType.equals(VoteType.DOWN)){
                vote.voteDown();
            } else if (vote.isDownVoted() && voteType.equals(VoteType.UP)){
                vote.voteUp();
            }
        }else {
            vote = new ReviewVote();
            vote.setCustomer(customer);
            vote.setReview(review);
            if (voteType.equals(VoteType.UP)){
                vote.voteUp();
            } else{
                vote.voteDown();
            }
        }

        reviewVoteRepo.save(vote);
        reviewRepo.updateVoteCount(reviewId);
        Integer voteCount = reviewRepo.getVoteCount(reviewId);

        return VoteResult.success("You have successfully voted " + voteType + " that reviews",voteCount );
    }


    public void markReviewsVotedForProductByCustomer(List<Review> listReviews, Integer productId,
                                                     Integer customerId) {
        List<ReviewVote> listVotes = reviewVoteRepo.findByProductAndCustomer(productId, customerId);

        for (ReviewVote vote : listVotes) {
            Review votedReview = vote.getReview();
            System.out.println(10);
            if (listReviews.contains(votedReview)) {
                int index = listReviews.indexOf(votedReview);
                Review review = listReviews.get(index);
                System.out.println(11);

                if (vote.isUpVoted()) {
                    review.setUpvotedByCurrentCustomer(true);
                } else if (vote.isDownVoted()) {
                    review.setDownvotedByCurrentCustomer(true);
                }
            }
        }
    }
}
