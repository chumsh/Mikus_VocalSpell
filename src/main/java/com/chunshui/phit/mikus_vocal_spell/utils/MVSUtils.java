package com.chunshui.phit.mikus_vocal_spell.utils;

import com.chunshui.phit.mikus_vocal_spell.registries.AttachmentRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MVSUtils {
    public static final Map<String, List<Object>> cache = new ConcurrentHashMap<>();
    public static final String POINTS_KEY ="points";

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

    //检查是否为PVP
    public static boolean isPvP(Entity entity) {
        MinecraftServer server = entity.level().getServer();

        if (server == null)
            return false;
        return server.isPvpAllowed();
    }


}
