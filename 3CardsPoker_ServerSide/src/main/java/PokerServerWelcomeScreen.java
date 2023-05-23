import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class PokerServerWelcomeScreen extends Application {
	private PokerServer pokerServer;
	private ObservableList<String> gameInfoList = FXCollections.observableArrayList();
	private ListView<String> gameInfoListView = new ListView<>(gameInfoList);

	@Override
	public void start(Stage primaryStage) {
		// Intro screen
		GridPane introGrid = new GridPane();
		introGrid.setAlignment(Pos.CENTER);
		introGrid.setHgap(10);
		introGrid.setVgap(10);
		introGrid.setPadding(new Insets(25, 25, 25, 25));

		Label welcomeLabel = new Label("Welcome to 3 Cards Poker!");
		welcomeLabel.setFont(new Font(18));
		Label welcomeLabel2 = new Label("(Server)");
		welcomeLabel2.setFont(new Font(18));
		Label portLabel = new Label("Port:");
		introGrid.add(portLabel, 0, 1);
		TextField portField = new TextField();
		introGrid.add(portField, 1, 1);

		Button startServerButton = new Button("Start Server");
		introGrid.add(startServerButton, 1, 2);

		// Status Bar
		Label statusLabel = new Label();
		statusLabel.setText("Status: Server Off");
		Label statusLabel2 = new Label();
		statusLabel2.setText("Status: Server On");

		// Borderpane Bar
		BorderPane borderPane = new BorderPane();
		VBox bCenter = new VBox(5, welcomeLabel, welcomeLabel2, introGrid);
		bCenter.setAlignment(Pos.CENTER);
		borderPane.setCenter(bCenter);
		borderPane.setBottom(statusLabel);
		introGrid.setAlignment(Pos.CENTER);
		borderPane.setAlignment(statusLabel, Pos.CENTER);
		Scene welcomeScene = new Scene(borderPane, 400, 275);

		borderPane.setBackground(
				new javafx.scene.layout.Background(
						new javafx.scene.layout.BackgroundFill(
								Color.LIGHTGREY, null, null
						)
				)
		);

		// Game info screen
		VBox gameInfoBox = new VBox(10);
		gameInfoBox.setPadding(new Insets(10, 10, 10, 10));

		Label gameInfoLabel = new Label("Game Information:");
		gameInfoBox.getChildren().add(gameInfoLabel);

		gameInfoListView.setPrefHeight(400);
		gameInfoBox.getChildren().add(gameInfoListView);

		HBox controlBox = new HBox(10);
		controlBox.setAlignment(Pos.CENTER);
		Button stopServerButton = new Button("Stop Server");
		controlBox.getChildren().add(stopServerButton);
		gameInfoBox.getChildren().add(controlBox);
		gameInfoBox.getChildren().add(statusLabel2);
		gameInfoBox.setAlignment(Pos.CENTER);

		Scene gameInfoScene = new Scene(gameInfoBox, 400, 500);


		// Button actions
		startServerButton.setOnAction(e -> {
			StringBuilder errors = new StringBuilder();

			try {
				int port = Integer.parseInt(portField.getText());
				//System.out.println(port);
				if (port < 1024 || port > 65535) {
					// Show an error message if the port number is out of range
					errors.append("Invalid port number. Please enter a port number between 1024 and 65535.");
					if (errors.length() > 0) {
						// display an alert dialog with the error messages
						Alert alert = new Alert(Alert.AlertType.ERROR, errors.toString());
						alert.setTitle("3 Card Poker Server");
						alert.showAndWait();
					}
					return;
				}

				pokerServer = new PokerServer(port, gameInfoList);
				pokerServer.startServer();

				primaryStage.setScene(gameInfoScene);
			} catch (NumberFormatException ex) {
				// Show an error message if the entered value is not a valid integer
				errors.append("Invalid port number. Please enter a valid integer between 1024 and 65535.");
			} catch (FileNotFoundException ex) {
				throw new RuntimeException(ex);
			}
			if (errors.length() > 0) {
				// display an alert dialog with the error messages
				Alert alert = new Alert(Alert.AlertType.ERROR, errors.toString());
				alert.setTitle("3 Card Poker Server");
				alert.showAndWait();
			}
		});


		stopServerButton.setOnAction(e -> {

			pokerServer.stopServer();
			primaryStage.setScene(welcomeScene);
		});

		primaryStage.setTitle("3 Card Poker Server");
		primaryStage.setScene(welcomeScene);
		primaryStage.show();
	}

	public static void main(String[] args) {

		launch(args);
	}
}
