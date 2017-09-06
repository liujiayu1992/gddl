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
* ʱ�䣺2009-03-26 
* ���ߣ� ll
* �޸����ݣ�1��ȥ����˺ͻ��˹��ܡ�
* 		   2������ж�ѭ���ۼƵ�ʱ�䣬�������û�����ݣ�ѭ����������
* 		   3��ҳ��ˢ��ʱ����ú���۽��м��㣬��ֱ�Ӵӿ���ȡ����
*/  
/* 
* ʱ�䣺2009-04-09 
* ���ߣ� ll
* �޸����ݣ��޸����¼�������ۼ�ʱ��yuetjkjb_id��ֵȡ���µ�yuetjkjb_id��
* 		   
*/  
/* 
* ʱ�䣺2009-04-10 
* ���ߣ� ll
* �޸����ݣ�
* 		   �޸����¼�������ۼ�ʱ����������ͬ������yuetjkjb_idֵ��bug���⡣
*/ 
/* 
* ʱ�䣺2009-5-4 
* ���ߣ� ll
* �޸����ݣ�
* 		   �޸Ĳ�ѯsql�г���Ϊ0��bug���⡣
/* 
* ʱ�䣺2009-5-4 
* ���ߣ� sy
* �޸����ݣ�
* 		  ����biaomdj��sql ��decodeƴд����Ŷ��
*/ 
/* 
* ʱ�䣺2009-05-18
* ���ߣ� ll
* �޸����ݣ�����ƽ�׵糧��һ�����ƣ�ƽ��һ���ƽ�׶���ͬ��һ������ϵͳ����������ݡ�
* 		   �������糧�ͬһ���±�ҳ��ʱҳ�����������beginResponse()���������û�����Ϊ�糧����
  �жϵ�½�糧��糧���Ƿ�һ�£������¼���ˢ��ҳ�档
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
        // TODO �Զ����ɷ������
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

    // ҳ��仯��¼
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

//		�ж�yuetjkjb���Ƿ��д���Ϣ��û�в��롣
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
                sql.append("insert into yuercbmdj(id,fenx,yuetjkjb_id,hetj,meij,meijs,yunj,yunjs,daozzf,zaf,qit,jiaohqzf,biaomdj,buhsbmdj,yunsjl,yunfsl,yunfslfs \n,daozzfslfs,zafslfs,qitslfs,jiaohqzfslfs,daozzfsl ,zafsl ,qitsl ,jiaohqzfsl ,DAOZZFS, zafs, qits,jiaohqzfs)values(" + id + ",\'����\'," + yuetjkjb_id + ",nvl(" + rsl.getDouble("hetj") + ",0),nvl(" + rsl.getDouble("meij") + ",0),nvl(" + rsl.getDouble("meijs") + ",0),nvl(" + rsl.getDouble("yunj") + ",0),nvl(" + rsl.getDouble("yunjs") + ",0),nvl(" + rsl.getDouble("daozzf") + ",0),nvl(" + rsl.getDouble("zaf") + ",0),nvl(" + rsl.getDouble("qit") + ",0),nvl(" + rsl.getDouble("jiaohqzf") + ",0),nvl(" + rsl.getDouble("biaomdj") + ",0),nvl(" + rsl.getDouble("buhsbmdj") + ",0),nvl(" + rsl.getDouble("yunsjl") + ",0),nvl(" + rsl.getDouble("yunfsl") + ",0),\'" + rsl.getString("yunfslfs") + "\',\'" + rsl.getString("daozzfslfs") + "\',\'" + rsl.getString("zafslfs") + "\',\'" + rsl.getString("qitslfs") + "\',\'" + rsl.getString("jiaohqzfsl") + "\',nvl(" + rsl.getDouble("daozzfsl") + ",0),nvl(" + rsl.getDouble("zafsl") + ",0),nvl(" + rsl.getDouble("qitsl") + ",0),nvl(" + rsl.getDouble("jiaohqzfsl") + ",0),nvl(" + rsl.getDouble("DAOZZFS") + ",0),nvl(" + rsl.getDouble("zafs") + ",0),nvl(" + rsl.getDouble("qits") + ",0),nvl(" + rsl.getDouble("jiaohqzfs") + ",0) " + ");\n");
            }

            sql.append("end;");
            con.getUpdate(sql.toString());
            this.LeijSelect();
        }

        con.Close();
        this.setMsg("����ɹ�!");
    }

    public void LeijSelect() {
        Visit visit = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        // ����������ݺ��·�������
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
            boolean jzyf=false;//���ý�ֹ�·ݵ�boolean
            String pdyf="select y.id as id from yuercbmdj y,yuetjkjb kj where y.yuetjkjb_id = kj.id \n"
                    +"and kj.diancxxb_id=" +visit.getDiancxxb_id()+" and kj.riq=to_date('"+intyear+"-"+(intMonth+i)+"-01','yyyy-mm-dd')" ;
            ResultSet pdyfsql=con.getResultSet(pdyf);
            try{
                //�ж�ѭ���·��Ƿ������ݣ�������jzyf��true,����jzyf��falseֹͣѭ����
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
            if(jzyf==true){//��ѭ���·������ݣ�ѭ���ۼ�ִ�У���ѭ���·������ݣ���ѭ��������
                //��ѯ�ۼ�ֵ=����+�����ۼ�
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
                        + "     and y.fenx='����'\n"
                        + "     and sl.fenx='����'\n"
                        + "     and zl.fenx='����'\n"
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
//					��ȡ�ۼƵ����ۼ�״̬
                    String shzt=
                            "select max(zhuangt) as zhuangt from yuercbmdj cb,yuetjkjb kj where cb.yuetjkjb_id=kj.id and kj.diancxxb_id=" +visit.getDiancxxb_id()+"\n" +
                                    "       and kj.riq=to_date('"+intyear+"-"+(intMonth+i)+"-01','yyyy-mm-dd')  and cb.fenx='�ۼ�'";
                    ResultSetList shenhzt=con.getResultSetList(shzt);
                    long zhuangt=0;

                    while(shenhzt.next()){
                        zhuangt=shenhzt.getLong("zhuangt");
                    }
                    String deletelj="delete from yuercbmdj where id in(select cb.id from yuercbmdj cb,yuetjkjb k \n" +
                            "where cb.yuetjkjb_id=k.id and k.riq= to_date('"+intyear+"-"+(intMonth+i)+"-01','yyyy-mm-dd')\n" +
                            "	   and cb.fenx='�ۼ�' and k.diancxxb_id=" +visit.getDiancxxb_id()+");\n";
                    sqllj.append(deletelj);

                    while (rsllj.next()) {

                        String kjidsql=
                                "select cb.yuetjkjb_id as yuetjkjbid from yuetjkjb kj,yuercbmdj cb\n" +
                                        "where cb.yuetjkjb_id=kj.id and kj.diancxxb_id="+rsllj.getLong("diancxxb_id")+" and kj.riq = to_date('"+intyear+"-"+(intMonth+i)+"-01','yyyy-mm-dd')\n" +
                                        "      and kj.gongysb_id="+rsllj.getLong("gongysb_id")+" and kj.jihkjb_id="+rsllj.getLong("jihkjb_id")+
                                        " and kj.yunsfsb_id="+rsllj.getLong("yunsfsb_id")+" and kj.pinzb_id="+rsllj.getLong("pinzb_id")+"\n"+
                                        " and cb.fenx='����'";
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
                                        +",'�ۼ�',"
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
        String CurrZnDate = getNianfValue() + "��" + getYuefValue() + "��";
        String riq = DateUtil.FormatOracleDate(getNianfValue() + "-" + getYuefValue() + "-01");
        String strSql = "delete from yuercbmdj where yuetjkjb_id in (select id from yuetjkjb where riq="
                + riq + " and diancxxb_id=" + getTreeid() + ")";
        int flag = con.getDelete(strSql);
        if (flag == -1) {
            WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:" + strSql);
            setMsg("ɾ�������з�������");
        } else {
            setMsg(CurrZnDate + "�����ݱ��ɹ�ɾ����");
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

        String chaxun = "select \'����\' as fenx, max(nvl(b.id,0)) as id,\n      riq,yuetjkjb_id,\n               decode(grouping(b.diancxxb_id), 1, (select mingc from diancxxb where id=" + this.getTreeid() + "),b.diancxxb_id) as diancxxb_id,\n" + "               decode(grouping(b.meikdqb_id),1,\'�ܼ�\',b.meikdqb_id) as meikdqb_id,\n" + "               decode(grouping(b.jihkjb_id),1,\'-\',b.jihkjb_id) as jihkjb_id,\n" + "               decode(grouping(b.pinzb_id),1,\'-\',b.pinzb_id) as pinzb_id,\n" + "               decode(grouping(b.yunsfsb_id),1,\'-\',b.yunsfsb_id) as yunsfsb_id,\n" + "               sum(nvl(b.laimsl,0)) as laimsl,\n" + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.farl)/sum(b.laimsl),2)),0) as farl,\n" + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.hetj)/sum(b.laimsl),2)),0) as hetj,\n" + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.meij)/sum(b.laimsl),2)),0) as meij,\n" + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.meijs)/sum(b.laimsl),2)),0) as meijs,\n" + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.yunj)/sum(b.laimsl),2)),0) as yunj,\n" + "\t\t\t\tmax(yunfslfs)yunfslfs,max(yunfsl)yunfsl," + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.yunjs)/sum(b.laimsl),2)),0) as yunjs,\n" + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.daozzf)/sum(b.laimsl),2)),0) as daozzf,\n" + "\t\t\t\tmax(daozzfslfs)daozzfslfs,max(daozzfsl)daozzfsl ,\n" + "   \t\t\t nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.daozzfs)/sum(b.laimsl),2)),0) as daozzfs,\n  " + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.zaf)/sum(b.laimsl),2)),0) as zaf,\n" + "\t\t\t\tmax(zafslfs)zafslfs,max(zafsl)zafsl ,\n" + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.zafs)/sum(b.laimsl),2)),0) as zafs,\n" + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.qit)/sum(b.laimsl),2)),0) as qit,\n" + "\t\t\t\tmax(qitslfs)qitslfs,max(qitsl) qitsl,\n" + "\t\t\t\tnvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.qits)/sum(b.laimsl),2)),0) as qits,\n" + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.jiaohqzf)/sum(b.laimsl),2)),0) as jiaohqzf,\n" + "\t\t\t\tmax(jiaohqzfslfs)jiaohqzfslfs,max(jiaohqzfsl)jiaohqzfsl,\n " + "\t\t\t\t nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.jiaohqzfs)/sum(b.laimsl),2)),0) as jiaohqzfs,\n" + "\t\t\t\tnvl(decode(decode(sum(b.laimsl),0,0,sum(b.laimsl*b.farl)/sum(b.laimsl)),0,0,round(round(decode(sum(b.laimsl),0,0,sum((b.meij+b.yunj+b.daozzf+b.zaf+b.qit+b.jiaohqzf-b.meijs-b.yunjs- b.daozzfs-b.zafs-b.qits-b.jiaohqzfs)*b.laimsl)/sum(b.laimsl)),2)*29.271/round(decode(sum(b.laimsl),0,0,sum(b.laimsl*b.farl)/sum(b.laimsl)),2),2)),0) as buhsbmdj,\n" + "               nvl(decode(decode(sum(b.laimsl),0,0,sum(b.laimsl*b.farl)/sum(b.laimsl)),0,0,round(round(decode(sum(b.laimsl),0,0,sum((b.meij+b.yunj+b.daozzf+b.zaf+b.qit+b.jiaohqzf)*b.laimsl)/sum(b.laimsl)),2)*29.271/round(decode(sum(b.laimsl),0,0,sum(b.laimsl*b.farl)/sum(b.laimsl)),2),2)),0) as biaomdj,\n" + "               nvl(decode(sum(b.laimsl),0,0,round(sum(b.laimsl*b.yunsjl)/sum(b.laimsl),2)),0) as yunsjl,\n" + "               sum(b.zhuangt) as zhuangt\n" + "  from(\n" + "select \'����\' as fenx, y.id,tj.riq,tj.id yuetjkjb_id,dc.mingc as diancxxb_id,g.mingc as meikdqb_id,j.mingc as jihkjb_id,p.mingc as pinzb_id,ys.mingc as yunsfsb_id,\n" + "       sl.laimsl,zl.qnet_ar as farl,y.hetj,y.meij,y.meijs,y.yunj,y.yunjs,y.daozzf,y.zaf,y.qit,y.jiaohqzf,\n" + "\t\t  round(decode(zl.qnet_ar,0,0,(y.meij+y.yunj+y.daozzf+y.zaf+y.qit+y.jiaohqzf-y.meijs-y.yunjs- y.daozzfs-y.zafs-y.qits-y.jiaohqzfs)*29.271/zl.qnet_ar),2) as buhsbmdj,\n" + "\t\t  round(decode(zl.qnet_ar,0,0,(y.meij+y.yunj+y.daozzf+y.zaf+y.qit+y.jiaohqzf)*29.271/zl.qnet_ar),2) as biaomdj,y.yunsjl,nvl(y.zhuangt,0) as zhuangt \n" + "\t\t\t,yunfslfs,yunfsl ,daozzfslfs,daozzfsl ,zafslfs,zafsl ,qitslfs,qitsl ,jiaohqzfslfs,jiaohqzfsl ,DAOZZFS, zafs, qits,jiaohqzfs\n" + "from (\n" + "select y.* from yuercbmdj  y ,yuetjkjb tj\n" + "       where tj.riq =to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\' )\n" + "       and y.yuetjkjb_id=tj.id\n" + "       and y.fenx=\'����\' " + zhuangt + ") y,\n" + " (select y.* from yueslb  y ,yuetjkjb tj\n" + "       where tj.riq =to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\' )\n" + "       and y.yuetjkjb_id=tj.id\n" + "       and y.fenx=\'����\' " + zhuangt + " ) sl,\n" + "(select y.* from yuezlb  y ,yuetjkjb tj\n" + "       where tj.riq =to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\' )\n" + "       and y.yuetjkjb_id=tj.id\n" + "       and y.fenx=\'����\' " + zhuangt + " ) zl ,yuetjkjb tj ,meikdqb g,jihkjb j,pinzb p,diancxxb dc,yunsfsb ys\n" + "where y.yuetjkjb_id(+)=tj.id\n" + "and tj.gongysb_id=g.id\n" + "and tj.jihkjb_id=j.id\n" + "and tj.pinzb_id=p.id\n" + "and tj.diancxxb_id=dc.id\n" + "and tj.yunsfsb_id=ys.id\n" + "and tj.id=zl.yuetjkjb_id(+)\n" + "and tj.id=sl.yuetjkjb_id(+)  " + str + "\n" + "and tj.riq=to_date(\'" + intyear + "-" + StrMonth + "-01\',\'yyyy-mm-dd\' )\n" + "and (sl.fenx=\'����\' or zl.fenx=\'����\' or y.fenx=\'����\')\n" + ")b \n" + "group by rollup(meikdqb_id,diancxxb_id,riq,yuetjkjb_id,jihkjb_id,pinzb_id, yunsfsb_id)\n" + "having grouping(meikdqb_id) = 1 or grouping(yunsfsb_id)=0\n" + "order by meikdqb_id desc,id";
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
            egu.getColumn("riq").setHeader("����");
            egu.getColumn("riq").setHidden(true);
            egu.getColumn("riq").setEditor((Field) null);
            egu.getColumn("fenx").setHidden(true);
            egu.getColumn("yuetjkjb_id").setHidden(true);
            egu.getColumn("diancxxb_id").setHeader("�糧����");
            egu.getColumn("diancxxb_id").setEditor((Field) null);
            egu.getColumn("meikdqb_id").setHeader("������λ");
            egu.getColumn("meikdqb_id").setEditor((Field) null);
            egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
            egu.getColumn("jihkjb_id").setEditor((Field) null);
            egu.getColumn("pinzb_id").setHeader("Ʒ��");
            egu.getColumn("pinzb_id").setHidden(true);
            egu.getColumn("pinzb_id").setEditor((Field) null);
            egu.getColumn("yunsfsb_id").setHeader("���䷽ʽ");
            egu.getColumn("yunsfsb_id").setEditor((Field) null);
            egu.getColumn("hetj").setHeader("��ͬ��<br>(Ԫ/��)");
            egu.getColumn("laimsl").setHeader("ʵ����<br>(��)");
            egu.getColumn("laimsl").setEditor((Field) null);
            egu.getColumn("farl").setHeader("����<br>((MJ/kg))");
            egu.getColumn("farl").setEditor((Field) null);
            egu.getColumn("meij").setHeader("ú��(��˰)<br>(Ԫ/��)");
            egu.getColumn("meijs").setHeader("ú˰<br>(Ԫ/��)");
            egu.getColumn("yunj").setHeader("�˼�(��˰)<br>(Ԫ/��)");
            egu.getColumn("yunfslfs").setHeader("�˼�<br>˰�ʷ�ʽ");
            egu.getColumn("yunfsl").setHeader("�˼�<br>˰��(%)");
            egu.getColumn("yunjs").setHeader("�˼�˰<br>(Ԫ/��)");
            egu.getColumn("daozzf").setHeader("��վ�ӷ�<br>(Ԫ/��)");
            egu.getColumn("zaf").setHeader("�ӷ�<br>(Ԫ/��)");
            egu.getColumn("qit").setHeader("����<br>(Ԫ/��)");
            egu.getColumn("jiaohqzf").setHeader("����ǰ�ӷ�<br>(Ԫ/��)");
            egu.getColumn("buhsbmdj").setHeader("��ú����(����˰)<br>(Ԫ/��)");
            egu.getColumn("buhsbmdj").setEditor((Field) null);
            egu.getColumn("biaomdj").setHeader("��ú����(��˰)<br>(Ԫ/��)");
            egu.getColumn("biaomdj").setEditor((Field) null);
            egu.getColumn("yunsjl").setHeader("�������<br>(����)");
            egu.getColumn("zhuangt").setHeader("״̬");
            egu.getColumn("zhuangt").setHidden(true);
            egu.getColumn("zhuangt").setEditor((Field) null);
            egu.getColumn("daozzfsl").setHeader("��վ�ӷ�<br>˰��(%)");
            egu.getColumn("daozzfslfs").setHeader("��վ�ӷ�<br>˰�ʷ�ʽ");
            egu.getColumn("zafsl").setHeader("�ӷ�<br>˰��(%)");
            egu.getColumn("zafslfs").setHeader("�ӷ�<br>˰�ʷ�ʽ");
            egu.getColumn("qitsl").setHeader("����<br>˰��(%)");
            egu.getColumn("qitslfs").setHeader("����<br>˰�ʷ�ʽ");
            egu.getColumn("jiaohqzfsl").setHeader("����ǰ�ӷ�<br>˰��(%)");
            egu.getColumn("jiaohqzfslfs").setHeader("����ǰ�ӷ�<br>˰�ʷ�ʽ");
            egu.getColumn("daozzfs").setHeader("��վ�ӷ�<br>˰<br>(Ԫ/��)");
            egu.getColumn("zafs").setHeader("�ӷ�<br>˰<br>(Ԫ/��)");
            egu.getColumn("qits").setHeader("����<br>˰<br>(Ԫ/��)");
            egu.getColumn("jiaohqzfs").setHeader("����ǰ�ӷ�<br>˰<br>(Ԫ/��)");
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
            l1.add(new IDropDownBean(0L, "��ֵ˰"));
            l1.add(new IDropDownBean(1L, "Ӫҵ˰"));
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
            sb1.append("if(e.record.get(\'GONGYSB_ID\')==\'�ܼ�\'){e.cancel=true;}");
            sb1.append("});");
            sb1.append("function gridDiv_save(record){if(record.get(\'meikdqb_id\')==\'�ܼ�\') return \'continue\';}");
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
            egu.addTbarText("���:");
            ComboBox comb1 = new ComboBox();
            comb1.setTransform("NIANF");
            comb1.setId("NIANF");
            comb1.setLazyRender(true);
            comb1.setWidth(60);
            egu.addToolbarItem(comb1.getScript());
            egu.addTbarText("-");
            egu.addTbarText("�·�:");
            ComboBox comb2 = new ComboBox();
            comb2.setTransform("YUEF");
            comb2.setId("YUEF");
            comb2.setLazyRender(true);
            comb2.setWidth(50);
            egu.addToolbarItem(comb2.getScript());
            egu.addTbarText("-");
            egu.addTbarText("��λ:");
            ExtTreeUtil etu = new ExtTreeUtil("diancTree", 10, ((Visit) this.getPage().getVisit()).getDiancxxb_id(), this.getTreeid());
            this.setTree(etu);
            egu.addTbarTreeBtn("diancTree");
            egu.addOtherScript("NIANF.on(\'select\',function(){document.forms[0].submit();});YUEF.on(\'select\',function(){document.forms[0].submit();});");
            egu.addTbarText("-");
            StringBuffer rsb = new StringBuffer();
            rsb.append("function (){").append(MainGlobal.getExtMessageBox("\'����ˢ��\'+Ext.getDom(\'NIANF\').value+\'��\'+Ext.getDom(\'YUEF\').value+\'�µ�����,���Ժ�\'", true)).append("document.getElementById(\'RefreshButton\').click();}");
            GridButton gbr = new GridButton("ˢ��", rsb.toString());
            gbr.setIcon("imgs/btnicon/refurbish.gif");
            egu.addTbarBtn(gbr);
            if (visit.getRenyjb() == 3 && !yincan) {
                egu.addToolbarButton(1, (String) null);
                egu.addToolbarButton(11, "SaveButton", MainGlobal.getExtMessageShow("���ڱ���,���Ժ�...", "������...", 200));
            }

            egu.addTbarText("->");
            egu.addTbarText("<font color=\"#EE0000\">��λ:�֡�Ԫ��MJ/Kg������</font>");
            StringBuffer sb = new StringBuffer();
            sb.append("gridDiv_grid.on(\'afteredit\',function(e){");
            sb.append("if (e.field ==\'MEIJ\'){e.record.set(\'MEIJS\',Round(parseFloat(e.record.get(\'MEIJ\')==\'\'?0:e.record.get(\'MEIJ\'))- (parseFloat(e.record.get(\'MEIJ\')==\'\'?0:e.record.get(\'MEIJ\'))/1.17),2))};");
            sb.append("if (e.field ==\'YUNJ\'||e.field ==\'YUNFSL\'||e.field ==\'YUNFSLFS\'){");
            sb.append(" if(e.record.get(\'YUNFSLFS\')==\'��ֵ˰\')  ");
            sb.append("{e.record.set(\'YUNJS\', Round(parseFloat(e.record.get(\'YUNJ\')==\'\'?0:e.record.get(\'YUNJ\'))- (parseFloat(e.record.get(\'YUNJ\')==\'\'?0:e.record.get(\'YUNJ\'))/(1+e.record.get(\'YUNFSL\')/100)),2))}");
            sb.append("else{e.record.set(\'YUNJS\',Round( parseFloat(e.record.get(\'YUNJ\')==\'\'&&e.record.get(\'YUNFSL\')==0?0:e.record.get(\'YUNJ\'))*(e.record.get(\'YUNFSL\')/100),2))}}");
            sb.append("if (e.field ==\'DAOZZF\'||e.field ==\'DAOZZFSL\'||e.field ==\'DAOZZFSLFS\'){");
            sb.append(" if(e.record.get(\'DAOZZFSLFS\')==\'��ֵ˰\')  ");
            sb.append("{e.record.set(\'DAOZZFS\', Round(parseFloat(e.record.get(\'DAOZZF\')==\'\'?0:e.record.get(\'DAOZZF\'))- (parseFloat(e.record.get(\'DAOZZF\')==\'\'?0:e.record.get(\'DAOZZF\'))/(1+e.record.get(\'DAOZZFSL\')/100)),2))}");
            sb.append("else{e.record.set(\'DAOZZFS\',Round( parseFloat(e.record.get(\'DAOZZF\')==\'\'&&e.record.get(\'DAOZZFSL\')==0?0:e.record.get(\'DAOZZF\'))*(e.record.get(\'DAOZZFSL\')/100),2))}}");
            sb.append("if (e.field ==\'ZAF\'||e.field ==\'ZAFSL\'||e.field ==\'ZAFSLFS\'){");
            sb.append(" if(e.record.get(\'ZAFSLFS\')==\'��ֵ˰\')  ");
            sb.append("{e.record.set(\'ZAFS\', Round(parseFloat(e.record.get(\'ZAF\')==\'\'?0:e.record.get(\'ZAF\'))- (parseFloat(e.record.get(\'ZAF\')==\'\'?0:e.record.get(\'ZAF\'))/(1+e.record.get(\'ZAFSL\')/100)),2))}");
            sb.append("else{e.record.set(\'ZAFS\',Round( parseFloat(e.record.get(\'ZAF\')==\'\'&&e.record.get(\'ZAFSL\')==0?0:e.record.get(\'ZAF\'))*(e.record.get(\'ZAFSL\')/100),2))}}");
            sb.append("if (e.field ==\'QIT\'||e.field ==\'QITSL\'||e.field ==\'QITSLFS\'){");
            sb.append(" if(e.record.get(\'QITSLFS\')==\'��ֵ˰\')  ");
            sb.append("{e.record.set(\'QITS\', Round(parseFloat(e.record.get(\'QIT\')==\'\'?0:e.record.get(\'QIT\'))- (parseFloat(e.record.get(\'QIT\')==\'\'?0:e.record.get(\'QIT\'))/(1+e.record.get(\'QITSL\')/100)),2))}");
            sb.append("else{e.record.set(\'QITS\',Round( parseFloat(e.record.get(\'QIT\')==\'\'&&e.record.get(\'QITSL\')==0?0:e.record.get(\'QIT\'))*(e.record.get(\'QITSL\')/100),2))}}");
            sb.append("if (e.field ==\'JIAOHQZF\'||e.field ==\'JIAOHQZFSL\'||e.field ==\'JIAOHQZFSLFS\'){");
            sb.append(" if(e.record.get(\'JIAOHQZFSLFS\')==\'��ֵ˰\')  ");
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
                this.setMsg("û��������������Ϣ,����ȥ������ά����������ά��ҳ����д������������Ϣ!");
            }

            this.setExtGrid(egu);
            con.Close();
            return;
        }
    }

    public String getBtnHandlerScript(String btnName) {
//		��ť��script
        StringBuffer btnsb = new StringBuffer();
        String cnDate = getNianfValue() + "��" + getYuefValue() + "��";
        btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
        if (btnName.endsWith("CreateButton")) {
            btnsb.append("���������ݽ�����")
                    .append(cnDate).append("���Ѵ����ݣ��Ƿ������");
        } else {
            btnsb.append("�Ƿ�ɾ��").append(cnDate).append("�����ݣ�");
        }
        btnsb.append("',function(btn){if(btn == 'yes'){")
                .append("document.getElementById('").append(btnName).append("').click()")
//		-------------------------------------------------------------------
                .append(";Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...',")
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
            // �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
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

    //	 ���
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

    // �·�
    public boolean Changeyuef = false;

    private static IPropertySelectionModel _YuefModel;

    public IPropertySelectionModel getYuefModel() {
        if (_YuefModel == null) {
            getYuefModels();
        }
        return _YuefModel;
    }

    private IDropDownBean _YuefValue;
//-----------------------�޸ĺ��·���ʾ�ϸ���-----------------------------------

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
        // listYuef.add(new IDropDownBean(-1,"��ѡ��"));
        for (int i = 1; i < 13; i++) {
            listYuef.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _YuefModel = new IDropDownModel(listYuef);
        return _YuefModel;
    }

    public void setYuefModel(IPropertySelectionModel _value) {
        _YuefModel = _value;
    }


    //	 �õ���½�û����ڵ糧���߷ֹ�˾������
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
            // TODO �Զ����� catch ��
            e.printStackTrace();
        } finally {
            cn.Close();
        }

        return diancmc;

    }

    //�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
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
            // TODO �Զ����� catch ��
            e.printStackTrace();
        } finally {
            cn.Close();
        }

        return IDropDownDiancmc;

    }

    //�õ��糧��Ĭ�ϵ�վ
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


    //	�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
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