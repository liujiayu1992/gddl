package com.zhiren.common.Jiesgs;

/**
 * Created by liuzhiyu on 2017/6/6.
 */

import bsh.EvalError;
import bsh.Interpreter;
import com.zhiren.common.CustomMaths;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class Jiesgs extends Interpreter {

    double ����۸� = 0.0;
    double ������ = 0;
    double ָ���۵��� = 0;
    double ӯ�� = 0;
    String ��ͬ��׼ = "";
    double �������� = 0;
    double ��������_������ = 0;

    double ӯ��_�������� = 0;
    double ӯ��_Qnetar = 0;
    double ӯ��_Std = 0;
    double ӯ��_Ad = 0;
    double ӯ��_Vdaf = 0;
    double ӯ��_Mt = 0;
    double ӯ��_Qgrad = 0;
    double ӯ��_Qbad = 0;
    double ӯ��_Had = 0;
    double ӯ��_Stad = 0;
    double ӯ��_Mad = 0;
    double ӯ��_Aar = 0;
    double ӯ��_Aad = 0;
    double ӯ��_Vad = 0;
    double ӯ��_T2 = 0;
    double ӯ��_�˾� = 0;
    double ӯ��_Star = 0;

    double �۵���_�������� = 0;
    double �۵���_Qnetar = 0;
    double �۵���_Std = 0;
    double �۵���_Ad = 0;
    double �۵���_Vdaf = 0;
    double �۵���_Mt = 0;
    double �۵���_Qgrad = 0;
    double �۵���_Qbad = 0;
    double �۵���_Had = 0;
    double �۵���_Stad = 0;
    double �۵���_Mad = 0;
    double �۵���_Aar = 0;
    double �۵���_Aad = 0;
    double �۵���_Vad = 0;
    double �۵���_T2 = 0;
    double �۵���_�˾� = 0;
    double �۵���_Star = 0;

    // �����۽�����������_begin

    double ������_�������� = 0;
    double ������_Qnetar = 0;
    double ������_Std = 0;
    double ������_Ad = 0;
    double ������_Vdaf = 0;
    double ������_Mt = 0;
    double ������_Qgrad = 0;
    double ������_Qbad = 0;
    double ������_Had = 0;
    double ������_Stad = 0;
    double ������_Mad = 0;
    double ������_Aar = 0;
    double ������_Aad = 0;
    double ������_Vad = 0;
    double ������_T2 = 0;
    double ������_�˾� = 0;
    double ������_Star = 0;

    String ��������λ_�������� = "";
    String ��������λ_Qnetar = "";
    String ��������λ_Std = "";
    String ��������λ_Ad = "";
    String ��������λ_Vdaf = "";
    String ��������λ_Mt = "";
    String ��������λ_Qgrad = "";
    String ��������λ_Qbad = "";
    String ��������λ_Had = "";
    String ��������λ_Stad = "";
    String ��������λ_Mad = "";
    String ��������λ_Aar = "";
    String ��������λ_Aad = "";
    String ��������λ_Vad = "";
    String ��������λ_T2 = "";
    String ��������λ_�˾� = "";
    String ��������λ_Star = "";

    // �����۽�����������_end

    String ��ͬ��׼_�������� = "";
    String ��ͬ��׼_Qnetar = "";
    String ��ͬ��׼_Std = "";
    String ��ͬ��׼_Ad = "";
    String ��ͬ��׼_Vdaf = "";
    String ��ͬ��׼_Mt = "";
    String ��ͬ��׼_Qgrad = "";
    String ��ͬ��׼_Qbad = "";
    String ��ͬ��׼_Had = "";
    String ��ͬ��׼_Stad = "";
    String ��ͬ��׼_Mad = "";
    String ��ͬ��׼_Aar = "";
    String ��ͬ��׼_Aad = "";
    String ��ͬ��׼_Vad = "";
    String ��ͬ��׼_T2 = "";
    String ��ͬ��׼_�˾� = "";
    String ��ͬ��׼_Star = "";

    double Dblvalue = 0;    //��ʱ����
    private int ú�˰���۱���С��λ;
    private String Zengkjdw;
    private String Qnetar�������۹�ʽ;
    private String Qnetar_����;

    private String Qnetar��������;
    private String T2��������;
    private String Star��������;
    private String Ad��������;
    private String Aad��������;
    private String Vdaf��������;
    private String Mt��������;
    private String Qgrad��������;
    private String Std��������;
    private String Aar��������;
    private String Mad��������;
    private String Stad��������;
    private String Had��������;
    private String Vad��������;
    private String �˾��������;



    private String T2���ۿ������λ;
    private String Star���ۿ������λ;
    private String Ad���ۿ������λ;
    private String Aad���ۿ������λ;
    private String Vdaf���ۿ������λ;
    private String Mt���ۿ������λ;
    private String Qgrad���ۿ������λ;
    private String Std���ۿ������λ;
    private String Aar���ۿ������λ;
    private String Mad���ۿ������λ;
    private String Stad���ۿ������λ;
    private String Had���ۿ������λ;
    private String Vad���ۿ������λ;
    private String �˾����ۿ������λ;


    private String �û��Զ���Ŀ¼�۹�ʽ;

    private String ������ʽ;
    private String �Ƽ۷�ʽ;
    private String �۸�λ;
    private double Qnetar����;
    private double  Qnetar����;
    private double �������ۼ�;
    private double  Qnetar��������;
    private String  Qnetar�����۵�λ;
    private String Qnetar���ۿ�����;
    private double Qnetar���ۿ����;
    private String Qnetar���ۿ������λ;
    private double Qnetar��׼���ۼ�;
    private String QnetarС������;
    private double Qnetar�۸�����;
    private String Qnetar�۸����۹�ʽ;
    private String Qnetar�۸��۵�λ;
    private String Qnetar_����;

    private double ��ֵ����_Qnetar ;
    private double �ӷ��ݱȼ�_Vdaf ;
    private double ��ֱȼ�_Stad ;
    private double �ҷֱȼ�_Aar ;
    private double Ʒ�ֱȼ� ;
    private double �����ԼӼ� ;
    private double �Ӽ�;
    private String Qnetarǿ�Ƽ���;

    private String T2���ۿ�����;
    private double T2;
    private double T2���ۿ����;
    private String T2�����۵�λ;
    private double T2��׼���ۼ�;
    private String T2С������;
    private String T2�۸��۵�λ;
    private double T2����;
    private double T2����;
    private double T2��������;
    private String T2�������۹�ʽ;
    private double T2�۸�����;
    private String T2�۸����۹�ʽ;
    private double T2_����;
    private double T2_����;
    private String T2ǿ�Ƽ���;
    private String Star���ۿ�����;
    private double Star;
    private double Star���ۿ����;
    private String Star�����۵�λ;
    private double Star��׼���ۼ�;
    private String StarС������;
    private String Star�۸��۵�λ;
    private double Star����;
    private double Star����;
    private double Star��������;
    private String Star�������۹�ʽ;
    private double Star�۸�����;
    private String Star�۸����۹�ʽ;
    private double Star_����;
    private double Star_����;
    private String Starǿ�Ƽ���;
    private String Ad���ۿ�����;
    private double Ad;
    private double Ad���ۿ����;
    private String Ad�����۵�λ;
    private double Ad��׼���ۼ�;
    private String AdС������;
    private String Ad�۸��۵�λ;
    private double Ad����;
    private double Ad����;
    private double Ad��������;
    private String Ad�������۹�ʽ;
    private double Ad�۸�����;
    private String Ad�۸����۹�ʽ;
    private double Ad_����;
    private double Ad_����;
    private String Adǿ�Ƽ���;
    private String Aad���ۿ�����;
    private double Aad;
    private double Aad���ۿ����;
    private String Aad�����۵�λ;
    private double Aad��׼���ۼ�;
    private String AadС������;
    private String Aad�۸��۵�λ;
    private double Aad����;
    private double Aad����;
    private double Aad��������;
    private String Aad�������۹�ʽ;
    private double Aad�۸�����;
    private String Aad�۸����۹�ʽ;
    private double Aad_����;
    private double Aad_����;
    private String Aadǿ�Ƽ���;
    private String Vdaf���ۿ�����;
    private double Vdaf;
    private double Vdaf���ۿ����;
    private String Vdaf�����۵�λ;
    private double Vdaf��׼���ۼ�;
    private String VdafС������;
    private String Vdaf�۸��۵�λ;
    private double Vdaf����;
    private double Vdaf����;
    private double Vdaf��������;
    private String Vdaf�������۹�ʽ;
    private double Vdaf�۸�����;
    private String Vdaf�۸����۹�ʽ;
    private double Vdaf_����;
    private double Vdaf_����;
    private String Vdafǿ�Ƽ���;
    private String Mt���ۿ�����;
    private double Mt;
    private double Mt���ۿ����;
    private String Mt�����۵�λ;
    private double Mt��׼���ۼ�;
    private String MtС������;
    private String Mt�۸��۵�λ;
    private double Mt����;
    private double Mt����;
    private double Mt��������;
    private String Mt�������۹�ʽ;
    private double Mt�۸�����;
    private String Mt�۸����۹�ʽ;
    private double Mt_����;
    private double Mt_����;
    private String Mtǿ�Ƽ���;
    private String Qgrad���ۿ�����;
    private double Qgrad;
    private double Qgrad���ۿ����;
    private String Qgrad�����۵�λ;
    private double Qgrad��׼���ۼ�;
    private String QgradС������;
    private String Qgrad�۸��۵�λ;
    private double Qgrad����;
    private double Qgrad����;
    private double Qgrad��������;
    private String Qgrad�������۹�ʽ;
    private double Qgrad�۸�����;
    private String Qgrad�۸����۹�ʽ;
    private double Qgrad_����;
    private double Qgrad_����;
    private String Qgradǿ�Ƽ���;
    private String Std���ۿ�����;
    private double Std;
    private double Std���ۿ����;
    private String Std�����۵�λ;
    private double Std��׼���ۼ�;
    private String StdС������;
    private String Std�۸��۵�λ;
    private double Std����;
    private double Std����;
    private double Std��������;
    private String Std�������۹�ʽ;
    private double Std�۸�����;
    private String Std�۸����۹�ʽ;
    private double Std_����;
    private double Std_����;
    private String Stdǿ�Ƽ���;
    private String Aar���ۿ�����;
    private double Aar;
    private double Aar���ۿ����;
    private String Aar�����۵�λ;
    private double Aar��׼���ۼ�;
    private String AarС������;
    private String Aar�۸��۵�λ;
    private double Aar����;
    private double Aar����;
    private double Aar��������;
    private String Aar�������۹�ʽ;
    private double Aar�۸�����;
    private String Aar�۸����۹�ʽ;
    private double Aar_����;
    private double Aar_����;
    private String Aarǿ�Ƽ���;
    private String Mad���ۿ�����;
    private double Mad;
    private double Mad���ۿ����;
    private String Mad�����۵�λ;
    private double Mad��׼���ۼ�;
    private String MadС������;
    private String Mad�۸��۵�λ;
    private double Mad����;
    private double Mad����;
    private double Mad��������;
    private String Mad�������۹�ʽ;
    private double Mad�۸�����;
    private String Mad�۸����۹�ʽ;
    private double Mad_����;
    private double Mad_����;
    private String Madǿ�Ƽ���;

    private String Stad���ۿ�����;
    private double Stad;
    private double Stad���ۿ����;
    private String Stad�����۵�λ;
    private double Stad��׼���ۼ�;
    private String StadС������;
    private String Stad�۸��۵�λ;
    private double Stad����;
    private double Stad����;
    private double Stad��������;
    private String Stad�������۹�ʽ;
    private double Stad�۸�����;
    private String Stad�۸����۹�ʽ;
    private double Stad_����;
    private double Stad_����;
    private String Stadǿ�Ƽ���;

    private String Had���ۿ�����;
    private double Had;
    private double Had���ۿ����;
    private String Had�����۵�λ;
    private double Had��׼���ۼ�;
    private String HadС������;
    private String Had�۸��۵�λ;
    private double Had����;
    private double Had����;
    private double Had��������;
    private String Had�������۹�ʽ;
    private double Had�۸�����;
    private String Had�۸����۹�ʽ;
    private double Had_����;
    private double Had_����;
    private String Hadǿ�Ƽ���;
    private String Qbad���ۿ�����;
    private double Qbad;
    private double Qbad���ۿ����;
    private String Qbad�����۵�λ;
    private double Qbad��׼���ۼ�;
    private String QbadС������;
    private String Qbad�۸��۵�λ;
    private double Qbad����;
    private double Qbad����;
    private double Qbad��������;
    private String Qbad�������۹�ʽ;
    private double Qbad�۸�����;
    private String Qbad�۸����۹�ʽ;
    private double Qbad_����;
    private double Qbad_����;
    private String Qbadǿ�Ƽ���;

    private String Vad���ۿ�����;
    private double Vad;
    private double Vad���ۿ����;
    private String Vad�����۵�λ;
    private double Vad��׼���ۼ�;
    private String VadС������;
    private String Vad�۸��۵�λ;
    private double Vad����;
    private double Vad����;
    private double Vad��������;
    private String Vad�������۹�ʽ;
    private double Vad�۸�����;
    private String Vad�۸����۹�ʽ;
    private double Vad_����;
    private double Vad_����;
    private String Vadǿ�Ƽ���;

    private String �˾����ۿ�����;
    private double �˾�;
    private double �˾����ۿ����;
    private String �˾������۵�λ;
    private double �˾��׼���ۼ�;
    private String �˾�С������;
    private String �˾�۸��۵�λ;
    private double �˾�����;
    private double �˾�����;
    private double �˾���������;
    private String �˾��������۹�ʽ;
    private double �˾�۸�����;
    private String �˾�۸����۹�ʽ;
    private double �˾�_����;
    private double �˾�_����;
    private String �˾�ǿ�Ƽ���;

    private String �����������ۿ�����;
    private double �����������ۿ����;
    private String �������������۵�λ;
    private double ����������׼���ۼ�;
    private String ��������С������;
    private String ���������۸��۵�λ;
    private double ������������;
    private double ������������;
    private double ����������������;
    private String ���������������۹�ʽ;
    private double ���������۸�����;
    private String ���������۸����۹�ʽ;
    private double ��������_����;
    private double ��������_����;
    private double Qgrad�׽�ǧ��;
    private double Qbad�׽�ǧ��;
    private String ��������������λ;
    private double ����������;
    private double �����������;
    private String �����������ۿ������λ;
    private String ú�˰����ȡ����ʽ;
    private double ���ú��;
    private String ��ͬ�۸�ʽ;
    void Initialize() {
        //��ʼ������
        ָ���۵��� = 0;
        ӯ�� = 0;
        ��ͬ��׼ = "";
    }

    double Mjkg_to_kcalkg(double value, int xiaosw) {
        //		�׽�/ǧ��ת��Ϊǧ��/ǧ��
        Dblvalue = 0;
        if (value < 100) {

            Dblvalue = Round_new(value * 1000 / 4.1816, xiaosw);
        } else {

            Dblvalue = value;
        }
        return Dblvalue;
    }

    double Mjkg_to_kcalkg(double value, int xiaosw, String xiaosclfs) {
        //		�׽�/ǧ��ת��Ϊǧ��/ǧ��,��С���������
        double Dblvalue = 0;
        if (Math.abs(value) < 100) {

            if (xiaosclfs.equals("��������")) {
                //				��������
                Dblvalue = CustomMaths.Round_new(value * 1000 / 4.1816, xiaosw);
            } else if (xiaosclfs.equals("��ȥ")) {
                //				��ȥ
                Dblvalue = Math.floor(Math.abs(value) * 1000 / 4.1816);
            } else if (xiaosclfs.equals("��λ")) {
                //				��λ
                Dblvalue = Math.floor(Math.abs(value) * 1000 / 4.1816) + 1;
            }

        } else {

            Dblvalue = value;
        }
        return Dblvalue;
    }

    double Kcalkg_to_mjkg(double value, int xiaosw) {
        //		ǧ��/ǧ��ת��Ϊ�׽�
        Dblvalue = 0;
        if (value > 100) {

            Dblvalue = Round_new(value * 4.1816 / 1000, xiaosw);
        } else {

            Dblvalue = value;
        }
        return Dblvalue;
    }

    private String StrValue;
    public void set(String key, Object value) {
        Class c = this.getClass();
//        Instance instance = new Instance();
        Field field = null;//nameΪ��Instance�е�private����
        try {
            field = c.getDeclaredField(key);
            field.setAccessible(true);//�ؼ��������ɷ���˽�б�����
            Class<?>  typeClass = field.getType();
            Constructor<?>  con = typeClass.getConstructor(typeClass);
            if(value==null)value="";
            Object v = con.newInstance(value);//Ҫ����ֵ
            field.set(this, v);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void set(String key, String value) {
        Class c = this.getClass();
//        Instance instance = new Instance();
        Field field = null;//nameΪ��Instance�е�private����
        try {
            field = c.getDeclaredField(key);
            field.setAccessible(true);//�ؼ��������ɷ���˽�б�����
//            Class<?>  typeClass = field.getType();
//            Constructor<?>  con = typeClass.getConstructor(typeClass);
//            if(value==null)value="";
//            Object v = con.newInstance(value);//Ҫ����ֵ
            field.set(this, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void set(String key, int value) {
        Class c = this.getClass();
//        Instance instance = new Instance();
        Field field = null;//nameΪ��Instance�е�private����
        try {
            field = c.getDeclaredField(key);
            field.setAccessible(true);//�ؼ��������ɷ���˽�б�����
//            Class<?>  typeClass = field.getType();
//            Constructor<?>  con = typeClass.getConstructor(typeClass);
//            Object v = con.newInstance(value);//Ҫ����ֵ
            field.set(this, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set(String key, double value) {
        Class c = this.getClass();
//        Instance instance = new Instance();
        Field field = null;//nameΪ��Instance�е�private����
        try {
            field = c.getDeclaredField(key);
            field.setAccessible(true);//�ؼ��������ɷ���˽�б�����
//            Class<?>  typeClass = field.getType();
//            Constructor<?>  con = typeClass.getConstructor(typeClass);
//            Object v = con.newInstance(value);//Ҫ����ֵ
            field.set(this, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Object get(String key) {
        Class c = this.getClass();
//        Instance instance = new Instance();
        Field field = null;//nameΪ��Instance�е�private����
        try {
            field = c.getDeclaredField(key);
            return field.get(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private String StrQz;
    private int i=0;
    double Quzcz(String Quzfs, double value, int xiaosw) {
        //			����ú�˰���۱���С��λ������
        //			������ȡ����ʽ��Ҫ�����ֵ��������С��λ
        Dblvalue = 0;
        StrValue = "";
        StrQz = "1";    //Ȩ��
        StrValue = String.valueOf(value);
        if (StrValue.indexOf('.') == -1) {

            return value;
        } else {

            StrValue = StrValue.substring(StrValue.indexOf('.'));
            if (Double.parseDouble(StrValue) == 0) {

                return value;
            }
        }

        i = 0;

        for (i=0; i < xiaosw; i++) {

            StrQz = StrQz + "0";
        }

        switch (Quzfs) {        //�жϴ�������

            case "":
                Dblvalue = value;
                break;

            case "��λ":
                Dblvalue = Math.floor(CustomMaths.mul(Math.abs(value), Double.parseDouble(StrQz))) + 1;
                Dblvalue = CustomMaths.div(Dblvalue, Double.parseDouble(StrQz));
                if (value < 0) {

                    Dblvalue = 0 - Dblvalue;
                }
                break;

            case "��ȥ":
                Dblvalue = Math.floor(CustomMaths.mul(Math.abs(value), Double.parseDouble(StrQz)));
                Dblvalue = CustomMaths.div(Dblvalue, Double.parseDouble(StrQz));
                if (value < 0) {

                    Dblvalue = 0 - Dblvalue;
                }
                break;

            case "��������":
                Dblvalue = Round_new(Math.abs(value), xiaosw);
                if (value < 0) {

                    Dblvalue = 0 - Dblvalue;
                }
                break;
        }

        return Dblvalue;

    }

    double Xiaoscl(double value, String chultj) {
        //		С����������������롢��λ����ȥ�Ĳ���
        Dblvalue = 0;
        StrValue = "";
        StrValue = String.valueOf(value);
        if (StrValue.indexOf('.') == -1) {

            return value;
        } else {

            StrValue = StrValue.substring(StrValue.indexOf('.'));
            if (Double.parseDouble(StrValue) == 0) {

                return value;
            }
        }

        switch (chultj) {        //�жϴ�������

            case "":
                Dblvalue = value;
                break;

            case "��λ":
                Dblvalue = Math.floor(Math.abs(value)) + 1;
                if (value < 0) {

                    Dblvalue = 0 - Dblvalue;
                }
                break;

            case "��ȥ":
                Dblvalue = Math.floor(Math.abs(value));
                if (value < 0) {

                    Dblvalue = 0 - Dblvalue;
                }
                break;

            case "��������":
                Dblvalue = Round_new(Math.abs(value), 0);
                if (value < 0) {

                    Dblvalue = 0 - Dblvalue;
                }
                break;

            case "��������һλ":
                Dblvalue = Round_new(Math.abs(value), 1);
                if (value < 0) {

                    Dblvalue = 0 - Dblvalue;
                }
                break;

            case "����������λ":
                Dblvalue = Round_new(Math.abs(value), 2);
                if (value < 0) {

                    Dblvalue = 0 - Dblvalue;
                }
                break;

            case "����������λ":
                Dblvalue = Round_new(Math.abs(value), 3);
                if (value < 0) {

                    Dblvalue = 0 - Dblvalue;
                }
                break;

            case "����������λ":
                Dblvalue = Round_new(Math.abs(value), 4);
                if (value < 0) {

                    Dblvalue = 0 - Dblvalue;
                }
                break;
        }

        return Dblvalue;
    }


    double Round_new(double value, int _bit) {
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

    void setEvaluate(String Zhibyk, double Yingk, String Hetbz, double Zhedj) {
        //������ָ��ӯ��(��ֵ��)��ӯ������ͬ��׼��ָ���۵���
        //���в����丳ֵ

        switch (Zhibyk) {

            case "ӯ��_Qnetar":

                ӯ��_Qnetar = Yingk;
                �۵���_Qnetar = Zhedj;
                ��ͬ��׼_Qnetar = Hetbz;
                break;

            case "ӯ��_Std":

                ӯ��_Std = Yingk;
                �۵���_Std = Zhedj;
                ��ͬ��׼_Std = Hetbz;
                break;

            case "ӯ��_Ad":

                ӯ��_Ad = Yingk;
                �۵���_Ad = Zhedj;
                ��ͬ��׼_Ad = Hetbz;
                break;

            case "ӯ��_Vdaf":

                ӯ��_Vdaf = Yingk;
                �۵���_Vdaf = Zhedj;
                ��ͬ��׼_Vdaf = Hetbz;
                break;

            case "ӯ��_Mt":

                ӯ��_Mt = Yingk;
                �۵���_Mt = Zhedj;
                ��ͬ��׼_Mt = Hetbz;
                break;

            case "ӯ��_Qgrad":

                ӯ��_Qgrad = Yingk;
                �۵���_Qgrad = Zhedj;
                ��ͬ��׼_Qgrad = Hetbz;
                break;

            case "ӯ��_Qbad":

                ӯ��_Qbad = Yingk;
                �۵���_Qbad = Zhedj;
                ��ͬ��׼_Qbad = Hetbz;
                break;

            case "ӯ��_Had":

                ӯ��_Had = Yingk;
                �۵���_Had = Zhedj;
                ��ͬ��׼_Had = Hetbz;
                break;

            case "ӯ��_Stad":

                ӯ��_Stad = Yingk;
                �۵���_Stad = Zhedj;
                ��ͬ��׼_Stad = Hetbz;
                break;

            case "ӯ��_Mad":

                ӯ��_Mad = Yingk;
                �۵���_Mad = Zhedj;
                ��ͬ��׼_Mad = Hetbz;
                break;

            case "ӯ��_Aar":

                ӯ��_Aar = Yingk;
                �۵���_Aar = Zhedj;
                ��ͬ��׼_Aar = Hetbz;
                break;

            case "ӯ��_Aad":

                ӯ��_Aad = Yingk;
                �۵���_Aad = Zhedj;
                ��ͬ��׼_Aad = Hetbz;
                break;

            case "ӯ��_Vad":

                ӯ��_Vad = Yingk;
                �۵���_Vad = Zhedj;
                ��ͬ��׼_Vad = Hetbz;
                break;

            case "ӯ��_T2":

                ӯ��_T2 = Yingk;
                �۵���_T2 = Zhedj;
                ��ͬ��׼_T2 = Hetbz;
                break;

            case "ӯ��_��������":

                ӯ��_�������� = Yingk;
                �۵���_�������� = Zhedj;
                ��ͬ��׼_�������� = Hetbz;
                break;

            case "ӯ��_�˾�":

                ӯ��_�˾� = Yingk;
                �۵���_�˾� = Zhedj;
                ��ͬ��׼_�˾� = Hetbz;
                break;

            case "ӯ��_Star":

                ӯ��_Star = Yingk;
                �۵���_Star = Zhedj;
                ��ͬ��׼_Star = Hetbz;
                break;
        }
    }

    //Ϊ�������ۿֵ
    void setEvaluate_Jiessl(String Zhibyk, String Hetbz, double Yingk, double Zhedj, String Zhejdw) {
        //������ָ��ӯ����ָ���۵��ۣ��֣�����ͬ��׼���ۼ۵�λ
        //���в����丳ֵ

        switch (Zhibyk) {

            case "ӯ��_Qnetar":

                ӯ��_Qnetar = Yingk;
                ������_Qnetar = Zhedj;
                ��������λ_Qnetar = Zhejdw;
                ��ͬ��׼_Qnetar = Hetbz;
                break;

            case "ӯ��_Std":

                ӯ��_Std = Yingk;
                ������_Std = Zhedj;
                ��������λ_Std = Zhejdw;
                ��ͬ��׼_Std = Hetbz;
                break;

            case "ӯ��_Ad":

                ӯ��_Ad = Yingk;
                ������_Ad = Zhedj;
                ��������λ_Ad = Zhejdw;
                ��ͬ��׼_Ad = Hetbz;
                break;

            case "ӯ��_Vdaf":

                ӯ��_Vdaf = Yingk;
                ������_Vdaf = Zhedj;
                ��������λ_Vdaf = Zhejdw;
                ��ͬ��׼_Vdaf = Hetbz;
                break;

            case "ӯ��_Mt":

                ӯ��_Mt = Yingk;
                ������_Mt = Zhedj;
                ��������λ_Mt = Zhejdw;
                ��ͬ��׼_Mt = Hetbz;
                break;

            case "ӯ��_Qgrad":

                ӯ��_Qgrad = Yingk;
                ������_Qgrad = Zhedj;
                ��������λ_Qgrad = Zhejdw;
                ��ͬ��׼_Qgrad = Hetbz;
                break;

            case "ӯ��_Qbad":

                ӯ��_Qbad = Yingk;
                ������_Qbad = Zhedj;
                ��������λ_Qbad = Zhejdw;
                ��ͬ��׼_Qbad = Hetbz;
                break;

            case "ӯ��_Had":

                ӯ��_Had = Yingk;
                ������_Had = Zhedj;
                ��������λ_Had = Zhejdw;
                ��ͬ��׼_Had = Hetbz;
                break;

            case "ӯ��_Stad":

                ӯ��_Stad = Yingk;
                ������_Stad = Zhedj;
                ��������λ_Stad = Zhejdw;
                ��ͬ��׼_Stad = Hetbz;
                break;

            case "ӯ��_Mad":

                ӯ��_Mad = Yingk;
                ������_Mad = Zhedj;
                ��������λ_Mad = Zhejdw;
                ��ͬ��׼_Mad = Hetbz;
                break;

            case "ӯ��_Aar":

                ӯ��_Aar = Yingk;
                ������_Aar = Zhedj;
                ��������λ_Aar = Zhejdw;
                ��ͬ��׼_Aar = Hetbz;
                break;

            case "ӯ��_Aad":

                ӯ��_Aad = Yingk;
                ������_Aad = Zhedj;
                ��������λ_Aad = Zhejdw;
                ��ͬ��׼_Aad = Hetbz;
                break;

            case "ӯ��_Vad":

                ӯ��_Vad = Yingk;
                ������_Vad = Zhedj;
                ��������λ_Vad = Zhejdw;
                ��ͬ��׼_Vad = Hetbz;
                break;

            case "ӯ��_T2":

                ӯ��_T2 = Yingk;
                ������_T2 = Zhedj;
                ��������λ_T2 = Zhejdw;
                ��ͬ��׼_T2 = Hetbz;
                break;

            case "ӯ��_��������":

                ӯ��_�������� = Yingk;
                ������_�������� = Zhedj;
                ��������λ_�������� = Zhejdw;
                ��ͬ��׼_�������� = Hetbz;
                break;

            case "ӯ��_�˾�":

                ӯ��_�˾� = Yingk;
                ������_�˾� = Zhedj;
                ��������λ_�˾� = Zhejdw;
                ��ͬ��׼_�˾� = Hetbz;
                break;

            case "ӯ��_Star":

                ӯ��_Star = Yingk;
                ������_Star = Zhedj;
                ��������λ_Star = Zhejdw;
                ��ͬ��׼_Star = Hetbz;
                break;
        }
    }
    private int ú�����ۿ��С��λ;
    private double ��ͬ�۸�;
    //���ݷ������������۸�(�۸�λ:"Ԫ/ǧ��")
    void Jiesjgjs_farl_yk(String Jildw, String Zengkkdw, String Zengkktj, double Zhibvalue, double Hetsx, double Hetxx, String Zhibyk,
                          double Zengkkjs, String Zengkkjsdw, double Zengkkdj, String Zengkkdjgs, String Zengkklx, double Zengkkjzzkj, String Zengkkxscl) {

        //����۸����
        //������������λ�����ۿλ�����ۿ�������ָ��ֵ����ͬ���ޣ���ͬ���ޣ�ָ��ӯ��(��ֵ��)�����ۿ���������ۿ��,���ۿ����ͣ��������۸���

        if (Zengkktj.equals("���ڵ���") || Zengkktj.equals("����")) {

            ӯ�� = Round_new((Zhibvalue - Hetxx), 2);
            ��ͬ��׼ = String.valueOf(Hetxx);
        } else if (Zengkktj.equals("С�ڵ���") || Zengkktj.equals("С��")) {

            ӯ�� = Round_new((Zhibvalue - Hetsx), 2);
            ��ͬ��׼ = String.valueOf(Hetsx);
        } else if (Zengkktj.equals("����")) {

            if (Zengkklx.equals("����")) {

                ӯ�� = Round_new((Zhibvalue - Hetxx), 2);
            } else if (Zengkklx.equals("�۸�")) {

                ӯ�� = Round_new((Zhibvalue - Hetsx), 2);
            }
            ��ͬ��׼ = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
        }

        if (!Zengkkdjgs.equals("")) {
            //���ڹ�ʽ
            //������ۿ���ڹ�ʽ����ô�ù�ʽ�����ֵ���¸������ۿ�ۡ����и�ֵ
                Zengkkdj = (double)eval(Zengkkdjgs);
        }


        if (Zengkkdw.equals("Ԫ/ǧ��")) {

            if (Zengkkdj > 0) {

                if (Jildw.equals("�׽�ǧ��")) {
                    //                 			�����������λ�����׽�ǧ�ˣ���Ҫ���е�λת��
                    ӯ�� = Mjkg_to_kcalkg(ӯ��, 0);
                    Zhibvalue = Mjkg_to_kcalkg(Zhibvalue, 0);
                }

                if (Zengkkjsdw.equals("�׽�ǧ��")) {
                    //                 			�����������λ�����׽�ǧ�ˣ���Ҫ���е�λת��
                    Zengkkjs = Mjkg_to_kcalkg(Zengkkjs, 0);
                }

                if (Zengkklx.equals("����")) {

                    //�������������ģ����������������ơ����ڣ�С�ڡ��ģ����ա�Ԫ/ǧ����ִ������
                    //��������۵��ۣ���λ�ǣ���
                    if (Zengkkjs > 0) {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);


                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }

                } else if (Zengkklx.equals("�۸�")) {

                    if (Zengkkjs > 0) {


                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);

                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(-Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }
                }

                ����۸� = ��ͬ�۸� + ָ���۵���;
                ����۸� = Round_new(����۸� * Zhibvalue, ú�˰���۱���С��λ);
                setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);

            } else {

                if (����۸� == 0) {

                    if (Jildw.equals("�׽�ǧ��")) {
                        //                 			�����������λ�����׽�ǧ�ˣ���Ҫ���е�λת��
                        Zhibvalue = Mjkg_to_kcalkg(Zhibvalue, 0);
                    }

                    //���������ۿ������ģ������ۿ����������ơ����䡢���ڡ��ģ������ۿ���������ۿ�� ��ȫ��Ϊ��ʱ�������ա���ͬ�ۡ����н���
                    ����۸� = ��ͬ�۸� * Zhibvalue;
                    ��ͬ��׼ = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                    setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);
                }
            }

        } else if (Zengkkdw.equals("Ԫ/��")) {

            // ���ա�Ԫ/�֡�ִ������
            if (Zengkkdj > 0) {

                if (!Jildw.equals(Zengkkjsdw)) {

                    if (Jildw.equals("�׽�ǧ��")) {

                        Zengkkjs = Kcalkg_to_mjkg(Zengkkjs, 2);
                        Zhibvalue = Mjkg_to_kcalkg(Zhibvalue, 0);
                    } else if (Jildw.equals("ǧ��ǧ��")) {

                        Zengkkjs = Mjkg_to_kcalkg(Zengkkjs, 0);
                    }
                }

                if (Zengkklx.equals("����")) {

                    //�������������ģ����������������ơ����ڣ�С�ڡ��ģ����ա�Ԫ/ǧ����ִ������
                    //��������۵��ۣ���λ�ǣ�Ԫ
                    if (Zengkkjs > 0) {


                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);

                    } else if (Zengkkjs == 0) {


                        ָ���۵��� += Round_new(Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }

                } else if (Zengkklx.equals("�۸�")) {

                    if (Zengkkjs > 0) {


                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);


                    } else if (Zengkkjs == 0) {


                        ָ���۵��� += Round_new(-Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }
                }

                ����۸� = Round_new((��ͬ�۸� * Zhibvalue + ָ���۵���), ú�˰���۱���С��λ);

            } else {

                if (����۸� == 0) {
                    // ���������ۿ������ģ������ۿ����������ơ����䡢���ڡ��ģ������ۿ���������ۿ�� ��ȫ��Ϊ��ʱ�������ա���ͬ�ۡ����н���
                    if (Jildw.equals("�׽�ǧ��")) {

                        Zhibvalue = Mjkg_to_kcalkg(Zhibvalue, 0);
                    }

                    ����۸� = ��ͬ�۸� * Zhibvalue;
                    ��ͬ��׼ = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                }
            }
            //���ָ����ϡ����޵Ĳλ�ǡ��׽�ǧ�ˡ��򽫡�ӯ��������λת����Ϊ�˷�����ʾ�ʹ���
            if (Jildw.equals("�׽�ǧ��")) {

                ӯ�� = Mjkg_to_kcalkg(ӯ��, 0);
            }
            setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);

        } else if (Zengkkdw.equals("Ԫ/�׽�")) {

            if (Zengkkdj > 0) {

                if (Jildw.equals("ǧ��ǧ��")) {
                    //                 			�����������λ����ǧ��ǧ�ˣ���Ҫ���е�λת��
                    ӯ�� = Kcalkg_to_mjkg(ӯ��, 2);
                    Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                }

                if (Zengkkjsdw.equals("ǧ��ǧ��")) {
                    //                 			�����������λ����ǧ��ǧ�ˣ���Ҫ���е�λת��
                    Zengkkjs = Kcalkg_to_mjkg(Zengkkjs, 2);
                }

                if (Zengkklx.equals("����")) {

                    //�������������ģ����������������ơ����ڣ�С�ڡ��ģ����ա�Ԫ/ǧ����ִ������
                    //��������۵��ۣ���λ�ǣ���
                    if (Zengkkjs > 0) {


                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);

                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }
                } else if (Zengkklx.equals("�۸�")) {

                    if (Zengkkjs > 0) {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);

                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(-Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }
                }

                ָ���۵��� = Round_new(ָ���۵���, ú�����ۿ��С��λ);
                ����۸� = Round_new(��ͬ�۸� * Zhibvalue + ָ���۵���, ú�˰���۱���С��λ);
            } else {

                // ���������ۿ������ģ������ۿ����������ơ����䡢���ڡ��ģ������ۿ���������ۿ�� ��ȫ��Ϊ��ʱ�������ա���ͬ�ۡ����н���
                if (����۸� != 0) {


                } else {

                    if (Jildw.equals("ǧ��ǧ��")) {

                        //                 					�����������λ����ǧ��ǧ�ˣ���Ҫ���е�λת��
                        Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                    }

                    ����۸� = ��ͬ�۸� * Zhibvalue;
                    ��ͬ��׼ = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                }
            }
            //���ָ����ϡ����޵Ĳλ�ǡ��׽�ǧ�ˡ��򽫡�ӯ��������λת����Ϊ�˷�����ʾ�ʹ���
            if (Jildw.equals("�׽�ǧ��")) {

                ӯ�� = Mjkg_to_kcalkg(ӯ��, 0);
            }
            setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);

        } else if (Zengkkdw.equals("")) {

            // ���ա�Ԫ/�֡�ִ������
            if (Zengkkdj > 0) {

                if (!Jildw.equals(Zengkkjsdw)) {

                    if (Jildw.equals("�׽�ǧ��")) {

                        Zengkkjs = Kcalkg_to_mjkg(Zengkkjs, 2);
                    } else if (Jildw.equals("ǧ��ǧ��")) {

                        Zengkkjs = Mjkg_to_kcalkg(Zengkkjs, 0);
                    }
                }

                if (Zengkklx.equals("����")) {

                    // �������������ģ����������������ơ����ڣ�С�ڡ��ģ����ա�Ԫ/ǧ����ִ������
                    //��������۵��ۣ���λ�ǣ�Ԫ
                    if (Zengkkjs > 0) {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);

                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }
                } else if (Zengkklx.equals("�۸�")) {

                    if (Zengkkjs > 0) {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);

                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(-Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }
                }

                ����۸� = Round_new((��ͬ�۸� + ָ���۵���), ú�˰���۱���С��λ);

            } else {

                // ���������ۿ������ģ������ۿ����������ơ����䡢���ڡ��ģ������ۿ���������ۿ�� ��ȫ��Ϊ��ʱ�������ա���ͬ�ۡ����н���
                if (����۸� != 0) {

                } else {

                    if (Jildw.equals("�׽�ǧ��")) {

                        Zhibvalue = Mjkg_to_kcalkg(Zhibvalue, 0);
                    }
                    ����۸� = ��ͬ�۸� * Zhibvalue;
                    ��ͬ��׼ = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                }

            }
            if (Jildw.equals("�׽�ǧ��")) {

                ӯ�� = Mjkg_to_kcalkg(ӯ��, 0);
            }
            setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);
        }
    }


    //���ݷ������������۸�(�۸�λ:"Ԫ/��")
    void Jiesjgjs_farl_yd(String Jildw, String Zengkkdw, String Zengkktj, double Zhibvalue, double Hetsx, double Hetxx, String Zhibyk,
                          double Zengkkjs, String Zengkkjsdw, double Zengkkdj, String Zengkkdjgs, String Zengkklx, double Zengkkjzzkj, String Zengkkxscl) {
        //����۸����
        //���������ۿλ�����ۿ�������ָ��ֵ����ͬ���ޣ���ͬ���ޣ�ָ��ӯ��(��ֵ��)�����ۿ���������ۿ��,���ۿ����ͣ����ۿ�С������

        if (Zengkktj.equals("���ڵ���") || Zengkktj.equals("����")) {

            ӯ�� = Round_new((Zhibvalue - Hetxx), 2);
            ��ͬ��׼ = String.valueOf(Hetxx);
        } else if (Zengkktj.equals("С�ڵ���") || Zengkktj.equals("С��")) {

            ӯ�� = Round_new((Zhibvalue - Hetsx), 2);
            ��ͬ��׼ = String.valueOf(Hetsx);
        } else if (Zengkktj.equals("����")) {

            if (Zengkklx.equals("����")) {

                ӯ�� = Round_new((Zhibvalue - Hetxx), 2);
            } else if (Zengkklx.equals("�۸�")) {

                ӯ�� = Round_new((Zhibvalue - Hetsx), 2);
            }
            ��ͬ��׼ = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
        }

        if (!Zengkkdjgs.equals("")) {
            //�������ۿʽ
            //�ù�ʽ���¸�"Zengkkdj"��ֵ

            Zengkkdj = (double)eval(Zengkkdjgs);
        }

        if (Zengkkdw.equals("Ԫ/ǧ��")) {

            if (Zengkkdj > 0) {

                if (Jildw.equals("�׽�ǧ��")) {
                    //                 			�����������λ�����׽�ǧ�ˣ���Ҫ���е�λת��
                    ӯ�� = Mjkg_to_kcalkg(ӯ��, 0);
                    Zhibvalue = Mjkg_to_kcalkg(Zhibvalue, 0);
                }

                if (Zengkkjsdw.equals("�׽�ǧ��")) {
                    //                 			�����������λ�����׽�ǧ�ˣ���Ҫ���е�λת��
                    Zengkkjs = Mjkg_to_kcalkg(Zengkkjs, 0);
                }

                if (Zengkklx.equals("����")) {

                    //�������������ģ����������������ơ����ڣ�С�ڡ��ģ����ա�Ԫ/ǧ����ִ������
                    //��������۵��ۣ���λ�ǣ���
                    if (Zengkkjs > 0) {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);

                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }

                } else if (Zengkklx.equals("�۸�")) {

                    if (Zengkkjs > 0) {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);

                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(-Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }
                }

                ָ���۵��� = Round_new(ָ���۵��� * Zhibvalue, ú�����ۿ��С��λ);
                ����۸� = ��ͬ�۸� + ָ���۵���;
                setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);
            } else {

                // ���������ۿ������ģ������ۿ����������ơ����䡢���ڡ��ģ������ۿ���������ۿ�� ��ȫ��Ϊ��ʱ�������ա���ͬ�ۡ����н���
                if (����۸� != 0) {


                } else {

                    ����۸� = ��ͬ�۸�;
                    ��ͬ��׼ = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                    setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);
                }
            }

        } else if (Zengkkdw.equals("Ԫ/��")) {

            // ���ա�Ԫ/�֡�ִ������
            if (Zengkkdj > 0) {

                if (!Jildw.equals(Zengkkjsdw)) {

                    if (Jildw.equals("�׽�ǧ��")) {

                        Zengkkjs = Kcalkg_to_mjkg(Zengkkjs, 2);
                    } else if (Jildw.equals("ǧ��ǧ��")) {

                        Zengkkjs = Mjkg_to_kcalkg(Zengkkjs, 0);
                    }
                }

                if (Zengkklx.equals("����")) {

                    // �������������ģ����������������ơ����ڣ�С�ڡ��ģ����ա�Ԫ/ǧ����ִ������
                    //��������۵��ۣ���λ�ǣ�Ԫ
                    if (Zengkkjs > 0) {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);

                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }

                } else if (Zengkklx.equals("�۸�")) {

                    if (Zengkkjs > 0) {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);

                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(-Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }
                }

                ����۸� = Round_new((��ͬ�۸� + ָ���۵���), ú�˰���۱���С��λ);
            } else {

                // ���������ۿ������ģ������ۿ����������ơ����䡢���ڡ��ģ������ۿ���������ۿ�� ��ȫ��Ϊ��ʱ�������ա���ͬ�ۡ����н���
                if (����۸� != 0) {

                } else {

                    ����۸� = ��ͬ�۸�;
                    ��ͬ��׼ = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                }

            }
            //���ָ����ϡ����޵Ĳλ�ǡ��׽�ǧ�ˡ��򽫡�ӯ��������λת����Ϊ�˷�����ʾ�ʹ���
            if (Jildw.equals("�׽�ǧ��")) {

                ӯ�� = Mjkg_to_kcalkg(ӯ��, 0);
            }
            setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);

        } else if (Zengkkdw.equals("Ԫ/�׽�")) {

            if (Zengkkdj > 0) {

                if (Jildw.equals("ǧ��ǧ��")) {
                    //                 			�����������λ����ǧ��ǧ�ˣ���Ҫ���е�λת��
                    ӯ�� = Kcalkg_to_mjkg(ӯ��, 2);
                    Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                }

                if (Zengkkjsdw.equals("ǧ��ǧ��")) {
                    //                 			�����������λ����ǧ��ǧ�ˣ���Ҫ���е�λת��
                    Zengkkjs = Kcalkg_to_mjkg(Zengkkjs, 2);
                }

                if (Zengkklx.equals("����")) {

                    //�������������ģ����������������ơ����ڣ�С�ڡ��ģ����ա�Ԫ/ǧ����ִ������
                    //��������۵��ۣ���λ�ǣ���
                    if (Zengkkjs > 0) {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);

                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }
                } else if (Zengkklx.equals("�۸�")) {

                    if (Zengkkjs > 0) {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);

                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(-Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }
                }

                ָ���۵��� = Round_new(ָ���۵���, ú�����ۿ��С��λ);
                ����۸� = Round_new(��ͬ�۸� * Zhibvalue + ָ���۵���, ú�˰���۱���С��λ);
            } else {

                // ���������ۿ������ģ������ۿ����������ơ����䡢���ڡ��ģ������ۿ���������ۿ�� ��ȫ��Ϊ��ʱ�������ա���ͬ�ۡ����н���
                if (����۸� != 0) {


                } else {

                    if (Jildw.equals("ǧ��ǧ��")) {

                        //                 					�����������λ����ǧ��ǧ�ˣ���Ҫ���е�λת��
                        Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                    }

                    ����۸� = ��ͬ�۸� * Zhibvalue;
                    ��ͬ��׼ = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                }
            }
            //���ָ����ϡ����޵Ĳλ�ǡ��׽�ǧ�ˡ��򽫡�ӯ��������λת����Ϊ�˷�����ʾ�ʹ���
            if (Jildw.equals("�׽�ǧ��")) {

                ӯ�� = Mjkg_to_kcalkg(ӯ��, 0);
            }
            setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);

        } else if (Zengkkdw.equals("")) {

            // ���ա�Ԫ/�֡�ִ������
            if (Zengkkdj > 0) {

                if (!Jildw.equals(Zengkkjsdw)) {

                    if (Jildw.equals("�׽�ǧ��")) {

                        Zengkkjs = Kcalkg_to_mjkg(Zengkkjs, 2);
                    } else if (Jildw.equals("ǧ��ǧ��")) {

                        Zengkkjs = Mjkg_to_kcalkg(Zengkkjs, 0);
                    }
                }

                if (Zengkklx.equals("����")) {

                    // �������������ģ����������������ơ����ڣ�С�ڡ��ģ����ա�Ԫ/ǧ����ִ������
                    //��������۵��ۣ���λ�ǣ�Ԫ
                    if (Zengkkjs > 0) {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);

                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }
                } else if (Zengkklx.equals("�۸�")) {

                    if (Zengkkjs > 0) {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);

                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(-Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }
                }

                ����۸� = Round_new((��ͬ�۸� + ָ���۵���), ú�˰���۱���С��λ);

            } else {
                // ���������ۿ������ģ������ۿ����������ơ����䡢���ڡ��ģ������ۿ���������ۿ�� ��ȫ��Ϊ��ʱ�������ա���ͬ�ۡ����н���
                if (����۸� != 0) {

                } else {

                    ����۸� = ��ͬ�۸�;
                    ��ͬ��׼ = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                }

            }
            if (Jildw.equals("�׽�ǧ��")) {

                ӯ�� = Mjkg_to_kcalkg(ӯ��, 0);
            }
            setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);
        }
    }

    //���ݷ������������۸�(�۸�λ:"�׽�/��")
    void Jiesjgjs_farl_Mjd(String Jildw, String Zengkkdw, String Zengkktj, double Zhibvalue, double Hetsx, double Hetxx, String Zhibyk,
                           double Zengkkjs, String Zengkkjsdw, double Zengkkdj, String Zengkkdjgs, String Zengkklx, double Zengkkjzzkj, String Zengkkxscl) {
        //����۸����
        //���������ۿλ�����ۿ�������ָ��ֵ����ͬ���ޣ���ͬ���ޣ�ָ��ӯ��(��ֵ��)�����ۿ���������ۿ��,���ۿ�����,���ۿ�С������
        //�׽�/�ֵļ۸���㷽ʽ ��ͬ�۸���׽�

        if (Zengkktj.equals("���ڵ���") || Zengkktj.equals("����")) {

            ӯ�� = Round_new((Zhibvalue - Hetxx), 2);
            ��ͬ��׼ = String.valueOf(Hetxx);
        } else if (Zengkktj.equals("С�ڵ���") || Zengkktj.equals("С��")) {

            ӯ�� = Round_new((Zhibvalue - Hetsx), 2);
            ��ͬ��׼ = String.valueOf(Hetsx);
        } else if (Zengkktj.equals("����")) {

            if (Zengkklx.equals("����")) {

                ӯ�� = Round_new((Zhibvalue - Hetxx), 2);
            } else if (Zengkklx.equals("�۸�")) {

                ӯ�� = Round_new((Zhibvalue - Hetsx), 2);
            }
            ��ͬ��׼ = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
        }


        if (!Zengkkdjgs.equals("")) {
            //�������ۿʽ
            //�ù�ʽ���¸�"Zengkkdj"��ֵ

            Zengkkdj = (double)eval(Zengkkdjgs);
        }

        if (Zengkkdw.equals("Ԫ/ǧ��")) {

            if (Zengkkdj > 0) {

                if (Jildw.equals("�׽�ǧ��")) {
                    //                 			�����������λ�����׽�ǧ�ˣ���Ҫ���е�λת��
                    ӯ�� = Mjkg_to_kcalkg(ӯ��, 0);
                    Zhibvalue = Mjkg_to_kcalkg(Zhibvalue, 0);
                }

                if (Zengkkjsdw.equals("�׽�ǧ��")) {
                    //                 			�����������λ�����׽�ǧ�ˣ���Ҫ���е�λת��
                    Zengkkjs = Mjkg_to_kcalkg(Zengkkjs, 0);
                }

                if (Zengkklx.equals("����")) {

                    //�������������ģ����������������ơ����ڣ�С�ڡ��ģ����ա�Ԫ/ǧ����ִ������
                    //��������۵��ۣ���λ�ǣ���
                    if (Zengkkjs > 0) {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);

                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }
                } else if (Zengkklx.equals("�۸�")) {

                    if (Zengkkjs > 0) {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);

                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(-Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);

                    }
                }

                ָ���۵��� = Round_new(ָ���۵��� * Zhibvalue, ú�����ۿ��С��λ);
                Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                ����۸� = Round_new(��ͬ�۸� * Zhibvalue + ָ���۵���, ú�˰���۱���С��λ);
                setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);
            } else {

                // ���������ۿ������ģ������ۿ����������ơ����䡢���ڡ��ģ������ۿ���������ۿ�� ��ȫ��Ϊ��ʱ�������ա���ͬ�ۡ����н���
                if (����۸� != 0) {

                } else {

                    if (Jildw.equals("ǧ��ǧ��")) {
                        //                 					�����������λ����ǧ��ǧ�ˣ���Ҫ���е�λת��
                        Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                    }
                    ����۸� = ��ͬ�۸� * Zhibvalue;
                    ��ͬ��׼ = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                    setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);
                }
            }

        } else if (Zengkkdw.equals("Ԫ/��")) {

            // ���ա�Ԫ/�֡�ִ������
            if (Zengkkdj > 0) {

                if (!Jildw.equals(Zengkkjsdw)) {

                    if (Jildw.equals("�׽�ǧ��")) {

                        Zengkkjs = Kcalkg_to_mjkg(Zengkkjs, 2);
                    } else if (Jildw.equals("ǧ��ǧ��")) {

                        Zengkkjs = Mjkg_to_kcalkg(Zengkkjs, 0);
                    }
                }

                if (Zengkklx.equals("����")) {

                    // �������������ģ����������������ơ����ڣ�С�ڡ��ģ����ա�Ԫ/ǧ����ִ������
                    //��������۵��ۣ���λ�ǣ�Ԫ
                    if (Zengkkjs > 0) {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);

                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);

                    }
                } else if (Zengkklx.equals("�۸�")) {

                    if (Zengkkjs > 0) {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);

                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(-Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);

                    }
                }

                if (Jildw.equals("ǧ��ǧ��")) {

                    Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                }

                ����۸� = Round_new((��ͬ�۸� * Zhibvalue + ָ���۵���), ú�˰���۱���С��λ);

            } else {

                // ���������ۿ������ģ������ۿ����������ơ����䡢���ڡ��ģ������ۿ���������ۿ�� ��ȫ��Ϊ��ʱ�������ա���ͬ�ۡ����н���
                if (����۸� != 0) {

                } else {

                    if (Jildw.equals("ǧ��ǧ��")) {

                        Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                    }
                    ����۸� = ��ͬ�۸� * Zhibvalue;
                    ��ͬ��׼ = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                }

            }
            //���ָ����ϡ����޵Ĳλ�ǡ��׽�ǧ�ˡ��򽫡�ӯ��������λת����Ϊ�˷�����ʾ�ʹ���
            if (Jildw.equals("�׽�ǧ��")) {

                ӯ�� = Mjkg_to_kcalkg(ӯ��, 0);
            }
            setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);

        } else if (Zengkkdw.equals("Ԫ/�׽�")) {

            if (Zengkkdj > 0) {

                if (Jildw.equals("ǧ��ǧ��")) {
                    //                 			�����������λ����ǧ��ǧ�ˣ���Ҫ���е�λת��
                    ӯ�� = Kcalkg_to_mjkg(ӯ��, 2);
                    Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                }

                if (Zengkkjsdw.equals("ǧ��ǧ��")) {
                    //                 			�����������λ����ǧ��ǧ�ˣ���Ҫ���е�λת��
                    Zengkkjs = Kcalkg_to_mjkg(Zengkkjs, 2);
                }

                if (Zengkklx.equals("����")) {

                    //�������������ģ����������������ơ����ڣ�С�ڡ��ģ����ա�Ԫ/ǧ����ִ������
                    //��������۵��ۣ���λ�ǣ���
                    if (Zengkkjs > 0) {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);

                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }
                } else if (Zengkklx.equals("�۸�")) {

                    if (Zengkkjs > 0) {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);
                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(-Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }
                }

                ָ���۵��� = Round_new(ָ���۵���, ú�����ۿ��С��λ);
                ����۸� = Round_new(��ͬ�۸� * Zhibvalue + ָ���۵���, ú�˰���۱���С��λ);
            } else {

                // ���������ۿ������ģ������ۿ����������ơ����䡢���ڡ��ģ������ۿ���������ۿ�� ��ȫ��Ϊ��ʱ�������ա���ͬ�ۡ����н���
                if (����۸� != 0) {


                } else {

                    if (Jildw.equals("ǧ��ǧ��")) {

                        //                 					�����������λ����ǧ��ǧ�ˣ���Ҫ���е�λת��
                        Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                    }

                    ����۸� = ��ͬ�۸� * Zhibvalue;
                    ��ͬ��׼ = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                }
            }
            //���ָ����ϡ����޵Ĳλ�ǡ��׽�ǧ�ˡ��򽫡�ӯ��������λת����Ϊ�˷�����ʾ�ʹ���
            if (Jildw.equals("�׽�ǧ��")) {

                ӯ�� = Mjkg_to_kcalkg(ӯ��, 0);
            }
            setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);

        } else if (Zengkkdw.equals("")) {

            // ���ա�Ԫ/�֡�ִ������
            if (Zengkkdj > 0) {

                if (!Jildw.equals(Zengkkjsdw)) {

                    if (Jildw.equals("�׽�ǧ��")) {

                        Zengkkjs = Kcalkg_to_mjkg(Zengkkjs, 2);
                    } else if (Jildw.equals("ǧ��ǧ��")) {

                        Zengkkjs = Mjkg_to_kcalkg(Zengkkjs, 0);
                    }
                }

                if (Zengkklx.equals("����")) {

                    // �������������ģ����������������ơ����ڣ�С�ڡ��ģ����ա�Ԫ/ǧ����ִ������
                    //��������۵��ۣ���λ�ǣ�Ԫ
                    if (Zengkkjs > 0) {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);
                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }
                } else if (Zengkklx.equals("�۸�")) {

                    if (Zengkkjs > 0) {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(ӯ�� / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);
                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(-Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }
                }

                if (Jildw.equals("ǧ��ǧ��")) {

                    Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                }
                ����۸� = Round_new((��ͬ�۸� * Zhibvalue + ָ���۵���), ú�˰���۱���С��λ);

            } else {

                // ���������ۿ������ģ������ۿ����������ơ����䡢���ڡ��ģ������ۿ���������ۿ�� ��ȫ��Ϊ��ʱ�������ա���ͬ�ۡ����н���
                if (����۸� != 0) {

                } else {
                    if (Jildw.equals("ǧ��ǧ��")) {

                        //                 					�����������λ����ǧ��ǧ�ˣ���Ҫ���е�λת��
                        Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                    }
                    ����۸� = ��ͬ�۸� * Zhibvalue;
                    ��ͬ��׼ = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                }

            }
            if (Jildw.equals("�׽�ǧ��")) {

                ӯ�� = Mjkg_to_kcalkg(ӯ��, 0);
            }
            setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);
        }

    }
    private String Qnetar������λ;
    private double Qnetarǧ��ǧ��;
    private double Qnetar�׽�ǧ��;
    //��������ָ���ۼ�
    void Jiesqtzbzj(String Zengkktj, double Zhibvalue, double Hetsx, double Hetxx, String Zhibyk,
                    double Zengkkjs, double Zengkkdj, String Zengkkdjgs, String Zengkjdw, String Zengkklx, double Zengkkjzzkj,
                    String Zengkkxscl) {
        //����۸����
        //���������ۿ�������ָ��ֵ����ͬ���ޣ���ͬ���ޣ�ָ��ӯ��(��ֵ��)�����ۿ���������ۿ�ۣ����ۿ����ͣ��������۸���


        if (Zengkktj.equals("���ڵ���") || Zengkktj.equals("����")) {

            ӯ�� = Zhibvalue - Hetxx;
            ��ͬ��׼ = String.valueOf(Hetxx);
        } else if (Zengkktj.equals("С�ڵ���") || Zengkktj.equals("С��")) {

            ӯ�� = Zhibvalue - Hetsx;
            ��ͬ��׼ = String.valueOf(Hetsx);
        } else if (Zengkktj.equals("����")) {

            if (Zengkklx.equals("����")) {

                ӯ�� = Round_new((Zhibvalue - Hetxx), 2);
            } else if (Zengkklx.equals("�۸�")) {

                ӯ�� = Round_new((Zhibvalue - Hetsx), 2);
            }
            ��ͬ��׼ = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
        }


        if (!Zengkkdjgs.equals("")) {
            //�������ۿʽ
            //�ù�ʽ���¸�"Zengkkdj"��ֵ

            ָ���۵��� = (double)eval(Zengkkdjgs);
            setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);
        } else if (!(Zengkkdj == 0)) {

            //�������������ģ����������������ơ����ڣ�С�ڡ��ģ����ա�Ԫ/ǧ����ִ������

            if (Zengkklx.equals("����")) {

                if (Zengkkjs > 0) {

                    ָ���۵��� += Round_new(Round_new(Xiaoscl(Math.abs(ӯ��) / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);
                } else if (Zengkkjs == 0) {

                    ָ���۵��� += Round_new(Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                }
            } else if (Zengkklx.equals("�۸�")) {

                if (Zengkkjs > 0) {

                    if (ӯ�� >= 0) {

                        ָ���۵��� += Round_new(-Round_new(Xiaoscl(Math.abs(ӯ��) / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);
                    } else {

                        ָ���۵��� += Round_new(-Round_new(Xiaoscl(Math.abs(ӯ��) / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }

                } else if (Zengkkjs == 0) {

                    if (ӯ�� >= 0) {

                        ָ���۵��� += Round_new(-Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    } else {

                        ָ���۵��� += Round_new(-Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }
                }
            }

            if (Zengkjdw.equals("Ԫ/ǧ��")) {

                if (Qnetar������λ.equals("ǧ��ǧ��")) {

                    ��������_������ = Qnetarǧ��ǧ��;
                } else if (Qnetar������λ.equals("�׽�ǧ��")) {

                    ��������_������ = Mjkg_to_kcalkg(Qnetar�׽�ǧ��, 0);
                }

                ָ���۵��� = CustomMaths.mul(ָ���۵���, ��������_������);
            }

            if (Zengkjdw.equals("%��")) {

                setEvaluate_Jiessl(Zhibyk, ��ͬ��׼, ӯ��, ָ���۵���, Zengkjdw);
            } else {

                setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);
            }

        } else {

            //���������ۿ������ģ������ۿ����������ơ����䡢���ڡ��ģ������ۿ���������ۿ�� ��ȫ��Ϊ��ʱ�������ա���ͬ�ۡ����н���
            if (ָ���۵��� != 0) {

            } else {

                ָ���۵��� = 0;

                if (Zengkjdw.equals("%��")) {

                    setEvaluate_Jiessl(Zhibyk, ��ͬ��׼, ӯ��, ָ���۵���, Zengkjdw);
                } else {

                    setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);
                }
            }
        }
    }

    //��������ָ���ۼ�_������������ ��֡��˾�����û����XX�۶��ٶ���Ǯ��
    void Jiesqtzbzj_fx(String Zengkktj, double Zhibvalue, double Hetsx, double Hetxx, String Zhibyk,
                       double Zengkkjs, double Zengkkdj, String Zengkjdw, String Zengkklx, double Zengkkjzzkj,
                       String Zengkkxscl) {

        //����۸����
        //���������ۿ�������ָ��ֵ����ͬ���ޣ���ͬ���ޣ�ָ��ӯ��(��ֵ��)�����ۿ���������ۿ�ۣ����ۿ����ͣ��������۸���,��׼���ۼۣ�С������


        if (Zengkktj.equals("���ڵ���") || Zengkktj.equals("����")) {

            ӯ�� = Round_new((Zhibvalue - Hetxx), 2);
            ��ͬ��׼ = String.valueOf(Hetxx);
        } else if (Zengkktj.equals("С�ڵ���") || Zengkktj.equals("С��")) {

            ӯ�� = Round_new((Zhibvalue - Hetsx), 2);
            ��ͬ��׼ = String.valueOf(Hetsx);
        } else if (Zengkktj.equals("����")) {

            if (Zengkklx.equals("����")) {

                ӯ�� = Round_new((Zhibvalue - Hetsx), 2);
            } else if (Zengkklx.equals("�۸�")) {

                ӯ�� = Round_new((Zhibvalue - Hetxx), 2);
            }
            ��ͬ��׼ = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
        }

        if (!(Zengkkdj == 0)) {

            //�������������ģ����������������ơ����ڣ�С�ڡ��ģ����ա�Ԫ/ǧ����ִ������

            if (Zengkklx.equals("����")) {

                if (Zengkkjs > 0) {

                    ָ���۵��� += Round_new(Round_new(Xiaoscl(Math.abs(ӯ��) / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);
                } else if (Zengkkjs == 0) {

                    ָ���۵��� += Round_new(Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                }
            } else if (Zengkklx.equals("�۸�")) {

                if (Zengkkjs > 0) {

                    if (ӯ�� >= 0) {

                        ָ���۵��� += Round_new(-Round_new(Xiaoscl(Math.abs(ӯ��) / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);
                    } else {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(Math.abs(ӯ��) / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }

                } else if (Zengkkjs == 0) {

                    if (ӯ�� >= 0) {

                        ָ���۵��� += Round_new(-Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    } else {

                        ָ���۵��� += Round_new(Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }
                }
            }

            if (Zengkjdw.equals("Ԫ/ǧ��")) {

                if (Qnetar������λ.equals("ǧ��ǧ��")) {

                    ��������_������ = Qnetarǧ��ǧ��;
                } else if (Qnetar������λ.equals("�׽�ǧ��")) {

                    ��������_������ = Mjkg_to_kcalkg(Qnetar�׽�ǧ��, 0);
                }

                ָ���۵��� = CustomMaths.mul(ָ���۵���, ��������_������);
            }

            setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);
        } else {

            //���������ۿ������ģ������ۿ����������ơ����䡢���ڡ��ģ������ۿ���������ۿ�� ��ȫ��Ϊ��ʱ�������ա���ͬ�ۡ����н���
            if (ָ���۵��� != 0) {

            } else {

                ָ���۵��� = 0;
                setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);
            }
        }
    }

    //���������۵��ۣ����ڹ�ú����������ͬ��ʱ���Կ󷽽����ã�
    void Jiesslzj(String Jildw, String Zengkkdw, String Zengkktj, double Zhibvalue, double Hetsx, double Hetxx, String Zhibyk,
                  double Zengkkjs, String Zengkkjsdw, double Zengkkdj, String Zengkkdjgs, String Zengkklx, double Zengkkjzzkj, String Zengkkxscl) {

        //�������������λ����֣��ڵ��ù�ʽǰ���Ѿ�java�н�����ת�����ؼ���Ҫ�жϼ�����λ�����ۿ����֮��ĵ�λ
        if (Jildw.equals("���")) {

            if (Zengkkjsdw.equals("��")) {

                Zengkkjs = Round_new(Zengkkjs / 10000, 10);
            }
        } else if (Jildw.equals("��")) {

            if (Zengkkjsdw.equals("���")) {

                Zengkkjs = Round_new(Zengkkjs * 10000, 2);
            }
        }

        if (Zengkktj.equals("���ڵ���") || Zengkktj.equals("����")) {

            ӯ�� = Round_new((Zhibvalue - Hetxx), 2);
            ��ͬ��׼ = String.valueOf(Hetxx);
        } else if (Zengkktj.equals("С�ڵ���") || Zengkktj.equals("С��")) {

            ӯ�� = Round_new((Zhibvalue - Hetsx), 2);
            ��ͬ��׼ = String.valueOf(Hetsx);
        } else if (Zengkktj.equals("����")) {

            if (Zengkklx.equals("����")) {

                ӯ�� = Round_new((Zhibvalue - Hetxx), 2);
            } else if (Zengkklx.equals("�۸�")) {

                ӯ�� = Round_new((Zhibvalue - Hetsx), 2);
            }
            ��ͬ��׼ = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
        }

        if (!Zengkkdjgs.equals("")) {
            //�������ۿʽ
            //�ù�ʽ���¸�"Zengkkdj"��ֵ

            Zengkkdj = (double)eval(Zengkkdjgs);
        }

        if (Zengkkdw.equals("Ԫ/ǧ��")) {

            if (Qnetar������λ.equals("ǧ��ǧ��")) {

                ��������_������ = Qnetarǧ��ǧ��;
            } else if (Qnetar������λ.equals("�׽�ǧ��")) {

                ��������_������ = Mjkg_to_kcalkg(Qnetar�׽�ǧ��, 0);
            }

            if (Zengkkdj > 0) {

                if (Zengkklx.equals("����")) {

                    //�������������ģ����������������ơ����ڣ�С�ڡ��ģ����ա�Ԫ/ǧ����ִ������
                    //��������۵��ۣ���λ�ǣ���

                    if (Zengkkjs > 0) {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(Math.abs(ӯ��) / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);
                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }
                } else if (Zengkklx.equals("�۸�")) {

                    if (Zengkkjs > 0) {

                        if (ӯ�� >= 0) {

                            ָ���۵��� += Round_new(-Round_new(Xiaoscl(Math.abs(ӯ��) / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);
                        } else {

                            ָ���۵��� += Round_new(-Round_new(Xiaoscl(Math.abs(ӯ��) / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);
                        }

                    } else if (Zengkkjs == 0) {

                        if (ӯ�� >= 0) {

                            ָ���۵��� += Round_new(-Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                        } else {

                            ָ���۵��� += Round_new(-Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                        }
                    }
                }

                ָ���۵��� = Round_new(ָ���۵��� * ��������_������, ú�����ۿ��С��λ);
                setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);
            } else {

                // ���������ۿ������ģ������ۿ����������ơ����䡢���ڡ��ģ������ۿ���������ۿ�� ��ȫ��Ϊ��ʱ�������ա���ͬ�ۡ����н���
                if (ָ���۵��� != 0) {

                } else {

                    ��ͬ��׼ = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                    setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);
                }
            }

        } else if (Zengkkdw.equals("Ԫ/��")) {

            // ���ա�Ԫ/�֡�ִ������
            if (Zengkkdj > 0) {

                if (Zengkklx.equals("����")) {

                    // �������������ģ����������������ơ����ڣ�С�ڡ��ģ����ա�Ԫ/ǧ����ִ������
                    //��������۵��ۣ���λ�ǣ�Ԫ
                    if (Zengkkjs > 0) {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(Math.abs(ӯ��) / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);
                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }
                } else if (Zengkklx.equals("�۸�")) {

                    if (Zengkkjs > 0) {

                        if (ӯ�� >= 0) {

                            ָ���۵��� += Round_new(-Round_new(Xiaoscl(Math.abs(ӯ��) / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);
                        } else {

                            ָ���۵��� += Round_new(-Round_new(Xiaoscl(Math.abs(ӯ��) / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);
                        }

                    } else if (Zengkkjs == 0) {

                        if (ӯ�� >= 0) {

                            ָ���۵��� += Round_new(-Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                        } else {

                            ָ���۵��� += Round_new(-Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                        }
                    }
                }

                setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);

            } else {

                // ���������ۿ������ģ������ۿ����������ơ����䡢���ڡ��ģ������ۿ���������ۿ�� ��ȫ��Ϊ��ʱ�������ա���ͬ�ۡ����н���
                if (ָ���۵��� != 0) {

                } else {

                    ��ͬ��׼ = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                    setEvaluate(Zhibyk, ӯ��, ��ͬ��׼, ָ���۵���);
                }

            }
        } else if (Zengkkdw.equals("%��")) {
            //        	���������ۼۡ�%�֡������

            if (Zengkkdj > 0) {

                if (Zengkklx.equals("����")) {

                    // �������������ģ����������������ơ����ڣ�С�ڡ��ģ����ա�Ԫ/ǧ����ִ������
                    //��������۵��ۣ���λ�ǣ�Ԫ
                    if (Zengkkjs > 0) {

                        ָ���۵��� += Round_new(Round_new(Xiaoscl(Math.abs(ӯ��) / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);
                    } else if (Zengkkjs == 0) {

                        ָ���۵��� += Round_new(Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                    }
                } else if (Zengkklx.equals("�۸�")) {

                    if (Zengkkjs > 0) {

                        if (ӯ�� >= 0) {

                            ָ���۵��� += Round_new(-Round_new(Xiaoscl(Math.abs(ӯ��) / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);
                        } else {

                            ָ���۵��� += Round_new(-Round_new(Xiaoscl(Math.abs(ӯ��) / Zengkkjs, Zengkkxscl) * Zengkkdj, ú�����ۿ��С��λ) + Zengkkjzzkj, ú�����ۿ��С��λ);
                        }

                    } else if (Zengkkjs == 0) {

                        if (ӯ�� >= 0) {

                            ָ���۵��� += Round_new(-Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                        } else {

                            ָ���۵��� += Round_new(-Zengkkdj + Zengkkjzzkj, ú�����ۿ��С��λ);
                        }
                    }
                }

                setEvaluate_Jiessl(Zhibyk, ��ͬ��׼, ӯ��, ָ���۵���, Zengkjdw);

            } else {

                // ���������ۿ������ģ������ۿ����������ơ����䡢���ڡ��ģ������ۿ���������ۿ�� ��ȫ��Ϊ��ʱ�������ա���ͬ�ۡ����н���
                if (ָ���۵��� != 0) {

                } else {

                    ��ͬ��׼ = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);

                    setEvaluate_Jiessl(Zhibyk, ��ͬ��׼, ӯ��, ָ���۵���, Zengkjdw);
                }
            }
        }
    }

    public Object eval(String gongs){
        jis();
        return 0;
    }

    public void jis() {
        //�����ν���
        if (������ʽ.equals("������") || ������ʽ.equals("��Ȩƽ��") || ������ʽ.equals("��Ȩ����")) {

            Initialize();

            //��ֵ���䰴���Ƽ�
            if (�Ƽ۷�ʽ.equals("��ֵ����(��)")) {

                if (�۸�λ.equals("Ԫ/ǧ��")) {

                    ������ = Qnetarǧ��ǧ��;

                    if (Qnetar������λ.equals("�׽�ǧ��")) {

                        ������ = Qnetar�׽�ǧ��;
                    }

                    ��ͬ��׼_Qnetar = String.valueOf(Mjkg_to_kcalkg(Qnetar����, 0)) + "-" + String.valueOf(Mjkg_to_kcalkg(Qnetar����, 0));
                    ����۸� = Round_new((������ * ��ͬ�۸�), ú�˰���۱���С��λ);
                }

                if (�۸�λ.equals("Ԫ/�׽�")) {

                    if (Qnetar������λ.equals("�׽�ǧ��")) {

                        ������ = Qnetar�׽�ǧ��;
                    } else if (Qnetar������λ.equals("ǧ��ǧ��")) {

                        ������ = Kcalkg_to_mjkg(Qnetarǧ��ǧ��, 2);
                    }

                    ��ͬ��׼_Qnetar = String.valueOf(Mjkg_to_kcalkg(Qnetar����, 0)) + "-" + String.valueOf(Mjkg_to_kcalkg(Qnetar����, 0));
                    ����۸� = Round_new((������ * ��ͬ�۸�), ú�˰���۱���С��λ);
                }
            }

            //��ֵ���䰴�ּƼ�
            if (�Ƽ۷�ʽ.equals("��ֵ����(��)")) {

                ��ͬ��׼_Qnetar = String.valueOf(Mjkg_to_kcalkg(Qnetar����, 0)) + "-" + String.valueOf(Mjkg_to_kcalkg(Qnetar����, 0));
                ����۸� = Round_new(��ͬ�۸�, ú�˰���۱���С��λ);
            }

            //��ֵ�����۸�

            �������ۼ� = 0.0;
            if (�Ƽ۷�ʽ.equals("��ֵ�����۸�")) {

                if (�۸�λ.equals("Ԫ/ǧ��")) {

                    if (Qnetar������λ.equals("ǧ��ǧ��")) {

                        ������ = Qnetarǧ��ǧ��;

                        //�û��ۼ�����۸�
                        //����(Ԫ/ǧ��)
                        //���ۿλ�����ۿ�������ָ��ֵ����ͬ���ޣ���ͬ���ޣ�ָ��ӯ��(��ֵ��)����ͬ��׼����ֵ�ã������ۿ���������ۿ�ۣ�ָ���۵��ۣ���ֵ�ã�

                        if (Qnetar�������� > 0 || !Qnetar�������۹�ʽ.equals("")) {

                            Jiesjgjs_farl_yk(Qnetar������λ, Qnetar�����۵�λ, Qnetar���ۿ�����, ������, Qnetar����, Qnetar����, "ӯ��_Qnetar",
                                    Qnetar���ۿ����, Qnetar���ۿ������λ, Qnetar��������, Qnetar�������۹�ʽ, "����", Qnetar��׼���ۼ�, QnetarС������);
                        }

                        if (Qnetar�۸����� > 0 || !Qnetar�۸����۹�ʽ.equals("")) {

                            Jiesjgjs_farl_yk(Qnetar������λ, Qnetar�۸��۵�λ, Qnetar���ۿ�����, ������, Qnetar����, Qnetar����, "ӯ��_Qnetar",
                                    Qnetar���ۿ����, Qnetar���ۿ������λ, Qnetar�۸�����, Qnetar�۸����۹�ʽ, "�۸�", Qnetar��׼���ۼ�, QnetarС������);
                        }

                        if (Qnetar�������� == 0 && Qnetar�۸����� == 0 && Qnetar�������۹�ʽ.equals("") && Qnetar�۸����۹�ʽ.equals("")) {

                            Jiesjgjs_farl_yk(Qnetar������λ, Qnetar�����۵�λ, Qnetar���ۿ�����, ������, Qnetar����, Qnetar����, "ӯ��_Qnetar",
                                    Qnetar���ۿ����, Qnetar���ۿ������λ, Qnetar��������, Qnetar�������۹�ʽ, "����", Qnetar��׼���ۼ�, QnetarС������);
                        }

                    }

                    if (Qnetar������λ.equals("�׽�ǧ��")) {

                        // �û��ۼ�����۸�
                        // ���ۣ�Ԫ/ǧ����
                        //�۸�λ.equals("Ԫ/ǧ��"),������λ(�׽�/ǧ��),����(Ԫ/ǧ��)
                        //�����ۼ۵�λΪ��Ԫ/ǧ������Ҫ���е�λת��

                        //                	������=Mjkg_to_kcalkg(Qnetar�׽�ǧ��,0);		//��λת��
                        ������ = Qnetar�׽�ǧ��;

                        if (Qnetar�������� > 0 || !Qnetar�������۹�ʽ.equals("")) {

                            Jiesjgjs_farl_yk(Qnetar������λ, Qnetar�����۵�λ, Qnetar���ۿ�����, ������, Qnetar����, Qnetar����, "ӯ��_Qnetar",
                                    Qnetar���ۿ����, Qnetar���ۿ������λ, Qnetar��������, Qnetar�������۹�ʽ, "����", Qnetar��׼���ۼ�, QnetarС������);
                        }

                        if (Qnetar�۸����� > 0 || !Qnetar�۸����۹�ʽ.equals("")) {

                            Jiesjgjs_farl_yk(Qnetar������λ, Qnetar�۸��۵�λ, Qnetar���ۿ�����, ������, Qnetar����, Qnetar����, "ӯ��_Qnetar",
                                    Qnetar���ۿ����, Qnetar���ۿ������λ, Qnetar�۸�����, Qnetar�۸����۹�ʽ, "�۸�", Qnetar��׼���ۼ�, QnetarС������);
                        }

                        if (Qnetar�������� == 0 && Qnetar�۸����� == 0 && Qnetar�������۹�ʽ.equals("") && Qnetar�۸����۹�ʽ.equals("")) {

                            Jiesjgjs_farl_yk(Qnetar������λ, Qnetar�����۵�λ, Qnetar���ۿ�����, ������, Qnetar����, Qnetar����, "ӯ��_Qnetar",
                                    Qnetar���ۿ����, Qnetar���ۿ������λ, Qnetar��������, Qnetar�������۹�ʽ, "����", Qnetar��׼���ۼ�, QnetarС������);
                        }
                    }
                }

                if (�۸�λ.equals("Ԫ/��")) {

                    if (Qnetar������λ.equals("ǧ��ǧ��")) {

                        ������ = Qnetarǧ��ǧ��;
                        //�û��ۼ�����۸�
                        //����(Ԫ/ǧ��)
                        //���ۿλ�����ۿ�������ָ��ֵ����ͬ���ޣ���ͬ���ޣ�ָ��ӯ��(��ֵ��)����ͬ��׼����ֵ�ã������ۿ���������ۿ�ۣ�ָ���۵��ۣ���ֵ�ã�

                        if (Qnetar�������� > 0 || !Qnetar�������۹�ʽ.equals("")) {

                            Jiesjgjs_farl_yd(Qnetar������λ, Qnetar�����۵�λ, Qnetar���ۿ�����, ������, Qnetar����, Qnetar����, "ӯ��_Qnetar",
                                    Qnetar���ۿ����, Qnetar���ۿ������λ, Qnetar��������, Qnetar�������۹�ʽ, "����", Qnetar��׼���ۼ�, QnetarС������);
                        }

                        if (Qnetar�۸����� > 0 || !Qnetar�۸����۹�ʽ.equals("")) {

                            Jiesjgjs_farl_yd(Qnetar������λ, Qnetar�۸��۵�λ, Qnetar���ۿ�����, ������, Qnetar����, Qnetar����, "ӯ��_Qnetar",
                                    Qnetar���ۿ����, Qnetar���ۿ������λ, Qnetar�۸�����, Qnetar�۸����۹�ʽ, "�۸�", Qnetar��׼���ۼ�, QnetarС������);
                        }

                        if (Qnetar�������� == 0 && Qnetar�۸����� == 0 && Qnetar�������۹�ʽ.equals("") && Qnetar�۸����۹�ʽ.equals("")) {

                            Jiesjgjs_farl_yd(Qnetar������λ, Qnetar�����۵�λ, Qnetar���ۿ�����, ������, Qnetar����, Qnetar����, "ӯ��_Qnetar",
                                    Qnetar���ۿ����, Qnetar���ۿ������λ, Qnetar��������, Qnetar�������۹�ʽ, "����", Qnetar��׼���ۼ�, QnetarС������);
                        }
                    }

                    if (Qnetar������λ.equals("�׽�ǧ��")) {

                        //�û��ۼ���+++���۸�
                        //���ۣ�Ԫ/ǧ����
                        //�۸�λ.equals("Ԫ/ǧ��"),������λ(�׽�/ǧ��),����(Ԫ/ǧ��)
                        //�����ۼ۵�λΪ��Ԫ/ǧ������Ҫ���е�λת��

                        //                	������=Mjkg_to_kcalkg(Qnetar�׽�ǧ��,0);		//��λת��
                        ������ = Qnetar�׽�ǧ��;

                        if (Qnetar�������� > 0 || !Qnetar�������۹�ʽ.equals("")) {

                            Jiesjgjs_farl_yd(Qnetar������λ, Qnetar�����۵�λ, Qnetar���ۿ�����, ������, Qnetar����, Qnetar����, "ӯ��_Qnetar",
                                    Qnetar���ۿ����, Qnetar���ۿ������λ, Qnetar��������, Qnetar�������۹�ʽ, "����", Qnetar��׼���ۼ�, QnetarС������);
                        }

                        if (Qnetar�۸����� > 0 || !Qnetar�۸����۹�ʽ.equals("")) {

                            Jiesjgjs_farl_yd(Qnetar������λ, Qnetar�۸��۵�λ, Qnetar���ۿ�����, ������, Qnetar����, Qnetar����, "ӯ��_Qnetar",
                                    Qnetar���ۿ����, Qnetar���ۿ������λ, Qnetar�۸�����, Qnetar�۸����۹�ʽ, "�۸�", Qnetar��׼���ۼ�, QnetarС������);
                        }

                        if (Qnetar�������� == 0 && Qnetar�۸����� == 0 && Qnetar�������۹�ʽ.equals("") && Qnetar�۸����۹�ʽ.equals("")) {

                            Jiesjgjs_farl_yd(Qnetar������λ, Qnetar�����۵�λ, Qnetar���ۿ�����, ������, Qnetar����, Qnetar����, "ӯ��_Qnetar",
                                    Qnetar���ۿ����, Qnetar���ۿ������λ, Qnetar��������, Qnetar�������۹�ʽ, "����", Qnetar��׼���ۼ�, QnetarС������);
                        }
                    }
                }

                if (�۸�λ.equals("Ԫ/�׽�")) {

                    if (Qnetar������λ.equals("ǧ��ǧ��")) {

                        ������ = Qnetarǧ��ǧ��;

                        // �û��ۼ�����۸�
                        //����(Ԫ/ǧ��)
                        //���ۿλ�����ۿ�������ָ��ֵ����ͬ���ޣ���ͬ���ޣ�ָ��ӯ��(��ֵ��)����ͬ��׼����ֵ�ã������ۿ���������ۿ�ۣ�ָ���۵��ۣ���ֵ�ã�

                        if (Qnetar�������� > 0 || !Qnetar�������۹�ʽ.equals("")) {
                            Jiesjgjs_farl_yd(Qnetar������λ, Qnetar�����۵�λ, Qnetar���ۿ�����, ������, Qnetar����, Qnetar����, "ӯ��_Qnetar",
                                    Qnetar���ۿ����, Qnetar���ۿ������λ, Qnetar��������, Qnetar�������۹�ʽ, "����", Qnetar��׼���ۼ�, QnetarС������);
                        }

                        if (Qnetar�۸����� > 0 || !Qnetar�۸����۹�ʽ.equals("")) {
                            Jiesjgjs_farl_yd(Qnetar������λ, Qnetar�۸��۵�λ, Qnetar���ۿ�����, ������, Qnetar����, Qnetar����, "ӯ��_Qnetar",
                                    Qnetar���ۿ����, Qnetar���ۿ������λ, Qnetar�۸�����, Qnetar�۸����۹�ʽ, "�۸�", Qnetar��׼���ۼ�, QnetarС������);
                        }

                        if (Qnetar�������� == 0 && Qnetar�۸����� == 0 && Qnetar�������۹�ʽ.equals("") && Qnetar�۸����۹�ʽ.equals("")) {

                            Jiesjgjs_farl_yd(Qnetar������λ, Qnetar�����۵�λ, Qnetar���ۿ�����, ������, Qnetar����, Qnetar����, "ӯ��_Qnetar",
                                    Qnetar���ۿ����, Qnetar���ۿ������λ, Qnetar��������, Qnetar�������۹�ʽ, "����", Qnetar��׼���ۼ�, QnetarС������);
                        }
                    }

                    if (Qnetar������λ.equals("�׽�ǧ��")) {

                        // �û��ۼ�����۸�
                        // ���ۣ�Ԫ/ǧ����
                        //�۸�λ.equals("Ԫ/ǧ��"),������λ(�׽�/ǧ��),����(Ԫ/ǧ��)
                        //�����ۼ۵�λΪ��Ԫ/ǧ������Ҫ���е�λת��

                        //                	������=Mjkg_to_kcalkg(Qnetar�׽�ǧ��,0);//��λת��
                        ������ = Qnetar�׽�ǧ��;

                        if (Qnetar�������� > 0 || !Qnetar�������۹�ʽ.equals("")) {

                            Jiesjgjs_farl_Mjd(Qnetar������λ, Qnetar�����۵�λ, Qnetar���ۿ�����, ������, Qnetar����, Qnetar����, "ӯ��_Qnetar",
                                    Qnetar���ۿ����, Qnetar���ۿ������λ, Qnetar��������, Qnetar�������۹�ʽ, "����", Qnetar��׼���ۼ�, QnetarС������);
                        }

                        if (Qnetar�۸����� > 0 || !Qnetar�۸����۹�ʽ.equals("")) {

                            Jiesjgjs_farl_Mjd(Qnetar������λ, Qnetar�۸��۵�λ, Qnetar���ۿ�����, ������, Qnetar����, Qnetar����, "ӯ��_Qnetar",
                                    Qnetar���ۿ����, Qnetar���ۿ������λ, Qnetar�۸�����, Qnetar�۸����۹�ʽ, "�۸�", Qnetar��׼���ۼ�, QnetarС������);
                        }

                        if (Qnetar�������� == 0 && Qnetar�۸����� == 0 && Qnetar�������۹�ʽ.equals("") && Qnetar�۸����۹�ʽ.equals("")) {

                            Jiesjgjs_farl_Mjd(Qnetar������λ, Qnetar�����۵�λ, Qnetar���ۿ�����, ������, Qnetar����, Qnetar����, "ӯ��_Qnetar",
                                    Qnetar���ۿ����, Qnetar���ۿ������λ, Qnetar��������, Qnetar�������۹�ʽ, "����", Qnetar��׼���ۼ�, QnetarС������);
                        }
                    }
                }
                if (��ͬ��׼_Qnetar.equals("")) {

                    ��ͬ��׼_Qnetar = String.valueOf(Mjkg_to_kcalkg(Qnetar����, 0)) + "-" + String.valueOf(Mjkg_to_kcalkg(Qnetar����, 0));
                }
            }
            if (�Ƽ۷�ʽ.equals("Ŀ¼��")) {

                //Ŀ¼���㷨:	�����=��ֵ����*�ӷ��ݱȼ�*��ֱȼ�*�ҷֱȼ�*1.05(Ʒ�ֱȼ�)+�����ԼӼ�+�Ӽ�;

                //����۸�=��ֵ����*�ӷ��ݱȼ�*��ֱȼ�*�ҷֱȼ�*1.05(Ʒ�ֱȼ�)+�����ԼӼ�+�Ӽ�;

                ��ͬ��׼ = String.valueOf(Qnetar_����) + "-" + String.valueOf(Qnetar_����);

                setEvaluate("ӯ��_Qnetar", 0, ��ͬ��׼, 0);

                ��ͬ��׼ = String.valueOf(Vdaf_����) + "-" + String.valueOf(Vdaf_����);

                setEvaluate("ӯ��_Vdaf", 0, ��ͬ��׼, 0);

                if (Stad_���� > 0 || Stad_���� > 0) {

                    ��ͬ��׼ = String.valueOf(Stad_����) + "-" + String.valueOf(Stad_����);

                    setEvaluate("ӯ��_Stad", 0, ��ͬ��׼, 0);
                } else if (Std_���� > 0 || Std_���� > 0) {

                    ��ͬ��׼ = String.valueOf(Std_����) + "-" + String.valueOf(Std_����);

                    setEvaluate("ӯ��_Std", 0, ��ͬ��׼, 0);
                }

                if (Aad_���� > 0 || Aad_���� > 0) {

                    ��ͬ��׼ = String.valueOf(Aad_����) + "-" + String.valueOf(Aad_����);

                    setEvaluate("ӯ��_Aad", 0, ��ͬ��׼, 0);

                } else if (Aar_���� > 0 || Aar_���� > 0) {

                    ��ͬ��׼ = String.valueOf(Aar_����) + "-" + String.valueOf(Aar_����);

                    setEvaluate("ӯ��_Aar", 0, ��ͬ��׼, 0);
                }


                if (!�û��Զ���Ŀ¼�۹�ʽ.equals("")) {

                    ����۸� = (double)eval(�û��Զ���Ŀ¼�۹�ʽ);
                } else {

                    ����۸� = ��ֵ����_Qnetar * �ӷ��ݱȼ�_Vdaf * ��ֱȼ�_Stad * �ҷֱȼ�_Aar * Ʒ�ֱȼ� + �����ԼӼ� + �Ӽ�;
                }
            }
        }


        //��ֵ�����۸�_end
        //����ָ���ۼ�
        if (Std���� > 0 || Std���� > 0) {

            Initialize();

            if (Std�������� != 0 || !Std�������۹�ʽ.equals("")) {

                Jiesqtzbzj(Std���ۿ�����, Std, Std����, Std����, "ӯ��_Std", Std���ۿ����,
                        Std��������, Std�������۹�ʽ, Std�����۵�λ, "����", Std��׼���ۼ�, StdС������);
            }

            if (Std�۸����� != 0 || !Std�۸����۹�ʽ.equals("")) {

                Jiesqtzbzj(Std���ۿ�����, Std, Std����, Std����, "ӯ��_Std", Std���ۿ����,
                        Std�۸�����, Std�۸����۹�ʽ, Std�۸��۵�λ, "�۸�", Std��׼���ۼ�, StdС������);
            }
        }

        if (Star���� > 0 || Star���� > 0) {

            Initialize();

            if (Star�������� > 0 || !Star�������۹�ʽ.equals("")) {

                Jiesqtzbzj(Star���ۿ�����, Star, Star����, Star����, "ӯ��_Star", Star���ۿ����,
                        Star��������, Star�������۹�ʽ, Star�����۵�λ, "����", Star��׼���ۼ�, StarС������);
            }

            if (Star�۸����� > 0 || !Star�۸����۹�ʽ.equals("")) {

                Jiesqtzbzj(Star���ۿ�����, Star, Star����, Star����, "ӯ��_Star", Star���ۿ����,
                        Star�۸�����, Star�۸����۹�ʽ, Star�۸��۵�λ, "�۸�", Star��׼���ۼ�, StarС������);
            }
        }


        if (Ad���� > 0 || Ad���� > 0) {


            Initialize();

            if (!(Ad�������� == 0) || !Ad�������۹�ʽ.equals("")) {
                Jiesqtzbzj(Ad���ۿ�����, Ad, Ad����, Ad����, "ӯ��_Ad", Ad���ۿ����,
                        Ad��������, Ad�������۹�ʽ, Ad�����۵�λ, "����", Ad��׼���ۼ�, AdС������);
            }

            if (!(Ad�۸����� == 0) || !Ad�۸����۹�ʽ.equals("")) {

                Jiesqtzbzj(Ad���ۿ�����, Ad, Ad����, Ad����, "ӯ��_Ad", Ad���ۿ����,
                        Ad�۸�����, Ad�۸����۹�ʽ, Ad�۸��۵�λ, "�۸�", Ad��׼���ۼ�, AdС������);
            }
        }


        if (Vdaf���� > 0 || Vdaf���� > 0) {

            Initialize();

            if (Vdaf�������� != 0 || !Vdaf�������۹�ʽ.equals("")) {

                Jiesqtzbzj(Vdaf���ۿ�����, Vdaf, Vdaf����, Vdaf����, "ӯ��_Vdaf", Vdaf���ۿ����,
                        Vdaf��������, Vdaf�������۹�ʽ, Vdaf�����۵�λ, "����", Vdaf��׼���ۼ�, VdafС������);
            }

            if (Vdaf�۸����� != 0 || !Vdaf�۸����۹�ʽ.equals("")) {

                Jiesqtzbzj(Vdaf���ۿ�����, Vdaf, Vdaf����, Vdaf����, "ӯ��_Vdaf", Vdaf���ۿ����,
                        Vdaf�۸�����, Vdaf�۸����۹�ʽ, Vdaf�۸��۵�λ, "�۸�", Vdaf��׼���ۼ�, VdafС������);
            }
        }


        if (Mt���� > 0 || Mt���� > 0) {

            Initialize();

            if (Mt�������� > 0 || !Mt�������۹�ʽ.equals("")) {

                Jiesqtzbzj(Mt���ۿ�����, Mt, Mt����, Mt����, "ӯ��_Mt", Mt���ۿ����,
                        Mt��������, Mt�������۹�ʽ, Mt�����۵�λ, "����", Mt��׼���ۼ�, MtС������);
            }

            if (Mt�۸����� > 0 || !Mt�۸����۹�ʽ.equals("")) {
                Jiesqtzbzj(Mt���ۿ�����, Mt, Mt����, Mt����, "ӯ��_Mt", Mt���ۿ����,
                        Mt�۸�����, Mt�۸����۹�ʽ, Mt�۸��۵�λ, "�۸�", Mt��׼���ۼ�, MtС������);
            }
        }


        if (Qgrad���� > 0 || Qgrad���� > 0) {

            Initialize();

            if (Qgrad�������� > 0 || !Qgrad�������۹�ʽ.equals("")) {
                Jiesqtzbzj(Qgrad���ۿ�����, Qgrad�׽�ǧ��, Qgrad����, Qgrad����, "ӯ��_Qgrad", Qgrad���ۿ����,
                        Qgrad��������, Qgrad�������۹�ʽ, Qgrad�����۵�λ, "����", Qgrad��׼���ۼ�, QgradС������);
            }

            if (Qgrad�۸����� > 0 || !Qgrad�۸����۹�ʽ.equals("")) {
                Jiesqtzbzj(Qgrad���ۿ�����, Qgrad�׽�ǧ��, Qgrad����, Qgrad����, "ӯ��_Qgrad", Qgrad���ۿ����,
                        Qgrad�۸�����, Qgrad�۸����۹�ʽ, Qgrad�۸��۵�λ, "�۸�", Qgrad��׼���ۼ�, QgradС������);
            }
        }

        if (Qbad���� > 0 || Qbad���� > 0) {

            Initialize();

            if (Qbad�������� > 0 || !Qbad�������۹�ʽ.equals("")) {
                Jiesqtzbzj(Qbad���ۿ�����, Qbad�׽�ǧ��, Qbad����, Qbad����, "ӯ��_Qbad", Qbad���ۿ����,
                        Qbad��������, Qbad�������۹�ʽ, Qbad�����۵�λ, "����", Qbad��׼���ۼ�, QbadС������);
            }

            if (Qbad�۸����� > 0 || !Qbad�۸����۹�ʽ.equals("")) {
                Jiesqtzbzj(Qbad���ۿ�����, Qbad�׽�ǧ��, Qbad����, Qbad����, "ӯ��_Qbad", Qbad���ۿ����,
                        Qbad�۸�����, Qbad�۸����۹�ʽ, Qbad�۸��۵�λ, "�۸�", Qbad��׼���ۼ�, QbadС������);
            }
        }

        if (Had���� > 0 || Had���� > 0) {

            Initialize();

            if (Had�������� > 0 || !Had�������۹�ʽ.equals("")) {

                Jiesqtzbzj(Had���ۿ�����, Had, Had����, Had����, "ӯ��_Had", Had���ۿ����,
                        Had��������, Had�������۹�ʽ, Had�����۵�λ, "����", Had��׼���ۼ�, HadС������);
            }

            if (Had�۸����� > 0 || !Had�۸����۹�ʽ.equals("")) {

                Jiesqtzbzj(Had���ۿ�����, Had, Had����, Had����, "ӯ��_Had", Had���ۿ����,
                        Had�۸�����, Had�۸����۹�ʽ, Had�۸��۵�λ, "�۸�", Had��׼���ۼ�, HadС������);
            }

        }

        if (Stad���� > 0 || Stad���� > 0) {

            Initialize();

            if (Stad�������� > 0 || !Stad�������۹�ʽ.equals("")) {

                Jiesqtzbzj(Stad���ۿ�����, Stad, Stad����, Stad����, "ӯ��_Stad", Stad���ۿ����,
                        Stad��������, Stad�������۹�ʽ, Stad�����۵�λ, "����", Stad��׼���ۼ�, StadС������);
            }

            if (Stad�۸����� > 0 || !Stad�۸����۹�ʽ.equals("")) {

                Jiesqtzbzj(Stad���ۿ�����, Stad, Stad����, Stad����, "ӯ��_Stad", Stad���ۿ����,
                        Stad�۸�����, Stad�۸����۹�ʽ, Stad�۸��۵�λ, "�۸�", Stad��׼���ۼ�, StadС������);
            }
        }

        if (Mad���� > 0 || Mad���� > 0) {

            Initialize();

            if (Mad�������� > 0 || !Mad�������۹�ʽ.equals("")) {

                Jiesqtzbzj(Mad���ۿ�����, Mad, Mad����, Mad����, "ӯ��_Mad", Mad���ۿ����,
                        Mad��������, Mad�������۹�ʽ, Mad�����۵�λ, "����", Mad��׼���ۼ�, MadС������);
            }

            if (Mad�۸����� > 0 || !Mad�۸����۹�ʽ.equals("")) {

                Jiesqtzbzj(Mad���ۿ�����, Mad, Mad����, Mad����, "ӯ��_Mad", Mad���ۿ����,
                        Mad�۸�����, Mad�۸����۹�ʽ, Mad�۸��۵�λ, "�۸�", Mad��׼���ۼ�, MadС������);
            }

        }


        if (Aar���� > 0 || Aar���� > 0) {

            Initialize();

            if (Aar�������� != 0 || !Aar�������۹�ʽ.equals("")) {

                Jiesqtzbzj(Aar���ۿ�����, Aar, Aar����, Aar����, "ӯ��_Aar", Aar���ۿ����,
                        Aar��������, Aar�������۹�ʽ, Aar�����۵�λ, "����", Aar��׼���ۼ�, AarС������);
            }

            if (Aar�۸����� != 0 || !Aar�۸����۹�ʽ.equals("")) {

                Jiesqtzbzj(Aar���ۿ�����, Aar, Aar����, Aar����, "ӯ��_Aar", Aar���ۿ����,
                        Aar�۸�����, Aar�۸����۹�ʽ, Aar�۸��۵�λ, "�۸�", Aar��׼���ۼ�, AarС������);
            }
        }

        if (T2���� > 0 || T2���� > 0) {

            Initialize();

            if (T2�������� > 0 || !T2�������۹�ʽ.equals("")) {

                Jiesqtzbzj(T2���ۿ�����, T2, T2����, T2����, "ӯ��_T2", T2���ۿ����,
                        T2��������, T2�������۹�ʽ, T2�����۵�λ, "����", T2��׼���ۼ�, T2С������);
            }

            if (T2�۸����� > 0 || !T2�۸����۹�ʽ.equals("")) {

                Jiesqtzbzj(T2���ۿ�����, T2, T2����, T2����, "ӯ��_T2", T2���ۿ����,
                        T2�۸�����, T2�۸����۹�ʽ, T2�۸��۵�λ, "�۸�", T2��׼���ۼ�, T2С������);
            }
        }

        if (Aad���� > 0 || Aad���� > 0) {

            Initialize();

            if (Aad�������� > 0 || !Aad�������۹�ʽ.equals("")) {

                Jiesqtzbzj(Aad���ۿ�����, Aad, Aad����, Aad����, "ӯ��_Aad", Aad���ۿ����,
                        Aad��������, Aad�������۹�ʽ, Aad�����۵�λ, "����", Aad��׼���ۼ�, AadС������);
            }

            if (Aad�۸����� > 0 || !Aad�۸����۹�ʽ.equals("")) {

                Jiesqtzbzj(Aad���ۿ�����, Aad, Aad����, Aad����, "ӯ��_Aad", Aad���ۿ����,
                        Aad�۸�����, Aad�۸����۹�ʽ, Aad�۸��۵�λ, "�۸�", Aad��׼���ۼ�, AadС������);
            }

        }

        if (Vad���� > 0 || Vad���� > 0) {

            Initialize();

            if (Vad�������� > 0 || !Vad�������۹�ʽ.equals("")) {

                Jiesqtzbzj(Vad���ۿ�����, Vad, Vad����, Vad����, "ӯ��_Vad", Vad���ۿ����,
                        Vad��������, Vad�������۹�ʽ, Vad�����۵�λ, "����", Vad��׼���ۼ�, VadС������);
            }

            if (Vad�۸����� > 0 || !Vad�۸����۹�ʽ.equals("")) {
                Jiesqtzbzj(Vad���ۿ�����, Vad, Vad����, Vad����, "ӯ��_Vad", Vad���ۿ����,
                        Vad�۸�����, Vad�۸����۹�ʽ, Vad�۸��۵�λ, "�۸�", Vad��׼���ۼ�, VadС������);
            }


        }

        if (�˾����� > 0 || �˾����� > 0) {

            Initialize();

            if (�˾��������� > 0 || !�˾��������۹�ʽ.equals("")) {

                Jiesqtzbzj(�˾����ۿ�����, �˾�, �˾�����, �˾�����, "ӯ��_�˾�", �˾����ۿ����,
                        �˾���������, �˾��������۹�ʽ, �˾������۵�λ, "����", �˾��׼���ۼ�, �˾�С������);
            }

            if (�˾�۸����� > 0 || !�˾�۸����۹�ʽ.equals("")) {

                Jiesqtzbzj(�˾����ۿ�����, �˾�, �˾�����, �˾�����, "ӯ��_�˾�", �˾����ۿ����,
                        �˾�۸�����, �˾�۸����۹�ʽ, �˾�۸��۵�λ, "�۸�", �˾��׼���ۼ�, �˾�С������);
            }

        }

        if (������������ > 0 || ������������ > 0) {

            Initialize();

            if (��������������λ.equals("��")) {

                �������� = ����������;
            } else if (��������������λ.equals("���")) {

                �������� = �����������;
            }

            if (���������������� > 0 || !���������������۹�ʽ.equals("")) {

                Jiesslzj(��������������λ, �������������۵�λ, �����������ۿ�����, ��������, ������������, ������������, "ӯ��_��������",
                        �����������ۿ����, �����������ۿ������λ, ����������������, ���������������۹�ʽ, "����", ����������׼���ۼ�, ��������С������);
            }

            if (���������۸����� > 0 || !���������۸����۹�ʽ.equals("")) {

                Jiesslzj(��������������λ, ���������۸��۵�λ, �����������ۿ�����, ��������, ������������, ������������, "ӯ��_��������",
                        �����������ۿ����, �����������ۿ������λ, ���������۸�����, ���������۸����۹�ʽ, "�۸�", ����������׼���ۼ�, ��������С������);
            }
        }

        //ȡ������
        ����۸� = Quzcz(ú�˰����ȡ����ʽ, (����۸� + �۵���_Std + �۵���_Star + �۵���_Ad + �۵���_Vdaf + �۵���_Mt + �۵���_Qgrad + �۵���_Qbad + �۵���_Had + �۵���_Stad + �۵���_Mad
                + �۵���_Aar + �۵���_Aad + �۵���_Vad + �۵���_T2 + �۵���_�������� + �۵���_�˾�), ú�˰���۱���С��λ);

        if (���ú�� > 0) {

            if (����۸� > ���ú��) {

                ����۸� = ���ú��;
            }
        }

    }

}