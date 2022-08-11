package mod.adrenix.nostalgic.fabric.event.client;

import mod.adrenix.nostalgic.client.event.ClientEventHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;

public abstract class CandyEvents
{
    public static void register()
    {
        // Screen Events

        onTitleScreen();
        onLoadScreen();

        // ...
    }

    /* Screen Events */

    public static void onTitleScreen()
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        ScreenEvents.BEFORE_INIT.register(
            (client, screen, scaledWidth, scaledHeight) -> ClientEventHelper.renderClassicTitle(screen, minecraft::setScreen)
        );
    }

    public static void onLoadScreen()
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        ScreenEvents.BEFORE_INIT.register(
            (client, screen, scaledWidth, scaledHeight) -> ClientEventHelper.renderClassicProgress(screen, minecraft::setScreen)
        );
    }
}
