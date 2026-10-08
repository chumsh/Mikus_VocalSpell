package com.chunshui.phit.mikus_vocal_spell.entity.spells.nom_nom_song;

import com.chunshui.phit.mikus_vocal_spell.registries.AttachmentRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEntityRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.VocalSpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class LuoTianYiProjectile extends AbstractMagicProjectile implements GeoEntity {
    private int hasShrink;

    public LuoTianYiProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public LuoTianYiProjectile(Level level) {
        super(MVSEntityRegistry.LTY_EATING.get(), level);
        super.setNoGravity(true);
    }

    @Override
    public void trailParticles() {
        Vec3 vec3 = getDeltaMovement();
        double d0 = this.getX() - vec3.x;
        double d1 = this.getY() - vec3.y;
        double d2 = this.getZ() - vec3.z;
        var count = Mth.clamp((int) (vec3.lengthSqr() * 4), 1, 4);
        for (int i = 0; i < count; i++) {
            Vec3 random = Utils.getRandomVec3(1).add(vec3.normalize()).scale(0.25);
            var f = i / ((float) count);
            var x = Mth.lerp(f, d0, this.getX() + vec3.x);
            var y = Mth.lerp(f, d1, this.getY() + vec3.y) - .4;
            var z = Mth.lerp(f, d2, this.getZ() + vec3.z);
            this.level().addParticle(ParticleTypes.TRIAL_OMEN, true, x - random.x, y + 0.5D - random.y, z - random.z, random.x * .5f, random.y * .5f, random.z * .5f);
        }
    }

    @Override
    protected void onHit(@NotNull HitResult hitresult) {
        if (level().isClientSide) return;
        if (hitresult instanceof EntityHitResult entityHitResult) {
            Entity target = entityHitResult.getEntity();
            Entity owner = getOwner();
            float baseDamage = getData(AttachmentRegistry.NOM_NOM_SONG_BASE_DAMAGE);
            int current = getData(AttachmentRegistry.CURRENT_CHARGE_OF_LUO);
            if (current < 0) return;
            int shrinkWanted = current * 3;
            if (owner instanceof Player player) {
                Inventory inventory = player.getInventory();

                for (int i = 0; i < inventory.getContainerSize(); i++) {
                    if (shrinkWanted == hasShrink) break;
                    ItemStack item = inventory.getItem(i);
                    if (item.getItem().getFoodProperties(item, player) != null) {
                        int count = item.getCount();
                        int shouldShrink = shrinkWanted - hasShrink;
                        if (count < shouldShrink) {
                            item.shrink(count);
                            hasShrink += count;
                        } else {
                            item.shrink(shouldShrink);
                            hasShrink = shrinkWanted;
                        }
                    }
                }
                if (hasShrink != 0) setDamage(baseDamage * hasShrink);
                else {
                    FoodData foodData = player.getFoodData();
                    int foodLevel = foodData.getFoodLevel();
                    if (foodLevel < 3 * current) setDamage(baseDamage);
                    else {
                        foodData.setFoodLevel(foodLevel - 3 * current);
                        setDamage(baseDamage * 3 * current);
                    }
                }
            } else setDamage(baseDamage * 3 * current);
            DamageSources.applyDamage(target, getDamage(), VocalSpellRegistry.NOM_NOM_SONG.get().getDamageSource(this, owner));
        }
        impactParticles(getX(), this.getBoundingBox().getCenter().y, getZ());
        hasShrink = 0;
        this.discardHelper(hitresult);
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        if (level() instanceof ServerLevel level)
            MagicManager.spawnParticles(level, ParticleTypes.POOF, x, y, z, 12, .08, .08, .08, 0.3, false);
    }

    public float getSpeed() {
        return 0.4F;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }
    private final AnimatableInstanceCache CACHE = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return CACHE;
    }
}
