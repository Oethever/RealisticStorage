package oethever.realisticstorage.handlers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import oethever.realisticstorage.RealisticStorage;
import oethever.realisticstorage.Registry;
import oethever.realisticstorage.block.PalletBlock;
import oethever.realisticstorage.Util;
import oethever.realisticstorage.blockentity.PalletBlockEntity;

import java.util.Optional;

@Mod.EventBusSubscriber(
        modid = RealisticStorage.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class PalletEventHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        Level world = event.getEntity().level;
        if (world.isClientSide()) {
            return;
        }
        BlockPos pos = event.getPos();
        Optional<PalletBlockEntity> pallet = getPalletBelow(world, pos);
        if (pallet.isPresent() && !pallet.get().wasPlacedBefore(pos)) {
            event.setUseBlock(Event.Result.DENY);
            event.setUseItem(Event.Result.DENY);
            event.setCanceled(true);
            Util.spawnItem(world, getSilkTouchDrop(world.getBlockState(pos)), pos);
            world.removeBlock(pos, false);
        }
    }

    @SubscribeEvent()
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        Level world = event.getEntity().level;
        if (world.isClientSide()) {
            return;
        }
        BlockPos pos = event.getPos();
        Optional<PalletBlockEntity> pallet = getPalletBelow(world, pos);
        if (pallet.isPresent()) {
            pallet.get().setPlaced(pos);
        }
    }

    private static Optional<PalletBlockEntity> getPalletBelow(LevelAccessor world, BlockPos pos) {
        if (pos == null) {
            return Optional.empty();
        }
        for (int i = 0; i < PalletBlockEntity.AREA_HEIGHT; ++i) {
            BlockPos lowerPos = pos.below(i + 1);
            Optional<PalletBlockEntity> pallet = world.getBlockEntity(lowerPos, Registry.PALLET_BLOCK_ENTITY.get());
            if (pallet.isPresent()) {
                return pallet;
            }
        }
        return Optional.empty();
    }

    private static ItemStack getSilkTouchDrop(BlockState state) {
        return new ItemStack(state.getBlock().asItem());
    }
}
