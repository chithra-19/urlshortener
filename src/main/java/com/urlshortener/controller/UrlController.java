package com.urlshortener.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.urlshortener.dto.UrlRequest;
import com.urlshortener.dto.UrlResponse;
import com.urlshortener.model.Url;
import com.urlshortener.service.UrlService;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    @Autowired
    private UrlService urlService;

    

    // Get stats
    @GetMapping("/stats/{shortCode}")
    public ResponseEntity<Url> getStats(@PathVariable String shortCode) {

        Optional<Url> urlOptional = urlService.getOriginalUrl(shortCode);

        return urlOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/top")
    public ResponseEntity<List<Url>> getTopUrls(){

        List<Url> urls = urlService.getTopUrls();

        return ResponseEntity.ok(urls);
    }
    @PostMapping("/shorten")
    public ResponseEntity<UrlResponse> shortenUrl(@RequestBody UrlRequest request) {

        if (request.getOriginalUrl() == null) {
            return ResponseEntity.badRequest().build();
        }

        String originalUrl = request.getOriginalUrl().trim();
        String customCode = request.getCustomCode();

        if (originalUrl.isEmpty() ||
            !(originalUrl.startsWith("http://") || originalUrl.startsWith("https://"))) {
            return ResponseEntity.badRequest().build();
        }

        Url url = urlService.shortenUrl(originalUrl, customCode);

        String baseUrl = System.getenv("BASE_URL");

        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = "http://localhost:8080";
        }

        String shortUrl = baseUrl + "/" + url.getShortCode();

        return ResponseEntity.ok(new UrlResponse(shortUrl, originalUrl));
    }
    
    @GetMapping("/{shortCode}")
    public ResponseEntity<?> redirect(@PathVariable String shortCode) {

        Optional<Url> urlOptional = urlService.getOriginalUrl(shortCode);

        if (urlOptional.isPresent()) {
            return ResponseEntity
                    .status(302)
                    .location(URI.create(urlOptional.get().getOriginalUrl()))
                    .build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}