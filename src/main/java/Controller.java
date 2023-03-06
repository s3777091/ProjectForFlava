import Adapter.Apdater;
import Model.FeedItem;
import WebScraper.CovidAPI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
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
public class Controller extends Apdater implements Initializable {

    // here is the variable call in screan builder
    @FXML
    private BorderPane parent;

    @FXML
    private Label Cases;

    @FXML
    private Label recovered;

    @FXML
    private Label death;

    @FXML
    private Label DynamicCaNhiem;

    @FXML
    private Label DynamicHoiPhuc;

    @FXML
    private ImageView DarkMode;


    @Override
    //Set the default page to be on Newest category
    public void initialize(URL location, ResourceBundle resources) {
            CovidData();
            Newest();
    }
    private boolean isLightMode = true;
    //Change between light mode and dark mode
    public void ChangeMode(ActionEvent event) {
        isLightMode = !isLightMode;
        if(isLightMode){
            setLightMode();
        } else {
            setDarkMode();
        }
    }
    //Set to light mode
    private void setLightMode(){
        parent.getStylesheets().remove("Design/Css/DarkPaper.css");
        parent.getStylesheets().add("Design/Css/LightPaper.css");
        Image Moon = new Image("https://i.ibb.co/f2TCfZ9/moon.png");
        DarkMode.setImage(Moon);
    }
    //Set to dark mode
    private void setDarkMode(){
        parent.getStylesheets().remove("Design/Css/LightPaper.css");
        parent.getStylesheets().add("Design/Css/DarkPaper.css");
        Image Sun = new Image("https://i.ibb.co/sCwP9Pr/sun.png");
        DarkMode.setImage(Sun);
    }
    //Covid API to get Cases, Recovered ad deceased
    private void CovidData(){
        try {
            JSONObject json = CovidAPI.readJsonFromUrl("https://corona.lmao.ninja/v2/countries/vn");
            String Case = String.valueOf(json.get("todayCases"));
            String Total_case = String.valueOf(json.get("cases"));
            String recovery = String.valueOf(json.get("todayRecovered"));
            String Total_recovered = String.valueOf(json.get("recovered"));
            String deaths = String.valueOf(json.get("deaths"));

            //using at json api
            if(Case.equals("0")){
                Cases.setText(Total_case);
                DynamicCaNhiem.setText("Total Case: ");
            } else {
                Cases.setText(Case);
                DynamicCaNhiem.setText("Case: ");
            }
            if(recovery.equals("0")){
                recovered.setText(Total_recovered);
                DynamicHoiPhuc.setText("Total Recovery: ");
            } else{
                recovered.setText(recovery);
                DynamicHoiPhuc.setText("Recovery: ");
            }
            death.setText(deaths);
            // dynamic change if the api update today value it will change and update for the user
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}