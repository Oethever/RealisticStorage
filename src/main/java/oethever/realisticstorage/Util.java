package oethever.realisticstorage;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Util {
    public static ItemEntity spawnItem(Level world, ItemStack itemStack, BlockPos pos) {
        ItemEntity entity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
        world.addFreshEntity(entity);
        return entity;
    }


    public static int tryToFillInventory(Inventory inventory, ItemStack stack, boolean commit) {
        int remaining = stack.getCount();
        int numberSlots = Config.CONFIG.getSlotLimit() == -1 ? Inventory.INVENTORY_SIZE : Config.CONFIG.getSlotLimit();
        for (int i = 0; i < numberSlots; ++i) {
            ItemStack otherStack = inventory.getItem(i);
            if (otherStack.isEmpty()) {
                if (commit) {
                    inventory.setItem(i, stack.copy());
                    stack.setCount(0);
                }
                return 0;
            } else if (otherStack.sameItem(stack)) {
                int count = Math.min(stack.getCount(), otherStack.getMaxStackSize() - otherStack.getCount());
                if (count > 0 && commit) {
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
