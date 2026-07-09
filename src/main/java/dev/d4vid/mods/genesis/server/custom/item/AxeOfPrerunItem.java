package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.custom.item.util.ItemEnchantmentsBuilder;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;

import static net.minecraft.world.level.block.SculkSensorBlock.COOLDOWN_TICKS;

public class AxeOfPrerunItem extends GenesisItem {
    private static final int AXE_OF_PRERUN_COLOR = 0x64C4FF;
    private static final int LORE_COLOR = 0x888888;
    private static final Component DISPLAY_NAME = Component
        .literal("Axe Of Prerun")
        .withStyle(s -> s.withItalic(false).withBold(true).withColor(AXE_OF_PRERUN_COLOR));

    public AxeOfPrerunItem() {
        super("pre_run_axe", Items.DIAMOND_AXE, DISPLAY_NAME);

        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamage, appliedDamage, blocked) -> {
            if (!(entity instanceof ServerPlayer victim)) return;
            if (!(source.getEntity() instanceof ServerPlayer attacker)) return;
            ItemStack stack = attacker.getMainHandItem();
            if (!this.is(stack)) return;
            if (attacker.getCooldowns().isOnCooldown(stack)) return;
            ServerLevel level = (ServerLevel) attacker.level();
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level, EntitySpawnReason.TRIGGERED);
            if (bolt != null) {
                bolt.teleportTo(victim.getX(), victim.getY(), victim.getZ());
                bolt.setVisualOnly(true);
                level.addFreshEntity(bolt);
            }
            victim.igniteForTicks(100);
            DamageSource trueSource = level.damageSources().generic();
            victim.hurtServer(level, trueSource, 3f);

            attacker.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 140, 0, false, true));
            attacker.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);
        });
    }
    @Override
    protected void build(RegistryAccess registries, ItemStack item) {
        //item.set(DataComponents.UNBREAKABLE, Unit.INSTANCE);
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
            .enchant(item);
    }

    private void applyLore(ItemStack item) {
        item.set(DataComponents.LORE, new ItemLore(List.of(
            Component.empty(),
            Component.literal("WIP")
                .withStyle(s -> s.withItalic(false).withBold(true).withColor(AXE_OF_PRERUN_COLOR))
        )));
    }
}
