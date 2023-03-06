package ArticleReading;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;

/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: 15/09/2021
  Author: team Flava
  Last modified date: 17/09/2021
  Author: members of team Flava
  Acknowledgement: in Document file
*/
public class TuoiTreGetDetail {
    //Tuoitre detail scraper
    public static void TuoiTreScraper(Elements article, VBox news) {

        for (Element e : article) {
            try {
                if (e.is("p") && e.hasText()) {
                    VBox vb = new VBox();
                    Label label = tool.paragraphView(e.text());
                    if (e.select("b").size() > 0)
                        label.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
                    vb.getChildren().add(label);
                    news.getChildren().add(vb);
                    news.setMargin(vb,
                            new Insets(10, 0, 10, 0));
                } else if (e.is("div")) {
                    if (e.attr("type").equals("Photo")) {
                        Elements i = e.select("img");
                        for (Element im : i) {
                            try {
                                VBox vb = new VBox();
                                String imageSrc = im.attr("src");
                                String text = e.select("p").text();
                                Image image = new Image(imageSrc, true);
                                vb.getChildren().add(tool.ImageView(image));
                                vb.getChildren().add(tool.paragraphView(text));
                                vb.setAlignment(Pos.CENTER);
                                news.getChildren().add(vb);
                                news.setMargin(vb,
                                        new Insets(10, 0, 10, 0));

                            } catch (StringIndexOutOfBoundsException exception) {
                            }
                        }
                    }
                    else if (e.attr("type").equals("VideoStream")) {
                        String videoSrc = e.attr("data-src");
                        videoSrc = videoSrc.substring(videoSrc.indexOf("hls"));
                        videoSrc = videoSrc.substring(0, videoSrc.indexOf(".mp4") + 4);
                        videoSrc = videoSrc.replace("&vid=", "/");
                        videoSrc = "https://" + videoSrc;
                        VBox vb = new VBox();
                        vb.getChildren().add(tool.VideoView(videoSrc, e.select("p").text()));
                        news.getChildren().add(vb);
                        vb.setAlignment(Pos.CENTER);
                        VBox.setMargin(vb,
                                new Insets(10, 0, 10, 0));
                    }
                    // Add wrapnote if element is wrapnote
                    else if (e.attr("type").equals("wrapnote")) {
                        VBox pane = tool.createWrapNote(news);
                        // Loop through elements in wrap note and add into wrapnote
                        TuoiTreScraper(e.select("> *"), pane);
                        news.getChildren().add(pane);
                    }
                }

            } catch (IllegalArgumentException ex) {
                continue;
            }
        }
    }

    //Video url getter

}

