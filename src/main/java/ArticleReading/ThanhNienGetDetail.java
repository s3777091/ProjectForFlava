package ArticleReading;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: 13/09/2021
  Author: team Flava
  Last modified date: 17/09/2021
  Author: members of team Flava
  Acknowledgement: in Document file
*/
public class ThanhNienGetDetail {
    public static void ThanhNienScraper(Elements article, VBox news) {
        for (Element e : article) {
            try {
                if (e.select("div").select("table").hasClass("imagefull")) {
                    VBox vb = new VBox();
                    String ima = e.select("div").select("table.imagefull img").attr("data-src");
                    Image image = new Image(ima, true);
                    vb.getChildren().add(tool.ImageView(image));
                    vb.getChildren().add(tool.paragraphView(e.select("div").select("table.imagefull").text()));
                    news.getChildren().add(vb);
                    vb.setAlignment(Pos.CENTER);
                    news.setMargin(vb,
                            new Insets(10, 0, 10, 0));
                }
                else if (e.select("div").select("table").hasClass("video")){
                    VBox vb = new VBox();
                    String video = e.select("div").select("table.video div").attr("data-video-src");
                    String caption = e.select("div").select("table.video").text();
                    vb.getChildren().add(tool.VideoView(video, caption));
                    news.getChildren().add(vb);
                    vb.setAlignment(Pos.CENTER);
                    news.setMargin(vb,
                            new Insets(10, 0, 10, 0));
                }
                else if (e.is("div")) {
                    VBox vb = new VBox();
                    Label pa = tool.paragraphView(e.text());
                    vb.getChildren().add(pa);
                    news.getChildren().add(vb);
                    vb.setAlignment(Pos.TOP_LEFT);
                    news.setMargin(vb,
                            new Insets(10, 0, 10, 0));
                }
            } catch (IllegalArgumentException ex) {
                continue;
            }
        }
    }

}
