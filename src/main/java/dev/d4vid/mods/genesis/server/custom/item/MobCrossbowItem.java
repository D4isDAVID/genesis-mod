package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.custom.item.util.ItemEnchantmentsBuilder;
import dev.d4vid.mods.genesis.server.event.GenesisCustomItemEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MobCrossbowItem extends GenesisItem {
    private static final int MOB_CROSSBOW_COLOR = 0x64C4FF;
    private static final int LORE_COLOR = 0x888888;
    private static final Component DISPLAY_NAME = Component
        .literal("Mob Crossbow")
        .withStyle(s -> s.withItalic(false).withBold(true).withColor(MOB_CROSSBOW_COLOR));

    private static final String MODE_KEY = "crossbow_mode";
    public static final int MODE_GHAST = 0;
    public static final int MODE_SKELETON = 1;
    public static final int MODE_GOAT = 2;

    public MobCrossbowItem() {
        super("mob_crossbow", Items.CROSSBOW, DISPLAY_NAME);

        GenesisCustomItemEvents.INSTANCE.getALLOW_ITEM_SWAP().register((player, stack) -> {
            if (this.is(stack)) {
                cycleMode(player, stack);
                return false;
            }
            return true;
        });

        ServerLivingEntityEvents.AFTER_DAMAGE.register((victim, source, baseDamage, appliedDamage, blocked) -> {
            if (!(source.getEntity() instanceof ServerPlayer attacker)) return;
            ItemStack weapon = source.getWeaponItem();
            if (weapon == null || !this.is(weapon)) return;

            int mode = getMode(weapon);
            ServerLevel level = (ServerLevel) attacker.level();

            switch (mode) {
                case MODE_GHAST -> applyGhastDamage(level, victim, appliedDamage);
                case MODE_SKELETON -> {
                    attacker.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 80, 2, false, true));
                }
                case MODE_GOAT -> {
                    Vec3 dir = victim.position().subtract(attacker.position()).normalize();
                    victim.knockback(0.6, -dir.x, -dir.z);
                    attacker.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 100, 3, false, true));
                }
            }
        });


        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
            if (!(entity instanceof AbstractArrow arrow)) return;
            if (!(arrow.getOwner() instanceof ServerPlayer shooter)) return;

            ItemStack weapon = arrow.getWeaponItem();
            if (weapon == null || !this.is(weapon)) return;
            if (getMode(weapon) != MODE_SKELETON) return;

            Vec3 look = shooter.getLookAngle();
            arrow.shoot(look.x, look.y, look.z, 4.5f, 0.2f); // vanilla crossbow default is power ~3.15, inaccuracy ~1.0
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
            .add(Enchantments.MENDING, 1)
            .add(Enchantments.LOOTING, 3)
            .add(Enchantments.PIERCING, 4)
            .add(Enchantments.QUICK_CHARGE, 1)
            .enchant(item);
    }

    public int getMode(ItemStack stack) {
        return readCustomData(stack).getIntOr(MODE_KEY, MODE_GHAST);
    }

    public boolean isGhastMode(ItemStack stack) {
        return this.is(stack) && getMode(stack) == MODE_GHAST;
    }

    private void cycleMode(ServerPlayer player, ItemStack stack) {
        int next = (getMode(stack) + 1) % 3;
        CompoundTag tag = readCustomData(stack);
        tag.putInt(MODE_KEY, next);
        saveCustomData(stack, tag);

        applyLore(stack);
        player.sendSystemMessage(getModeMessage(next), true);
    }

    private void applyGhastDamage(ServerLevel level, net.minecraft.world.entity.LivingEntity victim, float appliedDamage) {
        // Corrects total loss THIS hit down to exactly 2f (1 heart), regardless of
        // what the arrow's own armor-mitigated hit already did — effectively
        // replacing the normal damage rather than adding on top of it.
        float healthBeforeThisHit = victim.getHealth() + appliedDamage;
        victim.setHealth(Math.max(0f, healthBeforeThisHit - 2f));
    }

    private void applyLore(ItemStack item) {
        int mode = getMode(item);
        item.set(DataComponents.LORE, new ItemLore(List.of(
            Component.empty(),
            Component.empty()
                .append("Press [")
                .append(Component.keybind("key.swapOffhand"))
                .append("] to switch mode:")
                .withStyle(s -> s.withItalic(true).withColor(LORE_COLOR)),
            Component.literal(modeLabel(mode)).withStyle(s -> s.withItalic(true).withBold(true).withColor(MOB_CROSSBOW_COLOR))
        )));
    }

    private String modeLabel(int mode) {
        return switch (mode) {
            case MODE_SKELETON -> "Skeleton Mode";
            case MODE_GOAT -> "Goat Mode";
            default -> "Ghast Mode";
        };
    }

    private Component getModeMessage(int mode) {
        return Component.empty()
            .append(Component.literal("Mob Crossbow: ").withStyle(s -> s.withBold(true)))
            .append(modeLabel(mode));
    }
}
