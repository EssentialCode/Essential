package zenith.essential.common;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import zenith.essential.common.crafting.RecipeHandler;
import zenith.essential.common.lib.GeneralConstants;
import zenith.essential.common.proxy.CommonProxy;

@Mod(modid = GeneralConstants.MOD_ID, name = GeneralConstants.MOD_NAME, version = GeneralConstants.MOD_VERSION)
public class Essential{

	private static final EssentialLogger log = EssentialLogger.getLogger();
	
	@Instance(GeneralConstants.MOD_ID)
	public static Essential instance;

	@SidedProxy(serverSide = GeneralConstants.COMMON_PROXY, clientSide = GeneralConstants.CLIENT_PROXY)
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		log.info("PreInit started");
		proxy.preInit(event);
	}
	
	@EventHandler
    public void init(FMLInitializationEvent event){
		// some example code
		log.info("Init started");
		proxy.init(event);
		RecipeHandler.postInit();
    }

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		log.info("PostInit started");
		proxy.postInit(event);
	}
}
