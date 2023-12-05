package dev.polv.policeitemsmod.common;

import dev.polv.policeitemsmod.common.network.NetworkMessages;
import dev.polv.policeitemsmod.common.network.packet.HandcuffedPlayerS2CPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Pol Vallverdu (polv.dev)
 */
public class HandcuffHandler {

    public static class HandcuffData {
        private final UUID handcufferPlayerUUID;
        private final UUID handcuffedPlayerUUID;

        public HandcuffData(UUID handcufferPlayerUUID, UUID handcuffedPlayerUUID) {
            this.handcufferPlayerUUID = handcufferPlayerUUID;
            this.handcuffedPlayerUUID = handcuffedPlayerUUID;
        }

        public UUID getHandcufferPlayerUUID() {
            return handcufferPlayerUUID;
        }

        public UUID getHandcuffedPlayerUUID() {
            return handcuffedPlayerUUID;
        }
    }

    private final static List<HandcuffData> handcuffedPlayers = new ArrayList<>();

    public static void removeHandcuffD(PlayerEntity player) {
        ScaleData motionScaleData = ScaleTypes.MOTION.getScaleData(player);
        ScaleData reachScaleData = ScaleTypes.REACH.getScaleData(player);

        motionScaleData.resetScale();
        reachScaleData.resetScale();

        HandcuffData handcuffData = handcuffedPlayers.stream().filter(handcuffData1 -> handcuffData1.getHandcuffedPlayerUUID().equals(player.getUUID())).findFirst().orElse(null);
        if (handcuffData != null) {
            NetworkMessages.sendToAllPlayers(new HandcuffedPlayerS2CPacket(false, handcuffData.handcufferPlayerUUID, handcuffData.handcuffedPlayerUUID));
            handcuffedPlayers.remove(handcuffData);
        }
    }

    public static void addHandcuffS(PlayerEntity handcufferPlayer, PlayerEntity handcuffedPlayer) {
        if (handcuffedPlayers.stream().anyMatch(handcuffData -> handcuffData.getHandcuffedPlayerUUID().equals(handcuffedPlayer.getUUID()) || handcuffData.getHandcufferPlayerUUID().equals(handcufferPlayer.getUUID()))) {
            removeHandcuffD(handcuffedPlayer);
            removeHandcuffD(handcufferPlayer);
        }

        handcufferPlayer.removeVehicle();
        handcuffedPlayer.removeVehicle();

        NetworkMessages.sendToAllPlayers(new HandcuffedPlayerS2CPacket(true, handcufferPlayer.getUUID(), handcuffedPlayer.getUUID()));
        handcuffedPlayers.add(new HandcuffData(handcufferPlayer.getUUID(), handcuffedPlayer.getUUID()));
    }

    public static void addHandcuffC(UUID handcufferPlayerUUID, UUID handcuffedPlayerUUID) {
        handcuffedPlayers.add(new HandcuffData(handcufferPlayerUUID, handcuffedPlayerUUID));
    }

    public static void removeHandcuffC(UUID handcufferPlayerUUID, UUID handcuffedPlayerUUID) {
        handcuffedPlayers.removeIf(handcuffData -> handcuffData.getHandcuffedPlayerUUID().equals(handcuffedPlayerUUID) && handcuffData.getHandcufferPlayerUUID().equals(handcufferPlayerUUID));
    }

    public static void clear() {
        handcuffedPlayers.clear();
    }

    public static boolean isPlayerCuffed(PlayerEntity player) {
        return handcuffedPlayers.stream().anyMatch(handcuffData -> handcuffData.getHandcuffedPlayerUUID().equals(player.getUUID()));
    }

    public static boolean isPlayerCuffer(PlayerEntity player) {
        return handcuffedPlayers.stream().anyMatch(handcuffData -> handcuffData.getHandcufferPlayerUUID().equals(player.getUUID()));
    }

    public static void usedKeysS(PlayerEntity player) {
        removeHandcuffD(player);
    }

    public static PlayerEntity getHandcuffer(PlayerEntity player) {
        HandcuffData handcuffData = handcuffedPlayers.stream().filter(data -> data.getHandcuffedPlayerUUID().equals(player.getUUID())).findFirst().orElse(null);
        if (handcuffData == null) {
            removeHandcuffD(player);
            return null;
        }
        return player.getServer().getPlayerList().getPlayer(handcuffData.getHandcufferPlayerUUID());
    }

    public static PlayerEntity getHandcuffed(PlayerEntity player) {
        HandcuffData handcuffData = handcuffedPlayers.stream().filter(data -> data.getHandcufferPlayerUUID().equals(player.getUUID())).findFirst().orElse(null);
        if (handcuffData == null) {
            removeHandcuffD(player);
            return null;
        }
        return player.getServer().getPlayerList().getPlayer(handcuffData.getHandcuffedPlayerUUID());
    }

    public static void sendHandcuffs(PlayerEntity player) {
        handcuffedPlayers.forEach(handcuffData -> {
            NetworkMessages.sendToPlayer((ServerPlayerEntity) player, new HandcuffedPlayerS2CPacket(true, handcuffData.getHandcufferPlayerUUID(), handcuffData.getHandcuffedPlayerUUID()));
        });
    }

}
