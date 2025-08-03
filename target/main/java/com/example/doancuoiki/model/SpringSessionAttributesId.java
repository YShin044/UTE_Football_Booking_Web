package com.example.doancuoiki.model;

import java.io.Serializable;

import jakarta.persistence.*;

public class SpringSessionAttributesId implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@Column(name = "SESSION_PRIMARY_ID", nullable = false)
    private String sessionPrimaryId;

    @Column(name = "ATTRIBUTE_NAME", nullable = false)
    private String attributeName;

    // Constructors
    public SpringSessionAttributesId() {
    }

    public SpringSessionAttributesId(String sessionPrimaryId, String attributeName) {
        this.sessionPrimaryId = sessionPrimaryId;
        this.attributeName = attributeName;
    }

    // Getters và Setters
    public String getSessionPrimaryId() {
        return sessionPrimaryId;
    }

    public void setSessionPrimaryId(String sessionPrimaryId) {
        this.sessionPrimaryId = sessionPrimaryId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    // Override equals và hashCode để đảm bảo lớp này hoạt động như một composite key
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpringSessionAttributesId that = (SpringSessionAttributesId) o;

        if (!sessionPrimaryId.equals(that.sessionPrimaryId)) return false;
        return attributeName.equals(that.attributeName);
    }

    @Override
    public int hashCode() {
        int result = sessionPrimaryId.hashCode();
        result = 31 * result + attributeName.hashCode();
        return result;
    }
}
