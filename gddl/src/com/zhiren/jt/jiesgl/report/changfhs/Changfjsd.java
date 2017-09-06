package com.zhiren.jt.jiesgl.report.changfhs;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Date;
import org.apache.tapestry.html.BasePage;
import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Money;
import com.zhiren.common.ResultSetList;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;


/**
 * @author 曹林
 * 2009-03-25
 * 描述：1，锦州热电使用大唐国际统一结算单。
 * 2，调整了格式，增加了手写体签名和满足锦州流程的审批。
 * */
/**
 * @author 刘雨
 * 2009-05-27
 * 描述：修改了煤价计算中产生小数位不正确的BUG
 * */

/**
 * @author 王伟
 * 2009-07-05
 * 描述：针对国电结算单的一些修改
 *
 * 	  1 meij为增扣后的价格，如果再加上折价等于增扣了回
 * 		将每个指标的
 * 		meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Qnetar));
 * 		改成(即：合同价)
 * 		meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Qnetar));
 *
 * 	  2 如果结算单的盈吨字段为0时付""值
 *
 *    3 添加运费结算的数量 strjiesyfbz_sl，在计算运费单价时，除以strjiesyfbz_sl，替换 strjiesyfbz_sl
 *
 *    4 判断如果运输单位是公路时不显示运杂费小计
 */
/**
 * @author 张少君
 * 2009-07-07
 * 描述：在系统中增加参数设置，已显示“超/亏吨”
 * */
/**
 * @author 张少君
 * 2009-07-17
 * 描述：在中国大唐的结算单上废除了，“对付地点，付款方式”改为了“合同编号”
 * */

/**
 * @author  王伟
 * 2009-10-31
 * 描述：  修约国电扣款小数位，使其不产生无限小数
 *
 */

/**
 * @author  王伟
 * 2010-01-18
 * 描述：1 根据xitxxb中的配置得到结算单(国电)中结算部门的名称
 * 		INSERT INTO xitxxb VALUES(
getnewid(diancID),         --diancID   电厂ID
1,
diancID,                   --diancID   电厂ID
'结算部门名称',
'计划营销部',                --在结算单结算部门中显示的文字
'',
'结算',
1,
'使用'
)

 *		2 添加获取Visit的方法
 */
/*
 * 作者：玉沙沙
 * 时间：2012-4-20
 * 描述：针对酒泉电厂的结算情况，调整了两票结算类型的结算单显示内容的取数依据
 */
/*
 * 作者：夏峥
 * 时间：2012-9-01
 * 描述：针对青铝电厂的结算情况，调整结算单界面显示内容
 * 		使用参数控制是否显示预结算单信息。
 * insert into xitxxb(id,xuh,diancxxb_id,mingc,zhi,danw,leib,zhuangt,beiz)
 * values(getnewid(1),1,0,'是否读取预结算单信息','是','','结算',1,'使用');
 */
/*
 * 作者：夏峥
 * 时间：2012-9-11
 * 描述：修改界面中存在的BUG
 * 		使用参数控制结算单中实收量信息是否根据运输方式不同进行区分。
		INSERT INTO xitxxb (id,xuh,diancxxb_id,mingc,zhi,leib,zhuangt,beiz)
		VALUES(getnewid(1),1,476,'实收量是否根据运输方式不同进行区分','否','结算查询',1,'使用')
 */
/*
 * 作者：夏峥
 * 时间：2012-9-12
 * 描述：修改运费结算时实收量应取结算数量
 * 		使用参数控制结算单中实收量信息是否根据运输方式不同进行区分。
 */
/*
 * 作者：夏峥
 * 时间：2013-03-02
 * 描述：调整结算单样式针对北仑一发结算单做独立设定
 * 		新增MainGlobal.getXitxx_item("结算", "北仑特殊结算单样式", lgdiancxxb_id, "否").equals("是");参数用于控制结算单样式。
 * 		新增MainGlobal.getXitxx_item("结算", "结算单显示日期类型", lgdiancxxb_id, "结算").equals("结算")参数用于控制结算单显示日期类型。
 * 		新增MainGlobal.getXitxx_item("结算", "结算单是否显示实收数量", lgdiancxxb_id, "是").equals("否")参数用于控制是否显示实收数量。
 */
/*
 * 作者：夏峥
 * 时间：2013-03-26
 * 描述：调整酒泉结算单煤价显示金额
 */
/*
 * 作者：赵胜男
 * 时间：2013-05-16
 * 描述：调整青铝结算单格式
 */
/*
 * 作者：夏峥
 * 时间：2013-09-22
 * 描述：为大开热电增加通过xitxxb设置结算单显示灰分基准
 * 		MainGlobal.getXitxx_item("结算", "结算单显示灰分基准", lgdiancxxb_id, "AAR");
 */
/*
 * 作者：夏峥
 * 时间：2013-09-26
 * 描述：修改由于酒泉电厂的历史原因导致的显示BUG
 */
/*
 * 作者：夏峥
 * 时间：2013-10-14
 * 描述：增加大开电厂结算单页脚特殊设置
 */
/*
 * 作者：夏峥
 * 时间：2013-11-07
 * 描述：修正运费结算单不显示硫份的问题
 * 		将strjiesbz_Stad变更为strjiesbz_Star
 */
/*
 * 作者：夏峥
 * 时间：2013-11-11
 * 描述：修正处理青铝运费结算单不显示硫份的问题时出现的BUG，
 * 		厂方要求结算单界面硫份应显示为Stad，而不是Star，因此需将运费查询质量SQL进行相应调整。
 * 		并将结算单中硫份显示值由strjiesbz_Star变更为strjiesbz_Stad
 */
/*
 * 作者：夏峥
 * 时间：2013-11-11
 * 描述：修正处理青铝运费结算单将汽运单价调整为单价合计。
 */
/*
 * 作者：夏峥
 * 时间：2013-12-06
 * 描述：修正酒泉结算显示BUG
 */
/*
 * 作者：王耀霆
 * 时间：2014-1-08
 * 描述：修改酒泉的结算流程
 */
/*
 * 作者：王耀霆
 * 时间：2014-1-09
 * 描述：原本酒泉strjiesbz_Stad为空，现在已经修改
 */
/*
 * 作者：王耀霆
 * 时间：2014-1-14
 * 描述：对大开的结算单添加其他扣款的值  2682行
 */
/*
 * 作者：王耀霆
 * 时间：2014-1-22
 * 描述：把酒泉结算单里的热值以大卡显示2323行
 */
/*
 * 作者：夏峥
 * 时间：2014-02-10
 * 描述：修正酒泉结算显示BUG
 */
/*
 * 作者：夏峥
 * 时间：2015-04-02
 * 描述：修正结算单签字内容
 */
public class Changfjsd extends BasePage{

    /**
     * Visit
     */
    private Object visit;

    public void setVisit(Object visit) {
        this.visit = visit;
    }

    public Object getVisit() {
        return visit;
    }

    public Changfjsd(){

    }
    /**
     * @param where
     * @param iPageIndex
     * @param tables
     * @return
     */
    public String getChangfjsd(String where,int iPageIndex,String tables){		JDBCcon cn = new JDBCcon();
        Report rt=new Report();
        try{

            String type="";	//标志着结算单类型，目前中国大唐是"ZGDT",国电是"GD"，‘jzrd’是大唐国际锦州公司
            String table1="";
            String table2="";

            if(tables.indexOf(",")>-1){

                table1=tables.substring(0,tables.lastIndexOf(","));
                table2=tables.substring(tables.lastIndexOf(",")+1);
            }else{

                table1=table2=tables;
            }

            String sql="";
            String strjiesrq="";
            String strfahdw="";
            String strmeikdw = "";
            String strfaz="";
            String strdiabch="";
            long 	lgdiancxxb_id=0;
            String strbianh="";
            String strfahksrq="";
            String strfahjzrq="";
            String strfahrq = "";
            String strdiqdm = "";
            String stryuanshr = "";
            String strshoukdw = "";
            String strkaihyh = "";
            String strkaisysrq="";
            String strjiezysrq="";
            String stryansrq = "";
            String strhuowmc = "";
            String strxianshr = "";
            String stryinhzh = "";
            String strfahsl = "";
            String strches = "";
            String stryansbh = "";
            String strfapbh = "";
            String strduifdd = "";
            String strfukfs = "";
            String strshijfk = "";

            String strhetbz_sl="";
            String strgongfbz_sl = "";
            String strchangfys_sl = "";
            String strjiesbz_sl="";
            String strxiancsl_sl = "";
            String strzhejbz_sl = "";
            String strzhehje_sl = "";
            String strjiesyfbz_sl = "0"; //运费结算数量

            String strhetbz_Shulzb = "";		// 合同数量指标
            String strxiancsl_Shulzb = "";		// 数量指标盈亏
            String strzhejbz_Shulzb = "";		// 数量指标折单价
            String strzhehje_Shulzb = "";		// 数量指标折金额

            String strhetbz_Qnetar="";
            String strgongfbz_Qnetar = "";
            String strchangfys_Qnetar = "";
            String strjiesbz_Qnetar="";
            String strxiancsl_Qnetar = "";
            String strzhejbz_Qnetar = "";
            String strzhehje_Qnetar = "";

            String strhetbz_Std = "";
            String strgongfbz_Std = "";
            String strchangfys_Std = "";
            String strjiesbz_Std="";
            String strxiancsl_Std = "";
            String strzhejbz_Std = "";
            String strzhehje_Std = "";

            String strhetbz_Star = "";
            String strgongfbz_Star = "";
            String strchangfys_Star = "";
            String strjiesbz_Star="";
            String strxiancsl_Star = "";
            String strzhejbz_Star = "";
            String strzhehje_Star = "";

            String strhetbz_Ad="";
            String strgongfbz_Ad="";
            String strchangfys_Ad="";
            String strjiesbz_Ad="";
            String strxiancsl_Ad="";
            String strzhejbz_Ad="";
            String strzhehje_Ad="";

            String strhetbz_Vdaf="";
            String strgongfbz_Vdaf="";
            String strchangfys_Vdaf="";
            String strjiesbz_Vdaf="";
            String strxiancsl_Vdaf="";
            String strzhejbz_Vdaf="";
            String strzhehje_Vdaf="";

            String strhetbz_Mt="";
            String strgongfbz_Mt="";
            String strchangfys_Mt="";
            String strjiesbz_Mt="";
            String strxiancsl_Mt="";
            String strzhejbz_Mt="";
            String strzhehje_Mt="";

            String strhetbz_Qgrad="";
            String strgongfbz_Qgrad="";
            String strchangfys_Qgrad="";
            String strjiesbz_Qgrad="";
            String strxiancsl_Qgrad="";
            String strzhejbz_Qgrad="";
            String strzhehje_Qgrad="";

            String strhetbz_Qbad="";
            String strgongfbz_Qbad="";
            String strchangfys_Qbad="";
            String strjiesbz_Qbad="";
            String strxiancsl_Qbad="";
            String strzhejbz_Qbad="";
            String strzhehje_Qbad="";

            String strhetbz_Had="";
            String strgongfbz_Had="";
            String strchangfys_Had="";
            String strjiesbz_Had="";
            String strxiancsl_Had="";
            String strzhejbz_Had="";
            String strzhehje_Had="";

            String strhetbz_Stad="";
            String strgongfbz_Stad="";
            String strchangfys_Stad="";
            String strjiesbz_Stad="";
            String strxiancsl_Stad="";
            String strzhejbz_Stad="";
            String strzhehje_Stad="";

            String strhetbz_Mad="";
            String strgongfbz_Mad="";
            String strchangfys_Mad="";
            String strjiesbz_Mad="";
            String strxiancsl_Mad="";
            String strzhejbz_Mad="";
            String strzhehje_Mad="";

            String strhetbz_Aar="";
            String strgongfbz_Aar="";
            String strchangfys_Aar="";
            String strjiesbz_Aar="";
            String strxiancsl_Aar="";
            String strzhejbz_Aar="";
            String strzhehje_Aar="";

            String strhetbz_Aad="";
            String strgongfbz_Aad="";
            String strchangfys_Aad="";
            String strjiesbz_Aad="";
            String strxiancsl_Aad="";
            String strzhejbz_Aad="";
            String strzhehje_Aad="";

            String strhetbz_Vad="";
            String strgongfbz_Vad="";
            String strchangfys_Vad="";
            String strjiesbz_Vad="";
            String strxiancsl_Vad="";
            String strzhejbz_Vad="";
            String strzhehje_Vad="";

            String strhetbz_T2="";
            String strgongfbz_T2="";
            String strchangfys_T2="";
            String strjiesbz_T2="";
            String strxiancsl_T2="";
            String strzhejbz_T2="";
            String strzhehje_T2="";

            String strhetbz_Yunju="";
            String strgongfbz_Yunju="";
            String strchangfys_Yunju="";
            String strjiesbz_Yunju="";
            String strxiancsl_Yunju="";
            String strzhejbz_Yunju="";
            String strzhehje_Yunju="";

            String strdanj = "";
            String strbuhsdj = "";//运费不含税单价
            String strbuhsdj_m = "";//煤不含税单价
            String strjine = "";
            String strbukouqjk = "";
            String strjiakhj = "";
            String strshuil_mk = "";
            String strshuik_mk = "";
            String strjialhj = "";
            String strtielyf = "";
            String strtielzf = "";
            String strkuangqyf = "";
            String strkuangqzf = "";
            String strbukouqzf = "";
            String strjiskc = "";
            String strbuhsyf = "";
            String strshuil_ys = "";
            String strshuik_ys = "";
            String stryunzshj = "";
            String strhej_dx = "";
            String strhej_xx = "";
            String strbeiz = "";
            String strguohzl = "";
            String stryuns = "";
            String strranlbmjbr=" ";
            String strranlbmjbrq="";
            String strchangcwjbr=" ";
            String strchangcwjbrq="";
            String strzhijzxjbr=" ";
            String strzhijzxjbrq="";
            String strlingdqz=" ";
            String strlingdqzrq="";
            String strzonghcwjbr=" ";
            String strzonghcwjbrq="";
            String strmeikhjdx="";
            String stryunzfhjdx="";
            String strJihkj="";
            double danjc = 0;
            String stryunsfs="";	//运输方式
            String strChaokdl="";	//超亏吨量
            String strChaokdlx="";	//超亏吨类型
            String strHetbh="";	//合同编号
            String jiesdbz="";
            //
            double dblMeik =0;
            double dblYunf =0;
            String strkuidjfyf="";
            String strkuidjfzf="";
            String strbukmk = "";

            int is_yujs=0;//是否预结算
            long chongdjsb_id=0;//冲抵结算表id
            String changfysl="";//厂方验收量
            int jieslx_dat=0;//大同电厂结算类型

            //yuss 2012-4-6  适用于国电酒泉两票结算时的结算单
            String strzhuanxyf="";//专线运费
            String strqicyf="";//汽车运费
            String strzhuangxf="";//装卸费
            String strhulf="";//护路费
            String strjiessl="";//结算量
            String strjiesslcy="";//结算数量差异
            String stryunfhsdj="";//运费含税单价
            String strhansyf="";//含税运费
            //yuss 2012-4-6
            String gongysb_id = null;
            boolean flag = false;  //strchangfys_sl参数规则
            String shisltj = "净重"; //实收量默认按净重统计
            sql = "select * from xitxxb where mingc='实收量统计规则' and zhuangt=1 and leib = '结算'";
            ResultSetList rss = cn.getResultSetList(sql);
            if(rss.next()){
                flag = true;
                shisltj = rss.getString("zhi");
            }


            rss.close();
            sql="select * from "+table1+" where bianm='"+where+"'";
            ResultSet rs = cn.getResultSet(sql);

            int intLeix=3;
            long intDiancjsmkId=0;
            long strkuangfjsmkb_id = -1;
            boolean blnHasMeik =false;		//是否有煤款



            if(rs.next()){

                strHetbh = Jiesdcz.nvlStr(MainGlobal.getTableCol("hetb", "hetbh", "id", rs.getString("hetb_id")));
//				 danjc = rs.getDouble("danjc");
                stryunfhsdj=rs.getString("yunfhsdj");
                gongysb_id=rs.getString("gongysb_id");
                strhansyf=rs.getString("hansyf");
                strbukmk = rs.getString("bukmk");
                lgdiancxxb_id=rs.getLong("diancxxb_id");
                strbianh=rs.getString("bianm");
                strjiesrq=FormatDate(rs.getDate("jiesrq"));
                intLeix=rs.getInt("jieslx");//结算类型0 为两票一个供应商,1为两票两个供应商，2为运费或煤款
                intDiancjsmkId =rs.getLong("id");//煤款id
                strfahdw=rs.getString("gongysmc");//发货单位
                strmeikdw = rs.getString("meikdwmc");//煤矿单位
                strfahksrq=rs.getString("fahksrq");//发货开始日期
                strfahjzrq=rs.getString("fahjzrq");//发货截止日期
                strjiessl=rs.getString("jiessl");
                strjiesslcy=rs.getString("jiesslcy");
                jiesdbz=rs.getString("beiz");
                if(strfahksrq.equals(strfahjzrq)){
                    strfahrq = FormatDate(rs.getDate("fahksrq"));//发货日期
                }else{
                    strfahrq=FormatDate(rs.getDate("fahksrq"))+" 至 "+FormatDate(rs.getDate("fahjzrq"));
                }
                strfaz=rs.getString("faz");
                strdiabch=rs.getString("daibch");
                stryuanshr = rs.getString("yuanshr");//原收货人
                strshoukdw = rs.getString("shoukdw");//收款单位
                strkaihyh = rs.getString("kaihyh");//开户银行
                strkaisysrq=rs.getString("yansksrq");
                strjiezysrq=rs.getString("yansjzrq");
                strJihkj=MainGlobal.getTableCol("jihkjb", "mingc", "id", rs.getString("jihkjb_id"));

                if(strkaisysrq.equals(strjiezysrq)){
                    stryansrq=FormatDate(rs.getDate("yansksrq"));
                }else{
                    stryansrq=FormatDate(rs.getDate("yansksrq"))+" 至 "+FormatDate(rs.getDate("yansjzrq"));
                }
                strhuowmc = rs.getString("MEIZ");//货物名称
                strxianshr = rs.getString("xianshr");//现收货人
                stryinhzh = rs.getString("zhangh");//帐号
                strches = rs.getString("ches");//车数
                stryansbh = rs.getString("yansbh");//验收编号
                strfapbh = rs.getString("fapbh");//发票编号
                strduifdd = nvlStr(rs.getString("duifdd"));//兑付地点
                strfukfs =nvlStr(rs.getString("fukfs")) ;//付款方式
                strshijfk =rs.getString("hansdj");//实际付款
                strbuhsdj_m=rs.getString("buhsdj");	//不含税单价

                if(lgdiancxxb_id==476){
                    strbuhsdj=rs.getString("buhsdj");	//不含税单价
                }

                if(MainGlobal.getXitxx_item("结算", "是否读取预结算单信息", "0", "否").equals("是")){
                    is_yujs=rs.getInt("is_yujsd");//是否预结算单
                    chongdjsb_id=rs.getLong("chongdjsb_id");//冲抵结算表id
                    jieslx_dat=rs.getInt("jieslx_dt");//大同电厂结算类型
                }

                if(rs.getString("yunsfsb_id")!=null){//判断运输方式是否为空
                    stryunsfs=MainGlobal.getTableCol("yunsfsb", "mingc", "id", rs.getString("yunsfsb_id"));
                }
                strChaokdl=String.valueOf(Math.abs(rs.getDouble("chaokdl")));	//超/亏吨量
                strChaokdlx=Jiesdcz.nvlStr(rs.getString("chaokdlx"));	//超亏吨类型

                if(flag){
                    if(shisltj.equals("净重")){
                        strchangfys_sl = rs.getString("guohl");
                    }else if(shisltj.equals("票重")){
                        strchangfys_sl = rs.getString("guohl-yingd+kuid+yuns");
                    }else if(shisltj.equals("结算量")){
                        strchangfys_sl = rs.getString("jiessl");
                    }

                    if(lgdiancxxb_id!=323){//酒泉电厂都保留2位小数
                        if(lgdiancxxb_id==300||lgdiancxxb_id==301||lgdiancxxb_id==302||lgdiancxxb_id==303||lgdiancxxb_id==304){//大同不走这个结算数量小数位设置
                            //大同电厂,不再设置小数位数
                        }else{
                            if(MainGlobal.getXitxx_item("结算查询", "实收量是否根据运输方式不同进行区分", lgdiancxxb_id, "是").equals("是") ){
                                if(rs.getString("yunsfsb_id").equals("1")){
                                    strchangfys_sl = CustomMaths.round(strchangfys_sl,0)+"";
                                }else{
                                    strchangfys_sl = CustomMaths.round(strchangfys_sl,1)+"";
                                }
                            }
                        }
                    }
                }
                sql="select jieszbsjb.*,zhibb.bianm as mingc from jieszbsjb,"+table1+",zhibb "
                        + " where jieszbsjb.jiesdid="+table1+".id and zhibb.id=jieszbsjb.zhibb_id"
                        + " and "+table1+".bianm='"+where+"' and jieszbsjb.zhuangt=1 order by jieszbsjb.id";

                ResultSet rs2=cn.getResultSet(sql);
                while(rs2.next()){

                    if(rs2.getString("mingc").equals(Locale.jiessl_zhibb)){

                        strhetbz_sl = rs2.getString("hetbz");		//合同标准
                        strgongfbz_sl = rs2.getString("gongf");	//供方数量
                        strfahsl=strgongfbz_sl;
                        if(!flag){
                            strchangfys_sl = rs2.getString("CHANGF");	//验收数量
                        }
                        strjiesbz_sl = rs2.getString("JIES");		//结算数量
                        if(lgdiancxxb_id==323){
                            if(intLeix==1&&stryunfhsdj.equals("0")){
                                strxiancsl_sl =strjiesslcy;//酒泉两票结算时，jiesb.jiesslcy肯定是亏吨
                            }else{
                                strxiancsl_sl = String.valueOf((rs2.getDouble("YINGK")>0?0:-rs2.getDouble("YINGK")));//亏吨数量
                            }
                        }else  if(lgdiancxxb_id==300||lgdiancxxb_id==301||lgdiancxxb_id==302||lgdiancxxb_id==303||lgdiancxxb_id==304){
                            //大同二电,因为大同是一厂多制,所以diancxxb_id比较多,
                            strxiancsl_sl =  String.valueOf(rs2.getDouble("YINGK"));//大同不判断盈亏小于0时,显示为0.
                        }else{
                            strxiancsl_sl = String.valueOf((rs2.getDouble("YINGK")>0?(-rs2.getDouble("YINGK")):0));//亏吨数量
                        }

                        strzhejbz_sl = rs2.getString("ZHEJBZ");	//折价标准
                        strzhehje_sl = rs2.getString("ZHEJJE");	//折合金额
                        changfysl=rs2.getString("changf");//厂方验收量,大同用

                    }else if(rs2.getString("mingc").equals(Locale.Shul_zhibb)){

                        strhetbz_Shulzb = rs2.getString("hetbz");
                        strxiancsl_Shulzb = rs2.getString("YINGK");
                        strzhejbz_Shulzb = rs2.getString("ZHEJBZ");
                        strzhehje_Shulzb = rs2.getString("ZHEJJE");

                    }else if(rs2.getString("mingc").equals(Locale.Qnetar_zhibb)){

                        strhetbz_Qnetar = rs2.getString("hetbz");
                        strgongfbz_Qnetar = String.valueOf(Jiesdcz.getUnit_transform(rs2.getString("mingc"),Locale.qiankmqk_danw,rs2.getDouble("GONGF")));	    //供方热量
                        strchangfys_Qnetar = String.valueOf(Jiesdcz.getUnit_transform(rs2.getString("mingc"),Locale.qiankmqk_danw,rs2.getDouble("CHANGF")));	//验收热量
                        strjiesbz_Qnetar = String.valueOf(Jiesdcz.getUnit_transform(rs2.getString("mingc"),Locale.qiankmqk_danw,rs2.getDouble("jies")));		//结算热量
                        strxiancsl_Qnetar = rs2.getString("YINGK"); 		//相差数量热量
                        strzhejbz_Qnetar = rs2.getString("ZHEJBZ");		//折价标准热量
                        strzhehje_Qnetar = rs2.getString("ZHEJJE");	//折合金额热量

                    }else if(rs2.getString("mingc").equals(Locale.Std_zhibb)){

                        strhetbz_Std=rs2.getString("hetbz");
                        strgongfbz_Std = rs2.getString("GONGF");	//供方标准硫分
                        strchangfys_Std = rs2.getString("CHANGF");	//验收硫分
                        strjiesbz_Std=rs2.getString("jies");		//结算硫分
                        strxiancsl_Std = rs2.getString("YINGK");	//相差数量硫分
                        strzhejbz_Std = rs2.getString("ZHEJBZ");	//折价标准硫分
                        strzhehje_Std = rs2.getString("ZHEJJE");	//折合金额硫分

                    }else if(rs2.getString("mingc").equals(Locale.Star_zhibb)){

                        strhetbz_Star=rs2.getString("hetbz");
                        strgongfbz_Star = rs2.getString("GONGF");	//供方标准硫分
                        strchangfys_Star = rs2.getString("CHANGF");	//验收硫分
                        strjiesbz_Star = rs2.getString("jies");		//结算硫分
                        strxiancsl_Star = rs2.getString("YINGK");	//相差数量硫分
                        strzhejbz_Star = rs2.getString("ZHEJBZ");	//折价标准硫分
                        strzhehje_Star = rs2.getString("ZHEJJE");	//折合金额硫分

                    }else if(rs2.getString("mingc").equals(Locale.Ad_zhibb)){

                        strhetbz_Ad=rs2.getString("hetbz");
                        strgongfbz_Ad = rs2.getString("GONGF");//供方标准挥发分
                        strchangfys_Ad = rs2.getString("CHANGF");//验收挥发分
                        strjiesbz_Ad=rs2.getString("jies");//结算挥发分
                        strxiancsl_Ad = rs2.getString("YINGK");//相差数量挥发分
                        strzhejbz_Ad = rs2.getString("ZHEJBZ");//折价标准挥发分
                        strzhehje_Ad = rs2.getString("ZHEJJE");//折合金额挥发分

                    }else if(rs2.getString("mingc").equals(Locale.Vdaf_zhibb)){

                        strhetbz_Vdaf=rs2.getString("hetbz");
                        strgongfbz_Vdaf = rs2.getString("GONGF");//供方标准发分
                        strchangfys_Vdaf =rs2.getString("CHANGF");//验收发分
                        strjiesbz_Vdaf=rs2.getString("jies");//结算灰分
                        strxiancsl_Vdaf = rs2.getString("YINGK");//相差数量发分
                        strzhejbz_Vdaf =rs2.getString("ZHEJBZ");//折价标准发分
                        strzhehje_Vdaf =rs2.getString("ZHEJJE");//折合金额发分

                    }else if(rs2.getString("mingc").equals(Locale.Mt_zhibb)){

                        strhetbz_Mt=rs2.getString("hetbz");
                        strgongfbz_Mt = rs2.getString("GONGF");//供方标准水分
                        strchangfys_Mt = rs2.getString("CHANGF");//验收水分
                        strjiesbz_Mt=rs2.getString("jies");//结算水分
                        strxiancsl_Mt = rs2.getString("YINGK");//相差数量水分
                        strzhejbz_Mt = rs2.getString("ZHEJBZ");//折价标准水分
                        strzhehje_Mt = rs2.getString("ZHEJJE");//折合金额水分

                    }else if(rs2.getString("mingc").equals(Locale.Qgrad_zhibb)){

                        strhetbz_Qgrad=rs2.getString("hetbz");
                        strgongfbz_Qgrad = rs2.getString("GONGF");		//供方标准
                        strchangfys_Qgrad = rs2.getString("CHANGF");	//验收
                        strjiesbz_Qgrad=rs2.getString("jies");			//结算
                        strxiancsl_Qgrad = rs2.getString("YINGK");		//相差数量
                        strzhejbz_Qgrad = rs2.getString("ZHEJBZ");		//折价标准
                        strzhehje_Qgrad = rs2.getString("ZHEJJE");		//折合金额

                    }else if(rs2.getString("mingc").equals(Locale.Qbad_zhibb)){

                        strhetbz_Qbad=rs2.getString("hetbz");
                        strgongfbz_Qbad = rs2.getString("GONGF");		//供方标准
                        strchangfys_Qbad = rs2.getString("CHANGF");	//验收
                        strjiesbz_Qbad=rs2.getString("jies");			//结算
                        strxiancsl_Qbad = rs2.getString("YINGK");		//相差数量
                        strzhejbz_Qbad = rs2.getString("ZHEJBZ");		//折价标准
                        strzhehje_Qbad = rs2.getString("ZHEJJE");		//折合金额

                    }else if(rs2.getString("mingc").equals(Locale.Had_zhibb)){

                        strhetbz_Had=rs2.getString("hetbz");
                        strgongfbz_Had = rs2.getString("GONGF");	//供方标准
                        strchangfys_Had = rs2.getString("CHANGF");	//验收
                        strjiesbz_Had=rs2.getString("jies");		//结算
                        strxiancsl_Had = rs2.getString("YINGK");	//相差数量
                        strzhejbz_Had = rs2.getString("ZHEJBZ");	//折价标准
                        strzhehje_Had = rs2.getString("ZHEJJE");	//折合金额

                    }else if(rs2.getString("mingc").equals(Locale.Stad_zhibb)){

                        strhetbz_Stad=rs2.getString("hetbz");
                        strgongfbz_Stad = rs2.getString("GONGF");	//供方标准
                        strchangfys_Stad = rs2.getString("CHANGF");	//验收
                        strjiesbz_Stad = rs2.getString("jies");		//结算
                        strxiancsl_Stad = rs2.getString("YINGK");	//相差数量
                        strzhejbz_Stad = rs2.getString("ZHEJBZ");	//折价标准
                        strzhehje_Stad = rs2.getString("ZHEJJE");	//折合金额

                    }else if(rs2.getString("mingc").equals(Locale.Mad_zhibb)){

                        strhetbz_Mad=rs2.getString("hetbz");
                        strgongfbz_Mad = rs2.getString("GONGF");	//供方标准
                        strchangfys_Mad = rs2.getString("CHANGF");	//验收
                        strjiesbz_Mad = rs2.getString("jies");		//结算
                        strxiancsl_Mad = rs2.getString("YINGK");	//相差数量
                        strzhejbz_Mad = rs2.getString("ZHEJBZ");	//折价标准
                        strzhehje_Mad = rs2.getString("ZHEJJE");	//折合金额

                    }else if(rs2.getString("mingc").equals(Locale.Aar_zhibb)){

                        strhetbz_Aar=rs2.getString("hetbz");
                        strgongfbz_Aar = rs2.getString("GONGF");	//供方标准
                        strchangfys_Aar = rs2.getString("CHANGF");	//验收
                        strjiesbz_Aar = rs2.getString("jies");		//结算
                        strxiancsl_Aar = rs2.getString("YINGK");	//相差数量
                        strzhejbz_Aar = rs2.getString("ZHEJBZ");	//折价标准
                        strzhehje_Aar = rs2.getString("ZHEJJE");	//折合金额

                    }else if(rs2.getString("mingc").equals(Locale.Aad_zhibb)){

                        strhetbz_Aad=rs2.getString("hetbz");
                        strgongfbz_Aad = rs2.getString("GONGF");	//供方标准
                        strchangfys_Aad = rs2.getString("CHANGF");	//验收
                        strjiesbz_Aad = rs2.getString("jies");		//结算
                        strxiancsl_Aad = rs2.getString("YINGK");	//相差数量
                        strzhejbz_Aad = rs2.getString("ZHEJBZ");	//折价标准
                        strzhehje_Aad = rs2.getString("ZHEJJE");	//折合金额

                    }else if(rs2.getString("mingc").equals(Locale.Vad_zhibb)){

                        strhetbz_Vad=rs2.getString("hetbz");
                        strgongfbz_Vad = rs2.getString("GONGF");	//供方标准
                        strchangfys_Vad = rs2.getString("CHANGF");	//验收
                        strjiesbz_Vad = rs2.getString("jies");		//结算
                        strxiancsl_Vad = rs2.getString("YINGK");	//相差数量
                        strzhejbz_Vad = rs2.getString("ZHEJBZ");	//折价标准
                        strzhehje_Vad = rs2.getString("ZHEJJE");	//折合金额

                    }else if(rs2.getString("mingc").equals(Locale.T2_zhibb)){

                        strhetbz_T2=rs2.getString("hetbz");
                        strgongfbz_T2 = rs2.getString("GONGF");	//供方标准
                        strchangfys_T2 = rs2.getString("CHANGF");	//验收
                        strjiesbz_T2 = rs2.getString("jies");		//结算
                        strxiancsl_T2 = rs2.getString("YINGK");	//相差数量
                        strzhejbz_T2 = rs2.getString("ZHEJBZ");	//折价标准
                        strzhehje_T2 = rs2.getString("ZHEJJE");	//折合金额

                    }else if(rs2.getString("mingc").equals(Locale.Yunju_zhibb)){

                        strhetbz_Yunju=rs2.getString("hetbz");
                        strgongfbz_Yunju = rs2.getString("GONGF");		//供方标准
                        strchangfys_Yunju = rs2.getString("CHANGF");	//验收
                        strjiesbz_Yunju = rs2.getString("jies");		//结算
                        strxiancsl_Yunju = rs2.getString("YINGK");		//相差数量
                        strzhejbz_Yunju = rs2.getString("ZHEJBZ");		//折价标准
                        strzhehje_Yunju = rs2.getString("ZHEJJE");		//折合金额

                    }
                }

                rs2.close();

                //********************其他*****************//
                strdanj = rs.getString("hansdj");		//单价
                strjine = rs.getString("meikje");		//金额
                strbukouqjk = rs.getString("bukmk");	//补(扣)以前价款
                strjiakhj = rs.getString("buhsmk");	//价款合计
                strshuil_mk = rs.getString("shuil");	//税率(煤矿)
                strshuik_mk = rs.getString("shuik");	//税款(煤矿)
                strjialhj = rs.getString("hansmk");	//价税合计
                strguohzl =rs.getString("GUOHL");		//过衡重量
                stryuns =rs.getString("jiesslcy");		//运损(结算数量差异)
                strbeiz = nvlStr(rs.getString("beiz"));//备注
                dblMeik= Double.parseDouble(strjialhj);
                strranlbmjbr=rs.getString("ranlbmjbr");
                strranlbmjbrq=FormatDate(rs.getDate("ranlbmjbrq"));
                blnHasMeik=true;

            }


            double chongdjssl=0;//冲抵结算数量(大同用)
            if(chongdjsb_id!=0){//如果chongdjsb_id不等于0,证明是冲抵结算单
                String sql_chongdjsb="select jiessl from jiesb where id="+chongdjsb_id;
                ResultSetList rss2=cn.getResultSetList(sql_chongdjsb);
                if(rss2.next()){
                    chongdjssl=rss2.getDouble("jiessl");
                }
                rss2.close();
            }






//			1, 两票结算;
//			2, 煤款结算
//			3, 国铁运费
//			4, 地铁运费

            if ((blnHasMeik)&&(intLeix==1)){

                if(lgdiancxxb_id==323&&stryunfhsdj.equals("0")){
                    sql="select * from "+table2+" where bianm='"+where+"'";

                    ResultSet rs3=cn.getResultSet(sql);
                    if (rs3.next()){
                        stryunzshj = rs3.getString("hansyf");//运杂费合计
                        String jiesyfb_id=rs3.getString("id");//jiesyfb_id
                        String sql_feiy=
                                "select feiymcb.mingc mingc, f.zhi zhi\n" +
                                        "  from (select feiyxmb_id, zhi\n" +
                                        "          from feiyb\n" +
                                        "         where feiyb_id =\n" +
                                        "               (select feiyb_id\n" +
                                        "                  from yunfdjb\n" +
                                        "                 where id = (select distinct yunfdjb_id\n" +
                                        "                               from danjcpb\n" +
                                        "                              where yunfjsb_id = "+jiesyfb_id+"))) f,\n" +
                                        "       feiyxmb,\n" +
                                        "       feiymcb\n" +
                                        " where f.feiyxmb_id = feiyxmb.id\n" +
                                        "   and feiymcb.id = feiyxmb.feiymcb_id";
                        ResultSet rs_feiy=cn.getResultSet(sql_feiy);
                        while(rs_feiy.next()){
                            if(rs_feiy.getString("mingc").equals("国铁运费")){
                                strtielyf=rs_feiy.getString("zhi");
                            }else if(rs_feiy.getString("mingc").equals("专线运费")){
                                strzhuanxyf=rs_feiy.getString("zhi");
                            }else if(rs_feiy.getString("mingc").equals("汽车运费")){
                                strqicyf=rs_feiy.getString("zhi");
                            }else if(rs_feiy.getString("mingc").equals("装卸费")){
                                strzhuangxf=rs_feiy.getString("zhi");
                            }else{//护路费
                                strhulf=rs_feiy.getString("zhi");
                            }

                        }
                        rs_feiy.close();
                    }

                    rs3.close();
                }else{
                    sql="select * from "+table2+" where bianm='"+where+"'";

                    ResultSet rs3=cn.getResultSet(sql);
                    if (rs3.next()){

                        strtielyf =rs3.getString("GUOTYF");//铁路运费
                        strtielzf = rs3.getString("guotzf");//杂费
                        strkuangqyf = rs3.getString("kuangqyf");//矿区运费
                        strkuangqzf = rs3.getString("kuangqzf");//矿区杂费
                        strbukouqzf = rs3.getString("bukyf");//补(扣)以前运杂费
                        strjiskc = rs3.getString("JISKC");//计税扣除
                        strbuhsyf =rs3.getString("buhsyf");//不含税运费
                        strshuil_ys = rs3.getString("shuil");//税率(运费)
                        strshuik_ys = rs3.getString("shuik");//税款(运费)
                        stryunzshj = rs3.getString("hansyf");//运杂费合计
                        strshijfk = rs3.getString("hansdj");
                        dblYunf=rs3.getDouble("hansyf");
                        strkuidjfyf = rs3.getString("kuidjfyf");
                        strkuidjfzf = rs3.getString("kuidjfzf");
//					 strjiesyfbz_sl = rs3.getString("jiessl"); //运费结算数量
                        if(lgdiancxxb_id==300||lgdiancxxb_id==301||lgdiancxxb_id==302||lgdiancxxb_id==303||lgdiancxxb_id==304){
                            //大同二电,因为大同是一厂多制,所以diancxxb_id比较多,
                            //大同运费结算数量去jiessl
                            strjiesyfbz_sl = rs3.getString("jiessl");
                        }else{
                            strjiesyfbz_sl = rs3.getString("gongfsl");
                        }

                    }

                    rs3.close();
                }
            }else if(intLeix!=2){

                if( stryuanshr.equals("国电电力酒泉发电有限公司")){//当是酒泉电厂时
                    sql="select * from "+table2+"  where bianm='"+where+"'";
                    rs=cn.getResultSet(sql);
                    if(rs.next()){
//							 strshijfk =rs.getString("hansdj");		
                        lgdiancxxb_id=rs.getLong("diancxxb_id");
                        strbianh=rs.getString("bianm");
                        strjiesrq=FormatDate(rs.getDate("jiesrq"));
                        intLeix=rs.getInt("jieslx");//结算类型0 为两票一个供应商,1为两票两个供应商，2为运费或煤款
                        //					 intDiancjsmkId =rs.getInt("id");//煤款id
                        strfahdw=rs.getString("gongysmc");
                        strmeikdw = rs.getString("meikdwmc");
                        strfahksrq=rs.getString("fahksrq");
                        strfahjzrq=rs.getString("fahjzrq");
                        if(strfahksrq.equals(strfahjzrq)){
                            strfahrq = FormatDate(rs.getDate("fahksrq"));//发货日期
                        }else{
                            strfahrq=FormatDate(rs.getDate("fahksrq"))+" 至 "+FormatDate(rs.getDate("fahjzrq"));
                        }
                        //					 strfahrq = rs.getString("fahrq");//发货日期

                        strHetbh = Jiesdcz.nvlStr(MainGlobal.getTableCol("hetys", "hetbh", "id", rs.getString("hetb_id")));
                        strfaz=rs.getString("faz");
                        strdiabch=rs.getString("daibch");
                        stryuanshr = rs.getString("yuanshr");//原收货人
                        strshoukdw = rs.getString("shoukdw");//收款单位
                        strkaihyh = rs.getString("kaihyh");//开户银行
                        strkaisysrq=rs.getString("yansksrq");
                        strjiezysrq=rs.getString("yansjzrq");

                        strgongfbz_sl=rs.getString("gongfsl");
                        strchangfys_sl=rs.getString("guohl");
                        if(!flag){
                            strchangfys_sl=rs.getString("yanssl");
                        }
                        if(flag){
                            if(rs.getString("yunsfsb_id").equals("1")){
                                strchangfys_sl = CustomMaths.round(strchangfys_sl,0)+"";
                            }else{
                                strchangfys_sl = CustomMaths.round(strchangfys_sl,1)+"";
                            }
                        }
                        strjiesbz_sl=rs.getString("jiessl");
                        strxiancsl_sl=String.valueOf(-rs.getDouble("yingk"));


                        if(strkaisysrq.equals(strjiezysrq)){
                            stryansrq=FormatDate(rs.getDate("yansksrq"));
                        }else{
                            stryansrq=FormatDate(rs.getDate("yansksrq"))+" 至 "+FormatDate(rs.getDate("yansjzrq"));
                        }
                        //					 stryansrq = rs.getString("yansrq");//验收日期
                        strhuowmc = rs.getString("MEIZ");//货物名称
                        strxianshr = rs.getString("xianshr");//现收货人
                        stryinhzh = rs.getString("zhangh");//帐号
                        strfahsl =rs.getString("gongfsl");//发运数量？？？？？？？？？？？？？？？？？？
                        strches = rs.getString("ches");//车数
                        stryansbh = rs.getString("yansbh");//验收编号
                        strfapbh = rs.getString("fapbh");//发票编号
                        strduifdd = rs.getString("duifdd");//兑付地点
                        strfukfs = rs.getString("fukfs") ;//付款方式
                        strdiqdm="";
                        strshijfk =rs.getString("hansdj");//实际付款？？？？？？？？？？？？？？？？？
                        stryunsfs=MainGlobal.getTableCol("yunsfsb", "mingc", "id", rs.getString("yunsfsb_id"));

//							 2009-3-12 zsj增运费显示
                        strtielyf =rs.getString("GUOTYF");//铁路运费
                        strtielzf = rs.getString("guotzf");//杂费
                        strkuangqyf = rs.getString("kuangqyf");//矿区运费
                        strkuangqzf = rs.getString("kuangqzf");//矿区杂费
                        strbukouqzf = rs.getString("bukyf");//补(扣)以前运杂费
                        strjiskc = rs.getString("JISKC");//计税扣除
                        strbuhsyf =rs.getString("buhsyf");//不含税运费
                        strshuil_ys = rs.getString("shuil");//税率(运费)
                        strshuik_ys = rs.getString("shuik");//税款(运费)
                        stryunzshj = rs.getString("hansyf");//运杂费合计
                        dblYunf=rs.getDouble("hansyf");
                        strbuhsdj=String.valueOf(CustomMaths.mul(rs.getDouble("hansdj"),CustomMaths.sub(1,rs.getDouble("shuil"))));
//							 strjiesyfbz_sl = rs.getString("jiessl"); //运费结算数量
                        strjiesyfbz_sl = rs.getString("gongfsl");

                        strhetbz_Qnetar = "";	//合同
                        strgongfbz_Qnetar = "";//供方热量
                        strchangfys_Qnetar = "";//验收热量
                        strjiesbz_Qnetar = "";//结算标准
                        strxiancsl_Qnetar= "";//相差数量热量
                        strzhejbz_Qnetar = "";//折价标准热量
                        strzhehje_Qnetar = "";//折合金额热量

                        strhetbz_Std = "";		//合同
                        strgongfbz_Std = "";	//供方标准硫分
                        strchangfys_Std = "";	//验收硫分
                        strjiesbz_Std = "";	//结算标准
                        strxiancsl_Std = "";	//相差数量硫分
                        strzhejbz_Std = "";	//折价标准硫分
                        strzhehje_Std = "";	//折合金额硫分

                        strhetbz_Ad = "";		//合同
                        strgongfbz_Ad = "";	//供方标准挥发分
                        strchangfys_Ad = "";	//验收挥发分
                        strjiesbz_Ad = "";		//结算标准
                        strxiancsl_Ad = "";	//相差数量挥发分
                        strzhejbz_Ad = "";		//折价标准挥发分
                        strzhehje_Ad = "";		//折合金额挥发分

                        strhetbz_Vdaf = "";		//供方标准发分
                        strgongfbz_Vdaf = "";		//供方标准发分
                        strchangfys_Vdaf = "";		//验收发分
                        strjiesbz_Vdaf = "";		//结算标准
                        strxiancsl_Vdaf = "";		//相差数量发分
                        strzhejbz_Vdaf = "";		//折价标准发分
                        strzhehje_Vdaf = "";		//折合金额发分

                        strhetbz_Mt="";
                        strgongfbz_Mt = "";		//供方标准水分
                        strchangfys_Mt = "";		//验收水分
                        strjiesbz_Mt = "";			//结算标准
                        strxiancsl_Mt = "";		//相差数量水分
                        strzhejbz_Mt = "";			//折价标准水分
                        strzhehje_Mt = "";			//折合金额水分

                        strhetbz_Qgrad="";
                        strgongfbz_Qgrad = "";		//供方标准水分
                        strchangfys_Qgrad = "";	//验收水分
                        strjiesbz_Qgrad = "";		//结算标准
                        strxiancsl_Qgrad = "";		//相差数量水分
                        strzhejbz_Qgrad = "";		//折价标准水分
                        strzhehje_Qgrad = "";		//折合金额水分

                        strhetbz_Qbad="";
                        strgongfbz_Qbad = "";		//供方标准水分
                        strchangfys_Qbad = "";		//验收水分
                        strjiesbz_Qbad = "";		//结算标准
                        strxiancsl_Qbad = "";		//相差数量水分
                        strzhejbz_Qbad = "";		//折价标准水分
                        strzhehje_Qbad = "";		//折合金额水分

                        strhetbz_sl = "";			//合同数量
//							 strgongfbz_sl = "";		//供方数量
//							 strchangfys_sl ="";		//验收数量
//							 strjiesbz_sl = "";			//结算标准
//							 strxiancsl_sl = "";		//相差数量
                        strzhejbz_sl ="";			//亏吨数量
                        strzhehje_sl = "";			//折合金额



//							 strjiessl = rs.getString("jiessl");//结算数量
//							 strjiesbz_sl = rs.getString("gongfsl");
//							 strdanj = (double)Math.round(a);//单价
							 /*strjine = "0";//金额
							 strbukouqjk = "0";//补(扣)以前价款
							 strjiakhj = "0";//价款合计
							 strshuil_mk = "0";//税率(煤矿)
							 strshuik_mk = "0";//税款(煤矿)
							 strjialhj = "0";//价税合计
*/							 strguohzl =rs.getString("GUOHL");//过衡重量
                        strbeiz = nvlStr(rs.getString("beiz"));//备注
                        dblMeik= Double.parseDouble(strjialhj);
                        blnHasMeik=true;

                        strranlbmjbr=rs.getString("ranlbmjbr");
                        strranlbmjbrq=FormatDate(rs.getDate("ranlbmjbrq"));

                        strkuidjfyf = rs.getString("kuidjfyf");
                        strkuidjfzf = rs.getString("kuidjfzf");
                    }

                }else{

                    sql="select * from "+table2+"  where bianm='"+where+"'";
                    rs=cn.getResultSet(sql);
                    if(rs.next()){
//						 strshijfk =rs.getString("hansdj");		
                        lgdiancxxb_id=rs.getLong("diancxxb_id");
                        strbianh=rs.getString("bianm");
                        strjiesrq=FormatDate(rs.getDate("jiesrq"));
                        intLeix=rs.getInt("jieslx");//结算类型0 为两票一个供应商,1为两票两个供应商，2为运费或煤款
                        //					 intDiancjsmkId =rs.getInt("id");//煤款id
                        strfahdw=rs.getString("gongysmc");
                        strmeikdw = rs.getString("meikdwmc");
                        strfahksrq=rs.getString("fahksrq");
                        strfahjzrq=rs.getString("fahjzrq");
                        if(strfahksrq.equals(strfahjzrq)){
                            strfahrq = FormatDate(rs.getDate("fahksrq"));//发货日期
                        }else{
                            strfahrq=FormatDate(rs.getDate("fahksrq"))+" 至 "+FormatDate(rs.getDate("fahjzrq"));
                        }
                        //					 strfahrq = rs.getString("fahrq");//发货日期

                        strHetbh = Jiesdcz.nvlStr(MainGlobal.getTableCol("hetys", "hetbh", "id", rs.getString("hetb_id")));
                        strfaz=rs.getString("faz");
                        strdiabch=rs.getString("daibch");
                        stryuanshr = rs.getString("yuanshr");//原收货人
                        strshoukdw = rs.getString("shoukdw");//收款单位
                        strkaihyh = rs.getString("kaihyh");//开户银行
                        strkaisysrq=rs.getString("yansksrq");
                        strjiezysrq=rs.getString("yansjzrq");

                        strgongfbz_sl=rs.getString("gongfsl");
                        strchangfys_sl=rs.getString("guohl");
                        if(!flag){
                            strchangfys_sl=rs.getString("yanssl");
                        }
//						 使用参数判断，是否按照据运输方式不同进行取整方式的区分
                        if(flag){
                            if(MainGlobal.getXitxx_item("结算查询", "实收量是否根据运输方式不同进行区分", lgdiancxxb_id, "是").equals("是") ){
                                if(rs.getString("yunsfsb_id").equals("1")){
                                    strchangfys_sl = CustomMaths.round(strchangfys_sl,0)+"";
                                }else{
                                    strchangfys_sl = CustomMaths.round(strchangfys_sl,1)+"";
                                }
                            }
                        }
//						 青铝电厂运费结算时，实收量采用结算数量
                        if(lgdiancxxb_id==476){
                            strchangfys_sl=rs.getString("jiessl");
                        }

                        strjiesbz_sl=rs.getString("jiessl");
                        strxiancsl_sl=String.valueOf(-rs.getDouble("yingk"));


                        if(strkaisysrq.equals(strjiezysrq)){
                            stryansrq=FormatDate(rs.getDate("yansksrq"));
                        }else{
                            stryansrq=FormatDate(rs.getDate("yansksrq"))+" 至 "+FormatDate(rs.getDate("yansjzrq"));
                        }
                        //					 stryansrq = rs.getString("yansrq");//验收日期
                        strhuowmc = rs.getString("MEIZ");//货物名称
                        strxianshr = rs.getString("xianshr");//现收货人
                        stryinhzh = rs.getString("zhangh");//帐号
                        strfahsl =rs.getString("gongfsl");//发运数量？？？？？？？？？？？？？？？？？？
                        strches = rs.getString("ches");//车数
                        stryansbh = rs.getString("yansbh");//验收编号
                        strfapbh = rs.getString("fapbh");//发票编号
                        strduifdd = rs.getString("duifdd");//兑付地点
                        strfukfs = rs.getString("fukfs") ;//付款方式
                        strdiqdm="";
                        strshijfk =rs.getString("hansdj");//实际付款？？？？？？？？？？？？？？？？？
                        stryunsfs=MainGlobal.getTableCol("yunsfsb", "mingc", "id", rs.getString("yunsfsb_id"));

//						 2009-3-12 zsj增运费显示
                        strtielyf =rs.getString("GUOTYF");//铁路运费
                        strtielzf = rs.getString("guotzf");//杂费
                        strkuangqyf = rs.getString("kuangqyf");//矿区运费
                        strkuangqzf = rs.getString("kuangqzf");//矿区杂费
                        strbukouqzf = rs.getString("bukyf");//补(扣)以前运杂费
                        strjiskc = rs.getString("JISKC");//计税扣除
                        strbuhsyf =rs.getString("buhsyf");//不含税运费
                        strshuil_ys = rs.getString("shuil");//税率(运费)
                        strshuik_ys = rs.getString("shuik");//税款(运费)
                        stryunzshj = rs.getString("hansyf");//运杂费合计
                        dblYunf=rs.getDouble("hansyf");
                        strbuhsdj=String.valueOf(CustomMaths.mul(rs.getDouble("hansdj"),CustomMaths.sub(1,rs.getDouble("shuil"))));
//						 strjiesyfbz_sl = rs.getString("jiessl"); //运费结算数量



                        if(lgdiancxxb_id==300||lgdiancxxb_id==301||lgdiancxxb_id==302||lgdiancxxb_id==303||lgdiancxxb_id==304){
                            //大同电厂,实收数量显示结算量,
                            //运费结算量就显示jiessl
                            strchangfys_sl=CustomMaths.round(rs.getString("jiessl"),2)+"";
                            strjiesyfbz_sl = rs.getString("jiessl"); //运费结算数量

                        }else{
                            strjiesyfbz_sl = rs.getString("gongfsl");
                        }

                        strhetbz_Qnetar = "";	//合同
                        strgongfbz_Qnetar = "";//供方热量
                        strchangfys_Qnetar = "";//验收热量
                        strjiesbz_Qnetar = "";//结算标准
                        strxiancsl_Qnetar= "";//相差数量热量
                        strzhejbz_Qnetar = "";//折价标准热量
                        strzhehje_Qnetar = "";//折合金额热量

                        strhetbz_Std = "";		//合同
                        strgongfbz_Std = "";	//供方标准硫分
                        strchangfys_Std = "";	//验收硫分
                        strjiesbz_Std = "";	//结算标准
                        strxiancsl_Std = "";	//相差数量硫分
                        strzhejbz_Std = "";	//折价标准硫分
                        strzhehje_Std = "";	//折合金额硫分

                        strhetbz_Ad = "";		//合同
                        strgongfbz_Ad = "";	//供方标准挥发分
                        strchangfys_Ad = "";	//验收挥发分
                        strjiesbz_Ad = "";		//结算标准
                        strxiancsl_Ad = "";	//相差数量挥发分
                        strzhejbz_Ad = "";		//折价标准挥发分
                        strzhehje_Ad = "";		//折合金额挥发分

                        strhetbz_Vdaf = "";		//供方标准发分
                        strgongfbz_Vdaf = "";		//供方标准发分
                        strchangfys_Vdaf = "";		//验收发分
                        strjiesbz_Vdaf = "";		//结算标准
                        strxiancsl_Vdaf = "";		//相差数量发分
                        strzhejbz_Vdaf = "";		//折价标准发分
                        strzhehje_Vdaf = "";		//折合金额发分

                        strhetbz_Mt="";
                        strgongfbz_Mt = "";		//供方标准水分
                        strchangfys_Mt = "";		//验收水分
                        strjiesbz_Mt = "";			//结算标准
                        strxiancsl_Mt = "";		//相差数量水分
                        strzhejbz_Mt = "";			//折价标准水分
                        strzhehje_Mt = "";			//折合金额水分

                        strhetbz_Qgrad="";
                        strgongfbz_Qgrad = "";		//供方标准水分
                        strchangfys_Qgrad = "";	//验收水分
                        strjiesbz_Qgrad = "";		//结算标准
                        strxiancsl_Qgrad = "";		//相差数量水分
                        strzhejbz_Qgrad = "";		//折价标准水分
                        strzhehje_Qgrad = "";		//折合金额水分

                        strhetbz_Qbad="";
                        strgongfbz_Qbad = "";		//供方标准水分
                        strchangfys_Qbad = "";		//验收水分
                        strjiesbz_Qbad = "";		//结算标准
                        strxiancsl_Qbad = "";		//相差数量水分
                        strzhejbz_Qbad = "";		//折价标准水分
                        strzhehje_Qbad = "";		//折合金额水分

                        strhetbz_sl = "";			//合同数量
//						 strgongfbz_sl = "";		//供方数量
//						 strchangfys_sl ="";		//验收数量
//						 strjiesbz_sl = "";			//结算标准
//						 strxiancsl_sl = "";		//相差数量
                        strzhejbz_sl ="";			//亏吨数量
                        strzhehje_sl = "";			//折合金额



//						 strjiessl = rs.getString("jiessl");//结算数量
//						 strjiesbz_sl = rs.getString("gongfsl");
//						 strdanj = (double)Math.round(a);//单价
                        strjine = "0";//金额
                        strbukouqjk = "0";//补(扣)以前价款
                        strjiakhj = "0";//价款合计
                        strshuil_mk = "0";//税率(煤矿)
                        strshuik_mk = "0";//税款(煤矿)
                        strjialhj = "0";//价税合计
                        strguohzl =rs.getString("GUOHL");//过衡重量
                        strbeiz = nvlStr(rs.getString("beiz"));//备注
                        dblMeik= Double.parseDouble(strjialhj);
                        blnHasMeik=true;

                        strranlbmjbr=rs.getString("ranlbmjbr");
                        strranlbmjbrq=FormatDate(rs.getDate("ranlbmjbrq"));

                        strkuidjfyf = rs.getString("kuidjfyf");
                        strkuidjfzf = rs.getString("kuidjfzf");
                    }
                }
            }

            Money money=new Money();
            //计算合计
            strhej_xx=format(dblYunf+dblMeik,"0.00");
            strmeikhjdx=money.NumToRMBStr(dblMeik);
            stryunzfhjdx=money.NumToRMBStr(dblYunf);
            strhej_dx=money.NumToRMBStr(dblYunf+dblMeik);

            rs.close();
            cn.Close();
//			 控制低位热、挥发分、灰分、水分隐藏显示

            boolean Qnetar_bn=false;
            boolean Yunju_bn=false;	//运距
            boolean Shulzb_bn=false;	//数量指标
            boolean Ad_bn=false;
            boolean Vdaf_bn=false;
            boolean Mt_bn=false;
            boolean Qgrad_bn=false;
            boolean Qbad_bn=false;
            boolean Had_bn=false;
            boolean Stad_bn=false;
            boolean Mad_bn=false;
            boolean Aar_bn=false;
            boolean Aad_bn=false;
            boolean Vad_bn=false;
            boolean T2_bn=false;
            boolean Star_bn=false;

            //设置指标所在的行数
            int Qnetar_row=7;
            int Std_row=8;
            int Shulzb_row=9;
            int Ad_row=10;
            int Vdaf_row=11;
            int Mt_row=12;
            int Qgrad_row=13;
            int Qbad_row=14;
            int Had_row=15;
            int Stad_row=16;
            int Mad_row=17;
            int Aar_row=18;
            int Aad_row=19;
            int Vad_row=20;
            int T2_row=21;
            int Yunju_row=22;
            int Star_row=23;

            //设置指标字段不显示

            if(strhetbz_Shulzb.equals("")||strhetbz_Shulzb.equals("0")){
                Shulzb_bn=true;
            }
            if(strjiesbz_Qnetar.equals("")||strjiesbz_Qnetar.equals("0")){
                Qnetar_bn=true;
            }
            if(strjiesbz_Yunju.equals("")||strjiesbz_Yunju.equals("0")){
                Yunju_bn=true;
            }
            if(strjiesbz_Ad.equals("")||strjiesbz_Ad.equals("0")){
                Ad_bn=true;
            }
            if(strjiesbz_Vdaf.equals("")||strjiesbz_Vdaf.equals("0")){
                Vdaf_bn=true;
            }
            if(strjiesbz_Mt.equals("")||strjiesbz_Mt.equals("0")){
                Mt_bn=true;
            }
            if(strjiesbz_Qgrad.equals("")||strjiesbz_Qgrad.equals("0")){
                Qgrad_bn=true;
            }
            if(strjiesbz_Qbad.equals("")||strjiesbz_Qbad.equals("0")){
                Qbad_bn=true;
            }
            if(strjiesbz_Had.equals("")||strjiesbz_Had.equals("0")){
                Had_bn=true;
            }
            if(strjiesbz_Stad.equals("")||strjiesbz_Stad.equals("0")){
                Stad_bn=true;
            }
            if(strjiesbz_Mad.equals("")||strjiesbz_Mad.equals("0")){
                Mad_bn=true;
            }
            if(strjiesbz_Aar.equals("")||strjiesbz_Aar.equals("0")){
                Aar_bn=true;
            }
            if(strjiesbz_Aad.equals("")||strjiesbz_Aad.equals("0")){
                Aad_bn=true;
            }
            if(strjiesbz_Vad.equals("")||strjiesbz_Vad.equals("0")){
                Vad_bn=true;
            }
            if(strjiesbz_T2.equals("")||strjiesbz_T2.equals("0")){
                T2_bn=true;
            }
            if(strjiesbz_Star.equals("")||strjiesbz_Star.equals("0")){

                Star_bn=true;
            }

            type=MainGlobal.getXitxx_item("结算", "结算单所属单位", String.valueOf(lgdiancxxb_id), "GD");
            if(type.equals("GD")){//国电电力
//					 临时处理，青铝电厂

                String strGonghdc="";//供货地区
                String strYunsdw="";//运输单位
                String strHansdj="";//含税单价
                String strBuhsdj="";//不含税单价
                JDBCcon cn1 = new JDBCcon();
                String sql1=
                        "SELECT MAX(YD.MINGC) AS YUNSDW FROM CHEPB CP,YUNSDWB YD\n" +
                                "       WHERE CP.YUNSDWB_ID = YD.ID\n" +
                                "             AND CP.ID IN (\n" +
                                "SELECT CHEPB_ID FROM DANJCPB WHERE YUNFJSB_ID = (select id from jiesyfb where bianm ='"+where+"'))";
                ResultSetList rs1 = cn1.getResultSetList(sql1);
                while(rs1.next()){
                    strYunsdw = rs1.getString("YUNSDW");
                }
                rs1.close();
                String sql2 =
                        "select m.mingc as mingc from jiesyfb jy,meikxxb m where jy.meikxxb_id = m.id and jy.bianm='"+where+"'";
                ResultSetList rs2 = cn1.getResultSetList(sql2);
                while(rs2.next()){
                    strGonghdc = rs2.getString("mingc");
                }
                rs2.close();
                String sql3=
                        "select  jy.hansdj,(jy.hansdj-round_new(jy.hansdj*jy.shuil,2)) as buhsdj  from jiesyfb jy where jy.bianm='"+where+"'";
                ResultSetList rs3 = cn1.getResultSetList(sql3);
                while(rs3.next()){
                    strHansdj = rs3.getString("hansdj");
                    strBuhsdj = rs3.getString("buhsdj");
                }

                String tianbdw=getTianzdw(lgdiancxxb_id);//填制单位。（可根据条件来填入单位）
                if (strzhejbz_sl.equals("")){
                    strzhejbz_sl="0";
                }
                double meij=Double.parseDouble(strzhejbz_sl);//煤价
                double mkoukxj=0;//扣款小计
                boolean bTl_yunsfs=false;
                boolean bGl_yunsfs=false;

                if(!strzhejbz_Qnetar.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Qnetar));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Qnetar));
                }

                if(!strzhejbz_Std.equals("")){
                    meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Std));
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Std));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Std));
                }

                if(!strzhejbz_Ad.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Ad));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Ad));
                }

                if(!strzhejbz_Vdaf.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Vdaf));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Vdaf));
                }

                if(!strzhejbz_Mt.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Mt));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Mt));
                }

                if(!strzhejbz_Qgrad.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Qgrad));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Qgrad));
                }

                if(!strzhejbz_Qbad.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Qbad));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Qbad));
                }

                if(!strzhejbz_Had.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Had));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Had));
                }

                if(!strzhejbz_Stad.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Stad));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Stad));
                }

                if(!strzhejbz_Mad.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Mad));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Mad));
                }

                if(!strzhejbz_Aar.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Aar));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Aar));
                }

                if(!strzhejbz_Aad.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Aad));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Aad));
                }

                if(!strzhejbz_Vad.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Vad));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Vad));
                }

                if(!strzhejbz_T2.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_T2));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_T2));
                }

                if(!strzhejbz_Yunju.equals("")){
                    meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Yunju));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Yunju));
                }

                if(!strzhejbz_Star.equals("")){
                    meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Star));
                    mkoukxj+=(-Double.parseDouble(strzhejbz_Star));
                }

                //格式扣款小计，避免出项多小数位
                mkoukxj = CustomMaths.Round_new(mkoukxj, 2);

                if(stryunsfs.equals(Locale.tiel_yunsfs)){// 铁路
                    bTl_yunsfs=true;
                }else if(stryunsfs.equals(Locale.gongl_yunsfs)){

                    bGl_yunsfs=true;
                }


                String Relkk="";	//热量扣款
                String Huifkk="";	//灰分扣款
                String Huiffkk="";	//挥发分扣款
                String Shuifkk="";	//水分扣款
                String liufkk="";	//硫分扣款
                String Qitkk="";	//其他扣款

                Relkk=String.valueOf((-Double.parseDouble(strzhejbz_Qnetar.equals("")?"0":strzhejbz_Qnetar))
                        +(-Double.parseDouble(strzhejbz_Qgrad.equals("")?"0":strzhejbz_Qgrad))
                        +(-Double.parseDouble(strzhejbz_Qbad.equals("")?"0":strzhejbz_Qbad)));

                Huifkk=String.valueOf((-Double.parseDouble(strzhejbz_Ad.equals("")?"0":strzhejbz_Ad))
                        +(-Double.parseDouble(strzhejbz_Aar.equals("")?"0":strzhejbz_Aar))
                        +(-Double.parseDouble(strzhejbz_Aad.equals("")?"0":strzhejbz_Aad)));

                Huiffkk=String.valueOf((-Double.parseDouble(strzhejbz_Vdaf.equals("")?"0":strzhejbz_Vdaf))
                        +(-Double.parseDouble(strzhejbz_Vad.equals("")?"0":strzhejbz_Vad)));

                Shuifkk=String.valueOf((-Double.parseDouble(strzhejbz_Mt.equals("")?"0":strzhejbz_Mt))
                        +(-Double.parseDouble(strzhejbz_Mad.equals("")?"0":strzhejbz_Mad)));

                liufkk=String.valueOf((-Double.parseDouble(strzhejbz_Std.equals("")?"0":strzhejbz_Std))
                        +(-Double.parseDouble(strzhejbz_Stad.equals("")?"0":strzhejbz_Stad))
                        +(-Double.parseDouble(strzhejbz_Star.equals("")?"0":strzhejbz_Star))
                );

                Qitkk=String.valueOf((-Double.parseDouble(strzhejbz_T2.equals("")?"0":strzhejbz_T2))
                        +(-Double.parseDouble(strzhejbz_Yunju.equals("")?"0":strzhejbz_Yunju)));


                if(Relkk.equals("-0.0")){

                    Relkk="";
                }

                if(Huifkk.equals("-0.0")){

                    Huifkk="";
                }

                if(Huiffkk.equals("-0.0")){

                    Huiffkk="";
                }

                if(Shuifkk.equals("-0.0")){

                    Shuifkk="";
                }

                if(liufkk.equals("-0.0")){

                    liufkk="";
                }

                if(Qitkk.equals("-0.0")){

                    Qitkk="";
                }


                //				 新增liuf增扣款累加
                if((strjiesbz_Std.equals("")||strjiesbz_Std.equals("0"))
                        &&(strjiesbz_Star.equals("")||strjiesbz_Star.equals("0"))){

                    strjiesbz_Std="";
                }else if(!strjiesbz_Star.equals("")
                        &&!strjiesbz_Star.equals("0")){

                    if(!strjiesbz_Std.equals("")){

                        strjiesbz_Std=String.valueOf(Double.parseDouble(strjiesbz_Star)
                                +Double.parseDouble(strjiesbz_Std));
                    }else{

                        strjiesbz_Std=strjiesbz_Star;
                    }
                }

                //				 国电的结算单要显示除结算折价指标之外的其它指标
                //				 2010-01-19 ww
                //				 添加Aar、Star指标，国电结算单中显示收到基指标
                String JieszbArray[] = null;
                JieszbArray = getJieszbxx(table1,where);

                //				 直取danpcjsmxb中的热值

                strjiesbz_Qnetar = JieszbArray[0];

                if(strjiesbz_Ad.equals("")){

                    strjiesbz_Ad = JieszbArray[8];
                }

                if(strjiesbz_Aar.equals("")) {
                    strjiesbz_Aar = JieszbArray[9];
                }

                if(strjiesbz_Vdaf.equals("")){

                    strjiesbz_Vdaf = JieszbArray[4];
                }

                if(strjiesbz_Mt.equals("")){

                    strjiesbz_Mt = JieszbArray[5];
                }

                if(strjiesbz_Std.equals("")){

                    strjiesbz_Std = JieszbArray[1];
                }

                if(strjiesbz_Star.equals("")) {
                    strjiesbz_Star = JieszbArray[3];
                }

                if(strjiesbz_Stad.equals("")) {
                    strjiesbz_Stad = JieszbArray[2];
                }

                if (strjiesbz_Qnetar.equals("")){
                    strjiesbz_Qnetar="0";
                }
                if (strjiesbz_Aar.equals("")){
                    strjiesbz_Aar="0";
                }
                if (strjiesbz_Vdaf.equals("")){
                    strjiesbz_Vdaf="0";
                }
                if (strjiesbz_Mt.equals("")){
                    strjiesbz_Mt="0";
                }
                if (strjiesbz_Star.equals("")){
                    strjiesbz_Star="0";
                }
                if (strjiesbz_Std.equals("")){
                    strjiesbz_Std="0";
                }
                if (strjiesbz_Stad.equals("")){
                    strjiesbz_Stad="0";
                }

                if(intLeix!=Locale.meikjs_feiylbb_id && intLeix!=Locale.liangpjs_feiylbb_id){
//						 结算类型不等于两票和煤款

//						 让运费结算单上显示指标信息
                    long jiesyfb_id = 0;
                    jiesyfb_id = MainGlobal.getTableId(table2, "bianm", where);

                    if("".equals(strchangfys_Yunju)){
//							 得到运距
                        strchangfys_Yunju = MainGlobal.getTableCol(table2, "nvl(yunj,0)", "id = "+jiesyfb_id);

                        if(Double.parseDouble(strchangfys_Yunju)==0){

                            sql1 =
                                    "SELECT nvl(max(zhi),0) AS lic FROM licb\n" +
                                            "       WHERE liclxb_id = (SELECT ID FROM liclxb WHERE mingc='国铁')\n" +
                                            "             AND licb.faz_id = (SELECT id FROM chezxxb WHERE mingc='"+strfaz+"')";

                            rs3 = cn1.getResultSetList(sql1);
                            if(rs3.next()){

                                strchangfys_Yunju = rs3.getString("lic");
                            }
                        }
                    }

                    sql1 =
                            "SELECT\n" +
                                    "       decode(SUM(nvl(jingz,0)),0,0,round_new(SUM(jingz*nvl(qnet_ar,0))/SUM(jingz),2)) AS qnet_ar,\n" +
                                    "       decode(SUM(nvl(jingz,0)),0,0,round_new(SUM(jingz*nvl(aar,0))/SUM(jingz),2)) AS aar,\n" +
                                    "       decode(SUM(nvl(jingz,0)),0,0,round_new(SUM(jingz*nvl(ad,0))/SUM(jingz),2)) AS ad,\n" +
                                    "       decode(SUM(nvl(jingz,0)),0,0,round_new(SUM(jingz*nvl(vdaf,0))/SUM(jingz),2)) AS vdaf,\n" +
                                    "       decode(SUM(nvl(jingz,0)),0,0,round_new(SUM(jingz*nvl(mt,0))/SUM(jingz),1)) AS mt,\n" +
                                    "       decode(SUM(nvl(jingz,0)),0,0,round_new(SUM(jingz*nvl(Stad,0))/SUM(jingz),2)) AS stad,\n" +
                                    "       decode(SUM(nvl(jingz,0)),0,0,round_new(SUM(jingz*nvl(Stad,0))/SUM(jingz),2)) AS std,\n" +
                                    "       decode(SUM(nvl(jingz,0)),0,0,round_new(SUM(jingz*nvl(Star,0))/SUM(jingz),2)) AS star\n" +
                                    " FROM\n" +
                                    "--数量\n" +
                                    "(SELECT fahb_id,SUM(maoz-piz-zongkd) AS jingz FROM chepb WHERE ID IN (\n" +
                                    "  SELECT chepb_id FROM danjcpb d\n" +
                                    "         WHERE d.yunfjsb_id = "+jiesyfb_id+")\n" +
                                    "         GROUP BY fahb_id) sl,\n" +
                                    "\n" +
                                    "--质量\n" +
                                    "(SELECT fahb.id AS fahb_id,zhilb.* FROM fahb,zhilb WHERE fahb.zhilb_id = zhilb.id AND fahb.ID IN (\n" +
                                    "       SELECT fahb_id FROM chepb WHERE ID IN (\n" +
                                    "              SELECT chepb_id FROM danjcpb d\n" +
                                    "                     WHERE d.yunfjsb_id = "+jiesyfb_id+"))\n" +
                                    ") zl\n" +
                                    "WHERE sl.fahb_id = zl.fahb_id";

                    rs3 = cn1.getResultSetList(sql1);

                    if(rs3.next()){

                        if(strjiesbz_Qnetar.equals("0")){

                            strjiesbz_Qnetar = rs3.getString("qnet_ar");
                        }
                        if(strjiesbz_Aar.equals("0")){

                            strjiesbz_Aar = rs3.getString("aar");
                        }
                        if(strjiesbz_Ad.equals("0")){

                            strjiesbz_Ad = rs3.getString("ad");
                        }
                        if(strjiesbz_Vdaf.equals("0")){

                            strjiesbz_Vdaf = rs3.getString("vdaf");
                        }
                        if(strjiesbz_Mt.equals("0")){

                            strjiesbz_Mt = rs3.getString("mt");
                        }
                        if(strjiesbz_Star.equals("0")){

                            strjiesbz_Star = rs3.getString("star");
                        }
                        if(strjiesbz_Stad.equals("0")){

                            strjiesbz_Stad = rs3.getString("stad");
                        }
                        if(strjiesbz_Std.equals("0")){

                            strjiesbz_Std = rs3.getString("std");
                        }
                    }
                }
                rs3.close();
                cn1.Close();

                String strjiesbz_Shulzj="";
                sql = "select * from danpcjsmxb d,jiesb j where d.zhibb_id=21 and d.jiesdid= j.id and j.bianm='"+where+"'";
                ResultSetList rs4 = cn.getResultSetList(sql);
                if(rs4.next()){
                    strjiesbz_Shulzj = rs4.getString("zhejje");
                }
                rs4.close();
                //				 国电用MJ/kg
                strjiesbz_Qnetar=String.valueOf(MainGlobal.kcalkg_to_Mjkg(Double.parseDouble(strjiesbz_Qnetar),
                        ((Visit)this.getVisit()).getFarldec()));		//结算热量

                //根据xitxxb中的配置得到结算单中结算部门的名称
                String BuM = "燃料管理部";
                BuM = MainGlobal.getXitxx_item("结算", "结算部门名称", ""+lgdiancxxb_id, BuM);
                JDBCcon con=new JDBCcon();
                ResultSet frs=con.getResultSet("select f.url from hetb h inner join hetfjb f on h.id=f.hetid where h.hetbh='"+strHetbh+"' and f.url is not null");
                String url="#";
                if(frs.next()){
                    url=frs.getString("url");
                }
                strHetbh="<a href='"+url+"'>"+strHetbh+"</a>";
                String ArrHeader[][]=new String[5][19];
                ArrHeader[0]=new String[] {"","","","","","","","","","","","","","","","","","",""};
//					 ArrHeader[1]=new String[] {"国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司"};
//					 ArrHeader[2]=new String[] {"SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD."};
                ArrHeader[1]=new String[] {"燃  料  （ 煤 炭 ）  结  算  单","","","","","","","","","","","","","","","","","","",};
                ArrHeader[2]=new String[] {"","","","","","","","","","","","","","","","","","",""};
                ArrHeader[3]=new String[] {"单位："+tianbdw,"","","","","","到货日期："+stryansrq,"","","","","","","","单位：吨,元/吨,MJ/kg,%,元","","","","","","",};
                ArrHeader[4]=new String[] {"结算单编号："+strbianh,"","","","","","","","","","","","","","合同编号："+strHetbh,"","","","",""};


                String ArrBody[][]=new String[20][19];
                ArrBody[0]=new String[] {"结算部门：" + BuM,"","","供货单位："+strfahdw,"","","","","","运输单位："+strYunsdw,"","","","运输单位："+strYunsdw,"","","日期："+strjiesrq,"",""};//"品种："+strhuowmc,""};
                ArrBody[1]=new String[] {"数量","","","","","单价","","","","","","","","","","","煤款","","税金"};
                ArrBody[2]=new String[] {"车数","票重","盈吨","亏吨","实收","煤价","扣款","","","","","","","单价合计","不含税价","","","",""};
                ArrBody[3]=new String[] {"","","","","","","热值","灰分","挥发分","水分","硫分","其他","小计","","","","","",""};
                ArrBody[4]=new String[] {""+strches+"",""+strfahsl+"",""+("0.0".equals(strxiancsl_sl)?"":-Double.parseDouble(strxiancsl_sl)+"")+"",""+(Double.parseDouble(strxiancsl_sl)<0?"":""+strxiancsl_sl)+""+"",""+strchangfys_sl+"",""+meij+"",""+
                        ("".equals(Relkk)?"0":-Double.parseDouble(Relkk))+"",""+
                        ("".equals(Huifkk)?"0":-Double.parseDouble(Huifkk))+"",""+("".equals(Huiffkk)?"0":-Double.parseDouble(Huiffkk))+"",""+
                        ("".equals(Shuifkk)?"0":-Double.parseDouble(Shuifkk))+"",""+("".equals(liufkk)?"0":-Double.parseDouble(liufkk))+"",""+
                        ("".equals(Qitkk)?"0":-Double.parseDouble(Qitkk))+"",""+(-mkoukxj)+"",
                        ""+strzhejbz_sl+"",""+strbuhsdj+"","",""+formatq(strjiakhj)+"","",""+formatq(strshuik_mk)+""};
                ArrBody[5]=new String[] {"热值","灰分<br>A,d","挥发分<br>Vdaf","水分<br>Mt","硫分<br>St,d","应付价款","","应付税金","","其他扣款","","实付金额","","","","","","",""};
                ArrBody[6]=new String[] {""+ (((Visit)this.getVisit()).getFarldec()==3 ? new DecimalFormat("0.000").format(Double.parseDouble(strjiesbz_Qnetar)) : strjiesbz_Qnetar)+"<br>("+new DecimalFormat("0").format(CustomMaths.Round_new(Double.parseDouble(strjiesbz_Qnetar)/0.0041816,0))+")",
                        ""+ ("".equals(strjiesbz_Ad)?"":new DecimalFormat("0.00").format(Double.parseDouble(strjiesbz_Ad)))+"",""+
                        ("".equals(strjiesbz_Vdaf)?"":new DecimalFormat("0.00").format(Double.parseDouble(strjiesbz_Vdaf)))+"",""+
                        ("".equals(strjiesbz_Mt)?"":new DecimalFormat("0.00").format(Double.parseDouble(strjiesbz_Mt)))+"",""+
                        ("".equals(strjiesbz_Std)?"":new DecimalFormat("0.00").format(Double.parseDouble(strjiesbz_Std)))+"",""+strjiakhj+"","",""+formatq(strshuik_mk)+"","",""+strbukmk,"",""+formatq(strjialhj)+"","","","","","","",""};
                ArrBody[7]=new String[] {"运距","","运费单价明细","","","","","","","","","","","","","","","","印花税"};
                ArrBody[8]=new String[] {"国铁","地铁","国铁","地铁","矿运","专线","短运","汽运","其他运费","","","","","","运杂费","","","",""};
                ArrBody[9]=new String[] {"","","","","","","","","电附加","风沙","储装","道口","其它","小计","取送车","变更费","单价合计","不含税价",""};
                ArrBody[10]=new String[] {strchangfys_Yunju,//1
                        "",//2
                        ""+(bTl_yunsfs?String.valueOf(CustomMaths.Round_New(Double.parseDouble(strtielyf.equals("")?"0":strtielyf)/(Double.parseDouble(strjiesyfbz_sl)==0?1:Double.parseDouble(strjiesyfbz_sl)),2)):"")+"",
                        "",//4
                        ""+CustomMaths.Round_New(Double.parseDouble(strkuangqyf.equals("")?"0":strkuangqyf)/(Double.parseDouble(strjiesyfbz_sl)==0?1:Double.parseDouble(strjiesyfbz_sl)),2)+"",
                        "",//6
                        "",//7
                        ""+strHansdj+"",//8
                        "",//9
                        "",//10
                        "",//11
                        "",//12
                        "",//13
                        ""+(bTl_yunsfs?String.valueOf(CustomMaths.Round_New(Double.parseDouble(stryunzshj.equals("")?"0":stryunzshj)/Double.parseDouble(strches)==0?1:Double.parseDouble(strches),2)):"")+"",
                        "",//15
                        "",//16
                        ""+strHansdj+"",//17
                        ""+strBuhsdj+"",//18
                        ""};//19
                ArrBody[11]=new String[] {"国铁运费","","地铁运费","","矿区运费","","专线运费","","短途运费","","汽车运费","","其他运费","","运杂费","","扣款","","实付运费金额"};
                ArrBody[12]=new String[] {"","","","","","","","","","","","","","","","","扣款","",""};
                ArrBody[13]=new String[] {""+(bTl_yunsfs?strtielyf:"")+"","","","",""+strkuangqyf+"","","","","","",""+(bGl_yunsfs?strtielyf:"")+"","","","","","",""+strbukouqzf+"","",""+stryunzshj+""};
                ArrBody[14]=new String[] {"备注："+(jiesdbz==null?"":jiesdbz),"","","","","","","","","","","","","","","","","",""};
                ArrBody[15]=new String[] {"注：根据本企业资金管理制度权限履行会审及审批程序，会审部门及审批人写明意见、签名及日期。","","","","","","","","","","","","","","","","","",""};
                ArrBody[16]=new String[] {"","","","","","","","","","","","","","","","","","",""};
//----------------------------------
                sql="select fuid from gongysb where id="+gongysb_id;
                long fuid=0;
                ResultSet grs=con.getResultSet(sql);
                if(grs.next()){
                    fuid=grs.getLong("fuid");
                }

                ArrBody[17]=new String[] {"","","","","","","","","","","","","","","","","","",""};
                if(fuid==9382){
                    ArrBody[18]=new String[] {"经理:","","","主管经营领导:","","","","燃料管理部:","","","","财务审核:","","","","核算:","","",""};
                }else{
                    ArrBody[18]=new String[] {"经理:","","主管经营领导:","","","","燃料管理部:","","","","财务审核:","","","输煤除灰部:","","","核算:","",""};

                }
                ArrBody[19]=new String[] {"","","","","","","","","","","","","","","","&nbsp","","",""};

                int ArrWidth[]=new int[] {54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54};
//                int ArrWidthTitle[]=new int[] {220,170,320,160,160};
                //				 定义页Title
                rt.setTitle(new Table(ArrHeader,0,0,0));
                rt.setBody(new Table(ArrBody,0,0,0));
                if(fuid==9382){
                    rt.body.mergeCell(19,4,19,5);
                    rt.body.mergeCell(19,8,19,9);
                    rt.body.mergeCell(19,12,19,13);
                }else {
                    rt.body.mergeCell(19,3,19,4);
                    rt.body.mergeCell(19,7,19,8);
                    rt.body.mergeCell(19,11,19,12);
                    rt.body.mergeCell(19,14,19,15);
                }

                rt.body.setWidth(ArrWidth);
                rt.title.setWidth(ArrWidth);

                //				 合并单元格
                //				 表头_Begin
                //rt.title.mergeCell(1, 9, 1, 19);
                rt.title.mergeCell(2, 1, 2, 19);

                rt.title.mergeCell(3, 1, 3, 19);
                rt.title.mergeCell(4, 1, 4, 6);
                rt.title.mergeCell(4, 7, 4, 12);
                rt.title.mergeCell(4, 15, 4, 19);
                rt.title.mergeCell(5, 1, 5, 5);
                rt.title.mergeCell(5, 15, 5, 19);

                rt.title.getCell(5,4).align=Table.ALIGN_RIGHT;
                rt.title.getCell(5,5).align=Table.ALIGN_RIGHT;

//                rt.title.getCell(5,1).setBorderNone();
//                rt.title.getCell(5,2).setBorderNone();
//                rt.title.getCell(5,3).setBorderNone();
//                rt.title.getCell(5,4).setBorderNone();
//                rt.title.getCell(5,5).setBorderNone();
                for(int i=0;i<rt.title.getCols();i++){
                    rt.title.getCell(5,i).setBorderNone();
                }
                rt.title.setBorder(0,0,0,0);
                rt.title.setRowCells(1,Table.PER_BORDER_BOTTOM,0);
                rt.title.setRowCells(2,Table.PER_BORDER_BOTTOM,0);
                rt.title.setRowCells(3,Table.PER_BORDER_BOTTOM,0);
                rt.title.setRowCells(4,Table.PER_BORDER_BOTTOM,0);

                rt.title.setRowCells(1,Table.PER_BORDER_RIGHT,0);
                rt.title.setRowCells(2,Table.PER_BORDER_RIGHT,0);
                rt.title.setRowCells(3,Table.PER_BORDER_RIGHT,0);
                rt.title.setRowCells(4,Table.PER_BORDER_RIGHT,0);

                rt.title.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_CENTER);
                rt.title.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);
                rt.title.setRowCells(3, Table.PER_ALIGN, Table.ALIGN_CENTER);

                rt.title.setCellAlign(4, 1, Table.ALIGN_LEFT);
                rt.title.setCellAlign(4, 2, Table.ALIGN_LEFT);
                rt.title.setCellAlign(4, 3, Table.ALIGN_CENTER);
                rt.title.setCellAlign(4, 4, Table.ALIGN_LEFT);
                rt.title.setCellAlign(4, 5, Table.ALIGN_RIGHT);

                //				 字体
                rt.title.setCells(2, 1, 2, 5, Table.PER_FONTNAME, "隶书");
                rt.title.setCells(2, 1, 2, 5, Table.PER_FONTSIZE, 20);
                //				 字体

                //				 图片
                rt.title.setCellImage(1, 1, 228, 38, "imgs/report/GDTLogo_new.gif");	//国电的标志（到现场要一个换上就行了）
                rt.title.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_LEFT);
                rt.title.setCellImage(3, 1, rt.title.getWidth()/3+30, 10, "imgs/report/GDHX.gif");
                //				 图片_End

                //				 表头_End
                //				 表体_Begin
                rt.body.mergeCell(1,1,1,3);
                rt.body.mergeCell(1,4,1,9);
//                rt.body.mergeCell(1,10,1,13);
                rt.body.mergeCell(1,10,1,16);
                rt.body.mergeCell(1,17,1,19);
                rt.body.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_LEFT);

                rt.body.mergeCell(2,1,2,5);
                rt.body.mergeCell(2,6,2,16);
                rt.body.mergeCell(2,17,4,18);
                rt.body.mergeCell(2,19,4,19);
                rt.body.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);

                rt.body.mergeCell(3,1,4,1);
                rt.body.mergeCell(3,2,4,2);
                rt.body.mergeCell(3,3,4,3);
                rt.body.mergeCell(3,4,4,4);
                rt.body.mergeCell(3,5,4,5);
                rt.body.mergeCell(3,6,4,6);
                rt.body.mergeCell(3,7,3,13);
                rt.body.mergeCell(3,14,4,14);
                rt.body.mergeCell(3,15,4,16);
                rt.body.setRowCells(3, Table.PER_ALIGN, Table.ALIGN_CENTER);

                rt.body.setRowCells(4, Table.PER_ALIGN, Table.ALIGN_CENTER);

                rt.body.mergeCell(5,15,5,16);
                rt.body.mergeCell(5,17,5,18);
                rt.body.setRowCells(5, Table.PER_ALIGN, Table.ALIGN_CENTER);

                rt.body.mergeCell(6,6,6,7);
                rt.body.mergeCell(6,8,6,9);
                rt.body.mergeCell(6,10,6,11);
                rt.body.mergeCell(6,12,6,14);
                rt.body.mergeCell(6,15,6,16);
                rt.body.mergeCell(6,17,6,18);
                rt.body.setRowCells(6, Table.PER_ALIGN, Table.ALIGN_CENTER);

                rt.body.mergeCell(7,6,7,7);
                rt.body.mergeCell(7,8,7,9);
                rt.body.mergeCell(7,10,7,11);
                rt.body.mergeCell(7,12,7,14);
                rt.body.mergeCell(7,15,7,16);
                rt.body.mergeCell(7,17,7,18);
                rt.body.setRowCells(7, Table.PER_ALIGN, Table.ALIGN_CENTER);

                rt.body.mergeCell(8,1,8,2);
                rt.body.mergeCell(8,3,8,18);
                rt.body.mergeCell(8,19,10,19);
                rt.body.setRowCells(8, Table.PER_ALIGN, Table.ALIGN_CENTER);

                rt.body.mergeCell(9,1,10,1);
                rt.body.mergeCell(9,2,10,2);
                rt.body.mergeCell(9,3,10,3);
                rt.body.mergeCell(9,4,10,4);
                rt.body.mergeCell(9,5,10,5);
                rt.body.mergeCell(9,6,10,6);
                rt.body.mergeCell(9,7,10,7);
                rt.body.mergeCell(9,8,10,8);
                rt.body.mergeCell(9,9,9,14);
                rt.body.mergeCell(9,15,9,18);
                rt.body.setRowCells(9, Table.PER_ALIGN, Table.ALIGN_CENTER);

                rt.body.setRowCells(10, Table.PER_ALIGN, Table.ALIGN_CENTER);

                rt.body.setRowCells(11, Table.PER_ALIGN, Table.ALIGN_CENTER);

                rt.body.mergeCell(12,1,13,2);
                rt.body.mergeCell(12,3,13,4);
                rt.body.mergeCell(12,5,13,6);
                rt.body.mergeCell(12,7,13,8);
                rt.body.mergeCell(12,9,13,10);
                rt.body.mergeCell(12,11,13,12);
                rt.body.mergeCell(12,13,13,14);
                rt.body.mergeCell(12,15,13,16);
                rt.body.mergeCell(12,17,13,18);
                rt.body.mergeCell(12,19,13,19);
                rt.body.setRowCells(12, Table.PER_ALIGN, Table.ALIGN_CENTER);

                rt.body.setRowCells(13, Table.PER_ALIGN, Table.ALIGN_CENTER);

                rt.body.mergeCell(14,1,14,2);
                rt.body.mergeCell(14,3,14,4);
                rt.body.mergeCell(14,5,14,6);
                rt.body.mergeCell(14,7,14,8);
                rt.body.mergeCell(14,9,14,10);
                rt.body.mergeCell(14,11,14,12);
                rt.body.mergeCell(14,13,14,14);
                rt.body.mergeCell(14,15,14,16);
                rt.body.mergeCell(14,17,14,18);
                rt.body.setRowCells(14, Table.PER_ALIGN, Table.ALIGN_CENTER);
//----------------------------------
                rt.body.mergeCell(15,1,15,19);
                rt.body.setRowCells(15, Table.PER_BORDER_BOTTOM, 2);
                rt.body.setCells(15, 1, 15, 1, Table.PER_BORDER_RIGHT, 2);

                rt.body.mergeCell(16,1,16,19);
                rt.body.setRowCells(16, Table.PER_BORDER_BOTTOM, 2);
                rt.body.setCells(16, 1, 16, 1, Table.PER_BORDER_RIGHT, 2);
                //				 “注：”(的下一行)
                rt.body.mergeCell(17,1,17,19);
                rt.body.setRowHeight(17,8);
                rt.body.setRowCells(17, Table.PER_BORDER_BOTTOM, 0);
                rt.body.setRowCells(17, Table.PER_BORDER_RIGHT, 0);

                rt.body.setBorder(0, 0, 2, 0);
                rt.body.setCells(1, 1, 16, 1, Table.PER_BORDER_LEFT, 2);
                rt.body.setCells(1, 19, 16, 19, Table.PER_BORDER_RIGHT, 2);
                rt.body.setCells(1, 18, 1, 18, Table.PER_BORDER_RIGHT, 2);



                rt.body.mergeCell(18,1,18,11);
                rt.body.mergeCell(18,12,18,15);
                rt.body.mergeCell(18,16,18,19);
                rt.body.setRowHeight(18,5);
                rt.body.setRowCells(18, Table.PER_BORDER_BOTTOM, 0);
                rt.body.setRowCells(18, Table.PER_BORDER_RIGHT, 0);
//--------------------------------------------------

                rt.body.setRowCells(19, Table.PER_BORDER_BOTTOM, 0);
                rt.body.setRowCells(19, Table.PER_BORDER_RIGHT, 0);

                rt.body.mergeCell(20,12,20,15);
                rt.body.mergeCell(20,16,20,19);
                rt.body.setRowHeight(20,5);
                rt.body.setRowCells(20, Table.PER_BORDER_BOTTOM, 0);
                rt.body.setRowCells(20, Table.PER_BORDER_RIGHT, 0);

            }

            // 设置页数
            _CurrentPage = 1;
            _AllPages=1;
//				_AllPages = rt.body.getPages();
            if (_AllPages == 0) {
                _CurrentPage = 0;
            }

            //JDBCcon con = new JDBCcon();
            String sqll="select zhi from xitxxb where mingc='结算单分页显示'";
            ResultSetList rsl=cn.getResultSetList(sqll);
            if(rsl.next()){
                if(rsl.getString("zhi").equals("否")){

                    StringBuffer sb =new StringBuffer();
                    if (rt.title != null) {
                        sb.append(rt.title.getHtml());
                    }

                    if (rt.body != null) {
                        sb.append(rt.body.getHtml());
                    }

                    if (rt.footer != null) {
                        sb.append(rt.footer.getHtml());
                    }
                    return sb.toString();
                }
            }

        }catch(Exception e) {
            // TODO 自动生成方法存根
            e.printStackTrace();
        }finally{
            cn.Close();
        }
        return rt.getAllPagesHtml(iPageIndex);
    }

    private String getGongfsl(long jiesbid,String tables) {
        // TODO 自动生成方法存根
        JDBCcon con=new JDBCcon();
        String gongfsl="";
        try{

            String sql=" select gongf from jieszbsjb,"+tables+",zhibb "
                    + " where diancjsmkb.diancjsb_id= "+jiesbid+""
                    + " and diancjsmkb.id=jieszbsjb.jiesdid"
                    + " and jieszbsjb.zhibb_id=zhibb.id and zhibb.bianm='数量'";

            ResultSet rs=con.getResultSet(sql);
            if(rs.next()){

                gongfsl=rs.getString("gongf");
                return gongfsl;
            }
            rs.close();
        }catch(Exception e){

            e.printStackTrace();
        }finally{
            con.Close();
        }
        return null;
    }

    public String getTianzdw(long diancxxb_id) {
        String Tianzdw="";
        JDBCcon con=new JDBCcon();
        try{
            String sql="select quanc from diancxxb where id="+diancxxb_id;
            ResultSet rs=con.getResultSet(sql);
            if(rs.next()){

                Tianzdw=rs.getString("quanc");
            }
            rs.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            con.Close();
        }
        return Tianzdw;
    }

    private int _CurrentPage = -1;

    public int getCurrentPage() {
        return _CurrentPage;
    }

    public void setCurrentPage(int _value) {
        _CurrentPage = _value;
    }

    private int _AllPages = -1;

    public int getAllPages() {
        return _AllPages;
    }

    public void setAllPages(int _value) {
        _AllPages = _value;
    }

    private String FormatDate(Date _date) {
        if (_date == null) {
//			return MainGlobal.Formatdate("yyyy年 MM月 dd日", new Date());
            return "";
        }
        return DateUtil.Formatdate("yyyy年MM月dd日", _date);
    }

    private String nvlStr(String strValue){
        if (strValue==null) {
            return "";
        }else if(strValue.equals("null")){
            return "";
        }

        return strValue;
    }

    public String format(double dblValue,String strFormat){
        DecimalFormat df = new DecimalFormat(strFormat);
        return formatq(df.format(dblValue));

    }

    public String[] getJieszbxx(String Table,String Jiesbh){

//	函数功能：

//		得到某一张结算单的所有指标
//	函数逻辑：

//		从单批次结算明细表中查到该结算单对应的值
//	函数形参：

//		结算单编号

        String sql = "";
        String Jieszb[] = new String[11];
        long TalbeId = 0;

        try {
            TalbeId = MainGlobal.getTableId(Table, "bianm", Jiesbh);
        } catch (Exception e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }

        sql =
                "select\n" +
                        "   round_new(sum(decode(jiessl,0,0,jiessl*qnetar))/sum(decode(jiessl,0,1,jiessl))," + ((Visit)this.getVisit()).getFarldec() + ") as qnetar,\n" +
                        "   round_new(sum(decode(jiessl,0,0,jiessl*std))/sum(decode(jiessl,0,1,jiessl)),2) as std,\n" +
                        "   round_new(sum(decode(jiessl,0,0,jiessl*stad))/sum(decode(jiessl,0,1,jiessl)),2) as stad,\n" +
                        "   round_new(sum(decode(jiessl,0,0,jiessl*star))/sum(decode(jiessl,0,1,jiessl)),2) as star,\n" +
                        "   round_new(sum(decode(jiessl,0,0,jiessl*vdaf))/sum(decode(jiessl,0,1,jiessl)),2) as vdaf,\n" +
                        "   round_new(sum(decode(jiessl,0,0,jiessl*mt))/sum(decode(jiessl,0,1,jiessl)),1) as mt,\n" +
                        "   round_new(sum(decode(jiessl,0,0,jiessl*mad))/sum(decode(jiessl,0,1,jiessl)),2) as mad,\n" +
                        "   round_new(sum(decode(jiessl,0,0,jiessl*aad))/sum(decode(jiessl,0,1,jiessl)),2) as aad,\n" +
                        "   round_new(sum(decode(jiessl,0,0,jiessl*ad))/sum(decode(jiessl,0,1,jiessl)),2) as ad,\n" +
                        "   round_new(sum(decode(jiessl,0,0,jiessl*aar))/sum(decode(jiessl,0,1,jiessl)),2) as aar,\n" +
                        "   round_new(sum(decode(jiessl,0,0,jiessl*vad))/sum(decode(jiessl,0,1,jiessl)),2) as vad\n" +
                        " from\n" +
                        "   (select xuh,\n" +
                        "     max(mx.jiessl) as jiessl,\n" +
                        "     max(mx.qnetar)  as qnetar,\n" +
                        "     max(mx.std)  as std,\n" +
                        "     max(mx.stad)  as stad,\n" +
                        "     max(mx.star)  as star,\n" +
                        "     max(mx.vdaf)  as vdaf,\n" +
                        "     max(mx.mt)  as mt,\n" +
                        "     max(mx.mad) as mad,\n" +
                        "     max(mx.aad) as aad,\n" +
                        "     max(mx.ad) as ad,\n" +
                        "     max(mx.aar) as aar,\n" +
                        "     max(mx.vad) as vad\n" +
                        "   from danpcjsmxb mx\n" +
                        "        where leib=1 and jiesdid="+TalbeId+"\n" +
                        "        group by xuh)";

        JDBCcon con = new JDBCcon();
        ResultSetList rsl = con.getResultSetList(sql);
        if(rsl.next()){

            for(int i=0;i<rsl.getColumnCount();i++){

                Jieszb[i] = rsl.getString(i);
            }
        }
        rsl.close();
        con.Close();

        return Jieszb;
    }


    public String formatq(String strValue){//加千位分隔符
        String strtmp="",xiaostmp="",tmp="";
        int i=3;
        if(strValue.lastIndexOf(".")==-1){

            strtmp=strValue;
            if(strValue.equals("")){

                xiaostmp="";
            }else{

                xiaostmp=".00";
            }

        }else {

            strtmp=strValue.substring(0,strValue.lastIndexOf("."));

            if(strValue.substring(strValue.lastIndexOf(".")).length()==2){

                xiaostmp=strValue.substring(strValue.lastIndexOf("."))+"0";
            }else{

                xiaostmp=strValue.substring(strValue.lastIndexOf("."));
            }

        }
        tmp=strtmp;

        while(i<tmp.length()){
            strtmp=strtmp.substring(0,strtmp.length()-(i+(i-3)/3))+","+strtmp.substring(strtmp.length()-(i+(i-3)/3),strtmp.length());
            i=i+3;
        }

        return strtmp+xiaostmp;
    }

    public String formatq_2(String strValue){
        //如果结果是"-,169,864.60"转换为"-169,864.60"
        String strtmp=strValue;
        if(strtmp.lastIndexOf("-,")==0){

            strtmp=strtmp.substring(0,1)+strtmp.substring(2,strtmp.length());

        }
        return strtmp;

    }


}