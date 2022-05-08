package oethever.realisticstorage;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import oethever.realisticstorage.blocks.TileEntityPallet;

import java.util.ArrayList;
import java.util.List;

public class ServerEventHandler {

    private final float fastBreakSpeed = 50;
    private final float areaHeight = 8;
    private static final List<TileEntityPallet> registeredPallet = new ArrayList<>();

    public ServerEventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void addPallet(TileEntityPallet pallet) {
        registeredPallet.add(pallet);
    }

    public static void removePallet(TileEntityPallet pallet) {
        registeredPallet.remove(pallet);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void breakSpeedEvent(PlayerEvent.BreakSpeed event) {
        if (event.getEntity().world.isRemote) {
            return;
        }
        if (isInArea(event.getPos())) {
            event.setNewSpeed(fastBreakSpeed);
            event.setResult(Event.Result.ALLOW);
            if (event.isCancelable()) {
                event.setCanceled(false);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void harvestCheckEvent(PlayerEvent.HarvestCheck event) {
        if (event.getEntity().world.isRemote) {
            return;
        }
        if (isInArea(Utils.getTargetedBlock(event.getEntityPlayer()))) {
            event.setCanHarvest(true);
            event.setResult(Event.Result.ALLOW);
            if (event.isCancelable()) {
                event.setCanceled(false);
            }
        }

    }

    @SubscribeEvent(priority =  EventPriority.LOWEST)
    public void harvestDropsEvent(BlockEvent.HarvestDropsEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }
        if (isInArea(event.getPos())) {
            ItemStack itemStack = getSilkTouchDrop(event.getState());
            if (!itemStack.isEmpty()) {
                event.getDrops().clear();
                event.getDrops().add(itemStack);
                event.setDropChance(1);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void breakEvent(BlockEvent.BreakEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }
        if (isInArea(event.getPos())) {
            event.setResult(Event.Result.ALLOW);
            if (event.isCancelable()) {
                event.setCanceled(false);
            }
        } else {
            TileEntityPallet palletAtPos = getPalletAtPos(event.getPos());
            if (palletAtPos != null) {
                removePallet(palletAtPos);
            }
        }
    }

    private boolean isInArea(BlockPos pos) {
        if (pos == null) {
            return false;
        }
        for (TileEntityPallet pallet : registeredPallet) {
            if (pos.getX() == pallet.getPos().getX() &&
                pos.getZ() == pallet.getPos().getZ() &&
                pos.getY() > pallet.getPos().getY() &&
                pos.getY() <= pallet.getPos().getY() + areaHeight) {
                return true;
            }
        }
        return false;
    }

    private static TileEntityPallet getPalletAtPos(BlockPos pos) {
        if (pos == null) {
            return null;
        }
        for (TileEntityPallet pallet : registeredPallet) {
            if (pos.equals(pallet.getPos())) {
                return pallet;
            }
        }
        return null;
    }

    private static ItemStack getSilkTouchDrop(IBlockState state)
    {
        Block block = state.getBlock();
        Item item = Item.getItemFromBlock(block);
        int i = 0;

        if (item.getHasSubtypes()) {
            i = block.getMetaFromState(state);
        }

        return new ItemStack(item, 1, i);
    }
}
