package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.urlshortcut.dto.url.LongUrlDTO;
import ru.job4j.urlshortcut.dto.url.ShortUrlDTO;
import ru.job4j.urlshortcut.model.URL;
import ru.job4j.urlshortcut.repository.UrlRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

/**
 * @author: Egor Bekhterev
 * @date: 05.04.2023
 * @project: job4j_url_shortcut
 */
@Service
@AllArgsConstructor
public class UrlService {

    private UrlRepository urlRepository;

    private static final String HASH_ALGORITHM = "SHA-256";

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlService.class.getName());

    /**
     * Метод сокращает URL-адрес с помощью алгоритма хэширования SHA-256.
     * @param longUrl полный URL-адрес.
     * @param i число символов хэша, начиная с первого, которые будут переданы в БД.
     * @return сокращенный URL.
     * @throws NoSuchAlgorithmException если выбранный алгоритм хэширования не существует.
     */
    private static String shortenUrl(String longUrl, int i) throws NoSuchAlgorithmException {
        /* Создает объект реализующий выбранный алгоритм хеширования. */
        MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
        /* Производит хэширование с использованием выбранного алгоритма.*/
        var hash = md.digest(longUrl.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            /* Переводим байт в шестнадцатиричную систему счисления.*/
            sb.append(String.format("%02x", b & 0xff));
        }

        return sb.substring(0, i);
    }

    /**
     * Сохраняет указанный URL-адрес в базе данных. Если заданный shortURL уже существует, генерирует новый shortURL.
     * @param url объект {@link URL}.
     * @return сохраненный URL-адрес с сгенерированным shortURL.
     */
    public URL save(URL url) {
        var rsl = new URL();
        try {
            /* Записываем первые 6 символов хэша как shortURL.*/
            var i = 6;
            var shortUrl = shortenUrl(url.getLongUrl(), i);

            /* Решение коллизий. Если такой shortURL существует, то создаем shortURL из 12 символов. */
            while (findByShortUrl(url.getShortUrl()).isPresent()) {
                i += i;
                shortUrl = shortenUrl(url.getLongUrl(), i);
            }

            url.setShortUrl(shortUrl);
            rsl = urlRepository.save(url);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Error in the shortenUrl(String longUrl) method.", e);
        }
        return rsl;
    }

    /**
     * Метод-сервис для преобразования полной URL-ссылки в сокращенную.
     * Проверяет, есть ли полная ссылка в базе, если есть - возвращает shortUrl из БД, иначе выполняет сохранение URL.
     * @param longUrl полная URL-ссылка {@link LongUrlDTO}.
     * @return сокращенная URL-ссылка {@link ShortUrlDTO}
     */
    public ShortUrlDTO convert(LongUrlDTO longUrl) {
        var optionalURL = this.findByLongUrl(longUrl.getLongUrl());
        var shortURL = new ShortUrlDTO();

        shortURL.setShortUrl(optionalURL.map(URL::getShortUrl).orElseGet(() -> {
            var url = new URL();
            url.setLongUrl(longUrl.getLongUrl());
            return this.save(url).getShortUrl();
        }));

        return shortURL;
    }

    public Optional<URL> findByLongUrl(String longUrl) {
        return urlRepository.findByLongUrl(longUrl);
    }

    public Optional<URL> findByShortUrl(String shortUrl) {
        return urlRepository.findByShortUrl(shortUrl);
    }

    @Transactional
    public void updateCounterPlusOne(int id) {
        urlRepository.updateCounterById(id);
    }

    public List<URL> findAll() {
        return urlRepository.findAll();
    }
}
