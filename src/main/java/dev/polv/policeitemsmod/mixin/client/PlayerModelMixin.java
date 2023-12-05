package dev.polv.policeitemsmod.mixin.client;

import dev.polv.policeitemsmod.client.HandcuffPlayerModel;
import dev.polv.policeitemsmod.common.HandcuffHandler;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Pol Vallverdu (polv.dev)
 */
@Mixin(PlayerModel.class)
public class PlayerModelMixin<T extends LivingEntity> {

    @Inject(method = "setupAnim*", at = @At(value = "TAIL"))
    private void setupAnimTail(T entity, float animationPos, float animationSpeed, float animationBob, float deltaHeadYaw, float headPitch, CallbackInfo ci) {
        if(!(entity instanceof PlayerEntity))
            return;

        PlayerEntity player = (PlayerEntity) entity;

        PlayerModel<T> model = (PlayerModel<T>) (Object) this;
        ItemStack heldItem = player.getMainHandItem();

        boolean onHandCuff = HandcuffHandler.isPlayerCuffed(player);

        if (onHandCuff) {
            if (player.isLocalPlayer() && animationPos == 0.0F) {
                model.rightArm.xRot = 0;
                model.rightArm.yRot = 0;
                model.rightArm.zRot = 0;
                model.leftArm.xRot = 0;
                model.leftArm.yRot = 0;
                model.leftArm.zRot = 0;
            }

            HandcuffPlayerModel.injectCustomPose(model);
        }
    }

}
