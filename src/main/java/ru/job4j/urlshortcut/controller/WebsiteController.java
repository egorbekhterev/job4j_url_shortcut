package ru.job4j.urlshortcut.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.urlshortcut.dto.website.DomainNameDTO;
import ru.job4j.urlshortcut.model.Website;
import ru.job4j.urlshortcut.dto.website.WebsiteDTO;
import ru.job4j.urlshortcut.service.WebsiteService;

import javax.validation.Valid;

/**
 * @author: Egor Bekhterev
 * @date: 03.04.2023
 * @project: job4j_url_shortcut
 */
@RestController
@AllArgsConstructor
public class WebsiteController {

    private final WebsiteService websiteService;

    private static WebsiteDTO toDTO(Website website) {
        var websiteDTO = new WebsiteDTO();
        websiteDTO.setRegistration(website != null);
        websiteDTO.setLogin(website.getLogin());
        websiteDTO.setPassword(website.getPassword());
        return websiteDTO;
    }

    /**
     * Регистрирует новый веб-сайт с заданным доменным именем и возвращает сгенерированный логин и пароль для этого сайта.
     * @param domainName название домена нового веб-сайта.
     * @return Response с JSON-представлением {@link WebsiteDTO}
     */
    @PostMapping("/registration")
    public ResponseEntity<WebsiteDTO> signUp(@Valid @RequestBody DomainNameDTO domainName) {
        var website = new Website();
        website.setDomainName(domainName.getDomainName());

        var savedWebsite = websiteService.save(website);

        return new ResponseEntity<>(toDTO(savedWebsite), HttpStatus.CREATED);
    }
}
