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
    private static KeyBinding shortcutKeyBinding1;
    private static KeyBinding shortcutKeyBinding2;
    private static KeyBinding shortcutKeyBinding3;
    private static KeyBinding customTimeCycleKeyBinding;

    @Override
    public void onInitializeClient() {
        // Load config
        getConfig().load();

        // Setup keybinding
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "sunshine.keybindings.freezeToggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Y,
                "sunshine.title"
        ));

        customTimeCycleKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "sunshine.keybindings.customTimeCycle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "sunshine.title"
        ));

        shortcutKeyBinding1 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "sunshine.keybindings.shortcut1",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                "sunshine.title"
        ));

        shortcutKeyBinding2 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "sunshine.keybindings.shortcut2",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_COMMA,
                "sunshine.title"
        ));

        shortcutKeyBinding3 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "sunshine.keybindings.shortcut3",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_SEMICOLON,
                "sunshine.title"
        ));


        // Listen for keybinding
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBinding.wasPressed()) {
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

            if (shortcutKeyBinding1.wasPressed()) {
                getConfig().freezeTime = true;
                getConfig().customTimeCycle = false;
                getConfig().timeTick = getConfig().shortcut1TimeTick;
                getConfig().save();
            }
            if (shortcutKeyBinding2.wasPressed()) {
                getConfig().freezeTime = true;
                getConfig().customTimeCycle = false;
                getConfig().timeTick = getConfig().shortcut2TimeTick;
                getConfig().save();
            }
            if (shortcutKeyBinding3.wasPressed()) {
                getConfig().freezeTime = true;
                getConfig().customTimeCycle = false;
                getConfig().timeTick = getConfig().shortcut3TimeTick;
                getConfig().save();
            }

            if (customTimeCycleKeyBinding.wasPressed()) {
                getConfig().freezeTime = true;
                getConfig().customTimeCycle = !getConfig().customTimeCycle;
                getConfig().save();
            }

            if (getConfig().customTimeCycle) {
                if (getConfig().timeTick + getConfig().customTimeCycleSpeed >= 24_000) {
                    getConfig().timeTick = 0;
                } else {
                    if (getConfig().timeTick + getConfig().customTimeCycleSpeed < 0 && getConfig().customTimeCycleSpeed < 0) {
                        getConfig().timeTick = 24_000;
                    } else {
                        getConfig().timeTick += getConfig().customTimeCycleSpeed;
                    }
                }
            }
        });
    }

    public static SunshineConfig getConfig() {
        return SunshineConfig.DEFAULT;
    }
}
