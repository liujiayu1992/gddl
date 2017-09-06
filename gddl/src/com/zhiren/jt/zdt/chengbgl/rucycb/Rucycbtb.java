package com.zhiren.jt.zdt.chengbgl.rucycb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
* 时间：2009-03-26 
* 作者： ll
* 修改内容：1、去掉审核和回退功能。
* 		   2、添加判断循环累计的时间，如果本月没有数据，循环将结束。
*/  
/* 
* 时间：2009-05-4
* 作者： ll
* 修改内容：1、把MJ修改为MJ/kg。
* 		   
*/ 
/* 
* 时间：2009-05-18
* 作者： ll
* 修改内容：由于平圩电厂是一厂两制，平圩一电和平圩二电同在一个厂级系统中填报报表数据。
* 		   当两个电厂填报同一个月报页面时页面出错。所以在beginResponse()中增加了用户级别为电厂级，
  判断登陆电厂与电厂树是否一致，并重新加载刷新页面。
* 		   
*/
public class Rucycbtb extends BasePage implements PageValidateListener {
    private String msg = "";

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = MainGlobal.getExtMessageBox(msg, false);
        ;
    }

    protected void initialize() {
        super.initialize();
        setMsg("");
        setTbmsg(null);
    }

    private String tbmsg;

    public String getTbmsg() {
        return tbmsg;
    }

    public void setTbmsg(String tbmsg) {
        this.tbmsg = tbmsg;
    }

    // 页面变化记录
    private String Change;

    public String getChange() {
        return Change;
    }

    public void setChange(String change) {
        Change = change;
    }

    public int getDataColumnCount() {
        int count = 0;
        for (int c = 0; c < getExtGrid().getGridColumns().size(); c++) {
            if (((GridColumn) getExtGrid().getGridColumns().get(c)).coltype == GridColumn.ColType_default) {
                count++;
            }
        }
        return count;
    }

    //	日期控件
//	 年份
    private static IPropertySelectionModel _NianfModel;

    public IPropertySelectionModel getNianfModel() {
        if (_NianfModel == null) {
            getNianfModels();
        }
        return _NianfModel;
    }

    private IDropDownBean _NianfValue;

    public IDropDownBean getNianfValue() {
        if (_NianfValue == null) {
            for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
                Object obj = getNianfModel().getOption(i);
                if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
                        .getId()) {
                    _NianfValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _NianfValue;
    }

    public boolean nianfchanged;

    public void setNianfValue(IDropDownBean Value) {
        if (_NianfValue != Value) {
            nianfchanged = true;
        }
        _NianfValue = Value;
    }

    public IPropertySelectionModel getNianfModels() {
        List listNianf = new ArrayList();
        int i;
        for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
            listNianf.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _NianfModel = new IDropDownModel(listNianf);
        return _NianfModel;
    }

    public void setNianfModel(IPropertySelectionModel _value) {
        _NianfModel = _value;
    }

    // 月份
    public boolean Changeyuef = false;

    private static IPropertySelectionModel _YuefModel;

    public IPropertySelectionModel getYuefModel() {
        if (_YuefModel == null) {
            getYuefModels();
        }
        return _YuefModel;
    }

    private IDropDownBean _YuefValue;

//-----------------------修改后月份显示上个月-----------------------------------

    public IDropDownBean getYuefValue() {
        if (_YuefValue == null) {
            int _yuef = DateUtil.getMonth(new Date());
            if (_yuef == 1) {
                _yuef = 12;
            } else {
                _yuef = _yuef - 1;
            }
            for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
                Object obj = getYuefModel().getOption(i);
                if (_yuef == ((IDropDownBean) obj).getId()) {
                    _YuefValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _YuefValue;
    }


    public void setYuefValue(IDropDownBean Value) {
        long id = -2;
        if (_YuefValue != null) {
            id = getYuefValue().getId();
        }
        if (Value != null) {
            if (Value.getId() != id) {
                Changeyuef = true;
            } else {
                Changeyuef = false;
            }
        }
        _YuefValue = Value;

    }

    public IPropertySelectionModel getYuefModels() {
        List listYuef = new ArrayList();
        // listYuef.add(new IDropDownBean(-1,"请选择"));
        for (int i = 1; i < 13; i++) {
            listYuef.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _YuefModel = new IDropDownModel(listYuef);
        return _YuefModel;
    }

    public void setYuefModel(IPropertySelectionModel _value) {
        _YuefModel = _value;
    }

    public String getValueSql(GridColumn gc, String value) {
        if ("string".equals(gc.datatype)) {
            if (gc.combo != null) {
                if (gc.returnId) {
                    return "" + gc.combo.getBeanId(value);
                } else {
                    return "'" + value + "'";
                }
            } else {
                return "'" + value + "'";
            }

        } else if ("date".equals(gc.datatype)) {
            return "to_date('" + value + "','yyyy-mm-dd')";
        } else {
            return value;
        }
    }

    private void Save() {

//		 工具栏的年份和月份下拉框
        long intyear;
        if (getNianfValue() == null) {
            intyear = DateUtil.getYear(new Date());
        } else {
            intyear = getNianfValue().getId();
        }
        long intMonth;
        if (getYuefValue() == null) {
            intMonth = DateUtil.getMonth(new Date());
        } else {
            intMonth = getYuefValue().getId();
        }
        // 当月份是1的时候显示01,
        String StrMonth = "";
        if (intMonth < 10) {

            StrMonth = "0" + intMonth;
        } else {
            StrMonth = "" + intMonth;
        }
        String strdate = intyear + "-" + StrMonth + "-01";
        //--------------------------------
//		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
        JDBCcon con = new JDBCcon();
        Visit visit = (Visit) this.getPage().getVisit();
        ResultSetList drsl = visit.getExtGrid1().getDeleteResultSet(getChange());
        StringBuffer sql_delete = new StringBuffer("begin \n");

        while (drsl.next()) {
            sql_delete.append("delete from ").append("rucycbb").append(
                    " where id =").append(drsl.getLong("id")).append(";\n");
            sql_delete.append("delete from ").append("rucycbb").append(
                    " where id =").append("(select distinct sl.id from rucycbb sl,pinzb pz where sl.pinzb_id=pz.id and sl.riq=to_date('" + strdate + "','yyyy-mm-dd') and sl.fenx='累计' and sl.diancxxb_id=" + visit.getDiancxxb_id() +
                    "and pinzb_id=(select id from pinzb where mingc='" + drsl.getString("pinzb_id") + "'))").append(";\n");
        }
        sql_delete.append("end;");
        if (sql_delete.length() > 11) {
            con.getUpdate(sql_delete.toString());
        }
        ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
        StringBuffer sql = new StringBuffer();
        while (rsl.next()) {
            sql.delete(0, sql.length());
            sql.append("begin \n");
            long id = 0;
            if ("0".equals(rsl.getString("ID"))) {
                id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
                sql.append("insert into rucycbb("
                        + "id, diancxxb_id, riq, fenx, pinzb_id, shul, hanszhj, buhszhj, youj, yous, yunf, yunfs, yunzf, qitfy, youfrl, zhebmdj)values("
                        + id
                        + ","
                        + visit.getDiancxxb_id() + ",to_date('"
                        + strdate
                        + "','yyyy-mm-dd'),'本月',(select id from pinzb where mingc='" + rsl.getString("pinzb_id") + "'),"
                        + rsl.getDouble("shul") + ","
                        + rsl.getDouble("hanszhj") + ","
                        + rsl.getDouble("buhszhj") + ","
                        + rsl.getDouble("youj") + ","
                        + rsl.getDouble("yous") + ","
                        + rsl.getDouble("yunf") + ","
                        + rsl.getDouble("yunfs") + ","
                        + rsl.getDouble("yunzf") + ","
                        + rsl.getDouble("qitfy") + ","
                        + rsl.getDouble("youfrl") + ","
                        + rsl.getDouble("zhebmdj") + ");\n");
            } else {
                sql.append("update rucycbb set shul="
                        + rsl.getDouble("shul") + ",hanszhj="
                        + rsl.getDouble("hanszhj") + ",buhszhj="
                        + rsl.getDouble("buhszhj") + ",youj="
                        + rsl.getDouble("youj") + ",yous="
                        + rsl.getDouble("yous") + ",yunf="
                        + rsl.getDouble("yunf") + ",yunfs="
                        + rsl.getDouble("yunfs") + ",yunzf="
                        + rsl.getDouble("yunzf") + ",qitfy="
                        + rsl.getDouble("qitfy") + ",youfrl="
                        + rsl.getDouble("youfrl") + ",zhebmdj="
                        + rsl.getDouble("zhebmdj") + ",pinzb_id=(select id from pinzb where mingc='"
                        + rsl.getString("pinzb_id")
                        + "')  where id=" + rsl.getLong("id") + ";\n");

            }
            sql.append("end;");
            con.getUpdate(sql.toString());


            //添加累计
            String sqllj = "";
            int i = 0;
            for (i = 0; i <= 12 - intMonth; i++) {

                boolean jzyf = false;//设置截止月份的boolean
                String pdyf = "select rcy.id as id from rucycbb rcy where rcy.diancxxb_id=" + visit.getDiancxxb_id() + " and rcy.riq=to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd')";
                ResultSet pdyfsql = con.getResultSet(pdyf);
                try {
                    //判断循环月份是否有数据，有数据jzyf：true,否则jzyf：false停止循环。
                    if (pdyfsql.next()) {
                        jzyf = true;
                    } else {
                        continue;
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } finally {
                    con.Close();
                }
                if (jzyf == true) {//该循环月份有数据，循环累计执行；该循环月份有数据，则循环结束。
                    if (intMonth + i != 1) {
                        sqllj =
                                "select diancxxb_id,pinzb_id,sum(nvl(s.shul,0)) as shul, \n"
                                        + "       decode(sum(nvl(s.shul,0)),0,0,round(sum(nvl(s.hanszhj,0)*nvl(s.shul,0))/sum(nvl(s.shul,0)),2)) as hanszhj, \n"
                                        + "       decode(sum(nvl(s.shul,0)),0,0,round(sum(nvl(s.buhszhj,0)*nvl(s.shul,0))/sum(nvl(s.shul,0)),2)) as buhszhj, \n"
                                        + "       decode(sum(nvl(s.shul,0)),0,0,round(sum(nvl(s.youj,0)*nvl(s.shul,0))/sum(nvl(s.shul,0)),2)) as youj, \n"
                                        + "       decode(sum(nvl(s.shul,0)),0,0,round(sum(nvl(s.yous,0)*nvl(s.shul,0))/sum(nvl(s.shul,0)),2)) as yous, \n"
                                        + "       decode(sum(nvl(s.shul,0)),0,0,round(sum(nvl(s.yunf,0)*nvl(s.shul,0))/sum(nvl(s.shul,0)),2)) as yunf, \n"
                                        + "       decode(sum(nvl(s.shul,0)),0,0,round(sum(nvl(s.yunfs,0)*nvl(s.shul,0))/sum(nvl(s.shul,0)),2)) as yunfs, \n"
                                        + "       decode(sum(nvl(s.shul,0)),0,0,round(sum(nvl(s.yunzf,0)*nvl(s.shul,0))/sum(nvl(s.shul,0)),2)) as yunzf, \n"
                                        + "       decode(sum(nvl(s.shul,0)),0,0,round(sum(nvl(s.qitfy,0)*nvl(s.shul,0))/sum(nvl(s.shul,0)),2)) as qitfy, \n"
                                        + "       decode(sum(nvl(s.shul,0)),0,0,round(sum(nvl(s.youfrl,0)*nvl(s.shul,0))/sum(nvl(s.shul,0)),3)) as youfrl, \n"

                                        + "		  decode(sum(nvl(s.shul,0)),0,0,decode(sum(nvl(s.shul,0)*nvl(s.youfrl,0)),0,0,"
                                        + " 		round(sum(nvl(s.zhebmdj,0)*(nvl(s.shul,0)*nvl(s.youfrl,0)/29.271))/(sum(nvl(s.shul,0)*nvl(s.youfrl,0))/29.271),2))) as zhebmdj "
                                        + "  from rucycbb s,pinzb pz\n" +
                                        "  where (( s.riq= to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd') and s.fenx = '本月') or\n" +
                                        "         (s.riq=add_months( to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd'), -1) and s.fenx = '累计')) and s.pinzb_id =pz.id and pz.id=(select id from pinzb where mingc='" + rsl.getString("pinzb_id") + "') and s.diancxxb_id =" + visit.getDiancxxb_id() +
                                        "  group by (s.diancxxb_id,s.pinzb_id)\n";
                    } else {
                        sqllj =
                                "select s.id,s.fenx,s.pinzb_id,s.shul,s.hanszhj,s.buhszhj,s.youj,s.yous,s.yunf,s.yunfs,s.yunzf,s.qitfy,s.youfrl,s.zhebmdj \n" +
                                        " from rucycbb s,pinzb pz where s.riq=to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd') and s.pinzb_id =pz.id and pz.id=(select id from pinzb where mingc='" + rsl.getString("pinzb_id")
                                        + "') and s.diancxxb_id=" + visit.getDiancxxb_id() + "and s.fenx='本月' order by s.fenx";
                    }
                    ResultSetList rsllj = con.getResultSetList(sqllj);
                    if (rsllj.next()) {
                        StringBuffer strsqllj = new StringBuffer("begin \n");
                        long yuelj_id = 0;
                        rsllj.beforefirst();
                        while (rsllj.next()) {
                            //		 		 获取累计当月累计状态
                            String shzt = "select s.zhuangt as zhuangt from rucycbb s,pinzb pz where s.diancxxb_id=" + visit.getDiancxxb_id() + " and s.riq=to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd') and s.fenx='累计' and s.pinzb_id =pz.id and pz.id=(select id from pinzb where mingc='" + rsl.getString("pinzb_id") + "') \n";
                            ResultSetList shenhzt = con.getResultSetList(shzt);
                            long zhuangt = 0;
                            while (shenhzt.next()) {
                                zhuangt = shenhzt.getLong("zhuangt");
                            }

                            strsqllj.append("delete from ").append("rucycbb").append(
                                    " where id =").append("(select distinct sl.id from rucycbb sl where sl.riq=to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd') and sl.fenx='累计' and sl.diancxxb_id =" + visit.getDiancxxb_id() +
                                    "and pinzb_id=" + rsllj.getString("pinzb_id") + ")").append(";\n");
                            yuelj_id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));

                            strsqllj.append("insert into rucycbb("
                                    + "id,diancxxb_id,riq,fenx,pinzb_id,shul, hanszhj, buhszhj, youj, yous, yunf, yunfs, yunzf, qitfy, youfrl, zhebmdj,zhuangt)values("
                                    + yuelj_id
                                    + ","
                                    + visit.getDiancxxb_id()
                                    + ",to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd'),'累计'," + rsllj.getString("pinzb_id") + ","
                                    + rsllj.getDouble("shul") + ","
                                    + rsllj.getDouble("hanszhj") + ","
                                    + rsllj.getDouble("buhszhj") + ","
                                    + rsllj.getDouble("youj") + ","
                                    + rsllj.getDouble("yous") + ","
                                    + rsllj.getDouble("yunf") + ","
                                    + rsllj.getDouble("yunfs") + ","
                                    + rsllj.getDouble("yunzf") + ","
                                    + rsllj.getDouble("qitfy") + ","
                                    + rsllj.getDouble("youfrl") + ","
                                    + rsllj.getDouble("zhebmdj") + ","
                                    + zhuangt + ");\n");
                        }
                        strsqllj.append("end;");
                        con.getInsert(strsqllj.toString());
                    }

                }
            }
        }
        con.Close();
        setMsg("保存成功!");
    }

    //	-----------------------------------------------------------
    private boolean _SaveChick = false;

    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }

    private boolean _RefreshChick = false;

    public void RefreshButton(IRequestCycle cycle) {
        _RefreshChick = true;
    }

    public void submit(IRequestCycle cycle) {
        if (_SaveChick) {
            _SaveChick = false;
            Save();
            getSelectData();
        }
        if (_RefreshChick) {
            _RefreshChick = false;
            getSelectData();
        }
    }

    public void getSelectData() {

        JDBCcon con = new JDBCcon();
        Visit visit = (Visit) getPage().getVisit();
        String zhuangt = "";
        if (visit.isShifsh() == true) {
            if (visit.getRenyjb() == 3) {
                zhuangt = "";
            } else if (visit.getRenyjb() == 2) {
                zhuangt = " and (y.zhuangt=1 or y.zhuangt=2)";
            } else if (visit.getRenyjb() == 1) {
                zhuangt = " and y.zhuangt=2";
            }
        }
        String str = "";

        long intyear;
        if (getNianfValue() == null) {
            intyear = DateUtil.getYear(new Date());
        } else {
            intyear = getNianfValue().getId();
        }
        long intMonth;
        if (getYuefValue() == null) {
            intMonth = DateUtil.getMonth(new Date());
        } else {
            intMonth = getYuefValue().getId();
        }
        //当月份是1的时候显示01,
        String StrMonth = "";
        if (intMonth < 10) {

            StrMonth = "0" + intMonth;
        } else {
            StrMonth = "" + intMonth;
        }

        int treejib = this.getDiancTreeJib();
        if (treejib == 1) {// 选集团时刷新出所有的电厂
            str = " and dc.jib=3 ";
        } else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
            str = "and dc.fuid = " + getTreeid();
        } else if (treejib == 3) {// 选电厂只刷新出该电厂
            str = "and dc.id = " + getTreeid() + "";
        }
//		---------------------------------------------------------------------	
//		if(isReture) {
//		setMsg("如果有需要回退信息时，请选择所要回退的公司或电厂名称！");
//		}	
//	----------------------------------------------------------------------

        String sql =

                "select nvl(x.id,0) as id,\n" +
                        "decode(x.fenx,null,'本月',x.fenx) as fenx,\n" +
                        "     y.pzb_id as pinzb_id,nvl(x.shul,0) as shul,nvl(x.youfrl,0) as youfrl,nvl(x.hanszhj,0) as hanszhj,nvl(x.buhszhj,0) as buhszhj,\n" +
                        "     nvl(x.youj,0) as youj,nvl(x.yous,0) as yous,nvl(x.yunf,0) as yunf,nvl(x.yunfs,0) as yunfs,nvl(x.yunzf,0) as yunzf,\n" +
                        "     nvl(x.qitfy,0) as qitfy,nvl(x.zhebmdj,0) as zhebmdj,nvl(x.zhuangt,0) as zhuangt\n" +
                        "from (select nvl(sj.id,0) as id,decode(sj.fenx,null,'本月',sj.fenx) as fenx,\n" +
                        "     sj.pinzb_id as pinzb_id,nvl(sj.shul,0) as shul,nvl(sj.youfrl,0) as youfrl,nvl(sj.hanszhj,0) as hanszhj,nvl(sj.buhszhj,0) as buhszhj,\n" +
                        "     nvl(sj.youj,0) as youj,nvl(sj.yous,0) as yous,nvl(sj.yunf,0) as yunf,nvl(sj.yunfs,0) as yunfs,nvl(sj.yunzf,0) as yunzf,nvl(sj.qitfy,0) as qitfy,nvl(sj.zhebmdj,0) as zhebmdj,sj.zhuangt\n" +
                        " from diancxxb dc,(select nvl(y.id,0) as id,y.diancxxb_id,y.riq,y.fenx,p.mingc as pinzb_id,y.shul,y.hanszhj,y.buhszhj,y.youj,y.yous,y.yunf,y.yunfs,y.yunzf,y.qitfy,y.youfrl,y.zhebmdj,y.zhuangt\n" +
                        "             from rucycbb y,pinzb p where  y.riq=to_date('" + intyear + "-" + StrMonth + "-01','yyyy-mm-dd') and y.fenx='本月' and y.pinzb_id(+)=p.id " + zhuangt + " ) sj\n" +
                        " where dc.id=sj.diancxxb_id(+) " + str + " order by dc.xuh )x,\n" +
                        "\n" +
                        "     (  select pzb.mingc as pzb_id from rucycbb rcy,pinzb pzb,diancxxb dc " +
                        "				where rcy.riq=to_date('" + intyear + "-" + StrMonth + "-01','yyyy-mm-dd') " +
                        "	         and rcy.fenx='本月' and rcy.pinzb_id=pzb.id and rcy.diancxxb_id=dc.id " + str + "\n" +
                        "       union\n" +
                        "        select pzb.mingc as pzb_id from rucycbb rcy,pinzb pzb,diancxxb dc " +
                        "				where rcy.riq=add_months(to_date('" + intyear + "-" + StrMonth + "-01','yyyy-mm-dd'),-1)" +
                        "			 and rcy.fenx='本月' and rcy.pinzb_id=pzb.id and rcy.diancxxb_id=dc.id " + str + "\n" +
                        "      ) y\n" +
                        " where x.pinzb_id(+)=y.pzb_id";

        ResultSetList rsl = con.getResultSetList(sql);

        boolean yincan = false;
        while (rsl.next()) {
            if (visit.getRenyjb() == 3) {
                if (rsl.getLong("zhuangt") == 0) {
                    yincan = false;
                } else {
                    yincan = true;
                }
            }
            if (visit.getRenyjb() == 1 || visit.getRenyjb() == 2) {
                yincan = true;
            }
        }
        rsl.beforefirst();
        ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
        egu.setTableName("Rucycbb");
//		egu.setWidth(990);
        egu.getColumn("id").setHeader("RucycbtbB_ID");
        egu.getColumn("id").setEditor(null);
        egu.getColumn("id").setHidden(true);
//		egu.getColumn("diancxxb_id").setHeader("单位名称");
//		egu.getColumn("diancxxb_id").setEditor(null);
//		
//		egu.getColumn("riq").setHeader("日期");
//		egu.getColumn("riq").setHidden(true);
//		egu.getColumn("riq").setEditor(null);
        egu.getColumn("fenx").setHeader("分项");
        egu.getColumn("fenx").setHidden(true);
        egu.getColumn("fenx").setEditor(null);
        egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
        egu.getColumn("pinzb_id").setWidth(80);
        egu.getColumn("pinzb_id").setEditor(null);
        egu.getColumn("shul").setHeader("数量<br>(吨)");
        egu.getColumn("shul").setDefaultValue(0 + "");

        egu.getColumn("hanszhj").setHeader("含税综合价<br>(元/吨)");
        egu.getColumn("hanszhj").setEditor(null);
        egu.getColumn("hanszhj").setDefaultValue(0 + "");
        egu.getColumn("buhszhj").setHeader("不含税综合价<br>(元/吨)");
        egu.getColumn("buhszhj").setEditor(null);
        egu.getColumn("buhszhj").setDefaultValue(0 + "");
        egu.getColumn("youj").setHeader("油价(含税)<br>(元/吨)");
        egu.getColumn("youj").setDefaultValue(0 + "");
        egu.getColumn("yous").setHeader("油税<br>(元/吨)");
        egu.getColumn("yous").setDefaultValue(0 + "");
        egu.getColumn("yunf").setHeader("运费<br>(元/吨)");
        egu.getColumn("yunf").setDefaultValue(0 + "");
        egu.getColumn("yunfs").setHeader("运费税<br>(元/吨)");
        egu.getColumn("yunfs").setDefaultValue(0 + "");
        egu.getColumn("yunzf").setHeader("运杂费<br>(元/吨)");
        egu.getColumn("yunzf").setDefaultValue(0 + "");
        egu.getColumn("qitfy").setHeader("其他运费<br>(元/吨)");
        egu.getColumn("qitfy").setDefaultValue(0 + "");
        egu.getColumn("youfrl").setHeader("油发热量(MJ/kg)");
        egu.getColumn("youfrl").setDefaultValue(0 + "");
        egu.getColumn("zhebmdj").setHeader("折标煤单价<br>(元/吨)");
        egu.getColumn("zhebmdj").setEditor(null);
        egu.getColumn("zhebmdj").setDefaultValue(0 + "");
        egu.getColumn("zhuangt").setHeader("状态");
        egu.getColumn("zhuangt").setHidden(true);
        egu.getColumn("zhuangt").setEditor(null);

//		egu.getColumn("diancxxb_id").setWidth(120);
        egu.getColumn("shul").setWidth(80);
        egu.getColumn("hanszhj").setWidth(70);
        egu.getColumn("buhszhj").setWidth(80);
        egu.getColumn("youj").setWidth(70);
        egu.getColumn("yous").setWidth(65);
        egu.getColumn("yunf").setWidth(80);
        egu.getColumn("yunfs").setWidth(70);
        egu.getColumn("yunzf").setWidth(70);
        egu.getColumn("qitfy").setWidth(70);
        egu.getColumn("youfrl").setWidth(80);
        egu.getColumn("zhebmdj").setWidth(70);

        egu.getColumn("id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
//		egu.getColumn("riq").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
//		egu.getColumn("diancxxb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");

        egu.getColumn("hanszhj").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
        egu.getColumn("buhszhj").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
        egu.getColumn("zhebmdj").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");

        egu.setGridType(ExtGridUtil.Gridstyle_Edit);
        egu.addPaging(25);


        //品种下拉框
        egu.getColumn("pinzb_id").setEditor(new ComboBox());
        egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
                new IDropDownModel("select id, mingc from pinzb where leib='油'"));
        egu.getColumn("pinzb_id").setReturnId(true);
        egu.getColumn("pinzb_id").setDefaultValue("柴油");
//		设定列的小数位
        ((NumberField) egu.getColumn("shul").editor).setDecimalPrecision(3);
        ((NumberField) egu.getColumn("youfrl").editor).setDecimalPrecision(3);
        ((NumberField) egu.getColumn("youj").editor).setDecimalPrecision(2);
        ((NumberField) egu.getColumn("yous").editor).setDecimalPrecision(2);
        ((NumberField) egu.getColumn("yunf").editor).setDecimalPrecision(2);
        ((NumberField) egu.getColumn("yunfs").editor).setDecimalPrecision(2);
        ((NumberField) egu.getColumn("yunzf").editor).setDecimalPrecision(2);
        ((NumberField) egu.getColumn("qitfy").editor).setDecimalPrecision(2);
//		---------------页面js的计算开始------------------------------------------
        StringBuffer sb = new StringBuffer();

        sb.append("gridDiv_grid.on('afteredit',function(e){\n");
        sb.append("if(e.field == 'YOUJ' || e.field == 'YUNF' || e.field == 'YUNZF' || e.field == 'QITFY')\n");
//		sb.append("{e.record.set('HANSZHJ',parseFloat(e.record.get('YOUJ'))+parseFloat(e.record.get('YUNF'))+parseFloat(e.record.get('YUNZF'))+parseFloat(e.record.get('QITFY')));\n");
//		sb.append("e.record.set('BUHSZHJ',parseFloat(e.record.get('YOUJ'))+parseFloat(e.record.get('YUNF'))+parseFloat(e.record.get('YUNZF'))+parseFloat(e.record.get('QITFY'))-parseFloat(e.record.get('YOUS'))-parseFloat(e.record.get('YUNFS')));\n");
        sb.append("{e.record.set('HANSZHJ',Round(parseFloat(e.record.get('YOUJ'))+parseFloat(e.record.get('YUNF'))+parseFloat(e.record.get('YUNZF'))+parseFloat(e.record.get('QITFY')),2));\n");
        sb.append("e.record.set('BUHSZHJ',Round(parseFloat(e.record.get('YOUJ'))+parseFloat(e.record.get('YUNF'))+parseFloat(e.record.get('YUNZF'))+parseFloat(e.record.get('QITFY'))-parseFloat(e.record.get('YOUS'))-parseFloat(e.record.get('YUNFS')),2));\n");
        sb.append("		if(parseFloat(e.record.get('YOUFRL'))!=0){\n");
        sb.append("			e.record.set('ZHEBMDJ',Round((parseFloat(e.record.get('YOUJ'))+parseFloat(e.record.get('YUNF'))+parseFloat(e.record.get('YUNZF'))+parseFloat(e.record.get('QITFY')))*29.271/parseFloat(e.record.get('YOUFRL')),2));\n");
        sb.append("		}}");
        sb.append("if(e.field == 'YOUS' || e.field == 'YUNFS')\n");
        sb.append("{e.record.set('BUHSZHJ',parseFloat(e.record.get('HANSZHJ'))-parseFloat(e.record.get('YOUS'))-parseFloat(e.record.get('YUNFS')));}\n");
        sb.append("if(e.field == 'YOUFRL'){if(parseFloat(e.record.get('YOUFRL'))!=0){\n");
        sb.append("		e.record.set('ZHEBMDJ',Round((parseFloat(e.record.get('YOUJ'))+parseFloat(e.record.get('YUNF'))+parseFloat(e.record.get('YUNZF'))+parseFloat(e.record.get('QITFY')))*29.271/parseFloat(e.record.get('YOUFRL')),2));}}\n");
        sb.append("});\n");
        egu.addOtherScript(sb.toString());

//		 工具栏
        egu.addTbarText("年份:");
        ComboBox comb1 = new ComboBox();
        comb1.setTransform("NIANF");
        comb1.setId("NIANF");//和自动刷新绑定
        comb1.setLazyRender(true);//动态绑定
        comb1.setWidth(60);
        egu.addToolbarItem(comb1.getScript());

        egu.addTbarText("-");// 设置分隔符

        egu.addTbarText("月份:");
        ComboBox comb2 = new ComboBox();
        comb2.setTransform("YUEF");
        comb2.setId("YUEF");//和自动刷新绑定
        comb2.setLazyRender(true);//动态绑定
        comb2.setWidth(50);
        egu.addToolbarItem(comb2.getScript());
        egu.addTbarText("-");// 设置分隔符
        // 电厂树
        egu.addTbarText("单位名称:");
        ExtTreeUtil etu = new ExtTreeUtil("diancTree",
                ExtTreeUtil.treeWindowType_Dianc,
                ((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
        setTree(etu);
        egu.addTbarTreeBtn("diancTree");
        egu.addTbarText("-");
//		刷新按钮
        StringBuffer rsb = new StringBuffer();
        rsb.append("function (){")
                .append("document.getElementById('RefreshButton').click();}");
        GridButton gbr = new GridButton("刷新", rsb.toString());
        gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
        egu.addTbarBtn(gbr);
        egu.addTbarText("-");
        if (visit.getRenyjb() == 3) {
            if (yincan == false) {
                egu.addToolbarButton(GridButton.ButtonType_Delete, "");
                egu.addTbarText("-");
                //		添加按钮
                egu.addToolbarButton(GridButton.ButtonType_Insert, null);
                egu.addTbarText("-");
//				egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
                egu.addToolbarButton(GridButton.ButtonType_SaveAll, "SaveButton");
            }
        }

        setExtGrid(egu);
        con.Close();
    }

    public ExtGridUtil getExtGrid() {
        return ((Visit) this.getPage().getVisit()).getExtGrid1();
    }

    public void setExtGrid(ExtGridUtil extgrid) {
        ((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
    }

    public String getGridScript() {
        return getExtGrid().getGridScript();
    }

    public String getGridHtml() {
        return getExtGrid().getHtml();
    }

    public void pageValidate(PageEvent arg0) {
        String PageName = arg0.getPage().getPageName();
        String ValPageName = Login.ValidateLogin(arg0.getPage());
        if (!PageName.equals(ValPageName)) {
            ValPageName = Login.ValidateAdmin(arg0.getPage());
            if (!PageName.equals(ValPageName)) {
                IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
                throw new PageRedirectException(ipage);
            }
        }
    }

    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
        Visit visit = (Visit) getPage().getVisit();
        if (!visit.getActivePageName().toString().equals(
                this.getPageName().toString())) {
            // 在此添加，在页面第一次加载时需要置为空的变量或方法
            visit.setActivePageName(getPageName().toString());
            visit.setList1(null);
            this.setNianfValue(null);
            this.getNianfModels();
            this.setYuefValue(null);
            this.getYuefModels();
            visit.setShifsh(true);
            visit.setList1(null);
            visit.setString1(null);
            visit.setString2(null);
            visit.setString3(null);
            visit.setDefaultTree(null);
            this.setTreeid(null);
        }
        if (visit.getRenyjb() == 3) {
            if (!this.getTreeid().equals(visit.getDiancxxb_id() + "")) {
                visit.setActivePageName(getPageName().toString());
                visit.setList1(null);
                this.setNianfValue(null);
                this.getNianfModels();
                this.setYuefValue(null);
                this.getYuefModels();
                visit.setShifsh(true);
                visit.setList1(null);
                visit.setString1(null);
                visit.setString2(null);
                visit.setString3(null);
                visit.setDefaultTree(null);
                this.setTreeid(null);
            }
        }
        getSelectData();
    }


    boolean treechange = false;

    private String treeid = "";

    public String getTreeid() {

        if (treeid == null || treeid.equals("")) {

            treeid = String.valueOf(((Visit) getPage().getVisit())
                    .getDiancxxb_id());
        }
        return treeid;
    }

    public void setTreeid(String treeid) {
        if (treeid != null) {
            if (!this.treeid.equals(treeid)) {

                ((Visit) getPage().getVisit()).setboolean3(true);
                this.treeid = treeid;
            }
        }
        this.treeid = treeid;
    }

    public ExtTreeUtil getTree() {
        return ((Visit) this.getPage().getVisit()).getExtTree1();
    }

    public void setTree(ExtTreeUtil etu) {
        ((Visit) this.getPage().getVisit()).setExtTree1(etu);
    }

    public String getTreeHtml() {
        return getTree().getWindowTreeHtml(this);
    }

    public String getTreeScript() {
        return getTree().getWindowTreeScript();
    }

    // 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
    public int getDiancTreeJib() {
        JDBCcon con = new JDBCcon();
        int jib = -1;
        String DiancTreeJib = this.getTreeid();
        // System.out.println("jib:" + DiancTreeJib);
        if (DiancTreeJib == null || DiancTreeJib.equals("")) {
            DiancTreeJib = "0";
        }
        String sqlJib = "select d.jib from diancxxb d where d.id="
                + DiancTreeJib;
        ResultSet rs = con.getResultSet(sqlJib.toString());

        try {
            while (rs.next()) {
                jib = rs.getInt("jib");
            }
        } catch (SQLException e) {

            e.printStackTrace();
        } finally {
            con.Close();
        }

        return jib;
    }

    public String getNianf() {
        return ((Visit) getPage().getVisit()).getString1();
    }

    public void setNianf(String value) {
        ((Visit) getPage().getVisit()).setString1(value);
    }

    public String getYuef() {
        int intYuef = Integer.parseInt(((Visit) getPage().getVisit()).getString3());
        if (intYuef < 10) {
            return "0" + intYuef;
        } else {
            return ((Visit) getPage().getVisit()).getString3();
        }
    }

    public void setYuef(String value) {
        ((Visit) getPage().getVisit()).setString3(value);
    }

    public void setRiq() {
        setNianf(getNianfValue().getValue());
        setYuef(getYuefValue().getValue());
    }

}