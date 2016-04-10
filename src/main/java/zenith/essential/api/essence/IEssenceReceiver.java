package zenith.essential.api.essence;

public interface IEssenceReceiver {

	/* returns percentage full */
	public int getEssenceLevel();
	
	/* returns maximum essence this receiver can accept */
	public int getMaxAcceptableEssence();
	
	public boolean canAcceptEssence(int amount);

	/* sends essence to the receiver */
	public boolean sendEssence(int amount);

}
