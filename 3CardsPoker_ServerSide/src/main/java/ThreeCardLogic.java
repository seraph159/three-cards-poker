import java.util.ArrayList;

public class ThreeCardLogic {

    // Three cards of the same suit in sequence. For example, 10 of Hearts, Jack of Hearts, and Queen of Hearts.
    public static boolean isStraightFlush(ArrayList<Card> hand){
        if(isStraight(hand) && isFlush(hand)){
            return true;
        } else {
            return false;
        }
    }

    // Three cards of the same rank. For example, three Aces.
    public static boolean isThreeOfAKind(ArrayList<Card> hand){
        if((hand.get(0).getRank() == hand.get(1).getRank()) && (hand.get(0).getRank() == hand.get(1).getRank())
                && (hand.get(0).getRank() == hand.get(2).getRank()))
            return true;
        else
            return false;
    }

    // Three cards of any suit in sequence. For example, 3 of Hearts, 4 of Spades, and 5 of Diamonds.
    public static boolean isStraight(ArrayList<Card> hand){

        for (int i = 1; i < hand.size(); i++) {
            if (hand.get(i).getRank().ordinal() != hand.get(i-1).getRank().ordinal() + 1) {
                // Not ascending
                return false;
            }
        }
        return true;

    }

    // Three cards of the same suit. For example, Ace, 8, and 5 of Clubs.
    public static boolean isFlush(ArrayList<Card> hand){
        if((hand.get(0).getSuit() == hand.get(1).getSuit()) && (hand.get(0).getSuit() == hand.get(1).getSuit())
                && (hand.get(0).getSuit() == hand.get(2).getSuit()))
            return true;
        else
            return false;
    }

    // Two cards of the same rank. For example, two Kings
    public static boolean isPair(ArrayList<Card> hand){
        if((hand.get(0).getRank() == hand.get(1).getRank())|| (hand.get(1).getRank() == hand.get(2).getRank())
                || (hand.get(2).getRank() == hand.get(0).getRank()))
            return true;
        else
            return false;
    }

    // To find the high ranking card if the hand doesn't fall under the greater ranks
    // For example, if you have Q-8-5 of different suits, your hand would be a Queen-high hand
    public static int isHighCard(ArrayList<Card> hand) {
        int maxRank = 0;
        for (Card card : hand) {
            if (card.getRank().ordinal() > maxRank) {
                maxRank = card.getRank().ordinal();
            }
        }
        return maxRank;
    }
    public static int evalHand(ArrayList<Card> hand) {

        PokerUtils.orderAscendingCards(hand);
        // Evaluate the hand cards ranking
        if(isStraightFlush(hand)){
            return 500;
        } else if(isThreeOfAKind(hand)){
            return 400;
        }else if(isStraight(hand)){
            return 300;
        } else if(isFlush(hand)){
            return 200;
        } else if(isPair(hand)){
            return 100;
        } else{
            return isHighCard(hand);
        }
    }
    public static int evalPPWinnings(ArrayList<Card> hand, int bet){
        int handRanking = evalHand(hand);
        switch (handRanking) {
            case 500: // Straight Flush
                return bet * 40; // Payout 40 to 1 for straight flush
            case 400: // Three of a kind
                return bet * 30; // Payout 30 to 1 for three of a kind
            case 300: // Straight
                return bet * 6; // Payout 6 to 1 for straight
            case 200: // Flush
                return bet * 3; // Payout 3 to 1 for flush
            case 100: // Pair
                return bet * 1; // Payout 1 to 1 for pair
            default: // No payout
                return 0;
        }

    }
    public static int compareHands(ArrayList<Card> dealerHand, ArrayList<Card> playerHand) {
        // Compare dealer and player hands, return result
        if(evalHand(dealerHand) > evalHand(playerHand)){
            return 1;
        } else if (evalHand(playerHand) > evalHand(dealerHand)){
            return 2;
        } else {
            return -1;
        }
    }

}
