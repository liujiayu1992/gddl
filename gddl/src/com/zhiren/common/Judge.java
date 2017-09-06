package com.zhiren.common;

/**
 * @author wl ���鳬���ж�
 * @since 2007-11-21
 */
public class Judge {

	public static final int T_TYPE_Qbad = 0;
	public static final int T_TYPE_Stad = 1;
	public static final int T_TYPE_Mt = 2;
	public static final int T_TYPE_Mad = 3;
	public static final int T_TYPE_Vad = 4;
	public static final int T_TYPE_Aad = 5;
	public static final int T_TYPE_Feih = 6;
	
	public Judge() {
		super();
	}
	/**
	 * �Ƿ񳬲���ж�
	 * 
	 * @param T_type
	 * 			ȡ��T�����͹��������ö������
	 * @param Array����
	 * 			������Ҫ�жϵ�����
	 * 
	 * @return ���кϸ����ݵ�һά����
	 */
	
	public double[] getJudgeData(int T_type,double[] OriArray) {
		if(OriArray.length < 2) {
			return null;
		}
		for(int i = 2 ;i <= OriArray.length; i++) {
			double[] array = ArrayUtil.NewDbArray(OriArray,0,i);
			double T = getT(T_type,array); 
			double MaxRemainder = ArrayUtil.getDbMaxRemainder(array);
			if(MaxRemainder <= T) {
				return array;
			}
		}
		double [] array = (double [])OriArray.clone();
		ArrayUtil.DbBubbleSort(array);
		int i=0;
		while(array.length > 2) {
			if(ArrayUtil.getDbAvgDeviate(array,0,array.length-1)==0) {
				array = ArrayUtil.NewDbArray(array,1,array.length);
			}else {
				array = ArrayUtil.NewDbArray(array,0,array.length-1);
			}
			double T = getT(T_type,array); 
			double MaxRemainder = ArrayUtil.getDbMaxRemainder(array);
			if(MaxRemainder <= T) {
				return array;
			}
			if(i++ == 10) {
				return null;
			}
		}
		return null;
	}
	/**
	 * @return T
	 */	
	public double getT(int T_type,double[] array) {
		double T = 0.0;
		switch (T_type) {
			case T_TYPE_Qbad: T = getQbadT(array);break;
			case T_TYPE_Stad: T = getStadT(array);break;
			case T_TYPE_Mt: T = getMtT(array);break;
			case T_TYPE_Mad: T = getMadT(array);break;
			case T_TYPE_Vad: T = getVadT(array);break;
			case T_TYPE_Aad: T = getAadT(array);break;
			case T_TYPE_Feih: T = getFeihT(array);break;
			default : T =0.0;
		}
		return T;
	}
	/**
	 * @return Tx
	 */	
	public double getTx(double[] array) {
		double Tx =0.0;
		if(array.length ==2) {
			Tx = 1.0;
		}else
		if(array.length == 3) {
			Tx = 1.2;
		}else
			if(array.length == 4)
				Tx = 1.3;
		return Tx;
	}
	/**
	 * @return ������T
	 */
	public double getQbadT(double[] array) {
		double T = 120;
		double Tx = getTx(array);
		return CustomMaths.mul(T, Tx);
	}
	/**
	 * @return ���T
	 */	
	public double getStadT(double[] array) {
		double T =0.0;
		double Tx = getTx(array);
		double dblavg = ArrayUtil.getDbAvg(array);
		if(dblavg < 1.00) {
			T = 0.05;
		}else
			if(dblavg >= 1 && dblavg <= 4) {
				T = 0.1;
			}else
				T = 0.2;
		return CustomMaths.mul(T, Tx);
	}
	/**
	 * @return ȫˮ�ֵ�T
	 */	
	public double getMtT(double[] array) {
		double T =0.0;
		double Tx = getTx(array);
		double dblavg = ArrayUtil.getDbAvg(array);
		if(dblavg < 10.00) {
			T = 0.4;
		}else
			T = 0.5;
		return CustomMaths.mul(T, Tx);
	}
	
	/**
	 * @return ��ˮ��T
	 */	
	public double getMadT(double[] array) {
		double T =0.0;
		double Tx = getTx(array);
		double dblavg = ArrayUtil.getDbAvg(array);
		if(dblavg < 5) {
			T = 0.2;
		}else
			if(dblavg > 10) {
				T=0.4;
			}else
				T = 0.3;
		return CustomMaths.mul(T, Tx);
	}
	/**
	 * @return �ӷ��ֵ�T
	 */	
	public double getVadT(double[] array) {
		double T =0.0;
		double Tx = getTx(array);
		double dblavg = ArrayUtil.getDbAvg(array);
		if(dblavg < 20) {
			T = 0.3;
		}else
			if(dblavg > 40) {
				T=0.8;
			}else
				T = 0.5;
		return CustomMaths.mul(T, Tx);
	}
	/**
	 * @return �ҷֵ�T
	 */	
	public double getAadT(double[] array) {
		double T =0.0;
		double Tx = getTx(array);
		double dblavg = ArrayUtil.getDbAvg(array);
		if(dblavg < 15) {
			T = 0.2;
		}else
			if(dblavg > 30) {
				T=0.5;
			}else
				T = 0.3;
		return CustomMaths.mul(T, Tx);
	}
	/**
	 * @return �ɻҵ�T
	 */	
	public double getFeihT(double[] array) {
		double T =0.0;
		double Tx = getTx(array);
		double dblavg = ArrayUtil.getDbAvg(array);
		if(dblavg < 5) {
			T = 0.3;
		}else
			T=0.5;
		return CustomMaths.mul(T, Tx);
	}
}
