package com.shopwa.entity.section;

import com.shopwa.entity.Article;
import com.shopwa.entity.IdBasedEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "sections_articles")
public class ArticleSection extends IdBasedEntity {

	@Column(name = "article_order")
	private int articleOrder;

	@ManyToOne
	@JoinColumn(name = "article_id")
	private Article article;
}
