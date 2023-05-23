import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PokerClientWelcomeScreen extends Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) {
		GridPane welcomeUI = new GridPane();
		welcomeUI.setHgap(10);
		welcomeUI.setVgap(10);
		welcomeUI.setPadding(new Insets(20, 20, 20, 20));

		Label welcomeLabel = new Label("Welcome to 3 Cards Poker!");
		welcomeLabel.setFont(new Font(18));
		welcomeLabel.setTextFill(Color.WHITE);
		Label portLabel = new Label("Port Number:");
		portLabel.setTextFill(Color.WHITE);
		Label ipLabel = new Label("IP Address:");
		ipLabel.setTextFill(Color.WHITE);
		TextField portTextField = new TextField();
		portTextField.setPromptText("Port Number");
		portTextField.setText("25555");
		TextField ipTextField = new TextField();
		ipTextField.setPromptText("IP Address");
		ipTextField.setText("127.0.0.1");
		Button connectButton = new Button("Connect");

		welcomeUI.add(welcomeLabel, 0, 0);
		welcomeUI.add(ipLabel, 0, 1);
		welcomeUI.add(ipTextField, 1, 1);
		welcomeUI.add(portLabel, 0, 2);
		welcomeUI.add(portTextField, 1, 2);
		welcomeUI.add(connectButton, 0, 3);


		StackPane welcomeScreen = new StackPane(welcomeUI);

		Stop[] stops = new Stop[] {
				new Stop(0, Color.rgb(53, 118, 78)), // Dark green at center
				new Stop(1, Color.rgb(27, 60, 40))   // Light green at edges
		};

		RadialGradient gradient = new RadialGradient(
				0,                  // focusAngle
				0.3,                // focusDistance
				0.3,                // centerX
				0.7,                // centerY
				0,              // radius
				true,               // proportional
				CycleMethod.NO_CYCLE, // cycleMethod
				stops               // stops
		);

		welcomeScreen.setBackground(
				new javafx.scene.layout.Background(
						new javafx.scene.layout.BackgroundFill(
								gradient, null, null
						)
				)
		);

		primaryStage.setTitle("3 Cards Poker");
		Scene welcomeScene = new Scene(welcomeScreen);
		primaryStage.setScene(welcomeScene);
		primaryStage.show();


		// Button actions
		connectButton.setOnAction(event -> {
			try {
				String ipAddress = ipTextField.getText();
				int portNumber = Integer.parseInt(portTextField.getText());

				PokerClient pokerClient = new PokerClient(ipAddress, portNumber);

				if (pokerClient.isConnected()) {
					// Change the scene to the game play screen
					PokerClientGamePlayScreen gpScreen = new PokerClientGamePlayScreen(pokerClient);
					gpScreen.setDisplay(primaryStage);
				}
			} catch (Exception e) {
				// Show connection error message
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Connection Error");
				alert.setHeaderText(null);
				alert.setContentText("Failed to connect to the server. Please check the IP address and port number.");
				alert.showAndWait();
			}
		});

	}

}
