package mod.adrenix.nostalgic.forge.network;

import mod.adrenix.nostalgic.NostalgicTweaks;
import net.minecraft.util.Identifier;
import net.minecraftforge.network.NetworkRegistry;

public abstract class NostalgicNetwork
{
    public static void init()
    {
        NetworkRegistry.ChannelBuilder
            .named(new Identifier(NostalgicTweaks.MOD_ID, "nt_forge_verification"))
            .networkProtocolVersion(() -> NostalgicTweaks.PROTOCOL)
            .clientAcceptedVersions(NostalgicTweaks.PROTOCOL::equals)
            .serverAcceptedVersions(NostalgicTweaks.PROTOCOL::equals)
            .simpleChannel()
        ;
    }
}
