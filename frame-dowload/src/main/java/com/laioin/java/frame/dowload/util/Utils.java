package com.laioin.java.frame.dowload.util;

import com.laioin.java.frame.dowload.cons.ByteKeys;
import com.laioin.java.frame.dowload.cons.Keys;

import java.text.DecimalFormat;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2016/8/11
 * Time on 14:09
 */
public class Utils {

    // 保留小数点后两位
    private static DecimalFormat DF = new DecimalFormat("#.##");

    /**
     * 格式化字节， 如：2.4 M
     *
     * @param fileSize 文件的大小
     * @return 文件大小 kb,b,m,g,tb
     */
    public static String formatByte(long fileSize) {
        StringBuffer sb = new StringBuffer();
        if (fileSize < ByteKeys.DATA_KB) {
            sb.append(fileSize).append(" B");
        } else if (fileSize >= ByteKeys.DATA_KB && fileSize < ByteKeys.DATA_M) {  // KB
            sb.append(DF.format(fileSize / ByteKeys.DATA_KB)).append(" KB");
        } else if (fileSize >= ByteKeys.DATA_M && fileSize < ByteKeys.DATA_G) {  // M
            sb.append(DF.format(fileSize / ByteKeys.DATA_M)).append(" M");
        } else if (fileSize >= ByteKeys.DATA_G && fileSize < ByteKeys.DATA_TB) { // G
            sb.append(DF.format(fileSize / ByteKeys.DATA_G)).append(" G");
        } else {  // TB
            sb.append(DF.format(fileSize / ByteKeys.DATA_TB)).append(" TB");
        }
        return sb.toString();
    }


    /**
     * 获取百分比
     *
     * @param some  已完成（需要计算百分比的，基数）
     * @param total 总共
     * @return
     */
    public static String formatRatio(long some, long total) {
        if (0 == total) {
            return null;
        }
        float d = some;
        StringBuffer sb = new StringBuffer();
        sb.append(DF.format((d / total) * 100)).append(Keys.CHAR_RATIO);
        return sb.toString();
    }

    /**
     * 格式化，时间
     *
     * @param date 需要格式化的时间，单位 秒
     * @return 格式化好的，日期： 如 12 小时
     */
    public static String formatDate(long date) {
        if (0 == date)
            return "0 秒";
        StringBuilder sb = new StringBuilder();
        if (date < Keys.DATE_A_MINUTES) {
            sb.append(date).append(" 秒");
        } else if (date >= Keys.DATE_A_MINUTES && date < Keys.DATE_A_HOUR) {  // 分钟
            sb.append(date / Keys.DATE_A_MINUTES).append(" 分");
            if (date % Keys.DATE_A_MINUTES != 0) {
                sb.append(" ").append(Utils.formatDate(date % Keys.DATE_A_MINUTES));
            }
        } else if (date >= Keys.DATE_A_HOUR && date < Keys.DATE_A_DAY) {  // 小时
            sb.append(date / Keys.DATE_A_HOUR).append(" 小时");
            if (date % Keys.DATE_A_HOUR != 0) {
                sb.append(" ").append(Utils.formatDate(date % Keys.DATE_A_HOUR));
            }
        } else { // 天
            sb.append(date / Keys.DATE_A_DAY).append(" 天");
            if (date % Keys.DATE_A_DAY != 0) {
                sb.append(" ").append(Utils.formatDate(date % Keys.DATE_A_DAY));
            }
        }
        return sb.toString();
    }
}
