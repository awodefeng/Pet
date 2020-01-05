package com.xxun.watch.xunpet.utils;

/**
 * Created by huangyouyang on 2017/9/21.
 */

public class RankUtils {

    // 根据经验计算等级
    public static double getRankFromExper(double exper) {
        double rank = 1;
        if (exper < 20d)
            rank = 1d;
        else
            rank = Math.sqrt((45d + 4d * exper) / 20d) - 1d / 2d;
        return rank;
    }

    // 根据等级计算经验
    public static double getExperFromRank(double rank) {
        double exper = 0;
        if (rank < 2d)
            exper = 0d;
        else
            exper = 5d * Math.pow(rank, 2d) + 5d * rank - 10d;
        return exper;
    }

    // 根据等级计算所需经验
    public static double getNeedExperFromRank(double rank) {
        double exper = 0;
        if (rank < 2d)
            exper = 0d;
        else
            exper = rank * 10d;
        return exper;
    }
}
