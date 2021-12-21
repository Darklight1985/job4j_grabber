package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Модель данных
 */
public class Post {
    /**
     * поле id получаемое из БД
     */
    private int id;
    /**
     * Название вакансии
     */
    private String title;
    /**
     * Ссылка на описание вакансии
     */
    private String link;
    /**
     * Описание вакансии
     */
    private String description;
    /**
     * Дата создания ваканссии
     */
    private LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(title, post.title)
                && Objects.equals(link, post.link)
                && Objects.equals(description, post.description)
                && Objects.equals(created, post.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, link, description, created);
    }
}
