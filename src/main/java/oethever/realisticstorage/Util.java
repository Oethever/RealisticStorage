package oethever.realisticstorage;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Util {
    public static ItemEntity spawnItem(Level world, ItemStack itemStack, BlockPos pos) {
        ItemEntity entity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
        world.addFreshEntity(entity);
        return entity;
    }
}
