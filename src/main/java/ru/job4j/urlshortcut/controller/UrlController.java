package ru.job4j.urlshortcut.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.urlshortcut.annotation.JsonArg;
import ru.job4j.urlshortcut.dto.url.ShortUrlDTO;
import ru.job4j.urlshortcut.model.URL;
import ru.job4j.urlshortcut.service.UrlService;

/**
 * @author: Egor Bekhterev
 * @date: 05.04.2023
 * @project: job4j_url_shortcut
 */
@RestController
@AllArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PostMapping("/convert")
    public ResponseEntity<ShortUrlDTO> convert(@JsonArg("longUrl") String longUrl) {
        var optionalURL = urlService.findByLongUrl(longUrl);
        var shortUrl = new ShortUrlDTO();

        if (optionalURL.isPresent()) {
            shortUrl.setShortUrl(optionalURL.get().getShortUrl());
            return new ResponseEntity<>(shortUrl, HttpStatus.OK);
        }

        var url = new URL();
        url.setLongUrl(longUrl);
        urlService.save(url);

        shortUrl.setShortUrl(url.getShortUrl());
        return new ResponseEntity<>(shortUrl, HttpStatus.OK);
    }
}
