package egghunt.gui;

import egghunt.engine.GameEngine;
import egghunt.engine.MapPosition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * GUI for the Maze Runner Game.
 *
 * NOTE: Do NOT run this class directly in IntelliJ - run 'RunGame' instead.
 */
public class GameGUI extends Application {
    private GameEngine engine = new GameEngine(10);

    // Create and initialize cell
    private Cell[][] cell =  new Cell[engine.getSize()][engine.getSize()];

    // Create and initialize a status label
    private Label lblEggCount = new Label("Collected eggs: " + engine.totalEggs);
    private Label lblKeyCount = new Label("Collected keys: " + engine.totalEggs);

    private Button moveUp = new Button("^");
    private Button moveDown = new Button("v");
    private Button moveLeft = new Button("<-");
    private Button moveRight = new Button("->");

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("game_gui.fxml"));
        //Button root = new Button("Amazing Miner Game coming soon...");
        //root.setFont(new Font(24));

        //TODO replace with value collected by user
        engine.initialiseEngine(5);

        GridPane pane = new GridPane();
        for (int i = 0; i < engine.getSize(); i++)
            for (int j = 0; j < engine.getSize(); j++)
                pane.add(cell[i][j] = new Cell(), i, j);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(pane);

        VBox vbox = new VBox(8); // spacing = 8
        vbox.getChildren().addAll(lblEggCount, lblKeyCount);
        borderPane.setLeft(vbox);

        HBox hBox = new HBox(8);
        hBox.getChildren().addAll(moveUp, moveDown, moveLeft, moveRight);
        borderPane.setBottom(hBox);

        /**experiment*/
        //cell[2][2].setCellContents("X");

        //TODO extract button events to method. Move alerts and movement attempts outside of button events. Reduce duplicate code
        moveUp.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Go left
                MapPosition newCell = MapPosition.rightFrom(engine.currentCell);
                engine.attemptToMoveTo(newCell);
                mapRender();
            }
        });

        moveLeft.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Go left
                MapPosition newCell = MapPosition.downFrom(engine.currentCell);
                engine.attemptToMoveTo(newCell);
                mapRender();
            }
        });
        moveRight.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Go left
                MapPosition newCell = MapPosition.upFrom(engine.currentCell);
                String message = engine.attemptToMoveTo(newCell);
                if(!message.equals("")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
                    alert.showAndWait();
                }
                mapRender();
            }
        });
        moveDown.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Go left
                MapPosition newCell = MapPosition.leftFrom(engine.currentCell);
                String message = engine.attemptToMoveTo(newCell);
                if(!message.equals("")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
                    alert.showAndWait();
                }
                mapRender();
            }
        });

        primaryStage.setScene(new Scene(borderPane, 800, 600));
        primaryStage.setTitle("Maze Runner Game");
        primaryStage.show();

        mapRender();

        /**experiment
        cell[0][9].setCellContents(engine.mapAt(0, 0));
        //cell[9][0].setCellContents(engine.mapAt(9, 9));*/
    }

    /** In IntelliJ, do NOT run this method.  Run 'RunGame.main()' instead. */
    public static void main(String[] args) {
        launch(args);
    }
    // An inner class for a cell
    public class Cell extends StackPane {
        private Label cellContents = new Label();
        public Cell() {
            setStyle("-fx-border-color: black");
            this.setPrefSize(800, 800);
            this.getChildren().add(cellContents);
        }
        public void setCellContents(String contentValue) {
            cellContents.setText(contentValue);
        }
        public String getCellContents() {
            return cellContents.getText();
        }
    }
    private void mapRender(){
        for (int i = 0; i < engine.getSize(); i++) {
            for (int j = engine.getSize() - 1; j >= 0; j--) {
                cell[i][j].setCellContents(engine.mapAt(i, j));
            }
        }
    }
}

