package ru.job4j.html;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * В данном классе с помощью библиотеки jsoup парсируется первые пять страниц сайта
 * Находим ссылку на вакансию и дату публикования вакансаии
 */
public class SqlRuParse {
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
}