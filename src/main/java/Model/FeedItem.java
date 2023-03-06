package Model;

import java.time.LocalDateTime;
import java.util.concurrent.RecursiveAction;
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
public class FeedItem {
    //Declaring item inside Header
    private String title;
    private String description;
    private String pubDate;
    private String thumbnail;
    private String link;
    private String category;
    private String name;
    private LocalDateTime time;
    //Default constructor
    public FeedItem(){}

    //Constructor with parameter
    public FeedItem(String title, String description, String pubDate, String thumbnail, String link, String category,
                    String name, LocalDateTime time) {
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.thumbnail = thumbnail;
        this.link = link;
        this.category = category;
        this.name = name;
        this.time = time;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setLink(String Link) {this.link = Link;}

    @Override
    //Testing function
    public String toString() {
        return "\nTitle: " + title + "\n" + "Data: " + pubDate + "\n" + "Summary: "
                + description + "\n" + "Picture: " + thumbnail + "\n" + "Link:" + link + "\n";
    }
}
