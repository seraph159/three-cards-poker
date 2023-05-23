import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.FileNotFoundException;
import java.util.ArrayList;

class MyTest {

	@Test
	public void testIsStraightFlush() throws FileNotFoundException {
		ArrayList<Card> hand1 = new ArrayList<>();
		hand1.add(new Card(Suit.HEARTS, Rank.QUEEN, Suit.HEARTS.toString(), Rank.QUEEN.toString(), "/"));
		hand1.add(new Card(Suit.HEARTS, Rank.KING, Suit.HEARTS.toString(), Rank.KING.toString(), "/"));
		hand1.add(new Card(Suit.HEARTS, Rank.ACE, Suit.HEARTS.toString(), Rank.ACE.toString(), "/"));
		assertTrue(ThreeCardLogic.isStraightFlush(hand1));

		ArrayList<Card> hand2 = new ArrayList<>();
		hand2.add(new Card(Suit.CLUBS, Rank.TEN, Suit.CLUBS.toString(), Rank.TEN.toString(), "/"));
		hand2.add(new Card(Suit.CLUBS, Rank.JACK, Suit.CLUBS.toString(), Rank.JACK.toString(),"/"));
		hand2.add(new Card(Suit.CLUBS, Rank.SEVEN, Suit.CLUBS.toString(), Rank.SEVEN.toString(), "/"));
		assertFalse(ThreeCardLogic.isStraightFlush(hand2));
	}

	@Test
	public void testIsStraight() throws FileNotFoundException {
		ArrayList<Card> hand1 = new ArrayList<>();
		hand1.add(new Card(Suit.HEARTS, Rank.QUEEN, Suit.HEARTS.toString(), Rank.QUEEN.toString(), "."));
		hand1.add(new Card(Suit.CLUBS, Rank.KING, Suit.CLUBS.toString(), Rank.KING.toString(), "."));
		hand1.add(new Card(Suit.DIAMONDS, Rank.ACE, Suit.DIAMONDS.toString(), Rank.ACE.toString(), "."));
		assertTrue(ThreeCardLogic.isStraight(hand1));

		ArrayList<Card> hand2 = new ArrayList<>();
		hand2.add(new Card(Suit.CLUBS, Rank.TEN, Suit.CLUBS.toString(), Rank.TEN.toString(), "."));
		hand2.add(new Card(Suit.CLUBS, Rank.JACK, Suit.CLUBS.toString(), Rank.JACK.toString(),"."));
		hand2.add(new Card(Suit.CLUBS, Rank.SEVEN, Suit.CLUBS.toString(), Rank.SEVEN.toString(), "."));
		assertFalse(ThreeCardLogic.isStraight(hand2));
	}

	@Test
	public void testIsFlush() throws FileNotFoundException {
		ArrayList<Card> hand1 = new ArrayList<>();
		hand1.add(new Card(Suit.HEARTS, Rank.QUEEN, Suit.HEARTS.toString(), Rank.QUEEN.toString(), "."));
		hand1.add(new Card(Suit.CLUBS, Rank.KING, Suit.CLUBS.toString(), Rank.KING.toString(), "."));
		hand1.add(new Card(Suit.DIAMONDS, Rank.ACE, Suit.DIAMONDS.toString(), Rank.ACE.toString(), "."));
		assertFalse(ThreeCardLogic.isFlush(hand1));

		ArrayList<Card> hand2 = new ArrayList<>();
		hand2.add(new Card(Suit.CLUBS, Rank.TEN, Suit.CLUBS.toString(), Rank.TEN.toString(), "."));
		hand2.add(new Card(Suit.CLUBS, Rank.JACK, Suit.CLUBS.toString(), Rank.JACK.toString(),"."));
		hand2.add(new Card(Suit.CLUBS, Rank.SEVEN, Suit.CLUBS.toString(), Rank.SEVEN.toString(), "."));
		assertTrue(ThreeCardLogic.isFlush(hand2));
	}

	@Test
	public void testIsThreeOfAKind() throws FileNotFoundException {
		ArrayList<Card> hand1 = new ArrayList<>();
		hand1.add(new Card(Suit.HEARTS, Rank.QUEEN, Suit.HEARTS.toString(), Rank.QUEEN.toString(), "."));
		hand1.add(new Card(Suit.CLUBS, Rank.KING, Suit.CLUBS.toString(), Rank.KING.toString(), "."));
		hand1.add(new Card(Suit.DIAMONDS, Rank.ACE, Suit.DIAMONDS.toString(), Rank.ACE.toString(), "."));
		assertFalse(ThreeCardLogic.isThreeOfAKind(hand1));

		ArrayList<Card> hand2 = new ArrayList<>();
		hand2.add(new Card(Suit.CLUBS, Rank.JACK, Suit.CLUBS.toString(), Rank.JACK.toString(), "."));
		hand2.add(new Card(Suit.CLUBS, Rank.JACK, Suit.CLUBS.toString(), Rank.JACK.toString(),"."));
		hand2.add(new Card(Suit.CLUBS, Rank.JACK, Suit.CLUBS.toString(), Rank.JACK.toString(), "."));
		assertTrue(ThreeCardLogic.isThreeOfAKind(hand2));
	}

	@Test
	public void testIsPair() throws FileNotFoundException {
		ArrayList<Card> hand1 = new ArrayList<>();
		hand1.add(new Card(Suit.HEARTS, Rank.QUEEN, Suit.HEARTS.toString(), Rank.QUEEN.toString(), "."));
		hand1.add(new Card(Suit.CLUBS, Rank.QUEEN, Suit.CLUBS.toString(), Rank.QUEEN.toString(), "."));
		hand1.add(new Card(Suit.DIAMONDS, Rank.ACE, Suit.DIAMONDS.toString(), Rank.ACE.toString(), "."));
		assertTrue(ThreeCardLogic.isPair(hand1));

		ArrayList<Card> hand2 = new ArrayList<>();
		hand2.add(new Card(Suit.CLUBS, Rank.JACK, Suit.CLUBS.toString(), Rank.JACK.toString(), "."));
		hand2.add(new Card(Suit.CLUBS, Rank.JACK, Suit.CLUBS.toString(), Rank.JACK.toString(),"."));
		hand2.add(new Card(Suit.CLUBS, Rank.JACK, Suit.CLUBS.toString(), Rank.JACK.toString(), "."));
		assertTrue(ThreeCardLogic.isPair(hand2));

		ArrayList<Card> hand3 = new ArrayList<>();
		hand3.add(new Card(Suit.HEARTS, Rank.QUEEN, Suit.HEARTS.toString(), Rank.QUEEN.toString(), "."));
		hand3.add(new Card(Suit.CLUBS, Rank.TEN, Suit.CLUBS.toString(), Rank.TEN.toString(), "."));
		hand3.add(new Card(Suit.DIAMONDS, Rank.ACE, Suit.DIAMONDS.toString(), Rank.ACE.toString(), "."));
		assertFalse(ThreeCardLogic.isPair(hand3));
	}

}
