package com.chunshui.phit.mikus_vocal_spell.utils;

public interface ConvertibleSpell {
    int getColor();
    //获取多态Spell 的索引
    String getMessageKey(int index);
    //获取多态Spell 的模式上限
    int getChangeableTime();
}
