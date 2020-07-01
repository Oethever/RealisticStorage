package oethever.realisticstorage.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TileEntityPallet extends TileEntity {

    private final float fastBreakSpeed = 50;
    private final float areaHeight = 8;

    public TileEntityPallet() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void getBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (isInArea(event.getPos())) {
            event.setNewSpeed(fastBreakSpeed);
        }
    }

    private boolean isInArea(BlockPos pos) {
        return pos.getX() == getPos().getX() &&
                pos.getZ() == getPos().getZ() &&
                pos.getY() > getPos().getY() &&
                pos.getY() <= getPos().getY() + areaHeight;
    }
}
