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
        ItemStack stack = event.getItem().getItem();
        if (stack.isEmpty()) {
            return;
        }
        int remaining = tryToFill(player.getInventory(), stack, false);
        if (remaining > 0) {
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
            tryToFill(inventory, stack, true);
            if (!stack.isEmpty()) {
                player.drop(stack, false);
                inventory.setItem(i, ItemStack.EMPTY);
            }
        }
    }

    public static boolean isLockedSlot(int slotIndex) {
        int slotLimit = Config.CONFIG.getSlotLimit();
        return slotLimit > -1 && slotIndex >= slotLimit && slotIndex < MAX_SLOTS;
    }
    
    private static int tryToFill(Inventory inventory, ItemStack stack, boolean commit) {
        int remaining = stack.getCount();
        for (int i = 0; i < Config.CONFIG.getSlotLimit(); ++i) {
            ItemStack otherStack = inventory.getItem(i);
            if (otherStack.isEmpty()) {
                if (commit) {
                    inventory.setItem(i, stack.copy());
                    stack.setCount(0);
                }
                return 0;
            } else if (otherStack.sameItem(stack)) {
                int count = Math.min(stack.getCount(), otherStack.getMaxStackSize() - otherStack.getCount());
                if (commit) {
                    otherStack.grow(count);
                    stack.shrink(count);
                }
                remaining -= count;
            }
            if (remaining <= 0) {
                return 0;
            }
        }
        return remaining;
    }
}
