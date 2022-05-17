package oethever.realisticstorage;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(RealisticStorage.MOD_ID)
@Mod.EventBusSubscriber(
        modid = RealisticStorage.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class RealisticStorage {
    public static final String MOD_ID = "realisticstorage";
    public static final Logger LOGGER = LogManager.getLogger();

    public RealisticStorage() {
        Registry.register();
        Config.init();
    }

    @SubscribeEvent
    public static void onConfigLoading(ModConfigEvent.Loading event) {
        ContainerEventHandler.updateConfig();
    }

    @SubscribeEvent
    public static void onConfigReloading(ModConfigEvent.Reloading event) {
        ContainerEventHandler.updateConfig();
    }
}
