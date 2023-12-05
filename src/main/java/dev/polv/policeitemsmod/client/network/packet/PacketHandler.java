package dev.polv.policeitemsmod.client.network.packet;

import java.util.function.Supplier;

import dev.polv.policeitemsmod.common.HandcuffHandler;
import dev.polv.policeitemsmod.common.network.packet.HandcuffedPlayerMoveS2CPacket;
import dev.polv.policeitemsmod.common.network.packet.HandcuffedPlayerS2CPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * @author Pol Vallverdu (polv.dev)
 */
public class PacketHandler {
    @SuppressWarnings({ "resource", "static-access" })
    public static void HandcuffedPlayerHandlePacket(HandcuffedPlayerS2CPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        // Set the handcuffed status for the target player on the client-side.
        if (message.isCuffed()) {
            HandcuffHandler.addHandcuffC(message.getHandcuffer(), message.getHandcuffed());

            if (message.getHandcuffed().equals(Minecraft.getInstance().player.getUUID())) {
                Minecraft.getInstance().player.closeContainer();
                KeyBinding.releaseAll();
            }
        } else {
            HandcuffHandler.removeHandcuffC(message.getHandcuffer(), message.getHandcuffed());
        }
    }

    @SuppressWarnings({ "resource", "static-access" })
    public static void HandcuffedPlayerMoveHandlePacket(HandcuffedPlayerMoveS2CPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        // Set the handcuffed status for the target player on the client-side.
        if (message.getHandcuffed().equals(Minecraft.getInstance().player.getUUID())) {
            Minecraft.getInstance().player.setPos(message.getPos().x, message.getPos().y, message.getPos().z);
        }
    }
}
