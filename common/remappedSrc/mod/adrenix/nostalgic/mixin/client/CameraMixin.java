package mod.adrenix.nostalgic.mixin.client;

import mod.adrenix.nostalgic.common.config.ModConfig;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin
{
    /* Shadows */

    @Shadow private Entity entity;
    @Shadow private float eyeHeightOld;
    @Shadow private float eyeHeight;

    /**
     * Disables the smooth sneaking.
     * Controlled by the old sneaking tweak.
     */
    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    private void NT$onTick(CallbackInfo callback)
    {
        if (!ModConfig.Animation.oldSneaking())
            return;

        if (this.entity != null)
        {
            float newEyeHeight = this.entity.getStandingEyeHeight();

            this.eyeHeightOld = this.eyeHeight;
            if (this.eyeHeight < newEyeHeight)
                this.eyeHeight += (newEyeHeight - this.eyeHeight) * 0.7F;
            else
                this.eyeHeight = newEyeHeight;

            if (this.entity instanceof PlayerEntity player && newEyeHeight == 1.62F)
            {
                if (!player.getAbilities().flying && player.isInSneakingPose())
                    this.eyeHeight = ModConfig.Animation.getSneakHeight();
            }
        }

        callback.cancel();
    }
}