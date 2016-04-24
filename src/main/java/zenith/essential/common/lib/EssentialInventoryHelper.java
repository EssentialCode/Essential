package zenith.essential.common.lib;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import zenith.essential.common.tile.TileEntityBase;

public class EssentialInventoryHelper {
	
	private static final Random RANDOM = new Random();

	public static void dropItems(List<ItemStack> items, World world, BlockPos pos) {
		for(ItemStack stack : items) {
			if(stack != null){
				spewItemStack(stack, world, pos);
			}
		}
	}
	
	public static void spewItemStack(ItemStack stack, World world, BlockPos pos) {
        float offsetX = RANDOM.nextFloat() * 0.8F + 0.1F;
        float offsetY = RANDOM.nextFloat() * 0.8F + 0.1F;
        float offsetZ = RANDOM.nextFloat() * 0.8F + 0.1F;

        while (stack.stackSize > 0)
        {
            int i = RANDOM.nextInt(21) + 10;

            if (i > stack.stackSize)
            {
                i = stack.stackSize;
            }

            stack.stackSize -= i;
			EntityItem entityitem = new EntityItem(world, 
					pos.getX() + (double) offsetX, 
					pos.getY() + (double) offsetY, 
					pos.getZ() + (double) offsetZ, 
					new ItemStack(stack.getItem(), i, stack.getMetadata())
				);

            if (stack.hasTagCompound())
            {
                entityitem.getEntityItem().setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
            }

            float baseVelocity = 0.05F;
            entityitem.motionX = RANDOM.nextGaussian() * (double) baseVelocity;
            entityitem.motionY = RANDOM.nextGaussian() * (double) baseVelocity + 0.20000000298023224D;
            entityitem.motionZ = RANDOM.nextGaussian() * (double) baseVelocity;
            world.spawnEntityInWorld(entityitem);
        }
	}
	
	public static ItemStackHandler getItemStackHandler(int size, final TileEntityBase te){
		ItemStackHandler newHandler = new ItemStackHandler(size) {
			@Override
			protected void onContentsChanged(int slot)
			{
				super.onContentsChanged(slot);
				te.markForUpdate();
			}
		};
        return newHandler;
	}
}
