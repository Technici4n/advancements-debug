package technici4n.advdebug;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdvDebug implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("AdvDebug");
    public static int recursionDepth = 0;

    @Override
    public void onInitialize() {
        // nothing to do ^^
    }
}
