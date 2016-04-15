package zenith.essential.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import zenith.essential.common.block.BlockBase;

public class EssentialItemBlock extends ItemBlock {

	public EssentialItemBlock(Block block){
		super(block);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}
	
	@Override
	public int getMetadata(int meta){
		return meta;
	}

	// create a unique unlocalised name for each colour, so that we can give each one a unique name
	@Override
	public String getUnlocalizedName(ItemStack stack) {
	  Block block = Block.getBlockFromItem(stack.getItem());
	  if(!(block instanceof BlockBase)){
		  return block.getUnlocalizedName();
	  }
	  BlockBase base = (BlockBase) block;
	  String unlocalizedName = super.getUnlocalizedName();
	  String addon = base.getUnlocalizedIdentifier(stack.getMetadata());
	  if (addon == null || addon.isEmpty()){
		  return unlocalizedName;
	  }
	  return unlocalizedName + "." + addon;
	}
}
