package com.zhiren.jt.zdt.monthreport.tianb.yueshcy;

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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.*;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
* 时间：2009-03-26
* 作者： ll
* 修改内容：1、去掉审核和回退功能。
* 		   2、添加判断循环累计的时间，如果本月没有数据，循环将结束。
*/
/*
* 时间：2009-05-18
* 作者： ll
* 修改内容：由于平圩电厂是一厂两制，平圩一电和平圩二电同在一个厂级系统中填报报表数据。
* 		   当两个电厂填报同一个月报页面时页面出错。所以在beginResponse()中增加了用户级别为电厂级，
  判断登陆电厂与电厂树是否一致，并重新加载刷新页面。
*
*/

/*
* 时间：2009-08-4
* 作者： sy
* 修改内容：页面保留三位小数
*
*/

/*
* 时间：2009-11-02
* 作者： ll
* 修改内容：修改页面生成功能，增加自动计算上月品种累计。
*
*/
public class Yueshcytb extends BasePage implements PageValidateListener {
    private static final int youhl_shouyl = 0;
    private static final int youhl_sunh = 4;
    private static final int youhl_fadyy = 1;
    private static final int youhl_gongry = 2;
    private static final int youhl_qithy = 3;
    private static final int youhl_panyk =5;
    private static final int youhl_pinzb_id =6;
    //	private static  String pinzb_id="";
    private static final int qickc_kc = 0;
    private static final int huiz_qickc = 0;
    private static final int huiz_shouyl = 1;
    private static final int huiz_fadyy = 2;
    private static final int huiz_gongry = 3;
    private static final int huiz_qithy = 4;
    private static final int huiz_sunh = 5;
    private static final int huiz_diaocl = 6;
    private static final int huiz_panyk = 7;
    private static final int huiz_kuc  = 8;
    private static final int huiz_pinzb_id=9;

    //	界面用户提示
    private String msg="";
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = MainGlobal.getExtMessageBox(msg,false);;
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

    private void Save() {
        //--------------------------------
        String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
        JDBCcon con = new JDBCcon();
        Visit visit = (Visit) this.getPage().getVisit();
        ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
        StringBuffer sql = new StringBuffer();
        while (rsl.next()) {
            sql.delete(0, sql.length());
            sql.append("begin \n");
            long id = 0;
            if ("0".equals(rsl.getString("ID"))) {
                id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
                sql.append("insert into yueshcyb("
                        + "id,riq,fenx,qickc,shouyl,fadyy,gongry,qithy,sunh,diaocl,panyk,kuc,diancxxb_id,pinzb_id)values("
                        + id
                        +","
                        +CurrODate
                        + ",'本月',"
                        +rsl.getDouble("qickc")+","
                        +rsl.getDouble("shouyl")+","
                        +rsl.getDouble("fadyy")+","
                        +rsl.getDouble("gongry")+","
                        +rsl.getDouble("qithy")+","
                        +rsl.getDouble("sunh")+","
                        +rsl.getDouble("diaocl")+","
                        +rsl.getDouble("panyk")+","
                        +rsl.getDouble("kuc")+","
                        +visit.getDiancxxb_id()+","
                        +"(select id from pinzb where mingc='"+rsl.getString("pinzb_id")+"'));\n");
            } else {
                sql.append("update yueshcyb set qickc="
                        + rsl.getDouble("qickc")+",shouyl="
                        + rsl.getDouble("shouyl")+",fadyy="
                        + rsl.getDouble("fadyy")+",gongry="
                        + rsl.getDouble("gongry")+",qithy="
                        + rsl.getDouble("qithy")+",sunh="
                        + rsl.getDouble("sunh")+",diaocl="
                        + rsl.getDouble("diaocl")+",panyk="
                        + rsl.getDouble("panyk")+",kuc="
                        + rsl.getDouble("kuc")+",pinzb_id=(select id from pinzb where mingc='"
                        + rsl.getString("pinzb_id")
                        + "')  where id=" + rsl.getLong("id")+";\n");
            }
            sql.append("end;");
            con.getUpdate(sql.toString());
            //*************************修改添加累计值************************//
            LeijSelect();
        }
        con.Close();
        setMsg("保存成功!");
    }
    public void LeijSelect() {

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
        // 当月份是1的时候显示01,
        String StrMonth = "";
        if (intMonth < 10) {

            StrMonth = "0" + intMonth;
        } else {
            StrMonth = "" + intMonth;
        }
        String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
        Visit visit = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
//		添加累计
        String sqllj ="";
        int i=0;
        for(i=0;i<=12-intMonth;i++){

            boolean jzyf=false;//设置截止月份的boolean
            String pdyf="select hcy.id as id from yueshcyb hcy where hcy.diancxxb_id=" +visit.getDiancxxb_id()+" and hcy.riq=to_date('"+intyear+"-"+(intMonth+i)+"-01','yyyy-mm-dd')" ;
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

                if(intMonth+i!=1){
                    sqllj=
                            "select s.diancxxb_id, s.pinzb_id,sum(s.qickc) as qickc,sum(s.shouyl) as shouyl, sum(s.fadyy) as fadyy,sum(s.gongry) as gongry,sum(s.qithy) as qithy, sum(s.sunh) as sunh,sum(s.diaocl) as diaocl, sum(s.panyk) as panyk, max(s.kuc) as kuc\n" +
                                    "  from yueshcyb s\n" +
                                    "  where (( s.riq= to_date('"+intyear+"-"+(intMonth+i)+"-01','yyyy-mm-dd') and s.fenx = '本月') or\n" +
                                    "         (s.riq=add_months( to_date('"+intyear+"-"+(intMonth+i)+"-01','yyyy-mm-dd'), -1) and s.fenx = '累计')) and s.diancxxb_id =" +visit.getDiancxxb_id()+
                                    "  group by (s.diancxxb_id,s.pinzb_id)\n";
                }else{
                    sqllj =
                            "select s.id,s.fenx,s.pinzb_id,s.qickc,s.shouyl,s.fadyy,s.gongry,s.qithy,s.sunh,s.diaocl,s.panyk,s.kuc\n" +
                                    " from yueshcyb s " +
                                    "where s.riq=to_date('"+intyear+"-"+(intMonth+i)+"-01','yyyy-mm-dd')"
                                    +" and s.diancxxb_id="+visit.getDiancxxb_id()+"and s.fenx='本月' order by s.fenx";
                }
                ResultSetList rsllj = con.getResultSetList(sqllj);

                if (rsllj.getRows()!=0) {

                    StringBuffer strsqllj = new StringBuffer("begin \n");
                    long yuelj_id = 0;

                    //			 		获取累计当月累计状态
                    String shzt="select s.zhuangt as zhuangt from yueshcyb s,pinzb pz where s.diancxxb_id="+visit.getDiancxxb_id()+" and s.riq=to_date('"+ intyear + "-"+ (intMonth+i) + "-01','yyyy-mm-dd') and s.fenx='累计' and s.pinzb_id =pz.id and pz.id=(select id from pinzb where mingc='"+rsl.getString("pinzb_id")+"') \n";
                    ResultSetList shenhzt=con.getResultSetList(shzt);
                    long zhuangt=0;
                    while(shenhzt.next()){
                        zhuangt=shenhzt.getLong("zhuangt");
                    }
                    //				----------删除所选月份的全部累计，重新计算添加累计值---
                    strsqllj.append("delete from ").append("yueshcyb").append(
                            " where id in").append("(select distinct sl.id from yueshcyb sl where sl.riq=to_date('"+intyear+"-"+(intMonth+i)+"-01','yyyy-mm-dd') and sl.fenx='累计' and sl.diancxxb_id ="+visit.getDiancxxb_id() +
                            ")").append(";\n");
                    while (rsllj.next()) {
                        yuelj_id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));

                        strsqllj.append("insert into yueshcyb("
                                + "id,riq,fenx,qickc,shouyl,fadyy,gongry,qithy,sunh,diaocl,panyk,kuc,diancxxb_id,pinzb_id,zhuangt)values("
                                + yuelj_id
                                +",to_date('"+intyear+"-"+(intMonth+i)+"-01','yyyy-mm-dd'),'累计',"
                                +rsllj.getDouble("qickc")+","
                                +rsllj.getDouble("shouyl")+","
                                +rsllj.getDouble("fadyy")+","
                                +rsllj.getDouble("gongry")+","
                                +rsllj.getDouble("qithy")+","
                                +rsllj.getDouble("sunh")+","
                                +rsllj.getDouble("diaocl")+","
                                +rsllj.getDouble("panyk")+","
                                +rsllj.getDouble("kuc")+","
                                +visit.getDiancxxb_id()+","
                                +rsllj.getString("pinzb_id")+","
                                +zhuangt +");\n");
                    }

                    strsqllj.append("end;");
                    con.getInsert(strsqllj.toString());
                }
                con.Close();
            }
        }
    }
    private boolean _SaveClick = false;

    public void SaveButton(IRequestCycle cycle) {
        _SaveClick = true;
    }

    private boolean _Refreshclick = false;

    public void RefreshButton(IRequestCycle cycle) {
        _Refreshclick = true;
    }


    private boolean _CreateClick = false;

    public void CreateButton(IRequestCycle cycle) {
        _CreateClick = true;
    }

    private boolean _DelClick = false;

    public void DelButton(IRequestCycle cycle) {
        _DelClick = true;
    }

    public void submit(IRequestCycle cycle) {
        if (_SaveClick) {
            _SaveClick = false;
            Save();
            getSelectData(null);
        }
        if (_Refreshclick) {
            _Refreshclick = false;
            setRiq();
            getSelectData(null);
        }

        if (_CreateClick) {
            _CreateClick = false;
            CreateData();
            getSelectData(null);
        }
        if (_DelClick) {
            _DelClick = false;
            DelData();
        }
    }
    public void DelData() {
        Visit visit = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        String CurrZnDate = getNianf() + "年" + getYuef() + "月";
        String riq = getNianf() + "-" + getYuef();
        try {
            String strSql = "delete from yueshcyb where to_char(riq,'yyyy-MM')='"+riq +"' and diancxxb_id = "+getTreeid();
            int flag = con.getDelete(strSql);
            if (flag == -1) {
                WriteLog.writeErrorLog(ErrorMessage.DeleteYuezlbFailed + "SQL:"
                        + strSql);
                setMsg("删除过程中发生错误！");
            } else {
                setMsg(CurrZnDate + "的数据被成功删除！");
            }
            con.Close();
        } catch (Exception e) {
            setMsg("删除过程中发生错误！");
            e.printStackTrace();
        } finally {
            con.Close();
        }
    }
    public double[][] getYouhl(JDBCcon con, Visit visit, String CurrODate) {
        double youhl[][] = null;
        String strSql = "select shcyb.pinzb_id,nvl(sum(shcyb.shourl),0) as shouyl,nvl(sum(shcyb.fady),0) as fadyy,nvl(sum(shcyb.gongry),0) as gongry\n" +
                "		,nvl(sum(shcyb.qity),0) as qithy,nvl(sum(shcyb.cuns),0) as sunh,nvl(sum(shcyb.panyk),0) as panyk"+
                " from shouhcrbyb shcyb where shcyb.riq >="+CurrODate+" and shcyb.riq < Add_Months("+CurrODate+",1)\n" +
                " and shcyb.diancxxb_id="+visit.getDiancxxb_id()+"\n"+
                " group by(shcyb.pinzb_id)\n";
        ResultSetList rs=con.getResultSetList(strSql);
        int rows=rs.getRows();
        if (rs == null) {//判断是否连接失败
            WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + strSql);
            setMsg(ErrorMessage.NullResult);
            return youhl;
        }
        youhl = new double[rows][7];
        for(int i=0;rs.next();i++) {
//			pinzb_id=rs.getString("pinzb_id");
//			youhl[youhl_pinzb_id]=rs.getDouble("pinb_id");//品种
            youhl[i][youhl_shouyl]= rs.getDouble("shouyl");//收油量
            youhl[i][youhl_fadyy] = rs.getDouble("fadyy");//发热用
            youhl[i][youhl_gongry] = rs.getDouble("gongry");//供热用
            youhl[i][youhl_qithy] = rs.getDouble("qithy");//其它用
            youhl[i][youhl_sunh]= rs.getDouble("sunh");//损耗
            youhl[i][youhl_panyk]=rs.getDouble("panyk");//盘盈亏
            youhl[i][youhl_pinzb_id]=rs.getDouble("pinzb_id");//品种
        }
//		else {
////			不可能发生的错误
//			setMsg("未知错误！");
//			return youhl;
//		}

        return youhl;
    }

    public double[] getQickc(JDBCcon con, Visit visit, String CurrODate,int pinzb_id) {
        double qickc[] = null;

        String strSql=" select kuc from yueshcyb where riq=Add_Months("+CurrODate+",-1) and fenx ='本月' and diancxxb_id="+visit.getDiancxxb_id()+" and pinzb_id= "+pinzb_id;//查询上个月的库存数
        ResultSetList rs=con.getResultSetList(strSql);
        if (rs == null) {//判断是否连接失败
            WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + strSql);
            setMsg(ErrorMessage.NullResult);
            return qickc;
        }
        qickc = new double[1];
        if (rs.next()) {//但到上月的库存数据，本月/累计
            qickc[qickc_kc] = rs.getDouble("kuc");
        }else {
            qickc[qickc_kc] = 0.0;
        }
        return qickc;
    }

	/*public double[] getLeij(JDBCcon con,int pinzb_id) {
		Visit visit = (Visit) getPage().getVisit();
		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
		double leij[] =null;

		String strSql=" select qickc,shouyl,fadyy,gongry,qithy,sunh,diaocl,panyk,kuc " +
				"from yueshcyb where pinzb_id="+pinzb_id+" and riq=Add_Months("+CurrODate+",-1) and fenx ='累计' and diancxxb_id="+visit.getDiancxxb_id();//查询上个月的库存数
		ResultSetList rs=con.getResultSetList(strSql);
		if (rs == null) {//判断是否连接失败
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + strSql);
			setMsg(ErrorMessage.NullResult);
			return leij;
		}
		leij = new double[9];
		if(rs.next()) {
			leij[huiz_qickc] = rs.getDouble("qickc");
			leij[huiz_shouyl] = rs.getDouble("shouyl");
			leij[huiz_fadyy] = rs.getDouble("fadyy");
			leij[huiz_gongry] = rs.getDouble("gongry");
			leij[huiz_qithy] = rs.getDouble("qithy");
			leij[huiz_sunh] = rs.getDouble("sunh");
			leij[huiz_diaocl] = rs.getDouble("diaocl");
			leij[huiz_panyk] = rs.getDouble("panyk");
			leij[huiz_kuc] = rs.getDouble("kuc");
		}else{
			for(int i =0; i < leij.length ; i++) {
				leij[i] = 0.0;
			}
		}
		return leij;
	}*/

    public double[][] getLeij(JDBCcon con, Visit visit, String CurrODate) {
        double leij[][] = null;
        String strSql = "select sum(ys.qickc) as qickc,sum(ys.shouyl) as shouyl, sum(ys.fadyy) as fadyy,sum(ys.gongry) as gongry,sum(ys.qithy) as qithy,\n"
                + "                      sum(ys.sunh) as sunh,sum(ys.diaocl) as diaocl, sum(ys.panyk) as panyk, max(ys.kuc) as kuc,pinzb_id\n"
                + "from (select s.diancxxb_id, s.pinzb_id,sum(s.qickc) as qickc,sum(s.shouyl) as shouyl, sum(s.fadyy) as fadyy,sum(s.gongry) as gongry,sum(s.qithy) as qithy,\n"
                + "                      sum(s.sunh) as sunh,sum(s.diaocl) as diaocl, sum(s.panyk) as panyk, max(s.kuc) as kuc\n"
                + "              from yueshcyb s\n"
                + "              where s.riq=add_months("+CurrODate+", -1) and s.fenx = '累计' and s.diancxxb_id = "+visit.getDiancxxb_id()+"\n"
                + "              group by (s.diancxxb_id,s.pinzb_id)\n"
                + "      union\n"
                + "      select shcyb.diancxxb_id,shcyb.pinzb_id,0 as qickc,nvl(sum(shcyb.shourl),0) as shouyl,nvl(sum(shcyb.fady),0) as fadyy,nvl(sum(shcyb.gongry),0) as gongry\n"
                + "                ,nvl(sum(shcyb.qity),0) as qithy,nvl(sum(shcyb.cuns),0) as sunh,0 as diaocl,nvl(sum(shcyb.panyk),0) as panyk,0 as kuc\n"
                + "             from shouhcrbyb shcyb where shcyb.riq >="+CurrODate+"\n"
                + "                   and shcyb.riq < Add_Months("+CurrODate+",1)\n"
                + "                   and shcyb.diancxxb_id="+visit.getDiancxxb_id()+"\n"
                + "             group by(shcyb.diancxxb_id,shcyb.pinzb_id)\n"
                + ")ys\n" + "group by (ys.pinzb_id)";
        ResultSetList rs=con.getResultSetList(strSql);
        int rows=rs.getRows();
        if (rs == null) {//判断是否连接失败
            WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + strSql);
            setMsg(ErrorMessage.NullResult);
            return leij;
        }
        leij = new double[rows][10];
        for(int i=0;rs.next();i++) {
            leij[i][huiz_qickc] = rs.getDouble("qickc");
            leij[i][huiz_shouyl] = rs.getDouble("shouyl");
            leij[i][huiz_fadyy] = rs.getDouble("fadyy");
            leij[i][huiz_gongry] = rs.getDouble("gongry");
            leij[i][huiz_qithy] = rs.getDouble("qithy");
            leij[i][huiz_sunh] = rs.getDouble("sunh");
            leij[i][huiz_diaocl] = rs.getDouble("diaocl");
            leij[i][huiz_panyk] = rs.getDouble("panyk");
            leij[i][huiz_kuc] = rs.getDouble("kuc");
            leij[i][huiz_pinzb_id] = rs.getLong("pinzb_id");
        }

        return leij;
    }

    public void CreateData() {
        Visit visit = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        con.setAutoCommit(false);
        String CurrZnDate = getNianf()+"年"+getYuef()+"月";
        String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
        int intYuef=Integer.parseInt(getYuef());
//		StringBuffer sql=new StringBuffer();
//		sql.append("begin \n");
        String strSql="";
        int flag = 0;

//		取得当月耗煤量 [0] 发电用  [1] 供热用 [2] 其它用 = 其它用 + 非生产用
        double youhl[][] = getYouhl(con,visit,CurrODate);
        if(youhl == null) {
            return;
        }
        int rows=youhl.length;
        double shouyl=0;
        double sunh= 0;
        double fadyy= 0;
        double gongry= 0;
        double qithy= 0;
        double panyk=0;
        int pinzb_id=0;
        for (int k=0;k<rows;k++){
            //定义数据
            shouyl=youhl[k][youhl_shouyl];
            sunh= youhl[k][youhl_sunh];
            fadyy= youhl[k][youhl_fadyy];
            gongry= youhl[k][youhl_gongry];
            qithy= youhl[k][youhl_qithy];
            panyk=youhl[k][youhl_panyk];
            pinzb_id=(int)youhl[k][youhl_pinzb_id];
            //		本月数组
            double beny[] = new double[10];
            //		当月盘盈亏
            //		double panyk = 0.0;//盘盈亏

            //		当月库存
            double kuc = 0.0;
            //		取得期初库存 [0] 当月期初库存
            double qickc[] = getQickc(con,visit,CurrODate,pinzb_id);
            if(qickc == null) {
                return;
            }
            //		计算当月库存
            kuc += qickc[qickc_kc];
            kuc += (shouyl - sunh - fadyy - gongry - qithy -panyk);
            //		kuc += panyk;
            //		本月数组赋值 [0] 期初库存 [1] 收煤量 [2] 发电用 [3] 供热用 [4] 其它耗 [5] 损耗 [6] 调出量 [7] 盘盈亏 [8] 库存
            beny[huiz_qickc] = qickc[0];
            beny[huiz_shouyl] = shouyl;
            beny[huiz_fadyy] = fadyy;
            beny[huiz_gongry] = gongry;
            beny[huiz_qithy] = qithy;
            beny[huiz_sunh] = sunh;
            beny[huiz_diaocl] = 0;
            beny[huiz_panyk] = panyk;
            beny[huiz_kuc] = kuc;

            //		删除本月数
            strSql = "delete from yueshcyb where pinzb_id="+pinzb_id+" and riq="+CurrODate
                    +" and diancxxb_id = "+visit.getDiancxxb_id();
            flag = con.getDelete(strSql);
            if(flag == -1) {
                WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail+"SQL:"+strSql);
                setMsg("生成过程出现错误！月收耗存合计删除失败！");
                con.rollBack();
                con.Close();
                return;
            }
            //		生成当月数
            strSql = "insert into yueshcyb(id,diancxxb_id,riq,fenx,qickc,shouyl,fadyy,gongry,qithy,sunh,diaocl,panyk,kuc,pinzb_id) values("
                    + Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))
                    + "," + visit.getDiancxxb_id()
                    +","+CurrODate+",'本月',"+beny[huiz_qickc]+","+beny[huiz_shouyl]+","
                    + beny[huiz_fadyy] +","+beny[huiz_gongry]+","+beny[huiz_qithy]+","+beny[huiz_sunh]+","
                    + beny[huiz_diaocl]+","+beny[huiz_panyk]+","+beny[huiz_kuc]+","+pinzb_id+")";
            flag = con.getInsert(strSql);
            if(flag == -1) {
                WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+strSql);
                setMsg("生成过程出现错误！月收耗存合计未插入成功！");
                con.rollBack();
                con.Close();
                return;
            }

        }
//		取得上月累计数
        double leij[][] = getLeij(con,visit,CurrODate);
        if(leij == null) {
            return;
        }
        int rows2=leij.length;
//		新建本月的累计数
        double benylj[] = new double[10];

//		删除本月数
        strSql = "delete from yueshcyb where riq="+CurrODate
                +" and fenx='累计' and  diancxxb_id = "+visit.getDiancxxb_id();
        flag = con.getDelete(strSql);
        if(flag == -1) {
            WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail+"SQL:"+strSql);
            setMsg("生成过程出现错误！月收耗存合计删除失败！");
            con.rollBack();
            con.Close();
            return;
        }
//		计算本月累计数
        for (int g=0;g<rows2;g++){
            benylj[huiz_qickc]=leij[g][huiz_qickc];
            benylj[huiz_shouyl]=leij[g][huiz_shouyl];
            benylj[huiz_fadyy]=leij[g][huiz_fadyy];
            benylj[huiz_gongry]=leij[g][huiz_gongry];
            benylj[huiz_qithy]=leij[g][huiz_qithy];
            benylj[huiz_sunh]=leij[g][huiz_sunh];
            benylj[huiz_diaocl]=leij[g][huiz_diaocl];
            benylj[huiz_panyk]=leij[g][huiz_panyk];
            benylj[huiz_kuc]=leij[g][huiz_kuc];
            int pinzid=(int)leij[g][huiz_pinzb_id];

            //		生成累计数
            if(intYuef == 1) {
                //			如果是一月累计==本月
                strSql = "insert into yueshcyb(id,diancxxb_id,riq,fenx,qickc,shouyl,fadyy,gongry,qithy,sunh,diaocl,panyk,kuc,pinzb_id) values("
                        + Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))
                        + "," + visit.getDiancxxb_id()
                        +","+CurrODate+",'累计',"+benylj[huiz_qickc]+","+benylj[huiz_shouyl]+","
                        + benylj[huiz_fadyy] +","+benylj[huiz_gongry]+","+benylj[huiz_qithy]+","+benylj[huiz_sunh]+","
                        + benylj[huiz_diaocl]+","+benylj[huiz_panyk]+","+benylj[huiz_kuc]+","+pinzid+")";
            }else {
                //			如果不是一月则累计==上月累计+本月
                strSql = "insert into yueshcyb(id,diancxxb_id,riq,fenx,qickc,shouyl,fadyy,gongry,qithy,sunh,diaocl,panyk,kuc,pinzb_id) values("
                        + Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))
                        + "," + visit.getDiancxxb_id()
                        +","+CurrODate+",'累计',"+benylj[huiz_qickc]+","+benylj[huiz_shouyl]+","
                        + benylj[huiz_fadyy] +","+benylj[huiz_gongry]+","+benylj[huiz_qithy]+","+benylj[huiz_sunh]+","
                        + benylj[huiz_diaocl]+","+benylj[huiz_panyk]+","+benylj[huiz_kuc]+","+pinzid+")";
            }
            flag = con.getInsert(strSql);
            if(flag == -1) {
                WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+strSql);
                setMsg("生成过程出现错误！月收耗存合计未插入成功！");
                con.rollBack();
                con.Close();
                return;
            }
        }
        con.commit();
        con.Close();
        setMsg(CurrZnDate+"的数据成功生成！");
    }

    public void getSelectData(ResultSetList rsl) {
        Visit visit = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        String zhuangt="";
        String zhuangt2="";
        if(visit.isShifsh()==true){
            if(visit.getRenyjb()==3){
                zhuangt="";
            }else if(visit.getRenyjb()==2){
                zhuangt=" and (s.zhuangt=1 or s.zhuangt=2)";
                zhuangt2=" and (shcy.zhuangt=1 or shcy.zhuangt=2)";
            }else if(visit.getRenyjb()==1){
                zhuangt=" and s.zhuangt=2";
                zhuangt2=" and shcy.zhuangt=2";
            }
        }
        String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");

        // ----------电厂树--------------
        String str = "";


        int treejib = this.getDiancTreeJib();
        if (treejib == 1) {// 选集团时刷新出所有的电厂
            str = "";
        } else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
            str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
                    + getTreeid() + ")";

        } else if (treejib == 3) {// 选电厂只刷新出该电厂
            str = "and dc.id = " + getTreeid() + "";

        }
//			---------------------------------------------------------------------
//			if(isReture) {
//				setMsg("如果有需要回退信息时，请选择所要回退的公司或电厂名称！");
//			}
//		----------------------------------------------------------------------

        String strSql = "select * from shouhcrbyb shcyb,diancxxb dc where to_char(shcyb.riq,'yyyy-mm') = '"
                +getNianf()+"-"+getYuef()+"' and shcyb.diancxxb_id=dc.id "+str;
        boolean isNotReady = !con.getHasIt(strSql);
        if(isNotReady) {
//			setMsg("请在使用本模块之前，首先完成收耗存日报油表数据的计算！");
        }
        if(rsl==null){
//		strSql="select s.id,s.fenx,pz.mingc as pinzb_id,s.qickc,s.shouyl,s.fadyy,s.gongry,s.qithy,s.sunh,s.diaocl,s.panyk,s.kuc,s.zhuangt\n" +
//		" from yueshcyb s,pinzb pz,diancxxb dc where  s.pinzb_id=pz.id and s.riq="+CurrODate+zhuangt+" and s.diancxxb_id=dc.id "+str+"and s.fenx='本月' order by s.fenx";
            strSql=
                    " select nvl(x.id,0) as id,\n" +
                            "decode(x.fenx,null,'本月',x.fenx) as fenx,\n" +
                            "     y.pzb_id as pinzb_id,nvl(x.qickc,0) as qickc,nvl(x.shouyl,0) as shouyl,nvl(x.fadyy,0) as fadyy,nvl(x.gongry,0) as gongry,\n" +
                            "     nvl(x.qithy,0) as qithy,nvl(x.sunh,0) as sunh,nvl(x.diaocl,0) as diaocl,nvl(x.panyk,0) as panyk,nvl(x.kuc,0) as kuc,\n" +
                            "     nvl(x.zhuangt,0) as zhuangt\n" +
                            "from (\n" +
                            "select nvl(sj.id,0) as id,decode(sj.fenx,null,'本月',sj.fenx) as fenx,\n" +
                            "       sj.pinzb_id as pinzb_id,nvl(sj.qickc,0) as qickc,nvl(sj.shouyl,0) as shouyl,\n" +
                            "       nvl(sj.fadyy,0) as fadyy,nvl(sj.gongry,0) as gongry,nvl(sj.qithy,0) as qithy,\n" +
                            "     nvl(sj.sunh,0) as sunh,nvl(sj.diaocl,0) as diaocl,nvl(sj.panyk,0) as panyk,nvl(sj.kuc,0) as kuc,sj.zhuangt\n" +
                            "from diancxxb dc,\n" +
                            "(select nvl(s.id,0) as id,s.diancxxb_id,s.riq,s.fenx,pz.mingc as pinzb_id,s.qickc,s.shouyl,s.fadyy,s.gongry,s.qithy,s.sunh,s.diaocl,s.panyk,s.kuc,s.zhuangt\n" +
                            " from yueshcyb s,pinzb pz\n" +
                            " where  s.pinzb_id(+)=pz.id and s.riq="+CurrODate+zhuangt+"  and s.fenx='本月' )sj\n" +
                            " where dc.id=sj.diancxxb_id(+) "+str+" order by dc.xuh\n" +
                            " )x,\n" +
                            "(  select pzb.mingc as pzb_id from yueshcyb shcy,pinzb pzb,diancxxb dc\n" +
                            "              where shcy.riq="+CurrODate+zhuangt2+"\n" +
                            "                         and shcy.fenx='本月' and shcy.pinzb_id=pzb.id and shcy.diancxxb_id=dc.id "+str+"\n" +
                            "       union\n" +
                            "   select pzb.mingc as pzb_id from yueshcyb shcy,pinzb pzb,diancxxb dc\n" +
                            "            where shcy.riq=add_months("+CurrODate+",-1)"+zhuangt2+"\n" +
                            "                        and shcy.fenx='本月' and shcy.pinzb_id=pzb.id and shcy.diancxxb_id=dc.id "+str+" ) y\n" +
                            " where x.pinzb_id(+)=y.pzb_id";



            rsl = con.getResultSetList(strSql);
        }
        boolean yincan=false;
        while(rsl.next()){
            if(visit.getRenyjb()==3){
                if(rsl.getLong("zhuangt")==0){
                    yincan = false;
                }else{
                    yincan = true;
                }
            }else if(visit.getRenyjb()==1||visit.getRenyjb()==2){
                yincan = true;
            }

        }
        rsl.beforefirst();
        boolean showBtn=false;
        if(rsl.next()){
            rsl.beforefirst();
            showBtn = false;
        }else{
            showBtn=true;
        }
        ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
        egu.setGridType(ExtGridUtil.Gridstyle_Edit);
        egu.addPaging(0);
        // //设置表名称用于保存
        egu.setTableName("yueshcyb");
        // /设置显示列名称
        egu.setWidth("bodyWidth");

        egu.getColumn("fenx").setHeader("分项");
        egu.getColumn("fenx").setWidth(60);
        egu.getColumn("fenx").setHidden(true);
        egu.getColumn("fenx").setEditor(null);
        egu.getColumn("fenx").setDefaultValue("本月");
        egu.getColumn("pinzb_id").setHeader("品种");
        egu.getColumn("pinzb_id").setEditor(new ComboBox());
        egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
                new IDropDownModel("select id, mingc from pinzb where leib='油'"));
        egu.getColumn("pinzb_id").setReturnId(true);
        egu.getColumn("pinzb_id").setWidth(60);
        egu.getColumn("qickc").setHeader("期初库存(吨)");
        egu.getColumn("qickc").setDefaultValue(0+"");
        egu.getColumn("qickc").setWidth(80);
        egu.getColumn("shouyl").setHeader("收油量(吨)");
        egu.getColumn("shouyl").setDefaultValue(0+"");
        egu.getColumn("shouyl").setWidth(80);
        egu.getColumn("fadyy").setHeader("发电用油(吨)");
        egu.getColumn("fadyy").setDefaultValue(0+"");
        egu.getColumn("fadyy").setWidth(80);
        egu.getColumn("gongry").setHeader("供热用油(吨)");
        egu.getColumn("gongry").setDefaultValue(0+"");
        egu.getColumn("gongry").setWidth(80);
        egu.getColumn("qithy").setHeader("其它用油(吨)");
        egu.getColumn("qithy").setDefaultValue(0+"");
        egu.getColumn("qithy").setWidth(80);
        egu.getColumn("sunh").setHeader("损耗(吨)");
        egu.getColumn("sunh").setDefaultValue(0+"");
        egu.getColumn("sunh").setWidth(60);
        egu.getColumn("diaocl").setHeader("调出量(吨)");
        egu.getColumn("diaocl").setDefaultValue(0+"");
        egu.getColumn("diaocl").setWidth(80);
        egu.getColumn("panyk").setHeader("盘盈亏(吨)");
        egu.getColumn("panyk").setDefaultValue(0+"");
        egu.getColumn("panyk").setWidth(80);
        egu.getColumn("kuc").setHeader("库存(吨)");
        egu.getColumn("kuc").setDefaultValue(0+"");
        egu.getColumn("kuc").setWidth(80);
        egu.getColumn("zhuangt").setHeader("状态");
        egu.getColumn("zhuangt").setHidden(true);
        egu.getColumn("zhuangt").setEditor(null);


        egu.getColumn("kuc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
        egu.getColumn("kuc").setEditor(null);
//		设定列的小数位
        ((NumberField)egu.getColumn("qickc").editor).setDecimalPrecision(3);
        ((NumberField)egu.getColumn("shouyl").editor).setDecimalPrecision(3);
        ((NumberField)egu.getColumn("fadyy").editor).setDecimalPrecision(3);
        ((NumberField)egu.getColumn("gongry").editor).setDecimalPrecision(3);
        ((NumberField)egu.getColumn("qithy").editor).setDecimalPrecision(3);
        ((NumberField)egu.getColumn("sunh").editor).setDecimalPrecision(3);
        ((NumberField)egu.getColumn("diaocl").editor).setDecimalPrecision(3);
        ((NumberField)egu.getColumn("panyk").editor).setDecimalPrecision(3);

        // /设置按钮
        egu.addTbarText("年份");
        ComboBox comb1=new ComboBox();
        comb1.setWidth(50);
        comb1.setTransform("NianfDropDown");
        comb1.setId("NianfDropDown");//和自动刷新绑定
        comb1.setLazyRender(true);//动态绑定
        comb1.setEditable(true);
        egu.addToolbarItem(comb1.getScript());

        egu.addTbarText("月份");
        ComboBox comb2=new ComboBox();
        comb2.setWidth(50);
        comb2.setTransform("YuefDropDown");
        comb2.setId("YuefDropDown");//和自动刷新绑定
        comb2.setLazyRender(true);//动态绑定
        comb2.setEditable(true);
        egu.addToolbarItem(comb2.getScript());
        egu.addTbarText("-");
//		电厂树
        egu.addTbarText("单位:");
        ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
        setTree(etu);
        egu.addTbarTreeBtn("diancTree");

        egu.addTbarText("-");// 设置分隔符

//		判断数据是否被锁定
//		boolean isLocked = isLocked(con);
//		刷新按钮
        StringBuffer rsb = new StringBuffer();
        rsb.append("function (){")
                .append(MainGlobal.getExtMessageBox("'正在刷新'+Ext.getDom('NianfDropDown').value+'年'+Ext.getDom('YuefDropDown').value+'月的数据,请稍候！'",true))
                .append("document.getElementById('RefreshButton').click();}");
        GridButton gbr = new GridButton("刷新",rsb.toString());
        gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
        egu.addTbarBtn(gbr);
        if(visit.getRenyjb()==3){
            if(yincan ==false){
                //		生成按钮
//				if(showBtn){
                GridButton gbc = new GridButton("生成",getBtnHandlerScript("CreateButton"));
                gbc.setDisabled(false);
                gbc.setIcon(SysConstant.Btn_Icon_Create);
                egu.addTbarBtn(gbc);
//				}
                //		删除按钮
                GridButton gbd = new GridButton("删除", getBtnHandlerScript("DelButton"));
                gbd.setIcon(SysConstant.Btn_Icon_Delete);
                egu.addTbarBtn(gbd);
                //		添加按钮
                egu.addToolbarButton(GridButton.ButtonType_Insert, null);
                //		保存按钮
                GridButton gbs = new GridButton(GridButton.ButtonType_SaveAll,"gridDiv",egu.getGridColumns(),"SaveButton");
                egu.addTbarBtn(gbs);
            }
        }
//		---------------页面js的计算开始------------------------------------------
        StringBuffer sb = new StringBuffer();

        sb.append("gridDiv_grid.on('afteredit',function(e){");

        sb.append("if(e.field == 'QICKC'||e.field == 'SHOUYL'||e.field == 'FADYY'||e.field == 'GONGRY'||e.field == 'QITHY'||e.field=='SUNH'||e.field=='DIAOCL'||e.field=='PANYK'||e.field=='KUC')" +
                "{e.record.set('KUC',parseFloat(e.record.get('QICKC')==''?0:e.record.get('QICKC'))+parseFloat(e.record.get('SHOUYL')==''?0:e.record.get('SHOUYL'))-parseFloat(e.record.get('FADYY')==''?0:e.record.get('FADYY'))-parseFloat(e.record.get('GONGRY')==''?0:e.record.get('GONGRY'))" +
                " -parseFloat(e.record.get('QITHY')==''?0:e.record.get('QITHY'))-parseFloat(e.record.get('SUNH')==''?0:e.record.get('SUNH'))-parseFloat(e.record.get('DIAOCL')==''?0:e.record.get('DIAOCL'))+parseFloat(e.record.get('PANYK')==''?0:e.record.get('PANYK')))};");

        sb.append("});");

        egu.addOtherScript(sb.toString());

        //---------------页面js计算结束--------------------------

        setExtGrid(egu);
        con.Close();
    }

    public String getBtnHandlerScript(String btnName) {
//		按钮的script
        StringBuffer btnsb = new StringBuffer();
        String cnDate = getNianf()+"年"+getYuef()+"月";
        btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
        if(btnName.endsWith("CreateButton")) {
            btnsb.append("新生成数据将覆盖")
                    .append(cnDate).append("的已存数据，是否继续？");
        }else {
            btnsb.append("是否删除").append(cnDate).append("的数据？");
        }
        btnsb.append("',function(btn){if(btn == 'yes'){")
                .append("document.getElementById('").append(btnName).append("').click()")
//		-------------------------------------------------------------------
                .append(";Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',")
                .append("width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO}); \n")
//		-------------------------------------------------------------------
                .append("}; // end if \n").append("});}");
        return btnsb.toString();
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
            visit.setString1(null);
            visit.setString2(null);
            visit.setString3(null);
            visit.setShifsh(true);
            this.setTreeid(null);
            setYuefValue(null);
            setNianfValue(null);
            this.getYuefModels();
            this.getNianfModels();
            setRiq();
        }
        if(visit.getRenyjb()==3){
            if(!this.getTreeid().equals(visit.getDiancxxb_id()+"")){
                visit.setActivePageName(getPageName().toString());
                visit.setList1(null);
                visit.setString1(null);
                visit.setString2(null);
                visit.setString3(null);
                visit.setShifsh(true);
                this.setTreeid(null);
                setYuefValue(null);
                setNianfValue(null);
                this.getYuefModels();
                this.getNianfModels();
                setRiq();
            }
        }
        getSelectData(null);
    }

	/*public long isLocked(JDBCcon con) {//判断数据是否已锁定
		int intYuef=Integer.parseInt(getYuef());
		String CurrXDate = DateUtil.FormatOracleDate(getNianf()+"-"+(intYuef+1)+"-01");
		String CurrODate=getNianf()+"-"+getYuef();
		Visit visit = (Visit) getPage().getVisit();
		long falge=0;

		if(intYuef!=12){
			if(con.getHasIt("select * from yueshchjb where riq="+CurrXDate)){//判断上个月是否有数据
				if(con.getHasIt("select * from yuehcb yh,yuetjkjb yj where yh.yuetjkjb_id=yj.id and " +
						"to_char(yj.riq,'yyyy-mm')='"+CurrODate+"'\n" +
						" and yj.diancxxb_id="+visit.getDiancxxb_id())){//判断耗存表是否有数据
					falge=1;//收煤有数
				}else{
					falge=2;//下个月有数
				}
			}else{
				if(con.getHasIt("select * from yuehcb yh,yuetjkjb yj where yh.yuetjkjb_id=yj.id and " +
						"to_char(yj.riq,'yyyy-mm')='"+CurrODate+"'\n" +
						" and yj.diancxxb_id="+visit.getDiancxxb_id())){//判断耗存表是否有数据
					falge=1;//收煤有数
				}else{
					falge=0;//可能删除
				}
			}
		}else{
			if(con.getHasIt("select * from yuehcb yh,yuetjkjb yj where yh.yuetjkjb_id=yj.id and " +
					"to_char(yj.riq,'yyyy-mm')='"+CurrODate+"'\n" +
					" and yj.diancxxb_id="+visit.getDiancxxb_id())){//判断耗存表是否有数据
				falge=1;//收煤有数
			}else{
				falge=0;//可能删除
			}
		}
		return falge;
	} */

/*	public boolean isLocked(JDBCcon con) {//判断数据是否已锁定_保存
		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-"+"01");
		Visit visit = (Visit) getPage().getVisit();
		return con.getHasIt("select * from yuehcb yh,yuetjkjb yj where yh.yuetjkjb_id=yj.id and " +
				" yj.riq="+CurrODate+"\n" +
				" and yj.diancxxb_id="+visit.getDiancxxb_id());
	}*/

    public String getNianf() {
        return ((Visit) getPage().getVisit()).getString1();
    }
    public void setNianf(String value) {
        ((Visit) getPage().getVisit()).setString1(value);
    }

    public String getYuef() {
        int intYuef = Integer.parseInt(((Visit) getPage().getVisit()).getString3());
        if (intYuef<10){
            return "0"+intYuef;
        }else{
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

    // 年份下拉框
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
            int _nianf = DateUtil.getYear(new Date());
            int _yuef = DateUtil.getMonth(new Date());
            if (_yuef == 1) {
                _nianf = _nianf - 1;
            }
            for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
                Object obj = getNianfModel().getOption(i);
                if (_nianf == ((IDropDownBean) obj).getId()) {
                    _NianfValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _NianfValue;
    }

    public void setNianfValue(IDropDownBean Value) {
        if  (_NianfValue!=Value){
            _NianfValue = Value;
        }
    }

    public IPropertySelectionModel getNianfModels() {
        List listNianf = new ArrayList();
        int i;
        for (i = 2004; i <= DateUtil.getYear(new Date())+1; i++) {
            listNianf.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _NianfModel = new IDropDownModel(listNianf);
        return _NianfModel;
    }

    public void setNianfModel(IPropertySelectionModel _value) {
        _NianfModel = _value;
    }

    // 月份下拉框
    private static IPropertySelectionModel _YuefModel;

    public IPropertySelectionModel getYuefModel() {
        if (_YuefModel == null) {
            getYuefModels();
        }
        return _YuefModel;
    }

    private IDropDownBean _YuefValue;

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
        if  (_YuefValue!=Value){
            _YuefValue = Value;
        }
    }

    public IPropertySelectionModel getYuefModels() {
        List listYuef = new ArrayList();
        for (int i = 1; i < 13; i++) {
            listYuef.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _YuefModel = new IDropDownModel(listYuef);
        return _YuefModel;
    }

    public void setYuefModel(IPropertySelectionModel _value) {
        _YuefModel = _value;
    }

//  电厂树

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
        }finally{
            cn.Close();
        }

        return diancmc;

    }
    //得到电厂树下拉框的电厂名称或者分公司,集团的名称
    public String getIDropDownDiancmc(String diancmcId) {
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
            rs.close();
        } catch (SQLException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }finally{
            cn.Close();
        }

        return IDropDownDiancmc;

    }

    //得到电厂的默认到站
    public String getDiancDaoz(){
        String daoz = "";
        String treeid=this.getTreeid();
        if(treeid==null||treeid.equals("")){
            treeid="1";
        }
        JDBCcon con = new JDBCcon();
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("select dc.mingc, cz.mingc  as daoz\n");
            sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
            sql.append(" where dd.diancxxb_id=dc.id\n");
            sql.append(" and  dd.chezxxb_id=cz.id\n");
            sql.append("   and dc.id = "+treeid+"");

            ResultSet rs = con.getResultSet(sql.toString());

            while (rs.next()) {
                daoz = rs.getString("daoz");
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            con.Close();
        }

        return daoz;
    }

    private String treeid;

    public String getTreeid() {
        String treeid=((Visit) getPage().getVisit()).getString2();
        if(treeid==null||treeid.equals("")){
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
        String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
        ResultSet rs = con.getResultSet(sqlJib.toString());

        try {
            while (rs.next()) {
                jib = rs.getInt("jib");
            }
            rs.close();
        } catch (SQLException e) {

            e.printStackTrace();
        }finally{
            con.Close();
        }

        return jib;
    }
}