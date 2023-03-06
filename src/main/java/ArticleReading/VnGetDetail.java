package ArticleReading;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.HashSet;

/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: 10/09/2021
  Author: team Flava
  Last modified date: 17/09/2021
  Author: members of team Flava
  Acknowledgement: in Document file
*/
public class VnGetDetail {
    //VnExpress article detail scraper
    public static void VnScraper(Elements article, VBox news) {
        HashSet<String> thumbnail = new HashSet<>();
        HashSet<String> img = new HashSet<>();
        HashSet<String> text = new HashSet<>();
        VBox vb = new VBox();
        for (Element e : article) {
            if (e.hasClass("Normal")) {
                VBox newVB = new VBox();
                if(text.contains(e.select("p.Normal").text())) continue;
                text.add(e.select("p.Normal").text());
                Label pa = tool.paragraphView(e.select("p.Normal").text());
                newVB.getChildren().add(pa);
                news.getChildren().add(newVB);
                VBox.setMargin(newVB,
                        new Insets(10, 0, 10, 0));
            }
            if (e.select("article").select("meta").attr("itemprop").equals("url")) {
                if(thumbnail.contains(e.select("article").select("meta").attr("content"))) continue;
                thumbnail.add(e.select("article").select("meta").attr("content"));
            } else if (e.select("article").select("img").hasAttr("src")) {
                VBox newVB = new VBox();
                if(e.select("article").select("img").attr("src").equals("")) continue;
                if (img.contains(e.select("article").select("img").attr("src"))) continue;
                img.add(e.select("article").select("img").attr("src"));

                ArrayList<String> imageString = new ArrayList<>();
                String str = e.select("article").select("img").attr("src");
                String[] str2 = str.split(", ");
                if(str2.length<2) {
                    newVB.getChildren().add(tool.ImageView(new Image(e.select("article").select("img").attr("src"),true)));
                    newVB.getChildren().add(tool.paragraphView(captionGetter(e)));
                    news.getChildren().add(newVB);
                    newVB.setAlignment(Pos.CENTER);
                    VBox.setMargin(newVB,new Insets(10,0,10,0));
                    continue;
                }
                if (img.contains(str2[2].replace(" 2x", ""))) continue;
                img.add(str2[2].replace(" 2x", ""));
                imageString.add(str2[2].replace(" 2x", ""));
//                    VBox vb = new VBox();
                Image[] imageArray = new Image[imageString.size()];
                int position = 0;
                for (String i : imageString) {
                    imageArray[position] = new Image(imageString.get(position), true);
                    position++;
                }

                for (Image i : imageArray) {
                    newVB.getChildren().add(tool.ImageView(i));
                    newVB.getChildren().add(tool.paragraphView(captionGetter(e)));
                }
                news.getChildren().add(newVB);
                newVB.setAlignment(Pos.CENTER);
                VBox.setMargin(newVB,new Insets(10,0,10,0));

            } else if (e.select("picture").select("source").hasAttr("data-srcset")) {
                VBox newVB = new VBox();
                ArrayList<String> imageString = new ArrayList<>();
                String str = e.select("picture").select("source").attr("data-srcset");
                String[] str2 = str.split(", ");
                if(str2.length<2) {
                    newVB.getChildren().add(tool.ImageView(new Image(e.select("article").select("img").attr("src"),true)));
                    newVB.getChildren().add(tool.paragraphView(captionGetter(e)));
                    news.getChildren().add(newVB);
                    newVB.setAlignment(Pos.CENTER);
                    VBox.setMargin(newVB,new Insets(10,0,10,0));
                    continue;
                }
                if (img.contains(str2[2].replace(" 2x", ""))) continue;
                img.add(str2[2].replace(" 2x", ""));
                imageString.add(str2[2].replace(" 2x", ""));
//                    VBox vb = new VBox();
                Image[] imageArray = new Image[imageString.size()];
                int position = 0;
                for (String i : imageString) {

                    imageArray[position] = new Image(imageString.get(position), true);
                    position++;
                }
                for (Image i : imageArray) {
                    newVB.getChildren().add(tool.ImageView(i));
                    newVB.getChildren().add(tool.paragraphView(captionGetter(e)));
                }
                news.getChildren().add(newVB);
                newVB.setAlignment(Pos.CENTER);
                VBox.setMargin(newVB,new Insets(10,0,10,0));
            }
            if (e.select("video").hasAttr("src")) {
                VBox newVB = new VBox();
                if(img.contains(e.select("video").attr("src"))) continue;
                img.add(e.select("video").attr("src"));
                newVB.getChildren().add(tool.VideoView(getVideoUrl(e.select("video").attr("src")), ""));
                news.getChildren().add(newVB);
                newVB.setAlignment(Pos.CENTER);
                VBox.setMargin(newVB,
                        new Insets(10, 0, 10, 0));

            }
        }
    }
    //Scrape video url
    private static String getVideoUrl(String urlAddress) {
        String vidURL = urlAddress.replace("d1.", "v.");
        vidURL = vidURL.replaceFirst("video/", "");
        if (vidURL.contains("/vne/master.m3u8")) {
            vidURL = vidURL.replace("/vne/master.m3u8",".mp4");
            if(vidURL.substring(vidURL.indexOf(",",vidURL.lastIndexOf(","))+4).contains("720p")) vidURL = vidURL.replace("720p","");
            vidURL = vidURL.replace(vidURL.substring(vidURL.indexOf("/,"),vidURL.lastIndexOf(",")+1),"");
        } else if (vidURL.contains("index-v1-a1.m3u8")) {
            vidURL = vidURL.replace("/index-v1-a1.m3u8", ".mp4");
        }
        return vidURL;
    }
    //Scrape caption for image
    public static String captionGetter(Element e) {
        if (e.select("p").hasClass("Image")) {
            return e.select("p").text();
        }
        return "";
    }
}

