package ru.job4j.urlshortcut.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.urlshortcut.model.Website;

import java.util.Optional;

/**
 * @author: Egor Bekhterev
 * @date: 03.04.2023
 * @project: job4j_url_shortcut
 */
public interface WebsiteRepository extends CrudRepository<Website, Integer> {

    Optional<Website> findByLogin(String login);
}
