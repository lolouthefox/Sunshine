package lolousstudio.sunshine.integrations;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import lolousstudio.sunshine.Sunshine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> Sunshine.getConfig().makeScreen(parent);
    }

    // public ConfigScreenFactory<?> getModConfigScreenFactory() {
    //        return parent -> AutoConfig.getConfigScreen(ConfigMenu.class, parent).get();
    //    }
}
