package egghunt.gui;

import egghunt.engine.GameEngine;
import egghunt.engine.MapPosition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
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
    private Label lblKeyCount = new Label("Collected keys: " + engine.totalKeys);
    private Label lblTurnCount = new Label("Turns played: " + engine.moveNumber);
    private Label controls = new Label("Movement Controls:");
    private Button moveUp = new Button("^");
    private Button moveDown = new Button("v");
    private Button moveLeft = new Button("<");
    private Button moveRight = new Button(">");
    private Button helpButton = new Button("How To Play");

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("game_gui.fxml"));
        //Button root = new Button("Amazing Miner Game coming soon...");
        //root.setFont(new Font(24));

        //TODO replace with value collected by user

        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(5, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        dialog.setTitle("Choice Dialog");
        dialog.setHeaderText("Select a number between 0 and 10. (Default = 5)");
        dialog.setContentText("Choose:");
        dialog.showAndWait();
        engine.initialiseEngine(dialog.getSelectedItem());

        GridPane pane = new GridPane();
        for (int i = 0; i < engine.getSize(); i++)
            for (int j = 0; j < engine.getSize(); j++)
                pane.add(cell[i][j] = new Cell(), i, j);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(pane);

        VBox vbox = new VBox(8); // spacing = 8
        vbox.getChildren().addAll(lblEggCount, lblKeyCount, helpButton);
        borderPane.setLeft(vbox);

        HBox hBox = new HBox(8);
        hBox.getChildren().addAll(controls, moveUp, moveDown, moveLeft, moveRight);
        borderPane.setBottom(hBox);

        /**experiments
        //cell[2][2].setCellContents("X");
        //cell[0][9].setCellContents(engine.mapAt(0, 0));
        //cell[9][0].setCellContents(engine.mapAt(9, 9));
        */

        moveUp.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Repurposes the rightFrom function from GameEngine as the arrays used in the TUI and GUI are formed differently
                MapPosition newCell = MapPosition.rightFrom(engine.currentCell);
                engine.attemptToMoveTo(newCell);
                movementHandler(newCell);
            }
        });

        moveLeft.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Repurposes the downFrom function from GameEngine as the arrays used in the TUI and GUI are formed differently
                MapPosition newCell = MapPosition.downFrom(engine.currentCell);
                engine.attemptToMoveTo(newCell);
                movementHandler(newCell);
            }
        });
        moveRight.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Repurposes the upFrom function from GameEngine as the arrays used in the TUI and GUI are formed differently
                MapPosition newCell = MapPosition.upFrom(engine.currentCell);
                movementHandler(newCell);
            }
        });
        moveDown.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Repurposes the downFrom function from GameEngine as the arrays used in the TUI and GUI are formed differently
                MapPosition newCell = MapPosition.leftFrom(engine.currentCell);
                movementHandler(newCell);

            }
        });

        helpButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Use the directional arrow buttons to move. " +
                        "\nCollect all 5 eggs and reach the end to win. \nCollect Keys to open locks. " +
                        "\nYou have 100 moves to finish the game");
                alert.setTitle("Help");
                alert.setHeaderText("Help Guide:");
                alert.showAndWait();
            }
        });

        primaryStage.setScene(new Scene(borderPane, 800, 600));
        primaryStage.setTitle("Maze Runner Game");
        primaryStage.show();
        mapRender();
    }

    private void movementHandler(MapPosition newCell) {
        String message = engine.attemptToMoveTo(newCell);
        if(!message.equals("")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
            alert.showAndWait();
        }
        if (engine.moveNumber <= 100) {
            engine.moveNumber++;
            lblEggCount.setText("Collected eggs: " + engine.totalEggs);
            lblKeyCount.setText("Collected keys: " + engine.totalKeys);
            lblTurnCount.setText("Turns played: " + engine.moveNumber);
            mapRender();
        }
        //TODO create game end and scoring system here
        /**if (engine.moveNumber > 100) {

        }*/
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

