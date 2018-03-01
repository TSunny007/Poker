package poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hand {
	Deck pokerDeck;
	Card[] tableCards;
	Card[] playerOneCards;
	Card[] playerTwoCards;

	public Hand() {
		pokerDeck = new Deck();
	}
	//assuming you send in a sensible number here.(5 or 7)
	//////////this adds the following amount of cards to the tableDeck///////
	public void getRandomCards(int _numCards) {
		tableCards = new Card[_numCards];
		for (int i = 0; i < _numCards; i++)
		{
			tableCards[i] = pokerDeck.deal();//whatever is dealt comes into here
		}
	}
	/////////this method adds cards to the two players' array
	public void dealToPlayers()
	{
		playerOneCards = new Card[2];
		playerOneCards[0] =  pokerDeck.deal();
		playerOneCards[1] =  pokerDeck.deal();
		///////////////////
		playerTwoCards = new Card[2];
		playerTwoCards[0] =  pokerDeck.deal();
		playerTwoCards[1] =  pokerDeck.deal();
	}
	////////////// this one just compares the tablecards (Poker)
	public void dealToPlayer()
	{
		playerOneCards = new Card[2];
		playerOneCards[0] =  pokerDeck.deal();
		playerOneCards[1] =  pokerDeck.deal();
	}
	//////////////this one just compares the tablecards (Poker)
	public void dealToPlayer(Card _one, Card _two)
	{
	playerOneCards = new Card[2];
	playerOneCards[0] =  _one;
	playerOneCards[1] =  _two;
	pokerDeck.cardDeck.removeIf(card -> (card.getSuite() == _one.getSuite() && card.getValue() == _one.getValue()) ||(card.getSuite() == _two.getSuite() && card.getValue() == _two.getValue()));
	}
	public void dealToBothPlayers(Card _one, Card _two,Card _three, Card _four)
	{
	playerOneCards = new Card[2];
	playerTwoCards = new Card[2];
	playerOneCards[0] =  _one;
	playerOneCards[1] =  _two;
	playerTwoCards[0] =  _three;
	playerTwoCards[1] =  _four;
	pokerDeck.cardDeck.removeIf(card -> (card.getSuite() == _one.getSuite() && card.getValue() == _one.getValue()) ||(card.getSuite() == _two.getSuite() && card.getValue() == _two.getValue())
			||(card.getSuite() == _three.getSuite() && card.getValue() == _three.getValue()) ||(card.getSuite() == _four.getSuite() && card.getValue() == _four.getValue()));
	}
	public void dealToPlayerTwo()
	{
		playerTwoCards = new Card[2];
		playerTwoCards[0] =  pokerDeck.deal();
		playerTwoCards[1] =  pokerDeck.deal();
	}
	public Ranks getRank()
	{
			return Odds.getRank(tableCards);
	}
	////////////// this one will compare the hands
	public boolean compareHands()
	{
		 Card[] allCardsOne = new Card[tableCards.length + playerOneCards.length];
		    // copy the tableCards
		    System.arraycopy(tableCards, 0, allCardsOne, 0, tableCards.length);
		    // copy the player One cards
		    System.arraycopy(playerOneCards, 0, allCardsOne, tableCards.length, playerOneCards.length);
		    //////////////////
		    Card[] allCardsTwo = new Card[tableCards.length + playerTwoCards.length];
		    // copy the tableCards
		    System.arraycopy(tableCards, 0, allCardsTwo, 0, tableCards.length);
		    // copy the player Two cards
		    System.arraycopy(playerTwoCards, 0, allCardsTwo, tableCards.length, playerTwoCards.length);
		    //////////////////Case 1 our player wins
		    if (Odds.getRank(allCardsOne).ordinal() > Odds.getRank(allCardsTwo).ordinal() )
		    {
		    	return true;
		    }
		    ///////////Case 2 our player lost
		    if (Odds.getRank(allCardsOne).ordinal() < Odds.getRank(allCardsTwo).ordinal() )
		    {
		    	return false;
		    }
		    //Case 3 they tied
		    if (Odds.getRank(allCardsOne).ordinal() == Odds.getRank(allCardsTwo).ordinal())
		    {
		    	//if both are royal flush then you lose because tying ain't winning
		    	if (Odds.getRank(allCardsOne) == Ranks.RoyalFlush)
		    	{
		    		if (Odds.getRank(allCardsOne) == Ranks.RoyalFlush)
		    		{
		    		return false;
		    		}
		    		return true;
		    	}
		    	//if both of you getStraight flush then you compare the ordinal of the "highest" value
		    	if (Odds.getRank(allCardsOne) == Ranks.StraightFlush)
		    	{
		    		return Odds.isStraightFlush(allCardsOne).ordinal() >
		    		Odds.isStraightFlush(allCardsTwo).ordinal();
		    	}
		    	//if both are four of a kind then return which value is higher
		    	if (Odds.getRank(allCardsOne) == Ranks.FourOfKind)
		    	{
		    		return Odds.isFourOfAKind(allCardsOne).ordinal() >
		    		Odds.isFourOfAKind(allCardsTwo).ordinal();
		    	}
		    	//if full house then return which of them has a higher 
		    	if (Odds.getRank(allCardsOne) == Ranks.FullHouse)
		    	{
		    		Value[] fullHouseOne = getFullHouseCandidates(allCardsOne);
		    		if (fullHouseOne[0] == null && fullHouseOne[1] == null )
		    		{
		    			fullHouseOne = getFullHouseCandidates(allCardsOne);
		    		}
		    		Value[] fullHouseTwo = getFullHouseCandidates(allCardsTwo);
		    		if(fullHouseOne[0].ordinal() >  fullHouseTwo[0].ordinal())
		    		{
		    			return true;
		    		}
		    		if (fullHouseOne[0].ordinal() <  fullHouseTwo[0].ordinal())
		    		{
		    			return false;
		    		}
		    		if(fullHouseOne[1].ordinal() >  fullHouseTwo[1].ordinal())
		    		{
		    			return true;
		    		}
		    		return false;
		    	}
		    	
		    	//if both are flushes then return which of the sequences has the highest value
		    	if (Odds.getRank(allCardsOne) == Ranks.Flush)
		    	{
		    		return Odds.isFlush(allCardsOne).ordinal() >
		    		Odds.isFlush(allCardsTwo).ordinal();
		    	}
		    	
		    	//if both are flushes then return which of the sequences has the highest value
		    	if (Odds.getRank(allCardsOne) == Ranks.Straight)
		    	{
		    		return Odds.isStraight(allCardsOne) >
		    		Odds.isStraight(allCardsTwo);
		    	}
		    	
		    	//if both are Three of kind then return which of the sequences has the highest value
		    	if (Odds.getRank(allCardsOne) == Ranks.ThreeOfKind)
		    	{
		    		return Odds.isThreeOfAKind(allCardsOne).get(0).ordinal() >
		    		Odds.isThreeOfAKind(allCardsTwo).get(0).ordinal();
		    	}
		    	//if both are Three of kind then return which of the sequences has the highest value
		    	if (Odds.getRank(allCardsOne) == Ranks.TwoPair)
		    	{
		    		return Odds.isPair(allCardsOne).get(0).ordinal() >
		    		Odds.isPair(allCardsTwo).get(0).ordinal();
		    	}
		    	//if both are Three of kind then return which of the sequences has the highest value
		    	if (Odds.getRank(allCardsOne) == Ranks.TwoPair)
		    	{
		    		List<Value> firstPairs = Odds.isPair(allCardsOne);
		    		List<Value> secondPairs = Odds.isPair(allCardsOne);
		    		Collections.sort(firstPairs);
		    		Collections.sort(secondPairs);
		    		if (firstPairs.get(0).ordinal() > secondPairs.get(0).ordinal())
		    		{
		    			return true;
		    		}
		    		if (firstPairs.get(0).ordinal() < secondPairs.get(0).ordinal())
		    		{
		    			return false;
		    		}
		    		if (firstPairs.get(1).ordinal() > secondPairs.get(1).ordinal())
		    		{
		    			return true;
		    		}
		    		return false;
		    	}
			if (Odds.getRank(allCardsOne) == Ranks.Pair) {
				return Odds.isPair(allCardsOne).get(0).ordinal()
						> Odds.isPair(allCardsTwo).get(0).ordinal();
			}
		    	/////////////////
			if (Odds.getRank(allCardsOne) == Ranks.HighCard) {
				return Odds.highCard(allCardsOne).ordinal()
						> Odds.highCard(allCardsTwo).ordinal();
			}
		    	
		    	
		    }
		    return false;
	}
	/**
	 * @param allCardsTwo
	 */
	private Value[] getFullHouseCandidates(Card[] allCardsTwo) {
		List<List<Value>> flushCards = Odds.isFullHouse(allCardsTwo);
		if (flushCards.get(1) == null)
		{
			ArrayList<Value> triples = new ArrayList<>(flushCards.get(1));
		}
		ArrayList<Value>triples = new ArrayList<>(flushCards.get(1));
		Value[] fullHouseNumbers = new Value[2];
		//if there are 2 triplets
		if (triples.size() == 2)
		{
			if (triples.get(0).ordinal() > triples.get(1).ordinal())
			{
				fullHouseNumbers[0] = triples.get(0);
				fullHouseNumbers[1] = triples.get(1);
				return fullHouseNumbers;
			}
			else {
				fullHouseNumbers[0] = triples.get(1);
				fullHouseNumbers[1] = triples.get(0);
				return fullHouseNumbers;
			}
		}
		ArrayList<Value>doubles = new ArrayList<>(flushCards.get(1));
			fullHouseNumbers[0] = triples.get(0);
			fullHouseNumbers[1] = doubles.get(0);
//		ArrayList<Value>doubles = new ArrayList<>(flushCards.get(0));
//		//otherwise there has to be one pair and a triplet
//		if (triples.size() == 1) {
//			fullHouseNumbers[0] = triples.get(0);
//			fullHouseNumbers[1] = doubles.get(0);
//		}
		return fullHouseNumbers;
	}
	///////////////// this prints out the table plus the players's cards
	
	public Card[] getCards5(int index1, int index2, int index3, int index4, int index5) {
		tableCards = new Card[5];
		tableCards[0] = pokerDeck.deal(index1);
		tableCards[1] = pokerDeck.deal(index2);
		tableCards[2] = pokerDeck.deal(index3);
		tableCards[3] = pokerDeck.deal(index4);
		tableCards[4] = pokerDeck.deal(index5);

		return tableCards;
	}

	public Card[] getCards7(int index1, int index2, int index3, int index4, int index5, int index6, int index7) {
		tableCards = new Card[7];
		tableCards[0] = pokerDeck.deal(index1);
		tableCards[1] = pokerDeck.deal(index2);
		tableCards[2] = pokerDeck.deal(index3);
		tableCards[3] = pokerDeck.deal(index4);
		tableCards[4] = pokerDeck.deal(index5);
		tableCards[5] = pokerDeck.deal(index6);
		tableCards[6] = pokerDeck.deal(index7);

		return tableCards;
	}
	public String toString(Card[] playerCards)
	{
		String handToString = "/////Hand////\n";
		for (int k = 0; k < tableCards.length; k++)
		{
			handToString += tableCards[k] + "\n";
		}
		for (int l = 0; l < playerCards.length; l++)
		{
			handToString += playerCards[l] + "\n";
		}
		return handToString;
	}
	
	
}
