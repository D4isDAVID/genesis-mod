package dev.d4vid.mods.genesis.server.mixin.recipes;

import dev.d4vid.mods.genesis.server.custom.item.util.UltimateManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(ResultSlot.class)
public abstract class ResultSlotMixin {

    @Inject(method = "onTake", at = @At("HEAD"))
    private void genesis$onTake(Player player, ItemStack stack, CallbackInfo info) {
        if (player instanceof ServerPlayer serverPlayer) {
            UltimateManager.recordCraft(serverPlayer, stack);
        }
    }
}
