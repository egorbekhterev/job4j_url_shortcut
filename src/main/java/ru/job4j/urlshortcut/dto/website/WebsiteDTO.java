package ru.job4j.urlshortcut.dto.website;

import lombok.Data;

/**
 * @author: Egor Bekhterev
 * @date: 03.04.2023
 * @project: job4j_url_shortcut
 */
@Data
public class WebsiteDTO {

    private boolean registration;

    private String login;

    private  String password;
}
