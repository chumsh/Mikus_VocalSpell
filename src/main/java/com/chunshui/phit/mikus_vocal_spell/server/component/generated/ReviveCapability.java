package com.chunshui.phit.mikus_vocal_spell.server.component.generated;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public class ReviveCapability {
    
    public static final int MAX_CHARGES = 3;
    private static final String TAG_KEY = "mikus_vocal_spell_revive";
    private static final String TAG_CHARGES = "charges";
    private static final String TAG_ACTIVE = "active";
    private boolean hasCanceled = false;

    private final ServerPlayer player;

    public ReviveCapability(ServerPlayer player) {
        this.player = player;
    }

    private CompoundTag getTag() {
        CompoundTag tag = player.getPersistentData().getCompound(TAG_KEY);
        if (!player.getPersistentData().contains(TAG_KEY)) {
            tag.putBoolean(TAG_ACTIVE, false);
            tag.putInt(TAG_CHARGES, 0);
            player.getPersistentData().put(TAG_KEY, tag);
        }
        return tag;
    }

    private void saveTag(CompoundTag tag) {
        player.getPersistentData().put(TAG_KEY, tag);
    }

    public void activate(int charges) {
        CompoundTag tag = getTag();
        tag.putBoolean(TAG_ACTIVE, true);
        tag.putInt(TAG_CHARGES, Math.min(charges, MAX_CHARGES));
        saveTag(tag);
    }

    public boolean consumeCharge() {
        CompoundTag tag = getTag();
        boolean isActive = tag.getBoolean(TAG_ACTIVE);
        int charges = tag.getInt(TAG_CHARGES);
        
        if (isActive && charges > 0) {
            charges--;
            tag.putInt(TAG_CHARGES, charges);
            if (charges == 0) {
                tag.putBoolean(TAG_ACTIVE, false);
            }
            saveTag(tag);
            return true;
        }
        return false;
    }

    public boolean hasCharges() {
        CompoundTag tag = getTag();
        return tag.getBoolean(TAG_ACTIVE) && tag.getInt(TAG_CHARGES) > 0;
    }

    public int getCharges() {
        return getTag().getInt(TAG_CHARGES);
    }

    public boolean isActive() {
        return getTag().getBoolean(TAG_ACTIVE);
    }

    public void ensureCanceled(boolean result) {
        hasCanceled = result;
    }

    public boolean hasCanceled(){
        return hasCanceled;
    }
}
