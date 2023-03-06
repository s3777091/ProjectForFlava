package WebScraper;

import java.net.HttpURLConnection;
import java.net.URL;

import javafx.application.Platform;
import javafx.scene.control.Alert;

/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: 07/09/2021
  Author: team Flava
  Last modified date: 18/09/2021
  Author: members of team Flava
  Acknowledgement: in Document file
*/
public class InternetChecking implements findDifference {
    //static function to throw exception based of status code
    public static void checkInternet(String url) {
        int statusCode = -1;

        try {
            if (url == null || url == "") {
                url = "https://www.google.com/";
            }

            URL testURL = new URL(url);
            HttpURLConnection http = (HttpURLConnection)testURL.openConnection();
            statusCode = http.getResponseCode();
        }
        //failed to open connection (no internet)
        catch(Exception e){
            displayAlert("No internet.");
        }

        //request failed due to client/server error
        if (statusCode >= 400 && statusCode < 500) {
            displayAlert("Request error (Client-side)");
        }
        else if (statusCode >= 500) {
            displayAlert("Request error (Server-side)");
        } else if (statusCode == -1) {
            displayAlert("No internet.");
        }
    }

    //same function but does not require url
    static void checkInternet() throws Exception {
        int statusCode = -1;

        try {
            String url = "https://www.google.com/";

            URL testURL = new URL(url);
            HttpURLConnection http = (HttpURLConnection)testURL.openConnection();
            statusCode = http.getResponseCode();
        }
        //failed to open connection (no internet)
        catch(Exception e){
            displayAlert("No internet.");
        }

        //request failed due to client/server error
        if (statusCode >= 400 && statusCode < 500) {
            displayAlert("Request error (Client-side)");
        }
        else if (statusCode >= 500) {
            displayAlert("Request error (Server-side)");
        } else if (statusCode == -1) {
            displayAlert("No internet.");
        }
    }

    // void function to display error alert based on given string
    static void displayAlert(String infoString) {
        Platform.runLater(() -> {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Uh oh!");
            al.setHeaderText(infoString);
            al.showAndWait();
        });
    }
}
