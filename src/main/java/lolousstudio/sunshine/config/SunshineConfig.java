package lolousstudio.sunshine.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.gui.controllers.TickBoxController;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.gui.controllers.slider.IntegerSliderController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SunshineConfig {
    public static final SunshineConfig INSTANCE = new SunshineConfig();

    public final Path configFile = FabricLoader.getInstance().getConfigDir().resolve("sunshine.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public boolean freezeTime = false;
    public int timeTick = 6000;

    public void save() {
        try {
            Files.deleteIfExists(configFile);

            JsonObject json = new JsonObject();
            json.addProperty("freezeTime", freezeTime);
            json.addProperty("timeTick", timeTick);

            Files.writeString(configFile, gson.toJson(json));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            if (Files.notExists(configFile)) {
                save();
                return;
            }

            JsonObject json = gson.fromJson(Files.readString(configFile), JsonObject.class);

            if (json.has("freezeTime"))
                freezeTime = json.getAsJsonPrimitive("freezeTime").getAsBoolean();
            if (json.has("timeTick"))
                timeTick = json.getAsJsonPrimitive("timeTick").getAsInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Screen makeScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("sunshine.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("sunshine.title"))
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.translatable("sunshine.option.freezeTime"))
                                .tooltip(Text.translatable("sunshine.option.freezeTime.tooltip"))
                                .binding(
                                        true,
                                        () -> freezeTime,
                                        value -> freezeTime = value
                                )
                                .controller(TickBoxController::new)
                                .build())
                        .option(Option.createBuilder(int.class)
                                .name(Text.translatable("sunshine.option.timeTick"))
                                .tooltip(Text.translatable("sunshine.option.timeTick.tooltip"))
                                .binding(
                                        6000,
                                        () -> timeTick,
                                        value -> timeTick = value
                                )
                                .controller(yacl -> new IntegerSliderController(yacl, 0, 24000, 1000))
                                .build())
                        .build())
                .save(this::save)
                .build()
                .generateScreen(parent);
    }
}
