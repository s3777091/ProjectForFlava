package Adapter;

import CardCellController.CardController;
import Model.FeedItem;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RecursiveTask;

/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: 08/09/2021
  Author: team Flava
  Last modified date: 17/09/2021
  Author: members of team Flava
  Acknowledgement: in Document file
*/
public class CoreFunction {
    int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
    int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();

    @FXML
    public GridPane NewsGrid;
    @FXML
    Label taskLabel = new Label();

    @FXML
    private ProgressBar progressBar = new ProgressBar();
    //add progressbar variable

    //this function exchange progessbar value to % value
    public int ExchangeToPercents(int i, int max_value) {
        int result;
        result = (i * 100) / max_value;
        return result;
    }

    public void st(List<FeedItem> data) {
        //this one using the list data
        //but i want divide the task so i using the service
        Service<Void> service = new Service<>() {

            @Override
            protected Task<Void> createTask() {
                Task task = new Task() {
                    @Override
                    protected Void call() throws Exception {
                        progressBar.setMaxWidth(screenWidth);
                        int max_value = data.size();
                        for (int i = 0; i < data.size(); i++) {
                            updateProgress(i, max_value);
                            int Result = ExchangeToPercents(i, data.size());
                            updateMessage("Loading " + Result + " %");
                            Thread.sleep(5);
                        }
                        //call back the function exchange to percentage
                        updateMessage(" ");
                        progressBar.setVisible(true);
                        final CountDownLatch latch = new CountDownLatch(4);
                        Platform.runLater(() -> {
                            try {
                                RunGridPane(data);
                                progressBar.setVisible(false);
                            } finally {
                                latch.countDown();
                            }
                        });
                        latch.await();
                        return null;
                    }
                };
                taskLabel.textProperty().bind(task.messageProperty());
                progressBar.progressProperty().bind(task.progressProperty());
                return task;
            }
        };
        service.start();
    }

    public void RunGridPane(List<FeedItem> data) {
        List<FeedItem> itemsInController = Collections.synchronizedList(new ArrayList<>(data));
        GridPaneCardDisplay(itemsInController, 30);
    }

    public void GridPaneCardDisplay(List<FeedItem> data, int number) {
        try {
            int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
            int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
            int column = 0;
            int row = 1;
            int DynamicGridPane = 0;
            if (screenWidth <= 800 && screenHeight <= 600) {
                DynamicGridPane = 2;
            } else if (screenWidth <= 1280 && screenHeight <= 768) {
                DynamicGridPane = 3;
            } else if (screenWidth <= 1536 && screenHeight <= 864) {
                DynamicGridPane = 4;
            } else if (screenWidth <= 1920 && screenHeight <= 1080) {
                DynamicGridPane = 5;
            }
            for (int i = 0; i < number; i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Design/NewCard.fxml"));
                VBox NewsPosition = loader.load();
                CardController cardController = loader.getController();
                cardController.setdata(data.get(i));
                if (column == DynamicGridPane) {
                    column = 0;
                    ++row;
                }
                // add the data to grid pane and custome them
                NewsGrid.add(NewsPosition, column++, row);
                NewsGrid.setCache(true);
                NewsGrid.setCacheShape(true);
                NewsGrid.setCacheHint(CacheHint.QUALITY);
                // add the data to grid pane and add the position
                javafx.scene.layout.GridPane.setMargin(NewsPosition,
                        new Insets(30, 30, 30, 30));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
