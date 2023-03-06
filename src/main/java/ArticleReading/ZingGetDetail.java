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
  Created  date: 09/09/2021
  Author: team Flava
  Last modified date: 17/09/2021
  Author: members of team Flava
  Acknowledgement: in Document file
*/
public class ZingGetDetail {
    //Scrape article content from url and put into Vbox
    public static void ZingScraper(Elements article, VBox news, String zing) {
        for (Element e : article) {
            try {
                // text
                if (e.is("p")) {
                    VBox vb = new VBox();
                    Label pa = tool.paragraphView(e.text());
                    vb.getChildren().add(pa);
                    news.getChildren().add(vb);
                    vb.setAlignment(Pos.TOP_LEFT);
                    VBox.setMargin(vb,
                            new Insets(10, 0, 10, 0));
                }
                //p for question diffirent element in zing
                else if (e.is("p.question") || e.is("p.answer")) {
                    VBox vb = new VBox();
                    Label pa = tool.AgrumentView(e.text());
                    vb.getChildren().add(pa);
                    news.getChildren().add(vb);
                    vb.setAlignment(Pos.TOP_LEFT);
                    VBox.setMargin(vb,
                            new Insets(10, 0, 10, 0));
                }
                //H3
                else if (e.is("h3")) {
                    VBox vb = new VBox();
                    Label pa = tool.StrongView(e.text());
                    vb.getChildren().add(pa);
                    news.getChildren().add(vb);
                    vb.setAlignment(Pos.TOP_LEFT);
                    VBox.setMargin(vb,
                            new Insets(10, 0, 10, 0));
                }
                // video
                else if (e.is("figure")) {
                    VBox vb = new VBox();
                    String caption = e.select("figure figcaption").text();
                    vb.getChildren().add(tool.VideoView(e.attr("data-video-src"), caption));
                    news.getChildren().add(vb);
                    vb.setAlignment(Pos.CENTER);
                    VBox.setMargin(vb,
                            new Insets(10, 0, 10, 0));
                }
                // image
                else if (e.is("table.picture") || e.is("table.picture.gallery")) {
                    Elements img = e.select("table.picture tbody");
                    for(Element sr: img){
                        VBox vb = new VBox();
                        String ima = sr.select("td.pic img").attr("data-src");
                        String title = sr.select("td p").text();
                        Image image = new Image(ima, true);
                        vb.getChildren().add(tool.ImageView(image));
                        vb.getChildren().add(tool.paragraphView(title));
                        news.getChildren().add(vb);
                        vb.setAlignment(Pos.CENTER);
                        VBox.setMargin(vb,
                                new Insets(10, 0, 10, 0));
                    }
                }
                //this chart jsoup already have image so i don't knew using selium
                else if (e.is("div.widget")){
                    Elements chart = e.select("div.widget");
                    for(Element c: chart){
                        VBox vb = new VBox();
                        String Chart = c.attr("data-src");
                        Image image = new Image(Chart);
                        vb.getChildren().add(tool.Chart(image));
                        news.getChildren().add(vb);
                        vb.setAlignment(Pos.CENTER);
                        VBox.setMargin(vb,
                                new Insets(10, 0, 10, 0));
                    }
                }

                //scraper the notebox using selinium
                 else if (e.is("div.notebox.ncenter")) {
                    VBox vb = new VBox();
                    String pa = e.select("div.notebox.ncenter p").text();
                    vb.getChildren().add(tool.Note(pa));
                    news.getChildren().add(vb);
                    vb.setAlignment(Pos.CENTER);
                    VBox.setMargin(vb,
                            new Insets(10, 0, 10, 0));
                }
                 //block quote
                else if (e.is("blockquote")) {
                    VBox vb = new VBox();
                    String pa = e.select("blockquote p").text();
                    vb.getChildren().add(tool.Note(pa));
                    news.getChildren().add(vb);
                    vb.setAlignment(Pos.CENTER);
                    VBox.setMargin(vb,
                            new Insets(10, 0, 10, 0));
                }
                else if (e.is("table.picture")) {
                    VBox vb = new VBox();
                    String ima = e.select("div.zad-inimage-wrapper img").attr("data-src");
                    Image image = new Image(ima, true);
                    vb.getChildren().add(tool.ImageView(image));
                    vb.getChildren().add(tool.paragraphView(e.select("td p").text()));
                    news.getChildren().add(vb);
                    vb.setAlignment(Pos.CENTER);
                    VBox.setMargin(vb,
                            new Insets(10, 0, 10, 0));
                }
                else if (e.is("ul") || e.is("div")) {
                    ZingScraper(e.select("> *"), news, zing);
                }
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }

        }
    }
    //Covid chart


}
