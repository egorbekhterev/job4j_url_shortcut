package ru.job4j.urlshortcut.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.job4j.urlshortcut.validation.Operation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author: Egor Bekhterev
 * @date: 03.04.2023
 * @project: job4j_url_shortcut
 */
@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Website {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @NotNull(message = "Domain name must be not null.", groups = {Operation.OnSignUp.class})
    @Column(name = "domain_name")
    private String domainName;

    private String login;

    private String password;
}
