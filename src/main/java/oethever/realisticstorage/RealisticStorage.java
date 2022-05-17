package oethever.realisticstorage;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import oethever.realisticstorage.containerguard.ContainerGuard;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = RealisticStorage.MODID,
        name = RealisticStorage.NAME,
        version = RealisticStorage.VERSION
)
public class RealisticStorage {
    public static final String MODID = "realisticstorage";
    public static final String NAME = "Realistic Storage";
    public static final String VERSION = "1.2.1";

    @Mod.Instance(MODID)
    public static RealisticStorage INSTANCE;

    private static Logger logger;
    private ContainerGuard containerGuard = new ContainerGuard();
    private ServerEventHandler serverEventHandler = new ServerEventHandler();

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        containerGuard.setLogger(logger);
    }

    public void updateConfig() {
        containerGuard.updateConfig();
    }
}
