package com.chunshui.phit.mikus_vocal_spell.spells.vocal;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.entity.spells.NoneCheckArea;
import com.chunshui.phit.mikus_vocal_spell.entity.spells.core_melt.*;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEffectRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSSchoolRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.ConvertibleSpell;
import com.chunshui.phit.mikus_vocal_spell.utils.MVSUtils;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

@AutoSpellConfig
public class CoreMelt extends AbstractSpell implements ConvertibleSpell {

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.mikus_vocal_spell.damage", Utils.stringTruncation(getDamage(spellLevel, caster), 2)),
                Component.translatable("ui.mikus_vocal_spell.remaining", 20),
                Component.translatable("ui.mikus_vocal_spell.vsinger.miku").withColor(getColor())
        );
    }

    private static final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(
            MikusVocalSpellIronsSpellsAddon.MODID,
            "core_melt"
    );

    public CoreMelt(){
        this.baseSpellPower = 1;
        this.manaCostPerLevel = 10;
        this.baseManaCost = 70;
        this.spellPowerPerLevel = 1;
        this.castTime = 100;
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setAllowCrafting(true)
            .setCooldownSeconds(15)
            .setMaxLevel(3)
            .setMinRarity(SpellRarity.EPIC)
            .setSchoolResource(MVSSchoolRegistry.VOCAL_RESOURCE)
            .build();

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        int index = MVSUtils.getCurrentForm();
        if (index == 1) {
            entity.addEffect(new MobEffectInstance(MVSEffectRegistry.INNOCENCE_EFFECT, 400));
            MobEffectInstance  effectInstance = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN);
            NoneCheckArea noneCheckArea = new NoneCheckArea(world, effectInstance);
            noneCheckArea.setOwner(entity);
            noneCheckArea.setAmplifier(spellLevel - 1);
            noneCheckArea.setDuration(400);
            noneCheckArea.setPos(entity.position());
            world.addFreshEntity(noneCheckArea);
        }
        if (index == 3) {
            boolean hasCoreMelt = entity.getPersistentData().getBoolean(NBTKeyHelper.CORE_MELT_SPAWN);
            MikusVocalSpellIronsSpellsAddon.LOGGER.debug("CoreMelt: {}", hasCoreMelt);
            if (!entity.getPersistentData().getBoolean(NBTKeyHelper.CORE_MELT_SPAWN)) {
                Vec3 angel = new Vec3(entity.getLookAngle().x(), 0, entity.getLookAngle().z());
                float angelRange = 25 * Mth.DEG_TO_RAD;
                float directionRange = Utils.random.nextFloat() * 2 + 2;
                Vec3 position = entity.position().add(angel.yRot(Utils.random.nextFloat() * angelRange * 2 - angelRange).scale(directionRange));
                CoreMeltEntity coreMelt = new CoreMeltEntity(world);
                CoreMeltRing coreMeltRing = new CoreMeltRing(world, entity, spellLevel);
                coreMelt.setPos(position);
                coreMeltRing.setPos(position);
                coreMeltRing.setOwner(entity);
                world.addFreshEntity(coreMelt);
                world.addFreshEntity(coreMeltRing);
            }
        }
    }

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

    public float getDamage(int spellLevel, LivingEntity caster) {
        return 1 + getSpellPower(spellLevel, caster) * .75f;
    }

    @Override
    public int getColor() {
        return 167539201;
    }

    @Override
    public String getMessageKey(int index) {
        return "message.mikus_vocal_spell.change_spell_form.core_melt_form" + index;
    }

    @Override
    public int getChangeableTime() {
        return 3;
    }
}
