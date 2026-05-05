package com.chunshui.phit.mikus_vocal_spell.entity.spells.scallion_dance;

import com.chunshui.phit.mikus_vocal_spell.entity.spells.AbstractGroundProjectile;
import com.chunshui.phit.mikus_vocal_spell.registries.EntityRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.VocalSpellRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.MVSUtils;
import com.chunshui.phit.mikus_vocal_spell.utils.ParticleHelper;
import io.redspace.ironsspellbooks.api.entity.NoKnockbackProjectile;
import io.redspace.ironsspellbooks.damage.DamageSources;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ScallionProjectile extends AbstractGroundProjectile implements NoKnockbackProjectile {

    public ScallionProjectile(EntityType<? extends AbstractGroundProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public ScallionProjectile(Level level, LivingEntity entity) {
        super(EntityRegistry.SCALLION.get(), level, entity);
        this.setExpire_time(20 * 20);
        this.setCheck_interval(10);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void spawnParticles() {
        Vec3 pos = this.position();
        int amount = 1;
        List<Vec3> positions = MVSUtils.generateParticlePoints(amount, pos);
        for (int i = 0; i < amount; i++) {
            double x = positions.get(i).x;
            double y = positions.get(i).y;
            double z = positions.get(i).z;
            double xSpeed = Math.random() * 0.01 * 2 - 0.01;
            double ySpeed = Math.random() * 0.02 * 2 - 0.02;
            double zSpeed = Math.random() * 0.01 * 2 - 0.01;

            level().addParticle(ParticleHelper.SCALLION, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        var entity = entityHitResult.getEntity();
        DamageSources.applyDamage(entity, damage, VocalSpellRegistry.SCALLION_DANCE.get().getDamageSource(this, getOwner()));
    }

}
