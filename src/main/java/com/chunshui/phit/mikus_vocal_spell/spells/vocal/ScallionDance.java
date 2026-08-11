package com.chunshui.phit.mikus_vocal_spell.spells.vocal;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.entity.spells.scallion_dance.ScallionEffectArea;
import com.chunshui.phit.mikus_vocal_spell.entity.spells.scallion_dance.ScallionProjectile;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSSchoolRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.MVSUtils;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.spells.EntityCastData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;


import java.util.List;

public class ScallionDance extends AbstractSpell{
    public int scallionPoints;

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.mikus_vocal_spell.damage", Utils.stringTruncation(getDamage(spellLevel, caster), 2)),
                Component.translatable("ui.mikus_vocal_spell.remaining", 20),
                Component.translatable("ui.mikus_vocal_spell.vsinger.miku").withColor(3786171)
        );
    }

    private static final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(
            MikusVocalSpellIronsSpellsAddon.MODID,
            "scallion_dance"
    );

    public ScallionDance(){
        this.baseSpellPower = 1;
        this.manaCostPerLevel = 10;
        this.baseManaCost = 70;
        this.spellPowerPerLevel = 1;
        this.castTime = 100;
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setAllowCrafting(true)
            .setCooldownSeconds(15)
            .setMaxLevel(10)
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(MVSSchoolRegistry.VOCAL_RESOURCE)
            .build();

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData){
        if (!entity.getPersistentData().getBoolean(NBTKeyHelper.SCALLION_SPAWN)) {
            ScallionEffectArea scallionEffectArea = new ScallionEffectArea(world, entity, spellLevel);
            scallionEffectArea.setPos(entity.position());
            world.addFreshEntity(scallionEffectArea);
            playerMagicData.setAdditionalCastData(new EntityCastData(scallionEffectArea));
        }

        List<Vec3> positions = MVSUtils.generateCirclePoints(6, 2, entity.position());
        
        List<Object> list = MVSUtils.cache.get(MVSUtils.POINTS_KEY);
        if (list != null && !list.isEmpty()) {
            for (Object item : list) {
                if (item instanceof Integer amount) {
                    this.scallionPoints += amount;
                }
            }
        }
        for(int i = 0; i < scallionPoints && i < positions.size(); i ++ ) {
            Vec3 pos = positions.get(i);
            ScallionProjectile scallionProjectile = new ScallionProjectile(world, entity);
            scallionProjectile.setPos(pos);
            scallionProjectile.setDamage(getDamage(spellLevel, entity));
            world.addFreshEntity(scallionProjectile);
            playerMagicData.setAdditionalCastData(new EntityCastData(scallionProjectile));
        }
        MVSUtils.cache.clear();
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }


    @Override
    public ResourceLocation getSpellResource() { return spellId; }

    @Override
    public DefaultConfig getDefaultConfig() { return defaultConfig; }

    @Override
    public CastType getCastType() { return CastType.LONG; }

    @Override
    public boolean shouldAIStopCasting(int spellLevel, Mob mob, LivingEntity target) {
        return mob.distanceToSqr(target) > (10 * 10) * 1.2;
    }

    public float getDamage(int spellLevel, LivingEntity caster) {
        return 1 + getSpellPower(spellLevel, caster) * .75f;
    }
}
