package zenith.essential.common.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import zenith.essential.common.block.BlockCampfire;
import zenith.essential.common.block.EssentialBlocks;
import zenith.essential.common.capability.LimitedItemStackHandler;

public class TileEntityCampfire extends TileEntity implements ITickable {

	private final int MAX_BURN_TICKS = 100;
	private int burnTicksRemaining;
	
	private static final Random RANDOM = new Random();

	public TileEntityCampfire(){
		burnTicksRemaining = MAX_BURN_TICKS;
	}

	ItemStackHandler itemHandler = new LimitedItemStackHandler(4, 2) {
        @Override
        protected void onContentsChanged(int slot)
        {
            super.onContentsChanged(slot);
            TileEntityCampfire.this.markDirty();
        }

    };
    
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return true;
        return super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T)itemHandler;
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        itemHandler.deserializeNBT(compound.getCompoundTag("Inventory"));
        if(compound.hasKey("burnTicksRemaining")){
			this.burnTicksRemaining = compound.getInteger("burnTicksRemaining");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setTag("Inventory", itemHandler.serializeNBT());
        compound.setInteger("burnTicksRemaining", burnTicksRemaining);
    }
    
    public boolean insertItemFromPlayer(EntityPlayer player){
    	if(!player.worldObj.isRemote){

			ItemStack stack = player.getCurrentEquippedItem();
			if(FurnaceRecipes.instance().getSmeltingResult(stack) == null) {
				return false;
			}
			if(stack == null || stack.stackSize <= 0){
				return false;
			}

    		ItemStack newStack = ItemHandlerHelper.insertItem(itemHandler, ItemStack.copyItemStack(stack), false);
			stack.stackSize = newStack != null ? newStack.stackSize : 0;
    	}
    	
    	return true;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate){
    	return (oldState.getBlock() != newSate.getBlock());
    }
    
    public List<ItemStack> getContents(){
    	List<ItemStack> stacks = new ArrayList<ItemStack>();
    	for (int i = 0; i < itemHandler.getSlots(); i++) {
    		stacks.add(itemHandler.getStackInSlot(i));
    	}
    	return stacks;
    }
    
    public List<ItemStack> getContentsBroken(BlockCampfire.CampfireState state){
    	List<ItemStack> stacks = getContents();
    	switch(state){
			case NEW:
				stacks.add(new ItemStack(EssentialBlocks.campfire));
				return stacks;
			case DONE:
				return stacks;
			case BURNING:
				float percentLeft = ((float) burnTicksRemaining/ (float) MAX_BURN_TICKS) * 100;
				if(percentLeft > 50){
					stacks.add(new ItemStack(EssentialBlocks.campfire));
					return stacks;
				}
				if(percentLeft > 25){
					stacks.add(new ItemStack(Blocks.log));
				}
				int rand = RANDOM.nextInt(9);
				stacks.add(new ItemStack(Items.stick, 8 - rand)) ;
			default:
				return stacks;
    	}
    }
    
	@Override
	public void update() {
		if(this.hasWorldObj()){
			World world = this.getWorld();
			if(world.getBlockState(pos).getBlock() != EssentialBlocks.campfire){
				this.invalidate();
				return;
			}
			IBlockState state = world.getBlockState(pos);
			if(state.getValue(BlockCampfire.STATE) == BlockCampfire.CampfireState.BURNING){
				if(--this.burnTicksRemaining <= 0){
					this.smeltAllTheThings();
					BlockCampfire.setBurningState(BlockCampfire.CampfireState.DONE, this.worldObj, pos);
				}
			}
		}
	}
	
	public void resetBurning(){
		this.burnTicksRemaining = MAX_BURN_TICKS;
	}
	
	private void smeltAllTheThings(){
		List<ItemStack> stacks = getContents();
		for(int i = 0; i < stacks.size(); i++){
			ItemStack stack = stacks.get(i);
			if(stack != null){
				ItemStack targetItem = FurnaceRecipes.instance().getSmeltingResult(stack);
				ItemStack newStack = ItemHandlerHelper.copyStackWithSize(targetItem, stack.stackSize);
				itemHandler.setStackInSlot(i, newStack);
			}
		}
	}
}
