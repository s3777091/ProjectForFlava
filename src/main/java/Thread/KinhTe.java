package Thread;

import Function.FeedComparator;
import Model.FeedItem;
import WebScraper.*;
import javafx.concurrent.Task;
import org.apache.commons.lang3.time.StopWatch;

import java.util.*;
import java.util.concurrent.*;
/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: 10/08/2021
  Author: team Flava
  Last modified date: 15/09/2021
  Author: members of team Flava
  Acknowledgement: in Document file
*/
public class KinhTe extends Task<Void> {
    public static String zingurl = "https://zingnews.vn/";
    public static String thanhnienurl = "https://thanhnien.vn/";
    public static String tuoitreurl = "https://tuoitre.vn/rss/";
    public static String vnurl =  "https://vnexpress.net/";
    private static ArrayList<FeedItem> listItem = new ArrayList<>();
    private static KinhTe kt = null;

    public KinhTe() {
    }

    public static KinhTe getInstance() {
        if (kt == null)
            kt = new KinhTe();
        return kt;
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

    public static void ScrapItemInFromVnExpress(HashSet<FeedItem> ls) {
        VnExpress.list_VnExpress("https://vnexpress.net/kinh-doanh",ls);
        ls.removeIf(item -> item.getTitle().equals("") || item.getPubDate().equals("") || item.getThumbnail().equals(""));
        listItem = new ArrayList<>(ls);
        SortItem(listItem);
    }

    public static void ScrapItemInFromTuoiTre(HashSet<FeedItem> ls) {
        TuoiTreRSS.TUOITRERSS(tuoitreurl.concat("kinh-doanh.rss"),ls);
        ls.removeIf(item -> item.getTitle().equals("") || item.getPubDate().equals("") || item.getThumbnail().equals(""));
        listItem = new ArrayList<>(ls);
        SortItem(listItem);
    }

    public static void ScrapItemInFromZing(HashSet<FeedItem> ls) {
        zing.list_zing_home(zingurl.concat("kinh-doanh-tai-chinh.html"), ls);
        ls.removeIf(item -> item.getTitle().equals("") || item.getPubDate().equals("") || item.getThumbnail().equals(""));
        listItem = new ArrayList<>(ls);
        SortItem(listItem);
    }

    public static void ScrapItemInFromThanhNien(HashSet<FeedItem> ls) {
        thanhnien.list_thanh_nien(thanhnienurl.concat("kinh-te/"), ls);
        ls.removeIf(item -> item.getTitle().equals("") || item.getPubDate().equals("") || item.getThumbnail().equals(""));
        listItem = new ArrayList<>(ls);
        SortItem(listItem);
    }


}

