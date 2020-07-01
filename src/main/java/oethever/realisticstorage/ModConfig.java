package oethever.realisticstorage;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static oethever.realisticstorage.RealisticStorage.MODID;
import static oethever.realisticstorage.RealisticStorage.INSTANCE;

@Config(modid = MODID, category = "All")
@Mod.EventBusSubscriber(modid = MODID)
@Config.LangKey("realisticstorage.config.title")
public class ModConfig {
    @Config.LangKey("realisticstorage.config.checked.containers")
    @Config.Comment("Full Class path names of inventories that are checked for oversize items. Turn on debug log to get these names.")
    public static String[] checkedContainers = {
            "net.minecraft.inventory.ContainerChest",
            "blusunrize.immersiveengineering.common.gui.ContainerToolbox",
            "blusunrize.immersiveengineering.common.gui.ContainerCrate",
            "jds.bibliocraft.containers.ContainerFramedChest",
            "com.alcatrazescapee.notreepunching.common.container.ContainerLargeVessel",
            "charcoalPit.gui.ContainerCeramicPot"
    };

    @Config.LangKey("realisticstorage.config.ignored.slots")
    @Config.Comment("Class name of slots that are never checked. Turn on debug log to get these names. Do not enter InventoryBasic slots here!")
    public static String[] ignoredSlots = {
            "net.minecraft.entity.player.InventoryPlayer",
            "net.minecraft.inventory.InventoryCrafting",
            "net.minecraft.inventory.InventoryCraftResult",
            "net.minecraft.entity.item.EntityMinecartChest"
    };

    @Config.LangKey("realisticstorage.config.items.always")
    @Config.Comment("Items that are always ejected. F3+H to get names.")
    public static String[] alwaysEjectedItems = {
            "minecraft:armor_stand",
            "minecraft:bed",
            "minecraft:brewing_stand",
            "minecraft:cauldron",
            ".*boat",
            ".*_door",
            ".*minecart",
            ".*skull",
    };

    @Config.LangKey("realisticstorage.config.items.never")
    @Config.Comment("Items that are never ejected. F3+H to get names.")
    public static String[] neverEjectedItems = {
            // Plants
            ".*_mushroom.*",
            ".*_flower",
            ".*sapling",
            "minecraft:tallgrass",
            "minecraft:waterlily",
            "minecraft:deadbush",
            "minecraft:vine",
            ".*_plant",

            // Furniture
            "minecraft:barrier",
            "minecraft:iron_bars",
            "minecraft:ladder",
            "minecraft:sign",
            "minecraft:painting",
            "minecraft:tripwire_hook",
            "minecraft:lever",
            ".*fence",
            ".*fence_gate",
            ".*carpet.*",
            ".*trapdoor",
            ".*torch",
            ".*_button",
            ".*_pressure_plate",

            // Breakable blocks
            "minecraft:web",
            ".*glass.*",
            "minecraft:glowstone",
            "minecraft:bookshelf",
            "minecraft:melon_block",

            // Better with Mods
            "betterwithmods:siding_.*",
            "betterwithmods:moulding_.*",
            "betterwithmods:corner_.*",
            "betterwithmods:hand_crank",
            "betterwithmods:.*_axle",
            "betterwithmods:wicker",
            "betterwithmods:grate",
            "betterwithmods:slats",
            "betterwithmods:rope",
            "betterwithmods:urn",
            "betterwithmods:unfired_pottery:2",
            "betterwithmods:unfired_pottery:4",
            "betterwithmods:unfired_pottery:5",
            "betterwithmods:bamboo_chime",
            "betterwithmods:candle",
            "betterwithmods:candle_holder",
            "betterwithmods:hemp",

            // Bibliocraft
            "bibliocraft:fancysign",
            "bibliocraft:label",
            "bibliocraft:mapframe",
            "bibliocraft:paintingframeflat",
            "bibliocraft:paintingframesimple",
            "bibliocraft:paintingframemiddle",
            "bibliocraft:paintingframefancy",
            "bibliocraft:paintingframeborderless",
            "bibliocraft:typewriter",
            "bibliocraft:bell",
            "bibliocraft:dinnerplate",
            "bibliocraft:cookiejar",
            "bibliocraft:discrack",
            "bibliocraft:lanterngold",
            "bibliocraft:lanterniron",
            "bibliocraft:lampgold",
            "bibliocraft:lampiron",

            // Immersive Engineering
            "immersiveengineering:wooden_decoration:0",
            "immersiveengineering:wooden_device1:4",
            "immersiveengineering:cloth_device:1",
            "immersiveengineering:cloth_device:2",
            "immersiveengineering:metal_decoration1:0",
            "immersiveengineering:metal_decoration1:4",
            "immersiveengineering:metal_decoration2:1",
            "immersiveengineering:metal_decoration2:3",
            "immersiveengineering:metal_decoration2:4",
            "immersiveengineering:metal_ladder:0",
            "immersiveengineering:connector:0",
            "immersiveengineering:connector:1",
            "immersiveengineering:connector:2",
            "immersiveengineering:connector:3",
            "immersiveengineering:connector:4",
            "immersiveengineering:connector:5",
            "immersiveengineering:connector:6",
            "immersiveengineering:connector:9",
            "immersiveengineering:connector:12",
            "immersiveengineering:connector:13",
            "immersiveengineering:metal_device1:4",

            // Realistic torches
            ".*:torch.*",

            // Inspirations
            "inspirations:path",
            "inspirations:rope",
            "inspirations:pipe",

            // Tinker construct / ceramics
            "ceramics:faucet",
            "ceramics:channel",
            "tconstruct:faucet",
            "tconstruct:channel",
            "tconstruct:rack",
            "tconstruct:stone_ladder",

            // Dawn of Time: Builder Edition
            "dawnoftimebuilder:rice",
            "dawnoftimebuilder:camellia_seed"
    };

    @Config.LangKey("realisticstorage.config.message.send")
    @Config.Comment("True if players should be notified about issues.")
    public static Boolean messageSend = true;

    @Config.LangKey("realisticstorage.config.message.text")
    @Config.Comment("The message displayed in-game when items are ejected from the inventory.")
    public static String messageText = "These blocks are too big for this container!";

    @Config.LangKey("realisticstorage.config.message.location")
    @Config.Comment({"Where the eject message is sent.",
            "True = Action bar message. False = Chat Message."
    })
    public static Boolean messageLocation = true;

    @Config.LangKey("realisticstorage.config.addpallet")
    @Config.Comment("True if the Pallet block should be added to the game.")
    @Config.RequiresMcRestart
    public static Boolean addPallet = true;

    @Config.LangKey("realisticstorage.config.debug")
    @Config.Comment("Enable/Disable the debug logging of slots and container names.")
    public static Boolean debugLog = false;

    @Mod.EventBusSubscriber
    public static class SyncConfig {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(MODID)) {
                ConfigManager.sync(MODID, Config.Type.INSTANCE);
                INSTANCE.updateConfig();
            }
        }
    }
}