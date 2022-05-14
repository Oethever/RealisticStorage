package oethever.realisticstorage;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(RealisticStorage.MOD_ID)
public class RealisticStorage
{
    public static final String MOD_ID = "realisticstorage";
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ContainerGuard containerGuard = new ContainerGuard(LOGGER);
    private final Registry registry;

    public RealisticStorage() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(containerGuard);
        this.registry = new Registry();
        Config.init();
    }
}
