package com.zr.utils.math;




import com.zhiren.common.CustomMaths;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by liuzhiyu on 2017/3/13.
 */
public class Math extends CustomMaths {
    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(Object v1, Object v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.add(b2).doubleValue();
    }

    /**
     *
     * @param map 变量的值
     * @param gongs 公式
     * @return 计算结果
     */
    public static double getGongsjg(Map<String, Object> map, String gongs) throws Exception{
        String exp = "";
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey().toUpperCase();
            Object value = entry.getValue();
            exp = gongs.toUpperCase().replaceAll("\\b" + key + "\\b", value.toString());
            gongs = exp;
        }
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        Object result = engine.eval(exp);
        return Double.parseDouble(result.toString());
    }

    public static void test2() throws Exception {
        String str = "43*(2 + 1.4)+2*32/(3-2.1)";
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        Object result = engine.eval(str);
        System.out.println("结果类型:" + result.getClass().getName() + ",计算结果:" + result);
    }

    public static void main(String[] args) throws Exception {
        //test2();
        String substr = "aa aabb aa", regex = "\\baa\\b";

        // prints string1
        System.out.println("String = " + substr);

    /* replaces each substring of this string that matches the given
    regular expression with the given replacement */
        String str2 = substr.replaceAll(regex, "qq");
        System.out.println(str2);
    }
}
