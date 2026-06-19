package dev.d4vid.mods.genesis.server.mixin.resourcePack;

import com.mojang.authlib.GameProfile;
import dev.d4vid.mods.genesis.server.GenesisConfig;
import dev.d4vid.mods.genesis.server.resourcePack.ResourcePackPlayerData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(ServerCommonPacketListenerImpl.class)
public abstract class ServerCommonPacketListenerImplMixin {
    @Shadow
    protected abstract GameProfile playerProfile();

    @Inject(method = "handleResourcePackResponse", at = @At("HEAD"))
    private void genesis$handleResourcePackResponse(ServerboundResourcePackPacket packet, CallbackInfo info) {
        ServerCommonPacketListenerImpl listener = (ServerCommonPacketListenerImpl) (Object) this;
        GameProfile player = playerProfile();

        switch (packet.action()) {
            case DECLINED -> {
                System.out.println(player.name() + " declined");

                if (GenesisConfig.INSTANCE.shouldKickOnResourcePackDecline()) {
                    listener.disconnect(Component.literal("You must accept the resource pack to play GOOBER, also your choice will be remembered."));
                }
            }
            case SUCCESSFULLY_LOADED -> {
                System.out.println(player.name() + " accepted");
                ResourcePackPlayerData.INSTANCE.registerPlayerAccepted(player.id());
                ResourcePackPlayerData.INSTANCE.save();
            }
            default -> {
            }
        }
    }
}
