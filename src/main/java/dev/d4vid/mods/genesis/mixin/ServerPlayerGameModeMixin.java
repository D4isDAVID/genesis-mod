package dev.d4vid.mods.genesis.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
    @Shadow
    protected ServerLevel level;

    @Inject(method = "useItem", at = @At("HEAD"), cancellable = true)
    private void genesis$useItem(ServerPlayer player, Level level, ItemStack stack, InteractionHand hand, CallbackInfoReturnable<InteractionResult> callback) {
        if (stack.getItem() == Items.ENDER_EYE) {
            callback.setReturnValue(InteractionResult.FAIL);
            player.sendSystemMessage(Component.literal("The End are disabled!"), true);
        }
    }

    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void genesis$useItemOn(ServerPlayer player, Level level, ItemStack stack, InteractionHand hand, BlockHitResult blockHit, CallbackInfoReturnable<InteractionResult> callback) {
        if (stack.getItem() == Items.ENDER_EYE) {
            BlockState blockState = level.getBlockState(blockHit.getBlockPos());
            Block block = blockState.getBlock();

            if (block == Blocks.END_PORTAL_FRAME) {
                callback.setReturnValue(InteractionResult.FAIL);
                player.sendSystemMessage(Component.literal("The End is disabled!"), true);
            }
        }
    }
}
