package ru.job4j.urlshortcut.dto.website;

import lombok.Data;
import ru.job4j.urlshortcut.model.Website;

/**
 * Объект DTO для сущности {@link Website}, содержащий сгенерированный логин, пароль и статус выполнения регистрации.
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
