package oethever.realisticstorage;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("realisticstorage")
public class RealisticStorage
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    ContainerGuard containerGuard = new ContainerGuard(LOGGER);

    public RealisticStorage() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(containerGuard);
        Config.init();
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here

        }
    }
}
