package com.zhiren.jt.zdt.monthreport.changncwscsjwh_new;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zhiren.common.ext.form.Field;
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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/* 
* 时间：2009-03-30 
* 作者： wzb
* 修改内容：1、增加其它费用的指标，及js的计算
* 		   
*/ 
/* 
* 时间：2009-03-26 
* 作者： ll
* 修改内容：1、去掉审核和回退功能。
* 		   2、添加判断循环累计的时间，如果本月没有数据，循环将结束。
*/  
/* 
* 时间：2009-05-4
* 作者： ll
* 修改内容：1、替换表名,把yuezbb_zdt改为yuezbb。
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
public class Changnscsjwh_new extends BasePage implements PageValidateListener {
    private String msg = "";

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = MainGlobal.getExtMessageBox(msg,false);
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


    private void Save() {
        JDBCcon con = new JDBCcon();
        long intyear;
        if(this.getNianfValue() == null) {
            intyear = (long)DateUtil.getYear(new Date());
        } else {
            intyear = this.getNianfValue().getId();
        }

        long intMonth;
        if(this.getYuefValue() == null) {
            intMonth = (long)DateUtil.getMonth(new Date());
        } else {
            intMonth = this.getYuefValue().getId();
        }

        String StrMonth = "";
        if(intMonth < 10L) {
            StrMonth = "0" + intMonth;
        } else {
            StrMonth = "" + intMonth;
        }

        ResultSetList rsl = this.getExtGrid().getModifyResultSet(this.getChange().replaceAll("&nbsp;", ""));
        String zidm1 = "";
        String zidm2 = "";
        StringBuffer sb = new StringBuffer();
        sb.append("update yuezbb set ");

        while(rsl.next()) {
            switch(rsl.getInt("LANC")) {
                case 1:
                    zidm1 = "FADGRYTRML";
                    zidm2 = "RULMZBZML";
                    break;
                case 2:
                    zidm1 = "RULTRMPJFRL";
                    zidm2 = "";
                    break;
                case 3:
                    zidm1 = "FADYTRML";
                    zidm2 = "FADMZBML";
                    break;
                case 4:
                    zidm1 = "GONGRYTRML";
                    zidm2 = "GONGRMZBML";
                    break;
                case 5:
                    zidm1 = "FADGRYTRYL";
                    zidm2 = "RULYZBZML";
                    break;
                case 6:
                    zidm1 = "RULTRYPJFRL";
                    zidm2 = "";
                    break;
                case 7:
                    zidm1 = "FADYTRYL";
                    zidm2 = "FADYZBZML";
                    break;
                case 8:
                    zidm1 = "GONGRYTRYL";
                    zidm2 = "GONGRYZBZML";
                    break;
                case 9:
                    zidm1 = "FADGRYTRQL";
                    zidm2 = "RULQZBZML";
                    break;
                case 10:
                    zidm1 = "RULTRQPJFRL";
                    zidm2 = "";
                    break;
                case 11:
                    zidm1 = "FADYTRQL";
                    zidm2 = "FADQZBZML";
                    break;
                case 12:
                    zidm1 = "GONGRYTRQL";
                    zidm2 = "GONGRQZBZML";
                    break;
                case 13:
                    zidm1 = "";
                    zidm2 = "";
                    break;
                case 14:
                    zidm1 = "FADL";
                    zidm2 = "FADBZMH";
                    break;
                case 15:
                    zidm1 = "GONGDL";
                    zidm2 = "GONGDBZMH";
                    break;
                case 16:
                    zidm1 = "FADZHCGDL";
                    zidm2 = "";
                    break;
                case 17:
                    zidm1 = "GONGRCGDL";
                    zidm2 = "";
                    break;
                case 18:
                    zidm1 = "SHOUDL";
                    zidm2 = "GONGRBZMH";
                    break;
                case 19:
                    zidm1 = "GOURDL";
                    zidm2 = "";
                    break;
                case 20:
                    zidm1 = "GONGRL";
                    zidm2 = "RULTRMPJDJ";
                    break;
                case 21:
                    zidm1 = "SHOURL";
                    zidm2 = "QIZ_FADTRMDJ";
                    break;
                case 22:
                    zidm1 = "RANLCB_BHS";
                    zidm2 = "QIZ_GONGRTRMDJ";
                    break;
                case 23:
                    zidm1 = "FADMCB";
                    zidm2 = "RULTRYPJDJ";
                    break;
                case 24:
                    zidm1 = "FADYCB";
                    zidm2 = "QIZ_FADTRYDJ";
                    break;
                case 25:
                    zidm1 = "GONGRMCB";
                    zidm2 = "QIZ_GONGRTRYDJ";
                    break;
                case 26:
                    zidm1 = "GONGRYCB";
                    zidm2 = "RULTRQPJDJ";
                    break;
                case 27:
                    zidm1 = "FADRQCB";
                    zidm2 = "QIZ_FADTRQDJ";
                    break;
                case 28:
                    zidm1 = "GONGRRQCB";
                    zidm2 = "QIZ_GONGRTRQDJ";
                    break;
                case 29:
                    zidm1 = "GONGRCYDFTRLF";
                    zidm2 = "";
                    break;
                case 30:
                    zidm1 = "QIZ_RANM";
                    zidm2 = "FADBZMDJ";
                    break;
                case 31:
                    zidm1 = "QIZ_RANY";
                    zidm2 = "QIZ_MEIZBMDJ";
                    break;
                case 32:
                    zidm1 = "QIZ_RANQ";
                    zidm2 = "QIZ_YOUZBMDJ";
                    break;
                case 33:
                    zidm1 = "";
                    zidm2 = "QIZ_QIZBMDJ";
                    break;
                case 34:
                    zidm1 = "SHOUDDWBDCB";
                    zidm2 = "GONGRBZMDJ";
                    break;
                case 35:
                    zidm1 = "SHOUDDWGDCB";
                    zidm2 = "QIZ_MEIZBMDJ_GR";
                    break;
                case 36:
                    zidm1 = "SHOUDDJ";
                    zidm2 = "QIZ_YOUZBMDJ_GR";
                    break;
                case 37:
                    zidm1 = "SHOURDJ";
                    zidm2 = "QIZ_QIZBMDJ_GR";
                    break;
                case 38:
                    zidm1 = "SHOURDWCB";
                    zidm2 = "FADDWRLCB";
                    break;
                case 39:
                    zidm1 = "LIRZE";
                    zidm2 = "SHOUDDWRLCB_MEID";
                    break;
                case 40:
                    zidm1 = "";
                    zidm2 = "SHOUDDWRLCB_QID";
                    break;
                case 41:
                    zidm1 = "BENQRCMGJSL";
                    zidm2 = "";
                    break;
                case 42:
                    zidm1 = "BENQRCMGJZJE_BHS";
                    zidm2 = "";
                    break;
                case 43:
                    zidm1 = "QITFY";
                    zidm2 = "";
            }

            if(!zidm1.equals("")) {
                sb.append(zidm1).append("=").append(rsl.getDouble("zhi1")).append(",");
            }

            if(!zidm2.equals("")) {
                sb.append(zidm2).append("=").append(rsl.getDouble("zhi2")).append(",");
            }
        }

        long diancxxb_id = Long.parseLong(this.getTreeid());
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" where ").append("riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\')").append(" and");
        sb.append(" diancxxb_id=" + diancxxb_id + " and fenx=\'本月\'");
        con.getUpdate(sb.toString());
        boolean i = false;

        for(int var30 = 0; (long)var30 <= 12L - intMonth; ++var30) {
            boolean jzyf = false;
            String pdyf = "select yzb.id as id from yuezbb yzb where yzb.diancxxb_id=" + diancxxb_id + " and yzb.riq=to_date(\'" + intyear + "-" + (intMonth + (long)var30) + "-01\',\'yyyy-mm-dd\')";
            ResultSet pdyfsql = con.getResultSet(pdyf);

            try {
                if(!pdyfsql.next()) {
                    continue;
                }

                jzyf = true;
            } catch (SQLException var28) {
                var28.printStackTrace();
            } finally {
                con.Close();
            }

            if(jzyf) {
                String sqlljcx = "select y.diancxxb_id,\n--耗用天然煤\nsum(y.fadgrytrml) as fadgrytrml,\ndecode(sum(y.fadgrytrml),0,0,sum(y.fadgrytrml*y.rultrmpjfrl)/sum(y.fadgrytrml)) as rultrmpjfrl,\nsum(y.fadytrml) as fadytrml,sum(y.gongrytrml) as gongrytrml,\nsum(y.rulmzbzml) as rulmzbzml,sum(y.fadmzbml) as fadmzbzml,sum(y.gongrmzbml) as gongrmzbml,\n--耗用天然油\nsum(y.fadgrytryl) as fadgrytryl,\ndecode(sum(y.fadgrytryl),0,0,sum(y.fadgrytryl*y.rultrypjfrl)/sum(y.fadgrytryl)) as rulyrz,\nsum(y.fadytryl) as fadytry, sum(y.gongrytryl) as gongrytry,sum(y.rulyzbzml) as rulyzbzml,\nsum(y.fadyzbzml) as fadyzbzml,sum(y.gongryzbzml) as gongryzbzml,\n--耗用天然气\nsum(y.fadgrytrql) as fadgrytrql,\ndecode(sum(y.fadgrytrql),0,0,sum(y.fadgrytrql*y.rultrqpjfrl)/sum(y.fadgrytrql)) as rultrqfrl,\nsum(y.fadytrql) as fadytrql,sum(y.gongrytrql) as gongrytrql,sum(y.rulqzbzml) as rulqzbzml,\nsum(y.fadqzbzml) as fadqzbzml,sum(y.gongrqzbzml) as gongrqzbzml,\n--发电供电情况\nsum(y.fadl) as fadl,sum(y.gongdl) as gongdl,sum(y.fadzhcgdl) as fadzhcgdl,sum(y.gongrcgdl) as gongrcgdl, sum(y.shoudl) as shoudl,sum(y.gourdl) as gourdl,\nsum(gongrl) as gongrl,sum(y.shourl) as shourl,\n--煤耗情况\ndecode(sum(y.fadl),0,0,sum(y.fadmzbml+y.fadyzbzml+y.fadqzbzml)*100/sum(y.fadl)) as fadbzmh,\ndecode(sum(y.gongdl),0,0,sum(y.fadmzbml+y.fadyzbzml+y.fadqzbzml)*100/sum(y.fadl-y.gongdl)) as gongdbzmh,\ndecode(sum(y.gongrl),0,0,sum(y.gongrmzbml+y.gongryzbzml+y.gongrqzbzml)*1000/sum(y.gongrl)) as gongrbzmh,\n--燃料成本\nsum(y.ranlcb_bhs) as ranlcb_bhs,sum(y.fadmcb) as fadmcb,sum(y.fadycb) as fadycb,sum(y.gongrmcb) as gongrmcb,\nsum(y.gongrycb) as gongrycb,sum(y.fadrqcb) as fadrqcb,sum(y.gongrrqcb) as gongrrqcb,\n--供热厂用电分摊燃料费用\nsum(y.gongrcydftrlf) as gongrcydftrlf,sum(y.qiz_ranm) as qiz_ranm,sum(y.qiz_rany) as qiz_rany,sum(y.qiz_ranq) as qiz_ranq,\n-- 入炉煤平均单价\ndecode(sum(y.fadgrytrml),0,0,sum(y.fadmcb+y.gongrmcb)*10000/sum(y.fadgrytrml)) as rulmpjdj,\ndecode(sum(y.fadytrml),0,0,sum(y.fadmcb+y.qiz_ranm)*10000/sum(y.fadytrml)) as fadtrmdj,\ndecode(sum(y.gongrytrml),0,0,sum(y.gongrmcb-y.qiz_ranm)*10000/sum(y.gongrytrml)) as gongrtrmdj,\n--入炉油平均单价\ndecode(sum(y.fadgrytryl),0,0,sum(y.fadycb+y.gongrycb)*10000/sum(y.fadgrytryl)) as rulypjdj,\ndecode(sum(y.fadytryl),0,0,sum(y.fadycb+y.qiz_rany)*10000/sum(y.fadytryl)) as fadtrydj,\ndecode(sum(y.gongrytryl),0,0,sum(y.gongrycb-qiz_rany)*10000/sum(y.gongrytryl)) as gongrytrdj,\n--入炉天然气平均单价\ndecode(sum(y.fadgrytrql),0,0,sum(y.fadrqcb+y.gongrrqcb)*10000/sum(y.fadgrytrql)) as rultrqpjdj,\ndecode(sum(y.fadytrql),0,0,sum(y.fadrqcb+y.qiz_ranq)*10000/sum(y.fadytrql)) as  fadtrqdj,\ndecode(sum(y.gongrytrql),0,0,sum(y.gongrrqcb-y.qiz_ranq)*10000/sum(y.gongrytrql)) as gongrtrqdj,\n--售电售热情况\ndecode(sum(y.shoudl),0,0,sum(y.shouddwbdcb*y.shoudl)/sum(y.shoudl)) as shouddwbdcb,\ndecode(sum(y.shoudl),0,0,sum(y.shouddwgdcb*y.shoudl)/sum(y.shoudl)) as shouddwgdcb,\ndecode(sum(y.shoudl),0,0,sum(y.shouddj*y.shoudl)/sum(y.shoudl)) as shouddj,\ndecode(sum(y.shourl),0,0,sum(y.shourdj*y.shourl)/sum(y.shourl)) as shourdj,\ndecode(sum(y.shourl),0,0,sum(y.shourdwcb*y.shourl)/sum(y.shourl)) as shourdwcb,\nsum(y.lirze) as lirze,sum(y.benqrcmgjsl) as benqrcmgjsl, sum(y.benqrcmgjzje_bhs) as benqrcmgjzje_bhs,\n--发电煤折标煤单价\ndecode(sum(y.fadmzbml+y.fadyzbzml+y.fadqzbzml),0,0,sum(y.fadmcb+y.fadycb+y.fadrqcb+y.gongrcydftrlf)*10000/sum(y.fadmzbml+y.fadyzbzml+y.fadqzbzml)) as fadbmdj,\ndecode(sum(y.fadmzbml),0,0,sum(y.fadmcb+y.qiz_ranm)*10000/sum(y.fadmzbml)) as fadmbmdj,\ndecode(sum(y.fadyzbzml),0,0,sum(y.fadycb+y.qiz_rany)*10000/sum(y.fadyzbzml)) as fadybmdj,\ndecode(sum(y.fadqzbzml),0,0,sum(y.fadrqcb+y.qiz_ranq)*10000/sum(y.fadqzbzml)) as fadqbmdj,\n--供热煤折标煤单价\ndecode(sum(y.gongrmzbml+y.gongryzbzml+y.gongrqzbzml),0,0,sum(y.gongrmcb+y.gongrycb+y.gongrrqcb-y.gongrcydftrlf)*10000/sum(y.gongrmzbml+y.gongryzbzml+y.gongrqzbzml)) as gongrbzmdj,\ndecode(sum(y.gongrmzbml),0,0,sum(y.gongrmcb-y.qiz_ranm)*10000/sum(y.gongrmzbml)) as gongrmbmdj,\ndecode(sum(y.gongryzbzml),0,0,sum(y.gongrycb-y.qiz_rany)*10000/sum(y.gongryzbzml)) as gongrybmdj,\ndecode(sum(y.gongrqzbzml),0,0,sum(y.gongrrqcb-y.qiz_ranq)*10000/sum(y.gongrqzbzml)) as gongrqbmdj,\n--单位燃料成本\ndecode(sum(y.fadl-y.gongdl-y.gongrcgdl),0,0,sum(y.fadmcb+y.fadycb)*1000/sum(y.fadl-y.gongdl-y.gongrcgdl)) as faddwrlcb,\ndecode(sum(y.shoudl-y.gourdl),0,0,sum(y.fadmcb+y.fadycb)*1000/sum(y.shoudl-y.gourdl)) as shouddwrlcb_meid,\ndecode(sum(y.shoudl-y.gourdl),0,0,sum(y.fadrqcb)*1000/sum(y.shoudl-y.gourdl)) as shouddwrlcb_qid, \nsum(y.qitfy) as qitfy \nfrom yuezbb y\n where y.diancxxb_id = " + diancxxb_id + "\n" + "   and y.riq >= getYearFirstDate(to_date(\'" + intyear + "-" + (intMonth + (long)var30) + "-01\', \'yyyy-mm-dd\'))\n" + "   and y.riq <= to_date(\'" + intyear + "-" + (intMonth + (long)var30) + "-01\', \'yyyy-mm-dd\')\n" + "   and y.fenx=\'本月\'\n" + " group by (y.diancxxb_id)";
                ResultSetList rsllj = con.getResultSetList(sqlljcx);
                StringBuffer sqllj = new StringBuffer("begin \n");

                while(rsllj.next()) {
                    String shzt = "select zhuangt from yuezbb z where z.diancxxb_id=" + diancxxb_id + " and z.riq=to_date(\'" + intyear + "-" + (intMonth + (long)var30) + "-01\',\'yyyy-mm-dd\') and z.fenx=\'累计\'";
                    ResultSetList shenhzt = con.getResultSetList(shzt);

                    long zhuangt;
                    for(zhuangt = 0L; shenhzt.next(); zhuangt = shenhzt.getLong("zhuangt")) {
                        ;
                    }

                    sqllj.append("delete from yuezbb where diancxxb_id =" + diancxxb_id + " " + " and riq=to_date(\'" + intyear + "-" + (intMonth + (long)var30) + "-01\',\'yyyy-mm-dd\') " + " and fenx=\'累计\'").append(";\n");
                    long yuezbb_zdt_id = 0L;
                    yuezbb_zdt_id = Long.parseLong(MainGlobal.getNewID(((Visit)this.getPage().getVisit()).getDiancxxb_id()));
                    sqllj.append("insert into yuezbb(ID,DIANCXXB_ID,RIQ,FENX,FADGRYTRML,RULMZBZML,RULTRMPJFRL,FADYTRML,GONGRYTRML,FADMZBML,GONGRMZBML,FADGRYTRYL,RULTRYPJFRL,FADYTRYL,GONGRYTRYL,RULYZBZML,FADYZBZML,GONGRYZBZML,FADGRYTRQL,RULTRQPJFRL,FADYTRQL,GONGRYTRQL,RULQZBZML,FADQZBZML,GONGRQZBZML,FADL,GONGDL,FADZHCGDL,GONGRCGDL,SHOUDL,GOURDL,GONGRL,SHOURL,FADBZMH,GONGDBZMH,GONGRBZMH,RANLCB_BHS,FADMCB,FADYCB,GONGRMCB,GONGRYCB,FADRQCB,GONGRRQCB,GONGRCYDFTRLF,QIZ_RANM,QIZ_RANY,QIZ_RANQ,SHOUDDWBDCB,SHOUDDWGDCB,SHOUDDJ,SHOURDJ,SHOURDWCB,LIRZE,BENQRCMGJSL,BENQRCMGJZJE_BHS, RULTRMPJDJ,QIZ_FADTRMDJ,QIZ_GONGRTRMDJ,RULTRYPJDJ,QIZ_FADTRYDJ,QIZ_GONGRTRYDJ,RULTRQPJDJ,QIZ_FADTRQDJ,QIZ_GONGRTRQDJ,FADBZMDJ,QIZ_MEIZBMDJ,QIZ_YOUZBMDJ,QIZ_QIZBMDJ,GONGRBZMDJ,QIZ_MEIZBMDJ_GR,QIZ_YOUZBMDJ_GR,QIZ_QIZBMDJ_GR,FADDWRLCB,SHOUDDWRLCB_MEID,SHOUDDWRLCB_QID,ZHUANGT,qitfy) values(\n" + yuezbb_zdt_id + "," + diancxxb_id + ",to_date(\'" + intyear + "-" + (intMonth + (long)var30) + "-01\',\'yyyy-mm-dd\')," + "\'累计\'," + rsllj.getDouble("fadgrytrml") + "," + rsllj.getDouble("rulmzbzml") + "," + rsllj.getDouble("rultrmpjfrl") + "," + rsllj.getDouble("fadytrml") + "," + rsllj.getDouble("gongrytrml") + "," + rsllj.getDouble("fadmzbzml") + "," + rsllj.getDouble("gongrmzbml") + "," + rsllj.getDouble("fadgrytryl") + "," + rsllj.getDouble("rulyrz") + "," + rsllj.getDouble("fadytry") + "," + rsllj.getDouble("gongrytry") + "," + rsllj.getDouble("rulyzbzml") + "," + rsllj.getDouble("fadyzbzml") + "," + rsllj.getDouble("gongryzbzml") + "," + rsllj.getDouble("fadgrytrql") + "," + rsllj.getDouble("rultrqfrl") + "," + rsllj.getDouble("fadytrql") + "," + rsllj.getDouble("gongrytrql") + "," + rsllj.getDouble("rulqzbzml") + "," + rsllj.getDouble("fadqzbzml") + "," + rsllj.getDouble("gongrqzbzml") + "," + rsllj.getDouble("fadl") + "," + rsllj.getDouble("gongdl") + "," + rsllj.getDouble("fadzhcgdl") + "," + rsllj.getDouble("gongrcgdl") + "," + rsllj.getDouble("shoudl") + "," + rsllj.getDouble("gourdl") + "," + rsllj.getDouble("gongrl") + "," + rsllj.getDouble("shourl") + "," + rsllj.getDouble("fadbzmh") + "," + rsllj.getDouble("gongdbzmh") + "," + rsllj.getDouble("gongrbzmh") + "," + rsllj.getDouble("ranlcb_bhs") + "," + rsllj.getDouble("fadmcb") + "," + rsllj.getDouble("fadycb") + "," + rsllj.getDouble("gongrmcb") + "," + rsllj.getDouble("gongrycb") + "," + rsllj.getDouble("fadrqcb") + "," + rsllj.getDouble("gongrrqcb") + "," + rsllj.getDouble("gongrcydftrlf") + "," + rsllj.getDouble("qiz_ranm") + "," + rsllj.getDouble("qiz_rany") + "," + rsllj.getDouble("qiz_ranq") + "," + rsllj.getDouble("shouddwbdcb") + "," + rsllj.getDouble("shouddwgdcb") + "," + rsllj.getDouble("shouddj") + "," + rsllj.getDouble("shourdj") + "," + rsllj.getDouble("shourdwcb") + "," + rsllj.getDouble("lirze") + "," + rsllj.getDouble("benqrcmgjsl") + "," + rsllj.getDouble("benqrcmgjzje_bhs") + "," + rsllj.getDouble("rulmpjdj") + "," + rsllj.getDouble("fadtrmdj") + "," + rsllj.getDouble("gongrtrmdj") + "," + rsllj.getDouble("rulypjdj") + "," + rsllj.getDouble("fadtrydj") + "," + rsllj.getDouble("gongrytrdj") + "," + rsllj.getDouble("rultrqpjdj") + "," + rsllj.getDouble("fadtrqdj") + "," + rsllj.getDouble("gongrtrqdj") + "," + rsllj.getDouble("fadbmdj") + "," + rsllj.getDouble("fadmbmdj") + "," + rsllj.getDouble("fadybmdj") + "," + rsllj.getDouble("fadqbmdj") + "," + rsllj.getDouble("gongrbzmdj") + "," + rsllj.getDouble("gongrmbmdj") + "," + rsllj.getDouble("gongrybmdj") + "," + rsllj.getDouble("gongrqbmdj") + "," + rsllj.getDouble("faddwrlcb") + "," + rsllj.getDouble("shouddwrlcb_meid") + "," + rsllj.getDouble("shouddwrlcb_qid") + "," + zhuangt + "," + rsllj.getDouble("qitfy") + ");\n");
                }

                sqllj.append("end;");
                con.getInsert(sqllj.toString());
            }
        }

        con.Close();
        this.setMsg("保存成功!");
    }


    private boolean _SaveChick = false;

    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }
    private boolean _Refreshclick = false;

    public void RefreshButton(IRequestCycle cycle) {
        _Refreshclick = true;
    }
    private boolean _InsertButton = false;
    public void InsertButton(IRequestCycle cycle) {
        _InsertButton = true;
    }
    private boolean _DeleteButton = false;
    public void DeleteButton(IRequestCycle cycle) {
        _DeleteButton = true;
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
        if(_InsertButton){
            _InsertButton=false;
            Insert();
            getSelectData();
        }
        if(_DeleteButton){
            _DeleteButton = false;
            Delete();
            getSelectData();
        }

    }

    public void Delete(){
        Visit visit = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
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
        //删除本月和累计的数据
        long id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
        long diancxxb_id=Long.parseLong(this.getTreeid());
        String DeleteStr="delete yuezbb y where y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') " +
                "and y.diancxxb_id="+diancxxb_id+"";
        con.getDelete(DeleteStr);
        con.Close();
    }
    public void Insert(){
        Visit visit = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
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

        long id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
        long diancxxb_id=Long.parseLong(this.getTreeid());
        String isHaveZhi="select * from yuezbb y where y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') " +
                "and y.diancxxb_id="+diancxxb_id+" and y.fenx='本月'";
        ResultSetList rssy=con.getResultSetList(isHaveZhi);
        //插入时判断是否有值
        if(rssy.next()){
            con.Close();
            return;
        }else{

            con.getInsert("insert into yuezbb(ID,DIANCXXB_ID,RIQ,FENX,FADGRYTRML,RULMZBZML,RULTRMPJFRL,FADYTRML,GONGRYTRML,FADMZBML,GONGRMZBML,FADGRYTRYL,RULTRYPJFRL,FADYTRYL," +
                    "GONGRYTRYL,RULYZBZML,FADYZBZML,GONGRYZBZML,FADGRYTRQL,RULTRQPJFRL,FADYTRQL,GONGRYTRQL,RULQZBZML,FADQZBZML,GONGRQZBZML,FADL," +
                    "GONGDL,SHOUDL,GOURDL,GONGRL,SHOURL,FADBZMH,GONGDBZMH,GONGRBZMH,RANLCB_BHS,FADMCB,FADYCB,GONGRMCB,GONGRYCB,FADRQCB,GONGRRQCB," +
                    "GONGRCYDFTRLF,QIZ_RANM,QIZ_RANY,QIZ_RANQ,SHOUDDWBDCB,SHOUDDWGDCB,SHOUDDJ,SHOURDJ,SHOURDWCB,LIRZE,BENQRCMGJSL,BENQRCMGJZJE_BHS, "+
                    "RULTRMPJDJ,QIZ_FADTRMDJ,QIZ_GONGRTRMDJ,RULTRYPJDJ,QIZ_FADTRYDJ,QIZ_GONGRTRQDJ,QIZ_GONGRTRYDJ,RULTRQPJDJ,QIZ_FADTRQDJ,FADBZMDJ,"+
                    "QIZ_MEIZBMDJ,QIZ_YOUZBMDJ,QIZ_QIZBMDJ,GONGRBZMDJ,QIZ_MEIZBMDJ_GR,QIZ_YOUZBMDJ_GR,QIZ_QIZBMDJ_GR,FADDWRLCB,SHOUDDWRLCB_MEID,SHOUDDWRLCB_QID,ZHUANGT,GONGRCGDL,FADZHCGDL,qitfy) values(\n"
                    +id+","+diancxxb_id+",to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd'),'本月',0,0,0,0,0,0,0,0" +
                    ",0,0,0,0,0,0,0,0,0,0,0" +
                    ",0,0,0,0,0,0,0,0,0,0,0" +
                    ",0,0,0,0,0,0,0,0,0,0,0" +
                    ",0,0,0,0,0,0,0,0,0,0,0" +
                    ",0,0,0,0,0,0,0" +
                    ",0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
        }

        con.Close();
    }
    public void getSelectData() {
        Visit visit = (Visit)this.getPage().getVisit();
        JDBCcon con = new JDBCcon();
        String zhuangt = "";
        if(visit.isShifsh()) {
            if(visit.getRenyjb() == 3) {
                zhuangt = "";
            } else if(visit.getRenyjb() == 2) {
                zhuangt = " and (y.zhuangt=1 or y.zhuangt=2)";
            } else if(visit.getRenyjb() == 1) {
                zhuangt = " and y.zhuangt=2";
            }
        }

        long intyear;
        if(this.getNianfValue() == null) {
            intyear = (long)DateUtil.getYear(new Date());
        } else {
            intyear = this.getNianfValue().getId();
        }

        long intMonth;
        if(this.getYuefValue() == null) {
            intMonth = (long)DateUtil.getMonth(new Date());
        } else {
            intMonth = this.getYuefValue().getId();
        }

        String StrMonth = "";
        if(intMonth < 10L) {
            StrMonth = "0" + intMonth;
        } else {
            StrMonth = "" + intMonth;
        }

        String str = "";
        int treejib = this.getDiancTreeJib();
        if(treejib == 1) {
            str = "";
        } else if(treejib == 2) {
            str = "and (dc.id = " + this.getTreeid() + " or dc.fuid = " + this.getTreeid() + ")";
        } else if(treejib == 3) {
            (new StringBuffer("and dc.id = ")).append(this.getTreeid()).toString();
        }

        String chaxun = "select tj.lanc,tj.mingc1,tj.danw,tj.zhi1,tj.mingc2,tj.danw2,tj.zhi2,tj.zhuangt\nfrom (\n--耗用煤\nselect 1 as lanc,\'发电供热用天然煤量\' as mingc1,\'吨\' as danw,y.FADGRYTRML as zhi1 ,\'入炉煤折标准煤量\'as mingc2,\'吨\' as danw2,y.RULMZBZML as zhi2,y.zhuangt\n  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 2 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;入炉天然煤平均发热量\' as mingc1,\'千焦/千克\' as danw,y.RULTRMPJFRL as zhi1,\'\'as mingc2,\'\'as danw2, null as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 3 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发电用天然煤量\' as mingc1,\'吨\' as danw,y.FADYTRML as zhi1,\'发电煤折标准煤量\'as mingc2,\'吨\' as danw2, y.FADMZBML as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 4 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;供热用天然煤量\' as mingc1,\'吨\' as danw,y.GONGRYTRML as zhi1,\'供热煤折标准煤量\'as mingc2,\'吨\' as danw2, y.GONGRMZBML as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "---耗用油\n" + "select 5 as lanc,\'发电供热用天然油量\' as mingc1,\'吨\' as danw,y.FADGRYTRYL as zhi1,\'入炉油折标准煤量\'as mingc2,\'吨\' as danw2, y.RULYZBZML as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 6 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;入炉天然油平均发热量\' as mingc1,\'千焦/千克\' as danw,y.RULTRYPJFRL as zhi1,\'\'as mingc2,\'\'as danw2, null as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 7 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发电用天然油量\' as mingc1,\'吨\' as danw,y.FADYTRYL as zhi1,\'发电油折标准煤量\'as mingc2,\'吨\' as danw2, y.FADYZBZML as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 8 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;供热用天然油量\' as mingc1,\'吨\' as danw,y.GONGRYTRYL as zhi1,\'供热油折标准煤量\'as mingc2,\'吨\' as danw2, y.GONGRYZBZML as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "----天然气\n" + "select 9 as lanc,\'发电供热用天然气量\' as mingc1,\'吨\' as danw,y.FADGRYTRQL as zhi1,\'入炉气折标准煤量\'as mingc2,\'吨\' as danw2, y.RULQZBZML as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 10 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;入炉天然气平均发热量\' as mingc1,\'千焦/千克\' as danw,y.RULTRQPJFRL as zhi1,\'\'as mingc2,\'\'as danw2, null as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 11 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发电用天然气量\' as mingc1,\'吨\' as danw,y.FADYTRQL as zhi1,\'发电气折标准煤量\'as mingc2,\'吨\' as danw2, y.FADQZBZML as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 12 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;供热用天然气量\' as mingc1,\'吨\' as danw,y.GONGRYTRQL as zhi1,\'供热气折标准煤量\'as mingc2,\'吨\' as danw2, y.GONGRQZBZML as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "----发电供热情况\n" + "select 13 as lanc,\'发电供热售电情况\' as mingc1,\'\' as danw,null as zhi1,\'\'as mingc2,\'\'as danw2, null as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 14 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发电量\' as mingc1,\'万千瓦时\' as danw,y.FADL as zhi1,\'发电标准煤耗\'as mingc2,\'克/千瓦时\' as danw2, y.FADBZMH as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 15 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发电生产厂用电量\' as mingc1,\'万千瓦时\' as danw,y.gongdl as zhi1,\'供电标准煤耗\'as mingc2,\'克/千瓦时\' as danw2, y.GONGDBZMH as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 16 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发电综合厂用电量\' as mingc1,\'万千瓦时\' as danw,y.fadzhcgdl as zhi1,\'\'as mingc2,\'\' as danw2, null as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 17 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;供热厂用电量\' as mingc1,\'万千瓦时\' as danw,y.gongrcgdl as zhi1,\'\'as mingc2,\'\' as danw2, null as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 18 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;售电量\' as mingc1,\'万千瓦时\' as danw,y.SHOUDL as zhi1,\'供热标准煤耗\'as mingc2,\'千克/吉焦\' as danw2, y.GONGRBZMH as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 19 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;购入电量\' as mingc1,\'万千瓦时\' as danw,y.GOURDL as zhi1,\'\'as mingc2,\'\'as danw2, null as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 20 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;供热量\' as mingc1,\'吉焦\' as danw,y.GONGRL as zhi1,\'入炉天然煤平均单价\'as mingc2,\'元/吨\' as danw2, y.RULTRMPJDJ as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 21 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;售热量\' as mingc1,\'吉焦\' as danw,y.SHOURL as zhi1,\'其中：发电天然煤单价\'as mingc2,\'元/吨\' as danw2, y.QIZ_FADTRMDJ as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "---燃料成本\n" + "select 22 as lanc,\'燃料成本(不含税)\' as mingc1,\'万元\' as danw,y.RANLCB_BHS as zhi1,\'其中：供热天然煤单价\'as mingc2,\'元/吨\' as danw2, y.QIZ_GONGRTRMDJ as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 23 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发电燃煤成本\' as mingc1,\'万元\' as danw,y.FADMCB as zhi1,\'入炉天然油平均单价\'as mingc2,\'元/吨\' as danw2, y.RULTRYPJDJ as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 24 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发电燃油成本\' as mingc1,\'万元\' as danw,y.FADYCB as zhi1,\'其中：发电天然油单价\'as mingc2,\'元/吨\' as danw2, y.QIZ_FADTRYDJ as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 25 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;供热燃煤成本\' as mingc1,\'万元\' as danw,y.GONGRMCB as zhi1,\'其中：供热天然油单价\'as mingc2,\'元/吨\' as danw2, y.QIZ_GONGRTRYDJ as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 26 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;供热燃油成本\' as mingc1,\'万元\' as danw,y.GONGRYCB as zhi1,\'入炉天然气平均单价\'as mingc2,\'元/吨\' as danw2, y.RULTRQPJDJ as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 27 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发电燃气成本\' as mingc1,\'万元\' as danw,y.FADRQCB as zhi1,\'其中：发电天然气单价\'as mingc2,\'元/吨\' as danw2, y.QIZ_FADTRQDJ as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 28 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;供热燃气成本\' as mingc1,\'万元\' as danw,y.GONGRRQCB as zhi1,\'其中：供热天然气单价\'as mingc2,\'元/吨\' as danw2, y.QIZ_GONGRTRQDJ as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "------供热厂用电分摊燃料费\n" + "select 29 as lanc,\'供热厂用电分摊燃料费\' as mingc1,\'万元\' as danw,y.GONGRCYDFTRLF as zhi1,\'\'as mingc2,\'\' as danw2, null as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 30 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;其中：燃煤\' as mingc1,\'万元\' as danw,y.QIZ_RANM as zhi1,\'发电标准煤单价\'as mingc2,\'元/吨\' as danw2, y.FADBZMDJ as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 31 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;其中：燃油\' as mingc1,\'万元\' as danw,y.QIZ_RANY as zhi1,\'其中:煤折标煤单价\'as mingc2,\'元/吨\' as danw2, y.QIZ_MEIZBMDJ as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 32 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;其中：燃气\' as mingc1,\'万元\' as danw,y.QIZ_RANQ as zhi1,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;油折标煤单价\'as mingc2,\'元/吨\' as danw2, y.QIZ_YOUZBMDJ as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "--------售电情况\n" + "select 33 as lanc,\'售电情况\' as mingc1,\'\' as danw,null as zhi1,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;气折标煤单价\'as mingc2,\'元/吨\' as danw2, y.QIZ_QIZBMDJ as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 34 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;售电单位变动成本\' as mingc1,\'元/千千瓦时\' as danw,y.SHOUDDWBDCB as zhi1,\'供热标准煤单价\'as mingc2,\'元/吨\' as danw2, y.GONGRBZMDJ as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 35 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;售电单位固定成本\' as mingc1,\'元/千千瓦时\' as danw,y.SHOUDDWGDCB as zhi1,\' 其中:煤折标煤单价\'as mingc2,\'元/吨\' as danw2, y.QIZ_MEIZBMDJ_GR as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 36 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;售电单价\' as mingc1,\'元/千千瓦时\' as danw,y.SHOUDDJ as zhi1,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;油折标煤单价\'as mingc2,\'元/吨\' as danw2, y.QIZ_YOUZBMDJ_GR as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 37 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;售热单价\' as mingc1,\'元/吉焦\' as danw,y.SHOURDJ as zhi1,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;气折标煤单价\'as mingc2,\'元/吨\' as danw2, y.QIZ_QIZBMDJ_GR as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 38 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;售热单位成本\' as mingc1,\'元/吉焦\' as danw,y.SHOURDWCB as zhi1,\'供电单位燃料成本\'as mingc2,\'元/千千瓦时\' as danw2, y.FADDWRLCB as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 39 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;利润总额\' as mingc1,\'万元\' as danw,y.LIRZE as zhi1,\'售电单位燃料成本(煤电)\'as mingc2,\'元/千千瓦时\' as danw2, y.SHOUDDWRLCB_MEID as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "--估价情况\n" + "select 40 as lanc,\'估价情况\' as mingc1,\'\' as danw,null as zhi1,\'售电单位燃料成本(气电)\'as mingc2,\'元/千千瓦时\' as danw2, y.SHOUDDWRLCB_QID as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 41 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本期入厂煤估价数量\' as mingc1,\'吨\' as danw,y.BENQRCMGJSL as zhi1,\'\'as mingc2, \'\' as danw2,null as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 42 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本期入厂煤估价总金额(不含税)\' as mingc1,\'万元\' as danw,y.BENQRCMGJZJE_BHS as zhi1,\'\'as mingc2,\'\' as danw2, null as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + "union\n" + "select 43 as lanc,\'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;其它费用\' as mingc1,\'万元\' as danw,y.qitfy as zhi1,\'\'as mingc2,\'\' as danw2, null as zhi2,y.zhuangt\n" + "  from yuezbb y where y.fenx=\'本月\' and y.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\') and y.diancxxb_id=" + this.getTreeid() + zhuangt + "\n" + ") tj order by tj.lanc";
        ResultSetList rsl = con.getResultSetList(chaxun);
        boolean yincan = false;

        while(true) {
            while(rsl.next()) {
                if(visit.getRenyjb() == 3) {
                    if(rsl.getLong("zhuangt") == 0L) {
                        yincan = false;
                    } else {
                        yincan = true;
                    }
                } else if(visit.getRenyjb() == 2 || visit.getRenyjb() == 1) {
                    yincan = true;
                }
            }

            rsl.beforefirst();
            ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
            egu.setTableName("yuezbb");
            egu.getColumn("lanc").setCenterHeader("栏次");
            egu.getColumn("mingc1").setCenterHeader("项目");
            egu.getColumn("danw").setCenterHeader("单位");
            egu.getColumn("zhi1").setCenterHeader("完成值");
            egu.getColumn("mingc2").setCenterHeader("折算项目");
            egu.getColumn("danw2").setCenterHeader("折算单位");
            egu.getColumn("zhi2").setCenterHeader("值");
            egu.getColumn("lanc").setEditor((Field)null);
            egu.getColumn("mingc1").setEditor((Field)null);
            egu.getColumn("danw").setEditor((Field)null);
            egu.getColumn("mingc2").setEditor((Field)null);
            egu.getColumn("zhi2").setEditor((Field)null);
            egu.getColumn("danw2").setEditor((Field)null);
            egu.getColumn("zhuangt").setCenterHeader("状态");
            egu.getColumn("zhuangt").setHidden(true);
            egu.getColumn("zhuangt").setEditor((Field)null);
            egu.getColumn("lanc").setRenderer("function(value,metadata){metadata.css=\'tdTextext\'; return value;}");
            egu.getColumn("mingc1").setRenderer("function(value,metadata){metadata.css=\'tdTextext1\'; return value;}");
            egu.getColumn("danw").setRenderer("function(value,metadata){metadata.css=\'tdTextext\'; return value;}");
            egu.getColumn("zhi1").setRenderer("function(value,metadata,rec,rowIndex){if(rowIndex==0||rowIndex==4||rowIndex==8||rowIndex==12||rowIndex==21||rowIndex==28||rowIndex==32||rowIndex==39){metadata.css=\'tdTextext3\';} return value;}");
            egu.getColumn("mingc2").setRenderer("function(value,metadata){metadata.css=\'tdTextext\'; return value;}");
            egu.getColumn("danw2").setRenderer("function(value,metadata){metadata.css=\'tdTextext2\'; return value;}");
            egu.getColumn("zhi2").setRenderer("function(value,metadata){metadata.css=\'tdTextext\'; return value;}");
            egu.setDefaultsortable(false);
            egu.getColumn("lanc").setWidth(70);
            egu.getColumn("lanc").setAlign("right");
            egu.getColumn("mingc1").setWidth(180);
            egu.getColumn("danw").setWidth(80);
            egu.getColumn("danw2").setWidth(80);
            egu.getColumn("zhi1").setWidth(90);
            egu.getColumn("mingc2").setWidth(150);
            egu.getColumn("mingc2").setAlign("LEFT");
            egu.getColumn("zhi2").setWidth(80);
            egu.setGridType(1);
            egu.addPaging(100);
            egu.setWidth(1000);
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
            ExtTreeUtil etu = new ExtTreeUtil("diancTree", 10, ((Visit)this.getPage().getVisit()).getDiancxxb_id(), this.getTreeid());
            this.setTree(etu);
            egu.addTbarTreeBtn("diancTree");
            egu.addOtherScript("NIANF.on(\'select\',function(){document.forms[0].submit();});YUEF.on(\'select\',function(){document.forms[0].submit();});");
            egu.addTbarText("-");
            StringBuffer rsb = new StringBuffer();
            rsb.append("function (){").append(MainGlobal.getExtMessageBox("\'正在刷新\'+Ext.getDom(\'NIANF\').value+\'年\'+Ext.getDom(\'YUEF\').value+\'月的数据,请稍候！\'", true)).append("document.getElementById(\'RefreshButton\').click();}");
            GridButton gbr = new GridButton("刷新", rsb.toString());
            gbr.setIcon("imgs/btnicon/refurbish.gif");
            egu.addTbarBtn(gbr);
            StringBuffer sb;
            if(visit.getRenyjb() == 3 && !yincan) {
                sb = new StringBuffer();
                sb.append("function (){").append("document.getElementById(\'InsertButton\').click();}");
                GridButton gbIns = new GridButton("添加", sb.toString());
                gbIns.setIcon("imgs/btnicon/insert.gif");
                egu.addTbarBtn(gbIns);
                String ss = "Ext.MessageBox.confirm(\'警告\', \'你确定要删除本月所有的数据吗？\', function(btn) { if(btn==\'yes\'){document.getElementById(\'DeleteButton\').click();}})";
                StringBuffer del = new StringBuffer();
                del.append("function (){").append(ss + "}");
                GridButton gbDel = new GridButton("删除", del.toString());
                gbDel.setIcon("imgs/btnicon/delete.gif");
                egu.addTbarBtn(gbDel);
                egu.addToolbarButton(11, "SaveButton", MainGlobal.getExtMessageShow("正在保存数据,请稍后...", "保存中...", 200));
            }

            egu.addTbarText("->");
            sb = new StringBuffer();
            sb.append("gridDiv_grid.on(\'beforeedit\',function(e){");
            sb.append("if(e.record.get(\'LANC\')==\'1\'||e.record.get(\'LANC\')==\'5\'||e.record.get(\'LANC\')==\'9\'||e.record.get(\'LANC\')==\'13\'||e.record.get(\'LANC\')==\'22\'||e.record.get(\'LANC\')==\'29\'||e.record.get(\'LANC\')==\'33\'||e.record.get(\'LANC\')==\'40\'){e.cancel=true;}");
            sb.append("});");
            sb.append("gridDiv_grid.on(\'afteredit\',function(e){");
            sb.append("if(e.row==1 || e.row==2||e.row==3){").append("rec = gridDiv_ds.getAt(0);").append("rulmzr=gridDiv_ds.getAt(1);").append("recfadhm= gridDiv_ds.getAt(2);").append("recgongrhm= gridDiv_ds.getAt(3);").append("fadl= gridDiv_ds.getAt(13);").append("recfadhy= gridDiv_ds.getAt(6);").append("recfadhq= gridDiv_ds.getAt(10);").append("fadsccgdl=gridDiv_ds.getAt(14);").append("gongrl=gridDiv_ds.getAt(19);").append("shourl=gridDiv_ds.getAt(20);").append("ranlcb_bhs=gridDiv_ds.getAt(21);").append("fadmcb=gridDiv_ds.getAt(22);").append("fadycb=gridDiv_ds.getAt(23);").append("gongrmcb=gridDiv_ds.getAt(24);").append("fadqcb=gridDiv_ds.getAt(26);").append("gongrcydftrlf=gridDiv_ds.getAt(28);").append("qiz_rm=gridDiv_ds.getAt(29);").append("qiz_ry= gridDiv_ds.getAt(30);").append("var cb=eval(fadmcb.get(\'ZHI1\')||0)+eval(fadycb.get(\'ZHI1\')||0)+eval(fadqcb.get(\'ZHI1\')||0)+eval(gongrcydftrlf.get(\'ZHI1\')||0);").append("var cb1=eval(fadmcb.get(\'ZHI1\')||0)+eval(qiz_rm.get(\'ZHI1\')||0);").append("var cb2=eval(fadmcb.get(\'ZHI1\')||0)+eval(gongrmcb.get(\'ZHI1\')||0);").append("if(e.column==4){").append("recfadhm.set(\'ZHI1\', Round(eval(recfadhm.get(\'ZHI1\')||0),2) );").append("recgongrhm.set(\'ZHI1\', Round(eval(recgongrhm.get(\'ZHI1\')||0),2) );").append("rec.set(\'ZHI1\', Round(eval(recfadhm.get(\'ZHI1\')||0) + eval(recgongrhm.get(\'ZHI1\')||0),2));").append("recfadhm.set(\'ZHI2\', Round((eval(recfadhm.get(\'ZHI1\')||0) * eval(rulmzr.get(\'ZHI1\')||0)/29271),0));").append("recgongrhm.set(\'ZHI2\', Round((eval(recgongrhm.get(\'ZHI1\')||0) * eval(rulmzr.get(\'ZHI1\')||0)/29271),0));").append("rec.set(\'ZHI2\', Round(eval(recfadhm.get(\'ZHI2\')||0) + eval(recgongrhm.get(\'ZHI2\')||0),2));").append("if(eval(fadl.get(\'ZHI1\')||0)!=0){").append("fadl .set(\'ZHI2\', Round(((eval(recfadhm.get(\'ZHI2\')||0)+eval(recfadhy.get(\'ZHI2\')||0)+eval(recfadhq.get(\'ZHI2\')||0)) / eval(fadl.get(\'ZHI1\')||0)*100),1));").append("fadsccgdl .set(\'ZHI2\', Round(((eval(recfadhm.get(\'ZHI2\')||0)+eval(recfadhy.get(\'ZHI2\')||0)+eval(recfadhq.get(\'ZHI2\')||0)) /(eval(fadl.get(\'ZHI1\')||0)- eval(fadsccgdl.get(\'ZHI1\')||0))*100),1));").append("}").append("if(cb!=0){").append("qiz_rm.set(\'ZHI2\', Round(((eval(fadmcb.get(\'ZHI1\')||0)+eval(fadycb.get(\'ZHI1\')||0)+eval(fadqcb.get(\'ZHI1\')||0)+eval(gongrcydftrlf.get(\'ZHI1\')||0))*10000 /(eval(recfadhm.get(\'ZHI2\')||0)+eval(recfadhy.get(\'ZHI2\')||0)+eval(recfadhq.get(\'ZHI2\')||0))),2));").append("}").append("if(cb1!=0){").append(" qiz_ry.set(\'ZHI2\', Round(((eval(fadmcb.get(\'ZHI1\')||0)+eval(qiz_rm.get(\'ZHI1\')||0))*10000 /eval(recfadhm.get(\'ZHI2\')||0)),2));").append("}").append("if(cb2!=0){").append("gongrl.set(\'ZHI2\', Round(((eval(fadmcb.get(\'ZHI1\')||0)+eval(gongrmcb.get(\'ZHI1\')||0))*10000 /(eval(recfadhm.get(\'ZHI1\')||0)+eval(recgongrhm.get(\'ZHI1\')||0))),2));").append("}").append("if(eval(fadmcb.get(\'ZHI1\')||0)!=0){").append("shourl.set(\'ZHI2\', Round(((eval(fadmcb.get(\'ZHI1\')||0)+eval(qiz_rm.get(\'ZHI1\')||0))*10000 /eval(recfadhm.get(\'ZHI1\')||0)),2));").append("}").append("if(eval(gongrmcb.get(\'ZHI1\')||0)!=0){").append("ranlcb_bhs.set(\'ZHI2\', Round(((eval(gongrmcb.get(\'ZHI1\')||0)-eval(qiz_rm.get(\'ZHI1\')||0))*10000 /eval(recgongrhm.get(\'ZHI1\')||0)),2));").append("}").append("}else{").append("rec.set(\'ZHI2\', Round(eval(recfadhm.get(\'ZHI2\')||0) + eval(recgongrhm.get(\'ZHI2\')||0),2));").append("}").append("}");
            sb.append("if(e.row==5||e.row==6||e.row==7){").append("haoytry = gridDiv_ds.getAt(4);").append("rulyrz=gridDiv_ds.getAt(5);").append("fadhy=gridDiv_ds.getAt(6);").append("gongrhy=gridDiv_ds.getAt(7);").append("if(e.column==4){").append("fadhy.set(\'ZHI1\',Round(eval(fadhy.get(\'ZHI1\')||0),2));").append("gongrhy.set(\'ZHI1\',Round(eval(gongrhy.get(\'ZHI1\')||0),2));").append("haoytry .set(\'ZHI1\', Round(eval(fadhy.get(\'ZHI1\')||0) + eval(gongrhy.get(\'ZHI1\')||0),2));").append("fadhy.set(\'ZHI2\', Round((eval(fadhy.get(\'ZHI1\')||0) * eval(rulyrz.get(\'ZHI1\')||0)/29271),0));").append("gongrhy.set(\'ZHI2\', Round((eval(gongrhy.get(\'ZHI1\')||0) * eval(rulyrz.get(\'ZHI1\')||0)/29271),0));").append("haoytry .set(\'ZHI2\',  Round(eval(fadhy.get(\'ZHI2\')||0) + eval(gongrhy.get(\'ZHI2\')||0),2));").append("}else{").append("haoytry .set(\'ZHI2\',  Round(eval(fadhy.get(\'ZHI2\')||0) + eval(gongrhy.get(\'ZHI2\')||0),2));").append("}").append("}");
            sb.append("if(e.row==9||e.row==10||e.row==11){").append("haoytrq = gridDiv_ds.getAt(8);").append("trqrz=gridDiv_ds.getAt(9);").append("fadyq=gridDiv_ds.getAt(10);").append("gongryq=gridDiv_ds.getAt(11);").append("if(e.column==4){").append("fadyq.set(\'ZHI1\',Round(eval(fadyq.get(\'ZHI1\')||0),2));").append("gongryq.set(\'ZHI1\',Round(eval(gongryq.get(\'ZHI1\')||0),2));").append("haoytrq .set(\'ZHI1\', Round(eval(fadyq.get(\'ZHI1\')||0) + eval(gongryq.get(\'ZHI1\')||0),2));").append("fadyq.set(\'ZHI2\', Round((eval(fadyq.get(\'ZHI1\')||0) * eval(trqrz.get(\'ZHI1\')||0)/29271),0));").append("gongryq.set(\'ZHI2\', Round((eval(gongryq.get(\'ZHI1\')||0) * eval(trqrz.get(\'ZHI1\')||0)/29271),0));").append("haoytrq .set(\'ZHI2\',  Round(eval(fadyq.get(\'ZHI2\')||0) + eval(gongryq.get(\'ZHI2\')||0),2));").append("}else{").append("haoytrq .set(\'ZHI2\',  Round(eval(fadyq.get(\'ZHI2\')||0) + eval(gongryq.get(\'ZHI2\')||0),2));").append("}").append("}");
            sb.append("if(e.row==13||e.row==14||e.row==15||e.row==16||e.row==19){").append("if(e.column==4){").append("fadl=gridDiv_ds.getAt(13);").append("fadsccgdl=gridDiv_ds.getAt(14);").append("fadzhcgdl=gridDiv_ds.getAt(15);").append("gongrcgdl=gridDiv_ds.getAt(16);").append("shoudl=gridDiv_ds.getAt(17);").append("gongrl=gridDiv_ds.getAt(19);").append("recfadhm= gridDiv_ds.getAt(2);").append("recfadhy= gridDiv_ds.getAt(6);").append("recfadhq= gridDiv_ds.getAt(10);").append("recgongrhm= gridDiv_ds.getAt(3);").append("recgongrhy= gridDiv_ds.getAt(7);").append("recgongrhq= gridDiv_ds.getAt(11);").append("shourdwcb=gridDiv_ds.getAt(37);").append("fadmcb=gridDiv_ds.getAt(22);").append("fadycb=gridDiv_ds.getAt(23);").append("if(e.row==13){").append("fadl .set(\'ZHI2\', Round(((eval(recfadhm.get(\'ZHI2\')||0)+eval(recfadhy.get(\'ZHI2\')||0)+eval(recfadhq.get(\'ZHI2\')||0)) / eval(fadl.get(\'ZHI1\')||0)*100),1));").append("shourdwcb.set(\'ZHI2\', Round(((eval(fadmcb.get(\'ZHI1\')||0)+eval(fadycb.get(\'ZHI1\')||0))*10000 /((eval(fadl.get(\'ZHI1\')||0)-eval(fadsccgdl.get(\'ZHI1\')||0)-eval(gongrcgdl.get(\'ZHI1\')||0))*10)),2));").append("}").append("if(e.row==14){").append("fadsccgdl .set(\'ZHI2\', Round(((eval(recfadhm.get(\'ZHI2\')||0)+eval(recfadhy.get(\'ZHI2\')||0)+eval(recfadhq.get(\'ZHI2\')||0)) /(eval(fadl.get(\'ZHI1\')||0)- eval(fadsccgdl.get(\'ZHI1\')||0))*100),1));").append("shourdwcb.set(\'ZHI2\', Round(((eval(fadmcb.get(\'ZHI1\')||0)+eval(fadycb.get(\'ZHI1\')||0))*10000 /((eval(fadl.get(\'ZHI1\')||0)-eval(fadsccgdl.get(\'ZHI1\')||0)-eval(gongrcgdl.get(\'ZHI1\')||0))*10)),2));").append("}").append("if(e.row==16){").append("shourdwcb.set(\'ZHI2\', Round(((eval(fadmcb.get(\'ZHI1\')||0)+eval(fadycb.get(\'ZHI1\')||0))*10000 /((eval(fadl.get(\'ZHI1\')||0)-eval(fadsccgdl.get(\'ZHI1\')||0)-eval(gongrcgdl.get(\'ZHI1\')||0))*10)),2));").append("}").append("if(e.row==19){").append("shoudl .set(\'ZHI2\', Round(((eval(recgongrhm.get(\'ZHI2\')||0)+eval(recgongrhy.get(\'ZHI2\')||0)+eval(recgongrhq.get(\'ZHI2\')||0)) / eval(gongrl .get(\'ZHI1\')||0)*1000),2));}").append("}").append("}");
            sb.append("if(e.row==17||e.row==18){").append("shoudl=gridDiv_ds.getAt(17);").append("gourdl=gridDiv_ds.getAt(18);").append("fadmcb=gridDiv_ds.getAt(22);").append("fadycb=gridDiv_ds.getAt(23);").append("fadqcb=gridDiv_ds.getAt(26);").append("lirze=gridDiv_ds.getAt(38);").append("gujqk=gridDiv_ds.getAt(39);").append("lirze.set(\'ZHI2\', Round(((eval(fadmcb.get(\'ZHI1\')||0)+eval(fadycb.get(\'ZHI1\')||0))*10000 /((eval(shoudl.get(\'ZHI1\')||0)-eval(gourdl.get(\'ZHI1\')||0))*10)),2));").append("gujqk.set(\'ZHI2\', Round(((eval(fadqcb.get(\'ZHI1\')||0))*10000 /((eval(shoudl.get(\'ZHI1\')||0)-eval(gourdl.get(\'ZHI1\')||0))*10)),2));").append("}");
            sb.append("if(e.row==22||e.row==23||e.row==24||e.row==25||e.row==26||e.row==27){").append("if(e.column==4){").append("recfadhm= gridDiv_ds.getAt(2);").append("recgongrhm= gridDiv_ds.getAt(3);").append("fadhy=gridDiv_ds.getAt(6);").append("gongrhy=gridDiv_ds.getAt(7);").append("fadhq=gridDiv_ds.getAt(10);").append("gongrhq=gridDiv_ds.getAt(11);").append("fadl=gridDiv_ds.getAt(13);").append("fadsccgdl=gridDiv_ds.getAt(14);").append("fadzhcgdl=gridDiv_ds.getAt(15);").append("gongrcgdl=gridDiv_ds.getAt(16);").append("shoudl=gridDiv_ds.getAt(17);").append("gourdl=gridDiv_ds.getAt(18);").append("gongrl=gridDiv_ds.getAt(19);").append("shourl=gridDiv_ds.getAt(20);").append("ranlcb_bhs=gridDiv_ds.getAt(21);").append("fadmcb=gridDiv_ds.getAt(22);").append("fadycb=gridDiv_ds.getAt(23);").append("gongrmcb=gridDiv_ds.getAt(24);").append("gongrycb=gridDiv_ds.getAt(25);").append("fadqcb=gridDiv_ds.getAt(26);").append("gongrqcb=gridDiv_ds.getAt(27);").append("gongrcydftrlf=gridDiv_ds.getAt(28);").append("qiz_ranm=gridDiv_ds.getAt(29);").append("qiz_rany=gridDiv_ds.getAt(30);").append("qiz_ranq=gridDiv_ds.getAt(31);").append("shoudqk=gridDiv_ds.getAt(32);").append("shouddwbdcb=gridDiv_ds.getAt(33);").append("shouddwgdcb=gridDiv_ds.getAt(34);").append("shouddj=gridDiv_ds.getAt(35);").append("shourdj=gridDiv_ds.getAt(36);").append("shourdwcb=gridDiv_ds.getAt(37);").append("lirze=gridDiv_ds.getAt(38);").append("gujqk=gridDiv_ds.getAt(39);").append("fadmcb.set(\'ZHI1\',Round(eval(fadmcb.get(\'ZHI1\')||0),3));").append("fadycb.set(\'ZHI1\',Round(eval(fadycb.get(\'ZHI1\')||0),3));").append("gongrmcb.set(\'ZHI1\',Round(eval(gongrmcb.get(\'ZHI1\')||0),3));").append("gongrycb.set(\'ZHI1\',Round(eval(gongrycb.get(\'ZHI1\')||0),3));").append("fadqcb.set(\'ZHI1\',Round(eval(fadqcb.get(\'ZHI1\')||0),3));").append("gongrqcb.set(\'ZHI1\',Round(eval(gongrqcb.get(\'ZHI1\')||0),3));").append("ranlcb_bhs.set(\'ZHI1\', Round(eval(fadmcb.get(\'ZHI1\')||0) + eval(fadycb.get(\'ZHI1\')||0)+eval(gongrmcb.get(\'ZHI1\')||0)+eval(gongrycb.get(\'ZHI1\')||0)+eval(fadqcb.get(\'ZHI1\')||0)+eval(gongrqcb.get(\'ZHI1\')||0),2));").append("if(e.row==22){shourl.set(\'ZHI2\', Round(((eval(fadmcb.get(\'ZHI1\')||0)+eval(qiz_ranm.get(\'ZHI1\')||0))*10000 /eval(recfadhm.get(\'ZHI1\')||0)),2));").append("gongrl.set(\'ZHI2\', Round(((eval(fadmcb.get(\'ZHI1\')||0)+eval(gongrmcb.get(\'ZHI1\')||0))*10000 /(eval(recfadhm.get(\'ZHI1\')||0)+eval(recgongrhm.get(\'ZHI1\')||0))),2));").append("}").append("if(e.row==23){fadycb.set(\'ZHI2\', Round(((eval(fadycb.get(\'ZHI1\')||0)+eval(qiz_rany.get(\'ZHI1\')||0))*10000 /eval(fadhy.get(\'ZHI1\')||0)),2));").append("fadmcb.set(\'ZHI2\', Round(((eval(fadycb.get(\'ZHI1\')||0)+eval(gongrycb.get(\'ZHI1\')||0))*10000 /(eval(fadhy.get(\'ZHI1\')||0)+eval(gongrhy.get(\'ZHI1\')||0))),2));").append("}").append("if(e.row==24){ranlcb_bhs.set(\'ZHI2\', Round(((eval(gongrmcb.get(\'ZHI1\')||0)-eval(qiz_ranm.get(\'ZHI1\')||0))*10000 /eval(recgongrhm.get(\'ZHI1\')||0)),2));").append("gongrl.set(\'ZHI2\', Round(((eval(fadmcb.get(\'ZHI1\')||0)+eval(gongrmcb.get(\'ZHI1\')||0))*10000 /(eval(recfadhm.get(\'ZHI1\')||0)+eval(recgongrhm.get(\'ZHI1\')||0))),2));").append("}").append("if(e.row==25){gongrmcb.set(\'ZHI2\', Round(((eval(gongrycb.get(\'ZHI1\')||0)-eval(qiz_rany.get(\'ZHI1\')||0))*10000 /eval(gongrhy.get(\'ZHI1\')||0)),2));").append("fadmcb.set(\'ZHI2\', Round(((eval(fadycb.get(\'ZHI1\')||0)+eval(gongrycb.get(\'ZHI1\')||0))*10000 /(eval(fadhy.get(\'ZHI1\')||0)+eval(gongrhy.get(\'ZHI1\')||0))),2));").append("}").append("if(e.row==26){fadqcb.set(\'ZHI2\', Round(((eval(fadqcb.get(\'ZHI1\')||0)+eval(qiz_ranq.get(\'ZHI1\')||0))*10000 /eval(fadhq.get(\'ZHI1\')||0)),2));").append("gongrycb.set(\'ZHI2\', Round(((eval(fadqcb.get(\'ZHI1\')||0)+eval(gongrqcb.get(\'ZHI1\')||0))*10000 /(eval(fadhq.get(\'ZHI1\')||0)+eval(gongrhq.get(\'ZHI1\')||0))),2));").append("}").append("if(e.row==27){gongrqcb.set(\'ZHI2\', Round(((eval(gongrqcb.get(\'ZHI1\')||0)-eval(qiz_ranq.get(\'ZHI1\')||0))*10000 /eval(gongrhq.get(\'ZHI1\')||0)),2));").append("gongrycb.set(\'ZHI2\', Round(((eval(fadqcb.get(\'ZHI1\')||0)+eval(gongrqcb.get(\'ZHI1\')||0))*10000 /(eval(fadhq.get(\'ZHI1\')||0)+eval(gongrhq.get(\'ZHI1\')||0))),2));").append("}").append("if(e.row==22){qiz_rany.set(\'ZHI2\', Round(((eval(fadmcb.get(\'ZHI1\')||0)+eval(qiz_ranm.get(\'ZHI1\')||0))*10000 /eval(recfadhm.get(\'ZHI2\')||0)),2));").append("qiz_ranm.set(\'ZHI2\', Round(((eval(fadmcb.get(\'ZHI1\')||0)+eval(fadycb.get(\'ZHI1\')||0)+eval(fadqcb.get(\'ZHI1\')||0)+eval(gongrcydftrlf.get(\'ZHI1\')||0))*10000 /(eval(recfadhm.get(\'ZHI2\')||0)+eval(fadhy.get(\'ZHI2\')||0)+eval(fadhq.get(\'ZHI2\')||0))),2));").append("}").append("if(e.row==23){qiz_ranq.set(\'ZHI2\', Round(((eval(fadycb.get(\'ZHI1\')||0)+eval(qiz_rany.get(\'ZHI1\')||0))*10000 /eval(fadhy.get(\'ZHI2\')||0)),2));").append("qiz_ranm.set(\'ZHI2\', Round(((eval(fadmcb.get(\'ZHI1\')||0)+eval(fadycb.get(\'ZHI1\')||0)+eval(fadqcb.get(\'ZHI1\')||0)+eval(gongrcydftrlf.get(\'ZHI1\')||0))*10000 /(eval(recfadhm.get(\'ZHI2\')||0)+eval(fadhy.get(\'ZHI2\')||0)+eval(fadhq.get(\'ZHI2\')||0))),2));").append("}").append("if(e.row==26){shoudqk.set(\'ZHI2\', Round(((eval(fadqcb.get(\'ZHI1\')||0)+eval(qiz_ranq.get(\'ZHI1\')||0))*10000 /eval(fadhq.get(\'ZHI2\')||0)),2));").append("qiz_ranm.set(\'ZHI2\', Round(((eval(fadmcb.get(\'ZHI1\')||0)+eval(fadycb.get(\'ZHI1\')||0)+eval(fadqcb.get(\'ZHI1\')||0)+eval(gongrcydftrlf.get(\'ZHI1\')||0))*10000 /(eval(recfadhm.get(\'ZHI2\')||0)+eval(fadhy.get(\'ZHI2\')||0)+eval(fadhq.get(\'ZHI2\')||0))),2));").append("}").append("if(e.row==24){shouddwgdcb.set(\'ZHI2\', Round(((eval(gongrmcb.get(\'ZHI1\')||0)-eval(qiz_ranm.get(\'ZHI1\')||0))*10000 /eval(recgongrhm.get(\'ZHI2\')||0)),2));").append("shouddwbdcb.set(\'ZHI2\', Round(((eval(gongrmcb.get(\'ZHI1\')||0)+eval(gongrycb.get(\'ZHI1\')||0)+eval(gongrqcb.get(\'ZHI1\')||0)-eval(gongrcydftrlf.get(\'ZHI1\')||0))*10000 /(eval(recgongrhm.get(\'ZHI2\')||0)+eval(gongrhy.get(\'ZHI2\')||0)+eval(gongrhq.get(\'ZHI2\')||0))),2));").append("}").append("if(e.row==25){shouddj.set(\'ZHI2\', Round(((eval(gongrycb.get(\'ZHI1\')||0)-eval(qiz_rany.get(\'ZHI1\')||0))*10000 /eval(gongrhy.get(\'ZHI2\')||0)),2));").append("shouddwbdcb.set(\'ZHI2\', Round(((eval(gongrmcb.get(\'ZHI1\')||0)+eval(gongrycb.get(\'ZHI1\')||0)+eval(gongrqcb.get(\'ZHI1\')||0)-eval(gongrcydftrlf.get(\'ZHI1\')||0))*10000 /(eval(recgongrhm.get(\'ZHI2\')||0)+eval(gongrhy.get(\'ZHI2\')||0)+eval(gongrhq.get(\'ZHI2\')||0))),2));").append("}").append("if(e.row==27){shourdj.set(\'ZHI2\', Round(((eval(gongrqcb.get(\'ZHI1\')||0)-eval(qiz_ranq.get(\'ZHI1\')||0))*10000 /eval(gongrhq.get(\'ZHI2\')||0)),2));").append("shouddwbdcb.set(\'ZHI2\', Round(((eval(gongrmcb.get(\'ZHI1\')||0)+eval(gongrycb.get(\'ZHI1\')||0)+eval(gongrqcb.get(\'ZHI1\')||0)-eval(gongrcydftrlf.get(\'ZHI1\')||0))*10000 /(eval(recgongrhm.get(\'ZHI2\')||0)+eval(gongrhy.get(\'ZHI2\')||0)+eval(gongrhq.get(\'ZHI2\')||0))),2));").append("}").append("if(e.row==22||e.row==23){").append("shourdwcb.set(\'ZHI2\', Round(((eval(fadmcb.get(\'ZHI1\')||0)+eval(fadycb.get(\'ZHI1\')||0))*10000 /((eval(fadl.get(\'ZHI1\')||0)-eval(fadsccgdl.get(\'ZHI1\')||0)-eval(gongrcgdl.get(\'ZHI1\')||0))*10)),2));").append("lirze.set(\'ZHI2\', Round(((eval(fadmcb.get(\'ZHI1\')||0)+eval(fadycb.get(\'ZHI1\')||0))*10000 /((eval(shoudl.get(\'ZHI1\')||0)-eval(gourdl.get(\'ZHI1\')||0))*10)),2));").append("}").append("if(e.row==26){").append("gujqk.set(\'ZHI2\', Round(((eval(fadqcb.get(\'ZHI1\')||0))*10000 /((eval(shoudl.get(\'ZHI1\')||0)-eval(gourdl.get(\'ZHI1\')||0))*10)),2));").append("}").append("}").append("}");
            sb.append("if(e.row==29|| e.row==30||e.row==31){").append("recfadhm= gridDiv_ds.getAt(2);").append("recgongrhm= gridDiv_ds.getAt(3);").append("fadhy=gridDiv_ds.getAt(6);").append("gongrhy=gridDiv_ds.getAt(7);").append("fadhq=gridDiv_ds.getAt(10);").append("gongrhq=gridDiv_ds.getAt(11);").append("shourl=gridDiv_ds.getAt(20);").append("ranlcb_bhs=gridDiv_ds.getAt(21);").append("fadmcb=gridDiv_ds.getAt(22);").append("fadycb=gridDiv_ds.getAt(23);").append("gongrmcb=gridDiv_ds.getAt(24);").append("gongrycb=gridDiv_ds.getAt(25);").append("fadqcb=gridDiv_ds.getAt(26);").append("gongrqcb=gridDiv_ds.getAt(27);").append("shoudqk=gridDiv_ds.getAt(32);").append("shouddwbdcb=gridDiv_ds.getAt(33);").append("shouddwgdcb=gridDiv_ds.getAt(34);").append("shouddj=gridDiv_ds.getAt(35);").append("shourdj=gridDiv_ds.getAt(36);").append("gongrcydftf = gridDiv_ds.getAt(28);").append("qiz_rm=gridDiv_ds.getAt(29);").append("qiz_ry= gridDiv_ds.getAt(30);").append("qiz_rq= gridDiv_ds.getAt(31);").append("gongrcydftf.set(\'ZHI1\', Round(eval(qiz_rm.get(\'ZHI1\')||0) + eval(qiz_ry.get(\'ZHI1\')||0)+eval(qiz_rq.get(\'ZHI1\')||0),2));").append("if(e.row==29){").append(" qiz_ry.set(\'ZHI2\', Round(((eval(fadmcb.get(\'ZHI1\')||0)+eval(qiz_rm.get(\'ZHI1\')||0))*10000 /eval(recfadhm.get(\'ZHI2\')||0)),2));").append("qiz_rm.set(\'ZHI2\', Round(((eval(fadmcb.get(\'ZHI1\')||0)+eval(fadycb.get(\'ZHI1\')||0)+eval(fadqcb.get(\'ZHI1\')||0)+eval(gongrcydftf.get(\'ZHI1\')||0))*10000 /(eval(recfadhm.get(\'ZHI2\')||0)+eval(fadhy.get(\'ZHI2\')||0)+eval(fadhq.get(\'ZHI2\')||0))),2));").append("shouddwgdcb.set(\'ZHI2\', Round(((eval(gongrmcb.get(\'ZHI1\')||0)-eval(qiz_rm.get(\'ZHI1\')||0))*10000 /eval(recgongrhm.get(\'ZHI2\')||0)),2));").append("shouddwbdcb.set(\'ZHI2\', Round(((eval(gongrmcb.get(\'ZHI1\')||0)+eval(gongrycb.get(\'ZHI1\')||0)+eval(gongrqcb.get(\'ZHI1\')||0)-eval(gongrcydftf.get(\'ZHI1\')||0))*10000 /(eval(recgongrhm.get(\'ZHI2\')||0)+eval(gongrhy.get(\'ZHI2\')||0)+eval(gongrhq.get(\'ZHI2\')||0))),2));").append("shourl.set(\'ZHI2\', Round(((eval(fadmcb.get(\'ZHI1\')||0)+eval(qiz_rm.get(\'ZHI1\')||0))*10000 /eval(recfadhm.get(\'ZHI1\')||0)),2));").append("ranlcb_bhs.set(\'ZHI2\', Round(((eval(gongrmcb.get(\'ZHI1\')||0)-eval(qiz_rm.get(\'ZHI1\')||0))*10000 /eval(recgongrhm.get(\'ZHI1\')||0)),2));").append("}").append("if(e.row==30){").append(" qiz_rq.set(\'ZHI2\', Round(((eval(fadycb.get(\'ZHI1\')||0)+eval(qiz_ry.get(\'ZHI1\')||0))*10000 /eval(fadhy.get(\'ZHI2\')||0)),2));").append("qiz_rm.set(\'ZHI2\', Round(((eval(fadmcb.get(\'ZHI1\')||0)+eval(fadycb.get(\'ZHI1\')||0)+eval(fadqcb.get(\'ZHI1\')||0)+eval(gongrcydftf.get(\'ZHI1\')||0))*10000 /(eval(recfadhm.get(\'ZHI2\')||0)+eval(fadhy.get(\'ZHI2\')||0)+eval(fadhq.get(\'ZHI2\')||0))),2));").append("shouddj.set(\'ZHI2\', Round(((eval(gongrycb.get(\'ZHI1\')||0)-eval(qiz_ry.get(\'ZHI1\')||0))*10000 /eval(gongrhy.get(\'ZHI2\')||0)),2));").append("shouddwbdcb.set(\'ZHI2\', Round(((eval(gongrmcb.get(\'ZHI1\')||0)+eval(gongrycb.get(\'ZHI1\')||0)+eval(gongrqcb.get(\'ZHI1\')||0)-eval(gongrcydftf.get(\'ZHI1\')||0))*10000 /(eval(recgongrhm.get(\'ZHI2\')||0)+eval(gongrhy.get(\'ZHI2\')||0)+eval(gongrhq.get(\'ZHI2\')||0))),2));").append("fadycb.set(\'ZHI2\', Round(((eval(fadycb.get(\'ZHI1\')||0)+eval(qiz_ry.get(\'ZHI1\')||0))*10000 /eval(fadhy.get(\'ZHI1\')||0)),2));").append("gongrmcb.set(\'ZHI2\', Round(((eval(gongrycb.get(\'ZHI1\')||0)-eval(qiz_ry.get(\'ZHI1\')||0))*10000 /eval(gongrhy.get(\'ZHI1\')||0)),2));").append("}").append("if(e.row==31){").append(" shoudqk.set(\'ZHI2\', Round(((eval(fadqcb.get(\'ZHI1\')||0)+eval(qiz_rq.get(\'ZHI1\')||0))*10000 /eval(fadhq.get(\'ZHI2\')||0)),2));").append("qiz_rm.set(\'ZHI2\', Round(((eval(fadmcb.get(\'ZHI1\')||0)+eval(fadycb.get(\'ZHI1\')||0)+eval(fadqcb.get(\'ZHI1\')||0)+eval(gongrcydftf.get(\'ZHI1\')||0))*10000 /(eval(recfadhm.get(\'ZHI2\')||0)+eval(fadhy.get(\'ZHI2\')||0)+eval(fadhq.get(\'ZHI2\')||0))),2));").append("shourdj.set(\'ZHI2\', Round(((eval(gongrqcb.get(\'ZHI1\')||0)-eval(qiz_rq.get(\'ZHI1\')||0))*10000 /eval(gongrhq.get(\'ZHI2\')||0)),2));").append("shouddwbdcb.set(\'ZHI2\', Round(((eval(gongrmcb.get(\'ZHI1\')||0)+eval(gongrycb.get(\'ZHI1\')||0)+eval(gongrqcb.get(\'ZHI1\')||0)-eval(gongrcydftf.get(\'ZHI1\')||0))*10000 /(eval(recgongrhm.get(\'ZHI2\')||0)+eval(gongrhy.get(\'ZHI2\')||0)+eval(gongrhq.get(\'ZHI2\')||0))),2));").append("fadqcb.set(\'ZHI2\', Round(((eval(fadqcb.get(\'ZHI1\')||0)+eval(qiz_rq.get(\'ZHI1\')||0))*10000 /eval(fadhq.get(\'ZHI1\')||0)),2));").append("gongrqcb.set(\'ZHI2\', Round(((eval(gongrqcb.get(\'ZHI1\')||0)-eval(qiz_rq.get(\'ZHI1\')||0))*10000 /eval(gongrhq.get(\'ZHI1\')||0)),2));").append("}").append("}");
            sb.append("if(e.row==42){").append("fadmcb=gridDiv_ds.getAt(22);").append("fadycb=gridDiv_ds.getAt(23);").append("fadqcb=gridDiv_ds.getAt(26);").append("recfadhm= gridDiv_ds.getAt(2);").append("fadhy=gridDiv_ds.getAt(6);").append("fadhq=gridDiv_ds.getAt(10);").append("gongrcydftf = gridDiv_ds.getAt(28);").append("qiz_rm=gridDiv_ds.getAt(29);").append("qitfy=gridDiv_ds.getAt(42);").append("qiz_rm.set(\'ZHI2\', Round(((eval(fadmcb.get(\'ZHI1\')||0)+eval(fadycb.get(\'ZHI1\')||0)+eval(fadqcb.get(\'ZHI1\')||0)+eval(gongrcydftf.get(\'ZHI1\')||0)+eval(qitfy.get(\'ZHI1\')||0))*10000 /(eval(recfadhm.get(\'ZHI2\')||0)+eval(fadhy.get(\'ZHI2\')||0)+eval(fadhq.get(\'ZHI2\')||0))),2));").append("}");
            sb.append("});");
            egu.addOtherScript(sb.toString());
            this.setExtGrid(egu);
            con.Close();
            return;
        }

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
        if(visit.getRenyjb()==3){
            if(!this.getTreeid().equals(visit.getDiancxxb_id()+"")){
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
        if (_NianfValue == null||_NianfValue.equals("")) {
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

    /*public IDropDownBean getYuefValue() {
        if (_YuefValue == null) {
            for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
                Object obj = _YuefModel.getOption(i);
                if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
                        .getId()) {
                    _YuefValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _YuefValue;
    }*/
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
        if (_YuefValue!= null) {
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
    /*public String getTreeid() {
        return treeid;
    }
    public void setTreeid(String treeid) {
        this.treeid = treeid;
    }*/
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