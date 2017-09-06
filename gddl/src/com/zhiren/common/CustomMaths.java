package com.zhiren.common;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
/*
 * 作者：王磊
 * 时间：2009-10-18
 * 描述：增加计算String型数据进行减、乘、除法的方法
 */
/*
 * 作者：夏峥
 * 时间：2012-12-01
 * 描述：新增四舍五入方法，限制7位小数以内
 */

public class CustomMaths {
private static final int DEF_DIV_SCALE = 10;
	
	
	
	
	/**
	 * 
	 */
	
	public CustomMaths() {
		
		super();
	}
	
	
	
	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}
	
	
	
	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}
	
	public static String sub(String v1, String v2){
		BigDecimal b1;
		BigDecimal b2;
		try{
			b1 = new BigDecimal(v1);
			b2 = new BigDecimal(v2);
			return b1.subtract(b2).toString();
		}catch(Exception e){
			return null;
		}
	}
	
	
	
	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	
	public static double mul(double v1, double v2) {
		
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}
	
	public static String mul(String v1, String v2) {
		try{
			BigDecimal b1 = new BigDecimal(v1);
			BigDecimal b2 = new BigDecimal(v2);
			return b1.multiply(b2).toString();
		}catch(Exception e){
			return null;
		}
	}
	
	
	
	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		
		return div(v1, v2, DEF_DIV_SCALE);
	}
	
	
	
	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		double rtn = 0.0;
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		rtn = b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		if (Math.abs(rtn) < Math.pow(10, scale * -1)) {
			rtn = 0.0;
		}
		
		return rtn;
	}
	
	public static String div(String v1, String v2, int scale) {
		double rtn = 0.0;
		if (scale < 0) {
			return null;
		}
		try{
			BigDecimal b1 = new BigDecimal(v1);
			BigDecimal b2 = new BigDecimal(v2);
			rtn = b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
			
			if (Math.abs(rtn) < Math.pow(10, scale * -1)) {
				rtn = 0.0;
			}
			return String.valueOf(rtn);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	// public static double round(double v,int scale){
	// if(scale<0){
	// throw new IllegalArgumentException(
	// "The scale must be a positive integer or zero");
	// }
	// BigDecimal b = new BigDecimal(Double.toString(v));
	// BigDecimal one = new BigDecimal("1");
	// return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	// }
	//	
	public static long round(double v) {
		
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, 0, BigDecimal.ROUND_HALF_UP).longValue();
	}
	
	
	public static double round(Object o, int scale) {
		double rtn = 0.0;
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		double v = Double.valueOf(o.toString()).doubleValue();
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		rtn = b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		if (rtn < Math.pow(10, scale * -1)) {
			rtn = 0.0;
		}
		
		return rtn;
	}
	
	
	public static double round(String o, int scale) {
		double rtn = 0.0;
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		double v = Double.valueOf(o.toString()).doubleValue();
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		rtn = b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		if (rtn < Math.pow(10, scale * -1)) {
			rtn = 0.0;
		}
		
		return rtn;
		
	}
	
	
	
	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	
	public static double round(double v, int scale) {
		double rtn = 0.0;
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero.");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		rtn = b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		if (rtn < Math.pow(10, scale * -1)) {
			rtn = 0.0;
		}
		
		return rtn;
	}
	
	
	public String defaultNumberFormat(int _value) {
		
		String _result;
		
		NumberFormat nfInt = NumberFormat.getIntegerInstance();
		
		_result = nfInt.format(_value);
		
		return _result;
	}
	
	
	public String defaultNumberFormat(double _value) {
		
		String _result;
		
		NumberFormat nfNumber = NumberFormat.getNumberInstance();
		
		_result = nfNumber.format(_value);
		
		return _result;
	}
	
	
	public String defaultPercentFormat(double _value) {
		
		String _result;
		
		NumberFormat nfNumber = NumberFormat.getPercentInstance();
		
		_result = nfNumber.format(_value);
		
		return _result;
	}
	
	
	public String defaultCurrencyFormat(double _value) {
		
		String _result;
		
		NumberFormat nfNumber = NumberFormat.getCurrencyInstance();
		
		_result = nfNumber.format(_value);
		
		return _result;
	}
	
	
	public String defaultNumberFormat(Object obj) {
		
		String _result;
		
		NumberFormat nf = NumberFormat.getInstance();
		
		_result = nf.format(obj);
		
		return _result;
	}
	
	
	public static void customNumberFormat() {
		
		double x = 1000.0 / 3;
		
		System.out.println("default output is " + x);
		
		patternPrint("###,###.##", x);
		
		patternPrint("####.##", x);
		
		patternPrint("####.00", x);
		
		patternPrint("####.0#", x);
		
		patternPrint("00000.##", x);
		
		patternPrint("$###,###.##", x);
		
		patternPrint("0.###E0", x);
		
		patternPrint("00.##%", x);
		
		double y = 23.0012;
		
		System.out.println("default output is " + y);
		
		patternPrint("###,###.##", y);
		
		patternPrint("####.##", y);
		
		patternPrint("####.00", y);
		
		patternPrint("####.0#", y);
		
		patternPrint("00000.##", y);
		
		patternPrint("$###,###.##", y);
		
		patternPrint("0.###E0", y);
		
		patternPrint("00.##%", y);
		
		double z = 0.2012;
		
		System.out.println("default output is " + z);
		
		patternPrint("#.##", z);
		
		patternPrint("0.##", z);
		
		patternPrint("#.00", z);
		
		patternPrint("#.0#", z);
		
		patternPrint("0.##", z);
		
		patternPrint("$#.##", z);
		
		patternPrint("0.#E0", z);
		
		patternPrint("0.#%", z);
		
	}
	
	
	public static void patternPrint(String pattern, double x) {
		
		DecimalFormat df = new DecimalFormat(pattern);
		
		System.out.println("output for pattern " + pattern + " is "
				+ df.format(x));
		
	}
	
	
	public static String customNumberFormat(String pattern, double x) {
		
		String _result;
		
		DecimalFormat df = new DecimalFormat(pattern);
		
		_result = df.format(x);
		
		return _result;
		
	}
	
	
	public static String customNumberFormat(String pattern, String x) {
		
		String _result;
		
		double x1 = 0;
		
		try {
			x1 = Double.parseDouble(x);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			x1 = 0;
		}
		
		DecimalFormat df = new DecimalFormat(pattern);
		
		_result = df.format(x1);
		
		return _result;
		
	}
	
	
	public static int stringToInt(String x1) {
		
		int _result = 0;
		if (x1 == null) {
			x1 = "0";
		}
		if (x1.equals("")) {
			x1 = "0";
		}
		
		int y1 = 0;
		try {
			y1 = Integer.parseInt(x1);
			_result = y1;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return _result;
	}
	
	
	public static long stringToLong(String x1) {
		
		long _result = 0;
		if (x1 == null) {
			x1 = "0";
		}
		if (x1.equals("")) {
			x1 = "0";
		}
		
		long y1 = 0;
		try {
			y1 = Long.parseLong(x1);
			_result = y1;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return _result;
	}
	
	
	public static double stringToDouble(String x1) {
		
		double _result = 0;
		if (x1 == null) {
			x1 = "0";
		}
		if (x1.equals("")) {
			x1 = "0";
		}
		
		double y1 = 0;
		try {
			y1 = Double.parseDouble(x1);
			_result = y1;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return _result;
	}
	
	
	public static String toChineseMoney(String strNum) {
		
		int n, m, k, i, j, q, p, r, s = 0;
		int length, subLength, pstn;
		String change, output, subInput, input = strNum;
		output = "";
		if (strNum.equals("")) {
			return strNum;
		} else {
			length = input.length();
			pstn = input.indexOf('.'); // 小数点的位置
			
			if (pstn == -1) {
				subLength = length;// 获得小数点前的数字
				subInput = input;
			} else {
				subLength = pstn;
				subInput = input.substring(0, subLength);
			}
			
			char[] array = new char[4];
			char[] array2 = { '仟', '佰', '拾' };
			char[] array3 = { '亿', '万', '元', '角', '分','厘' ,'毫'};
			
			n = subLength / 4;// 以千为单位
			m = subLength % 4;
			
			if (m != 0) {
				for (i = 0; i < (4 - m); i++) {
					subInput = '0' + subInput;// 补充首位的零以便处理
				}
				n = n + 1;
			}
			k = n;
			
			for (i = 0; i < n; i++) {
				p = 0;
				change = subInput.substring(4 * i, 4 * (i + 1));
				array = change.toCharArray();// 转换成数组处理
				
				for (j = 0; j < 4; j++) {
					output += formatC(array[j]);// 转换成中文
					if (j < 3) {
						output += array2[j];// 补进单位，当为零是不补（千百十）
					}
					p++;
				}
				
				if (p != 0)
					output += array3[3 - k];// 补进进制（亿万元分角）
				// 把多余的零去掉
				
				String[] str = { "零仟", "零佰", "零拾" };
				for (s = 0; s < 3; s++) {
					while (true) {
						q = output.indexOf(str[s]);
						if (q != -1)
							output = output.substring(0, q) + "零"
									+ output.substring(q + str[s].length());
						else
							break;
					}
				}
				while (true) {
					q = output.indexOf("零零");
					if (q != -1) {
						output = output.substring(0, q) + "零"
								+ output.substring(q + 2);
					} else
						break;
				}
				String[] str1 = { "零亿", "零万", "零元" };
				for (s = 0; s < 3; s++) {
					while (true) {
						q = output.indexOf(str1[s]);
						if (q != -1) {
							output = output.substring(0, q)
									+ output.substring(q + 1);
						} else
							break;
					}
				}
				k--;
			}
			// 小数部分处理
			if (pstn != -1) {
				
				for (i = 1; i < length - pstn; i++) {
					if (input.charAt(pstn + i) != '0') {
						output += formatC(input.charAt(pstn + i));
						output += array3[2 + i];
					} else if (i < 2)
						output += "零";
					else
						output += "";
				}
			}
			if (output.substring(0, 1).equals("零"))
				output = output.substring(1);
			if (output.substring(output.length() - 1, output.length()).equals(
					"零"))
				output = output.substring(0, output.length() - 1);
			output = "￥" + output;
			return output += "整";
		}
		// return "￥" + chineseMoney;
	}
	
	
	public static String formatC(char x) {
		
		String a = "";
		switch (x) {
			case '0':
				a = "零";
				break;
			case '1':
				a = "壹";
				break;
			case '2':
				a = "贰";
				break;
			case '3':
				a = "叁";
				break;
			case '4':
				a = "肆";
				break;
			case '5':
				a = "伍";
				break;
			case '6':
				a = "陆";
				break;
			case '7':
				a = "柒";
				break;
			case '8':
				a = "捌";
				break;
			case '9':
				a = "玖";
				break;
		}
		return a;
	}
	
	
	public static String getMinValue(String s1, String s2) {
		
		JDBCcon cn = new JDBCcon();
		StringBuffer sql = new StringBuffer("");
		String _return = s1;
		if (s1 == null) {
			s1 = "";
		}
		if (s2 == null) {
			s2 = "";
		}
		s1 = s1.trim();
		s2 = s2.trim();
		sql.append("SELECT CASE WHEN '");
		sql.append(s1);
		sql.append("' < '");
		sql.append(s2);
		sql.append("' THEN '");
		sql.append(s1);
		sql.append("' ELSE '");
		sql.append(s2);
		sql.append("' END as minval FROM dual");
		ResultSetList rs = cn.getResultSetList(sql.toString());
		if (rs.next()) {
			_return = rs.getString("minval");
		}
		return _return;
	}
	
	
	public static String getMaxValue(String s1, String s2) {
		
		JDBCcon cn = new JDBCcon();
		StringBuffer sql = new StringBuffer("");
		String _return = s1;
		if (s1 == null) {
			s1 = "";
		}
		if (s2 == null) {
			s2 = "";
		}
		s1 = s1.trim();
		s2 = s2.trim();
		sql.append("SELECT CASE WHEN '");
		sql.append(s1);
		sql.append("' < '");
		sql.append(s2);
		sql.append("' THEN '");
		sql.append(s2);
		sql.append("' ELSE '");
		sql.append(s1);
		sql.append("' END as minval FROM dual");
		ResultSetList rs = cn.getResultSetList(sql.toString());
		if (rs.next()) {
			_return = rs.getString("minval");
		}
		return _return;
	}
	
	
	public static double Round_new(double value, int _bit) {
		// 四舍六入五成双的法则修约，
		// 即：
		// 1.拟舍弃数字的第一位大于5则进1，如24.236--->24.24,小于5则舍弃，如23.234--->23.23.
		// 2.拟舍弃数字的第一位等于5，且5后面的数字并非全为0时则进1，如23.2251--->23.23
		// 3.拟舍弃数字的第一位等于5，且5后面的数字全部为0时，若5前面一位为奇数，则进1成双，如23.235--->23.24;
		// 若5前面为偶数，则舍去，如23.225--->23.22
		double value1;// 拟舍弃数字的第一位等于5，且5前面的数字
		
		value1 = Math.floor(value * Math.pow(10, _bit))
				- Math.floor(value * Math.pow(10, _bit - 1)) * 10;
		double dbla = 0;
		dbla = (double) Math.round(value * Math.pow(10, _bit) * 10000000) / 10000000;
		
		if ((dbla - Math.floor(value * Math.pow(10, _bit))) >= 0.5
				&& (dbla - Math.floor(value * Math.pow(10, _bit))) < 0.6) {
			if ((dbla - Math.floor(value * Math.pow(10, _bit))) == 0.5) {
				if (value1 == 0 || value1 == 2 || value1 == 4 || value1 == 6
						|| value1 == 8) {
					return Math.floor(value * Math.pow(10, _bit))
							/ Math.pow(10, _bit);
				} else {
					return (Math.floor(value * Math.pow(10, _bit)) + 1)
							/ Math.pow(10, _bit);
				}
			} else {
				return Math.round(value * Math.pow(10, _bit))
						/ Math.pow(10, _bit);
			}
		} else {
			return Math.round(value * Math.pow(10, _bit)) / Math.pow(10, _bit);
		}
	}
	
	
	public static double Round_New(double value, int _bit) {
		
		double value1;// 拟舍弃数字
		
		value1 = Math.floor(value * Math.pow(10, _bit + 1))
				- Math.floor(value * Math.pow(10, _bit)) * 10.0;
		
		if (value1 / 10.0 < 0.6) {
			return Math.floor(value * Math.pow(10, _bit)) / Math.pow(10, _bit);
		} else {
			return (Math.floor(value * Math.pow(10, _bit)) + 1)
					/ Math.pow(10, _bit);
		}
	}
	
	
	public static double Round_New(double value, int _bit, double scale) {
		
		double value1;// 拟舍弃数字
		
		value1 = Math.floor(value * Math.pow(10, _bit + 1))
				- Math.floor(value * Math.pow(10, _bit)) * 10.0;
		
		if (value1 / 10.0 < scale) {
			return Math.floor(value * Math.pow(10, _bit)) / Math.pow(10, _bit);
		} else {
			return (Math.floor(value * Math.pow(10, _bit)) + 1)
					/ Math.pow(10, _bit);
		}
	}
	
//	四舍五入，限制7位小数以内
	public static double Round(double zhi, int ws){
		if(zhi>0){
			return Math.round(zhi*(Math.pow(10, ws))+0.00000001) / Math.pow(10, ws);
		}else if(zhi<0){
			zhi = zhi * -1;
			return Math.round(zhi*(Math.pow(10, ws))+0.00000001) / Math.pow(10, ws)* -1;
		}else{
			return 0;
		}
	}
}
