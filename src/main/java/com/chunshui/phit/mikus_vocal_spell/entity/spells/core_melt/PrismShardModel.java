package com.chunshui.phit.mikus_vocal_spell.entity.spells.core_melt;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PrismShardModel extends GeoModel<PrismShardEntity> {
    private final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(MikusVocalSpellIronsSpellsAddon.MODID, "geo/prism_shard.geo.json");
    private final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(MikusVocalSpellIronsSpellsAddon.MODID, "textures/entity/prism_shard/prism_shard.png");
    private final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(MikusVocalSpellIronsSpellsAddon.MODID, "animations/ps.json");

    @Override
    public ResourceLocation getModelResource(PrismShardEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(PrismShardEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(PrismShardEntity animatable) {
        return ANIMATION;
    }
}
