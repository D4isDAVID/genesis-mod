package dev.d4vid.mods.genesis.server.custom.item.util;

import dev.d4vid.mods.genesis.server.custom.item.GenesisItem;
import dev.d4vid.mods.genesis.server.event.GenesisCustomItemEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BootMovementAbilities {

    public static void registerDoubleJump(GenesisItem item) {
        Map<UUID, Boolean> hasDoubleJumped = new HashMap<>();
        Map<UUID, Integer> airborneTicks = new HashMap<>();

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
                if (!item.is(boots)) continue;

                UUID uuid = player.getUUID();

                if (player.onGround()) {
                    airborneTicks.put(uuid, 0);
                    hasDoubleJumped.put(uuid, false);
                } else {
                    airborneTicks.merge(uuid, 1, Integer::sum);
                }
            }
        });

        GenesisCustomItemEvents.INSTANCE.getJUMP_INPUT().register(player -> {
            ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
            if (!item.is(boots)) return;

            UUID uuid = player.getUUID();
            boolean eligible = airborneTicks.getOrDefault(uuid, 0) > 2;
            boolean alreadyUsed = hasDoubleJumped.getOrDefault(uuid, false);

            if (!eligible || alreadyUsed || !HungerCost.canAfford(player)) return;

            hasDoubleJumped.put(uuid, true);

            Vec3 moveIntent = player.getLastClientMoveIntent();
            Vec3 currentVelocity = player.getDeltaMovement();

            double horizontalKick = 0.5;
            double verticalKick = 0.7;

            Vec3 boosted = new Vec3(
                currentVelocity.x + moveIntent.x * horizontalKick,
                verticalKick,
                currentVelocity.z + moveIntent.z * horizontalKick
            );
            player.setDeltaMovement(boosted);
            player.fallDistance = 0f;
            player.connection.send(new ClientboundSetEntityMotionPacket(player));

            HungerCost.consume(player, 3);

            ServerLevel level = (ServerLevel) player.level();
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.SAND_BREAK, SoundSource.PLAYERS, 1.0f, 1.6f);
            level.sendParticles(ParticleTypes.CLOUD, true, true,
                player.getX(), player.getY() + 0.1, player.getZ(),
                20, 0.3, 0.05, 0.3, 0.03);
        });
    }

    public static void registerWallClimb(GenesisItem item) {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
                if (!item.is(boots)) continue;

                boolean pressingForward = player.getLastClientInput().forward();
                if (!pressingForward) continue;

                ServerLevel level = (ServerLevel) player.level();

                double yawRad = Math.toRadians(player.getYRot());
                double dx = -Math.sin(yawRad) * 0.4;
                double dz = Math.cos(yawRad) * 0.4;

                BlockPos frontFeet = BlockPos.containing(player.getX() + dx, player.getY() + 0.2, player.getZ() + dz);
                BlockPos frontChest = BlockPos.containing(player.getX() + dx, player.getY() + 1.2, player.getZ() + dz);

                boolean wallAhead = !level.getBlockState(frontFeet).getCollisionShape(level, frontFeet).isEmpty()
                    || !level.getBlockState(frontChest).getCollisionShape(level, frontChest).isEmpty();

                if (wallAhead) {
                    player.setDeltaMovement(player.getDeltaMovement().x, 0.15, player.getDeltaMovement().z);
                    player.fallDistance = 0f;
                    player.connection.send(new ClientboundSetEntityMotionPacket(player));


                    if (player.tickCount % 4 == 0) {
                        BlockState frontState = level.getBlockState(frontFeet);
                        if (!frontState.isAir()) {
                            double offsetX = (level.random.nextDouble() - 0.5) * 0.3;
                            double offsetZ = (level.random.nextDouble() - 0.5) * 0.3;

                            level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, frontState), true, true,
                                player.getX() + offsetX, player.getY() + 0.1, player.getZ() + offsetZ,
                                2, 0.1, 0.05, 0.1, 0.0);
                        }
                    }
                }
            }
        });
    }
}
