package mod.adrenix.nostalgic.mixin.client.world;

import mod.adrenix.nostalgic.common.config.ModConfig;
import mod.adrenix.nostalgic.common.config.tweak.TweakVersion;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public abstract class BiomeMixin
{
    /* Shadows */

    @Shadow @Final private BiomeEffects specialEffects;

    /* Helpers */

    private static boolean isNether()
    {
        return MinecraftClient.getInstance().world != null && MinecraftClient.getInstance().world.getRegistryKey() == World.NETHER;
    }

    private static boolean isOverworld()
    {
        return MinecraftClient.getInstance().world != null && MinecraftClient.getInstance().world.getRegistryKey() == World.OVERWORLD;
    }

    /**
     * Brings back the old fog universal fog colors in the overworld and the nether.
     * Controlled by the old biome colors tweak.
     */
    @Inject(method = "getFogColor", at = @At(value = "HEAD"), cancellable = true)
    private void NT$onGetFogColor(CallbackInfoReturnable<Integer> callback)
    {
        TweakVersion.Generic fog = ModConfig.Candy.getFogColor();

        if (fog == TweakVersion.Generic.MODERN)
            callback.setReturnValue(this.specialEffects.getFogColor());
        else if (isNether())
            callback.setReturnValue(0x100400);
        else if (isOverworld())
        {
            switch (fog)
            {
                case ALPHA -> callback.setReturnValue(0xb0d0ff);
                case BETA -> callback.setReturnValue(0xb0c6ff);
            }
        }
    }

    /**
     * Brings back the old universal sky color in the overworld.
     * Controlled by the old biome colors tweak.
     */
    @Inject(method = "getSkyColor", at = @At(value = "HEAD"), cancellable = true)
    private void NT$onGetSkyColor(CallbackInfoReturnable<Integer> callback)
    {
        TweakVersion.Generic sky = ModConfig.Candy.getSkyColor();

        if (sky == TweakVersion.Generic.MODERN)
            callback.setReturnValue(this.specialEffects.getSkyColor());
        else if (isNether())
            callback.setReturnValue(0x100400);
        else if (isOverworld())
        {
            switch (sky)
            {
                case ALPHA -> callback.setReturnValue(0x88bbff);
                case BETA -> callback.setReturnValue(0x92a6ff);
            }
        }
    }
}
