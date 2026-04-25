package com.chunshui.phit.mikus_vocal_spell.registries;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class MVSDamageType {

    public static ResourceKey<DamageType> register(String name){
        return ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(MikusVocalSpellIronsSpellsAddon.MODID, name));
    }

    //VocalSpell 伤害类型
    public static final ResourceKey<DamageType> VOCAL_MAGIC = register("vocal_magic");

    public static void bootstrap(BootstrapContext<DamageType> context){
        context.register(VOCAL_MAGIC, new DamageType("vocal_magic", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0f));
    }
}
