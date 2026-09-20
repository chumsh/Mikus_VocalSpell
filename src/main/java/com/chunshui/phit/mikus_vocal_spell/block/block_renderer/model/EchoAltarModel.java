package com.chunshui.phit.mikus_vocal_spell.block.block_renderer.model;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.block.block_entity.EchoAltarBE;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class EchoAltarModel extends GeoModel<EchoAltarBE> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(
            MikusVocalSpellIronsSpellsAddon.MODID, "geo/echo_altar.geo.json"
    );
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            MikusVocalSpellIronsSpellsAddon.MODID, "textures/block/echo_altar.png"
    );
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(
            MikusVocalSpellIronsSpellsAddon.MODID, "animations/cm.json"
    );

    @Override
    public ResourceLocation getModelResource(EchoAltarBE animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(EchoAltarBE animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(EchoAltarBE animatable) {
        return ANIMATION;
    }
}
