package dev.d4vid.mods.genesis.server.mixin.item;

import dev.d4vid.mods.genesis.server.item.CustomItems;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GrindstoneMenu.class)
public class GrindstoneProtectionMixin {

    @Inject(method = "slotsChanged", at = @At("HEAD"))
    private void genesis$slotsChanged(Container container, CallbackInfo info) {
        GrindstoneMenu menu = (GrindstoneMenu) (Object) this;
        ItemStack input1 = menu.getSlot(0).getItem();
        ItemStack input2 = menu.getSlot(1).getItem();

        if (CustomItems.isCustomItem(input1) || CustomItems.isCustomItem(input2)) {
            menu.getSlot(2).set(ItemStack.EMPTY);
        }
    }

}
