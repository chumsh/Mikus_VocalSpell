package com.chunshui.phit.mikus_vocal_spell.spells.vocal;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.entity.spells.nom_nom_song.LuoTianYiProjectile;
import com.chunshui.phit.mikus_vocal_spell.registries.AttachmentRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSSchoolRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;

public class NomNomSong extends AbstractSpell {
    public NomNomSong() {
        this.baseManaCost = 70;
        this.castTime = 20;
        this.baseSpellPower = 4;
        this.spellPowerPerLevel = 2;
    }

    private static final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(
            MikusVocalSpellIronsSpellsAddon.MODID,
            "nom_nom_song"
    );

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.mikus_vocal_spell.remaining", 180),
                Component.translatable("ui.mikus_vocal_spell.vsinger.luotianyi").withColor(6737151)
        );
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 32, .25f);
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!(world instanceof ServerLevel level)) return;
        if (!playerMagicData.getPlayerRecasts().hasRecastForSpell(getSpellId())) {
            playerMagicData.getPlayerRecasts().addRecast(new RecastInstance(getSpellId(), spellLevel, getRecastCount(spellLevel, entity), 320, castSource, null), playerMagicData);
        }
//        int remaining =  playerMagicData.getPlayerRecasts().getRemainingRecastsForSpell(this);
//        int total = getRecastCount(spellLevel, entity);
//        int current = total - remaining;
        //TODO:似乎这里的返回值有问题暂时手动计数

        int old = entity.getPersistentData().getInt("recast_time");
        int recastTime = old + 1;
        entity.getPersistentData().putInt("recast_time", recastTime);
        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData targetEntityCastData) {
            LivingEntity target = targetEntityCastData.getTarget(level);
            LuoTianYiProjectile luoTianYi = new LuoTianYiProjectile(world);
            luoTianYi.setData(AttachmentRegistry.CURRENT_CHARGE_OF_LUO, recastTime);
            luoTianYi.setData(AttachmentRegistry.NOM_NOM_SONG_BASE_DAMAGE, getSpellPower(spellLevel, entity));
            luoTianYi.setOwner(entity);
            Vec3 eyePosition = entity.getEyePosition();
            Vec3 actualPos = new Vec3(eyePosition.x(), eyePosition.y() + 1, eyePosition.z());
            luoTianYi.setPos(actualPos);
            luoTianYi.setHomingTarget(target);
            world.addFreshEntity(luoTianYi);
            luoTianYi.shoot(entity.getLookAngle());
            if (playerMagicData.getPlayerRecasts().getRemainingRecastsForSpell(this) <= 1)
                entity.getPersistentData().putInt("recast_time", 0);
        }
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setAllowCrafting(true)
            .setCooldownSeconds(10)
            .setMaxLevel(3)
            .setMinRarity(SpellRarity.UNCOMMON)
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

    @Override
    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        return spellLevel;
    }

    @Override
    public Vector3f getTargetingColor() {
        return new Vector3f(102F / 255F, 204F / 255F, 1);
    }
}
