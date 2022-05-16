package oethever.realisticstorage;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import oethever.realisticstorage.block.BlockPallet;

@Mod.EventBusSubscriber(
        modid = RealisticStorage.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = Dist.CLIENT
)
public class PalletEventHandler {
    private static final float FAST_BREAK_SPEED = 40;
    private static final int AREA_HEIGHT = 8;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void breakSpeedEvent(PlayerEvent.BreakSpeed event) {
        LevelAccessor world = event.getEntity().level;
        if (world.isClientSide()) {
            return;
        }
        if (isAbovePallet(world, event.getPos())) {
            event.setNewSpeed(FAST_BREAK_SPEED);
            event.setCanceled(false);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void breakEvent(BlockEvent.BreakEvent event) {
        LevelAccessor world = event.getWorld();
        if (world.isClientSide()) {
            return;
        }
        if (isAbovePallet(world, event.getPos())) {
            event.setCanceled(false);
            if (event.hasResult()) {
                event.setResult(Event.Result.ALLOW);
            }
        }
    }

    private static boolean isAbovePallet(LevelAccessor world, BlockPos pos) {
        if (pos == null) {
            return false;
        }
        RealisticStorage.LOGGER.info("Looking for pallet from " + pos.toShortString());
        for (int i = 1; i < AREA_HEIGHT; ++i) {
            BlockPos lowerPos = pos.below(i);
            if (world.getBlockState(lowerPos).getBlock() instanceof BlockPallet) {
                RealisticStorage.LOGGER.info("found pallet at " + lowerPos.toShortString());
                return true;
            }
        }
        return false;
    }

    //private static ItemStack getSilkTouchDrop(BlockState state) {
    //    return new ItemStack(state.getBlock().asItem());
    //}
}
