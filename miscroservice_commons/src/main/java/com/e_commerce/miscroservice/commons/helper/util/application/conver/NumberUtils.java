package com.e_commerce.miscroservice.commons.helper.util.application.conver;

import java.math.BigDecimal;
import java.text.DecimalFormat;


public class NumberUtils {

    public static boolean isMoreZero(double num1) {

        if (num1 > 0) {
            return true;
        } else {
            return false;
        }

    }


    public static int aMoreb(String num1, String num2) {

        double a = Double.valueOf(num1);
        double b = Double.valueOf(num2);

        if ((a - b) > 0) {
            return 1;
        } else if ((a - b) == 0) {
            return 0;
        } else {
            return -1;
        }
    }

    public static double format(double d, String format) {
        DecimalFormat df = new DecimalFormat(format);
        String ds = df.format(d);
        return Double.parseDouble(ds);
    }

    public static double format2(double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        String ds = df.format(d);
        return Double.parseDouble(ds);
    }

    public static String format2Str(String d) {
        DecimalFormat df = new DecimalFormat("0.00");
        String ds = df.format(d);
        return ds;
    }

    public static String format1S(String dw) {
        Double d = Double.valueOf(dw);
        DecimalFormat df = new DecimalFormat("0");
        String ds = df.format(d);
        return ds;
    }

    public static String format1SD(String dw) {
        Double d = Double.valueOf(dw);
        if (d > 10000) {
            d = d / 10000;
        }
        DecimalFormat df = new DecimalFormat("0");
        String ds = df.format(d);
        return ds + "万";
    }

    public static double format4(double d) {
        DecimalFormat df = new DecimalFormat("0.0000");
        String ds = df.format(d);
        return Double.parseDouble(ds);
    }

    public static double format6(double d) {
        DecimalFormat df = new DecimalFormat("0.000000");
        String ds = df.format(d);
        return Double.parseDouble(ds);
    }

    /**
     * 将double类型的数字改成科学计数法
     * 将10000 改成10，000
     *
     * @param d
     * @return
     */
    public static String formatToTwo(String d) {
        DecimalFormat a = new DecimalFormat("#,##0");
        Double de = Double.valueOf(d);
        String frmStr = a.format(de);
        return frmStr;
    }

    /**
     * 将double类型的数字改成科学计数法
     * 将10000 改成10，000
     *
     * @param d
     * @return
     */
    public static String formatToTwo2(Double d) {
        DecimalFormat a = new DecimalFormat("#,##0.00");
        String frmStr = a.format(d);
        return frmStr;
    }

    public static double getDouble(String str) {
        if (str == null || str.equals("")) {
            return 0.0;
        }
        double ret = 0.0;
        try {
            ret = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            ret = 0.0;
        }
        return format6(ret);
    }

    public static long getLong(String str) {
        if (str == null || str.equals("")) {
            return 0L;
        }
        long ret = 0;
        try {
            ret = Long.parseLong(str);
        } catch (NumberFormatException e) {
            ret = 0;
        }
        return ret;
    }

    public static Long[] getLongs(String[] str) {

        if (str == null || str.length < 1) {
            return new Long[]{0L};
        }
        Long[] ret = new Long[str.length];
        for (int i = 0; i < str.length; i++) {
            ret[i] = getLong(str[i]);
        }
        return ret;
    }

    public static int getInt(String str) {
        if (str == null || str.equals("")) {
            return 0;
        }
        int ret = 0;
        try {
            ret = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            ret = 0;
        }
        return ret;
    }

    public static int compare(double x, double y) {
        BigDecimal val1 = new BigDecimal(x);
        BigDecimal val2 = new BigDecimal(y);
        return val1.compareTo(val2);
    }

    /**
     * @param d
     * @param len
     * @return
     */
    public static double ceil(double d, int len) {
        String str = Double.toString(d);
        int a = str.indexOf(".");
        if (a + 3 > str.length()) {
            a = str.length();
        } else {
            a = a + 3;
        }
        str = str.substring(0, a);
        return Double.parseDouble(str);
    }

    public static double ceil(double d) {
        return ceil(d, 2);
    }

    /**
     * 显示成百分比
     *
     * @param @param  scales
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     * @Title: percent
     */
    public static String percent(String scales) {

        if (scales.length() > 5) {

            scales = scales.substring(0, 5);
        }

        String resc = String.valueOf(ceil(Double.valueOf(scales), 2) * 100);

        resc = resc.substring(0, resc.indexOf(".")) + "%";

        return resc;

    }

    /**
     * 截取两位小数double型
     *
     * @param @param  d
     * @param @return 设定文件
     * @return double 返回类型
     * @throws
     * @Title: format2
     */
    public static String strFormat2(String d) {

        BigDecimal b = new BigDecimal(d);

        double f1 = b.setScale(2, BigDecimal.ROUND_FLOOR).doubleValue();

        DecimalFormat df = new DecimalFormat("######0.00");

        return df.format(f1);

    }

    /**
     * 截取两位小数double型
     *
     * @param @param  d
     * @param @return 设定文件
     * @return double 返回类型
     * @throws
     * @Title: format2
     */
    public static String strFormat2(double d) {

        BigDecimal b = new BigDecimal(d);

        double f1 = b.setScale(2, BigDecimal.ROUND_FLOOR).doubleValue();

        DecimalFormat df = new DecimalFormat("######0.00");

        return df.format(f1);

    }

    /**
     * 两位小数double型
     *
     * @param @param  d
     * @param @return 设定文件
     * @return double 返回类型
     * @throws
     * @Title: format2
     */
    public static String strFormat2Round(String d) {

        BigDecimal b = new BigDecimal(d);

        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();

        DecimalFormat df = new DecimalFormat("######0.00");

        return df.format(f1);

    }

    /**
     * 两位小数double型
     *
     * @param @param  d
     * @param @return 设定文件
     * @return double 返回类型
     * @throws
     * @Title: format2
     */
    public static String strFormat2Round(double d) {

        BigDecimal b = new BigDecimal(d);

        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();

        DecimalFormat df = new DecimalFormat("######0.00");

        return df.format(f1);

    }

    /**
     * * 两个Double数相加 *
     *
     * @param v1 *
     * @param v2 *
     * @return Double
     */
    public static Double add(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.add(b2).doubleValue());
    }

    /**
     * 多数字 加法
     *
     * @param scale   四舍五入 小数点 位数
     * @param numbers 需要相加的数字
     * @return 相加后的number
     */
    public static Double adds(int scale, Number... numbers) {
        if (scale < 0 || numbers.length < 2) {
            throw new IllegalArgumentException(
                    "保留小数点必须大于0  或 两个数字以上 才能相加");
        } else {
            Integer length = numbers.length;
            BigDecimal def = new BigDecimal(numbers[0].toString());
            for (int i = 1; i < length; i++) {
                def = def.add(new BigDecimal(numbers[i].toString()));
            }
            Double amt = def.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
            return amt;
        }
    }

    public static void main(String[] args) {
        System.out.println(adds(2, 100.0, 2.0));
    }

    /**
     * 多数字 减法
     *
     * @param scale   四舍五入 小数点 位数
     * @param numbers 第一个为被减数
     * @return 相加后的number
     */
    public static Double subtract(int scale, Number... numbers) {
        if (scale < 0 || numbers.length < 2) {
            throw new IllegalArgumentException(
                    "保留小数点必须大于0  或 两个数字以上 才能相加");
        } else {
            Integer length = numbers.length;
            BigDecimal def = new BigDecimal(numbers[0].toString());
            for (int i = 1; i < length; i++) {
                def = def.subtract(new BigDecimal(numbers[i].toString()));
            }
            Double amt = def.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
            return amt;
        }
    }


    /**
     * 多数字相除
     *
     * @param scale   保留小数位数
     * @param numbers 第一个 数字为被除数
     * @return
     */
    public static Double divide(int scale, Number... numbers) {
        if (scale < 0 || numbers.length < 2) {
            throw new IllegalArgumentException(
                    "保留小数点必须大于0  或 两个数字以上 才能相除");
        } else {
            Integer length = numbers.length;
            if (numbers[0].toString().equals("0.0") || numbers[0].toString().equals("0")) {
                return numbers[0].doubleValue();
            }
            BigDecimal def = new BigDecimal(numbers[0].toString());
            for (int i = 1; i < length; i++) {
                BigDecimal dd = new BigDecimal(numbers[i].toString());
                def = def.divide(dd, scale, BigDecimal.ROUND_HALF_UP);
            }
            Double amt = def.doubleValue();
            return amt;
        }
    }

    /**
     * 多数字相乘
     *
     * @param scale   保留小数位数
     * @param numbers
     * @return
     */
    public static Double multiply(int scale, Number... numbers) {
        if (scale < 0 || numbers.length < 2) {
            throw new IllegalArgumentException(
                    "保留小数点必须大于0  或 两个数字以上 才能相除");
        } else {
            Integer length = numbers.length;
            BigDecimal def = new BigDecimal(numbers[0].toString());
            for (int i = 1; i < length; i++) {
                def = def.multiply(new BigDecimal(numbers[i].toString()));
            }
            Double amt = def.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
            return amt;
        }
    }


    /**
     * * 两个Double数相减 *
     *
     * @param v1 *
     * @param v2 *
     * @return Double
     */
    public static Double sub(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.subtract(b2).doubleValue());
    }

    /**
     * * 两个Double数相乘 *
     *
     * @param v1 *
     * @param v2 *
     * @return Double
     */
    public static Double mul(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.multiply(b2).doubleValue());
    }

    /**
     * * 两个Double数相除 *
     *
     * @param v1 *
     * @param v2 *
     * @return Double
     */
    public static Double div(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    /**
     * * 两个Double数相除，并保留scale位小数 *
     *
     * @param v1    *
     * @param v2    *
     * @param scale *
     * @return Double
     */
    public static Double div(Double v1, Double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {

        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * 判断是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
		/*for (int i = str.length();--i>=0;){
			if (!Character.isDigit(str.charAt(i))){
				return false;
			}
		}
		return true;*/

        Boolean strResult = str.matches("-?[0-9]+.*[0-9]*");
        if (strResult == true) {
            return true;
        } else {
            return false;
        }
    }


}
