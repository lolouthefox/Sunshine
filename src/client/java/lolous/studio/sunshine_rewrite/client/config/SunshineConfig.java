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

    public void save() {
        try {
            JsonObject config = new JsonObject();
            config.addProperty("timeTick", timeTick);
            config.addProperty("freezeTime", freezeTime);

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Screen makeScreen(Screen parent) {
        Option<Boolean> freezeTimeOption = Option.<Boolean>createBuilder()
                .name(Text.translatable("sunshine.option.freezeTime"))
                .description(OptionDescription.of(Text.translatable("sunshine.option.freezeTime.tooltip")))
                .binding(true, () -> freezeTime, value -> freezeTime = value)
                .controller(TickBoxControllerBuilderImpl::new)
                .build();

        Option<Integer> timeTickOption = Option.<Integer>createBuilder()
                .name(Text.translatable("sunshine.option.timeTick"))
                .description(OptionDescription.of(Text.translatable("sunshine.option.timeTick.tooltip")))
                .binding(6000, () -> timeTick, value -> timeTick = value)
                .controller(opt -> new IntegerSliderControllerBuilderImpl(opt).range(0, 24_000).step(1000).formatValue(value -> Text.of(value + " ticks")))
                .build();

        ConfigCategory sunshineCategory = ConfigCategory.createBuilder()
                .name(Text.translatable("sunshine.title"))
                .option(freezeTimeOption)
                .option(timeTickOption).build();

        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("sunshine.title"))
                .category(sunshineCategory)
                .save(this::save)
                .build()
                .generateScreen(parent);
    }
}
