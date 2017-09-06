/*
 * �������� 2007-11-20
 */
package com.zhiren.common;


/**
 * @author wl ���鳬���ж�
 */
public class ArrayUtil {

	public ArrayUtil() {

		super();
	}

	/**
	 * ����һ��������
	 * 
	 * @param Array����
	 *            Ϊ�������ȡֵ��Դ
	 * @param begin
	 *            [0...Array.length-1]
	 * @param end
	 *            [1...<Array.length]
	 * 
	 * @return ������
	 */
	public static double[] NewDbArray(double[] Array, int begin, int end) {
		int b = begin;
		int e = end;
		if (begin > Array.length || end > Array.length) {
			return null;
		}
		if (begin > end) {
			b = end;
			e = begin;
		}
		int length = e - b;
		double[] array = new double[length];
		for (int i = b, j = 0; i < e; i++, j++) {
			array[j] = Array[i];
		}
		return array;
	}

	/**
	 * ð������
	 * 
	 * @param Array����
	 *            ��֪����
	 * 
	 */
	public static void DbBubbleSort(double[] array) {
		if (array == null || array.length == 0) {
			return;
		}
		for (int i = 0; i < array.length; i++) {
			for (int j = array.length - 1; j > i; j--) {
				if (array[j] < array[j - 1]) {
					double tmp = array[j];
					array[j] = array[j - 1];
					array[j - 1] = tmp;
				}
			}
		}
	}

	/**
	 * �õ����������Ĳ�ֵ
	 * 
	 * @param Array����
	 *            ��֪����
	 * 
	 */
	public static double getDbMaxRemainder(double[] array) {
		double Remainder = 0.0;
		for (int i = 0; i < array.length - 1; i++) {
			for (int j = i + 1; j < array.length; j++) {
				if (Math.abs(array[i] - array[j]) > Remainder) {
					Remainder = Math.abs(array[i] - array[j]);
				}
			}
		}
		return Remainder;
	}

	/**
	 * �õ������ƽ��ֵ
	 * 
	 * @param Array����
	 *            ��֪����
	 * 
	 */
	public static double getDbAvg(double[] array) {
		double sum = 0.0;
		double avg = 0.0;
		for (int i = 0; i < array.length; i++) {
			sum = CustomMaths.add(sum, array[i]);
		}
		avg = CustomMaths.Round_New(sum / array.length, 2);
		return avg;
	}

	public static int getDbAvgDeviate(double[] array, int i, int j) {
		double avg = getDbAvg(array);
		if (Math.abs(array[i] - avg) > Math.abs(array[j] - avg)) {
			return i;
		} else
			return j;
	}
}
