package dev.polv.policeitemsmod.mixin;

import dev.polv.policeitemsmod.common.item.custom.BalisticShieldItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Pol Vallverdu (polv.dev)
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "isDamageSourceBlocked", at = @At("HEAD"), cancellable = true)
    private void injectBalisticShieldBlock(DamageSource p_184583_1_, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;

        if (livingEntity.isUsingItem() && livingEntity.getUseItem().getItem() instanceof BalisticShieldItem) {
            Vector3d vector3d2 = p_184583_1_.getSourcePosition();
            if (vector3d2 != null) {
                Vector3d vector3d = livingEntity.getViewVector(1.0F);
                Vector3d vector3d1 = vector3d2.vectorTo(livingEntity.position()).normalize();
                vector3d1 = new Vector3d(vector3d1.x, 0.0D, vector3d1.z);
                if (vector3d1.dot(vector3d) < 0.0D) {
                    cir.setReturnValue(true);
                }
            }
        }
    }

}
