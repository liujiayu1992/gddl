package com.zhiren.common;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
/*
 * ���ߣ�����
 * ʱ�䣺2009-10-18
 * ���������Ӽ���String�����ݽ��м����ˡ������ķ���
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-12-01
 * �����������������뷽��������7λС������
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
	 * �ṩ��ȷ�ļӷ����㡣
	 * 
	 * @param v1
	 *            ������
	 * @param v2
	 *            ����
	 * @return ���������ĺ�
	 */
	public static double add(double v1, double v2) {
		
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}
	
	
	
	/**
	 * �ṩ��ȷ�ļ������㡣
	 * 
	 * @param v1
	 *            ������
	 * @param v2
	 *            ����
	 * @return ���������Ĳ�
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
	 * �ṩ��ȷ�ĳ˷����㡣
	 * 
	 * @param v1
	 *            ������
	 * @param v2
	 *            ����
	 * @return ���������Ļ�
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
	 * �ṩ����ԣ���ȷ�ĳ������㣬�����������������ʱ����ȷ�� С�����Ժ�10λ���Ժ�������������롣
	 * 
	 * @param v1
	 *            ������
	 * @param v2
	 *            ����
	 * @return ������������
	 */
	public static double div(double v1, double v2) {
		
		return div(v1, v2, DEF_DIV_SCALE);
	}
	
	
	
	/**
	 * �ṩ����ԣ���ȷ�ĳ������㡣�����������������ʱ����scale����ָ �����ȣ��Ժ�������������롣
	 * 
	 * @param v1
	 *            ������
	 * @param v2
	 *            ����
	 * @param scale
	 *            ��ʾ��ʾ��Ҫ��ȷ��С�����Ժ�λ��
	 * @return ������������
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
	 * �ṩ��ȷ��С��λ�������봦��
	 * 
	 * @param v
	 *            ��Ҫ�������������
	 * @param scale
	 *            С���������λ
	 * @return ���������Ľ��
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
	 * �ṩ��ȷ��С��λ�������봦��
	 * 
	 * @param v
	 *            ��Ҫ�������������
	 * @param scale
	 *            С���������λ
	 * @return ���������Ľ��
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
			pstn = input.indexOf('.'); // С�����λ��
			
			if (pstn == -1) {
				subLength = length;// ���С����ǰ������
				subInput = input;
			} else {
				subLength = pstn;
				subInput = input.substring(0, subLength);
			}
			
			char[] array = new char[4];
			char[] array2 = { 'Ǫ', '��', 'ʰ' };
			char[] array3 = { '��', '��', 'Ԫ', '��', '��','��' ,'��'};
			
			n = subLength / 4;// ��ǧΪ��λ
			m = subLength % 4;
			
			if (m != 0) {
				for (i = 0; i < (4 - m); i++) {
					subInput = '0' + subInput;// ������λ�����Ա㴦��
				}
				n = n + 1;
			}
			k = n;
			
			for (i = 0; i < n; i++) {
				p = 0;
				change = subInput.substring(4 * i, 4 * (i + 1));
				array = change.toCharArray();// ת�������鴦��
				
				for (j = 0; j < 4; j++) {
					output += formatC(array[j]);// ת��������
					if (j < 3) {
						output += array2[j];// ������λ����Ϊ���ǲ�����ǧ��ʮ��
					}
					p++;
				}
				
				if (p != 0)
					output += array3[3 - k];// �������ƣ�����Ԫ�ֽǣ�
				// �Ѷ������ȥ��
				
				String[] str = { "��Ǫ", "���", "��ʰ" };
				for (s = 0; s < 3; s++) {
					while (true) {
						q = output.indexOf(str[s]);
						if (q != -1)
							output = output.substring(0, q) + "��"
									+ output.substring(q + str[s].length());
						else
							break;
					}
				}
				while (true) {
					q = output.indexOf("����");
					if (q != -1) {
						output = output.substring(0, q) + "��"
								+ output.substring(q + 2);
					} else
						break;
				}
				String[] str1 = { "����", "����", "��Ԫ" };
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
			// С�����ִ���
			if (pstn != -1) {
				
				for (i = 1; i < length - pstn; i++) {
					if (input.charAt(pstn + i) != '0') {
						output += formatC(input.charAt(pstn + i));
						output += array3[2 + i];
					} else if (i < 2)
						output += "��";
					else
						output += "";
				}
			}
			if (output.substring(0, 1).equals("��"))
				output = output.substring(1);
			if (output.substring(output.length() - 1, output.length()).equals(
					"��"))
				output = output.substring(0, output.length() - 1);
			output = "��" + output;
			return output += "��";
		}
		// return "��" + chineseMoney;
	}
	
	
	public static String formatC(char x) {
		
		String a = "";
		switch (x) {
			case '0':
				a = "��";
				break;
			case '1':
				a = "Ҽ";
				break;
			case '2':
				a = "��";
				break;
			case '3':
				a = "��";
				break;
			case '4':
				a = "��";
				break;
			case '5':
				a = "��";
				break;
			case '6':
				a = "½";
				break;
			case '7':
				a = "��";
				break;
			case '8':
				a = "��";
				break;
			case '9':
				a = "��";
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
		// �����������˫�ķ�����Լ��
		// ����
		// 1.���������ֵĵ�һλ����5���1����24.236--->24.24,С��5����������23.234--->23.23.
		// 2.���������ֵĵ�һλ����5����5��������ֲ���ȫΪ0ʱ���1����23.2251--->23.23
		// 3.���������ֵĵ�һλ����5����5���������ȫ��Ϊ0ʱ����5ǰ��һλΪ���������1��˫����23.235--->23.24;
		// ��5ǰ��Ϊż��������ȥ����23.225--->23.22
		double value1;// ���������ֵĵ�һλ����5����5ǰ�������
		
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
		
		double value1;// ����������
		
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
		
		double value1;// ����������
		
		value1 = Math.floor(value * Math.pow(10, _bit + 1))
				- Math.floor(value * Math.pow(10, _bit)) * 10.0;
		
		if (value1 / 10.0 < scale) {
			return Math.floor(value * Math.pow(10, _bit)) / Math.pow(10, _bit);
		} else {
			return (Math.floor(value * Math.pow(10, _bit)) + 1)
					/ Math.pow(10, _bit);
		}
	}
	
//	�������룬����7λС������
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
