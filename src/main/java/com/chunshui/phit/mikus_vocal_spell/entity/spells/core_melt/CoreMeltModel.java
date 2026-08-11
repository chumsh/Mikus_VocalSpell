package com.chunshui.phit.mikus_vocal_spell.entity.spells.core_melt;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CoreMeltModel extends GeoModel<CoreMeltEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(
            MikusVocalSpellIronsSpellsAddon.MODID, "geo/nuclear_reactor.geo.json"
    );
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            MikusVocalSpellIronsSpellsAddon.MODID, "textures/entity/core_melt/core_melt.png"
    );
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(
            MikusVocalSpellIronsSpellsAddon.MODID, "animations/cm.json"
    );

    @Override
    public ResourceLocation getModelResource(CoreMeltEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(CoreMeltEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(CoreMeltEntity animatable) {
        return ANIMATION;
    }
}
