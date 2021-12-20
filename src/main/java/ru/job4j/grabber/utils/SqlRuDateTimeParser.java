package ru.job4j.grabber.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {

    private static final Map<String, Integer> MONTHS = Map.ofEntries(
            Map.entry("янв", 1),
            Map.entry("фев", 2),
            Map.entry("мар", 3),
            Map.entry("апр", 4),
            Map.entry("май", 5),
            Map.entry("июн", 6),
            Map.entry("июл", 7),
            Map.entry("авг", 8),
            Map.entry("сен", 9),
            Map.entry("окт", 10),
            Map.entry("ноя", 11),
            Map.entry("дек", 12)
            );

    @Override
    public LocalDateTime parse(String parse) {
        LocalDate date;
        LocalTime time;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yy/M/dd");
        String[] data = parse.split(",");
        if ("сегодня".contains(data[0])) {
            date = LocalDate.now();
        } else {
            if ("вчера".contains(data[0])) {
                date = LocalDate.now().minusDays(1);
            } else {
                for (Map.Entry<String, Integer> map: MONTHS.entrySet()) {
                    data[0] = data[0].replaceFirst(map.getKey(), String.valueOf(map.getValue()));
                }
                String[] localDate = data[0].split(" ");
                date = LocalDate.of(
                        Integer.parseInt(localDate[2]),
                        Integer.parseInt(localDate[1]),
                        Integer.parseInt(localDate[0]));
            }
        }
        time = LocalTime.of(Integer.parseInt(data[1].split(":")[0].trim()),
                Integer.parseInt(data[1].split(":")[1]));
         LocalDateTime rsl = LocalDateTime.of(date, time);
        return rsl;
    }

    public static void main(String[] args) {
        SqlRuDateTimeParser sqlRuDateTimeParser = new SqlRuDateTimeParser();
        System.out.println(sqlRuDateTimeParser.parse("2 дек 19, 22:29"));
    }
}