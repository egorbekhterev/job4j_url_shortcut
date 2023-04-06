package ru.job4j.urlshortcut.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.urlshortcut.dto.url.LongUrlDTO;
import ru.job4j.urlshortcut.dto.url.ShortUrlDTO;
import ru.job4j.urlshortcut.dto.url.UrlStatDTO;
import ru.job4j.urlshortcut.model.URL;
import ru.job4j.urlshortcut.service.UrlService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

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
    public ResponseEntity<ShortUrlDTO> convert(@Valid @RequestBody LongUrlDTO longUrl) {
        var optionalURL = urlService.findByLongUrl(longUrl.getLongUrl());
        var shortUrl = new ShortUrlDTO();

        if (optionalURL.isPresent()) {
            shortUrl.setShortUrl(optionalURL.get().getShortUrl());
            return new ResponseEntity<>(shortUrl, HttpStatus.OK);
        }

        var url = new URL();
        url.setLongUrl(longUrl.getLongUrl());
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

        urlService.updateCounterPlusOne(url.getId());
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url.getLongUrl()))
                .build();
    }

    private static UrlStatDTO toDTO(URL url) {
        var dto = new UrlStatDTO();
        dto.setUrl(url.getLongUrl());
        dto.setTotal(url.getCount());
        return dto;
    }

    /**
     * GET-метод для получения статистики переходов по URL-адресу.
     * @return список {@link UrlStatDTO}
     */
    @GetMapping("/statistic")
    public List<UrlStatDTO> getStatistics() {
        return urlService.findAll().stream().map(UrlController::toDTO).toList();
    }
}
