package WebScraper;

import Model.FeedItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: 10/09/2021
  Author: team Flava
  Last modified date: 18/09/2021
  Author: members of team Flava
  Acknowledgement: in Document file
*/
public class VnNewest implements findDifference {
    //Scrape header information
    public static void list_VnExpress(String URL, HashSet<FeedItem> News) {
        ArrayList<FeedItem> newList = new ArrayList<>();
        try {
            InternetChecking.checkInternet(URL);
            Document doc = Jsoup.connect(URL).get();
            Elements TNTable = doc.select("article");
            ArrayList<FeedItem> noPubDate = new ArrayList<>();
            for (Element header : TNTable) {
                FeedItem item = new FeedItem();
                String title = header.select("a").attr("title");
                if (title.length() == 0) {
                    continue;
                }
                String link = header.select("a").attr("href");
                String thumbnail = header.select("source").attr("data-srcset");
                String thumbnailAlt = header.select("source").attr("srcset");
                String pubDate = header.select("span").attr("datetime");
                String[] split = thumbnail.split("1x,");
                String[] splitAlt = thumbnailAlt.split("1x,");
                String name = "VnExpress";
                if (split.length < 2) {
                    if (splitAlt.length < 2) {
                        item.setThumbnail("");
                    } else {
                        //System.out.println(splitAlt[1].substring(1, splitAlt[1].length() - 2));
                        item.setThumbnail(splitAlt[1].substring(1, splitAlt[1].length() - 2));
                    }
                } else {
                    item.setThumbnail(split[1].substring(1, split[1].length() - 2));
                }
                item.setLink(link);
                item.setName(name);
                if (!pubDate.isBlank()) {
                    item.setPubDate(findDifference(convertToLocalDateTime(pubDate)));
                    item.setTime(convertToLocalDateTime(pubDate));
                } else {

                    noPubDate.add(item);
                }

                item.setTitle(title);
                newList.add(item);
            }

            if (!(noPubDate.size() < 1)) {
                int cores = Runtime.getRuntime().availableProcessors();
                Runnable[] t = new Runnable[cores];
                for (int runner = 0, min = 0, extraTracker = 1; runner < cores; runner++) {
                    t[runner] = dateGetter(noPubDate, min, min + (noPubDate.size() / cores) + (((noPubDate.size() % cores - extraTracker) > 0) ? 1 : 0));
                    min = (min + noPubDate.size() / cores + (((noPubDate.size() % cores - extraTracker) > 0) ? 1 : 0));
                    extraTracker++;
                }
                ExecutorService es = Executors.newCachedThreadPool();
                for (int i = 0; i < cores; i++) {
                    es.execute(t[i]);
                }
                es.shutdown();
                boolean finished = es.awaitTermination(1, TimeUnit.MINUTES);
            }


            News.addAll(newList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Convert to usable time
    public static Runnable dateGetter(ArrayList<FeedItem> elementsList, int elementStartPerThread, int elementMaxPerThread) {
        Runnable forExecute = () -> {
            FeedItem item;
            for (int runner = elementStartPerThread; runner <= elementMaxPerThread; runner++) {
                try {
                    if (elementsList.size() < 1) {
                        System.out.println("elementalList line 107 VnNewest:" + elementsList.size());
                        break;
                    }

                    item = elementsList.get(runner);
                    String link = elementsList.get(runner).getLink();
                    if (link == null) {
                        continue;
                    }
                    Document dateGet = Jsoup.connect(link).get();
                    String Date = dateGet.select("span.date").text();
                    if (Date.contains("GMT")) {
                        Date = Date.substring(Date.indexOf(",") + 2, Date.length() - 8);
                        if (Date != "") {
                            item.setTime(convertToLocalDateTime(Date));
                        }
                        if (Date != "") {
                            Date = findDifference(convertToLocalDateTime(Date));
                        }
                    }
                    if (!Date.contains("ago")) {
                        System.out.println(dateGet.select("span.date").text());
                    }
                    item.setPubDate(Date);

                    elementsList.set(runner, item);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("link not working line 88 VnExpress Scraper");
                }
            }
        };
        return forExecute;
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

    public static LocalDateTime convertToLocalDateTime(String strToConvert) {

        DateTimeFormatter formatter;
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            return LocalDateTime.parse(strToConvert, formatter);
        } catch (Exception e) {
            try {
                DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("d/M/yyyy, HH:mm");
                return LocalDateTime.parse(strToConvert, formatter2);
            } catch (Exception e1) {
                System.out.println("Parsing error in VnNewest: " + strToConvert);
                return LocalDateTime.parse("0001-01-01 00:00:00", formatter);
            }
        }
    }
}
