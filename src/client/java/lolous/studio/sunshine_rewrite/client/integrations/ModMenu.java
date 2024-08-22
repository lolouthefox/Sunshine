package lolous.studio.sunshine_rewrite.client.integrations;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import static lolous.studio.sunshine_rewrite.client.Sunshine_rewriteClient.getConfig;

@Environment(EnvType.CLIENT)
public class ModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> getConfig().makeScreen(parent);
    }
}
