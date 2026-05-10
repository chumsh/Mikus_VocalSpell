package com.chunshui.phit.mikus_vocal_spell.spells.vocal;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEffectRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSSchoolRegistry;
import com.chunshui.phit.mikus_vocal_spell.server.component.generated.ReviveCapability;
import com.chunshui.phit.mikus_vocal_spell.server.component.generated.ReviveCapabilityManager;
import com.chunshui.phit.mikus_vocal_spell.utils.ParticleHelper;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

@AutoSpellConfig
public class ReincarnationSeed extends AbstractSpell {

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.mikus_vocal_spell.reincarnation").withColor(16777215),
                Component.translatable("ui.mikus_vocal_spell.vsinger.miku").withColor(3786171)
        );
    }

    private static final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(
            MikusVocalSpellIronsSpellsAddon.MODID,
            "reincarnation_seed"
    );

    public ReincarnationSeed() {
        this.baseManaCost = 226;
        this.baseSpellPower = 0;
        this.castTime = 40;
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.EPIC)
            .setSchoolResource(MVSSchoolRegistry.VOCAL_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(300)
            .build();

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }





    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity,
                       CastSource castSource, MagicData playerMagicData) {
        if (entity instanceof ServerPlayer player) {
            int durationTicks = 200 + ((spellLevel - 1) * 200);


            ReviveCapability capability = ReviveCapabilityManager.getReviveCapability(player);
            if (!capability.isActive()) {
                ReviveCapabilityManager.activateRevive(player, ReviveCapability.MAX_CHARGES);
                int buffAmplifier = Math.min(spellLevel-1,4);

                player.addEffect(new MobEffectInstance(
                        MVSEffectRegistry.REVIVE_BUFF,
                        durationTicks,
                        buffAmplifier,
                        false,
                        true,
                        true
                ));

                player.serverLevel().sendParticles(ParticleHelper.REINCARNATION,
                        player.getX(),
                        player.getY() + 1.5,
                        player.getZ(),
                        24,     // 粒子数量
                        0.03,     // X 扩散
                        0.0,     // Y 扩散
                        0.03,     // Z 扩散
                        0.008      // 速度)
                );

            }

            super.onCast(level, spellLevel, entity, castSource, playerMagicData);
        }
    }
}