package ru.job4j.urlshortcut.dto.website;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * DTO для POST-метода /registration.
 * Состоит из названия домена. Выполняется валидация на соответствие паттерну имени домена.
 * @author: Egor Bekhterev
 * @date: 05.04.2023
 * @project: job4j_url_shortcut
 */
@Data
public class DomainNameDTO {

    @NotNull
    @Pattern(regexp = "^([a-z0-9]+(-[a-z0-9]+)*\\.)+[a-z]{2,}$")
    private String domainName;
}
