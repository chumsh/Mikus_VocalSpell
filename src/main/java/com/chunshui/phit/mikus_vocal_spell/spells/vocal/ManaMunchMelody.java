package com.chunshui.phit.mikus_vocal_spell.spells.vocal;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEffectRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSSchoolRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

@AutoSpellConfig
public class ManaMunchMelody extends AbstractSpell {

    public ManaMunchMelody(){
        this.baseManaCost = 70;
        this.castTime = 40;
    }

    private static final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(
            MikusVocalSpellIronsSpellsAddon.MODID,
            "mana_munch_melody"
    );

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.mikus_vocal_spell.remaining", 180),
                Component.translatable("ui.mikus_vocal_spell.vsinger.luotianyi").withColor(6737151)
        );
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        entity.addEffect(new MobEffectInstance(
                MVSEffectRegistry.MELODY_EFFECT,
                180*20,
                0,
                false,
                true,
                true
        ));
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setAllowCrafting(true)
            .setCooldownSeconds(60)
            .setMaxLevel(1)
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(MVSSchoolRegistry.VOCAL_RESOURCE)
            .build();

    @Override
    public ResourceLocation getSpellResource() { return spellId; }

    @Override
    public DefaultConfig getDefaultConfig() { return defaultConfig; }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }
}
