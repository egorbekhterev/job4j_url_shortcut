package ru.job4j.urlshortcut.dto.url;

import lombok.Data;
import ru.job4j.urlshortcut.model.URL;

/**
 * Объект DTO для получения сокращенной ссылки {@link URL}.
 * @author: Egor Bekhterev
 * @date: 05.04.2023
 * @project: job4j_url_shortcut
 */
@Data
public class ShortUrlDTO {

    private String shortUrl;
}
