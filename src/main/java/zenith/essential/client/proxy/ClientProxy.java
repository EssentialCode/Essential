package zenith.essential.client.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import zenith.essential.client.fx.EssentialParticleHandler;
import zenith.essential.client.fx.FXEssence;
import zenith.essential.client.handler.TickHandler;
import zenith.essential.common.block.EssentialBlocks;
import zenith.essential.common.item.EssentialItems;
import zenith.essential.common.proxy.CommonProxy;

public class ClientProxy extends CommonProxy {
	public static final TickHandler TICK_HANDLER = new TickHandler();

	@Override
	public void preInit(FMLPreInitializationEvent event){
		super.preInit(event);
		EssentialItems.initModels();
		EssentialBlocks.initModels();
	}
	
	@Override
	public void init(FMLInitializationEvent event){
		super.init(event);
		MinecraftForge.EVENT_BUS.register(TICK_HANDLER);
		MinecraftForge.EVENT_BUS.register(new EssentialParticleHandler());
	}

	@Override
	public void essenceFX(World world, double x, double y, double z, float r, float g, float b, float motionx, float motiony, float motionz, float size, int m) {
		if(!doParticle(world))
			return;

		FXEssence essenceFX = new FXEssence(world, x, y, z, size, r, g, b, m);
		essenceFX.setSpeed(motionx, motiony, motionz);
		Minecraft.getMinecraft().effectRenderer.addEffect(essenceFX);
	}

	private boolean doParticle(World world) {
		if(!world.isRemote)
			return false;

//		if(!ConfigHandler.useVanillaParticleLimiter)
//			return true;

		float chance = 1F;
		if(Minecraft.getMinecraft().gameSettings.particleSetting == 1)
			chance = 0.6F;
		else if(Minecraft.getMinecraft().gameSettings.particleSetting == 2)
			chance = 0.2F;

		return chance == 1F || Math.random() < chance;
	}

}
