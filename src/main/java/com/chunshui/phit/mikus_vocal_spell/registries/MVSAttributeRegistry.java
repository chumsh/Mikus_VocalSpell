package com.chunshui.phit.mikus_vocal_spell.registries;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import io.redspace.ironsspellbooks.api.attribute.MagicRangedAttribute;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = MikusVocalSpellIronsSpellsAddon.MODID)
public class MVSAttributeRegistry {

    private static final DeferredRegister<Attribute> ATTRIBUTES =  DeferredRegister.create(Registries.ATTRIBUTE, MikusVocalSpellIronsSpellsAddon.MODID);

    public static void register(IEventBus eventBus){ ATTRIBUTES.register(eventBus);}

    //属性注册
    public static final DeferredHolder<Attribute, Attribute> VOCAL_SPELL_RESIST = ATTRIBUTES.register("vocal_spell_resist", () -> (new MagicRangedAttribute("attribute.miku_vocal_spell.vocal_spell_resist", 1.0D, -100, 100).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> VOCAL_SPELL_POWER = ATTRIBUTES.register("vocal_spell_power", () -> (new MagicRangedAttribute("attribute.mikus_vocal_spell.vocal_spell_power", 1.0D, -100, 100).setSyncable(true)));

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent e) {
        e.getTypes().forEach(entity -> ATTRIBUTES.getEntries().forEach(attribute -> e.add(entity, attribute)));
    }

}
