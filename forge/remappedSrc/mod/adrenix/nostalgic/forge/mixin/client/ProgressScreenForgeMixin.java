package mod.adrenix.nostalgic.forge.mixin.client;

import mod.adrenix.nostalgic.common.config.ModConfig;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProgressScreen.class)
public abstract class ProgressScreenForgeMixin
{
    /**
     * Prevents the progress screen from rendering. This issue only appears when using Forge.
     * Controlled by the old loading screens tweak.
     */
    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    private void NT$onRenderProgress(MatrixStack poseStack, int mouseX, int mouseY, float partialTick, CallbackInfo callback)
    {
        if (ModConfig.Candy.oldLoadingScreens())
            callback.cancel();
    }
}
