package ru.job4j.urlshortcut.dto.url;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * DTO для GET-метода /convert.
 * Состоит из полного URL-адреса, выполняется валидация на соответствие паттерну для веб-сайтов.
 * @author: Egor Bekhterev
 * @date: 05.04.2023
 * @project: job4j_url_shortcut
 */
@Data
public class LongUrlDTO {

    @NotNull
    @Pattern(regexp = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")
    private String longUrl;
}
