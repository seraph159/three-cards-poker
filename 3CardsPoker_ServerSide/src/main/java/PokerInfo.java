import java.io.Serializable;
import java.util.ArrayList;

class PokerInfo implements Serializable {
    private int playerId;
    private int anteWager;
    private int pairPlusWager;
    private int playWager;
    private String gameInfo;
    private int totalWinnings;
    private ArrayList<Card> dealerHand;
    private ArrayList<Card> playerHand;
    private boolean showDealerHand;
    private boolean showPlayerHand;

    public PokerInfo(int playerId, int anteWager, int pairPlusWager, int playWager, String gameInfo) {
        this.playerId = playerId;
        this.anteWager = anteWager;
        this.pairPlusWager = pairPlusWager;
        this.playWager = playWager;
        this.dealerHand = new ArrayList<>();
        this.playerHand = new ArrayList<>();
        this.gameInfo = gameInfo;
    }


    // Getters and setters for all fields
    public int getPlayerId(){
        return this.playerId;
    }

    public String getGameInfo() {
        return gameInfo;
    }

    public int getAnteWager() {
        return anteWager;
    }

    public int getPairPlusWager() {
        return pairPlusWager;
    }

    public int getPlayWager() {
        return playWager;
    }

    public int getTotalWinnings() {
        return totalWinnings;
    }

    public void setAnteWager(int anteWager) {
        this.anteWager = anteWager;
    }

    public void setGameInfo(String gameInfo) {
        this.gameInfo = gameInfo;
    }


    public void setPairPlusWager(int pairPlusWager) {
        this.pairPlusWager = pairPlusWager;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setPlayWager(int playWager) {
        this.playWager = playWager;
    }

    public void setTotalWinnings(int totalWinnings) {
        this.totalWinnings = totalWinnings;
    }

    public boolean getShowDealerHand(){
        return this.showDealerHand;
    }
    public void setShowDealerHand(boolean showDealerHand) {
        this.showDealerHand = showDealerHand;
    }

    public boolean getShowPlayerHand(){
        return this.showPlayerHand;
    }
    public void setShowPlayerHand(boolean showPlayerHand) {
        this.showPlayerHand = showPlayerHand;
    }

    public ArrayList<Card> getDealerHand() {
        return this.dealerHand;
    }

    public void setDealerHand(ArrayList<Card> dealerHand) {
        this.dealerHand = dealerHand;
    }

    public ArrayList<Card> getPlayerHand() {
        return this.playerHand;
    }

    public void setPlayerHand(ArrayList<Card> playerHand) {
        this.playerHand = playerHand;
    }

    @Override
    public String toString() {
        return "PokerInfo{" +
                "playerId=" + playerId +
                ", anteWager=" + anteWager +
                ", pairPlusWager=" + pairPlusWager +
                ", playWager=" + playWager +
                ", gameInfo='" + gameInfo + '\'' +
                ", totalWinnings=" + totalWinnings +
                '}';
    }
}
