package dev.d4vid.mods.genesis.server.mixin.item;

import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import dev.d4vid.mods.genesis.server.item.items.Drill;
import dev.d4vid.mods.genesis.server.item.items.MegaDrill;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.server.network.ServerGamePacketListenerImpl;


@Mixin(ServerGamePacketListenerImpl.class)
public class DrillToggleMixin {

    @Inject(method = "handlePlayerAction", at = @At("HEAD"), cancellable = true)
    private void genesis$handlePlayerAction(ServerboundPlayerActionPacket packet, CallbackInfo info) {
        if (packet.getAction() != ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND) return;

        ServerGamePacketListenerImpl listener = (ServerGamePacketListenerImpl) (Object) this;
        ServerPlayer player = listener.player;
        ItemStack stack = player.getMainHandItem();
        Identifier model = stack.get(DataComponents.ITEM_MODEL);

        if (Identifier.tryParse("genesis:drill").equals(model)) {
            Drill.toggleMode(stack, player.level().registryAccess());
            info.cancel(); // prevent actual swap
        } else if (Identifier.tryParse("genesis:megadrill").equals(model)) {
            MegaDrill.toggleMode(stack, player.level().registryAccess());
            info.cancel();
        }
    }
}
