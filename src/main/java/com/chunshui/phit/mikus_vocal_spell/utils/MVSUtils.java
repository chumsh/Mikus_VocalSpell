package com.chunshui.phit.mikus_vocal_spell.utils;

import com.chunshui.phit.mikus_vocal_spell.registries.AttachmentRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class MVSUtils {
    public static final Map<String, List<Object>> cache = new ConcurrentHashMap<>();
    public static final String POINTS_KEY ="points";

    // 获取一定半径平面内的随机坐标
    public static List<Vec2> generateCirclePatternPoints(float radius, int count, float minRadiusRatio) {
        List<Vec2> points = new ArrayList<>();
        Random random = new Random();

        float minRadius = radius * minRadiusRatio;

        for (int i = 0; i < count; i++) {
            float baseAngle = (float)i / count * 2 * (float)Math.PI;
            float angleJitter = (random.nextFloat() - 0.5f) * 0.2f;
            float angle = baseAngle + angleJitter;

            float r = minRadius + random.nextFloat() * (radius - minRadius);
            float radiusJitter = (random.nextFloat() - 0.5f) * 0.1f * radius;
            r += radiusJitter;

            points.add(new Vec2(
                    r * (float)Math.cos(angle),
                    r * (float)Math.sin(angle)
            ));
        }

        return points;
    }

    //获取柱状粒子坐标
    public static List<Vec3> generateParticlePoints(int count, Vec3 pos) {
        List<Vec3> points = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            int yOffset = 2;//等同于半高
            double xzOffset = .15;
            double ox = pos.x;
            double oy = pos.y + yOffset;
            double oz = pos.z;
            double x = ox + Math.random() * 2 * xzOffset - xzOffset;
            double y = oy + Math.random() * 2 * yOffset - yOffset;
            double z = oz + Math.random() * 2 * xzOffset - xzOffset;

            points.add(new Vec3(x, y, z));
        }
        return points;
    }

    //获取特定圆形等均匀分布坐标
    public static List<Vec3> generateCirclePoints(int radius, int density, Vec3 pos) {
        List<Vec3> points = new ArrayList<>();
        float angel = (float) (Math.PI / 8);
        int count = ( radius - density ) / density;

        for (int i = 0; i < count; i++) {
            int newRadius = radius - density * (count - i);
            PixelCircleGenerate.PerimeterResult result = PixelCircleGenerate.calculatePerimeterOptimized(newRadius);
            int time = result.pointCount;
            List<Object> list = cache.computeIfAbsent(POINTS_KEY, k -> new ArrayList<>());
            list.add(time);
            for (int j = 0; j < time; j++) {
                double x = result.points[j][0] + pos.x;
                double y = pos.y;
                double z = result.points[j][1] + pos.z;

                double relativeX = x - pos.x;

                double threshold = Math.cos(angel) * newRadius;
                if (Math.abs(relativeX - threshold) > 0.001) {
                    points.add(new Vec3(x, y, z));
                }
            }
        }
        return points;
    }

    //获取多态法术的当前形态
    public static int getCurrentForm(LivingEntity entity) {
        if (entity != null) {
            return entity.getData(AttachmentRegistry.CURRENT_FORM);
        }
        return -1;
    }

    public static void updateCurrentForm(LivingEntity entity) {
        if (entity != null) {
//            MikusVocalSpellIronsSpellsAddon.LOGGER.debug("update side is client: {}", entity.level().isClientSide);
            int oldForm = getCurrentForm(entity);
            entity.setData(AttachmentRegistry.CURRENT_FORM, oldForm +1);
        }
    }

    public static void resetCurrentForm(LivingEntity entity) {
        if (entity != null) {
//            MikusVocalSpellIronsSpellsAddon.LOGGER.debug("reset side is client: {}", entity.level().isClientSide);
            entity.setData(AttachmentRegistry.CURRENT_FORM, 1);
        }
    }

    //获取玩家周围的实体
    public static List<LivingEntity> getEntitiesAroundPlayer(Player player, int radius) {
        if (player != null) {
            Vec3 pos = player.position();
            AABB boundingBox = AABB.ofSize(pos, radius, radius, radius);
            return player.level().getEntitiesOfClass(LivingEntity.class, boundingBox);
        }
        return null;
    }

    //读写ItemStack到NBT(代码源自Iron' spells)
    public static void saveAllItems(CompoundTag tag, NonNullList<ItemStack> inputItems, String key, HolderLookup.@NotNull Provider registries) {
        ListTag listTag = new ListTag();
        if (inputItems.isEmpty()) return;
        for (int i = 0; i < inputItems.size(); i++) {
            ItemStack itemStack = inputItems.get(i);
            if (!itemStack.isEmpty()) {
                CompoundTag myTag = new CompoundTag();
                myTag.putByte("slot", (byte) i);
                listTag.add(itemStack.save(registries, myTag));
            }
        }
        if (listTag.isEmpty()) return;
        tag.put(key, listTag);
    }

    public static void loadAllItems(CompoundTag tag,NonNullList<ItemStack> inputItems, String key, HolderLookup.@NotNull Provider registries) {
        ListTag tags = tag.getList(key, Tag.TAG_COMPOUND);
        if (tags.isEmpty()) return;
        if (inputItems.isEmpty()) {
            inputItems = NonNullList.withSize(tags.size(), ItemStack.EMPTY);
        }
        for (int i = 0; i < tags.size(); i++) {
            CompoundTag compoundTag = tags.getCompound(i);
            int slot = compoundTag.getByte("slot") & 255;
            if (slot >= 0 && slot < inputItems.size()) {
                inputItems.set(slot, ItemStack.parse(registries, compoundTag).orElse(ItemStack.EMPTY));
            }
        }
    }
}
