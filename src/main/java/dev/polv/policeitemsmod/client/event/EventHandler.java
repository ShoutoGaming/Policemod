package dev.polv.policeitemsmod.client.event;

import dev.polv.policeitemsmod.PoliceItemsMod;
import dev.polv.policeitemsmod.common.HandcuffHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

/**
 * @author Pol Vallverdu (polv.dev)
 */
@Mod.EventBusSubscriber(modid = PoliceItemsMod.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class EventHandler {
    @SuppressWarnings("resource")
    @SubscribeEvent
    public static void onGuiOpenEvent(GuiOpenEvent event) {
        if (Minecraft.getInstance().level == null || !(Minecraft.getInstance().player instanceof PlayerEntity)) {
            return;
        }
        if (event.getGui() instanceof InventoryScreen) {
            if (HandcuffHandler.isPlayerCuffed(Minecraft.getInstance().player)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onServerDisconnect(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        HandcuffHandler.clear();
    }
}
