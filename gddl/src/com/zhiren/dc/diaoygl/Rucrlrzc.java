package com.zhiren.dc.diaoygl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.dc.diaoygl.AutoCreateDaily_Report_gd;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 作者：李琛基
 * 时间：2010-09-25
 * 描述：
 *     根据国电需要新增 收耗存日报
 */
/*
 * 作者：李琛基
 * 时间：2010-10-8
 * 描述：
 *     修改收耗存日报中第二个表格中数据的SQL，改为从新建的表shouhcfkb中取数
 */
/*
 * 修改人：李琛基
 * 修改时间：2010-10-29
 * 描述：
 *     当取得的开始时间=结束时间时，累计值=月初到结束时间的累积，否则累计值=开始时间至结束时间的累积
 */
/*
 * 修改人：李琛基
 * 修改时间：2010-12-25
 * 描述：
 *    修改分矿来煤情况里 煤价、运价、到厂热值为当日数据，修改来煤量的修约方式：  对每一笔fahb_id 实行 sum(round_new(fahb.laimsl,0)) 。
 */
/*
 * 修改人：songy
 * 修改时间：201-6-14
 * 描述：xitxxb增加参数判断，不显示国电电力和大同发电的数据，显示汇总数据
 *
 *    */
/*
 * 日期：2011-10-08
 * 作者：赵胜男
 * 适用范围：国电电力下属所有电厂
 * 描述：修正国电电力日收耗存情况下得分矿来煤情况增加地区、计划口径、品种列；
 * 		根据地区显示煤矿地区的总计和各个煤矿的小计。
 */
/*
 * 日期：2011-10-26
 * 作者：夏峥
 * 适用范围：国电电力及其下属所有电厂
 * 描述：修正分矿来煤情况报表查询SQL，以汇总信息为主，显示相关内容
 */
/*
 * 日期：2011-11-01
 * 作者：夏峥
 * 适用范围：国电电力及其下属所有电厂
 * 描述：修正分矿来煤情况报表查询SQL，以汇总信息为主，
 * 		显示所有分矿信息，如果没有运输方式则默认为空。
 * 		庄河电厂所有发货的运输方式均固定为海运
 */
/*
 * 日期：2011-11-03
 * 作者：夏峥
 * 适用范围：国电电力及其下属所有电厂
 * 描述：修正分矿来煤情况报表查询SQL，消除由于运输单位名称造成的重复数据。
 */
/*
 * 日期：2011-11-04
 * 作者：夏峥
 * 适用范围：国电电力及其下属所有电厂
 * 描述：修正分矿来煤情况报表查询SQL，消除由于运输单位名称造成当日信息显示异常的问题
 */
/*
 * 日期：2011-11-12
 * 作者：夏峥
 * 适用范围：国电电力及其下属所有电厂
 * 描述：修正分矿来煤情况报表查询SQL，热值，煤价，运价显示累计数据。
 * 		 按照统计口径进行汇总不区分是否有发货信息关联
 */
/*
 * 日期：2011-11-16
 * 作者：夏峥
 * 适用范围：国电电力及其下属所有电厂
 * 描述：修正分矿来煤情况报表查询SQL，新增供应商列，并修正界面显示排列方式。
 */
/*
 * 作者：夏峥
 * 修改时间：2011-11-23
 * 适用范围：国电电力及其下属单位
 * 描述：收耗存日报报表界面需提供系统自动检索功能，
 * 			查询所选单位是否有热值，煤价，耗用量为0的数据，如果有则弹出对话框列表，
 * 			列表中的内容为:
 * 				单位：日期
 * 				信息不完全，请注意
 */
/*
 * 作者：夏峥
 * 时间：2011-12-13
 * 适用范围：国电电力
 * 描述：修正报表加权运算方法
 */
/*
 * 作者：夏峥
 * 时间：2012-01-09
 * 适用范围：国电电力
 * 描述：增加参数‘是否对日报报表汇总信息取整’，默认为否
 */
/*
 * 作者：夏峥
 * 时间：2012-05-04
 * 适用范围：国电电力
 * 描述：选择日期框时界面不自动刷新,日期框中的内容按照用户选择变动
 */
/*
 * 作者：夏峥
 * 时间：2012-07-08
 * 适用范围：国电电力
 * 描述：耗用信息只包括发电用，供热用，其他和非生产用四项
 */
/*
 * 作者：夏峥
 * 时间：2012-08-10
 * 适用范围：国电电力及其下属电厂（不包括庄河和邯郸）
 * 描述：增加厂外煤场进煤量列，并统计当日和累计信息。
 */
/*
 * 作者：夏峥
 * 时间：2012-09-26
 * 适用范围：国电电力及其下属电厂（不包括庄河和邯郸）
 * 描述：调整煤价和运价的加权运算方式
 */
/*
 * 作者：夏峥
 * 时间：2013-01-06
 * 适用范围：国电电力公司
 * 描述：分矿情况新增单位列。
 * 		截止日期默认初始化为当前日期的前天。
 * 		如果登录用户为国电电力，那么电厂树默认为国电电力下属全部单位。
 */
/*
 * 作者：夏峥
 * 时间：2013-01-07
 * 适用范围：国电电力公司
 * 描述：新增报表类型下拉框,
 *      根据类型下拉框内容调整sql.
 */

/*
 * 作者：赵胜男
 * 时间：2013-03-04
 * 适用范围：国电电力
 * 描述：库存警戒列取消，增加供热量和发电量一列，调整报表格式
 */
/*
 * 作者：赵胜男
 * 时间：2013-03-26
 * 适用范围：国电电力
 * 描述：调整页面bug
 */
/*
 * 作者:夏峥
 * 日期:2013-09-30
 * 修改内容:增加含税标煤单价和不含税标煤单价
 * 			不含税标煤单价计算公式为(煤价+运价-煤价税-运价税)*29.271/热值
 */
/*
 * 作者:夏峥
 * 日期:2013-10-16
 * 修改内容:运输方式直接取自日报分矿表
 */
public class Rucrlrzc extends BasePage {

    //	界面用户提示
    private String msg="";
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = MainGlobal.getExtMessageBox(msg,false);;
    }

    public boolean getRaw() {
        return true;
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


    private boolean blnIsBegin = false;

    public String getPrintTable() {

        if (!blnIsBegin) {
            return "";
        }
        blnIsBegin = false;
//		setMsg(null);
        return getHetltj();
    }
    private String getHetltj() {
        JDBCcon con = new JDBCcon();
        String sDate=this.getBeginriqDate();
        String eDate=this.getEndriqDate();
//        setMsg("好");
        try {
            //----------------------------------------------------------------------------------------------------------
            this.setMsg("");
            boolean isHuay_rc=false;
            String cheksql_rc="select distinct riq from (select  to_char( f.daohrq,'yyyy-mm-dd') riq from fahb f left join zhilb z on f.zhilb_id=z.id "+
                    "where f.daohrq between date'"+sDate+"' and date'"+eDate+"' and z.id is null order by f.daohrq)\n" ;
            ResultSet crs_rc=con.getResultSet(cheksql_rc);
            String lossmsg_rc="到货日期:<br/>";
            while(crs_rc.next()){
                isHuay_rc=true;
                lossmsg_rc+=crs_rc.getString("riq")+"<br/>";
            }
            crs_rc.close();
            if(isHuay_rc){
                setMsg(lossmsg_rc+"缺失入厂化验值,请注意!<br/>");
            }


            boolean isHuay=false;
            String cheksql="select distinct riq from (select  to_char( m.rulrq,'yyyy-mm-dd') riq from meihyb m left join rulmzlb r on m.rulmzlb_id=r.id "+
                    "where m.rulrq between date'"+sDate+"' and date'"+eDate+"' and r.id is null order by m.rulrq)\n" ;
            ResultSet crs=con.getResultSet(cheksql);
            String lossmsg="入炉日期:<br/>";
            while(crs.next()){
                isHuay=true;
                lossmsg+=crs.getString("riq")+"<br/>";
            }
            crs.close();
            if(isHuay){
                setMsg(this.getMsg()+"<br/>"+lossmsg+"缺失入炉化验值,请注意!");
            }


            //----------------------------------------------------------------------------------------------------------
            String sql = "select\n" +
                    "rulrq ,laimsl,rc_qnet_ar ,to_char(rc_stad,'90.99') ,to_char(rc_mt,'90.9') ,to_char(rc_aad,'90.99'),to_char(rc_vdaf,'90.99') ,\n" +
                    "meil ,rl_qnet_ar ,to_char(rl_stad,'90.99') ,to_char(rl_mt,'90.9') ,to_char(rl_aad,'90.99') ,to_char(rl_vdaf,'90.99'),\n" +
                    "rc_qnet_ar-rl_qnet_ar ,round_new(meil*(1-(1-rl_mt/100)/(1-rc_mt/100)),2) \n" +
                    "from (\n" +
                    "(select\n" +
                    "decode(grouping(f.daohrq),1,'合计',to_char(f.daohrq,'yyyy-mm-dd')) daohrq,\n" +
                    "sum(laimsl) laimsl,\n" +
                    "round_new(decode(sum(nvl(laimsl,0)),0,0,sum(z.qnet_ar/0.0041816*laimsl)/sum(laimsl)),0) rc_qnet_ar ,\n" +
                    "round_new(decode(sum(nvl(laimsl,0)),0,0,sum(z.stad*laimsl)/sum(laimsl)),2) rc_stad  ,\n" +
                    "round_new(decode(sum(nvl(laimsl,0)),0,0,sum(z.mt*laimsl)/sum(laimsl)),1) rc_mt  , \n" +
                    "round_new(decode(sum(nvl(laimsl,0)),0,0,sum(z.aad*laimsl)/sum(laimsl)),2)  rc_aad,\n" +
                    "round_new(decode(sum(nvl(laimsl,0)),0,0,sum(z.vdaf*laimsl)/sum(laimsl)),2) rc_vdaf\n" +
                    "from fahb f \n" +
                    "inner join zhilb z on z.id=f.zhilb_id\n" +
                    "where f.daohrq between date'"+sDate+"' and date'"+eDate+"'\n" +
                    "group by rollup (f.daohrq)) rc\n" +
                    "inner join \n" +
                    "(select\n" +
                    "decode(grouping(m.rulrq),1,'合计',to_char(m.rulrq,'yyyy-mm-dd')) rulrq,\n" +
                    " sum(meil) meil,\n" +
                    "round_new(decode(sum(nvl(meil,0)),0,0,sum(r.qnet_ar/0.0041816*meil)/sum(meil)),0) rl_qnet_ar,\n" +
                    "round_new(decode(sum(nvl(meil,0)),0,0,sum(r.stad*meil)/sum(meil)),2)  rl_stad,\n" +
                    "round_new(decode(sum(nvl(meil,0)),0,0,sum(r.mt*meil)/sum(meil)),1) rl_mt , \n" +
                    "round_new(decode(sum(nvl(meil,0)),0,0,sum(r.aad*meil)/sum(meil)),2) rl_aad ,\n" +
                    "round_new(decode(sum(nvl(meil,0)),0,0,sum(r.vdaf*meil)/sum(meil)),2) rl_vdaf\n" +
                    "from meihyb m \n" +
                    "inner join rulmzlb r on m.rulmzlb_id=r.id \n" +
                    "where m.rulrq between date'"+sDate+"' and date'"+eDate+"'\n" +
                    "group by rollup (m.rulrq)\n" +
                    "order by grouping(m.rulrq) desc,m.rulrq \n" +
                    ") rl\n" +
                    "on rc.daohrq=rl.rulrq\n" +
                    ")" ;
            ResultSetList rs = con.getResultSetList(sql);
            Report rt = new Report();
            String[][] ArrHeader;
            int[] ArrWidth;
//		if(zhib){
            int cnt=rs.getColumnCount();
            ArrHeader = new String[1][cnt];
            ArrWidth = new int[]{80, 60, 100, 100, 60, 60, 60, 60, 60, 100, 100, 60, 60, 60, 60, 60, 60, 60, 60};
            ArrHeader[0]=new String[]{ "日期", "进煤量<br/>(吨)", "发热量<br/>Qnet,ar<br/>(kcal/kg)", "全硫干燥基<br/>Stad<br/>(%)","全水收到基<br/>Mt<br/>(%)",
                    "灰分干燥基<br/>Aad<br/>(%)", "挥发份干燥无灰基<br/>Vdaf<br/>(%)",
                    "入炉煤量<br/>Qnet,ar<br/>(kcal/kg)", "发热量<br/>MT<br/>(kcal/kg)","全硫干燥基<br/>Stad<br/>(%)", "全水收到基<br/>Mt<br/>(%)",
                    "灰分干燥基<br/>Aad<br/>(%)","挥发份干燥无灰基<br/>Vdaf<br/>(%)",
                    "入厂入炉煤热值差(kcal/kg)", "入厂入炉水分差调整量<br/>(%)"};
//            ArrHeader[0] = new String[]{"日期", "入厂", "入厂", "入厂", "入厂", "入厂", "入厂", "入厂", "入炉", "入炉", "入炉", "入炉", "入炉", "入炉", "入炉", "热值差", "热值差", "热值差", "热值差"};
//            ArrHeader[1] = new String[]{"日期", "数量", "热值", "热值", "水分", "硫份", "灰份", "挥发份", "数量", "热值", "热值", "水分", "硫份", "灰份", "挥发份", "水分调整前", "水分调整前", "水分调整后", "水分调整后"};
//            ArrHeader[2] = new String[]{"日期", "(吨)", "(MJ/kg)", "(kcal/kg)", "Mt(%)", "St,d(%)", "Ad(%)", "Vdaf(%)", "(吨)", "(MJ/kg)", "(kcal/kg)", "Mt(%)", "St,d(%)", "Ad(%)", "Vdaf(%)", "(MJ/kg)", "(kcal/Kg)", "(MJ/kg)", "(kcal/Kg)"};
            rt.setBody(new Table(rs, 1, 0, 1));
            //
//			int aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), ((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
//			rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
            rt.body.setWidth(ArrWidth);
            rt.body.setHeaderData(ArrHeader);
            rt.setTitle("入厂入炉煤检质统计表", ArrWidth);
            rt.title.setRowHeight(2, 50);
            rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
            rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
            rt.setDefaultTitle(3, 3, "制表单位:" + ((Visit) getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
            rt.setDefaultTitle(7, 10, "单位:(单位：t、kcal/kg、%)", Table.ALIGN_RIGHT);
//		rt.setDefaultTitle(6, 2, "年份:"+getNianfValue().getId(),Table.ALIGN_RIGHT);
            rt.body.setPageRows(rt.PAPER_ROWS);
//		增加长度的拉伸
//			rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
            rt.body.mergeFixedCols();
            rt.body.mergeFixedRow();
            rt.body.ShowZero = true;
            rt.createDefautlFooter(ArrWidth);
            rt.setDefautlFooter(1, 17, "打印日期:" + DateUtil.FormatDate(new Date()), Table.ALIGN_RIGHT);
            // 设置页数
            _CurrentPage = 1;
            _AllPages = rt.body.getPages();
            if (_AllPages == 0) {
                _CurrentPage = 0;
            }
            return rt.getAllPagesHtml();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            con.Close();
        }
        return null;
//		}else{
//			ArrHeader = new String[3][13];
//			ArrWidth = new int[] { 80,60,80,80,60,60,80,80,60,80,80,80,80};
//			ArrHeader[0] = new String[] { "日期", "入厂", "入厂","入厂","入厂","入炉","入炉","入炉","入炉","热值差","热值差","热值差","热值差"};
//			ArrHeader[1] = new String[] { "日期", "数量", "热值","热值","水分","数量","热值","热值","水分","水分调整前","水分调整前","水分调整后","水分调整后"};
//			ArrHeader[2] = new String[] { "日期", "(吨)", "(MJ/kg)","(Kcal/kg)","Mt(%)","(吨)","(MJ/kg)","(Kcal/kg)","Mt(%)","(MJ/kg)","(Kcal/kg)","（MJ/kg）","(kcal/kg)"};
//			rt.setBody(new Table(rs, 3, 0, 1));
//			//
//			int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
//			rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
//			rt.body.setWidth(ArrWidth);
//			rt.body.setHeaderData(ArrHeader);
//			rt.setTitle("入 厂 入 炉 热 值 差", ArrWidth);
//			rt.title.setRowHeight(2, 50);
//			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
//			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
//			rt.setDefaultTitle(1, 5, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
//			rt.setDefaultTitle(11, 3, "单位:(吨)" ,Table.ALIGN_RIGHT);
////			rt.setDefaultTitle(6, 2, "年份:"+getNianfValue().getId(),Table.ALIGN_RIGHT);
//			rt.body.setPageRows(rt.PAPER_COLROWS);
////			增加长度的拉伸
//			rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
//			rt.body.mergeFixedCols();
//			rt.body.mergeFixedRow();
//			rt.body.ShowZero=true;
//			rt.createDefautlFooter(ArrWidth);
//			rt.setDefautlFooter(1, 13, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
//			// 设置页数
//			_CurrentPage = 1;
//			_AllPages = rt.body.getPages();
//			if (_AllPages == 0) {
//				_CurrentPage = 0;
//			}
//			con.Close();
//			return rt.getAllPagesHtml();
//		}
    }


    private String zhi ="";
    public String getZhi() {
        if(this.zhi.equals("")){
            setZhi();
        }
        return zhi;
    }
    public void setZhi() {
        Visit v = (Visit)getPage().getVisit();
        JDBCcon con = new JDBCcon();
        ResultSetList rsl = con.getResultSetList("select zhi from xitxxb where mingc = '日报隐藏电厂名称' and diancxxb_id ="+v.getDiancxxb_id());
        if(rsl.next()){
            this.zhi="  "+rsl.getString("zhi");
        }else{
            this.zhi=" -1 ";
        }
        rsl.close();
        con.Close();
    }

    private String getShouhc() {
        Visit visit = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        String riq=FormatDate(DateUtil.getDate(getBeginriqDate()));
        String riq1=FormatDate(DateUtil.getDate(getEndriqDate()));
        //int jib=this.getDiancTreeJib();
        StringBuffer strSQL = new StringBuffer();
        String beginRiq=this.getBeginriqDate();
        String endRiq=this.getEndriqDate();
        if(beginRiq.equals(endRiq)){
            beginRiq=DateUtil.Formatdate("yyyy-MM-dd", DateUtil.getFirstDayOfMonth(DateUtil.getDate(endRiq)));
        }

        strSQL.append("select decode(grouping(dc.mingc), 0, dc.mingc, 1, '合计') diancmc,\n");

        if(MainGlobal.getXitxx_item("收耗存日报", "是否对日报报表汇总信息取整", "0", "否").equals("否")){
            strSQL.append("       sum(nvl(dr.gm, 0)) drdh,\n" +
                    "       sum(nvl(lj.gm, 0)) ljdh,\n" +
                    "       sum(nvl(dr.hj, 0)) drhy,\n" +
                    "       sum(nvl(lj.hj, 0)) ljhy,\n" +
                    "       sum(nvl(dr.kuc, 0)) drkc,\n" +
                    "       sum(nvl(dr.kedkc, 0)) drkdkc,\n" +
                    "       sum(nvl(dr.changwml, 0)) drchangwml,\n" +
                    "       sum(nvl(lj.changwml, 0)) ljchangwml,sum(nvl(dr.fadl, 0)) fadl,sum(nvl(dr.gongrl, 0)) gongrl\n" );
//					"		SUM(nvl(dr.jingjcml,0)) jingjcml \n"
        }else{
            strSQL.append("       round_new(sum(nvl(dr.gm, 0)), 0) drdh,\n" +
                    "       round_new(sum(nvl(lj.gm, 0)), 0) ljdh,\n" +
                    "       round_new(sum(nvl(dr.hj, 0)), 0) drhy,\n" +
                    "       round_new(sum(nvl(lj.hj, 0)), 0) ljhy,\n" +
                    "       round_new(sum(nvl(dr.kuc, 0)), 0) drkc,\n" +
                    "       round_new(sum(nvl(dr.kedkc, 0)), 0) drkdkc,\n" +
                    "       round_new(sum(nvl(dr.changwml, 0)), 0) drchangwml,\n" +
                    "       round_new(sum(nvl(lj.changwml, 0)), 0) ljchangwml,\n" +
                    "       round_new(SUM(nvl(dr.fadl,0)), 0) fadl, \n" +
                    "       round_new(SUM(nvl(dr.gongrl,0)), 0) gongrl \n");
            //	"		round_new(SUM(nvl(dr.jingjcml,0)), 0) jingjcml \n"
        }
        strSQL.append("  from (select distinct dc.id, dc.xuh, dc.mingc\n" +
                "          from vwdianc dc\n" +
                "         where " +
                //"(dc.id="+this.getTreeid()+" or dc.fuid = "+this.getTreeid()+" or dc.fuid ="+fid+" ) and dc.id <> 300" +
                " dc.id in ("+getTreeid()+") and dc.id not in ( "+this.getZhi()+
                "  	)) dc,\n" +
                "       (select dc.id as id,\n" +
                "               sum(dangrgm) gm,\n" +
                "				SUM(dc.jingjcml) AS jingjcml,"+
                "               sum(haoyqkdr) hy,\n" +
                "               sum(fady) as fady,\n" +
                "               sum(gongry) as gongry,\n" +
                "               sum(kuc) as kuc,\n" +
                "               sum(kedkc) as kedkc,\n" +
                "               sum(changwml) as changwml,\n" +
                "               sum(fady + gongry + qity + feiscy) as hj,SUM(gongrl) AS gongrl,SUM(fadl) AS fadl\n" +
                "          from shouhcrbb hc, vwdianc dc\n" +
                "         where hc.diancxxb_id = dc.id\n" +
                "           and riq = to_date('"+endRiq+"', 'yyyy-mm-dd')\n" +
                "         group by dc.id) dr,\n" +
                "       (select dc.id as id,\n" +
                "               sum(dangrgm) gm,\n" +
                "               sum(haoyqkdr) hy,\n" +
                "               sum(changwml) as changwml,\n" +
                "               sum(fady) as fady,\n" +
                "               sum(gongry) as gongry,\n" +
                "               sum(fady + gongry + qity + feiscy) as hj\n" +
                "          from shouhcrbb hc, vwdianc dc\n" +
                "         where hc.diancxxb_id = dc.id\n" +
                "           and riq >= to_date('"+beginRiq+"', 'yyyy-mm-dd')\n" +
                "           and riq <= to_date('"+endRiq+"', 'yyyy-mm-dd')\n" +
                "         group by dc.id) lj\n" +
                " where dc.id = dr.id(+)\n" +
                "   and dc.id = lj.id(+)\n" +
                " group by rollup(dc.mingc)\n" +
                " order by grouping(dc.mingc), max(dc.xuh)");


        //System.out.println(sbsql);
        ResultSetList rs = con.getResultSetList(strSQL.toString());
        Report rt = new Report();

        String ArrHeader[][]=new String[2][10];
        ArrHeader[0]=new String[] {"单位","实际到货","实际到货","耗用情况","耗用情况","当日库存","当日库存","其中：厂外煤量","其中：厂外煤量","发电量","供热量"};
        ArrHeader[1]=new String[] {"单位","当日","累计","当日","累计","账面库存","可用库存","当日进煤","累计进煤","发电量","供热量"};
        int ArrWidth[]=new int[] {100,90,90,90,90,90,90,90,90,80,80};
        //rs.beforefirst();
        rt.setBody(new Table(rs, 2, 0, 0));
        rt.body.setHeaderData(ArrHeader);
        rt.body.setWidth(ArrWidth);
        rt.body.setColAlign(1, Table.ALIGN_CENTER);
        rt.setTitle(getDiancmc(String.valueOf(visit.getDiancxxb_id()))+"燃料来耗存统计日报",ArrWidth);
        rt.setDefaultTitle(1, 3, "填报单位:"+getDiancmc(), Table.ALIGN_LEFT);
        rt.setDefaultTitle(4, 4, riq+"至"+riq1, Table.ALIGN_CENTER);
        rt.setDefaultTitle(8, 4, "单位:吨、元/吨、MJ/Kg、万千瓦时、吉焦", Table.ALIGN_RIGHT);
        rt.body.mergeFixedRow();
        rt.body.mergeFixedCols();

        //rt.body.setPageRows(21);
        // 设置页数
        _CurrentPage = 1;
        _AllPages = rt.body.getPages();
        if (_AllPages == 0) {
            _CurrentPage = 0;
        }
        con.Close();
        return rt.getAllPagesHtml();

    }

    private String getShouhcDetail(){
        JDBCcon con = new JDBCcon();
        StringBuffer SQL = new StringBuffer();
        String beginRiq=this.getBeginriqDate();
        String endRiq=this.getEndriqDate();
        String sr="LJ";
//		如果日期为当日，则显示当日的煤价等信息，否则显示累计信息
        if(beginRiq.equals(endRiq)){
            beginRiq=DateUtil.Formatdate("yyyy-MM-dd", DateUtil.getFirstDayOfMonth(DateUtil.getDate(endRiq)));
            sr="DR";
        }
        SQL.append("SELECT DANW,DQ,GONGYS,MEIK,JIHKJ,PINZ,YUNSMC,\n");
        SQL.append("MEIJ,YUNJ,REZ,\n");
        SQL.append("DECODE(REZ,0,0,ROUND((MEIJ+YUNJ)/REZ*29.271,2))HSBMDJ,\n");
        SQL.append("DECODE(REZ,0,0,ROUND((MEIJ+YUNJ-MEIJS-YUNJS)/REZ*29.271,2))BHSBMDJ,DR,LJ\n");
        SQL.append("FROM(SELECT \n");
        if(getBBLXValue().getId()==1){
            SQL.append("	   DECODE(GROUPING(dc.mingc), 1, '总计', dc.mingc) danw,\n");
            SQL.append("	   DECODE(GROUPING(SR.DQ) + GROUPING(dc.mingc), 1, '单位小计', SR.DQ) DQ,\n");
            SQL.append("	   DECODE(GROUPING(SR.DQ) + GROUPING(SR.GONGYS), 1, '小计',SR.GONGYS) GONGYS,\n");
        }else{
            SQL.append("	   DECODE(GROUPING(SR.DQ), 1, '总计', SR.DQ) dq,\n");
            SQL.append("	   DECODE(GROUPING(SR.DQ) + GROUPING(dc.mingc), 1, '地区小计', dc.mingc) danw,\n");
            SQL.append("	   DECODE(GROUPING(dc.mingc) + GROUPING(SR.GONGYS), 1, '小计',SR.GONGYS) GONGYS,\n");
        }

        SQL.append("       SR.MEIK,\n");
        SQL.append("       SR.JIHKJ,\n");
        SQL.append("       SR.PINZ,\n");
        SQL.append("       SR.YUNSMC YUNSMC,\n");
//		SQL.append("       ROUND_NEW(DECODE(SUM(DECODE(SR.MEIJ,0,0,LAIMSLMJ)),0,0,SUM(SR.MEIJ * LAIMSLMJ) /SUM(DECODE(SR.MEIJ,0,0,LAIMSLMJ))),2) MEIJ,\n");
//		SQL.append("       ROUND_NEW(DECODE(SUM(DECODE(SR.MEIJ, 0, 0, SR.LAIMSLMJ)),0,0,SUM(DECODE(SR.MEIJ, 0, 0, SR.LAIMSLMJ) * SR.YUNJ) /SUM(DECODE(SR.MEIJ, 0, 0, LAIMSLMJ))),2) YUNJ,\n");
        SQL.append("       ROUND_NEW(DECODE(SUM(DECODE(SR.MEIJ,0,DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ),SR.LAIMSLMJ)),0,0,SUM(DECODE(SR.MEIJ, 0, DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ), SR.LAIMSLMJ)* SR.MEIJ) /SUM(DECODE(SR.MEIJ,0,DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ),LAIMSLMJ))),2) MEIJ,\n");
        SQL.append("       ROUND_NEW(DECODE(SUM(DECODE(SR.MEIJ, 0, DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ), SR.LAIMSLMJ)),0,0,SUM(DECODE(SR.MEIJ, 0, DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ), SR.LAIMSLMJ) * SR.YUNJ) /SUM(DECODE(SR.MEIJ,0,DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ),LAIMSLMJ))),2) YUNJ,\n");
        SQL.append("       ROUND_NEW(DECODE(SUM(DECODE(SR.MEIJ,0,DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ),SR.LAIMSLMJ)),0,0,SUM(DECODE(SR.MEIJ, 0, DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ), SR.LAIMSLMJ)* SR.MEIJS) /SUM(DECODE(SR.MEIJ,0,DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ),LAIMSLMJ))),2) MEIJS,\n");
        SQL.append("       ROUND_NEW(DECODE(SUM(DECODE(SR.MEIJ, 0, DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ), SR.LAIMSLMJ)),0,0,SUM(DECODE(SR.MEIJ, 0, DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ), SR.LAIMSLMJ) * SR.YUNJS) /SUM(DECODE(SR.MEIJ,0,DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ),LAIMSLMJ))),2) YUNJS,\n");
        SQL.append("       ROUND_NEW(DECODE(SUM(DECODE(SR.REZ,0,0,LAIMSLREZ)),0,0,SUM(SR.REZ * SR.LAIMSLREZ) /SUM(DECODE(SR.REZ,0,0,LAIMSLREZ))),2) REZ,\n");
        if(MainGlobal.getXitxx_item("收耗存日报", "是否对日报报表汇总信息取整", "0", "否").equals("否")){
            SQL.append("       SUM(SR.DR) DR,\n");
            SQL.append("       SUM(SR.LJ) LJ\n");
        }else{
            SQL.append("       DECODE(GROUPING(SR.DQ) + GROUPING(SR.MEIK), 2,round_new(SUM(SR.DR),0),SUM(SR.DR)) DR,\n");
            SQL.append("       DECODE(GROUPING(SR.DQ) + GROUPING(SR.MEIK), 2,round_new(SUM(SR.LJ),0),SUM(SR.LJ)) lj\n");
        }
        SQL.append("  FROM (SELECT LJ.DIANCID,LJ.DQ,\n");
        SQL.append("               LJ.GONGYS,\n");
        SQL.append("               LJ.MEIK,\n");
        SQL.append("               LJ.JIHKJ,\n");
        SQL.append("               LJ.PINZ,\n");
        SQL.append("               DECODE(LJ.DIANCID,202,'海运',DECODE(LJ.YUNSMC,'', '其它', LJ.YUNSMC)) YUNSMC,\n");
        SQL.append("               "+sr+".MEIJ MEIJ,\n");
        SQL.append("               "+sr+".YUNJ YUNJ,\n");
        SQL.append("               "+sr+".MEIJS MEIJS,\n");
        SQL.append("               "+sr+".YUNJS YUNJS,\n");
        SQL.append("               "+sr+".REZ REZ,\n");
        SQL.append("               "+sr+".LAIMSLMJ LAIMSLMJ,\n");
        SQL.append("               "+sr+".LAIMSLREZ LAIMSLREZ,\n");
        SQL.append("               DR.LAIMSL DR,\n");
        SQL.append("               LJ.LAIMSL LJ\n");
        SQL.append("          FROM (SELECT DECODE(GROUPING(DQ.MINGC),1,'',DECODE(DQ.MINGC, NULL, '其它', DQ.MINGC)) DQ,\n");
        SQL.append("                       DECODE(GROUPING(GYS2.MINGC),1,'',DECODE(GYS2.MINGC, NULL, '其它', GYS2.MINGC)) GONGYS,\n");
        SQL.append("                       DECODE(GROUPING(MK.MINGC),1,'',DECODE(MK.MINGC, NULL, '其它', MK.MINGC)) MEIK,\n");
        SQL.append("                       DECODE(GROUPING(J.MINGC),1,'',DECODE(J.MINGC, NULL, '其它', J.MINGC)) JIHKJ,\n");
        SQL.append("                       DECODE(GROUPING(P.MINGC),1,'',DECODE(P.MINGC, NULL, '其它', P.MINGC)) PINZ,\n");
        SQL.append("                       DECODE(GROUPING(YSFS.MINGC),1,'',DECODE(YSFS.MINGC, NULL, '其它', YSFS.MINGC)) YUNSMC,\n");
        SQL.append("                       DIANC.ID DIANCID,\n");
//		SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL)),0,0,SUM(SHC.MEIJ * SHC.LAIMSL) /SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL))),2) MEIJ,\n");
//		SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ, 0, 0,SHC.YUNJ ) *SHC.LAIMSL ) /SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL))),2) YUNJ,\n");
        SQL.append("					   ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL) * SHC.MEIJ) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) MEIJ,\n");
        SQL.append("					   ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL) * SHC.YUNJ) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) YUNJ,\n");
        SQL.append("					   ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL) * SHC.MEIJS) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) MEIJS,\n");
        SQL.append("					   ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL) * SHC.YUNJS) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) YUNJS,\n");
        SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.REZ,0,0,SHC.LAIMSL)),0,0,SUM(SHC.REZ * SHC.LAIMSL) /SUM(DECODE(SHC.REZ,0,0,SHC.LAIMSL))),2) REZ,\n");
        SQL.append("                       SUM(SHC.LAIMSL) LAIMSL,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)) LAIMSLMJ,SUM(DECODE(SHC.REZ,0,0,SHC.LAIMSL))  LAIMSLREZ\n");
        SQL.append("                  FROM SHOUHCFKB SHC,\n");
        SQL.append("                       MEIKXXB MK,\n");
        SQL.append("                       GONGYSB GYS,\n");
        SQL.append("                       GONGYSB GYS2,\n");
        SQL.append("                       YUNSFSB YSFS,\n");
        SQL.append("                       JIHKJB J,\n");
        SQL.append("                       PINZB P,\n");
        SQL.append("                       MEIKDQB DQ,\n");
//		SQL.append("                       (SELECT S.ID, MAX(F.YUNSFSB_ID) YUNSFSB_ID\n");
//		SQL.append("                          FROM FAHB F, SHOUHCFKB S\n");
//		SQL.append("                         WHERE S.DIANCXXB_ID = F.DIANCXXB_ID\n");
//		SQL.append("                           AND S.RIQ = F.DAOHRQ\n");
//		SQL.append("                           AND S.MEIKXXB_ID = F.MEIKXXB_ID\n");
//		SQL.append("                           AND S.GONGYSB_ID = F.GONGYSB_ID\n");
//		SQL.append("                           AND S.JIHKJB_ID = F.JIHKJB_ID\n");
//		SQL.append("                           AND S.PINZB_ID = F.PINZB_ID\n");
//		SQL.append("                   		   AND S.RIQ = "+DateUtil.FormatOracleDate(endRiq)+"\n");
//		SQL.append("                         GROUP BY S.ID) FAH,\n");
        SQL.append("                       (SELECT ID\n");
        SQL.append("                          FROM VWDIANC\n");
        SQL.append("                         WHERE ID IN ("+getTreeid()+")\n");
        SQL.append("                           AND VWDIANC.ID NOT IN ("+this.getZhi()+")) DIANC\n");
//		SQL.append("                 WHERE SHC.ID = FAH.ID(+)\n");
        SQL.append("                   WHERE SHC.YUNSFSB_ID = YSFS.ID(+)\n");
        SQL.append("                   AND GYS2.ID = SHC.GONGYSB_ID\n");
        SQL.append("                   AND SHC.PINZB_ID = P.ID(+)\n");
        SQL.append("                   AND GYS.FUID = DQ.ID(+)\n");
        SQL.append("                   AND SHC.MEIKXXB_ID = MK.ID(+)\n");
        SQL.append("                   AND SHC.JIHKJB_ID = J.ID(+)\n");
        SQL.append("                   AND MK.MEIKDQ_ID = GYS.ID\n");
        SQL.append("                   AND SHC.DIANCXXB_ID = DIANC.ID\n");
        SQL.append("                   AND SHC.RIQ = "+DateUtil.FormatOracleDate(endRiq)+"\n");
        SQL.append("                 GROUP BY ROLLUP((DQ.MINGC, GYS2.MINGC, MK.MINGC, J.MINGC,P.MINGC, YSFS.MINGC, DIANC.ID))\n");
        SQL.append("                HAVING NOT GROUPING(DQ.MINGC) = 1) DR,\n");
        SQL.append("               (SELECT DECODE(GROUPING(DQ.MINGC),1,'',DECODE(DQ.MINGC, NULL, '其它', DQ.MINGC)) DQ,\n");
        SQL.append("                       DECODE(GROUPING(GYS2.MINGC),1,'',DECODE(GYS2.MINGC, NULL, '其它', GYS2.MINGC)) GONGYS,\n");
        SQL.append("                       DECODE(GROUPING(MK.MINGC),1,'',DECODE(MK.MINGC, NULL, '其它', MK.MINGC)) MEIK,\n");
        SQL.append("                       DECODE(GROUPING(J.MINGC),1,'',DECODE(J.MINGC, NULL, '其它', J.MINGC)) JIHKJ,\n");
        SQL.append("                       DECODE(GROUPING(P.MINGC),1,'',DECODE(P.MINGC, NULL, '其它', P.MINGC)) PINZ,\n");
        SQL.append("                       DECODE(GROUPING(YSFS.MINGC),1,'',DECODE(YSFS.MINGC, NULL, '其它', YSFS.MINGC)) YUNSMC,\n");
        SQL.append("                       DIANC.ID DIANCID,\n");
//		SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL)),0,0,SUM(SHC.MEIJ * SHC.LAIMSL) /SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL))),2) MEIJ,\n");
//		SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ, 0, 0,SHC.YUNJ ) *SHC.LAIMSL ) /SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL))),2) YUNJ,\n");
        SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)*SHC.MEIJ) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) MEIJ,\n");
        SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL) * SHC.YUNJ) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) YUNJ,\n");
        SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)*SHC.MEIJS) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) MEIJS,\n");
        SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL) * SHC.YUNJS) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) YUNJS,\n");
        SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.REZ,0,0,SHC.LAIMSL)),0,0,SUM(SHC.REZ * SHC.LAIMSL) /SUM(DECODE(SHC.REZ,0,0,SHC.LAIMSL))),2) REZ,\n");
        SQL.append("                       SUM(SHC.LAIMSL) LAIMSL,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)) LAIMSLMJ,SUM(DECODE(SHC.REZ,0,0,SHC.LAIMSL))  LAIMSLREZ\n");
        SQL.append("                  FROM SHOUHCFKB SHC,\n");
        SQL.append("                       MEIKXXB MK,\n");
        SQL.append("                       GONGYSB GYS,\n");
        SQL.append("                       GONGYSB GYS2,\n");
        SQL.append("                       YUNSFSB YSFS,\n");
        SQL.append("                       JIHKJB J,\n");
        SQL.append("                       PINZB P,\n");
        SQL.append("                       MEIKDQB DQ,\n");
//		SQL.append("                       (SELECT S.ID, MAX(F.YUNSFSB_ID) YUNSFSB_ID\n");
//		SQL.append("                          FROM FAHB F, SHOUHCFKB S\n");
//		SQL.append("                         WHERE S.DIANCXXB_ID = F.DIANCXXB_ID\n");
//		SQL.append("                           AND S.RIQ = F.DAOHRQ\n");
//		SQL.append("                           AND S.MEIKXXB_ID = F.MEIKXXB_ID\n");
//		SQL.append("                           AND S.GONGYSB_ID = F.GONGYSB_ID\n");
//		SQL.append("                           AND S.JIHKJB_ID = F.JIHKJB_ID\n");
//		SQL.append("                           AND S.PINZB_ID = F.PINZB_ID\n");
//		SQL.append("    			  		   AND S.RIQ <= "+DateUtil.FormatOracleDate(endRiq)+"\n");
//		SQL.append("    			   		   AND S.RIQ >= "+DateUtil.FormatOracleDate(beginRiq)+"\n");
//		SQL.append("                         GROUP BY S.ID) FAH,\n");
        SQL.append("                       (SELECT ID\n");
        SQL.append("                          FROM VWDIANC\n");
        SQL.append("                         WHERE ID IN ("+getTreeid()+")\n");
        SQL.append("                           AND VWDIANC.ID NOT IN ("+this.getZhi()+")) DIANC\n");
//		SQL.append("                 WHERE SHC.ID = FAH.ID(+)\n");
        SQL.append("                   WHERE SHC.YUNSFSB_ID = YSFS.ID(+)\n");
        SQL.append("                   AND SHC.PINZB_ID = P.ID(+)\n");
        SQL.append("                   AND GYS2.ID = SHC.GONGYSB_ID\n");
        SQL.append("                   AND GYS.FUID = DQ.ID(+)\n");
        SQL.append("                   AND SHC.MEIKXXB_ID = MK.ID(+)\n");
        SQL.append("                   AND SHC.JIHKJB_ID = J.ID(+)\n");
        SQL.append("                   AND MK.MEIKDQ_ID = GYS.ID\n");
        SQL.append("                   AND SHC.DIANCXXB_ID = DIANC.ID\n");
        SQL.append("    			   AND SHC.RIQ <= "+DateUtil.FormatOracleDate(endRiq)+"\n");
        SQL.append("    			   AND SHC.RIQ >= "+DateUtil.FormatOracleDate(beginRiq)+"\n");
        SQL.append("                 GROUP BY ROLLUP((DQ.MINGC, GYS2.MINGC, MK.MINGC, J.MINGC,P.MINGC, YSFS.MINGC, DIANC.ID))\n");
        SQL.append("                HAVING NOT GROUPING(DQ.MINGC) = 1) LJ\n");
        SQL.append("         WHERE DR.MEIK(+) = LJ.MEIK\n");
        SQL.append("           AND DR.GONGYS(+) = LJ.GONGYS\n");
        SQL.append("           AND DR.YUNSMC(+) = LJ.YUNSMC\n");
        SQL.append("           AND DR.JIHKJ(+) = LJ.JIHKJ\n");
        SQL.append("           AND DR.DQ(+) = LJ.DQ\n");
        SQL.append("           AND DR.PINZ(+) = LJ.PINZ\n");
        SQL.append("           AND DR.DIANCID(+) = LJ.DIANCID) SR, diancxxb dc WHERE sr.diancid=dc.id\n");
        if(getBBLXValue().getId()==1){
            SQL.append(" GROUP BY ROLLUP((dc.mingc,dc.xuh),SR.DQ, (SR.GONGYS, SR.MEIK, SR.JIHKJ, SR.PINZ, SR.YUNSMC))\n");
            SQL.append(" ORDER BY GROUPING(dc.xuh) DESC,dc.xuh,GROUPING(SR.DQ) DESC, SR.DQ, GROUPING(SR.GONGYS) DESC, SR.GONGYS");
        }else{
            SQL.append(" GROUP BY ROLLUP(SR.DQ,(dc.mingc,dc.xuh),(SR.GONGYS, SR.MEIK, SR.JIHKJ, SR.PINZ, SR.YUNSMC))\n");
            SQL.append("ORDER BY GROUPING(SR.DQ) DESC, SR.DQ, GROUPING(dc.xuh) DESC,dc.xuh,GROUPING(SR.GONGYS) DESC, SR.GONGYS\n");
        }
        SQL.append(")SR");

        ResultSetList rs = con.getResultSetList(SQL.toString());
        Report rt = new Report();

        String ArrHeader[][]=new String[3][14];
        if(getBBLXValue().getId()==1){
            ArrHeader[0]=new String[] {"分矿来煤情况","分矿来煤情况","分矿来煤情况","分矿来煤情况","分矿来煤情况","分矿来煤情况","分矿来煤情况","分矿来煤情况","分矿来煤情况","分矿来煤情况","分矿来煤情况","分矿来煤情况","分矿来煤情况","分矿来煤情况"};
            ArrHeader[1]=new String[] {"单位","地区","供应商","矿别","计划口径","品种","运输<br>方式","煤价<br>(含税)","运价<br>(含税)","到厂热值","含税<br>标煤单价","不含税<br>标煤单价","来煤量","来煤量"};
            ArrHeader[2]=new String[] {"单位","地区","供应商","矿别","计划口径","品种","运输<br>方式","煤价<br>(含税)","运价<br>(含税)","到厂热值","含税<br>标煤单价","不含税<br>标煤单价","当日","累计"};
        }else{
            ArrHeader[0]=new String[] {"分矿来煤情况","分矿来煤情况","分矿来煤情况","分矿来煤情况","分矿来煤情况","分矿来煤情况","分矿来煤情况","分矿来煤情况","分矿来煤情况","分矿来煤情况","分矿来煤情况","分矿来煤情况","分矿来煤情况","分矿来煤情况"};
            ArrHeader[1]=new String[] {"地区","单位","供应商","矿别","计划口径","品种","运输<br>方式","煤价<br>(含税)","运价<br>(含税)","到厂热值","含税<br>标煤单价","不含税<br>标煤单价","来煤量","来煤量"};
            ArrHeader[2]=new String[] {"地区","单位","供应商","矿别","计划口径","品种","运输<br>方式","煤价<br>(含税)","运价<br>(含税)","到厂热值","含税<br>标煤单价","不含税<br>标煤单价","当日","累计"};
        }

        int ArrWidth[]=new int[] {80,80,120,110,60,50,60,60,60,60,60,60,60,60};
        //rs.beforefirst();
        rt.setBody(new Table(rs, 3, 0, 0));
        rt.body.setHeaderData(ArrHeader);
        rt.body.setWidth(ArrWidth);
        rt.body.mergeFixedRow();
        rt.body.mergeFixedCol(1);
        rt.body.mergeFixedCol(2);
        rt.body.ShowZero=true;
        rt.body.setColAlign(1, Table.ALIGN_CENTER);
        rt.body.setColAlign(2, Table.ALIGN_CENTER);
        rt.body.setColAlign(3, Table.ALIGN_LEFT);
        rt.body.setColAlign(4, Table.ALIGN_LEFT);
        rt.body.setColAlign(5, Table.ALIGN_CENTER);
        rt.body.setColAlign(6, Table.ALIGN_CENTER);
        rt.body.setColAlign(7, Table.ALIGN_CENTER);


        rt.createFooter(1, ArrWidth);
        rt.setDefautlFooter(1, 2, "主管：", Table.ALIGN_CENTER);
        rt.setDefautlFooter(3, 3, "审核：", Table.ALIGN_CENTER);
        rt.setDefautlFooter(6, 2, "制表：", Table.ALIGN_CENTER);

        _CurrentPage = 1;
        _AllPages =rt.body.getPages();
        if (_AllPages == 0) {
            _CurrentPage = 0;
        }
        con.Close();
        return rt.getAllPagesHtml();
    }


    private boolean _QueryClick = false;

    public void QueryButton(IRequestCycle cycle) {
        _QueryClick = true;
    }

    public void submit(IRequestCycle cycle) {
        if (_QueryClick) {
            _QueryClick = false;
        }
    }

    public String getPageHome() {
        if (((Visit) getPage().getVisit()).getboolean1()) {
            return "window.location = '" + MainGlobal.getHomeContext(this)
                    + "';";
        } else {
            return "";
        }
    }
    private String FormatDate(Date _date) {
        if (_date == null) {
            return "";
        }
        return DateUtil.Formatdate("yyyy年MM月dd日", _date);
    }
    //	日期设置开始
    public String getBeginriqDate(){
        if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
            Calendar stra=Calendar.getInstance();
            stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
            stra.add(Calendar.MONTH,-1);
            ((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
        }
        return ((Visit) getPage().getVisit()).getString4();
    }
    public void setBeginriqDate(String value){
        ((Visit) getPage().getVisit()).setString4(value);
    }

    public String getEndriqDate(){
        if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
            Calendar stra=Calendar.getInstance();
            int T_Date=DateUtil.getDay(new Date());
            if(T_Date<4){
                T_Date=1;
            }else{
                T_Date=T_Date-2;
            }
            stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), T_Date);
            stra.add(Calendar.MONTH,-1);
            ((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(stra.getTime()));
        }
        return ((Visit) getPage().getVisit()).getString5();
    }

    public void setEndriqDate(String value){
        ((Visit) getPage().getVisit()).setString5(value);
    }
    //	日期设置结束
    private void getToolbars(){
        Toolbar tb1 = new Toolbar("tbdiv");
        tb1.addText(new ToolbarText("入炉日期:"));
        DateField df = new DateField();

        df.setValue(this.getBeginriqDate());
        df.Binding("qiandrq1","");// 与html页中的id绑定,并自动刷新
        df.setWidth(80);
        tb1.addField(df);
        tb1.addText(new ToolbarText("至"));
        DateField df1 = new DateField();
        df1.setValue(this.getEndriqDate());
        df1.Binding("qiandrq2","");// 与html页中的id绑定,并自动刷新
        df1.setWidth(80);
        tb1.addField(df1);
        tb1.addText(new ToolbarText("-"));

        ExtTreeUtil etu = new ExtTreeUtil("diancTree",
                ExtTreeUtil.treeWindowCheck_Dianc, ((Visit) this.getPage()
                .getVisit()).getDiancxxb_id(), getTreeid(),null,true);

        setTree(etu);
        TextField tf = new TextField();
        tf.setId("diancTree_text");
        tf.setWidth(100);
        String[] str=getTreeid().split(",");
        if(str.length>1){
            tf.setValue("组合电厂");
        }else{
            tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long.parseLong(str[0])));
        }

        ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
        tb2.setIcon("ext/resources/images/list-items.gif");
        tb2.setCls("x-btn-icon");
        tb2.setMinWidth(20);

        tb1.addText(new ToolbarText("单位:"));
        tb1.addField(tf);
        tb1.addItem(tb2);

        tb1.addText(new ToolbarText("-"));

//		tb1.addText(new ToolbarText("报表类型:"));
//		ComboBox cbo_bblx = new ComboBox();
//		cbo_bblx.setTransform("BBLXDropDown");
//		cbo_bblx.setWidth(120);
//		tb1.addField(cbo_bblx);
//		tb1.addText(new ToolbarText("-"));
        ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
        tb.setIcon(SysConstant.Btn_Icon_Refurbish);
        tb1.addItem(tb);

//		再刷新时给用户提示是否存在不合理数据
//		setMsg(null);
        Date beginRiq=DateUtil.getDate(this.getBeginriqDate());
        Date endRiq=DateUtil.getDate(this.getEndriqDate());
//		如果日期为当日，则显示当日的煤价等信息，否则显示累计信息
        AutoCreateDaily_Report_gd DR=new AutoCreateDaily_Report_gd();
        JDBCcon con = new JDBCcon();
        String errmsg=DR.RPChk(con, getTreeid(),beginRiq,endRiq );
//		if(errmsg.length()>0){
//			setMsg(errmsg+"信息不完全，请注意");
//		}
        con.Close();
        setToolbar(tb1);
    }
    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
        Visit visit = (Visit) getPage().getVisit();
        if (visit.getRenyID() == -1) {
            visit.setboolean1(true);
            setZhi();
            return;
        }
        if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
            visit.setActivePageName(getPageName().toString());
            visit.setList1(null);
            visit.setDropDownBean1(null);
            visit.setProSelectionModel1(null);
            visit.setDropDownBean2(null);
            visit.setProSelectionModel2(null);
            visit.setDropDownBean4(null);
            visit.setProSelectionModel4(null);
            visit.setExtTree1(null);
            visit.setString4(null);
            visit.setString5(null);
            setTreeid(null);
            setBBLXModel(null);
            setBBLXValue(null);
            setZhi();
//			如果登录用户为国电电力，那么单位下拉框默认为全部单位。
            if(visit.getDiancxxb_id()==112){
                initDiancTree();
            }
        }
        getToolbars();
        blnIsBegin = true;

    }

    //	新增报表类型下拉框
    // 盘点编号下拉框
    public IDropDownBean getBBLXValue() {
        if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
            ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean) getBBLXModel().getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean2();
    }

    public void setBBLXValue(IDropDownBean Value) {
        ((Visit) getPage().getVisit()).setDropDownBean2(Value);
    }

    public void setBBLXModel(IPropertySelectionModel value) {
        ((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public IPropertySelectionModel getBBLXModel() {
        getIBBLXModels();
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }

    public void getIBBLXModels() {
        List list= new ArrayList();
        list.add(new IDropDownBean(1, "按单位汇总"));
        list.add(new IDropDownBean(2, "按煤源地区汇总"));
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(list));
    }

    private String treeid;
    public String getTreeid() {
        if(treeid==null||treeid.equals("")){
            treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
        }
        return treeid;
    }
    public void setTreeid(String treeid) {
        this.treeid = treeid;
    }
    public ExtTreeUtil getTree() {
        return ((Visit) this.getPage().getVisit()).getExtTree1();
    }

    public void setTree(ExtTreeUtil etu) {
        ((Visit) this.getPage().getVisit()).setExtTree1(etu);
    }

    public String getTreeScript() {
        return getTree().getWindowTreeScript();
    }
    public String getTreeHtml() {
        return getTree().getWindowTreeHtml(this);
    }
    //	电厂名称
//	private IPropertySelectionModel _IDiancModel;
    public IPropertySelectionModel getDiancmcModel() {
        if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
            getDiancmcModels();
        }
        return ((Visit)getPage().getVisit()).getProSelectionModel1();
    }

    //	private boolean _DiancmcChange=false;
    public IDropDownBean getDiancmcValue() {
        if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
            ((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
        }
        return ((Visit)getPage().getVisit()).getDropDownBean1();
    }

    public void setDiancmcValue(IDropDownBean Value) {
        ((Visit)getPage().getVisit()).setDropDownBean1(Value);
    }

    public IPropertySelectionModel getDiancmcModels() {
        String sql = "select id,mingc from diancxxb";
        setDiancmcModel(new IDropDownModel(sql)) ;
        return ((Visit) getPage().getVisit()).getProSelectionModel1();
    }

    public void setDiancmcModel(IPropertySelectionModel _value) {
        ((Visit)getPage().getVisit()).setProSelectionModel1(_value);
    }
    //	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
    public int getDiancTreeJib() {
        JDBCcon con = new JDBCcon();
        int jib = -1;
        String DiancTreeJib = this.getTreeid();
        //System.out.println("jib:" + DiancTreeJib);
        if (DiancTreeJib == null || DiancTreeJib.equals("")) {
            DiancTreeJib = "0";
        }
        String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
        ResultSet rs = con.getResultSet(sqlJib.toString());

        try {
            while (rs.next()) {
                jib = rs.getInt("jib");
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }finally{
            con.Close();
        }
        return jib;
    }
    //	得到电厂树下拉框的电厂名称或者分公司,集团的名称
    public String getTreeDiancmc(String diancmcId) {
        if(diancmcId==null||diancmcId.equals("")){
            diancmcId="1";
        }
        String IDropDownDiancmc = "";
        JDBCcon cn = new JDBCcon();

        String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
        ResultSet rs = cn.getResultSet(sql_diancmc);
        try {
            while (rs.next()) {
                IDropDownDiancmc = rs.getString("mingc");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            cn.Close();
        }
        return IDropDownDiancmc;
    }
//	分公司下拉框
//	private boolean _fengschange = false;

    public IDropDownBean getFengsValue() {
        if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
            ((Visit) getPage().getVisit())
                    .setDropDownBean4((IDropDownBean) getFengsModel()
                            .getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean4();
    }

    public void setFengsValue(IDropDownBean Value) {
//		if (getFengsValue().getId() != Value.getId()) {
//			_fengschange = true;
//		}
        ((Visit) getPage().getVisit()).setDropDownBean4(Value);
    }

    public IPropertySelectionModel getFengsModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
            getFengsModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }

    public void setDiancxxModel(IPropertySelectionModel value) {
        ((Visit) getPage().getVisit()).setProSelectionModel4(value);
    }

    public void getFengsModels() {
        String sql;
        sql = "select id ,mingc from diancxxb where jib=2 order by id";
        setDiancxxModel(new IDropDownModel(sql,"中国大唐集团"));
    }

    //	得到系统信息表中配置的报表标题的单位名称
    public String getBiaotmc(){
        String biaotmc="";
        JDBCcon cn = new JDBCcon();
        String sql_biaotmc="select  zhi from xitxxb where mingc='报表标题单位名称'";
        ResultSet rs=cn.getResultSet(sql_biaotmc);
        try {
            while(rs.next()){
                biaotmc=rs.getString("zhi");
            }
            rs.close();
        } catch (SQLException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }finally{
            cn.Close();
        }

        return biaotmc;

    }
    //	 得到登陆人员所属电厂或分公司的名称
    public String getDiancmc(String diancId) {
        String diancmc = "";
        JDBCcon cn = new JDBCcon();
        //long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
        String sql_diancmc = "select d.quanc from diancxxb d where d.id="
                + diancId;
        ResultSet rs = cn.getResultSet(sql_diancmc);
        try {
            while (rs.next()) {
                diancmc = rs.getString("quanc");
            }
        } catch (SQLException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        cn.Close();
        return diancmc;

    }
    public String getDiancmc(){
        String[] str=getTreeid().split(",");
        if(str.length>1){
            return "组合电厂";
        }else{
            return getDiancmc(str[0]);
        }
    }
    //
    public Toolbar getToolbar() {
        return ((Visit)this.getPage().getVisit()).getToolbar();
    }
    public void setToolbar(Toolbar tb1) {
        ((Visit)this.getPage().getVisit()).setToolbar(tb1);
    }
    public String getToolbarScript() {
        return getToolbar().getRenderScript()+getOtherScript("diancTree");
    }

    //	初始化多选电厂树中的默认值
    private void initDiancTree(){
        Visit visit = (Visit) getPage().getVisit();
        String sql="SELECT ID\n" +
                "  FROM DIANCXXB\n" +
                " WHERE JIB > 2\n" +
                " START WITH ID = "+visit.getDiancxxb_id()+"\n" +
                "CONNECT BY FUID = PRIOR ID";

        JDBCcon con=new JDBCcon();
        ResultSetList rsl = con.getResultSetList(sql);
        String TreeID="";
        while(rsl.next()){
            TreeID+=rsl.getString("ID")+",";
        }
        if(TreeID.length()>1){
            TreeID=TreeID.substring(0, TreeID.length()-1);
            setTreeid(TreeID);
        }else{
            setTreeid(visit.getDiancxxb_id() + "");
        }
        rsl.close();
        con.Close();
    }

    //	增加电厂多选树的级联
    public String getOtherScript(String treeid){
        String str=" var "+treeid+"_history=\"\";\n"+
                treeid+"_treePanel.on(\"checkchange\",function(node,checked){\n" +
                "    if(checked){\n" +
                "      addNode(node);\n" +
                "    }else{\n" +
                "      subNode(node);\n" +
                "    }\n" +
                "    node.expand();\n" +
                "    node.attributes.checked = checked;\n" +
                "    node.eachChild(function(child) {\n" +
                "      if(child.attributes.checked != checked){\n" +
                "        if(checked){\n" +
                "          addNode(child);\n" +
                "        }else{\n" +
                "          subNode(child);\n" +
                "        }\n" +
                "        child.ui.toggleCheck(checked);\n" +
                "              child.attributes.checked = checked;\n" +
                "              child.fireEvent('checkchange', child, checked);\n" +
                "      }\n" +
                "    });\n" +
                "  },"+treeid+"_treePanel);\n" +
                "  function addNode(node){\n" +
                "    var history = '+,'+node.id+\";\";\n" +
                "    writesrcipt(node,history);\n" +
                "  }\n" +
                "\n" +
                "  function subNode(node){\n" +
                "    var history = '-,'+node.id+\";\";\n" +
                "    writesrcipt(node,history);\n" +
                "  }\n" +
                "function writesrcipt(node,history){\n" +
                "\t\tif("+treeid+"_history==\"\"){\n" +
                "\t\t\t"+treeid+"_history = history;\n" +
                //" 	   document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n"	+
                "\t\t}else{\n" +
                "\t\t\tvar his = "+treeid+"_history.split(\";\");\n" +
                "\t\t\tvar reset = false;\n" +
                "\t\t\tfor(i=0;i<his.length;i++){\n" +
                "\t\t\t\tif(his[i].split(\",\")[1] == node.id){\n" +
                "\t\t\t\t\this[i] = \"\";\n" +
                "\t\t\t\t\treset = true;\n" +
                "\t\t\t\t\tbreak;\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\tif(reset){\n" +
                "\t\t\t  "+treeid+"_history = his.join(\";\");\n" +
                //"      	 document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" +
                "    }else{\n" +
                "      	 "+treeid+"_history += history;\n" +
                //"        document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" +
                "    }\n" +
                "  }\n" +
                "\n" +
                "}";
        return str;
    }
}