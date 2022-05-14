package oethever.realisticstorage;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(RealisticStorage.MOD_ID)
@Mod.EventBusSubscriber(
        modid = RealisticStorage.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class RealisticStorage {
    public static final String MOD_ID = "realisticstorage";
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ContainerGuard containerGuard = new ContainerGuard(LOGGER);

    public RealisticStorage() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(containerGuard);
        Registry.register();
        Config.init();
    }
}
