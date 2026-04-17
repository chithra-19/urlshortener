package com.urlshortener.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.urlshortener.model.Url;
import com.urlshortener.repository.UrlRepository;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    // Create short URL
    public Url shortenUrl(String originalUrl, String customCode) {

        // check if URL already exists
        Optional<Url> existing = urlRepository.findByOriginalUrl(originalUrl);

        if(existing.isPresent() && (customCode == null || customCode.isEmpty())) {
            return existing.get();
        }

        String shortCode;

        if(customCode != null && !customCode.isEmpty()) {

            // check if custom code already exists
            Optional<Url> codeExists = urlRepository.findByShortCode(customCode);

            if(codeExists.isPresent()) {
                throw new RuntimeException("Custom short code already taken");
            }

            shortCode = customCode;

        } else {
            shortCode = generateShortCode();
        }

        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortCode(shortCode);
        url.setClickCount(0);

        return urlRepository.save(url);
    }

    // Get original URL from short code
    public Optional<Url> getOriginalUrl(String shortCode) {
        return urlRepository.findByShortCode(shortCode);
    }

    // Increment click count
    public void incrementClickCount(Url url) {
        url.setClickCount(url.getClickCount() + 1);
        urlRepository.save(url);
    }

    // Generate short code
    private String generateShortCode() {

        String shortCode;

        do {
            shortCode = UUID.randomUUID().toString().substring(0,6);
        }
        while(urlRepository.findByShortCode(shortCode).isPresent());

        return shortCode;
    }
  
    public List<Url> getTopUrls() {
        return urlRepository.findTop10ByOrderByClickCountDesc();
    }
}