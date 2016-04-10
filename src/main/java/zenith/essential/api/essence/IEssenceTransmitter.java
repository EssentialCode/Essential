package zenith.essential.api.essence;

public interface IEssenceTransmitter {

	/* adds a receiver to list of potential receivers to which essence can be sent */
	public void registerReceiver(IEssenceReceiver receiver);
	
	public boolean requestEssence(int amountRequested);

}
