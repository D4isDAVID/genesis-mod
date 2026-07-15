package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.custom.item.util.HungerCost;
import dev.d4vid.mods.genesis.server.custom.item.util.ItemEnchantmentsBuilder;
import dev.d4vid.mods.genesis.server.event.GenesisCustomItemEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;

public class TheLeechItem extends GenesisItem {
    private static final int THE_LEECH_COLOR = 0x64C4FF;
    private static final int REENTRY_GUARD_TICKS = 1;
    private static final int LORE_COLOR = 0x888888;
    private static final float BONUS_DAMAGE = 4f;      // +2 hearts, guaranteed, bypasses armor/enchants/effects
    private static final float SATURATION_COST = 0.5f;
    private static final Component DISPLAY_NAME = Component
        .literal("The Leech")
        .withStyle(s -> s.withItalic(false).withBold(true).withColor(THE_LEECH_COLOR));

    public TheLeechItem() {
        super("the_leech", Items.DIAMOND_SWORD, DISPLAY_NAME);

        ServerLivingEntityEvents.AFTER_DAMAGE.register((victim, source, baseDamage, appliedDamage, blocked) -> {
            if (!(source.getEntity() instanceof ServerPlayer attacker)) return;
            ItemStack stack = attacker.getMainHandItem();
            if (!this.is(stack)) return;
            if (attacker.getCooldowns().isOnCooldown(stack)) return;
            if (!HungerCost.canAfford(attacker)) return;
            attacker.getCooldowns().addCooldown(stack, REENTRY_GUARD_TICKS);

            ServerLevel level = (ServerLevel) attacker.level();

            Holder<DamageType> voidType = level.registryAccess()
                .lookupOrThrow(Registries.DAMAGE_TYPE)
                .getOrThrow(DamageTypes.FELL_OUT_OF_WORLD);
            DamageSource voidSource = new DamageSource(voidType, attacker);

            victim.hurtServer(level, voidSource, appliedDamage + BONUS_DAMAGE);

            HungerCost.consume(attacker, SATURATION_COST);

        });
    }
    @Override
    protected void build(RegistryAccess registries, ItemStack item) {
        enchant(registries, item);
        applyLore(item);
    }
    @Override
    public boolean canBeEnchanted() {
        return false;
    }

    private void enchant(RegistryAccess registries, ItemStack item) {
        new ItemEnchantmentsBuilder(registries)
            .add(Enchantments.SHARPNESS, 5)
            .add(Enchantments.UNBREAKING, 3)
            .add(Enchantments.MENDING, 1)
            .add(Enchantments.FIRE_ASPECT, 2)
            .add(Enchantments.LOOTING, 3)
            .add(Enchantments.SWEEPING_EDGE, 3)
            .enchant(item);
    }

    private void applyLore(ItemStack item) {
        item.set(DataComponents.LORE, new ItemLore(List.of(
            Component.empty(),
            Component.literal("WIP")
                .withStyle(s -> s.withItalic(false).withBold(true).withColor(THE_LEECH_COLOR))
        )));
    }
}
