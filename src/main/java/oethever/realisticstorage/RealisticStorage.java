package oethever.realisticstorage;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oethever.realisticstorage.blocks.BlockPallet;
import oethever.realisticstorage.blocks.TileEntityPallet;
import oethever.realisticstorage.containerguard.ContainerGuard;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = RealisticStorage.MODID,
        name = RealisticStorage.NAME,
        version = RealisticStorage.VERSION
)
public class RealisticStorage {
    public static final String MODID = "realisticstorage";
    public static final String NAME = "Realistic Storage";
    public static final String VERSION = "1.1";

    @Mod.Instance(MODID)
    public static RealisticStorage INSTANCE;

    private static Logger logger;
    private ContainerGuard containerGuard;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        containerGuard = new ContainerGuard(logger);
    }

    public void updateConfig() {
        containerGuard.updateConfig();
    }
}
