package com.chunshui.phit.mikus_vocal_spell.entity.spells;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.PartEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractGroundProjectile extends Projectile {
    protected static final int FAILSAFE_EXPIRE_TIME = 20 * 20;
    private int check_interval = 20;
    private int expire_time;
    protected int age;
    protected int tickCounter;
    protected float damage;
    protected final ConePart[] subEntities;


    public AbstractGroundProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
        this.blocksBuilding = false;

        this.subEntities = new ConePart[] {
                new ConePart(this, "part1", 1.0f, 1.5f),
                new ConePart(this, "part2", 1.5f, 2.5f)
        };
    }

    public AbstractGroundProjectile(EntityType<? extends Projectile> entityType, Level level, LivingEntity owner) {
        this(entityType, level);
        setOwner(owner);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.expire_time == 0) {
            if (this.age++ > FAILSAFE_EXPIRE_TIME) {
                this.discard();
            }
        } else {
            if (this.age++ > expire_time) {
                this.discard();
            }
        }
        this.setPos(this.position());

        for (int i = 0; i < subEntities.length; i++) {
            var subEntity = subEntities[i];
            double height;
            if(i > 0) {
                var subEntityOld = subEntities[i - 1];
                height = subEntityOld.getDimensions(null).height();
                Vec3 newPos = new Vec3(this.position().x, this.position().y + height, this.position().z);
                subEntity.setPos(newPos);
                subEntity.xo = newPos.x;
                subEntity.yo = newPos.y;
                subEntity.zo = newPos.z;
                subEntity.xOld = newPos.x;
                subEntity.yOld = newPos.y;
                subEntity.zOld = newPos.z;
            } else{
                subEntity.setPos(this.position());
                subEntity.xo = this.position().x;
                subEntity.yo = this.position().y;
                subEntity.zo = this.position().z;
                subEntity.xOld = this.position().x;
                subEntity.yOld = this.position().y;
                subEntity.zOld = this.position().z;
            }
        }

        if (!level().isClientSide) {
            if (this.tickCounter++ >= this.check_interval) {
                for (Entity entity : getSubEntityCollisions()) {
                    onHitEntity(new EntityHitResult(entity));
                }
                this.tickCounter = 0;
            }
            }
         else {
            spawnParticles();
        }
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    public abstract void spawnParticles();

    public void setExpire_time(int expire_time) { this.expire_time = expire_time; }

    public void setCheck_interval(int check_interval) { this.check_interval = check_interval; }

    protected Set<Entity> getSubEntityCollisions() {
        List<Entity> collisions = new ArrayList<>();
        for (Entity conepart : subEntities) {
            collisions.addAll(level().getEntities(conepart, conepart.getBoundingBox()));
        }

        return collisions.stream().filter(target ->
                target != getOwner() && target instanceof LivingEntity).collect(Collectors.toSet());
    }


    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    protected abstract void onHitEntity(@NotNull EntityHitResult entityHitResult);

    @Override
    public PartEntity<?> @NotNull [] getParts() {
        return this.subEntities;
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Override
    public void setId(int id) {
        super.setId(id);
        for (int i = 0; i < this.subEntities.length; i++)
            this.subEntities[i].setId(id + i + 1);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putFloat("Damage", this.damage);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.damage = pCompound.getFloat("Damage");
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {}
}
