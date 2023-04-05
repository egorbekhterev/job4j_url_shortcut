package ru.job4j.urlshortcut.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.urlshortcut.annotation.JsonArg;
import ru.job4j.urlshortcut.dto.url.ShortUrlDTO;
import ru.job4j.urlshortcut.model.URL;
import ru.job4j.urlshortcut.service.UrlService;

import java.net.URI;

/**
 * @author: Egor Bekhterev
 * @date: 05.04.2023
 * @project: job4j_url_shortcut
 */
@RestController
@AllArgsConstructor
public class UrlController {

    private final UrlService urlService;

    /**
     * POST-метод для преобразования полного URL-адреса в сокращенный. Если полный URL существует в БД,
     * пользователь получит ответ с соответствующим сокращенным URL-адресом. Иначе будет сгенерирован shortURL
     * для этого полного URL-адреса.
     * @param longUrl длинный URL-адрес, который нужно сократить или найти в базе данных.
     * @return Response с JSON-представлением {@link ShortUrlDTO}.
     */
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


    /**
     * GET-метод для перенаправления по сокращенной ссылке (shortUrl).
     * @param shortUrl сокращенная ссылка для поиска соответствующего URL-адреса.
     * @return ответ с кодом перенаправления (302 Found) и заголовком ‘Location’,
     * если shortUrl находится в базе данных, иначе ошибку (400 Bad Request).
     * @throws ResponseStatusException при попытке обработки запроса с shortURL, который не найден в БД.
     */
    @GetMapping("/redirect/{shortUrl}")
    public ResponseEntity<Void> redirect(@PathVariable String shortUrl) {
        var url = urlService.findByShortUrl(shortUrl).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "This unique code is not associated with any URL.")
        );

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url.getLongUrl()))
                .build();
    }
}
