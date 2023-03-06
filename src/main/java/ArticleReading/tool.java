package ArticleReading;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;


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
public class tool {

    //this one is tool to display text
    public static Label paragraphView(String pa) {
        Label label = new Label(pa);
        label.setFont(Font.font("Arial", 20));
        label.setTextFill(Color.valueOf("black"));
        label.setWrapText(true);
        label.setLineSpacing(1.2);
        return label;
    }

    //tool to display the text but looking bigger
    public static Label StrongView(String pa) {
        Label label = new Label(pa);
        label.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 30));
        label.setTextFill(Color.valueOf("black"));
        label.setWrapText(true);
        label.setLineSpacing(1.2);
        return label;
    }

    //tool to display text but in agrument
    public static Label AgrumentView(String pa) {
        Label label = new Label(pa);
        label.setFont(Font.font("Arial", FontPosture.ITALIC, 22));
        label.setTextFill(Color.valueOf("black"));
        label.setText("`` " + pa + " ``");
        label.setWrapText(true);
        label.setLineSpacing(1.2);
        return label;
    }
    //this tool display the image View
    public static VBox ImageView(Image im) {
        VBox layout = new VBox();
        layout.setAlignment(Pos.TOP_CENTER);

        ImageView imageView = new ImageView(im);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(700);
        imageView.setFitHeight(600);

        imageView.getFitWidth();

        layout.getChildren().add(imageView);
        return layout;
    }


    //some caption in video and image need
    public static Label captionVideo(String alt)  {
        Label label = new Label(alt);
        label.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 12));
        label.setTextFill(Color.valueOf("black"));
        label.setLineSpacing(1.2);
        return label;
    }

    //video view
    public static Label VideoView(String dataVideosrc, String alt) {
        Media media = new Media(dataVideosrc);
        MediaPlayer mediaplayer = new MediaPlayer(media);

        MediaView mediaView = new MediaView(mediaplayer);
        mediaView.setFitWidth(600);
        mediaView.setSmooth(true);
        // i using this one to set the width of the media screan

        Button player = new Button("Play");
        Button plause = new Button("Pause");

        //add to button in the media view


        Slider progess = new Slider();
        mediaplayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> progess.setValue(newValue.toSeconds()));

        //slider watching the progessbar while watching video
        progess.setOnMousePressed(event -> mediaplayer.seek(Duration.seconds(progess.getValue())));

        //from start user can press the silder
        progess.setOnMouseDragged(event -> mediaplayer.seek(Duration.seconds(progess.getValue())));
        //from start user can dragger the silder to moving faster the video watching

        mediaplayer.setOnReady(() -> {
            Duration to = media.getDuration();
            progess.setMax(to.toSeconds());
        });


        player.setOnAction(arg0 -> {
            mediaplayer.play(); //call play method
        });


        plause.setOnAction(arg0 -> {
            mediaplayer.pause(); //call stop()
        });

        Label volumeLabel = new Label("Vol: ");
        //silder for volumn


        final Slider volumeSlider = new Slider();
        volumeSlider.setPrefWidth(70);
        volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
        volumeSlider.setMinWidth(30);
        //this one is the silder size

        volumeSlider.valueProperty().addListener(observable -> {
            if (volumeSlider.isValueChanging()) //check for volume change
            {
                mediaplayer.setVolume(volumeSlider.getValue() / 100.0); //set volume
            }
        });

        //using stackpane to see custome the media view
        StackPane layout = new StackPane();

        Label label = captionVideo(alt);

        HBox hBox = new HBox();
        hBox.getChildren().add(player);
        hBox.getChildren().add(plause);
        hBox.getChildren().add(volumeLabel);
        hBox.getChildren().add(volumeSlider);


        HBox.setMargin(player,
                new Insets(5, 10, 5, 10));

        HBox.setMargin(plause,
                new Insets(5, 10, 5, 10));

        HBox.setMargin(volumeSlider,
                new Insets(5, 150, 5, 0));

        HBox.setMargin(volumeLabel,
                new Insets(5, 150, 5, 0));
        hBox.setAlignment(Pos.BOTTOM_CENTER);
        //setup where i want to display it



        VBox vBox = new VBox();

        vBox.getChildren().add(mediaView);
        vBox.getChildren().add(progess);
        vBox.getChildren().add(hBox);
        vBox.setAlignment(Pos.TOP_CENTER);

        layout.getChildren().add(vBox);
        //err from here
        layout.getChildren().add(label);

        VBox.setMargin(mediaView,
                new Insets(5, 0, 5, 0));

        // Adjust label position and size

        label.setGraphic(layout);
        label.setPrefWidth(mediaView.getFitWidth());
        return label;
    }

    //this tool is the note
    public static VBox Note(String p) {
        VBox note = new VBox();
        note.setStyle("-fx-background-color: linear-gradient(to right, #c5b48f, #c0c389, #acd58e, #84e8a4, #15f9cb); " +
                "-fx-border-radius: 2");
        Label pa = new Label();
        pa.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        pa.setWrapText(true);
        pa.setLineSpacing(0.5);
        pa.setText(p);
        VBox vB = new VBox();
        VBox.setMargin(pa,
                new Insets(20, 20, 20, 20));
        vB.getChildren().add(pa);
        note.getChildren().addAll(vB);
        return note;
    }
    // some chart push image
    public static VBox Chart(Image im) {
        VBox layout = new VBox();
        layout.setAlignment(Pos.TOP_CENTER);
        ImageView imageView = new ImageView(im);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(600);
        layout.getChildren().add(imageView);

        return layout;
    }

    protected static VBox createWrapNote(VBox news) {
        VBox pane = new VBox();

        pane.setStyle("-fx-border-color: #2c91c7; -fx-background-color: #404040; -fx-alignment: center");
        pane.prefWidthProperty().bind(news.widthProperty().subtract(380));
        return pane;
    }


}
