package mod.adrenix.nostalgic.client.config.gui.widget.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mod.adrenix.nostalgic.client.config.tweak.GuiTweak;
import mod.adrenix.nostalgic.client.config.gui.screen.config.ConfigScreen;
import mod.adrenix.nostalgic.client.config.gui.widget.ConfigRowList;
import mod.adrenix.nostalgic.client.config.reflect.TweakCache;
import mod.adrenix.nostalgic.client.config.reflect.GroupType;
import mod.adrenix.nostalgic.client.config.reflect.StatusType;
import mod.adrenix.nostalgic.util.NostalgicLang;
import mod.adrenix.nostalgic.util.NostalgicUtil;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class StatusButton extends Button
{
    protected static boolean flipState = false;
    protected static long currentTime = 0L;
    protected final TweakCache<?> cache;
    protected final AbstractWidget anchor;

    public StatusButton(TweakCache<?> cache, AbstractWidget anchor)
    {
        super(0, 0, 0, 0, Component.empty(), (ignored) -> {});
        this.cache = cache;
        this.anchor = anchor;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick)
    {
        if (this.cache.getStatus() == StatusType.LOADED) return;

        boolean isRenderable = (Boolean) TweakCache.get(GroupType.GUI, GuiTweak.DISPLAY_FEATURE_STATUS.getKey()).getCurrent();
        if (!isRenderable && this.cache.getStatus() != StatusType.WARN)
            return;

        StatusType status = flipState ? StatusType.LOADED : this.cache.getStatus();
        RenderSystem.setShaderTexture(0, NostalgicUtil.Resource.WIDGETS_LOCATION);
        Minecraft minecraft = Minecraft.getInstance();
        Screen screen = minecraft.screen;

        if (screen == null) return;
        if (currentTime == 0) currentTime = Util.getMillis();
        if (Util.getMillis() - currentTime > 1000)
        {
            currentTime = 0;
            flipState = !flipState;
        }

        int uWidth = 4;
        int vHeight = 20;
        int xStart = this.anchor.x - ConfigRowList.ROW_WIDGET_GAP - uWidth;
        int yStart = this.anchor.y;
        boolean isMouseOver = (mouseX >= xStart && mouseX <= xStart + uWidth) && (mouseY >= yStart && mouseY <= yStart + vHeight);

        switch (status)
        {
            case WAIT -> screen.blit(poseStack, xStart, yStart, 14, 21, uWidth, vHeight);
            case LOADED -> screen.blit(poseStack, xStart, yStart, 14, 0, uWidth, vHeight);
            case WARN -> screen.blit(poseStack, xStart, yStart, 21, 0, uWidth, vHeight);
            case FAIL -> screen.blit(poseStack, xStart, yStart, 27, 0, uWidth, vHeight);
        }

        if (isMouseOver && screen instanceof ConfigScreen)
        {
            switch (this.cache.getStatus())
            {
                case WAIT ->
                    ((ConfigScreen) screen).renderLast.add(() ->
                        screen.renderComponentTooltip(poseStack, NostalgicUtil.Wrap.tooltips(Component.translatable(NostalgicLang.Gui.STATUS_WAIT), 40), mouseX, mouseY));
                case WARN ->
                    ((ConfigScreen) screen).renderLast.add(() ->
                        screen.renderComponentTooltip(poseStack, NostalgicUtil.Wrap.tooltips(Component.translatable(NostalgicLang.Gui.STATUS_WARN), 40), mouseX, mouseY));
                case FAIL ->
                    ((ConfigScreen) screen).renderLast.add(() ->
                        screen.renderComponentTooltip(poseStack, NostalgicUtil.Wrap.tooltips(Component.translatable(NostalgicLang.Gui.STATUS_FAIL), 40), mouseX, mouseY));
            }
        }
    }
}
