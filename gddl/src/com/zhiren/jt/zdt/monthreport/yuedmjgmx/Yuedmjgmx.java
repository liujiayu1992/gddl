package com.zhiren.jt.zdt.monthreport.yuedmjgmx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zhiren.common.*;
import com.zhiren.common.ext.form.Field;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
* 时间：2009-03-26 
* 作者： ll
* 修改内容：1、去掉审核和回退功能。
* 		   2、添加判断循环累计的时间，如果本月没有数据，循环将结束。
* 		   3、页面刷新时，标煤单价进行计算，不直接从库中取数。
*/  
/* 
* 时间：2009-04-09 
* 作者： ll
* 修改内容：修改重新计算添加累计时，yuetjkjb_id的值取本月的yuetjkjb_id。
* 		   
*/  
/* 
* 时间：2009-04-10 
* 作者： ll
* 修改内容：
* 		   修改重新计算添加累计时，产生有相同条件的yuetjkjb_id值的bug问题。
*/ 
/* 
* 时间：2009-5-4 
* 作者： ll
* 修改内容：
* 		   修改查询sql中除数为0的bug问题。
/* 
* 时间：2009-5-4 
* 作者： sy
* 修改内容：
* 		  计算biaomdj的sql 中decode拼写错了哦。
*/ 
/* 
* 时间：2009-05-18
* 作者： ll
* 修改内容：由于平圩电厂是一厂两制，平圩一电和平圩二电同在一个厂级系统中填报报表数据。
* 		   当两个电厂填报同一个月报页面时页面出错。所以在beginResponse()中增加了用户级别为电厂级，
  判断登陆电厂与电厂树是否一致，并重新加载刷新页面。
* 		   
*/
public class Yuedmjgmx extends BasePage implements PageValidateListener {
    private String msg = "";

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = MainGlobal.getExtMessageBox(msg, false);
    }

    protected void initialize() {
        // TODO 自动生成方法存根
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

    private long getKoujId(String strDate, String strDiancName, String strGongysName, String strJihkj, String strPinzbName, String strYunsfs) {
        String strSqly = "";
        long lngKoujID = 0;
        JDBCcon con = new JDBCcon();

//		判断yuetjkjb中是否有此信息，没有插入。
//		strSqly="select nvl(max(id),0) as id from yuetjkjb where riq=to_date('"+strDate+"','yyyy-mm-dd') and diancxxb_id=getDiancId('"+strDiancName+"')\n"
//			+" and meikdqb_id=getGongysId('"+strGongysName+"') and pinzb_id=get_Pinzb_Id('"+strPinzbName+"') " +
//			"and jihkjb_id=getJihkjbId('"+strJihkj+"') and yunsfsb_id=get_Yunsfsb_Id('"+strYunsfs+"')";
        strSqly = "select nvl(max(kj.id),0) as id from yuetjkjb kj,yueslb sl where kj.id=sl.yuetjkjb_id and kj.riq=to_date('" + strDate + "','yyyy-mm-dd') and kj.diancxxb_id=getDiancId('" + strDiancName + "')\n"
                + " and kj.meikdqb_id=getGongysId('" + strGongysName + "') and kj.pinzb_id=get_Pinzb_Id('" + strPinzbName + "') " +
                "and kj.jihkjb_id=getJihkjbId('" + strJihkj + "') and kj.yunsfsb_id=get_Yunsfsb_Id('" + strYunsfs + "')";
        ResultSetList rec = con.getResultSetList(strSqly);
        if (rec != null) {
            if (!rec.next() || rec.getLong("id") == 0) {
                lngKoujID = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
                con.getInsert("insert into yuetjkjb(id,riq,diancxxb_id,xuh,meikdqb_id,pinzb_id,jihkjb_id,yunsfsb_id) values(\n"
                        + lngKoujID + ",to_date('" + strDate + "','yyyy-mm-dd'),getDiancId('" + strDiancName + "'),0,getGongysId('" + strGongysName
                        + "'),get_Pinzb_Id('" + strPinzbName + "'),getJihkjbId('" + strJihkj + "'),get_Yunsfsb_Id('" + strYunsfs + "'))");
            } else {
                lngKoujID = rec.getLong("id");
            }
        }

        con.Close();
        return lngKoujID;
    }

    private void Save() {
        JDBCcon con = new JDBCcon();
        Visit visit = (Visit) this.getPage().getVisit();
        ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(this.getChange());
        StringBuffer sql = new StringBuffer();

        while (rsl.next()) {
            sql.delete(0, sql.length());
            sql.append("begin \n");
            long id = 0L;
            long yuetjkjb_id = rsl.getLong("YUETJKJB_ID");
            ;
            String ID_1 = rsl.getString("ID");

            if (!"0".equals(ID_1) && !"".equals(ID_1)) {
//                yuetjkjb_id = this.getKoujId(rsl.getString("riq"), rsl.getString("diancxxb_id"), rsl.getString("meikdqb_id"), rsl.getString("jihkjb_id"), rsl.getString("pinzb_id"), rsl.getString("yunsfsb_id"));
                sql.append("update yuercbmdj set yuetjkjb_id=" + yuetjkjb_id + ",hetj=" + rsl.getDouble("hetj") + ",meij=" + rsl.getDouble("meij") + ",meijs=" + rsl.getDouble("meijs") + ",yunj=" + rsl.getDouble("yunj") + ",yunjs=" + rsl.getDouble("yunjs") + ",daozzf=" + rsl.getDouble("daozzf") + ",zaf=" + rsl.getDouble("zaf") + ",qit=" + rsl.getDouble("qit") + ",jiaohqzf=" + rsl.getDouble("jiaohqzf") + ",biaomdj=" + rsl.getDouble("biaomdj") + ",buhsbmdj=" + rsl.getDouble("buhsbmdj") + ",yunsjl=" + rsl.getDouble("yunsjl") + ",yunfsl=" + rsl.getDouble("yunfsl") + ",yunfslfs=\'" + rsl.getString("yunfslfs") + "\'" + ",daozzfsl=" + rsl.getDouble("daozzfsl") + ",daozzfslfs=\'" + rsl.getString("daozzfslfs") + "\' " + ",zafsl=" + rsl.getDouble("zafsl") + ",zafslfs=\'" + rsl.getString("zafslfs") + "\' " + ",qitsl=" + rsl.getDouble("qitsl") + ",qitslfs=\'" + rsl.getString("qitslfs") + "\' " + ",jiaohqzfsl=" + rsl.getDouble("jiaohqzfsl") + ",jiaohqzfslfs=\'" + rsl.getString("jiaohqzfslfs") + "\', daozzfs=" + rsl.getDouble("daozzfs") + ",zafs=" + rsl.getDouble("zafs") + ",qits=" + rsl.getDouble("qits") + ",jiaohqzfs=" + rsl.getDouble("jiaohqzfs") + " where id=" + rsl.getLong("id") + ";\n");
            } else {
                id = Long.parseLong(MainGlobal.getNewID(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
//                yuetjkjb_id = this.getKoujId(rsl.getString("riq"), rsl.getString("diancxxb_id"), rsl.getString("meikdqb_id"), rsl.getString("jihkjb_id"), rsl.getString("pinzb_id"), rsl.getString("yunsfsb_id"));
                sql.append("insert into yuercbmdj(id,fenx,yuetjkjb_id,hetj,meij,meijs,yunj,yunjs,daozzf,zaf,qit,jiaohqzf,biaomdj,buhsbmdj,yunsjl,yunfsl,yunfslfs \n,daozzfslfs,zafslfs,qitslfs,jiaohqzfslfs,daozzfsl ,zafsl ,qitsl ,jiaohqzfsl ,DAOZZFS, zafs, qits,jiaohqzfs)values(" + id + ",\'本月\'," + yuetjkjb_id + ",nvl(" + rsl.getDouble("hetj") + ",0),nvl(" + rsl.getDouble("meij") + ",0),nvl(" + rsl.getDouble("meijs") + ",0),nvl(" + rsl.getDouble("yunj") + ",0),nvl(" + rsl.getDouble("yunjs") + ",0),nvl(" + rsl.getDouble("daozzf") + ",0),nvl(" + rsl.getDouble("zaf") + ",0),nvl(" + rsl.getDouble("qit") + ",0),nvl(" + rsl.getDouble("jiaohqzf") + ",0),nvl(" + rsl.getDouble("biaomdj") + ",0),nvl(" + rsl.getDouble("buhsbmdj") + ",0),nvl(" + rsl.getDouble("yunsjl") + ",0),nvl(" + rsl.getDouble("yunfsl") + ",0),\'" + rsl.getString("yunfslfs") + "\',\'" + rsl.getString("daozzfslfs") + "\',\'" + rsl.getString("zafslfs") + "\',\'" + rsl.getString("qitslfs") + "\',\'" + rsl.getString("jiaohqzfsl") + "\',nvl(" + rsl.getDouble("daozzfsl") + ",0),nvl(" + rsl.getDouble("zafsl") + ",0),nvl(" + rsl.getDouble("qitsl") + ",0),nvl(" + rsl.getDouble("jiaohqzfsl") + ",0),nvl(" + rsl.getDouble("DAOZZFS") + ",0),nvl(" + rsl.getDouble("zafs") + ",0),nvl(" + rsl.getDouble("qits") + ",0),nvl(" + rsl.getDouble("jiaohqzfs") + ",0) " + ");\n");
            }

            sql.append("end;");
            con.getUpdate(sql.toString());
            this.LeijSelect();
        }

        con.Close();
        this.setMsg("保存成功!");
    }

    public void LeijSelect() {
        Visit visit = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        // 工具栏的年份和月份下拉框
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
        int i;
        for(i=0;i<=12-intMonth;i++){
            String leijrq= intyear + "-"+ (intMonth+i) + "-01";
            boolean jzyf=false;//设置截止月份的boolean
            String pdyf="select y.id as id from yuercbmdj y,yuetjkjb kj where y.yuetjkjb_id = kj.id \n"
                    +"and kj.diancxxb_id=" +visit.getDiancxxb_id()+" and kj.riq=to_date('"+intyear+"-"+(intMonth+i)+"-01','yyyy-mm-dd')" ;
            ResultSet pdyfsql=con.getResultSet(pdyf);
            try{
                //判断循环月份是否有数据，有数据jzyf：true,否则jzyf：false停止循环。
                if(pdyfsql.next()){
                    jzyf=true;
                }else{
                    continue;
                }
            }catch(SQLException e1){
                e1.printStackTrace();
            }finally{
                con.Close();
            }
            if(jzyf==true){//该循环月份有数据，循环累计执行；该循环月份有数据，则循环结束。
                //查询累计值=本月+上月累计
                String sqlljcx = "select kj.diancxxb_id,kj.gongysb_id as gongysb_id,kj.jihkjb_id as jihkjb_id,\n"
                        + "       kj.pinzb_id as pinzb_id,kj.yunsfsb_id as yunsfsb_id ,\n"
                        + "       max(dc.mingc) as diancmc,max(meikdqb.mingc) as gongysbmc,max(jihkjb.mingc) as jihkjbmc,max(pinzb.mingc) as pinzbmc,max(yunsfsb.mingc) as yunsfsbmc,\n"
                        + "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.hetj)/sum(sl.laimsl),2)) as hetj,\n"
                        + "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.meij)/sum(sl.laimsl),2)) as meij,\n"
                        + "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.meijs)/sum(sl.laimsl),2)) as meijs,\n"
                        + "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.yunj)/sum(sl.laimsl),2)) as yunj,\n"
                        + "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.yunjs)/sum(sl.laimsl),2)) as yunjs,\n"
                        + "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.daozzf)/sum(sl.laimsl),2)) as daozzf,\n"
                        + "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.zaf)/sum(sl.laimsl),2)) as zaf,\n"
                        + "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.qit)/sum(sl.laimsl),2)) as qit,\n"
                        + "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.jiaohqzf)/sum(sl.laimsl),2)) as jiaohqzf,\n"
                        + "       decode(sum(sl.laimsl*zl.qnet_ar),0,0, round((sum(sl.laimsl*y.meij)+sum(sl.laimsl*y.yunj)+sum(sl.laimsl*y.daozzf)+sum(sl.laimsl*y.zaf)+sum(sl.laimsl*y.qit)\n"
                        + "                    +sum(sl.laimsl*y.jiaohqzf))/sum(sl.laimsl)*29.271/(sum(sl.laimsl*zl.qnet_ar)/sum(sl.laimsl))\n"
                        + "                    ,2) ) as  biaomdj,\n"
                        + "       decode(sum(sl.laimsl*zl.qnet_ar),0,0, round((sum(sl.laimsl*y.meij)+sum(sl.laimsl*y.yunj)+sum(sl.laimsl*y.daozzf)+sum(sl.laimsl*y.zaf)+sum(sl.laimsl*y.qit)\n"
                        + "                    +sum(sl.laimsl*y.jiaohqzf)-sum(sl.laimsl*y.meijs)-sum(sl.laimsl*y.yunjs))/sum(sl.laimsl)*29.271/(sum(sl.laimsl*zl.qnet_ar)/sum(sl.laimsl))\n"
                        + "                    ,2) ) as  buhsbmdj,\n"
//					+ "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.biaomdj)/sum(sl.laimsl),2)) as biaomdj,\n"
//					+ "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.buhsbmdj)/sum(sl.laimsl),2)) as buhsbmdj,\n"
                        + "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.yunsjl)/sum(sl.laimsl),0)) as yunsjl\n"
                        + "  from yuercbmdj y, yuetjkjb kj, yueslb sl,yuezlb zl,meikdqb, jihkjb, pinzb, yunsfsb,diancxxb dc\n"
                        + " where y.yuetjkjb_id = kj.id\n"
                        + "     and kj.id = sl.yuetjkjb_id(+)\n"
                        + "     and kj.id = zl.yuetjkjb_id(+)\n"
                        + "     and y.fenx='本月'\n"
                        + "     and sl.fenx='本月'\n"
                        + "     and zl.fenx='本月'\n"
                        + "	    and kj.gongysb_id = meikdqb.id\n"
                        + "     and kj.jihkjb_id = jihkjb.id\n"
                        + "     and kj.pinzb_id = pinzb.id\n"
                        + "     and kj.yunsfsb_id = yunsfsb.id\n"
                        + "     and dc.id=kj.diancxxb_id\n"
                        + "     and diancxxb_id ="+ visit.getDiancxxb_id()+"\n"
                        + "   and kj.riq >=getYearFirstDate(to_date('"+intyear+"-"+(intMonth+i)+"-01','yyyy-mm-dd'))\n"
                        + "   and kj.riq<=to_date('"+intyear+"-"+(intMonth+i)+"-01', 'yyyy-mm-dd')\n"
                        + "   group by (kj.diancxxb_id,kj.gongysb_id,kj.jihkjb_id,kj.pinzb_id,kj.yunsfsb_id)";
                ResultSetList rsllj = con.getResultSetList(sqlljcx);
                if(rsllj!=null&&rsllj.getRows()!=0){

                    long yuercbmdjid = 0;
                    StringBuffer sqllj = new StringBuffer("begin \n");
//					获取累计当月累计状态
                    String shzt=
                            "select max(zhuangt) as zhuangt from yuercbmdj cb,yuetjkjb kj where cb.yuetjkjb_id=kj.id and kj.diancxxb_id=" +visit.getDiancxxb_id()+"\n" +
                                    "       and kj.riq=to_date('"+intyear+"-"+(intMonth+i)+"-01','yyyy-mm-dd')  and cb.fenx='累计'";
                    ResultSetList shenhzt=con.getResultSetList(shzt);
                    long zhuangt=0;

                    while(shenhzt.next()){
                        zhuangt=shenhzt.getLong("zhuangt");
                    }
                    String deletelj="delete from yuercbmdj where id in(select cb.id from yuercbmdj cb,yuetjkjb k \n" +
                            "where cb.yuetjkjb_id=k.id and k.riq= to_date('"+intyear+"-"+(intMonth+i)+"-01','yyyy-mm-dd')\n" +
                            "	   and cb.fenx='累计' and k.diancxxb_id=" +visit.getDiancxxb_id()+");\n";
                    sqllj.append(deletelj);

                    while (rsllj.next()) {

                        String kjidsql=
                                "select cb.yuetjkjb_id as yuetjkjbid from yuetjkjb kj,yuercbmdj cb\n" +
                                        "where cb.yuetjkjb_id=kj.id and kj.diancxxb_id="+rsllj.getLong("diancxxb_id")+" and kj.riq = to_date('"+intyear+"-"+(intMonth+i)+"-01','yyyy-mm-dd')\n" +
                                        "      and kj.gongysb_id="+rsllj.getLong("gongysb_id")+" and kj.jihkjb_id="+rsllj.getLong("jihkjb_id")+
                                        " and kj.yunsfsb_id="+rsllj.getLong("yunsfsb_id")+" and kj.pinzb_id="+rsllj.getLong("pinzb_id")+"\n"+
                                        " and cb.fenx='本月'";
                        long kjb_id=0;
                        ResultSet kjid=con.getResultSet(kjidsql);
                        try{
                            while(kjid.next()){
                                kjb_id=kjid.getLong("yuetjkjbid");
                            }
                        }catch(SQLException e1){
                            e1.fillInStackTrace();
                        }finally{
                            con.Close();
                        }
                        if(kjb_id==0){
                            kjb_id=getKoujId(leijrq,rsllj.getString("diancmc"),rsllj.getString("gongysbmc"),rsllj.getString("jihkjbmc"),rsllj.getString("pinzbmc"),rsllj.getString("yunsfsbmc"));
                        }

                        yuercbmdjid = Long.parseLong(MainGlobal.getNewID(((Visit)getPage().getVisit()).getDiancxxb_id()));
                        sqllj.append(
                                "insert into yuercbmdj(id,fenx,yuetjkjb_id,hetj,meij,meijs,yunj,yunjs,daozzf,zaf,qit,jiaohqzf,biaomdj,buhsbmdj,yunsjl,zhuangt) values " +"("
                                        + yuercbmdjid
                                        +",'累计',"
                                        +kjb_id
                                        +","+rsllj.getDouble("hetj")+","+""
                                        +""+rsllj.getDouble("meij")+","+""
                                        +""+rsllj.getDouble("meijs")+""+""
                                        +","+rsllj.getDouble("yunj")
                                        +","+rsllj.getDouble("yunjs")
                                        +","+rsllj.getDouble("daozzf")
                                        +","+rsllj.getDouble("zaf")
                                        +","+rsllj.getDouble("qit")
                                        +","+rsllj.getDouble("jiaohqzf")
                                        +","+rsllj.getDouble("biaomdj")
                                        +","+rsllj.getDouble("buhsbmdj")
                                        +","+rsllj.getDouble("yunsjl")
                                        +","+zhuangt
                                        +");\n");
                    }
                    sqllj.append("end;");
                    con.getInsert(sqllj.toString());
                }
            }
            con.Close();
        }
    }

    private boolean _SaveChick = false;

    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }

    private boolean _CopyButton = false;

    public void CopyButton(IRequestCycle cycle) {
        _CopyButton = true;
    }

    private boolean _Refreshclick = false;

    public void RefreshButton(IRequestCycle cycle) {
        _Refreshclick = true;
    }

    private boolean _DelClick = false;

    public void DelButton(IRequestCycle cycle) {
        _DelClick = true;
    }

    public void submit(IRequestCycle cycle) {
        if (_SaveChick) {
            _SaveChick = false;
            Save();
            getSelectData();
        }
        if (_Refreshclick) {
            _Refreshclick = false;
            getSelectData();
        }
        if (_DelClick) {
            _DelClick = false;
            DelData();
        }
    }

    public void DelData() {
        Visit visit = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        String CurrZnDate = getNianfValue() + "年" + getYuefValue() + "月";
        String riq = DateUtil.FormatOracleDate(getNianfValue() + "-" + getYuefValue() + "-01");
        String strSql = "delete from yuercbmdj where yuetjkjb_id in (select id from yuetjkjb where riq="
                + riq + " and diancxxb_id=" + getTreeid() + ")";
        int flag = con.getDelete(strSql);
        if (flag == -1) {
            WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:" + strSql);
            setMsg("删除过程中发生错误！");
        } else {
            setMsg(CurrZnDate + "的数据被成功删除！");
        }
        con.Close();
    }

    public void getSelectData() {
        Visit visit = (Visit) this.getPage().getVisit();
        JDBCcon con = new JDBCcon();
        String zhuangt = "";
        String shulzt = "";
        String zhilzt = "";
        if (visit.isShifsh()) {
            if (visit.getRenyjb() == 3) {
                zhuangt = "";
            } else if (visit.getRenyjb() == 2) {
                zhuangt = " and (y.zhuangt=1 or y.zhuangt=2)";
            } else if (visit.getRenyjb() == 1) {
                zhuangt = " and y.zhuangt=2";
            }
        }

        long intyear;
        if (this.getNianfValue() == null) {
            intyear = (long) DateUtil.getYear(new Date());
        } else {
            intyear = this.getNianfValue().getId();
        }

        long intMonth;
        if (this.getYuefValue() == null) {
            intMonth = (long) DateUtil.getMonth(new Date());
        } else {
            intMonth = this.getYuefValue().getId();
        }

        String StrMonth = "";
        if (intMonth < 10L) {
            StrMonth = "0" + intMonth;
        } else {
            StrMonth = "" + intMonth;
        }

        String str = "";
        int treejib = this.getDiancTreeJib();
        if (treejib == 1) {
            str = "";
        } else if (treejib == 2) {
            str = "and (dc.id = " + this.getTreeid() + " or dc.fuid = " + this.getTreeid() + ")";
        } else if (treejib == 3) {
            str = "and dc.id = " + this.getTreeid();
        }

        String chaxun = "select \'本月\' as fenx, max(nvl(b.id,0)) as id,\n      riq,yuetjkjb_id,\n               decode(grouping(b.diancxxb_id), 1, (select mingc from diancxxb where id=" + this.getTreeid() + "),b.diancxxb_id) as diancxxb_id,\n" + "               decode(grouping(b.meikdqb_id),1,\'总计\',b.meikdqb_id) as meikdqb_id,\n" + "               decode(grouping(b.jihkjb_id),1,\'-\',b.jihkjb_id) as jihkjb_id,\n" + "               decode(grouping(b.pinzb_id),1,\'-\',b.pinzb_id) as pinzb_id,\n" + "               decode(grouping(b.yunsfsb_id),1,\'-\',b.yunsfsb_id) as yunsfsb_id,\n" + "               sum(nvl(b.laimsl,0)) as laimsl,\n" + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.farl)/sum(b.laimsl),2)),0) as farl,\n" + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.hetj)/sum(b.laimsl),2)),0) as hetj,\n" + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.meij)/sum(b.laimsl),2)),0) as meij,\n" + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.meijs)/sum(b.laimsl),2)),0) as meijs,\n" + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.yunj)/sum(b.laimsl),2)),0) as yunj,\n" + "\t\t\t\tmax(yunfslfs)yunfslfs,max(yunfsl)yunfsl," + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.yunjs)/sum(b.laimsl),2)),0) as yunjs,\n" + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.daozzf)/sum(b.laimsl),2)),0) as daozzf,\n" + "\t\t\t\tmax(daozzfslfs)daozzfslfs,max(daozzfsl)daozzfsl ,\n" + "   \t\t\t nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.daozzfs)/sum(b.laimsl),2)),0) as daozzfs,\n  " + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.zaf)/sum(b.laimsl),2)),0) as zaf,\n" + "\t\t\t\tmax(zafslfs)zafslfs,max(zafsl)zafsl ,\n" + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.zafs)/sum(b.laimsl),2)),0) as zafs,\n" + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.qit)/sum(b.laimsl),2)),0) as qit,\n" + "\t\t\t\tmax(qitslfs)qitslfs,max(qitsl) qitsl,\n" + "\t\t\t\tnvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.qits)/sum(b.laimsl),2)),0) as qits,\n" + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.jiaohqzf)/sum(b.laimsl),2)),0) as jiaohqzf,\n" + "\t\t\t\tmax(jiaohqzfslfs)jiaohqzfslfs,max(jiaohqzfsl)jiaohqzfsl,\n " + "\t\t\t\t nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.jiaohqzfs)/sum(b.laimsl),2)),0) as jiaohqzfs,\n" + "\t\t\t\tnvl(decode(decode(sum(b.laimsl),0,0,sum(b.laimsl*b.farl)/sum(b.laimsl)),0,0,round(round(decode(sum(b.laimsl),0,0,sum((b.meij+b.yunj+b.daozzf+b.zaf+b.qit+b.jiaohqzf-b.meijs-b.yunjs- b.daozzfs-b.zafs-b.qits-b.jiaohqzfs)*b.laimsl)/sum(b.laimsl)),2)*29.271/round(decode(sum(b.laimsl),0,0,sum(b.laimsl*b.farl)/sum(b.laimsl)),2),2)),0) as buhsbmdj,\n" + "               nvl(decode(decode(sum(b.laimsl),0,0,sum(b.laimsl*b.farl)/sum(b.laimsl)),0,0,round(round(decode(sum(b.laimsl),0,0,sum((b.meij+b.yunj+b.daozzf+b.zaf+b.qit+b.jiaohqzf)*b.laimsl)/sum(b.laimsl)),2)*29.271/round(decode(sum(b.laimsl),0,0,sum(b.laimsl*b.farl)/sum(b.laimsl)),2),2)),0) as biaomdj,\n" + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.yunsjl)/sum(b.laimsl),2)),0) as yunsjl,\n" + "               sum(b.zhuangt) as zhuangt\n" + "  from(\n" + "select \'本月\' as fenx, y.id,tj.riq,tj.id yuetjkjb_id,dc.mingc as diancxxb_id,g.mingc as meikdqb_id,j.mingc as jihkjb_id,p.mingc as pinzb_id,ys.mingc as yunsfsb_id,\n" + "       sl.laimsl,zl.qnet_ar as farl,y.hetj,y.meij,y.meijs,y.yunj,y.yunjs,y.daozzf,y.zaf,y.qit,y.jiaohqzf,\n" + "\t\t  round(decode(zl.qnet_ar,0,0,(y.meij+y.yunj+y.daozzf+y.zaf+y.qit+y.jiaohqzf-y.meijs-y.yunjs- y.daozzfs-y.zafs-y.qits-y.jiaohqzfs)*29.271/zl.qnet_ar),2) as buhsbmdj,\n" + "\t\t  round(decode(zl.qnet_ar,0,0,(y.meij+y.yunj+y.daozzf+y.zaf+y.qit+y.jiaohqzf)*29.271/zl.qnet_ar),2) as biaomdj,y.yunsjl,nvl(y.zhuangt,0) as zhuangt \n" + "\t\t\t,yunfslfs,yunfsl ,daozzfslfs,daozzfsl ,zafslfs,zafsl ,qitslfs,qitsl ,jiaohqzfslfs,jiaohqzfsl ,DAOZZFS, zafs, qits,jiaohqzfs\n" + "from (\n" + "select y.* from yuercbmdj  y ,yuetjkjb tj\n" + "       where tj.riq =to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\' )\n" + "       and y.yuetjkjb_id=tj.id\n" + "       and y.fenx=\'本月\' " + zhuangt + ") y,\n" + " (select y.* from yueslb  y ,yuetjkjb tj\n" + "       where tj.riq =to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\' )\n" + "       and y.yuetjkjb_id=tj.id\n" + "       and y.fenx=\'本月\' " + zhuangt + " ) sl,\n" + "(select y.* from yuezlb  y ,yuetjkjb tj\n" + "       where tj.riq =to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\' )\n" + "       and y.yuetjkjb_id=tj.id\n" + "       and y.fenx=\'本月\' " + zhuangt + " ) zl ,yuetjkjb tj ,meikdqb g,jihkjb j,pinzb p,diancxxb dc,yunsfsb ys\n" + "where y.yuetjkjb_id(+)=tj.id\n" + "and tj.gongysb_id=g.id\n" + "and tj.jihkjb_id=j.id\n" + "and tj.pinzb_id=p.id\n" + "and tj.diancxxb_id=dc.id\n" + "and tj.yunsfsb_id=ys.id\n" + "and tj.id=zl.yuetjkjb_id(+)\n" + "and tj.id=sl.yuetjkjb_id(+)  " + str + "\n" + "and tj.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\' )\n" + "and (sl.fenx=\'本月\' or zl.fenx=\'本月\' or y.fenx=\'本月\')\n" + ")b \n" + "group by rollup(meikdqb_id,diancxxb_id,riq,yuetjkjb_id,jihkjb_id,pinzb_id, yunsfsb_id)\n" + "having grouping(meikdqb_id) = 1 or grouping(yunsfsb_id)=0\n" + "order by meikdqb_id desc,id";
        ResultSetList rsl = con.getResultSetList(chaxun);
        boolean yincan = false;

        while (true) {
            while (rsl.next()) {
                if (visit.getRenyjb() == 3) {
                    if (rsl.getLong("zhuangt") == 0L) {
                        yincan = false;
                    } else {
                        yincan = true;
                    }
                } else if (visit.getRenyjb() == 1 || visit.getRenyjb() == 2) {
                    yincan = true;
                }
            }

            rsl.beforefirst();
            ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
            egu.setTableName("yuercbmdj");
            egu.setWidth("bodyWidth");
            egu.getColumn("riq").setHeader("日期");
            egu.getColumn("riq").setHidden(true);
            egu.getColumn("riq").setEditor((Field) null);
            egu.getColumn("fenx").setHidden(true);
            egu.getColumn("yuetjkjb_id").setHidden(true);
            egu.getColumn("diancxxb_id").setHeader("电厂名称");
            egu.getColumn("diancxxb_id").setEditor((Field) null);
            egu.getColumn("meikdqb_id").setHeader("供货单位");
            egu.getColumn("meikdqb_id").setEditor((Field) null);
            egu.getColumn("jihkjb_id").setHeader("计划口径");
            egu.getColumn("jihkjb_id").setEditor((Field) null);
            egu.getColumn("pinzb_id").setHeader("品种");
            egu.getColumn("pinzb_id").setHidden(true);
            egu.getColumn("pinzb_id").setEditor((Field) null);
            egu.getColumn("yunsfsb_id").setHeader("运输方式");
            egu.getColumn("yunsfsb_id").setEditor((Field) null);
            egu.getColumn("hetj").setHeader("合同价<br>(元/吨)");
            egu.getColumn("laimsl").setHeader("实收量<br>(吨)");
            egu.getColumn("laimsl").setEditor((Field) null);
            egu.getColumn("farl").setHeader("热量<br>((MJ/kg))");
            egu.getColumn("farl").setEditor((Field) null);
            egu.getColumn("meij").setHeader("煤价(含税)<br>(元/吨)");
            egu.getColumn("meijs").setHeader("煤税<br>(元/吨)");
            egu.getColumn("yunj").setHeader("运价(含税)<br>(元/吨)");
            egu.getColumn("yunfslfs").setHeader("运价<br>税率方式");
            egu.getColumn("yunfsl").setHeader("运价<br>税率(%)");
            egu.getColumn("yunjs").setHeader("运价税<br>(元/吨)");
            egu.getColumn("daozzf").setHeader("到站杂费<br>(元/吨)");
            egu.getColumn("zaf").setHeader("杂费<br>(元/吨)");
            egu.getColumn("qit").setHeader("其它<br>(元/吨)");
            egu.getColumn("jiaohqzf").setHeader("交货前杂费<br>(元/吨)");
            egu.getColumn("buhsbmdj").setHeader("标煤单价(不含税)<br>(元/吨)");
            egu.getColumn("buhsbmdj").setEditor((Field) null);
            egu.getColumn("biaomdj").setHeader("标煤单价(含税)<br>(元/吨)");
            egu.getColumn("biaomdj").setEditor((Field) null);
            egu.getColumn("yunsjl").setHeader("运输距离<br>(公里)");
            egu.getColumn("zhuangt").setHeader("状态");
            egu.getColumn("zhuangt").setHidden(true);
            egu.getColumn("zhuangt").setEditor((Field) null);
            egu.getColumn("daozzfsl").setHeader("到站杂费<br>税率(%)");
            egu.getColumn("daozzfslfs").setHeader("到站杂费<br>税率方式");
            egu.getColumn("zafsl").setHeader("杂费<br>税率(%)");
            egu.getColumn("zafslfs").setHeader("杂费<br>税率方式");
            egu.getColumn("qitsl").setHeader("其它<br>税率(%)");
            egu.getColumn("qitslfs").setHeader("其它<br>税率方式");
            egu.getColumn("jiaohqzfsl").setHeader("交货前杂费<br>税率(%)");
            egu.getColumn("jiaohqzfslfs").setHeader("交货前杂费<br>税率方式");
            egu.getColumn("daozzfs").setHeader("到站杂费<br>税<br>(元/吨)");
            egu.getColumn("zafs").setHeader("杂费<br>税<br>(元/吨)");
            egu.getColumn("qits").setHeader("其它<br>税<br>(元/吨)");
            egu.getColumn("jiaohqzfs").setHeader("交货前杂费<br>税<br>(元/吨)");
            egu.getColumn("biaomdj").setRenderer("function(value,metadata){metadata.css=\'tdTextext\'; return value;}");
            egu.getColumn("buhsbmdj").setRenderer("function(value,metadata){metadata.css=\'tdTextext\'; return value;}");
            egu.getColumn("yunjs").setRenderer("function(value,metadata){metadata.css=\'tdTextext\'; return value;}");
            egu.getColumn("yunjs").setEditor((Field) null);
            egu.getColumn("meijs").setRenderer("function(value,metadata){metadata.css=\'tdTextext\'; return value;}");
            egu.getColumn("meijs").setEditor((Field) null);
            egu.getColumn("riq").setWidth(80);
            egu.getColumn("meikdqb_id").setWidth(90);
            egu.getColumn("diancxxb_id").setWidth(90);
            egu.getColumn("jihkjb_id").setWidth(70);
            egu.getColumn("pinzb_id").setWidth(65);
            egu.getColumn("yunsfsb_id").setWidth(70);
            egu.getColumn("hetj").setWidth(65);
            egu.getColumn("laimsl").setWidth(65);
            egu.getColumn("farl").setWidth(65);
            egu.getColumn("meij").setWidth(65);
            egu.getColumn("meijs").setWidth(65);
            egu.getColumn("daozzf").setWidth(65);
            egu.getColumn("zaf").setWidth(60);
            egu.getColumn("qit").setWidth(60);
            egu.getColumn("jiaohqzf").setWidth(70);
            egu.getColumn("buhsbmdj").setWidth(100);
            egu.getColumn("biaomdj").setWidth(90);
            egu.getColumn("yunj").setWidth(65);
            egu.getColumn("yunjs").setWidth(65);
            egu.getColumn("yunsjl").setWidth(80);
            egu.getColumn("yunfslfs").setWidth(80);
            egu.getColumn("yunfsl").setWidth(60);
            ArrayList l1 = new ArrayList();
            l1.add(new IDropDownBean(0L, "增值税"));
            l1.add(new IDropDownBean(1L, "营业税"));
            egu.getColumn("yunfslfs").setEditor(new ComboBox());
            egu.getColumn("yunfslfs").setComboEditor(egu.gridId, new IDropDownModel(l1));
            egu.getColumn("daozzfslfs").setEditor(new ComboBox());
            egu.getColumn("daozzfslfs").setComboEditor(egu.gridId, new IDropDownModel(l1));
            egu.getColumn("zafslfs").setEditor(new ComboBox());
            egu.getColumn("zafslfs").setComboEditor(egu.gridId, new IDropDownModel(l1));
            egu.getColumn("qitslfs").setEditor(new ComboBox());
            egu.getColumn("qitslfs").setComboEditor(egu.gridId, new IDropDownModel(l1));
            egu.getColumn("jiaohqzfslfs").setEditor(new ComboBox());
            egu.getColumn("jiaohqzfslfs").setComboEditor(egu.gridId, new IDropDownModel(l1));
            StringBuffer sb1 = new StringBuffer();
            sb1.append("gridDiv_grid.on(\'beforeedit\',function(e){");
            sb1.append("if(e.record.get(\'GONGYSB_ID\')==\'总计\'){e.cancel=true;}");
            sb1.append("});");
            sb1.append("function gridDiv_save(record){if(record.get(\'meikdqb_id\')==\'总计\') return \'continue\';}");
            egu.addOtherScript(sb1.toString());
            egu.setGridType(1);
            egu.addPaging(25);
            egu.getColumn("hetj").setDefaultValue("0");
            egu.getColumn("meij").setDefaultValue("0");
            egu.getColumn("meijs").setDefaultValue("0");
            egu.getColumn("yunj").setDefaultValue("0");
            egu.getColumn("yunjs").setDefaultValue("0");
            egu.getColumn("daozzf").setDefaultValue("0");
            egu.getColumn("zaf").setDefaultValue("0");
            egu.getColumn("qit").setDefaultValue("0");
            egu.getColumn("jiaohqzf").setDefaultValue("0");
            egu.getColumn("biaomdj").setDefaultValue("0");
            egu.getColumn("buhsbmdj").setDefaultValue("0");
            egu.getColumn("yunsjl").setDefaultValue("0");
            egu.getColumn("yunfsl").setDefaultValue("0");
            ((NumberField) egu.getColumn("hetj").editor).setDecimalPrecision(2L);
            ((NumberField) egu.getColumn("meij").editor).setDecimalPrecision(2L);
            ((NumberField) egu.getColumn("yunj").editor).setDecimalPrecision(2L);
            ((NumberField) egu.getColumn("daozzf").editor).setDecimalPrecision(2L);
            ((NumberField) egu.getColumn("zaf").editor).setDecimalPrecision(2L);
            ((NumberField) egu.getColumn("qit").editor).setDecimalPrecision(2L);
            ((NumberField) egu.getColumn("jiaohqzf").editor).setDecimalPrecision(2L);
            int treejib2 = this.getDiancTreeJib();
            egu.getColumn("riq").setDefaultValue(intyear + "-" + StrMonth + "-01");
            egu.addTbarText("年份:");
            ComboBox comb1 = new ComboBox();
            comb1.setTransform("NIANF");
            comb1.setId("NIANF");
            comb1.setLazyRender(true);
            comb1.setWidth(60);
            egu.addToolbarItem(comb1.getScript());
            egu.addTbarText("-");
            egu.addTbarText("月份:");
            ComboBox comb2 = new ComboBox();
            comb2.setTransform("YUEF");
            comb2.setId("YUEF");
            comb2.setLazyRender(true);
            comb2.setWidth(50);
            egu.addToolbarItem(comb2.getScript());
            egu.addTbarText("-");
            egu.addTbarText("单位:");
            ExtTreeUtil etu = new ExtTreeUtil("diancTree", 10, ((Visit) this.getPage().getVisit()).getDiancxxb_id(), this.getTreeid());
            this.setTree(etu);
            egu.addTbarTreeBtn("diancTree");
            egu.addOtherScript("NIANF.on(\'select\',function(){document.forms[0].submit();});YUEF.on(\'select\',function(){document.forms[0].submit();});");
            egu.addTbarText("-");
            StringBuffer rsb = new StringBuffer();
            rsb.append("function (){").append(MainGlobal.getExtMessageBox("\'正在刷新\'+Ext.getDom(\'NIANF\').value+\'年\'+Ext.getDom(\'YUEF\').value+\'月的数据,请稍候！\'", true)).append("document.getElementById(\'RefreshButton\').click();}");
            GridButton gbr = new GridButton("刷新", rsb.toString());
            gbr.setIcon("imgs/btnicon/refurbish.gif");
            egu.addTbarBtn(gbr);
            if (visit.getRenyjb() == 3 && !yincan) {
                egu.addToolbarButton(1, (String) null);
                egu.addToolbarButton(11, "SaveButton", MainGlobal.getExtMessageShow("正在保存,请稍后...", "保存中...", 200));
            }

            egu.addTbarText("->");
            egu.addTbarText("<font color=\"#EE0000\">单位:吨、元、MJ/Kg、公里</font>");
            StringBuffer sb = new StringBuffer();
            sb.append("gridDiv_grid.on(\'afteredit\',function(e){");
            sb.append("if (e.field ==\'MEIJ\'){e.record.set(\'MEIJS\',Round(parseFloat(e.record.get(\'MEIJ\')==\'\'?0:e.record.get(\'MEIJ\'))- (parseFloat(e.record.get(\'MEIJ\')==\'\'?0:e.record.get(\'MEIJ\'))/1.17),2))};");
            sb.append("if (e.field ==\'YUNJ\'||e.field ==\'YUNFSL\'||e.field ==\'YUNFSLFS\'){");
            sb.append(" if(e.record.get(\'YUNFSLFS\')==\'增值税\')  ");
            sb.append("{e.record.set(\'YUNJS\', Round(parseFloat(e.record.get(\'YUNJ\')==\'\'?0:e.record.get(\'YUNJ\'))- (parseFloat(e.record.get(\'YUNJ\')==\'\'?0:e.record.get(\'YUNJ\'))/(1+e.record.get(\'YUNFSL\')/100)),2))}");
            sb.append("else{e.record.set(\'YUNJS\',Round( parseFloat(e.record.get(\'YUNJ\')==\'\'&&e.record.get(\'YUNFSL\')==0?0:e.record.get(\'YUNJ\'))*(e.record.get(\'YUNFSL\')/100),2))}}");
            sb.append("if (e.field ==\'DAOZZF\'||e.field ==\'DAOZZFSL\'||e.field ==\'DAOZZFSLFS\'){");
            sb.append(" if(e.record.get(\'DAOZZFSLFS\')==\'增值税\')  ");
            sb.append("{e.record.set(\'DAOZZFS\', Round(parseFloat(e.record.get(\'DAOZZF\')==\'\'?0:e.record.get(\'DAOZZF\'))- (parseFloat(e.record.get(\'DAOZZF\')==\'\'?0:e.record.get(\'DAOZZF\'))/(1+e.record.get(\'DAOZZFSL\')/100)),2))}");
            sb.append("else{e.record.set(\'DAOZZFS\',Round( parseFloat(e.record.get(\'DAOZZF\')==\'\'&&e.record.get(\'DAOZZFSL\')==0?0:e.record.get(\'DAOZZF\'))*(e.record.get(\'DAOZZFSL\')/100),2))}}");
            sb.append("if (e.field ==\'ZAF\'||e.field ==\'ZAFSL\'||e.field ==\'ZAFSLFS\'){");
            sb.append(" if(e.record.get(\'ZAFSLFS\')==\'增值税\')  ");
            sb.append("{e.record.set(\'ZAFS\', Round(parseFloat(e.record.get(\'ZAF\')==\'\'?0:e.record.get(\'ZAF\'))- (parseFloat(e.record.get(\'ZAF\')==\'\'?0:e.record.get(\'ZAF\'))/(1+e.record.get(\'ZAFSL\')/100)),2))}");
            sb.append("else{e.record.set(\'ZAFS\',Round( parseFloat(e.record.get(\'ZAF\')==\'\'&&e.record.get(\'ZAFSL\')==0?0:e.record.get(\'ZAF\'))*(e.record.get(\'ZAFSL\')/100),2))}}");
            sb.append("if (e.field ==\'QIT\'||e.field ==\'QITSL\'||e.field ==\'QITSLFS\'){");
            sb.append(" if(e.record.get(\'QITSLFS\')==\'增值税\')  ");
            sb.append("{e.record.set(\'QITS\', Round(parseFloat(e.record.get(\'QIT\')==\'\'?0:e.record.get(\'QIT\'))- (parseFloat(e.record.get(\'QIT\')==\'\'?0:e.record.get(\'QIT\'))/(1+e.record.get(\'QITSL\')/100)),2))}");
            sb.append("else{e.record.set(\'QITS\',Round( parseFloat(e.record.get(\'QIT\')==\'\'&&e.record.get(\'QITSL\')==0?0:e.record.get(\'QIT\'))*(e.record.get(\'QITSL\')/100),2))}}");
            sb.append("if (e.field ==\'JIAOHQZF\'||e.field ==\'JIAOHQZFSL\'||e.field ==\'JIAOHQZFSLFS\'){");
            sb.append(" if(e.record.get(\'JIAOHQZFSLFS\')==\'增值税\')  ");
            sb.append("{e.record.set(\'JIAOHQZFS\', Round(parseFloat(e.record.get(\'JIAOHQZF\')==\'\'?0:e.record.get(\'JIAOHQZF\'))- (parseFloat(e.record.get(\'JIAOHQZF\')==\'\'?0:e.record.get(\'JIAOHQZF\'))/(1+e.record.get(\'JIAOHQZFSL\')/100)),2))}");
            sb.append("else{e.record.set(\'JIAOHQZFS\',Round( parseFloat(e.record.get(\'JIAOHQZF\')==\'\'&&e.record.get(\'JIAOHQZFSL\')==0?0:e.record.get(\'JIAOHQZF\'))*(e.record.get(\'JIAOHQZFSL\')/100),2))}}");
            sb.append("if(e.record.get(\'FARL\')==0||e.record.get(\'FARL\')==null){ e.record.set(\'BIAOMDJ\',0 );}else{");
            sb.append("if(e.field == \'MEIJ\'||e.field == \'YUNJ\'||e.field == \'DAOZZF\'||e.field == \'ZAF\'||e.field == \'QIT\'||e.field == \'JIAOHQZF\'||e.field == \'FARL\')").append("{e.record.set(\'BIAOMDJ\',Round(((parseFloat(e.record.get(\'MEIJ\')==\'\'?0:e.record.get(\'MEIJ\'))").append("+parseFloat(e.record.get(\'YUNJ\')==\'\'?0:e.record.get(\'YUNJ\'))").append("+parseFloat(e.record.get(\'ZAF\')==\'\'?0:e.record.get(\'ZAF\'))").append("+parseFloat(e.record.get(\'DAOZZF\')==\'\'?0:e.record.get(\'DAOZZF\'))").append("+parseFloat(e.record.get(\'QIT\')==\'\'?0:e.record.get(\'QIT\'))").append("+parseFloat(e.record.get(\'JIAOHQZF\')==\'\'?0:e.record.get(\'JIAOHQZF\'))").append(")*29.271/parseFloat(e.record.get(\'FARL\')==\'\'?0:e.record.get(\'FARL\'))),2) )};").append("");
            sb.append("};");
            sb.append("if(e.record.get(\'FARL\')==0||e.record.get(\'FARL\')==null){ e.record.set(\'BIAOMDJ\',0 );}else{");
            sb.append("if(e.field == \'MEIJ\'||e.field == \'MEIJS\'||e.field == \'YUNJ\'||e.field == \'YUNJS\'||e.field == \'DAOZZF\'||e.field == \'ZAF\'||e.field == \'QIT\'||e.field == \'JIAOHQZF\'||e.field == \'FARL\'||e.field == \'DAOZZFS\'||e.field == \'ZAFS\'||e.field == \'QITS\'||e.field == \'JIAOHQZFS\'||e.field == \'DAOZZFSL\'||e.field == \'ZAFSL\'||e.field == \'QITSL\'||e.field == \'JIAOHQZFSL\'||e.field == \'DAOZZFSLFS\'||e.field == \'ZAFSLFS\'||e.field == \'QITSLFS\'||e.field == \'JIAOHQZFSLFS\')").append("{e.record.set(\'BUHSBMDJ\',Round(((parseFloat(e.record.get(\'MEIJ\')==\'\'?0:e.record.get(\'MEIJ\'))").append("+parseFloat(e.record.get(\'YUNJ\')==\'\'?0:e.record.get(\'YUNJ\'))").append("+parseFloat(e.record.get(\'ZAF\')==\'\'?0:e.record.get(\'ZAF\'))").append("+parseFloat(e.record.get(\'DAOZZF\')==\'\'?0:e.record.get(\'DAOZZF\'))").append("+parseFloat(e.record.get(\'QIT\')==\'\'?0:e.record.get(\'QIT\'))").append("+parseFloat(e.record.get(\'JIAOHQZF\')==\'\'?0:e.record.get(\'JIAOHQZF\'))").append("-parseFloat(e.record.get(\'MEIJS\')==\'\'?0:e.record.get(\'MEIJS\'))").append("-parseFloat(e.record.get(\'YUNJS\')==\'\'?0:e.record.get(\'YUNJS\'))").append("-parseFloat(e.record.get(\'DAOZZFS\')==\'\'?0:e.record.get(\'DAOZZFS\'))").append("-parseFloat(e.record.get(\'ZAFS\')==\'\'?0:e.record.get(\'ZAFS\'))").append("-parseFloat(e.record.get(\'QITS\')==\'\'?0:e.record.get(\'QITS\'))").append("-parseFloat(e.record.get(\'JIAOHQZFS\')==\'\'?0:e.record.get(\'JIAOHQZFS\'))").append(")*29.271/parseFloat(e.record.get(\'FARL\')==\'\'?0:e.record.get(\'FARL\'))),2) )};").append("");
            sb.append("};");
            sb.append("});");
            egu.addOtherScript(sb.toString());
            if (!con.getHasIt(chaxun)) {
                this.setMsg("没有数量和质量信息,请先去月数量维护和月质量维护页面填写数量和质量信息!");
            }

            this.setExtGrid(egu);
            con.Close();
            return;
        }
    }

    public String getBtnHandlerScript(String btnName) {
//		按钮的script
        StringBuffer btnsb = new StringBuffer();
        String cnDate = getNianfValue() + "年" + getYuefValue() + "月";
        btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
        if (btnName.endsWith("CreateButton")) {
            btnsb.append("新生成数据将覆盖")
                    .append(cnDate).append("的已存数据，是否继续？");
        } else {
            btnsb.append("是否删除").append(cnDate).append("的数据？");
        }
        btnsb.append("',function(btn){if(btn == 'yes'){")
                .append("document.getElementById('").append(btnName).append("').click()")
//		-------------------------------------------------------------------
                .append(";Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',")
                .append("width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO}); \n")
//		-------------------------------------------------------------------
                .append("}; // end if \n")
                .append("});}");
        return btnsb.toString();
    }

    public ExtGridUtil getExtGrid() {
        return ((Visit) this.getPage().getVisit()).getExtGrid1();
    }

    public void setExtGrid(ExtGridUtil extgrid) {
        ((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
    }

    public String getGridScript() {
        if (getExtGrid() == null) {
            return "";
        }
        if (getTbmsg() != null) {
            getExtGrid().addToolbarItem("'->'");
            getExtGrid().addToolbarItem(
                    "'<marquee width=200 scrollamount=2>" + getTbmsg()
                            + "</marquee>'");
        }
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
            this.setTreeid(null);
            this.setYuefValue(null);
            this.getYuefModels();
            visit.setShifsh(true);
            setTbmsg(null);
        }
        if (visit.getRenyjb() == 3) {
            if (!this.getTreeid().equals(visit.getDiancxxb_id() + "")) {
                visit.setActivePageName(getPageName().toString());
                visit.setList1(null);
                this.setNianfValue(null);
                this.getNianfModels();
                this.setTreeid(null);
                this.setYuefValue(null);
                this.getYuefModels();
                visit.setShifsh(true);
                setTbmsg(null);
            }
        }
        getSelectData();
    }

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
            for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
                Object obj = _NianfModel.getOption(i);
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

    //---------------------------------------------------------
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


    //	 得到登陆用户所在电厂或者分公司的名称
    public String getDiancmc() {
        String diancmc = "";
        JDBCcon cn = new JDBCcon();
        long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
        String sql_diancmc = "select d.quanc from diancxxb d where d.id="
                + diancid;
        ResultSet rs = cn.getResultSet(sql_diancmc);
        try {
            while (rs.next()) {
                diancmc = rs.getString("quanc");
            }
            rs.close();
        } catch (SQLException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        } finally {
            cn.Close();
        }

        return diancmc;

    }

    //得到电厂树下拉框的电厂名称或者分公司,集团的名称
    public String getIDropDownDiancmc(String diancmcId) {
        if (diancmcId == null || diancmcId.equals("")) {
            diancmcId = "1";
        }
        String IDropDownDiancmc = "";
        JDBCcon cn = new JDBCcon();

        String sql_diancmc = "select d.mingc from diancxxb d where d.id=" + diancmcId;
        ResultSet rs = cn.getResultSet(sql_diancmc);
        try {
            while (rs.next()) {
                IDropDownDiancmc = rs.getString("mingc");
            }
            rs.close();
        } catch (SQLException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        } finally {
            cn.Close();
        }

        return IDropDownDiancmc;

    }

    //得到电厂的默认到站
    public String getDiancDaoz() {
        String daoz = "";
        String treeid = this.getTreeid();
        if (treeid == null || treeid.equals("")) {
            treeid = "1";
        }
        JDBCcon con = new JDBCcon();
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("select dc.mingc, cz.mingc  as daoz\n");
            sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
            sql.append(" where dd.diancxxb_id=dc.id\n");
            sql.append(" and  dd.chezxxb_id=cz.id\n");
            sql.append("   and dc.id = " + treeid + "");

            ResultSet rs = con.getResultSet(sql.toString());

            while (rs.next()) {
                daoz = rs.getString("daoz");
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }

        return daoz;
    }


    private String treeid;

    public String getTreeid() {
        String treeid = ((Visit) getPage().getVisit()).getString2();
        if (treeid == null || treeid.equals("")) {
            ((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
        }
        return ((Visit) getPage().getVisit()).getString2();
    }

    public void setTreeid(String treeid) {
        ((Visit) getPage().getVisit()).setString2(treeid);
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


    //	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
    public int getDiancTreeJib() {
        JDBCcon con = new JDBCcon();
        int jib = -1;
        String DiancTreeJib = this.getTreeid();
        //System.out.println("jib:" + DiancTreeJib);
        if (DiancTreeJib == null || DiancTreeJib.equals("")) {
            DiancTreeJib = "0";
        }
        String sqlJib = "select d.jib from diancxxb d where d.id=" + DiancTreeJib;
        ResultSet rs = con.getResultSet(sqlJib.toString());

        try {
            while (rs.next()) {
                jib = rs.getInt("jib");
            }
            rs.close();
        } catch (SQLException e) {

            e.printStackTrace();
        } finally {
            con.Close();
        }

        return jib;
    }
}