package ru.job4j.urlshortcut.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Модель данных для URL, включающая идентификатор, полный URL и сокращенный URL.
 * @author: Egor Bekhterev
 * @date: 05.04.2023
 * @project: job4j_url_shortcut
 */
@Entity
@Data
public class URL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "long_url")
    private String longUrl;

    @Column(name = "short_url")
    private String shortUrl;
}
