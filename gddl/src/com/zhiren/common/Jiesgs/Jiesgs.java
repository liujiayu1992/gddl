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

    double 结算价格 = 0.0;
    double 发热量 = 0;
    double 指标折单价 = 0;
    double 盈亏 = 0;
    String 合同标准 = "";
    double 结算数量 = 0;
    double 结算数量_发热量 = 0;

    double 盈亏_结算数量 = 0;
    double 盈亏_Qnetar = 0;
    double 盈亏_Std = 0;
    double 盈亏_Ad = 0;
    double 盈亏_Vdaf = 0;
    double 盈亏_Mt = 0;
    double 盈亏_Qgrad = 0;
    double 盈亏_Qbad = 0;
    double 盈亏_Had = 0;
    double 盈亏_Stad = 0;
    double 盈亏_Mad = 0;
    double 盈亏_Aar = 0;
    double 盈亏_Aad = 0;
    double 盈亏_Vad = 0;
    double 盈亏_T2 = 0;
    double 盈亏_运距 = 0;
    double 盈亏_Star = 0;

    double 折单价_结算数量 = 0;
    double 折单价_Qnetar = 0;
    double 折单价_Std = 0;
    double 折单价_Ad = 0;
    double 折单价_Vdaf = 0;
    double 折单价_Mt = 0;
    double 折单价_Qgrad = 0;
    double 折单价_Qbad = 0;
    double 折单价_Had = 0;
    double 折单价_Stad = 0;
    double 折单价_Mad = 0;
    double 折单价_Aar = 0;
    double 折单价_Aad = 0;
    double 折单价_Vad = 0;
    double 折单价_T2 = 0;
    double 折单价_运距 = 0;
    double 折单价_Star = 0;

    // 处理折结算量的问题_begin

    double 折数量_结算数量 = 0;
    double 折数量_Qnetar = 0;
    double 折数量_Std = 0;
    double 折数量_Ad = 0;
    double 折数量_Vdaf = 0;
    double 折数量_Mt = 0;
    double 折数量_Qgrad = 0;
    double 折数量_Qbad = 0;
    double 折数量_Had = 0;
    double 折数量_Stad = 0;
    double 折数量_Mad = 0;
    double 折数量_Aar = 0;
    double 折数量_Aad = 0;
    double 折数量_Vad = 0;
    double 折数量_T2 = 0;
    double 折数量_运距 = 0;
    double 折数量_Star = 0;

    String 折数量单位_结算数量 = "";
    String 折数量单位_Qnetar = "";
    String 折数量单位_Std = "";
    String 折数量单位_Ad = "";
    String 折数量单位_Vdaf = "";
    String 折数量单位_Mt = "";
    String 折数量单位_Qgrad = "";
    String 折数量单位_Qbad = "";
    String 折数量单位_Had = "";
    String 折数量单位_Stad = "";
    String 折数量单位_Mad = "";
    String 折数量单位_Aar = "";
    String 折数量单位_Aad = "";
    String 折数量单位_Vad = "";
    String 折数量单位_T2 = "";
    String 折数量单位_运距 = "";
    String 折数量单位_Star = "";

    // 处理折结算量的问题_end

    String 合同标准_结算数量 = "";
    String 合同标准_Qnetar = "";
    String 合同标准_Std = "";
    String 合同标准_Ad = "";
    String 合同标准_Vdaf = "";
    String 合同标准_Mt = "";
    String 合同标准_Qgrad = "";
    String 合同标准_Qbad = "";
    String 合同标准_Had = "";
    String 合同标准_Stad = "";
    String 合同标准_Mad = "";
    String 合同标准_Aar = "";
    String 合同标准_Aad = "";
    String 合同标准_Vad = "";
    String 合同标准_T2 = "";
    String 合同标准_运距 = "";
    String 合同标准_Star = "";

    double Dblvalue = 0;    //临时变量
    private int 煤款含税单价保留小数位;
    private String Zengkjdw;
    private String Qnetar增付单价公式;
    private String Qnetar_上限;

    private String Qnetar基价类型;
    private String T2基价类型;
    private String Star基价类型;
    private String Ad基价类型;
    private String Aad基价类型;
    private String Vdaf基价类型;
    private String Mt基价类型;
    private String Qgrad基价类型;
    private String Std基价类型;
    private String Aar基价类型;
    private String Mad基价类型;
    private String Stad基价类型;
    private String Had基价类型;
    private String Vad基价类型;
    private String 运距基价类型;



    private String T2增扣款基数单位;
    private String Star增扣款基数单位;
    private String Ad增扣款基数单位;
    private String Aad增扣款基数单位;
    private String Vdaf增扣款基数单位;
    private String Mt增扣款基数单位;
    private String Qgrad增扣款基数单位;
    private String Std增扣款基数单位;
    private String Aar增扣款基数单位;
    private String Mad增扣款基数单位;
    private String Stad增扣款基数单位;
    private String Had增扣款基数单位;
    private String Vad增扣款基数单位;
    private String 运距增扣款基数单位;


    private String 用户自定义目录价公式;

    private String 结算形式;
    private String 计价方式;
    private String 价格单位;
    private double Qnetar下限;
    private double  Qnetar上限;
    private double 发热量折价;
    private double  Qnetar增付单价;
    private String  Qnetar增付价单位;
    private String Qnetar增扣款条件;
    private double Qnetar增扣款基数;
    private String Qnetar增扣款基数单位;
    private double Qnetar基准增扣价;
    private String Qnetar小数处理;
    private double Qnetar扣付单价;
    private String Qnetar扣付单价公式;
    private String Qnetar扣付价单位;
    private String Qnetar_下限;

    private double 热值基价_Qnetar ;
    private double 挥发份比价_Vdaf ;
    private double 硫分比价_Stad ;
    private double 灰分比价_Aar ;
    private double 品种比价 ;
    private double 政策性加价 ;
    private double 加价;
    private String Qnetar强制计算;

    private String T2增扣款条件;
    private double T2;
    private double T2增扣款基数;
    private String T2增付价单位;
    private double T2基准增扣价;
    private String T2小数处理;
    private String T2扣付价单位;
    private double T2上限;
    private double T2下限;
    private double T2增付单价;
    private String T2增付单价公式;
    private double T2扣付单价;
    private String T2扣付单价公式;
    private double T2_下限;
    private double T2_上限;
    private String T2强制计算;
    private String Star增扣款条件;
    private double Star;
    private double Star增扣款基数;
    private String Star增付价单位;
    private double Star基准增扣价;
    private String Star小数处理;
    private String Star扣付价单位;
    private double Star上限;
    private double Star下限;
    private double Star增付单价;
    private String Star增付单价公式;
    private double Star扣付单价;
    private String Star扣付单价公式;
    private double Star_下限;
    private double Star_上限;
    private String Star强制计算;
    private String Ad增扣款条件;
    private double Ad;
    private double Ad增扣款基数;
    private String Ad增付价单位;
    private double Ad基准增扣价;
    private String Ad小数处理;
    private String Ad扣付价单位;
    private double Ad上限;
    private double Ad下限;
    private double Ad增付单价;
    private String Ad增付单价公式;
    private double Ad扣付单价;
    private String Ad扣付单价公式;
    private double Ad_下限;
    private double Ad_上限;
    private String Ad强制计算;
    private String Aad增扣款条件;
    private double Aad;
    private double Aad增扣款基数;
    private String Aad增付价单位;
    private double Aad基准增扣价;
    private String Aad小数处理;
    private String Aad扣付价单位;
    private double Aad上限;
    private double Aad下限;
    private double Aad增付单价;
    private String Aad增付单价公式;
    private double Aad扣付单价;
    private String Aad扣付单价公式;
    private double Aad_下限;
    private double Aad_上限;
    private String Aad强制计算;
    private String Vdaf增扣款条件;
    private double Vdaf;
    private double Vdaf增扣款基数;
    private String Vdaf增付价单位;
    private double Vdaf基准增扣价;
    private String Vdaf小数处理;
    private String Vdaf扣付价单位;
    private double Vdaf上限;
    private double Vdaf下限;
    private double Vdaf增付单价;
    private String Vdaf增付单价公式;
    private double Vdaf扣付单价;
    private String Vdaf扣付单价公式;
    private double Vdaf_下限;
    private double Vdaf_上限;
    private String Vdaf强制计算;
    private String Mt增扣款条件;
    private double Mt;
    private double Mt增扣款基数;
    private String Mt增付价单位;
    private double Mt基准增扣价;
    private String Mt小数处理;
    private String Mt扣付价单位;
    private double Mt上限;
    private double Mt下限;
    private double Mt增付单价;
    private String Mt增付单价公式;
    private double Mt扣付单价;
    private String Mt扣付单价公式;
    private double Mt_下限;
    private double Mt_上限;
    private String Mt强制计算;
    private String Qgrad增扣款条件;
    private double Qgrad;
    private double Qgrad增扣款基数;
    private String Qgrad增付价单位;
    private double Qgrad基准增扣价;
    private String Qgrad小数处理;
    private String Qgrad扣付价单位;
    private double Qgrad上限;
    private double Qgrad下限;
    private double Qgrad增付单价;
    private String Qgrad增付单价公式;
    private double Qgrad扣付单价;
    private String Qgrad扣付单价公式;
    private double Qgrad_下限;
    private double Qgrad_上限;
    private String Qgrad强制计算;
    private String Std增扣款条件;
    private double Std;
    private double Std增扣款基数;
    private String Std增付价单位;
    private double Std基准增扣价;
    private String Std小数处理;
    private String Std扣付价单位;
    private double Std上限;
    private double Std下限;
    private double Std增付单价;
    private String Std增付单价公式;
    private double Std扣付单价;
    private String Std扣付单价公式;
    private double Std_下限;
    private double Std_上限;
    private String Std强制计算;
    private String Aar增扣款条件;
    private double Aar;
    private double Aar增扣款基数;
    private String Aar增付价单位;
    private double Aar基准增扣价;
    private String Aar小数处理;
    private String Aar扣付价单位;
    private double Aar上限;
    private double Aar下限;
    private double Aar增付单价;
    private String Aar增付单价公式;
    private double Aar扣付单价;
    private String Aar扣付单价公式;
    private double Aar_下限;
    private double Aar_上限;
    private String Aar强制计算;
    private String Mad增扣款条件;
    private double Mad;
    private double Mad增扣款基数;
    private String Mad增付价单位;
    private double Mad基准增扣价;
    private String Mad小数处理;
    private String Mad扣付价单位;
    private double Mad上限;
    private double Mad下限;
    private double Mad增付单价;
    private String Mad增付单价公式;
    private double Mad扣付单价;
    private String Mad扣付单价公式;
    private double Mad_下限;
    private double Mad_上限;
    private String Mad强制计算;

    private String Stad增扣款条件;
    private double Stad;
    private double Stad增扣款基数;
    private String Stad增付价单位;
    private double Stad基准增扣价;
    private String Stad小数处理;
    private String Stad扣付价单位;
    private double Stad上限;
    private double Stad下限;
    private double Stad增付单价;
    private String Stad增付单价公式;
    private double Stad扣付单价;
    private String Stad扣付单价公式;
    private double Stad_下限;
    private double Stad_上限;
    private String Stad强制计算;

    private String Had增扣款条件;
    private double Had;
    private double Had增扣款基数;
    private String Had增付价单位;
    private double Had基准增扣价;
    private String Had小数处理;
    private String Had扣付价单位;
    private double Had上限;
    private double Had下限;
    private double Had增付单价;
    private String Had增付单价公式;
    private double Had扣付单价;
    private String Had扣付单价公式;
    private double Had_下限;
    private double Had_上限;
    private String Had强制计算;
    private String Qbad增扣款条件;
    private double Qbad;
    private double Qbad增扣款基数;
    private String Qbad增付价单位;
    private double Qbad基准增扣价;
    private String Qbad小数处理;
    private String Qbad扣付价单位;
    private double Qbad上限;
    private double Qbad下限;
    private double Qbad增付单价;
    private String Qbad增付单价公式;
    private double Qbad扣付单价;
    private String Qbad扣付单价公式;
    private double Qbad_下限;
    private double Qbad_上限;
    private String Qbad强制计算;

    private String Vad增扣款条件;
    private double Vad;
    private double Vad增扣款基数;
    private String Vad增付价单位;
    private double Vad基准增扣价;
    private String Vad小数处理;
    private String Vad扣付价单位;
    private double Vad上限;
    private double Vad下限;
    private double Vad增付单价;
    private String Vad增付单价公式;
    private double Vad扣付单价;
    private String Vad扣付单价公式;
    private double Vad_下限;
    private double Vad_上限;
    private String Vad强制计算;

    private String 运距增扣款条件;
    private double 运距;
    private double 运距增扣款基数;
    private String 运距增付价单位;
    private double 运距基准增扣价;
    private String 运距小数处理;
    private String 运距扣付价单位;
    private double 运距上限;
    private double 运距下限;
    private double 运距增付单价;
    private String 运距增付单价公式;
    private double 运距扣付单价;
    private String 运距扣付单价公式;
    private double 运距_下限;
    private double 运距_上限;
    private String 运距强制计算;

    private String 结算数量增扣款条件;
    private double 结算数量增扣款基数;
    private String 结算数量增付价单位;
    private double 结算数量基准增扣价;
    private String 结算数量小数处理;
    private String 结算数量扣付价单位;
    private double 结算数量上限;
    private double 结算数量下限;
    private double 结算数量增付单价;
    private String 结算数量增付单价公式;
    private double 结算数量扣付单价;
    private String 结算数量扣付单价公式;
    private double 结算数量_下限;
    private double 结算数量_上限;
    private double Qgrad兆焦千克;
    private double Qbad兆焦千克;
    private String 结算数量计量单位;
    private double 结算数量吨;
    private double 结算数量万吨;
    private String 结算数量增扣款基数单位;
    private String 煤款含税单价取整方式;
    private double 最高煤价;
    private String 合同价格公式;
    void Initialize() {
        //初始化方法
        指标折单价 = 0;
        盈亏 = 0;
        合同标准 = "";
    }

    double Mjkg_to_kcalkg(double value, int xiaosw) {
        //		兆焦/千克转化为千卡/千克
        Dblvalue = 0;
        if (value < 100) {

            Dblvalue = Round_new(value * 1000 / 4.1816, xiaosw);
        } else {

            Dblvalue = value;
        }
        return Dblvalue;
    }

    double Mjkg_to_kcalkg(double value, int xiaosw, String xiaosclfs) {
        //		兆焦/千克转化为千卡/千克,带小数处理参数
        double Dblvalue = 0;
        if (Math.abs(value) < 100) {

            if (xiaosclfs.equals("四舍五入")) {
                //				四舍五入
                Dblvalue = CustomMaths.Round_new(value * 1000 / 4.1816, xiaosw);
            } else if (xiaosclfs.equals("舍去")) {
                //				舍去
                Dblvalue = Math.floor(Math.abs(value) * 1000 / 4.1816);
            } else if (xiaosclfs.equals("进位")) {
                //				进位
                Dblvalue = Math.floor(Math.abs(value) * 1000 / 4.1816) + 1;
            }

        } else {

            Dblvalue = value;
        }
        return Dblvalue;
    }

    double Kcalkg_to_mjkg(double value, int xiaosw) {
        //		千卡/千克转化为兆焦
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
        Field field = null;//name为类Instance中的private属性
        try {
            field = c.getDeclaredField(key);
            field.setAccessible(true);//关键。。。可访问私有变量。
            Class<?>  typeClass = field.getType();
            Constructor<?>  con = typeClass.getConstructor(typeClass);
            if(value==null)value="";
            Object v = con.newInstance(value);//要赋的值
            field.set(this, v);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void set(String key, String value) {
        Class c = this.getClass();
//        Instance instance = new Instance();
        Field field = null;//name为类Instance中的private属性
        try {
            field = c.getDeclaredField(key);
            field.setAccessible(true);//关键。。。可访问私有变量。
//            Class<?>  typeClass = field.getType();
//            Constructor<?>  con = typeClass.getConstructor(typeClass);
//            if(value==null)value="";
//            Object v = con.newInstance(value);//要赋的值
            field.set(this, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void set(String key, int value) {
        Class c = this.getClass();
//        Instance instance = new Instance();
        Field field = null;//name为类Instance中的private属性
        try {
            field = c.getDeclaredField(key);
            field.setAccessible(true);//关键。。。可访问私有变量。
//            Class<?>  typeClass = field.getType();
//            Constructor<?>  con = typeClass.getConstructor(typeClass);
//            Object v = con.newInstance(value);//要赋的值
            field.set(this, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set(String key, double value) {
        Class c = this.getClass();
//        Instance instance = new Instance();
        Field field = null;//name为类Instance中的private属性
        try {
            field = c.getDeclaredField(key);
            field.setAccessible(true);//关键。。。可访问私有变量。
//            Class<?>  typeClass = field.getType();
//            Constructor<?>  con = typeClass.getConstructor(typeClass);
//            Object v = con.newInstance(value);//要赋的值
            field.set(this, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Object get(String key) {
        Class c = this.getClass();
//        Instance instance = new Instance();
        Field field = null;//name为类Instance中的private属性
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
        //			处理煤款含税单价保留小数位的问题
        //			参数：取整方式、要处理的值、保留的小数位
        Dblvalue = 0;
        StrValue = "";
        StrQz = "1";    //权重
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

        switch (Quzfs) {        //判断处理条件

            case "":
                Dblvalue = value;
                break;

            case "进位":
                Dblvalue = Math.floor(CustomMaths.mul(Math.abs(value), Double.parseDouble(StrQz))) + 1;
                Dblvalue = CustomMaths.div(Dblvalue, Double.parseDouble(StrQz));
                if (value < 0) {

                    Dblvalue = 0 - Dblvalue;
                }
                break;

            case "舍去":
                Dblvalue = Math.floor(CustomMaths.mul(Math.abs(value), Double.parseDouble(StrQz)));
                Dblvalue = CustomMaths.div(Dblvalue, Double.parseDouble(StrQz));
                if (value < 0) {

                    Dblvalue = 0 - Dblvalue;
                }
                break;

            case "四舍五入":
                Dblvalue = Round_new(Math.abs(value), xiaosw);
                if (value < 0) {

                    Dblvalue = 0 - Dblvalue;
                }
                break;
        }

        return Dblvalue;

    }

    double Xiaoscl(double value, String chultj) {
        //		小数处理：完成四舍五入、进位、舍去的操作
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

        switch (chultj) {        //判断处理条件

            case "":
                Dblvalue = value;
                break;

            case "进位":
                Dblvalue = Math.floor(Math.abs(value)) + 1;
                if (value < 0) {

                    Dblvalue = 0 - Dblvalue;
                }
                break;

            case "舍去":
                Dblvalue = Math.floor(Math.abs(value));
                if (value < 0) {

                    Dblvalue = 0 - Dblvalue;
                }
                break;

            case "四舍五入":
                Dblvalue = Round_new(Math.abs(value), 0);
                if (value < 0) {

                    Dblvalue = 0 - Dblvalue;
                }
                break;

            case "四舍五入一位":
                Dblvalue = Round_new(Math.abs(value), 1);
                if (value < 0) {

                    Dblvalue = 0 - Dblvalue;
                }
                break;

            case "四舍五入两位":
                Dblvalue = Round_new(Math.abs(value), 2);
                if (value < 0) {

                    Dblvalue = 0 - Dblvalue;
                }
                break;

            case "四舍五入三位":
                Dblvalue = Round_new(Math.abs(value), 3);
                if (value < 0) {

                    Dblvalue = 0 - Dblvalue;
                }
                break;

            case "四舍五入四位":
                Dblvalue = Round_new(Math.abs(value), 4);
                if (value < 0) {

                    Dblvalue = 0 - Dblvalue;
                }
                break;
        }

        return Dblvalue;
    }


    double Round_new(double value, int _bit) {
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

    void setEvaluate(String Zhibyk, double Yingk, String Hetbz, double Zhedj) {
        //参数：指标盈亏(赋值用)，盈亏，合同标准，指标折单价
        //进行参数间赋值

        switch (Zhibyk) {

            case "盈亏_Qnetar":

                盈亏_Qnetar = Yingk;
                折单价_Qnetar = Zhedj;
                合同标准_Qnetar = Hetbz;
                break;

            case "盈亏_Std":

                盈亏_Std = Yingk;
                折单价_Std = Zhedj;
                合同标准_Std = Hetbz;
                break;

            case "盈亏_Ad":

                盈亏_Ad = Yingk;
                折单价_Ad = Zhedj;
                合同标准_Ad = Hetbz;
                break;

            case "盈亏_Vdaf":

                盈亏_Vdaf = Yingk;
                折单价_Vdaf = Zhedj;
                合同标准_Vdaf = Hetbz;
                break;

            case "盈亏_Mt":

                盈亏_Mt = Yingk;
                折单价_Mt = Zhedj;
                合同标准_Mt = Hetbz;
                break;

            case "盈亏_Qgrad":

                盈亏_Qgrad = Yingk;
                折单价_Qgrad = Zhedj;
                合同标准_Qgrad = Hetbz;
                break;

            case "盈亏_Qbad":

                盈亏_Qbad = Yingk;
                折单价_Qbad = Zhedj;
                合同标准_Qbad = Hetbz;
                break;

            case "盈亏_Had":

                盈亏_Had = Yingk;
                折单价_Had = Zhedj;
                合同标准_Had = Hetbz;
                break;

            case "盈亏_Stad":

                盈亏_Stad = Yingk;
                折单价_Stad = Zhedj;
                合同标准_Stad = Hetbz;
                break;

            case "盈亏_Mad":

                盈亏_Mad = Yingk;
                折单价_Mad = Zhedj;
                合同标准_Mad = Hetbz;
                break;

            case "盈亏_Aar":

                盈亏_Aar = Yingk;
                折单价_Aar = Zhedj;
                合同标准_Aar = Hetbz;
                break;

            case "盈亏_Aad":

                盈亏_Aad = Yingk;
                折单价_Aad = Zhedj;
                合同标准_Aad = Hetbz;
                break;

            case "盈亏_Vad":

                盈亏_Vad = Yingk;
                折单价_Vad = Zhedj;
                合同标准_Vad = Hetbz;
                break;

            case "盈亏_T2":

                盈亏_T2 = Yingk;
                折单价_T2 = Zhedj;
                合同标准_T2 = Hetbz;
                break;

            case "盈亏_结算数量":

                盈亏_结算数量 = Yingk;
                折单价_结算数量 = Zhedj;
                合同标准_结算数量 = Hetbz;
                break;

            case "盈亏_运距":

                盈亏_运距 = Yingk;
                折单价_运距 = Zhedj;
                合同标准_运距 = Hetbz;
                break;

            case "盈亏_Star":

                盈亏_Star = Yingk;
                折单价_Star = Zhedj;
                合同标准_Star = Hetbz;
                break;
        }
    }

    //为数量增扣款赋值
    void setEvaluate_Jiessl(String Zhibyk, String Hetbz, double Yingk, double Zhedj, String Zhejdw) {
        //参数：指标盈亏，指标折单价（吨），合同标准，折价单位
        //进行参数间赋值

        switch (Zhibyk) {

            case "盈亏_Qnetar":

                盈亏_Qnetar = Yingk;
                折数量_Qnetar = Zhedj;
                折数量单位_Qnetar = Zhejdw;
                合同标准_Qnetar = Hetbz;
                break;

            case "盈亏_Std":

                盈亏_Std = Yingk;
                折数量_Std = Zhedj;
                折数量单位_Std = Zhejdw;
                合同标准_Std = Hetbz;
                break;

            case "盈亏_Ad":

                盈亏_Ad = Yingk;
                折数量_Ad = Zhedj;
                折数量单位_Ad = Zhejdw;
                合同标准_Ad = Hetbz;
                break;

            case "盈亏_Vdaf":

                盈亏_Vdaf = Yingk;
                折数量_Vdaf = Zhedj;
                折数量单位_Vdaf = Zhejdw;
                合同标准_Vdaf = Hetbz;
                break;

            case "盈亏_Mt":

                盈亏_Mt = Yingk;
                折数量_Mt = Zhedj;
                折数量单位_Mt = Zhejdw;
                合同标准_Mt = Hetbz;
                break;

            case "盈亏_Qgrad":

                盈亏_Qgrad = Yingk;
                折数量_Qgrad = Zhedj;
                折数量单位_Qgrad = Zhejdw;
                合同标准_Qgrad = Hetbz;
                break;

            case "盈亏_Qbad":

                盈亏_Qbad = Yingk;
                折数量_Qbad = Zhedj;
                折数量单位_Qbad = Zhejdw;
                合同标准_Qbad = Hetbz;
                break;

            case "盈亏_Had":

                盈亏_Had = Yingk;
                折数量_Had = Zhedj;
                折数量单位_Had = Zhejdw;
                合同标准_Had = Hetbz;
                break;

            case "盈亏_Stad":

                盈亏_Stad = Yingk;
                折数量_Stad = Zhedj;
                折数量单位_Stad = Zhejdw;
                合同标准_Stad = Hetbz;
                break;

            case "盈亏_Mad":

                盈亏_Mad = Yingk;
                折数量_Mad = Zhedj;
                折数量单位_Mad = Zhejdw;
                合同标准_Mad = Hetbz;
                break;

            case "盈亏_Aar":

                盈亏_Aar = Yingk;
                折数量_Aar = Zhedj;
                折数量单位_Aar = Zhejdw;
                合同标准_Aar = Hetbz;
                break;

            case "盈亏_Aad":

                盈亏_Aad = Yingk;
                折数量_Aad = Zhedj;
                折数量单位_Aad = Zhejdw;
                合同标准_Aad = Hetbz;
                break;

            case "盈亏_Vad":

                盈亏_Vad = Yingk;
                折数量_Vad = Zhedj;
                折数量单位_Vad = Zhejdw;
                合同标准_Vad = Hetbz;
                break;

            case "盈亏_T2":

                盈亏_T2 = Yingk;
                折数量_T2 = Zhedj;
                折数量单位_T2 = Zhejdw;
                合同标准_T2 = Hetbz;
                break;

            case "盈亏_结算数量":

                盈亏_结算数量 = Yingk;
                折数量_结算数量 = Zhedj;
                折数量单位_结算数量 = Zhejdw;
                合同标准_结算数量 = Hetbz;
                break;

            case "盈亏_运距":

                盈亏_运距 = Yingk;
                折数量_运距 = Zhedj;
                折数量单位_运距 = Zhejdw;
                合同标准_运距 = Hetbz;
                break;

            case "盈亏_Star":

                盈亏_Star = Yingk;
                折数量_Star = Zhedj;
                折数量单位_Star = Zhejdw;
                合同标准_Star = Hetbz;
                break;
        }
    }
    private int 煤款增扣款保留小数位;
    private double 合同价格;
    //根据发热量计算结算价格(价格单位:"元/千卡")
    void Jiesjgjs_farl_yk(String Jildw, String Zengkkdw, String Zengkktj, double Zhibvalue, double Hetsx, double Hetxx, String Zhibyk,
                          double Zengkkjs, String Zengkkjsdw, double Zengkkdj, String Zengkkdjgs, String Zengkklx, double Zengkkjzzkj, String Zengkkxscl) {

        //结算价格计算
        //参数：计量单位，增扣款单位，增扣款条件，指标值，合同上限，合同下限，指标盈亏(赋值用)，增扣款基数，增扣款单价,增扣款类型（增付、扣付）

        if (Zengkktj.equals("大于等于") || Zengkktj.equals("大于")) {

            盈亏 = Round_new((Zhibvalue - Hetxx), 2);
            合同标准 = String.valueOf(Hetxx);
        } else if (Zengkktj.equals("小于等于") || Zengkktj.equals("小于")) {

            盈亏 = Round_new((Zhibvalue - Hetsx), 2);
            合同标准 = String.valueOf(Hetsx);
        } else if (Zengkktj.equals("区间")) {

            if (Zengkklx.equals("增付")) {

                盈亏 = Round_new((Zhibvalue - Hetxx), 2);
            } else if (Zengkklx.equals("扣付")) {

                盈亏 = Round_new((Zhibvalue - Hetsx), 2);
            }
            合同标准 = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
        }

        if (!Zengkkdjgs.equals("")) {
            //存在公式
            //如果增扣款存在公式，那么用公式计算的值重新给“增扣款单价”进行赋值
                Zengkkdj = (double)eval(Zengkkdjgs);
        }


        if (Zengkkdw.equals("元/千卡")) {

            if (Zengkkdj > 0) {

                if (Jildw.equals("兆焦千克")) {
                    //                 			如果“计量单位”是兆焦千克，则要进行单位转换
                    盈亏 = Mjkg_to_kcalkg(盈亏, 0);
                    Zhibvalue = Mjkg_to_kcalkg(Zhibvalue, 0);
                }

                if (Zengkkjsdw.equals("兆焦千克")) {
                    //                 			如果“基数单位”是兆焦千克，则要进行单位转换
                    Zengkkjs = Mjkg_to_kcalkg(Zengkkjs, 0);
                }

                if (Zengkklx.equals("增付")) {

                    //符合增款条件的，即增款条件，类似“大于，小于”的，按照“元/千卡”执行增付
                    //算出热量折单价，单位是：分
                    if (Zengkkjs > 0) {

                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);


                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }

                } else if (Zengkklx.equals("扣付")) {

                    if (Zengkkjs > 0) {


                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);

                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(-Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }
                }

                结算价格 = 合同价格 + 指标折单价;
                结算价格 = Round_new(结算价格 * Zhibvalue, 煤款含税单价保留小数位);
                setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);

            } else {

                if (结算价格 == 0) {

                    if (Jildw.equals("兆焦千克")) {
                        //                 			如果“计量单位”是兆焦千克，则要进行单位转换
                        Zhibvalue = Mjkg_to_kcalkg(Zhibvalue, 0);
                    }

                    //不符合增扣款条件的，即增扣款条件，类似“区间、等于”的，或“增扣款基数、增扣款单价 不全或为零时”，按照“合同价”进行结算
                    结算价格 = 合同价格 * Zhibvalue;
                    合同标准 = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                    setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);
                }
            }

        } else if (Zengkkdw.equals("元/吨")) {

            // 按照“元/吨”执行增付
            if (Zengkkdj > 0) {

                if (!Jildw.equals(Zengkkjsdw)) {

                    if (Jildw.equals("兆焦千克")) {

                        Zengkkjs = Kcalkg_to_mjkg(Zengkkjs, 2);
                        Zhibvalue = Mjkg_to_kcalkg(Zhibvalue, 0);
                    } else if (Jildw.equals("千卡千克")) {

                        Zengkkjs = Mjkg_to_kcalkg(Zengkkjs, 0);
                    }
                }

                if (Zengkklx.equals("增付")) {

                    //符合增款条件的，即增款条件，类似“大于，小于”的，按照“元/千卡”执行增付
                    //算出热量折单价，单位是：元
                    if (Zengkkjs > 0) {


                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);

                    } else if (Zengkkjs == 0) {


                        指标折单价 += Round_new(Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }

                } else if (Zengkklx.equals("扣付")) {

                    if (Zengkkjs > 0) {


                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);


                    } else if (Zengkkjs == 0) {


                        指标折单价 += Round_new(-Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }
                }

                结算价格 = Round_new((合同价格 * Zhibvalue + 指标折单价), 煤款含税单价保留小数位);

            } else {

                if (结算价格 == 0) {
                    // 不符合增扣款条件的，即增扣款条件，类似“区间、等于”的，或“增扣款基数、增扣款单价 不全或为零时”，按照“合同价”进行结算
                    if (Jildw.equals("兆焦千克")) {

                        Zhibvalue = Mjkg_to_kcalkg(Zhibvalue, 0);
                    }

                    结算价格 = 合同价格 * Zhibvalue;
                    合同标准 = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                }
            }
            //如果指标和上、下限的差单位是“兆焦千克”则将“盈亏”做单位转换，为了方便显示和储存
            if (Jildw.equals("兆焦千克")) {

                盈亏 = Mjkg_to_kcalkg(盈亏, 0);
            }
            setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);

        } else if (Zengkkdw.equals("元/兆焦")) {

            if (Zengkkdj > 0) {

                if (Jildw.equals("千卡千克")) {
                    //                 			如果“计量单位”是千卡千克，则要进行单位转换
                    盈亏 = Kcalkg_to_mjkg(盈亏, 2);
                    Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                }

                if (Zengkkjsdw.equals("千卡千克")) {
                    //                 			如果“基数单位”是千卡千克，则要进行单位转换
                    Zengkkjs = Kcalkg_to_mjkg(Zengkkjs, 2);
                }

                if (Zengkklx.equals("增付")) {

                    //符合增款条件的，即增款条件，类似“大于，小于”的，按照“元/千卡”执行增付
                    //算出热量折单价，单位是：分
                    if (Zengkkjs > 0) {


                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);

                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }
                } else if (Zengkklx.equals("扣付")) {

                    if (Zengkkjs > 0) {

                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);

                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(-Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }
                }

                指标折单价 = Round_new(指标折单价, 煤款增扣款保留小数位);
                结算价格 = Round_new(合同价格 * Zhibvalue + 指标折单价, 煤款含税单价保留小数位);
            } else {

                // 不符合增扣款条件的，即增扣款条件，类似“区间、等于”的，或“增扣款基数、增扣款单价 不全或为零时”，按照“合同价”进行结算
                if (结算价格 != 0) {


                } else {

                    if (Jildw.equals("千卡千克")) {

                        //                 					如果“计量单位”是千卡千克，则要进行单位转换
                        Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                    }

                    结算价格 = 合同价格 * Zhibvalue;
                    合同标准 = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                }
            }
            //如果指标和上、下限的差单位是“兆焦千克”则将“盈亏”做单位转换，为了方便显示和储存
            if (Jildw.equals("兆焦千克")) {

                盈亏 = Mjkg_to_kcalkg(盈亏, 0);
            }
            setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);

        } else if (Zengkkdw.equals("")) {

            // 按照“元/吨”执行增付
            if (Zengkkdj > 0) {

                if (!Jildw.equals(Zengkkjsdw)) {

                    if (Jildw.equals("兆焦千克")) {

                        Zengkkjs = Kcalkg_to_mjkg(Zengkkjs, 2);
                    } else if (Jildw.equals("千卡千克")) {

                        Zengkkjs = Mjkg_to_kcalkg(Zengkkjs, 0);
                    }
                }

                if (Zengkklx.equals("增付")) {

                    // 符合增款条件的，即增款条件，类似“大于，小于”的，按照“元/千卡”执行增付
                    //算出热量折单价，单位是：元
                    if (Zengkkjs > 0) {

                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);

                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }
                } else if (Zengkklx.equals("扣付")) {

                    if (Zengkkjs > 0) {

                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);

                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(-Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }
                }

                结算价格 = Round_new((合同价格 + 指标折单价), 煤款含税单价保留小数位);

            } else {

                // 不符合增扣款条件的，即增扣款条件，类似“区间、等于”的，或“增扣款基数、增扣款单价 不全或为零时”，按照“合同价”进行结算
                if (结算价格 != 0) {

                } else {

                    if (Jildw.equals("兆焦千克")) {

                        Zhibvalue = Mjkg_to_kcalkg(Zhibvalue, 0);
                    }
                    结算价格 = 合同价格 * Zhibvalue;
                    合同标准 = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                }

            }
            if (Jildw.equals("兆焦千克")) {

                盈亏 = Mjkg_to_kcalkg(盈亏, 0);
            }
            setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);
        }
    }


    //根据发热量计算结算价格(价格单位:"元/吨")
    void Jiesjgjs_farl_yd(String Jildw, String Zengkkdw, String Zengkktj, double Zhibvalue, double Hetsx, double Hetxx, String Zhibyk,
                          double Zengkkjs, String Zengkkjsdw, double Zengkkdj, String Zengkkdjgs, String Zengkklx, double Zengkkjzzkj, String Zengkkxscl) {
        //结算价格计算
        //参数：增扣款单位，增扣款条件，指标值，合同上限，合同下限，指标盈亏(赋值用)，增扣款基数，增扣款单价,增扣款类型，增扣款小数处理

        if (Zengkktj.equals("大于等于") || Zengkktj.equals("大于")) {

            盈亏 = Round_new((Zhibvalue - Hetxx), 2);
            合同标准 = String.valueOf(Hetxx);
        } else if (Zengkktj.equals("小于等于") || Zengkktj.equals("小于")) {

            盈亏 = Round_new((Zhibvalue - Hetsx), 2);
            合同标准 = String.valueOf(Hetsx);
        } else if (Zengkktj.equals("区间")) {

            if (Zengkklx.equals("增付")) {

                盈亏 = Round_new((Zhibvalue - Hetxx), 2);
            } else if (Zengkklx.equals("扣付")) {

                盈亏 = Round_new((Zhibvalue - Hetsx), 2);
            }
            合同标准 = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
        }

        if (!Zengkkdjgs.equals("")) {
            //存在增扣款公式
            //用公式重新给"Zengkkdj"赋值

            Zengkkdj = (double)eval(Zengkkdjgs);
        }

        if (Zengkkdw.equals("元/千卡")) {

            if (Zengkkdj > 0) {

                if (Jildw.equals("兆焦千克")) {
                    //                 			如果“计量单位”是兆焦千克，则要进行单位转换
                    盈亏 = Mjkg_to_kcalkg(盈亏, 0);
                    Zhibvalue = Mjkg_to_kcalkg(Zhibvalue, 0);
                }

                if (Zengkkjsdw.equals("兆焦千克")) {
                    //                 			如果“基数单位”是兆焦千克，则要进行单位转换
                    Zengkkjs = Mjkg_to_kcalkg(Zengkkjs, 0);
                }

                if (Zengkklx.equals("增付")) {

                    //符合增款条件的，即增款条件，类似“大于，小于”的，按照“元/千卡”执行增付
                    //算出热量折单价，单位是：分
                    if (Zengkkjs > 0) {

                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);

                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }

                } else if (Zengkklx.equals("扣付")) {

                    if (Zengkkjs > 0) {

                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);

                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(-Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }
                }

                指标折单价 = Round_new(指标折单价 * Zhibvalue, 煤款增扣款保留小数位);
                结算价格 = 合同价格 + 指标折单价;
                setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);
            } else {

                // 不符合增扣款条件的，即增扣款条件，类似“区间、等于”的，或“增扣款基数、增扣款单价 不全或为零时”，按照“合同价”进行结算
                if (结算价格 != 0) {


                } else {

                    结算价格 = 合同价格;
                    合同标准 = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                    setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);
                }
            }

        } else if (Zengkkdw.equals("元/吨")) {

            // 按照“元/吨”执行增付
            if (Zengkkdj > 0) {

                if (!Jildw.equals(Zengkkjsdw)) {

                    if (Jildw.equals("兆焦千克")) {

                        Zengkkjs = Kcalkg_to_mjkg(Zengkkjs, 2);
                    } else if (Jildw.equals("千卡千克")) {

                        Zengkkjs = Mjkg_to_kcalkg(Zengkkjs, 0);
                    }
                }

                if (Zengkklx.equals("增付")) {

                    // 符合增款条件的，即增款条件，类似“大于，小于”的，按照“元/千卡”执行增付
                    //算出热量折单价，单位是：元
                    if (Zengkkjs > 0) {

                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);

                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }

                } else if (Zengkklx.equals("扣付")) {

                    if (Zengkkjs > 0) {

                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);

                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(-Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }
                }

                结算价格 = Round_new((合同价格 + 指标折单价), 煤款含税单价保留小数位);
            } else {

                // 不符合增扣款条件的，即增扣款条件，类似“区间、等于”的，或“增扣款基数、增扣款单价 不全或为零时”，按照“合同价”进行结算
                if (结算价格 != 0) {

                } else {

                    结算价格 = 合同价格;
                    合同标准 = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                }

            }
            //如果指标和上、下限的差单位是“兆焦千克”则将“盈亏”做单位转换，为了方便显示和储存
            if (Jildw.equals("兆焦千克")) {

                盈亏 = Mjkg_to_kcalkg(盈亏, 0);
            }
            setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);

        } else if (Zengkkdw.equals("元/兆焦")) {

            if (Zengkkdj > 0) {

                if (Jildw.equals("千卡千克")) {
                    //                 			如果“计量单位”是千卡千克，则要进行单位转换
                    盈亏 = Kcalkg_to_mjkg(盈亏, 2);
                    Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                }

                if (Zengkkjsdw.equals("千卡千克")) {
                    //                 			如果“基数单位”是千卡千克，则要进行单位转换
                    Zengkkjs = Kcalkg_to_mjkg(Zengkkjs, 2);
                }

                if (Zengkklx.equals("增付")) {

                    //符合增款条件的，即增款条件，类似“大于，小于”的，按照“元/千卡”执行增付
                    //算出热量折单价，单位是：分
                    if (Zengkkjs > 0) {

                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);

                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }
                } else if (Zengkklx.equals("扣付")) {

                    if (Zengkkjs > 0) {

                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);

                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(-Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }
                }

                指标折单价 = Round_new(指标折单价, 煤款增扣款保留小数位);
                结算价格 = Round_new(合同价格 * Zhibvalue + 指标折单价, 煤款含税单价保留小数位);
            } else {

                // 不符合增扣款条件的，即增扣款条件，类似“区间、等于”的，或“增扣款基数、增扣款单价 不全或为零时”，按照“合同价”进行结算
                if (结算价格 != 0) {


                } else {

                    if (Jildw.equals("千卡千克")) {

                        //                 					如果“计量单位”是千卡千克，则要进行单位转换
                        Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                    }

                    结算价格 = 合同价格 * Zhibvalue;
                    合同标准 = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                }
            }
            //如果指标和上、下限的差单位是“兆焦千克”则将“盈亏”做单位转换，为了方便显示和储存
            if (Jildw.equals("兆焦千克")) {

                盈亏 = Mjkg_to_kcalkg(盈亏, 0);
            }
            setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);

        } else if (Zengkkdw.equals("")) {

            // 按照“元/吨”执行增付
            if (Zengkkdj > 0) {

                if (!Jildw.equals(Zengkkjsdw)) {

                    if (Jildw.equals("兆焦千克")) {

                        Zengkkjs = Kcalkg_to_mjkg(Zengkkjs, 2);
                    } else if (Jildw.equals("千卡千克")) {

                        Zengkkjs = Mjkg_to_kcalkg(Zengkkjs, 0);
                    }
                }

                if (Zengkklx.equals("增付")) {

                    // 符合增款条件的，即增款条件，类似“大于，小于”的，按照“元/千卡”执行增付
                    //算出热量折单价，单位是：元
                    if (Zengkkjs > 0) {

                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);

                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }
                } else if (Zengkklx.equals("扣付")) {

                    if (Zengkkjs > 0) {

                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);

                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(-Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }
                }

                结算价格 = Round_new((合同价格 + 指标折单价), 煤款含税单价保留小数位);

            } else {
                // 不符合增扣款条件的，即增扣款条件，类似“区间、等于”的，或“增扣款基数、增扣款单价 不全或为零时”，按照“合同价”进行结算
                if (结算价格 != 0) {

                } else {

                    结算价格 = 合同价格;
                    合同标准 = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                }

            }
            if (Jildw.equals("兆焦千克")) {

                盈亏 = Mjkg_to_kcalkg(盈亏, 0);
            }
            setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);
        }
    }

    //根据发热量计算结算价格(价格单位:"兆焦/吨")
    void Jiesjgjs_farl_Mjd(String Jildw, String Zengkkdw, String Zengkktj, double Zhibvalue, double Hetsx, double Hetxx, String Zhibyk,
                           double Zengkkjs, String Zengkkjsdw, double Zengkkdj, String Zengkkdjgs, String Zengkklx, double Zengkkjzzkj, String Zengkkxscl) {
        //结算价格计算
        //参数：增扣款单位，增扣款条件，指标值，合同上限，合同下限，指标盈亏(赋值用)，增扣款基数，增扣款单价,增扣款类型,增扣款小数处理
        //兆焦/吨的价格计算方式 合同价格×兆焦

        if (Zengkktj.equals("大于等于") || Zengkktj.equals("大于")) {

            盈亏 = Round_new((Zhibvalue - Hetxx), 2);
            合同标准 = String.valueOf(Hetxx);
        } else if (Zengkktj.equals("小于等于") || Zengkktj.equals("小于")) {

            盈亏 = Round_new((Zhibvalue - Hetsx), 2);
            合同标准 = String.valueOf(Hetsx);
        } else if (Zengkktj.equals("区间")) {

            if (Zengkklx.equals("增付")) {

                盈亏 = Round_new((Zhibvalue - Hetxx), 2);
            } else if (Zengkklx.equals("扣付")) {

                盈亏 = Round_new((Zhibvalue - Hetsx), 2);
            }
            合同标准 = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
        }


        if (!Zengkkdjgs.equals("")) {
            //存在增扣款公式
            //用公式重新给"Zengkkdj"赋值

            Zengkkdj = (double)eval(Zengkkdjgs);
        }

        if (Zengkkdw.equals("元/千卡")) {

            if (Zengkkdj > 0) {

                if (Jildw.equals("兆焦千克")) {
                    //                 			如果“计量单位”是兆焦千克，则要进行单位转换
                    盈亏 = Mjkg_to_kcalkg(盈亏, 0);
                    Zhibvalue = Mjkg_to_kcalkg(Zhibvalue, 0);
                }

                if (Zengkkjsdw.equals("兆焦千克")) {
                    //                 			如果“基数单位”是兆焦千克，则要进行单位转换
                    Zengkkjs = Mjkg_to_kcalkg(Zengkkjs, 0);
                }

                if (Zengkklx.equals("增付")) {

                    //符合增款条件的，即增款条件，类似“大于，小于”的，按照“元/千卡”执行增付
                    //算出热量折单价，单位是：分
                    if (Zengkkjs > 0) {

                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);

                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }
                } else if (Zengkklx.equals("扣付")) {

                    if (Zengkkjs > 0) {

                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);

                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(-Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);

                    }
                }

                指标折单价 = Round_new(指标折单价 * Zhibvalue, 煤款增扣款保留小数位);
                Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                结算价格 = Round_new(合同价格 * Zhibvalue + 指标折单价, 煤款含税单价保留小数位);
                setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);
            } else {

                // 不符合增扣款条件的，即增扣款条件，类似“区间、等于”的，或“增扣款基数、增扣款单价 不全或为零时”，按照“合同价”进行结算
                if (结算价格 != 0) {

                } else {

                    if (Jildw.equals("千卡千克")) {
                        //                 					如果“计量单位”是千卡千克，则要进行单位转换
                        Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                    }
                    结算价格 = 合同价格 * Zhibvalue;
                    合同标准 = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                    setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);
                }
            }

        } else if (Zengkkdw.equals("元/吨")) {

            // 按照“元/吨”执行增付
            if (Zengkkdj > 0) {

                if (!Jildw.equals(Zengkkjsdw)) {

                    if (Jildw.equals("兆焦千克")) {

                        Zengkkjs = Kcalkg_to_mjkg(Zengkkjs, 2);
                    } else if (Jildw.equals("千卡千克")) {

                        Zengkkjs = Mjkg_to_kcalkg(Zengkkjs, 0);
                    }
                }

                if (Zengkklx.equals("增付")) {

                    // 符合增款条件的，即增款条件，类似“大于，小于”的，按照“元/千卡”执行增付
                    //算出热量折单价，单位是：元
                    if (Zengkkjs > 0) {

                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);

                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);

                    }
                } else if (Zengkklx.equals("扣付")) {

                    if (Zengkkjs > 0) {

                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);

                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(-Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);

                    }
                }

                if (Jildw.equals("千卡千克")) {

                    Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                }

                结算价格 = Round_new((合同价格 * Zhibvalue + 指标折单价), 煤款含税单价保留小数位);

            } else {

                // 不符合增扣款条件的，即增扣款条件，类似“区间、等于”的，或“增扣款基数、增扣款单价 不全或为零时”，按照“合同价”进行结算
                if (结算价格 != 0) {

                } else {

                    if (Jildw.equals("千卡千克")) {

                        Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                    }
                    结算价格 = 合同价格 * Zhibvalue;
                    合同标准 = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                }

            }
            //如果指标和上、下限的差单位是“兆焦千克”则将“盈亏”做单位转换，为了方便显示和储存
            if (Jildw.equals("兆焦千克")) {

                盈亏 = Mjkg_to_kcalkg(盈亏, 0);
            }
            setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);

        } else if (Zengkkdw.equals("元/兆焦")) {

            if (Zengkkdj > 0) {

                if (Jildw.equals("千卡千克")) {
                    //                 			如果“计量单位”是千卡千克，则要进行单位转换
                    盈亏 = Kcalkg_to_mjkg(盈亏, 2);
                    Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                }

                if (Zengkkjsdw.equals("千卡千克")) {
                    //                 			如果“基数单位”是千卡千克，则要进行单位转换
                    Zengkkjs = Kcalkg_to_mjkg(Zengkkjs, 2);
                }

                if (Zengkklx.equals("增付")) {

                    //符合增款条件的，即增款条件，类似“大于，小于”的，按照“元/千卡”执行增付
                    //算出热量折单价，单位是：分
                    if (Zengkkjs > 0) {

                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);

                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }
                } else if (Zengkklx.equals("扣付")) {

                    if (Zengkkjs > 0) {

                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);
                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(-Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }
                }

                指标折单价 = Round_new(指标折单价, 煤款增扣款保留小数位);
                结算价格 = Round_new(合同价格 * Zhibvalue + 指标折单价, 煤款含税单价保留小数位);
            } else {

                // 不符合增扣款条件的，即增扣款条件，类似“区间、等于”的，或“增扣款基数、增扣款单价 不全或为零时”，按照“合同价”进行结算
                if (结算价格 != 0) {


                } else {

                    if (Jildw.equals("千卡千克")) {

                        //                 					如果“计量单位”是千卡千克，则要进行单位转换
                        Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                    }

                    结算价格 = 合同价格 * Zhibvalue;
                    合同标准 = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                }
            }
            //如果指标和上、下限的差单位是“兆焦千克”则将“盈亏”做单位转换，为了方便显示和储存
            if (Jildw.equals("兆焦千克")) {

                盈亏 = Mjkg_to_kcalkg(盈亏, 0);
            }
            setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);

        } else if (Zengkkdw.equals("")) {

            // 按照“元/吨”执行增付
            if (Zengkkdj > 0) {

                if (!Jildw.equals(Zengkkjsdw)) {

                    if (Jildw.equals("兆焦千克")) {

                        Zengkkjs = Kcalkg_to_mjkg(Zengkkjs, 2);
                    } else if (Jildw.equals("千卡千克")) {

                        Zengkkjs = Mjkg_to_kcalkg(Zengkkjs, 0);
                    }
                }

                if (Zengkklx.equals("增付")) {

                    // 符合增款条件的，即增款条件，类似“大于，小于”的，按照“元/千卡”执行增付
                    //算出热量折单价，单位是：元
                    if (Zengkkjs > 0) {

                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);
                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }
                } else if (Zengkklx.equals("扣付")) {

                    if (Zengkkjs > 0) {

                        指标折单价 += Round_new(Round_new(Xiaoscl(盈亏 / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);
                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(-Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }
                }

                if (Jildw.equals("千卡千克")) {

                    Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                }
                结算价格 = Round_new((合同价格 * Zhibvalue + 指标折单价), 煤款含税单价保留小数位);

            } else {

                // 不符合增扣款条件的，即增扣款条件，类似“区间、等于”的，或“增扣款基数、增扣款单价 不全或为零时”，按照“合同价”进行结算
                if (结算价格 != 0) {

                } else {
                    if (Jildw.equals("千卡千克")) {

                        //                 					如果“计量单位”是千卡千克，则要进行单位转换
                        Zhibvalue = Kcalkg_to_mjkg(Zhibvalue, 2);
                    }
                    结算价格 = 合同价格 * Zhibvalue;
                    合同标准 = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                }

            }
            if (Jildw.equals("兆焦千克")) {

                盈亏 = Mjkg_to_kcalkg(盈亏, 0);
            }
            setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);
        }

    }
    private String Qnetar计量单位;
    private double Qnetar千卡千克;
    private double Qnetar兆焦千克;
    //结算其它指标折价
    void Jiesqtzbzj(String Zengkktj, double Zhibvalue, double Hetsx, double Hetxx, String Zhibyk,
                    double Zengkkjs, double Zengkkdj, String Zengkkdjgs, String Zengkjdw, String Zengkklx, double Zengkkjzzkj,
                    String Zengkkxscl) {
        //结算价格计算
        //参数：增扣款条件，指标值，合同上限，合同下限，指标盈亏(赋值用)，增扣款基数，增扣款单价，增扣款类型（增付、扣付）


        if (Zengkktj.equals("大于等于") || Zengkktj.equals("大于")) {

            盈亏 = Zhibvalue - Hetxx;
            合同标准 = String.valueOf(Hetxx);
        } else if (Zengkktj.equals("小于等于") || Zengkktj.equals("小于")) {

            盈亏 = Zhibvalue - Hetsx;
            合同标准 = String.valueOf(Hetsx);
        } else if (Zengkktj.equals("区间")) {

            if (Zengkklx.equals("增付")) {

                盈亏 = Round_new((Zhibvalue - Hetxx), 2);
            } else if (Zengkklx.equals("扣付")) {

                盈亏 = Round_new((Zhibvalue - Hetsx), 2);
            }
            合同标准 = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
        }


        if (!Zengkkdjgs.equals("")) {
            //存在增扣款公式
            //用公式重新给"Zengkkdj"赋值

            指标折单价 = (double)eval(Zengkkdjgs);
            setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);
        } else if (!(Zengkkdj == 0)) {

            //符合增款条件的，即增款条件，类似“大于，小于”的，按照“元/千卡”执行增付

            if (Zengkklx.equals("增付")) {

                if (Zengkkjs > 0) {

                    指标折单价 += Round_new(Round_new(Xiaoscl(Math.abs(盈亏) / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);
                } else if (Zengkkjs == 0) {

                    指标折单价 += Round_new(Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                }
            } else if (Zengkklx.equals("扣付")) {

                if (Zengkkjs > 0) {

                    if (盈亏 >= 0) {

                        指标折单价 += Round_new(-Round_new(Xiaoscl(Math.abs(盈亏) / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);
                    } else {

                        指标折单价 += Round_new(-Round_new(Xiaoscl(Math.abs(盈亏) / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }

                } else if (Zengkkjs == 0) {

                    if (盈亏 >= 0) {

                        指标折单价 += Round_new(-Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    } else {

                        指标折单价 += Round_new(-Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }
                }
            }

            if (Zengkjdw.equals("元/千卡")) {

                if (Qnetar计量单位.equals("千卡千克")) {

                    结算数量_发热量 = Qnetar千卡千克;
                } else if (Qnetar计量单位.equals("兆焦千克")) {

                    结算数量_发热量 = Mjkg_to_kcalkg(Qnetar兆焦千克, 0);
                }

                指标折单价 = CustomMaths.mul(指标折单价, 结算数量_发热量);
            }

            if (Zengkjdw.equals("%吨")) {

                setEvaluate_Jiessl(Zhibyk, 合同标准, 盈亏, 指标折单价, Zengkjdw);
            } else {

                setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);
            }

        } else {

            //不符合增扣款条件的，即增扣款条件，类似“区间、等于”的，或“增扣款基数、增扣款单价 不全或为零时”，按照“合同价”进行结算
            if (指标折单价 != 0) {

            } else {

                指标折单价 = 0;

                if (Zengkjdw.equals("%吨")) {

                    setEvaluate_Jiessl(Zhibyk, 合同标准, 盈亏, 指标折单价, Zengkjdw);
                } else {

                    setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);
                }
            }
        }
    }

    //结算其它指标折价_反向（用来处理 硫分、运距这种没升高XX扣多少多少钱）
    void Jiesqtzbzj_fx(String Zengkktj, double Zhibvalue, double Hetsx, double Hetxx, String Zhibyk,
                       double Zengkkjs, double Zengkkdj, String Zengkjdw, String Zengkklx, double Zengkkjzzkj,
                       String Zengkkxscl) {

        //结算价格计算
        //参数：增扣款条件，指标值，合同上限，合同下限，指标盈亏(赋值用)，增扣款基数，增扣款单价，增扣款类型（增付、扣付）,基准增扣价，小数处理


        if (Zengkktj.equals("大于等于") || Zengkktj.equals("大于")) {

            盈亏 = Round_new((Zhibvalue - Hetxx), 2);
            合同标准 = String.valueOf(Hetxx);
        } else if (Zengkktj.equals("小于等于") || Zengkktj.equals("小于")) {

            盈亏 = Round_new((Zhibvalue - Hetsx), 2);
            合同标准 = String.valueOf(Hetsx);
        } else if (Zengkktj.equals("区间")) {

            if (Zengkklx.equals("增付")) {

                盈亏 = Round_new((Zhibvalue - Hetsx), 2);
            } else if (Zengkklx.equals("扣付")) {

                盈亏 = Round_new((Zhibvalue - Hetxx), 2);
            }
            合同标准 = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
        }

        if (!(Zengkkdj == 0)) {

            //符合增款条件的，即增款条件，类似“大于，小于”的，按照“元/千卡”执行增付

            if (Zengkklx.equals("增付")) {

                if (Zengkkjs > 0) {

                    指标折单价 += Round_new(Round_new(Xiaoscl(Math.abs(盈亏) / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);
                } else if (Zengkkjs == 0) {

                    指标折单价 += Round_new(Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                }
            } else if (Zengkklx.equals("扣付")) {

                if (Zengkkjs > 0) {

                    if (盈亏 >= 0) {

                        指标折单价 += Round_new(-Round_new(Xiaoscl(Math.abs(盈亏) / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);
                    } else {

                        指标折单价 += Round_new(Round_new(Xiaoscl(Math.abs(盈亏) / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }

                } else if (Zengkkjs == 0) {

                    if (盈亏 >= 0) {

                        指标折单价 += Round_new(-Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    } else {

                        指标折单价 += Round_new(Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }
                }
            }

            if (Zengkjdw.equals("元/千卡")) {

                if (Qnetar计量单位.equals("千卡千克")) {

                    结算数量_发热量 = Qnetar千卡千克;
                } else if (Qnetar计量单位.equals("兆焦千克")) {

                    结算数量_发热量 = Mjkg_to_kcalkg(Qnetar兆焦千克, 0);
                }

                指标折单价 = CustomMaths.mul(指标折单价, 结算数量_发热量);
            }

            setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);
        } else {

            //不符合增扣款条件的，即增扣款条件，类似“区间、等于”的，或“增扣款基数、增扣款单价 不全或为零时”，按照“合同价”进行结算
            if (指标折单价 != 0) {

            } else {

                指标折单价 = 0;
                setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);
            }
        }
    }

    //结算数量折单价（用于供煤吨数超过合同量时给以矿方奖励用）
    void Jiesslzj(String Jildw, String Zengkkdw, String Zengkktj, double Zhibvalue, double Hetsx, double Hetxx, String Zhibyk,
                  double Zengkkjs, String Zengkkjsdw, double Zengkkdj, String Zengkkdjgs, String Zengkklx, double Zengkkjzzkj, String Zengkkxscl) {

        //如果数量计量单位是万吨，在调用公式前就已经java中进行了转换，关键是要判断计量单位和增扣款基数之间的单位
        if (Jildw.equals("万吨")) {

            if (Zengkkjsdw.equals("吨")) {

                Zengkkjs = Round_new(Zengkkjs / 10000, 10);
            }
        } else if (Jildw.equals("吨")) {

            if (Zengkkjsdw.equals("万吨")) {

                Zengkkjs = Round_new(Zengkkjs * 10000, 2);
            }
        }

        if (Zengkktj.equals("大于等于") || Zengkktj.equals("大于")) {

            盈亏 = Round_new((Zhibvalue - Hetxx), 2);
            合同标准 = String.valueOf(Hetxx);
        } else if (Zengkktj.equals("小于等于") || Zengkktj.equals("小于")) {

            盈亏 = Round_new((Zhibvalue - Hetsx), 2);
            合同标准 = String.valueOf(Hetsx);
        } else if (Zengkktj.equals("区间")) {

            if (Zengkklx.equals("增付")) {

                盈亏 = Round_new((Zhibvalue - Hetxx), 2);
            } else if (Zengkklx.equals("扣付")) {

                盈亏 = Round_new((Zhibvalue - Hetsx), 2);
            }
            合同标准 = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
        }

        if (!Zengkkdjgs.equals("")) {
            //存在增扣款公式
            //用公式重新给"Zengkkdj"赋值

            Zengkkdj = (double)eval(Zengkkdjgs);
        }

        if (Zengkkdw.equals("元/千卡")) {

            if (Qnetar计量单位.equals("千卡千克")) {

                结算数量_发热量 = Qnetar千卡千克;
            } else if (Qnetar计量单位.equals("兆焦千克")) {

                结算数量_发热量 = Mjkg_to_kcalkg(Qnetar兆焦千克, 0);
            }

            if (Zengkkdj > 0) {

                if (Zengkklx.equals("增付")) {

                    //符合增款条件的，即增款条件，类似“大于，小于”的，按照“元/千卡”执行增付
                    //算出热量折单价，单位是：分

                    if (Zengkkjs > 0) {

                        指标折单价 += Round_new(Round_new(Xiaoscl(Math.abs(盈亏) / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);
                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }
                } else if (Zengkklx.equals("扣付")) {

                    if (Zengkkjs > 0) {

                        if (盈亏 >= 0) {

                            指标折单价 += Round_new(-Round_new(Xiaoscl(Math.abs(盈亏) / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);
                        } else {

                            指标折单价 += Round_new(-Round_new(Xiaoscl(Math.abs(盈亏) / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);
                        }

                    } else if (Zengkkjs == 0) {

                        if (盈亏 >= 0) {

                            指标折单价 += Round_new(-Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                        } else {

                            指标折单价 += Round_new(-Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                        }
                    }
                }

                指标折单价 = Round_new(指标折单价 * 结算数量_发热量, 煤款增扣款保留小数位);
                setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);
            } else {

                // 不符合增扣款条件的，即增扣款条件，类似“区间、等于”的，或“增扣款基数、增扣款单价 不全或为零时”，按照“合同价”进行结算
                if (指标折单价 != 0) {

                } else {

                    合同标准 = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                    setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);
                }
            }

        } else if (Zengkkdw.equals("元/吨")) {

            // 按照“元/吨”执行增付
            if (Zengkkdj > 0) {

                if (Zengkklx.equals("增付")) {

                    // 符合增款条件的，即增款条件，类似“大于，小于”的，按照“元/千卡”执行增付
                    //算出热量折单价，单位是：元
                    if (Zengkkjs > 0) {

                        指标折单价 += Round_new(Round_new(Xiaoscl(Math.abs(盈亏) / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);
                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }
                } else if (Zengkklx.equals("扣付")) {

                    if (Zengkkjs > 0) {

                        if (盈亏 >= 0) {

                            指标折单价 += Round_new(-Round_new(Xiaoscl(Math.abs(盈亏) / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);
                        } else {

                            指标折单价 += Round_new(-Round_new(Xiaoscl(Math.abs(盈亏) / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);
                        }

                    } else if (Zengkkjs == 0) {

                        if (盈亏 >= 0) {

                            指标折单价 += Round_new(-Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                        } else {

                            指标折单价 += Round_new(-Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                        }
                    }
                }

                setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);

            } else {

                // 不符合增扣款条件的，即增扣款条件，类似“区间、等于”的，或“增扣款基数、增扣款单价 不全或为零时”，按照“合同价”进行结算
                if (指标折单价 != 0) {

                } else {

                    合同标准 = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);
                    setEvaluate(Zhibyk, 盈亏, 合同标准, 指标折单价);
                }

            }
        } else if (Zengkkdw.equals("%吨")) {
            //        	处理数量折价“%吨”的情况

            if (Zengkkdj > 0) {

                if (Zengkklx.equals("增付")) {

                    // 符合增款条件的，即增款条件，类似“大于，小于”的，按照“元/千卡”执行增付
                    //算出热量折单价，单位是：元
                    if (Zengkkjs > 0) {

                        指标折单价 += Round_new(Round_new(Xiaoscl(Math.abs(盈亏) / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);
                    } else if (Zengkkjs == 0) {

                        指标折单价 += Round_new(Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                    }
                } else if (Zengkklx.equals("扣付")) {

                    if (Zengkkjs > 0) {

                        if (盈亏 >= 0) {

                            指标折单价 += Round_new(-Round_new(Xiaoscl(Math.abs(盈亏) / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);
                        } else {

                            指标折单价 += Round_new(-Round_new(Xiaoscl(Math.abs(盈亏) / Zengkkjs, Zengkkxscl) * Zengkkdj, 煤款增扣款保留小数位) + Zengkkjzzkj, 煤款增扣款保留小数位);
                        }

                    } else if (Zengkkjs == 0) {

                        if (盈亏 >= 0) {

                            指标折单价 += Round_new(-Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                        } else {

                            指标折单价 += Round_new(-Zengkkdj + Zengkkjzzkj, 煤款增扣款保留小数位);
                        }
                    }
                }

                setEvaluate_Jiessl(Zhibyk, 合同标准, 盈亏, 指标折单价, Zengkjdw);

            } else {

                // 不符合增扣款条件的，即增扣款条件，类似“区间、等于”的，或“增扣款基数、增扣款单价 不全或为零时”，按照“合同价”进行结算
                if (指标折单价 != 0) {

                } else {

                    合同标准 = String.valueOf(Hetxx) + "-" + String.valueOf(Hetsx);

                    setEvaluate_Jiessl(Zhibyk, 合同标准, 盈亏, 指标折单价, Zengkjdw);
                }
            }
        }
    }

    public Object eval(String gongs){
        jis();
        return 0;
    }

    public void jis() {
        //单批次结算
        if (结算形式.equals("单批次") || 结算形式.equals("加权平均") || 结算形式.equals("加权分列")) {

            Initialize();

            //热值区间按卡计价
            if (计价方式.equals("热值区间(卡)")) {

                if (价格单位.equals("元/千卡")) {

                    发热量 = Qnetar千卡千克;

                    if (Qnetar计量单位.equals("兆焦千克")) {

                        发热量 = Qnetar兆焦千克;
                    }

                    合同标准_Qnetar = String.valueOf(Mjkg_to_kcalkg(Qnetar下限, 0)) + "-" + String.valueOf(Mjkg_to_kcalkg(Qnetar上限, 0));
                    结算价格 = Round_new((发热量 * 合同价格), 煤款含税单价保留小数位);
                }

                if (价格单位.equals("元/兆焦")) {

                    if (Qnetar计量单位.equals("兆焦千克")) {

                        发热量 = Qnetar兆焦千克;
                    } else if (Qnetar计量单位.equals("千卡千克")) {

                        发热量 = Kcalkg_to_mjkg(Qnetar千卡千克, 2);
                    }

                    合同标准_Qnetar = String.valueOf(Mjkg_to_kcalkg(Qnetar下限, 0)) + "-" + String.valueOf(Mjkg_to_kcalkg(Qnetar上限, 0));
                    结算价格 = Round_new((发热量 * 合同价格), 煤款含税单价保留小数位);
                }
            }

            //热值区间按吨计价
            if (计价方式.equals("热值区间(吨)")) {

                合同标准_Qnetar = String.valueOf(Mjkg_to_kcalkg(Qnetar下限, 0)) + "-" + String.valueOf(Mjkg_to_kcalkg(Qnetar上限, 0));
                结算价格 = Round_new(合同价格, 煤款含税单价保留小数位);
            }

            //热值按卡扣付

            发热量折价 = 0.0;
            if (计价方式.equals("热值按卡扣付")) {

                if (价格单位.equals("元/千卡")) {

                    if (Qnetar计量单位.equals("千卡千克")) {

                        发热量 = Qnetar千卡千克;

                        //用基价计算出价格
                        //增价(元/千卡)
                        //增扣款单位，增扣款条件，指标值，合同上限，合同下限，指标盈亏(赋值用)，合同标准（赋值用），增扣款基数，增扣款单价，指标折单价（赋值用）

                        if (Qnetar增付单价 > 0 || !Qnetar增付单价公式.equals("")) {

                            Jiesjgjs_farl_yk(Qnetar计量单位, Qnetar增付价单位, Qnetar增扣款条件, 发热量, Qnetar上限, Qnetar下限, "盈亏_Qnetar",
                                    Qnetar增扣款基数, Qnetar增扣款基数单位, Qnetar增付单价, Qnetar增付单价公式, "增付", Qnetar基准增扣价, Qnetar小数处理);
                        }

                        if (Qnetar扣付单价 > 0 || !Qnetar扣付单价公式.equals("")) {

                            Jiesjgjs_farl_yk(Qnetar计量单位, Qnetar扣付价单位, Qnetar增扣款条件, 发热量, Qnetar上限, Qnetar下限, "盈亏_Qnetar",
                                    Qnetar增扣款基数, Qnetar增扣款基数单位, Qnetar扣付单价, Qnetar扣付单价公式, "扣付", Qnetar基准增扣价, Qnetar小数处理);
                        }

                        if (Qnetar增付单价 == 0 && Qnetar扣付单价 == 0 && Qnetar增付单价公式.equals("") && Qnetar扣付单价公式.equals("")) {

                            Jiesjgjs_farl_yk(Qnetar计量单位, Qnetar增付价单位, Qnetar增扣款条件, 发热量, Qnetar上限, Qnetar下限, "盈亏_Qnetar",
                                    Qnetar增扣款基数, Qnetar增扣款基数单位, Qnetar增付单价, Qnetar增付单价公式, "增付", Qnetar基准增扣价, Qnetar小数处理);
                        }

                    }

                    if (Qnetar计量单位.equals("兆焦千克")) {

                        // 用基价计算出价格
                        // 增价（元/千卡）
                        //价格单位.equals("元/千卡"),计量单位(兆焦/千克),增价(元/千卡)
                        //由于折价单位为“元/千卡”故要进行单位转换

                        //                	发热量=Mjkg_to_kcalkg(Qnetar兆焦千克,0);		//单位转换
                        发热量 = Qnetar兆焦千克;

                        if (Qnetar增付单价 > 0 || !Qnetar增付单价公式.equals("")) {

                            Jiesjgjs_farl_yk(Qnetar计量单位, Qnetar增付价单位, Qnetar增扣款条件, 发热量, Qnetar上限, Qnetar下限, "盈亏_Qnetar",
                                    Qnetar增扣款基数, Qnetar增扣款基数单位, Qnetar增付单价, Qnetar增付单价公式, "增付", Qnetar基准增扣价, Qnetar小数处理);
                        }

                        if (Qnetar扣付单价 > 0 || !Qnetar扣付单价公式.equals("")) {

                            Jiesjgjs_farl_yk(Qnetar计量单位, Qnetar扣付价单位, Qnetar增扣款条件, 发热量, Qnetar上限, Qnetar下限, "盈亏_Qnetar",
                                    Qnetar增扣款基数, Qnetar增扣款基数单位, Qnetar扣付单价, Qnetar扣付单价公式, "扣付", Qnetar基准增扣价, Qnetar小数处理);
                        }

                        if (Qnetar增付单价 == 0 && Qnetar扣付单价 == 0 && Qnetar增付单价公式.equals("") && Qnetar扣付单价公式.equals("")) {

                            Jiesjgjs_farl_yk(Qnetar计量单位, Qnetar增付价单位, Qnetar增扣款条件, 发热量, Qnetar上限, Qnetar下限, "盈亏_Qnetar",
                                    Qnetar增扣款基数, Qnetar增扣款基数单位, Qnetar增付单价, Qnetar增付单价公式, "增付", Qnetar基准增扣价, Qnetar小数处理);
                        }
                    }
                }

                if (价格单位.equals("元/吨")) {

                    if (Qnetar计量单位.equals("千卡千克")) {

                        发热量 = Qnetar千卡千克;
                        //用基价计算出价格
                        //增价(元/千卡)
                        //增扣款单位，增扣款条件，指标值，合同上限，合同下限，指标盈亏(赋值用)，合同标准（赋值用），增扣款基数，增扣款单价，指标折单价（赋值用）

                        if (Qnetar增付单价 > 0 || !Qnetar增付单价公式.equals("")) {

                            Jiesjgjs_farl_yd(Qnetar计量单位, Qnetar增付价单位, Qnetar增扣款条件, 发热量, Qnetar上限, Qnetar下限, "盈亏_Qnetar",
                                    Qnetar增扣款基数, Qnetar增扣款基数单位, Qnetar增付单价, Qnetar增付单价公式, "增付", Qnetar基准增扣价, Qnetar小数处理);
                        }

                        if (Qnetar扣付单价 > 0 || !Qnetar扣付单价公式.equals("")) {

                            Jiesjgjs_farl_yd(Qnetar计量单位, Qnetar扣付价单位, Qnetar增扣款条件, 发热量, Qnetar上限, Qnetar下限, "盈亏_Qnetar",
                                    Qnetar增扣款基数, Qnetar增扣款基数单位, Qnetar扣付单价, Qnetar扣付单价公式, "扣付", Qnetar基准增扣价, Qnetar小数处理);
                        }

                        if (Qnetar增付单价 == 0 && Qnetar扣付单价 == 0 && Qnetar增付单价公式.equals("") && Qnetar扣付单价公式.equals("")) {

                            Jiesjgjs_farl_yd(Qnetar计量单位, Qnetar增付价单位, Qnetar增扣款条件, 发热量, Qnetar上限, Qnetar下限, "盈亏_Qnetar",
                                    Qnetar增扣款基数, Qnetar增扣款基数单位, Qnetar增付单价, Qnetar增付单价公式, "增付", Qnetar基准增扣价, Qnetar小数处理);
                        }
                    }

                    if (Qnetar计量单位.equals("兆焦千克")) {

                        //用基价计算+++出价格
                        //增价（元/千卡）
                        //价格单位.equals("元/千卡"),计量单位(兆焦/千克),增价(元/千卡)
                        //由于折价单位为“元/千卡”故要进行单位转换

                        //                	发热量=Mjkg_to_kcalkg(Qnetar兆焦千克,0);		//单位转换
                        发热量 = Qnetar兆焦千克;

                        if (Qnetar增付单价 > 0 || !Qnetar增付单价公式.equals("")) {

                            Jiesjgjs_farl_yd(Qnetar计量单位, Qnetar增付价单位, Qnetar增扣款条件, 发热量, Qnetar上限, Qnetar下限, "盈亏_Qnetar",
                                    Qnetar增扣款基数, Qnetar增扣款基数单位, Qnetar增付单价, Qnetar增付单价公式, "增付", Qnetar基准增扣价, Qnetar小数处理);
                        }

                        if (Qnetar扣付单价 > 0 || !Qnetar扣付单价公式.equals("")) {

                            Jiesjgjs_farl_yd(Qnetar计量单位, Qnetar扣付价单位, Qnetar增扣款条件, 发热量, Qnetar上限, Qnetar下限, "盈亏_Qnetar",
                                    Qnetar增扣款基数, Qnetar增扣款基数单位, Qnetar扣付单价, Qnetar扣付单价公式, "扣付", Qnetar基准增扣价, Qnetar小数处理);
                        }

                        if (Qnetar增付单价 == 0 && Qnetar扣付单价 == 0 && Qnetar增付单价公式.equals("") && Qnetar扣付单价公式.equals("")) {

                            Jiesjgjs_farl_yd(Qnetar计量单位, Qnetar增付价单位, Qnetar增扣款条件, 发热量, Qnetar上限, Qnetar下限, "盈亏_Qnetar",
                                    Qnetar增扣款基数, Qnetar增扣款基数单位, Qnetar增付单价, Qnetar增付单价公式, "增付", Qnetar基准增扣价, Qnetar小数处理);
                        }
                    }
                }

                if (价格单位.equals("元/兆焦")) {

                    if (Qnetar计量单位.equals("千卡千克")) {

                        发热量 = Qnetar千卡千克;

                        // 用基价计算出价格
                        //增价(元/千卡)
                        //增扣款单位，增扣款条件，指标值，合同上限，合同下限，指标盈亏(赋值用)，合同标准（赋值用），增扣款基数，增扣款单价，指标折单价（赋值用）

                        if (Qnetar增付单价 > 0 || !Qnetar增付单价公式.equals("")) {
                            Jiesjgjs_farl_yd(Qnetar计量单位, Qnetar增付价单位, Qnetar增扣款条件, 发热量, Qnetar上限, Qnetar下限, "盈亏_Qnetar",
                                    Qnetar增扣款基数, Qnetar增扣款基数单位, Qnetar增付单价, Qnetar增付单价公式, "增付", Qnetar基准增扣价, Qnetar小数处理);
                        }

                        if (Qnetar扣付单价 > 0 || !Qnetar扣付单价公式.equals("")) {
                            Jiesjgjs_farl_yd(Qnetar计量单位, Qnetar扣付价单位, Qnetar增扣款条件, 发热量, Qnetar上限, Qnetar下限, "盈亏_Qnetar",
                                    Qnetar增扣款基数, Qnetar增扣款基数单位, Qnetar扣付单价, Qnetar扣付单价公式, "扣付", Qnetar基准增扣价, Qnetar小数处理);
                        }

                        if (Qnetar增付单价 == 0 && Qnetar扣付单价 == 0 && Qnetar增付单价公式.equals("") && Qnetar扣付单价公式.equals("")) {

                            Jiesjgjs_farl_yd(Qnetar计量单位, Qnetar增付价单位, Qnetar增扣款条件, 发热量, Qnetar上限, Qnetar下限, "盈亏_Qnetar",
                                    Qnetar增扣款基数, Qnetar增扣款基数单位, Qnetar增付单价, Qnetar增付单价公式, "增付", Qnetar基准增扣价, Qnetar小数处理);
                        }
                    }

                    if (Qnetar计量单位.equals("兆焦千克")) {

                        // 用基价计算出价格
                        // 增价（元/千卡）
                        //价格单位.equals("元/千卡"),计量单位(兆焦/千克),增价(元/千卡)
                        //由于折价单位为“元/千卡”故要进行单位转换

                        //                	发热量=Mjkg_to_kcalkg(Qnetar兆焦千克,0);//单位转换
                        发热量 = Qnetar兆焦千克;

                        if (Qnetar增付单价 > 0 || !Qnetar增付单价公式.equals("")) {

                            Jiesjgjs_farl_Mjd(Qnetar计量单位, Qnetar增付价单位, Qnetar增扣款条件, 发热量, Qnetar上限, Qnetar下限, "盈亏_Qnetar",
                                    Qnetar增扣款基数, Qnetar增扣款基数单位, Qnetar增付单价, Qnetar增付单价公式, "增付", Qnetar基准增扣价, Qnetar小数处理);
                        }

                        if (Qnetar扣付单价 > 0 || !Qnetar扣付单价公式.equals("")) {

                            Jiesjgjs_farl_Mjd(Qnetar计量单位, Qnetar扣付价单位, Qnetar增扣款条件, 发热量, Qnetar上限, Qnetar下限, "盈亏_Qnetar",
                                    Qnetar增扣款基数, Qnetar增扣款基数单位, Qnetar扣付单价, Qnetar扣付单价公式, "扣付", Qnetar基准增扣价, Qnetar小数处理);
                        }

                        if (Qnetar增付单价 == 0 && Qnetar扣付单价 == 0 && Qnetar增付单价公式.equals("") && Qnetar扣付单价公式.equals("")) {

                            Jiesjgjs_farl_Mjd(Qnetar计量单位, Qnetar增付价单位, Qnetar增扣款条件, 发热量, Qnetar上限, Qnetar下限, "盈亏_Qnetar",
                                    Qnetar增扣款基数, Qnetar增扣款基数单位, Qnetar增付单价, Qnetar增付单价公式, "增付", Qnetar基准增扣价, Qnetar小数处理);
                        }
                    }
                }
                if (合同标准_Qnetar.equals("")) {

                    合同标准_Qnetar = String.valueOf(Mjkg_to_kcalkg(Qnetar下限, 0)) + "-" + String.valueOf(Mjkg_to_kcalkg(Qnetar上限, 0));
                }
            }
            if (计价方式.equals("目录价")) {

                //目录价算法:	结算价=热值基价*挥发份比价*硫分比价*灰分比价*1.05(品种比价)+政策性加价+加价;

                //结算价格=热值基价*挥发份比价*硫分比价*灰分比价*1.05(品种比价)+政策性加价+加价;

                合同标准 = String.valueOf(Qnetar_下限) + "-" + String.valueOf(Qnetar_上限);

                setEvaluate("盈亏_Qnetar", 0, 合同标准, 0);

                合同标准 = String.valueOf(Vdaf_下限) + "-" + String.valueOf(Vdaf_上限);

                setEvaluate("盈亏_Vdaf", 0, 合同标准, 0);

                if (Stad_下限 > 0 || Stad_上限 > 0) {

                    合同标准 = String.valueOf(Stad_下限) + "-" + String.valueOf(Stad_上限);

                    setEvaluate("盈亏_Stad", 0, 合同标准, 0);
                } else if (Std_下限 > 0 || Std_上限 > 0) {

                    合同标准 = String.valueOf(Std_下限) + "-" + String.valueOf(Std_上限);

                    setEvaluate("盈亏_Std", 0, 合同标准, 0);
                }

                if (Aad_下限 > 0 || Aad_上限 > 0) {

                    合同标准 = String.valueOf(Aad_下限) + "-" + String.valueOf(Aad_上限);

                    setEvaluate("盈亏_Aad", 0, 合同标准, 0);

                } else if (Aar_下限 > 0 || Aar_上限 > 0) {

                    合同标准 = String.valueOf(Aar_下限) + "-" + String.valueOf(Aar_上限);

                    setEvaluate("盈亏_Aar", 0, 合同标准, 0);
                }


                if (!用户自定义目录价公式.equals("")) {

                    结算价格 = (double)eval(用户自定义目录价公式);
                } else {

                    结算价格 = 热值基价_Qnetar * 挥发份比价_Vdaf * 硫分比价_Stad * 灰分比价_Aar * 品种比价 + 政策性加价 + 加价;
                }
            }
        }


        //热值按卡扣付_end
        //其它指标折价
        if (Std上限 > 0 || Std下限 > 0) {

            Initialize();

            if (Std增付单价 != 0 || !Std增付单价公式.equals("")) {

                Jiesqtzbzj(Std增扣款条件, Std, Std上限, Std下限, "盈亏_Std", Std增扣款基数,
                        Std增付单价, Std增付单价公式, Std增付价单位, "增付", Std基准增扣价, Std小数处理);
            }

            if (Std扣付单价 != 0 || !Std扣付单价公式.equals("")) {

                Jiesqtzbzj(Std增扣款条件, Std, Std上限, Std下限, "盈亏_Std", Std增扣款基数,
                        Std扣付单价, Std扣付单价公式, Std扣付价单位, "扣付", Std基准增扣价, Std小数处理);
            }
        }

        if (Star上限 > 0 || Star下限 > 0) {

            Initialize();

            if (Star增付单价 > 0 || !Star增付单价公式.equals("")) {

                Jiesqtzbzj(Star增扣款条件, Star, Star上限, Star下限, "盈亏_Star", Star增扣款基数,
                        Star增付单价, Star增付单价公式, Star增付价单位, "增付", Star基准增扣价, Star小数处理);
            }

            if (Star扣付单价 > 0 || !Star扣付单价公式.equals("")) {

                Jiesqtzbzj(Star增扣款条件, Star, Star上限, Star下限, "盈亏_Star", Star增扣款基数,
                        Star扣付单价, Star扣付单价公式, Star扣付价单位, "扣付", Star基准增扣价, Star小数处理);
            }
        }


        if (Ad上限 > 0 || Ad下限 > 0) {


            Initialize();

            if (!(Ad增付单价 == 0) || !Ad增付单价公式.equals("")) {
                Jiesqtzbzj(Ad增扣款条件, Ad, Ad上限, Ad下限, "盈亏_Ad", Ad增扣款基数,
                        Ad增付单价, Ad增付单价公式, Ad增付价单位, "增付", Ad基准增扣价, Ad小数处理);
            }

            if (!(Ad扣付单价 == 0) || !Ad扣付单价公式.equals("")) {

                Jiesqtzbzj(Ad增扣款条件, Ad, Ad上限, Ad下限, "盈亏_Ad", Ad增扣款基数,
                        Ad扣付单价, Ad扣付单价公式, Ad扣付价单位, "扣付", Ad基准增扣价, Ad小数处理);
            }
        }


        if (Vdaf上限 > 0 || Vdaf下限 > 0) {

            Initialize();

            if (Vdaf增付单价 != 0 || !Vdaf增付单价公式.equals("")) {

                Jiesqtzbzj(Vdaf增扣款条件, Vdaf, Vdaf上限, Vdaf下限, "盈亏_Vdaf", Vdaf增扣款基数,
                        Vdaf增付单价, Vdaf增付单价公式, Vdaf增付价单位, "增付", Vdaf基准增扣价, Vdaf小数处理);
            }

            if (Vdaf扣付单价 != 0 || !Vdaf扣付单价公式.equals("")) {

                Jiesqtzbzj(Vdaf增扣款条件, Vdaf, Vdaf上限, Vdaf下限, "盈亏_Vdaf", Vdaf增扣款基数,
                        Vdaf扣付单价, Vdaf扣付单价公式, Vdaf扣付价单位, "扣付", Vdaf基准增扣价, Vdaf小数处理);
            }
        }


        if (Mt上限 > 0 || Mt下限 > 0) {

            Initialize();

            if (Mt增付单价 > 0 || !Mt增付单价公式.equals("")) {

                Jiesqtzbzj(Mt增扣款条件, Mt, Mt上限, Mt下限, "盈亏_Mt", Mt增扣款基数,
                        Mt增付单价, Mt增付单价公式, Mt增付价单位, "增付", Mt基准增扣价, Mt小数处理);
            }

            if (Mt扣付单价 > 0 || !Mt扣付单价公式.equals("")) {
                Jiesqtzbzj(Mt增扣款条件, Mt, Mt上限, Mt下限, "盈亏_Mt", Mt增扣款基数,
                        Mt扣付单价, Mt扣付单价公式, Mt扣付价单位, "扣付", Mt基准增扣价, Mt小数处理);
            }
        }


        if (Qgrad上限 > 0 || Qgrad下限 > 0) {

            Initialize();

            if (Qgrad增付单价 > 0 || !Qgrad增付单价公式.equals("")) {
                Jiesqtzbzj(Qgrad增扣款条件, Qgrad兆焦千克, Qgrad上限, Qgrad下限, "盈亏_Qgrad", Qgrad增扣款基数,
                        Qgrad增付单价, Qgrad增付单价公式, Qgrad增付价单位, "增付", Qgrad基准增扣价, Qgrad小数处理);
            }

            if (Qgrad扣付单价 > 0 || !Qgrad扣付单价公式.equals("")) {
                Jiesqtzbzj(Qgrad增扣款条件, Qgrad兆焦千克, Qgrad上限, Qgrad下限, "盈亏_Qgrad", Qgrad增扣款基数,
                        Qgrad扣付单价, Qgrad扣付单价公式, Qgrad扣付价单位, "扣付", Qgrad基准增扣价, Qgrad小数处理);
            }
        }

        if (Qbad上限 > 0 || Qbad下限 > 0) {

            Initialize();

            if (Qbad增付单价 > 0 || !Qbad增付单价公式.equals("")) {
                Jiesqtzbzj(Qbad增扣款条件, Qbad兆焦千克, Qbad上限, Qbad下限, "盈亏_Qbad", Qbad增扣款基数,
                        Qbad增付单价, Qbad增付单价公式, Qbad增付价单位, "增付", Qbad基准增扣价, Qbad小数处理);
            }

            if (Qbad扣付单价 > 0 || !Qbad扣付单价公式.equals("")) {
                Jiesqtzbzj(Qbad增扣款条件, Qbad兆焦千克, Qbad上限, Qbad下限, "盈亏_Qbad", Qbad增扣款基数,
                        Qbad扣付单价, Qbad扣付单价公式, Qbad扣付价单位, "扣付", Qbad基准增扣价, Qbad小数处理);
            }
        }

        if (Had上限 > 0 || Had下限 > 0) {

            Initialize();

            if (Had增付单价 > 0 || !Had增付单价公式.equals("")) {

                Jiesqtzbzj(Had增扣款条件, Had, Had上限, Had下限, "盈亏_Had", Had增扣款基数,
                        Had增付单价, Had增付单价公式, Had增付价单位, "增付", Had基准增扣价, Had小数处理);
            }

            if (Had扣付单价 > 0 || !Had扣付单价公式.equals("")) {

                Jiesqtzbzj(Had增扣款条件, Had, Had上限, Had下限, "盈亏_Had", Had增扣款基数,
                        Had扣付单价, Had扣付单价公式, Had扣付价单位, "扣付", Had基准增扣价, Had小数处理);
            }

        }

        if (Stad上限 > 0 || Stad下限 > 0) {

            Initialize();

            if (Stad增付单价 > 0 || !Stad增付单价公式.equals("")) {

                Jiesqtzbzj(Stad增扣款条件, Stad, Stad上限, Stad下限, "盈亏_Stad", Stad增扣款基数,
                        Stad增付单价, Stad增付单价公式, Stad增付价单位, "增付", Stad基准增扣价, Stad小数处理);
            }

            if (Stad扣付单价 > 0 || !Stad扣付单价公式.equals("")) {

                Jiesqtzbzj(Stad增扣款条件, Stad, Stad上限, Stad下限, "盈亏_Stad", Stad增扣款基数,
                        Stad扣付单价, Stad扣付单价公式, Stad扣付价单位, "扣付", Stad基准增扣价, Stad小数处理);
            }
        }

        if (Mad上限 > 0 || Mad下限 > 0) {

            Initialize();

            if (Mad增付单价 > 0 || !Mad增付单价公式.equals("")) {

                Jiesqtzbzj(Mad增扣款条件, Mad, Mad上限, Mad下限, "盈亏_Mad", Mad增扣款基数,
                        Mad增付单价, Mad增付单价公式, Mad增付价单位, "增付", Mad基准增扣价, Mad小数处理);
            }

            if (Mad扣付单价 > 0 || !Mad扣付单价公式.equals("")) {

                Jiesqtzbzj(Mad增扣款条件, Mad, Mad上限, Mad下限, "盈亏_Mad", Mad增扣款基数,
                        Mad扣付单价, Mad扣付单价公式, Mad扣付价单位, "扣付", Mad基准增扣价, Mad小数处理);
            }

        }


        if (Aar上限 > 0 || Aar下限 > 0) {

            Initialize();

            if (Aar增付单价 != 0 || !Aar增付单价公式.equals("")) {

                Jiesqtzbzj(Aar增扣款条件, Aar, Aar上限, Aar下限, "盈亏_Aar", Aar增扣款基数,
                        Aar增付单价, Aar增付单价公式, Aar增付价单位, "增付", Aar基准增扣价, Aar小数处理);
            }

            if (Aar扣付单价 != 0 || !Aar扣付单价公式.equals("")) {

                Jiesqtzbzj(Aar增扣款条件, Aar, Aar上限, Aar下限, "盈亏_Aar", Aar增扣款基数,
                        Aar扣付单价, Aar扣付单价公式, Aar扣付价单位, "扣付", Aar基准增扣价, Aar小数处理);
            }
        }

        if (T2上限 > 0 || T2下限 > 0) {

            Initialize();

            if (T2增付单价 > 0 || !T2增付单价公式.equals("")) {

                Jiesqtzbzj(T2增扣款条件, T2, T2上限, T2下限, "盈亏_T2", T2增扣款基数,
                        T2增付单价, T2增付单价公式, T2增付价单位, "增付", T2基准增扣价, T2小数处理);
            }

            if (T2扣付单价 > 0 || !T2扣付单价公式.equals("")) {

                Jiesqtzbzj(T2增扣款条件, T2, T2上限, T2下限, "盈亏_T2", T2增扣款基数,
                        T2扣付单价, T2扣付单价公式, T2扣付价单位, "扣付", T2基准增扣价, T2小数处理);
            }
        }

        if (Aad上限 > 0 || Aad下限 > 0) {

            Initialize();

            if (Aad增付单价 > 0 || !Aad增付单价公式.equals("")) {

                Jiesqtzbzj(Aad增扣款条件, Aad, Aad上限, Aad下限, "盈亏_Aad", Aad增扣款基数,
                        Aad增付单价, Aad增付单价公式, Aad增付价单位, "增付", Aad基准增扣价, Aad小数处理);
            }

            if (Aad扣付单价 > 0 || !Aad扣付单价公式.equals("")) {

                Jiesqtzbzj(Aad增扣款条件, Aad, Aad上限, Aad下限, "盈亏_Aad", Aad增扣款基数,
                        Aad扣付单价, Aad扣付单价公式, Aad扣付价单位, "扣付", Aad基准增扣价, Aad小数处理);
            }

        }

        if (Vad上限 > 0 || Vad下限 > 0) {

            Initialize();

            if (Vad增付单价 > 0 || !Vad增付单价公式.equals("")) {

                Jiesqtzbzj(Vad增扣款条件, Vad, Vad上限, Vad下限, "盈亏_Vad", Vad增扣款基数,
                        Vad增付单价, Vad增付单价公式, Vad增付价单位, "增付", Vad基准增扣价, Vad小数处理);
            }

            if (Vad扣付单价 > 0 || !Vad扣付单价公式.equals("")) {
                Jiesqtzbzj(Vad增扣款条件, Vad, Vad上限, Vad下限, "盈亏_Vad", Vad增扣款基数,
                        Vad扣付单价, Vad扣付单价公式, Vad扣付价单位, "扣付", Vad基准增扣价, Vad小数处理);
            }


        }

        if (运距上限 > 0 || 运距下限 > 0) {

            Initialize();

            if (运距增付单价 > 0 || !运距增付单价公式.equals("")) {

                Jiesqtzbzj(运距增扣款条件, 运距, 运距上限, 运距下限, "盈亏_运距", 运距增扣款基数,
                        运距增付单价, 运距增付单价公式, 运距增付价单位, "增付", 运距基准增扣价, 运距小数处理);
            }

            if (运距扣付单价 > 0 || !运距扣付单价公式.equals("")) {

                Jiesqtzbzj(运距增扣款条件, 运距, 运距上限, 运距下限, "盈亏_运距", 运距增扣款基数,
                        运距扣付单价, 运距扣付单价公式, 运距扣付价单位, "扣付", 运距基准增扣价, 运距小数处理);
            }

        }

        if (结算数量上限 > 0 || 结算数量下限 > 0) {

            Initialize();

            if (结算数量计量单位.equals("吨")) {

                结算数量 = 结算数量吨;
            } else if (结算数量计量单位.equals("万吨")) {

                结算数量 = 结算数量万吨;
            }

            if (结算数量增付单价 > 0 || !结算数量增付单价公式.equals("")) {

                Jiesslzj(结算数量计量单位, 结算数量增付价单位, 结算数量增扣款条件, 结算数量, 结算数量上限, 结算数量下限, "盈亏_结算数量",
                        结算数量增扣款基数, 结算数量增扣款基数单位, 结算数量增付单价, 结算数量增付单价公式, "增付", 结算数量基准增扣价, 结算数量小数处理);
            }

            if (结算数量扣付单价 > 0 || !结算数量扣付单价公式.equals("")) {

                Jiesslzj(结算数量计量单位, 结算数量扣付价单位, 结算数量增扣款条件, 结算数量, 结算数量上限, 结算数量下限, "盈亏_结算数量",
                        结算数量增扣款基数, 结算数量增扣款基数单位, 结算数量扣付单价, 结算数量扣付单价公式, "扣付", 结算数量基准增扣价, 结算数量小数处理);
            }
        }

        //取整操作
        结算价格 = Quzcz(煤款含税单价取整方式, (结算价格 + 折单价_Std + 折单价_Star + 折单价_Ad + 折单价_Vdaf + 折单价_Mt + 折单价_Qgrad + 折单价_Qbad + 折单价_Had + 折单价_Stad + 折单价_Mad
                + 折单价_Aar + 折单价_Aad + 折单价_Vad + 折单价_T2 + 折单价_结算数量 + 折单价_运距), 煤款含税单价保留小数位);

        if (最高煤价 > 0) {

            if (结算价格 > 最高煤价) {

                结算价格 = 最高煤价;
            }
        }

    }

}