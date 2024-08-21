package lolous.studio.sunshine_rewrite.client;

import lolous.studio.sunshine_rewrite.client.config.SunshineConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class Sunshine_rewriteClient implements ClientModInitializer {
    private static KeyBinding keyBinding;

    @Override
    public void onInitializeClient() {
        // Load config
        getConfig().load();

        // Setup keybinding
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "sunshine.keybind.freezeToggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Y,
                "sunshine.title"
        ));

        // Listen for keybind
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                getConfig().freezeTime = !getConfig().freezeTime;
                getConfig().save();
                if (client.player != null) {
                    if (getConfig().freezeTime) {
                        client.player.sendMessage(Text.translatable("sunshine.chat.frozen"), false);
                    } else {
                        client.player.sendMessage(Text.translatable("sunshine.chat.unfrozen"), false);
                    }
                }
            }
        });
    }

    public static SunshineConfig getConfig() {
        return SunshineConfig.DEFAULT;
    }
}
