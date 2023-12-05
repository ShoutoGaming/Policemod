package dev.polv.policeitemsmod.common.item;

import dev.polv.policeitemsmod.common.item.custom.BalisticShieldItem;
import dev.polv.policeitemsmod.common.item.custom.HandcuffKeysItem;
import dev.polv.policeitemsmod.common.item.custom.HandcuffsItem;
import dev.polv.policeitemsmod.PoliceItemsMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author Pol Vallverdu (polv.dev)
 */
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PoliceItemsMod.MOD_ID);

    public static final RegistryObject<Item> HANDCUFFS = ITEMS.register("handcuffs", HandcuffsItem::new);
    public static final RegistryObject<Item> HANDCUFF_KEYS = ITEMS.register("handcuff_keys", () -> new HandcuffKeysItem(new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> BALISTIC_SHIELD = ITEMS.register("balistic_shield", () -> new BalisticShieldItem(new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_COMBAT)));

    public static void Register(final IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }

}
