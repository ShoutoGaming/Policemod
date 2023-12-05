package dev.polv.policeitemsmod.common.network.packet;

import java.util.UUID;
import java.util.function.Supplier;
import dev.polv.policeitemsmod.client.network.packet.PacketHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * @author Pol Vallverdu (polv.dev)
 */
public class HandcuffedPlayerS2CPacket {
    private boolean cuffed;
    private UUID handcuffer;
    private UUID handcuffed;

    public HandcuffedPlayerS2CPacket(boolean cuffed, UUID handcuffer, UUID handcuffed) {
        this.cuffed = cuffed;
        this.handcuffer = handcuffer;
        this.handcuffed = handcuffed;
    }

    public static void Encode(HandcuffedPlayerS2CPacket message, PacketBuffer buffer) {
        buffer.writeBoolean(message.cuffed);
        buffer.writeUUID(message.handcuffer);
        buffer.writeUUID(message.handcuffed);
    }

    public static HandcuffedPlayerS2CPacket Decode(PacketBuffer buffer) {
        return new HandcuffedPlayerS2CPacket(buffer.readBoolean(), buffer.readUUID(), buffer.readUUID());
    }

    public static void Handle(HandcuffedPlayerS2CPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> PacketHandler.HandcuffedPlayerHandlePacket(message, contextSupplier));
        });
        contextSupplier.get().setPacketHandled(true);
    }

    public boolean isCuffed() {
        return cuffed;
    }

    public UUID getHandcuffer() {
        return handcuffer;
    }

    public UUID getHandcuffed() {
        return handcuffed;
    }
}
