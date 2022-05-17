package oethever.realisticstorage.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oethever.realisticstorage.ModConfig;
import oethever.realisticstorage.RealisticStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid= RealisticStorage.MODID)
public class ModBlocks {

    private static final List<String> woodTypes = Arrays.asList("oak", "spruce", "dark_oak", "acacia", "birch", "jungle");
    private static final List<Block> blockPallets = new ArrayList<>();
    static {
        for (String woodType : woodTypes) {
            blockPallets.add(new BlockPallet("pallet_" + woodType));
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        if (ModConfig.addPallet) {
            for (Block blockPallet : blockPallets) {
                event.getRegistry().register(blockPallet);
            }
            GameRegistry.registerTileEntity(TileEntityPallet.class,
                    new ResourceLocation("realisticstorage", "pallet"));
        }
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        if (ModConfig.addPallet) {
            for (Block blockPallet : blockPallets) {
                event.getRegistry().registerAll(new ItemBlock(blockPallet).setRegistryName(blockPallet.getRegistryName()));
            }
        }
    }

    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent event) {
        if (ModConfig.addPallet) {
            for (Block blockPallet : blockPallets) {
                registerRender(Item.getItemFromBlock(blockPallet));
            }
        }
    }

    public static void registerRender(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}