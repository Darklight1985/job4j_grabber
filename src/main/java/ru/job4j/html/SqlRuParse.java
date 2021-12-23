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

    public static void main(String[] args) throws Exception {
        for (int i = 1; i <= 5; i++) {
            Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers/" + i).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                System.out.println(href.attr("href"));
                System.out.println(href.text());
                System.out.println(td.parent().child(5).text());
            }
        }
    }

    @Override
    public List<Post> list(String link) {
        SqlRuParse sqlRuParse = new SqlRuParse(new SqlRuDateTimeParser());
        List<Post> list = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            try {
                Document doc = Jsoup.connect(link + "/" + i).get();
                Elements row = doc.select(".postslisttopic");
                for (Element td : row) {
                    Post post = sqlRuParse.detail(td.child(0).attr("href"));
                    if (!post.getTitle().equalsIgnoreCase("javascript")) {
                        list.add(post);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public Post detail(String link) throws IOException {
        /**
         * Ссылка на вакансию
         */
        String ssilka = null;
        /**
         * Дата обновления поста
         */
        String updDate;
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