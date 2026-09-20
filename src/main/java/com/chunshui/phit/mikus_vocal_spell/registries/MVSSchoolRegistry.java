package com.chunshui.phit.mikus_vocal_spell.registries;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.tags.ItemTags;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import static io.redspace.ironsspellbooks.api.registry.SchoolRegistry.SCHOOL_REGISTRY_KEY;

public class MVSSchoolRegistry extends SoundRegistry{
   
    private static final DeferredRegister<SchoolType> SCHOOLS = DeferredRegister.create(SCHOOL_REGISTRY_KEY, MikusVocalSpellIronsSpellsAddon.MODID);
    public static void register(IEventBus eventBus){ SCHOOLS.register(eventBus);}

    private static Supplier<SchoolType> registerSchool(SchoolType schoolType) {
        return SCHOOLS.register(schoolType.getId().getPath(), () -> schoolType);
    }


    public static final ResourceLocation VOCAL_RESOURCE = MikusVocalSpellIronsSpellsAddon.id("vocal");

    //学派注册
    public static final Supplier<SchoolType> VOCAL = registerSchool(new SchoolType(
            VOCAL_RESOURCE,
            ItemTags.VOCAL_FOCUS,
            Component.translatable("school.mikus_vocalspell.vocal").withStyle(Style.EMPTY.withColor(0x39c5bb)),
            MVSAttributeRegistry.VOCAL_SPELL_POWER,
            MVSAttributeRegistry.VOCAL_SPELL_RESIST,
            //暂时使用iss的音效
            SoundRegistry.NATURE_CAST,
            MVSDamageType.VOCAL_MAGIC
    ));
}



