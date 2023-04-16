package lolousstudio.sunshine;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ModInitializer;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

public class Sunshine implements ModInitializer {
    @Override
    public void onInitialize() {
        AutoConfig.register(ConfigMenu.class, GsonConfigSerializer::new);
    }
}
