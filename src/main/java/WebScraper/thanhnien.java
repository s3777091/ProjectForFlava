package WebScraper;

import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Model.FeedItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.temporal.ChronoUnit;
import java.util.HashSet;

/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: 10/08/2021
  Author: team Flava
  Last modified date: 18/09/2021
  Author: members of team Flava
  Acknowledgement: in Document file
*/
public class thanhnien implements findDifference {

    public static void list_thanh_nien(String URL, HashSet<FeedItem> News) {
        try {
            InternetChecking.checkInternet(URL);
            Document doc = Jsoup.connect(URL).get();
            Elements TNTable = doc.select("article");
            for (Element header : TNTable) {
                FeedItem item = new FeedItem();
                String title = header.select("a").attr("title");
                String link = header.select("a").attr("href");
                if (!link.contains("https")) {
                    link = "https://thanhnien.vn"
                            + link;
                }
                String thumbnail = header.select("img").attr("data-src");
                String pubDate = header.select("time.timebox").text();
                String name = "ThanhNien";
                item.setTitle(title);
                item.setThumbnail(thumbnail);
                if (pubDate != "") {
                    item.setTime(convertToLocalDateTime(pubDate));
                }
                if (pubDate != "") {
                    pubDate = findDifference(convertToLocalDateTime(pubDate));
                }
                item.setPubDate(pubDate);
                item.setLink(link);
                item.setName(name);
                News.add(item);
//                try{
//                    Document docN = Jsoup.connect(item.getLink()).get();
//                    String description = docN.select("div.pswp-content div").text();
//                    item.setDescription(description);
//                    News.add(item);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String findDifference(LocalDateTime fromDateTime) {
        LocalDateTime toDateTime
                = LocalDateTime.now();

        LocalDateTime tempDateTime = LocalDateTime.from(fromDateTime);

        long years = tempDateTime.until(toDateTime, ChronoUnit.YEARS);
        tempDateTime = tempDateTime.plusYears(years);

        long months = tempDateTime.until(toDateTime, ChronoUnit.MONTHS);
        tempDateTime = tempDateTime.plusMonths(months);

        long days = tempDateTime.until(toDateTime, ChronoUnit.DAYS);
        tempDateTime = tempDateTime.plusDays(days);


        long hours = tempDateTime.until(toDateTime, ChronoUnit.HOURS);
        tempDateTime = tempDateTime.plusHours(hours);

        long minutes = tempDateTime.until(toDateTime, ChronoUnit.MINUTES);
        tempDateTime = tempDateTime.plusMinutes(minutes);

        long seconds = tempDateTime.until(toDateTime, ChronoUnit.SECONDS);


        String time = "";
        if (years > 0) {
            time += years;
            time += " years";
            time += " ago";

            return time;
        }
        if (months > 0) {
            time += months;
            time += " months";
            time += " ago";

            return time;
        }
        if (days > 0) {
            time += days;
            time += " days";
            time += " ago";

            return time;
        }
        if (hours > 0) {
            time += hours;
            time += " hours ";
            time += " ago";
            return time;
        }
        if (minutes > 0) {
            time += minutes;
            time += " minutes ";
            time += " ago";
            return time;
        } else {
            time += seconds;
            time += " seconds ";
            time += " ago";
            return time;
        }
    }

    //Convert to usable time
    public static LocalDateTime convertToLocalDateTime(String strToConvert) {
        DateTimeFormatter formatter;
        formatter = DateTimeFormatter.ofPattern("HH:mm, dd/MM/yyyy");
        LocalDateTime dateTime = LocalDateTime.parse(strToConvert, formatter);
        return dateTime;
    }

}
