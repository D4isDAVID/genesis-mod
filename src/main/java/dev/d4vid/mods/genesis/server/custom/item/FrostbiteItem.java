package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.custom.item.util.ItemEnchantmentsBuilder;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrostbiteItem extends GenesisItem {
    private static final int FROSTBITE_COLOR = 0x64C4FF;
    private static final int LORE_COLOR = 0x888888;
    private static final Component DISPLAY_NAME = Component
        .literal("Frostbite")
        .withStyle(s -> s.withItalic(false).withBold(true).withColor(FROSTBITE_COLOR));

    public FrostbiteItem() {
        super("frostbite", Items.IRON_SWORD, DISPLAY_NAME);

        ServerLivingEntityEvents.AFTER_DAMAGE.register((victim, source, baseDamage, appliedDamage, blocked) -> {
            if (!(source.getEntity() instanceof ServerPlayer attacker)) return;
            ItemStack stack = attacker.getMainHandItem();
            if (!this.is(stack)) return;
            if (attacker.getCooldowns().isOnCooldown(stack)) return;
            ServerLevel level = (ServerLevel) attacker.level();

            victim.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 80, 0, false, true));
            victim.setTicksFrozen(140);
        });
    }
    @Override
    protected void build(RegistryAccess registries, ItemStack item) {
        //item.set(DataComponents.UNBREAKABLE, Unit.INSTANCE);
        item.set(DataComponents.MAX_DAMAGE, 1561);
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
            .add(Enchantments.LOOTING, 3)
            .add(Enchantments.SWEEPING_EDGE, 3)
            .enchant(item);
    }

    private void applyLore(ItemStack item) {
        item.set(DataComponents.LORE, new ItemLore(List.of(
            Component.empty(),
            Component.literal("WIP")
                .withStyle(s -> s.withItalic(false).withBold(true).withColor(FROSTBITE_COLOR))
        )));
    }
    @Override
    public String[] getRecipePattern() {
        return new String[] {
            "IAI",
            "IAI",
            "NSN"
        };
    }

    @Override
    public Map<Character, String> getRecipeIngredients() {
        Map<Character, String> ingredients = new HashMap<>();
        ingredients.put('I', "minecraft:blue_ice");
        ingredients.put('A', "minecraft:iron_ingot");
        ingredients.put('N', "minecraft:netherite_ingot");
        ingredients.put('S', "minecraft:stick");
        return ingredients;
    }
}
