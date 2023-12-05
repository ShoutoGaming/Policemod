package dev.polv.policeitemsmod.raycast;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;

/**
 * @author Pol Vallverdu (polv.dev)
 */
public class EntityHit {

    private LivingEntity entity;
    private Vector3d hitPos;

    public EntityHit(LivingEntity entity, Vector3d hitPos) {
        this.entity = entity;
        this.hitPos = hitPos;
    }

    public LivingEntity getEntity() {
        return entity;
    }

}
