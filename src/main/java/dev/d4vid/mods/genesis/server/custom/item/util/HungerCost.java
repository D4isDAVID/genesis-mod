package dev.d4vid.mods.genesis.server.custom.item.util;

import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;

public class HungerCost {
    private static final int MIN_HUNGER_TO_USE = 6;

    public static boolean canAfford(ServerPlayer player) {
        return player.getFoodData().getFoodLevel() >= MIN_HUNGER_TO_USE;
    }

    public static void consume(ServerPlayer player, float saturationCost) {
        FoodData foodData = player.getFoodData();
        float saturation = foodData.getSaturationLevel();

        if (saturation >= saturationCost) {
            foodData.setSaturation(saturation - saturationCost);
        } else {
            foodData.setSaturation(0f);
            foodData.setFoodLevel(Math.max(0, foodData.getFoodLevel() - 2));
        }

        player.connection.send(new ClientboundSetHealthPacket(
            player.getHealth(), foodData.getFoodLevel(), foodData.getSaturationLevel()));
    }

}
