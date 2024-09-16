package lolous.studio.sunshine_rewrite.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.impl.controller.IntegerSliderControllerBuilderImpl;
import dev.isxander.yacl3.impl.controller.TickBoxControllerBuilderImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SunshineConfig {
    public static final SunshineConfig DEFAULT = new SunshineConfig();

    public final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path configFile = FabricLoader.getInstance().getConfigDir().resolve("sunshine.json");

    public boolean freezeTime = false;
    public int timeTick = 6000;

    public int shortcut1TimeTick = 0;
    public int shortcut2TimeTick = 6000;
    public int shortcut3TimeTick = 12000;

    public void save() {
        try {
            JsonObject config = new JsonObject();
            config.addProperty("timeTick", timeTick);
            config.addProperty("freezeTime", freezeTime);

            config.addProperty("shortcut1TimeTick", shortcut1TimeTick);
            config.addProperty("shortcut2TimeTick", shortcut2TimeTick);
            config.addProperty("shortcut3TimeTick", shortcut3TimeTick);

            Files.write(configFile, gson.toJson(config).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() {
        try {
            if (!Files.exists(configFile)) {
                save();
                return;
            }

            JsonObject config = gson.fromJson(new String(Files.readAllBytes(configFile)), JsonObject.class);

            if (config.has("timeTick")) {
                timeTick = config.getAsJsonPrimitive("timeTick").getAsInt();
            }
            if (config.has("freezeTime")) {
                freezeTime = config.getAsJsonPrimitive("freezeTime").getAsBoolean();
            }
            if (config.has("shortcut1TimeTick")) {
                shortcut1TimeTick = config.getAsJsonPrimitive("shortcut1TimeTick").getAsInt();
            }
            if (config.has("shortcut2TimeTick")) {
                shortcut2TimeTick = config.getAsJsonPrimitive("shortcut2TimeTick").getAsInt();
            }
            if (config.has("shortcut3TimeTick")) {
                shortcut3TimeTick = config.getAsJsonPrimitive("shortcut3TimeTick").getAsInt();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Screen makeScreen(Screen parent) {
        // MAIN OPTIONS
        Option<Boolean> freezeTimeOption = Option.<Boolean>createBuilder()
                .name(Text.translatable("sunshine.options.main.freezeTime"))
                .description(OptionDescription.of(Text.translatable("sunshine.options.main.freezeTime.tooltip")))
                .binding(true, () -> freezeTime, value -> freezeTime = value)
                .controller(TickBoxControllerBuilderImpl::new)
                .build();

        Option<Integer> timeTickOption = Option.<Integer>createBuilder()
                .name(Text.translatable("sunshine.options.main.timeTick"))
                .description(OptionDescription.of(Text.translatable("sunshine.options.main.timeTick.tooltip")))
                .binding(6000, () -> timeTick, value -> timeTick = value)
                .controller(opt -> new IntegerSliderControllerBuilderImpl(opt).range(0, 24_000).step(1000).formatValue(value -> Text.of(value + " ticks")))
                .build();

        ConfigCategory sunshineCategory = ConfigCategory.createBuilder()
                .name(Text.translatable("sunshine.options.main.title"))
                .option(freezeTimeOption)
                .option(timeTickOption).build();

        // SHORTCUTS
        Option<Integer> shortcut1TimeTickOption = Option.<Integer>createBuilder()
                .name(Text.translatable("sunshine.options.shortcuts.shortcut1"))
                .description(OptionDescription.of(Text.translatable("sunshine.options.shortcuts.shortcut1.tooltip")))
                .binding(6000, () -> shortcut1TimeTick, value -> shortcut1TimeTick = value)
                .controller(opt -> new IntegerSliderControllerBuilderImpl(opt).range(0, 24_000).step(1000).formatValue(value -> Text.of(value + " ticks")))
                .build();

        Option<Integer> shortcut2TimeTickOption = Option.<Integer>createBuilder()
                .name(Text.translatable("sunshine.options.shortcuts.shortcut2"))
                .description(OptionDescription.of(Text.translatable("sunshine.options.shortcuts.shortcut2.tooltip")))
                .binding(6000, () -> shortcut2TimeTick, value -> shortcut2TimeTick = value)
                .controller(opt -> new IntegerSliderControllerBuilderImpl(opt).range(0, 24_000).step(1000).formatValue(value -> Text.of(value + " ticks")))
                .build();

        Option<Integer> shortcut3TimeTickOption = Option.<Integer>createBuilder()
                .name(Text.translatable("sunshine.options.shortcuts.shortcut3"))
                .description(OptionDescription.of(Text.translatable("sunshine.options.shortcuts.shortcut3.tooltip")))
                .binding(6000, () -> shortcut3TimeTick, value -> shortcut3TimeTick = value)
                .controller(opt -> new IntegerSliderControllerBuilderImpl(opt).range(0, 24_000).step(1000).formatValue(value -> Text.of(value + " ticks")))
                .build();

        ConfigCategory shortcutsCategory = ConfigCategory.createBuilder()
                .name(Text.translatable("sunshine.options.shortcuts.title"))
                .option(shortcut1TimeTickOption)
                .option(shortcut2TimeTickOption)
                .option(shortcut3TimeTickOption)
                .build();

        // FINISH BUILD
        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("sunshine.title"))
                .category(sunshineCategory)
                .category(shortcutsCategory)
                .save(this::save)
                .build()
                .generateScreen(parent);
    }
}
