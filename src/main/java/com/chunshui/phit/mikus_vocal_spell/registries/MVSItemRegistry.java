package com.chunshui.phit.mikus_vocal_spell.registries;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.item.ManaPotion;
import com.chunshui.phit.mikus_vocal_spell.item.staff.MVSStaffTier;
import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.item.UpgradeOrbItem;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class MVSItemRegistry {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MikusVocalSpellIronsSpellsAddon.MODID);
    public static void register(IEventBus eventBus) { ITEMS.register(eventBus); }

    //注册物品
    /*the upgradeOrb is based on ISS*/
    public static final DeferredHolder<Item, Item> VOCAL_UPGRADE_ORB = registerItem("vocal_upgrade_orb", (properties) -> new UpgradeOrbItem(properties.fireResistant().rarity(Rarity.EPIC).component(ComponentRegistry.UPGRADE_ORB_TYPE, MVSUpgradeOrbTypeRegistry.VOCAL_SPELL_POWER)));
    public static final DeferredHolder<Item, Item> MIKU_STAFF = registerItem("miku_staff", (properties) -> new StaffItem(properties.fireResistant().attributes(ExtendedSwordItem.createAttributes(MVSStaffTier.MikuStaff))));

    //动画物品
    public static final DeferredHolder<Item, Item> REINCARNATION_APPLE = ITEMS.register("reincarnation_apple", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> REINCARNATION_APPLE_TWO = ITEMS.register("reincarnation_apple_two", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> REINCARNATION_APPLE_THREE = ITEMS.register("reincarnation_apple_three", () -> new Item(new Item.Properties()));

    //一般物品
    public static final DeferredHolder<Item, Item> MANA_POTION = ITEMS.register("mana_potion", ()-> new ManaPotion( new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, Item> EMPTY_MANA_POTION = ITEMS.register("empty_mana_potion", ()-> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredHolder<Item, Item> VOCAL_ESSENCE = ITEMS.register("vocal_essence", () -> new Item(new Item.Properties()));

    private static <T extends Item>DeferredHolder<Item, T> registerItem (String name, Function<Item.Properties, T> itemFactory) {
        return ITEMS.register(name, ()-> itemFactory.apply(new Item.Properties()));
    }
}
