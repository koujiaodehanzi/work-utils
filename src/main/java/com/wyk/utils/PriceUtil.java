package com.wyk.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class PriceUtil {

	/**
	 * 把元转换成分
	 * 
	 * @param price
	 * @return
	 */
	public static long convertToFen(float price) {
		return PriceUtil.convertToFen(Float.toString(price));
	}

	/**
	 * 把元转换成分
	 * 
	 * @param price 单位:元
	 * @return 返回值
	 */
	public static long convertToFen(String price) {
		return new BigDecimal(price).multiply(new BigDecimal(100f)).longValue();
	}

	/**
	 * 格式化订单金额.
	 * 
	 * <pre>
	 * 格式：整数位不前补零,小数位补齐2位即：不超过10位整数位+1位小数点+2位小数
	 * 无效格式如123，.10，1.1
	 * 有效格式如1.00，0.10
	 * </pre>
	 * 
	 * @param amount 以分为单位的金额
	 * @return
	 */
	public static String trans2YuanStr(Long amount) {
		return trans2YuanStr(amount, 2, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 *
	 * @param amount	以分为单位的金额
	 * @param scale		保留小数位数
	 * @return
	 */
	public static String trans2YuanStr(Long amount, int scale) {
		return trans2YuanStr(amount, 2, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 *
	 * @param amount	以分为单位的金额
	 * @param scale		保留小数位数
	 * @param roundingMode	进位策略
	 * @return
	 */
	public static String trans2YuanStr(Long amount, int scale, int roundingMode) {
		if(amount==null){
			return "";
		}
		final int MULTIPLIER = 100;
		return new BigDecimal(amount).divide(new BigDecimal(MULTIPLIER), scale, roundingMode).toString();
	}

	/**
	 * 得到整形元
	 * 
	 * @param ammount
	 * @return
	 */
	public static Long getLongPriceYuan(Long ammount) {
		return Long.valueOf(ammount / 100);
	}

	public static long convertToFen(BigDecimal price) {
		return price.multiply(new BigDecimal(100)).longValue();
	}

	/**
	 * 金额转换
	 * 
	 * @param price
	 * @return
	 */
	public static float convertToYuan(final Long price) {
		if (price == null) {
			return 0.00f;
		}
		BigDecimal p = BigDecimal.valueOf(price);
		return p.divide(BigDecimal.valueOf(100L), 2, BigDecimal.ROUND_UP).floatValue();
	}

	public static String formatDecimal(Object price) {
		DecimalFormat df = new DecimalFormat("####.00");
		String strPrice = df.format(price);
		return strPrice;
	}

	public static float convertToYuan(final BigDecimal price) {
		return convertToYuan(price.longValue());
	}

	/**
	 * 把字符串钱转换成分
	 * 
	 * @param price
	 * @return
	 */
	public static long moneyConvertLongPrice(String price) {
		return PriceUtil.convertToFen(new BigDecimal(price));
	}
	
	/**
	 * 金额由分转化为元
	 * 
	 * @param price
	 * @return
	 */
	public static String moneyConvertStr(long price) {
		BigDecimal bigDecimal = BigDecimal.valueOf(price);
		return String.valueOf(bigDecimal.floatValue() / 100);
	}

	public static Long convertToHoursForDistribution(Long minutes) {
		int min = Integer.parseInt(minutes.toString());
		int lesMin = min % 60;
		if (lesMin != 0) {
			return (minutes / 60) + 1;
		} else {
			return minutes / 60;
		}
	}

    /**
     * 使用java正则表达式去掉多余的.与0
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    public static void main(String[] args) {
		System.out.println(PriceUtil.subZeroAndDot(trans2YuanStr(1120L)));
	}
}
