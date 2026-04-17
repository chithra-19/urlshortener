package com.urlshortener.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.urlshortener.model.Url;
import com.urlshortener.service.UrlService;

@RestController
public class RedirectController {

    @Autowired
    private UrlService urlService;

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {

        Optional<Url> urlOptional = urlService.getOriginalUrl(shortCode);

        if (urlOptional.isPresent()) {

            Url url = urlOptional.get();

            if(url.getExpiryDate() != null &&  url.getExpiryDate().isBefore(LocalDateTime.now())) {

                return ResponseEntity.status(410).build();
            }

            urlService.incrementClickCount(url);

            return ResponseEntity
                    .status(302)
                    .location(URI.create(url.getOriginalUrl()))
                    .build();
        }

        return ResponseEntity.notFound().build();
    }
}
