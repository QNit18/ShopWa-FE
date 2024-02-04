package com.shopwa.entity;

import com.shopwa.Constants;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends IdBasedEntity{
	
	@Column(length = 128, nullable = false, unique = true)
	private String email;
	
	@Column(length = 64, nullable = false)
	private String password;
	
	@Column(name = "first_name", length = 45, nullable = false)
	private String firstName;
	
	@Column(name = "last_name", length = 45, nullable = false)
	private String lastName;
	
	@Column(length = 64)
	private String photos;
	
	private boolean enabled;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
			)
	private Set<Role> roles = new HashSet<>();
	
	public User() {
	}
	
	public User(String email, String password, String firstName, String lastName) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public void addRole(Role role) {
		this.roles.add(role);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", roles=" + roles + "]";
	}
	
	@Transient
	public String getPhotosImagePath() {
		if (id == null || photos == null) return "/images/default-user.png";
		
		return Constants.S3_BASE_URI + "/user-photos/" + this.id + "/" + this.photos;
	}
	
	@Transient
	public String getFullName() {
		return firstName + " " + lastName;
	}

	public boolean hasRole(String roleName) {
		Iterator<Role> iterator = roles.iterator();
		while (iterator.hasNext()){
			Role role = iterator.next();
			if (role.getName().equals(roleName)){
				return true;
			}
		}
		return false;
	}
}
