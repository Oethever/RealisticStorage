package oethever.realisticstorage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class Utils {
    public static BlockPos getTargetedBlock(EntityPlayer player) {
        RayTraceResult rayTraceResult = player.rayTrace(player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue(), 1);
        if (rayTraceResult != null && rayTraceResult.typeOfHit != RayTraceResult.Type.MISS) {
            return rayTraceResult.getBlockPos();
        }
        return null;
    }
}
