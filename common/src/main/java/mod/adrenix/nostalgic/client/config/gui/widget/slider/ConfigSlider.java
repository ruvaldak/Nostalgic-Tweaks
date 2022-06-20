package mod.adrenix.nostalgic.client.config.gui.widget.slider;

import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import mod.adrenix.nostalgic.client.config.annotation.TweakEntry;
import mod.adrenix.nostalgic.client.config.gui.widget.ConfigRowList;
import mod.adrenix.nostalgic.client.config.reflect.ConfigReflect;
import mod.adrenix.nostalgic.client.config.reflect.TweakCache;

public class ConfigSlider extends GenericSlider
{
    protected final TweakCache<Integer> cache;

    public ConfigSlider(TweakCache<Integer> cache)
    {
        super(
            cache::setCurrent,
            cache::getCurrent,
            null,
            ConfigRowList.getControlStartX(),
            0,
            ConfigRowList.CONTROL_BUTTON_WIDTH,
            ConfigRowList.BUTTON_HEIGHT
        );

        this.cache = cache;

        ConfigEntry.BoundedDiscrete bounds = ConfigReflect.getAnnotation(
            cache.getGroup(),
            cache.getKey(),
            ConfigEntry.BoundedDiscrete.class
        );

        if (bounds != null)
        {
            this.setMinimum((int) bounds.min());
            this.setMaximum((int) bounds.max());
        }

        TweakEntry.Gui.SliderType sliderType = ConfigReflect.getAnnotation(
            cache.getGroup(),
            cache.getKey(),
            TweakEntry.Gui.SliderType.class
        );

        if (sliderType != null)
            this.setSlider(sliderType.slider());
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick)
    {
        if (!this.cache.isResettable() && !this.isHovered)
            this.setValue(this.cache.getCurrent());

        this.updateMessage();
        super.render(poseStack, mouseX, mouseY, partialTick);
    }
}
