package com.chunshui.phit.mikus_vocal_spell.registries;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.entity.spells.core_melt.CoreMeltEntity;
import com.chunshui.phit.mikus_vocal_spell.entity.spells.core_melt.CoreMeltRing;
import com.chunshui.phit.mikus_vocal_spell.entity.spells.scallion_dance.ScallionEffectArea;
import com.chunshui.phit.mikus_vocal_spell.entity.spells.scallion_dance.ScallionProjectile;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MVSEntityRegistry {

    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, MikusVocalSpellIronsSpellsAddon.MODID);

    public static void register(IEventBus eventBus) { ENTITIES.register(eventBus); }

    public static final DeferredHolder<EntityType<?>, EntityType<ScallionProjectile>> SCALLION =
            ENTITIES.register("scallion", () -> EntityType.Builder.<ScallionProjectile>of(ScallionProjectile::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(MikusVocalSpellIronsSpellsAddon.MODID, "scallion").toString())
            );

    public static final DeferredHolder<EntityType<?>, EntityType<ScallionEffectArea>> SCALLION_AREA =
        ENTITIES.register("scallion_area", () -> EntityType.Builder.<ScallionEffectArea>of(ScallionEffectArea::new, MobCategory.MISC)
                .sized(1.0F, 1.0F)
                .clientTrackingRange(128)
                .setUpdateInterval(20)
                .build("scallion_area")
        );

    public static final DeferredHolder<EntityType<?>, EntityType<CoreMeltEntity>> CORE_MELT =
            ENTITIES.register("core_melt", ()-> EntityType.Builder.<CoreMeltEntity>of(CoreMeltEntity::new, MobCategory.MISC)
                    .sized(1.5F, 1.5F)
                    .clientTrackingRange(32)
                    .build(ResourceLocation.fromNamespaceAndPath(MikusVocalSpellIronsSpellsAddon.MODID, "core_melt").toString())
            );

    public static final DeferredHolder<EntityType<?>, EntityType<CoreMeltRing>> CORE_MELT_RING =
            ENTITIES.register("core_melt_ring", ()-> EntityType.Builder.<CoreMeltRing>of(CoreMeltRing::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(32)
                    .build(ResourceLocation.fromNamespaceAndPath(MikusVocalSpellIronsSpellsAddon.MODID, "core_melt_ring").toString())
            );

}
