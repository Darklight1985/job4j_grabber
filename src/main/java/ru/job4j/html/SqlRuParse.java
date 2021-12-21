package ru.job4j.html;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.utils.Post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * В данном классе с помощью библиотеки jsoup парсируется первые пять страниц сайта
 * Находим ссылку на вакансию и дату публикования вакансаии
 */
public class SqlRuParse implements Parse {
    public static void main(String[] args) throws Exception {
        for (int i = 1; i <= 5; i++) {
            Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers/" + i).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                System.out.println(href.attr("href"));
                postParse(href.attr("href"));
                System.out.println(href.text());
                System.out.println(td.parent().child(5).text());
            }
        }
    }

    /**
     * Метод парисрует по ссылке описание вакансии и дату ее опубликования
     *
     * @param postStr
     * @throws IOException
     */
    public static void postParse(String postStr) throws IOException {
        Document document = Jsoup.connect(postStr).get();
        Elements row = document.select(".msgBody");
        Element td = row.get(1);
        System.out.println(td.text());
        Element data = td.parents().select(".msgFooter").first();
        System.out.println(data.text().substring(0, data.text().indexOf("[")));
    }

    @Override
    public List<Post> list(String link) {
        SqlRuParse sqlRuParse = new SqlRuParse();
        List<Post> list = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            try {
                list.add(sqlRuParse.detail(link + "/" + i));
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
          String crDate;

        Document doc = Jsoup.connect(link).get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element href = td.child(0);
            ssilka = href.attr("href");
            publication = href.text();
            postParse(href.attr("href"));
            updDate = td.parent().child(5).text();
        }
        Document document = Jsoup.connect(ssilka).get();
        Elements rowPost = document.select(".msgBody");
        Element td = rowPost.get(1);
        decription = rowPost.get(1).text();
        Element data = td.parents().select(".msgFooter").first();
        crDate = data.text().substring(0, data.text().indexOf("["));
        return new Post();
    }
}