package oethever.realisticstorage.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import oethever.realisticstorage.Utils;

public class TileEntityPallet extends TileEntity {

    private final float fastBreakSpeed = 50;
    private final float areaHeight = 8;

    public TileEntityPallet() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void getBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (isInArea(event.getPos())) {
            event.setNewSpeed(fastBreakSpeed);
            event.setCanceled(false);
            event.setResult(Event.Result.ALLOW);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void doPlayerHarvestCheck(PlayerEvent.HarvestCheck event) {
        if (isInArea(Utils.getTargetedBlock(event.getEntityPlayer()))) {
            event.setCanHarvest(true);
            event.setCanceled(false);
            event.setResult(Event.Result.ALLOW);
        }

    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void breakBlock(BlockEvent.BreakEvent event) {
        if (isInArea(event.getPos())) {
            event.setCanceled(false);
            event.setResult(Event.Result.ALLOW);
        }
    }

    private boolean isInArea(BlockPos pos) {
        return pos != null &&
                getPos() != null &&
                pos.getX() == getPos().getX() &&
                pos.getZ() == getPos().getZ() &&
                pos.getY() > getPos().getY() &&
                pos.getY() <= getPos().getY() + areaHeight;
    }
}
