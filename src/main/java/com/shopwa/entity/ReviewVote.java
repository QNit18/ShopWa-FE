package com.shopwa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "review_votes")
public class ReviewVote extends IdBasedEntity{
    private static final int VOTE_UP_POINT = 1;
    private static final int VOTE_DOWN_POINT = -1;

    private int votes;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;


    public void voteUp() {
        this.votes = VOTE_UP_POINT;
    }

    public void voteDown() {
        this.votes = VOTE_DOWN_POINT;
    }

    @Override
    public String toString() {
        return "ReviewVote [" +
                "votes=" + votes +
                ", customer=" + customer.getFullName() +
                ", review=" + review +
                ']';
    }

    @Transient
    public boolean isUpVoted(){
        return this.votes == VOTE_UP_POINT;
    }
    @Transient
    public boolean isDownVoted(){
        return this.votes == VOTE_DOWN_POINT;
    }
}
