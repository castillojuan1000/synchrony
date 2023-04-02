package com.castillojuan.synchrony.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_hash")
    private String imageHash;

    @Column(name = "image_link")
    private String imageLink;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Image() {
    }

    public Image(String imageHash, String imageLink, User user) {
        this.imageHash = imageHash;
        this.imageLink = imageLink;
        this.user = user;
    }

    // Getters and setters for all fields

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageHash() {
        return imageHash;
    }

    public void setImageHash(String imageHash) {
        this.imageHash = imageHash;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

	@Override
	public String toString() {
		return "Image [id=" + id + ", imageHash=" + imageHash + ", imageLink=" + imageLink + ", user=" + user + "]";
	}

	
    
}
