package oethever.realisticstorage.handlers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import oethever.realisticstorage.RealisticStorage;
import oethever.realisticstorage.block.BlockPallet;
import oethever.realisticstorage.block.Util;

@Mod.EventBusSubscriber(
        modid = RealisticStorage.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class PalletEventHandler {
    private static final int AREA_HEIGHT = 8;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void breakSpeedEvent(PlayerInteractEvent.LeftClickBlock event) {
        Level world = event.getEntity().level;
        if (world.isClientSide()) {
            return;
        }
        BlockPos pos = event.getPos();
        if (isAbovePallet(world, pos)) {
            event.setUseBlock(Event.Result.DENY);
            event.setUseItem(Event.Result.DENY);
            event.setCanceled(true);
            Util.spawnItem(world, getSilkTouchDrop(world.getBlockState(pos)), pos);
            world.removeBlock(pos, false);
        }
    }

    private static boolean isAbovePallet(LevelAccessor world, BlockPos pos) {
        if (pos == null) {
            return false;
        }
        for (int i = 1; i < AREA_HEIGHT + 1; ++i) {
            BlockPos lowerPos = pos.below(i);
            if (world.getBlockState(lowerPos).getBlock() instanceof BlockPallet) {
                return true;
            }
        }
        return false;
    }

    private static ItemStack getSilkTouchDrop(BlockState state) {
        return new ItemStack(state.getBlock().asItem());
    }
}
