package dev.polv.policeitemsmod.client;

import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;

/**
 * @author Pol Vallverdu (polv.dev)
 */
public class HandcuffPlayerModel {

    public static <T extends LivingEntity> void injectCustomPose(PlayerModel<T> model) {
        model.rightArm.xRot = (float) Math.toRadians(45);
        model.rightSleeve.xRot = (float) Math.toRadians(45);
        model.rightArm.zRot = (float) Math.toRadians(-35);
        model.rightSleeve.zRot = (float) Math.toRadians(-35);

        model.leftArm.xRot = (float) Math.toRadians(45);
        model.leftSleeve.xRot = (float) Math.toRadians(45);
        model.leftArm.zRot = (float) Math.toRadians(35);
        model.leftSleeve.zRot = (float) Math.toRadians(35);
    }

}
