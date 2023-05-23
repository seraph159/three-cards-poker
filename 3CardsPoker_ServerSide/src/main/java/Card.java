import javafx.scene.image.Image;
import java.io.Serializable;

enum Suit {
    CLUBS, DIAMONDS, HEARTS, SPADES
}

enum Rank {
    TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"), TEN("10"), JACK("J"), QUEEN("Q"), KING("K"), ACE("A");

    private final String value;

    private Rank(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

class Card implements Serializable {
    private Suit suit;
    private Rank rank;

    private String suitValue;
    private String rankValue;
    private String imagePath;

    private Image imageObject;

    public Card(String imagePath){
        this.imagePath = imagePath;
    }

    public Card(Suit suit, Rank rank, String suitValue, String rankValue, String imagePath) {
        this.suit = suit;
        this.rank = rank;
        this.suitValue = suitValue;
        this.rankValue = rankValue;
        this.imagePath = imagePath;
    }

    // Getters and setters
    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Image getImage(){
        return imageObject = new Image(imagePath);
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}