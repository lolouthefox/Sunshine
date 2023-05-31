package lolousstudio.sunshine;

import lolousstudio.sunshine.config.SunshineConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class Sunshine implements ClientModInitializer {

    private static KeyBinding keyBinding;

    @Override
    public void onInitializeClient() {
        getConfig().load();

        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "sunshine.keybind.freezeToggle", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_Y, // The keycode of the key
                "sunshine.title" // The translation key of the keybinding's category.
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                getConfig().freezeTime = !getConfig().freezeTime;
                if (getConfig().freezeTime){
                    client.player.sendMessage(Text.literal("Time freezed!"), false);
                } else {
                    client.player.sendMessage(Text.literal("Time unfreezed!"), false);
                }
            }
        });
    }

    public static SunshineConfig getConfig() {
        return SunshineConfig.INSTANCE;
    }
}
