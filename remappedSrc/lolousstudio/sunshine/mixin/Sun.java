package lolousstudio.sunshine.mixin;

import lolousstudio.sunshine.ConfigMenu;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static lolousstudio.sunshine.Sunshine.getConfig;

@Mixin(ClientWorld.class)
public class Sun {
    // ConfigMenu config = AutoConfig.getConfigHolder(ConfigMenu.class).getConfig();
    @Shadow
    @Final
    private ClientWorld.Properties clientWorldProperties;
    @Shadow @Final private MinecraftClient client;
    @Inject(at = @At("TAIL"), method = "setTimeOfDay")
    @Environment(EnvType.CLIENT)
    public void setTimeOfDay(long time, CallbackInfo ci) {
        if (getConfig().freezeTime) {
            this.clientWorldProperties.setTimeOfDay(getConfig().timeTick);
        }
    }
}
