package oethever.realisticstorage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class Utils {
    public static BlockPos getTargetedBlock(EntityPlayer player) {
        float partialTicks = 1;
        double blockReachDistance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
        Vec3d eyePosition = player.getPositionEyes(partialTicks);
        Vec3d lookVector = player.getLook(partialTicks);
        Vec3d rayTraceVector = eyePosition.addVector(lookVector.x * blockReachDistance, lookVector.y * blockReachDistance, lookVector.z * blockReachDistance);
        RayTraceResult rayTraceResult = player.world.rayTraceBlocks(eyePosition, rayTraceVector, false, false, true);
        if (rayTraceResult != null && rayTraceResult.typeOfHit != RayTraceResult.Type.MISS) {
            return rayTraceResult.getBlockPos();
        }
        return null;
    }
}
