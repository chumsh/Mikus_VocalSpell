package com.chunshui.phit.mikus_vocal_spell.entity.spells.core_melt;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.registries.AttachmentRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSDamageType;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEntityRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.AnimationHelper;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;

public class PrismShardEntity extends Projectile implements GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private static final int MAX_LIFETIME = 100;

    public PrismShardEntity(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public PrismShardEntity(Level level, LivingEntity owner) {
        super(MVSEntityRegistry.PRISM_SHARD.get(), level);
        this.setNoGravity(true);
        setOwner(owner);
        xRotO  = owner.getXRot();
        yRotO = owner.getYRot();
        setXRot(owner.getXRot());
        setYRot(owner.getYRot());
        MikusVocalSpellIronsSpellsAddon.LOGGER.info("PrismShardEntity tick{}{}",this.getYRot(), owner.getYRot());
    }

    @Override
    public void tick() {
        super.tick();

        if (getOwner() != null)
            MikusVocalSpellIronsSpellsAddon.LOGGER.info("PrismShardEntity created{}{}",this.getYRot(), getOwner().getYRot());

        if (this.tickCount >= MAX_LIFETIME) {
            this.discard();
            if (getOwner() == null)
                return;
            getOwner().setData(AttachmentRegistry.CORE_MELT_LEVEL, 0);
            getOwner().setData(AttachmentRegistry.TOTAL_DAMAGE, 0F);
        }
        if (!level().isClientSide) {
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() == HitResult.Type.ENTITY) {
                onHitEntity((EntityHitResult) hitresult);
            }
            if (hitresult.getType() == HitResult.Type.BLOCK) {
                if (hitresult instanceof BlockHitResult) {
                    super.onHitBlock((BlockHitResult) hitresult);
                }
                this.discard();
            }
        }
        this.setPos(position().add(getDeltaMovement()));
        //setYRot(45);
        if (level() instanceof ServerLevel serverLevel)
            serverLevel.sendParticles(getTrailParticle(), this.getX(), this.getY(), this.getZ(), 3, 0.0D, 0.0D, 0.0D, 0.1D);

    }
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.WITCH;
    }

    public void shoot(Vec3 angle) {
        setDeltaMovement(angle.scale(1.2));
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        int spellLevel = Objects.requireNonNull(getOwner()).getData(AttachmentRegistry.CORE_MELT_LEVEL);
        float damage = Objects.requireNonNull(getOwner()).getData(AttachmentRegistry.TOTAL_DAMAGE) * spellLevel;
        result.getEntity().hurt(this.damageSources().source(MVSDamageType.VOCAL_MAGIC), damage);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {}

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "fission", state -> {
            if (this.tickCount > 10 && this.tickCount <= 20)
              return state.setAndContinue(AnimationHelper.PS_FISSION);
            return PlayState.STOP;
        }));
        controllers.add(new AnimationController<>(this, "fly", state -> state.setAndContinue(AnimationHelper.PS_FLY)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }
}
