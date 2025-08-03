package com.example.doancuoiki.model;

import java.io.Serializable;

import jakarta.persistence.*;



@Entity
@Table(name = "spring_session_attributes")
public class SpringSessionAttributesModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@EmbeddedId
    private SpringSessionAttributesId id;

    @Lob
    @Column(name = "ATTRIBUTE_BYTES", nullable = false)
    private byte[] attributeBytes;

    // Getters và Setters
    public SpringSessionAttributesId getId() {
        return id;
    }

    public void setId(SpringSessionAttributesId id) {
        this.id = id;
    }

    public byte[] getAttributeBytes() {
        return attributeBytes;
    }

    public void setAttributeBytes(byte[] attributeBytes) {
        this.attributeBytes = attributeBytes;
    }
	

}
