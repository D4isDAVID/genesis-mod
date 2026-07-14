package dev.d4vid.mods.genesis.server.custom.item.util;

import dev.d4vid.mods.genesis.server.custom.item.GenesisItem;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BootMovementAbilities {

    public static void registerDoubleJump(GenesisItem item) {
        Map<UUID, Boolean> hasDoubleJumped = new HashMap<>();
        Map<UUID, Boolean> wasJumpingLastTick = new HashMap<>();

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
                if (!item.is(boots)) continue;

                boolean isJumpingNow = player.getLastClientInput().jump();
                boolean wasJumpingBefore = wasJumpingLastTick.getOrDefault(player.getUUID(), false);
                boolean justPressed = isJumpingNow && !wasJumpingBefore; // rising edge only

                if (player.onGround()) {
                    hasDoubleJumped.put(player.getUUID(), false);
                }

                boolean alreadyUsed = hasDoubleJumped.getOrDefault(player.getUUID(), false);

                if (justPressed && !player.onGround() && !alreadyUsed) {
                    hasDoubleJumped.put(player.getUUID(), true);
                    player.setDeltaMovement(player.getDeltaMovement().x, 0.6, player.getDeltaMovement().z);
                    player.hurtMarked = true;

                    FoodData foodData = player.getFoodData();
                    foodData.setSaturation(Math.max(0f, foodData.getSaturationLevel() - 0.5f));
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

                if (player.horizontalCollision && player.getLastClientInput().forward()) {
                    player.setDeltaMovement(player.getDeltaMovement().x, 0.15, player.getDeltaMovement().z);
                    player.fallDistance = 0f;
                }
            }
        });
    }
}
