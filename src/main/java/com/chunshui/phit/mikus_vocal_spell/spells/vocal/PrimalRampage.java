package com.chunshui.phit.mikus_vocal_spell.spells.vocal;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.registries.AttachmentRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEffectRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSSchoolRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.AntiCancelChanting;
import com.chunshui.phit.mikus_vocal_spell.utils.AntiCancelCooldown;
import com.chunshui.phit.mikus_vocal_spell.utils.ConvertibleSpell;
import com.chunshui.phit.mikus_vocal_spell.utils.MVSUtils;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

public class PrimalRampage extends AbstractSpell implements ConvertibleSpell, AntiCancelCooldown, AntiCancelChanting {

    private final ResourceLocation SPELL_ID = ResourceLocation.fromNamespaceAndPath(
            MikusVocalSpellIronsSpellsAddon.MODID,
            "primal_rampage"
    );

    public PrimalRampage() {
        this.baseSpellPower = 1;
        this.baseManaCost = 300;
        this.castTime = 60;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.mikus_vocal_spell.current_form").append(Component
                         .translatable("message.mikus_vocal_spell.change_spell_form.primal_rampage_form" + MVSUtils.getCurrentForm(caster))),
                Component.translatable("ui.mikus_vocal_spell.vsinger.miku").withColor(3786171)
        );
    }

    private final DefaultConfig CONFIG = new DefaultConfig()
            .setMinRarity(SpellRarity.EPIC)
            .setSchoolResource(MVSSchoolRegistry.VOCAL_RESOURCE)
            .setMaxLevel(3)
            .setCooldownSeconds(300)
            .build();

    @Override
    public ResourceLocation getSpellResource() {
        return SPELL_ID;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return CONFIG;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public int getColor() {
        return 3786171;
    }

    @Override
    public String getMessageKey(int index) {
        return "message.mikus_vocal_spell.change_spell_form.primal_rampage_form" + index;
    }

    @Override
    public int getChangeableTime() {
        return 2;
    }

    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!(entity instanceof Player)) {
            return;
        }
        int currentForm = entity.getData(AttachmentRegistry.CURRENT_FORM);
        if (currentForm == 1) {
            entity.addEffect(new MobEffectInstance(
                    MVSEffectRegistry.PRIMAL_VANISH_EFFECT,
                    spellLevel * (int) (300 * (getSpellPower(spellLevel, entity))),
                    0,
                    false,
                    true,
                    true
            ));
        }
        if (currentForm == 2) {
            entity.addEffect(new MobEffectInstance(
                    MVSEffectRegistry.PRIMAL_CHANT_EFFECT,
                    spellLevel * (int) (100 * (getSpellPower(spellLevel, entity))),
                    0,
                    false,
                    true,
                    true
            ));
        }
    }
}



