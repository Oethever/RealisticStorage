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
            ".*grass",
            "minecraft:waterlily",
            "minecraft:deadbush",
            "minecraft:grass",
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
            ".*_fence",
            ".*_fence_gate",
            ".*_carpet.*",
            ".*trapdoor",
            ".*_torch",
            ".*_button",
            ".*_pressure_plate",

            // Breakable blocks
            "minecraft:web",
            ".*glass.*",
            "minecraft:glowstone",
            "minecraft:bookshelf",
            "minecraft:melon_block",

            // Better with Mods
            ".*_siding",
            ".*_moulding",
            ".*_corner",
            ".*_hand_crank"
    };

    @Config.LangKey("realisticstorage.config.message.send")
    @Config.Comment({"If players should be notified about issues.",
            "True = Send the player a message. False = No message."
    })
    public static Boolean messageSend = true;

    @Config.LangKey("realisticstorage.config.message.text")
    @Config.Comment("The message displayed in-game when items are ejected from the inventory.")
    public static String messageText = "These blocks are too big for this container!";

    @Config.LangKey("realisticstorage.config.message.location")
    @Config.Comment({"Where the eject message is sent.",
            "True = Action bar message. False = Chat Message."
    })
    public static Boolean messageLocation = true;

    @Config.LangKey("realisticstorage.config.debug")
    @Config.Comment("Enable/Disable the debug logging of slots and container names.")
    public static Boolean debugLog = false;

    @Mod.EventBusSubscriber
    public static class SyncConfig {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(MODID)) {
                ConfigManager.sync(MODID, Config.Type.INSTANCE);
                INSTANCE.updateRegex();
            }
        }
    }
}