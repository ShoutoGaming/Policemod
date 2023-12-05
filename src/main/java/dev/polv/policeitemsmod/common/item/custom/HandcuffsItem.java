package dev.polv.policeitemsmod.common.item.custom;

import dev.polv.policeitemsmod.common.HandcuffHandler;
import dev.polv.policeitemsmod.raycast.EntityHit;
import dev.polv.policeitemsmod.utils.HandcuffUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pol Vallverdu (polv.dev)
 */
public class HandcuffsItem extends Item {
    private PlayerEntity targetPlayer;
    private static final int useTimeTicks = 5*20;

    public HandcuffsItem() {
        super(new Item.Properties().stacksTo(4).tab(ItemGroup.TAB_TOOLS));
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack item, PlayerEntity player, LivingEntity target, Hand hand) {
        if (!(target instanceof PlayerEntity)) {
            return ActionResultType.FAIL;
        }

        if (player.getCooldowns().isOnCooldown(item.getItem())) {
            return ActionResultType.FAIL;
        }

        this.targetPlayer = (PlayerEntity) target;
        player.startUsingItem(hand);
        return ActionResultType.SUCCESS;
    }

    @Override
    public int getUseDuration(ItemStack item) {
        return useTimeTicks;
    }

    @Override
    public void onUseTick(World world, LivingEntity entity, ItemStack item, int tick) {
        if (world.isClientSide()) {
            return;
        }

        final PlayerEntity player = (PlayerEntity) entity;

        // Raycast from player to player looking at*5 to check if they are still in range
        Vector3d to = player.getLookAngle().scale(5).add(player.position());
        List<EntityHit> hits = raycastEntities(player, new Vector3d(player.getX(), player.getEyeY(), player.getZ()), to);

        if (hits.isEmpty() || hits.get(0).getEntity().getId() != this.targetPlayer.getId()) {
            player.stopUsingItem();
            return;
        }

        HandcuffUtils.sendCuffingStatus((PlayerEntity) entity, this.targetPlayer, useTimeTicks - tick);

        if (tick > 1) {
            return;
        }

        player.getCooldowns().addCooldown(item.getItem(), 50);

        ((PlayerEntity) entity).getCooldowns().addCooldown(item.getItem(), 50);
        ScaleData motionScaleData = ScaleTypes.MOTION.getScaleData(this.targetPlayer);
        ScaleData reachScaleData = ScaleTypes.REACH.getScaleData(this.targetPlayer);

        HandcuffUtils.sendCuffedMessage((PlayerEntity) entity, this.targetPlayer);

        HandcuffHandler.addHandcuffS((PlayerEntity) entity, this.targetPlayer);
        motionScaleData.setScale(0.0F);
        reachScaleData.setScale(0.0F);

        player.stopUsingItem();
    }

    public List<EntityHit> raycastEntities(Entity except, Vector3d startPos, Vector3d endPos) {
        AxisAlignedBB box = new AxisAlignedBB(startPos, endPos);

        List<Entity> entities = except.level.getEntities(except, box, entity -> (entity instanceof LivingEntity));
        List<EntityHit> entitiesHit = new ArrayList<>();
        entities.forEach(entity -> {
            AxisAlignedBB entityBox = entity.getBoundingBox();

            Vector3d hitPos = entityBox.clip(startPos, endPos).orElse(null);

            LivingEntity livingEntity = (LivingEntity) entity;
            if (livingEntity.isAlive()) {
                entitiesHit.add(new EntityHit(livingEntity, hitPos));
            }
        });

        return entitiesHit;
    }
}
