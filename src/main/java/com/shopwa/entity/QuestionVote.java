package com.shopwa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "questions_votes")
public class QuestionVote {
	public static final int VOTE_UP_POINT = 1;
	public static final int VOTE_DOWN_POINT = -1;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "question_id")
	private Question question;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	private int votes;

	public void voteUp() {
		this.votes = VOTE_UP_POINT;
	}

	public void voteDown() {
		this.votes = VOTE_DOWN_POINT;
	}

	@Transient
	public boolean isUpvoted() {
		return this.votes == VOTE_UP_POINT;
	}
	
	@Transient
	public boolean isDownvoted() {
		return this.votes == VOTE_DOWN_POINT;
	}	

}
