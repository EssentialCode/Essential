package zenith.essential.common.tile;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import zenith.essential.api.essence.EnumEssenceAltarTier;
import zenith.essential.api.essence.EnumEssenceType;
import zenith.essential.api.essence.IEssenceReceiver;
import zenith.essential.api.essence.recipe.EssenceRecipeManager;
import zenith.essential.api.essence.recipe.IEssenceRecipe;
import zenith.essential.common.EssentialLogger;
import zenith.essential.common.block.BlockAltarAttuned;
import zenith.essential.common.capability.IInventoryHandler;
import zenith.essential.common.capability.InputInventoryHandler;
import zenith.essential.common.lib.EssentialInventoryHelper;

public class TileEntityAltar extends TileEntityBase implements ITickable, IEssenceReceiver{
	
	private int essence;
	private int workRemaining;
	private EnumEssenceType essenceType;
	private int altarTier;

	private int lastMeterOutput = 0;

	ItemStackHandler inputItemHandler; 
	ItemStackHandler workingItemHandler;
	ItemStackHandler outputItemHandler;	
	
	IInventoryHandler workingInvHandler;
	
	public TileEntityAltar(){
		inputItemHandler   = EssentialInventoryHelper.getItemStackHandler(1, this);
		workingItemHandler = EssentialInventoryHelper.getItemStackHandler(1, this);
		outputItemHandler  = EssentialInventoryHelper.getItemStackHandler(1, this);
		altarTier = 0;
		essence = EnumEssenceAltarTier.TIER_0.getBaseCost();
		workRemaining = 0;
		
		setupInventoryHandlers();
	}
	
	public TileEntityAltar(EnumEssenceType essenceType){
		this();
		this.essenceType = essenceType;
	}
	
	public int getMeterOutput(){
		int max = EnumEssenceAltarTier.getTier(altarTier).getCapacity();
		int output = (int) Math.floor((float) essence / (float) max * 10);
		return output;
	}
	
	private void setupInventoryHandlers(){
		Random rand = new Random();
		addInterfaceHandle(
					new InputInventoryHandler(inputItemHandler, 
							0, new Vec3(0.3, 1, 0.3), 
							new Vec3(0.7, 1.4, 0.7), 
							new Vec3(0,1.1,0))
							.setRotationOffset(rand.nextInt(360))
						);
		workingInvHandler = new InputInventoryHandler(workingItemHandler, 
									0, new Vec3(0.3, 1, 0.3), 
									new Vec3(0.7, 1.4, 0.7), 
									new Vec3(0,1.1,0))
									.setRotationOffset(rand.nextInt(360));
		addInterfaceHandle(workingInvHandler);
		addInterfaceHandle(
					new InputInventoryHandler(outputItemHandler, 
							0, new Vec3(0.3, 1.6, 0.3), 
							new Vec3(0.7, 1.8, 0.7), 
							new Vec3(0,1.7,0))
							.setRotationOffset(rand.nextInt(360))
						);
	}

	@SideOnly(Side.CLIENT)
	public ResourceLocation getEmblem(){
		return worldObj.getBlockState(pos).getValue(BlockAltarAttuned.ESSENCE_TYPE).getEmblem();
	}

    
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != EnumFacing.UP)
            return true;
        return super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != EnumFacing.UP){
        	if(facing == EnumFacing.DOWN){
        		return (T) outputItemHandler;
        	}
        	return (T) inputItemHandler;
        }
        return super.getCapability(capability, facing);
    }

    public boolean activatedByPlayer(EntityPlayer player){
    	EssentialLogger.quickInfo("Current essence: " + essence + "/" + EnumEssenceAltarTier.getTier(altarTier).getCapacity() );
    	EssentialLogger.quickInfo("Meter output: " + getMeterOutput());
			EssentialLogger.quickInfo("input: " + (getInputStack() == null ? "empty" : getInputStack().getDisplayName() + " (" + getInputStack().stackSize + ")"));
			EssentialLogger.quickInfo("working: " + (getWorkingStack() == null ? "empty" : getWorkingStack().getDisplayName() + " (" + getWorkingStack().stackSize + ")"));
			EssentialLogger.quickInfo("output: " + (getOutputStack() == null ? "empty" : getOutputStack().getDisplayName() + " (" + getOutputStack().stackSize + ")"));
    	if(!player.worldObj.isRemote){
			ItemStack stack = player.getCurrentEquippedItem();
    		if(player.isSneaking()){
				return removeItemByPlayer(player, true);
    		}
			// make sure the stack exists
			if(!stackIsMeaningful(stack)){
				return removeItemByPlayer(player);
			}
			// Make sure it can transmute in this altar
			if(EssenceRecipeManager.findMatch(stack, essenceType) == null) {
				return false;
			}

    		ItemStack newStack = ItemHandlerHelper.insertItem(inputItemHandler, ItemStack.copyItemStack(stack), false);
			stack.stackSize = newStack != null ? newStack.stackSize : 0;
			player.worldObj.markBlockForUpdate(pos);
    	}
    	return true;
    }
    
    private boolean removeItemByPlayer(EntityPlayer player){ return removeItemByPlayer(player, false); }
	private boolean removeItemByPlayer(EntityPlayer player, boolean preserveOutput) {
		if(!preserveOutput){
			ItemStack outputStack = getOutputStack();
			if(stackIsMeaningful(outputStack)){
				outputStack = ItemHandlerHelper.copyStackWithSize(outputStack, outputStack.stackSize);
				giveItemToPlayer(player, outputStack);
				if(outputStack.stackSize > 0){
					EssentialInventoryHelper.spewItemStack(outputStack, player.worldObj, pos);
				}
				outputItemHandler.setStackInSlot(0, null);
				return true;
			}
		}
		if(player.isSneaking()){
			// we want to remove one item at a time
			if(hasWork()){
				ItemStack stack = getInputStack();
				if(giveItemToPlayer(player, ItemHandlerHelper.copyStackWithSize(stack, 1))){
					stack.stackSize = Math.max(stack.stackSize -1, 0);
					if(!stackIsMeaningful(stack)){
						inputItemHandler.setStackInSlot(0, null);
						// TODO: here
					}
				}
			}
		}

		return false;
	}
	
	private ItemStack getInputStack(){
		return inputItemHandler.getStackInSlot(0);
	}

	private ItemStack getWorkingStack(){
		return workingItemHandler.getStackInSlot(0);
	}

	private ItemStack getOutputStack(){
		return outputItemHandler.getStackInSlot(0);
	}
	
	private boolean giveItemToPlayer(EntityPlayer player, ItemStack stack){ 
		return  giveItemToPlayer(player, stack, stack.getMaxStackSize());
	}
	private boolean giveItemToPlayer(EntityPlayer player, ItemStack stack, int max){
		ItemStack playerStack = player.getCurrentEquippedItem();
		// If the player is already holding the output, just give it to them.
		boolean given = false;
		if(ItemHandlerHelper.canItemStacksStack(playerStack, stack)){
			if(playerStack.stackSize < playerStack.getMaxStackSize()){
				int delta = Math.min(max, Math.min(playerStack.getMaxStackSize() - playerStack.stackSize, stack.stackSize));
				if(delta > 0){
					playerStack.stackSize += delta;
					stack.stackSize -= delta;
					given = true;
				}
			}
		}
		return given;
		
	}

	@Override
	public void update() {
		if(essenceType == null){
			this.essenceType = worldObj.getBlockState(pos).getValue(BlockAltarAttuned.ESSENCE_TYPE);
		}
		
		if(getMeterOutput() != lastMeterOutput){
			lastMeterOutput = getMeterOutput();
			if(worldObj.isRemote){
				worldObj.markBlockForUpdate(pos);
			}
			worldObj.checkLightFor(EnumSkyBlock.BLOCK, pos);
		}

		EnumEssenceAltarTier tier = EnumEssenceAltarTier.getTier(altarTier);
		boolean wasWorking = false;

		// generate essence
		if(essence < tier.getCapacity()){
			this.essence = Math.min(essence + tier.getRegenRate(), tier.getCapacity());
		}
		
		// go to work (maybe)
		if(isWorking() && false){
			wasWorking = true;
			float heightDest = 1.7f;
			float starting = 1.1f;
			float workingProgress = (float) workRemaining / (float) currentRecipe().getCost();
			float actualHeight = heightDest - ((heightDest - starting) * workingProgress);
			((InputInventoryHandler) workingInvHandler).setRenderOffset(new Vec3(0, actualHeight, 0 ));
			int maxWorkingEssence = Math.min(essence, tier.getRegenRate() * 2);
			int essenceToUse = Math.min(workRemaining, maxWorkingEssence);
			workRemaining -= essenceToUse;
			essence = Math.max(essence - essenceToUse, 0);
			
			if(workRemaining <= 0){
				ItemStack outputStack = getOutputStack();
				if(!stackIsMeaningful(outputStack) || 
						outputStack.stackSize + currentRecipeYield() < outputStack.getMaxStackSize()){
					ItemStack resultStack = currentRecipe().getResult();
					if(outputStack == null){
						outputStack = ItemHandlerHelper.copyStackWithSize(resultStack, resultStack.stackSize);
					} else {
						outputStack = ItemHandlerHelper.copyStackWithSize(outputStack, outputStack.stackSize + resultStack.stackSize);
						
					}
					workingItemHandler.setStackInSlot(0, null);
					outputItemHandler.setStackInSlot(0, outputStack);
				}
			}
		} else if(!wasWorking && hasWork() && canWork()){
			ItemStack inputStack = getInputStack();
			ItemStack workStack = ItemHandlerHelper.copyStackWithSize(inputStack, 1);
			inputStack.stackSize -= 1;
			workRemaining = currentRecipe().getCost();

			if(inputStack.stackSize <= 0){
				inputItemHandler.setStackInSlot(0, null);
			}
			workingItemHandler.setStackInSlot(0, workStack);
		}

		this.essence = Math.min(essence, EnumEssenceAltarTier.getTier(altarTier).getCapacity());
	}
	
	public boolean isWorking(){
		return stackIsMeaningful(getWorkingStack());
	}
	
	public boolean hasWork(){
		return stackIsMeaningful(getInputStack());
	}
	
	private boolean stackIsMeaningful(ItemStack stack){
		return stack != null && stack.stackSize > 0;
	}
	
	public IEssenceRecipe currentRecipe(){
		if(getWorkingStack() != null){
			return EssenceRecipeManager.findMatch(getWorkingStack(), essenceType);
		}
		return EssenceRecipeManager.findMatch(getInputStack(), essenceType);
	}
	
	private int currentRecipeYield(){
		IEssenceRecipe current = currentRecipe();
		if(current == null){
			return 0;
		}
		return current.getResult().stackSize;
	}
	
	private boolean canWork(){
		if(EssenceRecipeManager.findMatch(getInputStack(), essenceType) == null){
			return false;
		}
		ItemStack outputStack = getOutputStack();
		int recipeYield = currentRecipe().getResult().stackSize;
		return outputStack == null ||
				(currentRecipe().outputMatches(outputStack) && outputStack.stackSize + recipeYield <= outputStack.getMaxStackSize());
				
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        inputItemHandler.deserializeNBT(compound.getCompoundTag("inputInventory"));
        workingItemHandler.deserializeNBT(compound.getCompoundTag("workingInventory"));
        outputItemHandler.deserializeNBT(compound.getCompoundTag("outputInventory"));
        if(compound.hasKey("essence")){
			this.essence = compound.getInteger("essence");
        }
        if(compound.hasKey("workRemaining")){
			this.workRemaining = compound.getInteger("workRemaining");
        }
        if(compound.hasKey("altarTier")){
			this.altarTier = compound.getInteger("altarTier");
        }
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {

        super.writeToNBT(compound);
        compound.setTag("inputInventory", inputItemHandler.serializeNBT());
        compound.setTag("workingInventory", workingItemHandler.serializeNBT());
        compound.setTag("outputInventory", outputItemHandler.serializeNBT());
        compound.setInteger("essence", essence);
        compound.setInteger("workRemaining", workRemaining);
        compound.setInteger("altarTier", altarTier);
	}
	
	private EnumEssenceAltarTier getTier(){
		return EnumEssenceAltarTier.getTier(altarTier);
	}
	
	public int getMaxEssence(){
		return getTier().getCapacity();
	}

	@Override
	public int getEssenceLevel() {
		return essence;
	}

	@Override
	public int getMaxAcceptableEssence() {
		return getTier().getCapacity() - essence;
	}

	@Override
	public boolean canAcceptEssence(int amount) {
		return getTier().getCapacity() - essence >= amount;
	}

	@Override
	public boolean sendEssence(int amount) {
		if(canAcceptEssence(amount)){
			this.essence += amount;
			worldObj.markBlockForUpdate(pos);
			return true;
		}
		return false;
	}
	
	public EnumEssenceType getEssenceType(){
		return essenceType;
	}
	
}	
