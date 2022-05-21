package oethever.realisticstorage.blockentity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import oethever.realisticstorage.Registry;

import java.lang.reflect.Array;
import java.util.Arrays;

public class PalletBlockEntity extends BlockEntity {
    public static final int AREA_HEIGHT = 8;
    private boolean[] placedBefore = new boolean[AREA_HEIGHT];

    public PalletBlockEntity(BlockPos pos, BlockState state) {
        super(Registry.PALLET_BLOCK_ENTITY.get(), pos, state);
        Arrays.fill(placedBefore, true);
    }

    // Here we assume that abovePos and this.getBlockPos() have the same X and Z coordinates. No check is made.
    public boolean wasPlacedBefore(BlockPos abovePos) {
        int dY = abovePos.getY() - getBlockPos().getY();
        if (dY >= 1 && dY <= AREA_HEIGHT) {
            return placedBefore[dY - 1];
        } else {
            return false;
        }
    }

    // Here we assume that abovePos and this.getBlockPos() have the same X and Z coordinates. No check is made.
    public void setPlaced(BlockPos abovePos) {
        int dY = abovePos.getY() - getBlockPos().getY();
        if (dY >= 1 && dY <= AREA_HEIGHT) {
            placedBefore[dY - 1] = false;
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        byte[] bytes = new byte[AREA_HEIGHT];
        for (int i = 0; i < AREA_HEIGHT; ++i) {
            bytes[i] = placedBefore[i] ? (byte) 1 : 0;
        }
        tag.putByteArray("placedBefore", bytes);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        byte[] bytes = tag.getByteArray("placedBefore");
        if (bytes.length != AREA_HEIGHT) {
            return;
        }
        for (int i = 0; i < AREA_HEIGHT; ++i) {
            placedBefore[i] = bytes[i] == (byte)1;
        }
    }
}
