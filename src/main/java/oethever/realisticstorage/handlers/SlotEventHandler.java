package oethever.realisticstorage.handlers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import oethever.realisticstorage.Config;
import oethever.realisticstorage.RealisticStorage;

// Code inspired from the mod TrophySlot by Lomeli12, under GNU Lesser General Public License v3.0.
// https://github.com/Lomeli12/TrophySlots
@Mod.EventBusSubscriber(
        modid = RealisticStorage.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class SlotEventHandler {
    public static int MAX_SLOTS = 4 * 9;

    @SubscribeEvent
    public static void onEntityItemPickup(EntityItemPickupEvent event) {
        if (Config.CONFIG.getSlotLimit() == -1) {
            return;
        }
        Player player = event.getPlayer();
        if (player.level.isClientSide() || player.isCreative() || player.isDeadOrDying()) {
            return;
        }
        if (event.getItem().getItem().isEmpty()) {
            return;
        }
        Inventory inventory = player.getInventory();
        int emptySlot = getFirstEmptySlot(inventory);
        if (emptySlot == -1) {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (Config.CONFIG.getSlotLimit() == -1) {
            return;
        }
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Player player = event.player;
        if (player.level.isClientSide() || player.isCreative() || player.isDeadOrDying()) {
            return;
        }
        Inventory inventory = player.getInventory();
        for (int i = Config.CONFIG.getSlotLimit(); i < Math.min(MAX_SLOTS, inventory.getContainerSize()); ++i) {
            ItemStack stack = inventory.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            int emptySlot = getFirstEmptySlot(inventory);
            if (emptySlot == -1) {
                player.drop(stack, false);
            } else {
                inventory.setItem(emptySlot, stack);
            }
            inventory.setItem(i, ItemStack.EMPTY);
        }
    }

    private static int getFirstEmptySlot(Inventory inventory) {
        for (int i = 0; i < Math.min(Config.CONFIG.getSlotLimit(), inventory.getContainerSize()); ++i) {
            ItemStack stack = inventory.getItem(i);
            if (stack.isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isLockedSlot(int slotIndex) {
        int slotLimit = Config.CONFIG.getSlotLimit();
        return slotLimit > -1 && slotIndex >= slotLimit && slotIndex < MAX_SLOTS;
    }
}
