package dev.polv.policeitemsmod.common.network.packet;

import dev.polv.policeitemsmod.client.network.packet.PacketHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * @author Pol Vallverdu (polv.dev)
 */
public class HandcuffedPlayerMoveS2CPacket {
    private UUID handcuffed;
    private Vector3d pos;

    public HandcuffedPlayerMoveS2CPacket(UUID handcuffed, Vector3d pos) {
        this.handcuffed = handcuffed;
        this.pos = pos;
    }

    public static void Encode(HandcuffedPlayerMoveS2CPacket message, PacketBuffer buffer) {
        buffer.writeUUID(message.handcuffed);
        buffer.writeDouble(message.pos.x);
        buffer.writeDouble(message.pos.y);
        buffer.writeDouble(message.pos.z);
    }

    public static HandcuffedPlayerMoveS2CPacket Decode(PacketBuffer buffer) {
        return new HandcuffedPlayerMoveS2CPacket(buffer.readUUID(), new Vector3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()));
    }

    public static void Handle(HandcuffedPlayerMoveS2CPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> PacketHandler.HandcuffedPlayerMoveHandlePacket(message, contextSupplier));
        });
        contextSupplier.get().setPacketHandled(true);
    }

    public UUID getHandcuffed() {
        return handcuffed;
    }

    public Vector3d getPos() {
        return pos;
    }
}
