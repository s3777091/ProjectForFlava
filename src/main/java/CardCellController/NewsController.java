package CardCellController;

import ArticleReading.*;
import Model.FeedItem;
import WebScraper.InternetChecking;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Objects;


/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: 31/08/2021
  Author: team Flava
  Last modified date: 18/09/2021
  Author: members of team Flava
  Acknowledgement: in Document file
*/
public class NewsController {

    @FXML
    private ImageView BackGround;

    @FXML
    private Label Header;

    @FXML
    private Label Sumary;

    @FXML
    private VBox Article;

    @FXML
    private BorderPane parent;

    private boolean isLightMode = true;

    //Set to change mode in NewsController
    public void ChangeMode() {
        isLightMode = !isLightMode;
        if (isLightMode) {
            setLightMode();
        } else {
            setDarkMode();
        }
    }

    //Set to light mode in NewsController
    private void setLightMode() {
        parent.getStylesheets().remove("Design/Css/DarkPaper.css");
        parent.getStylesheets().add("Design/Css/LightPaper.css");
    }

    //Set to dark mode in NewsController
    private void setDarkMode() {
        parent.getStylesheets().remove("Design/Css/LightPaper.css");
        parent.getStylesheets().add("Design/Css/DarkPaper.css");
    }

    //Go back to main
    public void NavigateToMain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("Design/MainDesign.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
        Article.getChildren().clear();
    }

    public void NewReader(FeedItem item) {
        Platform.runLater(() -> {
            switch (item.getName()) {
                case "Zing News" -> readZing(item.getLink(), item.getThumbnail());
                case "ThanhNien" -> readThanhNien(item.getLink(), item.getThumbnail());
                case "VnExpress" -> readVn(item.getLink(), item.getThumbnail());
                case "TuoiTre" -> readTuoiTre(item.getLink(), item.getThumbnail());
            }
        });
    }


    //Choose what reader function to use base on the header's news website
    //<---------------------------------------------------Article Reader function-------------------------------------------->
    //our straryty is while scraper the newsaper by jsoup using tool and render it directly to read.
    private void readZing(String urlAddress, String imageLink) {
        try {
            String Zinglink = "https://zingnews.vn/";
            String ZINGADRESS = Zinglink.concat(urlAddress);
            InternetChecking.checkInternet(ZINGADRESS);
            //function internet checking make by minh this one check if internet is unstable or disconect will be stop
            Document doc = Jsoup.connect(ZINGADRESS).timeout(5000).get();
            Elements article = doc.select("div.the-article-body");
            ZingGetDetail.ZingScraper(article, Article, ZINGADRESS);
            String header = doc.select("h1.the-article-title").text();
            Header.setText(header);
            //set header
            Image image = new Image(imageLink, true);
            BackGround.setImage(image);
            //backGround is the image that cuong design
            String summary = doc.select("p.the-article-summary").text();
            Sumary.setText(summary);
            //add the text

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void readThanhNien(String urlAddress, String imageLink) {
        try {
            InternetChecking.checkInternet(urlAddress);
            Document doc = Jsoup.connect(urlAddress).timeout(5000).get();
            Elements body = doc.select("body").select("div#abody.cms-body");
            body = body.first().children();
            Elements summary = doc.select("div.sapo");
            String sum = summary.select("div").text();
            Sumary.setText(sum);
            Image image = new Image(imageLink, true);
            BackGround.setImage(image);
//            ThanhNienGetDetail.ThanhNienHeaderScraper(header, Article, Sumary);
            ThanhNienGetDetail.ThanhNienScraper(body, Article);
            Elements meta = doc.select("h1.details__headline");
            String title = meta.text();
            Header.setText(title);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void readVn(String urlAdress, String imageLink) {
        try {
            Document doc = Jsoup.connect(urlAdress).timeout(5000).get();
            Elements article = doc.getAllElements();
            VnGetDetail.VnScraper(article, Article);
            String header = doc.select("title").text();
            Header.setText(header);
            Image image = new Image(imageLink, true);
            BackGround.setImage(image);
            Elements description = doc.select("p.description");
            String str = description.toString();
            if (str.contains("<span>")) {
                str = getRidOfSpan(str);
                Document AnotherDoc = Jsoup.parse(str);
                Sumary.setText(AnotherDoc.text());
                System.out.println(AnotherDoc.text());
            } else {
                Sumary.setText(Jsoup.parse(str).text());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readTuoiTre(String urlAdress, String imageLink) {
        try {
            Document doc = Jsoup.connect(urlAdress).timeout(5000).get();
            Elements body = doc.select("div#mainContentDetail");
            Elements article = body.select("div#main-detail-body > *");
            String header = doc.select("meta[name=title]").attr("content");
            Header.setText(header);
            Image image = new Image(imageLink, true);
            BackGround.setImage(image);
            String sumary = doc.select("meta[property=og:description]").attr("content");
            Sumary.setText(sumary);
            TuoiTreGetDetail.TuoiTreScraper(article, Article);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //<---------------------------------------------------End of article reader function-------------------------------------------->


    //Get rid of string "<span>"
    public static String getRidOfSpan(String str) {
        int i = 1;
        String str1 = "";
        while (i <= str.length()) {                                         //Get string "*</span>"
            str1 = str1.concat(str.substring(i - 1, i));
            if (str1.contains("</span>")) {
                break;
            }
            i++;
        }
        i = str1.length();
        String str2 = "";
        while (i >= 1) {                                                  //Get string "<span>*</span>"
            i--;
            str2 = str2.concat(str1.substring(i, i + 1));

            if (str2.contains(">napn<")) {
                break;
            }
        }
        StringBuilder reverse = new StringBuilder().append(str2);
        reverse.reverse();
        str = str.replace(reverse, "");
        return str;
    }
}