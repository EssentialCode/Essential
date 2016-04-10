package zenith.essential.common.event;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zenith.essential.common.EssentialLogger;
import zenith.essential.common.item.EssentialItems;
import zenith.essential.common.item.ItemEssentialCrafting;

public class ForgeEventHandlers {
	private EssentialLogger log = EssentialLogger.getLogger();
	private Random RANDOM = new Random();

	// Keeping this code around to be reused for other random drops
//	@SubscribeEvent
//	public void dropGrassFibers(BlockEvent.BreakEvent e){
//		if(!e.world.isRemote && 
//		   !e.getPlayer().capabilities.isCreativeMode &&
//		   e.state.getBlock() == Blocks.tallgrass
//		   ){
//			int chance = (int) Math.floor(Math.random() * 8);
//			if(chance == 0){
//				float xOff = RANDOM.nextFloat() * 0.8F + 0.1F;
//				float yOff = RANDOM.nextFloat() * 0.8F + 0.1F;
//				float zOff = RANDOM.nextFloat() * 0.8F + 0.1F;
//				int amt = (int) Math.floor(Math.random() * 2) + 1;
//				ItemStack newStack = new ItemStack(EssentialItems.crafting,amt,ItemEssentialCrafting.GRASS_FIBER);
//				EntityItem entity = new EntityItem(
//					e.world, 
//					e.pos.getX() + (double) xOff, 
//					e.pos.getY() + (double) yOff, 
//					e.pos.getZ() + (double) zOff, 
//					newStack
//				);
//
//				// I should think of a better name to call this than 'f3'
//				float f3 = 0.05F;
//				entity.motionX = RANDOM.nextGaussian() * (double)f3;
//				entity.motionY = RANDOM.nextGaussian() * (double)f3 + 0.20000000298023224D;
//				entity.motionZ = RANDOM.nextGaussian() * (double)f3;
//				e.world.spawnEntityInWorld(entity);
//			}
//		}
//	}
}