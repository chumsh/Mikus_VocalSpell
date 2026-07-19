package com.chunshui.phit.mikus_vocal_spell.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

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
    public static int getCurrentForm() {
        Player player = Minecraft.getInstance().player;
        return player.getPersistentData().getInt(NBTKeyHelper.FORM_INDEX);
    }

    //获取玩家周围的实体
    public static List<LivingEntity> getEntitiesAroundPlayer(Player player, int radius) {
        if (player != null) {
            Vec3 pos = player.position();
            AABB boundingBox = AABB.ofSize(pos, radius, radius, radius);
            return player.level().getEntitiesOfClass(LivingEntity.class,  boundingBox);
        }
        return  null;
    }
}
