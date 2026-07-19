package com.chunshui.phit.mikus_vocal_spell.entity.spells.core_melt;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.model.GeoModel;

public class InnocenceShellModel extends GeoModel<InnocenceShellEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(
            MikusVocalSpellIronsSpellsAddon.MODID, "geo/innocence_shell.geo.json"
    );
    private  static final ResourceLocation TEXTURE_OWN = ResourceLocation.fromNamespaceAndPath(
            MikusVocalSpellIronsSpellsAddon.MODID, "textures/entity/innocence_shell/in_rotate.png"
    );
    private static final ResourceLocation TEXTURE_TWO = ResourceLocation.fromNamespaceAndPath(
            MikusVocalSpellIronsSpellsAddon.MODID, "textures/entity/innocence_shell/in_break.png"
    );
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(
            MikusVocalSpellIronsSpellsAddon.MODID, "animations/in.json"
    );

    @Override
    public ResourceLocation getModelResource(InnocenceShellEntity animatable) { return MODEL; }

    @Override
    public ResourceLocation getTextureResource(InnocenceShellEntity animatable) {
        Player player =  Minecraft.getInstance().player;
        if (player != null && player.getPersistentData().getInt(NBTKeyHelper.INNOCENCE_DURATION) > 30) {
            return TEXTURE_OWN;
        } else {
            return TEXTURE_TWO;
        }
    }

    @Override
    public ResourceLocation getAnimationResource(InnocenceShellEntity animatable) { return ANIMATION; }
}
