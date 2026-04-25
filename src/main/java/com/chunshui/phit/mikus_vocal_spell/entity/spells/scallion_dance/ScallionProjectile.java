package com.chunshui.phit.mikus_vocal_spell.entity.spells.scallion_dance;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.registries.EntityRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.VocalSpellRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import io.redspace.ironsspellbooks.api.entity.NoKnockbackProjectile;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractConeProjectile;
import io.redspace.ironsspellbooks.entity.spells.AbstractShieldEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class ScallionProjectile extends AbstractConeProjectile implements NoKnockbackProjectile {

    private boolean hasSpawnedArea = false;

    public ScallionProjectile(EntityType<? extends AbstractConeProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public ScallionProjectile(Level level, LivingEntity entity) {
        super(EntityRegistry.SCALLION.get(), level, entity);

    }

    @Override
    public void tick() {
        if (!level().isClientSide && !hasSpawnedArea && dealDamageActive) {
            boolean NBT = Objects.requireNonNull(getOwner()).getPersistentData().getBoolean(ScallionEffectArea.SPAWN);
            MikusVocalSpellIronsSpellsAddon.LOGGER.info("player's SpawnedNBT: {}", NBT);
            Vec3 castStart = Objects.requireNonNull(getOwner()).getEyePosition();
            Vec3 castAngel = getOwner().getLookAngle().normalize();
            int distance = 15;
            Vec3 castEnd = castStart.add(castAngel.scale(distance));
            var searchBox = this.getBoundingBox().expandTowards(castAngel.scale(distance)).inflate(1.0);
            HitResult hitResult = ProjectileUtil.getEntityHitResult(
                    level(),
                    getOwner(),
                    castStart,
                    castEnd,
                    searchBox,
                    entity -> !entity.isSpectator() && canPlayerBeAttacked(entity) && entity.isPickable()
            );
            if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
                HitResult shieldResult = Utils.raycastForEntityOfClass(level(), this, getOwner().getEyePosition(), hitResult.getLocation(), false, AbstractShieldEntity.class);
                if (shieldResult.getType() == HitResult.Type.MISS) {
                    Vec3 pos = hitResult.getLocation().subtract(castAngel.scale(.5));
                    BlockPos blockPos = BlockPos.containing(pos.x, pos.y, pos.z);
                    if (level().getBlockState(blockPos).isAir() && !getOwner().getPersistentData().getBoolean(ScallionEffectArea.SPAWN)) {
                        ScallionEffectArea scallionEffectArea = new ScallionEffectArea(level(), getOwner());
                        scallionEffectArea.setPos(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);

                        level().addFreshEntity(scallionEffectArea);
                        hasSpawnedArea = true;
                    }
                }
            }
        }

        if (!level().isClientSide) {
            boolean hasSpawned = Objects.requireNonNull(getOwner()).getPersistentData().getBoolean(ScallionEffectArea.SPAWN);
            if (getOwner() instanceof LivingEntity player) {
                int cast_count = getOwner().getPersistentData().getInt(NBTKeyHelper.CASTING_COUNT);
                MikusVocalSpellIronsSpellsAddon.LOGGER.info("cast count: {}", cast_count);
                if (hasSpawned && MagicData.getPlayerMagicData(player).isCasting() && cast_count == 2) {
                    if (getOwner() instanceof Player owner) {
                        owner.displayClientMessage(Component.translatable("message.mikus_vocal_spell.scallion_area.spawn").withColor(16711680), true);
                    }
                }
            }
            super.tick();
        }
    }
    private boolean canPlayerBeAttacked(Entity entity) {
        if (getOwner() instanceof Player ownerPlayer) {
            if (entity instanceof Player targetPlayer) {
                return ownerPlayer.canHarmPlayer(targetPlayer);
            }else {
                return true;
            }
        }
        return true;
    }

    @Override
    public void spawnParticles() {

    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        var entity = entityHitResult.getEntity();
        DamageSources.applyDamage(entity, damage, VocalSpellRegistry.SCALLION_DANCE.get().getDamageSource(this, getOwner()));
    }

}
