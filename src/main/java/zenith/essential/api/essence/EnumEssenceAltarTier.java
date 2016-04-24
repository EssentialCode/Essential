package zenith.essential.api.essence;

import zenith.essential.common.EssentialLogger;

public enum EnumEssenceAltarTier {
	TIER_0(20000),
	TIER_1(40000),
	TIER_2(80000),
	TIER_3(160000);
	
	private final int HALF_PERCENT = 200;
	private final static EnumEssenceAltarTier[] LOOKUP = new EnumEssenceAltarTier[values().length];

	private int capacity;
	
	EnumEssenceAltarTier(int capacity){
		this.capacity = capacity;
	}
	
	public int getCapacity(){
		return capacity;
	}
	
	public int getRegenRate(){
		return Math.floorDiv(Math.floorDiv(capacity, HALF_PERCENT), 2);
	}
	
	public int getBaseCost(){
		return capacity / 10;
	}
	
	public static EnumEssenceAltarTier getTier(int tier){
		if(tier >= values().length){
			EssentialLogger.quickError("Provided tier does not exist: " + tier);
			return null;
		}
		return LOOKUP[tier];
	}
	
	static {
		for(EnumEssenceAltarTier tier : values()){
			LOOKUP[tier.ordinal()] = tier;
		}
	}
}
