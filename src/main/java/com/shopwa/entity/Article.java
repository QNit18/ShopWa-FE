package com.shopwa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "articles")
public class Article extends IdBasedEntity {
	
	@Column(nullable = false, length = 256)
	private String title;
	
	@Column(nullable = false)
	@Lob
	private String content;
	
	@Column(nullable = false, length = 500)
	private String alias;
	
	@Enumerated(EnumType.ORDINAL)
	private ArticleType type;
	
	@Column(name = "updated_time")
	private Date updatedTime;
	
	private boolean published;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Article() {
	}
	
	public Article(Integer id, String title, ArticleType type, Date updatedTime, boolean published, User user) {
		this.id = id;
		this.title = title;
		this.type = type;
		this.updatedTime = updatedTime;
		this.published = published;
		this.user = user;
	}
	
	public Article(Integer id, String title) {
		this.id = id;
		this.title = title;
	}	

	public Article(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Article [title=" + title + ", type=" + type + "]";
	}
	
	
}
