package com.castillojuan.synchrony.entity;


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

    @Column(name = "user_id")
    private Long userId;

    public Image() {
    }

    public Image(String imageHash, String imageLink, Long userId) {
        this.imageHash = imageHash;
        this.imageLink = imageLink;
        this.userId = userId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

	@Override
	public String toString() {
		return "Image [id=" + id + ", imageHash=" + imageHash + ", imageLink=" + imageLink + ", userId=" + userId + "]";
	}
    
}
