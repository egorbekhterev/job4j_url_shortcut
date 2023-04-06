package ru.job4j.urlshortcut.dto.url;

import lombok.Data;

/**
 * DTO для формирования статистики переходов по URL. Состоит из URL-адреса и кол-ва вызовов этого адреса.
 * @author: Egor Bekhterev
 * @date: 05.04.2023
 * @project: job4j_url_shortcut
 */
@Data
public class UrlStatDTO {

    private String url;

    private int total;
}
