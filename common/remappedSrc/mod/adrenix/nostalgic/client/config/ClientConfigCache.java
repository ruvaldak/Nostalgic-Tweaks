package mod.adrenix.nostalgic.client.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import mod.adrenix.nostalgic.NostalgicTweaks;
import mod.adrenix.nostalgic.client.config.reflect.TweakClientCache;
import net.minecraft.util.ActionResult;

/**
 * This class is used exclusively by the client. It caches the current values stored on disk.
 *
 * The {@link TweakClientCache} caches these values and lets the user change those values without interfering with the
 * values saved on disk.
 */

public abstract class ClientConfigCache
{
    /* Configuration Caching */

    private static boolean initialized = false;
    private static final ClientConfig SERVER_CACHE = new ClientConfig();
    private static ClientConfig cache;
    private static ClientConfig getCache()
    {
        // This cache is only used by the ModConfig class and is not used for logic
        if (NostalgicTweaks.isServer())
            return SERVER_CACHE;

        if (!initialized)
            preloadConfiguration();
        return cache;
    }

    public static ClientConfig getRoot() { return getCache(); }
    public static ClientConfig.Sound getSound() { return getCache().sound; }
    public static ClientConfig.EyeCandy getCandy() { return getCache().eyeCandy; }
    public static ClientConfig.Gameplay getGameplay() { return getCache().gameplay; }
    public static ClientConfig.Animation getAnimation() { return getCache().animation; }
    public static ClientConfig.Swing getSwing() { return getCache().swing; }
    public static ClientConfig.Gui getGui() { return getCache().gui; }

    private static ActionResult reloadConfiguration()
    {
        // Retrieve new config and validate its data
        ClientConfigCache.cache = AutoConfig.getConfigHolder(ClientConfig.class).getConfig();
        CustomSwings.validate();

        // Let consoles know what happened and what was loaded
        NostalgicTweaks.LOGGER.info("Config was reloaded");

        return ActionResult.SUCCESS;
    }

    public static void preloadConfiguration()
    {
        if (NostalgicTweaks.isServer())
        {
            String fail = String.format("[%s] Cannot initialize client config for server.", NostalgicTweaks.MOD_NAME);
            throw new AssertionError(fail);
        }

        NostalgicTweaks.LOGGER.info("Initializing client config prematurely for mixin compatibility");
        initializeConfiguration();
    }

    public static void initializeConfiguration()
    {
        // Do not initialize again if this method was run prematurely
        if (!initialized)
            initialized = true;
        else
            return;

        // Register and cache config
        AutoConfig.register(ClientConfig.class, GsonConfigSerializer::new);
        AutoConfig.getConfigHolder(ClientConfig.class).registerLoadListener((manager, update) -> reloadConfiguration());
        AutoConfig.getConfigHolder(ClientConfig.class).registerSaveListener((manager, data) -> reloadConfiguration());
        reloadConfiguration();

        // List loaded tweaks
        NostalgicTweaks.LOGGER.info(String.format("Loaded %d tweaks", TweakClientCache.all().size()));

        // Let consoles know what happened
        NostalgicTweaks.LOGGER.info(String.format("Registered %d customized swing speeds", cache.custom.size()));
    }
}
