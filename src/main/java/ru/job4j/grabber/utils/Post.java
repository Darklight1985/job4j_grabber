package ru.job4j.grabber.utils;

import org.jsoup.nodes.Element;

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

    public Post(int id, String title, String link, String description, LocalDateTime created) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.description = description;
        this.created = created;
    }

    public Post(String title, String link, String description, LocalDateTime created) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

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