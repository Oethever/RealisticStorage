package oethever.realisticstorage.blocks;

import net.minecraft.tileentity.TileEntity;
import oethever.realisticstorage.RealisticStorage;
import oethever.realisticstorage.ServerEventHandler;

public class TileEntityPallet extends TileEntity {

    public TileEntityPallet() {
        ServerEventHandler.addPallet(this);
    }
}
