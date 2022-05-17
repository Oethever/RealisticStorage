package oethever.realisticstorage;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;


public class Config {
    public static final ForgeConfigSpec CONFIG_SPEC;
    public static final Config CONFIG;
    private final ForgeConfigSpec.BooleanValue sendMessage;
    private final ForgeConfigSpec.BooleanValue debugLog;
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> checkedContainers;
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> alwaysEjected;
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> neverEjected;

    static {
        Pair<Config,ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Config::new);
        CONFIG_SPEC = specPair.getRight();
        CONFIG = specPair.getLeft();
    }

    Config(ForgeConfigSpec.Builder builder) {
        sendMessage = builder
                .comment("True if players should be notified when blocks are ejected.")
                .define("send_message", true);
        debugLog = builder
                .comment("Enable/Disable the debug logging of slots and container names.")
                .define("debug_log", false);
        checkedContainers = builder.
                comment("Short class names of inventories that are checked for oversize items. Turn on debug log to get these names.")
                .defineList("checked_container", Arrays.asList(
                    "ChestBlockEntity",
                    "TrappedChestBlockEntity",
                    "PlayerEnderChestContainer",
                    "BarrelBlockEntity",
                    "DispenserBlockEntity",
                    "ShulkerBoxBlockEntity",
                    "DropperBlockEntity"
                ), o -> true);
        alwaysEjected = builder.
                comment("Items that are always ejected. F3+H to get names.")
                .defineList("always_ejected", Arrays.asList(
                    "minecraft:armor_stand",
                    ".*_bed",
                    ".*_banner",
                    "minecraft:brewing_stand",
                    "minecraft:cauldron",
                    ".*boat",
                    ".*_door",
                    ".*minecart",
                    ".*_skull",
                    ".*_head"
                ), o -> true);
        neverEjected = builder.
                comment("Items that are never ejected. F3+H to get names.")
                .defineList("never_ejected", Arrays.asList(
                    // Plants
                    ".*_sapling",
                    "minecraft:cobweb",
                    ".*_mushroom",
                    "minecraft:grass",
                    "minecraft:fern",
                    ".*azalea",
                    "minecraft:dead_bush",
                    "minecraft:seagrass",
                    "minecraft:sea_pickle",
                    "minecraft:dandelion",
                    "minecraft:poppy",
                    "minecraft:blue_orchid",
                    "minecraft:allium",
                    "minecraft:azure_bluet",
                    "minecraft:.*vines",
                    ".*_tulip",
                    "minecraft:oxeye_daisy",
                    "minecraft:cornflower",
                    "minecraft:lily_of_the_valley",
                    "minecraft:wither_rose",
                    "minecraft:spore_blossom",
                    ".*_fungus",
                    ".*_roots",
                    "minecraft:nether_sprouts",
                    "minecraft:vine",
                    "minecraft:sugar_cane",
                    "minecraft:kelp",
                    ".*_dripleaf",
                    "minecraft:bamboo",
                    "minecraft:glow_lichen",
                    "minecraft:lily_pad",
                    "minecraft:sunflower",
                    "minecraft:lilac",
                    "minecraft:rose_bush",
                    "minecraft:peony",
                    "minecraft:tall_grass",
                    "minecraft:large_fern",
                    ".*_coral",
                    ".*_coral_fan",

                    // Furniture
                    ".*fence",
                    ".*torch",
                    "minecraft:ladder",
                    "minecraft:snow",
                    "minecraft:iron_bars",
                    "minecraft:chain",
                    ".*glass_pane",
                    ".*carpet.*",
                    "minecraft:painting",
                    ".*_sign",
                    "minecraft:item_frame",
                    "minecraft:glow_item_frame",
                    "minecraft:flower_pot",
                    "minecraft:end_crystal",
                    "minecraft:bell",
                    ".*lantern",
                    ".*candle",
                    ".*_amethyst_bud",
                    "minecraft:amethyst_cluster",
                    "minecraft:pointed_dripstone",

                    // Redstone
                    "minecraft:lever",
                    "minecraft:lightning_rod",
                    "minecraft:tripwire_hook",
                    "minecraft:repeater",
                    "minecraft:comparator",
                    ".*_fence_gate",
                    ".*_trapdoor",
                    ".*_button",
                    ".*_pressure_plate"
                ), o -> true);
    }

    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CONFIG_SPEC);
    }

    public boolean getSendMessage() {
        return sendMessage.get();
    }

    public boolean getDebugLog() {
        return debugLog.get();
    }

    public List<String> getCheckedContainers() {
        return (List<String>) checkedContainers.get();
    }

    public List<String> getAlwaysEjected() {
        return (List<String>) alwaysEjected.get();
    }

    public List<String> getNeverEjected() {
        return (List<String>) neverEjected.get();
    }
}