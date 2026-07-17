package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.custom.item.util.ItemEnchantmentsBuilder;
import dev.d4vid.mods.genesis.server.custom.item.util.TrueDamage;
import dev.d4vid.mods.genesis.server.event.GenesisCustomItemEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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

    private static final int SKELETON_LOAD_TICKS = 100;
    private static final int GHAST_LOAD_TICKS = 100;
    private static final float GHAST_DAMAGE = 4f;
    private static final int GHAST_BURN_TICKS = 60;
    private static final double GOAT_KNOCKBACK = 1.8;
    private static final double SKELETON_LOCK_RANGE = 64.0;
    private static final double SKELETON_CURVE_STRENGTH = 0.08;

    private final Map<UUID, AbstractArrow> skeletonArrows = new ConcurrentHashMap<>();
    private final Map<AbstractArrow, LivingEntity> homingArrows = new ConcurrentHashMap<>();
    private final Map<UUID, ItemStack> projectileWeapons = new ConcurrentHashMap<>();

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

            Entity projectile = source.getDirectEntity();
            ItemStack weapon = null;
            if (projectile != null) {
                weapon = projectileWeapons.remove(projectile.getUUID());
            }
            if (weapon == null) weapon = attacker.getMainHandItem();
            if (!this.is(weapon)) return;


            int mode = getMode(weapon);

            switch (mode) {
                case MODE_GHAST -> {
                    TrueDamage.applyExact(victim, GHAST_DAMAGE);
                    victim.igniteForTicks(GHAST_BURN_TICKS);
                }
                case MODE_SKELETON -> {
                    attacker.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 80, 2, false, true));
                }
                case MODE_GOAT -> {
                    Vec3 dir = victim.position().subtract(attacker.position()).normalize();
                    victim.knockback(GOAT_KNOCKBACK, -dir.x, -dir.z);
                }
            }
        });

        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
            if (!(entity instanceof AbstractArrow arrow)) return;
            if (!(arrow.getOwner() instanceof ServerPlayer shooter)) return;

            ItemStack held = shooter.getMainHandItem();
            projectileWeapons.put(arrow.getUUID(), held);
            if (!this.is(held)) return;

            if (getMode(held) == MODE_GHAST) {
                Vec3 vel = arrow.getDeltaMovement();
                arrow.discard();
                LargeFireball fireball = new LargeFireball((ServerLevel) level, shooter, vel, 0);
                fireball.setPos(arrow.position());
                fireball.addTag("genesis_ghast_fireball");
                ((ServerLevel) level).addFreshEntity(fireball);
                return;
            }
            if (getMode(held) == MODE_SKELETON) {
                Vec3 look = shooter.getLookAngle();
                arrow.shoot(look.x, look.y, look.z, 4.5f, 0.1f);
                arrow.addTag("genesis_skeleton_arrow");
                return;
            }
        });


        ServerTickEvents.END_SERVER_TICK.register(server -> {
            skeletonArrows.entrySet().removeIf(entry -> {
                AbstractArrow arrow = entry.getValue();
                if (!arrow.isAlive()) return true;

                Vec3 vel = arrow.getDeltaMovement();
                Vec3 pos = arrow.position();
                double speed = vel.length();
                Vec3 dir = vel.normalize();

                LivingEntity closest = null;
                double closestDist = Double.MAX_VALUE;

                for (LivingEntity entity : arrow.level().getEntitiesOfClass(
                    LivingEntity.class,
                    arrow.getBoundingBox().expandTowards(vel.scale(8)).inflate(4)
                )) {
                    if (entity == arrow.getOwner()) continue;
                    if (!entity.isAlive()) continue;

                    Vec3 toEntity = entity.getBoundingBox().getCenter().subtract(pos);
                    double along = toEntity.dot(dir);
                    if (along < 0) continue;

                    Vec3 projected = pos.add(dir.scale(along));
                    double perpDist = entity.getBoundingBox().getCenter().distanceTo(projected);

                    if (perpDist < 3.0 && along < closestDist) {
                        closest = entity;
                        closestDist = along;
                    }
                }

                if (closest != null) {
                    Vec3 toTarget = closest.getBoundingBox().getCenter().subtract(pos).normalize();
                    Vec3 curved = dir.scale(1 - SKELETON_CURVE_STRENGTH)
                        .add(toTarget.scale(SKELETON_CURVE_STRENGTH))
                        .normalize()
                        .scale(speed);
                    arrow.setDeltaMovement(curved);
                }

                return false;
            });
        });
    }

    private LivingEntity findLockOnTarget(ServerPlayer shooter, ServerLevel level) {
        Vec3 eyePos = shooter.getEyePosition(1.0f);
        Vec3 look = shooter.getViewVector(1.0f);
        Vec3 reach = eyePos.add(look.scale(SKELETON_LOCK_RANGE));

        EntityHitResult hit = ProjectileUtil.getEntityHitResult(
            level, shooter, eyePos, reach,
            shooter.getBoundingBox().expandTowards(look.scale(SKELETON_LOCK_RANGE)).inflate(1.0),
            entity -> entity instanceof LivingEntity && entity != shooter && entity.isAlive(),
            0f
        );

        if (hit == null) return null;
        return (LivingEntity) hit.getEntity();
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
            .add(Enchantments.PIERCING,4)
            .add(Enchantments.UNBREAKING, 3)
            .add(Enchantments. MENDING, 1)
            .add(Enchantments.QUICK_CHARGE, 2)
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
        var tag = readCustomData(stack);
        tag.putInt(MODE_KEY, next);
        saveCustomData(stack, tag);

        applyLore(stack);
        player.sendSystemMessage(getModeMessage(next), true);
    }

    private void applyLore(ItemStack item) {
        int mode = getMode(item);
        item.set(net.minecraft.core.component.DataComponents.LORE, new ItemLore(List.of(
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

    @Override
    public String[] getRecipePattern() {
        return new String[] {
            "BHB",
            "TST",
            " B "
        };
    }

    @Override
    public Map<Character, String> getRecipeIngredients() {
        Map<Character, String> ingredients = new HashMap<>();
        ingredients.put('B', "minecraft:blaze_rod");
        ingredients.put('T', "minecraft:ghast_tear");
        ingredients.put('S', "minecraft:wither_skeleton_skull");
        ingredients.put('H', "minecraft:goat_horn");
        return ingredients;
    }
}
