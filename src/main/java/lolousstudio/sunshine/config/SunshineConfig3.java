package lolousstudio.sunshine.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.SliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.gui.controllers.TickBoxController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SunshineConfig3 {
    public static final SunshineConfig3 INSTANCE = new SunshineConfig3();

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

    public Screen makeScreen(Screen parent){
        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("sunshine.title"))

                .category(ConfigCategory
                        .createBuilder()
                        .name(Text.translatable("sunshine.title"))
                        .option(
                                Option.<Boolean>createBuilder()
                                        .name(Text.translatable("sunshine.option.freezeTime"))
                                        .binding(
                                                true,
                                                () -> freezeTime,
                                                value -> freezeTime = value
                                        )
                                        .description(
                                                OptionDescription
                                                        .createBuilder().
                                                        text(Text.translatable("sunshine.option.freezeTime.tooltip"))
                                                        .build()
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build()
                        ).option(
                                Option.<Integer>createBuilder()
                                        .name(Text.translatable("sunshine.option.timeTick"))
                                        .binding(
                                                6000,
                                                () -> timeTick,
                                                value -> timeTick = value
                                        )
                                        .description(
                                                OptionDescription
                                                        .createBuilder().
                                                        text(Text.translatable("sunshine.option.timeTick.tooltip"))
                                                        .build()
                                        )
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(0, 24000).step(1000))
                                        .build()
                        )
                        .build())

                .build()
                .generateScreen(parent);
    }
}
