package poker;

import java.util.HashMap;

public class Card implements Comparable{
	Suite suite;
	Value value;
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	static HashMap<Suite, String> suiteKey = new HashMap<Suite,String>();
	static{
		suiteKey.put(Suite.Heart, "♥");
		suiteKey.put(Suite.Club, "♣");
		suiteKey.put(Suite.Spade, "♠");
		suiteKey.put(Suite.Diamond, "♦");
	}
	
	static HashMap<Value, String> numberKey = new HashMap<Value,String>();
	static{
		numberKey.put(Value.Ace, "A");
		numberKey.put(Value.Two, "2");
		numberKey.put(Value.Three, "3");
		numberKey.put(Value.Four, "4");
		numberKey.put(Value.Five, "5");
		numberKey.put(Value.Six, "6");
		numberKey.put(Value.Seven, "7");
		numberKey.put(Value.Eight, "8");
		numberKey.put(Value.Nine, "9");
		numberKey.put(Value.Ten, "10");
		numberKey.put(Value.Jack, "J");
		numberKey.put(Value.Queen, "Q");
		numberKey.put(Value.King, "K");
	}
	boolean isDealt = false;

	public Card(Suite _suite, Value _value) {
		suite = _suite;
		value = _value;
		// TODO Auto-generated constructor stub
	}

	public Value getValue() {
		return value;//returns the value (stored as an enum)
	}

	public void deal() {
		if (isDealt = false)
			isDealt = true;//only flag we need to check is that the card is not dealt yet
	}

	public boolean isDealt() {
		return isDealt;
	}

	public Suite getSuite() {
		return suite;
	}

	public String toString() {
		String cardOutput = "";
		return suiteKey.get(this.getSuite()) + " " + numberKey.get(this.getValue());
	}

	@Override
	public int compareTo(Object o) {
		try{
			Card c = (Card) o;//we have to try to cast the object into a card to compare
			return (this.getValue().ordinal() - c.getValue().ordinal());//this returns the magic value
		}
		catch(Exception e)
		{
			System.out.println("there was a problem with comparing so we reurn zero "+ e.getMessage());
			return 0;
		}
	}

}
