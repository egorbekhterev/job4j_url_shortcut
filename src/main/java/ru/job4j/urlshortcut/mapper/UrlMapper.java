package ru.job4j.urlshortcut.mapper;

import ru.job4j.urlshortcut.dto.url.UrlStatDTO;
import ru.job4j.urlshortcut.model.URL;

/**
 * @author: Egor Bekhterev
 * @date: 07.04.2023
 * @project: job4j_url_shortcut
 */
public class UrlMapper {

    public static UrlStatDTO toDTO(URL url) {
        var dto = new UrlStatDTO();
        dto.setUrl(url.getLongUrl());
        dto.setTotal(url.getCount());
        return dto;
    }
}
