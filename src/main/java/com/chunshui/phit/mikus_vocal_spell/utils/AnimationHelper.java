package com.chunshui.phit.mikus_vocal_spell.utils;

import software.bernie.geckolib.animation.RawAnimation;

public class AnimationHelper {
    //CORE_MELT实体
    public static final RawAnimation CM_START = RawAnimation.begin().thenPlay("start");
    public static final RawAnimation CM_END = RawAnimation.begin().thenPlay("end");
    public static final RawAnimation IN_ROTATE = RawAnimation.begin().thenPlay("rotate");
    public static final RawAnimation IN_EXPLOSION = RawAnimation.begin().thenPlay("explosion");
    public static final RawAnimation PS_FISSION = RawAnimation.begin().thenPlay("fission");
    public static final RawAnimation PS_FLY = RawAnimation.begin().thenPlay("fly");
}
