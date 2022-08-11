package mod.adrenix.nostalgic.forge.mixin.client;

import mod.adrenix.nostalgic.NostalgicTweaks;
import mod.adrenix.nostalgic.util.client.ModClientUtil;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin allows for the mod to tap into optifine changes, if that mod is installed.
 * Otherwise, we should use Forge's ViewportEvent.RenderFog event.
 */
@Mixin(BackgroundRenderer.class)
public abstract class OptifineFogRendererMixin
{
    @Inject(method = "setupFog", at = @At(value = "RETURN"))
    private static void NT$onSetupFog(Camera camera, BackgroundRenderer.FogType fogMode, float farPlaneDistance, boolean nearFog, float partialTick, CallbackInfo callback)
    {
        if (NostalgicTweaks.isOptifineInstalled)
        {
            ModClientUtil.Fog.setupFog(camera, fogMode);
            ModClientUtil.Fog.setupNetherFog(camera, fogMode);
        }
    }
}
