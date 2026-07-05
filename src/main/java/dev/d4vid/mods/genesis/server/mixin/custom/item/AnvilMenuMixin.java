package dev.d4vid.mods.genesis.server.mixin.custom.item;

import dev.d4vid.mods.genesis.server.custom.item.GenesisItem;
import dev.d4vid.mods.genesis.server.custom.item.GenesisItems;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {
    @Inject(method = "setItemName", at = @At("HEAD"), cancellable = true)
    private void genesis$setItemName(String name, CallbackInfoReturnable<Boolean> info) {
        AnvilMenu menu = (AnvilMenu) (Object) this;
        ItemStack input = menu.getSlot(0).getItem();
        GenesisItem item = GenesisItems.get(input);
        if (item != null && !item.canBeRenamed()) {
            info.setReturnValue(false);
            info.cancel();
        }
    }
}
