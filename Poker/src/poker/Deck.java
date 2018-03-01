package poker;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
	public ArrayList<Card> cardDeck = new ArrayList<Card>();
	//since fisher-yates goes down in cards, suitable to use ArrayList
	private Random_Generator toShuffle = new Javas_Random_Generator();

	public Deck() {
		for (Suite _suite : Suite.values()) {
			for (Value _value : Value.values()) {
				//this should add 52 cards because those are our only combinations 
				cardDeck.add(new Card(_suite, _value));
			}
		}
	}
	
	public void setToShuffle(Random_Generator _toShuffle) {
		toShuffle = _toShuffle;
		}
	
	public Card deal(int index) {
		Card card = cardDeck.get(index);
		return card;
		}

	public void reset()
	{
		cardDeck.clear();
		//lol i do not know how to call our constructor from this method so i'm just copying 
		//the code over instead 
		for (Suite _suite : Suite.values()) {
			for (Value _value : Value.values()) {
				//this should add 52 cards because those are our only combinations 
				cardDeck.add(new Card(_suite, _value));
			}
		}
	}
	public Card deal() {
		//deals the card that shuffling fisher-yates style throws out 
		return shuffle();
	}

		//for our purposes, shuffle is a helper method for deal, where it picks a random card 
		//and discards it, by which we mean it returns it to the deal method.
		//shuffling the deck is UNNECESSARY if you just deal a random card
		//after all that is why you shuffle your cards: to deal them.
		private Card shuffle() {
			int selectedIndex = toShuffle.next_int(cardDeck.size() - 1);//our random can 
			Card selected = new Card(cardDeck.get(selectedIndex).getSuite(), cardDeck.get(selectedIndex).getValue());
			//stores the card at rnd(nsize) {see below}
			cardDeck.set(selectedIndex, cardDeck.get(cardDeck.size() - 1));
			//replaces this card with the last card
			cardDeck.remove(cardDeck.size() - 1);
			selected.isDealt = true;
			return selected;
			//returns the card thrown out of the deck (the card that will be dealt)
		}

	   /*
		* This is how Fisher Yates works
		* <------ n[] ------>
		*0 1 2 3 4 5 6 7 8 9  nsize  rnd(nsize)  output
		*-------------------  -----  ----------  ------
		*0 1 2 3 4 5 6 7 8 9     10           4       4
		*0 1 2 3 9 5 6 7 8        9           7       7
		*0 1 2 3 9 5 6 8          8           2       2
		*0 1 8 3 9 5 6            7           6       6
		*0 1 8 3 9 5              6           0       0
		*5 1 8 3 9                5           2       8
		*5 1 9 3                  4           1       1
		*5 3 9                    3           0       5
		*9 3                      2           1       3
		*9                        1           0       9
		*/
	public String toString() {
		String deckedOut = "///////Deck////////\n";
		for (Card c : cardDeck) {
			deckedOut += c.toString() + "\n";
		}
		return deckedOut;
	}

}
