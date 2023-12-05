package dev.polv.policeitemsmod.common.event;

import dev.polv.policeitemsmod.PoliceItemsMod;
import dev.polv.policeitemsmod.common.HandcuffHandler;
import dev.polv.policeitemsmod.common.item.custom.HandcuffKeysItem;
import dev.polv.policeitemsmod.common.network.NetworkMessages;
import dev.polv.policeitemsmod.common.network.packet.HandcuffedPlayerMoveS2CPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

/**
 * @author Pol Vallverdu (polv.dev)
 */
@Mod.EventBusSubscriber(modid = PoliceItemsMod.MOD_ID, bus = Bus.FORGE)
public class EventHandler {
    @SubscribeEvent
    public static void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (HandcuffHandler.isPlayerCuffed(event.getPlayer()) && !(event.getItemStack().getItem() instanceof HandcuffKeysItem)) {
            event.setCanceled(true);
            event.setCancellationResult(ActionResultType.FAIL);
        }
    }

    @SubscribeEvent
    public static void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
        HandcuffHandler.removeHandcuffD(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        HandcuffHandler.sendHandcuffs(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        HandcuffHandler.removeHandcuffD(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!event.side.isServer() || event.phase != TickEvent.Phase.END) return;

        if (HandcuffHandler.isPlayerCuffed(event.player)) {
            event.player.setDeltaMovement(0, 0, 0);
            PlayerEntity player = HandcuffHandler.getHandcuffer(event.player);
            if (player == null) return;

            Vector3d handcuffedPos = player.getLookAngle().scale(2.5).add(player.position());
            handcuffedPos = new Vector3d(handcuffedPos.x, player.getY(), handcuffedPos.z);

            // Player should look to handcuffed player-180 degrees
            event.player.xRot = 0;
            event.player.yRot = player.yRot;

//            event.player.teleportToWithTicket(handcuffedPos.x, player.getY(), handcuffedPos.z);
            event.player.setPos(handcuffedPos.x, handcuffedPos.y, handcuffedPos.z);
            event.player.setPosAndOldPos(handcuffedPos.x, handcuffedPos.y, handcuffedPos.z);
            NetworkMessages.sendToPlayer((ServerPlayerEntity) event.player, new HandcuffedPlayerMoveS2CPacket(event.player.getUUID(), handcuffedPos));
        }
    }

    @SubscribeEvent
    public static void onEntityDamage(LivingDamageEvent event) {
        if (!(event.getEntityLiving() instanceof PlayerEntity)) return;

        if (HandcuffHandler.isPlayerCuffed((PlayerEntity) event.getEntityLiving()) && !(event.getSource().getEntity() instanceof PlayerEntity)) {
            event.setCanceled(true);
            event.setAmount(0f);
        }
    }

}
