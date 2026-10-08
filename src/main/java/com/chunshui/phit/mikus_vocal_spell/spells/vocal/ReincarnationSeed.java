package com.chunshui.phit.mikus_vocal_spell.spells.vocal;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.registries.AttachmentRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEffectRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSSchoolRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import com.chunshui.phit.mikus_vocal_spell.utils.ParticleHelper;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

public class ReincarnationSeed extends AbstractSpell {

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.mikus_vocal_spell.reincarnation").append("3"),
                Component.translatable("ui.mikus_vocal_spell.remaining", getRemainingTime(spellLevel, getSpellPower(spellLevel,caster))),
                Component.translatable("ui.mikus_vocal_spell.introduction").append(Component.translatable(getEffectKey(spellLevel))),
                Component.translatable("ui.mikus_vocal_spell.vsinger.miku").withColor(3786171)
        );
    }

    private String getEffectKey(int spellLevel) {
        if (spellLevel <= 5) {
            return "ui.mikus_vocal_spell.introduction." + spellLevel;
        } else return "ui.mikus_vocal_spell.introduction.realme";

    }

    private static final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(
            MikusVocalSpellIronsSpellsAddon.MODID,
            "reincarnation_seed"
    );

    public ReincarnationSeed() {
        this.baseManaCost = 46;
        this.manaCostPerLevel = 30;
        this.baseSpellPower = 1;
        this.castTime = 40;
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchoolResource(MVSSchoolRegistry.VOCAL_RESOURCE)
            .setMaxLevel(6)
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

    public String getRemainingTime (int spellLevel, float spellPower) {
        int durationTicks = (int) ((200 + ((spellLevel - 1) * 200)) * spellPower);
        return Utils.timeFromTicks(durationTicks, 2);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity,
                       CastSource castSource, MagicData playerMagicData) {
        if (entity instanceof ServerPlayer player) {
            float spellPower = getSpellPower(spellLevel, player);
            player.getPersistentData().putFloat(NBTKeyHelper.REVIVE_SPELL_POWER, spellPower);
            int durationTicks = (int) ((200 + ((spellLevel - 1) * 200)) * spellPower);

            int chance = player.getData(AttachmentRegistry.REVIVE_CHANCE);
            if (chance <= 0) {
                if (spellLevel <= 5) {

                    player.addEffect(new MobEffectInstance(
                            MVSEffectRegistry.REVIVE_BUFF,
                            durationTicks,
                            spellLevel - 1,
                            false,
                            true,
                            true
                    ));
                } else {
                    int time = (int) ((600 * getSpellPower(spellLevel, entity)));
                    player.addEffect(new MobEffectInstance(MVSEffectRegistry.REAL_ME_EFFECT, time, spellLevel - 6, false, true));
                }

                player.serverLevel().sendParticles(ParticleHelper.REINCARNATION,
                        player.getX(),
                        player.getY() + 1.5,
                        player.getZ(),
                        24,
                        0.03,
                        0.0,
                        0.03,
                        0.008
                );
            }

            super.onCast(level, spellLevel, entity, castSource, playerMagicData);
        }
    }
}