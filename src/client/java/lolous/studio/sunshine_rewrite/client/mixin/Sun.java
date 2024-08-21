package lolous.studio.sunshine_rewrite.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static lolous.studio.sunshine_rewrite.client.Sunshine_rewriteClient.getConfig;

@Mixin(ClientWorld.class)
public class Sun {
    @Shadow @Final private ClientWorld.Properties clientWorldProperties;

    @Inject(at = @At("TAIL"), method = "setTimeOfDay")
    @Environment(EnvType.CLIENT)
    public void setTimeOfDay(long time, CallbackInfo ci) {
        if (getConfig().freezeTime) {
            this.clientWorldProperties.setTimeOfDay(getConfig().timeTick);
        }
    }
}
