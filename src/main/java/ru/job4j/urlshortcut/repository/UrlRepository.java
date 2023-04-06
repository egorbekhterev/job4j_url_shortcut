package ru.job4j.urlshortcut.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.urlshortcut.model.URL;

import java.util.List;
import java.util.Optional;

/**
 * @author: Egor Bekhterev
 * @date: 05.04.2023
 * @project: job4j_url_shortcut
 */
public interface UrlRepository extends CrudRepository<URL, Integer> {

    Optional<URL> findByLongUrl(String longUrl);

    Optional<URL> findByShortUrl(String shortUrl);

    @Modifying
    @Query("UPDATE URL u set u.count = u.count + 1 WHERE u.id = :id")
    void updateCounterById(int id);

    List<URL> findAll();
}
