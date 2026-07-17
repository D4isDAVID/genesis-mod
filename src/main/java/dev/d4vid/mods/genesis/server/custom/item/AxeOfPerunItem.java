package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.Genesis;
import dev.d4vid.mods.genesis.server.custom.item.util.ItemEnchantmentsBuilder;
import dev.d4vid.mods.genesis.server.custom.item.util.TrueDamage;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.UseCooldown;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AxeOfPerunItem extends GenesisItem {
    private static final int AXE_OF_PERUN_COLOR = 0x64C4FF;
    private static final int LORE_COLOR = 0x888888;
    private static final float LIGHTNING_DAMAGE = 8f;
    private static final Component DISPLAY_NAME = Component
        .literal("Axe Of Perun")
        .withStyle(s -> s.withItalic(false).withBold(true).withColor(AXE_OF_PERUN_COLOR));
    private static final int COOLDOWN_TICKS = 600;

    public AxeOfPerunItem() {
        super("axe_perun", Items.DIAMOND_AXE, DISPLAY_NAME);

        ServerLivingEntityEvents.AFTER_DAMAGE.register((victim, source, baseDamage, appliedDamage, blocked) -> {
            if (!(source.getEntity() instanceof ServerPlayer attacker)) return;
            ItemStack stack = attacker.getMainHandItem();
            if (!this.is(stack)) return;
            if (attacker.getCooldowns().isOnCooldown(stack)) return;

            attacker.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);

            ServerLevel level = (ServerLevel) attacker.level();

            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level, EntitySpawnReason.TRIGGERED);
            if (bolt != null) {
                bolt.teleportTo(victim.getX(), victim.getY(), victim.getZ());
                bolt.setVisualOnly(true);
                level.addFreshEntity(bolt);
            }

            victim.igniteForTicks(100);

            TrueDamage.applyExact(victim, LIGHTNING_DAMAGE);

            attacker.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 0, false, true));
        });
    }
    @Override
    protected void build(RegistryAccess registries, ItemStack item) {
        //item.set(DataComponents.UNBREAKABLE, Unit.INSTANCE);
        enchant(registries, item);
        applyLore(item);
        applyCooldownGroup(item);
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
                .withStyle(s -> s.withItalic(false).withBold(true).withColor(AXE_OF_PERUN_COLOR))
        )));
    }
    private void applyCooldownGroup(ItemStack item) {
        item.set(DataComponents.USE_COOLDOWN, new UseCooldown(0.01f, Optional.of(
            Identifier.fromNamespaceAndPath(Genesis.MOD_ID, "axe_perun")
        )));
    }
    @Override
    public String[] getRecipePattern() {
        return new String[] {
            "NDL",
            "DB ",
            " B "
        };
    }

    @Override
    public Map<Character, String> getRecipeIngredients() {
        Map<Character, String> ingredients = new HashMap<>();
        ingredients.put('L', "minecraft:lightning_rod");
        ingredients.put('N', "minecraft:netherite_ingot");
        ingredients.put('B', "minecraft:breeze_rod");
        ingredients.put('D', "minecraft:diamond");
        return ingredients;
    }
}
