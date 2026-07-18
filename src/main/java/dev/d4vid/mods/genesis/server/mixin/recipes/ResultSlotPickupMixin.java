package dev.d4vid.mods.genesis.server.mixin.recipes;

import dev.d4vid.mods.genesis.server.custom.item.util.UltimateManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(Slot.class)
public abstract class ResultSlotPickupMixin {

    @Shadow
    public abstract ItemStack getItem();

    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
    private void genesis$mayPickup(Player player, CallbackInfoReturnable<Boolean> callback) {
        if (!((Object) this instanceof ResultSlot)) return;

        if (player instanceof ServerPlayer serverPlayer && !UltimateManager.canCraft(serverPlayer, this.getItem())) {
            callback.setReturnValue(false);
        }
    }
}
