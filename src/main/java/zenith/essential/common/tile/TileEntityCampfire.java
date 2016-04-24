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
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import zenith.essential.common.EssentialLogger;
import zenith.essential.common.block.BlockCampfire;
import zenith.essential.common.block.EssentialBlocks;
import zenith.essential.common.capability.IInventoryHandler;
import zenith.essential.common.capability.InputInventoryHandler;
import zenith.essential.common.capability.LimitedItemStackHandler;

public class TileEntityCampfire extends TileEntityBase implements ITickable {

	private final int MAX_BURN_TICKS = 1600;
	private int burnTicksRemaining;
	
	private static final Random RANDOM = new Random();

	ItemStackHandler itemHandler = new LimitedItemStackHandler(4, 2) {
        @Override
        protected void onContentsChanged(int slot)
        {
            super.onContentsChanged(slot);
            TileEntityCampfire.this.markDirty();
        }

    };

	public TileEntityCampfire(){
		super();
		burnTicksRemaining = MAX_BURN_TICKS;
		EnumFacing[] sides = EnumFacing.values();
		for(int slotIndex = 0; slotIndex < 4; slotIndex++){
			// offset by 2 to get only NSEW sides
			EnumFacing side = sides[slotIndex + 2];
			int nextSlot = (slotIndex + 1) % 3;

			addInterfaceHandle(
					new InputInventoryHandler(itemHandler, 
							slotIndex, EnumSlotPos.LEFT.getMinHit(), 
							EnumSlotPos.LEFT.getMaxHit(), 
							EnumSlotPos.LEFT.getOffset(side))
						.setSide(side)
						.setRotationOffset(RANDOM.nextInt(360))
					);

			addInterfaceHandle(
					new InputInventoryHandler(itemHandler, 
							nextSlot, EnumSlotPos.RIGHT.getMinHit(), 
							EnumSlotPos.RIGHT.getMaxHit(), 
							EnumSlotPos.RIGHT.getOffset(side))
						.showStack(false)
						.setRotationOffset(RANDOM.nextInt(360))
						.setSide(side)
						);

		}
		for(IInventoryHandler handler : handlers){
			EssentialLogger.quickInfo(handler.getSide().name() + ", " + handler.getSlot());
		}
	}

	public void clickOpenHanded(){
		EssentialLogger.quickInfo("About to check "+ handlers.size() +" of them" );
		int count = 0;
		for(IInventoryHandler handler : handlers){
			EssentialLogger.quickInfo("Checking number " + ++count);
			((InputInventoryHandler) handler).printContents();
		}
	}
    
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
			player.worldObj.markBlockForUpdate(pos);
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

	@Override
	public List<IInventoryHandler> getInventoryHandlers() {
		if(worldObj.getBlockState(pos).getValue(BlockCampfire.STATE) != BlockCampfire.CampfireState.DONE)
			return handlers;
		return new ArrayList<IInventoryHandler>();
	}
	
	public static enum EnumSlotPos {
		LEFT(new float[]{0.0f, 0.2f}, 
			 new float[]{0.0f, 1.0f}),
		RIGHT(new float[]{0.7f, 1.0f}, 
			  new float[]{0.0f, 1.0f});
		
		private Vec3 minHit;
		private Vec3 maxHit;
		private Vec3 offset = new Vec3(0.3f, 0.2f, 0.3f);

		private EnumSlotPos(float[] x, float[] y){
			minHit = new Vec3(x[0], y[0], 0);
			maxHit = new Vec3(x[1], y[1], 0);
		}
		
		public Vec3 getMinHit(){
			return minHit;
		}

		public Vec3 getMaxHit(){
			return maxHit;
		}

		public Vec3 getOffset(EnumFacing side){
			int multX = 1; 
			int multZ = 1;
			if(side == EnumFacing.WEST || side == EnumFacing.SOUTH){
				multX = -1;
			}
			if(side == EnumFacing.NORTH || side == EnumFacing.WEST){
				multZ = -1;
			}
			EssentialLogger.quickInfo("Offset multipliers: " + multX + ", " + multZ);
			Vec3 newOffset = new Vec3(offset.xCoord * multX,offset.yCoord,offset.zCoord * multZ);
			return newOffset;
		}
		
		@SuppressWarnings("unused")
		public static EnumSlotPos getFromIndex(int index){
			for(int i = 0; i < values().length; i++){
				if(i == index){
					return values()[i];
				}
			}
			return null;
		}
	}
}
