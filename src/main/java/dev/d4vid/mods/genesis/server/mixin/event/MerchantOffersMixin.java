package dev.d4vid.mods.genesis.server.mixin.event;

import dev.d4vid.mods.genesis.server.event.RecipeAssembleCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(MerchantOffers.class)
class MerchantOffersMixin {
    @Inject(method = "getRecipeFor", at = @At("RETURN"), cancellable = true)
    private void genesis$getRecipeFor(ItemStack stack, ItemStack stack2, int i, CallbackInfoReturnable<MerchantOffer> callback) {
        MerchantOffer offer = callback.getReturnValue();
        if (offer == null) {
            return;
        }

        ItemStack resultItem = offer.getResult();
        InteractionResult result = RecipeAssembleCallback.Companion.getEVENT().invoker().interact(new ItemStack[]{stack, stack2}, resultItem);

        if (result != InteractionResult.PASS) {
            callback.setReturnValue(null);
        }
    }
}
