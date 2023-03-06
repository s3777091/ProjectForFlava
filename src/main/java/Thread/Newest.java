package Thread;

import Function.FeedComparator;
import Model.FeedItem;
import WebScraper.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.apache.commons.lang3.time.StopWatch;

import java.util.*;
import java.util.concurrent.*;
/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: 10/09/2021
  Author: team Flava
  Last modified date: 15/09/2021
  Author: members of team Flava
  Acknowledgement: in Document file
*/
public class Newest extends Task<Void> {
    private static ExecutorService es;
    private static ArrayList<FeedItem> listItem = new ArrayList<>();
    private static Newest newest = null;
    public Newest() {
    }

    public static Newest getInstance() {
        if (newest == null)
            newest = new Newest();
        return newest;
    }

    public ArrayList<FeedItem> getListItem() {
        return listItem;
    }
    public static void threadScheduale() {
        try {
            System.gc();
            System.runFinalization();
            int corepool = 10;
            int maxpool = 20;
            long keepAliveTime = 3000;
            ExecutorService es = new ThreadPoolExecutor(corepool,
                    maxpool,
                    keepAliveTime,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(150));

            StopWatch watch = new StopWatch();
            watch.start();
            HashSet<FeedItem> ls = new HashSet<>();
            es.execute(() -> ScrapItemInFromThanhNien(ls));
            es.execute(() -> ScrapItemInFromZing(ls));
            es.execute(() -> ScrapItemInFromTuoiTre(ls));
            es.execute(() -> ScrapItemInFromVnExpress(ls));
            es.shutdown();
            es.awaitTermination(10, TimeUnit.SECONDS);
            watch.stop();
            System.out.println("Time to take all the news: " + watch.getTime(TimeUnit.MILLISECONDS) + "Milliseconds");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void start() {
        threadScheduale();
    }

    @Override
    protected Void call() {
        return null;
    }

    public static void SortItem(List<FeedItem> list) {
        FeedComparator comparator = new FeedComparator();
        list.sort(comparator);
        Collections.reverse(list);
    }


    public static void ScrapItemInFromTuoiTre(HashSet<FeedItem> ls) {
        TuoiTreRSS.TUOITRERSS("https://tuoitre.vn/rss/thoi-su.rss", ls);
        ls.removeIf(item -> item.getTitle().equals("") || item.getPubDate().equals("") || item.getThumbnail().equals(""));
        listItem = new ArrayList<>(ls);
        SortItem(listItem);
    }

    public static void ScrapItemInFromZing(HashSet<FeedItem> ls) {
        zing.list_zing_home("https://zingnews.vn/thoi-su.html", ls);
        ls.removeIf(item -> item.getTitle().equals("") || item.getPubDate().equals("") || item.getThumbnail().equals(""));
        listItem = new ArrayList<>(ls);
        SortItem(listItem);
    }

    public static void ScrapItemInFromThanhNien(HashSet<FeedItem> ls) {
        thanhnien.list_thanh_nien("https://thanhnien.vn/tin-24h.html", ls);
        ls.removeIf(item -> item.getTitle().equals("") || item.getPubDate().equals("") || item.getThumbnail().equals(""));
        listItem = new ArrayList<>(ls);
        SortItem(listItem);
    }

    public static void ScrapItemInFromVnExpress(HashSet<FeedItem> ls) {
        VnNewest.list_VnExpress("https://vnexpress.net/tin-tuc-24h",ls);
        ls.removeIf(item -> item.getTitle().equals("") || item.getPubDate().equals("") || item.getThumbnail().equals(""));
        listItem = new ArrayList<>(ls);
        SortItem(listItem);
    }
}