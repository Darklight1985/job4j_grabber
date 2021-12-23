package ru.job4j.html;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.Post;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * В данном классе с помощью библиотеки jsoup парсируется первые пять страниц сайта
 * Находим ссылку на вакансию и дату публикования вакансаии
 */
public class SqlRuParse implements Parse {
    private final DateTimeParser dateTimeParser;

    public SqlRuParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String link) {
        List<Post> list = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            try {
                Document doc = Jsoup.connect(link + "/" + i).get();
                Elements row = doc.select(".postslisttopic");
                for (Element td : row) {
                    Post post = detail(td.child(0).attr("href"));
                    if (!post.getTitle().equalsIgnoreCase("javascript")
                            && post.getTitle().equalsIgnoreCase("java")) {
                        list.add(post);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * Метож создает объект Post для СУБД на основе данных из постов на вакансию
     * @param link - ссылка на вакансию
     * @return
     * @throws IOException
     */
    @Override
    public Post detail(String link) throws IOException {
        /**
         * Название поста
         */
          String publication;
        /**
         * Описание поста
         */
        String decription;
        /**
         * Дата создания поста
         */
          LocalDateTime crDate;

        Document document = Jsoup.connect(link).get();
        publication = document.select(".messageHeader").get(0).ownText();
        Elements rowPost = document.select(".msgBody");
        Element td = rowPost.get(1);
        decription = rowPost.get(1).text();
        Element data = td.parents().select(".msgFooter").first();
        crDate = this.dateTimeParser.parse(data.text().substring(0,
                data.text().indexOf("[")).trim());
        return new Post(publication, link, decription, crDate);
    }

}