package dev.d4vid.mods.genesis.server.mixin.mobgriefing;

import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(EnderMan.class)
public class EnderManMixin {
    @Inject(method = "setCarriedBlock", at = @At("HEAD"), cancellable = true)
    private void genesis$noBlockPickup(BlockState state, CallbackInfo callback) {
        callback.cancel();
    }
}
