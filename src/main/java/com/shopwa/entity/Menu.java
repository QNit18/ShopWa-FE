package com.shopwa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "menus")
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.ORDINAL)
	private MenuType type;

	@Column(nullable = false, length = 128, unique = true)
	private String title;
	
	@Column(nullable = false, length = 256, unique = true)
	private String alias;
	
	private int position;
	
	private boolean enabled;
	
	@ManyToOne
	@JoinColumn(name = "article_id")
	private Article article;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Menu other = (Menu) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Menu [id=" + id + ", type=" + type + ", title=" + title + ", position=" + position + "]";
	}
}
