import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PokerUtils {

    public static void orderAscendingCards(ArrayList<Card> hand) {
        Collections.sort(hand, new Comparator<Card>() {
            public int compare(Card card1, Card card2) {
                return card1.getRank().compareTo(card2.getRank());
            }
        });
    }

}