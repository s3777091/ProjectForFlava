import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

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
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    double xOffset, yOffset;
    //I create 2 variable to control the app moving
    Parent root;

    @Override
    //Start application
    public void start(Stage primaryStage) throws Exception {
        //this one load the file Desgin fxml
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Design/MainDesign.fxml")));//Get MainDesign.fxml
        //this one set icon by using api image
        primaryStage.getIcons().add(new Image("https://i.ibb.co/BnpnJ6Y/APPLOGO.png"));
        //add scene is root(parent)
        Scene scene = new Scene(root);
        //disable tabbar in javafx default
        //create name for it
        primaryStage.setTitle("NewSpaper");
        primaryStage.setScene(scene);
        //i need to set full screen
        primaryStage.setFullScreen(true);
        //show the screan while start
        primaryStage.show();
        //make screan background is transparent
        scene.setFill(Color.TRANSPARENT);

        //two function that can moving screan
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

    }


}
