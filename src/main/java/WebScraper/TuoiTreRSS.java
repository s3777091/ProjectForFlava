package WebScraper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import Model.FeedItem;

/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: 18/09/2021
  Author: team Flava
  Last modified date: 11/08/2021
  Author: members of team Flava
  Acknowledgement: in Document file
*/
public class TuoiTreRSS implements findDifference {
    enum choices {
        none,
        title,
        desc,
        date,
        link
    }

    public static HashSet<FeedItem> TUOITRERSS(String URL, HashSet<FeedItem> TuoiTreFeed) {
        try {
            String title = "";                                                                                          //Store titles
            String desc = "";                                                                                           //Store descriptions
            String link = "";                                                                                           //Store links
            String date = "";                                                                                           //Store dates
            String img = "";                                                                                            //Store images
            String category = "";
            boolean ScannedTitle = false;
            boolean ScannedLink = false;
            boolean ScannedDate = false;
            boolean ScannedDesc = false;
            //==============================================URL READER==================================================
            URL Url = new URL(URL);
            BufferedReader in = new BufferedReader(new InputStreamReader(Url.openStream()));
            //Scanner input = new Scanner(in);                                                                          Delete this if no bug is found

            String line;

            //EXTRACT URL FROM BUFFERED READER TO ARRAYLIST OF STRINGS
            ArrayList<String> line2 = new ArrayList<>();
            while ((line = in.readLine()) != null) {
                line2.add(line);
            }


            //TESTING VARIABLES
            int i = 0;                                                                                                  //RUNS FROM 0 TO STRING LENGTH
            String temp;                                                                                                //STORE STRING TEMPORARILY
            char whitespace;                                                                                            //WHITESPACE DETECT
            choices choice;
            Date dates = new Date();
            //STORE VARIABLE FOR CHOICE


            //<==============================================RSS DECODER=================================================>
            while (i < line2.size()) {

                //For choosing switch statement
                choice = choices.none;


                if (line2.get(i).contains("<description>")) {
                    choice = choices.desc;
                }

                if (line2.get(i).contains("<title>")) {
                    choice = choices.title;
                }

                if (line2.get(i).contains("<pubDate>")) {
                    choice = choices.date;

                }

                if (line2.get(i).contains("<link>")) {
                    choice = choices.link;

                }


                //Switch statement
                switch (choice) {

                    case title:                                                                                         //Extract titles
                        temp = line2.get(i);
                        temp = temp.replace("    <title><![CDATA[", "");
                        temp = temp.replace("]]></title>", "");

                        //White space removal
                        whitespace = temp.charAt(0);
                        while (whitespace == ' ') {
                            temp = temp.substring(1);
                            whitespace = temp.charAt(0);
                        }


                        title = temp;
                        ScannedTitle = true;
                        break;


                    case desc:                                                                                          //Extract descriptions
                        temp = line2.get(i);
                        //System.out.println("Original:                      "  + temp);
                        try {
                            if (temp.contains("/></a>TTO -")) {
                                temp = temp.substring(temp.indexOf("<img src=\"") + "<img src=\"".length(), temp.indexOf("\" /></a>TTO - "));
                            } else {
                                temp = temp.substring(temp.indexOf("<img src=\"") + "<img src=\"".length(), temp.indexOf("\" /></a>"));
                            }
                            //System.out.println("image:                      "  + temp);
                        } catch (StringIndexOutOfBoundsException e) {
                            img = "null";
                        }
                        img = temp;
                        temp = line2.get(i);
                        if (temp.contains("/></a>TTO - ")) {
                            temp = temp.substring(temp.indexOf("/></a>TTO - ") + "/></a>TTO - ".length());
                        } else {
                            temp = temp.substring(temp.indexOf("/></a>") + "/></a>".length());
                        }
                        //System.out.println("EoF image:                      "  + temp);
                        temp = temp.replace("]]></description>", "");
                        //System.out.println("EoF desc:                      "  + temp);

                        //White space removal
                        whitespace = temp.charAt(0);
                        while (whitespace == ' ') {
                            temp = temp.substring(1);
                            whitespace = temp.charAt(0);
                        }


                        desc = temp;
                        ScannedDesc = true;
                        break;

                    case link:                                                                                          //Extract link
                        temp = line2.get(i);
                        temp = temp.replace("    <link><![CDATA[", "");
                        temp = temp.replace("]]></link>", "");

                        //White space removal
                        whitespace = temp.charAt(0);
                        while (whitespace == ' ') {
                            temp = temp.substring(1);
                            whitespace = temp.charAt(0);
                        }


                        link = temp;
                        ScannedLink = true;
                        break;

                    case date:                                                                                          //Extract date

                        //Extract date
                        temp = line2.get(i);
                        temp = temp.replace("    <pubDate><![CDATA[", "");
                        temp = temp.replace("]]></pubDate>", "");
                        temp = temp.replace("<pubDate>", "");
                        temp = temp.replace("</pubDate>", "");

                        //White space removal
                        whitespace = temp.charAt(0);
                        while (whitespace == ' ') {
                            temp = temp.substring(1);
                            whitespace = temp.charAt(0);
                        }
                        if (temp.contains("GMT+7")) {
                            temp = temp.replace("GMT+7", "+0700");
                        }
                        DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
                        try {
                            dates = formatter.parse(temp);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        temp = findDifference(convertToLocalDateTimeViaSqlTimestamp(dates));
                        date = findDifference(convertToLocalDateTimeViaSqlTimestamp(dates));
                        ScannedDate = true;
                        break;
                }
                if (ScannedDate && ScannedLink && ScannedTitle && !ScannedDesc) {
                    category = title.replace("Tuổi Trẻ Online - ", "");
                    category = category.replace(" - RSS Feed", "");
                    TuoiTreFeed.add(new FeedItem("", "", date, "", link, category, "TuoiTre", convertToLocalDateTimeViaSqlTimestamp(dates)));
                    ScannedDate = false;
                    ScannedLink = false;
                    ScannedTitle = false;
                } else if (ScannedDate && ScannedLink && ScannedTitle) {
                    TuoiTreFeed.add(new FeedItem(title, desc, date, img, link, category, "TuoiTre", convertToLocalDateTimeViaSqlTimestamp(dates)));
                    ScannedDate = false;
                    ScannedLink = false;
                    ScannedTitle = false;
                    ScannedDesc = false;
                }

                i++;
                temp = "";

            }
            in.close();


            return TuoiTreFeed;
        } catch (MalformedURLException ue) {
            System.out.println("Bad URL");
        } catch (IOException ioe) {
            System.out.println("Something went wrong reading the content");
        }
        return null;

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
    public static LocalDateTime convertToLocalDateTimeViaSqlTimestamp(Date dateToConvert) {
        return new java.sql.Timestamp(
                dateToConvert.getTime()).toLocalDateTime();
    }

    public static LocalDateTime convertToLocalDateTime(String strToConvert) {
        DateTimeFormatter formatter;
        formatter = DateTimeFormatter.ofPattern("HH:mm, yyyy-MM-dd ");
        LocalDateTime dateTime = LocalDateTime.parse(strToConvert, formatter);
        return dateTime;
    }

}
