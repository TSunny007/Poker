package poker;

import java.util.Arrays;

public class PlayPoker {

	private static final int COMPARISIONS = 100;
	public static void main(String[] args) {
		double[] histogram = new double[2];
		//errorCheck(histogram , 367);
		//errorCheck(histogram , 1000);
		//errorCheck(histogram , 10000);
		//errorCheck(histogram , 100000);
		//errorCheck(histogram , 1000000);
		//exhaustiveErrorCheck(false);
		
		double num = 0;
		
		for(int i = 0; i < COMPARISIONS; i++){
			Card[] playerOneHand = new Card[2];
			playerOneHand[0] = new Card(Suite.Club, Value.Ace);
			playerOneHand[1] = new Card(Suite.Spade, Value.Ace);
			
			Card[] playerTwoHand = new Card[2];
			playerTwoHand[0] = new Card(Suite.Club, Value.Ace);
			playerTwoHand[1] = new Card(Suite.Spade, Value.King);
			
			num = num + Odds.oddsToWin(playerOneHand, playerTwoHand);
		}
		System.out.println("Average Percentage: " + (num / COMPARISIONS));
		
		
		
	}

	/**
	 * 
	 */
	private static void exhaustiveErrorCheck(boolean isHoldEm) {
		if (isHoldEm) {
			exhaustive7();
			for (int j = 0; j < Odds.histogram.length; j++) {
				j = j / 133_784_560;
			}
			System.out.println(Arrays.toString(Odds.histogram));
		} else {
			exhaustive5();
			for (int j = 0; j < Odds.histogram.length; j++) {
				j = j / 2598960;
			}
			System.out.println(Arrays.toString(Odds.histogram));
		}
	}

	/**
	 * @param histogram
	 */
	private static void errorCheck(double[] histogram, int n) {
		for (int i =0 ; i < n; i++)
		{
			Hand pokerFace = new Hand();
			pokerFace.getRandomCards(7);
			histogram[Odds.getRank(pokerFace.tableCards).ordinal()]++;
		}
		for(int j = 0; j < histogram.length; j++)
		{
			histogram[j] = histogram[j]/n;
		}
		System.out.println( "Shocastic error: " + Arrays.toString(histogram));
	}
	
	public static void exhaustive5(){
		Odds.exhaustive(5);
	}
	public static void exhaustive7(){
		Odds.exhaustive(7);
	}

}
