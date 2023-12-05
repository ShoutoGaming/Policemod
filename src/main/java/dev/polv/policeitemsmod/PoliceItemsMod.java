package dev.polv.policeitemsmod;

import dev.polv.policeitemsmod.common.item.ModItems;
import dev.polv.policeitemsmod.common.network.NetworkMessages;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * @author Pol Vallverdu (polv.dev)
 */
@Mod(PoliceItemsMod.MOD_ID)
public class PoliceItemsMod {
    public static final String MOD_ID = "policeitemsmod";

    public PoliceItemsMod() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        NetworkMessages.RegisterPackets();
        ModItems.Register(eventBus);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
    }

    private void setupClient(final FMLClientSetupEvent event)
    {
        event.enqueueWork(() ->
                ItemModelsProperties.register(
                        ModItems.BALISTIC_SHIELD.get(),
                        new ResourceLocation(MOD_ID, "blocking"),
                        (stack, world, entity) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
                )
        );
    }
}
