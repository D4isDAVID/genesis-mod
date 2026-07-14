package dev.d4vid.mods.genesis.server.custom.item.util;

import dev.d4vid.mods.genesis.server.custom.item.GenesisItem;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BootMovementAbilities {

    public static void registerDoubleJump(GenesisItem item) {
        Map<UUID, Boolean> hasDoubleJumped = new HashMap<>();
        Map<UUID, Boolean> wasJumpingLastTick = new HashMap<>();
        Map<UUID, Integer> airborneTicks = new HashMap<>();

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
                if (!item.is(boots)) continue;

                boolean isJumpingNow = player.getLastClientInput().jump();
                boolean wasJumpingBefore = wasJumpingLastTick.getOrDefault(player.getUUID(), false);
                boolean justPressed = isJumpingNow && !wasJumpingBefore;

                if (player.onGround()) {
                    airborneTicks.put(player.getUUID(), 0);
                    hasDoubleJumped.put(player.getUUID(), false);
                } else {
                    airborneTicks.merge(player.getUUID(), 1, Integer::sum);
                }

                // Requires a couple full ticks of real airtime before the double jump
                // becomes usable — avoids catching the tail end of the FIRST jump's
                // liftoff, which can share a tick with "just started falling."
                boolean eligible = airborneTicks.getOrDefault(player.getUUID(), 0) > 2;
                boolean alreadyUsed = hasDoubleJumped.getOrDefault(player.getUUID(), false);

                if (justPressed && eligible && !alreadyUsed) {
                    hasDoubleJumped.put(player.getUUID(), true);
                    player.setDeltaMovement(player.getDeltaMovement().x, 0.6, player.getDeltaMovement().z);
                    player.hurtMarked = true;

                    FoodData foodData = player.getFoodData();
                    foodData.setSaturation(Math.max(0f, foodData.getSaturationLevel() - 0.5f));

                    ServerLevel level = (ServerLevel) player.level();
                    level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.WIND_CHARGE_THROW, SoundSource.PLAYERS, 1.0f, 1.4f);
                    level.sendParticles(ParticleTypes.CLOUD,
                        player.getX(), player.getY() + 0.1, player.getZ(),
                        12, 0.3, 0.05, 0.3, 0.02);
                }

                wasJumpingLastTick.put(player.getUUID(), isJumpingNow);
            }
        });
    }

    public static void registerWallClimb(GenesisItem item) {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
                if (!item.is(boots)) continue;

                boolean colliding = player.horizontalCollision;
                boolean pressingForward = player.getLastClientInput().forward();

                // TEMP DEBUG — remove once confirmed working
                if (player.tickCount % 10 == 0) {
                    player.sendSystemMessage(Component.literal(
                        "[Climb] collision=" + colliding + " forward=" + pressingForward + " onGround=" + player.onGround()
                    ));
                }

                if (colliding && pressingForward) {
                    player.setDeltaMovement(player.getDeltaMovement().x, 0.15, player.getDeltaMovement().z);
                    player.fallDistance = 0f;

                    if (player.tickCount % 4 == 0) {
                        ServerLevel level = (ServerLevel) player.level();

                        double yawRad = Math.toRadians(player.getYRot());
                        double dx = -Math.sin(yawRad);
                        double dz = Math.cos(yawRad);
                        BlockPos frontPos = BlockPos.containing(player.getX() + dx, player.getY() + 0.5, player.getZ() + dz);
                        BlockState frontState = level.getBlockState(frontPos);

                        if (!frontState.isAir()) {
                            level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, frontState),
                                player.getX(), player.getY() + 0.5, player.getZ(),
                                6, 0.2, 0.2, 0.2, 0.05);
                        }
                    }
                }
            }
        });
    }
}
