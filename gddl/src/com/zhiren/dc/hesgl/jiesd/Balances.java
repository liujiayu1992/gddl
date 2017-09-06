/*
 * 创建日期 2008-4-20
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.zhiren.dc.hesgl.jiesd;
/**
 * @author zsj
 * 运费处理原则：
 * 1、当运费单价在煤款合同中时
 * 两票结算：
 * 煤款是到厂价：
 * <p>
 * 1、在getMeiPrise中计算出运费总金额
 * 2、在computData、computData_Dpc算出煤款的含税总金额。
 * 3、在reCount中用含税煤款-含税运费，赋值给含税煤款，再进行含税单价等指标计算。
 * （截止2009-8-3日，系统会认为这时的运价为含税运价）
 * <p>
 * 煤款不是到厂价：
 * <p>
 * 1、在getMeiPrise中计算出运费总金额
 * 2、在computData算出煤款的含税总金额。
 * <p>
 * <p>
 * 煤款结算：
 * 煤款是到厂价：
 * <p>
 * 1、在getMeiPrise中只得到煤款合同中的运费单价
 * 2、在computData、computData_Dpc用煤单价-运费单价=煤单价
 * （截止2009-8-3日，系统会认为这时的运价为和煤款单价的含税和不含税属性相同）
 * 3、由于不调用getYunFei方法，所以运费栏为空。
 * <p>
 * 煤款不是到厂价：
 * <p>
 * 1、在getMeiPrise中只得到煤款合同中的运费单价
 * 2、在computData、computData_Dpc中不用煤单价-运费单价
 * 3、由于不调用getYunFei方法，所以运费栏为空。
 * <p>
 * 运费结算：
 * <p>
 * 1、直接计算出运费getYunFei中实现。
 * <p>
 * 2009-11-06 针对石嘴山电厂的需求进行增扣款逻辑的处理
 * 需求如下：
 * 结算期内批量煤收到基低位发热量按加权平均值结算。出卖人当月供煤量完成15万吨,
 * 且当月批量煤收到基低位发热量加权平均在19 MJ/kg以上，（当月煤量和煤质同时完成上述要求时），
 * 每吨煤在合同价格基础上奖励1元/吨。当月供煤量超过 15万吨，且当月批量煤收到基低位发热量加权平均在19MJ/kg以上，
 * 超出部分每吨煤在合同价格基础上奖励5元/吨。如果每月未完成本合同量，
 * 未完成的煤量相应扣除5元/吨，扣除金额从当月供应煤款中扣除。
 * <p>
 * 主要处理增扣款中的“适用范围”，超出部分、未完成部分
 * 处理逻辑：将设置了适用范围为“超出部分”（hetzkkb.shiyfw=1）的增扣款放在后面进行计算。
 * <p>
 * 2009-11-12 针对双鸭山电厂的需求进行运费结算的特殊算法处理
 * 需求如下：
 * 运费结算时由于运输单位运输多个煤矿，各个煤矿的运距不同，在结算时只能一个一个煤矿进行结算，
 * 这样就造成结算时间比较长，所以电厂提出要求可以进行多个煤矿一起选择进行单批次的运费结算，最后总金额进行相加。
 * <p>
 * 处理逻辑：
 * 注意：和煤款单批次结算不同，运费的单批次结算时才存danpcjsmkb，否则不存该表
 * <p>
 * 1、在结算设置方案中增加一个“运费结算单批次分组条件”，值为fahb中列
 * 2、在CountYf方法中，构建一个二维数组。第二维的值为[0]meikxxb_id，[1]分组的条件，[2]分组的值
 * 3、根据分组条件,对可能影响运费价格的因素进行重新赋值。（目前已知：运距，煤矿。而运距又是根据煤矿取得的,所以在构建分组数组时要将meikxxb_id作为必备条件）
 * 4、在计算增扣款前须对数量质量重新赋值，之后进行每笔分组的运费结算
 * 5、计算出一组后调用 computData_Yf 方法，将结算数据后将值存入 danpcjsmxb；此处如果不是单批次结算则不存 danpcjsmxb
 * 6、在reCount方法中从新计算页面煤款、运费要显示的值
 * <p>
 * 2009-11-19 针对石嘴山电厂的需求增加了对增扣款单位 “%吨” 的处理逻辑
 * <p>
 * 处理逻辑：
 * 注意：“%吨”的处理逻辑是扣除实际的结算数量。要重新计算结算差异
 * <p>
 * 1、在结算中加入 各个指标的 “折数量_” 。
 * 2、在公式中加入对 “折数量_” 的计算逻辑。
 * 3、在setJieszb 方法中用结算量减去 “折数量_”，并在备注中记录下来。
 * <p>
 * 2009-11-24 针对石嘴山电厂的需求增加了对增扣款加权方式中的 “加权平均” 的处理逻辑
 * <p>
 * 处理逻辑：
 * 前提：我们认为单批次结算时才会出现这种情况
 * <p>
 * 1、在得到要处理的煤价前首先要得到增扣款表中需要进行加权平均处理的指标。
 * 2、得到指标后，用加权平均的方法去挑用，getjiesslzl的方法，这时bsv 里的所有指标都会被赋值。
 * 3、我们找出增扣款中的那个指标，对其的static状态进行赋值。
 * 4、在煤价、运价和增扣款的计算中如果一个指标的static 状态有值，我们要用这个值，否则用结算值，
 * 只改 Jiesdcz.getZhib_info 方法，在方法中将对应指标的 js 值置为 static_value
 * 注意：在后续的操作中不可再运行getjiesslzl的方法，因为该方法会重写 所有指标的 js
 * <p>
 * 2009-12-29 将加权平均算法下，需要单批次进行处理的指标存入特定的表中，表结构如下：
 * id,jiesb_id,zhibb_id,lie_id,zhi,jiessl,zonghdj,zengkje
 * <p>
 * 2009-12-29 针对石嘴山的问题在合同增扣款的加权方式中加入“加权分列” 的处理逻辑
 * <p>
 * 问题描述如下：
 * 先加权算出某个指标的加权值，根据加权值在增扣款中找到对应的扣值，并将加权值设为该指标的增扣款上限，
 * 再用所选的列的指标和此上限做比较，得出分别的增扣款值，进行单批次结算。
 * <p>
 * 例如：
 * 合同煤价185元/吨
 * 先用加权平均算出热值，与合同中对比得出上限与扣值0.05；
 * <p>
 * 然后再用煤量*（煤价-（上限-每批煤的差值）*0.05），例：
 * 474.26    *   （185-（4304-4199.11）*0.05）
 * 628.98    *   （185-（4304-4188.11）*0.05）
 * 469.12    *   （185-（4304-4442.32）*0.05）
 * 473.06    *   （185-（4304-4262.24）*0.05）
 * sum（每批得出的钱）为最终煤款
 * <p>
 * 处理逻辑如下：
 * 1、在合同价格中增加加权方式“加权分列”，和单批次加权中的方法共用，
 * 把指标的值存入其对应的 static_value 中。
 * 2、我们找出增扣款中的那个指标，对其的static_dc状态进行赋值。
 * 3、首先用指标的static_dc值，然后调用计算加权煤价，得出StringBuffer各指标的折单价。
 * 4、如果价格中使用的是加权平均算法，则系统不再进行加权平均计算，。如果价格中使用的是单批次算法，
 * 则用该值影响增扣款的指标值。
 * 5、在煤价、运价和增扣款的计算中如果一个指标的static_dc 状态有值，我们要用这个值，否则用结算值，
 * 只改 Jiesdcz.getZhib_info 方法，在方法中将对应指标的 js 值置为 static_value
 * 注意：在后续的操作中不可再运行getjiesslzl的方法，因为该方法会重写 所有指标的 js
 * <p>
 * <p>
 * 2010-05-06 针对石嘴山的问题
 * <p>
 * 在合同价格中的结算形式设置为单批次，增扣款中“结算数量”指标设置为“加权平均”时
 * 对结算数量增扣款操作时不再每个批次都计算“结算数量”指标增扣款，而是在最后时统一计算。
 * <p>
 * 2010-05-13	针对石嘴山的问题
 * <p>
 * 在单批次结算时，先用所有的单批次列进行加权平均，确定统一的合同价格。
 * 再用各发货列的指标进行增扣款计算。
 * <p>
 * 关于结算的新问题，
 * 比如：某矿3批煤结算，热值分别为 3500，3800，4200
 * 加权热值为3900
 * 合同价格233，热量按区间扣价4000-4500 扣10元/吨
 * 3500-4000 扣20元/吨，
 * 增扣款4000基础上每大卡增减0.05元
 * 结算时先用加权热值3900 取得价格233-20=213元/吨
 * 然后在单批次和4000标准进行增扣款
 * （4000-3500）*0.05
 * （4000-3800）*0.05
 * （4200-4000）*0.05
 * <p>
 * 处理逻辑：
 * 1、在结算设置方案中增加一个方案参数 "单批次结算用加权平均确定价格"，值为"是"或"否"
 * 2、当合同价格中，有多条价格条款时。在单批次结算处理时，判断该参数的值为"是"，就进行加权平均计算结算价格。
 * 3、再计算每个单批次，在计算时先判断该设置值，如果为"是"则跳过价格计算的步骤，直接计算增扣款
 * <p>
 * 2010-05-24 针对西部电厂的问题，单批次处理结算扣吨量时要在最后的reCount方法中，计算扣吨对应的总金额，从总的煤价款中或运费价款中减去，
 * 记入结算单扣吨栏。
 * <p>
 * <p>
 * 2011-07-04 针对邯郸电厂及类似结算情况，一个lie_id对应多个pinzb_id，在结算时需要记录页面选择品种，并传到结算界面
 * 1、在getBalanceData 方法中记录选择的品种
 * 2、在getBaseInfo 方法中判断bsv.getpinz_id是否为空，如不为空则不再重新赋值，并保持ranlpz 变量的一致性
 * 3、修改Jiesdcz.getJiesszl_Sql 方法，增加品种的判断条件
 * <p>
 * 修改：徐文理
 * 时间：2012-08-14
 * 描述：增加参数控制国电宣威加权平均考核之后再单批次考核的问题。
 * <p>
 * <p>
 * 作者：夏峥
 * 时间：2012-12-01
 * 描述：参数控制国电西部热电是否进行特殊配置。
 * MainGlobal.getXitxx_item("结算", "西部特殊结算", "0", "")
 * <p>
 * 作者：夏峥
 * 时间：2012-12-06
 * 描述：判断是否扣款时改为取样本容量而不是净重。
 * 增付价仍以结算数量为准
 * <p>
 * 作者：夏峥
 * 时间：2013-01-14
 * 描述：在单条价格且非目录价，结算类型为单批次结算时，判断是否采用西部特殊结算
 * <p>
 * 作者：夏峥
 * 时间：2013-03-25
 * 描述：增加强制计算标识。使用系统信息配置硫份强制计算。
 * 更新程序时需替换相应的Jiesgs.xml
 * <p>
 * 修改：徐文理
 * 时间：2012-08-14
 * 描述：增加参数控制国电宣威加权平均考核之后再单批次考核的问题。
 * <p>
 * <p>
 * 作者：夏峥
 * 时间：2012-12-01
 * 描述：参数控制国电西部热电是否进行特殊配置。
 * MainGlobal.getXitxx_item("结算", "西部特殊结算", "0", "")
 * <p>
 * 作者：夏峥
 * 时间：2012-12-06
 * 描述：判断是否扣款时改为取样本容量而不是净重。
 * 增付价仍以结算数量为准
 * <p>
 * 作者：夏峥
 * 时间：2013-01-14
 * 描述：在单条价格且非目录价，结算类型为单批次结算时，判断是否采用西部特殊结算
 * <p>
 * 作者：夏峥
 * 时间：2013-03-25
 * 描述：增加强制计算标识。使用系统信息配置硫份强制计算。
 * 更新程序时需替换相应的Jiesgs.xml
 * <p>
 * 修改：徐文理
 * 时间：2012-08-14
 * 描述：增加参数控制国电宣威加权平均考核之后再单批次考核的问题。
 * <p>
 * <p>
 * 作者：夏峥
 * 时间：2012-12-01
 * 描述：参数控制国电西部热电是否进行特殊配置。
 * MainGlobal.getXitxx_item("结算", "西部特殊结算", "0", "")
 * <p>
 * 作者：夏峥
 * 时间：2012-12-06
 * 描述：判断是否扣款时改为取样本容量而不是净重。
 * 增付价仍以结算数量为准
 * <p>
 * 作者：夏峥
 * 时间：2013-01-14
 * 描述：在单条价格且非目录价，结算类型为单批次结算时，判断是否采用西部特殊结算
 * <p>
 * 作者：夏峥
 * 时间：2013-03-25
 * 描述：增加强制计算标识。使用系统信息配置硫份强制计算。
 * 更新程序时需替换相应的Jiesgs.xml
 * <p>
 * 修改：徐文理
 * 时间：2012-08-14
 * 描述：增加参数控制国电宣威加权平均考核之后再单批次考核的问题。
 * <p>
 * <p>
 * 作者：夏峥
 * 时间：2012-12-01
 * 描述：参数控制国电西部热电是否进行特殊配置。
 * MainGlobal.getXitxx_item("结算", "西部特殊结算", "0", "")
 * <p>
 * 作者：夏峥
 * 时间：2012-12-06
 * 描述：判断是否扣款时改为取样本容量而不是净重。
 * 增付价仍以结算数量为准
 * <p>
 * 作者：夏峥
 * 时间：2013-01-14
 * 描述：在单条价格且非目录价，结算类型为单批次结算时，判断是否采用西部特殊结算
 * <p>
 * 作者：夏峥
 * 时间：2013-03-25
 * 描述：增加强制计算标识。使用系统信息配置硫份强制计算。
 * 更新程序时需替换相应的Jiesgs.xml
 */

/**
 * 修改：徐文理
 * 时间：2012-08-14
 * 描述：增加参数控制国电宣威加权平均考核之后再单批次考核的问题。
 *
 */
/**
 * 作者：夏峥
 * 时间：2012-12-01
 * 描述：参数控制国电西部热电是否进行特殊配置。
 * 		MainGlobal.getXitxx_item("结算", "西部特殊结算", "0", "")
 */
/**
 * 作者：夏峥
 * 时间：2012-12-06
 * 描述：判断是否扣款时改为取样本容量而不是净重。
 * 		增付价仍以结算数量为准
 */
/**
 * 作者：夏峥
 * 时间：2013-01-14
 * 描述：在单条价格且非目录价，结算类型为单批次结算时，判断是否采用西部特殊结算
 */
/**
 * 作者：夏峥
 * 时间：2013-03-25
 * 描述：增加强制计算标识。使用系统信息配置硫份强制计算。
 * 		更新程序时需替换相应的Jiesgs.xml
 */

import java.sql.*;
import java.util.Date;

import com.zhiren.common.Jiesgs.Jiesgs;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.*;
import com.zhiren.main.Visit;

import bsh.*;

public class Balances extends BasePage implements PageValidateListener {


    Balances_variable bsv = new Balances_variable();
    Visit visit = new Visit();
    private IPropertySelectionModel _ZhibModel;

    public Balances_variable getBsv() {

        return bsv;
    }

    public void setBsv(Balances_variable value) {

        bsv = value;
    }

    public Balances_variable getBalanceData(String SelIds, long Diancxxb_id, long Jieslx, long Gongysb_id,
                                            long Hetb_id, String Jieszbsftz, String Yansbh, double Jieskdl, double Jieskkje, long Yunsdwb_id,
                                            double Shangcjsl, String Yunsdw, Visit visit, double Yujsjz) throws Exception {
        bsv.setIsError(true);
        bsv.setErroInfo("");
        bsv.setJieslx(Jieslx);
        bsv.setDiancxxb_id(Diancxxb_id);
        bsv.setSelIds(SelIds);
        bsv.setYunsdw(Yunsdw);
        bsv.setYunsdwb_id(Yunsdwb_id);
        bsv.setJieskdl(Jieskdl);
        bsv.setKouk_js(Jieskkje);
        bsv.setJieszbsftz(Jieszbsftz);
        bsv.setShangcjsl(Shangcjsl);
        bsv.setGuohxt(visit.getString19());    // 针对山西阳城电厂两个过衡系统(A系统、B系统)的运费结算问题，将结算运费时选择的过衡系统放到bsv中
        if ("".equals(visit.getString19()) || visit.getString19() == null) {
            visit.setString19("0");
        }
        bsv.setRanlpzb_Id(Long.parseLong(visit.getString19()));
        this.visit = visit;
        bsv.setYujsjz(Yujsjz);

        if (Jieslx == Locale.liangpjs_feiylbb_id || Jieslx == Locale.meikjs_feiylbb_id) {

            //region Description//两票结算、煤款结算
            getBaseInfo(SelIds, Diancxxb_id, Gongysb_id, Hetb_id, Jieszbsftz, Yansbh, Jieskdl);
            if (bsv.getErroInfo().equals("")) {
//				得到供应商、运输方式等基本信息、要求数量和质量一定要审核
                if (Gongysb_id == 0) {
//					如果在结算选择页面供应商没做选择
                    Gongysb_id = bsv.getGongysb_Id();
                }

            } else {
                bsv.getErroInfo();
                return bsv;
            }

//			得到公式。
//			得到全部公式，（有可能在单结算煤款时，出现到厂价情况，还要计算运费）
            if (!getGongsInfo(Diancxxb_id, "ALL")) {

                return bsv;
            }

//			算煤价,合同中的相关值
            if (getMeiPrice(bsv.getRanlpzb_Id(), bsv.getYunsfsb_id(), bsv.getFaz_Id(),
                    bsv.getDaoz_Id(), Diancxxb_id, bsv.getHetb_Id(),
                    bsv.getFahksrq(), Jieslx, Jieszbsftz, SelIds,
                    Gongysb_id, Jieskdl, Yunsdwb_id, Shangcjsl)) {

            } else {

                bsv.getErroInfo();
                return bsv;
            }

            if (Jieslx == Locale.liangpjs_feiylbb_id) {
//				如果是两票结算，从这里计算运费
                getJiesszl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl, Yunsdwb_id, Jieslx, Shangcjsl, "");
                if (getYunFei(SelIds, Jieslx, bsv.getHetb_Id(), Shangcjsl)) {

                } else {
                    bsv.getErroInfo();
                    return bsv;
                }
            }

            if (bsv.getKoud_js() > 0) {
                bsv.setBeiz(bsv.getBeiz() + " 结算扣吨:" + bsv.getKoud_js());
            }
            if (bsv.getKouk_js() > 0) {
                bsv.setBeiz(bsv.getBeiz() + " 结算扣款:" + bsv.getKouk_js());
            }

//			国电宣威：将当前扣水考核总量写入备注中
            if (MainGlobal.getXitxx_item("结算", "国电宣威结算量是否进行水分考核调整", String.valueOf(visit.getDiancxxb_id()), "否").equals("是")) {
                double shuifkhzl = getHetsfkhzl(SelIds);
                if (shuifkhzl > 0) {
                    bsv.setBeiz(bsv.getBeiz() + "水分考核总量：" + shuifkhzl);
                }
            }
            //endregion


        } else {    //运费结算、包括地铁运费
            getBaseInfo(SelIds, Diancxxb_id, Gongysb_id, Hetb_id, Jieszbsftz, Yansbh, Jieskdl);

            if (Hetb_id > 0) {
//				如果单结算运费，有两种情况：
//				1、通过核对货票得到运费
//				2、通过运输合同计算出运费
//				当Hetb_id>0时说明是第二种情况
                getYunshtInfo(Hetb_id);
            }

            if (bsv.getErroInfo().equals("")) {
//				得到供应商、运输方式等基本信息
                if (Gongysb_id == 0) {
//					如果在结算选择页面供应商没做选择
                    Gongysb_id = bsv.getGongysb_Id();
                }
            } else {
                bsv.getErroInfo();
                return bsv;
            }

            getJiesszl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl, Yunsdwb_id, Jieslx, Shangcjsl, "");


//			得到运费公式
            if (getGongsInfo(Diancxxb_id, "YF")) {

            } else {
                //return ErroInfo;
                return bsv;
            }

//			算运费
            if (getYunFei(SelIds, Jieslx, bsv.getHetb_Id(), Shangcjsl)) {

            } else {
                bsv.getErroInfo();
                return bsv;
            }
//			由于增加了单批次运费结算故computData_Yf方法在每批次中调用，就不统一调用了
//			computData_Yf();
        }

        CountZonghzkk();
        reCount();
        computYunfAndHej();

        bsv.setIsError(false);

        return bsv;
    }

    //得到系统信息，运费税率，煤款税率，公式。
    private boolean getGongsInfo(long _Diancxxb_id, String _Type) throws Exception {
//		参数说明：_diancxx_id，电厂公式
//				 _Type,		  如果为"MK"那就是煤款公式，如果为"YF"那就是运费公式,如果为"ALL"那就是两票结算
//		JDBCcon con =new JDBCcon();
//	    try {
//
//            //煤款结算公式
//	    	ResultSet rs= con.getResultSet("select id from gongsb where mingc='结算煤价' and leix='结算' and zhuangt=1 and diancxxb_id="+_Diancxxb_id);
//            if (rs.next()) {
//
//            	DataBassUtil clob=new DataBassUtil();
//
//            	bsv.setGongs_Mk(clob.getClob("gongsb", "gongs", rs.getLong(1)));
//
//            }else{
//            	bsv.setErroInfo("没有得到煤价公式的系统设置值");
//            	return false;
//            }
//            rs.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//        	con.Close();
//        }
//
//		return true;

        String str_Gongs_Mk = "";
        String str_Gongs_Yf = "";

        if (_Type.equals("ALL")) {

            str_Gongs_Mk = Jiesdcz.GetJiesgs(_Diancxxb_id, SysConstant.Gs_JS_HeadName_Mk);
            str_Gongs_Yf = Jiesdcz.GetJiesgs(_Diancxxb_id, SysConstant.Gs_JS_HeadName_Yf);

            if (str_Gongs_Mk.equals("")) {

                bsv.setErroInfo("没有得到煤价公式的系统设置值");
                return false;
            } else {

                bsv.setGongs_Mk(str_Gongs_Mk);
            }

            if (str_Gongs_Yf.equals("")) {

//				bsv.setErroInfo("没有得到运费公式的系统设置值");
//	        	return false;
            } else {

                bsv.setGongs_Yf(str_Gongs_Yf);
            }
        } else if (_Type.equals("MK")) {

            str_Gongs_Mk = Jiesdcz.GetJiesgs(_Diancxxb_id, SysConstant.Gs_JS_HeadName_Mk);

            if (str_Gongs_Mk.equals("")) {

                bsv.setErroInfo("没有得到煤价公式的系统设置值");
                return false;
            } else {

                bsv.setGongs_Mk(str_Gongs_Mk);
            }
        } else if (_Type.equals("YF")) {

            str_Gongs_Yf = Jiesdcz.GetJiesgs(_Diancxxb_id, SysConstant.Gs_JS_HeadName_Yf);
            if (str_Gongs_Yf.equals("")) {

                bsv.setErroInfo("没有得到运费公式的系统设置值");
                return false;
            } else {

                bsv.setGongs_Yf(str_Gongs_Yf);
            }
        }

        return true;
    }


//	得到要结算的数量，质量等基本信息

    /**
     * @return
     * @throws Exception
     */

    public void getYunshtInfo(long Yunshtb_id) {
//		得到运输合同的收款单位，开户银行，帐号
        JDBCcon con = new JDBCcon();
        try {

            String sql =
                    "select nvl(yd.quanc,'') as quanc,\n" +
                            "         nvl(yd.kaihyh,'') as kaihyh,\n" +
                            "         nvl(yd.zhangh,'') as zhangh\n" +
                            "       from hetys hys,yunsdwb yd\n" +
                            "       where hys.yunsdwb_id = yd.id\n" +
                            "             and hys.id=" + Yunshtb_id;


            ResultSet rs = con.getResultSet(sql);

            while (rs.next()) {

                bsv.setShoukdw(rs.getString("quanc"));
                bsv.setKaihyh(rs.getString("kaihyh"));
                bsv.setZhangH(rs.getString("zhangh"));
            }
            rs.close();
        } catch (SQLException s) {
            // TODO 自动生成 catch 块
            s.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
    }

    public Balances_variable getBaseInfo(String SelIds, long Diancxxb_id, long Gongysb_id, long Hetb_id, String Jieszbsftz, String Yansbh, double Jieskdl) throws Exception {

        JDBCcon con = new JDBCcon();
        try {
            //发货日期、到货日期、车数、标重、盈亏、运损、发热量、硫 from fahb
            //过衡量按列取证，结算量火车煤是按列取整，汽车煤是求和取整

//	      读取结算设置表中结算设置参数
//      	1、结算数量
//        	2、结算加权数量
//        	3、结算显示指标
//        	4、结算数量保留小数位
//        	5、结算数量取整方式
//        	6、Mt保留小数位
//        	7、Mad保留小数位
//        	8、Aar保留小数位
//        	9、Aad保留小数位
//        	10、Adb保留小数位
//        	11、Vad保留小数位
//        	12、Vdaf保留小数位
//        	13、Stad保留小数位
//        	14、Std保留小数位
//        	15、Had保留小数位
//        	16、Qnetar保留小数位
//        	17、Qbad保留小数位
//        	18、Qgrad保留小数位
//	    	19、T2保留小数位
//        	19、结算指标调整
//	    	20、是否以矿方质量结算

//	        String jies_Jssl="biaoz+yingk-koud-kous-kouz";			//结算数量
//	        String jies_Jqsl="jingz";								//结算加权数量
//	        String jies_Jsslblxs="0";								//结算数量保留小数位
//	        String jies_Jieslqzfs="sum(round())";					//结算数量取整方式
//	        String jies_Kdkskzqzfs="round(sum())";					//扣吨、扣水、扣杂取整方式
//	        String jies_yunfjssl="jingz";							//运费结算数量
//	        boolean mbl_yunfjssl=false;								//用来判断用户是否单独设定了运费的结算数量，false没有，就取煤款结算数量
//
//
//	        jies_Jqsl="f."+MainGlobal.getXitxx_item("结算", Locale.jiaqsl_xitxx,
//        			String.valueOf(Diancxxb_id),jies_Jqsl);
//
//        if(Jiesdcz.getJiessz_items(Diancxxb_id,Gongysb_id,Hetb_id)!=null){
//
//        	String JiesszArray[][]=null;
//
//        	JiesszArray=Jiesdcz.getJiessz_items(Diancxxb_id,Gongysb_id,Hetb_id);
//
//			for(int i=0;i<JiesszArray.length;i++){
//
//				if(JiesszArray[i][0]!=null){
//
//					if(JiesszArray[i][0].equals(Locale.jiesslzcfs_jies)){
//
//						jies_Jssl=JiesszArray[i][1];
//					}else if(JiesszArray[i][0].equals(Locale.jiesjqsl_jies)){
//
//						jies_Jqsl=JiesszArray[i][1];
//					}else if(JiesszArray[i][0].equals(Locale.jiesslblxsw_jies)){
//
//						jies_Jsslblxs=JiesszArray[i][1];
//					}else if(JiesszArray[i][0].equals(Locale.kdkskzqzfs_jies)){
//
//						jies_Kdkskzqzfs=JiesszArray[i][1];
//					}else if(JiesszArray[i][0].equals(Locale.meiksl_jies)){
//
//						bsv.setMeiksl(Double.parseDouble(JiesszArray[i][1]));
//					}else if(JiesszArray[i][0].equals(Locale.yunfsl_jies)){
//
//						bsv.setYunfsl(Double.parseDouble(JiesszArray[i][1]));
//					}else if(JiesszArray[i][0].equals(Locale.jiesslqzfs_jies)){
//
//						jies_Jieslqzfs=JiesszArray[i][1];
//					}else if(JiesszArray[i][0].equals(Locale.yunfjsslzcfs_jies)){
//
//						jies_yunfjssl=JiesszArray[i][1];
//						mbl_yunfjssl=true;
//					}
//				}
//			}
//        }
//
//        if(!mbl_yunfjssl){
//
////        	false表示用户没有单独设置运费结算数量，就取煤款结算数量
//        	jies_yunfjssl=jies_Jssl;
//        }

            String sql = "";

            sql = " select hetb_id,yunj,meikxxb_id,gongysb_id,pinzb_id,yunsfsb_id,minfahrq,maxfahrq,mindaohrq,maxdaohrq,gongysqc,meikdwqc,faz,faz_id,daoz_id,yuanshdw," +
                    " pinz,yunsfs,nvl(getMeiksx(meikxxb_id,diancxxb_id,'运距'),0) as yunju,jihkjb_id		\n " +
                    " from 														\n" +
                    " (select max(hetb_id) as hetb_id,max(m.yunj) as yunj,max(f.meikxxb_id) as meikxxb_id,max(f.gongysb_id) as gongysb_id, \n " +
                    " max(pinzb_id) as pinzb_id,max(yunsfsb_id) as yunsfsb_id,to_char(min(fahrq),'yyyy-mm-dd') as minfahrq,to_char(max(fahrq),'yyyy-mm-dd') as maxfahrq, \n " +
                    " max(pz.mingc) as pinz,max(ysfs.mingc) as yunsfs," +
                    " to_char(min(daohrq),'yyyy-mm-dd') as mindaohrq,to_char(max(daohrq),'yyyy-mm-dd') as maxdaohrq,max(g.quanc) as gongysqc,max(m.quanc) as meikdwqc, \n " +
                    " max(cz.mingc) as faz,max(cz.id) as faz_id,max(dz.id) as daoz_id,max(decode(vwydw.mingc,null,(select mingc from diancxxb where id = f.diancxxb_id),vwydw.mingc)) as yuanshdw, \n " +
                    " max(f.diancxxb_id) as diancxxb_id,max(f.jihkjb_id) as jihkjb_id " +
                    " from fahb f,zhilb z,kuangfzlb kz,gongysb g,meikxxb m,chezxxb cz,vwyuanshdw vwydw,pinzb pz,yunsfsb ysfs,chezxxb dz" +
                    " where f.zhilb_id=z.id and kz.id(+)=f.kuangfzlb_id and f.faz_id=cz.id and f.pinzb_id=pz.id " +
                    " and f.gongysb_id=g.id and f.meikxxb_id=m.id and f.yuanshdwb_id=vwydw.id(+) and f.yunsfsb_id=ysfs.id and f.daoz_id=dz.id" +
                    " and z.liucztb_id=1 and f.liucztb_id=1 " +
                    " and f.lie_id in(" + SelIds + "))";

            ResultSet rs = con.getResultSet(sql);

            if (rs.next()) {

                bsv.setFahksrq(rs.getDate("minfahrq"));
                bsv.setFahjzrq(rs.getDate("maxfahrq"));
                if (bsv.getFahksrq().equals(bsv.getFahjzrq())) {

                    bsv.setFahrq(Jiesdcz.FormatDate(bsv.getFahksrq()));
                } else {

                    bsv.setFahrq(Jiesdcz.FormatDate(bsv.getFahksrq()) + "至" + Jiesdcz.FormatDate(bsv.getFahjzrq()));
                }

                bsv.setYansksrq(rs.getDate("mindaohrq"));
                bsv.setYansjsrq(rs.getDate("maxdaohrq"));

                if (bsv.getYansksrq().equals(bsv.getYansjsrq())) {

                    bsv.setDaohrq(Jiesdcz.FormatDate(bsv.getYansksrq()));
                } else {

                    bsv.setDaohrq(Jiesdcz.FormatDate(bsv.getYansksrq()) + "至" + Jiesdcz.FormatDate(bsv.getYansjsrq()));
                }

                bsv.setHetb_Id(Hetb_id);
                bsv.setMeikxxb_Id(rs.getLong("meikxxb_id"));
                bsv.setGongysb_Id(rs.getLong("gongysb_id"));
                if (bsv.getRanlpzb_Id() == 0) {
                    bsv.setRanlpzb_Id(rs.getLong("pinzb_id"));
                    bsv.setRanlpz(rs.getString("pinz"));
                } else {
                    bsv.setRanlpz(MainGlobal.getTableCol("pinzb", "mingc", "id", String.valueOf(bsv.getRanlpzb_Id())));
                }

                bsv.setShoukdw(rs.getString("gongysqc"));
                bsv.setFahdw(rs.getString("gongysqc"));
                bsv.setMeikdw(rs.getString("meikdwqc"));
                bsv.setFaz(rs.getString("faz"));
                bsv.setFaz_Id(rs.getLong("faz_id"));
                bsv.setDaoz_Id(rs.getLong("daoz_id"));
                bsv.setYuanshr(rs.getString("yuanshdw"));
                bsv.setXianshr(rs.getString("yuanshdw"));
                bsv.setTianzdw(MainGlobal.getTableCol("diancxxb", "quanc", "id", String.valueOf(Diancxxb_id)));
                bsv.setYunsfs(rs.getString("yunsfs"));
                bsv.setYunsfsb_id(rs.getLong("yunsfsb_id"));
                bsv.setJiesrq(Jiesdcz.FormatDate(new Date()));    //结算日期
                bsv.setJiesbh(Jiesdcz.getJiesbh(String.valueOf(Diancxxb_id), ""));
                bsv.setDaibcc(MainGlobal.getShouwch(SelIds));
                bsv.setYunju_cf(rs.getDouble("yunju"));        //厂方
                bsv.setJihkjb_id(rs.getLong("jihkjb_id"));
                bsv.setHetbh(MainGlobal.getTableCol("(select id,hetbh from hetb union select id,hetbh from hetys)", "hetbh", "id", String.valueOf(bsv.getHetb_Id())));

            } else {
                bsv.setErroInfo("要结算的车皮信息不存在可能已被其他用户删除!");
                return bsv;
            }
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }

        return bsv;
    }

    public Balances_variable getJiesszl(String Jieszbsftz, String SelIds, long Diancxxb_id, long Gongysb_id,
                                        long Hetb_id, double Jieskdl, long Yunsdwb_id, long Jieslx, double Shangcjsl, String Tsclzb_where) {

//		结算数、质量
        JDBCcon con = new JDBCcon();
        ResultSetList rsl = null;
        try {

            rsl = con.getResultSetList(Jiesdcz.getJiesszl_Sql(bsv, visit, Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id,
                    Jieskdl, Yunsdwb_id, Jieslx, Shangcjsl, Tsclzb_where).toString());

            if (rsl.next()) {

//				数量
                bsv.setKoud(rsl.getDouble("koud"));                    //扣吨
                bsv.setKous(rsl.getDouble("kous"));                    //扣水
                bsv.setKouz(rsl.getDouble("kouz"));                    //扣杂
                bsv.setChes(rsl.getLong("ches"));                    //车数

                bsv.setGongfsl(rsl.getDouble("biaoz"));                //标重
                bsv.setYingksl(rsl.getDouble("yingk"));                //盈亏
                bsv.setYingd(rsl.getDouble("yingd"));                //盈吨
                bsv.setKuid(rsl.getDouble("kuid"));
                bsv.setBaifbdsl(rsl.getDouble("baifbdjs"));            //百分比吨基数

//            	结算扣吨处理_begin
                if (bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)) {

                    bsv.setJiessl(rsl.getDouble("jiessl") - Jieskdl);        //结算重量

                    if (Jieslx == Locale.guotyf_feiylbb_id || Jieslx == Locale.daozdt_feiylbb_leib || Jieslx == Locale.haiyyf_feiylbb_id) {

                        bsv.setJiessl(rsl.getDouble("yunfjssl") - Jieskdl);
                    }

                    if (bsv.getLiangpjsyfbjxkd().equals("是") && Jieslx == Locale.liangpjs_feiylbb_id) {
//                		两票结算运费不进行扣吨
                        bsv.setYunfjsl(rsl.getDouble("yunfjssl"));        //运费结算数量
                    } else {

                        bsv.setYunfjsl(rsl.getDouble("yunfjssl") - Jieskdl);    //运费结算数量

                        String zhi = MainGlobal.getXitxx_item("结算", "实际结算量是否与合同量比较", String.valueOf(visit.getDiancxxb_id()), "否");
                        if (zhi.equals("是")) {
                            if (rsl.getDouble("yunfjssl") < rsl.getDouble("jiessl")) {
                                bsv.setYunfjsl(rsl.getDouble("jiessl"));
                            }
                        }
                    }

                    if (Jieslx == Locale.liangpjs_feiylbb_id && MainGlobal.getXitxx_item("结算", "大同结算条件特殊处理", "300", "否").equals("是")) {
                        //如果是两票结算,并且是大同结算条件特殊处理时,运费的结算数量取煤款的结算数量
                        bsv.setYunfjsl(rsl.getDouble("jiessl"));
                    }


                    bsv.setKoud_js(Jieskdl);                            //结算扣吨

                    bsv.setJiessl(CustomMaths.add(bsv.getJiessl(), Shangcjsl));    //实际结算数量+上次结算数量（为了算数量折价用）
                } else {
//            		结算形式不是“加权平均”结算，否则结算扣吨放在reCount方法中进行
                    bsv.setJiessl(rsl.getDouble("jiessl"));        //结算重量

                    if (Jieslx == Locale.guotyf_feiylbb_id || Jieslx == Locale.daozdt_feiylbb_leib || Jieslx == Locale.haiyyf_feiylbb_id) {

                        bsv.setJiessl(rsl.getDouble("yunfjssl"));
                    }

                    if (bsv.getLiangpjsyfbjxkd().equals("是") && Jieslx == Locale.liangpjs_feiylbb_id) {
//                		两票结算运费不进行扣吨
                        bsv.setYunfjsl(rsl.getDouble("yunfjssl"));        //运费结算数量
                    } else {

                        bsv.setYunfjsl(rsl.getDouble("yunfjssl"));    //运费结算数量

                        String zhi = MainGlobal.getXitxx_item("结算", "实际结算量是否与合同量比较", String.valueOf(visit.getDiancxxb_id()), "否");
                        if (zhi.equals("是")) {
                            if (rsl.getDouble("yunfjssl") < rsl.getDouble("jiessl")) {
                                bsv.setYunfjsl(rsl.getDouble("jiessl"));
                            }
                        }
                    }
                    bsv.setKoud_js(Jieskdl);                            //结算扣吨
                }
//				结算扣吨处理_end

                bsv.setYanssl(rsl.getDouble("yanssl"));                //厂方验收数量
                bsv.setJingz(rsl.getDouble("jingz"));                //净重

                bsv.setJiesslcy(CustomMaths.Round_new((bsv.getJiessl() - bsv.getJingz()), 2));    //结算数量差异(结算量和过衡量的差值)
//            	bsv.setJiesslcy(rsl.getDouble("jieslcy"));			//结算数量差异
                bsv.setYuns(rsl.getDouble("yuns"));                    //实际运损

                bsv.setChaokdl(rsl.getDouble("chaokdl"));            //超亏吨量（要放到danpcmxb中）

//            	if(!blnDandszyfjssl&&Jieslx==Locale.liangpjs_feiylbb_id){
////            		没有单独设置运费结算量且结算方式为两票，则运费结算数据量为gongfsl
//            		bsv.setYunfjsl(rsl.getDouble("biaoz"));
//            	}

//	        	厂方指标
                bsv.setQnetar_cf(rsl.getDouble("Qnetar_cf"));
                bsv.setStd_cf(rsl.getDouble("Std_cf"));
                bsv.setMt_cf(rsl.getDouble("Mt_cf"));
                bsv.setMad_cf(rsl.getDouble("Mad_cf"));
                bsv.setAar_cf(rsl.getDouble("Aar_cf"));
                bsv.setAad_cf(rsl.getDouble("Aad_cf"));
                bsv.setAd_cf(rsl.getDouble("Ad_cf"));
                bsv.setVad_cf(rsl.getDouble("Vad_cf"));
                bsv.setVdaf_cf(rsl.getDouble("Vdaf_cf"));
                bsv.setStad_cf(rsl.getDouble("Stad_cf"));
                bsv.setStar_cf(rsl.getDouble("Star_cf"));
                bsv.setHad_cf(rsl.getDouble("Had_cf"));
                bsv.setQbad_cf(rsl.getDouble("Qbad_cf"));
                bsv.setQgrad_cf(rsl.getDouble("Qgrad_cf"));
                bsv.setT2_cf(rsl.getDouble("T2_cf"));

//              矿方指标
                bsv.setQnetar_kf(rsl.getDouble("Qnetar_kf"));
                bsv.setStd_kf(rsl.getDouble("Std_kf"));
                bsv.setStar_kf(rsl.getDouble("Star_kf"));
                bsv.setMt_kf(rsl.getDouble("Mt_kf"));
                bsv.setMad_kf(rsl.getDouble("Mad_kf"));
                bsv.setAar_kf(rsl.getDouble("Aar_kf"));
                bsv.setAad_kf(rsl.getDouble("Aad_kf"));
                bsv.setAd_kf(rsl.getDouble("Ad_kf"));
                bsv.setVad_kf(rsl.getDouble("Vad_kf"));
                bsv.setVdaf_kf(rsl.getDouble("Vdaf_kf"));
                bsv.setStad_kf(rsl.getDouble("Stad_kf"));
                bsv.setHad_kf(rsl.getDouble("Had_kf"));
                bsv.setQbad_kf(rsl.getDouble("Qbad_kf"));
                bsv.setQgrad_kf(rsl.getDouble("Qgrad_kf"));
                bsv.setT2_kf(rsl.getDouble("T2_kf"));

//              结算指标
                String strcforkf = "_cf";
//                if(jies_shifykfzljs.equals("是")){
                if (bsv.getShifykfzljs().equals("是")) {
//                	是否用矿方质量结算
                    strcforkf = "_kf";
                }
                String Q=rsl.getString("Qnetar_kf");
                //临河新增判断
                strcforkf=Q!=null&&Double.parseDouble(Q)>0?"_kf":"_cf";
                boolean is_datong = MainGlobal.getXitxx_item("结算", "大同结算条件特殊处理", "300", "否").equals("是");
                if (is_datong) {
                    if (Jieslx == Locale.liangpjs_feiylbb_id || Jieslx == Locale.meikjs_feiylbb_id) {
                        strcforkf = "_js";
                    }

                }

                if (Jieszbsftz.equals("是")) {

                    bsv.setQnetar_js(rsl.getDouble("Qnetar_js"));
                    bsv.setStd_js(rsl.getDouble("Std_js"));
                    bsv.setMt_js(rsl.getDouble("Mt_js"));
                    bsv.setMad_js(rsl.getDouble("Mad_js"));
                    bsv.setAar_js(rsl.getDouble("Aar_js"));
                    bsv.setAad_js(rsl.getDouble("Aad_js"));
                    bsv.setAd_js(rsl.getDouble("Ad_js"));
                    bsv.setVad_js(rsl.getDouble("Vad_js"));
                    bsv.setVdaf_js(rsl.getDouble("Vdaf_js"));
                    if (!bsv.getDanglgs().equals("")) { // 如果结算设置方案中设置了当量的公式，那么用当量的值替换stad_js的值
                        bsv.setStad_js(rsl.getDouble("dangl"));
                    } else {
                        bsv.setStad_js(rsl.getDouble("Stad" + strcforkf));
                    }
                    bsv.setStar_js(rsl.getDouble("Star_js"));
                    bsv.setHad_js(rsl.getDouble("Had_js"));
                    bsv.setQbad_js(rsl.getDouble("Qbad_js"));
                    bsv.setQgrad_js(rsl.getDouble("Qgrad_js"));
                    bsv.setT2_js(rsl.getDouble("T2_js"));

                } else if (Jieszbsftz.equals("否")) {

                    bsv.setQnetar_js(rsl.getDouble("Qnetar" + strcforkf));
                    bsv.setStd_js(rsl.getDouble("Std" + strcforkf));
                    bsv.setMt_js(rsl.getDouble("Mt" + strcforkf));
                    bsv.setMad_js(rsl.getDouble("Mad" + strcforkf));
                    bsv.setAar_js(rsl.getDouble("Aar" + strcforkf));
                    bsv.setAad_js(rsl.getDouble("Aad" + strcforkf));
                    bsv.setAd_js(rsl.getDouble("Ad" + strcforkf));
                    bsv.setVad_js(rsl.getDouble("Vad" + strcforkf));
                    bsv.setVdaf_js(rsl.getDouble("Vdaf" + strcforkf));
                    if (!bsv.getDanglgs().equals("")) { // 如果结算设置方案中设置了当量的公式，那么用当量的值替换stad_js的值
                        bsv.setStad_js(rsl.getDouble("dangl"));
                    } else {
                        bsv.setStad_js(rsl.getDouble("Stad" + strcforkf));
                    }
                    bsv.setStar_js(rsl.getDouble("Star" + strcforkf));
                    bsv.setHad_js(rsl.getDouble("Had" + strcforkf));
                    bsv.setQbad_js(rsl.getDouble("Qbad" + strcforkf));
                    bsv.setQgrad_js(rsl.getDouble("Qgrad" + strcforkf));
                    bsv.setT2_js(rsl.getDouble("T2" + strcforkf));


                    if (bsv.getQnetar_js() == 0) {

                        bsv.setErroInfo("没有矿方化验数据，请录入！");
                    }
                }
                bsv.setYunju_js(bsv.getYunju_cf());        //运距赋值
                bsv.setYunju_jsbz(String.valueOf(bsv.getYunju_js()));    //结算表中（yunj赋值）
                if (bsv.getKoud() + bsv.getKous() + bsv.getKouz() + bsv.getKoud_js() > 0) {
                    if (!is_datong) {//不是大同时,显示扣吨
                        bsv.setBeiz(bsv.getBeiz() + " 结算扣吨:" + (bsv.getKoud() + bsv.getKous() + bsv.getKouz() + bsv.getKoud_js()));
                    }

                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
        return bsv;
    }

    //计算煤价,热量折价,硫折价,灰熔点折价
    private boolean getMeiPrice(long Ranlpzb_id, long Yunsfsb_id, long Faz_id, long Daoz_id, long Diancxxb_id,
                                long Hetb_id, Date Minfahrq, long Jieslx,
                                String Jieszbsftz, String SelIds, long Gongysb_id,
                                double Jieskdl, long yunsdwb_id, double Shangcjsl) {
//		结算金额取整方式
        String jiesjdqzfs = MainGlobal.getXitxx_item("结算", "结算金额取整方式", String.valueOf(Diancxxb_id), "Round_new");
        bsv.setJiesjeqzfs(jiesjdqzfs);
        //得到合同信息中的运价
        JDBCcon con = new JDBCcon();
        String sql = "";
//        Interpreter bsh = new Interpreter();
        Interpreter bsh = new Jiesgs();
        Jiesdcz Jscz = new Jiesdcz();
        try {

//				数量(合同中以月为单位，每月存一条，暂定先取一条数)
            sql = "select nvl(htsl.hetl,0) as hetl,htb.gongfdwmc,htb.gongfkhyh,htb.gongfzh \n"
                    + " from hetb htb, hetslb htsl		\n"
                    + " where htb.id=htsl.hetb_id(+)	\n"
                    + " and (htsl.pinzb_id=" + Ranlpzb_id + " or htsl.pinzb_id is null) and (yunsfsb_id=" + Yunsfsb_id
                    + " or yunsfsb_id is null) and (faz_id=" + Faz_id + " or faz_id is null) and (daoz_id=" + Daoz_id + " or daoz_id is null)	\n"
                    + " and (htsl.diancxxb_id=" + Diancxxb_id + " or htsl.diancxxb_id is null) and (htsl.riq<=to_date('" + Jiesdcz.FormatDate(Minfahrq)
                    + "','yyyy-MM-dd') or htsl.riq is null)	\n"
                    + " and htb.id=" + Hetb_id + "";

            ResultSetList rsl = con.getResultSetList(sql);
            if (rsl.next()) {

                bsv.setHetml(rsl.getString("hetl"));
                bsv.setShoukdw(rsl.getString("gongfdwmc"));
                bsv.setKaihyh(rsl.getString("gongfkhyh"));
                bsv.setZhangH(rsl.getString("gongfzh"));
            }

//				质量(合同中一个合同号对应多个质量记录)
            sql = "select zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw	\n"
                    + " from hetb htb, hetzlb htzl,zhibb zbb,tiaojb tjb,danwb dwb	\n"
                    + " where htb.id=htzl.hetb_id and htzl.zhibb_id=zbb.id and htzl.tiaojb_id=tjb.id	\n"
                    + " and htzl.danwb_id=dwb.id	\n"
                    + " and tjb.leib=1 and zbb.leib=1 and tjb.leib=1	\n"
                    + " and htb.id=" + Hetb_id + " ";

//				得到增扣款中需要特殊处理(单批次、适用范围=“超出部分”)的指标，如果指标的类别为3属于需要特殊处理的合同结算指标
            sql = "select distinct zbb.bianm as zhib,nvl(shiyfw,0) as shiyfw\n" +
                    "        from hetb htb, hetzkkb htzkk,zhibb zbb,hetjsxsb xs\n" +
                    "        where htb.id=htzkk.hetb_id\n" +
                    "              and htzkk.zhibb_id=zbb.id\n" +
                    "              and htzkk.hetjsxsb_id=xs.id(+)\n" +
                    "              and (xs.bianm='" + Locale.danpc_jiesxs + "' or htzkk.shiyfw=1) \n" +
                    "              and (zbb.leib=1 or zbb.leib=3)\n" +
                    "              and htb.id=" + Hetb_id + "";

            rsl = con.getResultSetList(sql);
            while (rsl.next()) {
//					SelIds为全部结算lie_id
//					特殊指标数组只能赋值一次
                bsv.setJieszbtscl_Items(bsv.getJieszbtscl_Items() + Jiesdcz.getJieszbtscl(rsl.getString("zhib"), SelIds, rsl.getInt("shiyfw")));
            }

            {    //西部热电     部分奖款、扣款功能
                String s =
                        "select canzxx,xiax,shangx,kouj,zengfj from hetb h,hetzkkb zkk\n" +
                                "where h.id=zkk.hetb_id and h.id=" + Hetb_id + "\n" +
                                "and zkk.zhibb_id=1\n" +
                                "--and shiyfw=0\n" +
                                "and canzxm=2\n" +

                                "order by canzxx";
                ResultSetList rs = con.getResultSetList(s);
                while (rs.next()) {
                    if (rs.getDouble("kouj") > 0) {
                        s =
                                "select sum(f.maoz-f.piz) yanssl\n" +
                                        "  from fahb f, zhilb z\n" +
                                        " where f.zhilb_id = z.id\n" +
                                        "   and f.lie_id in (" + SelIds + ")\n" +
                                        "   and round_new(z.qnet_ar/4.1816*1000,0) >=" + rs.getDouble("canzxx");
                        ResultSetList rs1 = con.getResultSetList(s);
                        if (rs1.next()) {
                            bsv.setShul_xb_kou_static_status(true);
                            bsv.setShul_xb_kou_static_value(rs1.getDouble("yanssl"));
                        }
                    } else if (rs.getDouble("zengfj") > 0) {
                        s =
                                "select sum(f.maoz-f.piz) yanssl\n" +
                                        "  from fahb f, zhilb z\n" +
                                        " where f.zhilb_id = z.id\n" +
                                        "   and f.lie_id in (" + SelIds + ")\n" +
                                        "   and round_new(z.qnet_ar/4.1816*1000,0) >=" + rs.getDouble("canzxx");
                        ResultSetList rs1 = con.getResultSetList(s);
                        if (rs1.next()) {
                            bsv.setShul_xb_jiang_static_status(true);
                            bsv.setShul_xb_jiang_static_value(rs1.getDouble("yanssl"));
                        }

                        bsv.setShul_rz_szs_jiang_static_value(rs.getDouble("canzxx"));
                    }
                }
            }

            if (!bsv.getJieszbtscl_Items().equals("")
                    && bsv.getTsclzbs() == null) {
//					说明有要特殊处理的指标
                String ArrayTsclzbs[] = null;
                ArrayTsclzbs = bsv.getJieszbtscl_Items().split(";");
                bsv.setTsclzbs(ArrayTsclzbs);
//					0,运距,meikxxb_id,100,10,0;
//					1,运距,meikxxb_id,101,15,0;
//					2,运距,meikxxb_id,102,20,0;
//					3,Std,meikxxb_id,100,1.0,0;
//					超出部分组成的特殊数组
//					0、数量、'jiessl'、'jiessl'、'C'、0

            }

//				得到增扣款中需要特殊处理（加权平均）的指标，在这种情况下，我们一般认为价格里面的加权方式为“单批次”
//				得到增扣款中需要采用“加权分列”计算增扣款的指标
            sql = "select distinct zbb.bianm as zhib \n" +
                    "        from hetb htb, hetzkkb htzkk,zhibb zbb,hetjsxsb xs\n" +
                    "        where htb.id=htzkk.hetb_id\n" +
                    "              and htzkk.zhibb_id=zbb.id\n" +
                    "              and htzkk.hetjsxsb_id=xs.id(+)\n" +
                    "              and (xs.bianm='" + Locale.jiaqpj_jiesxs + "' or xs.bianm='" + Locale.jiaqfl_jiesxs + "') \n" +
                    "              and (zbb.leib=1 or zbb.leib=3)\n" +
                    "              and htb.id=" + Hetb_id + "";

            rsl = con.getResultSetList(sql);
            if (rsl.getRows() > 0) {
//					说明有记录
//					定义StringBuffer 用来存储要特殊处理的指标
                StringBuffer sb = new StringBuffer("");
                while (rsl.next()) {

                    sb.append(rsl.getString("zhib")).append(",");
                }

                sb.deleteCharAt(sb.length() - 1);

                if (sb.length() > 0) {
//						为这些需要特殊处理的指标打上标志
                    Jiesdcz.setJieszbtscl_Jqpj_Flag(bsv, sb);
                    bsv.setJiesxs(Locale.jiaqpj_jiesxs);
//						以加权平均的方式调用getJiesslzl方法，为bsv中的所有指标赋值
                    getJiesszl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl, bsv.getYunsdwb_id(), Jieslx, Shangcjsl, "");
//						为这些指标的static_value项赋值
                    Jiesdcz.setJieszbtscl_Jqpj_value(bsv, sb);
                }
            }

//				合同变量
            sql = "select bianlmc, value from hetblb where hetb_id=" + Hetb_id;
            rsl = con.getResultSetList(sql);

            while (rsl.next()) {

                if (MainGlobal.isDigit(rsl.getString("value"))) {
//						是数字
                    bsh.set(rsl.getString("bianlmc"), rsl.getDouble("value"));
                } else {
//						不是数字
                    bsh.set(rsl.getString("bianlmc"), rsl.getString("value"));
                }
            }

//				价格（合同中一个合同对应多个基础价格）
            sql = "select htjg.id as hetjgb_id,zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw,jij,jijlx,	\n"
                    + " jijdw.bianm as jijdw,nvl(jijgs,'') as jijgs,jsfs.bianm as jiesfs,jsxs.bianm as jiesxs,yunj,htjg.pinzb_id,		\n"
                    + " yjdw.bianm as yunjdw,yingdkf,ysfs.mingc as yunsfs,zuigmj,htjfsb.bianm as hejfs,fengsjj,htb.gongfdwmc,htb.gongfkhyh,htb.gongfzh	\n"
                    + " from hetb htb, hetjgb htjg,zhibb zbb,tiaojb tjb,danwb dwb,danwb jijdw,hetjsfsb jsfs,	\n"
                    + " hetjsxsb jsxs,danwb yjdw,yunsfsb ysfs,hetjjfsb htjfsb									\n"
                    + " where htb.id=htjg.hetb_id and htjg.zhibb_id=zbb.id and htjg.tiaojb_id=tjb.id			\n"
                    + " and htjg.danwb_id=dwb.id and htjg.jijdwid=jijdw.id and htjg.hetjsfsb_id=jsfs.id			\n"
                    + " and htjg.hetjsxsb_id=jsxs.id and htjg.yunjdw_id=yjdw.id(+)								\n"
                    + " and htjg.yunsfsb_id=ysfs.id																\n"
                    + " and htjg.hetjjfsb_id=htjfsb.id															\n"
                    + " and tjb.leib=1 and zbb.leib=1 and tjb.leib=1											\n"
                    + " and htb.id=" + Hetb_id + " order by zbb.bianm,tjb.bianm,xiax,shangx";

            rsl = con.getResultSetList(sql);

            if (rsl.next()) {

//					通过合同设置结算值
                setJieshtinfo(rsl, bsh);

//					合同基价指标,取出符合条件的合同基价
                if (rsl.getRows() == 1) {

//						就一条合同
//						目录价

//						计算运费
//						if(Jieslx==Locale.liangpjs_feiylbb_id){
//
//							getYunFei(SelIds,Jieslx,bsv.getHetb_Id(),Shangcjsl);
//						}

                    if (bsv.getHetjjfs().equals(Locale.mulj_hetjjfs)) {    //								目录价结算
                        System.out.println("===============Locale.mulj_hetjjfs==============");
                        if (bsv.getJiesxs().equals(Locale.danpc_jiesxs)) {//								单批次结算
                            System.out.println("===============1.Locale.danpc_jiesxs==============");
                            String[] test = new String[1];

                            if (SelIds.indexOf(",") == -1) {

                                test[0] = SelIds;
                            } else {

                                test = SelIds.split(",");
                            }

                            for (int i = 0; i < test.length; i++) {

//									获得结算数量、质量
                                getJiesszl(Jieszbsftz, test[i], Diancxxb_id, Gongysb_id, Hetb_id,
                                        Jieskdl, yunsdwb_id, Jieslx, Shangcjsl, "");

//									为目录价赋值
                                computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);

//									获得增扣款
                                getZengkk(Hetb_id, bsh, true, null);

//									煤款含税单价保留小数位
                                bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//									含税单价取整方式
                                bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//									用户自定义公式
                                bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());

//									增扣款保留小数位
                                bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//									执行合同价格公式
                                ExecuteHetmdjgs(bsh);

//									执行公式
                                bsh.eval(bsv.getGongs_Mk());

//									得到计算后的指标
                                setJieszb(bsh, 0, Shangcjsl);

//									根据增扣款中设置的指标基价类型(jijlx=0为含税、jijlx=1为不含税)重新计算煤款含税单价，并对煤款含税单价进行取整处理。
                                getMeikhsdj(bsh);

//									计算煤款金额
                                computData_Dpc(SelIds, Hetb_id, Shangcjsl, test[i]);
                            }

                        } else if (bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)) {//						加权平均

//								获得结算数量、质量
                            System.out.println("===============1.Locale.jiaqpj_jiesxs==============");
                            getJiesszl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id,
                                    Jieskdl, yunsdwb_id, Jieslx, Shangcjsl, "");

//								为目录价赋值
                            computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);

//								获得增扣款
                            getZengkk(Hetb_id, bsh, true, null);

//								煤款含税单价保留小数位
                            bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//								用户自定义公式
                            bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());

//								含税单价取整方式
                            bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//								增扣款保留小数位
                            bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//								执行合同价格公式
                            ExecuteHetmdjgs(bsh);

//								执行公式
                            bsh.eval(bsv.getGongs_Mk());

//								得到计算后的指标
                            setJieszb(bsh, 0, Shangcjsl);

                            if (bsv.getTsclzbs() != null) {
//									如果存在需要单独处理的特殊指标
                                Jiestszbcl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id,
                                        Jieskdl, yunsdwb_id, Jieslx, Shangcjsl);
                            }

//								根据增扣款中设置的指标基价类型(jijlx=0为含税、jijlx=1为不含税)重新计算煤款含税单价，并对煤款含税单价进行取整处理。
                            getMeikhsdj(bsh);

//								计算煤款金额
                            computData(SelIds, Hetb_id, Shangcjsl);

                        }

                    } else {

//							一条价格
//							非目录价
                        if (bsv.getJiesxs().equals(Locale.danpc_jiesxs)) {//							单批次结算
                            System.out.println("===============2.Locale.danpc_jiesxs==============");
                            String[] test = new String[1];

                            if (SelIds.indexOf(",") == -1) {

                                test[0] = SelIds;
                            } else {

                                test = SelIds.split(",");
                            }

//								在单条价格且非目录价，结算类型为单批次结算时，判断是否采用西部特殊结算
                            bsv.setShul_xb_dongt_static_value(0);
                            ResultSetList rrss = null;

                            if ("是".equals(MainGlobal.getXitxx_item("结算", "扣%吨时，数量系数以验收数量为准", String.valueOf(visit.getDiancxxb_id()), "否"))) {
                                bsv.setKoud_shulxs_yanssl(true);
                            }
                            bsv.setKoud_plm_ws(Integer.parseInt(MainGlobal.getXitxx_item("结算", "扣%吨时，一批煤多指标扣吨，最终保留位数", String.valueOf(visit.getDiancxxb_id()), "5")));
                            if ("是".equals(MainGlobal.getXitxx_item("结算", "单批次结算处理符合参照项目的数量部分", String.valueOf(visit.getDiancxxb_id()), "否"))) {
                                rrss = con.getResultSetList("select sum(sanfsl) yanssl from fahb f,zhilb z where f.zhilb_id=z.id and f.lie_id in(" + SelIds + ") and z.qnet_ar/4.1816*1000>=" + bsv.getShul_rz_szs_jiang_static_value());
                                rrss.next();
                                bsv.setShul_xb_dongt_static_value(rrss.getDouble("yanssl"));
                            }

                            for (int i = 0; i < test.length; i++) {

//									获得结算数量、质量
                                getJiesszl(Jieszbsftz, test[i], Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl,
                                        yunsdwb_id, Jieslx, Shangcjsl, "");

                                double Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");

                                Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());

                                bsh.set(rsl.getString("zhib") + Jiesdcz.getZhibbdw(rsl.getString("zhib"), rsl.getString("danw")), Dbltmp);    //结算值
                                bsh.set(rsl.getString("zhib") + "计量单位", rsl.getString("danw"));                                //指标单位

                                bsv.setYifzzb(rsl.getString("zhib"));    //默认的已赋值指标

                                bsh.set(rsl.getString("zhib") + "上限", rsl.getDouble("shangx"));        //指标上限
                                bsh.set(rsl.getString("zhib") + "下限", rsl.getDouble("xiax"));            //指标下限

                                bsh.set(rsl.getString("zhib") + "增付单价", 0);
                                bsh.set(rsl.getString("zhib") + "增付单价公式", "");
                                bsh.set(rsl.getString("zhib") + "扣付单价", 0);
                                bsh.set(rsl.getString("zhib") + "扣付单价公式", "");
                                bsh.set(rsl.getString("zhib") + "增付价单位", "");
                                bsh.set(rsl.getString("zhib") + "增扣款条件", "");
                                bsh.set(rsl.getString("zhib") + "增扣款基数", 0);
                                bsh.set(rsl.getString("zhib") + "增扣款基数单位", "");
                                bsh.set(rsl.getString("zhib") + "基准增扣价", 0);
                                bsh.set(rsl.getString("zhib") + "小数处理", "");
                                bsh.set(rsl.getString("zhib") + "强制计算", "false");


//									获得增扣款
                                getZengkk(Hetb_id, bsh, true, null);

//									用户自定义公式
                                bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());

//									煤款含税单价保留小数位
                                bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//									含税单价取整方式
                                bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//									增扣款保留小数位
                                bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//									执行合同价格公式
                                ExecuteHetmdjgs(bsh);

//									执行公式
                                bsh.eval(bsv.getGongs_Mk());

//									得到计算后的指标
                                setJieszb(bsh, 0, Shangcjsl);

//									根据增扣款中设置的指标基价类型(jijlx=0为含税、jijlx=1为不含税)重新计算煤款含税单价，并对煤款含税单价进行取整处理。
                                getMeikhsdj(bsh);

//									计算煤款金额
                                computData_Dpc(SelIds, Hetb_id, Shangcjsl, test[i]);
                            }

                        } else if (bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)) {//					加权平均

//								获得结算数量、质量
                            System.out.println("===============2.Locale.jiaqpj_jiesxs==============");
                            getJiesszl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id,
                                    Hetb_id, Jieskdl, yunsdwb_id, Jieslx, Shangcjsl, "");

                            double Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");
                            Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());

                            bsh.set(rsl.getString("zhib") + Jiesdcz.getZhibbdw(rsl.getString("zhib"), rsl.getString("danw")), Dbltmp);    //结算值
                            bsh.set(rsl.getString("zhib") + "计量单位", rsl.getString("danw"));

                            bsv.setYifzzb(rsl.getString("zhib"));    //默认的已赋值指标

                            bsh.set(rsl.getString("zhib") + "上限", rsl.getDouble("shangx"));        //指标上限
                            bsh.set(rsl.getString("zhib") + "下限", rsl.getDouble("xiax"));            //指标下限

                            bsh.set(rsl.getString("zhib") + "增付单价", 0);
                            bsh.set(rsl.getString("zhib") + "增付单价公式", "");
                            bsh.set(rsl.getString("zhib") + "扣付单价", 0);
                            bsh.set(rsl.getString("zhib") + "扣付单价公式", "");
                            bsh.set(rsl.getString("zhib") + "增付价单位", "");
                            bsh.set(rsl.getString("zhib") + "增扣款条件", "");
                            bsh.set(rsl.getString("zhib") + "增扣款基数", 0);
                            bsh.set(rsl.getString("zhib") + "增扣款基数单位", "");
                            bsh.set(rsl.getString("zhib") + "基准增扣价", 0);
                            bsh.set(rsl.getString("zhib") + "小数处理", "");
                            bsh.set(rsl.getString("zhib") + "强制计算", "false");

//								获得增扣款
                            getZengkk(Hetb_id, bsh, true, null);

//								用户自定义公式
                            bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());

//								煤款含税单价保留小数位
                            bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//								含税单价取整方式
                            bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//								增扣款保留小数位
                            bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//								执行合同价格公式
                            ExecuteHetmdjgs(bsh);

//								执行公式
                            bsh.eval(bsv.getGongs_Mk());

//								得到计算后的指标
                            setJieszb(bsh, 0, Shangcjsl);

                            if (bsv.getTsclzbs() != null) {
//									如果存在需要单独处理的特殊指标
                                Jiestszbcl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id,
                                        Jieskdl, yunsdwb_id, Jieslx, Shangcjsl);
                            }

//								根据增扣款中设置的指标基价类型(jijlx=0为含税、jijlx=1为不含税)重新计算煤款含税单价，并对煤款含税单价进行取整处理。
                            getMeikhsdj(bsh);

//								计算煤款金额
                            computData(SelIds, Hetb_id, Shangcjsl);

                        } else if (bsv.getJiesxs().equals(Locale.jiaqfl_jiesxs)) {//					加权分列

//								获得结算数量、质量（加权方式获取）
                            System.out.println("===============2.Locale.jiaqfl_jiesxs==============");
                            getJiesszl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id,
                                    Hetb_id, Jieskdl, yunsdwb_id, Jieslx, Shangcjsl, "");

                            double Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");
                            Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());

                            bsh.set(rsl.getString("zhib") + Jiesdcz.getZhibbdw(rsl.getString("zhib"), rsl.getString("danw")), Dbltmp);    //结算值
                            bsh.set(rsl.getString("zhib") + "计量单位", rsl.getString("danw"));

                            bsv.setYifzzb(rsl.getString("zhib"));    //默认的已赋值指标

                            bsh.set(rsl.getString("zhib") + "上限", rsl.getDouble("shangx"));        //指标上限
                            bsh.set(rsl.getString("zhib") + "下限", rsl.getDouble("xiax"));            //指标下限

                            bsh.set(rsl.getString("zhib") + "增付单价", 0);
                            bsh.set(rsl.getString("zhib") + "增付单价公式", "");
                            bsh.set(rsl.getString("zhib") + "扣付单价", 0);
                            bsh.set(rsl.getString("zhib") + "扣付单价公式", "");
                            bsh.set(rsl.getString("zhib") + "增付价单位", "");
                            bsh.set(rsl.getString("zhib") + "增扣款条件", "");
                            bsh.set(rsl.getString("zhib") + "增扣款基数", 0);
                            bsh.set(rsl.getString("zhib") + "增扣款基数单位", "");
                            bsh.set(rsl.getString("zhib") + "基准增扣价", 0);
                            bsh.set(rsl.getString("zhib") + "小数处理", "");
                            bsh.set(rsl.getString("zhib") + "强制计算", "false");

//								得到增扣款指标个数
                            ResultSetList rszkk = MainGlobal.getTableRsl(con,
                                    "select distinct zhibb.bianm as zhibbm\n" +
                                            "        from hetzkkb,zhibb\n" +
                                            "        where hetzkkb.zhibb_id = zhibb.id\n" +
                                            "				and zhibb.leib=1\n" +
                                            "              and hetzkkb.hetb_id=" + Hetb_id);

                            bsv.setJiaqfl_zhibsz(new String[rszkk.getRows()][14]);

                            for (int i = 0; i < bsv.getJiaqfl_zhibsz().length; i++) {

                                rszkk.next();
                                bsv.getJiaqfl_zhibsz()[i][0] = rszkk.getString("zhibbm");
                            }
                            rszkk.close();

//								获得增扣款（调用增扣款函数取得全局增扣款标准，放入“加权分列指标数组”）
                            getZengkk(Hetb_id, bsh, true, null);

//								“加权分列指标数组”初始化完成，下面调用单批次结算算法计算
                            String[] test = new String[1];

                            if (SelIds.indexOf(",") == -1) {

                                test[0] = SelIds;
                            } else {

                                test = SelIds.split(",");
                            }

                            for (int i = 0; i < test.length; i++) {

//									获得结算数量、质量
                                getJiesszl(Jieszbsftz, test[i], Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl,
                                        yunsdwb_id, Jieslx, Shangcjsl, "");

                                Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");

                                Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());

                                bsh.set(rsl.getString("zhib") + Jiesdcz.getZhibbdw(rsl.getString("zhib"), rsl.getString("danw")), Dbltmp);    //结算值
                                bsh.set(rsl.getString("zhib") + "计量单位", rsl.getString("danw"));                                //指标单位

                                bsv.setYifzzb(rsl.getString("zhib"));    //默认的已赋值指标

                                bsh.set(rsl.getString("zhib") + "上限", rsl.getDouble("shangx"));        //指标上限
                                bsh.set(rsl.getString("zhib") + "下限", rsl.getDouble("xiax"));            //指标下限

                                bsh.set(rsl.getString("zhib") + "增付单价", 0);
                                bsh.set(rsl.getString("zhib") + "增付单价公式", "");
                                bsh.set(rsl.getString("zhib") + "扣付单价", 0);
                                bsh.set(rsl.getString("zhib") + "扣付单价公式", "");
                                bsh.set(rsl.getString("zhib") + "增付价单位", "");
                                bsh.set(rsl.getString("zhib") + "增扣款条件", "");
                                bsh.set(rsl.getString("zhib") + "增扣款基数", 0);
                                bsh.set(rsl.getString("zhib") + "增扣款基数单位", "");
                                bsh.set(rsl.getString("zhib") + "基准增扣价", 0);
                                bsh.set(rsl.getString("zhib") + "小数处理", "");
                                bsh.set(rsl.getString("zhib") + "强制计算", "false");

//									获得增扣款
//									getZengkk(Hetb_id,bsh,true,null);
                                getZengkk_Jiaqfl(bsh);    //加权分列增扣款

//									用户自定义公式
                                bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());

//									煤款含税单价保留小数位
                                bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//									含税单价取整方式
                                bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//									增扣款保留小数位
                                bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//									执行合同价格公式
                                ExecuteHetmdjgs(bsh);

//									执行公式
                                bsh.eval(bsv.getGongs_Mk());

//									得到计算后的指标
                                setJieszb(bsh, 0, Shangcjsl);

//									根据增扣款中设置的指标基价类型(jijlx=0为含税、jijlx=1为不含税)重新计算煤款含税单价，并对煤款含税单价进行取整处理。
                                getMeikhsdj(bsh);

//									计算煤款金额
                                computData_Dpc(SelIds, Hetb_id, Shangcjsl, test[i]);
                            }
                        }
                    }

                    bsv.setHetjgpp_Flag(true);
                    bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));
                } else {
//						有多个价格
//						目录价

                    if (bsv.getHetjjfs().equals(Locale.mulj_hetjjfs)) {            //目录价结算

//							计算运费
//							if(Jieslx==Locale.liangpjs_feiylbb_id){
//
//								getYunFei(SelIds,Jieslx,bsv.getHetb_Id(),Shangcjsl);
//							}
                        System.out.println("===============3.Locale.mulj_hetjjfs==============");
                        if (bsv.getJiesxs().equals(Locale.danpc_jiesxs)) {        //单批次结算
                            System.out.println("===============3.Locale.danpc_jiesxs==============");
                            String[] test = new String[1];

                            if (SelIds.indexOf(",") == -1) {

                                test[0] = SelIds;
                            } else {

                                test = SelIds.split(",");
                            }

                            for (int i = 0; i < test.length; i++) {

                                //								获得结算数量、质量
                                getJiesszl(Jieszbsftz, test[i], Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl,
                                        yunsdwb_id, Jieslx, Shangcjsl, "");

                                //								为目录价赋值
                                computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);

                                //								获得增扣款
                                getZengkk(Hetb_id, bsh, true, null);

//									用户自定义公式
                                bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());

//									煤款含税单价保留小数位
                                bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//									含税单价取整方式
                                bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//									增扣款保留小数位
                                bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//									执行合同价格公式
                                ExecuteHetmdjgs(bsh);

                                //								执行公式
                                bsh.eval(bsv.getGongs_Mk());

                                //								得到计算后的指标
                                setJieszb(bsh, 0, Shangcjsl);

//									根据增扣款中设置的指标基价类型(jijlx=0为含税、jijlx=1为不含税)重新计算煤款含税单价，并对煤款含税单价进行取整处理。
                                getMeikhsdj(bsh);

                                //								计算煤款金额
                                computData_Dpc(SelIds, Hetb_id, Shangcjsl, test[i]);
                            }
                        } else if (bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)) {//					加权平均


//								获得结算数量、质量
                            System.out.println("===============3.Locale.jiaqpj_jiesxs==============");
                            getJiesszl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl,
                                    yunsdwb_id, Jieslx, Shangcjsl, "");

//								为目录价赋值
                            computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);

//								获得增扣款
                            getZengkk(Hetb_id, bsh, true, null);

//								用户自定义公式
                            bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());

//								煤款含税单价保留小数位
                            bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//								含税单价取整方式
                            bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//								增扣款保留小数位
                            bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//								执行合同价格公式
                            ExecuteHetmdjgs(bsh);

//								执行公式
                            bsh.eval(bsv.getGongs_Mk());

//								得到计算后的指标
                            setJieszb(bsh, 0, Shangcjsl);

                            if (bsv.getTsclzbs() != null) {
//									如果存在需要单独处理的特殊指标
                                Jiestszbcl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id,
                                        Jieskdl, yunsdwb_id, Jieslx, Shangcjsl);
                            }

//								根据增扣款中设置的指标基价类型(jijlx=0为含税、jijlx=1为不含税)重新计算煤款含税单价，并对煤款含税单价进行取整处理。
                            getMeikhsdj(bsh);

//								计算煤款金额
                            computData(SelIds, Hetb_id, Shangcjsl);
                        }
                    } else {

//							多个价格
//							非目录价
                        double Dbljijzb = 0;

                        if (bsv.getJiesxs().equals(Locale.danpc_jiesxs)) {    //单批次结算

//									判断参数 "单批次结算用加权平均确定价格"，值为"是"或"否"
//									当合同价格中，有多条价格条款时。在单批次结算处理时，判断该参数的值为"是"，就进行加权平均计算结算价格。
                            System.out.println("===============3.Locale.danpc_jiesxs==============");
                            String danpcjsyjqpjqdjg = "否";

                            danpcjsyjqpjqdjg = MainGlobal.getXitxx_item("结算", Locale.danpcjsyjqpjqdjg,
                                    String.valueOf(Diancxxb_id), danpcjsyjqpjqdjg);

                            danpcjsyjqpjqdjg = Jiesdcz.getJiessz_item(Diancxxb_id, Gongysb_id,
                                    Hetb_id, Locale.danpcjsyjqpjqdjg, danpcjsyjqpjqdjg);

                            if (danpcjsyjqpjqdjg.equals("是")) {

                                getJiesszl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl,
                                        yunsdwb_id, Jieslx, Shangcjsl, "");

                                rsl.beforefirst();
                                while (rsl.next()) {

                                    Dbljijzb = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");
                                    Dbljijzb = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbljijzb, bsv.getMj_to_kcal_xsclfs());
                                    bsv.setJiagpzId(rsl.getString("pinzb_id"));

                                    if (Dbljijzb >= rsl.getDouble("xiax") && Dbljijzb <= (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx"))
                                            && Jiesdcz.getHet_condition(bsv.getJiagpzId(), String.valueOf(bsv.getRanlpzb_Id()))
                                            ) {

                                        setJieshtinfo(rsl, bsh);

//												设置结算基础数据
                                        bsh.set(rsl.getString("zhib") + Jiesdcz.getZhibbdw(rsl.getString("zhib"), rsl.getString("danw")), Dbljijzb);    //结算值
                                        bsh.set(rsl.getString("zhib") + "计量单位", rsl.getString("danw"));

                                        bsv.setYifzzb(rsl.getString("zhib"));    //默认的已赋值指标

                                        bsh.set(rsl.getString("zhib") + "上限", rsl.getDouble("shangx"));        //指标上限
                                        bsh.set(rsl.getString("zhib") + "下限", rsl.getDouble("xiax"));            //指标下限

                                        bsh.set(rsl.getString("zhib") + "增付单价", 0);
                                        bsh.set(rsl.getString("zhib") + "增付单价公式", "");
                                        bsh.set(rsl.getString("zhib") + "扣付单价", 0);
                                        bsh.set(rsl.getString("zhib") + "扣付单价公式", "");
                                        bsh.set(rsl.getString("zhib") + "增付价单位", "");
                                        bsh.set(rsl.getString("zhib") + "增扣款条件", "");
                                        bsh.set(rsl.getString("zhib") + "增扣款基数", 0);
                                        bsh.set(rsl.getString("zhib") + "增扣款基数单位", "");
                                        bsh.set(rsl.getString("zhib") + "基准增扣价", 0);
                                        bsh.set(rsl.getString("zhib") + "小数处理", "");
                                        bsh.set(rsl.getString("zhib") + "强制计算", "false");

                                        break;
                                    }
                                }
                            }

                            String[] test = new String[1];

                            if (SelIds.indexOf(",") == -1) {

                                test[0] = SelIds;
                            } else {

                                test = SelIds.split(",");
                            }

                            bsv.setShul_xb_dongt_static_value(0);
                            ResultSetList rrss = null;
                            //石嘴山    处理：	全部煤加权值达到参照项目标准的所有批量煤
                            if ("是".equals(MainGlobal.getXitxx_item("结算", "单批次结算处理数量全部分", String.valueOf(visit.getDiancxxb_id()), "否"))) {
                                rrss = con.getResultSetList("select sum(sanfsl) yanssl from fahb  where lie_id in(" + SelIds + ")");
                                if (rrss.next()) {
                                    bsv.setShul_xb_dongt_static_value(rrss.getDouble("yanssl"));
                                }
                                rrss = con.getResultSetList("select round_new(sum(f.sanfsl*z.qnet_ar)/sum(f.sanfsl),2) qnet_ar from fahb f, zhilb z where f.zhilb_id = z.id and f.lie_id in(" + SelIds + ")");
                                if (rrss.next()) {
                                    bsv.setShul_rz_szs_jiang_static_status(true);
                                    bsv.setShul_rz_szs_jiang_static_value(rrss.getDouble("qnet_ar"));
                                }
                                rrss.close();

                            }

                            {//西部用
                                if ("是".equals(MainGlobal.getXitxx_item("结算", "扣%吨时，数量系数以验收数量为准", String.valueOf(visit.getDiancxxb_id()), "否"))) {
                                    bsv.setKoud_shulxs_yanssl(true);
                                }
                                bsv.setKoud_plm_ws(Integer.parseInt(MainGlobal.getXitxx_item("结算", "扣%吨时，一批煤多指标扣吨，最终保留位数", String.valueOf(visit.getDiancxxb_id()), "5")));
                                if ("是".equals(MainGlobal.getXitxx_item("结算", "单批次结算处理符合参照项目的数量部分", String.valueOf(visit.getDiancxxb_id()), "否"))) {
                                    rrss = con.getResultSetList("select sum(sanfsl) yanssl from fahb f,zhilb z where f.zhilb_id=z.id and f.lie_id in(" + SelIds + ") and z.qnet_ar/4.1816*1000>=" + bsv.getShul_rz_szs_jiang_static_value());
                                    rrss.next();
                                    bsv.setShul_xb_dongt_static_value(rrss.getDouble("yanssl"));
                                }
                            }
                            for (int i = 0; i < test.length; i++) {


//										获得结算数量、质量
                                getJiesszl(Jieszbsftz, test[i], Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl,
                                        yunsdwb_id, Jieslx, Shangcjsl, "");

//										单批次结算是否起用不同品种结算（起用：会增加con、影响结算速度）
                                if ("是".equals(MainGlobal.getXitxx_item("结算", "单批次结算是否起用不同品种结算", String.valueOf(visit.getDiancxxb_id()), "否"))) {
                                    rrss = con.getResultSetList("select f.pinzb_id from fahb f where f.lie_id in(" + test[i] + ")");
                                    if (rrss.next()) {
//												重新设置品种
                                        bsv.setRanlpzb_Id(rrss.getLong("pinzb_id"));
                                    }
                                    rrss.close();
                                }


                                //西部xb       处理:     单批煤达到参照项目标准,并超出数量部分的批量煤
                                if ("是".equals(MainGlobal.getXitxx_item("结算", "单批次结算处理数量超出部分", String.valueOf(visit.getDiancxxb_id()), "否"))) {
                                    rrss = con.getResultSetList("select sanfsl yanssl,z.qnet_ar/4.1816*1000 qnet_ar from fahb f,zhilb z where f.zhilb_id=z.id and f.lie_id in(" + test[i] + ")");
                                    if (rrss.next()) {
                                        if (rrss.getDouble("qnet_ar") >= bsv.getShul_rz_szs_jiang_static_value()) {
                                            bsv.setShul_xb_dongt_static_value(rrss.getDouble("yanssl") + bsv.getShul_xb_dongt_static_value());
                                        }
                                    }
                                    rrss.close();
                                }

                                rsl.beforefirst();

                                if (danpcjsyjqpjqdjg.equals("是")) {


//												获得增扣款
                                    getZengkk(Hetb_id, bsh, true, null);

//												用户自定义公式
                                    bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());

//												煤款含税单价保留小数位
                                    bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//												含税单价取整方式
                                    bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//												增扣款保留小数位
                                    bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//												执行合同价格公式
                                    ExecuteHetmdjgs(bsh);

//												执行公式
                                    bsh.eval(bsv.getGongs_Mk());

//												得到计算后的指标
                                    setJieszb(bsh, 0, Shangcjsl);

//												根据增扣款中设置的指标基价类型(jijlx=0为含税、jijlx=1为不含税)重新计算煤款含税单价，并对煤款含税单价进行取整处理。
                                    getMeikhsdj(bsh);

//												计算煤款金额
                                    computData_Dpc(SelIds, Hetb_id, Shangcjsl, test[i]);

                                    bsv.setHetjgpp_Flag(true);
                                    bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));

                                } else {

                                    while (rsl.next()) {

                                        Dbljijzb = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");
                                        Dbljijzb = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbljijzb, bsv.getMj_to_kcal_xsclfs());
                                        bsv.setJiagpzId(rsl.getString("pinzb_id"));

                                        if (Dbljijzb >= rsl.getDouble("xiax") && Dbljijzb <= (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx"))
                                                && Jiesdcz.getHet_condition(bsv.getJiagpzId(), String.valueOf(bsv.getRanlpzb_Id()))
                                                ) {

                                            setJieshtinfo(rsl, bsh);

//													计算运费（注意：只要算一次，要加一个变量判断）
//													if(Jieslx==Locale.liangpjs_feiylbb_id&&!bsv.getDanpcysyf()){
////														判断条件：1、是两票结算；2、单批次结算还没有算过运费
//														getYunFei(SelIds,Jieslx,bsv.getHetb_Id(),Shangcjsl);
//														bsv.setDanpcysyf(true);
//													}

//													设置结算基础数据
                                            bsh.set(rsl.getString("zhib") + Jiesdcz.getZhibbdw(rsl.getString("zhib"), rsl.getString("danw")), Dbljijzb);    //结算值
                                            bsh.set(rsl.getString("zhib") + "计量单位", rsl.getString("danw"));

                                            bsv.setYifzzb(rsl.getString("zhib"));    //默认的已赋值指标

                                            bsh.set(rsl.getString("zhib") + "上限", rsl.getDouble("shangx"));        //指标上限
                                            bsh.set(rsl.getString("zhib") + "下限", rsl.getDouble("xiax"));            //指标下限

                                            bsh.set(rsl.getString("zhib") + "增付单价", 0);
                                            bsh.set(rsl.getString("zhib") + "增付单价公式", "");
                                            bsh.set(rsl.getString("zhib") + "扣付单价", 0);
                                            bsh.set(rsl.getString("zhib") + "扣付单价公式", "");
                                            bsh.set(rsl.getString("zhib") + "增付价单位", "");
                                            bsh.set(rsl.getString("zhib") + "增扣款条件", "");
                                            bsh.set(rsl.getString("zhib") + "增扣款基数", 0);
                                            bsh.set(rsl.getString("zhib") + "增扣款基数单位", "");
                                            bsh.set(rsl.getString("zhib") + "基准增扣价", 0);
                                            bsh.set(rsl.getString("zhib") + "小数处理", "");
                                            bsh.set(rsl.getString("zhib") + "强制计算", "false");

                                            //											获得增扣款
                                            getZengkk(Hetb_id, bsh, true, null);

                                            //											用户自定义公式
                                            bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());

//													煤款含税单价保留小数位
                                            bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//													含税单价取整方式
                                            bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//													增扣款保留小数位
                                            bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//													执行合同价格公式
                                            ExecuteHetmdjgs(bsh);

                                            //											执行公式
                                            bsh.eval(bsv.getGongs_Mk());

                                            //											得到计算后的指标
                                            setJieszb(bsh, 0, Shangcjsl);

//													根据增扣款中设置的指标基价类型(jijlx=0为含税、jijlx=1为不含税)重新计算煤款含税单价，并对煤款含税单价进行取整处理。
                                            getMeikhsdj(bsh);

                                            //											计算煤款金额
                                            computData_Dpc(SelIds, Hetb_id, Shangcjsl, test[i]);

                                            bsv.setHetjgpp_Flag(true);
                                            bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));

                                            break;
                                        }
                                    }
                                }
                            }

                        } else if (bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)) {//					加权平均

//									获得结算数量、质量
                            System.out.println("===================4.Locale.jiaqpj_jiesxs===================");
                            getJiesszl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl,
                                    yunsdwb_id, Jieslx, Shangcjsl, "");

                            do {
                                System.out.println("zhib : " + rsl.getString("zhib"));
                                Dbljijzb = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");

                                Dbljijzb = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbljijzb, bsv.getMj_to_kcal_xsclfs());

                                bsv.setJiagpzId(rsl.getString("pinzb_id"));

//                                        Dbljijzb>=rsl.getDouble("xiax")
//                                                &&Dbljijzb<=(rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx"))
//                                                &&Jiesdcz.getHet_condition(bsv.getJiagpzId(), String.valueOf(bsv.getRanlpzb_Id()))
//                                System.out.println("Dbljijzb:" + Dbljijzb);
//                                System.out.println("xiax:" + rsl.getDouble("xiax"));
//                                System.out.println("shangx:" + rsl.getDouble("shangx"));
//                                System.out.println("JiagpzId:" + bsv.getJiagpzId());
//                                System.out.println("Ranlpzb_Id:" + bsv.getRanlpzb_Id());
//                                System.out.println(Jiesdcz.getHet_condition(bsv.getJiagpzId(), String.valueOf(bsv.getRanlpzb_Id())));
                                if (Dbljijzb >= rsl.getDouble("xiax") && Dbljijzb <= (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx"))
                                        && Jiesdcz.getHet_condition(bsv.getJiagpzId(), String.valueOf(bsv.getRanlpzb_Id()))
                                        ) {

//											设置结算的合同值
                                    this.setJieshtinfo(rsl, bsh);

//											计算运费（注意：只要算一次）
//											if(Jieslx==Locale.liangpjs_feiylbb_id){
////												判断条件：是两票结算
//												getYunFei(SelIds,Jieslx,bsv.getHetb_Id(),Shangcjsl);
//											}

                                    bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));        //合同价格表id

                                    bsh.set(rsl.getString("zhib") + Jiesdcz.getZhibbdw(rsl.getString("zhib"), rsl.getString("danw")), Dbljijzb);    //结算值
                                    bsh.set(rsl.getString("zhib") + "计量单位", rsl.getString("danw"));

                                    bsv.setYifzzb(rsl.getString("zhib"));            //默认的已赋值指标

                                    bsh.set(rsl.getString("zhib") + "上限", rsl.getDouble("shangx"));        //指标上限
                                    bsh.set(rsl.getString("zhib") + "下限", rsl.getDouble("xiax"));            //指标下限

                                    bsh.set(rsl.getString("zhib") + "增付单价", 0);
                                    bsh.set(rsl.getString("zhib") + "增付单价公式", "");
                                    bsh.set(rsl.getString("zhib") + "扣付单价", 0);
                                    bsh.set(rsl.getString("zhib") + "扣付单价公式", "");
                                    bsh.set(rsl.getString("zhib") + "增付价单位", "");
                                    bsh.set(rsl.getString("zhib") + "增扣款条件", "");
                                    bsh.set(rsl.getString("zhib") + "增扣款基数", 0);
                                    bsh.set(rsl.getString("zhib") + "增扣款基数单位", "");
                                    bsh.set(rsl.getString("zhib") + "基准增扣价", 0);
                                    bsh.set(rsl.getString("zhib") + "小数处理", "");
                                    bsh.set(rsl.getString("zhib") + "强制计算", "false");

//												获得增扣款
                                    getZengkk(Hetb_id, bsh, true, null);

//												用户自定义公式
                                    bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());

//											煤款含税单价保留小数位
                                    bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//											含税单价取整方式
                                    bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//											增扣款保留小数位
                                    bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//											执行合同价格公式
                                    ExecuteHetmdjgs(bsh);

//											执行公式
//                                    String gongs=bsv.getGongs_Mk();
//                                    System.out.println(gongs);
                                    bsh.eval(bsv.getGongs_Mk());

//												得到计算后的指标
                                    setJieszb(bsh, 0, Shangcjsl);

                                    if (bsv.getTsclzbs() != null) {
//												如果存在需要单独处理的特殊指标
                                        Jiestszbcl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id,
                                                Jieskdl, yunsdwb_id, Jieslx, Shangcjsl);
                                    }

//											根据增扣款中设置的指标基价类型(jijlx=0为含税、jijlx=1为不含税)重新计算煤款含税单价，并对煤款含税单价进行取整处理。
                                    getMeikhsdj(bsh);

//												计算煤款金额
                                    computData(SelIds, Hetb_id, Shangcjsl);

                                    bsv.setHetjgpp_Flag(true);
                                    bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));

                                    break;
                                }

                            } while (rsl.next());
                        } else if (bsv.getJiesxs().equals(Locale.jiaqfl_jiesxs)) {    //加权分列

//								获得结算数量、质量
                            System.out.println("===============4.Locale.jiaqfl_jiesxs==============");
                            getJiesszl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl,
                                    yunsdwb_id, Jieslx, Shangcjsl, "");

                            do {
                                Dbljijzb = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");
                                Dbljijzb = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbljijzb, bsv.getMj_to_kcal_xsclfs());

                                bsv.setJiagpzId(rsl.getString("pinzb_id"));

                                if (Dbljijzb >= rsl.getDouble("xiax") && Dbljijzb <= (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx"))
                                        && Jiesdcz.getHet_condition(bsv.getJiagpzId(), String.valueOf(bsv.getRanlpzb_Id()))
                                        ) {

//										设置结算的合同值
                                    this.setJieshtinfo(rsl, bsh);

                                    bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));        //合同价格表id

//										得到增扣款指标个数
                                    ResultSetList rszkk = MainGlobal.getTableRsl(con,
                                            "select distinct zhibb.bianm as zhibbm\n" +
                                                    "        from hetzkkb,zhibb\n" +
                                                    "        where hetzkkb.zhibb_id = zhibb.id\n" +
                                                    "			   and zhibb.leib=1\n" +
                                                    "              and hetzkkb.hetb_id=" + Hetb_id);

                                    bsv.setJiaqfl_zhibsz(new String[rszkk.getRows()][14]);

                                    for (int i = 0; i < bsv.getJiaqfl_zhibsz().length; i++) {

                                        rszkk.next();
                                        bsv.getJiaqfl_zhibsz()[i][0] = rszkk.getString("zhibbm");
                                    }
                                    rszkk.close();

//										获得增扣款（调用增扣款函数取得全局增扣款标准，放入“加权分列指标数组”）
                                    getZengkk(Hetb_id, bsh, true, null);

//										“加权分列指标数组”初始化完成，下面调用单批次结算算法计算
                                    String[] test = new String[1];

                                    if (SelIds.indexOf(",") == -1) {

                                        test[0] = SelIds;
                                    } else {

                                        test = SelIds.split(",");
                                    }

                                    for (int i = 0; i < test.length; i++) {

//											获得结算数量、质量
                                        getJiesszl(Jieszbsftz, test[i], Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl,
                                                yunsdwb_id, Jieslx, Shangcjsl, "");

                                        Dbljijzb = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");
                                        Dbljijzb = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbljijzb, bsv.getMj_to_kcal_xsclfs());

                                        bsh.set(rsl.getString("zhib") + Jiesdcz.getZhibbdw(rsl.getString("zhib"), rsl.getString("danw")), Dbljijzb);    //结算值
                                        bsh.set(rsl.getString("zhib") + "计量单位", rsl.getString("danw"));

                                        bsv.setYifzzb(rsl.getString("zhib"));            //默认的已赋值指标

                                        bsh.set(rsl.getString("zhib") + "上限", rsl.getDouble("shangx"));        //指标上限
                                        bsh.set(rsl.getString("zhib") + "下限", rsl.getDouble("xiax"));            //指标下限

                                        bsh.set(rsl.getString("zhib") + "增付单价", 0);
                                        bsh.set(rsl.getString("zhib") + "增付单价公式", "");
                                        bsh.set(rsl.getString("zhib") + "扣付单价", 0);
                                        bsh.set(rsl.getString("zhib") + "扣付单价公式", "");
                                        bsh.set(rsl.getString("zhib") + "增付价单位", "");
                                        bsh.set(rsl.getString("zhib") + "增扣款条件", "");
                                        bsh.set(rsl.getString("zhib") + "增扣款基数", 0);
                                        bsh.set(rsl.getString("zhib") + "增扣款基数单位", "");
                                        bsh.set(rsl.getString("zhib") + "基准增扣价", 0);
                                        bsh.set(rsl.getString("zhib") + "小数处理", "");
                                        bsh.set(rsl.getString("zhib") + "强制计算", "false");

                                        getZengkk_Jiaqfl(bsh);    //加权分列增扣款

//											用户自定义公式
                                        bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());

//											煤款含税单价保留小数位
                                        bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//											含税单价取整方式
                                        bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//											增扣款保留小数位
                                        bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//											执行合同价格公式
                                        ExecuteHetmdjgs(bsh);

//											执行公式
                                        bsh.eval(bsv.getGongs_Mk());

//											得到计算后的指标
                                        setJieszb(bsh, 0, Shangcjsl);

                                        if (bsv.getTsclzbs() != null) {
//												如果存在需要单独处理的特殊指标
                                            Jiestszbcl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id,
                                                    Jieskdl, yunsdwb_id, Jieslx, Shangcjsl);
                                        }

//											根据增扣款中设置的指标基价类型(jijlx=0为含税、jijlx=1为不含税)重新计算煤款含税单价，并对煤款含税单价进行取整处理。
                                        getMeikhsdj(bsh);

//												计算煤款金额
                                        computData(SelIds, Hetb_id, Shangcjsl);

                                        bsv.setHetjgpp_Flag(true);
                                        bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));
                                    }
                                    break;
                                }

                            } while (rsl.next());
                        }
                    }
                }
            } else {
//					如果没得到合同价格，要得到结算的数量、质量
                getJiesszl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl,
                        yunsdwb_id, Jieslx, Shangcjsl, "");
            }

            if (!bsv.getHetjgpp_Flag()) {

                bsv.setErroInfo("没有合同价格与结算数据匹配！");
            }

            rsl.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            con.Close();
        }

        return true;
    }

    private void setJieshtinfo(ResultSetList rsl, Interpreter bsh) {
//		函数功能：

//				当结算时，要将合同信息给bsh赋值，以便后面结算用
//		函数逻辑：

//				将rsl的值取出赋值给bsh
//		函数形参：rsl 合同的数据集，bsh 结算自动计算的容器

        bsv.setHetmdj(rsl.getDouble("jij"));            //结算煤单价
        bsv.setJijlx(rsl.getInt("jijlx"));                //基价类型（0、含税；1、不含税）
        bsv.setZuigmj(rsl.getDouble("zuigmj"));            //最高煤价
        bsv.setHetmdjdw(rsl.getString("jijdw"));        //合同煤基价单位
        bsv.setHetmdjgs(Jiesdcz.getTransform_Hetjgs(
                rsl.getString("jijgs").trim(),
                rsl.getString("danw"),
                rsl.getDouble("jij"),
                bsv.getDiancxxb_id()));        //合同单价公式
        bsv.setJiesfs(rsl.getString("jiesfs"));            //结算方式（到厂价格、出矿价格）
        bsv.setJiesxs(rsl.getString("jiesxs"));            //结算形式（单批次、加权平均）
        bsv.setHetyj(rsl.getDouble("yunj"));            //合同运价单价
        bsv.setHetyjdw(rsl.getString("yunjdw"));        //合同运价单位
        bsv.setHetjjfs(rsl.getString("hejfs"));            //合同计价方式（目录价、热值区间(卡)、热值区间(吨)、按卡扣付价）
        bsv.setFengsjj(rsl.getDouble("fengsjj"));        //合同价格中的分公司加价（统一结算用）
//															分公司加价处理逻辑：
//																1、根据合同价格类型（含税、不含税）算出原始结算价（可能是含税也可能是不含税），
//																	用变量保存,并将分公司加价进行保存。
//																2、如果是含税价，结算单价=结算单价+分公司加价；
//																		如果是不含税价，结算单价=最后计算出的含税价+分公司加价
//
        bsv.setShoukdw(rsl.getString("gongfdwmc"));                    //收款单位
        bsv.setKaihyh(rsl.getString("gongfkhyh"));                        //开户银行
        bsv.setZhangH(rsl.getString("gongfzh"));                        //银行帐号
        bsv.setJiagpzId(rsl.getString("pinzb_id"));            //价格里的品种，为了区分一个合同不同品种不同价格的情况
        try {
            bsh.set("结算形式", bsv.getJiesxs());
            bsh.set("计价方式", bsv.getHetjjfs());
            bsh.set("价格单位", bsv.getHetmdjdw());
            bsh.set("合同价格", bsv.getHetmdj());
            bsh.set("最高煤价", bsv.getZuigmj());
            bsh.set("合同价格公式", bsv.getHetmdjgs());

        } catch (EvalError e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void ExecuteHetmdjgs(Interpreter bsh) {
//		执行合同价格公式
//		函数功能：

//			在执行结算公式前，如果合同价格中存在价格公式，要先执行价格公式，将该值赋值给“合同价格”。
//		函数逻辑：

//			执行合同价格的公式。
//		函数形参：bsh 结算自动计算的容器

        if (!bsv.getHetmdjgs().equals("")) {

            try {
                bsh.eval(bsv.getHetmdjgs());
                bsv.setHetmdj(Double.parseDouble(bsh.get("合同价格").toString()));        //合同价格
                bsh.set("合同价格", bsv.getHetmdj());
            } catch (EvalError e) {
                // TODO 自动生成 catch 块
                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    private boolean getZengkk(long Hetb_id, Interpreter bsh, boolean Falg, String[] Tsclzb_item) {
//		增扣款
//		2010-5-8 zsj改：
//			当数量增扣款单位为 “万吨” 时，如果将 结算数量/10000 再进行增扣款操作，由于计算盈亏时只保留2位小数，会产生问题
//				修改方法如下：如果增扣款的指标为结算数量，那么如果增扣款单位为 “万吨” 将上下限乘以 10000 ，并将增扣款单位转为 “吨”

        JDBCcon con = new JDBCcon();
        try {
            ResultSetList rsl = null;

            //国电宣威电厂,增加参数判断是否价格加权，增扣款单批次单独处理。
            String jiesxs_condition = "";
            if (MainGlobal.getXitxx_item("结算", "宣威增扣款单批次单独处理", String.valueOf(visit.getDiancxxb_id()), "否").equals("是") && bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)) {
                jiesxs_condition = " and xs.BIANM <> '" + Locale.danpc_jiesxs + "'";
            }


            String sql = "select distinct zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw,jis,	\n"
                    + " jisdw.bianm as jisdw,kouj,koujgs,kjdw.bianm as koujdw,zengfj,zengfjgs,zfjdw.bianm as zengfjdw,	\n"
                    + " xiaoscl,jizzkj,jizzb,czxm.bianm as canzxm,czxmdw.bianm as canzxmdw,nvl(canzsx,0) as canzsx, \n"
                    + " nvl(canzxx,0) as canzxx,shiyfw,nvl(xs.bianm,'') as jiesxs,pz.mingc as pinz,pz.id as pinzb_id, decode(htzkk.jijlx, 0, '含税单价', '不含税单价') jijlx\n"
                    + " from hetb htb, hetzkkb htzkk,zhibb zbb,tiaojb tjb,danwb dwb,danwb jisdw,danwb kjdw,	\n"
                    + " danwb zfjdw,zhibb czxm,danwb czxmdw,hetjsxsb xs,pinzb pz		\n"
                    + " where htb.id=htzkk.hetb_id and htzkk.zhibb_id=zbb.id and htzkk.tiaojb_id=tjb.id	\n"
                    + " and htzkk.danwb_id=dwb.id(+) and htzkk.jisdwid=jisdw.id(+)	\n"
                    + " and htzkk.koujdw=kjdw.id(+)		\n"
                    + " and htzkk.hetjsxsb_id=xs.id(+)	\n"
                    + " and htzkk.zengfjdw=zfjdw.id(+)	\n"
                    + " and htzkk.canzxm=czxm.id(+)		\n"
                    + " and htzkk.canzxmdw=czxmdw.id(+)	\n"
                    + " and htzkk.pinzb_id=pz.id(+)	\n"
                    + " and tjb.leib=1 and zbb.leib=1	" + jiesxs_condition + "\n"
                    + " and htb.id=" + Hetb_id + " order by zbb.bianm,tjb.bianm,xiax,shangx ";

            rsl = con.getResultSetList(sql);
            double Dbltmp = 0;        //记录指标结算值
            double Dblczxm = 0;        //记录参照项目的值
            String Strtmp = "";        //记录设定的指标
            String Strimplementedzb = "";    //记录已经执行过的指标（即已经参与过执行的指标）。
            double Dblimplementedzbsx = 0;    //记录已执行过的指标的上限
            StringBuffer sb = new StringBuffer();    //记录合同增扣款中的适用范围为1的记录
            String Feitsclzb = "";        //记录增扣款中影响煤款结算单价的非特殊处理指标

            String m_zhib = "";    //指标
            String m_danw = "";    //单位
            String m_tiaoj = "";    //条件
            double m_zengfj = 0;    //增付价
            String m_zengfjgs = "";    //增付价公式
            double m_kouj = 0;    //扣价
            String m_koujgs = "";    //扣价公式
            String m_zengfjdw = "";    //增付价单位
            String m_koujdw = "";        //扣付价单位
            double m_shangx = 0;    //上限
            double m_xiax = 0;    //下限
            double m_jis = 0;        //基数
            String m_jisdw = "";    //基数单位
            double m_jizzkj = 0;    //基准增扣价
            int m_xiaoscl = 0;    //小数处理
            int m_shiyfw = 0;        //适用范围
            String m_jijlx = "";        //基价类型(0为含税单价、1为不含税单价)
            boolean m_qiangzjs = false;    //强制计算标志（某些指标没有达到增扣款的条件，
//										但也要显示在结算单上，此时需要记录het标志，验收数量，盈亏等信息，
//										通过此标志在公式中判断，如果为真说明强制计算，折金额为0）

            if (Falg) {
//				Falg=true 说明是正常增扣款计算，此时如果指标在特殊处理数组中 应不计算

                while (rsl.next()) {

                    Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");
//					得到参照项目的结算标准
                    Dblczxm = Jiesdcz.getZhib_info(bsv, rsl.getString("canzxm"), "js");

//					指标的结算指标
                    Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());
                    Dblczxm = Jiesdcz.getUnit_transform(rsl.getString("canzxm"), rsl.getString("canzxmdw"), Dblczxm, bsv.getMj_to_kcal_xsclfs());

                    //西部奖扣部分单价
                    if (rsl.getString("zhib").equals(Locale.jiessl_zhibb)
                            && rsl.getString("canzxm").equals(Locale.Qnetar_zhibb)) {
                        if (rsl.getDouble("kouj") > 0 && bsv.isShul_xb_kou_static_status()) {
                            Dbltmp = bsv.getShul_xb_kou_static_value();
                        } else if (rsl.getDouble("zengfj") > 0 && bsv.isShul_xb_jiang_static_status()) {
                            Dbltmp = bsv.getShul_xb_dongt_static_value() - 1;
                            if (bsv.isShul_rz_szs_jiang_static_status()) {
                                Dblczxm = bsv.getShul_rz_szs_jiang_static_value();
                            }
                        }
                    }
                    boolean liufqzjs = false;
                    if (MainGlobal.getXitxx_item("结算", "硫份是否进行强制计算", String.valueOf(visit.getDiancxxb_id()), "否").equals("是")) {
                        liufqzjs = (rsl.getString("zhib").equals(Locale.Std_zhibb) || rsl.getString("zhib").equals(Locale.Stad_zhibb) || rsl.getString("zhib").equals(Locale.Star_zhibb));    //增加std/stad/star的强制增扣款计算
                    }
                    if (Dbltmp >= Jiesdcz.getZengkkxx_Value(Strimplementedzb, rsl.getString("zhib"), Dblimplementedzbsx, rsl.getDouble("xiax"))
                            && Dbltmp <= (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx"))
                            && Jiesdcz.getHet_condition(rsl.getString("canzxm"), Dblczxm, rsl.getDouble("canzxx"), rsl.getDouble("canzsx"))
                            && Jiesdcz.getJieszbtscl_Zkk(bsv.getJiesxs(), rsl.getString("jiesxs"), rsl.getInt("shiyfw"), rsl.getString("zhib"))    //如果该指标需要特殊（单批次、超出范围）处理，将不参加统一的增扣款的计算
                            && Jiesdcz.getHet_condition(rsl.getString("pinzb_id"), String.valueOf(bsv.getRanlpzb_Id()))        //品种判断
                            || liufqzjs
                            ) {

//						判断是否强制计算标识。
                        if (!(Dbltmp >= Jiesdcz.getZengkkxx_Value(Strimplementedzb, rsl.getString("zhib"), Dblimplementedzbsx, rsl.getDouble("xiax")) && Dbltmp <= (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx"))
                                && Jiesdcz.getHet_condition(rsl.getString("canzxm"), Dblczxm, rsl.getDouble("canzxx"), rsl.getDouble("canzsx"))
                                && Jiesdcz.getJieszbtscl_Zkk(bsv.getJiesxs(), rsl.getString("jiesxs"), rsl.getInt("shiyfw"), rsl.getString("zhib"))    //如果该指标需要特殊（单批次、超出范围）处理，将不参加统一的增扣款的计算
                                && Jiesdcz.getHet_condition(rsl.getString("pinzb_id"), String.valueOf(bsv.getRanlpzb_Id())))) {
//							不符合正常增扣款条件，属于强制计算

                            if (Strimplementedzb.equals(rsl.getString("zhib")) && !m_qiangzjs) {
//								如果发现上次不是强算，而此次要强算时，应该避免强算情况，直接往下走循环
                                continue;
//								m_qiangzjs = true;
                            } else {

                                m_qiangzjs = true;
                            }

                        } else {

                            m_qiangzjs = false;
                        }

                        if ((bsv.isShul_xb_kou_static_status() || bsv.isShul_xb_jiang_static_status()) && rsl.getString("zhib").equals(Locale.jiessl_zhibb)) {
                            Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");
                            Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());
                        }
                        if (bsv.isShul_rz_szs_jiang_static_status()) {
                            Dblczxm = Jiesdcz.getUnit_transform(rsl.getString("canzxm"), rsl.getString("canzxmdw"), Dblczxm, bsv.getMj_to_kcal_xsclfs());
                        }

                        //指标名称是通过zhibb的编码字段进行配置，指标单位是通过danwb的编码字段进行配置,只有数量和热量可返回单位
                        Strimplementedzb = rsl.getString("zhib");    //记录已使用的指标
                        Dblimplementedzbsx = rsl.getDouble("shangx");

                        if (bsv.getJiesxs().equals(Locale.jiaqfl_jiesxs)) {
//							如果结算形式为“加权分列”那么记录加权指标的增扣款条件，

//							没有被赋值，加权平均还没有被赋完值
                            for (int i = 0; i < bsv.getJiaqfl_zhibsz().length; i++) {

                                if (bsv.getJiaqfl_zhibsz()[i][0].equals(rsl.getString("zhib"))) {

                                    bsv.getJiaqfl_zhibsz()[i][1] = rsl.getString("danw");
                                    bsv.getJiaqfl_zhibsz()[i][2] = rsl.getString("tiaoj");
                                    bsv.getJiaqfl_zhibsz()[i][3] = rsl.getString("zengfj");
                                    bsv.getJiaqfl_zhibsz()[i][4] = rsl.getString("zengfjgs");
                                    bsv.getJiaqfl_zhibsz()[i][5] = rsl.getString("kouj");
                                    bsv.getJiaqfl_zhibsz()[i][6] = rsl.getString("koujgs");
                                    bsv.getJiaqfl_zhibsz()[i][7] = rsl.getString("zengfjdw");
                                    bsv.getJiaqfl_zhibsz()[i][8] = rsl.getString("koujdw");
                                    bsv.getJiaqfl_zhibsz()[i][9] = rsl.getString("shangx");
                                    bsv.getJiaqfl_zhibsz()[i][10] = rsl.getString("xiax");
                                    bsv.getJiaqfl_zhibsz()[i][11] = rsl.getString("jis");
                                    bsv.getJiaqfl_zhibsz()[i][12] = rsl.getString("jisdw");
                                    bsv.getJiaqfl_zhibsz()[i][13] = rsl.getString("jizzkj");
                                    bsv.getJiaqfl_zhibsz()[i][14] = rsl.getString("xiaoscl");
                                    bsv.getJiaqfl_zhibsz()[i][15] = rsl.getString("shiyfw");
                                    bsv.getJiaqfl_zhibsz()[i][16] = String.valueOf(m_qiangzjs);

                                    if (rsl.getString("zhib").equals(Locale.jiessl_zhibb)
                                            && rsl.getString("danw").equals(Locale.wandun_danw)) {
//										为了处理增扣款结算指标为“结算数量”、且单位为 “万吨”时使用
                                        bsv.getJiaqfl_zhibsz()[i][1] = Locale.dun_danw;
                                        bsv.getJiaqfl_zhibsz()[i][7] = String.valueOf(CustomMaths.mul(rsl.getDouble("shangx"), 10000));
                                        bsv.getJiaqfl_zhibsz()[i][8] = String.valueOf(CustomMaths.mul(rsl.getDouble("xiax"), 10000));
                                    }

                                    continue;
                                }
                            }
                            continue;

                        } else if (bsv.getJiesxs().equals(Locale.danpc_jiesxs)
                                && rsl.getString("jiesxs").equals(Locale.jiaqpj_jiesxs)
                                && rsl.getString("zhib").equals(Locale.jiessl_zhibb)) {
//								如果价格的结算形式为“单批次结算”、增扣款中“结算数量”指标的结算形式为“加权平均”
//							此时我们认为“结算数量”指标的增扣款应在所有单批次结算完成后再进行。
                            continue;

                        } else {

//							结算形式不是“加权分列”
                            m_zhib = rsl.getString("zhib");
                            m_danw = rsl.getString("danw");
                            m_tiaoj = rsl.getString("tiaoj");
                            m_zengfj = rsl.getDouble("zengfj");
                            m_zengfjgs = rsl.getString("zengfjgs");
                            m_kouj = rsl.getDouble("kouj");
                            m_koujgs = rsl.getString("koujgs");
                            m_zengfjdw = rsl.getString("zengfjdw");
                            m_koujdw = rsl.getString("koujdw");
                            m_shangx = rsl.getDouble("shangx");
                            m_xiax = rsl.getDouble("xiax");
                            m_jis = rsl.getDouble("jis");
                            m_jisdw = rsl.getString("jisdw");
                            m_jizzkj = rsl.getDouble("jizzkj");
                            m_xiaoscl = rsl.getInt("xiaoscl");
                            m_shiyfw = rsl.getInt("shiyfw");
                            m_jijlx = rsl.getString("jijlx");

                            if (rsl.getString("zhib").equals(Locale.jiessl_zhibb)
                                    && rsl.getString("danw").equals(Locale.wandun_danw)) {
//								为了处理增扣款结算指标为“结算数量”、且单位为 “万吨”时使用
                                m_danw = Locale.dun_danw;
                                m_shangx = CustomMaths.mul(rsl.getDouble("shangx"), 10000);
                                m_xiax = CustomMaths.mul(rsl.getDouble("xiax"), 10000);
                                Dbltmp = CustomMaths.mul(Dbltmp, 10000);
                            }
                        }

//						当硫份的指标低于合同标准时，已确认进行了强制计算的设置，但是单批次结算明细表中仍为保存相应的信息。
                        bsh.set(m_zhib + Jiesdcz.getZhibbdw(m_zhib, m_danw), Dbltmp);    //结算值
                        bsh.set(m_zhib + "计量单位", m_danw);            //指标单位（上下限单位）
                        bsh.set(m_zhib + "增扣款条件", m_tiaoj);            //大于等于、大于、小于、小于等于、	区间、等于
                        bsh.set(m_zhib + "增付单价", m_zengfj);            //增付价
                        bsh.set(m_zhib + "增付单价公式", m_zengfjgs == null ? "" : m_zengfjgs);        //增付价公式
                        bsh.set(m_zhib + "扣付单价", m_kouj);            //扣价
                        bsh.set(m_zhib + "扣付单价公式", m_koujgs == null ? "" : m_koujgs);            //扣价公式
                        bsh.set(m_zhib + "增付价单位", m_zengfjdw == null ? "" : m_zengfjdw);    //增价单位
                        bsh.set(m_zhib + "扣付价单位", m_koujdw == null ? "" : m_koujdw);    //扣价单位
                        bsh.set(m_zhib + "上限", m_shangx);        //指标上限
                        bsh.set(m_zhib + "下限", m_xiax);            //指标下限
                        bsh.set(m_zhib + "增扣款基数", m_jis);            //基数（每升高xx或降低xx）
                        bsh.set(m_zhib + "增扣款基数单位", m_jisdw);    //基数单位
                        bsh.set(m_zhib + "基准增扣价", m_jizzkj);        //基准增扣价（用于多段增扣价累计时使用）
                        bsh.set(m_zhib + "小数处理", Jiesdcz.getTransform_Xiaoscl(m_xiaoscl));        //小数处理（每升高xx或降低xx）
                        bsh.set(m_zhib + "基价类型", m_jijlx);        //基价类型(0为含税、1为不含税)
                        bsh.set(m_zhib + "强制计算", String.valueOf(m_qiangzjs));    //强制计算标志

                        Strtmp += "'" + m_zhib + "',";                    //记录用户设置的影响结算单价的指标
                        Feitsclzb += m_zhib + ",";                        //记录增扣款中影响煤款结算单价的非特殊处理指标
//						处理曾扣款适用范围方法：
//							原理：先将增扣款的适用信息记录到一个StringBuffer变量中，形式为:Qnetar,1;结算数量,1;
//							(注：如果shiyfw为1认为是超出部分适用，才记录，如果是0则是适用于全部数据，不用记录)
//							使用时解析这个StringBufffer
                        if (m_shiyfw > 0) {

//							适用范围为1 说明只对超出部分进行操作
                            sb.append(m_zhib).append(",").append(m_shiyfw).append(";");
                        }
                    }
//					增加ELSE对未执行的指标进行记录
                }
            } else {
//				Falg=false 说明是处理特殊增扣款计算，此时如果指标不在特殊处理数组中 应不计算
                while (rsl.next()) {

                    if (Tsclzb_item[1].equals(rsl.getString("zhib"))) {

//						得到增扣款指标值
                        if (Tsclzb_item[Tsclzb_item.length - 2].equals("'C'")) {
//							表示是对超出部分进行特殊增扣款操作
                            Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");
                        } else {
//							表示对加权平均中的单批次的特殊处理逻辑
                            Dbltmp = Double.parseDouble(Tsclzb_item[Tsclzb_item.length - 2]);
                        }


//						给重新计算增扣款的特殊处理的指标数组打标志
                        Jiesdcz.Mark_Tsclzbs_bz(bsv.getTsclzbs(), Tsclzb_item[0] + "," + rsl.getString("zhib"));

//						得到参照项目的结算标准(只适用于加权平均的参照项目)
                        Dblczxm = Jiesdcz.getZhib_info(bsv, rsl.getString("canzxm"), "js");

//						指标的结算指标
                        Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());
                        Dblczxm = Jiesdcz.getUnit_transform(rsl.getString("canzxm"), rsl.getString("canzxmdw"), Dblczxm, bsv.getMj_to_kcal_xsclfs());

//						增加硫份强制计算标识
                        boolean liufqzjs = false;
                        if (MainGlobal.getXitxx_item("结算", "硫份是否进行强制计算", String.valueOf(visit.getDiancxxb_id()), "否").equals("是")) {
                            liufqzjs = (rsl.getString("zhib").equals(Locale.Std_zhibb) || rsl.getString("zhib").equals(Locale.Stad_zhibb) || rsl.getString("zhib").equals(Locale.Star_zhibb));    //增加std/stad/star的强制增扣款计算
                        }

                        if (Dbltmp >= Jiesdcz.getZengkkxx_Value(Strimplementedzb, rsl.getString("zhib"), Dblimplementedzbsx, rsl.getDouble("xiax")) && Dbltmp <= (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx"))
                                && Jiesdcz.getHet_condition(rsl.getString("canzxm"), Dblczxm, rsl.getDouble("canzxx"), rsl.getDouble("canzsx"))
                                && !Jiesdcz.getJieszbtscl_Zkk(bsv.getJiesxs(), rsl.getString("jiesxs"), rsl.getInt("shiyfw"), rsl.getString("zhib"))    //如果该指标需要特殊（单批次）处理，将不参加统一的增扣款的计算
                                && Jiesdcz.getHet_condition(rsl.getString("pinzb_id"), String.valueOf(bsv.getRanlpzb_Id()))            //品种判断
                                || liufqzjs
                                ) {
//							判断是否使用强制计算
                            if (!(Dbltmp >= Jiesdcz.getZengkkxx_Value(Strimplementedzb, rsl.getString("zhib"), Dblimplementedzbsx, rsl.getDouble("xiax")) && Dbltmp <= (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx"))
                                    && Jiesdcz.getHet_condition(rsl.getString("canzxm"), Dblczxm, rsl.getDouble("canzxx"), rsl.getDouble("canzsx"))
                                    && Jiesdcz.getJieszbtscl_Zkk(bsv.getJiesxs(), rsl.getString("jiesxs"), rsl.getInt("shiyfw"), rsl.getString("zhib"))    //如果该指标需要特殊（单批次、超出范围）处理，将不参加统一的增扣款的计算
                                    && Jiesdcz.getHet_condition(rsl.getString("pinzb_id"), String.valueOf(bsv.getRanlpzb_Id())))) {
                                //							不符合正常增扣款条件，属于强制计算
                                if (Strimplementedzb.equals(rsl.getString("zhib")) && !m_qiangzjs) {
                                    //								如果发现上次不是强算，而此次要强算时，应该避免强算情况，直接往下走循环
                                    continue;
                                } else {

                                    m_qiangzjs = true;
                                }
                            } else {

                                m_qiangzjs = false;
                            }

                            //指标名称是通过zhibb的编码字段进行配置，指标单位是通过danwb的编码字段进行配置,只有数量和热量可返回单位
                            Strimplementedzb = rsl.getString("zhib");    //记录已使用的指标
                            Dblimplementedzbsx = rsl.getDouble("shangx");

                            if (rsl.getString("zhib").equals(Locale.jiessl_zhibb)
                                    && rsl.getString("danw").equals(Locale.wandun_danw)) {
//								为了处理增扣款结算指标为“结算数量”、且单位为 “万吨”时使用
                                m_danw = Locale.dun_danw;
                                m_shangx = CustomMaths.mul(rsl.getDouble("shangx"), 10000);
                                m_xiax = CustomMaths.mul(rsl.getDouble("xiax"), 10000);
                                Dbltmp = CustomMaths.mul(Dbltmp, 10000);
                            } else {

                                m_danw = rsl.getString("danw");
                                m_shangx = rsl.getDouble("shangx");
                                m_xiax = rsl.getDouble("xiax");
                            }

                            bsh.set(rsl.getString("zhib") + Jiesdcz.getZhibbdw(rsl.getString("zhib"), m_danw), Dbltmp);    //结算值
                            bsh.set(rsl.getString("zhib") + "计量单位", m_danw);            //指标单位（上下限单位）
                            bsh.set(rsl.getString("zhib") + "增扣款条件", rsl.getString("tiaoj"));        //大于等于、大于、小于、小于等于、	区间、等于
                            bsh.set(rsl.getString("zhib") + "增付单价", rsl.getDouble("zengfj"));        //增付价
                            bsh.set(rsl.getString("zhib") + "增付单价公式", rsl.getString("zengfjgs") == null ? "" : rsl.getString("zengfjgs"));        //增付价公式
                            bsh.set(rsl.getString("zhib") + "扣付单价", rsl.getDouble("kouj"));            //扣价
                            bsh.set(rsl.getString("zhib") + "扣付单价公式", rsl.getString("koujgs") == null ? "" : rsl.getString("koujgs"));        //扣价公式
                            bsh.set(rsl.getString("zhib") + "增付价单位", rsl.getString("zengfjdw") == null ? "" : rsl.getString("zengfjdw"));    //增价单位
                            bsh.set(rsl.getString("zhib") + "扣付价单位", rsl.getString("koujdw") == null ? "" : rsl.getString("koujdw"));    //扣价单位
                            bsh.set(rsl.getString("zhib") + "上限", m_shangx);        //指标上限
                            bsh.set(rsl.getString("zhib") + "下限", m_xiax);            //指标下限
                            bsh.set(rsl.getString("zhib") + "增扣款基数", rsl.getDouble("jis"));            //基数（每升高xx或降低xx）
                            bsh.set(rsl.getString("zhib") + "增扣款基数单位", rsl.getString("jisdw"));    //基数单位
                            bsh.set(rsl.getString("zhib") + "基准增扣价", rsl.getDouble("jizzkj"));        //基准增扣价（用于多段增扣价累计时使用）
                            bsh.set(rsl.getString("zhib") + "小数处理", Jiesdcz.getTransform_Xiaoscl(rsl.getInt("xiaoscl")));        //小数处理（每升高xx或降低xx）
                            bsh.set(rsl.getString("zhib") + "基价类型", rsl.getString("jijlx"));        //基价类型(0为含税、1为不含税)
                            bsh.set(rsl.getString("zhib") + "强制计算", String.valueOf(m_qiangzjs));        //小数处理（每升高xx或降低xx）

                            Strtmp += "'" + rsl.getString("zhib") + "',";                    //记录用户设置的影响结算单价的指标

//							认为这个指标值应进行增扣款计算
                            bsv.setTsclzbzkksfxyjs(true);

//							处理曾扣款适用范围方法：
//								原理：先将增扣款的适用信息记录到一个StringBuffer变量中，形式为:Qnetar,1;结算数量,1;
//								(注：如果shiyfw为1认为是超出部分适用，才记录，如果是0则是适用于全部数据，不用记录)
//								使用时解析这个StringBufffer

                            if (rsl.getInt("shiyfw") > 0) {

//								适用范围为1 说明只对超出部分进行操作
                                sb.append(rsl.getString("zhib")).append(",").append(rsl.getInt("shiyfw")).append(";");
                            }
                        }
//						增加ELSE对未执行的指标进行记录

                    }
                }
            }

            bsv.setMeikzkksyfw(sb);    //记录煤款增扣款有适用范围为超出部分的数据
            bsv.setFeitsclzb(Feitsclzb); //记录增扣款中影响煤款结算单价的非特殊处理指标

//			if(Strtmp.equals("")){
//
//				if(!bsv.getHetjjfs().equals(Locale.mulj_hetjjfs)){
//
//					bsv.setErroInfo("系统中没有符合要求的增扣款项。");
//					return false;
//				}
//
//			}

            String Strtmpdw = "";

//			取出zhibb中没有在hetzkkb中体现的项目，目的是也要放到公式中去计算
            if (!Falg) {
//				如果是特殊处理的增扣款项目，在价格中赋值的yifzzb(已赋值指标)在此处就不管用了，应以清除
                bsv.setYifzzb("");
            }
            if (!Strtmp.equals("") && bsv.getYifzzb().equals("")) {

                sql = "select distinct bianm as zhib from zhibb where bianm not in (" + Strtmp.substring(0, Strtmp.lastIndexOf(",")) + ") and leib=1 ";
            } else if (!Strtmp.equals("") && !bsv.getYifzzb().equals("")) {

                sql = "select distinct bianm as zhib from zhibb where bianm not in (" + Strtmp + "'" + bsv.getYifzzb() + "') and leib=1 ";
            } else if (!bsv.getYifzzb().equals("")) {

                sql = "select distinct bianm as zhib from zhibb where bianm not in ('" + bsv.getYifzzb() + "') and leib=1 ";
            } else {

                sql = "select distinct bianm as zhib from zhibb where leib=1 ";
            }

            rsl = con.getResultSetList(sql);

            String l_qiangzjs = "false";
            while (rsl.next()) {
//					增加IF判断对记录在案的指标进行强制判断
//					如果满足条件则将强制计算标识设置为True
//				    l_qiangzjs="true"
                Strtmpdw = Jiesdcz.getZhibbdw(rsl.getString("zhib"), "");
                bsh.set(rsl.getString("zhib") + Strtmpdw, 0);                        //结算值
                bsh.set(rsl.getString("zhib") + "计量单位", Strtmpdw);                //指标单位
                bsh.set(rsl.getString("zhib") + "增扣款条件", "区间");                    //大于等于、大于、小于、小于等于、	区间、等于
                bsh.set(rsl.getString("zhib") + "增付单价", 0);                            //增付价
                bsh.set(rsl.getString("zhib") + "增付单价公式", "");                    //增付价
                bsh.set(rsl.getString("zhib") + "扣付单价", 0);
                bsh.set(rsl.getString("zhib") + "扣付单价公式", "");                    //扣价
                bsh.set(rsl.getString("zhib") + "增付价单位", "");                    //增价单位
                bsh.set(rsl.getString("zhib") + "扣付价单位", "");                    //扣价单位
                bsh.set(rsl.getString("zhib") + "上限", 0);                        //指标上限
                bsh.set(rsl.getString("zhib") + "下限", 0);                        //指标下限
                bsh.set(rsl.getString("zhib") + "增扣款基数", 0);                        //基数（每升高xx或降低xx）
                bsh.set(rsl.getString("zhib") + "增扣款基数单位", "");                //基数单位
                bsh.set(rsl.getString("zhib") + "基准增扣价", 0);                        //基准增扣价（用于多段增扣价累计时使用）
                bsh.set(rsl.getString("zhib") + "小数处理", "");                    //小数处理（每升高xx或降低xx）
                bsh.set(rsl.getString("zhib") + "强制计算", l_qiangzjs);                    //强制计算标志
            }
            rsl.close();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
        return true;
    }

    //	增扣款_加权分列
    private boolean getZengkk_Jiaqfl(Interpreter bsh) {
//		将加权分列数组的值，当作增扣款的公式赋值

        JDBCcon con = new JDBCcon();
        try {
            double Dbltmp = 0;        //记录指标结算值
            double Dblczxm = 0;        //记录参照项目的值
            String Strtmp = "";        //记录设定的指标
            StringBuffer sb = new StringBuffer();    //记录合同增扣款中的适用范围为1的记录
            String sql = "";

            for (int i = 0; i < bsv.getJiaqfl_zhibsz().length; i++) {

//				指标的结算指标
                Dbltmp = Jiesdcz.getZhib_info(bsv, bsv.getJiaqfl_zhibsz()[i][0], "js");
                Dbltmp = Jiesdcz.getUnit_transform(bsv.getJiaqfl_zhibsz()[i][0], bsv.getJiaqfl_zhibsz()[i][1], Dbltmp, bsv.getMj_to_kcal_xsclfs());

                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + Jiesdcz.getZhibbdw(bsv.getJiaqfl_zhibsz()[i][0], bsv.getJiaqfl_zhibsz()[i][1]), Dbltmp);    //结算值
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "计量单位", bsv.getJiaqfl_zhibsz()[i][1]);            //指标单位（上下限单位）
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "增扣款条件", bsv.getJiaqfl_zhibsz()[i][2]);            //大于等于、大于、小于、小于等于、	区间、等于
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "增付单价", Double.parseDouble(bsv.getJiaqfl_zhibsz()[i][3]));            //增付价
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "增付单价公式", bsv.getJiaqfl_zhibsz()[i][4] == null ? "" : bsv.getJiaqfl_zhibsz()[i][4]);            //增付价公式
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "扣付单价", Double.parseDouble(bsv.getJiaqfl_zhibsz()[i][5]));            //扣价
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "扣付单价公式", bsv.getJiaqfl_zhibsz()[i][6] == null ? "" : bsv.getJiaqfl_zhibsz()[i][6]);            //扣价公式
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "增付价单位", bsv.getJiaqfl_zhibsz()[i][7] == null ? "" : bsv.getJiaqfl_zhibsz()[i][7]);    //增价单位
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "扣付价单位", bsv.getJiaqfl_zhibsz()[i][8] == null ? "" : bsv.getJiaqfl_zhibsz()[i][8]);    //扣价单位
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "上限", Double.parseDouble(bsv.getJiaqfl_zhibsz()[i][9]));            //指标上限
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "下限", Double.parseDouble(bsv.getJiaqfl_zhibsz()[i][10]));            //指标下限
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "增扣款基数", Double.parseDouble(bsv.getJiaqfl_zhibsz()[i][11]));            //基数（每升高xx或降低xx）
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "增扣款基数单位", bsv.getJiaqfl_zhibsz()[i][12]);            //基数单位
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "基准增扣价", Double.parseDouble(bsv.getJiaqfl_zhibsz()[i][13]));        //基准增扣价（用于多段增扣价累计时使用）
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "小数处理", Jiesdcz.getTransform_Xiaoscl(Integer.parseInt(bsv.getJiaqfl_zhibsz()[i][14])));        //小数处理（每升高xx或降低xx）
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "强制计算", bsv.getJiaqfl_zhibsz()[i][16]);        //小数处理（每升高xx或降低xx）
                Strtmp += "'" + bsv.getJiaqfl_zhibsz()[i][0] + "',";                    //记录用户设置的影响结算单价的指标
            }

            if (!Strtmp.equals("") && bsv.getYifzzb().equals("")) {

                sql = "select distinct bianm as zhib from zhibb where bianm not in (" + Strtmp.substring(0, Strtmp.lastIndexOf(",")) + ") and leib=1 ";
            } else if (!Strtmp.equals("") && !bsv.getYifzzb().equals("")) {

                sql = "select distinct bianm as zhib from zhibb where bianm not in (" + Strtmp + "'" + bsv.getYifzzb() + "') and leib=1 ";
            } else if (!bsv.getYifzzb().equals("")) {

                sql = "select distinct bianm as zhib from zhibb where bianm not in ('" + bsv.getYifzzb() + "') and leib=1 ";
            } else {

                sql = "select distinct bianm as zhib from zhibb where leib=1 ";
            }

            ResultSetList rsl = con.getResultSetList(sql);

            String Strtmpdw = "";

            while (rsl.next()) {


                Strtmpdw = Jiesdcz.getZhibbdw(rsl.getString("zhib"), "");

                bsh.set(rsl.getString("zhib") + Strtmpdw, 0);                        //结算值
                bsh.set(rsl.getString("zhib") + "计量单位", Strtmpdw);                //指标单位
                bsh.set(rsl.getString("zhib") + "增扣款条件", "区间");                    //大于等于、大于、小于、小于等于、	区间、等于
                bsh.set(rsl.getString("zhib") + "增付单价", 0);                            //增付价
                bsh.set(rsl.getString("zhib") + "增付单价公式", "");                    //增付价公式
                bsh.set(rsl.getString("zhib") + "扣付单价", 0);                            //扣价
                bsh.set(rsl.getString("zhib") + "扣付单价公式", "");                    //扣价公式
                bsh.set(rsl.getString("zhib") + "增付价单位", "");                    //增价单位
                bsh.set(rsl.getString("zhib") + "扣付价单位", "");                    //扣价单位
                bsh.set(rsl.getString("zhib") + "上限", 0);                        //指标上限
                bsh.set(rsl.getString("zhib") + "下限", 0);                        //指标下限
                bsh.set(rsl.getString("zhib") + "增扣款基数", 0);                        //基数（每升高xx或降低xx）
                bsh.set(rsl.getString("zhib") + "增扣款基数单位", "");                //基数单位
                bsh.set(rsl.getString("zhib") + "基准增扣价", 0);                        //基准增扣价（用于多段增扣价累计时使用）
                bsh.set(rsl.getString("zhib") + "小数处理", "");                    //小数处理（每升高xx或降低xx）
                bsh.set(rsl.getString("zhib") + "强制计算", "false");                    //强制计算
            }
            rsl.close();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
        return true;
    }

    private long getYunshtb_id(long Hetb_id) {
//		得到hetys.id
        JDBCcon con = new JDBCcon();
        long lngYunshtb_id = 0;
        long lngFinYshtb_id = 0;

//		情况1：如果是两票结算、先从煤款合同表中取运费、
//		如果没有取到运费，再从煤款运费合同关联表中取出运费合同id,
//		再根据运费合同中的煤矿取出结算价格

        try {
//			在getMeiprise方法中，已经记录下合同所用的hetjgb_id
            ResultSet rs = null;
            String sql = "select yunj,danwb.bianm as yunjdw from hetjgb,danwb 	\n"
                    + " where hetjgb.yunjdw_id=danwb.id and hetjgb.id=" + bsv.getHetjgb_id();
            ResultSet rec = con.getResultSet(sql);
            while (rec.next()) {

                bsv.setHetyj(rec.getDouble("yunj"));
                bsv.setHetyjdw(rec.getString("yunjdw"));
            }

            if (bsv.getHetyj() == 0) {
//				如果没有取到运费，再从煤款运费合同关联表中取出运费合同id

                sql = "select hetys_id from meikyfhtglb where hetb_id=" + Hetb_id;
                rec = con.getResultSet(sql);
                while (rec.next()) {
//					有多个运费合同
                    lngYunshtb_id = rec.getLong("hetys_id");
                    sql = "select hetys.id from hetysjgb,hetys 	\n"
                            + " where hetys.id=hetysjgb.hetys_id	\n"
                            + " and hetys.id=" + lngYunshtb_id + " 		\n"
                            + " and (meikxxb_id=" + bsv.getMeikxxb_Id()
                            + " or meikxxb_id = 0)";

                    rs = con.getResultSet(sql);
                    while (rs.next()) {

                        lngFinYshtb_id = lngYunshtb_id;
                        return lngFinYshtb_id;
                    }
                }
            }
            if (rs != null) {

                rs.close();
            }
            if (rec != null) {

                rec.close();
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
        return 0;
    }

    private void countYf_jiuq(String Selids, long Hetb_id, double Shangcjsl) {

//		计算运费
//		思路：
//			情况1：煤款合同中有运价（bsv.getHetyj()>0），运价单位有两种（元/吨，元/吨*公里）,
//					没有曾扣款情况.因为没法得到运费合同，所以不能算增扣款
//			情况2：是Hetyj、Hetyjdw无值,要从hetysjgb中取值，有增扣款
        try {
            String Yunfdpc[][] = null;    //二维数组，保存运费单批次结算的分组值
            Interpreter bsh = new Interpreter();
//			运费含税单价保留小数位
            if (bsv.getHetyj() > 0 && !bsv.getHetyjdw().equals("")) {
//				情况1
//				取hetb中的运价

                if (!bsv.getYunfjsdpcfztj().equals("")) {
//					运费结算分组条件不为空
//					按条件单批次结算运费
                    Yunfdpc = Jiesdcz.getYunfjsdpcsz(bsv.getYunfjsdpcfztj(), bsv.getSelIds(), bsv.getGuohxt());
                    if (Yunfdpc != null && Yunfdpc.length > 0) {

                        for (int i = 0; i < Yunfdpc.length; i++) {
//							逻辑：如果需要运费按某一分组条件单批次结算，应重新为运费结算关键字赋值
//								1、对于煤款合同中含运费的情况从价格的角度，只需重新获得“运距”指标即可。
                            bsh.set("合同运价", bsv.getHetyj());
                            bsh.set("合同运价单位", bsv.getHetyjdw());
//							bsh.set("运价里程", bsv.getYunju_js());

                            bsv.setYunju_js(Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl", "nvl(gl.zhi,0) as zhi"
                                    , "m.id=gl.meikxxb_id and m.id=" + Yunfdpc[i][0] + " and \n"
                                            + " gl.diancxxb_id=" + bsv.getDiancxxb_id()
                                            + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'")));

                            bsh.set("运价里程", bsv.getYunju_js());

                            if (!bsv.getGongs_Yf().equals("")) {

//								按单批次分组条件重算结算指标
                                this.getJiesszl(bsv.getJieszbsftz(), bsv.getSelIds(), bsv.getDiancxxb_id(), bsv.getGongysb_Id(), Hetb_id
                                        , bsv.getJieskdl(), bsv.getYunsdwb_id(), bsv.getJieslx(), Shangcjsl,
                                        Jiesdcz.getSql_Where_assemble(Yunfdpc[i][1], Yunfdpc[i][2]));

//								运费含税单价保留小数位
                                bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                getZengkk_yf(Hetb_id, bsh);    //不起任何作用只是公式上不报错
//								算运费
                                bsh.eval(bsv.getGongs_Yf());

                                setJieszb(bsh, 1, Shangcjsl);
//								煤矿单位
                                bsv.setMeikxxb_Id(Long.parseLong(Yunfdpc[i][0]));
//								运距
//								2010-05-12 zsj改：
//								阳城需求，一个电厂有两个汽车衡，各矿到两个汽车衡的运距不同，当煤款结算时运距按最小值进行结算
                                bsv.setYunju_js(Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl", "nvl(gl.zhi,0) as zhi"
                                        , "m.id=gl.meikxxb_id and m.id=" + Yunfdpc[i][0] + " and \n"
                                                + " gl.diancxxb_id=" + bsv.getDiancxxb_id()
                                                + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'")));

                                bsv.setYunju_cf(bsv.getYunju_js());

                                computData_Yf("0");

                            } else {

                                bsv.setErroInfo("请设置运费计算公式");
                                return;
                            }
                        }
                    }
                } else {

                    bsh.set("合同运价", bsv.getHetyj());
                    bsh.set("合同运价单位", bsv.getHetyjdw());
                    bsh.set("运价里程", bsv.getYunju_js());

                    if (!bsv.getGongs_Yf().equals("")) {

//						运费含税单价保留小数位
                        bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                        getZengkk_yf(Hetb_id, bsh);    //不起任何作用只是公式上部报错
//						算运费
                        bsh.eval(bsv.getGongs_Yf());

                        setJieszb(bsh, 1, Shangcjsl);

                        computData_Yf("0");

                    } else {

                        bsv.setErroInfo("请设置运费计算公式");
                        return;
                    }
                }

            } else {

//				取hetys中的运价
                JDBCcon con = new JDBCcon();
                String sql = "";
                ResultSetList rsl = null;

                if (!bsv.getYunfjsdpcfztj().equals("")) {
//					运费结算分组条件不为空
//					按条件单批次结算运费
                    Yunfdpc = Jiesdcz.getYunfjsdpcsz(bsv.getYunfjsdpcfztj(), bsv.getSelIds(), bsv.getGuohxt());

                    if (Yunfdpc != null && Yunfdpc.length > 0) {

                        String m_meikxxb_id = "";

                        for (int i = 0; i < Yunfdpc.length; i++) {
//							逻辑：如果需要运费按某一分组条件单批次结算，应重新为运费结算关键字赋值
//							1、对于使用单独运费合同来处理的运费结算从价格的角度，要为煤矿、运距、所有指标重新赋值。

                            if (Yunfdpc[i][0].indexOf(",") > -1) {
//								如果得到的分组条件值为多个，第一个一定为 meikxxb_id 的值
                                String m_tmp[] = null;
                                m_tmp = Yunfdpc[i][0].split(",");
                                m_meikxxb_id = m_tmp[0];
                            } else {

                                m_meikxxb_id = Yunfdpc[i][0];
                            }

//							得到运输合同价格
                            sql = " select distinct zb.bianm as zhib,tj.bianm as tiaoj,jg.shangx,jg.xiax,dw.bianm as danw,	\n"
                                    + " yunja,yjdw.bianm as yunjdw,nvl(yunsjgfab_id,0) as yunsjgfab_id \n"
                                    + " from hetys ht,hetysjgb jg,zhibb zb,tiaojb tj,danwb dw,	\n"
                                    + " danwb yjdw										\n"
                                    + " where ht.id=jg.hetys_id and jg.zhibb_id=zb.id(+)	\n"
                                    + " and jg.tiaojb_id=tj.id(+)						\n"
                                    + " and jg.danwb_id=dw.id(+)						\n"
                                    + " and jg.yunjdw_id=yjdw.id(+)						\n"
                                    + " and ht.id=" + Hetb_id + " and (jg.meikxxb_id="
                                    + m_meikxxb_id + " or jg.meikxxb_id = 0)		\n";

                            rsl = con.getResultSetList(sql);

                            if (rsl.next()) {
//								单批次结算
//								检查是否有结算价格方案，如果有说明是特殊方式结算，不走原来的合同增扣款形式
                                if (rsl.getLong("yunsjgfab_id") > 0) {
//									处理运费结算方案的特殊算法
                                    bsh.set("合同运价", 0);
                                    bsh.set("合同运价单位", Locale.yuanmd_danw);
                                    bsh.set("运价里程", 0);

//									解析运费价格明细中的数据
                                    bsh = Jiesdcz.CountYfjsfa(bsv, Long.parseLong(Yunfdpc[i][0].indexOf(",") > 0 ? Yunfdpc[i][0].substring(0, Yunfdpc[i][0].indexOf(",")) : Yunfdpc[i][0]), bsv.getDiancxxb_id(), bsv.getFaz_Id(),
                                            bsv.getDaoz_Id(), rsl.getLong("yunsjgfab_id"), bsh);

//									给合同运价赋值
                                    bsv.setHetyj(Double.parseDouble(bsh.get("合同运价").toString()));
                                    bsv.setHetyjdw(Locale.yuanmd_danw);

                                    if (Double.parseDouble(bsh.get("合同运价").toString()) == 0) {

                                        bsv.setErroInfo("没有和运输价格方案匹配的数据！");
                                        return;
                                    }

//									按单批次分组条件重算结算指标
                                    getJiesszl(bsv.getJieszbsftz(), bsv.getSelIds(), bsv.getDiancxxb_id(), bsv.getGongysb_Id(), Hetb_id
                                            , bsv.getJieskdl(), bsv.getYunsdwb_id(), bsv.getJieslx(), Shangcjsl,
                                            Jiesdcz.getSql_Where_assemble(Yunfdpc[i][1], Yunfdpc[i][2]));

//									获得增扣款
                                    getZengkk_yf(Hetb_id, bsh);

//									运费含税单价保留小数位
                                    bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                    bsh.eval(bsv.getGongs_Yf());
                                    setJieszb(bsh, 1, Shangcjsl);

//									为煤矿信息赋值
                                    bsv.setMeikxxb_Id(Long.parseLong(Yunfdpc[i][0].indexOf(",") > 0 ? Yunfdpc[i][0].substring(0, Yunfdpc[i][0].indexOf(",")) : Yunfdpc[i][0]));

                                    computData_Yf("0");

                                } else {
//									单批次结算
//									没有结算价格方案，按原来的价格条款和增扣款条款进行结算
                                    String where_condition = "";
                                    if (rsl.getRows() == 1) {
//										就一条合同价格
                                        bsh.set("合同运价", rsl.getDouble("yunja"));
                                        bsh.set("合同运价单位", rsl.getString("yunjdw"));
                                        bsh.set("运价里程", bsv.getYunju_js());

                                        bsv.setHetyj(rsl.getDouble("yunja"));
                                        bsv.setHetyjdw(rsl.getString("yunjdw"));

//										增加对阳城电厂A系统、B系统的处理
                                        if (Yunfdpc[i][0].indexOf(",") > -1) {
//											如果得到的分组条件值为多个，第一个一定为 meikxxb_id 的值
                                            String m_tmp[] = null;
                                            m_tmp = Yunfdpc[i][0].split(",");

                                            where_condition = " and i.bianm=" + m_tmp[1] + " \n";
                                        }

                                        bsv.setYunju_js(Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl,item i", "nvl(gl.zhi,0) as zhi"
                                                , "m.id=gl.meikxxb_id and gl.shuxmc = i.id and m.id=" + m_meikxxb_id + " and \n"
                                                        + " gl.diancxxb_id=" + bsv.getDiancxxb_id() + " \n"
                                                        + where_condition
                                                        + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'")));

                                        bsh.set("运价里程", bsv.getYunju_js());

                                        if (rsl.getDouble("yunja") == 0) {

                                            bsv.setErroInfo("合同运价为0，请检查合同！");
                                            return;
                                        }

//										按单批次分组条件重算结算指标
                                        getJiesszl(bsv.getJieszbsftz(), bsv.getSelIds(), bsv.getDiancxxb_id(), bsv.getGongysb_Id(), Hetb_id
                                                , bsv.getJieskdl(), bsv.getYunsdwb_id(), bsv.getJieslx(), Shangcjsl,
                                                Jiesdcz.getSql_Where_assemble(Yunfdpc[i][1], Yunfdpc[i][2]));

                                        double jiessl = 0;
                                        if (bsv.getJieslx() == Locale.guotyf_feiylbb_id || bsv.getJieslx() == Locale.daozdt_feiylbb_leib) {

                                            jiessl = bsv.getJiessl();
                                        } else {

                                            jiessl = bsv.getYunfjsl();
                                        }
                                        if (jiessl > 0) {
//											判断该单批次运费结算是否有数量，要是没有就不做计算了

//											获得增扣款
                                            getZengkk_yf(Hetb_id, bsh);

//											运费含税单价保留小数位
                                            bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                            bsh.eval(bsv.getGongs_Yf());
                                            setJieszb(bsh, 1, Shangcjsl);
//											煤矿信息表id
                                            bsv.setMeikxxb_Id(Long.parseLong(m_meikxxb_id));
//											运距
                                            bsv.setYunju_js(Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl,item i", "nvl(min(gl.zhi),0) as zhi"
                                                    , "m.id=gl.meikxxb_id and gl.shuxmc = i.id and m.id=" + m_meikxxb_id + " and \n"
                                                            + " gl.diancxxb_id=" + bsv.getDiancxxb_id() + " \n"
                                                            + where_condition
                                                            + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'")));

                                            bsv.setYunju_cf(bsv.getYunju_js());

                                            computData_Yf("0");
                                        }

                                    } else {
//										单批次结算
//										多个合同价格
                                        double shangx = 0;
                                        double xiax = 0;
                                        double yunju = bsv.getYunju_cf();    //运距
                                        double Dbltmp = 0;


//										按单批次分组条件重算结算指标
                                        getJiesszl(bsv.getJieszbsftz(), bsv.getSelIds(), bsv.getDiancxxb_id(), bsv.getGongysb_Id(), Hetb_id
                                                , bsv.getJieskdl(), bsv.getYunsdwb_id(), bsv.getJieslx(), Shangcjsl,
                                                Jiesdcz.getSql_Where_assemble(Yunfdpc[i][1], Yunfdpc[i][2]));

                                        double jiessl = 0;
                                        if (bsv.getJieslx() == Locale.guotyf_feiylbb_id || bsv.getJieslx() == Locale.daozdt_feiylbb_leib) {

                                            jiessl = bsv.getJiessl();
                                        } else {

                                            jiessl = bsv.getYunfjsl();
                                        }

                                        if (jiessl > 0) {
//											判断该单批次运费结算是否有数量，要是没有就不做计算了
                                            do {
                                                shangx = rsl.getDouble("shangx");
                                                xiax = rsl.getDouble("xiax");

                                                Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");

                                                Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());

//												由于运费价格目前只和运距有关，故在确定运费价格时只用运距指标
//												单批次运费结算要重新获得yunju指标

                                                if (Yunfdpc[i][0].indexOf(",") > -1) {
//													如果得到的分组条件值为多个，第一个一定为 meikxxb_id 的值
                                                    String m_tmp[] = null;
                                                    m_tmp = Yunfdpc[i][0].split(",");

                                                    where_condition = " and i.bianm=" + m_tmp[1] + " \n";
                                                }

                                                yunju = Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl,item i", "nvl(gl.zhi,0) as zhi"
                                                        , "m.id=gl.meikxxb_id and gl.shuxmc = i.id and m.id=" + m_meikxxb_id + " and \n"
                                                                + " gl.diancxxb_id=" + bsv.getDiancxxb_id() + " \n"
                                                                + where_condition
                                                                + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'"));

                                                if (yunju >= xiax && yunju <= (shangx == 0 ? 1e308 : shangx)) {

                                                    bsh.set("合同运价", rsl.getDouble("yunja"));
                                                    bsh.set("合同运价单位", rsl.getString("yunjdw"));
                                                    bsh.set("运价里程", bsv.getYunju_js());

                                                    bsv.setHetyj(rsl.getDouble("yunja"));
                                                    bsv.setHetyjdw(rsl.getString("yunjdw"));
                                                    bsv.setYunju_js(yunju);

                                                    bsh.set("运价里程", yunju);

                                                    if (rsl.getDouble("yunja") == 0) {

                                                        bsv.setErroInfo("合同运价为0，请检查合同！");
                                                        return;
                                                    }

                                                    getZengkk_yf(Hetb_id, bsh);

//													运费含税单价保留小数位
                                                    bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                                    bsh.eval(bsv.getGongs_Yf());
                                                    setJieszb(bsh, 1, Shangcjsl);
//													煤矿信息表
                                                    bsv.setMeikxxb_Id(Long.parseLong(m_meikxxb_id));
//													运距
                                                    bsv.setYunju_js(Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl,item i", "nvl(gl.zhi,0) as zhi"
                                                            , "m.id=gl.meikxxb_id and gl.shuxmc = i.id and m.id=" + m_meikxxb_id + " and \n"
                                                                    + " gl.diancxxb_id=" + bsv.getDiancxxb_id() + "	\n"
                                                                    + where_condition
                                                                    + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'")));

                                                    bsv.setYunju_cf(bsv.getYunju_js());

                                                    computData_Yf("0");
                                                    break;
                                                }

                                            } while (rsl.next());
                                        }
                                    }
                                }
                            } else {

                                bsv.setErroInfo("没有得到运费合同价格！");
                                return;
                            }
                        }
                    }

                } else {

                    sql = " select distinct zb.bianm as zhib,tj.bianm as tiaoj,jg.shangx,jg.xiax,dw.bianm as danw,	\n"
                            + " yunja,yjdw.bianm as yunjdw,nvl(yunsjgfab_id,0) as yunsjgfab_id \n"
                            + " from hetys ht,hetysjgb jg,zhibb zb,tiaojb tj,danwb dw,	\n"
                            + " danwb yjdw										\n"
                            + " where ht.id=jg.hetys_id and jg.zhibb_id=zb.id(+)	\n"
                            + " and jg.tiaojb_id=tj.id(+)						\n"
                            + " and jg.danwb_id=dw.id(+)						\n"
                            + " and jg.yunjdw_id=yjdw.id(+)						\n"
                            + " and ht.id=" + Hetb_id + " and (jg.meikxxb_id="
                            + bsv.getMeikxxb_Id() + " or jg.meikxxb_id = 0)		\n";

                    rsl = con.getResultSetList(sql);

                    if (rsl.next()) {
//						检查是否有结算价格方案，如果有说明是特殊方式结算，不走原来的合同增扣款形式
                        if (rsl.getLong("yunsjgfab_id") > 0) {
//							处理运费结算方案的特殊算法、此算法运距是含在运价中的，且运价是多段，故不再重新赋值
                            bsh.set("合同运价", 0);
                            bsh.set("合同运价单位", Locale.yuanmd_danw);
                            bsh.set("运价里程", 0);

//							解析运费价格明细中的数据
                            bsh = Jiesdcz.CountYfjsfa(bsv, bsv.getMeikxxb_Id(), bsv.getDiancxxb_id(), bsv.getFaz_Id(),
                                    bsv.getDaoz_Id(), rsl.getLong("yunsjgfab_id"), bsh);

                            bsv.setHetyj(Double.parseDouble(bsh.get("合同运价").toString()));
                            bsv.setHetyjdw(Locale.yuanmd_danw);

                            if (Double.parseDouble(bsh.get("合同运价").toString()) == 0) {

                                bsv.setErroInfo("没有和运输价格方案匹配的数据！");
                                return;
                            }

//							获得增扣款
                            getZengkk_yf(Hetb_id, bsh);

//							运费含税单价保留小数位
                            bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                            bsh.eval(bsv.getGongs_Yf());
                            setJieszb(bsh, 1, Shangcjsl);

                        } else {
//							没有结算价格方案，按原来的价格条款和增扣款条款进行结算
                            if (rsl.getRows() == 1) {
//								就一条合同价格
//								yuss
                                String yunxkdxs = "";//允许亏吨系数
                                double hetyj = 0;//合同运价
                                double yingk = 0;//发货盈亏
                                double yunxkssl = 0;//允许亏损数量
                                double kaohkssl = 0;//考核亏损数量
                                double biaoz = 0;//票重
                                double hetmj = 0;//合同煤价
                                double shijyfdj = 0;//实际运费单价
                                hetyj = rsl.getDouble("yunja");
                                //得到矿发数量、实收数量
                                String sql_fah = "select sum(biaoz) biaoz,sum(yingk) yingk from fahb f where f.lie_id in(" + Selids + ")";
                                ResultSetList rsl_fah = con.getResultSetList(sql_fah);
                                if (rsl_fah.next()) {//
                                    yingk = rsl_fah.getDouble("yingk");
                                    biaoz = rsl_fah.getDouble("biaoz");
                                }
                                //得到允许亏吨系数
                                String sql_yunxkdxs =

                                        "select a.zhi\n" +
                                                "   from (select zhi, jiesszbmb_id\n" +
                                                "           from jiesszb\n" +
                                                "             where jiesszfab_id in\n" +
                                                "                (select jiesszfab_id\n" +
                                                "                   from jiesszfahtglb gl\n" +
                                                "                      where hetb_id = (select hetb_id\n" +
                                                "                           from meikyfhtglb\n" +
                                                "                          where hetys_id = " + Hetb_id + ")\n" +
                                                "                 )) a,\n" +
                                                " (select id from jiesszbmb where bianm = '允许亏吨系数') b\n" +
                                                " where a.jiesszbmb_id = b.id\n" +
                                                "";

                                ResultSetList rsl_yunxkdxs = con.getResultSetList(sql_yunxkdxs);
                                if (rsl_yunxkdxs.next()) {//说明结算设置方案中设置了“允许亏吨系数”，即肯定是重点煤
                                    yunxkdxs = rsl_yunxkdxs.getString("zhi");
                                    yunxkssl = CustomMaths.Round_New(biaoz * Double.parseDouble(yunxkdxs), 4);
                                    if (-yingk <= yunxkssl) {//即运损在允许亏损之内，不用进行考核

                                        bsh.set("合同运价", hetyj);
                                        bsh.set("合同运价单位", rsl.getString("yunjdw"));
                                        bsh.set("运价里程", bsv.getYunju_js());

                                        bsv.setHetyj(rsl.getDouble("yunja"));
                                        bsv.setHetyjdw(rsl.getString("yunjdw"));
                                    } else {//即运损超过了允许亏损的范围，要进行考核
                                        kaohkssl = (-yingk) - yunxkssl;
                                        String sql_hetmj = "select jij from hetjgb where hetb_id=(select hetb_id from meikyfhtglb where hetys_id=" + Hetb_id + ")";
                                        ResultSetList rsl_hetmj = con.getResultSetList(sql_hetmj);
                                        if (rsl_hetmj.next()) {
                                            hetmj = rsl_hetmj.getDouble("jij");
                                        }
                                        //实际运费单价
                                        shijyfdj = CustomMaths.Round_new((biaoz * hetyj - kaohkssl * hetmj - kaohkssl * hetyj) / biaoz, 2);

                                        bsh.set("合同运价", shijyfdj);
                                        bsh.set("合同运价单位", rsl.getString("yunjdw"));
                                        bsh.set("运价里程", bsv.getYunju_js());

                                        bsv.setHetyj(shijyfdj);
                                        bsv.setHetyjdw(rsl.getString("yunjdw"));
                                    }

                                    rsl_fah.close();
                                    rsl_yunxkdxs.close();
                                }
                                //yuss
                                /*

								bsh.set("合同运价", rsl.getDouble("yunja"));
								bsh.set("合同运价单位", rsl.getString("yunjdw"));
								bsh.set("运价里程", bsv.getYunju_js());
//								给合同运价赋值
								bsv.setHetyj(rsl.getDouble("yunja"));
								bsv.setHetyjdw(rsl.getString("yunjdw"));*/

                                if (rsl.getDouble("yunja") == 0) {

                                    bsv.setErroInfo("合同运价为0，请检查合同！");
                                    return;
                                }
//								获得增扣款
                                getZengkk_yf(Hetb_id, bsh);

//								运费含税单价保留小数位
                                bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                bsh.eval(bsv.getGongs_Yf());
                                setJieszb(bsh, 1, Shangcjsl);
                                computData_Yf("0");

                            } else {
//								多个合同价格
                                double shangx = 0;
                                double xiax = 0;
                                double yunju = bsv.getYunju_cf();    //运距
                                double Dbltmp = 0;

                                do {
                                    shangx = rsl.getDouble("shangx");
                                    xiax = rsl.getDouble("xiax");

                                    Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");

                                    Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());

                                    if (yunju >= xiax && yunju <= (shangx == 0 ? 1e308 : shangx)) {

                                        bsh.set("合同运价", rsl.getDouble("yunja"));
                                        bsh.set("合同运价单位", rsl.getString("yunjdw"));
                                        bsh.set("运价里程", bsv.getYunju_js());

//										给合同运价赋值
                                        bsv.setHetyj(rsl.getDouble("yunja"));
                                        bsv.setHetyjdw(rsl.getString("yunjdw"));
                                        bsv.setYunju_js(yunju);

                                        if (rsl.getDouble("yunja") == 0) {

                                            bsv.setErroInfo("合同运价为0，请检查合同！");
                                            return;
                                        }

                                        getZengkk_yf(Hetb_id, bsh);

//										运费含税单价保留小数位
                                        bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                        bsh.eval(bsv.getGongs_Yf());
                                        setJieszb(bsh, 1, Shangcjsl);
                                    }

                                } while (rsl.next());
                            }
                        }
                    } else {

                        bsv.setErroInfo("没有得到运费合同价格！");
                        return;
                    }
                }

                rsl.close();
                con.Close();
            }
//			针对山西阳城电厂有两个过衡系统(A系统、B系统)的业务，计算过衡系统的卸车费，并显示在结算单里的备注中。
            if (bsv.getJieslx() == Locale.guotyf_feiylbb_id || bsv.getJieslx() == Locale.liangpjs_feiylbb_id) {
                String zhi = MainGlobal.getXitxx_item("数量", "是否显示过衡系统下拉框", String.valueOf(visit.getDiancxxb_id()), "否");
                if (zhi.equals("是")) {
                    zhi = MainGlobal.getXitxx_item("结算", "过衡系统卸车费", String.valueOf(visit.getDiancxxb_id()), "");
                    if (visit.getString19().equals(zhi)) {
                        String xiecf = MainGlobal.getXitxx_item("结算", "每车卸车费", String.valueOf(visit.getDiancxxb_id()), "6");
                        bsv.setBeiz(bsv.getBeiz() + "卸车费:" + Double.parseDouble(xiecf) * bsv.getChes());
                        visit.setDouble17(Double.parseDouble(xiecf) * bsv.getChes());
                    }
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void CountYf(long Hetb_id, double Shangcjsl) {
//		计算运费
//		思路：
//			情况1：煤款合同中有运价（bsv.getHetyj()>0），运价单位有两种（元/吨，元/吨*公里）,
//					没有曾扣款情况.因为没法得到运费合同，所以不能算增扣款
//			情况2：是Hetyj、Hetyjdw无值,要从hetysjgb中取值，有增扣款
        try {
            String Yunfdpc[][] = null;    //二维数组，保存运费单批次结算的分组值
            Interpreter bsh = new Interpreter();
//			运费含税单价保留小数位
            if (bsv.getHetyj() > 0 && !bsv.getHetyjdw().equals("")) {
//				情况1
//				取hetb中的运价

                if (!bsv.getYunfjsdpcfztj().equals("")) {
//					运费结算分组条件不为空
//					按条件单批次结算运费
                    Yunfdpc = Jiesdcz.getYunfjsdpcsz(bsv.getYunfjsdpcfztj(), bsv.getSelIds(), bsv.getGuohxt());
                    if (Yunfdpc != null && Yunfdpc.length > 0) {

                        for (int i = 0; i < Yunfdpc.length; i++) {
//							逻辑：如果需要运费按某一分组条件单批次结算，应重新为运费结算关键字赋值
//								1、对于煤款合同中含运费的情况从价格的角度，只需重新获得“运距”指标即可。
                            bsh.set("合同运价", bsv.getHetyj());
                            bsh.set("合同运价单位", bsv.getHetyjdw());
//							bsh.set("运价里程", bsv.getYunju_js());

                            bsv.setYunju_js(Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl", "nvl(gl.zhi,0) as zhi"
                                    , "m.id=gl.meikxxb_id and m.id=" + Yunfdpc[i][0] + " and \n"
                                            + " gl.diancxxb_id=" + bsv.getDiancxxb_id()
                                            + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'")));

                            bsh.set("运价里程", bsv.getYunju_js());

                            if (!bsv.getGongs_Yf().equals("")) {

//								按单批次分组条件重算结算指标
                                this.getJiesszl(bsv.getJieszbsftz(), bsv.getSelIds(), bsv.getDiancxxb_id(), bsv.getGongysb_Id(), Hetb_id
                                        , bsv.getJieskdl(), bsv.getYunsdwb_id(), bsv.getJieslx(), Shangcjsl,
                                        Jiesdcz.getSql_Where_assemble(Yunfdpc[i][1], Yunfdpc[i][2]));

//								运费含税单价保留小数位
                                bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                getZengkk_yf(Hetb_id, bsh);    //不起任何作用只是公式上不报错
//								算运费
                                bsh.eval(bsv.getGongs_Yf());

                                setJieszb(bsh, 1, Shangcjsl);
//								煤矿单位
                                bsv.setMeikxxb_Id(Long.parseLong(Yunfdpc[i][0]));
//								运距
//								2010-05-12 zsj改：
//								阳城需求，一个电厂有两个汽车衡，各矿到两个汽车衡的运距不同，当煤款结算时运距按最小值进行结算
                                bsv.setYunju_js(Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl", "nvl(gl.zhi,0) as zhi"
                                        , "m.id=gl.meikxxb_id and m.id=" + Yunfdpc[i][0] + " and \n"
                                                + " gl.diancxxb_id=" + bsv.getDiancxxb_id()
                                                + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'")));

                                bsv.setYunju_cf(bsv.getYunju_js());

                                computData_Yf("0");

                            } else {

                                bsv.setErroInfo("请设置运费计算公式");
                                return;
                            }
                        }
                    }
                } else {

                    bsh.set("合同运价", bsv.getHetyj());
                    bsh.set("合同运价单位", bsv.getHetyjdw());
                    bsh.set("运价里程", bsv.getYunju_js());

                    if (!bsv.getGongs_Yf().equals("")) {

//						运费含税单价保留小数位
                        bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                        getZengkk_yf(Hetb_id, bsh);    //不起任何作用只是公式上部报错
//						算运费
                        bsh.eval(bsv.getGongs_Yf());

                        setJieszb(bsh, 1, Shangcjsl);

                        computData_Yf("0");

                    } else {

                        bsv.setErroInfo("请设置运费计算公式");
                        return;
                    }
                }

            } else {

//				取hetys中的运价
                JDBCcon con = new JDBCcon();
                String sql = "";
                ResultSetList rsl = null;

                if (!bsv.getYunfjsdpcfztj().equals("")) {
//					运费结算分组条件不为空
//					按条件单批次结算运费
                    Yunfdpc = Jiesdcz.getYunfjsdpcsz(bsv.getYunfjsdpcfztj(), bsv.getSelIds(), bsv.getGuohxt());

                    if (Yunfdpc != null && Yunfdpc.length > 0) {

                        String m_meikxxb_id = "";

                        for (int i = 0; i < Yunfdpc.length; i++) {
//							逻辑：如果需要运费按某一分组条件单批次结算，应重新为运费结算关键字赋值
//							1、对于使用单独运费合同来处理的运费结算从价格的角度，要为煤矿、运距、所有指标重新赋值。

                            if (Yunfdpc[i][0].indexOf(",") > -1) {
//								如果得到的分组条件值为多个，第一个一定为 meikxxb_id 的值
                                String m_tmp[] = null;
                                m_tmp = Yunfdpc[i][0].split(",");
                                m_meikxxb_id = m_tmp[0];
                            } else {

                                m_meikxxb_id = Yunfdpc[i][0];
                            }

//							得到运输合同价格
                            sql = " select distinct zb.bianm as zhib,tj.bianm as tiaoj,jg.shangx,jg.xiax,dw.bianm as danw,	\n"
                                    + " yunja,yjdw.bianm as yunjdw,nvl(yunsjgfab_id,0) as yunsjgfab_id \n"
                                    + " from hetys ht,hetysjgb jg,zhibb zb,tiaojb tj,danwb dw,	\n"
                                    + " danwb yjdw										\n"
                                    + " where ht.id=jg.hetys_id and jg.zhibb_id=zb.id(+)	\n"
                                    + " and jg.tiaojb_id=tj.id(+)						\n"
                                    + " and jg.danwb_id=dw.id(+)						\n"
                                    + " and jg.yunjdw_id=yjdw.id(+)						\n"
                                    + " and ht.id=" + Hetb_id + " and (jg.meikxxb_id="
                                    + m_meikxxb_id + " or jg.meikxxb_id = 0)		\n";

                            rsl = con.getResultSetList(sql);

                            if (rsl.next()) {
//								单批次结算
//								检查是否有结算价格方案，如果有说明是特殊方式结算，不走原来的合同增扣款形式
                                if (rsl.getLong("yunsjgfab_id") > 0) {
//									处理运费结算方案的特殊算法
                                    bsh.set("合同运价", 0);
                                    bsh.set("合同运价单位", Locale.yuanmd_danw);
                                    bsh.set("运价里程", 0);

//									解析运费价格明细中的数据
                                    bsh = Jiesdcz.CountYfjsfa(bsv, Long.parseLong(Yunfdpc[i][0].indexOf(",") > 0 ? Yunfdpc[i][0].substring(0, Yunfdpc[i][0].indexOf(",")) : Yunfdpc[i][0]), bsv.getDiancxxb_id(), bsv.getFaz_Id(),
                                            bsv.getDaoz_Id(), rsl.getLong("yunsjgfab_id"), bsh);

//									给合同运价赋值
                                    bsv.setHetyj(Double.parseDouble(bsh.get("合同运价").toString()));
                                    bsv.setHetyjdw(Locale.yuanmd_danw);

                                    if (Double.parseDouble(bsh.get("合同运价").toString()) == 0) {

                                        bsv.setErroInfo("没有和运输价格方案匹配的数据！");
                                        return;
                                    }

//									按单批次分组条件重算结算指标
                                    getJiesszl(bsv.getJieszbsftz(), bsv.getSelIds(), bsv.getDiancxxb_id(), bsv.getGongysb_Id(), Hetb_id
                                            , bsv.getJieskdl(), bsv.getYunsdwb_id(), bsv.getJieslx(), Shangcjsl,
                                            Jiesdcz.getSql_Where_assemble(Yunfdpc[i][1], Yunfdpc[i][2]));

//									获得增扣款
                                    getZengkk_yf(Hetb_id, bsh);

//									运费含税单价保留小数位
                                    bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                    bsh.eval(bsv.getGongs_Yf());
                                    setJieszb(bsh, 1, Shangcjsl);

//									为煤矿信息赋值
                                    bsv.setMeikxxb_Id(Long.parseLong(Yunfdpc[i][0].indexOf(",") > 0 ? Yunfdpc[i][0].substring(0, Yunfdpc[i][0].indexOf(",")) : Yunfdpc[i][0]));

                                    computData_Yf("0");

                                } else {
//									单批次结算
//									没有结算价格方案，按原来的价格条款和增扣款条款进行结算
                                    String where_condition = "";
                                    if (rsl.getRows() == 1) {
//										就一条合同价格
                                        bsh.set("合同运价", rsl.getDouble("yunja"));
                                        bsh.set("合同运价单位", rsl.getString("yunjdw"));
                                        bsh.set("运价里程", bsv.getYunju_js());

                                        bsv.setHetyj(rsl.getDouble("yunja"));
                                        bsv.setHetyjdw(rsl.getString("yunjdw"));

//										增加对阳城电厂A系统、B系统的处理
                                        if (Yunfdpc[i][0].indexOf(",") > -1) {
//											如果得到的分组条件值为多个，第一个一定为 meikxxb_id 的值
                                            String m_tmp[] = null;
                                            m_tmp = Yunfdpc[i][0].split(",");

                                            where_condition = " and i.bianm=" + m_tmp[1] + " \n";
                                        }

                                        bsv.setYunju_js(Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl,item i", "nvl(max(gl.zhi),0) as zhi"
                                                , "m.id=gl.meikxxb_id and gl.shuxmc = i.id and m.id=" + m_meikxxb_id + " and \n"
                                                        + " gl.diancxxb_id=" + bsv.getDiancxxb_id() + " \n"
                                                        + where_condition
                                                        + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'")));

                                        bsh.set("运价里程", bsv.getYunju_js());

                                        if (rsl.getDouble("yunja") == 0) {

                                            bsv.setErroInfo("合同运价为0，请检查合同！");
                                            return;
                                        }

//										按单批次分组条件重算结算指标
                                        getJiesszl(bsv.getJieszbsftz(), bsv.getSelIds(), bsv.getDiancxxb_id(), bsv.getGongysb_Id(), Hetb_id
                                                , bsv.getJieskdl(), bsv.getYunsdwb_id(), bsv.getJieslx(), Shangcjsl,
                                                Jiesdcz.getSql_Where_assemble(Yunfdpc[i][1], Yunfdpc[i][2]));

                                        double jiessl = 0;
                                        if (bsv.getJieslx() == Locale.guotyf_feiylbb_id || bsv.getJieslx() == Locale.daozdt_feiylbb_leib) {

                                            jiessl = bsv.getJiessl();
                                        } else {

                                            jiessl = bsv.getYunfjsl();
                                        }
                                        if (jiessl > 0) {
//											判断该单批次运费结算是否有数量，要是没有就不做计算了

//											获得增扣款
                                            getZengkk_yf(Hetb_id, bsh);

//											运费含税单价保留小数位
                                            bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                            bsh.eval(bsv.getGongs_Yf());
                                            setJieszb(bsh, 1, Shangcjsl);
//											煤矿信息表id
                                            bsv.setMeikxxb_Id(Long.parseLong(m_meikxxb_id));
//											运距
                                            bsv.setYunju_js(Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl,item i", "nvl(min(gl.zhi),0) as zhi"
                                                    , "m.id=gl.meikxxb_id and gl.shuxmc = i.id and m.id=" + m_meikxxb_id + " and \n"
                                                            + " gl.diancxxb_id=" + bsv.getDiancxxb_id() + " \n"
                                                            + where_condition
                                                            + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'")));

                                            bsv.setYunju_cf(bsv.getYunju_js());

                                            computData_Yf("0");
                                        }

                                    } else {
//										单批次结算
//										多个合同价格
                                        double shangx = 0;
                                        double xiax = 0;
                                        double yunju = bsv.getYunju_cf();    //运距
                                        double Dbltmp = 0;


//										按单批次分组条件重算结算指标
                                        getJiesszl(bsv.getJieszbsftz(), bsv.getSelIds(), bsv.getDiancxxb_id(), bsv.getGongysb_Id(), Hetb_id
                                                , bsv.getJieskdl(), bsv.getYunsdwb_id(), bsv.getJieslx(), Shangcjsl,
                                                Jiesdcz.getSql_Where_assemble(Yunfdpc[i][1], Yunfdpc[i][2]));

                                        double jiessl = 0;
                                        if (bsv.getJieslx() == Locale.guotyf_feiylbb_id || bsv.getJieslx() == Locale.daozdt_feiylbb_leib) {

                                            jiessl = bsv.getJiessl();
                                        } else {

                                            jiessl = bsv.getYunfjsl();
                                        }

                                        if (jiessl > 0) {
//											判断该单批次运费结算是否有数量，要是没有就不做计算了
                                            do {
                                                shangx = rsl.getDouble("shangx");
                                                xiax = rsl.getDouble("xiax");

                                                Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");

                                                Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());

//												由于运费价格目前只和运距有关，故在确定运费价格时只用运距指标
//												单批次运费结算要重新获得yunju指标

                                                if (Yunfdpc[i][0].indexOf(",") > -1) {
//													如果得到的分组条件值为多个，第一个一定为 meikxxb_id 的值
                                                    String m_tmp[] = null;
                                                    m_tmp = Yunfdpc[i][0].split(",");

                                                    where_condition = " and i.bianm=" + m_tmp[1] + " \n";
                                                }

                                                yunju = Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl,item i", "nvl(gl.zhi,0) as zhi"
                                                        , "m.id=gl.meikxxb_id and gl.shuxmc = i.id and m.id=" + m_meikxxb_id + " and \n"
                                                                + " gl.diancxxb_id=" + bsv.getDiancxxb_id() + " \n"
                                                                + where_condition
                                                                + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'"));

                                                if (yunju >= xiax && yunju <= (shangx == 0 ? 1e308 : shangx)) {

                                                    bsh.set("合同运价", rsl.getDouble("yunja"));
                                                    bsh.set("合同运价单位", rsl.getString("yunjdw"));
                                                    bsh.set("运价里程", bsv.getYunju_js());

                                                    bsv.setHetyj(rsl.getDouble("yunja"));
                                                    bsv.setHetyjdw(rsl.getString("yunjdw"));
                                                    bsv.setYunju_js(yunju);

                                                    bsh.set("运价里程", yunju);

                                                    if (rsl.getDouble("yunja") == 0) {

                                                        bsv.setErroInfo("合同运价为0，请检查合同！");
                                                        return;
                                                    }

                                                    getZengkk_yf(Hetb_id, bsh);

//													运费含税单价保留小数位
                                                    bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                                    bsh.eval(bsv.getGongs_Yf());
                                                    setJieszb(bsh, 1, Shangcjsl);
//													煤矿信息表
                                                    bsv.setMeikxxb_Id(Long.parseLong(m_meikxxb_id));
//													运距
                                                    bsv.setYunju_js(Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl,item i", "nvl(gl.zhi,0) as zhi"
                                                            , "m.id=gl.meikxxb_id and gl.shuxmc = i.id and m.id=" + m_meikxxb_id + " and \n"
                                                                    + " gl.diancxxb_id=" + bsv.getDiancxxb_id() + "	\n"
                                                                    + where_condition
                                                                    + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'")));

                                                    bsv.setYunju_cf(bsv.getYunju_js());

                                                    computData_Yf("0");
                                                    break;
                                                }

                                            } while (rsl.next());
                                        }
                                    }
                                }
                            } else {

                                bsv.setErroInfo("没有得到运费合同价格！");
                                return;
                            }
                        }
                    }

                } else {

                    sql = " select distinct zb.bianm as zhib,tj.bianm as tiaoj,jg.shangx,jg.xiax,dw.bianm as danw,	\n"
                            + " yunja,yjdw.bianm as yunjdw,nvl(yunsjgfab_id,0) as yunsjgfab_id \n"
                            + " from hetys ht,hetysjgb jg,zhibb zb,tiaojb tj,danwb dw,	\n"
                            + " danwb yjdw										\n"
                            + " where ht.id=jg.hetys_id and jg.zhibb_id=zb.id(+)	\n"
                            + " and jg.tiaojb_id=tj.id(+)						\n"
                            + " and jg.danwb_id=dw.id(+)						\n"
                            + " and jg.yunjdw_id=yjdw.id(+)						\n"
                            + " and ht.id=" + Hetb_id + " and (jg.meikxxb_id="
                            + bsv.getMeikxxb_Id() + " or jg.meikxxb_id = 0)		\n";

                    rsl = con.getResultSetList(sql);

                    if (rsl.next()) {
//						检查是否有结算价格方案，如果有说明是特殊方式结算，不走原来的合同增扣款形式
                        if (rsl.getLong("yunsjgfab_id") > 0) {
//							处理运费结算方案的特殊算法、此算法运距是含在运价中的，且运价是多段，故不再重新赋值
                            bsh.set("合同运价", 0);
                            bsh.set("合同运价单位", Locale.yuanmd_danw);
                            bsh.set("运价里程", 0);

//							解析运费价格明细中的数据
                            bsh = Jiesdcz.CountYfjsfa(bsv, bsv.getMeikxxb_Id(), bsv.getDiancxxb_id(), bsv.getFaz_Id(),
                                    bsv.getDaoz_Id(), rsl.getLong("yunsjgfab_id"), bsh);

                            bsv.setHetyj(Double.parseDouble(bsh.get("合同运价").toString()));
                            bsv.setHetyjdw(Locale.yuanmd_danw);

                            if (Double.parseDouble(bsh.get("合同运价").toString()) == 0) {

                                bsv.setErroInfo("没有和运输价格方案匹配的数据！");
                                return;
                            }

//							获得增扣款
                            getZengkk_yf(Hetb_id, bsh);

//							运费含税单价保留小数位
                            bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                            bsh.eval(bsv.getGongs_Yf());
                            setJieszb(bsh, 1, Shangcjsl);

                        } else {
//							没有结算价格方案，按原来的价格条款和增扣款条款进行结算
                            if (rsl.getRows() == 1) {
//								就一条合同价格
                                bsh.set("合同运价", rsl.getDouble("yunja"));
                                bsh.set("合同运价单位", rsl.getString("yunjdw"));
                                bsh.set("运价里程", bsv.getYunju_js());
//								给合同运价赋值
                                bsv.setHetyj(rsl.getDouble("yunja"));
                                bsv.setHetyjdw(rsl.getString("yunjdw"));

                                if (rsl.getDouble("yunja") == 0) {

                                    bsv.setErroInfo("合同运价为0，请检查合同！");
                                    return;
                                }
//								获得增扣款
                                getZengkk_yf(Hetb_id, bsh);

//								运费含税单价保留小数位
                                bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                bsh.eval(bsv.getGongs_Yf());
                                setJieszb(bsh, 1, Shangcjsl);
                                computData_Yf("0");

                            } else {
//								多个合同价格
                                double shangx = 0;
                                double xiax = 0;
                                double yunju = bsv.getYunju_cf();    //运距
                                double Dbltmp = 0;

                                do {
                                    shangx = rsl.getDouble("shangx");
                                    xiax = rsl.getDouble("xiax");

                                    Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");

                                    Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());

                                    if (yunju >= xiax && yunju <= (shangx == 0 ? 1e308 : shangx)) {

                                        bsh.set("合同运价", rsl.getDouble("yunja"));
                                        bsh.set("合同运价单位", rsl.getString("yunjdw"));
                                        bsh.set("运价里程", bsv.getYunju_js());

//										给合同运价赋值
                                        bsv.setHetyj(rsl.getDouble("yunja"));
                                        bsv.setHetyjdw(rsl.getString("yunjdw"));
                                        bsv.setYunju_js(yunju);

                                        if (rsl.getDouble("yunja") == 0) {

                                            bsv.setErroInfo("合同运价为0，请检查合同！");
                                            return;
                                        }

                                        getZengkk_yf(Hetb_id, bsh);

//										运费含税单价保留小数位
                                        bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                        bsh.eval(bsv.getGongs_Yf());
                                        setJieszb(bsh, 1, Shangcjsl);
                                    }

                                } while (rsl.next());
                            }
                        }
                    } else {

                        bsv.setErroInfo("没有得到运费合同价格！");
                        return;
                    }
                }

                rsl.close();
                con.Close();
            }
//			针对山西阳城电厂有两个过衡系统(A系统、B系统)的业务，计算过衡系统的卸车费，并显示在结算单里的备注中。
            if (bsv.getJieslx() == Locale.guotyf_feiylbb_id || bsv.getJieslx() == Locale.liangpjs_feiylbb_id) {
                String zhi = MainGlobal.getXitxx_item("数量", "是否显示过衡系统下拉框", String.valueOf(visit.getDiancxxb_id()), "否");
                if (zhi.equals("是")) {
                    zhi = MainGlobal.getXitxx_item("结算", "过衡系统卸车费", String.valueOf(visit.getDiancxxb_id()), "");
                    if (visit.getString19().equals(zhi)) {
                        String xiecf = MainGlobal.getXitxx_item("结算", "每车卸车费", String.valueOf(visit.getDiancxxb_id()), "6");
                        bsv.setBeiz(bsv.getBeiz() + "卸车费:" + Double.parseDouble(xiecf) * bsv.getChes());
                        visit.setDouble17(Double.parseDouble(xiecf) * bsv.getChes());
                    }
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void getZengkk_yf(long Hetb_id, Interpreter bsh) {

//		运费的增扣款原则：如果运费价格里用到了该指标，还可以在增扣款中累计计算
        JDBCcon con = new JDBCcon();
        try {

            String sql = " select distinct zb.bianm as zhib,tj.bianm as tiaoj,shangx,xiax,dw.bianm as danw,			\n"
                    + " 	jis,jsdw.bianm as jisdw,kouj,kjdw.bianm as koujdw,zengfj,zfjdw.bianm as zengfjdw,			\n"
                    + " 	xiaoscl,jizzkj,jizzb,czxm.bianm as canzxm,czxmdw.bianm as canzxmdw,nvl(canzsx,0) as canzsx,	\n"
                    + "		nvl(canzxx,0) as canzxx			\n"
                    + " 	from hetys ht,hetyszkkb zkk,zhibb zb,tiaojb tj,danwb dw,danwb jsdw,danwb kjdw,				\n"
                    + " 		danwb zfjdw,zhibb czxm,danwb czxmdw														\n"
                    + " 		where ht.id=zkk.hetys_id and zkk.zhibb_id=zb.id and zkk.tiaojb_id=tj.id					\n"
                    + "  			and zkk.danwb_id=dw.id and zkk.jisdwid=jsdw.id(+) and zkk.koujdw=kjdw.id(+)			\n"
                    + "  			and zkk.zengfjdw=zfjdw.id(+) and zkk.canzxm=czxm.id(+) and zkk.canzxmdw=czxmdw.id(+)	\n"
                    + " 			and ht.id=" + Hetb_id + " order by zb.bianm,tj.bianm,xiax,shangx ";
            ResultSetList rsl = con.getResultSetList(sql);
            double Dbltmp = 0;        //记录指标结算值
            String Strtmp = "";        //记录设定的指标
            double Dblczxm = 0;        //记录参照项目的值
            String Strimplementedzb = "";    //记录已经执行过的指标（即已经参与过执行的指标）。
            double Dblimplementedzbsx = 0;    //记录已执行过的指标的上限
            String m_danw = "";
            double m_shangx = 0;
            double m_xiax = 0;

            while (rsl.next()) {

                Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");
                Dblczxm = Jiesdcz.getZhib_info(bsv, rsl.getString("canzxm"), "js");
//				指标的结算指标
                Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());
                Dblczxm = Jiesdcz.getUnit_transform(rsl.getString("canzxm"), rsl.getString("canzxmdw"), Dblczxm, bsv.getMj_to_kcal_xsclfs());

                if (Dbltmp >= Jiesdcz.getZengkkxx_Value(Strimplementedzb, rsl.getString("zhib"), Dblimplementedzbsx, rsl.getDouble("xiax")) && Dbltmp <= (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx"))
                        && Jiesdcz.getHet_condition(rsl.getString("canzxm"), Dblczxm, rsl.getDouble("canzxx"), rsl.getDouble("canzsx"))

                        ) {

                    if (bsv.getShifyrzcjs().equals("是")) {

                        Dbltmp = Dbltmp - bsv.getKuikjs();

                    } else if (MainGlobal.getXitxx_item("结算", "是否以热值差结算", String.valueOf(bsv.getDiancxxb_id()), "否").equals("是")) {

                        Dbltmp = Dbltmp - Double.parseDouble(MainGlobal.getXitxx_item("结算", "亏卡基数", String.valueOf(bsv.getDiancxxb_id()), "0"));

                    }

                    Strimplementedzb = rsl.getString("zhib");
                    Dblimplementedzbsx = rsl.getDouble("shangx");

                    if (rsl.getString("zhib").equals(Locale.jiessl_zhibb)
                            && rsl.getString("danw").equals(Locale.wandun_danw)) {
//						为了处理增扣款结算指标为“结算数量”、且单位为 “万吨”时使用
                        m_danw = Locale.dun_danw;
                        m_shangx = CustomMaths.mul(rsl.getDouble("shangx"), 10000);
                        m_xiax = CustomMaths.mul(rsl.getDouble("xiax"), 10000);
                        Dbltmp = CustomMaths.mul(Dbltmp, 10000);
                    } else {

                        m_danw = rsl.getString("danw");
                        m_shangx = rsl.getDouble("shangx");
                        m_xiax = rsl.getDouble("xiax");
                    }

                    bsh.set(rsl.getString("zhib") + Jiesdcz.getZhibbdw(rsl.getString("zhib"), m_danw), Dbltmp);    //结算值
                    bsh.set(rsl.getString("zhib") + "计量单位", m_danw);            //指标单位
                    bsh.set(rsl.getString("zhib") + "增扣款条件", rsl.getString("tiaoj"));        //大于等于、大于、小于、小于等于、	区间、等于
                    bsh.set(rsl.getString("zhib") + "增付单价", rsl.getDouble("zengfj"));        //增付价
                    bsh.set(rsl.getString("zhib") + "扣付单价", rsl.getDouble("kouj"));            //扣价
                    bsh.set(rsl.getString("zhib") + "增付价单位", rsl.getString("zengfjdw") == null ? "" : rsl.getString("zengfjdw"));    //增价单位
                    bsh.set(rsl.getString("zhib") + "扣付价单位", rsl.getString("koujdw") == null ? "" : rsl.getString("koujdw"));    //扣价单位
                    bsh.set(rsl.getString("zhib") + "上限", m_shangx);        //指标上限
                    bsh.set(rsl.getString("zhib") + "下限", m_xiax);            //指标下限
                    bsh.set(rsl.getString("zhib") + "增扣款基数", rsl.getDouble("jis"));            //基数（每升高xx或降低xx）
                    bsh.set(rsl.getString("zhib") + "基准增扣价", rsl.getDouble("jizzkj"));        //基准增扣价（用于多段增扣价累计时使用）
                    bsh.set(rsl.getString("zhib") + "小数处理", Jiesdcz.getTransform_Xiaoscl(rsl.getInt("xiaoscl")));        //小数处理（每升高xx或降低xx）

                    Strtmp += "'" + rsl.getString("zhib") + "',";                    //记录用户设置的影响运费结算单价的指标
                }
            }

            if (!Strtmp.equals("")) {

                sql = "select distinct bianm as zhib from zhibb where bianm not in (" + Strtmp.substring(0, Strtmp.lastIndexOf(",")) + ") and leib=1 ";

            } else {

                sql = "select distinct bianm as zhib from zhibb where leib=1 ";
            }

            rsl = con.getResultSetList(sql);

            String Strtmpdw = "";

            while (rsl.next()) {


                Strtmpdw = Jiesdcz.getZhibbdw(rsl.getString("zhib"), "");

                bsh.set(rsl.getString("zhib") + Strtmpdw, 0);                        //结算值
                bsh.set(rsl.getString("zhib") + "计量单位", Strtmpdw);                //指标单位
                bsh.set(rsl.getString("zhib") + "增扣款条件", "区间");                    //大于等于、大于、小于、小于等于、	区间、等于
                bsh.set(rsl.getString("zhib") + "增付单价", 0);                        //增付价
                bsh.set(rsl.getString("zhib") + "扣付单价", 0);                        //扣价
                bsh.set(rsl.getString("zhib") + "增付价单位", "");                    //增价单位
                bsh.set(rsl.getString("zhib") + "扣付价单位", "");                    //扣价单位
                bsh.set(rsl.getString("zhib") + "上限", 0);                        //指标上限
                bsh.set(rsl.getString("zhib") + "下限", 0);                        //指标下限
                bsh.set(rsl.getString("zhib") + "增扣款基数", 0);                        //基数（每升高xx或降低xx）
                bsh.set(rsl.getString("zhib") + "基准增扣价", 0);                        //基准增扣价（用于多段增扣价累计时使用）
                bsh.set(rsl.getString("zhib") + "小数处理", "");                    //小数处理（每升高xx或降低xx）

            }

            rsl.close();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
    }

    //计算运费，矿区杂费，计税扣除，地铁运费
    private boolean getYunFei(String SelIds, long Jieslx, long Hetb_id, double Shangcjsl) {
        JDBCcon con = new JDBCcon();
        try {

            String sql = "";
            ResultSet rs = null;
            String sql_colum = "";    //附加列（矿区运费用）
            String sql_talbe = "";    //附加表（矿区运费用）
            String sql_where = "";    //为了处理矿区运费为空时候用的
            String strJieslx = "";    //由于庄河电厂有“海运运费”这种结算类型，所以两票结算时，运费可能是“国铁运费”或是“海运运费”，
            //或者两者都有，那么在此将两票结算中的运费转换成“国铁运费”和“海运运费”，
            //目前两票结算时的运费先暂时这么处理。
            long lngJieslx = 0;
            long lngYunshtb_id = 0;
            lngJieslx = Jieslx;
            strJieslx = String.valueOf(Jieslx);
            String yunjslx = MainGlobal.getXitxx_item("结算", "运费税是否是增值税", String.valueOf(visit.getDiancxxb_id()), "是");

//				针对山西阳城电厂的需求，区分A系统和B系统的来煤，并分开进行运费结算
            String zhi = "否";
            String guohxt = "";
            if (Jieslx == Locale.guotyf_feiylbb_id || Jieslx == Locale.dityf_feiylbb_id) {
                zhi = MainGlobal.getXitxx_item("数量", "是否显示过衡系统下拉框", String.valueOf(visit.getDiancxxb_id()), "否");
                if (zhi.equals("是")) {
                    guohxt = "and c.zhongchh = '" + visit.getString19() + "'\n";
                }
            }


            if (lngJieslx == Locale.liangpjs_feiylbb_id || lngJieslx == Locale.guotyf_feiylbb_id || lngJieslx == Locale.haiyyf_feiylbb_id) {
            //region Description两票结算、国铁运费
//
                /*
                *huochaoyuan
	    		*2009-10-22增加运输合同结算运费时，结算单上自动取到运输合同中输入的开户银行税号等信息
	    		*/
                if (lngJieslx == Locale.guotyf_feiylbb_id || lngJieslx == Locale.haiyyf_feiylbb_id) {
                    JDBCcon con1 = new JDBCcon();
                    String sqll = "select h.gongfdwmc,h.gongfzh,h.gongfkhyh from hetys h where \n"
                            + "h.id=" + Hetb_id;

                    ResultSetList rsll = con1.getResultSetList(sqll);
                    if (rsll.next()) {

                        bsv.setShoukdw(rsll.getString("gongfdwmc"));
                        bsv.setKaihyh(rsll.getString("gongfkhyh"));
                        bsv.setZhangH(rsll.getString("gongfzh"));
                    }
                    con1.Close();
                }
//		    		end
                sql_colum = ",decode(kuangqyf,null,0,kuangqyf) as kuangqyf,	\n"
                        + "decode(kuangqzf,null,0,kuangqzf) as kuangqzf,	\n"
                        + "decode(kuangqyfs,null,0,kuangqyfs) as kuangqyfsk,	\n"
                        + "decode(kuangqyfj,null,0,kuangqzf) as kuangqyfjk	\n";

                sql_talbe = " , (select max(0) as id,sum(zhi) as kuangqyf,\n";
//		        	税率调整
                String CONDITON = "";
                if (yunjslx.equals("是")) {
                    CONDITON = "sum(zhi)-round_new(sum(zhi)/(1+shuil),2) as kuangqyfs, \n"
                            + " round_new(sum(zhi)/(1+shuil),2) as kuangqyfj,";
                } else {
                    CONDITON = "round_new(sum(zhi)*shuil,2) as kuangqyfs, \n"
                            + " sum(zhi)-round_new(sum(zhi)*shuil,2) as kuangqyfj,";
                }
                //
                sql_talbe += CONDITON + " shuil from 	\n"
                        + " (select distinct feiyb.zhi,yunfdjb_id,feiyxmb.shuil  																	\n"
                        + " from yunfdjb,danjcpb,feiylbb,feiyb,feiyxmb															\n"
                        + " where danjcpb.yunfdjb_id=yunfdjb.id and yunfdjb.feiylbb_id=feiylbb.id						\n"
                        + " and yunfdjb.feiyb_id=feiyb.feiyb_id	and feiyb.feiyxmb_id=feiyxmb.id 														\n"
                        + " and feiylbb.id=" + Locale.kuangqyf_feiylbb_id + " and feiyb.shuib=1 and feiyxmb.juflx=0 and danjcpb.chepb_id in	\n"
                        + " (select c.id from chepb c,fahb f where f.id=c.fahb_id " + guohxt + " and f.lie_id in (" + SelIds + "))) group by shuil) c		\n"
                        + " ,																							\n"
                        + "(select max(0) as id,sum(zhi) as kuangqzf from 	\n"
                        + " (select distinct feiyb.*,yunfdjb.id  																	\n"
                        + " from yunfdjb,danjcpb,feiylbb,feiyb,feiyxmb															\n"
                        + " where danjcpb.yunfdjb_id=yunfdjb.id and yunfdjb.feiylbb_id=feiylbb.id						\n"
                        + " and yunfdjb.feiyb_id=feiyb.feiyb_id	and feiyb.feiyxmb_id=feiyxmb.id														\n"
                        + " and feiylbb.id=" + Locale.kuangqyf_feiylbb_id + " and feiyb.shuib=0 and feiyxmb.juflx=0 and danjcpb.chepb_id in	\n"
                        + " (select c.id from chepb c,fahb f where f.id=c.fahb_id " + guohxt + " and f.lie_id in (" + SelIds + ")))) d		\n";

                sql_where = " and a.id=c.id(+) and a.id=d.id(+) ";

                lngJieslx = Locale.guotyf_feiylbb_id;
                strJieslx = Locale.guotyf_feiylbb_id + "," + Locale.haiyyf_feiylbb_id;
                //endregion
            }


            sql = " select decode(tielyf,null,0,tielyf) as tielyf,												\n"
                    + " decode(tielzf,null,0,tielzf) as tielzf,														\n"
                    + " decode(tielyfs,null,0,tielyfs) as tielyfsk,"
                    + " decode(tielyfj,null,0,tielyfj) as tielyfjk "
                    + sql_colum
                    + " from	"
                    + "(select max(0) as id,sum(zhi) as tielyf,\n";

//		        	税率调整
            String CONDITON = "";
            if (yunjslx.equals("是")) {
                CONDITON = "sum(zhi)-round_new(sum(zhi)/(1+shuil),2) as tielyfs, \n"
                        + " round_new(sum(zhi)/(1+shuil),2) as tielyfj, \n";
            } else {
                CONDITON = "round_new(sum(zhi)*shuil,2) as tielyfs,  \n"
                        + " sum(zhi)-round_new(sum(zhi)*shuil,2) as tielyfj, \n";
            }
//
            sql += CONDITON + " shuil from	\n"
                    + "(select distinct feiyb.zhi,yunfdjb_id,feiyxmb.shuil																		\n"
                    + " from yunfdjb,danjcpb,feiylbb,feiyb,feiyxmb															\n"
                    + " where danjcpb.yunfdjb_id=yunfdjb.id and yunfdjb.feiylbb_id=feiylbb.id						\n"
                    + " and yunfdjb.feiyb_id=feiyb.feiyb_id	and feiyb.feiyxmb_id=feiyxmb.id 														\n"
                    + " and feiylbb.id in (" + strJieslx + ") and feiyb.shuib=1 and feiyxmb.juflx=0 and danjcpb.chepb_id in							\n"
                    + " (select c.id from chepb c,fahb f where f.id=c.fahb_id " + guohxt + " and f.lie_id in (" + SelIds + "))) group by shuil) a,		\n"
                    + "(select max(0) as id,sum(zhi) as tielzf from			\n"
                    + " (select distinct feiyb.*,yunfdjb.id  																\n"
                    + " from yunfdjb,danjcpb,feiylbb,feiyb,feiyxmb															\n"
                    + " where danjcpb.yunfdjb_id=yunfdjb.id and yunfdjb.feiylbb_id=feiylbb.id						\n"
                    + " and yunfdjb.feiyb_id=feiyb.feiyb_id															\n"
                    + " and feiylbb.id in (" + strJieslx + ") and feiyb.shuib=0 and feiyxmb.juflx=0 and danjcpb.chepb_id in							\n"
                    + " (select c.id from chepb c,fahb f where f.id=c.fahb_id " + guohxt + " and f.lie_id in (" + SelIds + ")))) b		\n"
                    + sql_talbe
                    + " where a.id=b.id(+) "
                    + sql_where;

            rs = con.getResultSet(sql);

            if (rs.next()) {
//				        两票结算（铁路，从yunfdjb,danjcpb中取值，前提是要先进行货票核对）
//				    	yunf铁路大票上所有的费用
//				    	yunfzf铁路大票上所有不可抵税的费用
//				    	两票结算或国铁运费时用
                bsv.setTielyf(rs.getDouble("tielyf"));
                bsv.setTielzf(rs.getDouble("tielzf"));
                bsv.setTielyfsk(rs.getDouble("tielyfsk"));
                bsv.setTielyfjk(rs.getDouble("tielyfjk"));

                if (lngJieslx == Locale.liangpjs_feiylbb_id
                        || lngJieslx == Locale.guotyf_feiylbb_id || lngJieslx == Locale.haiyyf_feiylbb_id) {

//				    		两票结算、国铁运费
                    bsv.setKuangqyf(rs.getDouble("kuangqyf"));
                    bsv.setKuangqzf(rs.getDouble("kuangqzf"));
                    bsv.setKuangqjk(rs.getDouble("kuangqyfjk"));
                    bsv.setKuangqsk(rs.getDouble("kuangqyfsk"));
                }
            } else {

                bsv.setTielyf(0);
                bsv.setTielzf(0);
                bsv.setTielyfsk(0);
                bsv.setTielyfjk(0);

                bsv.setKuangqyf(0);
                bsv.setKuangqzf(0);
                bsv.setKuangqjk(0);
                bsv.setKuangqsk(0);
            }

            if (bsv.getTielyf() == 0 && bsv.getTielzf() == 0 && bsv.getKuangqyf() == 0 && bsv.getKuangqzf() == 0) {
//				    	如果从yunfdjb,danjcpb中取值不到，说明是从煤款单价中或是运费合同中取数

//				    	情况1：如果是两票结算、先从煤款合同表中取运费、
//				    			如果没有取到运费，再从煤款运费合同关联表中取出运费合同id,
//				    			再根据运费合同中的煤矿取出结算价格

//				    	情况2：如果是单结算运费，那么Hetb_id就是运费合同表id,
//				    			再根据运费合同中的煤矿取出结算价格

//				    	bsv.setYunfjsl(bsv.getGongfsl());
                //region Description
                if (Jieslx == Locale.liangpjs_feiylbb_id) {
//				    		两票结算，情况1

                    if (bsv.getHetyj() > 0 && !bsv.getHetyjdw().equals("")) {
//			    				如果煤款合同中有运费则计算

                        CountYf(0, Shangcjsl);
                    } else {
//			    				如果煤款合同中没有运费，从煤款运费合同关联表中取出运费合同id
                        lngYunshtb_id = getYunshtb_id(Hetb_id);

                        if (lngYunshtb_id > 0) {
//			    					说明有已找到相应的运费合同了
                            CountYf(lngYunshtb_id, Shangcjsl);
                        } else if (bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)) {
//			    					到厂价结算,如果是两票结算运费，而又是到厂价合同的话，不计算运费

                        } else {
//			    					出矿价结算
                            bsv.setErroInfo("煤款合同<" + Jiesdcz.getHetbh(bsv.getHetb_Id()) + ">没有对应的运费合同，请设置！");
                            return false;
                        }
                    }
                } else if (Jieslx != Locale.meikjs_feiylbb_id) {
//				    		国铁运费结算(单独结算运费)
                    CountYf(bsv.getHetb_Id(), Shangcjsl);
                }
                //endregion
            } else {
//				    	从yunfdjb,danjcpb中取到数据，说明是通过核对货票生成的运费，运费结算量为biaoz
//				    	如果是单结算的运费，数据来源也是yunfdjb,danjcpb,那么认为结算数量也为biaoz
                bsv.setYunfjsl(bsv.getGongfsl());
                if (Jieslx == Locale.guotyf_feiylbb_id || Jieslx == Locale.haiyyf_feiylbb_id) {

                    bsv.setJiessl(bsv.getGongfsl());
                }
            }

////		    		一口价运费源于煤款
//		    		if(bsv.getYikj_yunfyymk().equals("是")){
//
//		    			sql=" select decode(tielyf,null,0,tielyf) as tielyf,												\n"
//				        	+ " decode(tielzf,null,0,tielzf) as tielzf														\n"
//				        	+ " from																						\n"
//				        	+ "(select sum(zhi) as tielyf																	\n"
//				        	+ " from yunfdjb,danjcpb,feiylbb,feiyb															\n"
//				        	+ " where danjcpb.yunfdjb_id=yunfdjb.id and yunfdjb.feiylbb_id=feiylbb.id						\n"
//				        	+ " and yunfdjb.feiyb_id=feiyb.feiyb_id															\n"
//				        	+ " and feiylbb.mingc='"+Locale.guotyf_feiylbb+"' and feiyb.shuib=1 and danjcpb.chepb_id in		\n"
//				        	+ " (select c.id from chepb c,fahb f where f.id=c.fahb_id and f.lie_id in ("+SelIds+")))		\n"
//				        	+ " ,																							\n"
//				        	+ " (select sum(zhi) as tielzf  																\n"
//				        	+ " from yunfdjb,danjcpb,feiylbb,feiyb															\n"
//				        	+ " where danjcpb.yunfdjb_id=yunfdjb.id and yunfdjb.feiylbb_id=feiylbb.id						\n"
//				        	+ " and yunfdjb.feiyb_id=feiyb.feiyb_id															\n"
//				        	+ " and feiylbb.mingc='"+Locale.guotyf_feiylbb+"' and feiyb.shuib=0 and danjcpb.chepb_id in		\n"
//				        	+ " (select c.id from chepb c,fahb f where f.id=c.fahb_id and f.lie_id in ("+SelIds+")))";
//		    		}else{
//
//		    			if(bsv.getHetjgb_id()>0){
////		    				已经有合同价格表
//
//		    				sql="select hj.yunj as qiyj	from hetjgb hj where hj.id="+bsv.getHetjgb_id();
//		    			}else{
//
//		    				sql="select hj.yunj as qiyj	from hetjgb hj,hetb ht where hj.hetb_id=ht.id and ht.id="+Hetb_id;
//		    			}
//		    		}
//
//		    		rs=con.getResultSet(sql);
//
//		    		if(rs.next()){
//
//		    			bsv.setTielyf((double)CustomMaths.Round_new(rs.getDouble("qiyj")*bsv.getYunfjsl(),2));
//		    		}
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            con.Close();
        }
        return true;
    }

    private void setJieszb(Interpreter bsh, int Type, double Shangcjsl) {
//		此方法在Jiesdcz.java中还有一个类似的方法，如果修改此方法，要连同那个方法一起修改
        try {

//			Type	0:煤款结算
//					1:运费结算
            if (Type == 0 || (Type == 1 && bsv.getJieslx() != Locale.liangpjs_feiylbb_id
                    && bsv.getJieslx() != Locale.meikjs_feiylbb_id)) {
//				如果是煤款结算或两票结算结算煤款时可以进行赋值，否则只有在单独结算运费时才可赋值

                //			数量增扣款取值
                bsv.setShul_ht(Jiesdcz.Regular_Ht(bsh.get("合同标准_结算数量").toString()));
                bsv.setShul_yk(Double.parseDouble(bsh.get("盈亏_结算数量").toString()));
                bsv.setShul_zdj(Double.parseDouble(bsh.get("折单价_结算数量").toString()));
                bsv.setShul_zsl(Double.parseDouble(bsh.get("折数量_结算数量").toString()));
                bsv.setShul_zsldw(bsh.get("折数量单位_结算数量").toString());

                //			Qnetar
                bsv.setQnetar_ht(Jiesdcz.Regular_Ht(bsh.get("合同标准_Qnetar").toString()));
                bsv.setQnetar_yk(Double.parseDouble(bsh.get("盈亏_Qnetar").toString()));
                bsv.setQnetar_zdj(Double.parseDouble(bsh.get("折单价_Qnetar").toString()));
                bsv.setQnetar_zsl(Double.parseDouble(bsh.get("折数量_Qnetar").toString()));
                bsv.setQnetar_zsldw(bsh.get("折数量单位_Qnetar").toString());

                //			Std
                bsv.setStd_ht(Jiesdcz.Regular_Ht(bsh.get("合同标准_Std").toString()));
                bsv.setStd_yk(Double.parseDouble(bsh.get("盈亏_Std").toString()));
                bsv.setStd_zdj(Double.parseDouble(bsh.get("折单价_Std").toString()));
                bsv.setStd_zsl(Double.parseDouble(bsh.get("折数量_Std").toString()));
                bsv.setStd_zsldw(bsh.get("折数量单位_Std").toString());

                //			Ad
                bsv.setAd_ht(Jiesdcz.Regular_Ht(bsh.get("合同标准_Ad").toString()));
                bsv.setAd_yk(Double.parseDouble(bsh.get("盈亏_Ad").toString()));
                bsv.setAd_zdj(Double.parseDouble(bsh.get("折单价_Ad").toString()));
                bsv.setAd_zsl(Double.parseDouble(bsh.get("折数量_Ad").toString()));
                bsv.setAd_zsldw(bsh.get("折数量单位_Ad").toString());

                //			Vdaf
                bsv.setVdaf_ht(Jiesdcz.Regular_Ht(bsh.get("合同标准_Vdaf").toString()));
                bsv.setVdaf_yk(Double.parseDouble(bsh.get("盈亏_Vdaf").toString()));
                bsv.setVdaf_zdj(Double.parseDouble(bsh.get("折单价_Vdaf").toString()));
                bsv.setVdaf_zsl(Double.parseDouble(bsh.get("折数量_Vdaf").toString()));
                bsv.setVdaf_zsldw(bsh.get("折数量单位_Vdaf").toString());

                //			Mt
                bsv.setMt_ht(Jiesdcz.Regular_Ht(bsh.get("合同标准_Mt").toString()));
                bsv.setMt_yk(Double.parseDouble(bsh.get("盈亏_Mt").toString()));
                bsv.setMt_zdj(Double.parseDouble(bsh.get("折单价_Mt").toString()));
                bsv.setMt_zsl(Double.parseDouble(bsh.get("折数量_Mt").toString()));
                bsv.setMt_zsldw(bsh.get("折数量单位_Mt").toString());

                //			Qgrad
                bsv.setQgrad_ht(Jiesdcz.Regular_Ht(bsh.get("合同标准_Qgrad").toString()));
                bsv.setQgrad_yk(Double.parseDouble(bsh.get("盈亏_Qgrad").toString()));
                bsv.setQgrad_zdj(Double.parseDouble(bsh.get("折单价_Qgrad").toString()));
                bsv.setQgrad_zsl(Double.parseDouble(bsh.get("折数量_Qgrad").toString()));
                bsv.setQgrad_zsldw(bsh.get("折数量单位_Qgrad").toString());

                //			Qbad
                bsv.setQbad_ht(Jiesdcz.Regular_Ht(bsh.get("合同标准_Qbad").toString()));
                bsv.setQbad_yk(Double.parseDouble(bsh.get("盈亏_Qbad").toString()));
                bsv.setQbad_zdj(Double.parseDouble(bsh.get("折单价_Qbad").toString()));
                bsv.setQbad_zsl(Double.parseDouble(bsh.get("折数量_Qbad").toString()));
                bsv.setQbad_zsldw(bsh.get("折数量单位_Qbad").toString());

                //			Had
                bsv.setHad_ht(Jiesdcz.Regular_Ht(bsh.get("合同标准_Had").toString()));
                bsv.setHad_yk(Double.parseDouble(bsh.get("盈亏_Had").toString()));
                bsv.setHad_zdj(Double.parseDouble(bsh.get("折单价_Had").toString()));
                bsv.setHad_zsl(Double.parseDouble(bsh.get("折数量_Had").toString()));
                bsv.setHad_zsldw(bsh.get("折数量单位_Had").toString());

                //			Stad
                bsv.setStad_ht(Jiesdcz.Regular_Ht(bsh.get("合同标准_Stad").toString()));
                bsv.setStad_yk(Double.parseDouble(bsh.get("盈亏_Stad").toString()));
                bsv.setStad_zdj(Double.parseDouble(bsh.get("折单价_Stad").toString()));
                bsv.setStad_zsl(Double.parseDouble(bsh.get("折数量_Stad").toString()));
                bsv.setStad_zsldw(bsh.get("折数量单位_Stad").toString());

                //			Star
                bsv.setStar_ht(Jiesdcz.Regular_Ht(bsh.get("合同标准_Star").toString()));
                bsv.setStar_yk(Double.parseDouble(bsh.get("盈亏_Star").toString()));
                bsv.setStar_zdj(Double.parseDouble(bsh.get("折单价_Star").toString()));
                bsv.setStar_zsl(Double.parseDouble(bsh.get("折数量_Star").toString()));
                bsv.setStar_zsldw(bsh.get("折数量单位_Star").toString());

                //			Mad
                bsv.setMad_ht(Jiesdcz.Regular_Ht(bsh.get("合同标准_Mad").toString()));
                bsv.setMad_yk(Double.parseDouble(bsh.get("盈亏_Mad").toString()));
                bsv.setMad_zdj(Double.parseDouble(bsh.get("折单价_Mad").toString()));
                bsv.setMad_zsl(Double.parseDouble(bsh.get("折数量_Mad").toString()));
                bsv.setMad_zsldw(bsh.get("折数量单位_Mad").toString());

                //			Aar
                bsv.setAar_ht(Jiesdcz.Regular_Ht(bsh.get("合同标准_Aar").toString()));
                bsv.setAar_yk(Double.parseDouble(bsh.get("盈亏_Aar").toString()));
                bsv.setAar_zdj(Double.parseDouble(bsh.get("折单价_Aar").toString()));
                bsv.setAar_zsl(Double.parseDouble(bsh.get("折数量_Aar").toString()));
                bsv.setAar_zsldw(bsh.get("折数量单位_Aar").toString());

                //			Aad
                bsv.setAad_ht(Jiesdcz.Regular_Ht(bsh.get("合同标准_Aad").toString()));
                bsv.setAad_yk(Double.parseDouble(bsh.get("盈亏_Aad").toString()));
                bsv.setAad_zdj(Double.parseDouble(bsh.get("折单价_Aad").toString()));
                bsv.setAad_zsl(Double.parseDouble(bsh.get("折数量_Aad").toString()));
                bsv.setAad_zsldw(bsh.get("折数量单位_Aad").toString());

                //			Vad
                bsv.setVad_ht(Jiesdcz.Regular_Ht(bsh.get("合同标准_Vad").toString()));
                bsv.setVad_yk(Double.parseDouble(bsh.get("盈亏_Vad").toString()));
                bsv.setVad_zdj(Double.parseDouble(bsh.get("折单价_Vad").toString()));
                bsv.setVad_zsl(Double.parseDouble(bsh.get("折数量_Vad").toString()));
                bsv.setVad_zsldw(bsh.get("折数量单位_Vad").toString());

                //			St
                bsv.setT2_ht(Jiesdcz.Regular_Ht(bsh.get("合同标准_T2").toString()));
                bsv.setT2_yk(Double.parseDouble(bsh.get("盈亏_T2").toString()));
                bsv.setT2_zdj(Double.parseDouble(bsh.get("折单价_T2").toString()));
                bsv.setT2_zsl(Double.parseDouble(bsh.get("折数量_T2").toString()));
                bsv.setT2_zsldw(bsh.get("折数量单位_T2").toString());

                //			运距
                bsv.setYunju_ht(Jiesdcz.Regular_Ht(bsh.get("合同标准_运距").toString()));
                bsv.setYunju_yk(Double.parseDouble(bsh.get("盈亏_运距").toString()));
                bsv.setYunju_zdj(Double.parseDouble(bsh.get("折单价_运距").toString()));
                bsv.setYunju_zsl(Double.parseDouble(bsh.get("折数量_运距").toString()));
                bsv.setYunju_zsldw(bsh.get("折数量单位_运距").toString());
            }
            //结算单价
            if (Type == 0) {

//				处理数量“%吨”的情况
                Jiesdcz.getReCountJiessl(bsv, 0, Type);

                bsv.setHansmj(Double.parseDouble(bsh.get("结算价格").toString()));

            } else if (Type == 1) {

                if (bsv.getJieslx() != Locale.liangpjs_feiylbb_id
                        && bsv.getJieslx() != Locale.meikjs_feiylbb_id) {

//					处理数量“%吨”的情况
                    Jiesdcz.getReCountJiessl(bsv, 0, Type);
                    bsv.setHansmj(Double.parseDouble(bsh.get("结算价格").toString()));
                    bsv.setJiessl(bsv.getYunfjsl());
                }

                bsv.setYunfjsdj(Double.parseDouble(bsh.get("结算价格").toString()));
                bsv.setYunfjsdj_mk(bsv.getYunfjsdj());    //用于记录单结算煤款时，当运费参与计算时的值（用于结算表的保存
//				如果是非核对货票方式进行运费结算，在为运费单价赋值后直接计算出运费合计
                bsv.setTielyf((double) CustomMaths.Round_new(bsv.getYunfjsdj() * bsv.getYunfjsl(), 2));
            }
            if ((!bsv.getShangcjslct_Flag()) && bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)) {

                bsv.setJiessl(CustomMaths.sub(bsv.getJiessl(), Shangcjsl)); //算完数量折价后将上次结算量删除，即为本次结算量
                bsv.setShangcjslct_Flag(true);
            }

        } catch (EvalError e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void computMlj(Interpreter bsh, ResultSetList rsl, Jiesdcz Jscz, long Diancxxb_id, long Gongysb_id, long Hetb_id) {

//		为计算目录价赋值
        try {
            double Dbljijzb = 0;

            bsh.set("热值基价_" + Locale.Qnetar_zhibb, 0);
            bsh.set(Locale.Qnetar_zhibb + "_上限", 0);
            bsh.set(Locale.Qnetar_zhibb + "_下限", 0);
            bsh.set("挥发份比价_" + Locale.Vdaf_zhibb, 0);
            bsh.set(Locale.Vdaf_zhibb + "_上限", 0);
            bsh.set(Locale.Vdaf_zhibb + "_下限", 0);
            bsh.set("硫分比价_" + Locale.Std_zhibb, 0);
            bsh.set(Locale.Std_zhibb + "_上限", 0);
            bsh.set(Locale.Std_zhibb + "_下限", 0);
            bsh.set("硫分比价_" + Locale.Stad_zhibb, 0);
            bsh.set(Locale.Stad_zhibb + "_上限", 0);
            bsh.set(Locale.Stad_zhibb + "_下限", 0);
            bsh.set("灰分比价_" + Locale.Aar_zhibb, 0);
            bsh.set(Locale.Aar_zhibb + "_上限", 0);
            bsh.set(Locale.Aar_zhibb + "_下限", 0);
            bsh.set("灰分比价_" + Locale.Aad_zhibb, 0);
            bsh.set(Locale.Aad_zhibb + "_上限", 0);
            bsh.set(Locale.Aad_zhibb + "_下限", 0);

            do {

                Dbljijzb = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");
                Dbljijzb = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbljijzb, bsv.getMj_to_kcal_xsclfs());

                if (Dbljijzb >= rsl.getDouble("xiax") && Dbljijzb <= (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx"))) {

                    if (Jiesdcz.CheckMljRz(rsl.getString("zhib"))) {

                        bsh.set(rsl.getString("zhib") + "_上限", Jiesdcz.getUnit_transform(rsl.getString("zhib"), Locale.qiankmqk_danw, (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx"))));

                        bsh.set(rsl.getString("zhib") + "_下限", Jiesdcz.getUnit_transform(rsl.getString("zhib"), Locale.qiankmqk_danw, rsl.getDouble("xiax")));

                        bsh.set("热值基价_" + rsl.getString("zhib"), rsl.getDouble("jij"));
                    }
                    if (Jiesdcz.CheckMljHff(rsl.getString("zhib"))) {

                        bsh.set(rsl.getString("zhib") + "_上限", (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx")));

                        bsh.set(rsl.getString("zhib") + "_下限", rsl.getDouble("xiax"));

                        bsh.set("挥发份比价_" + rsl.getString("zhib"), rsl.getDouble("jij"));
                    }
                    if (Jiesdcz.CheckMljLiuf(rsl.getString("zhib"))) {

                        bsh.set(rsl.getString("zhib") + "_上限", (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx")));

                        bsh.set(rsl.getString("zhib") + "_下限", rsl.getDouble("xiax"));

                        bsh.set("硫分比价_" + rsl.getString("zhib"), rsl.getDouble("jij"));
                    }
                    if (Jiesdcz.CheckMljHiuf(rsl.getString("zhib"))) {

                        bsh.set(rsl.getString("zhib") + "_上限", (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx")));

                        bsh.set(rsl.getString("zhib") + "_下限", rsl.getDouble("xiax"));

                        bsh.set("灰分比价_" + rsl.getString("zhib"), rsl.getDouble("jij"));
                    }

                    bsv.setHetyj(rsl.getDouble("yunj"));            //合同运价单价
                    bsv.setHetyjdw(rsl.getString("yunjdw"));        //合同运价单位

                    bsv.setHetjgpp_Flag(true);
                }

            } while (rsl.next());

            bsh.set("品种比价", Jscz.getMljPzbj(bsv.getRanlpzb_Id()));

            //	政策性加价
            bsh.set(Locale.zhengcxjj_jies, Double.parseDouble(Jiesdcz.getJiessz_item(Diancxxb_id, Gongysb_id, Hetb_id, Locale.zhengcxjj_jies, "0")));

            //	加价
            bsh.set(Locale.jiaj_jies, Double.parseDouble(Jiesdcz.getJiessz_item(Diancxxb_id, Gongysb_id, Hetb_id, Locale.jiaj_jies, "0")));

        } catch (EvalError e) {

            e.printStackTrace();
        }
    }

    //计算费用加权
    private void computData(String selIds, long hetb_id, double shangcjsl) {
        //计算煤价,热量折价,硫折价,灰熔点折价
        //煤款
        double _Hansmj = bsv.getHansmj();
        double _Jiessl = bsv.getJiessl();    //煤款结算数量
        double _Meiksl = bsv.getMeiksl();

        //运费
//		double _Tielyf=bsv.getTielyf();
//		double _Tielzf=bsv.getTielzf();
//		double _Yunfsl=bsv.getYunfsl();
//		double _Kuangqyf=bsv.getKuangqyf();
//		double _Kuangqzf=bsv.getKuangqzf();
//		double _Kuangqsk=bsv.getKuangqsk();
//		double _Kuangqjk=bsv.getKuangqjk();

        //指标盈亏
        double _Shulzbyk = bsv.getShul_yk();        //执行合同中的超吨奖励用(数量指标盈亏)

        double _Jiashj = 0;
        double _Jiakhj = 0;
        double _Jiaksk = 0;
        double _Jine = 0;
        double _Buhsmj = 0;
        double _Shulzjbz = 0;
//		double _Yunfsk=0;
//		double _Yunzfhj=0;
//		double _Buhsyf=0;
        double _Hej = 0;

        //指标折金额
        double _Qnetarzje = 0;
        double _Stdzje = 0;
        double _Adzje = 0;
        double _Vdafzje = 0;
        double _Mtzje = 0;
        double _Qgradzje = 0;
        double _Qbadzje = 0;
        double _Hadzje = 0;
        double _Stadzje = 0;
        double _Madzje = 0;
        double _Aarzje = 0;
        double _Aadzje = 0;
        double _Vadzje = 0;
        double _T2zje = 0;
        double _Shulzje = 0;
        double _Shulzbzje = 0;
        double _Yunjuzje = 0;
        double _Starzje = 0;

        double _Meikzkktzsj[] = null;
        boolean _Iszksjtz = false; //判断是否已经进行了增扣款的数据调整了

        Danjsmk_dcjcl(1, selIds, hetb_id, shangcjsl);

//		if(!bsv.getMeikzkksyfw().equals("")
//				&&bsv.getMeikzkksyfw()!=null){
////			说明有要部分增扣款的项目,目前只处理数量部分享受加价的业务
////			1、超出部分对应的折单价
////			2、超出部分折金额
//
////			处理逻辑：
////			总金额：	(hansmj-超出部分的折价)*结算数量+超出部分的折价×超出部分
//			_Meikzkktzsj=Jiesdcz.Zengkktz(bsv);
//		}
        //价格金额计算
//		2008-12-9zsj加：
//		逻辑：	如果合同价格的结算方式是“到厂价格”，且合同价格中还包含含税运费单价，
//					则我们认为是一下两种情况的一种：
//						情况一：

//							含税总煤款=到厂价×煤款结算数量-运费含税单价×运费结算数量
//							含税总运费=运费含税单价×运费结算数量
//							处理方法：在结算设置里增加“一口价(运费源于煤款)”的设置，默认值“否”，如果值为“是”
//									则按照此情况处理。
//						情况二：

//							煤款含税单价=计算出的煤款含税单价-合同价格中的含税运费单价
//							含税运费单价=合同价格中的含税运费单价
//							同时要更新Hansmj
//							处理方法：系统默认，即如果合同价格的结算方式是“到厂价格”，且合同价格中还包含含税运费单价，
//									且结算设置中“一口价(运费源于煤款)”值为“否”，则按照此情况处理。

//
        //			如果合同价格的结算方式是“到厂价格”，且合同价格中运费含税单价大于0，且运费价格单位不等于“”

//					if(bsv.getHetyjdw().equals(Locale.yuanmd_danw)){
//		//				如果运费单价单位=“元/吨”
//		//					保留小数的处理方法：根据结算设置"煤款含税单价保留小数位"
////						原则：
//		//					1、关于煤款是到厂价运费还有数的问题，如果煤价是不含税的，操作起来有问题，故先不予考虑，视为运费和煤款属于同种单价类型
////							2、关于分公司加价问题，认为加价都为含税价。
////								如果煤款为含税价，含税单价=含税单价+分公司加价
////								如果煤款为不含税价，含税单价=不含税单价×（1+税率）+分公司加价
//
////						增扣款调整，放到下午处理
////						if(_Meikzkktzsj!=null){
////
//////							1、超出部分对应的折单价
//////							2、超出部分折金额
//////							(hansmj-超出部分的折价)*结算数量+超出部分折金额
////							_Jiashj=(_Hansmj-_Meikzkktzsj[0])*_Jiessl+_Meikzkktzsj[1];
////							if(bsv.getJijlx()==0){
//////								含税单价
////								_Hansmj=(double)CustomMaths.Round_new(_Jiashj/_Jiessl,bsv.getMeikhsdjblxsw());
////							}else if(bsv.getJijlx()==1){
//////								不含税
////								_Hansmj=(double)CustomMaths.Round_new(_Jiashj/_Jiessl,7);
////							}
////						}
//
//						_Hansmj=(double)CustomMaths.Round_new(_Hansmj-bsv.getHetyj(), bsv.getMeikhsdjblxsw());
//					}
        //			else if(bsv.getHetyjdw().equals(Locale.dun_danw)){
        ////				如果运费单价单位=“吨”
        ////				保留小数的处理方法：根据结算设置"结算数量保留小数位"
        //				_Jiessl=(double)CustomMaths.Round_new(_Jiessl-bsv.getHetyj(), Integer.parseInt(bsv.getJiesslblxs()));
        //				bsv.setJiessl(_Jiessl);
        //			}

//		计算指标特殊处理实现逻辑

        double jieszjecj = 0;    //结算总金额差价（特殊指标增扣款折金额计算完成后要在jieszje上要增减的钱，
//		                      jieszjecj是含税的，当Jijlx为1时，将jieszjecj转换为不含税的）

        if (bsv.getTsclzbs() != null) {
//			这时的Tsclzbs数组里存的是指标的折价信息
//			数组中元素排列：指标编码,指标折单价,折价数量,折金额
            String tmp[] = null;
            String zhibbm = "";
            double zhibzje = 0;
            double zhibzdj = 0;
            double zhibjsbz = 0;        //特殊指标的结算标准(单批次结算指标的值)
            double zhibjsbzdysl = 0;    //特殊指标结算标准对应的数量
            double zhibjsbzjqz = 0;    //特殊指标结算标准的加权值（特殊指标的结算标准×特殊指标结算标准对应的数量）

            for (int i = 0; i < bsv.getTsclzbs().length; i++) {

                tmp = bsv.getTsclzbs()[i].split(",");

                if (zhibbm.equals(tmp[0])) {
//						同一个指标
//						重新计算折金额
                    zhibzje = CustomMaths.add(zhibzje, Double.parseDouble(tmp[tmp.length - 1]));
//						重新计算折单价
                    zhibzdj = CustomMaths.Round_new(zhibzje / bsv.getJiessl(), bsv.getMeikzkkblxsw());
//
//						单批次结算指标的值
                    if (tmp[1].equals("'C'")) {
//						说明是对超出部分的增扣款处理
                        zhibjsbz = 0;
                    } else {

                        zhibjsbz = Double.parseDouble(tmp[1]);
                    }
//						结算标准对应的数量
                    zhibjsbzdysl = CustomMaths.add(zhibjsbzdysl, Double.parseDouble(tmp[3]));
//						累加结算标准的加权值
                    zhibjsbzjqz = CustomMaths.add(zhibjsbzjqz, CustomMaths.mul(zhibjsbz, Double.parseDouble(tmp[3])));
//						给指标赋值
                    Jiesdcz.setJieszbzdj_Tszb(zhibbm, bsv, zhibzdj, CustomMaths.Round_new(CustomMaths.div(zhibjsbzjqz, zhibjsbzdysl), bsv.getMeikzkkblxsw()), zhibzje);

                } else {
//						跟上一次不是同一个指标
                    zhibbm = tmp[0];
//						算出折金额
                    zhibzje = Double.parseDouble(tmp[tmp.length - 1]);
//						算出折单价
                    zhibzdj = Double.parseDouble(tmp[2]);
//						单批次结算指标的值
                    if (tmp[1].equals("'C'")) {
//						说明是对超出部分的增扣款处理
                        zhibjsbz = 0;
                    } else {
//						说明是对“加权平均”中的“单批次”的逻辑的处理
                        zhibjsbz = Double.parseDouble(tmp[1]);
                    }

//						结算标准对应的数量
                    zhibjsbzdysl = Double.parseDouble(tmp[3]);
//						结算标准的加权值
                    zhibjsbzjqz = CustomMaths.mul(zhibjsbz, zhibjsbzdysl);
//						给指标赋值
                    Jiesdcz.setJieszbzdj_Tszb(zhibbm, bsv, zhibzdj, zhibjsbz, zhibzje);
                }
//				将特殊增扣款的折价金额记录下来
                jieszjecj = zhibzje;
            }
        }

//			处理分公司加价、和不含税单价计算
        if (bsv.getJijlx() == 0) {
//					含税单价

            if (jieszjecj == 0) {
//				没有特殊指标单独处理

                bsv.setJiajqdj(_Hansmj);                                        //保存加价前单价
                if (bsv.getYunzfhj() > 0 && bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)) {
//					需要重算含税单价(到厂价含运费)
                    _Jiashj = (double) CustomMaths.sub((double) CustomMaths.Round_new(_Hansmj * _Jiessl, 2), bsv.getYunzfhj());        //价税合计
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                    bsv.setJiajqdj(_Hansmj);
                }
                if (bsv.getJiesfs().equals(Locale.pingcjg_ht_jsfs) || bsv.getJiesfs().equals(Locale.chebjg_ht_jsfs)) { // 平仓价格 或 车板价格
                    _Hansmj = _Hansmj + bsv.getFengsjj();                                //加上分公司加价
                    bsv.setHansmj(_Hansmj);                                            //更新含税单价
                    _Jiashj = (double) CustomMaths.Round_new(_Hansmj * _Jiessl, 2);        //价税合计
                    _Jiakhj = (double) CustomMaths.Round_new((_Jiashj) / (1 + _Meiksl), 2);    //价款合计
                    _Jiaksk = (double) CustomMaths.Round_new((_Jiashj - _Jiakhj), 2);        //价款税款
                    _Jine = _Jiakhj;                                                    //金额
                    _Buhsmj = (double) CustomMaths.Round_new(_Jiakhj / _Jiessl, 7);        //不含税单价

                } else {
                    _Hansmj = _Hansmj + bsv.getFengsjj();                                //加上分公司加价
                    bsv.setHansmj(_Hansmj);                                            //更新含税单价
                    _Jiashj = (double) CustomMaths.Round_new(_Hansmj * _Jiessl, 2);        //价税合计
                    _Jiakhj = (double) CustomMaths.Round_new((_Jiashj) / (1 + _Meiksl), 2);    //价款合计
                    _Jiaksk = (double) CustomMaths.Round_new((_Jiashj - _Jiakhj), 2);        //价款税款
                    _Jine = _Jiakhj;                                                    //金额
                    _Buhsmj = (double) CustomMaths.Round_new(_Jiakhj / _Jiessl, 7);        //不含税单价
                }

            } else {
//				有特殊指标单独处理

                _Jiashj = (double) CustomMaths.Round_new(_Hansmj * _Jiessl, 2);        //价税合计
                if (bsv.getYunzfhj() > 0 && bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)) {

                    _Jiashj = (double) CustomMaths.sub(_Jiashj, bsv.getYunzfhj());
                }
                if (bsv.getJiesfs().equals(Locale.pingcjg_ht_jsfs) || bsv.getJiesfs().equals(Locale.chebjg_ht_jsfs)) { // 平仓价格 或 车板价格
                    _Jiashj = (double) CustomMaths.Round_new(CustomMaths.add(_Jiashj, jieszjecj), 2);            //计算不含分公司加价的价税合计
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());    //反推加钱前含税单价
                    bsv.setJiajqdj(_Hansmj);
                    _Jiashj = (double) CustomMaths.Round_new(_Jiashj
                            + (double) CustomMaths.Round_new(CustomMaths.mul(bsv.getFengsjj(), _Jiessl), 2), 2); //j结算特殊指标增扣款后价税合计
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());    //反推含税单价
                    bsv.setHansmj(_Hansmj);                                            //更新含税单价
                    _Jiakhj = (double) CustomMaths.Round_new((_Jiashj) / (1 + _Meiksl), 2);    //价款合计
                    _Jiaksk = (double) CustomMaths.Round_new((_Jiashj - _Jiakhj), 2);        //价款税款
                    _Jine = _Jiakhj;                                                    //金额
                    _Buhsmj = (double) CustomMaths.Round_new(_Jiakhj / _Jiessl, 7);        //不含税单价
                } else {
                    _Jiashj = (double) CustomMaths.Round_new(CustomMaths.add(_Jiashj, jieszjecj), 2);            //计算不含分公司加价的价税合计
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());    //反推加钱前含税单价
                    bsv.setJiajqdj(_Hansmj);
                    _Jiashj = (double) CustomMaths.Round_new(_Jiashj
                            + (double) CustomMaths.Round_new(CustomMaths.mul(bsv.getFengsjj(), _Jiessl), 2), 2);                                    //j结算特殊指标增扣款后价税合计
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());    //反推含税单价
                    bsv.setHansmj(_Hansmj);                                            //更新含税单价
                    _Jiakhj = (double) CustomMaths.Round_new((_Jiashj) / (1 + _Meiksl), 2);    //价款合计
                    _Jiaksk = (double) CustomMaths.Round_new((_Jiashj - _Jiakhj), 2);        //价款税款
                    _Jine = _Jiakhj;                                                    //金额
                    _Buhsmj = (double) CustomMaths.Round_new(_Jiakhj / _Jiessl, 7);        //不含税单价
                }

            }

        } else if (bsv.getJijlx() == 1) {
//						基价类型（0、含税；1、不含税）
//						不含税
//					bsv.setJiajqdj(_Hansmj);

            if (jieszjecj == 0) {
//				没有特殊指标单独处理

                _Buhsmj = _Hansmj;
                _Jiakhj = (double) CustomMaths.Round_new(_Buhsmj * _Jiessl, 2);
//				计算加价前含税单价
                _Jiashj = (double) CustomMaths.Round_new(_Jiakhj * (1 + _Meiksl), 2);

                if (bsv.getYunzfhj() > 0 && bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)) {

                    _Jiashj = (double) CustomMaths.sub(_Jiashj, bsv.getYunzfhj());
                }

                if (bsv.getJiesfs().equals(Locale.pingcjg_ht_jsfs) || bsv.getJiesfs().equals(Locale.chebjg_ht_jsfs)) { // 平仓价格 或 车板价格
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                    bsv.setJiajqdj(_Hansmj);
//							计算加价前含税单价_end

                    _Jiashj = (double) CustomMaths.Round_new((double) CustomMaths.Round_new(_Jiakhj * (1 + _Meiksl), 2)
                            + (double) CustomMaths.Round_new(bsv.getFengsjj() * bsv.getJiessl(), 2), 2);    //处理分公司加价
                    _Jiakhj = (double) CustomMaths.Round_new((_Jiashj) / (1 + _Meiksl), 2);
                    _Jine = _Jiakhj;
                    _Jiaksk = (double) CustomMaths.Round_new((_Jiashj - _Jiakhj), 2);
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                    bsv.setHansmj(_Hansmj);
                } else {
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                    bsv.setJiajqdj(_Hansmj);
//							计算加价前含税单价_end

                    _Jiashj = (double) CustomMaths.Round_new((double) CustomMaths.Round_new(_Jiakhj * (1 + _Meiksl), 2)
                            + (double) CustomMaths.Round_new(bsv.getFengsjj() * bsv.getJiessl(), 2), 2);    //处理分公司加价
                    _Jiakhj = (double) CustomMaths.Round_new((_Jiashj) / (1 + _Meiksl), 2);
                    _Jine = _Jiakhj;
                    _Jiaksk = (double) CustomMaths.Round_new((_Jiashj - _Jiakhj), 2);
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                    bsv.setHansmj(_Hansmj);
                }

				/*
                *huochaoyuan
				*2009-10-22针对本地实际情况注掉以下语句，结算单上显示计算结果正确
				*/                                                                //金额
//								_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);		//不含税单价
//												end
            } else {
//				有特殊指标单独处理

                _Buhsmj = _Hansmj;
                _Jiakhj = (double) CustomMaths.Round_new(_Buhsmj * _Jiessl, 2);
//				_Jiakhj=CustomMaths.add(_Jiakhj,jieszjecj);										//计算特殊处理的指标折金额
                _Jiakhj = CustomMaths.add(_Jiakhj, CustomMaths.Round_new(jieszjecj / (1 + _Meiksl), 2));    //计算特殊处理的指标折金额
                _Jiashj = (double) CustomMaths.Round_new(_Jiakhj * (1 + _Meiksl), 2);
                if (bsv.getYunzfhj() > 0 && bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)) {

                    _Jiashj = (double) CustomMaths.sub(_Jiashj, bsv.getYunzfhj());
                }

                if (bsv.getJiesfs().equals(Locale.pingcjg_ht_jsfs) || bsv.getJiesfs().equals(Locale.chebjg_ht_jsfs)) { // 平仓价格 或 车板价格
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                    bsv.setJiajqdj(_Hansmj);
//							计算加价前含税单价_end

                    _Jiashj = (double) CustomMaths.Round_new((double) CustomMaths.Round_new(_Jiakhj * (1 + _Meiksl), 2)
                            + (double) CustomMaths.Round_new(bsv.getFengsjj() * bsv.getJiessl(), 2), 2);    //处理分公司加价
                    _Jiakhj = (double) CustomMaths.Round_new((_Jiashj) / (1 + _Meiksl), 2);
                    _Jine = _Jiakhj;
                    _Jiaksk = (double) CustomMaths.Round_new((_Jiashj - _Jiakhj), 2);
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                    bsv.setHansmj(_Hansmj);
                } else {
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                    bsv.setJiajqdj(_Hansmj);    //计算加价前含税单价_end

                    _Jiashj = (double) CustomMaths.Round_new((double) CustomMaths.Round_new(_Jiakhj * (1 + _Meiksl), 2)
                            + (double) CustomMaths.Round_new(bsv.getFengsjj() * bsv.getJiessl(), 2), 2);    //处理分公司加价
                    _Jiakhj = (double) CustomMaths.Round_new((_Jiashj) / (1 + _Meiksl), 2);
                    _Jine = _Jiakhj;
                    _Jiaksk = (double) CustomMaths.Round_new((_Jiashj - _Jiakhj), 2);
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                    bsv.setHansmj(_Hansmj);
                    _Buhsmj = (double) CustomMaths.Round_new(_Jiakhj / _Jiessl, 7);        //不含税单价
                }

            }
        }

        _Shulzjbz = _Hansmj;
        //合计
        _Hej = (double) CustomMaths.Round_new((_Jiashj), 2);

        //指标折单价

        double _Qnetar = bsv.getQnetar_zdj();
        double _Std = bsv.getStd_zdj();
        double _Ad = bsv.getAd_zdj();
        double _Vdaf = bsv.getVdaf_zdj();
        double _Mt = bsv.getMt_zdj();
        double _Qgrad = bsv.getQgrad_zdj();
        double _Qbad = bsv.getQbad_zdj();
        double _Had = bsv.getHad_zdj();
        double _Stad = bsv.getStad_zdj();
        double _Mad = bsv.getMad_zdj();
        double _Aar = bsv.getAar_zdj();
        double _Aad = bsv.getAad_zdj();
        double _Vad = bsv.getVad_zdj();
        double _T2 = bsv.getT2_zdj();
        double _Shulzb = bsv.getShul_zdj();        //数量指标折单价
        double _Yunju = bsv.getYunju_zdj();        //运距折单价
        double _Star = bsv.getStar_zdj();            //Star折单价

        //计算盈亏，折价金额

//		单纯的数量折价_begin
        if (bsv.getMeikzkksyfw().indexOf(Locale.jiessl_zhibb) > -1) {
//				说明超出部分的增扣款中包含该指标

            _Shulzbzje = bsv.getShul_zje_tscl();    //超过矿发量的盈亏
            bsv.setShulzb_zje(_Shulzbzje);
            bsv.setShulzb_zdj(CustomMaths.Round_new(CustomMaths.div(_Shulzbzje, _Shulzbyk), bsv.getMeikzkkblxsw()));
        } else {

            _Shulzbzje = CustomMaths.add((double) CustomMaths.Round_new(_Shulzb * _Jiessl, 2), bsv.getShul_zje_tscl());    //超过矿发量的盈亏
            bsv.setShulzb_zje(_Shulzbzje);
            bsv.setShulzb_zdj(CustomMaths.Round_new(CustomMaths.div(_Shulzbzje, _Jiessl), bsv.getMeikzkkblxsw()));
        }


//		单纯的数量折价_end

        _Qnetarzje = CustomMaths.add((double) CustomMaths.Round_new(_Qnetar * _Jiessl, 2), bsv.getQnetar_zje_tscl());
        bsv.setQnetar_zje(_Qnetarzje);
        bsv.setQnetar_zdj(CustomMaths.Round_new(CustomMaths.div(_Qnetarzje, _Jiessl), bsv.getMeikzkkblxsw()));

        _Stdzje = CustomMaths.add((double) CustomMaths.Round_new(_Std * _Jiessl, 2), bsv.getStd_zje_tscl());
        bsv.setStd_zje(_Stdzje);
        bsv.setStd_zdj(CustomMaths.Round_new(CustomMaths.div(_Stdzje, _Jiessl), bsv.getMeikzkkblxsw()));

        _Adzje = CustomMaths.add((double) CustomMaths.Round_new(_Ad * _Jiessl, 2), bsv.getAd_zje_tscl());
        bsv.setAd_zje(_Adzje);
        bsv.setAd_zdj(CustomMaths.Round_new(CustomMaths.div(_Adzje, _Jiessl), bsv.getMeikzkkblxsw()));

        _Vdafzje = CustomMaths.add((double) CustomMaths.Round_new(_Vdaf * _Jiessl, 2), bsv.getVdaf_zje_tscl());
        bsv.setVdaf_zje(_Vdafzje);
        bsv.setVdaf_zdj(CustomMaths.Round_new(CustomMaths.div(_Vdafzje, _Jiessl), bsv.getMeikzkkblxsw()));

        _Mtzje = CustomMaths.add((double) CustomMaths.Round_new(_Mt * _Jiessl, 2), bsv.getMt_zje_tscl());
        bsv.setMt_zje(_Mtzje);
        bsv.setMt_zdj(CustomMaths.Round_new(CustomMaths.div(_Mtzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Qgradzje = CustomMaths.add((double) CustomMaths.Round_new(_Qgrad * _Jiessl, 2), bsv.getQgrad_zje_tscl());
        bsv.setQgrad_zje(_Qgradzje);
        bsv.setQgrad_zdj(CustomMaths.Round_new(CustomMaths.div(_Qgradzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Qbadzje = CustomMaths.add((double) CustomMaths.Round_new(_Qbad * _Jiessl, 2), bsv.getQbad_zje_tscl());
        bsv.setQbad_zje(_Qbadzje);
        bsv.setQbad_zdj(CustomMaths.Round_new(CustomMaths.div(_Qbadzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Hadzje = CustomMaths.add((double) CustomMaths.Round_new(_Had * _Jiessl, 2), bsv.getHad_zje_tscl());
        bsv.setHad_zje(_Hadzje);
        bsv.setHad_zdj(CustomMaths.Round_new(CustomMaths.div(_Hadzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Stadzje = CustomMaths.add((double) CustomMaths.Round_new(_Stad * _Jiessl, 2), bsv.getStad_zje_tscl());
        bsv.setStad_zje(_Stadzje);
        bsv.setStad_zdj(CustomMaths.Round_new(CustomMaths.div(_Stadzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Starzje = CustomMaths.add((double) CustomMaths.Round_new(_Star * _Jiessl, 2), bsv.getStar_zje_tscl());
        bsv.setStar_zje(_Starzje);
        bsv.setStar_zdj(CustomMaths.Round_new(CustomMaths.div(_Starzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Madzje = CustomMaths.add((double) CustomMaths.Round_new(_Mad * _Jiessl, 2), bsv.getMad_zje_tscl());
        bsv.setMad_zje(_Madzje);
        bsv.setMad_zdj(CustomMaths.Round_new(CustomMaths.div(_Madzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Aarzje = CustomMaths.add((double) CustomMaths.Round_new(_Aar * _Jiessl, 2), bsv.getAar_zje_tscl());
        bsv.setAar_zje(_Aarzje);
        bsv.setAar_zdj(CustomMaths.Round_new(CustomMaths.div(_Aarzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Aadzje = CustomMaths.add((double) CustomMaths.Round_new(_Aad * _Jiessl, 2), bsv.getAad_zje_tscl());
        bsv.setAad_zje(_Aadzje);
        bsv.setAad_zdj(CustomMaths.Round_new(CustomMaths.div(_Aadzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Vadzje = CustomMaths.add((double) CustomMaths.Round_new(_Vad * _Jiessl, 2), bsv.getVad_zje_tscl());
        bsv.setVad_zje(_Vadzje);
        bsv.setVad_zdj(CustomMaths.Round_new(CustomMaths.div(_Vadzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _T2zje = CustomMaths.add((double) CustomMaths.Round_new(_T2 * _Jiessl, 2), bsv.getT2_zje_tscl());
        bsv.setT2_zje(_T2zje);
        bsv.setT2_zdj(CustomMaths.Round_new(CustomMaths.div(_T2zje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Yunjuzje = CustomMaths.add((double) CustomMaths.Round_new(_Yunju * bsv.getJiessl(), 2), bsv.getYunju_zje_tscl());            //运距折金额
        bsv.setYunju_zje(_Yunjuzje);
        bsv.setYunju_zdj(CustomMaths.Round_new(CustomMaths.div(_Yunjuzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Shulzje = (double) CustomMaths.Round_new(_Shulzjbz * bsv.getYingksl(), 2);    //超过矿发量的盈亏
        bsv.setShul_zje(_Shulzje);
        bsv.setShul_zdj(CustomMaths.Round_new(CustomMaths.div(_Shulzje, _Jiessl), bsv.getMeikzkkblxsw()));

//		_Shulzje=(double)Math.round(_Shul*_Jiessl*100/100);		记录超过合同标准的按吨奖励的算法

        //结算单显示时指标折金额项
        bsv.setJiashj(_Jiashj);
        bsv.setJiakhj(_Jiakhj);
        bsv.setJiaksk(_Jiaksk);
        bsv.setJine(_Jine);
        bsv.setBuhsmj(_Buhsmj);
        bsv.setShulzjbz(_Shulzjbz);
//		bsv.setYunfsk(_Yunfsk);
//		bsv.setBuhsyf(_Buhsyf);
//		bsv.setYunzfhj(_Yunzfhj);
//		bsv.setYunfsk(_Yunfsk);
//		bsv.setBuhsyf(_Buhsyf);
        bsv.setHej(_Hej);

        JDBCcon con = new JDBCcon();
        StringBuffer sql = new StringBuffer("begin	\n");

        bsv.setXuh(bsv.getXuh() + 1);
        if (bsv.getMeikjsb_id() == 0) {

            bsv.setMeikjsb_id(Long.parseLong(MainGlobal.getNewID(bsv.getDiancxxb_id())));
        }

        sql.append(Jiesdcz.InsertDanpcjsmkb(bsv, this.getZhibModel(), "MK", selIds));

        sql.append("end;");

        if (sql.length() > 13) {

            con.getInsert(sql.toString());
        }
        sql.setLength(0);

        con.Close();
    }

    //	计算费用单批次
    private void computData_Dpc(String selIds, long hetb_id, double shangcjsl, String lie_id) {
        //计算煤价,热量折价,硫折价,灰熔点折价
        //煤款
        JDBCcon con = new JDBCcon();
        StringBuffer sql = new StringBuffer("begin 	\n");

        double _Hansmj = bsv.getHansmj();
        double _Jiessl = bsv.getJiessl();
        double _Meiksl = bsv.getMeiksl();

        //指标折单价
        double _Qnetar = bsv.getQnetar_zdj();
        double _Std = bsv.getStd_zdj();
        double _Ad = bsv.getAd_zdj();
        double _Vdaf = bsv.getVdaf_zdj();
        double _Mt = bsv.getMt_zdj();
        double _Qgrad = bsv.getQgrad_zdj();
        double _Qbad = bsv.getQbad_zdj();
        double _Had = bsv.getHad_zdj();
        double _Stad = bsv.getStad_zdj();
        double _Star = bsv.getStar_zdj();
        double _Mad = bsv.getMad_zdj();
        double _Aar = bsv.getAar_zdj();
        double _Aad = bsv.getAad_zdj();
        double _Vad = bsv.getVad_zdj();
        double _T2 = bsv.getT2_zdj();
        double _Shulzb = bsv.getShul_zdj();
        double _Yunju = bsv.getYunju_zdj();
        //指标盈亏
        double _Shulyk = bsv.getShul_yk();                                //执行合同中的超吨奖励用

        double _Jiashj = 0;
        double _Jiakhj = 0;
        double _Jiaksk = 0;
        double _Jine = 0;
        double _Buhsmj = 0;
        double _Shulzjbz = 0;
//		double _Yunfsk=0;
//		double _Yunzfhj=0;
//		double _Buhsyf=0;
        double _Hej = 0;

        //指标折金额
        double _Qnetarzje = 0;
        double _Stdzje = 0;
        double _Adzje = 0;
        double _Vdafzje = 0;
        double _Mtzje = 0;
        double _Qgradzje = 0;
        double _Qbadzje = 0;
        double _Hadzje = 0;
        double _Stadzje = 0;
        double _Starzje = 0;
        double _Madzje = 0;
        double _Aarzje = 0;
        double _Aadzje = 0;
        double _Vadzje = 0;
        double _T2zje = 0;
        double _Shulzje = 0;
        double _Shulzbzje = 0;
        double _Yunjuzje = 0;

        Danjsmk_dcjcl(1, selIds, hetb_id, 0);

        //价格金额计算
//		2008-12-9zsj加：
//		逻辑：	如果合同价格的结算方式是“到厂价格”，且合同价格中还包含含税运费单价，
//		则我们认为是一下两种情况的一种：
//			情况一：

//				含税总煤款=到厂价×煤款结算数量-运费含税单价×运费结算数量
//				含税总运费=运费含税单价×运费结算数量
//				处理方法：在结算设置里增加“一口价(运费源于煤款)”的设置，默认值“否”，如果值为“是”
//						则按照此情况处理。
//			情况二：

//				煤款含税单价=计算出的煤款含税单价-合同价格中的含税运费单价
//				含税运费单价=合同价格中的含税运费单价
//				同时要更新Hansmj
//				处理方法：系统默认，即如果合同价格的结算方式是“到厂价格”，且合同价格中还包含含税运费单价，
//						且结算设置中“一口价(运费源于煤款)”值为“否”，则按照此情况处理。

        if ("西部特殊结算".equals(MainGlobal.getXitxx_item("结算", "西部特殊结算", "0", ""))) {

            _Hansmj -= bsv.getShul_zdj();
            //bsv.getShul_zdj();

            ResultSetList rsl1 = con.getResultSetList("SELECT * FROM hetb WHERE guoqrq>=DATE'2012-09-24' and id=" + bsv.getHetb_Id());
            if (rsl1.getRows() > 0) {
//				判断是否扣款时改为取样本容量而不是净重
//				增付价仍以结算数量为准
//				于2013-11-29根据电厂要求调整为不分煤种进行数量加价考核
                String sql1 = "select sum(maoz-piz) yangbrl, sum(decode(pinzb_id,2018237,jingz,jingz)) from fahb f where f.lie_id in(" + selIds + ")";
                rsl1 = con.getResultSetList(sql1);
                double sl_all = 0;
                double sl_cym = 0;
                if (rsl1.next()) {
                    sl_all = rsl1.getDouble(0);
                    sl_cym = rsl1.getDouble(1);
                }

                //所有煤种数量
                rsl1 = con.getResultSetList(
                        "select kouj,zengfj,shangx,xiax,canzxx,pinzb_id from hetzkkb where zhibb_id=1 and hetb_id=" + bsv.getHetb_Id() + " order by xiax desc");
                while (rsl1.next()) {
                    if (bsv.getRanlpzb_Id() == rsl1.getLong("pinzb_id")) {
                        //原煤直接加价
                        _Hansmj += rsl1.getDouble("zengfj");
                    } else {
                        //小于下限直接扣价（所有煤种）
                        if (sl_all < rsl1.getDouble("shangx") && rsl1.getDouble("xiax") == 0) {
                            _Hansmj -= rsl1.getDouble("kouj");
                            continue;
                        }
                        //大于上限直接增价（除原煤的其它煤种）
//						于2013-11-29根据电厂要求调整为不分煤种进行数量加价考核，取消&& 2018237 != this.bsv.getRanlpzb_Id()
                        if ((sl_cym >= rsl1.getDouble("xiax") && sl_cym < rsl1.getDouble("shangx")) || (sl_cym >= rsl1.getDouble("xiax") && 0 == rsl1.getDouble("shangx") && 0 != rsl1.getDouble("xiax"))) {
                            if (bsv.getShul_xb_dongt_static_value() >= rsl1.getDouble("xiax") && (CustomMaths.Round_new(this.bsv.getQnetar_js() / 4.1816 * 1000, 0) >= rsl1.getDouble("canzxx"))) {
                                _Hansmj += rsl1.getDouble("zengfj");
                            }
                        }
                    }
                }
                if (_Hansmj < 0) {
                    _Hansmj = 0;
                }

                bsv.setHansmj(_Hansmj);
            }
        }


        if (bsv.getJijlx() == 0) {
//								含税单价

//							if(Meikzkktzsj!=null){
////								说明有部分享受加价的情况
////								1、超出部分对应的折单价
////								2、超出部分折金额
////								(hansmj-超出部分的折价)*结算数量+超出部分折金额
//
//							}

            bsv.setJiajqdj(_Hansmj);
            if (bsv.getYunzfhj() > 0 && bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)) {
//					需要重算含税单价(到厂价含运费)
                _Jiashj = (double) CustomMaths.sub((double) getRound_xz(_Hansmj * _Jiessl, 2), bsv.getYunzfhj());        //价税合计
                _Hansmj = (double) getRound_xz(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                bsv.setJiajqdj(_Hansmj);
            }
            if (bsv.getJiesfs().equals(Locale.pingcjg_ht_jsfs) || bsv.getJiesfs().equals(Locale.chebjg_ht_jsfs)) { // 平仓价格 或 车板价格
//					保存加价前单价
                _Hansmj = _Hansmj + bsv.getFengsjj();                                //加上分公司加价
                bsv.setHansmj(_Hansmj);                                            //更新含税单价
                _Jiashj = (double) getRound_xz(_Hansmj * _Jiessl, 2);        //价税合计
                _Jiakhj = (double) getRound_xz((_Jiashj) / (1 + _Meiksl), 2);    //价款合计
                _Jiaksk = (double) getRound_xz((_Jiashj - _Jiakhj), 2);        //价款税款
                _Jine = _Jiakhj;                                                    //金额
                _Buhsmj = (double) getRound_xz(_Jiakhj / _Jiessl, 7);        //不含税单价
            } else {
//					保存加价前单价
                _Hansmj = _Hansmj + bsv.getFengsjj();                                //加上分公司加价
                bsv.setHansmj(_Hansmj);                                            //更新含税单价
                _Jiashj = (double) getRound_xz(_Hansmj * _Jiessl, 2);        //价税合计
                _Jiakhj = (double) getRound_xz((_Jiashj) / (1 + _Meiksl), 2);    //价款合计
                _Jiaksk = (double) getRound_xz((_Jiashj - _Jiakhj), 2);        //价款税款
                _Jine = _Jiakhj;                                                    //金额
                _Buhsmj = (double) getRound_xz(_Jiakhj / _Jiessl, 7);        //不含税单价
            }

        } else if (bsv.getJijlx() == 1) {
//								基价类型（0、含税；1、不含税）
//								不含税
            _Buhsmj = _Hansmj;
            _Jiakhj = (double) getRound_xz(_Buhsmj * _Jiessl, 2);
//				计算加价前含税单价
            _Jiashj = (double) getRound_xz(_Jiakhj * (1 + _Meiksl), 2);

            if (bsv.getYunzfhj() > 0 && bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)) {

                _Jiashj = (double) CustomMaths.sub(_Jiashj, bsv.getYunzfhj());
            }

            if (bsv.getJiesfs().equals(Locale.pingcjg_ht_jsfs) || bsv.getJiesfs().equals(Locale.chebjg_ht_jsfs)) { // 平仓价格 或 车板价格
                _Hansmj = (double) getRound_xz(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                bsv.setJiajqdj(_Hansmj);
//					计算加价前含税单价_end

                _Jiashj = (double) getRound_xz((double) getRound_xz(_Jiakhj * (1 + _Meiksl), 2)
                        + (double) getRound_xz(bsv.getFengsjj() * bsv.getJiessl(), 2), 2);    //处理分公司加价
                _Jiakhj = (double) getRound_xz((_Jiashj) / (1 + _Meiksl), 2);
                _Jine = _Jiakhj;
                _Jiaksk = (double) getRound_xz((_Jiashj - _Jiakhj), 2);
                _Hansmj = (double) getRound_xz(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                bsv.setHansmj(_Hansmj);
                _Buhsmj = (double) getRound_xz(_Jiakhj / _Jiessl, 7);        //不含税单价
            } else {
                _Hansmj = (double) getRound_xz(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                bsv.setJiajqdj(_Hansmj);
//					计算加价前含税单价_end

                _Jiashj = (double) getRound_xz((double) getRound_xz(_Jiakhj * (1 + _Meiksl), 2)
                        + (double) getRound_xz(bsv.getFengsjj() * bsv.getJiessl(), 2), 2);    //处理分公司加价
                _Jiakhj = (double) getRound_xz((_Jiashj) / (1 + _Meiksl), 2);
                _Jine = _Jiakhj;
                _Jiaksk = (double) getRound_xz((_Jiashj - _Jiakhj), 2);
                _Hansmj = (double) getRound_xz(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                bsv.setHansmj(_Hansmj);
                _Buhsmj = (double) getRound_xz(_Jiakhj / _Jiessl, 7);        //不含税单价
            }

        }

        _Shulzjbz = _Hansmj;
        //合计
        _Hej = (double) getRound_xz((_Jiashj), 2);

        //计算盈亏，折价金额
        _Qnetarzje = (double) CustomMaths.Round_new(_Qnetar * _Jiessl, 2);
        _Stdzje = (double) CustomMaths.Round_new(_Std * _Jiessl, 2);
        _Adzje = (double) CustomMaths.Round_new(_Ad * _Jiessl, 2);
        _Vdafzje = (double) CustomMaths.Round_new(_Vdaf * _Jiessl, 2);
        _Mtzje = (double) CustomMaths.Round_new(_Mt * _Jiessl, 2);
        _Qgradzje = (double) CustomMaths.Round_new(_Qgrad * _Jiessl, 2);
        _Qbadzje = (double) CustomMaths.Round_new(_Qbad * _Jiessl, 2);
        _Hadzje = (double) CustomMaths.Round_new(_Had * _Jiessl, 2);
        _Stadzje = (double) CustomMaths.Round_new(_Stad * _Jiessl, 2);
        _Starzje = (double) CustomMaths.Round_new(_Star * _Jiessl, 2);
        _Madzje = (double) CustomMaths.Round_new(_Mad * _Jiessl, 2);
        _Aarzje = (double) CustomMaths.Round_new(_Aar * _Jiessl, 2);
        _Aadzje = (double) CustomMaths.Round_new(_Aad * _Jiessl, 2);
        _Vadzje = (double) CustomMaths.Round_new(_Vad * _Jiessl, 2);
        _T2zje = (double) CustomMaths.Round_new(_T2 * _Jiessl, 2);
        _Yunjuzje = (double) CustomMaths.Round_new(_Yunju * _Jiessl, 2);    //运距折金额
        _Shulzbzje = (double) CustomMaths.Round_new(_Shulzb * _Jiessl, 2);        //记录超过合同标准的按吨奖励的算法
        _Shulzje = (double) CustomMaths.Round_new(_Shulzjbz * bsv.getYingksl(), 2);    //超过狂发量的盈亏

        //结算单显示时指标折金额项
        bsv.setShulzjbz(_Shulzjbz);
        bsv.setShulzjje(_Shulzje);
        bsv.setJiashj(_Jiashj);
        bsv.setJiakhj(_Jiakhj);
        bsv.setJiaksk(_Jiaksk);
        bsv.setJine(_Jine);
        bsv.setHej(_Hej);

        bsv.setShulzb_zje(_Shulzbzje);
        bsv.setQnetar_zje(_Qnetarzje);
        bsv.setStd_zje(_Stdzje);
        bsv.setAd_zje(_Adzje);
        bsv.setVdaf_zje(_Vdafzje);
        bsv.setMt_zje(_Mtzje);
        bsv.setQgrad_zje(_Qgradzje);
        bsv.setQbad_zje(_Qbadzje);
        bsv.setHad_zje(_Hadzje);
        bsv.setStad_zje(_Stadzje);
        bsv.setMad_zje(_Madzje);
        bsv.setAar_zje(_Aarzje);
        bsv.setAad_zje(_Aadzje);
        bsv.setVad_zje(_Vadzje);
        bsv.setT2_zje(_T2);
        bsv.setStar_zje(_Starzje);
        bsv.setYunju_zje(_Yunjuzje);

        bsv.setXuh(bsv.getXuh() + 1);

        if (bsv.getMeikjsb_id() == 0) {

            bsv.setMeikjsb_id(Long.parseLong(MainGlobal.getNewID(bsv.getDiancxxb_id())));
        }

        sql.append(Jiesdcz.InsertDanpcjsmkb(bsv, this.getZhibModel(), "MK", lie_id));

        sql.append("end;");

        if (sql.length() > 13) {

            con.getInsert(sql.toString());
        }
        sql.setLength(0);

        con.Close();
    }

    private void computData_Yf(String lie_id) {
        //计算运费结算后的折单价

        double _Hansmj = bsv.getHansmj();
        double _Jiessl = bsv.getJiessl();    //煤款结算数量
        //指标折金额
        double _Qnetarzje = 0;
        double _Stdzje = 0;
        double _Adzje = 0;
        double _Vdafzje = 0;
        double _Mtzje = 0;
        double _Qgradzje = 0;
        double _Qbadzje = 0;
        double _Hadzje = 0;
        double _Stadzje = 0;
        double _Madzje = 0;
        double _Aarzje = 0;
        double _Aadzje = 0;
        double _Vadzje = 0;
        double _T2zje = 0;
        double _Shulzbzje = 0;
        double _Shulzje = 0;
        double _Yunjuzje = 0;
        double _Starzje = 0;
        double _Shulzjbz = 0;
        //指标折单价
        double _Qnetar = bsv.getQnetar_zdj();
        double _Std = bsv.getStd_zdj();
        double _Ad = bsv.getAd_zdj();
        double _Vdaf = bsv.getVdaf_zdj();
        double _Mt = bsv.getMt_zdj();
        double _Qgrad = bsv.getQgrad_zdj();
        double _Qbad = bsv.getQbad_zdj();
        double _Had = bsv.getHad_zdj();
        double _Stad = bsv.getStad_zdj();
        double _Mad = bsv.getMad_zdj();
        double _Aar = bsv.getAar_zdj();
        double _Aad = bsv.getAad_zdj();
        double _Vad = bsv.getVad_zdj();
        double _T2 = bsv.getT2_zdj();
        double _Shul = bsv.getShul_zdj();
        double _Yunju = bsv.getYunju_zdj();        //运距折单价
        double _Star = bsv.getStar_zdj();            //Star折单价

        //计算盈亏，折价金额

        _Shulzbzje = CustomMaths.add((double) CustomMaths.Round_new(_Shul * _Jiessl, 2), bsv.getShul_zje_tscl());    //超过狂发量的盈亏
        bsv.setShulzb_zje(_Shulzbzje);
        bsv.setShulzb_zdj(CustomMaths.Round_new(CustomMaths.div(_Shulzbzje, _Jiessl), bsv.getMeikzkkblxsw()));

        bsv.setQnetar_yk(Jiesdcz.reCoundYk(MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_ht(), "-", 0),
                MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_js(), 0, bsv.getMj_to_kcal_xsclfs()), bsv.getQnetar_yk()));
        _Qnetarzje = CustomMaths.add((double) CustomMaths.Round_new(_Qnetar * _Jiessl, 2), bsv.getQnetar_zje_tscl());
        bsv.setQnetar_zje(_Qnetarzje);
        bsv.setQnetar_zdj(CustomMaths.Round_new(CustomMaths.div(_Qnetarzje, _Jiessl), bsv.getMeikzkkblxsw()));

        bsv.setStd_yk(Jiesdcz.reCoundYk(bsv.getStd_ht(), bsv.getStd_js(), bsv.getStd_yk()));
        _Stdzje = CustomMaths.add((double) CustomMaths.Round_new(_Std * _Jiessl, 2), bsv.getStd_zje_tscl());
        bsv.setStd_zje(_Stdzje);
        bsv.setStd_zdj(CustomMaths.Round_new(CustomMaths.div(_Stdzje, _Jiessl), bsv.getMeikzkkblxsw()));

        bsv.setAd_yk(Jiesdcz.reCoundYk(bsv.getAd_ht(), bsv.getAd_js(), bsv.getAd_yk()));
        _Adzje = CustomMaths.add((double) CustomMaths.Round_new(_Ad * _Jiessl, 2), bsv.getAd_zje_tscl());
        bsv.setAd_zje(_Adzje);
        bsv.setAd_zdj(CustomMaths.Round_new(CustomMaths.div(_Adzje, _Jiessl), bsv.getMeikzkkblxsw()));

        _Vdafzje = CustomMaths.add((double) CustomMaths.Round_new(_Vdaf * _Jiessl, 2), bsv.getVdaf_zje_tscl());
        bsv.setVdaf_zje(_Vdafzje);
        bsv.setVdaf_zdj(CustomMaths.Round_new(CustomMaths.div(_Vdafzje, _Jiessl), bsv.getMeikzkkblxsw()));

        _Mtzje = CustomMaths.add((double) CustomMaths.Round_new(_Mt * _Jiessl, 2), bsv.getMt_zje_tscl());
        bsv.setMt_zje(_Mtzje);
        bsv.setMt_zdj(CustomMaths.Round_new(CustomMaths.div(_Mtzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Qgradzje = CustomMaths.add((double) CustomMaths.Round_new(_Qgrad * _Jiessl, 2), bsv.getQgrad_zje_tscl());
        bsv.setQgrad_zje(_Qgradzje);
        bsv.setQgrad_zdj(CustomMaths.Round_new(CustomMaths.div(_Qgradzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Qbadzje = CustomMaths.add((double) CustomMaths.Round_new(_Qbad * _Jiessl, 2), bsv.getQbad_zje_tscl());
        bsv.setQbad_zje(_Qbadzje);
        bsv.setQbad_zdj(CustomMaths.Round_new(CustomMaths.div(_Qbadzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Hadzje = CustomMaths.add((double) CustomMaths.Round_new(_Had * _Jiessl, 2), bsv.getHad_zje_tscl());
        bsv.setHad_zje(_Hadzje);
        bsv.setHad_zdj(CustomMaths.Round_new(CustomMaths.div(_Hadzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Stadzje = CustomMaths.add((double) CustomMaths.Round_new(_Stad * _Jiessl, 2), bsv.getStad_zje_tscl());
        bsv.setStad_zje(_Stadzje);
        bsv.setStad_zdj(CustomMaths.Round_new(CustomMaths.div(_Stadzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Starzje = CustomMaths.add((double) CustomMaths.Round_new(_Star * _Jiessl, 2), bsv.getStar_zje_tscl());
        bsv.setStar_zje(_Starzje);
        bsv.setStar_zdj(CustomMaths.Round_new(CustomMaths.div(_Starzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Madzje = CustomMaths.add((double) CustomMaths.Round_new(_Mad * _Jiessl, 2), bsv.getMad_zje_tscl());
        bsv.setMad_zje(_Madzje);
        bsv.setMad_zdj(CustomMaths.Round_new(CustomMaths.div(_Madzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Aarzje = CustomMaths.add((double) CustomMaths.Round_new(_Aar * _Jiessl, 2), bsv.getAar_zje_tscl());
        bsv.setAar_zje(_Aarzje);
        bsv.setAar_zdj(CustomMaths.Round_new(CustomMaths.div(_Aarzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Aadzje = CustomMaths.add((double) CustomMaths.Round_new(_Aad * _Jiessl, 2), bsv.getAad_zje_tscl());
        bsv.setAad_zje(_Aadzje);
        bsv.setAad_zdj(CustomMaths.Round_new(CustomMaths.div(_Aadzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Vadzje = CustomMaths.add((double) CustomMaths.Round_new(_Vad * _Jiessl, 2), bsv.getVad_zje_tscl());
        bsv.setVad_zje(_Vadzje);
        bsv.setVad_zdj(CustomMaths.Round_new(CustomMaths.div(_Vadzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _T2zje = CustomMaths.add((double) CustomMaths.Round_new(_T2 * _Jiessl, 2), bsv.getT2_zje_tscl());
        bsv.setT2_zje(_T2zje);
        bsv.setT2_zdj(CustomMaths.Round_new(CustomMaths.div(_T2zje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Yunjuzje = CustomMaths.add((double) CustomMaths.Round_new(_Yunju * bsv.getJiessl(), 2), bsv.getYunju_zje_tscl());            //运距折金额
        bsv.setYunju_zje(_Yunjuzje);
        bsv.setYunju_zdj(CustomMaths.Round_new(CustomMaths.div(_Yunjuzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Shulzje = (double) CustomMaths.Round_new(_Shulzjbz * bsv.getYingksl(), 2);    //超过狂发量的盈亏
        bsv.setShul_zje(_Shulzje);
        bsv.setShul_zdj(CustomMaths.Round_new(CustomMaths.div(_Shulzje, _Jiessl), bsv.getMeikzkkblxsw()));

        //结算单显示时指标折金额项
        bsv.setShulzjbz(_Shulzjbz);

        if (!bsv.getYunfjsdpcfztj().equals("")) {
//			如果运费不是单批次结算的，则不存danpcjsmxb;
//			只有运费需要单批次计算时才将单批次结算的值存入danpcjsmxb

            JDBCcon con = new JDBCcon();
            StringBuffer sql = new StringBuffer("begin 	\n");

            bsv.setXuh_yf(bsv.getXuh_yf() + 1);

            if (bsv.getYunfjsb_id() == 0) {

                bsv.setYunfjsb_id(Long.parseLong(MainGlobal.getNewID(bsv.getDiancxxb_id())));
            }

            computYunfAndHej();

            if (bsv.getYunju_ht().equals("")) {

//				考虑到一定要插入单批次结算记录，如果运距指标为空就对其进行赋值
                bsv.setYunju_ht("0");
            }

//			如果运费不是单批次结算的，也存danpcjsmxb一条记录
            sql.append(Jiesdcz.InsertDanpcjsmkb(bsv, this.getZhibModel(), "YF", lie_id));

            sql.append("end;");

            if (sql.length() > 13) {

                con.getInsert(sql.toString());
            }
            sql.setLength(0);
        }
    }

    private void CountZonghzkk() {

//		函数功能：
//				为了处理合同价格中的结算方式为“加权平均”、增扣款中“结算数量”指标的结算方式为“单批次”时，
//			结算时的逻辑应为先计算单批次的逻辑，在最后reCount 方法中，再计算“结算数量”指标对全部结算数量的增扣款信息

//		函数逻辑：
//				首先判断有没有这种业务情形，如果是这样：
//				1、处理逻辑类似于getMeiPrice 方法中加权平均结算逻辑
//				2、调用增扣款信息得到结算数量的折单价信息，存入danpcjsmxb中，xuh为0

//		函数形参：
//			bsv Balances_variable型变量 , jiessl 要作为加数, Type:0		煤款结算；1	运费结算

        if (bsv.getJiesxs().equals(Locale.danpc_jiesxs)) {

            JDBCcon con = new JDBCcon();
            StringBuffer sql = new StringBuffer();
            ResultSetList rsl = null;
            Interpreter bsh = new Interpreter();

            try {

                sql.append("select distinct zbb.bianm as zhib \n" +
                        "        from hetb htb, hetzkkb htzkk,zhibb zbb,hetjsxsb xs\n" +
                        "        where htb.id=htzkk.hetb_id\n" +
                        "              and htzkk.zhibb_id=zbb.id\n" +
                        "              and htzkk.hetjsxsb_id=xs.id(+)\n" +
                        "              and (xs.bianm='" + Locale.jiaqpj_jiesxs + "' or xs.bianm='" + Locale.jiaqfl_jiesxs + "') \n" +
                        "              and (zbb.leib=1 or zbb.leib=3)\n" +
                        "              and htb.id=" + bsv.getHetb_Id() + "");

                rsl = con.getResultSetList(sql.toString());
                if (rsl.getRows() > 0) {
                    //			说明有记录
                    //			定义StringBuffer 用来存储要特殊处理的指标
                    StringBuffer sb = new StringBuffer("");
                    while (rsl.next()) {

                        sb.append(rsl.getString("zhib")).append(",");
                    }

                    if (sb.indexOf(Locale.jiessl_zhibb) > -1) {
                        //					说明有结算数量
                        sql.setLength(0);

                        if (bsv.getMeikjsb_id() == 0) {

                            bsv.setMeikjsb_id(Long.parseLong(MainGlobal.getNewID(bsv.getDiancxxb_id())));
                        }

                        bsv.setXuh(0);

//						初始化结算其它指标
                        getJiesszl(bsv.getJieszbsftz(), bsv.getSelIds(), bsv.getDiancxxb_id(), bsv.getGongysb_Id(), bsv.getHetb_Id(),
                                bsv.getJieskdl(), bsv.getYunsdwb_id(), bsv.getJieslx(), bsv.getShangcjsl(), "");

                        sql.append(
                                "select sum(jiessl) as jiessl from\n" +
                                        "	(select xuh,\n" +
                                        "        max(mx.jiessl) as jiessl\n" +
                                        "        from danpcjsmxb mx\n" +
                                        "        where leib=1 and zhekfs = 0 and jiesdid=" + bsv.getMeikjsb_id() + "\n" +
                                        "        group by xuh)");

                        rsl = con.getResultSetList(sql.toString());
                        while (rsl.next()) {

                            bsv.setJiessl(rsl.getDouble("jiessl"));

                            bsv.setJiessl(CustomMaths.add(bsv.getJiessl(), bsv.getShangcjsl()));
                        }

                        String[] tmp = new String[6];
                        tmp[0] = "0";
                        tmp[1] = Locale.jiessl_zhibb;
                        tmp[2] = "meikxxb_id";
                        tmp[3] = "-1";
                        tmp[4] = String.valueOf(bsv.getJiessl());
                        tmp[5] = "0";

                        bsv.setTsclzbs(new String[1]);
                        bsv.getTsclzbs()[0] = "0, " + Locale.jiessl_zhibb + ", meikxxb_id, 0, " + String.valueOf(bsv.getJiessl()) + ", 0";

//						0,运距,meikxxb_id,100,10,0;

                        bsh.set("结算形式", bsv.getJiesxs());
                        bsh.set("计价方式", bsv.getHetjjfs());
                        bsh.set("价格单位", bsv.getHetmdjdw());
                        bsh.set("合同价格", bsv.getHetmdj());
                        bsh.set("最高煤价", bsv.getZuigmj());
                        bsh.set("合同价格公式", bsv.getHetmdjgs());

                        double Dbltmp = Jiesdcz.getZhib_info(bsv, Locale.jiessl_zhibb, "js");

                        Dbltmp = Jiesdcz.getUnit_transform(Locale.jiessl_zhibb, Locale.dun_danw, Dbltmp, bsv.getMj_to_kcal_xsclfs());

                        bsh.set(Locale.jiessl_zhibb + Locale.dun_danw, Dbltmp);    //结算值
                        bsh.set(rsl.getString("zhib") + "计量单位", Locale.dun_danw);                                //指标单位

                        bsv.setYifzzb(Locale.jiessl_zhibb);    //默认的已赋值指标

                        bsh.set(rsl.getString("zhib") + "上限", 0);        //指标上限
                        bsh.set(rsl.getString("zhib") + "下限", 0);            //指标下限

                        bsh.set(rsl.getString("zhib") + "增付单价", 0);
                        bsh.set(rsl.getString("zhib") + "增付单价公式", "");
                        bsh.set(rsl.getString("zhib") + "扣付单价", 0);
                        bsh.set(rsl.getString("zhib") + "扣付单价公式", "");
                        bsh.set(rsl.getString("zhib") + "增付价单位", "");
                        bsh.set(rsl.getString("zhib") + "增扣款条件", "");
                        bsh.set(rsl.getString("zhib") + "增扣款基数", 0);
                        bsh.set(rsl.getString("zhib") + "增扣款基数单位", "");
                        bsh.set(rsl.getString("zhib") + "基准增扣价", 0);
                        bsh.set(rsl.getString("zhib") + "小数处理", "");

//						获得增扣款
                        getZengkk(bsv.getHetb_Id(), bsh, false, tmp);

//						用户自定义公式
                        bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());

//						煤款含税单价保留小数位
                        bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//						含税单价取整方式
                        bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//						增扣款保留小数位
                        bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//						执行合同价格公式
                        ExecuteHetmdjgs(bsh);

//						执行公式
                        bsh.eval(bsv.getGongs_Mk());

                        Jiesdcz.SetZhib_het_clear(bsv);    //清空所有指标的合同标准值

                        bsv.setShul_ht(Jiesdcz.Regular_Ht(bsh.get("合同标准_结算数量").toString()));
                        bsv.setShul_yk(Double.parseDouble(bsh.get("盈亏_结算数量").toString()));
                        bsv.setShul_zdj(Double.parseDouble(bsh.get("折单价_结算数量").toString()));
                        bsv.setShul_zsl(Double.parseDouble(bsh.get("折数量_结算数量").toString()));
                        bsv.setShul_zsldw(bsh.get("折数量单位_结算数量").toString());


                        if (bsh.get(Locale.jiessl_zhibb + "计量单位").equals(Locale.wandun_danw)) {
//							因为 结算取值出来的结算数量单位为“吨”，故如果增扣款中的数量单位为“万吨” 在最后复制时要进行转换
//							Double.parseDouble(value.substring(value.indexOf("-")+1))
                            if (bsv.getShul_ht().indexOf("-") > -1) {
//								说明有连接符

                                String shulht = "";

                                shulht = String.valueOf(CustomMaths.mul(Double.parseDouble(bsv.getShul_ht().substring(0, bsv.getShul_ht().indexOf("-"))), 10000));

                                shulht = shulht + "-" + String.valueOf(CustomMaths.mul(Double.parseDouble(bsv.getShul_ht().substring(bsv.getShul_ht().indexOf("-") + 1)), 10000));

                                bsv.setShul_ht(shulht);

                            } else {

                                bsv.setShul_ht(String.valueOf(CustomMaths.mul(Double.parseDouble(bsv.getShul_ht()), 10000)));
                            }

                            bsv.setShul_yk(CustomMaths.mul(bsv.getShul_yk(), 10000));
                        }


                        double _Shulzb = bsv.getShul_zdj();        //数量指标折单价
                        double _Shulzbzje = CustomMaths.add((double) CustomMaths.Round_new(_Shulzb * bsv.getJiessl(), 2), bsv.getShul_zje_tscl());

                        bsv.setShulzb_zdj(_Shulzb);    //数量指标折单价
                        bsv.setShulzb_zje(_Shulzbzje);    //数量指标折金额


                        sql.setLength(0);

                        sql.append("begin	\n");
                        sql.append(Jiesdcz.InsertDanpcjsmkb(bsv, this.getZhibModel(), "MK", bsv.getSelIds()));
                        sql.append("end;");
                        if (sql.length() > 13) {

                            con.getUpdate(sql.toString());
                        }
                    }
                }

            } catch (Exception e) {

                e.printStackTrace();
            }

            if (rsl != null) {

                rsl.close();
            }
            con.Close();
        }
    }

    private void reCount() {
//		根据danpcjsmxb 给最后的单批次总结算单赋值。注：加权
        JDBCcon con = new JDBCcon();
        try {

//			得到所有折价的结算指标信息
            String strSql =
                    "select zb.bianm,\n" +
                            "   max(mx.hetbz) as hetbz,\n" +
                            "   case when zb.bianm='" + Locale.Shul_zhibb + "' then sum(gongf)\n" +
                            "        else\n" +
                            "             case when zb.bianm='" + Locale.Qnetar_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.gongf))/sum(decode(jiessl,0,1,jiessl)),0)\n" +
                            "                  else\n" +
                            "                       case when zb.bianm='" + Locale.Mt_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.gongf))/sum(decode(jiessl,0,1,jiessl)),1)\n" +
                            "                            else\n" +
                            "                                 round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.gongf))/sum(decode(jiessl,0,1,jiessl)),2)\n" +
                            "                       end\n" +
                            "              end\n" +
                            "   end as gongf,\n" +
                            "   case when zb.bianm='" + Locale.Shul_zhibb + "' then sum(changf)\n" +
                            "        else\n" +
                            "             case when zb.bianm='" + Locale.Qnetar_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.changf))/sum(decode(jiessl,0,1,jiessl)),0)\n" +
                            "                  else\n" +
                            "                       case when zb.bianm='" + Locale.Mt_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.changf))/sum(decode(jiessl,0,1,jiessl)),1)\n" +
                            "                            else\n" +
                            "                                 round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.changf))/sum(decode(jiessl,0,1,jiessl)),2)\n" +
                            "                       end\n" +
                            "             end\n" +
                            "   end as changf,\n" +
                            "   case when zb.bianm='" + Locale.Shul_zhibb + "' then sum(jies)\n" +
                            "        else\n" +
                            "             case when zb.bianm='" + Locale.Qnetar_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.jies))/sum(decode(jiessl,0,1,jiessl)),0)\n" +
                            "                  else\n" +
                            "                       case when zb.bianm='" + Locale.Mt_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.jies))/sum(decode(jiessl,0,1,jiessl)),1)\n" +
                            "                            else\n" +
                            "                                 round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.jies))/sum(decode(jiessl,0,1,jiessl))," + bsv.getDanglblxsw() + ")\n" +
                            "                       end\n" +
                            "             end\n" +
                            "   end as jies,\n" +
                            "   sum(mx.yingk) as yingk,\n" +
                            "   round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.zhejbz))/sum(decode(jiessl,0,1,jiessl))," + bsv.getDanglblxsw() + ") as zhejbz,\n" +
                            "   sum(mx.zhejje) as zhejje\n" +
                            " from danpcjsmxb mx,zhibb zb\n" +
                            " where zb.id=mx.zhibb_id and mx.leib=1 and mx.zhekfs = 0 and jiesdid=" + bsv.getMeikjsb_id() + "\n" +
                            " group by zb.bianm";

            ResultSet rs = con.getResultSet(strSql);
            while (rs.next()) {

                if (rs.getString("bianm").equals(Locale.Shul_zhibb)) {

                    bsv.setShul_ht(rs.getString("hetbz"));
                    bsv.setGongfsl(rs.getDouble("gongf"));
                    bsv.setYanssl(rs.getDouble("changf"));
                    bsv.setJiessl(rs.getDouble("jies"));
                    bsv.setShulzb_yk(rs.getDouble("yingk"));
                    bsv.setShulzb_zdj(rs.getDouble("zhejbz"));
                    bsv.setShulzb_zje(rs.getDouble("zhejje"));


                } else if (rs.getString("bianm").equals(Locale.Qnetar_zhibb)) {

                    bsv.setQnetar_ht(rs.getString("hetbz"));
                    bsv.setQnetar_kf(rs.getDouble("gongf"));
                    bsv.setQnetar_cf(rs.getDouble("changf"));
                    bsv.setQnetar_js(rs.getDouble("jies"));
                    bsv.setQnetar_yk(rs.getDouble("yingk"));
                    bsv.setQnetar_zdj(rs.getDouble("zhejbz"));
                    bsv.setQnetar_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Std_zhibb)) {

                    bsv.setStd_ht(rs.getString("hetbz"));
                    bsv.setStd_kf(rs.getDouble("gongf"));
                    bsv.setStd_cf(rs.getDouble("changf"));
                    bsv.setStd_js(rs.getDouble("jies"));
                    bsv.setStd_yk(rs.getDouble("yingk"));
                    bsv.setStd_zdj(rs.getDouble("zhejbz"));
                    bsv.setStd_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Ad_zhibb)) {

                    bsv.setAd_ht(rs.getString("hetbz"));
                    bsv.setAd_kf(rs.getDouble("gongf"));
                    bsv.setAd_cf(rs.getDouble("changf"));
                    bsv.setAd_js(rs.getDouble("jies"));
                    bsv.setAd_yk(rs.getDouble("yingk"));
                    bsv.setAd_zdj(rs.getDouble("zhejbz"));
                    bsv.setAd_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Vdaf_zhibb)) {

                    bsv.setVdaf_ht(rs.getString("hetbz"));
                    bsv.setVdaf_kf(rs.getDouble("gongf"));
                    bsv.setVdaf_cf(rs.getDouble("changf"));
                    bsv.setVdaf_js(rs.getDouble("jies"));
                    bsv.setVdaf_yk(rs.getDouble("yingk"));
                    bsv.setVdaf_zdj(rs.getDouble("zhejbz"));
                    bsv.setVdaf_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Mt_zhibb)) {

                    bsv.setMt_ht(rs.getString("hetbz"));
                    bsv.setMt_kf(rs.getDouble("gongf"));
                    bsv.setMt_cf(rs.getDouble("changf"));
                    bsv.setMt_js(rs.getDouble("jies"));
                    bsv.setMt_yk(rs.getDouble("yingk"));
                    bsv.setMt_zdj(rs.getDouble("zhejbz"));
                    bsv.setMt_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Qgrad_zhibb)) {

                    bsv.setQgrad_ht(rs.getString("hetbz"));
                    bsv.setQgrad_kf(rs.getDouble("gongf"));
                    bsv.setQgrad_cf(rs.getDouble("changf"));
                    bsv.setQgrad_js(rs.getDouble("jies"));
                    bsv.setQgrad_yk(rs.getDouble("yingk"));
                    bsv.setQgrad_zdj(rs.getDouble("zhejbz"));
                    bsv.setQgrad_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Qbad_zhibb)) {

                    bsv.setQbad_ht(rs.getString("hetbz"));
                    bsv.setQbad_kf(rs.getDouble("gongf"));
                    bsv.setQbad_cf(rs.getDouble("changf"));
                    bsv.setQbad_js(rs.getDouble("jies"));
                    bsv.setQbad_yk(rs.getDouble("yingk"));
                    bsv.setQbad_zdj(rs.getDouble("zhejbz"));
                    bsv.setQbad_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Had_zhibb)) {

                    bsv.setHad_ht(rs.getString("hetbz"));
                    bsv.setHad_kf(rs.getDouble("gongf"));
                    bsv.setHad_cf(rs.getDouble("changf"));
                    bsv.setHad_js(rs.getDouble("jies"));
                    bsv.setHad_yk(rs.getDouble("yingk"));
                    bsv.setHad_zdj(rs.getDouble("zhejbz"));
                    bsv.setHad_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Stad_zhibb)) {

                    bsv.setStad_ht(rs.getString("hetbz"));
                    bsv.setStad_kf(rs.getDouble("gongf"));
                    bsv.setStad_cf(rs.getDouble("changf"));
                    bsv.setStad_js(rs.getDouble("jies"));
                    bsv.setStad_yk(rs.getDouble("yingk"));
                    bsv.setStad_zdj(rs.getDouble("zhejbz"));
                    bsv.setStad_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Star_zhibb)) {

                    bsv.setStar_ht(rs.getString("hetbz"));
                    bsv.setStar_kf(rs.getDouble("gongf"));
                    bsv.setStar_cf(rs.getDouble("changf"));
                    bsv.setStar_js(rs.getDouble("jies"));
                    bsv.setStar_yk(rs.getDouble("yingk"));
                    bsv.setStar_zdj(rs.getDouble("zhejbz"));
                    bsv.setStar_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Mad_zhibb)) {

                    bsv.setMad_ht(rs.getString("hetbz"));
                    bsv.setMad_kf(rs.getDouble("gongf"));
                    bsv.setMad_cf(rs.getDouble("changf"));
                    bsv.setMad_js(rs.getDouble("jies"));
                    bsv.setMad_yk(rs.getDouble("yingk"));
                    bsv.setMad_zdj(rs.getDouble("zhejbz"));
                    bsv.setMad_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Aar_zhibb)) {

                    bsv.setAar_ht(rs.getString("hetbz"));
                    bsv.setAar_kf(rs.getDouble("gongf"));
                    bsv.setAar_cf(rs.getDouble("changf"));
                    bsv.setAar_js(rs.getDouble("jies"));
                    bsv.setAar_yk(rs.getDouble("yingk"));
                    bsv.setAar_zdj(rs.getDouble("zhejbz"));
                    bsv.setAar_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Aad_zhibb)) {

                    bsv.setAad_ht(rs.getString("hetbz"));
                    bsv.setAad_kf(rs.getDouble("gongf"));
                    bsv.setAad_cf(rs.getDouble("changf"));
                    bsv.setAad_js(rs.getDouble("jies"));
                    bsv.setAad_yk(rs.getDouble("yingk"));
                    bsv.setAad_zdj(rs.getDouble("zhejbz"));
                    bsv.setAad_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Vad_zhibb)) {

                    bsv.setVad_ht(rs.getString("hetbz"));
                    bsv.setVad_kf(rs.getDouble("gongf"));
                    bsv.setVad_cf(rs.getDouble("changf"));
                    bsv.setVad_js(rs.getDouble("jies"));
                    bsv.setVad_yk(rs.getDouble("yingk"));
                    bsv.setVad_zdj(rs.getDouble("zhejbz"));
                    bsv.setVad_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.T2_zhibb)) {

                    bsv.setT2_ht(rs.getString("hetbz"));
                    bsv.setT2_kf(rs.getDouble("gongf"));
                    bsv.setT2_cf(rs.getDouble("changf"));
                    bsv.setT2_js(rs.getDouble("jies"));
                    bsv.setT2_yk(rs.getDouble("yingk"));
                    bsv.setT2_zdj(rs.getDouble("zhejbz"));
                    bsv.setT2_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Yunju_zhibb)) {

                    bsv.setYunju_ht(rs.getString("hetbz"));
                    bsv.setYunju_kf(rs.getDouble("gongf"));
                    bsv.setYunju_cf(rs.getDouble("changf"));
                    bsv.setYunju_js(rs.getDouble("jies"));
                    bsv.setYunju_yk(rs.getDouble("yingk"));
                    bsv.setYunju_zdj(rs.getDouble("zhejbz"));
                    bsv.setYunju_zje(rs.getDouble("zhejje"));
                }
            }

//			结算数量、价格、金额信息
            strSql = "select sum(gongfsl) as gongfsl,sum(yanssl) as yanssl,sum(jiessl) as jiessl,sum(koud) as koud,	\n"
                    + " 	sum(kous) as kous,sum(kouz) as kouz,sum(ches) as ches,sum(jingz) as jingz,	\n"
                    + " 	sum(koud_js) as koud_js,sum(jiesslcy) as jiesslcy,sum(yuns) as yuns, sum(shulzbyk) as shulzbyk,	\n"
                    + " 	round_new(sum(decode(jiessl,0,0,jiessl*jiajqdj))/sum(decode(jiessl,0,1,jiessl))," + bsv.getMeikhsdjblxsw() + ") as jiajqdj,	\n"
                    + " 	round_new(sum(decode(jiessl,0,0,jiessl*jiesdj))/sum(decode(jiessl,0,1,jiessl))," + bsv.getMeikhsdjblxsw() + ") as jiesdj,	\n"
                    + " 	sum(jiakhj) as jiakhj,sum(jiaksk) as jiaksk,sum(jiashj) as jiashj,sum(chaokdl) as chaokdl,		\n"
                    + " 	round_new(sum(decode(jiessl,0,0,jiessl*biaomdj))/sum(decode(jiessl,0,1,jiessl)),2) as biaomdj,	\n"
                    + " 	round_new(sum(decode(jiessl,0,0,jiessl*buhsbmdj))/sum(decode(jiessl,0,1,jiessl)),2) as buhsbmdj	\n"
                    + " from 	\n"
                    + " 	(select xuh,	\n"
                    + " 		max(mx.gongfsl) as gongfsl,		\n"
                    + " 		max(mx.yanssl) as yanssl,		\n"
                    + " 		max(mx.jiessl) as jiessl,		\n"
                    + " 		max(mx.koud)  as koud,			\n"
                    + " 		max(mx.kous)  as kous,			\n"
                    + " 		max(mx.kouz)  as kouz,			\n"
                    + " 		max(mx.ches)  as ches,			\n"
                    + " 		max(mx.jingz)  as jingz,		\n"
                    + " 		max(mx.koud_js)  as koud_js,	\n"
                    + " 		max(mx.jiesslcy) as jiesslcy,	\n"
                    + " 		max(mx.yuns) as yuns,			\n"
                    + " 		max(mx.jiajqdj) as jiajqdj,		\n"
                    + " 		max(mx.jiesdj) as jiesdj,		\n"
                    + " 		max(mx.jiakhj) as jiakhj,		\n"
                    + " 		max(mx.jiaksk) as jiaksk,		\n"
                    + " 		max(mx.jiashj) as jiashj,		\n"
                    + "			max(mx.chaokdl) as chaokdl,		\n"
                    + "			max(mx.shulzbyk) as shulzbyk,	\n"
                    + " 		0 as biaomdj,		\n"
                    + " 		0 as buhsbmdj		\n"
                    + " 	from danpcjsmxb mx		\n"
                    + " 	where leib=1 and jiesdid=" + bsv.getMeikjsb_id() + "	\n"
                    + "			and xuh>0 and mx.zhekfs = 0	\n"
                    + " 	group by xuh)";
            rs = con.getResultSet(strSql);
            while (rs.next()) {

                if (rs.getDouble("jiessl") > 0) {

//					结算价格
                    bsv.setGongfsl(rs.getDouble("gongfsl"));
                    bsv.setYanssl(rs.getDouble("yanssl"));
                    bsv.setJiessl(rs.getDouble("jiessl"));
                    bsv.setKoud(rs.getDouble("koud"));
                    bsv.setKous(rs.getDouble("kous"));
                    bsv.setKouz(rs.getDouble("kouz"));
                    bsv.setChes(rs.getLong("ches"));
                    bsv.setJingz(rs.getDouble("jingz"));
                    bsv.setKoud_js(rs.getDouble("koud_js"));
                    bsv.setJiesslcy(rs.getDouble("jiesslcy"));
                    bsv.setYuns(rs.getDouble("yuns"));
                    bsv.setJiajqdj(rs.getDouble("jiajqdj"));
                    bsv.setShulzjbz(rs.getDouble("jiesdj"));
                    bsv.setHansmj(rs.getDouble("jiesdj"));
                    if (bsv.getJiajqdj() >= bsv.getHansmj()) {

                        bsv.setJiajqdj(CustomMaths.sub(bsv.getHansmj(), bsv.getFengsjj()));
                    }
                    bsv.setJiakhj(rs.getDouble("jiakhj"));
                    bsv.setJine(rs.getDouble("jiakhj"));
                    bsv.setJiaksk(rs.getDouble("jiaksk"));
                    bsv.setJiashj(rs.getDouble("jiashj"));
                    bsv.setBuhsmj((double) CustomMaths.Round_new(bsv.getJiakhj() / bsv.getJiessl(), 7));
                    bsv.setChaokdl(rs.getDouble("chaokdl"));
                    bsv.setYingksl(rs.getDouble("shulzbyk"));
                }
            }

//			煤款结算时，如果价格的结算形式为“单批次”形式，且有“结算数量”指标在增扣款中为“加权平均”结算（在danpcjsmxb中xuh=0 的为全局增扣款）
//				此时在计算完最终价格后重新价款并反推单价

            strSql =
                    "select nvl(sum(mx.zhejje),0) as zhejje\n" +
                            "          from danpcjsmxb mx\n" +
                            "         where leib = 1\n" +
                            "           and jiesdid = " + bsv.getMeikjsb_id() + " \n" +
                            "           and xuh=0 and zhekfs = 0";

            rs = con.getResultSet(strSql);
            if (rs.next()) {

                if (rs.getDouble("zhejje") != 0) {

//					单价差
                    double jiac = CustomMaths.sub(bsv.getHansmj(), bsv.getJiajqdj());

                    bsv.setJiashj(CustomMaths.Round_new(CustomMaths.add(bsv.getJiashj(), rs.getDouble("zhejje")), 2));
                    bsv.setShulzjbz(CustomMaths.Round_new(CustomMaths.div(bsv.getJiashj(), bsv.getJiessl()), bsv.getMeikhsdjblxsw()));
                    bsv.setHansmj(bsv.getShulzjbz());
                    bsv.setJiajqdj(CustomMaths.Round_new(CustomMaths.sub(bsv.getHansmj(), jiac), bsv.getMeikhsdjblxsw()));
                    bsv.setJiakhj(CustomMaths.Round_new(CustomMaths.div(bsv.getJiashj(), (1 + bsv.getMeiksl())), 2));
                    bsv.setJiaksk(CustomMaths.Round_new(CustomMaths.sub(bsv.getJiashj(), bsv.getJiakhj()), 2));
                    bsv.setJine(bsv.getJiakhj());
                    bsv.setBuhsmj(CustomMaths.Round_new(CustomMaths.div(bsv.getJiakhj(), bsv.getJiessl()), 7));
                }
            }

//			如果结算类型不等于两票结算或煤款结算。且运费为单批次结算情况，需要计算运费的折价情况
            if (bsv.getJieslx() != Locale.liangpjs_feiylbb_id
                    && bsv.getJieslx() != Locale.meikjs_feiylbb_id
                    && (!bsv.getYunfjsdpcfztj().equals(""))) {


//				计算运费的折价
                strSql = "select zb.bianm,\n" +
                        "   max(mx.hetbz) as hetbz,\n" +
                        "   case when zb.bianm='" + Locale.Shul_zhibb + "' then sum(gongf)\n" +
                        "        else\n" +
                        "             case when zb.bianm='" + Locale.Qnetar_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.gongf))/sum(decode(jiessl,0,1,jiessl)),0)\n" +
                        "                  else\n" +
                        "                       case when zb.bianm='" + Locale.Mt_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.gongf))/sum(decode(jiessl,0,1,jiessl)),1)\n" +
                        "                            else\n" +
                        "                                 round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.gongf))/sum(decode(jiessl,0,1,jiessl)),2)\n" +
                        "                       end\n" +
                        "              end\n" +
                        "   end as gongf,\n" +
                        "   case when zb.bianm='" + Locale.Shul_zhibb + "' then sum(changf)\n" +
                        "        else\n" +
                        "             case when zb.bianm='" + Locale.Qnetar_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.changf))/sum(decode(jiessl,0,1,jiessl)),0)\n" +
                        "                  else\n" +
                        "                       case when zb.bianm='" + Locale.Mt_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.changf))/sum(decode(jiessl,0,1,jiessl)),1)\n" +
                        "                            else\n" +
                        "                                 round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.changf))/sum(decode(jiessl,0,1,jiessl)),2)\n" +
                        "                       end\n" +
                        "             end\n" +
                        "   end as changf,\n" +
                        "   case when zb.bianm='" + Locale.Shul_zhibb + "' then sum(jies)\n" +
                        "        else\n" +
                        "             case when zb.bianm='" + Locale.Qnetar_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.jies))/sum(decode(jiessl,0,1,jiessl)),0)\n" +
                        "                  else\n" +
                        "                       case when zb.bianm='" + Locale.Mt_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.jies))/sum(decode(jiessl,0,1,jiessl)),1)\n" +
                        "                            else\n" +
                        "                                 round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.jies))/sum(decode(jiessl,0,1,jiessl)),2)\n" +
                        "                       end\n" +
                        "             end\n" +
                        "   end as jies,\n" +
                        "   sum(mx.yingk) as yingk,\n" +
                        "   round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.zhejbz))/sum(decode(jiessl,0,1,jiessl)),4) as zhejbz,\n" +
                        "   sum(mx.zhejje) as zhejje\n" +
                        " from danpcjsmxb mx,zhibb zb\n" +
                        " where zb.id=mx.zhibb_id and mx.leib=1 and mx.zhekfs = 0 and jiesdid=" + bsv.getYunfjsb_id() + " \n" +
                        " group by zb.bianm";
                rs = con.getResultSet(strSql);

                while (rs.next()) {

                    if (rs.getString("bianm").equals(Locale.Shul_zhibb)) {

                        bsv.setShul_ht(rs.getString("hetbz"));
                        bsv.setGongfsl(rs.getDouble("gongf"));
                        bsv.setYanssl(rs.getDouble("changf"));
                        bsv.setJiessl(rs.getDouble("jies"));
                        bsv.setShulzb_yk(rs.getDouble("yingk"));
                        bsv.setShulzb_zdj(rs.getDouble("zhejbz"));
                        bsv.setShulzb_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Qnetar_zhibb)) {

                        bsv.setQnetar_ht(rs.getString("hetbz"));
                        bsv.setQnetar_kf(rs.getDouble("gongf"));
                        bsv.setQnetar_cf(rs.getDouble("changf"));
                        bsv.setQnetar_js(rs.getDouble("jies"));
                        bsv.setQnetar_yk(rs.getDouble("yingk"));
                        bsv.setQnetar_zdj(rs.getDouble("zhejbz"));
                        bsv.setQnetar_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Std_zhibb)) {

                        bsv.setStd_ht(rs.getString("hetbz"));
                        bsv.setStd_kf(rs.getDouble("gongf"));
                        bsv.setStd_cf(rs.getDouble("changf"));
                        bsv.setStd_js(rs.getDouble("jies"));
                        bsv.setStd_yk(rs.getDouble("yingk"));
                        bsv.setStd_zdj(rs.getDouble("zhejbz"));
                        bsv.setStd_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Ad_zhibb)) {

                        bsv.setAd_ht(rs.getString("hetbz"));
                        bsv.setAd_kf(rs.getDouble("gongf"));
                        bsv.setAd_cf(rs.getDouble("changf"));
                        bsv.setAd_js(rs.getDouble("jies"));
                        bsv.setAd_yk(rs.getDouble("yingk"));
                        bsv.setAd_zdj(rs.getDouble("zhejbz"));
                        bsv.setAd_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Vdaf_zhibb)) {

                        bsv.setVdaf_ht(rs.getString("hetbz"));
                        bsv.setVdaf_kf(rs.getDouble("gongf"));
                        bsv.setVdaf_cf(rs.getDouble("changf"));
                        bsv.setVdaf_js(rs.getDouble("jies"));
                        bsv.setVdaf_yk(rs.getDouble("yingk"));
                        bsv.setVdaf_zdj(rs.getDouble("zhejbz"));
                        bsv.setVdaf_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Mt_zhibb)) {

                        bsv.setMt_ht(rs.getString("hetbz"));
                        bsv.setMt_kf(rs.getDouble("gongf"));
                        bsv.setMt_cf(rs.getDouble("changf"));
                        bsv.setMt_js(rs.getDouble("jies"));
                        bsv.setMt_yk(rs.getDouble("yingk"));
                        bsv.setMt_zdj(rs.getDouble("zhejbz"));
                        bsv.setMt_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Qgrad_zhibb)) {

                        bsv.setQgrad_ht(rs.getString("hetbz"));
                        bsv.setQgrad_kf(rs.getDouble("gongf"));
                        bsv.setQgrad_cf(rs.getDouble("changf"));
                        bsv.setQgrad_js(rs.getDouble("jies"));
                        bsv.setQgrad_yk(rs.getDouble("yingk"));
                        bsv.setQgrad_zdj(rs.getDouble("zhejbz"));
                        bsv.setQgrad_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Qbad_zhibb)) {

                        bsv.setQbad_ht(rs.getString("hetbz"));
                        bsv.setQbad_kf(rs.getDouble("gongf"));
                        bsv.setQbad_cf(rs.getDouble("changf"));
                        bsv.setQbad_js(rs.getDouble("jies"));
                        bsv.setQbad_yk(rs.getDouble("yingk"));
                        bsv.setQbad_zdj(rs.getDouble("zhejbz"));
                        bsv.setQbad_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Had_zhibb)) {

                        bsv.setHad_ht(rs.getString("hetbz"));
                        bsv.setHad_kf(rs.getDouble("gongf"));
                        bsv.setHad_cf(rs.getDouble("changf"));
                        bsv.setHad_js(rs.getDouble("jies"));
                        bsv.setHad_yk(rs.getDouble("yingk"));
                        bsv.setHad_zdj(rs.getDouble("zhejbz"));
                        bsv.setHad_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Stad_zhibb)) {

                        bsv.setStad_ht(rs.getString("hetbz"));
                        bsv.setStad_kf(rs.getDouble("gongf"));
                        bsv.setStad_cf(rs.getDouble("changf"));
                        bsv.setStad_js(rs.getDouble("jies"));
                        bsv.setStad_yk(rs.getDouble("yingk"));
                        bsv.setStad_zdj(rs.getDouble("zhejbz"));
                        bsv.setStad_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Star_zhibb)) {

                        bsv.setStar_ht(rs.getString("hetbz"));
                        bsv.setStar_kf(rs.getDouble("gongf"));
                        bsv.setStar_cf(rs.getDouble("changf"));
                        bsv.setStar_js(rs.getDouble("jies"));
                        bsv.setStar_yk(rs.getDouble("yingk"));
                        bsv.setStar_zdj(rs.getDouble("zhejbz"));
                        bsv.setStar_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Mad_zhibb)) {

                        bsv.setMad_ht(rs.getString("hetbz"));
                        bsv.setMad_kf(rs.getDouble("gongf"));
                        bsv.setMad_cf(rs.getDouble("changf"));
                        bsv.setMad_js(rs.getDouble("jies"));
                        bsv.setMad_yk(rs.getDouble("yingk"));
                        bsv.setMad_zdj(rs.getDouble("zhejbz"));
                        bsv.setMad_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Aar_zhibb)) {

                        bsv.setAar_ht(rs.getString("hetbz"));
                        bsv.setAar_kf(rs.getDouble("gongf"));
                        bsv.setAar_cf(rs.getDouble("changf"));
                        bsv.setAar_js(rs.getDouble("jies"));
                        bsv.setAar_yk(rs.getDouble("yingk"));
                        bsv.setAar_zdj(rs.getDouble("zhejbz"));
                        bsv.setAar_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Aad_zhibb)) {

                        bsv.setAad_ht(rs.getString("hetbz"));
                        bsv.setAad_kf(rs.getDouble("gongf"));
                        bsv.setAad_cf(rs.getDouble("changf"));
                        bsv.setAad_js(rs.getDouble("jies"));
                        bsv.setAad_yk(rs.getDouble("yingk"));
                        bsv.setAad_zdj(rs.getDouble("zhejbz"));
                        bsv.setAad_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Vad_zhibb)) {

                        bsv.setVad_ht(rs.getString("hetbz"));
                        bsv.setVad_kf(rs.getDouble("gongf"));
                        bsv.setVad_cf(rs.getDouble("changf"));
                        bsv.setVad_js(rs.getDouble("jies"));
                        bsv.setVad_yk(rs.getDouble("yingk"));
                        bsv.setVad_zdj(rs.getDouble("zhejbz"));
                        bsv.setVad_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.T2_zhibb)) {

                        bsv.setT2_ht(rs.getString("hetbz"));
                        bsv.setT2_kf(rs.getDouble("gongf"));
                        bsv.setT2_cf(rs.getDouble("changf"));
                        bsv.setT2_js(rs.getDouble("jies"));
                        bsv.setT2_yk(rs.getDouble("yingk"));
                        bsv.setT2_zdj(rs.getDouble("zhejbz"));
                        bsv.setT2_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Yunju_zhibb)) {

                        bsv.setYunju_ht(rs.getString("hetbz"));
                        bsv.setYunju_kf(rs.getDouble("gongf"));
                        bsv.setYunju_cf(rs.getDouble("changf"));
                        bsv.setYunju_js(rs.getDouble("jies"));
                        bsv.setYunju_yk(rs.getDouble("yingk"));
                        bsv.setYunju_zdj(rs.getDouble("zhejbz"));
                        bsv.setYunju_zje(rs.getDouble("zhejje"));
                    }
                }
            }

//			计算运费的结算数量、价格、金额信息
            strSql = "select sum(gongfsl) as gongfsl,sum(yanssl) as yanssl,sum(jiessl) as jiessl,sum(koud) as koud,	\n"
                    + " 	sum(kous) as kous,sum(kouz) as kouz,sum(ches) as ches,sum(jingz) as jingz,	\n"
                    + " 	sum(koud_js) as koud_js,sum(jiesslcy) as jiesslcy,sum(yuns) as yuns, sum(shulzbyk) as shulzbyk,	\n"
                    + " 	round_new(sum(decode(jiessl,0,0,jiessl*jiajqdj))/sum(decode(jiessl,0,1,jiessl))," + bsv.getMeikhsdjblxsw() + ") as jiajqdj,	\n"
                    + " 	round_new(sum(decode(jiessl,0,0,jiessl*jiesdj))/sum(decode(jiessl,0,1,jiessl))," + bsv.getMeikhsdjblxsw() + ") as jiesdj,	\n"
                    + " 	sum(jiakhj) as jiakhj,sum(jiaksk) as jiaksk,sum(jiashj) as jiashj,sum(chaokdl) as chaokdl,		\n"
                    + " 	round_new(sum(decode(jiessl,0,0,jiessl*biaomdj))/sum(decode(jiessl,0,1,jiessl)),2) as biaomdj,	\n"
                    + " 	round_new(sum(decode(jiessl,0,0,jiessl*buhsbmdj))/sum(decode(jiessl,0,1,jiessl)),2) as buhsbmdj	\n"
                    + " from 	\n"
                    + " 	(select xuh,	\n"
                    + " 		max(mx.gongfsl) as gongfsl,		\n"
                    + " 		max(mx.yanssl) as yanssl,		\n"
                    + " 		max(mx.jiessl) as jiessl,		\n"
                    + " 		max(mx.koud)  as koud,			\n"
                    + " 		max(mx.kous)  as kous,			\n"
                    + " 		max(mx.kouz)  as kouz,			\n"
                    + " 		max(mx.ches)  as ches,			\n"
                    + " 		max(mx.jingz)  as jingz,		\n"
                    + " 		max(mx.koud_js)  as koud_js,	\n"
                    + " 		max(mx.jiesslcy) as jiesslcy,	\n"
                    + " 		max(mx.yuns) as yuns,			\n"
                    + " 		max(mx.jiajqdj) as jiajqdj,		\n"
                    + " 		max(mx.jiesdj) as jiesdj,		\n"
                    + " 		max(mx.jiakhj) as jiakhj,		\n"
                    + " 		max(mx.jiaksk) as jiaksk,		\n"
                    + " 		max(mx.jiashj) as jiashj,		\n"
                    + "			max(mx.chaokdl) as chaokdl,		\n"
                    + "			max(mx.shulzbyk) as shulzbyk,	\n"
                    + " 		0 as biaomdj,		\n"
                    + " 		0 as buhsbmdj		\n"
                    + " 	from danpcjsmxb mx		\n"
                    + " 	where leib=1 and zhekfs = 0 and jiesdid=" + bsv.getYunfjsb_id() + "	\n"
                    + " 	group by xuh)";
            rs = con.getResultSet(strSql);
            while (rs.next()) {

                if (rs.getDouble("jiessl") > 0) {

                    if (bsv.getJieslx() != Locale.liangpjs_feiylbb_id
                            && bsv.getJieslx() != Locale.meikjs_feiylbb_id
                            && (!bsv.getYunfjsdpcfztj().equals(""))) {

//						纯运费结算，要通过danpcjsmxb给页面的项目赋值

//						结算价格
                        bsv.setGongfsl(rs.getDouble("gongfsl"));
                        bsv.setYanssl(rs.getDouble("yanssl"));
                        bsv.setJiessl(rs.getDouble("jiessl"));
                        bsv.setYunfjsl(bsv.getJiessl());
                        bsv.setKoud(rs.getDouble("koud"));
                        bsv.setKous(rs.getDouble("kous"));
                        bsv.setKouz(rs.getDouble("kouz"));
                        bsv.setChes(rs.getLong("ches"));
                        bsv.setJingz(rs.getDouble("jingz"));
                        bsv.setKoud_js(rs.getDouble("koud_js"));
                        bsv.setJiesslcy(rs.getDouble("jiesslcy"));
                        bsv.setYuns(rs.getDouble("yuns"));
                        bsv.setJiajqdj(rs.getDouble("jiajqdj"));
//						bsv.setShulzjbz(rs.getDouble("jiesdj"));
                        bsv.setHansmj(rs.getDouble("jiesdj"));
                        bsv.setYunfjsdj(rs.getDouble("jiesdj"));
                        bsv.setYunfjsdj_mk(rs.getDouble("jiesdj"));
                        bsv.setBuhsyf(rs.getDouble("jiakhj"));    //setJiakhj
//						bsv.setJine(rs.getDouble("jiakhj"));
                        bsv.setYunfsk(rs.getDouble("jiaksk"));    //setJiaksk
                        bsv.setYunzfhj(rs.getDouble("jiashj"));    //setJiashj
                        bsv.setTielyf(rs.getDouble("jiashj"));    //单批次结算运费的情况只可能是运输合同的，
                        //	运费不可能是通过核对货票获得的。
//						bsv.setBuhsmj((double)CustomMaths.Round_new(bsv.getJiakhj()/bsv.getJiessl(),7));
                        bsv.setChaokdl(rs.getDouble("chaokdl"));
                        bsv.setYingksl(rs.getDouble("shulzbyk"));

                    } else if ((!bsv.getYunfjsdpcfztj().equals(""))
                            && (bsv.getJieslx() == Locale.liangpjs_feiylbb_id
                            || bsv.getJieslx() == Locale.meikjs_feiylbb_id)) {

                        bsv.setBuhsyf(rs.getDouble("jiakhj"));    //setJiakhj
                        bsv.setYunfsk(rs.getDouble("jiaksk"));    //setJiaksk
                        bsv.setYunzfhj(rs.getDouble("jiashj"));    //setJiashj
                        bsv.setTielyf(rs.getDouble("jiashj"));    //单批次结算运费的情况只可能是运输合同的，
                        //	运费不可能是通过核对货票获得的。
                        bsv.setYunfjsdj(rs.getDouble("jiesdj"));
                        bsv.setYunfjsdj_mk(rs.getDouble("jiesdj"));
                        bsv.setYingksl(rs.getDouble("shulzbyk"));
                    }
                }
            }

            rs.close();

//			结算到厂价中煤款运费的情况
            if (bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)) {
//				如果合同中的结算方式为到厂价,统一算法：
//				如果有运费，那么用总煤款减去总运费剩下的就是总煤款
//				computYunfAndHej();
//				if(bsv.getYunzfhj()>0){

//					bsv.setJiashj((double)CustomMaths.Round_new(bsv.getJiashj()-bsv.getYunzfhj(),2));	//原始价税合计
//					bsv.setJiakhj((double)CustomMaths.Round_new(bsv.getJiashj()/(1+bsv.getMeiksl()),2));
//					bsv.setJiaksk((double)CustomMaths.Round_new(bsv.getJiashj()-bsv.getJiakhj(),2));
//					bsv.setJine(bsv.getJiakhj());
//					bsv.setHansmj((double)CustomMaths.Round_new(bsv.getJiashj()/bsv.getJiessl(), bsv.getMeikhsdjblxsw()));
//					bsv.setBuhsmj((double)CustomMaths.Round_new(bsv.getJiakhj()/bsv.getJiessl(),7));
//
////					Jiesdcz.UpdateDanpcjsmkb(bsv.getMeikjsb_id(), bsv.getHansmj(), bsv.getJiakhj(),
////							bsv.getJiaksk(), bsv.getJiashj(), CustomMaths.sub(bsv.getHansmj(), bsv.getFengsjj()));

                Danjsmk_dcjcl(2, "", 0, 0);
//				}
            }


//	    	计算拒付亏吨运费
            if (bsv.getKuidjfyf().equals("是")) {

                double Tielyfdj = (double) CustomMaths.Round_new(bsv.getTielyf() / bsv.getGongfsl(), 2);
                double Tielzfdj = (double) CustomMaths.Round_new(bsv.getTielzf() / bsv.getGongfsl(), 2);
                double Tielyfsdj = (double) CustomMaths.Round_new(bsv.getTielyfsk() / bsv.getGongfsl(), 2);

                double Kuangqyfdj = (double) CustomMaths.Round_new(bsv.getKuangqyf() / bsv.getGongfsl(), 2);
                double Kuangqzfdj = (double) CustomMaths.Round_new(bsv.getKuangqzf() / bsv.getGongfsl(), 2);
                double Kuangqyfsdj = (double) CustomMaths.Round_new(bsv.getKuangqsk() / bsv.getGongfsl(), 2);

                bsv.setTielyf((double) CustomMaths.Round_new(bsv.getTielyf() - (double) CustomMaths.Round_new(Tielyfdj * bsv.getKuid(), 2), 2));
                bsv.setTielzf((double) CustomMaths.Round_new(bsv.getTielzf() - (double) CustomMaths.Round_new(Tielzfdj * bsv.getKuid(), 2), 2));
                bsv.setTielyfsk((double) CustomMaths.Round_new(bsv.getTielyfsk() - (double) CustomMaths.Round_new(Tielyfsdj * bsv.getKuid(), 2), 2));

                bsv.setKuangqyf((double) CustomMaths.Round_new(bsv.getKuangqyf() - (double) CustomMaths.Round_new(Kuangqyfdj * bsv.getKuid(), 2), 2));
                bsv.setKuangqzf((double) CustomMaths.Round_new(bsv.getKuangqzf() - (double) CustomMaths.Round_new(Kuangqzfdj * bsv.getKuid(), 2), 2));
                bsv.setKuangqsk((double) CustomMaths.Round_new(bsv.getKuangqsk() - (double) CustomMaths.Round_new(Kuangqyfsdj * bsv.getKuid(), 2), 2));

                if ((double) CustomMaths.Round_new(Tielyfdj * bsv.getKuid(), 2) > 0) {

                    bsv.setBeiz(bsv.getBeiz() + " " + "亏吨拒付运费：" + (double) CustomMaths.Round_new(Tielyfdj * bsv.getKuid(), 2) + "元，亏吨拒付杂费：" + (double) CustomMaths.Round_new(Tielzfdj * bsv.getKuid(), 2) + "元");
                }

                if ((double) CustomMaths.Round_new(Kuangqyfdj * bsv.getKuid(), 2) > 0) {

                    bsv.setBeiz(bsv.getBeiz() + " " + "亏吨拒付矿运费：" + (double) CustomMaths.Round_new(Kuangqyfdj * bsv.getKuid(), 2) + "元，亏吨拒付矿杂费：" + (double) CustomMaths.Round_new(Kuangqzfdj * bsv.getKuid(), 2) + "元");
                }

                bsv.setKuidjfyf_je((double) CustomMaths.Round_new(Tielyfdj * bsv.getKuid(), 2) + (double) CustomMaths.Round_new(Kuangqyfdj * bsv.getKuid(), 2));
                bsv.setKuidjfzf_je((double) CustomMaths.Round_new(Tielzfdj * bsv.getKuid(), 2) + (double) CustomMaths.Round_new(Kuangqzfdj * bsv.getKuid(), 2));
            }

//			单批次结算煤款时，如果有扣吨量有值时，在此做最后的计算
            if ((!bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs))
                    && bsv.getJieskdl() != 0) {
//				如果是煤款结算，且结算形式不是“加权平均”，重新计算结算扣吨和上次结算量

//				计算扣吨+上次结算量对应的折金额
                double koudzje = CustomMaths.Round_new(CustomMaths.mul(-bsv.getJieskdl(), bsv.getHansmj()), 2);

                bsv.setJiashj(CustomMaths.Round_new(CustomMaths.add(bsv.getJiashj(), koudzje), 2));
                if (bsv.getKoudcxjsdj()) {
                    bsv.setHansmj(CustomMaths.Round_new(CustomMaths.div(bsv.getJiashj(), bsv.getJiessl()), 2));
                    bsv.setBuhsmj(CustomMaths.Round_new(CustomMaths.div(bsv.getHansmj(), (1 + bsv.getMeiksl())), 2));
                }
                bsv.setJiakhj(CustomMaths.Round_new(CustomMaths.div(bsv.getJiashj(), (1 + bsv.getMeiksl())), 2));
                bsv.setJiaksk(CustomMaths.Round_new(CustomMaths.sub(bsv.getJiashj(), bsv.getJiakhj()), 2));
                bsv.setJine(bsv.getJiakhj());
                bsv.setJiessl(CustomMaths.add(bsv.getJiessl(), -bsv.getJieskdl()));
                bsv.setJiesslcy(CustomMaths.Round_new((bsv.getJiessl() - bsv.getJingz()), 2));    //结算数量差异(结算量和过衡量的差值)

                if (bsv.getShulzb_yk() != 0) {
//					存在对“数量”指标的增扣款，要重新计算盈亏

                    bsv.setShulzb_yk(Jiesdcz.reCoundYk(bsv.getShul_ht(), bsv.getJiessl(), bsv.getShul_yk()));
                }
            }

//			单批次结算煤款时，如果有扣金额时，在此做最后的计算
            if ((!bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs))
                    && bsv.getKouk_js() != 0) {

                bsv.setJiashj(bsv.getJiashj() - bsv.getKouk_js());
                bsv.setHansmj(CustomMaths.Round_new(CustomMaths.div(bsv.getJiashj(), bsv.getJiessl()), 2));
                bsv.setBuhsmj(CustomMaths.Round_new(CustomMaths.div(bsv.getHansmj(), (1 + bsv.getMeiksl())), 2));
                bsv.setJiakhj(CustomMaths.Round_new(CustomMaths.div(bsv.getJiashj(), (1 + bsv.getMeiksl())), 2));
                bsv.setJiaksk(CustomMaths.Round_new(CustomMaths.sub(bsv.getJiashj(), bsv.getJiakhj()), 2));
                bsv.setJine(bsv.getJiakhj());
            }

//			单批次运费结算时，如果有扣吨或上次结算量有值时，在此做最后的计算
            if (bsv.getJieslx() != Locale.liangpjs_feiylbb_id
                    && bsv.getJieslx() != Locale.meikjs_feiylbb_id
                    && (!bsv.getYunfjsdpcfztj().equals(""))
                    && (bsv.getJieskdl() != 0)
                    && (!bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs))
//					运费为单批次结算时，如果存在扣吨或上次结算量要重新计算总运价总金额
                    ) {

                bsv.setJiessl(CustomMaths.add(bsv.getJiessl(), -bsv.getJieskdl()));
                bsv.setYunfjsl(bsv.getJiessl());
                bsv.setJiesslcy(CustomMaths.sub(bsv.getJiessl(), bsv.getJingz()));

                if (bsv.getJieslx() != Locale.liangpjs_feiylbb_id
                        && bsv.getJieslx() != Locale.meikjs_feiylbb_id
                        && (!bsv.getYunfjsdpcfztj().equals(""))) {

//					运费结算
                    bsv.setYunzfhj(CustomMaths.sub(bsv.getYunzfhj(), CustomMaths.mul(bsv.getYunfjsdj(), -bsv.getJieskdl())));
                    bsv.setBuhsyf(CustomMaths.Round_new(bsv.getYunzfhj() * (1 - bsv.getYunfsl()), 2));    //setJiakhj
                    bsv.setYunfsk(CustomMaths.sub(bsv.getYunzfhj(), bsv.getBuhsyf()));    //setJiaksk
                    bsv.setTielyf(bsv.getYunzfhj());    //单批次结算运费的情况只可能是运输合同的，
                    //	运费不可能是通过核对货票获得的。
                } else if ((!bsv.getYunfjsdpcfztj().equals(""))
                        && (bsv.getJieslx() == Locale.liangpjs_feiylbb_id
                        || bsv.getJieslx() == Locale.meikjs_feiylbb_id)) {

                    bsv.setYunzfhj(CustomMaths.sub(bsv.getYunzfhj(), CustomMaths.mul(bsv.getYunfjsdj(), -bsv.getJieskdl())));
                    bsv.setBuhsyf(CustomMaths.Round_new(bsv.getYunzfhj() * (1 - bsv.getYunfsl()), 2));    //setJiakhj
                    bsv.setYunfsk(CustomMaths.sub(bsv.getYunzfhj(), bsv.getBuhsyf()));    //setJiaksk
                    bsv.setTielyf(bsv.getYunzfhj());    //单批次结算运费的情况只可能是运输合同的，
                    //	运费不可能是通过核对货票获得的。
                }

                if (bsv.getShulzb_yk() != 0) {
//					存在对“数量”指标的增扣款，要重新计算盈亏

                    bsv.setShulzb_yk(Jiesdcz.reCoundYk(bsv.getShul_ht(), bsv.getJiessl(), bsv.getShul_yk()));
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
    }

    private void computYunfAndHej() {

        //运费
        double _Tielyf = bsv.getTielyf();
        double _Tielzf = bsv.getTielzf();
        double _Yunfsl = bsv.getYunfsl();        //运费税率
        double _Tielyfjk = bsv.getTielyfjk();
        double _Tielyfsk = bsv.getTielyfsk();
        double _Kuangqyf = bsv.getKuangqyf();
        double _Kuangqzf = bsv.getKuangqzf();
        double _Kuangqsk = bsv.getKuangqsk();
        double _Kuangqjk = bsv.getKuangqjk();
        double _Bukjk = bsv.getBukjk();
        double _Bukyzf = bsv.getBukyzf();

        double _Yunfsk = 0;
        double _Yunzfhj = 0;
        double _Buhsyf = 0;
        double _Hej = 0;
        String _Hejdx = "";

        //计算运费项
        _Yunzfhj = (double) CustomMaths.Round_new(_Tielyf + _Tielzf + _Kuangqyf + _Kuangqzf + _Bukjk, 2);                                    //运杂费合计
        String yunjslx = MainGlobal.getXitxx_item("结算", "运费税是否是增值税", visit.getDiancxxb_id(), "是");
//		税率调整
        if (_Tielyfsk + _Kuangqsk > 0) {
            //		如果是根据货票出来的
            if (yunjslx.equals("是")) {
                _Yunfsk = (double) CustomMaths.Round_new(CustomMaths.add(CustomMaths.add(_Tielyfsk, _Kuangqsk), (double) CustomMaths.Round_new(_Bukjk - _Bukjk / (1 + _Yunfsl), 2)), 2);        //运费税款
            } else {
                _Yunfsk = (double) CustomMaths.Round_new(CustomMaths.add(CustomMaths.add(_Tielyfsk, _Kuangqsk), (double) CustomMaths.Round_new(_Bukjk * _Yunfsl, 2)), 2);        //运费税款
            }
        } else {
            //		运费税率相关调整此处
            if (yunjslx.equals("是")) {
                _Yunfsk = (double) CustomMaths.Round_new(((double) CustomMaths.Round_new(_Tielyf - _Tielyf / (1 + _Yunfsl), 2) + _Kuangqsk + (double) CustomMaths.Round_new(_Bukjk - _Bukjk / (1 + _Yunfsl), 2)), 2);        //运费税款
            } else {
                _Yunfsk = (double) CustomMaths.Round_new(((double) CustomMaths.Round_new(_Tielyf * _Yunfsl, 2) + _Kuangqsk + (double) CustomMaths.Round_new(_Bukjk * _Yunfsl, 2)), 2);        //运费税款
            }
        }

        _Buhsyf = (double) CustomMaths.Round_new(((double) CustomMaths.Round_new((_Yunzfhj - _Yunfsk), 2) + _Kuangqjk), 2);        //不含税运费

        if (_Yunzfhj == 0 || _Yunzfhj == _Bukjk) {

            _Yunzfhj = (double) CustomMaths.Round_new(bsv.getYunfjsdj() * bsv.getYunfjsl() + _Bukjk, 2);                        //运费税款
//			税率调整
//			运费税率相关调整此处
            if (yunjslx.equals("是")) {
                _Yunfsk = (double) CustomMaths.Round_new(_Yunzfhj - _Yunzfhj / (1 + _Yunfsl), 2);
            } else {
                _Yunfsk = (double) CustomMaths.Round_new(_Yunzfhj * _Yunfsl, 2); //运费税款
            }
            //运费税款
            _Buhsyf = (double) CustomMaths.Round_new((_Yunzfhj - _Yunfsk), 2);                                            //不含税运费
            _Tielyf = _Yunzfhj;
            bsv.setTielyf(_Tielyf);
        }

//		用于记录单结算煤款时，当运费参与计算时的值（用于结算表的保存
        if (bsv.getYunzfhj_mk() == 0) {

            bsv.setYunzfhj_mk(_Yunzfhj);
            bsv.setBuhsyf_mk(_Buhsyf);
        } else if (_Yunzfhj > 0 && bsv.getYunzfhj_mk() != _Yunzfhj) {

            bsv.setYunzfhj_mk(_Yunzfhj);
            bsv.setBuhsyf_mk(_Buhsyf);
        }
//		记录运费结算数量
        bsv.setYunfjsl_mk(bsv.getYunfjsl());
//		用于记录单结算煤款时，当运费参与计算时的值（用于结算表的保存_end

        bsv.setJiashj(CustomMaths.add(bsv.getJiashj(), _Bukyzf));
        bsv.setJiakhj(CustomMaths.Round_new((bsv.getJiashj()) / (1 + bsv.getMeiksl()), 2));    //价款合计
        bsv.setJiaksk((double) CustomMaths.Round_new((bsv.getJiashj() - bsv.getJiakhj()), 2));        //价款税款
        //合计

        _Hej = (double) CustomMaths.Round_new((_Yunzfhj + bsv.getJiashj()), 2);
        _Hejdx = getDXMoney(_Hej);

        bsv.setYunfsk(_Yunfsk);
        bsv.setBuhsyf(_Buhsyf);
        bsv.setYunzfhj(_Yunzfhj);
        bsv.setYunfsk(_Yunfsk);
        bsv.setBuhsyf(_Buhsyf);
        bsv.setHej(_Hej);
        bsv.setHejdx(_Hejdx);

        if (bsv.getYunfjsdj() == 0.0) {
            bsv.setYunfjsdj(CustomMaths.Round_new(_Yunzfhj / bsv.getYunfjsl(), bsv.getYunfhsdjblxsw()));
        }

        String flag = MainGlobal.getXitxx_item("结算", "两票结算,运费算到煤款里,并且反算单价", String.valueOf(bsv.getDiancxxb_id()), "否");
        if ("是".equals(flag)) {
//			煤款里加入运费------------因煤款煤量与运费煤量不一，所以反算
            bsv.setJiashj(bsv.getJiashj() + bsv.getYunzfhj());
            bsv.setJiakhj(getRound_xz((bsv.getJiashj()) / (1 + bsv.getMeiksl()), 2));
            bsv.setJiaksk(getRound_xz((bsv.getJiashj() - bsv.getJiakhj()), 2));
            bsv.setHansmj(getRound_xz(bsv.getJiashj() / bsv.getJiessl(), 2));
            bsv.setBuhsmj(getRound_xz(CustomMaths.div(bsv.getHansmj(), (1 + bsv.getMeiksl())), 2));
            bsv.setJiajqdj(bsv.getJiashj());
            bsv.setJine(bsv.getJiashj());
//			清空所有运费信息
            bsv.setTielyf(0);
            bsv.setTielzf(0);
            bsv.setYunfsk(0);
            bsv.setBuhsyf(0);
            bsv.setYunzfhj(0);
            bsv.setYunfsk(0);
            bsv.setBuhsyf(0);
            bsv.setYunfjsdj(0);
            bsv.setYunfsl(0);
        }
    }

    private void Jiestszbcl(String Jieszbsftz, String SelIds, long Diancxxb_id, long Gongysb_id,
                            long Hetb_id, double Jieskdl, long Yunsdwb_id, long Jieslx, double Shangcjsl) {
//		结算特殊指标处理
//		目前先只处理加权平均结算时，某些指标需要单批次计算，进行特殊处理的情况
//		该函数只计算增扣款
        Interpreter bsh = null;
        String tmp[] = null;
        JDBCcon con = new JDBCcon();
        ResultSetList rsl = null;
        String strtmp = "";
        double jiessl = bsv.getJiessl();
        try {
            for (int i = 0; i < bsv.getTsclzbs().length; i++) {

                tmp = bsv.getTsclzbs()[i].split(",");
                if (tmp[tmp.length - 1].equals("0")) {
//					说明该指标还未经过特殊处理

                    bsh = new Interpreter();
                    bsh.set("结算形式", Locale.jiaqpj_jiesxs);

//					煤款含税单价保留小数位
                    bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//					含税单价取整方式
                    bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

                    bsh.set("计价方式", Locale.rezakkf_hetjjfs);
                    bsh.set("价格单位", Locale.yuanmqk_danw);
                    bsh.set("合同价格", 0);
                    bsh.set("最高煤价", bsv.getZuigmj());

//					增扣款保留小数位
                    bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//					用户自定义公式
                    bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());

                    this.getZengkk(bsv.getHetb_Id(), bsh, false, tmp);
                    if (bsv.getTsclzbzkksfxyjs()) {

//						将下一次指标值不进行增扣款计算
                        bsv.setTsclzbzkksfxyjs(false);
//						该指标需要增扣款特殊处理
                        bsh.eval(bsv.getGongs_Mk());
//						得到增扣款价格信息
                        Jiesdcz.setJieszb_Tszbcl(bsh, bsv, tmp[1]);

//						要找到该增扣款对应的数量
                        rsl = con.getResultSetList(Jiesdcz.getJiesszl_Sql(bsv, visit, Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id,
                                Hetb_id, Jieskdl, Yunsdwb_id, Jieslx, Shangcjsl, " and " + tmp[2] + "=" + tmp[3]).toString());
                        bsv.setJiessl(jiessl);
                        if (rsl.next()) {

//							检查某一个指标的增扣款是否为折数量,如果是就在总结算量上减去，不做后面的折金额计算
                            if (!Jiesdcz.checkZbzkksl(bsv, tmp[1])) {

                                double _Meiksl = bsv.getMeiksl();
                                double zhibzdj = Double.parseDouble(bsh.get("折单价_" + tmp[1]).toString());

                                if (bsh.get(tmp[1] + "基价类型").toString().equals("不含税单价")) {
                                    zhibzdj = CustomMaths.Round_new(Double.parseDouble(bsh.get("折单价_" + tmp[1]).toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw());
                                }

//								得到该指标的折价标准
//								字符串规则：指标编码,指标的值,指标折单价,折价数量,折金额
                                if (tmp[tmp.length - 2].equals("'C'")) {
//									说明是对超出部分的单独计算折价的操作，此时增扣款对应的量应为 该指标的盈亏量（因为只处理数量折价，目前是这样的）
//									strtmp+=tmp[1]+","+tmp[4]+","+bsh.get("折单价_"+tmp[1]).toString()+","+bsh.get("盈亏_"+tmp[1]).toString()+","+CustomMaths.Round_new(CustomMaths.mul(Double.parseDouble(bsh.get("折单价_"+tmp[1]).toString()),Double.parseDouble(bsh.get("盈亏_"+tmp[1]).toString())),2)+";";
                                    strtmp += tmp[1] + "," + tmp[4] + "," + zhibzdj + "," + bsh.get("盈亏_" + tmp[1]).toString() + "," + CustomMaths.Round_new(CustomMaths.mul(zhibzdj, Double.parseDouble(bsh.get("盈亏_" + tmp[1]).toString())), 2) + ";";
                                } else {
//									说明是对增扣款中“单批次”结算的处理
//									strtmp+=tmp[1]+","+tmp[4]+","+bsh.get("折单价_"+tmp[1]).toString()+","+rsl.getString("jiessl")+","+CustomMaths.Round_new(CustomMaths.mul(Double.parseDouble(bsh.get("折单价_"+tmp[1]).toString()),rsl.getDouble("jiessl")),2)+";";
                                    strtmp += tmp[1] + "," + tmp[4] + "," + zhibzdj + "," + rsl.getString("jiessl") + "," + CustomMaths.Round_new(CustomMaths.mul(zhibzdj, rsl.getDouble("jiessl")), 2) + ";";
                                }
                            } else {

                                Jiesdcz.getReCountJiessl(bsv, rsl.getDouble("jiessl"), 0);
                            }
                        }
                    } else if (tmp[1].equals(Locale.jiessl_zhibb)) {

                        bsv.setJiessl(jiessl);
                    }
                }
            }

            if (!strtmp.equals("")) {
//				如果有特殊处理的指标，就将Tsclzbs赋值为特殊指标的增扣款记录

                bsv.setTsclzbs(strtmp.split(";"));
//				运距,25,10,100,1000;
//				运距,23,12,300,3600;
//				如果是对超出部分的增扣款操作，字符串的样式如下:
//					结算数量,'C',4.75,95.0,451.25;
            } else {
//				将Tsclzbs至空
                bsv.setTsclzbs(null);
            }

            if (rsl != null) {

                rsl.close();
            }

            con.Close();

        } catch (EvalError ev) {
            // TODO 自动生成 catch 块
            ev.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void Danjsmk_dcjcl(int Place, String selIds, long hetb_id, double shangcjsl) {
//		函数名称：

//			单结算煤款（到厂价处理）
//		函数功能：

//			处理单结算煤款时，当煤款合同为到厂价时，减运费的问题
//		函数逻辑：
//			逻辑1：
//				如果是单结算煤款且又是到厂价时，计算运费。

//			逻辑2：
//				在recount函数中已经计算过了到厂价的情况，要将运费里面的值清空
//		函数形参：
//			Place要应用的逻辑结构，1为逻辑1；2为逻辑2
//			selIds要结算的列id
//			idhetb_id煤款合同表

        if (Place == 1) {
//			逻辑1
            if ((bsv.getJieslx() == Locale.meikjs_feiylbb_id || bsv.getJieslx() == Locale.liangpjs_feiylbb_id)
                    && bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)
                    ) {
//				特殊业务情形，如果是单结算煤款、合同价格是到厂价时，计算运费，
//					在后面的recount中从煤款中减掉运费，并将运费数据清空。
                this.getYunFei(selIds, Locale.liangpjs_feiylbb_id, hetb_id, shangcjsl);
                computYunfAndHej();
            }
        } else if (Place == 2) {
//			逻辑2
            if (bsv.getJieslx() == Locale.meikjs_feiylbb_id
                    && bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)
                    ) {
//				特殊业务情形，如果是单结算煤款、合同价格是到厂价时，计算运费，
//					在后面的recount中从煤款中减掉运费，并将运费数据清空。
                bsv.setTielyf(0);
                bsv.setTielzf(0);
                bsv.setYunfsl(0);        //运费税率
                bsv.setKuangqyf(0);
                bsv.setKuangqzf(0);
                bsv.setKuangqsk(0);
                bsv.setKuangqjk(0);
                bsv.setYunfjsdj(0);
                bsv.setYunfjsl(0);
            }
        }
    }

    /**
     * 根据增扣款中设置的指标基价类型(jijlx=0为含税、jijlx=1为不含税)重新计算煤款含税单价，
     * 并对煤款含税单价进行取整处理。
     * @author yinjm
     * @param bsh
     * @return void
     */
    public void getMeikhsdj(Interpreter bsh) {
//		  函数名称：
//		      获取煤矿含税单价
//
//		  函数功能：
//	         根据增扣款中设置的指标基价类型(jijlx=0为含税、jijlx=1为不含税)重新计算煤款含税单价，
//		     这些指标为增扣款中的非特殊处理指标。
//
//	      函数逻辑：
//			  取得增扣款中影响煤款结算单价的非特殊处理指标，判断指标的基价类型是否含税，
//			  如果不为含税，那么将指标的折单价转换成含税的。
//
//	      函数形参：
//	     	 bsh 从Interpreter对象中取得指标信息

        double _Meiksl = bsv.getMeiksl();

        try {

//			取得增扣款中影响煤款结算单价的非特殊处理指标
            String[] zengkkzb = bsv.getFeitsclzb().split(",");

            for (int i = 0; i < zengkkzb.length; i++) {

                if (zengkkzb[i].equals(Locale.Std_zhibb)) {

                    if (bsh.get("Std基价类型").toString().equals("不含税单价")) {
                        bsv.setStd_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("折单价_Std").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Ad_zhibb)) {

                    if (bsh.get("Ad基价类型").toString().equals("不含税单价")) {
                        bsv.setAd_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("折单价_Ad").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Vdaf_zhibb)) {

                    if (bsh.get("Vdaf基价类型").toString().equals("不含税单价")) {
                        bsv.setVdaf_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("折单价_Vdaf").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Mt_zhibb)) {

                    if (bsh.get("Mt基价类型").toString().equals("不含税单价")) {
                        bsv.setMt_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("折单价_Mt").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Qgrad_zhibb)) {

                    if (bsh.get("Qgrad基价类型").toString().equals("不含税单价")) {
                        bsv.setQgrad_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("折单价_Qgrad").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Qbad_zhibb)) {

                    if (bsh.get("Qbad基价类型").toString().equals("不含税单价")) {
                        bsv.setQbad_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("折单价_Qbad").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Had_zhibb)) {

                    if (bsh.get("Had基价类型").toString().equals("不含税单价")) {
                        bsv.setHad_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("折单价_Had").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Stad_zhibb)) {

                    if (bsh.get("Stad基价类型").toString().equals("不含税单价")) {
                        bsv.setStad_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("折单价_Stad").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Star_zhibb)) {

                    if (bsh.get("Star基价类型").toString().equals("不含税单价")) {
                        bsv.setStar_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("折单价_Star").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Mad_zhibb)) {

                    if (bsh.get("Mad基价类型").toString().equals("不含税单价")) {
                        bsv.setMad_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("折单价_Mad").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Aar_zhibb)) {

                    if (bsh.get("Aar基价类型").toString().equals("不含税单价")) {
                        bsv.setAar_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("折单价_Aar").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Aad_zhibb)) {

                    if (bsh.get("Aad基价类型").toString().equals("不含税单价")) {
                        bsv.setAad_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("折单价_Aad").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Vad_zhibb)) {

                    if (bsh.get("Vad基价类型").toString().equals("不含税单价")) {
                        bsv.setVad_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("折单价_Vad").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.T2_zhibb)) {

                    if (bsh.get("T2基价类型").toString().equals("不含税单价")) {
                        bsv.setT2_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("折单价_T2").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Yunju_zhibb)) {

                    if (bsh.get("运距基价类型").toString().equals("不含税单价")) {
                        bsv.setT2_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("折单价_运距").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.jiessl_zhibb)) {

                    if (bsh.get("结算数量基价类型").toString().equals("不含税单价")) {
                        bsv.setShul_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("折单价_结算数量").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                }
            }

//			计算煤款结算单价

            //zld 2010-12-10 结算公式中已经加了其他指标的折价。不需要重复加了。
            double value = Double.parseDouble(bsh.get("结算价格").toString());
            //+ bsv.getStd_zdj() + bsv.getAd_zdj() + bsv.getVdaf_zdj() + bsv.getMt_zdj()
            //+ bsv.getQgrad_zdj() + bsv.getQbad_zdj() + bsv.getHad_zdj() + bsv.getStad_zdj()
            //+ bsv.getStar_zdj() + bsv.getMad_zdj() + bsv.getAar_zdj() + bsv.getAad_zdj()
            //+ bsv.getVad_zdj() + bsv.getT2_zdj() + bsv.getYunju_zdj() + bsv.getShul_zdj();
            //zld end
//			对煤款结算单价进行取整处理
            getMeikhsdj_quz(bsv.getMeikhsdj_qzfs(), value, bsv.getMeikhsdjblxsw());

            if (bsv.getZuigmj() > 0) {

                if (bsv.getHansmj() > bsv.getZuigmj()) {

                    bsv.setHansmj(bsv.getZuigmj());
                }
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (EvalError e) {
            e.printStackTrace();
        }

    }

    /**
     * 处理煤款含税单价的保留小数位
     * 参数：bsh变量、取整方式、要处理的值、保留的小数位
     * @author yinjm
     * @param Quzfs
     * @param value
     * @param xiaosw
     * @return
     */
    public void getMeikhsdj_quz(String Quzfs, double value, int xiaosw) {
//	  函数名称：
//	     煤款含税单价取整
//
//	  函数功能：
//       对煤款含税单价进行取整处理
//
//    函数形参：
//   	 Quzfs  取整方式(进位、舍去、四舍五入)
//		 value  煤款含税单价
//		 xiaosw 保留小数位

        double Dblvalue = 0;
        String StrValue = "";
        String StrQz = "1";    //权重
        StrValue = String.valueOf(value);

        if (StrValue.indexOf('.') == -1) {

            Dblvalue = value;
        } else {

            StrValue = StrValue.substring(StrValue.indexOf('.'));
            if (Double.parseDouble(StrValue) == 0) {

                Dblvalue = value;
            }
        }

        for (int i = 0; i < xiaosw; i++) {
            StrQz = StrQz + "0";
        }

        if (Quzfs.equals("")) {

            Dblvalue = value;

        } else if (Quzfs.equals("进位")) {

            Dblvalue = Math.floor(CustomMaths.mul(Math.abs(value), Double.parseDouble(StrQz))) + 1;
            Dblvalue = CustomMaths.div(Dblvalue, Double.parseDouble(StrQz));
            if (value < 0) {
                Dblvalue = 0 - Dblvalue;
            }

        } else if (Quzfs.equals("舍去")) {

            Dblvalue = Math.floor(CustomMaths.mul(Math.abs(value), Double.parseDouble(StrQz)));
            Dblvalue = CustomMaths.div(Dblvalue, Double.parseDouble(StrQz));
            if (value < 0) {

                Dblvalue = 0 - Dblvalue;
            }

        } else if (Quzfs.equals("四舍五入")) {

            Dblvalue = CustomMaths.Round_new(Math.abs(value), xiaosw);
            if (value < 0) {

                Dblvalue = 0 - Dblvalue;
            }

        }

        bsv.setHansmj(Dblvalue);
    }

    public String getDXMoney(double _Money) {
        Money money = new Money();
        return money.NumToRMBStr(_Money);
    }

    public IPropertySelectionModel getZhibModel() {

        if (_ZhibModel == null) {
            getIZhibmModels();
        }
        return _ZhibModel;
    }

    public IPropertySelectionModel getIZhibmModels() {

        String sql = "select id,bianm from zhibb order by bianm";
        _ZhibModel = new IDropDownModel(sql);
        return _ZhibModel;
    }

    public void pageValidate(PageEvent arg0) {
        // TODO 自动生成方法存根

    }

    public double getRound_xz(double v, int scale) {
        if ("Round".equals(bsv.getJiesjeqzfs())) {
            if ("西部特殊结算".equals(MainGlobal.getXitxx_item("结算", "西部特殊结算", "0", ""))) {
                return CustomMaths.Round(v, scale);
            } else {
                return CustomMaths.round(v, scale);
            }
        } else {
            return CustomMaths.Round_new(v, scale);
        }
    }

    private double getHetsfkhzl(String SelIds) {
        JDBCcon con = new JDBCcon();
        ResultSetList rsl = null;
        double shuifkhzl = 0;
        try {
            rsl = con.getResultSetList("select sum(getHetsfkhl(fh.lie_id)) tiaozsl   from fahb fh,zhilb zl,hetsfkhb sfkh where fh.ZHILB_ID = zl.id and fh.HETB_ID = sfkh.HETB_ID and fh.LIE_ID in (" + SelIds + ")");
            if (rsl.next()) {
                shuifkhzl = rsl.getDouble("tiaozsl");
            }
        } catch (Exception e) {
        } finally {
            rsl.close();
            con.Close();
        }
        return shuifkhzl;
    }
}