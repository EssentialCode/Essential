package zenith.essential.common.block;

public class BlockTableShort extends BlockTable {
	public static final String name = "tableShort";

	public BlockTableShort(){
		super(name);
		setBlockBounds();
	}
	
	private void setBlockBounds() {
		float pixel = 1F / 16;
		float minX = 0;
		float minY = 0;
		float minZ = 0;
		float maxX = 1F;
		float maxY = 12F * pixel;
		float maxZ = 1F;
		setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
	}

}
