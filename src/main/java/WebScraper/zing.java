package WebScraper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;

import Model.FeedItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: 10/08/2021
  Author: team Flava
  Last modified date: 18/09/2021
  Author: members of team TVA
  Acknowledgement: in Document file
*/
public class zing implements findDifference {
    //Scrape header information
    public static void list_zing_home(String zingUrl, HashSet<FeedItem> ls) {
        HashSet<String> titleTest = new HashSet<>();
        try {
            InternetChecking.checkInternet(zingUrl);
            Document doc = Jsoup.connect(zingUrl).get();
            Elements zingTable = doc.select("article");
            for (Element header : zingTable) {
                LocalDateTime DateTime = LocalDateTime.now();
                String title = header.select("header p.article-title a").text();
                String pubDate = header.select("header p.article-meta span.article-publish span.friendly-time").text();
                if (pubDate.contains("gi")) {
                    DateTime = ago(pubDate);
                    pubDate = findDifference(DateTime);
                }
                if (pubDate.contains("ph")) {
                    DateTime = mago(pubDate);
                    pubDate = findDifference(DateTime);
                }
                if (pubDate.contains("/")) {
                    DateTime = convertToLocalDateTime(pubDate);
                    pubDate = findDifference(convertToLocalDateTime(pubDate));
                }
                String link = header.select("a").attr("href");
                String thumbnail = header.select("p.article-thumbnail a img").attr("data-src");

                if (titleTest.contains(title)) {
                    continue;
                }
                titleTest.add(title);

                ls.add(new FeedItem(title, null, pubDate, thumbnail, "https://zingnews.vn" + link, "null", "Zing News", DateTime));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //Convert to usable time
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

    public static LocalDateTime convertToLocalDateTime(String strToConvert) {
        DateTimeFormatter formatter;
        DateTimeFormatter formatter2;
        DateTimeFormatter formatter3;
        DateTimeFormatter formatter4;


        formatter = DateTimeFormatter.ofPattern("HH:mm dd/M/yyyy");
        formatter2 = DateTimeFormatter.ofPattern("HH:mm d/M/yyyy");
        formatter3 = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        formatter4 = DateTimeFormatter.ofPattern("HH:mm d/MM/yyyy");
        LocalDateTime dateTime = LocalDateTime.now();
        if (strToConvert.matches("(.*)\\d{2}/\\d{1}/\\d{4}(.*)")) {
            dateTime = LocalDateTime.parse(strToConvert, formatter);
        }
        if (strToConvert.matches("(.*)\\d{1}/\\d{1}/\\d{4}(.*)")) {
            dateTime = LocalDateTime.parse(strToConvert, formatter2);
        }
        if (strToConvert.matches("(.*)\\d{2}/\\d{2}/\\d{4}(.*)")) {
            dateTime = LocalDateTime.parse(strToConvert, formatter3);
        }
        if (strToConvert.matches("(.*)\\d{1}/\\d{2}/\\d{4}(.*)")) {
            dateTime = LocalDateTime.parse(strToConvert, formatter4);
        }

        return dateTime;
    }

    public static LocalDateTime ago(String str) {
        String clean = str.replaceAll("\\D+", "");
        LocalDateTime toDateTime
                = LocalDateTime.now();
        int i = Integer.parseInt(clean);
        toDateTime = toDateTime.minusHours(i);

        return toDateTime;
    }

    public static LocalDateTime mago(String str) {
        String clean = str.replaceAll("\\D+", "");
        LocalDateTime toDateTime
                = LocalDateTime.now();
        int i = Integer.parseInt(clean);
        toDateTime = toDateTime.minusMinutes(i);

        return toDateTime;
    }

}
