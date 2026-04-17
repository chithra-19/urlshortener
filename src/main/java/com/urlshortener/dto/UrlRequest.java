package com.urlshortener.dto;

public class UrlRequest {

    private String originalUrl;
    private String customCode;
    private Integer expiryHours;

   

	public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getCustomCode() {
        return customCode;
    }

    public void setCustomCode(String customCode) {
        this.customCode = customCode;
    }
    
    public Integer getExpiryHours() {
		return expiryHours;
	}

	public void setExpiryHours(Integer expiryHours) {
		this.expiryHours = expiryHours;
	}
}