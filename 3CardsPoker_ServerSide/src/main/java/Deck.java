import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Deck {
    private List<Card> cards;
    public Deck() throws FileNotFoundException {
        cards = new ArrayList<Card>();

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                String imagePath = "/" + suit.toString().toLowerCase() + "_" + rank.toString() + ".png";
                Card card = new Card(suit, rank, suit.toString(), rank.toString(), imagePath);
                cards.add(card);
            }
        }
    }

    public void shuffle() {

        Collections.shuffle(cards);
    }

    public Card dealCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(cards.size() - 1);
    }
}