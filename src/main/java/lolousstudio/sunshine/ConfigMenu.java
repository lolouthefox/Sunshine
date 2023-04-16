package lolousstudio.sunshine;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

@Config(name = "alwayssun")
public class ConfigMenu implements ConfigData, ModMenuApi {
    public boolean staticTime = true;
    public int timeTick = 6000;

    public Screen MyConfig() throws ValidationException {
        ConfigMenu config = AutoConfig.getConfigHolder(ConfigMenu.class).getConfig();
        config.validatePostLoad();

        ConfigBuilder builder = (ConfigBuilder) ConfigBuilder.create()
                .setTitle(Text.literal("Sunshine"))
                .setTransparentBackground(true)
                .setSavingRunnable(() -> {

                });
        ConfigCategory general = builder.getOrCreateCategory(Text.literal("Time of day"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Make time static"), config.staticTime)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Time will not cycle."))
                .setSaveConsumer(newValue -> config.staticTime = newValue)
                .build()
        );
        general.addEntry(entryBuilder.startIntSlider(Text.literal("World time tick"), config.timeTick, 0, 24000)
                .setDefaultValue(6000)
                .setTooltip(Text.literal("Time that the game will show."))
                .setSaveConsumer(newValue -> config.timeTick = newValue)
                .build()
        );
        Screen screen = builder.build();
        return screen;
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            try {
                return MyConfig();
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }
        };
    }
}























