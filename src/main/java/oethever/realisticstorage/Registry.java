package oethever.realisticstorage;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import oethever.realisticstorage.block.BlockPallet;

public class Registry {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RealisticStorage.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RealisticStorage.MOD_ID);

    public Registry() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

        String[] woodTypes = {
                "oak",
                "jungle",
                "acacia",
                "spruce",
                "dark_oak",
                "warped",
                "birch",
                "crimson"
        };
        for (String woodType : woodTypes) {
            String palletName = woodType + "_pallet";
            BlockPallet blockPallet = new BlockPallet();
            BLOCKS.register(palletName, () -> blockPallet);
            ITEMS.register(palletName, () -> new BlockItem(
                    blockPallet,
                    new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS))
            );
        }
    }
}