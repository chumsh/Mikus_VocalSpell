package com.chunshui.phit.mikus_vocal_spell.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class ScallionParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    public ScallionParticle(ClientLevel level, double x, double y, double z, double xMotion, double yMotion, double zMotion, SpriteSet sprites) {
        super(level, x, y, z);
        this.scale(2.5f);
        // 设置运动速度
        this.xd = xMotion;
        this.yd = yMotion;
        this.zd = zMotion;
        this.sprites = sprites;

        // 设置粒子属性
        this.lifetime = 20;           // 存活时间（刻）
        this.gravity = 0f;          // 重力影响

        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;

        this.setSprite(sprites.get(0,1));
    }
    @Override
    public void tick() {
        // 更新位置
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        // 应用运动
        this.x += this.xd;
        this.y += this.yd;
        this.z += this.zd;

        // 应用重力
        this.yd -= this.gravity;

        //设置精灵图

        this.setSpriteFromAge(sprites);



        // 逐渐淡出
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            // 计算剩余生命周期的比例 (1.0 -> 0.0)
            this.alpha = 1.0f - (float) this.age/ this.lifetime; // 渐隐效果
        }
    }

    @Override
    protected int getLightColor(float pPartialTick) { return LightTexture.FULL_BRIGHT; }

    @Override
    public @NotNull ParticleRenderType getRenderType() { return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT; }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType particleType, @NotNull ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new ScallionParticle(level, x, y, z, dx, dy, dz, this.sprites);
        }

    }
}
