package poker;

public class My_Best_Random_Generator implements Random_Generator{
	
	private long multiplier, increment, seed;
	private static final long mask = (1L << 48) - 1;
	
	public My_Best_Random_Generator() {
		
	}

	@Override
	public int next_int(int max) {
		seed = (multiplier * seed + increment) & mask;
		return (int) seed % max;
	}

	@Override
	public void set_seed(long seed) {
		this.seed = seed;
		
	}

	@Override
	public void set_constants(long multiplier, long increment) {
		this.multiplier = multiplier;
		this.increment = increment;
		
	}

}
