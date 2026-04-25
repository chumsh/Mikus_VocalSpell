package com.chunshui.phit.mikus_vocal_spell.registries;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.item.weapon.aigenerated.ReviveTalismanItem;
import com.chunshui.phit.mikus_vocal_spell.item.staff.MVSStaffTier;
import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.item.UpgradeOrbItem;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;

public class ItemRegistry {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MikusVocalSpellIronsSpellsAddon.MODID);
    public static void register(IEventBus eventBus) { ITEMS.register(eventBus); }

    //注册物品
    /*the upgradeOrb is based on ISS*/
    public static final DeferredHolder<Item, Item> VOCAL_UPGRADE_ORB = ITEMS.register("vocal_upgrade_orb", () -> new UpgradeOrbItem(ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON).component(ComponentRegistry.UPGRADE_ORB_TYPE, MVSUpgradeOrbTypeRegistry.VOCAL_SPELL_POWER)));
    /*the magic staff is based on ISS*/
    public static DeferredHolder<Item, Item> MIKU_STAFF = ITEMS.register("miku_staff", ()-> new StaffItem(ItemPropertiesHelper.equipment(1).fireResistant().attributes(ExtendedSwordItem.createAttributes(MVSStaffTier.MikuStaff)).rarity(Rarity.EPIC)));
    
    // 重生护身符 - 右键使用获得三次重生机会
    public static final DeferredHolder<Item, Item> REVIVE_TALISMAN = ITEMS.register(
        "revive_talisman", 
        ReviveTalismanItem::new
    );

    //动画物品
    public static final DeferredHolder<Item, Item> REINCARNATION_APPLE = ITEMS.register("reincarnation_apple", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> REINCARNATION_APPLE_TWO = ITEMS.register("reincarnation_apple_two", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> REINCARNATION_APPLE_THREE = ITEMS.register("reincarnation_apple_three", () -> new Item(new Item.Properties()));



    public static Collection<DeferredHolder<Item, ? extends Item>> getMVSItems() {
        return ITEMS.getEntries();
    }
}
