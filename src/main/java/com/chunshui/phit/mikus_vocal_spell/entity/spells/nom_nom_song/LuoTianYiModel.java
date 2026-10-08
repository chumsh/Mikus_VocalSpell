package com.chunshui.phit.mikus_vocal_spell.entity.spells.nom_nom_song;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LuoTianYiModel extends GeoModel<LuoTianYiProjectile> {
    private static final ResourceLocation MODEL = MikusVocalSpellIronsSpellsAddon.id("geo/luo_tian_yi.geo.json");
    private static final ResourceLocation TEXTURE = MikusVocalSpellIronsSpellsAddon.id("textures/entity/lty_eating/luo_tian_yi.png");

    @Override
    public ResourceLocation getModelResource(LuoTianYiProjectile animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(LuoTianYiProjectile animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(LuoTianYiProjectile animatable) {
        return null;
    }
}
