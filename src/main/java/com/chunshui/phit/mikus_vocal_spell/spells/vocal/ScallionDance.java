package com.chunshui.phit.mikus_vocal_spell.spells.vocal;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.entity.spells.scallion_dance.ScallionProjectile;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSSchoolRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.AbstractConeProjectile;
import io.redspace.ironsspellbooks.spells.EntityCastData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;


import java.util.List;

@AutoSpellConfig
public class ScallionDance extends AbstractSpell {

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.mikus_vocal_spell.damage", Utils.stringTruncation(getSpellPower(spellLevel, caster), 2)),
                Component.translatable("ui.mikus_vocal_spell.vsinger.reincarnation").withColor(3786171)
        );
    }

    private static final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(
            MikusVocalSpellIronsSpellsAddon.MODID,
            "scallion_dance"
    );

    public ScallionDance(){
        this.baseSpellPower = 1;
        this.manaCostPerLevel = 2;
        this.baseManaCost = 6;
        this.spellPowerPerLevel = 1;
        this.castTime = 100;
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setAllowCrafting(true)
            .setCooldownSeconds(1)
            .setMaxLevel(10)
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(MVSSchoolRegistry.VOCAL_RESOURCE)
            .build();

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData){
        if (playerMagicData.isCasting() && playerMagicData.getCastingSpellId().equals(this.getSpellId())
                && playerMagicData.getAdditionalCastData() instanceof EntityCastData entityCastData
                && entityCastData.getCastingEntity() instanceof AbstractConeProjectile cone) {
            cone.setDealDamageActive();
        } else {
            ScallionProjectile scallionProjectile = new ScallionProjectile(world, entity);
            scallionProjectile.setPos(entity.position().add(0, entity.getEyeHeight() * .7, 0));
            scallionProjectile.setDamage(getDamage(spellLevel, entity));
            world.addFreshEntity(scallionProjectile);

            playerMagicData.setAdditionalCastData(new EntityCastData(scallionProjectile));
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public ResourceLocation getSpellResource() { return spellId; }

    @Override
    public DefaultConfig getDefaultConfig() { return defaultConfig; }

    @Override
    public CastType getCastType() { return CastType.CONTINUOUS; }

    @Override
    public boolean shouldAIStopCasting(int spellLevel, Mob mob, LivingEntity target) {
        return mob.distanceToSqr(target) > (10 * 10) * 1.2;
    }

    public float getDamage(int spellLevel, LivingEntity caster) {
        return 1 + getSpellPower(spellLevel, caster) * .75f;
    }
}
