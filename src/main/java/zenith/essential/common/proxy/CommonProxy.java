package zenith.essential.common.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import zenith.essential.common.event.ForgeEventHandlers;
import zenith.essential.common.item.EssentialItems;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event){
		EssentialItems.init();
	}

	public void init(FMLInitializationEvent event){
		
		MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
	}

	public void postInit(FMLPostInitializationEvent event) {
		// TODO Auto-generated method stub
		
	}
}
