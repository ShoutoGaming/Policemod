package dev.polv.policeitemsmod.common.network;

import dev.polv.policeitemsmod.common.network.packet.HandcuffedPlayerMoveS2CPacket;
import dev.polv.policeitemsmod.common.network.packet.HandcuffedPlayerS2CPacket;
import dev.polv.policeitemsmod.PoliceItemsMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * @author Pol Vallverdu (polv.dev)
 */
public class NetworkMessages {

    private static final String PROTOCOL_VERSION = "1";

    private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(PoliceItemsMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);

    public static void RegisterPackets() {
        int packetID = 0;

        INSTANCE.messageBuilder(HandcuffedPlayerS2CPacket.class, packetID++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(HandcuffedPlayerS2CPacket::Encode)
                .decoder(HandcuffedPlayerS2CPacket::Decode)
                .consumer(HandcuffedPlayerS2CPacket::Handle)
                .add();

        INSTANCE.messageBuilder(HandcuffedPlayerMoveS2CPacket.class, packetID++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(HandcuffedPlayerMoveS2CPacket::Encode)
                .decoder(HandcuffedPlayerMoveS2CPacket::Decode)
                .consumer(HandcuffedPlayerMoveS2CPacket::Handle)
                .add();
    }

    public static <MSG> void sentToAllPlayersTrackingPlayerAndSelf(PlayerEntity target, MSG message) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> target), message);
    }

    public static <MSG> void sendToAllPlayers(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }

    public static void sendToPlayer(ServerPlayerEntity player, HandcuffedPlayerS2CPacket handcuffedPlayerS2CPacket) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), handcuffedPlayerS2CPacket);
    }

    public static void sendToPlayer(ServerPlayerEntity player, HandcuffedPlayerMoveS2CPacket handcuffedPlayerS2CPacket) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), handcuffedPlayerS2CPacket);
    }
}
