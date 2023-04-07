package ru.job4j.urlshortcut.mapper;

import ru.job4j.urlshortcut.dto.website.WebsiteDTO;
import ru.job4j.urlshortcut.model.Website;

/**
 * @author: Egor Bekhterev
 * @date: 07.04.2023
 * @project: job4j_url_shortcut
 */
public class WebsiteMapper {

    public static WebsiteDTO toDTO(Website website) {
        var websiteDTO = new WebsiteDTO();
        websiteDTO.setRegistration(website != null);
        websiteDTO.setLogin(website.getLogin());
        websiteDTO.setPassword(website.getPassword());
        return websiteDTO;
    }
}
