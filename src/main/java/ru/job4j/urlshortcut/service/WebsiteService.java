package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.urlshortcut.model.Website;
import ru.job4j.urlshortcut.repository.WebsiteRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author: Egor Bekhterev
 * @date: 03.04.2023
 * @project: job4j_url_shortcut
 */
@Service
@AllArgsConstructor
public class WebsiteService {

    private WebsiteRepository websiteRepository;

    private BCryptPasswordEncoder encoder;

    private static String generateLogin() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private static String generatePassword() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    public Website save(Website website) {
        website.setLogin(generateLogin());
        var pass = generatePassword();
        website.setPassword(encoder.encode(pass));

        websiteRepository.save(website);

        website.setPassword(pass);
        return website;
    }

    public Optional<Website> findByLogin(String login) {
        return websiteRepository.findByLogin(login);
    }
}
