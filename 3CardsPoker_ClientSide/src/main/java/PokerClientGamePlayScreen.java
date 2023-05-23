import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.util.Duration;
import java.util.ArrayList;

class PokerClientGamePlayScreen{

    HBox dealerCards;
    HBox playerCards;
    HBox chooseBetsBox;
    PokerClient pokerClient;
    Label balanceLabel;

    FlowPane flowPaneAnte;
    FlowPane flowPanePairPlus;
    FlowPane flowPanePlay;

    ArrayList<StackPane> anteStackPaneList;
    int anteTotalValue;
    int pairPlusTotalValue;
    int playerIdValue;

    BorderPane root;

    Label statusBar;

    Color n1 = Color.rgb(53, 118, 78);
    Color n2 = Color.rgb(27, 60, 40);

    Button clearButton;

    public PokerClientGamePlayScreen(PokerClient pokerClient) {
        this.pokerClient = pokerClient;
        anteStackPaneList = new ArrayList<>();
        anteTotalValue = 0;
        pairPlusTotalValue = 0;
        playerIdValue = 0;
    }

    private HBox createTopUI() {
        HBox topUI = new HBox();
        topUI.setAlignment(Pos.CENTER);
        topUI.setSpacing(50);

        balanceLabel = new Label("Balance: 1000");
        balanceLabel.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white; -fx-font-size: 18px; " +
                "-fx-border-color: black; -fx-border-width: 2px; -fx-border-style: solid; -fx-padding: 4px;");
        balanceLabel.setOnMouseEntered(event -> {
            balanceLabel.setScaleX(1.5);
            balanceLabel.setScaleY(1.5);
        });

        balanceLabel.setOnMouseExited(event -> {
            balanceLabel.setScaleX(1.0);
            balanceLabel.setScaleY(1.0);
        });

        Button optionsMenu = createButton("Options"); // Replace with a real options menu
        animateButton(optionsMenu);

        optionsMenu.setOnAction(e->{
            Stage optionsStage = new Stage();
            optionsStage.initModality(Modality.APPLICATION_MODAL);

            Button exitButton = createButton("Exit");
            exitButton.setOnAction(f -> {
                System.exit(0);
            });

            Button freshStartButton = createButton("Fresh Start");
            freshStartButton.setPrefWidth(120);
            freshStartButton.setOnAction(f -> {
                pokerClient.sendPokerInfo(playerIdValue, anteTotalValue, pairPlusTotalValue, anteTotalValue, "restart");
                //resetWinnings();
                optionsStage.hide();
            });

            Button newLookButton = createButton("NewLook");
            animateButton(exitButton);
            animateButton(freshStartButton);
            animateButton(newLookButton);

            VBox optionsLayout = new VBox(20, exitButton, freshStartButton, newLookButton);
            optionsLayout.setAlignment(Pos.CENTER);
            Scene optionsScene = new Scene(optionsLayout, 200, 250);
            optionsStage.setScene(optionsScene);
            optionsStage.setTitle("Options");
            optionsStage.show();

            //----------------------------
            Stop[] stops1 = new Stop[] {
                    new Stop(0, n1), // Dark green at center
                    new Stop(1, n2)   // Light green at edges
            };

            RadialGradient gradient1 = new RadialGradient(
                    0,                  // focusAngle
                    0.3,                // focusDistance
                    0.3,                // centerX
                    0.7,                // centerY
                    0,              // radius
                    true,               // proportional
                    CycleMethod.NO_CYCLE, // cycleMethod
                    stops1               // stops
            );

            // Set the radial gradient as the background of the pane
            optionsLayout.setBackground(
                    new javafx.scene.layout.Background(
                            new javafx.scene.layout.BackgroundFill(
                                    gradient1, null, null
                            )
                    )
            );

            newLookButton.setOnAction(f -> {
                statusBar.setTextFill(Color.BLACK);
                n1 = Color.LIGHTGREY;
                n2 = Color.DARKGREY;

                // Define the radial gradient colors and stops
                Stop[] stops = new Stop[] {
                        new Stop(0, n1), // Dark green at center
                        new Stop(1, n2)   // Light green at edges
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


                // Set the radial gradient as the background of the pane
                root.setBackground(
                        new javafx.scene.layout.Background(
                                new javafx.scene.layout.BackgroundFill(
                                        gradient, null, null
                                )
                        )
                );

                optionsStage.hide();
            });

        });

        dealerCards = new HBox(); // Add dealer's card images here
        dealerCards.setPrefSize(320, 150);

        topUI.getChildren().addAll(balanceLabel, dealerCards, optionsMenu);
        return topUI;
    }

    private VBox createMiddleUI() {
        VBox middleUI = new VBox();
        middleUI.setAlignment(Pos.CENTER);
        middleUI.setSpacing(20);

        // Create the Circle objects with a transparent fill and black border
        int RADIUS = 50;
        Circle anteCircle = new Circle(50);
        anteCircle.setFill(Color.TRANSPARENT);
        anteCircle.setStroke(Color.DARKGREY);

        Circle pairPlusCircle = new Circle(50);
        pairPlusCircle.setFill(Color.TRANSPARENT);
        pairPlusCircle.setStroke(Color.DARKGREY);

        Circle playCircle = new Circle(50);
        playCircle.setFill(Color.TRANSPARENT);
        playCircle.setStroke(Color.DARKGREY);


        // Set the CSS styles for the StackPane objects containing the Circle objects and Label objects
        Label anteLabel = new Label("Ante");
        anteLabel.setTextFill(Color.WHITE);
        anteLabel.setStyle("-fx-font-size: 18px;");
        StackPane anteSpot = new StackPane(anteCircle, anteLabel);
        anteSpot.setStyle("-fx-background-color: transparent");

        Label pairPlusLabel = new Label("Pair Plus");
        pairPlusLabel.setTextFill(Color.WHITE);
        pairPlusLabel.setStyle("-fx-font-size: 18px;");
        StackPane pairPlusSpot = new StackPane(pairPlusCircle, pairPlusLabel);
        pairPlusSpot.setStyle("-fx-background-color: transparent");

        Label playLabel = new Label("Play");
        playLabel.setTextFill(Color.WHITE);
        playLabel.setStyle("-fx-font-size: 18px;");
        StackPane playSpot = new StackPane(playCircle, playLabel);
        playSpot.setStyle("-fx-background-color: transparent");

        flowPaneAnte = new FlowPane(anteSpot);
        flowPaneAnte.setPadding(new Insets(10));
        flowPaneAnte.setHgap(10);
        flowPaneAnte.setVgap(10);
        flowPaneAnte.setPrefWrapLength(RADIUS);
        flowPaneAnte.setLayoutX(200 - RADIUS);
        flowPaneAnte.setLayoutY(200 - RADIUS);
        flowPaneAnte.setAlignment(Pos.CENTER);
        flowPaneAnte.setStyle("-fx-background-color: #2b2b2b;");
        flowPaneAnte.setOnMouseEntered(event -> {
            flowPaneAnte.setEffect(new Glow());
            flowPaneAnte.setStyle("-fx-background-color: #3f3f3f; -fx-text-fill: white; -fx-font-size: 18px;");
        });

        flowPaneAnte.setOnMouseExited(event -> {
            flowPaneAnte.setEffect(null);
            flowPaneAnte.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white; -fx-font-size: 18px;");
        });

        flowPanePairPlus = new FlowPane(pairPlusSpot);
        flowPanePairPlus.setPadding(new Insets(10));
        flowPanePairPlus.setHgap(10);
        flowPanePairPlus.setVgap(10);
        flowPanePairPlus.setPrefWrapLength(RADIUS);
        flowPanePairPlus.setLayoutX(200 - RADIUS);
        flowPanePairPlus.setLayoutY(200 - RADIUS);
        flowPanePairPlus.setAlignment(Pos.CENTER);
        flowPanePairPlus.setStyle("-fx-background-color: #2b2b2b;");
        flowPanePairPlus.setOnMouseEntered(event -> {
            flowPanePairPlus.setEffect(new Glow());
            flowPanePairPlus.setStyle("-fx-background-color: #3f3f3f; -fx-text-fill: white; -fx-font-size: 18px;");
        });

        flowPanePairPlus.setOnMouseExited(event -> {
            flowPanePairPlus.setEffect(null);
            flowPanePairPlus.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white; -fx-font-size: 18px;");
        });



        flowPanePlay = new FlowPane(playSpot);
        flowPanePlay.setPadding(new Insets(10));
        flowPanePlay.setHgap(10);
        flowPanePlay.setVgap(10);
        flowPanePlay.setPrefWrapLength(RADIUS);
        flowPanePlay.setLayoutX(200 - RADIUS);
        flowPanePlay.setLayoutY(200 - RADIUS);
        flowPanePlay.setAlignment(Pos.CENTER);
        flowPanePlay.setStyle("-fx-background-color: #2b2b2b;");
        flowPanePlay.setOnMouseEntered(event -> {
            flowPanePlay.setEffect(new Glow());
            flowPanePlay.setStyle("-fx-background-color: #3f3f3f; -fx-text-fill: white; -fx-font-size: 18px;");
        });

        flowPanePlay.setOnMouseExited(event -> {
            flowPanePlay.setEffect(null);
            flowPanePlay.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white; -fx-font-size: 18px;");
        });


        middleUI.getChildren().addAll(flowPaneAnte, flowPanePairPlus, flowPanePlay);
        return middleUI;
    }

    private Circle createChip(Color color, double radius) {
        Circle circle = new Circle(radius-60);
        circle.setFill(color);
        circle.setStroke(Color.WHITE);
        return circle;
    }

    private Text createText(String text, double offsetX, Color color, double size) {
        Text textNode = new Text(text);
        textNode.setFill(color);
        textNode.setFont(new Font(size-10));
        //textNode.setTranslateX(offsetX-55);
        return textNode;
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white; -fx-font-size: 18px;");
        button.setPrefSize(100, 50);
        button.setOnMouseEntered(event -> {
            button.setEffect(new Glow());
            button.setStyle("-fx-background-color: #3f3f3f; -fx-text-fill: white; -fx-font-size: 18px;");
        });

        button.setOnMouseExited(event -> {
            button.setEffect(null);
            button.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white; -fx-font-size: 18px;");
        });

        return button;
    }

    private void animateButton(Button button) {
        // Create a Timeline for the button press animation
        Timeline pressAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(button.scaleXProperty(), 1.0), new KeyValue(button.scaleYProperty(), 1.0)),
                new KeyFrame(Duration.millis(100), new KeyValue(button.scaleXProperty(), 0.9), new KeyValue(button.scaleYProperty(), 0.9))
        );

        // Create a Timeline for the button release animation
        Timeline releaseAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(button.scaleXProperty(), 0.9), new KeyValue(button.scaleYProperty(), 0.9)),
                new KeyFrame(Duration.millis(100), new KeyValue(button.scaleXProperty(), 1.0), new KeyValue(button.scaleYProperty(), 1.0))
        );

        // Set the event handlers for button press and release
        button.setOnMousePressed(event -> pressAnimation.play());
        button.setOnMouseReleased(event -> releaseAnimation.play());
    }

    private void setDraggable(StackPane stackPane) {
        stackPane.setOnDragDetected(event -> {
            Dragboard db = stackPane.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString("stackPane");
            db.setContent(content);
            event.consume();
        });

        setOnDragOver(flowPaneAnte);
        setOnDragOver(flowPanePairPlus);

        setOnDragDropped(flowPaneAnte, 1);
        setOnDragDropped(flowPanePairPlus, 2);
    }

    private void setOnDragOver(FlowPane flowPane) {
        flowPane.setOnDragOver(event -> {
            if (event.getGestureSource() != flowPane && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
    }

    private void setOnDragDropped(FlowPane flowPane, int val) {
        flowPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                StackPane clonedStackPane = cloneStackPane((StackPane) event.getGestureSource(), val);
                flowPane.getChildren().add(clonedStackPane);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private StackPane cloneStackPane(StackPane original, int val) {
        StackPane clonedStackPane = new StackPane();
        Text originalText = new Text();
        Circle originalCircle = new Circle();

        // Clone children
        for (Node child : original.getChildren()) {
            if (child instanceof Circle) {
                originalCircle = (Circle) child;
                Circle clonedCircle = new Circle(originalCircle.getRadius(), originalCircle.getFill());
                clonedStackPane.getChildren().add(clonedCircle);
            } else if (child instanceof Text) {
                originalText = (Text) child;
                Text clonedText = new Text(originalText.getText());
                clonedText.setFont(originalText.getFont());
                clonedText.setFill(originalText.getFill());
                clonedStackPane.getChildren().add(clonedText);
            }
        }

        if(val == 1) {
            Circle chip2 = new Circle(originalCircle.getRadius());
            chip2.setFill(originalCircle.getFill());
            Text text2 = new Text(originalText.getText());
            text2.setFont(originalText.getFont());
            text2.setFill(originalText.getFill());
            StackPane stackPane2 = new StackPane(chip2, text2);
            stackPane2.setAlignment(Pos.CENTER);
            anteTotalValue = anteTotalValue + Integer.parseInt(originalText.getText());
            anteStackPaneList.add(stackPane2);
        }
        else
            pairPlusTotalValue = pairPlusTotalValue + Integer.parseInt(originalText.getText());

        clonedStackPane.setPrefSize(original.getPrefWidth(), original.getPrefHeight());
        clonedStackPane.setStyle(original.getStyle());
        return clonedStackPane;
    }

    private HBox createBottomUI() {

        chooseBetsBox =  new HBox();
        chooseBetsBox.setPrefSize(150, 150);
        //chooseBetsBox.setStyle("-fx-border-width: 1px; -fx-border-color: black; -fx-background-radius: 10;");


        Circle chip1 = createChip(Color.WHITE, 70);
        Text text1 = createText("2", 48, Color.BLACK, 30);
        StackPane stackPane1 = new StackPane(chip1, text1);
        stackPane1.setAlignment(Pos.CENTER);

        Circle chip2 = createChip(Color.WHITE, 75);
        Text text2 = createText("5", 56, Color.BLACK, 34);
        StackPane stackPane2 = new StackPane(chip2, text2);
        stackPane2.setAlignment(Pos.CENTER);

        Circle chip3 = createChip(Color.BLUE, 80);
        Text text3 = createText("10", 64, Color.WHITE, 36);
        StackPane stackPane3 = new StackPane(chip3, text3);
        stackPane3.setAlignment(Pos.CENTER);

        Circle chip4 = createChip(Color.GREEN, 85);
        Text text4 = createText("15", 64, Color.WHITE, 36);
        StackPane stackPane4 = new StackPane(chip4, text4);
        stackPane4.setAlignment(Pos.CENTER);

        Circle chip5 = createChip(Color.RED, 90);
        Text text5 = createText("20", 64, Color.WHITE, 36);
        StackPane stackPane5 = new StackPane(chip5, text5);
        stackPane5.setAlignment(Pos.CENTER);

        Circle chip6 = createChip(Color.BLACK, 95);
        Text text6 = createText("25", 64, Color.WHITE, 36);
        StackPane stackPane6 = new StackPane(chip6, text6);
        stackPane6.setAlignment(Pos.CENTER);

        setDraggable(stackPane1);
        setDraggable(stackPane2);
        setDraggable(stackPane3);
        setDraggable(stackPane4);
        setDraggable(stackPane5);
        setDraggable(stackPane6);

        FlowPane flowPane = new FlowPane(stackPane1, stackPane2, stackPane3,
                stackPane4, stackPane5, stackPane6);
        flowPane.setHgap(5);
        flowPane.setVgap(5);
        Label cBetsBox = new Label("Choose Bet Amount: (Drag And Drop)");
        cBetsBox.setFont(new Font(12));
        cBetsBox.setTextFill(Color.WHITE);
        chooseBetsBox.getChildren().add(flowPane);
        VBox VchooseBetsBox = new VBox(cBetsBox, chooseBetsBox);


        playerCards = new HBox(); // Add player's card images here
        playerCards.setPrefSize(320, 150);
        playerCards.setMargin(playerCards, new Insets(0, 50, 0, 0));
        //playerCards.setStyle("-fx-border-width: 1px; -fx-border-color: black; -fx-background-radius: 10;");

        Button foldButton = createButton("Fold");
        foldButton.setPrefSize(80, 40);
        // Add drop shadow effect
        DropShadow dropShadow = new DropShadow(10, Color.GRAY);
        foldButton.setEffect(dropShadow);

        // Create the play button
        Button playButton = createButton("Play");
        playButton.setPrefSize(80, 40);
        // Add drop shadow effect
        playButton.setEffect(dropShadow);

        // Create the deal button
        Button dealButton = createButton("Deal");
        dealButton.setPrefSize(80, 40);
        // Add drop shadow effect
        dealButton.setEffect(dropShadow);

        // Create the deal button
        clearButton = createButton("Clear");
        clearButton.setPrefSize(80, 40);
        // Add drop shadow effect
        clearButton.setEffect(dropShadow);

        // animate the buttons
        animateButton(foldButton);
        animateButton(playButton);
        animateButton(dealButton);
        animateButton(clearButton);

        // Create a VBox to hold the buttons
        VBox btnSet1 = new VBox(20, clearButton, foldButton);
        btnSet1.setAlignment(Pos.CENTER);
        VBox btnSet2 = new VBox(20, playButton, dealButton);
        btnSet2.setAlignment(Pos.CENTER);
        HBox actionButtonsBox = new HBox(20, btnSet1, btnSet2);
        actionButtonsBox.setAlignment(Pos.CENTER);

        // Button actions
        foldButton.setDisable(true);
        playButton.setDisable(true);

        clearButton.setOnAction(e->{
            anteTotalValue = 0;
            pairPlusTotalValue = 0;

            // Clear Flowpane Items
            // Play FLowpane
            flowPanePlay.getChildren().clear();
            Circle playCircle = new Circle(50);
            playCircle.setFill(Color.TRANSPARENT);
            playCircle.setStroke(Color.DARKGREY);

            Label playLabel = new Label("Play");
            playLabel.setTextFill(Color.WHITE);
            playLabel.setStyle("-fx-font-size: 18px;");
            StackPane playSpot = new StackPane(playCircle, playLabel);
            playSpot.setStyle("-fx-background-color: transparent");
            flowPanePlay.getChildren().add(playSpot);

            // Ante Flowpane
            flowPaneAnte.getChildren().clear();
            Circle anteCircle = new Circle(50);
            anteCircle.setFill(Color.TRANSPARENT);
            anteCircle.setStroke(Color.DARKGREY);

            Label anteLabel = new Label("Ante");
            anteLabel.setTextFill(Color.WHITE);
            anteLabel.setStyle("-fx-font-size: 18px;");
            StackPane anteSpot = new StackPane(anteCircle, anteLabel);
            anteSpot.setStyle("-fx-background-color: transparent");
            flowPaneAnte.getChildren().add(anteSpot);

            anteStackPaneList.clear();

            // PairPlus Flowpane
            flowPanePairPlus.getChildren().clear();
            Circle pairPlusCircle = new Circle(50);
            pairPlusCircle.setFill(Color.TRANSPARENT);
            pairPlusCircle.setStroke(Color.DARKGREY);

            Label pairPlusLabel = new Label("Pair Plus");
            pairPlusLabel.setTextFill(Color.WHITE);
            pairPlusLabel.setStyle("-fx-font-size: 18px;");
            StackPane pairPlusSpot = new StackPane(pairPlusCircle, pairPlusLabel);
            pairPlusSpot.setStyle("-fx-background-color: transparent");
            flowPanePairPlus.getChildren().add(pairPlusSpot);

        });

        dealButton.setOnAction(e->{
            if(anteStackPaneList.isEmpty()){
                statusBar.setText("Status: Ante amount not selected!");
                return;
            }
            pokerClient.sendPokerInfo(playerIdValue, anteTotalValue, pairPlusTotalValue, anteTotalValue, "deal");
            dealButton.setDisable(true);
            playButton.setDisable(false);
            foldButton.setDisable(false);
            clearButton.setDisable(true);
            flowPanePlay.getChildren().clear();
            Circle playCircle = new Circle(50);
            playCircle.setFill(Color.TRANSPARENT);
            playCircle.setStroke(Color.DARKGREY);

            Label playLabel = new Label("Play");
            playLabel.setTextFill(Color.WHITE);
            playLabel.setStyle("-fx-font-size: 18px;");
            StackPane anteSpot = new StackPane(playCircle, playLabel);
            anteSpot.setStyle("-fx-background-color: transparent");
            flowPanePlay.getChildren().add(anteSpot);
        });

        playButton.setOnAction(e->{
            if(!anteStackPaneList.isEmpty()){
                flowPanePlay.getChildren().clear();
                Circle playCircle = new Circle(50);
                playCircle.setFill(Color.TRANSPARENT);
                playCircle.setStroke(Color.DARKGREY);

                Label playLabel = new Label("Play");
                playLabel.setTextFill(Color.WHITE);
                playLabel.setStyle("-fx-font-size: 18px;");
                StackPane anteSpot = new StackPane(playCircle, playLabel);
                anteSpot.setStyle("-fx-background-color: transparent");
                flowPanePlay.getChildren().add(anteSpot);
                for(StackPane stackPane : anteStackPaneList){
                    flowPanePlay.getChildren().add(stackPane);
                }
            }

            pokerClient.sendPokerInfo(playerIdValue, anteTotalValue, pairPlusTotalValue, anteTotalValue, "play");
            dealButton.setDisable(false);
            clearButton.setDisable(false);
            playButton.setDisable(true);
            foldButton.setDisable(true);
        });

        foldButton.setOnAction(e->{
            pokerClient.sendPokerInfo(playerIdValue, anteTotalValue, pairPlusTotalValue, anteTotalValue, "fold");
            dealButton.setDisable(false);
            clearButton.setDisable(false);
            playButton.setDisable(true);
            foldButton.setDisable(true);
        });

        statusBar = new Label("Status: Game has started");
        statusBar.setTextFill(Color.WHITE);
        VBox playerCardsSection = new VBox(5, playerCards, statusBar);
        playerCardsSection.setAlignment(Pos.CENTER);
        HBox bottomUI = new HBox(VchooseBetsBox, playerCardsSection, actionButtonsBox);
        bottomUI.setSpacing(30);
        bottomUI.setAlignment(Pos.CENTER);

        return bottomUI;
    }

    public void setDisplay(Stage primaryStage){
        root = new BorderPane();

        root.setTop(createTopUI());
        root.setCenter(createMiddleUI());
        root.setBottom(createBottomUI());

        // Define the radial gradient colors and stops
        Stop[] stops = new Stop[] {
                new Stop(0, n1), // Dark green at center
                new Stop(1, n2)   // Light green at edges
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

        // Set the radial gradient as the background of the pane
        root.setBackground(
                new javafx.scene.layout.Background(
                        new javafx.scene.layout.BackgroundFill(
                                gradient, null, null
                        )
                )
        );

        Scene scene = new Scene(root, 900, 750);
        primaryStage.setTitle("3 Cards Poker");
        ArrayList<Card> dummyHand = new ArrayList<>();
        setHand(dummyHand, dealerCards);
        setHand(dummyHand, playerCards);

        // center the stage on the screen
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((screenBounds.getWidth() - scene.getWidth()) / 2);
        primaryStage.setY((screenBounds.getHeight() - scene.getHeight()) / 2);
        primaryStage.setScene(scene);
        primaryStage.show();

        pokerClient.addPokerInfoListener(new PokerInfoListener() {
            @Override
            public void onPokerInfoReceived(PokerInfo pokerInfo) {
                Platform.runLater(() -> {

                    System.out.println("Received new poker info: " + pokerInfo);
                    ArrayList<Card> dealerHand = pokerInfo.getDealerHand();
                    setHand(dealerHand, dealerCards);

                    ArrayList<Card> playerHand = pokerInfo.getPlayerHand();
                    setHand(playerHand, playerCards);

                    balanceLabel.setText("Balance: " + Integer.toString(pokerInfo.getTotalWinnings()));
                    statusBar.setText("Status: " + pokerInfo.getGameInfo());

                    if (pokerInfo.getGameInfo().contains("lost") || pokerInfo.getGameInfo().contains("won")){
                        Stage gameEndStage = new Stage();
                        gameEndStage.initModality(Modality.APPLICATION_MODAL);

                        Button playAgainButton = createButton("Play Again?");
                        playAgainButton.setPrefWidth(120);
                        animateButton(playAgainButton);

                        playAgainButton.setOnAction(e->{
                            setHand(dummyHand, dealerCards);
                            setHand(dummyHand, playerCards);
                            statusBar.setText("Status: New Game");
                            clearButton.fire();
                            gameEndStage.hide();
                        });

                        Label gameEndLabel = new Label(pokerInfo.getGameInfo());
                        gameEndLabel.setFont(new Font(18));
                        gameEndLabel.setTextFill(Color.WHITE);

                        VBox gameEndBox = new VBox(10, gameEndLabel, playAgainButton);

                        gameEndBox.setAlignment(Pos.CENTER);
                        Scene gamEndScene = new Scene(gameEndBox, 400, 250);
                        gameEndStage.setScene(gamEndScene);
                        gameEndStage.setTitle("3 Cards Poker Game Results");
                        gameEndStage.show();

                        //----------------------------
                        Stop[] stops1 = new Stop[] {
                                new Stop(0, n1),
                                new Stop(1, n2)
                        };

                        RadialGradient gradient1 = new RadialGradient(
                                0,                  // focusAngle
                                0.3,                // focusDistance
                                0.3,                // centerX
                                0.7,                // centerY
                                0,              // radius
                                true,               // proportional
                                CycleMethod.NO_CYCLE, // cycleMethod
                                stops1               // stops
                        );

                        // Set the radial gradient as the background of the pane
                        gameEndBox.setBackground(
                                new javafx.scene.layout.Background(
                                        new javafx.scene.layout.BackgroundFill(
                                                gradient1, null, null
                                        )
                                )
                        );
                    }
                });
            }
        });
    }

    private void setHand(ArrayList<Card> playerHand, HBox playerCards) {
        Card c1;
        Card c2;
        Card c3;
        if(playerHand.isEmpty()){
            c1 = new Card("/back_dark.png");
            c2 = new Card("/back_dark.png");
            c3 = new Card("/back_dark.png");
            ImageView cView1 = new ImageView(c1.getImage());
            ImageView cView2 = new ImageView(c2.getImage());
            ImageView cView3 = new ImageView(c3.getImage());
            cView1.setFitWidth(100);
            cView1.setFitHeight(150);
            cView2.setFitWidth(100);
            cView2.setFitHeight(150);
            cView3.setFitWidth(100);
            cView3.setFitHeight(150);
            playerCards.getChildren().clear();
            playerCards.getChildren().addAll(cView1, cView2, cView3);
        }
        else {
            c1 = playerHand.get(0);
            c2 = playerHand.get(1);
            c3 = playerHand.get(2);
            ImageView cView1 = new ImageView(c1.getImage());
            ImageView cView2 = new ImageView(c2.getImage());
            ImageView cView3 = new ImageView(c3.getImage());
            cView1.setFitWidth(100);
            cView1.setFitHeight(150);
            cView2.setFitWidth(100);
            cView2.setFitHeight(150);
            cView3.setFitWidth(100);
            cView3.setFitHeight(150);
            playerCards.getChildren().clear();
            playerCards.getChildren().addAll(cView1, cView2, cView3);

        }
        playerCards.setAlignment(Pos.CENTER);
        playerCards.setSpacing(10);
    }


}
