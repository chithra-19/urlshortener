package com.urlshortener.dto;

public class UrlResponse {

    private String shortUrl;
    private String originalUrl;

    public UrlResponse(String shortUrl, String originalUrl) {
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }
}