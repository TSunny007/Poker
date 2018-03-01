package poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

public class Odds {
	private static final int TRIALS = 15_000;
	////////////README//////////////////////////////////////////////
	//Do not send in the same exact card into this! why would you even do that? 
	//We even wrote the code to check for duplicates, but this just wastes system resources
	//
	protected static double[] histogram = new double[10];
	static Hand hand = new Hand();
	public static Ranks getRank(Card[] allCards)
	{

		if(isRoyalFlush(allCards))
			return Ranks.RoyalFlush;
		if(isStraightFlush(allCards) != null)
			return Ranks.StraightFlush;
		if(isFourOfAKind(allCards) != null)
			return Ranks.FourOfKind;
		if(isFullHouse(allCards) != null || (isThreeOfAKind(allCards) != null && isThreeOfAKind(allCards).size() >= 2))
			return Ranks.FullHouse;
		if(isFlush(allCards) != null)
			return Ranks.Flush;
		if(isStraight(allCards) != null)
			return Ranks.Straight;
		if(isThreeOfAKind(allCards) != null)
			return Ranks.ThreeOfKind;
		if(isPair(allCards) != null && isPair(allCards).size() >= 2)
			return Ranks.TwoPair;
		if(isPair(allCards) != null)
			return Ranks.Pair;
		return Ranks.HighCard;
	}
	protected static boolean isRoyalFlush(Card[] allCards) {
		Map<Suite, List<Card>> groupedCards = Arrays.asList(allCards).stream()
				//we will store the cards as a map of Suite and the cards inside each suite. Stream iterates over all the cards
		.filter(card -> (card.getValue() == Value.Ten || card.getValue() == Value.Ace
		//filter filters the list so that only cards that have a value of Ace ten jack queen or king will be passed on 
				|| card.getValue() == Value.Jack || card.getValue() == Value.Queen
				|| card.getValue() == Value.King))
		//collect will collect all this streamed data and group it using the get suite method from Card class on each one of these card objects
				.collect(Collectors.groupingBy(Card::getSuite));
		boolean royalFlushFlag = groupedCards.values().stream().anyMatch(cards -> cards.size() == 5);
		//this checks if the cards are all of same suite
		return royalFlushFlag;
	}
	
/////////////////////////////////
	/**
	 * @param candidateStraight
	 * @return
	 */
	protected static Value isStraightFlush(Card[] allCards) {
		Value bigNumber = null;
		Map<Suite, List<Card>> groupedCards = Arrays.asList(allCards).stream()
				.collect(Collectors.groupingBy(Card::getSuite));
		List<List<Card>> flushCards = groupedCards.values().stream().filter(cards -> cards.size() >= 5)
				.collect(Collectors.toList());
		if (flushCards != null && flushCards.size() == 1) {
			List<Card> candidateStraight = flushCards.get(0);
			Collections.sort(candidateStraight);
			bigNumber = getValueForStraight(candidateStraight, false);
			//the helper method does all of the hard work
			if (bigNumber == null && candidateStraight.get(candidateStraight.size() - 1).value == Value.Ace) {
				bigNumber = getValueForStraight(candidateStraight, true);
			}
		}
		return bigNumber;
	}

	/**
	 *
	 * @param candidateStraight
	 * @param aceFlag
	 * @return
	 */
	private static Value getValueForStraight(List<Card> candidateStraight, boolean aceFlag) {
		Value bigNumber = null;
		if (aceFlag == true) {
			//if the flag is true is true then remove the ace from last in the list to first
			Card lastCard = candidateStraight.get(candidateStraight.size() - 1);
			candidateStraight.remove(candidateStraight.size() - 1);
			candidateStraight.add(0, lastCard);
		}
		int straightCount = 1;
		for (int i = 0; i < candidateStraight.size(); i++) {
			if (i != 0) {
				//if ace flag is true then do the left hand side of the colon
				if ((aceFlag ? getOrdinal(candidateStraight.get(i)) : candidateStraight.get(i).value.ordinal())
						- (aceFlag ? getOrdinal(candidateStraight.get(i - 1))
								: candidateStraight.get(i - 1).value.ordinal()) == 1) 
				//this checks and does the appropriate action based on whether the ace will form a straight from the beginning or the end
					{
					straightCount++;
					if (straightCount >= 5) {
						bigNumber = candidateStraight.get(i).value;
					}
				} else {
					if (straightCount >= 5) {
						break;
					} else {
						straightCount = 1;
					}
				}
			}
		}
		return bigNumber;
	}
	/////////////////
	public static double oddsToWin(Card[] twoCards)
	{
		Hand playThis;
		int wins = 0;
		for (int i = 0; i < TRIALS; i++)
		{
			playThis = new Hand();
			playThis.dealToPlayer(twoCards[0], twoCards[1]);
			playThis.dealToPlayerTwo();
			playThis.getRandomCards(5);
			if (playThis.compareHands())
				wins++;
		}

		double odds = ((double)wins/TRIALS);
		return odds;
	}
	public static double oddsToWin(Card[] player1, Card[] player2)
	{
		Hand playThis;
		int wins = 0;
		for (int i = 0; i < TRIALS; i++)
		{
			playThis = new Hand();
			playThis.dealToBothPlayers(player1[0], player1[1], player2[0], player2[1]);
			playThis.getRandomCards(5);
			if (playThis.compareHands())
				wins++;
		}

		double odds = ((double)wins/TRIALS);
		return odds;
	}
	//////////////////
	private static int getOrdinal(Card card) {
		if (card.value.ordinal() == 12) {
			return 0;
		} else {
			//shift everything's ordinal up
			return card.value.ordinal() + 1;
		}

	}
////////////////////////////////
	

	protected static Value isFourOfAKind(Card[] allCards) {
		Map<Value, List<Card>> groupedCards = Arrays.asList(allCards).stream()
						.collect(Collectors.groupingBy(Card::getValue));
		try{
		List<Card> fourAKind = groupedCards.values().stream().filter(cards -> cards.size() == 4).findFirst().get();
		//since we set size to only equal 4 this will only return
					return fourAKind.get(0).getValue();
					//this returns the card value of the four of a kind hand
		}
				catch(NoSuchElementException e)
				{
					return null;
				}
		}
	
	protected static List<Value> isThreeOfAKind(Card[] allCards) {
		//converting two three of a kind into a full house
		Map<Value, List<Card>> groupedCards = Arrays.asList(allCards).stream()
						.collect(Collectors.groupingBy(Card::getValue));
				List<List<Card>> candidates = groupedCards.values().stream().filter(cards -> cards.size() == 3).collect(Collectors.toList());
				 	List<Value> returnValue = new ArrayList();
					for (List<Card> cardList : candidates)
					{
						returnValue.add(cardList.get(0).getValue());
					}
					if (returnValue.size() ==0)
						returnValue = null;
					//returns a list of all the cards that make a three of a kind (should only be two at max)
				return returnValue;
	}	

	protected static Integer isStraight(Card[] allCards) {
		Set<Integer> valuesPresent = new HashSet<Integer>();
		for (int i = 0; i < allCards.length; i++)
		{
			//if ace then there's a number added both in the beginning and the end
			valuesPresent.add(allCards[i].getValue().ordinal());
			if (allCards[i].getValue() == Value.Ace)
			{
				valuesPresent.add(-1);
			}
		}
		//adds the values to a set and then converts it back into an arraylist which can be analyzed
		List<Integer>valuesPresentList = new ArrayList<Integer>(valuesPresent);
		int straightCount = 0;
		Integer highestOrdinal = null;
		for (int k = 1; k < valuesPresentList.size() ; k++) {
			if (valuesPresentList.get(k) - valuesPresentList.get(k-1) == 1)
			{
				straightCount++;
				highestOrdinal = valuesPresentList.get(k);
				//update the highest value as you go higher 
				//the list should already be sorted because of adding to the set
			}
			else if (straightCount == 4)
				break;
			else {
				straightCount = 0;
			}
		}
		if (straightCount >= 4)
		{
			if (highestOrdinal.equals(-1) || highestOrdinal.equals(13))
			{
				return 12;
			}
			return highestOrdinal;
		}
		return null;
	}
	
	         //this list contains lists (only 2) containing values that satisfy either three of a kind or two of a kind
	protected static List<List<Value>> isFullHouse(Card[] allCards) {
		List<List<Value>> fullHouseInfo = new ArrayList<List<Value>>();
		//this will only add the two of a kind and three of a kind. the three won't be part of the two
		//convert list of a list into a list
		fullHouseInfo.add(isPair(allCards));
		fullHouseInfo.add(isThreeOfAKind(allCards));
		if (fullHouseInfo.get(0) == null && fullHouseInfo.get(1) == null)
			return null;
		if (fullHouseInfo.get(0) == null && fullHouseInfo.get(1).size() != 2)
			return null;
		if (fullHouseInfo.get(1) == null)
			return null;
		return fullHouseInfo;
	}	
	
	protected static Value isFlush(Card[] allCards) {
		Map<Suite, List<Card>> groupedCards = Arrays.asList(allCards).stream()
						.collect(Collectors.groupingBy(Card::getSuite));
		//this collects a map based on suite (Key) and then has a list that contains the groupedCards as the values
		//make this into just a list
				List<List<Card>> isFlush = groupedCards.values().stream().filter(cards -> cards.size() >= 5).collect(Collectors.toList());
				Value highestFlush = null;
				for (List<Card> eachFlush : isFlush)
				{
					//sort the values
					Collections.sort(eachFlush);
					if (highestFlush == null)
					{
						//
						highestFlush = eachFlush.get(eachFlush.size()-1).getValue();
					}
					else if (highestFlush.ordinal() < eachFlush.get(eachFlush.size()-1).getValue().ordinal()){
						highestFlush = eachFlush.get(eachFlush.size()-1).getValue();
					}
					
				}
				return highestFlush;
	}	

	public static void exhaustive(int handSize) {
		
		//find first through five or seven cards
		for (int cardOne = 0; cardOne < 52; cardOne++) {
			for (int cardTwo = cardOne + 1; cardTwo < 52; cardTwo++) {
				for (int cardThree = cardTwo+1; cardThree < 52; cardThree++) {
					for (int cardFour = cardThree + 1; cardFour < 52; cardFour++) {
						for (int cardFive = cardFour + 1; cardFive < 52; cardFive++) {

							if (handSize == 5) {
								//if five hand size then find correlating cards
								Card[] currentHand = hand.getCards5(cardOne, cardTwo, cardThree, cardFour, cardFive);
								//System.out.println(Arrays.toString(currentHand));
								Ranks rankOfHand = Odds.getRank(currentHand);
								histogram[rankOfHand.ordinal()]++;
							}
							if (handSize == 7) {
								for (int cardSix = cardFive + 1; cardSix < 52; cardSix++) {
									for (int cardSeven = cardSix + 1; cardSeven < 52; cardSeven++) {
										Card[] currentHand = hand.getCards7(cardOne, cardTwo, cardThree, cardFour,
												cardFive, cardSix, cardSeven);
										Ranks rankOfHand = Odds.getRank(currentHand);
										histogram[rankOfHand.ordinal()]++;
									}
								}
							}

						}
					}
				}
			}
		}
	}

	
	
	protected static List<Value> isPair(Card[] allCards) {
		Map<Value, List<Card>> groupedCards = Arrays.asList(allCards).stream()
				.collect(Collectors.groupingBy(Card::getValue));
		List<List<Card>> candidates = groupedCards.values().stream().filter(cards -> cards.size() == 2).collect(Collectors.toList());
			if (candidates.size() == 0)
			{
				return null;
			}
			List<Value> returnValue = new ArrayList();
			for (List<Card> cardList : candidates)
			{
				returnValue.add(cardList.get(0).getValue());
			}
			//returns a list of all the cards that make a three of a kind (should only be two at max)
		return returnValue;
	}

	protected static Value highCard(Card[] allCards) {
		//this returns the highest card of the hand, or returns an Ace
		Arrays.sort(allCards);
		if (allCards[0].getValue() == Value.Ace )
			return Value.Ace;
		return allCards[allCards.length - 1].getValue();
	}	
}
