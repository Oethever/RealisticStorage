package oethever.realisticstorage;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(RealisticStorage.MOD_ID)
public class RealisticStorage {
    public static final String MOD_ID = "realisticstorage";
    public static final Logger LOGGER = LogManager.getLogger();

    public RealisticStorage() {
        Registry.register();
        Config.init();
    }
}
