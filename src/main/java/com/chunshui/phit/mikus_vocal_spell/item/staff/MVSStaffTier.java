package com.chunshui.phit.mikus_vocal_spell.item.staff;


import com.chunshui.phit.mikus_vocal_spell.registries.MVSAttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.IronsWeaponTier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class MVSStaffTier implements IronsWeaponTier {

    public static MVSStaffTier MikuStaff = new MVSStaffTier(5,3,
            new AttributeContainer(AttributeRegistry.CAST_TIME_REDUCTION, .15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(MVSAttributeRegistry.VOCAL_SPELL_POWER, .15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            );


    public MVSStaffTier(float damage, float speed, AttributeContainer... issAttributes){
        this.damage = damage;
        this.speed = speed;
        this.issAttributes = issAttributes;

    }

    float damage;
    float speed;
    AttributeContainer[] issAttributes;
    @Override
    public float getAttackDamageBonus() {
        return damage;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public AttributeContainer[] getAdditionalAttributes() {
        return this.issAttributes;
    }

}
