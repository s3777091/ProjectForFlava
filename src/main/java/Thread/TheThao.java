package Thread;

import Function.FeedComparator;
import Model.FeedItem;
import WebScraper.*;
import javafx.concurrent.Task;
import org.apache.commons.lang3.time.StopWatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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

//this one is multithread i separate it too several Category
public class TheThao extends Task<Void> {
    public static String zingurl = "https://zingnews.vn/";
    public static String thanhnienurl = "https://thanhnien.vn/";
    public static String tuoitreurl = "https://tuoitre.vn/rss/";
    public static String vnurl = "https://vnexpress.net/";
    //adding the url them
    private static ExecutorService es;

    private static ArrayList<FeedItem> listItem = new ArrayList<>();
    private static TheThao thehao = null;
    //call public variable

    public TheThao() {
    }

    public static TheThao getInstance() {
        if (thehao == null)
            thehao = new TheThao();
        return thehao;
    }


    public ArrayList<FeedItem> getListItem() {
        return listItem;
    }

    public static void threadScheduale() {
        try {
            int corepool = 10;
            int maxpool = 20;
            long keepAliveTime = 3000;
            // seting maxcore is 20 and corepool is 10
            ExecutorService es = new ThreadPoolExecutor(corepool,
                    maxpool,
                    keepAliveTime,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(150));
            //Using the ExecutorService to run thread pool with block is small is 50

            StopWatch watch = new StopWatch();
            watch.start();

            //function to caculate the time start the code below

            HashSet<FeedItem> ls = new HashSet<>();
            es.execute(() -> ScrapItemInFromThanhNien(ls));
            es.execute(() -> ScrapItemInFromZing(ls));
            es.execute(() -> ScrapItemInFromTuoiTre(ls));
            es.execute(() -> ScrapItemInFromVnExpress(ls));

            watch.stop();
            System.out.println("Time to take all the news: " + watch.getTime(TimeUnit.MILLISECONDS) + "Milliseconds");
            es.shutdown();
            es.awaitTermination(10, TimeUnit.SECONDS);
            System.out.println(es.isShutdown());
            //while it end give me time and push every thing in the ListItem that i call in begin
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
        //function sort item to make the newest category
    }



    //all this using the scaper by jsoup and push in hashset
    //i use hashset to remove duplication
    // and if some of the newspaper have null value it will remove
    //but i relize i need another filter to remove several newspaper have video but i don't have time
    public static void ScrapItemInFromTuoiTre(HashSet<FeedItem> ls) {
        TuoiTreRSS.TUOITRERSS(tuoitreurl.concat("the-thao.rss"), ls);
        ls.removeIf(item -> item.getTitle().equals("") || item.getPubDate().equals("") || item.getThumbnail().equals(""));
        listItem = new ArrayList<>(ls);
        SortItem(listItem);
    }

    public static void ScrapItemInFromZing(HashSet<FeedItem> ls) {
        zing.list_zing_home(zingurl.concat("the-thao.html"), ls);
        ls.removeIf(item -> item.getTitle().equals("") || item.getPubDate().equals("") || item.getThumbnail().equals(""));
        listItem = new ArrayList<>(ls);
        SortItem(listItem);
    }

    public static void ScrapItemInFromThanhNien(HashSet<FeedItem> ls) {
        thanhnien.list_thanh_nien(thanhnienurl.concat("the-thao/"), ls);
        ls.removeIf(item -> item.getTitle().equals("") || item.getPubDate().equals("") || item.getThumbnail().equals(""));
        listItem = new ArrayList<>(ls);
        SortItem(listItem);
    }
    public static void ScrapItemInFromVnExpress(HashSet<FeedItem> ls) {
        VnExpress.list_VnExpress("https://vnexpress.net/the-thao", ls);
        ls.removeIf(item -> item.getTitle().equals("") || item.getPubDate().equals("") || item.getThumbnail().equals(""));
        listItem = new ArrayList<>(ls);
        SortItem(listItem);
    }
}
