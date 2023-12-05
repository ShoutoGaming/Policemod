package dev.polv.policeitemsmod.common.item.custom;

import dev.polv.policeitemsmod.common.HandcuffHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/**
 * @author Pol Vallverdu (polv.dev)
 */
public class HandcuffKeysItem extends Item {

    public HandcuffKeysItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (world.isClientSide) return super.use(world, player, hand);

        if (HandcuffHandler.isPlayerCuffed(player)) {
            HandcuffHandler.usedKeysS(player);
            return ActionResult.consume(player.getItemInHand(hand));
        } else if (HandcuffHandler.isPlayerCuffer(player)) {
            PlayerEntity handcuffedPlayer = HandcuffHandler.getHandcuffed(player);
            if (handcuffedPlayer == null) return super.use(world, player, hand);

            HandcuffHandler.usedKeysS(handcuffedPlayer);
        }

        return super.use(world, player, hand);
    }
}
