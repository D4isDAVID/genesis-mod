package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.Genesis;
import dev.d4vid.mods.genesis.server.custom.item.util.ItemEnchantmentsBuilder;
import dev.d4vid.mods.genesis.server.event.GenesisCustomItemEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.UseCooldown;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CreeperPantsItem extends GenesisItem {
    private static final int CREEPER_PANTS_COLOR = 0x64C4FF;
    private static final int LORE_COLOR = 0x888888;
    private static final int COOLDOWN_TICKS = 40; // TESTING ONLY — 2 seconds
    // private static final int COOLDOWN_TICKS = 3000; // real value: 2:30
    private static final float BLAST_DAMAGE = 7f;
    private static final double KB_STRENGTH = 1.6;
    private static final Component DISPLAY_NAME = Component
        .literal("Creeper Pants")
        .withStyle(s -> s.withItalic(false).withBold(true).withColor(CREEPER_PANTS_COLOR));

    public CreeperPantsItem() {
        super("creeper_pants", Items.DIAMOND_LEGGINGS, DISPLAY_NAME);
        ServerLivingEntityEvents.AFTER_DAMAGE.register((victim, source, baseDamage, appliedDamaged, blocked) -> {
            if (!(victim instanceof ServerPlayer player)) return;
            ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
            if (!this.is(legs)) return;
            if (player.getHealth() / player.getMaxHealth() >= 0.35f) return;
            if (player.getCooldowns().isOnCooldown(legs)) return;
            player.getCooldowns().addCooldown(legs, COOLDOWN_TICKS);
            ServerLevel level = (ServerLevel) player.level();
            level.explode(player, player.getX(), player.getY(), player.getZ(), 1.5f, false, Level.ExplosionInteraction.NONE);
            List<LivingEntity> nearby = level.getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(6.0),
                e -> e != player
            );
            for (LivingEntity entity: nearby) {
                entity.hurtServer(level, level.damageSources().generic(), BLAST_DAMAGE);

                double dx = player.getX() - entity.getX();
                double dz = player.getZ() - entity.getZ();
                entity.knockback(KB_STRENGTH, dx, dz);
            }
        });
    }
    @Override
    protected void build(RegistryAccess registries, ItemStack item) {
        enchant(registries, item);
        applyLore(item);
        applyEquippable(item);
        applyCooldownGroup(item);
    }

    private void enchant(RegistryAccess registries, ItemStack item) {
        new ItemEnchantmentsBuilder(registries)
            .add(Enchantments.PROTECTION, 2)
            .add(Enchantments.UNBREAKING, 3)
            .add(Enchantments.MENDING, 1)
            .add(Enchantments.SWIFT_SNEAK, 3)
            .enchant(item);
    }

    private void applyLore(ItemStack item) {
        item.set(DataComponents.LORE, new ItemLore(List.of(
            Component.empty(),
            Component.literal("WIP")
                .withStyle(s -> s.withItalic(false).withBold(true).withColor(LORE_COLOR))
        )));
    }
    @Override
    public boolean canBeEnchanted() {
        return false;
    }
    private void applyEquippable(ItemStack item) {
        ResourceKey<EquipmentAsset> assetKey = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(Genesis.MOD_ID, "creeper_pants")
        );
        item.set(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.LEGS)
            .setAsset(assetKey)
            .build());
    }
    private void applyCooldownGroup(ItemStack item) {
        item.set(DataComponents.USE_COOLDOWN, new UseCooldown(0.01f, Optional.of(
            Identifier.fromNamespaceAndPath(Genesis.MOD_ID, "creeper_pants")
        )));
    }
    @Override
    public String[] getRecipePattern() {
        return new String[] {
            "TTT",
            "D D",
            "N N"
        };
    }

    @Override
    public Map<Character, String> getRecipeIngredients() {
        Map<Character, String> ingredients = new HashMap<>();
        ingredients.put('N', "minecraft:netherite_ingot");
        ingredients.put('T', "minecraft:tnt");
        ingredients.put('D', "minecraft:diamond");
        return ingredients;
    }
}
