package CardCellController;

import Model.FeedItem;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

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
public class CardController {
    @FXML
    private ImageView thumbnail;

    @FXML
    private Label title;

    @FXML
    private Label pubDate;

    @FXML
    private Button Button;

    @FXML
    private Label source;

    @FXML
    private Pane Card;


    private Stage stage;
    private Scene scene;

    //data card
    FeedItem cardItem;

    //Set data for card and handle error if the image is null value or some kind missng value in text or date
    public void setdata(FeedItem item) {
        cardItem = item;
        Rectangle clip = new Rectangle(
                thumbnail.getFitWidth(), thumbnail.getFitHeight()
        );
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        thumbnail.setClip(clip);
        thumbnail.setEffect(new DropShadow(20, Color.BLACK));
        thumbnail.setCache(true);
        thumbnail.setCacheHint(CacheHint.SPEED);
        String thumbnailUrl = item.getThumbnail();
        Image image;
        //catch error by unsupported image url
        if (!thumbnailUrl.equals("")) {
            image = new Image(thumbnailUrl);
        } else {
            image = new Image("https://i.ibb.co/0CNNqRb/Buster.png");
        }
        thumbnail.setImage(image);
        if (!item.getTitle().equals("")) {
            title.setText(item.getTitle());
        } else {
            title.setText("News don't have title");
        }
        if (!item.getPubDate().equals("")) {
            pubDate.setText(item.getPubDate());
        } else {
            pubDate.setText("News don't have time");
        }
        if (!item.getName().equals("")) {
            source.setText(item.getName());
        } else {
            source.setText("Unknow");
        }
    }

    double xOffset, yOffset;


    //To read news
    public void Read(ActionEvent e) throws IOException{

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Design/DisplayNews.fxml"));
            Parent root = loader.load();
            NewsController newsController = loader.getController();
            newsController.NewReader(cardItem);
            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();

            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
            //function moving the app
            root.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });
            root.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });
    }


    //Zoom in card animation
    public void hoverCard() {
        ScaleTransition scaleOut = new ScaleTransition();
        scaleOut.setNode(Card);
        scaleOut.setDuration(Duration.millis(200));
        scaleOut.setInterpolator(Interpolator.EASE_BOTH);
        scaleOut.setToX(1.2);
        scaleOut.setToY(1.2);

        FadeTransition fadeOut = new FadeTransition();
        fadeOut.setNode(Button);
        fadeOut.setDuration(Duration.millis(300));
        fadeOut.setInterpolator(Interpolator.EASE_BOTH);
        fadeOut.setToValue(1);

        ParallelTransition Parallel = new ParallelTransition(scaleOut, fadeOut);
        Parallel.play();
    }

    //Zoom out card animation
    public void exitCard() {
        ScaleTransition scaleIn = new ScaleTransition();
        scaleIn.setNode(Card);
        scaleIn.setDuration(Duration.millis(400));
        scaleIn.setInterpolator(Interpolator.EASE_BOTH);
        scaleIn.setToX(1);
        scaleIn.setToY(1);

        FadeTransition FadeIn = new FadeTransition();
        FadeIn.setNode(Button);
        FadeIn.setDuration(Duration.millis(600));
        FadeIn.setToValue(0);
        FadeIn.setInterpolator(Interpolator.EASE_BOTH);

        ParallelTransition Parallel = new ParallelTransition(scaleIn, FadeIn);
        Parallel.play();
    }
}

