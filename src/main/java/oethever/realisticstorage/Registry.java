package oethever.realisticstorage;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import oethever.realisticstorage.block.PalletBlock;
import oethever.realisticstorage.blockentity.PalletBlockEntity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Registry {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RealisticStorage.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RealisticStorage.MOD_ID);
    private  static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, RealisticStorage.MOD_ID);
    private static final String[] WOOD_TYPES = {
            "oak",
            "jungle",
            "acacia",
            "spruce",
            "dark_oak",
            "warped",
            "birch",
            "crimson"
    };
    public static final List<RegistryObject<Block>> BLOCK_PALLETS = Arrays.stream(WOOD_TYPES).map(woodType ->
            BLOCKS.register(woodType + "_pallet", () -> (Block)new PalletBlock())).toList();

    public static final List<RegistryObject<Item>> ITEM_PALLETS = IntStream.range(0, WOOD_TYPES.length).mapToObj(i ->
            ITEMS.register(WOOD_TYPES[i].concat("_pallet"), () -> (Item)new BlockItem(
                    BLOCK_PALLETS.get(i).get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)
            ))).toList();

    public static final RegistryObject<BlockEntityType<PalletBlockEntity>> PALLET_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
        "pallet",
        () -> BlockEntityType.Builder.of(
            PalletBlockEntity::new,
            BLOCK_PALLETS.stream().map(RegistryObject::get).toArray(PalletBlock[]::new)
        ).build(null)
    );

    public static void register() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
    }
}
