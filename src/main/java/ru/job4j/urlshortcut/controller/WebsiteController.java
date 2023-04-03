package ru.job4j.urlshortcut.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.urlshortcut.model.Website;
import ru.job4j.urlshortcut.dto.WebsiteDTO;
import ru.job4j.urlshortcut.service.WebsiteService;
import ru.job4j.urlshortcut.validation.Operation;

/**
 * @author: Egor Bekhterev
 * @date: 03.04.2023
 * @project: job4j_url_shortcut
 */
@RestController
@AllArgsConstructor
public class WebsiteController {

    private final WebsiteService websiteService;

    @PostMapping("/registration")
    public ResponseEntity<WebsiteDTO> signUp(@Validated(Operation.OnSignUp.class) @RequestBody Website website) {
        var savedWebsite = websiteService.save(website);

        var websiteDTO = new WebsiteDTO();
        websiteDTO.setRegistration(savedWebsite != null);
        websiteDTO.setLogin(website.getLogin());
        websiteDTO.setPassword(website.getPassword());

        return new ResponseEntity<>(websiteDTO, HttpStatus.CREATED);
    }
}
