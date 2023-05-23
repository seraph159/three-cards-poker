import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

class Game {
    private ArrayList<Card> handDealer;
    private ArrayList<Card> handPlayer;
    private int anteWager;
    private int pairPlusWager;
    private int playWager;
    private int totalWin;

    private AtomicInteger playerId;

    private ObservableList<String> gameInfoList;

    public Game(AtomicInteger playerId, ObservableList<String> gameInfoList) throws FileNotFoundException {
        handDealer = new ArrayList<>();
        handPlayer = new ArrayList<>();
        totalWin = 1000;
        this.playerId = playerId;
        this.gameInfoList = gameInfoList;
    }

    public void updatePlayerDealState(PokerInfo pokerInfo) throws FileNotFoundException {

        Platform.runLater(() -> {
            gameInfoList.add("Client " + playerId + " playing another hand: " + true);
        });
        Deck deck = new Deck();
        deck.shuffle();
        ArrayList<Card> hD = new ArrayList<>();
        ArrayList<Card> hP = new ArrayList<>();

        hD.add(deck.dealCard());
        hD.add(deck.dealCard());
        hD.add(deck.dealCard());
        hP.add(deck.dealCard());
        hP.add(deck.dealCard());
        hP.add(deck.dealCard());
        handDealer = hD;
        handPlayer = hP;

        // Get the bet values
        anteWager = pokerInfo.getAnteWager();
        pairPlusWager = pokerInfo.getPairPlusWager();
        playWager = pokerInfo.getPlayWager();

        // Set the cards facing up or down
        pokerInfo.setShowDealerHand(false);
        pokerInfo.setShowPlayerHand(true);

        // Set the hands after shuffling for the dealer and player
        pokerInfo.setDealerHand(new ArrayList<Card>());
        pokerInfo.setPlayerHand(handPlayer);
        pokerInfo.setTotalWinnings(totalWin);

    }

    public void updatePlayerPlayState(PokerInfo pokerInfo){
        anteWager = pokerInfo.getAnteWager();
        pairPlusWager = pokerInfo.getPairPlusWager();
        playWager = pokerInfo.getPlayWager();
        pokerInfo.setShowDealerHand(true);
        pokerInfo.setShowPlayerHand(true);
        pokerInfo.setDealerHand(handDealer);
        pokerInfo.setPlayerHand(handPlayer);

        int ppWin = 0;
        int ppLose = 0;
        // Check if pair, then do calculation and add value, else decrement
        if(ThreeCardLogic.isPair(handPlayer)){
            System.out.println("isPair");
            //pokerInfo.setTotalWinnings(pokerInfo.getTotalWinnings() + ThreeCardLogic.evalPPWinnings(handPlayer, pairPlusWager));
            totalWin = totalWin + ThreeCardLogic.evalPPWinnings(handPlayer, pairPlusWager);
            pokerInfo.setTotalWinnings(totalWin);
            ppWin = ThreeCardLogic.evalPPWinnings(handPlayer, pairPlusWager);
            System.out.println(ppWin);
            int finalPpWin1 = ppWin;
            Platform.runLater(() -> {
                gameInfoList.add("Client " + playerId + " pairPlus win: " + "won");
                gameInfoList.add("Client " + playerId + " pairPlus won amount: $" + finalPpWin1);
            });
        } else {
            System.out.println("isNOTPair");

            pokerInfo.setTotalWinnings(pokerInfo.getTotalWinnings() - pairPlusWager);
            totalWin = totalWin - pairPlusWager;
            ppLose = pairPlusWager;
            int finalPpLose = ppLose;
            Platform.runLater(() -> {
                gameInfoList.add("Client " + playerId + " pairPlus win: " + "lost");
                gameInfoList.add("Client " + playerId + " pairPlus lost amount: $" + finalPpLose);
            });
        }

        if(ThreeCardLogic.compareHands(handDealer, handPlayer) == 2) {
            // player won
            totalWin = totalWin + (anteWager * 2) + (playWager * 2);
            int winAmount = (anteWager * 2) + (playWager * 2) + ppWin - ppLose;
            pokerInfo.setTotalWinnings(totalWin);
            pokerInfo.setGameInfo("You won the game! The amount won: $" + winAmount);
            Platform.runLater(() -> {
                gameInfoList.add("Client " + playerId + " won/lost: " + "won");
                gameInfoList.add("Client " + playerId + " won amount: $" + winAmount);
            });
        } else {
            // dealer won
            totalWin = totalWin - anteWager - playWager;
            int lostAmount = anteWager + playWager + ppLose - ppWin;
            pokerInfo.setTotalWinnings(totalWin);
            pokerInfo.setGameInfo("You lost the game! The amount lost: $" + lostAmount);
            anteWager = 0;
            playWager = 0;
            Platform.runLater(() -> {
                gameInfoList.add("Client " + playerId + " won/lost: " + "lost");
                gameInfoList.add("Client " + playerId + " lost amount: $" + lostAmount);
            });
        }

    }

    // If Fold, the Player loses Ante and Pair (if opted) to the Dealer. The dealer cards are face up.
    public void updatePlayerFoldState(PokerInfo pokerInfo){

        pokerInfo.setShowDealerHand(true);
        pokerInfo.setShowPlayerHand(true);
        totalWin = totalWin - anteWager - pairPlusWager;
        int foldLoss = anteWager + pairPlusWager;
        pokerInfo.setGameInfo("You folded the game! The amount lost: $" + foldLoss);
        pokerInfo.setTotalWinnings(totalWin);
        Platform.runLater(() -> {
            gameInfoList.add("Client " + playerId + " folded. The lost amount: $" + foldLoss);
        });
    }

    public void setTotalWin(PokerInfo pokerInfo, int totalWin) {
        this.totalWin = totalWin;
        pokerInfo.setTotalWinnings(totalWin);
    }

    // Other methods for game logic...
}