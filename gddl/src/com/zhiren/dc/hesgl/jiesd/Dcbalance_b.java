package com.zhiren.dc.hesgl.jiesd;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.DateUtil;
import com.zhiren.common.Money;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.Liuc;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.dc.chengbgl.Chengbcl;
import java.text.DecimalFormat;
/**
 * @author ���پ�
 * 2009-10-20
 * ������������б���ķ�������jiesrcrl,jiesrl������Ķ��� ��ֵ
 * 		��������ָ����еķ����������Ҳ�� ��ֵ
 * */
/*
 * ���ߣ����
 * ʱ�䣺2012-05-30
 * ����������gongysb_id,meikxxb_id�ֶ�
 * 		 �ڱ���ʱ��ʹ�� ���� MainGlobal.getTableId("gongysb", "quanc",((Dcbalancebean) getEditValues().get(0)).getFahdw())�ķ������gongysb_id��meikxxb_id
 */


/**
 * ���ߣ�������
 * ʱ�䣺2012-08-14
 * ���������Ӳ������ƹ�������ָ���Ȩ���˺����ο����Լ���װú���⡣
 */
/**
 * ���ߣ���ʤ��
 * ʱ�䣺2013-08-6
 * ������chh 2010-11-06
 * 		 ǰ��Լ�����˺�����jiesyf���������danjcpb��jiesyfb_id����󣬵���ִ��
 * 		 ���˷ѽ���������������Ƿ��ж�Ӧ��diancjsmkb_id(���˷ѽ����Ӧ��jiesb��id) ��û��ʱ����Ϊ0
 * 		 ������jiesyfb��֤ú�����˷ѵ�����ȷ��Ӧ��ϵ
 *      public boolean UpdateDiancjsmkb_id(JDBCcon cn ,long jiesyfb_id){
 *      ����save���������� ������Ӧ�ж�	UpdateDiancjsmkb_id(con,Yfid);
 */


public class Dcbalance_b extends BasePage {
    // List1
    // dropdwon1,2

    private static int _editTableRow = -1;// �༭����ѡ�е���

    public int getEditTableRow() {
        return _editTableRow;
    }

    public void setEditTableRow(int _value) {
        _editTableRow = _value;
    }

    //	�����Ƿ�������㵥��_begin
    public String getShifcsdj() {

        if(((Visit) getPage().getVisit()).getString16().equals("")){

            ((Visit) getPage().getVisit()).setString16(MainGlobal.getXitxx_item("����", Locale.bukyqjksfcsdj_xitxx,
                    String.valueOf(((Visit) getPage().getVisit()).getLong1()), "��"));
        }
        return ((Visit) getPage().getVisit()).getString16();
    }

    public void setShifcsdj(String _value) {
        ((Visit) getPage().getVisit()).setString16(_value);
    }

    //	ú�˰���۱���С��λ
    public int getMeikhsdjblxsw(){

        return ((Visit) getPage().getVisit()).getInt1();
    }

    public void setMeikhsdjblxsw(int value){

        ((Visit) getPage().getVisit()).setInt1(value);
    }

    //	�˷Ѻ�˰���۱���С��λ
    public int getYunfhsdjblxsw(){

        return ((Visit) getPage().getVisit()).getInt2();
    }

    public void setYunfhsdjblxsw(int value){

        ((Visit) getPage().getVisit()).setInt2(value);
    }

//	�����Ƿ�������㵥��_end

    private String _msg;

    public String _liucmc;

    protected void initialize() {
        _msg = "";
        _liucmc = "";
    }

    public void setMsg(String _value) {
        _msg = MainGlobal.getExtMessageBox(_value, false);
    }

    public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }

    public void setLiucmc(String _value) {
        _liucmc = _value;
    }

    public String getLiucmc() {
        if (_liucmc == null) {
            _liucmc = "";
        }
        return _liucmc;
    }

    public String getTitle() {

        return Locale.jiesd_title;
    }

    public String getJiesslcyText() {

        return Locale.jiesslcy_title;
    }

    private String getDiancjsbs(long diancxxb_id) {

        JDBCcon con = new JDBCcon();
        String sql = "", diancjsbs = "";

        try {

            sql = "select JIESBDCBS from diancxxb where id=" + diancxxb_id;
            ResultSet rs = con.getResultSet(sql);
            if (rs.next()) {

                diancjsbs = rs.getString("JIESBDCBS") + "-";
            }
            rs.close();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
        return diancjsbs;
    }

    private long SaveDiancjsmkb(JDBCcon con, String TableName_mk,String TableName_yf) {
        // �洢ú���
        String sql = "";
        long Id = 0;

        try {

            if (((Dcbalancebean) getEditValues().get(0)).getMeikjsb_id() > 0) {
//				���danpcjsmxb��jiesbid����0˵��danpcjsmxb��������ϢҪ��jiesb�������
//					�ʽ�����idӦȡdanpcjsmxb��jiesbid��
                Id = ((Dcbalancebean) getEditValues().get(0)).getMeikjsb_id();
            } else {

                Id = Long
                        .parseLong(MainGlobal.getNewID(con,((Visit) getPage().getVisit()).getLong1()));
            }

            sql = "insert into "+TableName_mk+" (id, diancxxb_id, bianm, gongysb_id, gongysmc, yunsfsb_id, yunj, yingd, kuid, \n" +
                    "faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, \n" +
                    "zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, yuns, koud, jiesslcy, hansdj, bukmk, hansmk, buhsmk, \n" +
                    "meikje, shuik, shuil, buhsdj, jieslx, jiesrq, ruzrq, hetb_id, liucztb_id, LIUCGZID, beiz, RANLBMJBR, RANLBMJBRQ ,\n" +
                    "jihkjb_id, kouk_js,chongdjsb_id,weicdje,jieslx_dt) "
                    + " values ("
                    + Id
                    + ", "
                    + ((Visit) getPage().getVisit()).getLong1()
                    + ",'"
                    + ((Dcbalancebean) getEditValues().get(0)).getJiesbh()
                    + "',"
                    + MainGlobal
                    .getTableId("gongysb", "quanc",
                            ((Dcbalancebean) getEditValues().get(0))
                                    .getFahdw())
                    + ", '"
                    + ((Dcbalancebean) getEditValues().get(0)).getFahdw()
                    + "',"
                    + ((Dcbalancebean) getEditValues().get(0)).getYunsfsb_id()
                    + ","
                    + ((Dcbalancebean) getEditValues().get(0)).getYunju()
                    + ","
                    + ((Dcbalancebean) getEditValues().get(0)).getYingd()
                    + ","
                    + ((Dcbalancebean) getEditValues().get(0)).getKuid()
                    + ",'"
                    + ((Dcbalancebean) getEditValues().get(0)).getFaz()
                    + "', to_date('"
                    + this.FormatDate(((Dcbalancebean) getEditValues().get(0))
                    .getFahksrq())
                    + "','yyyy-MM-dd'),to_date('"
                    + this.FormatDate(((Dcbalancebean) getEditValues().get(0))
                    .getFahjzrq())
                    + "','yyyy-MM-dd'),'"
                    + ((Dcbalancebean) getEditValues().get(0)).getPinz()
                    + "', '"
                    + ((Dcbalancebean) getEditValues().get(0)).getDaibcc()
                    + "', '"
                    + ((Dcbalancebean) getEditValues().get(0)).getYuanshr()
                    + "', '"
                    + ((Dcbalancebean) getEditValues().get(0)).getXianshr()
                    + "', to_date('"
                    + this.FormatDate(((Dcbalancebean) getEditValues().get(0))
                    .getYansksrq())
                    + "','yyyy-MM-dd'),to_date('"
                    + this.FormatDate(((Dcbalancebean) getEditValues().get(0))
                    .getYansjzrq())
                    + "','yyyy-MM-dd'),'"
                    + ((Dcbalancebean) getEditValues().get(0)).getYansbh()
                    + "', '"
                    + ((Dcbalancebean) getEditValues().get(0)).getShoukdw()
                    + "', '"
                    + ((Dcbalancebean) getEditValues().get(0)).getKaihyh()
                    + "', '"
                    + ((Dcbalancebean) getEditValues().get(0)).getZhangh()
                    + "', '"
                    + ((Dcbalancebean) getEditValues().get(0)).getFapbh()
                    + "', '"
                    + ((Dcbalancebean) getEditValues().get(0)).getFukfs()
                    + "', '"
                    + ((Dcbalancebean) getEditValues().get(0)).getDuifdd()
                    + "',"
                    + ((Dcbalancebean) getEditValues().get(0)).getChes()
                    + ", "
                    + ((Dcbalancebean) getEditValues().get(0)).getJiessl()
                    + ", "
                    + ((Dcbalancebean) getEditValues().get(0)).getJingz()
                    + ", "
                    + ((Dcbalancebean) getEditValues().get(0)).getYuns()
                    + ", "
                    + ((Dcbalancebean) getEditValues().get(0)).getKoud_js()
                    + ", "
                    + ((Dcbalancebean) getEditValues().get(0)).getJiesslcy()
                    + ", "
                    + ((Dcbalancebean) getEditValues().get(0)).getShulzjbz()
                    + ", "
                    + ((Dcbalancebean) getEditValues().get(0)).getBukyqjk()
                    + ", "
                    + ((Dcbalancebean) getEditValues().get(0)).getJiasje()
                    + ", "
                    + ((Dcbalancebean) getEditValues().get(0)).getJiakhj()
                    + ", "
                    + ((Dcbalancebean) getEditValues().get(0)).getJiakje()
                    + ","
                    + ((Dcbalancebean) getEditValues().get(0)).getJiaksk()
                    + ","
                    + ((Dcbalancebean) getEditValues().get(0)).getJiaksl()
                    + ", "
                    + ((Dcbalancebean) getEditValues().get(0)).getBuhsdj()
                    + ", "
                    + ((Visit) getPage().getVisit()).getLong2()
                    + ", to_date('"
                    + this.FormatDate(((Dcbalancebean) getEditValues().get(0))
                    .getJiesrq())
                    + "','yyyy-MM-dd'),null,"
                    + ((Dcbalancebean) getEditValues().get(0)).getHetb_id()
                    + ",0,0,'"
                    + ((Dcbalancebean) getEditValues().get(0)).getBeiz()
                    + "','"
                    + ((Dcbalancebean) getEditValues().get(0)).getRanlbmjbr()
                    + "',to_date('"
                    + this.FormatDate(((Dcbalancebean) getEditValues().get(0))
                    .getRanlbmjbrq()) + "','yyyy-MM-dd'),"
                    + ((Dcbalancebean) getEditValues().get(0)).getJihkjb_id()
                    + ", "
                    + ((Dcbalancebean) getEditValues().get(0)).getKouk_js()
                    + ", "
                    + ((Dcbalancebean) getEditValues().get(0)).getChongdjsb_id()
                    + ", "
                    + ((Dcbalancebean) getEditValues().get(0)).getweicdje()
                    + ", "
                    + ((Dcbalancebean) getEditValues().get(0)).getjieslx_dt()
                    + ")";

            if(MainGlobal.getXitxx_item("����", "���������ǽ�������", String.valueOf(((Visit)getPage().getVisit()).getDiancxxb_id()), "��").equals("��")){
                sql = "begin\n "+ sql +";\n" +
                        "update " + TableName_mk + " set ruzrq = to_date('" +
                        this.FormatDate(((Dcbalancebean) getEditValues().get(0)).getJiesrq()) +
                        "','yyyy-mm-dd') where id=" + Id + ";\n end;\n";
            }

            if(MainGlobal.getXitxx_item("����", "��ͬ�����������⴦��", 300, "��").equals("��")){
                //��ͬ���㵥��ִ���,��¼��ֺ�ʣ����
                sql = "begin\n "+ sql +";\n" +
                        "update " + TableName_mk + " set weicdje=weicdje-"+((Dcbalancebean) getEditValues().get(0)).getweicdje()+" where id=" + ((Dcbalancebean) getEditValues().get(0)).getChongdjsb_id() + ";\n end;\n";
            }


            if (con.getInsert(sql) >= 0) {

                if (UpdateJiesb(Id,con,TableName_mk)) {

                    ((Dcbalancebean) getEditValues().get(0)).setId(Id);

                    if (UpdateFahb_Jiesbid(Id,con)) {
                        // ���·�������jiesb_id

                        //������������Ҫ����baozmxxb
                        if(MainGlobal.getXitxx_item("����", "������װú", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "��").equals("��")){
                            if(UpdateBaozmxxb_Jiesbid(Id, con));
                        }

                        if(UpdateJiesyfb_Diancjsmkbid(Id,con,TableName_yf)){
//							����Ƚ������˷ѣ��ٽ���ú����Ҫ��jiesyfb���й���
                            return Id;
                        }
                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return 0;
    }

    private  boolean UpdateJiesyfb_Diancjsmkbid(long meikjsb_id,
                                                JDBCcon con,String TableName_yf){
//		����Ƚ������˷ѣ��ٽ���ú����Ҫ��jiesyfb���й���
        boolean Flag=false;
        Jiesdcz Jscz = new Jiesdcz();
        String where = " diancjsmkb_id";
        if(TableName_yf.equals("kuangfjsyfb")){
//			�����˷ѱ�
            where = " kuangfjsmkb_id";
        }

        String sql="update "+TableName_yf+" set "+where+"="+meikjsb_id+" where id in("+
                "select distinct dj.yunfjsb_id\n" +
                "       from fahb f,chepb c,danjcpb dj,yunfdjb yd\n" +
                "       where f.id=c.fahb_id\n" +
                "             and c.id=dj.chepb_id\n" +
                "             and dj.yunfdjb_id=yd.id\n" +
                "             and yd.feiylbb_id="+Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
                .getLong2())+"\n" +
                "             and f.jiesb_id in ("+meikjsb_id+"))";

        if(con.getUpdate(sql)>=0){

            Flag=true;
        }
        return Flag;
    }

    private boolean UpdateJiesb(long Jiesb_Id,JDBCcon con,String TableName) {

        String sql;
        try {
            sql = "update "+TableName+" set jiesrl="
                    + MainGlobal.Mjkg_to_kcalkg(
                    ((Dcbalancezbbean) getJieszbValues()
                            .get(0))
                            .getQnetar_js(), 0 ,
                    ((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs())
                    + "," + " jieslf="
                    + ((Dcbalancezbbean) getJieszbValues().get(0)).getStd_js()
                    + "," + " jiesrcrl="
                    + ((Dcbalancezbbean) getJieszbValues().get(0)).getQnetar_cf()
                    + "," + " meikxxb_id="
                    + MainGlobal.getTableId("meikxxb", "quanc", ((Dcbalancebean) getEditValues().get(0)).getMeikdw())
                    + ","+ " meikdwmc='"
                    + ((Dcbalancebean) getEditValues().get(0)).getMeikdw()
                    + "',hetj="
                    + ((Dcbalancebean) getEditValues().get(0)).getHetjg()
                    + ",fengsjj="
                    +((Dcbalancebean) getEditValues().get(0)).getFengsjj()
                    +",jiajqdj="
                    +((Dcbalancebean) getEditValues().get(0)).getJiajqdj()
                    +",jijlx="
                    +((Dcbalancebean) getEditValues().get(0)).getJijlx()
                    +",Yunfhsdj="
                    +((Dcbalancebean) getEditValues().get(0)).getYunfjsdj_mk()
                    +",hansyf="
                    +(((Dcbalancebean) getEditValues().get(0)).getYunzfhj()==0?
                    ((Dcbalancebean) getEditValues().get(0)).getYunzfhj_mk():
                    ((Dcbalancebean) getEditValues().get(0)).getYunzfhj())
                    +",Buhsyf="
                    +(((Dcbalancebean) getEditValues().get(0)).getBuhsyf()==0?
                    ((Dcbalancebean) getEditValues().get(0)).getBuhsyf_mk():
                    ((Dcbalancebean) getEditValues().get(0)).getBuhsyf())
                    +",yunfjsl="
                    +((Dcbalancebean) getEditValues().get(0)).getYunfjsl_mk();

            if(((Dcbalancebean) getEditValues().get(0)).getJieslx()==Locale.meikjs_feiylbb_id){

                sql+= ",kuidjfyf="
                        +((Dcbalancebean) getEditValues().get(0)).getKuidjfyf()
                        + ",kuidjfzf="
                        +((Dcbalancebean) getEditValues().get(0)).getKuidjfzf();
            }

            if(((Dcbalancebean) getEditValues().get(0)).getChaodOrKuid().equals("CD")){
//				����"����"
                sql+=",chaokdl="+((Dcbalancebean) getEditValues().get(0)).getChaokdl()
                        +", chaokdlx='"+((Dcbalancebean) getEditValues().get(0)).getChaodOrKuid()+"'";
            }else if(((Dcbalancebean) getEditValues().get(0)).getChaodOrKuid().equals("KD")){
//				����"����"
                sql+=",chaokdl="+(-((Dcbalancebean) getEditValues().get(0)).getChaokdl())
                        +", chaokdlx='"+((Dcbalancebean) getEditValues().get(0)).getChaodOrKuid()+"'";
            }

            sql+=" where id=" + Jiesb_Id;
            if (con.getUpdate(sql) >= 0) {

                return true;
            }

        } catch (Exception e) {
            // TODO �Զ����� catch ��
            e.printStackTrace();
        }

        return false;
    }

    private boolean UpdateFahb_Jiesbid(long JiesbId,JDBCcon con) {
        // ���·�����jiesb_id
        boolean Flag = false;
        if(((Visit) getPage().getVisit()).getString13().equals("")||((Visit) getPage().getVisit()).getString13()==null){
            //(Visit) getPage().getVisit()).getString13()���Ϊ��ʱ,���¸��·�����ʧ��,��ͬ�����������,���Ե�Ϊ��ʱ,��GetString13()
            //���½��и�ֵ.��jiesxz.java�ı��淽����,��getString21Ҳ��¼�˷������id
            ((Visit) getPage().getVisit()).setString13(((Visit) getPage().getVisit()).getString21());
        }


        String sql = "update fahb set jiesb_id=" + JiesbId
                + " where id in ("
                + ((Visit) getPage().getVisit()).getString13() + ") \n"
                + "and id not in(select id from fahb where jiesb_id in("
                + MainGlobal.getProperId(((Visit) getPage().getVisit()).getProSelectionModel8(),((Visit) getPage().getVisit()).getString20())
                +"))";

        if (con.getUpdate(sql) >= 0) {

            Flag = true;
        }
        return Flag;
    }

    /*
     * ����baozmxxb��jiesb_id
     */
    private boolean UpdateBaozmxxb_Jiesbid(long JiesbId,JDBCcon con) {
        // ���·�����jiesb_id
        boolean Flag = false;
        String sql =  "update baozmxxb set jiesb_id=" + JiesbId	+ " where id in ( \n"+
                "select bzm.ID from (select gongysb_id ,max(fahrq) maxFahrq,min(fahrq) minFahrq\n" +
                "from fahb where id in ("+ ((Visit) getPage().getVisit()).getString13()+") group by gongysb_id ) fh ,baozmxxb bzm\n" +
                "where fh.GONGYSB_ID = bzm.GONGYSB_ID and fh.maxFahrq >= bzm.ERIQ and fh.minFahrq <= bzm.BRIQ and bzm.JIESB_ID = 0 )";

        if (con.getUpdate(sql) >= 0) {

            Flag = true;
        }
        return Flag;
    }

    private boolean UpdateDanjcpb_Jiesyfbid(long JiesyfbId, JDBCcon con) {
        // ���ݳ�Ƥ��yunfjsb_id
        // ����ڽ���ѡ��ҳ����ѡ����Ҫ�˶Ի�Ʊ��ʱ˵����yunfdjb��danjcpb���Ѿ��м�¼
        // ����ڽ���ѡ��ҳ����ѡ�񡰲���Ҫ�˶Ի�Ʊ��ʱ˵����yunfdjb��danjcpb�ж�û�м�¼��Ҫ�����¼�¼
        boolean Flag = false;
        boolean Hedbz = false; // true �Ѻ˶ԡ�false δ�˶�
        Jiesdcz Jscz = new Jiesdcz();
        String yunsdw_contion="";
        String strChepb="chepb";
        String strFinalArray[]=null;	//�����жϲ����Ƿ���Ҫ���в�ִ���
        String where_fahb_continue="";		//�������������
        String where_chep_continue="";		//��Ƥ���������
        String where_chep_daozcpb_continue="";	//��װ��Ƥ��������
        String sql ="";

        int IntRow_num=0;

        if(((Visit) getPage().getVisit()).getLong9()>-1){

            if(((Visit) getPage().getVisit()).getLong2()==Locale.daozyf_feiylbb_id){
//				��װ�˷� 
                yunsdw_contion=" and c.yunsdw='"+((Visit) getPage().getVisit()).getString10()+"'";
            }else{

                yunsdw_contion=" and c.yunsdwb_id="+((Visit) getPage().getVisit()).getLong9();
            }
        }

        if(((Visit) getPage().getVisit()).getLong2()==Locale.daozyf_feiylbb_id){
//			��װ�˷� 

            strChepb="daozcpb";
        }

        try {

            strFinalArray = Jiesdcz.getFenzzfc(
                    ((Visit) getPage().getVisit()).getString13(), ",", 1000);	//�õ������ַ���

            StringBuffer sbcpid = new StringBuffer();

            if(strFinalArray!=null){
//				˵���з���
                where_fahb_continue="";
                for(int i=0;i<strFinalArray.length;i++){

                    if(i==0){

                        where_fahb_continue="f.id in ("+strFinalArray[i]+")";
                    }else{

                        where_fahb_continue+=" or f.id in ("+strFinalArray[i]+")";
                    }
                }

                sql = "select c.id from fahb f,"+strChepb+" c		\n"
                        + " where f.id=c.fahb_id and ("+where_fahb_continue+")	\n"
                        + yunsdw_contion;

            }else{

                sql = "select c.id from fahb f,"+strChepb+" c		\n"
                        + " where f.id=c.fahb_id and f.id in ("
                        + ((Visit) getPage().getVisit()).getString13() + ")	\n"
                        + yunsdw_contion;
            }

            ResultSetList rsl = con.getResultSetList(sql);
            IntRow_num = rsl.getRows();
            while (rsl.next()) {

                sbcpid.append(rsl.getLong("id")).append(",");
            }
            sbcpid.deleteCharAt(sbcpid.length() - 1);

            // �ж�yunfdjb��danjcpb���Ƿ���ڼ�¼���������Hedbz=trueֱ�Ӹ��£����������Hedbz=false;�Ƚ����ݲ�������ٸ���״̬

            strFinalArray = null;
            strFinalArray = Jiesdcz.getFenzzfc(
                    sbcpid.toString(), ",", 1000);	//�õ������ַ���


            if(strFinalArray!=null){
//				˵���з���
                where_chep_continue="";
                where_chep_daozcpb_continue="";
                for(int i=0;i<strFinalArray.length;i++){

                    if(i==0){

                        where_chep_continue=" danjcpb.chepb_id in ("+strFinalArray[i]+")";
                        where_chep_daozcpb_continue=" id in ("+strFinalArray[i]+")";
                    }else{

                        where_chep_continue+=" or danjcpb.chepb_id in ("+strFinalArray[i]+")";
                        where_chep_daozcpb_continue+=" or id in ("+strFinalArray[i]+")";
                    }
                }

                sql = "select yunfdjb.id from yunfdjb,danjcpb where danjcpb.yunfdjb_id=yunfdjb.id 	\n"
                        + " and ("
                        + where_chep_continue
                        + ") and yunfdjb.feiylbb_id="
                        + Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
                        .getLong2()) + "";

            }else{

                sql = "select yunfdjb.id from yunfdjb,danjcpb where danjcpb.yunfdjb_id=yunfdjb.id 	\n"
                        + " and danjcpb.chepb_id in ("
                        + sbcpid.toString()
                        + ") and yunfdjb.feiylbb_id="
                        + Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
                        .getLong2()) + "";
            }

            rsl = con.getResultSetList(sql);

            if(IntRow_num!=rsl.getRows()){
//				˵����һ����û�н���danjcpb��yunfdjb�Ĳ������

                if(!where_chep_continue.equals("")
                        ||!where_fahb_continue.equals("")
                        ){

                    if(!where_fahb_continue.equals("")&&!where_chep_continue.equals("")){

                        sql="select c.id from fahb f,"+strChepb+" c		\n"
                                + " where f.id=c.fahb_id and ("+where_fahb_continue+")	\n"
                                + yunsdw_contion
                                + " minus	\n"
                                + "select danjcpb.chepb_id as id from yunfdjb,danjcpb where danjcpb.yunfdjb_id=yunfdjb.id 	\n"
                                + " and ("
                                + where_chep_continue
                                + ") and yunfdjb.feiylbb_id="
                                + Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
                                .getLong2());
                    }else if(!where_fahb_continue.equals("")){

                        sql="select c.id from fahb f,"+strChepb+" c		\n"
                                + " where f.id=c.fahb_id and ("+where_fahb_continue+")	\n"
                                + yunsdw_contion
                                + " minus	\n"
                                + "select danjcpb.chepb_id as id from yunfdjb,danjcpb where danjcpb.yunfdjb_id=yunfdjb.id 	\n"
                                + " and danjcpb.chepb_id in ("
                                + sbcpid.toString()
                                + ") and yunfdjb.feiylbb_id="
                                + Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
                                .getLong2());

                    }else if(!where_chep_continue.equals("")){

                        sql="select c.id from fahb f,"+strChepb+" c		\n"
                                + " where f.id=c.fahb_id and f.id in ("
                                +((Visit) getPage().getVisit()).getString13()+")	\n"
                                + yunsdw_contion
                                + " minus	\n"
                                + "select danjcpb.chepb_id as id from yunfdjb,danjcpb where danjcpb.yunfdjb_id=yunfdjb.id 	\n"
                                + " and ("
                                + where_chep_continue
                                + ") and yunfdjb.feiylbb_id="
                                + Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
                                .getLong2());
                    }
                }else{

                    sql="select c.id from fahb f,"+strChepb+" c		\n"
                            + " where f.id=c.fahb_id and f.id in ("
                            + ((Visit) getPage().getVisit()).getString13() + ")	\n"
                            + yunsdw_contion
                            + " minus	\n"
                            + "select danjcpb.chepb_id as id from yunfdjb,danjcpb where danjcpb.yunfdjb_id=yunfdjb.id 	\n"
                            + " and danjcpb.chepb_id in ("
                            + sbcpid.toString()
                            + ") and yunfdjb.feiylbb_id="
                            + Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
                            .getLong2());
                }

                rsl=con.getResultSetList(sql);

                if(rsl.getRows()>0){

                    StringBuffer sb = new StringBuffer("begin		\n");
                    String Yunfdjb_id = "0";
                    int i=0;
                    while(rsl.next()){
                        i++;

                        Yunfdjb_id = MainGlobal.getNewID(con,((Visit) getPage()
                                .getVisit()).getLong1());
                        sb
                                .append(
                                        "insert into yunfdjb(id, danjbh, feiyb_id, biaoz, zongje, caozy, caozsj, beiz, feiylbb_id, ches)	\n")
                                .append(" values		\n")
                                .append(
                                        " ("
                                                + Yunfdjb_id
                                                + ", '', 0, 0, 0, '"
                                                + ((Visit) getPage().getVisit())
                                                .getRenymc()
                                                + "', sysdate, ")
                                .append(
                                        "'����˶Ի�Ʊ�����˷ѽ���', "
                                                + Jscz
                                                .Feiylbb_transition(((Visit) getPage()
                                                        .getVisit())
                                                        .getLong2())
                                                + ", 1);	\n");

                        sb
                                .append(
                                        "insert into danjcpb(yunfdjb_id, chepb_id, yunfjsb_id, yansbhb_id, jifzl, id)		\n")
                                .append(" values		\n").append(
                                "("
                                        + Yunfdjb_id
                                        + ", "
                                        + rsl.getString("id")
                                        + ", 0, 0, 0, getnewid("
                                        + ((Visit) getPage().getVisit())
                                        .getLong1() + "));	\n");

                        if(i>=1000&&i%1000==0){
                            sb.append(" end;\n");

                            if(con.getUpdate(sb.toString())<0){

                                Flag = false;
                                return Flag;
                            }else{

                                sb.setLength(0);
                                sb.append("begin \n");
                            }

                        }

                    }
                    sb.append("end;");
                    if(sb.length()>13){

                        if(con.getInsert(sb.toString())<0){

                            Flag = false;
                            return Flag;
                        }
                    }
                }
            }

            rsl.close();

            String Feiylb_condition="";	//Ϊ�˴�����Ʊ����ʱ��
//									�������˷ѺͿ����˷�һ��˶��˵�����£��ڸ���danjcpb��
//									yunfjsb_idʱ�޶���yunfdjb��feiylbb_idΪ�����˷ѵ�bug��
//									�����˸ñ��������((Visit) getPage().getVisit()).getLong2()Ϊ��Ʊ���㣨1����
//									���������ֵΪ �����˷� or �����˷�

            if(((Visit) getPage().getVisit())
                    .getLong2()==Locale.liangpjs_feiylbb_id){

                Feiylb_condition=Locale.guotyf_feiylbb_id+","+Locale.kuangqyf_feiylbb_id;
            }else{

                Feiylb_condition=String.valueOf(Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
                        .getLong2()));
            }

            String chepb_ids = sbcpid.toString();
            if (MainGlobal.getXitxx_item("����", "�Ƿ���ʾ����ϵͳ������", String.valueOf(((Visit)getPage().getVisit()).getDiancxxb_id()), "��").equals("��")) {
//				˵����ɽ�����ǵ糧���˷ѽ������⣬A����ϵͳ��B����ϵͳ���˷���Ҫ�ֿ����㡣
                chepb_ids = "select cp.id from chepb cp where cp.zhongchh = '"+ ((Visit)getPage().getVisit()).getString19() +"' and cp.id in ("+ sbcpid.toString() +")";
            }

            if(!where_chep_continue.equals("")){

                sql = "update danjcpb set yunfjsb_id="
                        + JiesyfbId
                        + " where ("+where_chep_continue+") \n"
                        + " and danjcpb.yunfdjb_id in	\n"
                        + " (select yunfdjb.id from yunfdjb,danjcpb where danjcpb.yunfdjb_id=yunfdjb.id	\n"
                        + "	and ("+where_chep_continue+") and yunfdjb.feiylbb_id in ("
                        + Feiylb_condition + ")) ";

            }else{

                sql = "update danjcpb set yunfjsb_id="
                        + JiesyfbId
                        + " where chepb_id in ("
                        + chepb_ids
                        + ") 	\n"
                        + " and danjcpb.yunfdjb_id in	\n"
                        + " (select yunfdjb.id from yunfdjb,danjcpb where danjcpb.yunfdjb_id=yunfdjb.id	\n"
                        + "	and danjcpb.chepb_id in ("
                        + chepb_ids
                        + ") and yunfdjb.feiylbb_id in ("
                        + Feiylb_condition + ")) ";
            }

            if (con.getUpdate(sql) > 0) {

                if(((Visit) getPage().getVisit()).getLong2()==Locale.daozyf_feiylbb_id){
//					��װ�˷� 

                    if(!where_chep_daozcpb_continue.equals("")){

                        sql = "update daozcpb set jiesb_id="+JiesyfbId+" where ("+where_chep_daozcpb_continue+")";
                    }else{

                        sql = "update daozcpb set jiesb_id="+JiesyfbId+" where id in ("+sbcpid.toString()+")";
                    }


                    if(con.getUpdate(sql)>0){

                        Flag = true;
                    }
                }else{

                    Flag = true;
                }

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return Flag;
    }

    private long SaveDiancjsyfb(long Meikid,JDBCcon con,String TableName) {
        // �洢�˷ѱ�
        String sql = "";
        long Id = 0;
        try {

            String guanlb_id = "diancjsmkb_id";	//����������

            if(TableName.equals("kuangfjsyfb")){

                guanlb_id = "kuangfjsmkb_id";
            }

            if (((Dcbalancebean) getEditValues().get(0)).getYunfjsb_id() > 0) {

                Id = ((Dcbalancebean) getEditValues().get(0)).getYunfjsb_id();
            } else {

                Id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                        .getVisit()).getLong1()));
            }

            String xiecf = String.valueOf(((Visit)getPage().getVisit()).getDouble17());
            if (xiecf.equals("0.0")) {
                xiecf = "null";
            }

            sql = " insert into "+TableName+" (id,diancxxb_id, bianm, gongysb_id, gongysmc, meikxxb_id, "
                    + " meikdwmc, yunsfsb_id, yunj, yingd, kuid, faz, fahksrq, fahjzrq, meiz, daibch, "
                    + " yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, "
                    + " fukfs, duifdd, ches, gongfsl, yanssl, jiessl, yingk, guohl, yuns, koud, jiesslcy, "
                    + " guotyf, guotzf, kuangqyf, kuangqzf, jiskc, hansdj, bukyf, hansyf, buhsyf, shuik, "
                    + " shuil, buhsdj, jieslx, jiesrq, ruzrq, hetb_id, liucztb_id, LIUCGZID, beiz, "
                    + " "+guanlb_id+", RANLBMJBR, RANLBMJBRQ, kuidjfyf,kuidjfzf, zhongchh, xiecf)"
                    + " values("
                    + Id
                    + ", "
                    + ((Visit) getPage().getVisit()).getLong1()
                    + ", '"
                    + ((Dcbalancebean) getEditValues().get(0)).getJiesbh()
                    + "', "
                    + MainGlobal
                    .getTableId("gongysb", "quanc",
                            ((Dcbalancebean) getEditValues().get(0))
                                    .getFahdw())
                    + ", '"
                    + ((Dcbalancebean) getEditValues().get(0)).getFahdw()
                    + "',"
                    + MainGlobal
                    .getTableId("meikxxb", "quanc",
                            ((Dcbalancebean) getEditValues().get(0))
                                    .getMeikdw())
                    + ",'"
                    + ((Dcbalancebean) getEditValues().get(0)).getMeikdw()
                    + "',"
                    + ((Dcbalancebean) getEditValues().get(0)).getYunsfsb_id()
                    + ",'"
                    + ((Dcbalancebean) getEditValues().get(0)).getYunju()
                    + "',"
                    + ((Dcbalancebean) getEditValues().get(0)).getYingd()
                    + ","
                    + ((Dcbalancebean) getEditValues().get(0)).getKuid()
                    + ",'"
                    + ((Dcbalancebean) getEditValues().get(0)).getFaz()
                    + "',to_date('"
                    + this.FormatDate(((Dcbalancebean) getEditValues().get(0))
                    .getFahksrq())
                    + "','yyyy-MM-dd'),to_date('"
                    + this.FormatDate(((Dcbalancebean) getEditValues().get(0))
                    .getFahjzrq())
                    + "','yyyy-MM-dd'),'"
                    + ((Dcbalancebean) getEditValues().get(0)).getPinz()
                    + "', '"
                    + ((Dcbalancebean) getEditValues().get(0)).getDaibcc()
                    + "', '"
                    + ((Dcbalancebean) getEditValues().get(0)).getYuanshr()
                    + "', '"
                    + ((Dcbalancebean) getEditValues().get(0)).getXianshr()
                    + "',to_date('"
                    + this.FormatDate(((Dcbalancebean) getEditValues().get(0))
                    .getYansksrq())
                    + "','yyyy-MM-dd'), to_date('"
                    + this.FormatDate(((Dcbalancebean) getEditValues().get(0))
                    .getYansjzrq())
                    + "','yyyy-MM-dd'), '"
                    + ((Dcbalancebean) getEditValues().get(0)).getYansbh()
                    + "', '"
                    + ((Dcbalancebean) getEditValues().get(0)).getShoukdw()
                    + "', '"
                    + ((Dcbalancebean) getEditValues().get(0)).getKaihyh()
                    + "', '"
                    + ((Dcbalancebean) getEditValues().get(0)).getZhangh()
                    + "', '"
                    + ((Dcbalancebean) getEditValues().get(0)).getFapbh()
                    + "', '"
                    + ((Dcbalancebean) getEditValues().get(0)).getFukfs()
                    + "', '"
                    + ((Dcbalancebean) getEditValues().get(0)).getDuifdd()
                    + "', "
                    + ((Dcbalancebean) getEditValues().get(0)).getChes()
                    + ", "
                    + ((Dcbalancebean) getEditValues().get(0)).getGongfsl()
                    + ", "
                    + ((Dcbalancebean) getEditValues().get(0)).getYanssl()
                    + ", "
                    + ((Dcbalancebean) getEditValues().get(0)).getYunfjsl()
                    + ", "
                    + ((Dcbalancebean) getEditValues().get(0)).getYingksl()
                    + ", "
                    + ((Dcbalancebean) getEditValues().get(0)).getJingz()
                    + ","
                    + ((Dcbalancebean) getEditValues().get(0)).getYuns()
                    + ","
                    + ((Dcbalancebean) getEditValues().get(0)).getKoud_js()
                    + ","
                    + ((Dcbalancebean) getEditValues().get(0)).getJiesslcy()
                    + ","
                    + ((Dcbalancebean) getEditValues().get(0)).getTielyf()
                    + ","
                    + ((Dcbalancebean) getEditValues().get(0)).getTielzf()
                    + ","
                    + ((Dcbalancebean) getEditValues().get(0)).getKuangqyf()
                    + ","
                    + ((Dcbalancebean) getEditValues().get(0)).getKuangqzf()
                    + ","
                    + (double) Math
                    .round((((Dcbalancebean) getEditValues().get(0))
                            .getTielzf() + ((Dcbalancebean) getEditValues()
                            .get(0)).getKuangqzf()) * 100)
                    / 100
                    + ","
                    + ((Dcbalancebean) getEditValues().get(0)).getYunfhsdj()
                    + ","
                    + ((Dcbalancebean) getEditValues().get(0)).getBukyqyzf()
                    + ","
                    + ((Dcbalancebean) getEditValues().get(0)).getYunzfhj()
                    + ","
                    + ((Dcbalancebean) getEditValues().get(0)).getBuhsyf()
                    + ","
                    + ((Dcbalancebean) getEditValues().get(0)).getYunfsk()
                    + ", "
                    + ((Dcbalancebean) getEditValues().get(0)).getYunfsl()
                    + ", 0, "
                    + ((Visit) getPage().getVisit()).getLong2()
                    + ", to_date('"
                    + this.FormatDate(((Dcbalancebean) getEditValues().get(0))
                    .getJiesrq())
                    + "','yyyy-MM-dd'), null,"
                    + ((Dcbalancebean) getEditValues().get(0)).getHetb_id()
                    + ", 0, 0, '"
                    + ((Dcbalancebean) getEditValues().get(0)).getBeiz()
                    + "',"
                    + GetMeikjsb_id(Meikid)
                    + ",'"
                    + ((Dcbalancebean) getEditValues().get(0)).getRanlbmjbr()
                    + "',to_date('"
                    + this.FormatDate(((Dcbalancebean) getEditValues().get(0))
                    .getRanlbmjbrq()) + "','yyyy-MM-dd')"
                    + ","
                    + ((Dcbalancebean) getEditValues().get(0)).getKuidjfyf()
                    + ","
                    + ((Dcbalancebean) getEditValues().get(0)).getKuidjfzf()
                    + ", '"+ ((Visit)getPage().getVisit()).getString19() +"', "+ xiecf +")";

            if (con.getInsert(sql) >= 0) {

                ((Dcbalancebean) getEditValues().get(0)).setYid(Id);
                if (UpdateDanjcpb_Jiesyfbid(Id, con)) {

                    return Id;
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return 0;
    }

    private long GetMeikjsb_id(long meikjsb_id) throws SQLException{
//		�����˷�ʱҪ�ͽ������а󶨣�������jiesyfb.diancjsmkb_idΪjiesb.id
//		ԭ������˷ѽ���ķ�������һ��ú����㣬�Ǿ͹�����ú����㡣�������ú����㣬�����й�����
        long Jiesb_id=meikjsb_id;
        if(meikjsb_id>0){
//			�������Ʊ���㣬meikjsb_id>0

        }else{

            JDBCcon con=new JDBCcon();
            String sql="select distinct nvl(jiesb_id,0) as jiesb_id from fahb where id in ("+((Visit) getPage().getVisit()).getString13()+")";
            ResultSetList rsl=con.getResultSetList(sql);
            if(rsl.getRows()>1){
//				˵����һ���˷ѽ����Ӧ���ú�����˴�����ú����й���

            }else if(rsl.getRows()==1){
//				˵���Ƕ�Ӧһ��ú�����
                Jiesb_id=rsl.getLong("jiesb_id");
            }
            rsl.close();
            con.Close();
        }

        return Jiesb_id;
    }

    private boolean UpdateJieszfb(long id, JDBCcon con) {
        // TODO �Զ����ɷ������
        // �����ӷѱ���jiesyfb_id�ֶ�
        // ������jieszfʱ�Ѹôβ�����session�������
        // ����jieszf��ʱ����session��ͬ�ļ�¼
        // JDBCcon con=new JDBCcon();
        String sql = "update jieszfb set jiesyfb_id=" + id
                + " where serversession='"
                + ((Visit) getPage().getVisit()).getSession() + "'";
        if (con.getUpdate(sql) >= 0) {

            return true;
        }
        // con.Close();
        return false;
    }

    private String SaveJszbsjb(long Mkid, String mingc, String hetbz,
                               double gongf, double changf, double jies, double yingk,
                               double zhejbz, double zhejje, int zhuangt) {
        // ������㵥��ָ������
        Visit visit = new Visit();
        String sql = "";
        long Id = 0;
        try {

            Id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getLong1()));

            sql = " insert into jieszbsjb (id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt, yansbhb_id) "
                    + " values ("
                    + Id
                    + ", "
                    + Mkid
                    + ", "
                    + getProperId(getIZhibbmModel(), mingc)
                    + ", '"
                    + hetbz
                    + "', "
                    + gongf
                    + ", "
                    + changf
                    + ", "
                    + jies
                    + ", "
                    + yingk
                    + ", "
                    + zhejbz
                    + ", "
                    + zhejje
                    + ","
                    + zhuangt
                    + ","
                    + MainGlobal.getTableId("yansbhb", "bianm",
                    ((Visit) getPage().getVisit()).getString4())
                    + ");	\n";

        } catch (Exception e) {

            e.printStackTrace();
        }
        return sql;
    }
    public boolean UpdateDiancjsmkb_id(JDBCcon cn ,long jiesyfb_id){
		/* chh 2010-11-06
		 * ǰ��Լ�����˺�����jiesyf���������danjcpb��jiesyfb_id����󣬵���ִ��
		 * ���˷ѽ���������������Ƿ��ж�Ӧ��diancjsmkb_id(���˷ѽ����Ӧ��jiesb��id) ��û��ʱ����Ϊ0
		 * ������jiesyfb��֤ú�����˷ѵ�����ȷ��Ӧ��ϵ 
		 */
        String sql="update jiesyfb js set diancjsmkb_id=nvl((select max(jiesb_id) from fahb fh,chepb cp,danjcpb dc\n" +
                "        where fh.id=cp.fahb_id\n" +
                "        and cp.id=dc.chepb_id\n" +
                "        and dc.yunfjsb_id="+jiesyfb_id+"),0) where id=" +jiesyfb_id;

        cn.getUpdate(sql);

        return true;
    }

    private boolean Save() {
        // ��Ҫ˵�������н����δ��д ��ͬid������״̬id�����̸���id���󷽽���id
        String msg = "";
        long Mkid = 0;// ú��id
        long Yfid = 0;// �˷�id
        boolean Flag = false;
        String table_mk = "jiesb";
        String table_yf = "jiesyfb";
        JDBCcon con =new JDBCcon();
        con.setAutoCommit(false);
        try {

            if (Jiesdcz.CheckHetshzt(((Dcbalancebean) getEditValues().get(0))
                    .getHetb_id(),((Visit) getPage().getVisit()).getLong1())) {
                // �жϺ�ͬ�����״̬

                if (getEditValues() != null
                        && !getEditValues().isEmpty()
                        && !((Dcbalancebean) getEditValues().get(0))
                        .getJiesbh().equals("")) {

                    if (Jiesdcz.isFengsj(((Visit) getPage().getVisit()).getString15())){

//						�Ƿֹ�˾�ɹ�����
                        table_mk="kuangfjsmkb";
                        table_yf="kuangfjsyfb";
                    }

                    if (Jiesdcz.checkbh(table_mk,table_yf,
                            ((Dcbalancebean) getEditValues().get(0))
                                    .getJiesbh(), 0, 0)) {

                        if (((Visit) getPage().getVisit()).getLong2() == Locale.liangpjs_feiylbb_id) {// ��Ʊ����

                            if (((Dcbalancebean) getEditValues().get(0))
                                    .getId() == 0) {
                                // �糧ú���
                                Mkid = SaveDiancjsmkb(con,table_mk,table_yf);

                                if (Mkid > 0) {
                                    // �����糧�����˷ѱ�
                                    Yfid = SaveDiancjsyfb(Mkid,con,table_yf);

                                    if (Yfid > 0) {
                                        // Ҫ�ͻ�����Ϣָ��ģ�鶨һ��zhibb����

                                        if (SaveZhib(Mkid,con)) {

                                            Flag = true;
                                        }
                                    }
                                }
                            }

                        } else if (((Visit) getPage().getVisit()).getLong2() == Locale.meikjs_feiylbb_id) {
                            // ������ú��
                            if (((Dcbalancebean) getEditValues().get(0))
                                    .getId() == 0) {
                                // ����ú���
                                Mkid = SaveDiancjsmkb(con,table_mk,table_yf);

                                if (Mkid > 0) {

                                    if (this.SaveZhib(Mkid,con)) {

                                        Flag = true;
                                    }
                                }
                            }
                        } else {
                            // �������˷�
                            if (checkXitszDjyf()) {
                                // ��ϵͳ��Ϣ������ã�����˾ϵͳ�ܲ��ܵ������˷�
                                Yfid = SaveDiancjsyfb(0,con,table_yf);

                                if (Yfid > 0) {
                                    //��������˷�,��Ҫ������ú�������ϵ,��jiesyfb ��diancmkjsb_id
                                    UpdateDiancjsmkb_id(con,Yfid);
                                    Flag = true;
//									}
                                }
                            } else {

                                msg = "��ѡ������˷Ѷ�Ӧ��ú����㵥";
                            }
                        }
                    } else {

                        msg = "���㵥����ظ�";
                    }
                } else {

                    msg = "���ܱ���ս��㵥";
                }

            } else {

                msg = "��ͬδ��˲��ܱ��棡";
            }

            if (Flag) {

                setMsg("��������ɹ���");
                con.commit();
                Chengbcl.CountCb_js(((Visit) getPage().getVisit()).getString15(),Mkid, Yfid);

            } else {

                setMsg(msg + " �������ʧ�ܣ�");
                con.rollBack();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{

            con.Close();
        }
        return Flag;
    }

    private boolean SaveZhib(long Mkid,JDBCcon con) {
        // ������㵥ָ��
        boolean Flag = false;

        StringBuffer sbsql = new StringBuffer(" begin \n");

        sbsql.append(this.SaveJszbsjb(Mkid, Locale.jiessl_zhibb,
                ((Dcbalancebean) getEditValues().get(0)).getHetsl(),
                ((Dcbalancebean) getEditValues().get(0)).getGongfsl(),
                ((Dcbalancebean) getEditValues().get(0)).getYanssl(),
                ((Dcbalancebean) getEditValues().get(0)).getJiessl(),
                ((Dcbalancebean) getEditValues().get(0)).getYingksl(),
                ((Dcbalancebean) getEditValues().get(0)).getShulzjbz(),
                ((Dcbalancebean) getEditValues().get(0)).getShulzjje(), 1));

        if (!((Dcbalancezbbean) getJieszbValues().get(0)).getShulzb_ht()
                .equals("")) {

            sbsql.append(this.SaveJszbsjb(Mkid, Locale.Shul_zhibb,
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getShulzb_ht(),
                    ((Dcbalancebean) getEditValues().get(0)).getGongfsl(),
                    ((Dcbalancebean) getEditValues().get(0)).getYanssl(),
                    ((Dcbalancebean) getEditValues().get(0)).getJiessl(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getShulzb_yk(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getShulzb_zdj(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getShulzb_zje(),
                    1));
        }

        if (!((Dcbalancezbbean) getJieszbValues().get(0)).getQnetar_ht()
                .equals("")) {

            sbsql
                    .append(this
                            .SaveJszbsjb(
                                    Mkid,
                                    Locale.Qnetar_zhibb,
                                    MainGlobal
                                            .Mjkg_to_kcalkg(
                                                    ((Dcbalancezbbean) getJieszbValues()
                                                            .get(0))
                                                            .getQnetar_ht(),
                                                    "-", 0),
                                    MainGlobal
                                            .Mjkg_to_kcalkg(
                                                    ((Dcbalancezbbean) getJieszbValues()
                                                            .get(0))
                                                            .getQnetar_kf(), 0 ,
                                                    ((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
                                    MainGlobal
                                            .Mjkg_to_kcalkg(
                                                    ((Dcbalancezbbean) getJieszbValues()
                                                            .get(0))
                                                            .getQnetar_cf(), 0 ,
                                                    ((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
                                    MainGlobal
                                            .Mjkg_to_kcalkg(
                                                    ((Dcbalancezbbean) getJieszbValues()
                                                            .get(0))
                                                            .getQnetar_js(), 0 ,
                                                    ((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
                                    ((Dcbalancezbbean) getJieszbValues().get(0))
                                            .getQnetar_yk(),
                                    ((Dcbalancezbbean) getJieszbValues().get(0))
                                            .getQnetar_zdj(),
                                    ((Dcbalancezbbean) getJieszbValues().get(0))
                                            .getQnetar_zje(), 1));
        }

        if (!((Dcbalancezbbean) getJieszbValues().get(0)).getStd_ht()
                .equals("")) {

            sbsql.append(this.SaveJszbsjb(Mkid, Locale.Std_zhibb,
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getStd_ht(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getStd_kf(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getStd_cf(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getStd_js(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getStd_yk(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getStd_zdj(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getStd_zje(),
                    1));
        }

        if (!((Dcbalancezbbean) getJieszbValues().get(0)).getAd_ht().equals("")) {

            sbsql.append(this
                    .SaveJszbsjb(Mkid, Locale.Ad_zhibb,
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getAd_ht(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getAd_kf(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getAd_cf(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getAd_js(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getAd_yk(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getAd_zdj(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getAd_zje(), 1));
        }

        if (!((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_ht().equals(
                "")) {

            sbsql.append(this.SaveJszbsjb(Mkid, Locale.Vdaf_zhibb,
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_ht(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_kf(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_cf(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_js(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_yk(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_zdj(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_zje(),
                    1));
        }

        if (!((Dcbalancezbbean) getJieszbValues().get(0)).getMt_ht().equals("")) {

            sbsql.append(this
                    .SaveJszbsjb(Mkid, Locale.Mt_zhibb,
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getMt_ht(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getMt_kf(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getMt_cf(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getMt_js(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getMt_yk(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getMt_zdj(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getMt_zje(), 1));
        }

        if (!((Dcbalancezbbean) getJieszbValues().get(0)).getQgrad_ht().equals(
                "")) {

            sbsql
                    .append(this
                            .SaveJszbsjb(
                                    Mkid,
                                    Locale.Qgrad_zhibb,
                                    MainGlobal
                                            .Mjkg_to_kcalkg(
                                                    ((Dcbalancezbbean) getJieszbValues()
                                                            .get(0))
                                                            .getQgrad_ht(),
                                                    "-", 0),
                                    MainGlobal
                                            .Mjkg_to_kcalkg(
                                                    ((Dcbalancezbbean) getJieszbValues()
                                                            .get(0))
                                                            .getQgrad_kf(), 0 ,
                                                    ((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
                                    MainGlobal
                                            .Mjkg_to_kcalkg(
                                                    ((Dcbalancezbbean) getJieszbValues()
                                                            .get(0))
                                                            .getQgrad_cf(), 0 ,
                                                    ((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
                                    MainGlobal
                                            .Mjkg_to_kcalkg(
                                                    ((Dcbalancezbbean) getJieszbValues()
                                                            .get(0))
                                                            .getQgrad_js(), 0 ,
                                                    ((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
                                    ((Dcbalancezbbean) getJieszbValues().get(0))
                                            .getQgrad_yk(),
                                    ((Dcbalancezbbean) getJieszbValues().get(0))
                                            .getQgrad_zdj(),
                                    ((Dcbalancezbbean) getJieszbValues().get(0))
                                            .getQgrad_zje(), 1));
        }

        if (!((Dcbalancezbbean) getJieszbValues().get(0)).getQbad_ht().equals(
                "")) {

            sbsql.append(this.SaveJszbsjb(Mkid, Locale.Qbad_zhibb, MainGlobal
                            .Mjkg_to_kcalkg(
                                    ((Dcbalancezbbean) getJieszbValues().get(0))
                                            .getQbad_ht(), "-", 0), MainGlobal
                            .Mjkg_to_kcalkg(
                                    ((Dcbalancezbbean) getJieszbValues().get(0))
                                            .getQbad_kf(), 0 ,
                                    ((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()), MainGlobal
                            .Mjkg_to_kcalkg(
                                    ((Dcbalancezbbean) getJieszbValues().get(0))
                                            .getQbad_cf(), 0 ,
                                    ((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()), MainGlobal
                            .Mjkg_to_kcalkg(
                                    ((Dcbalancezbbean) getJieszbValues().get(0))
                                            .getQbad_js(), 0 ,
                                    ((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getQbad_yk(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getQbad_zdj(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getQbad_zje(),
                    1));
        }

        if (!((Dcbalancezbbean) getJieszbValues().get(0)).getHad_ht()
                .equals("")) {

            sbsql.append(this.SaveJszbsjb(Mkid, Locale.Had_zhibb,
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getHad_ht(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getHad_kf(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getHad_cf(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getHad_js(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getHad_yk(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getHad_zdj(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getHad_zje(),
                    1));
        }

        if (!((Dcbalancezbbean) getJieszbValues().get(0)).getStad_ht().equals(
                "")) {

            sbsql.append(this.SaveJszbsjb(Mkid, Locale.Stad_zhibb,
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getStad_ht(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getStad_kf(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getStad_cf(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getStad_js(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getStad_yk(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getStad_zdj(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getStad_zje(),
                    1));
        }

        if (!((Dcbalancezbbean) getJieszbValues().get(0)).getMad_ht()
                .equals("")) {

            sbsql.append(this.SaveJszbsjb(Mkid, Locale.Mad_zhibb,
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getMad_ht(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getMad_kf(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getMad_cf(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getMad_js(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getMad_yk(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getMad_zdj(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getMad_zje(),
                    1));
        }

        if (!((Dcbalancezbbean) getJieszbValues().get(0)).getAar_ht()
                .equals("")) {

            sbsql.append(this.SaveJszbsjb(Mkid, Locale.Aar_zhibb,
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getAar_ht(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getAar_kf(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getAar_cf(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getAar_js(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getAar_yk(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getAar_zdj(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getAar_zje(),
                    1));
        }

        if (!((Dcbalancezbbean) getJieszbValues().get(0)).getAad_ht()
                .equals("")) {

            sbsql.append(this.SaveJszbsjb(Mkid, Locale.Aad_zhibb,
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getAad_ht(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getAad_kf(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getAad_cf(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getAad_js(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getAad_yk(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getAad_zdj(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getAad_zje(),
                    1));
        }

        if (!((Dcbalancezbbean) getJieszbValues().get(0)).getVad_ht()
                .equals("")) {

            sbsql.append(this.SaveJszbsjb(Mkid, Locale.Vad_zhibb,
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getVad_ht(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getVad_kf(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getVad_cf(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getVad_js(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getVad_yk(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getVad_zdj(),
                    ((Dcbalancezbbean) getJieszbValues().get(0)).getVad_zje(),
                    1));
        }

        if (!((Dcbalancezbbean) getJieszbValues().get(0)).getT2_ht().equals("")) {

            sbsql.append(this
                    .SaveJszbsjb(Mkid, Locale.T2_zhibb,
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getT2_ht(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getT2_kf(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getT2_cf(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getT2_js(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getT2_yk(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getT2_zdj(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getT2_zje(), 1));
        }

        if (!((Dcbalancezbbean) getJieszbValues().get(0)).getYunju_ht().equals(
                "")) {

            sbsql.append(this
                    .SaveJszbsjb(Mkid, Locale.Yunju_zhibb,
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getYunju_ht(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getYunju_kf(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getYunju_cf(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getYunju_js(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getYunju_yk(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getYunju_zdj(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getYunju_zje(), 1));
        }

        if (!((Dcbalancezbbean) getJieszbValues().get(0)).getStar_ht().equals(
                "")) {

            sbsql.append(this
                    .SaveJszbsjb(Mkid, Locale.Star_zhibb,
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getStar_ht(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getStar_kf(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getStar_cf(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getStar_js(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getStar_yk(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getStar_zdj(),
                            ((Dcbalancezbbean) getJieszbValues().get(0))
                                    .getStar_zje(), 1));
        }

        sbsql.append(" end; ");

        if (con.getInsert(sbsql.toString()) >= 0) {

            Flag = true;
        }
        return Flag;
    }

    private boolean checkXitszDjyf() {
        // TODO �Զ����ɷ������
        // ���ϵͳ�����е�"�ɵ��������˷�"����
        JDBCcon con = new JDBCcon();
        try {
            String zhi = "";

            String sql = "select zhi from xitxxb where mingc='�ɵ��������˷�'";
            ResultSet rs = con.getResultSet(sql);
            if (rs.next()) {

                zhi = rs.getString("zhi");
            }

            if (zhi.trim().equals("��")) {

                return true;
            }
            rs.close();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }

        return false;
    }

    private boolean _SaveChick = false;

    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }

    private boolean _QuedChick = false;

    public void QuedButton(IRequestCycle cycle) {

        _QuedChick = true;
    }

    private boolean _ReturnChick = false;

    public void ReturnButton(IRequestCycle cycle) {

        _ReturnChick = true;
    }

    private boolean _KuangqzfChick = false;

    public void KuangqzfButton(IRequestCycle cycle) {

        _KuangqzfChick = true;
    }

    public void submit(IRequestCycle cycle) {
        if (_SaveChick) {
            _SaveChick = false;
            Save();
        }
        if (_QuedChick) {
            _QuedChick = false;
            Submit();
        }
        if (_ReturnChick) {
            _ReturnChick = false;
            Qux(cycle);
        }
        if (_KuangqzfChick) {
            _KuangqzfChick = false;
            Kuangqzf(cycle);
        }
    }

    private void Kuangqzf(IRequestCycle cycle) {

        ((Visit) getPage().getVisit()).getLong1(); // �糧��Ϣ��id
        ((Visit) getPage().getVisit()).getLong3(); // ��Ӧ�̱�id
        ((Visit) getPage().getVisit()).getLong4(); // ú����Ϣ��id
        ((Visit) getPage().getVisit()).getLong5(); // ��վid
        ((Visit) getPage().getVisit()).getLong6(); // ��վid
        ((Visit) getPage().getVisit()).getDouble1(); // ��Ʊ��
        ((Visit) getPage().getVisit())
                .setLong7(((Dcbalancebean) getEditValues().get(0)).getYid()); // �����˷ѱ�id
        ((Visit) getPage().getVisit()).setSession(cycle.getRequestContext()
                .getSession());
        ((Visit) getPage().getVisit()).setString11("DCBalance");
        cycle.activate("Kuangqzf");
    }

    private void Qux(IRequestCycle cycle) {
        // TODO �Զ����ɷ������

        cycle.activate("Jiesxz");
    }

    private void Submit() {
        // TODO �Զ����ɷ������
        String table_mk = "jiesb";
        String table_yf = "jiesyfb";
        if (((Dcbalancebean) getEditValues().get(0)).getId() == 0
                && ((Dcbalancebean) getEditValues().get(0)).getYid() == 0) {// û������������

            if (Save() && this.getProperId(this.getILiucmcModel(), getLiucmc()) > -1) {

                if (Jiesdcz.isFengsj(((Visit) getPage().getVisit()).getString15())){

//					�Ƿֹ�˾�ɹ�����
                    table_mk="kuangfjsmkb";
                    table_yf="kuangfjsyfb";
                }

                // ���̷���
                if (((Visit) getPage().getVisit()).getLong2() == Locale.liangpjs_feiylbb_id) {// ��Ʊ����

                    Liuc.tij(table_mk, ((Dcbalancebean) getEditValues().get(0))
                            .getId(), ((Visit) getPage().getVisit())
                            .getRenyID(), "", getProperId(this.getILiucmcModel(), getLiucmc()));

                    Liuc.tij(table_yf,
                            ((Dcbalancebean) getEditValues().get(0)).getYid(),
                            ((Visit) getPage().getVisit()).getRenyID(), "",
                            getProperId(this.getILiucmcModel(), getLiucmc()));

//					Jiesdcz.SubmitGsDiancjsmkb(((Dcbalancebean) getEditValues()
//							.get(0)).getId());
//					Jiesdcz.SubmitGsDiancjsyfb(((Dcbalancebean) getEditValues()
//							.get(0)).getYid());

                } else if (((Visit) getPage().getVisit()).getLong2() == Locale.meikjs_feiylbb_id) {// ú�����

                    Liuc.tij(table_mk, ((Dcbalancebean) getEditValues().get(0))
                            .getId(), ((Visit) getPage().getVisit())
                            .getRenyID(), "", getProperId(this.getILiucmcModel(), getLiucmc()));

//					Jiesdcz.SubmitGsDiancjsmkb(((Dcbalancebean) getEditValues()
//							.get(0)).getId());

                } else {// �˷ѽ���

                    Liuc.tij(table_yf,
                            ((Dcbalancebean) getEditValues().get(0)).getYid(),
                            ((Visit) getPage().getVisit()).getRenyID(), "",
                            getProperId(this.getILiucmcModel(), getLiucmc()));

//					Jiesdcz.SubmitGsDiancjsyfb(((Dcbalancebean) getEditValues()
//							.get(0)).getYid());
                }

            } else if (getProperId(this.getILiucmcModel(), getLiucmc()) == -1) {

                setMsg("��ѡ���������ƣ�");
            }

        } else {// �Ѿ�����

            if (this.getProperId(this.getILiucmcModel(), getLiucmc()) > -1) {
                // ���̷���
                if (((Visit) getPage().getVisit()).getLong2() == Locale.liangpjs_feiylbb_id) {// ��Ʊ����

                    Liuc.tij(table_mk, ((Dcbalancebean) getEditValues().get(0))
                            .getId(), ((Visit) getPage().getVisit())
                            .getRenyID(), "", getProperId(this.getILiucmcModel(), getLiucmc()));
                    Liuc.tij(table_yf,
                            ((Dcbalancebean) getEditValues().get(0)).getYid(),
                            ((Visit) getPage().getVisit()).getRenyID(), "",
                            getProperId(this.getILiucmcModel(), getLiucmc()));

                } else if (((Visit) getPage().getVisit()).getLong2() == Locale.meikjs_feiylbb_id) {// ú�����

                    Liuc.tij(table_mk, ((Dcbalancebean) getEditValues().get(0))
                            .getId(), ((Visit) getPage().getVisit())
                            .getRenyID(), "", getProperId(this.getILiucmcModel(), getLiucmc()));

                } else {// �˷ѽ���

                    Liuc.tij(table_yf,
                            ((Dcbalancebean) getEditValues().get(0)).getYid(),
                            ((Visit) getPage().getVisit()).getRenyID(), "",
                            getProperId(this.getILiucmcModel(), getLiucmc()));
                }

            } else if (getProperId(this.getILiucmcModel(), getLiucmc()) == -1) {

                setMsg("��ѡ���������ƣ�");
            }
        }
    }

    private Dcbalancebean _EditValue;

    public List getEditValues() {
        return ((Visit) getPage().getVisit()).getList1();
    }

    public void setEditValues(List editList) {
        ((Visit) getPage().getVisit()).setList1(editList);
    }

    public List getJieszbValues() {
        return ((Visit) getPage().getVisit()).getList2();
    }

    public void setJieszbValues(List editList) {
        ((Visit) getPage().getVisit()).setList2(editList);
    }

    public Dcbalancebean getEditValue() {
        try{
            for(int i=0;i<((Visit) getPage().getVisit()).getList1().size();i++){
                String _value=((IDropDownBean)(((Visit) getPage().getVisit()).getList10().get(i))).getValue();
//				shoukdw:kaihyh,zhangh
                String shoukdw = _value.split(":")[0];
                if(_EditValue.getShoukdw().equals(shoukdw)){
                    _EditValue.setKaihyh(_value.split(":")[1].split(",")[0]);
                    _EditValue.setZhangh(_value.split(":")[1].split(",")[1]);
                }else{
                    _EditValue.setKaihyh("");
                    _EditValue.setZhangh("");
                }
            }
        }catch(Exception e){

        }

        return _EditValue;
    }

    public void setEditValue(Dcbalancebean EditValue) {
        _EditValue = EditValue;
    }

    public List getSelectData() {
        Visit visit = (Visit) getPage().getVisit();
        List _editvalues = new ArrayList();
        List _Jieszbvalues = new ArrayList();

        if (getEditValues() != null) {

            getEditValues().clear();
        }
        if (getJieszbValues() != null) {

            getJieszbValues().clear();
        }

        JDBCcon con = new JDBCcon();
        try {

            long mid = 0;
            long myid = 0;
            String mtianzdw = MainGlobal.getTableCol("diancxxb", "quanc", "id",
                    String.valueOf(((Visit) getPage().getVisit()).getLong1()));
            String mjiesbh = "";
            String mfahdw = "";
            String mmeikdw="";
            String mfaz = "";
            String mshoukdw = "";
            long meikxxb_id=0;
            long gongysb_id=0;
            Date mfahksrq = new Date();
            Date mfahjzrq = new Date();
            Date myansksrq = new Date();
            Date myansjzrq = new Date();
            String mfahrq = "";
            String mkaihyh = "";
            String mpinz = "";
            String myuanshr = mtianzdw;
            String mzhangh = "";
            String mhetsl = "";// ��ͬ����
            double mgongfsl = 0;
            long mches = 0;
            String mxianshr = myuanshr;
            String mfapbh = "";
            String mdaibcc = "";
            String myansbh = "";
            String mduifdd = "";
            String mfukfs = "";
            double mshulzjbz = 0;
            double myanssl = 0;
            double myingksl = 0;
            double mshulzjje = 0;
            String mMjtokcalxsclfs=""; 	//�׽�ת��С������ʽ
            double myunfhsdj = 0;		//�˷Ѻ�˰����

            String mShulzb_ht="";	//����ָ���ͬ
            double mShulzb_yk=0;	//����ָ��ӯ��
            double mShulzb_zdj=0;	//����ָ���۵���
            double mShulzb_zje=0;	//����ָ���۽��

            String mQnetar_ht = ""; // ��ͬ����
            double mQnetar_kf = 0; // ��������
            double mQnetar_cf = 0; // ��������
            double mQnetar_js = 0; // ��������
            double mQnetar_yk = 0; // ����ӯ��
            double mQnetar_zdj = 0; // �۵���
            double mQnetar_zje = 0; // ����

            String mStd_ht = ""; // ��ͬ���
            double mStd_kf = 0; // ��������
            double mStd_cf = 0; // ��������
            double mStd_js = 0; // ��������
            double mStd_yk = 0; // ����ӯ��
            double mStd_zdj = 0; // �۵���
            double mStd_zje = 0; // ����

            String mAd_ht = ""; // ��ͬ���
            double mAd_kf = 0; // ��������
            double mAd_cf = 0; // ��������
            double mAd_js = 0; // ��������
            double mAd_yk = 0; // ����ӯ��
            double mAd_zdj = 0; // �۵���
            double mAd_zje = 0; // ����

            String mVdaf_ht = ""; // ��ͬ���
            double mVdaf_kf = 0; // ��������
            double mVdaf_cf = 0; // ��������
            double mVdaf_js = 0; // ��������
            double mVdaf_yk = 0; // ����ӯ��
            double mVdaf_zdj = 0; // �۵���
            double mVdaf_zje = 0; // ����

            String mMt_ht = ""; // ��ͬ���
            double mMt_kf = 0; // ��������
            double mMt_cf = 0; // ��������
            double mMt_js = 0; // ��������
            double mMt_yk = 0; // ����ӯ��
            double mMt_zdj = 0; // �۵���
            double mMt_zje = 0; // ����

            String mQgrad_ht = ""; // ��ͬ���
            double mQgrad_kf = 0; // ��������
            double mQgrad_cf = 0; // ��������
            double mQgrad_js = 0; // ��������
            double mQgrad_yk = 0; // ����ӯ��
            double mQgrad_zdj = 0; // �۵���
            double mQgrad_zje = 0; // ����

            String mQbad_ht = ""; // ��ͬ���
            double mQbad_kf = 0; // ��������
            double mQbad_cf = 0; // ��������
            double mQbad_js = 0; // ��������
            double mQbad_yk = 0; // ����ӯ��
            double mQbad_zdj = 0; // �۵���
            double mQbad_zje = 0; // ����

            String mHad_ht = ""; // ��ͬ���
            double mHad_kf = 0; // ��������
            double mHad_cf = 0; // ��������
            double mHad_js = 0; // ��������
            double mHad_yk = 0; // ����ӯ��
            double mHad_zdj = 0; // �۵���
            double mHad_zje = 0; // ����

            String mStad_ht = ""; // ��ͬ���
            double mStad_kf = 0; // ��������
            double mStad_cf = 0; // ��������
            double mStad_js = 0; // ��������
            double mStad_yk = 0; // ����ӯ��
            double mStad_zdj = 0; // �۵���
            double mStad_zje = 0; // ����

            String mStar_ht = ""; // ��ͬ���
            double mStar_kf = 0; // ��������
            double mStar_cf = 0; // ��������
            double mStar_js = 0; // ��������
            double mStar_yk = 0; // ����ӯ��
            double mStar_zdj = 0; // �۵���
            double mStar_zje = 0; // ����

            String mMad_ht = ""; // ��ͬ���
            double mMad_kf = 0; // ��������
            double mMad_cf = 0; // ��������
            double mMad_js = 0; // ��������
            double mMad_yk = 0; // ����ӯ��
            double mMad_zdj = 0; // �۵���
            double mMad_zje = 0; // ����

            String mAar_ht = ""; // ��ͬ���
            double mAar_kf = 0; // ��������
            double mAar_cf = 0; // ��������
            double mAar_js = 0; // ��������
            double mAar_yk = 0; // ����ӯ��
            double mAar_zdj = 0; // �۵���
            double mAar_zje = 0; // ����

            String mAad_ht = ""; // ��ͬ���
            double mAad_kf = 0; // ��������
            double mAad_cf = 0; // ��������
            double mAad_js = 0; // ��������
            double mAad_yk = 0; // ����ӯ��
            double mAad_zdj = 0; // �۵���
            double mAad_zje = 0; // ����

            String mVad_ht = ""; // ��ͬ���
            double mVad_kf = 0; // ��������
            double mVad_cf = 0; // ��������
            double mVad_js = 0; // ��������
            double mVad_yk = 0; // ����ӯ��
            double mVad_zdj = 0; // �۵���
            double mVad_zje = 0; // ����

            String mT2_ht = ""; // ��ͬ���
            double mT2_kf = 0; // ��������
            double mT2_cf = 0; // ��������
            double mT2_js = 0; // ��������
            double mT2_yk = 0; // ����ӯ��
            double mT2_zdj = 0; // �۵���
            double mT2_zje = 0; // ����

            String mYunju_ht = ""; // ��ͬ�˾�
            double mYunju_kf = 0; // �����˾�
            double mYunju_cf = 0; // �����˾�
            double mYunju_js = 0; // �����˾�
            double mYunju_yk = 0; // ����ӯ��
            double mYunju_zdj = 0; // �۵���
            double mYunju_zje = 0; // ����

            double mYunfjsdj_mk = 0;	//�˷ѽ��㵥��(jiesb)
            double mYunzfhj_mk = 0;		//���ӷѺϼƣ�jiesb��
            double mBuhsyf_mk = 0;		//����˰�˷ѣ�jiesb��
            double mYunfjsl_mk = 0;		//�˷ѽ�������(jiesb)	

            long mhetb_id = 0;
            double mjiessl = 0;
            double myunfjsl = 0;
            double mkoud_js = 0; // ����ʱ�Ŀ۶�
            double mkouk_js = 0;	 	//����ʱ�ۿ�
            double mbuhsdj = 0;
            double mjiakje = 0;
            double mbukyqjk = 0;
            double mjiakhj = 0;
            double mjiaksl = 0.13;
            double mjiaksk = 0;
            double mjiasje = 0;
            double mtielyf = 0;
            double mtielzf = 0;
            double mkuangqyf = 0;
            double mkuangqzf = 0;
            double mkuangqsk = 0;
            double mkuangqjk = 0;
            double mjiesslcy = 0;
            double mbukyqyzf = 0;
            double mjiskc = 0;
            double mbuhsyf = 0;
            double myunfsl = 0.07;
            double myunfsk = 0;
            double myunzfhj = 0;
            double mhej = 0;
            String mdaxhj = "";
            String mmeikhjdx = "";
            String myunzfhjdx = "";
            String mbeiz = "";
            String mranlbmjbr = ((Visit) getPage().getVisit()).getRenymc();
            Date mranlbmjbrq = new Date();
            double mkuidjf = 0;
            double mjingz = 0;
            Date mjiesrq = new Date();
            String mchangcwjbr = "";
            Date mchangcwjbrq = null;
            Date mruzrq = null;
            String mjieszxjbr = "";
            Date mjieszxjbrq = null;
            String mgongsrlbjbr = ((Visit) getPage().getVisit()).getRenymc();
            Date mgongsrlbjbrq = new Date();
            double mhetjg = 0;
            long mjieslx = ((Visit) getPage().getVisit()).getLong2();
            double myuns = 0;
            String myunsfs = "";
            long myunsfsb_id = 0;
            String mdiancjsbs = "";
            mdiancjsbs = getDiancjsbs(((Visit) getPage().getVisit()).getLong1());
            String mstrJieszb = "";
            String mstrHejdxh= "";
            String mErroMessage = "";
            double myingd = 0;
            double mkuid = 0;
            String myunju = ""; // ��������˾�varchar2��

            // ���е����ν���ʱ��Ҫ��ÿһ�����εĽ��������������������danpcjsmxb�У���ʱ�Ͳ�����id
            // ����ʱҪ�ж��������id������о�һ��Ҫ�����id
            long mMeikjsb_id = 0;
            long mYunfjsb_id = 0;
            long mJihkjb_id = 0;

            double mfengsjj=0;	//�ֹ�˾�Ӽ�
            double mjiajqdj=0;	//�Ӽ�ǰ����
            int mjijlx=0;		//�������ͣ�0����˰��1������˰��

            double mkuidjfyf_je=0;	//���־ܸ��˷ѽ��
            double mkuidjfzf_je=0;	//���־ܸ��ӷѽ��
            double mchaokdl=0;		//��/������
            String mChaodorKuid="";	//��/���ֱ�ʶ��CD,KD,""��

            double yujsjz = 0; //Ԥ������
            double yujsjz_zj=0;//Ԥ�����ܽ��,��γ�ֺ�,��ע��ʹ��
            double yujssl=0;//Ԥ��������
            long yujscs=0;//Ԥ���㳵��
            String yujsbm="";//Ԥ������
            long chongdjsb_id=0;//���Ԥ�����id
            double weicdje=0;//δ��ֽ��(��ֺ�ʣ��Ϊ��ֵĽ��)
            int jieslx_dt=0;//��ͬ��������,0���������㵥,1��Ԥ������㵥,2��������ֽ��㵥,3�ǲ�����ֵĽ��㵥
            boolean isLastChongd=false;
            ResultSetList rsl = con.getResultSetList("select id,hansmk,jiessl,ches,bianm,weicdje from jiesb where id in(" + MainGlobal.getProperId(visit.getProSelectionModel8(),visit.getString20())+")");
            if(rsl.next()){

                yujsjz = rsl.getDouble("hansmk");
                yujsjz_zj=rsl.getDouble("hansmk");
                yujssl=rsl.getDouble("jiessl");
                yujscs=rsl.getLong("ches");
                yujsbm=rsl.getString("bianm");
                chongdjsb_id=rsl.getLong("id");
                weicdje=rsl.getDouble("weicdje");
            }

            rsl = con.getResultSetList("select * from jiesb j where j.chongdjsb_id="+MainGlobal.getProperId(visit.getProSelectionModel8(),visit.getString20())+"");
            if(rsl.next()){//�Ѿ����й����,���Ƿ��û�û�г����.
                yujsjz=weicdje;//��ʣ��û�г����Ľ�����¸�ֵ��yujsjz
                isLastChongd=true;//��γ���Ժ�,�����ν���Ľ�����weicdje���ʱ,isLastChongd=true;
            }

            // mkuangqyf=((Visit) getPage().getVisit()).getDouble2(); //�����˷�
            // mkuangqzf=((Visit) getPage().getVisit()).getDouble3(); //�����ӷ�
            // mkuangqsk=((Visit) getPage().getVisit()).getDouble4(); //����˰��
            // mkuangqjk=((Visit) getPage().getVisit()).getDouble5(); //�����ۿ�

            // ((Visit) getPage().getVisit()).getLong1() //�糧��Ϣ��id
            // ((Visit) getPage().getVisit()).getString1() //��Id
            // ((Visit) getPage().getVisit()).getLong2() //��������
            // ((Visit) getPage().getVisit()).getString2() //�Ƿ���Ҫ����ָ��������ǡ���
            // ((Visit) getPage().getVisit()).getString4() //���ձ��
            // ((Visit) getPage().getVisit()).getLong3() //������λ��id
            // ((Visit) getPage().getVisit()).getLong8() //��ͬ��id
            // ((Visit) getPage().getVisit()).getLong9() //���䵥λid			

            Balances bls = new Balances();
            Balances_variable bsv = new Balances_variable(); // Balances����
            // bsv.setKuangqyf(mkuangqyf);
            // bsv.setKuangqzf(mkuangqzf);
            // bsv.setKuangqsk(mkuangqsk);
            // bsv.setKuangqjk(mkuangqjk);
            bls.setBsv(bsv);

            boolean xuanwjsd = MainGlobal.getXitxx_item("����", "���������������Ƿ����ˮ�ֿ��˵���", String.valueOf(visit.getDiancxxb_id()), "��").equals("��");
            if(xuanwjsd){
                //�����������µ�fahb��
                Jiesdcz.UpdateFahShuiftzl(((Visit) getPage().getVisit()).getString1());
            }
            String lie_ids=((Visit) getPage().getVisit()).getString1();
            bls.getBalanceData(((Visit) getPage().getVisit()).getString1(),
                    ((Visit) getPage().getVisit()).getLong1(),
                    ((Visit) getPage().getVisit()).getLong2(),
                    ((Visit) getPage().getVisit()).getLong3(),
                    ((Visit) getPage().getVisit()).getLong8(),
                    ((Visit) getPage().getVisit()).getString2(),
                    ((Visit) getPage().getVisit()).getString4(),
                    Double.parseDouble(((Visit) getPage().getVisit()).getString7()),
                    Double.parseDouble(((Visit) getPage().getVisit()).getString18()),
                    ((Visit) getPage().getVisit()).getLong9(),
                    Double.parseDouble(((Visit) getPage().getVisit()).getString12()),
                    ((Visit) getPage().getVisit()).getString10(),visit,
                    yujsjz
            );

            bsv = bls.getBsv();
            //������������ԭ�п�����ɺ�׷�Ӽ۸�Ϊ��Ȩƽ�������ۿ�Ϊ�����ε�����µ��������ۿ������
            if(MainGlobal.getXitxx_item("����", "�������ۿ���ε�������", String.valueOf(visit.getDiancxxb_id()), "��").equals("��") && bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)){
                bsv = getDanpczzk(bsv);
            }
            if(MainGlobal.getXitxx_item("����", "������װú", String.valueOf(visit.getDiancxxb_id()), "��").equals("��")){
                bsv = getBaozmkc(bsv);
            }



            mErroMessage = bsv.getErroInfo();
            mjiesbh = bsv.getJiesbh();

            if(Jiesdcz.isFengsj(((Visit) getPage().getVisit()).getString15())){
//				����Ƿֹ�˾�ɹ����㣬���¼���ɹ������
                mjiesbh = Jiesdcz.getJiesbh(((Visit) getPage().getVisit()).getString15()+","
                        +String.valueOf(((Visit) getPage().getVisit()).getLong1()), "");
            }

            mfahdw = bsv.getFahdw();
            mmeikdw = bsv.getMeikdw();
            meikxxb_id=bsv.getMeikxxb_Id();
            gongysb_id=bsv.getGongysb_Id();
            mfaz = bsv.getFaz();
            mshoukdw = bsv.getShoukdw();
            mfahksrq = bsv.getFahksrq();
            mfahjzrq = bsv.getFahjzrq();
            mfahrq = bsv.getFahrq();
            myansksrq = bsv.getYansksrq();
            myansjzrq = bsv.getYansjsrq();
            mkaihyh = bsv.getKaihyh();
            mpinz = bsv.getRanlpz();
            mzhangh = bsv.getZhangH();
            mhetsl = String.valueOf(bsv.getHetml());
            mgongfsl = bsv.getGongfsl();
            mches = bsv.getChes();
            myansbh = bsv.getYansbh();
            mshulzjbz = bsv.getHansmj();
            mhetb_id = bsv.getHetb_Id();
            //-----------------------------------------����-----------------------------------------------------------------
            String zbsql="select distinct bianm from zhibb where leib=1 \n" +
                    "and (bianm  like 'M%' \n" +
                    "or bianm like 'Q%'\n" +
                    "or bianm like 'S%'\n" +
                    "or bianm like 'H%'\n" +
                    "or bianm like 'T%'\n" +
                    "or bianm like 'A%'\n" +
                    "or bianm like 'V%' ) and bianm!='Qnetar'";
            ResultSet rs =con.getResultSet(zbsql);
            String zksql="--����--\n" +
                    "\n" +
                    "select  k.hetb_id,z.zhibdm,z.zhibz,\n" +
                    "decode(k.xiax*k.shangx,0,k.shangx+k.xiax,k.xiax)  hetzbz,\n" +
                    "k.tiaojb_id,k.shangx,k.xiax,k.jis,k.jizzb,k.kouj,k.zengfj,(k.kouj-k.zengfj)*(z.zhibz-k.jizzb)*k.jis zengk,f.laimsl,\n" +
                    "z.zhibz-k.jizzb yk,\n" +
                    "round(f.laimsl*(k.kouj-k.zengfj)*(z.zhibz-k.jizzb)*k.jis,2) zhje from\n" +
                    "(\n"+ "(select   'Qnetar' zhibdm,round_new(sum(laimsl*z.Qnet_ar/0.0041816)/sum(laimsl),0) zhibz," +
                    "round_new(sum(laimsl*nvl(k.Qnet_ar,0)/0.0041816)/sum(laimsl),0) kfzbz\n " +
                    "from  zhilb z \n" +
                    "inner join fahb f on z.id=f.zhilb_id \n" +
                    "left join kuangfzlb k on f.kuangfzlb_id=k.id\n"+
                    "where f.lie_id in ("+lie_ids+")  group by f.hetb_id)\n";
            while(rs.next()){
                String bianm=rs.getString("bianm");
                zksql+="union all\n" +
                        "(select  '"+bianm+"' zhibdm,round_new(sum(laimsl*z."+bianm+")/sum(laimsl),2) zhibz ," +
                        "round_new(sum(laimsl*nvl(k."+bianm+",0))/sum(laimsl),2) kfzbz\n " +
                        "from  zhilb z\n" +
                        "inner join fahb f on z.id=f.zhilb_id \n" +
                        "left join kuangfzlb k on f.kuangfzlb_id=k.id\n" +
                        "where f.lie_id in ("+lie_ids+") )\n" ;
            }

            zksql+= ") z\n" +
                    "inner join zhibb b on  b.bianm=z.zhibdm\n" +
                    "inner join hetzkkb k on k.zhibb_id=b.id and hetb_id="+mhetb_id+"\n" +
                    "inner join (select sum(laimsl) laimsl from fahb where lie_id in ("+lie_ids+")) f on 1=1\n" +
                    "where  ( (z.zhibz <k.shangx+k.xiax  and (k.tiaojb_id=3 or k.tiaojb_id=4))\n" +
                    "or (z.zhibz between k.xiax and k.shangx and k.tiaojb_id=5)\n" +
                    "or (z.zhibz>k.shangx+k.xiax and (k.tiaojb_id=1 or k.tiaojb_id=2) ))\n" +
                    "\n";
            rs=con.getResultSet(zksql);
            while(rs.next()){
                String  zhibdm=rs.getString("zhibdm");
                double zhibz=rs.getDouble("zhibz");
                String hetzbz=rs.getString("hetzbz");
                double jizzb=rs.getDouble("jizzb");
                double zengk=rs.getDouble("zengk");
                double yk=rs.getDouble("yk");
                double zhje=rs.getDouble("zhje");
                if(zhibdm.equals("Qnetar")){
                    mQnetar_ht = hetzbz;
                    mQnetar_kf = jizzb;
                    mQnetar_cf =zhibz;
                    mQnetar_js =zhibz;
                    mQnetar_yk =yk;
                    mQnetar_zdj = zengk;
                    mQnetar_zje = zhje;
                }else if(zhibdm.equals("Std")){
                    mStd_ht = hetzbz;
                    mStd_kf = jizzb;
                    mStd_cf = zhibz;
                    mStd_js = zhibz;
                    mStd_yk = yk;
                    mStd_zdj = zengk;
                    mStd_zje = zhje;
                }else if(zhibdm.equals("Ad")){
                    mAd_ht = hetzbz;
                    mAd_kf = jizzb;
                    mAd_cf = zhibz;
                    mAd_js = zhibz;
                    mAd_yk = yk;
                    mAd_zdj = zengk;
                    mAd_zje = zhje;
                }else if(zhibdm.equals("Vdaf")){
                    mVdaf_ht = hetzbz;
                    mVdaf_kf = jizzb;
                    mVdaf_cf =zhibz;
                    mVdaf_js = zhibz;
                    mVdaf_yk = yk;
                    mVdaf_zdj =zengk;
                    mVdaf_zje = zhje;
                }else if(zhibdm.equals("Mt")){
                    mMt_ht = hetzbz;
                    mMt_kf = jizzb;
                    mMt_cf = zhibz;
                    mMt_js = zhibz;
                    mMt_yk = yk;
                    mMt_zdj = zengk;
                    mMt_zje = zhje;
                }

            }

            String sql="select h.hetb_id,j.jiesl,j.qnet_ar,j.jij,j.jij+h.zengk danj,h.zengk,(j.jij+h.zengk)*j.jiesl jiesje from (\n" +
                    "--����--\n" +
                    "select hetb_id,sum(zengk) zengk from(\n" +
                    zksql+
                    "\n" +
                    "\n" +
                    ") group by hetb_id) h inner join\n" +
                    "---����----\n" +
                    "(\n" +
                    "select j.hetb_id,fz.jiesl,j.jij,fz.qnet_ar,fz.std,j.xiax,j.shangx from (\n" +
                    "select f.hetb_id,sum(laimsl) jiesl,\n" +
                    "round_new(sum(laimsl*(z.Qnet_ar/0.0041816))/sum(laimsl),0) qnet_ar,\n" +
                    "round_new(sum(laimsl*z.std)/sum(laimsl),2) std\n" +
                    "from fahb f \n" +
                    "inner join zhilb z on f.zhilb_id=z.id \n" +
                    "where  f.lie_id in ("+lie_ids+")\n" +
                    "group by f.hetb_id\n" +
                    ") fz\n" +
                    "inner join hetjgb j on j.hetb_id=fz.hetb_id\n" +
                    "where (j.zhibb_id=3 and fz.std >j.xiax+j.shangx) or\n" +
                    "(\n" +
                    "j.zhibb_id=2 and(\n" +
                    "(fz.qnet_ar<j.xiax+j.shangx and (j.tiaojb_id=3 or j.tiaojb_id=4))\n" +
                    "or (fz.qnet_ar between j.xiax and j.shangx and j.tiaojb_id=5)\n" +
                    "or (fz.qnet_ar>j.shangx+j.xiax and (j.tiaojb_id=1 or j.tiaojb_id=2) ))\n" +
                    ")\n" +
                    ") j on h.hetb_id=j.hetb_id\n";
            rs=con.getResultSet(sql);
            if(rs.next()){
                mshulzjbz= rs.getDouble("danj");
            }


            rs.close();
            //----------------------------------------------------------------------------------------------------------
            myanssl = bsv.getYanssl();
            myingksl = bsv.getYingksl();
            mshulzjje = bsv.getShul_zje();

            mdaibcc = bsv.getDaibcc();
            mkoud_js = bsv.getKoud_js();
            mkouk_js = bsv.getKouk_js();
            myunsfsb_id = bsv.getYunsfsb_id();
            myunju = bsv.getYunju_jsbz();
//			�ӼۺͲ���˰���۴���
            mfengsjj=bsv.getFengsjj();
            mjiajqdj=bsv.getJiajqdj();
            mjijlx=bsv.getJijlx();
            mMjtokcalxsclfs=bsv.getMj_to_kcal_xsclfs();//�׽�ת��С������ʽ
            myunfhsdj=bsv.getYunfjsdj();	//�˷Ѻ�������
            this.setMeikhsdjblxsw(bsv.getMeikhsdjblxsw());	//ú�˰���۱���С��λ
            this.setYunfhsdjblxsw(bsv.getYunfhsdjblxsw());	//�˷Ѻ�˰���۱���С��λ

//			ָ��_Begin
//			��������ָ��_Begin
            mShulzb_ht=bsv.getShul_ht();
            mShulzb_yk=bsv.getShul_yk();
            mShulzb_zdj=bsv.getShulzb_zdj();
            mShulzb_zje=bsv.getShulzb_zje();
//			��������ָ��_End

//			mQnetar_ht = bsv.getQnetar_ht();
//			mQnetar_kf = bsv.getQnetar_kf();
//			mQnetar_cf = bsv.getQnetar_cf();
//			mQnetar_js = bsv.getQnetar_js();
//			mQnetar_yk = bsv.getQnetar_yk();
//			mQnetar_zdj = bsv.getQnetar_zdj();
//			mQnetar_zje = bsv.getQnetar_zje();
//
//			mAd_ht = bsv.getAd_ht();
//			mAd_kf = bsv.getAd_kf();
//			mAd_cf = bsv.getAd_cf();
//			mAd_js = bsv.getAd_js();
//			mAd_yk = bsv.getAd_yk();
//			mAd_zdj = bsv.getAd_zdj();
//			mAd_zje = bsv.getAd_zje();
//
//			mStd_ht = bsv.getStd_ht();
//			mStd_kf = bsv.getStd_kf();
//			mStd_cf = bsv.getStd_cf();
//			mStd_js = bsv.getStd_js();
//			mStd_yk = bsv.getStd_yk();
//			mStd_zdj = bsv.getStd_zdj();
//			mStd_zje = bsv.getStd_zje();
//
//			mVdaf_ht = bsv.getVdaf_ht();
//			mVdaf_kf = bsv.getVdaf_kf();
//			mVdaf_cf = bsv.getVdaf_cf();
//			mVdaf_js = bsv.getVdaf_js();
//			mVdaf_yk = bsv.getVdaf_yk();
//			mVdaf_zdj = bsv.getVdaf_zdj();
//			mVdaf_zje = bsv.getVdaf_zje();
//
//			mMt_ht = bsv.getMt_ht();
//			mMt_kf = bsv.getMt_kf();
//			mMt_cf = bsv.getMt_cf();
//			mMt_js = bsv.getMt_js();
//			mMt_yk = bsv.getMt_yk();
//			mMt_zdj = bsv.getMt_zdj();
//			mMt_zje = bsv.getMt_zje();

            mQgrad_ht = bsv.getQgrad_ht();
            mQgrad_kf = bsv.getQgrad_kf();
            mQgrad_cf = bsv.getQgrad_cf();
            mQgrad_js = bsv.getQgrad_js();
            mQgrad_yk = bsv.getQgrad_yk();
            mQgrad_zdj = bsv.getQgrad_zdj();
            mQgrad_zje = bsv.getQgrad_zje();

            mQbad_ht = bsv.getQbad_ht();
            mQbad_kf = bsv.getQbad_kf();
            mQbad_cf = bsv.getQbad_cf();
            mQbad_js = bsv.getQbad_js();
            mQbad_yk = bsv.getQbad_yk();
            mQbad_zdj = bsv.getQbad_zdj();
            mQbad_zje = bsv.getQbad_zje();

            mHad_ht = bsv.getHad_ht();
            mHad_kf = bsv.getHad_kf();
            mHad_cf = bsv.getHad_cf();
            mHad_js = bsv.getHad_js();
            mHad_yk = bsv.getHad_yk();
            mHad_zdj = bsv.getHad_zdj();
            mHad_zje = bsv.getHad_zje();

            mStad_ht = bsv.getStad_ht();
            mStad_kf = bsv.getStad_kf();
            mStad_cf = bsv.getStad_cf();
            mStad_js = bsv.getStad_js();
            mStad_yk = bsv.getStad_yk();
            mStad_zdj = bsv.getStad_zdj();
            mStad_zje = bsv.getStad_zje();

            mStar_ht = bsv.getStar_ht();
            mStar_kf = bsv.getStar_kf();
            mStar_cf = bsv.getStar_cf();
            mStar_js = bsv.getStar_js();
            mStar_yk = bsv.getStar_yk();
            mStar_zdj = bsv.getStar_zdj();
            mStar_zje = bsv.getStar_zje();

            mMad_ht = bsv.getMad_ht();
            mMad_kf = bsv.getMad_kf();
            mMad_cf = bsv.getMad_cf();
            mMad_js = bsv.getMad_js();
            mMad_yk = bsv.getMad_yk();
            mMad_zdj = bsv.getMad_zdj();
            mMad_zje = bsv.getMad_zje();

            mAar_ht = bsv.getAar_ht();
            mAar_kf = bsv.getAar_kf();
            mAar_cf = bsv.getAar_cf();
            mAar_js = bsv.getAar_js();
            mAar_yk = bsv.getAar_yk();
            mAar_zdj = bsv.getAar_zdj();
            mAar_zje = bsv.getAar_zje();

            mAad_ht = bsv.getAad_ht();
            mAad_kf = bsv.getAad_kf();
            mAad_cf = bsv.getAad_cf();
            mAad_js = bsv.getAad_js();
            mAad_yk = bsv.getAad_yk();
            mAad_zdj = bsv.getAad_zdj();
            mAad_zje = bsv.getAad_zje();

            mVad_ht = bsv.getVad_ht();
            mVad_kf = bsv.getVad_kf();
            mVad_cf = bsv.getVad_cf();
            mVad_js = bsv.getVad_js();
            mVad_yk = bsv.getVad_yk();
            mVad_zdj = bsv.getVad_zdj();
            mVad_zje = bsv.getVad_zje();

            mT2_ht = bsv.getT2_ht();
            mT2_kf = bsv.getT2_kf();
            mT2_cf = bsv.getT2_cf();
            mT2_js = bsv.getT2_js();
            mT2_yk = bsv.getT2_yk();
            mT2_zdj = bsv.getT2_zdj();
            mT2_zje = bsv.getT2_zje();

            mYunju_ht = bsv.getYunju_ht(); // ��ͬ�˾�
            mYunju_kf = bsv.getYunju_kf(); // �����˾�
            mYunju_cf = bsv.getYunju_cf(); // �����˾�
            mYunju_js = bsv.getYunju_js(); // �����˾�
            mYunju_yk = bsv.getYunju_yk(); // �˾�ӯ��
            mYunju_zdj = bsv.getYunju_zdj(); // �˾��۵���
            mYunju_zje = bsv.getYunju_zje(); // �۽��

            // ָ��_End

//			��ú����е��˷Ѳ���

            mYunfjsdj_mk = bsv.getYunfjsdj_mk();
            mYunzfhj_mk = bsv.getYunzfhj_mk();
            mBuhsyf_mk = bsv.getBuhsyf_mk();
            mYunfjsl_mk = bsv.getYunfjsl_mk();
//			��ú����е��˷Ѳ���_end

            mjiessl = bsv.getJiessl();
            myunfjsl = bsv.getYunfjsl();
//			mbuhsdj = bsv.getBuhsmj();
//			mjiakje = bsv.getJine();
            mjiakhj = bsv.getJiakhj();

            mjiaksl = bsv.getMeiksl();
            mbuhsdj=mshulzjbz*(1-mjiaksl);
            mjiakhj=mbuhsdj*mjiessl;
            mjiakje=mjiakhj;
//			mjiaksk = bsv.getJiaksk();
            mjiaksk= mjiessl*mshulzjbz*mjiaksl;
//			mjiasje = bsv.getJiashj();
            mjiasje=mjiessl*mshulzjbz;
            mtielyf = bsv.getTielyf();
            mtielzf = bsv.getTielzf();
//			�����˷ѡ��ӷ�
            mkuangqyf=bsv.getKuangqyf();
            mkuangqzf=bsv.getKuangqzf();

            mjiesslcy = bsv.getJiesslcy();
            mbuhsyf = bsv.getBuhsyf();
            myunfsl = bsv.getYunfsl();
            myunfsk = bsv.getYunfsk();
            myunzfhj = bsv.getYunzfhj();
            mhej = bsv.getHej();
            Money mn = new Money();
            mdaxhj = mn.NumToRMBStr(mhej);
            mmeikhjdx = mn.NumToRMBStr(bsv.getJiashj());
            mmeikhjdx = mn.NumToRMBStr(mjiakhj);
            myunzfhjdx = mn.NumToRMBStr(bsv.getYunzfhj());
            mbeiz = bsv.getBeiz();
            mjingz = bsv.getJingz();
            mhetjg = bsv.getHetmdj();
            myuns = bsv.getYuns();
            myunsfs = bsv.getYunsfs();
            myingd = bsv.getYingd();
            mkuid = bsv.getKuid();
            mJihkjb_id = bsv.getJihkjb_id();
            mMeikjsb_id = bsv.getMeikjsb_id();
            mYunfjsb_id = bsv.getYunfjsb_id();

            mkuidjfyf_je = bsv.getKuidjfyf_je();
            mkuidjfzf_je = bsv.getKuidjfzf_je();
            mchaokdl = Math.abs(bsv.getChaokdl());	//��/����������������ʾ
            mChaodorKuid = bsv.getChaodOrKuid();	//��/���ֱ�ʶ

            ((Visit) getPage().getVisit()).setLong4(bsv.getMeikxxb_Id()); // ú����Ϣ��id
            ((Visit) getPage().getVisit()).setLong5(bsv.getFaz_Id()); // ��վid
            ((Visit) getPage().getVisit()).setLong6(bsv.getDaoz_Id()); // ��վid
            ((Visit) getPage().getVisit()).setDouble1(bsv.getGongfsl()); // ��������



            boolean datong_beiz = MainGlobal.getXitxx_item("����", "�����ͬר��", "300", "��").equals("��");
            if(datong_beiz){
                //��ͬ�糧�������Ԥ������ʱ,ɾ����ע��Ϣ
                bsv.setBeiz("");
                mbeiz="";
            }


            if(yujsjz>0){//��ͬ�糧Ԥ���㴦����
                double yingfje=0;
                if(mjiasje>=yujsjz){//���ν����ú��+˰�����Ԥ����Ľ��

                    if(isLastChongd){

                        String Sql_chongd_jil_sql="select rownum as xuh,a.* from (\n" +
                                "select j.jiessl,j.hansdj,round(j.jiessl*j.hansdj,2) as jine\n" +
                                "from jiesb j where j.chongdjsb_id="+chongdjsb_id+"\n" +
                                "order by id) a";
                        rsl = con.getResultSetList(Sql_chongd_jil_sql);
                        String xuh="";
                        String lisjl_jiessl="";//��ʷ��¼_��������
                        String lisjl_hansdj="";//��ʷ��¼_��˰����
                        String lisjl_jine="";//��ʷ��¼_���

                        while (rsl.next()){
                            //Ǳ������.��ֻ֧��2�γ��,����Ƕ�γ��,��ע�ͻ������������
                            xuh=rsl.getString("xuh");
                            lisjl_jiessl=rsl.getDouble("jiessl")+"";
                            lisjl_hansdj=rsl.getDouble("hansdj")+"";
                            lisjl_jine=rsl.getDouble("jine")+"";
                        }

                        //������γ���Ժ�,���һ�εĽ��������Ԥ����ʣ�����ҵ���߼�
                        mches=0;
                        mgongfsl=mgongfsl;
                        myanssl=myanssl;
                        mjiessl=mjiessl;
                        myingksl=mjiessl;
                        //�����ۼ۽��
                        mshulzjje=CustomMaths.Round_new(myingksl*mshulzjbz,2);
                        yingfje=mjiasje;//�����㵥Ӧ�����
                        //��˰�ϼ�
                        mjiasje=mjiasje-yujsjz;
                        //ú��
                        mjiakhj=CustomMaths.Round_new(mjiasje/1.17,2);
                        //˰��
                        mjiaksk=mjiasje-mjiakhj;
                        //���
                        mjiakje=mjiakhj;
                        //����˰ú��(����5λС��)
                        mbuhsdj=CustomMaths.Round_new(mjiakhj/mjiessl,5);
                        //ú��ϼƴ�д
                        mmeikhjdx=mn.NumToRMBStr(mjiasje);

                        //�ϼƴ�д(ú��ϼƼ����˷Ѻϼ�)
                        mdaxhj=mn.NumToRMBStr(myunzfhj+mjiasje);
                        bsv.setHejdx(mdaxhj);
                        //�ϼ�Сд
                        mhej=myunzfhj+mjiasje;
                        bsv.setHej(mhej);


                        //��ע
                        mbeiz="��1�ν���:��������"+lisjl_jiessl+",���㵥��"+lisjl_hansdj+",���"+lisjl_jine+"," +
                                "��2�ν���:��������:"+mjiessl+",���㵥��"+mshulzjbz+",���"+yingfje+",<br>" +
                                "��ֽ�����"+yujsbm+",�������"+yujssl+",��ֽ��"+yujsjz_zj+"";
                        //mbeiz="�����㵥Ӧ�����"+yingfje+",���Ԥ���㵥:"+yujsbm+"  ,��ֽ��Ϊ"+yujsjz+",��ֺ�ʵ�����Ϊ: "+mjiasje;
                        //������������
                        mjiesslcy=CustomMaths.Round_new((mjiessl-mgongfsl),3);
                        //��������=��������
                        mjingz=myanssl;

                        //��ͬ��������
                        jieslx_dt=4;//��һ�ν��㲻�����,�ڶ��ν���Ź������Ĺ���ֵĽ��㵥
                        weicdje=yujsjz;



                    }else{
                        //������һ�γ�־��ܳ��������Ԥ�������ҵ���߼�
                        mches=mches-yujscs;
                        mgongfsl=mgongfsl-yujssl;
                        myanssl=myanssl-yujssl;
                        //����ʵ�Ľ�������,���㵥��,���ܽ���ȼ�¼����,Ȼ�����ڱ�ע��
                        double True_jiesl=mjiessl;
                        double True_shulzjbz=mshulzjbz;
                        String  Trut_jiashj= new DecimalFormat("0.00").format(mjiasje);

                        mjiessl=mjiessl-yujssl;
                        myingksl=CustomMaths.Round_new((mjiessl-mgongfsl),3);
                        //�����ۼ۽��
                        mshulzjje=CustomMaths.Round_new(myingksl*mshulzjbz,2);
                        //��˰�ϼ�
                        mjiasje=mjiasje-yujsjz;
                        //ú��
                        mjiakhj=CustomMaths.Round_new(mjiasje/1.17,2);
                        //˰��
                        mjiaksk=mjiasje-mjiakhj;
                        //���
                        mjiakje=mjiakhj;
                        //����˰ú��(����5λС��)
                        mbuhsdj=CustomMaths.Round_new(mjiakhj/mjiessl,5);
                        //ú��ϼƴ�д
                        mmeikhjdx=mn.NumToRMBStr(mjiasje);

                        //�ϼƴ�д(ú��ϼƼ����˷Ѻϼ�)
                        mdaxhj=mn.NumToRMBStr(myunzfhj+mjiasje);
                        bsv.setHejdx(mdaxhj);
                        //�ϼ�Сд
                        mhej=myunzfhj+mjiasje;
                        bsv.setHej(mhej);


                        //��ע
                        mbeiz="������:"+True_jiesl+",���㵥��:"+True_shulzjbz+",�����ܽ��:"+Trut_jiashj+","+"���Ԥ���㵥:"+yujsbm+"  ,�������: "+yujssl+" , ��ֽ��:"+new DecimalFormat("0.00").format(yujsjz);
                        //������������
                        mjiesslcy=CustomMaths.Round_new((mjiessl-mgongfsl),3);
                        //��������=��������
                        mjingz=myanssl;

                        //��ͬ��������
                        jieslx_dt=2;//�����Ĺ���ֵĽ��㵥

                        weicdje=yujsjz;//���jieslx_dt������1,weicdje����ֶξʹ������ʵ�ʳ�ֵĽ��,��jieslx_dt=1ʱ,weicdje�Ŵ����δ��ֵĽ��
                    }



                }else{//���ν���Ľ������Ԥ����Ľ��

                    mches=0;
                    mgongfsl=mgongfsl;
                    myanssl=myanssl;
                    mjiessl=mjiessl;
                    myingksl=CustomMaths.Round_new((mjiessl-mgongfsl),3);
                    //�����ۼ۽��
                    mshulzjje=CustomMaths.Round_new(myingksl*mshulzjbz,2);
                    //��˰�ϼ�
                    mjiasje=mjiasje;
                    yingfje=mjiasje;//�����㵥Ӧ�����
                    String str_yingfje= new DecimalFormat("0.00").format(yingfje);
                    //ú��
                    mjiakhj=CustomMaths.Round_new(mjiasje/1.17,2);
                    //˰��
                    mjiaksk=mjiasje-mjiakhj;
                    //���
                    mjiakje=mjiakhj;
                    //����˰ú��(����5λС��)
                    mbuhsdj=CustomMaths.Round_new(mjiakhj/mjiessl,5);
                    double jl_weicdje=mjiasje;
                    weicdje=CustomMaths.Round_new((yujsjz-mjiasje),2);//��ֺ�ʣ��δ��ֵĽ��
                    mjiasje=0;//��Ϊ���ν���������Ԥ����,�����ڵȼ�����ú���˰���Ժ�,��ú��˰�ϼ����¸�ֵΪ0;
                    //ú��ϼƴ�д
                    mmeikhjdx=mn.NumToRMBStr(mjiasje);

                    //�ϼƴ�д(ú��ϼƼ����˷Ѻϼ�)
                    mdaxhj=mn.NumToRMBStr(myunzfhj+mjiasje);
                    bsv.setHejdx(mdaxhj);
                    //�ϼ�Сд
                    mhej=myunzfhj+mjiasje;
                    bsv.setHej(mhej);


                    //��ע
                    mbeiz="�����㵥Ӧ�����:"+str_yingfje+",���Ԥ���㵥:"+yujsbm+"���г��,��ֽ��Ϊ: "+str_yingfje+",�����㵥ʵ�����0, ʣ��δ��ֽ��:"+weicdje;
                    //������������
                    mjiesslcy=CustomMaths.Round_new((mjiessl-mgongfsl),3);
                    //��������=��������
                    mjingz=myanssl;

                    jieslx_dt=3;//���������Ĺ���ֵĽ��㵥

                    weicdje=jl_weicdje;//��weicdje����ֶμ���Ϊʣ��δ��ֽ���ֵ��"beiz"�ֶ��Ժ�,�����¸�weicdje�ֶθ�ֵΪ��ֵĽ��

                }

            }



            Jiesdcz Jsdcz = new Jiesdcz();

            if (mShulzb_ht!=null && !mShulzb_ht.equals("")) {

                mstrJieszb += Jsdcz.SetJieszb(
                        "����(��)", "Shulzb_ht",
                        "Shulzb_kf", "Shulzb_cf", "Shulzb_js", "Shulzb_yk",
                        "Shulzb_zdj", "Shulzb_zje", mShulzb_ht, mgongfsl, myanssl,
                        mjiessl, mShulzb_yk, mShulzb_zdj, mShulzb_zje);
            }

            if (mQnetar_ht!=null && !mQnetar_ht.equals("")) {

                mstrJieszb += Jsdcz.SetJieszb("Qnetar"+"(kcal/kg)", "Qnetar_ht",
                        "Qnetar_kf", "Qnetar_cf", "Qnetar_js", "Qnetar_yk",
                        "Qnetar_zdj", "Qnetar_zje", MainGlobal.Mjkg_to_kcalkg(
                                mQnetar_ht, "-", 0), MainGlobal.Mjkg_to_kcalkg(
                                mQnetar_kf, 0,mMjtokcalxsclfs), MainGlobal.Mjkg_to_kcalkg(
                                mQnetar_cf, 0,mMjtokcalxsclfs), MainGlobal.Mjkg_to_kcalkg(
                                mQnetar_js, 0,mMjtokcalxsclfs), mQnetar_yk, mQnetar_zdj,
                        mQnetar_zje);
            }

            if (mAd_ht!=null && !mAd_ht.equals("")) {

                mstrJieszb += Jsdcz.SetJieszb("Ad(%)", "Ad_ht", "Ad_kf",
                        "Ad_cf", "Ad_js", "Ad_yk", "Ad_zdj", "Ad_zje", mAd_ht,
                        mAd_kf, mAd_cf, mAd_js, mAd_yk, mAd_zdj, mAd_zje);
            }

            if (mStd_ht!=null && !mStd_ht.equals("")) {

                mstrJieszb += Jsdcz.SetJieszb("Std(%)", "Std_ht", "Std_kf",
                        "Std_cf", "Std_js", "Std_yk", "Std_zdj", "Std_zje",
                        mStd_ht, mStd_kf, mStd_cf, mStd_js, mStd_yk, mStd_zdj,
                        mStd_zje);
            }

            if (mVdaf_ht!=null && !mVdaf_ht.equals("")) {

                mstrJieszb += Jsdcz.SetJieszb("Vdaf(%)", "Vdaf_ht", "Vdaf_kf",
                        "Vdaf_cf", "Vdaf_js", "Vdaf_yk", "Vdaf_zdj",
                        "Vdaf_zje", mVdaf_ht, mVdaf_kf, mVdaf_cf, mVdaf_js,
                        mVdaf_yk, mVdaf_zdj, mVdaf_zje);
            }

            if (mMt_ht!=null && !mMt_ht.equals("")) {

                mstrJieszb += Jsdcz.SetJieszb("Mt(%)", "Mt_ht", "Mt_kf",
                        "Mt_cf", "Mt_js", "Mt_yk", "Mt_zdj", "Mt_zje", mMt_ht,
                        mMt_kf, mMt_cf, mMt_js, mMt_yk, mMt_zdj, mMt_zje);
            }

            if (mQgrad_ht!=null && !mQgrad_ht.equals("")) {

                mstrJieszb += Jsdcz.SetJieszb("Qgrad(kcal/kg)", "Qgrad_ht",
                        "Qgrad_kf", "Qgrad_cf", "Qgrad_js", "Qgrad_yk",
                        "Qgrad_zdj", "Qgrad_zje", MainGlobal.Mjkg_to_kcalkg(
                                mQgrad_ht, "-", 0), MainGlobal.Mjkg_to_kcalkg(
                                mQgrad_kf, 0,mMjtokcalxsclfs), MainGlobal.Mjkg_to_kcalkg(
                                mQgrad_cf, 0,mMjtokcalxsclfs), MainGlobal.Mjkg_to_kcalkg(
                                mQgrad_js, 0,mMjtokcalxsclfs), mQgrad_yk, mQgrad_zdj,
                        mQgrad_zje);
            }

            if (mQbad_ht!=null && !mQbad_ht.equals("")) {

                mstrJieszb += Jsdcz.SetJieszb("Qbad(kcal/kg)", "Qbad_ht",
                        "Qbad_kf", "Qbad_cf", "Qbad_js", "Qbad_yk", "Qbad_zdj",
                        "Qbad_zje",
                        MainGlobal.Mjkg_to_kcalkg(mQbad_ht, "-", 0), MainGlobal
                                .Mjkg_to_kcalkg(mQbad_kf, 0,mMjtokcalxsclfs), MainGlobal
                                .Mjkg_to_kcalkg(mQbad_cf, 0,mMjtokcalxsclfs), MainGlobal
                                .Mjkg_to_kcalkg(mQbad_js, 0,mMjtokcalxsclfs), mQbad_yk,
                        mQbad_zdj, mQbad_zje);
            }

            if (mHad_ht!=null && !mHad_ht.equals("")) {

                mstrJieszb += Jsdcz.SetJieszb("Had(%)", "Had_ht", "Had_kf",
                        "Had_cf", "Had_js", "Had_yk", "Had_zdj", "Had_zje",
                        mHad_ht, mHad_kf, mHad_cf, mHad_js, mHad_yk, mHad_zdj,
                        mHad_zje);
            }

            if (mStad_ht!=null && !mStad_ht.equals("")) {

                mstrJieszb += Jsdcz.SetJieszb("Stad(%)", "Stad_ht", "Stad_kf",
                        "Stad_cf", "Stad_js", "Stad_yk", "Stad_zdj",
                        "Stad_zje", mStad_ht, mStad_kf, mStad_cf, mStad_js,
                        mStad_yk, mStad_zdj, mStad_zje);
            }

            if (mStar_ht!=null && !mStar_ht.equals("")) {

                mstrJieszb += Jsdcz.SetJieszb("Star(%)", "Star_ht", "Star_kf",
                        "Star_cf", "Star_js", "Star_yk", "Star_zdj",
                        "Star_zje", mStar_ht, mStar_kf, mStar_cf, mStar_js,
                        mStar_yk, mStar_zdj, mStar_zje);
            }

            if (mMad_ht!=null && !mMad_ht.equals("")) {

                mstrJieszb += Jsdcz.SetJieszb("Mad(%)", "Mad_ht", "Mad_kf",
                        "Mad_cf", "Mad_js", "Mad_yk", "Mad_zdj", "Mad_zje",
                        mMad_ht, mMad_kf, mMad_cf, mMad_js, mMad_yk, mMad_zdj,
                        mMad_zje);
            }

            if (mAar_ht!=null && !mAar_ht.equals("")) {

                mstrJieszb += Jsdcz.SetJieszb("Aar(%)", "Aar_ht", "Aar_kf",
                        "Aar_cf", "Aar_js", "Aar_yk", "Aar_zdj", "Aar_zje",
                        mAar_ht, mAar_kf, mAar_cf, mAar_js, mAar_yk, mAar_zdj,
                        mAar_zje);
            }

            if (mAad_ht!=null && !mAad_ht.equals("")) {

                mstrJieszb += Jsdcz.SetJieszb("Aad(%)", "Aad_ht", "Aad_kf",
                        "Aad_cf", "Aad_js", "Aad_yk", "Aad_zdj", "Aad_zje",
                        mAad_ht, mAad_kf, mAad_cf, mAad_js, mAad_yk, mAad_zdj,
                        mAad_zje);
            }

            if (mVad_ht!=null && !mVad_ht.equals("")) {

                mstrJieszb += Jsdcz.SetJieszb("Vad(%)", "Vad_ht", "Vad_kf",
                        "Vad_cf", "Vad_js", "Vad_yk", "Vad_zdj", "Vad_zje",
                        mVad_ht, mVad_kf, mVad_cf, mVad_js, mVad_yk, mVad_zdj,
                        mVad_zje);
            }

            if (mT2_ht!=null && !mT2_ht.equals("")) {

                mstrJieszb += Jsdcz.SetJieszb("T2(%)", "T2_ht", "T2_kf",
                        "T2_cf", "T2_js", "T2_yk", "T2_zdj", "T2_zje", mT2_ht,
                        mT2_kf, mT2_cf, mT2_js, mT2_yk, mT2_zdj, mT2_zje);
            }

            if (mYunju_ht!=null && !mYunju_ht.equals("")) {

                mstrJieszb += Jsdcz.SetJieszb("�˾�(Km)", "Yunju_ht", "Yunju_kf",
                        "Yunju_cf", "Yunju_js", "Yunju_yk", "Yunju_zdj",
                        "Yunju_zje", mYunju_ht, mYunju_kf, mYunju_cf,
                        mYunju_js, mYunju_yk, mYunju_zdj, mYunju_zje);
            }

            setJieszb(mstrJieszb);

//			���ó�/���ֵ���ʾ	
            mstrHejdxh=Jsdcz.SetHejdxh(bsv.getChaodOrKuid(),mchaokdl,bsv.getHej(),bsv.getHejdx());

            this.setHejdxh(mstrHejdxh);





            if (!mErroMessage.equals("")) {

                this.setMsg(mErroMessage);
            } else if ((((Visit) getPage().getVisit()).getLong2() == Locale.liangpjs_feiylbb_id || ((Visit) getPage()
                    .getVisit()).getLong2() == Locale.meikjs_feiylbb_id)
                    && mshulzjbz == 0) {

                this.setMsg("��ͬ������������");
            }

            _editvalues.add(new Dcbalancebean(mid, myid, mtianzdw, mjiesbh,
                    mfahdw, mmeikdw, mfaz, myunsfsb_id, mshoukdw, mfahksrq, mfahjzrq,
                    myansksrq, myansjzrq, mkaihyh, mpinz, myuanshr, mzhangh,
                    mhetsl, mgongfsl, mches, mxianshr, mfapbh, mdaibcc,
                    myansbh, mduifdd, mfukfs, mshulzjbz, myanssl, myingksl,
                    myingd, mkuid, mshulzjje, mjiessl, mjiesslcy, myunfjsl,
                    mbuhsdj, mjiakje, mbukyqjk, mjiakhj, mjiaksl, mjiaksk,
                    mjiasje, mtielyf, mtielzf, mkuangqyf, mkuangqzf, mkuangqsk,
                    mkuangqjk, mbukyqyzf, mjiskc, mbuhsyf, myunfsl, myunfsk,
                    myunzfhj, mhej, mmeikhjdx, myunzfhjdx, mdaxhj, mbeiz,
                    mranlbmjbr, mranlbmjbrq, mkuidjf, mjingz, mjiesrq, mfahrq,
                    mchangcwjbr, mchangcwjbrq, mruzrq, mjieszxjbr, mjieszxjbrq,
                    mgongsrlbjbr, mgongsrlbjbrq, mhetjg, mjieslx, myuns,
                    mkoud_js, myunsfs, mdiancjsbs, mhetb_id, myunju,
                    mMeikjsb_id, mYunfjsb_id, mJihkjb_id, mfengsjj, mjiajqdj,
                    mjijlx,mMjtokcalxsclfs,mkuidjfyf_je,mkuidjfzf_je,mchaokdl,
                    mChaodorKuid,myunfhsdj,mYunfjsdj_mk,mYunzfhj_mk,mBuhsyf_mk,
                    mYunfjsl_mk,mkouk_js,meikxxb_id,gongysb_id,chongdjsb_id,weicdje,jieslx_dt));

            _Jieszbvalues.add(new Dcbalancezbbean(mShulzb_ht,mQnetar_ht, mStd_ht, mAd_ht,
                    mVdaf_ht, mMt_ht, mQgrad_ht, mQbad_ht, mHad_ht, mStad_ht,
                    mMad_ht, mAar_ht, mAad_ht, mVad_ht, mT2_ht, mYunju_ht,mStar_ht,
                    mQnetar_kf, mStd_kf, mAd_kf, mVdaf_kf, mMt_kf, mQgrad_kf,
                    mQbad_kf, mHad_kf, mStad_kf, mMad_kf, mAar_kf, mAad_kf,
                    mVad_kf, mT2_kf, mYunju_kf, mStar_kf,mQnetar_cf, mStd_cf, mAd_cf,
                    mVdaf_cf, mMt_cf, mQgrad_cf, mQbad_cf, mHad_cf, mStad_cf,
                    mMad_cf, mAar_cf, mAad_cf, mVad_cf, mT2_cf, mYunju_cf,mStar_cf,
                    mQnetar_js, mStd_js, mAd_js, mVdaf_js, mMt_js, mQgrad_js,
                    mQbad_js, mHad_js, mStad_js, mMad_js, mAar_js, mAad_js,
                    mVad_js, mT2_js, mYunju_js, mStar_js, mShulzb_yk, mQnetar_yk, mStd_yk, mAd_yk,
                    mVdaf_yk, mMt_yk, mQgrad_yk, mQbad_yk, mHad_yk, mStad_yk,
                    mMad_yk, mAar_yk, mAad_yk, mVad_yk, mT2_yk, mYunju_yk,mStar_yk,
                    mShulzb_zdj,mQnetar_zdj, mStd_zdj, mAd_zdj, mVdaf_zdj, mMt_zdj,
                    mQgrad_zdj, mQbad_zdj, mHad_zdj, mStad_zdj, mMad_zdj,
                    mAar_zdj, mAad_zdj, mVad_zdj, mT2_zdj, mYunju_zdj,mStar_zdj,
                    mShulzb_zje, mQnetar_zje, mStd_zje, mAd_zje, mVdaf_zje, mMt_zje,
                    mQgrad_zje, mQbad_zje, mHad_zje, mStad_zje, mMad_zje,
                    mAar_zje, mAad_zje, mVad_zje, mT2_zje, mYunju_zje,
                    mStar_zje));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        if (_editvalues == null) {
            _editvalues.add(new Dcbalancebean());
        }
        _editTableRow = -1;
        setEditValues(_editvalues);
        this.setJieszbValues(_Jieszbvalues);
        return getEditValues();
    }

    public void setJieszb(String value) {

        ((Visit) getPage().getVisit()).setString8(value);
    }

    public String getJieszb() {

        return ((Visit) getPage().getVisit()).getString8();
    }

    public void setHejdxh(String value){

        ((Visit) getPage().getVisit()).setString9(value);
    }

    public String getHejdxh(){

        return ((Visit) getPage().getVisit()).getString9();
    }

    private void getToolbars() {
        Toolbar tb1 = new Toolbar("tbdiv");

        // ����
        ToolbarButton quxbt = new ToolbarButton(null, "����",
                "function(){ document.Form0.ReturnButton.click();}");
        quxbt.setId("fanhbt");
        tb1.addItem(quxbt);
        tb1.addText(new ToolbarText("-"));
        //		
        if (((Dcbalancebean) getEditValues().get(0)).getId() == 0
                || ((Dcbalancebean) getEditValues().get(0)).getYid() == 0) {

            // ����
            ToolbarButton savebt = new ToolbarButton(null, "����",
                    "function(){ document.Form0.SaveButton.click(); \n" +
                            MainGlobal.getExtMessageShow("���ڴ�������,���Ժ�...", "������...", 200)+
                            "}");
            savebt.setId("savebt");
            tb1.addItem(savebt);

            // if(((Visit) getPage().getVisit()).getLong2()==1||((Visit)
            // getPage().getVisit()).getLong2()==3){
            //				
            // tb1.addText(new ToolbarText("-"));
            //				
            // ToolbarButton kuangqzfbt=new
            // ToolbarButton(null,"�����ӷ�¼��","function(){
            // document.Form0.KuangqzfButton.click(); }");
            // kuangqzfbt.setId("kuangqzfbt");
            // tb1.addItem(kuangqzfbt);
            // }

        } else if (((Dcbalancebean) getEditValues().get(0)).getId() > 0
                || ((Dcbalancebean) getEditValues().get(0)).getYid() > 0) {

            // �ύ��������
            ToolbarButton submitbt = new ToolbarButton(
                    null,
                    "�ύ��������",
                    "function(){  \n"
                            + " if(!win){	\n"
                            + "	\tvar form = new Ext.form.FormPanel({	\n"
                            + " \tbaseCls: 'x-plain',	\n"
                            + " \tlabelAlign:'right',	\n"
                            + " \tdefaultType: 'textfield',	\n"
                            + " \titems: [{		\n"
                            + " \txtype:'fieldset',	\n"
                            + " \ttitle:'��ѡ����������',	\n"
                            + " \tautoHeight:false,	\n"
                            + " \theight:220,	\n"
                            + " \titems:[	\n"
                            + " \tlcmccb=new Ext.form.ComboBox({	\n"
                            + " \twidth:150,	\n"
                            + " \tselectOnFocus:true,	\n"
                            + "	\ttransform:'LIUCMCSelect',	\n"
                            + " \tlazyRender:true,	\n"
                            + " \tfieldLabel:'��������',		\n"
                            + " \ttriggerAction:'all',	\n"
                            + " \ttypeAhead:true,	\n"
                            + " \tforceSelection:true,	\n"
                            + " \teditable:false	\n"
                            + " \t})	\n"
                            + " \t]		\n"
                            + " \t}]	\n"
                            + " \t});	\n"
                            + " \twin = new Ext.Window({	\n"
                            + " \tel:'hello-win',	\n"
                            + " \tlayout:'fit',	\n"
                            + " \twidth:500,	\n"
                            + " \theight:300,	\n"
                            + " \tcloseAction:'hide',	\n"
                            + " \tplain: true,	\n"
                            + " \ttitle:'����',	\n"
                            + " \titems: [form],	\n"
                            + " \tbuttons: [{	\n"
                            + " \ttext:'ȷ��',	\n"
                            + " \thandler:function(){	\n"
                            + " \twin.hide();	\n"
                            + " \tif(lcmccb.getRawValue()=='��ѡ��'){		\n"
                            + "	\t	alert('��ѡ���������ƣ�');		\n"
                            + " \t}else{"
                            + " \t\t document.getElementById('TEXT_LIUCMCSELECT_VALUE').value=lcmccb.getRawValue();	\n"
                            + " \t\t document.all.item('QuedButton').click();	\n"
                            + " \t}	\n"
                            + " \t}	\n"
                            + " \t},{	\n"
                            + " \ttext: 'ȡ��',	\n"
                            + " \thandler: function(){	\n"
                            + " \twin.hide();	\n"
                            + " \tdocument.getElementById('TEXT_LIUCMCSELECT_VALUE').value='';	\n"
                            + " \t}		\n"
                            + " \t}]	\n"
                            + " \t});}	\n"
                            + " \twin.show(this);	\n"

                            + " \tif(document.getElementById('TEXT_LIUCMCSELECT_VALUE').value!=''){	\n"
//							+ " \tChangb.setRawValue(document.getElementById('TEXT_LIUCMCSELECT_VALUE').value);	\n"
                            + " \t}	\n" + " \t}");
            submitbt.setId("submitbt");
            tb1.addItem(submitbt);
        }

        setToolbar(tb1);
    }

    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
        Visit visit = (Visit) getPage().getVisit();
        if (!visit.getActivePageName().toString().equals(
                this.getPageName().toString())) {
            // �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
            visit.setActivePageName(getPageName().toString());
//			visit.setList1(null);

            // ((Visit) getPage().getVisit()).getLong1() //�糧��Ϣ��id
            // ((Visit) getPage().getVisit()).getString1() //fahb_Id
            // ((Visit) getPage().getVisit()).getLong2() //��������
            // ((Visit) getPage().getVisit()).getString2() //�Ƿ���Ҫ����ָ��������ǡ���
            // ((Visit) getPage().getVisit()).getString4() //���ձ��
            // ((Visit) getPage().getVisit()).getLong3() //������λ��id
            // visit.setLong4(0); //ú����Ϣ��id
            // visit.setLong5(0); //��վ
            // visit.setLong6(0); //��վ
            // visit.setLong7(0); //�����˷ѱ�id
            // visit.setLong8(0); //��ͬ��id
            // visit.setLong9(0); //���䵥λid
            // visit.setDouble1(0); //��Ʊ��
//			visit.setString12("");	//�ϴν�����
//			visit.setString13(string); //����id
//			visit.setString14("");	//gongysbmlt_id(��Ӧ�̱�)
//			visit.setString10("");	//���䵥λ

//			visit.setString15("");	//���㵥λ����������ҳ�����õĽ��㵥λid����
            //visit.setString19("");  //�������
//			visit.setString20("");  //ȼ��Ʒ��ID
//			�ò���Ҫ������һ��ҳ�棬����ʱ���ж���

            ((Visit) getPage().getVisit()).setDropDownBean1(null); // zhibb
            ((Visit) getPage().getVisit()).setProSelectionModel1(null);

            ((Visit) getPage().getVisit()).setDropDownBean3(null); // ��Ӧ��
            ((Visit) getPage().getVisit()).setProSelectionModel3(null);

            ((Visit) getPage().getVisit()).setDropDownBean4(null); // ��վ
            ((Visit) getPage().getVisit()).setProSelectionModel4(null);

            ((Visit) getPage().getVisit()).setDropDownBean6(null); // Ʒ��
            ((Visit) getPage().getVisit()).setProSelectionModel6(null);

            ((Visit) getPage().getVisit()).setDropDownBean7(null); // �տλ
            ((Visit) getPage().getVisit()).setProSelectionModel7(null);

            ((Visit) getPage().getVisit()).setString8(""); // ����ָ��ҳ����ʾֵ
            ((Visit) getPage().getVisit()).setString9(""); // �ϼƴ�д����ʾ���ݣ�Ϊʵ�ֶ�̬���á����۶֡���ʾ�ã�

            ((Visit) getPage().getVisit()).setLong4(0); // ú����Ϣ��id
            ((Visit) getPage().getVisit()).setLong5(0); // ��վ
            ((Visit) getPage().getVisit()).setLong6(0); // ��վ
            ((Visit) getPage().getVisit()).setLong7(0); // �����˷ѱ�id
            ((Visit) getPage().getVisit()).setDouble1(0); // ��Ʊ��
            ((Visit) getPage().getVisit()).setDouble17(0); // ��ж����
            ((Visit) getPage().getVisit()).setString11(""); // ����Kuangqzf����ʱ����ת����ȷ�Ľ���(DCBalance,Jiesdxg)
            ((Visit) getPage().getVisit()).setString16("");	// ���ڴ��� ��д���ۿ���Ƿ�������㵥��
            ((Visit) getPage().getVisit()).setInt1(2);	// ú�˰���۱���С��λ
            ((Visit) getPage().getVisit()).setInt2(2);	// �˷Ѻ�˰���۱���С��λ

            setLiucmcValue(null);// 10
            setILiucmcModel(null);// 10
            getILiucmcModels();

            getIZhibbmModels();
            getIShoukdwModels();
            ((Visit) getPage().getVisit()).setboolean1(false); // ��ͬ��
            ((Visit) getPage().getVisit()).setboolean2(false); // ���Ƶ�λ
            getSelectData();
        }
        getToolbars();
    }

    // ָ������Model

    public IDropDownBean getZhibbmValue() {
        if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
            ((Visit) getPage().getVisit())
                    .setDropDownBean1((IDropDownBean) getIZhibbmModel()
                            .getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean1();
    }

    public void setZhibbmValue(IDropDownBean value) {

        ((Visit) getPage().getVisit()).setDropDownBean1(value);
    }

    public void setIZhibbmModel(IPropertySelectionModel value) {

        ((Visit) getPage().getVisit()).setProSelectionModel1(value);
    }

    public IPropertySelectionModel getIZhibbmModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
            getIZhibbmModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel1();
    }

    public IPropertySelectionModel getIZhibbmModels() {
        String sql = "select id,bianm from zhibb order by bianm";
        ((Visit) getPage().getVisit())
                .setProSelectionModel1(new IDropDownModel(sql, "��ѡ��"));
        return ((Visit) getPage().getVisit()).getProSelectionModel1();
    }

    // ָ������Model_end

    // �տλ
    public IDropDownBean getShoukdwValue() {
        if (((Visit) getPage().getVisit()).getDropDownBean7() == null) {
            ((Visit) getPage().getVisit())
                    .setDropDownBean7((IDropDownBean) getIShoukdwModel()
                            .getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean7();
    }

    public void setShoukdwValue(IDropDownBean Value) {

        ((Visit) getPage().getVisit()).setDropDownBean7(Value);
    }

    public void setIShoukdwModel(IPropertySelectionModel value) {

        ((Visit) getPage().getVisit()).setProSelectionModel7(value);
    }

    public IPropertySelectionModel getIShoukdwModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {

            getIShoukdwModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel7();
    }

    public IPropertySelectionModel getIShoukdwModels() {

        JDBCcon con = new JDBCcon();
        List List = new ArrayList();
        List list_tmp = new ArrayList();
        try {

            int i = -1;
            List.add(new IDropDownBean(i++, "��ѡ��"));
            String sql = " select shoukdw,kaihyh,zhangh from (	\n"
                    + " select distinct gongfdwmc as shoukdw,gongfkhyh as kaihyh,gongfzh as zhangh from hetb h where to_char(guoqrq,'yyyy')=to_char(sysdate,'yyyy')	\n"
                    + " union	\n"
                    + " select distinct mingc as shoukdw,kaihyh,zhangh from shoukdw	\n"
                    + " union	\n"
                    + " select gongfdwmc as shoukdw,gongfkhyh as kaihyh,gongfzh as zhangh from hetys where to_char(guoqrq,'yyyy')=to_char(sysdate,'yyyy')\n"
                    + " ) order by shoukdw";
            ResultSet rs = con.getResultSet(sql);
            while (rs.next()) {

                List.add(new IDropDownBean(i++, rs.getString("shoukdw")));

                String kaihyh = "";
                String zhangh = "";
                if(rs.getString("kaihyh")!=null){kaihyh=rs.getString("kaihyh");}
                if(rs.getString("zhangh")!=null){zhangh=rs.getString("zhangh");}
                list_tmp.add(new IDropDownBean(i++, rs.getString("shoukdw")+":"+kaihyh+","+zhangh));
            }
            rs.close();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
        ((Visit) getPage().getVisit())
                .setProSelectionModel7(new IDropDownModel(List));
        ((Visit) getPage().getVisit()).setList10(list_tmp);
        return ((Visit) getPage().getVisit()).getProSelectionModel7();
    }

    // �տλ_end

    // ��������
    public IDropDownBean getLiucmcValue() {
        if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
            ((Visit) getPage().getVisit())
                    .setDropDownBean10((IDropDownBean) getILiucmcModel()
                            .getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean10();
    }

    public void setLiucmcValue(IDropDownBean value) {

        if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {

            ((Visit) getPage().getVisit()).setDropDownBean10(value);
        }
    }

    public void setILiucmcModel(IPropertySelectionModel value) {

        ((Visit) getPage().getVisit()).setProSelectionModel10(value);
    }

    public IPropertySelectionModel getILiucmcModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

            getILiucmcModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel10();
    }

    public IPropertySelectionModel getILiucmcModels() {

        String sql = "select liucb.id,liucb.mingc from liucb,liuclbb where liucb.liuclbb_id=liuclbb.id and liuclbb.mingc='����' order by mingc";

        ((Visit) getPage().getVisit())
                .setProSelectionModel10(new IDropDownModel(sql, "��ѡ��"));
        return ((Visit) getPage().getVisit()).getProSelectionModel10();
    }

    // �糧����_end

    private String FormatDate(Date _date) {
        if (_date == null) {
            return DateUtil.Formatdate("yyyy-MM-dd", new Date());
        }
        return DateUtil.Formatdate("yyyy-MM-dd", _date);
    }

    private String getProperValue(IPropertySelectionModel _selectModel,
                                  int value) {
        int OprionCount;
        OprionCount = _selectModel.getOptionCount();
        for (int i = 0; i < OprionCount; i++) {
            if (((IDropDownBean) _selectModel.getOption(i)).getId() == value) {
                return ((IDropDownBean) _selectModel.getOption(i)).getValue();
            }
        }
        return null;
    }

    private long getProperId(IPropertySelectionModel _selectModel, String value) {
        int OprionCount;
        OprionCount = _selectModel.getOptionCount();

        for (int i = 0; i < OprionCount; i++) {
            if (((IDropDownBean) _selectModel.getOption(i)).getValue().equals(
                    value)) {
                return ((IDropDownBean) _selectModel.getOption(i)).getId();
            }
        }
        return -1;
    }

    // ������_begin

    public Toolbar getToolbar() {
        return ((Visit) this.getPage().getVisit()).getToolbar();
    }

    public void setToolbar(Toolbar tb1) {
        ((Visit) this.getPage().getVisit()).setToolbar(tb1);
    }

    public String getToolbarScript() {
        return getToolbar().getRenderScript();
    }

    // ������_end
    protected String getpageLinks() {
        String PageLink = "";
        IRequestCycle cycle = this.getRequestCycle();
        if (cycle.isRewinding())
            return "";
        String _servername = cycle.getRequestContext().getRequest()
                .getServerName();
        String _scheme = cycle.getRequestContext().getRequest().getScheme();
        int _ServerPort = cycle.getRequestContext().getRequest()
                .getServerPort();
        if (_ServerPort != 80) {
            PageLink = _scheme + "://" + _servername + ":" + _ServerPort
                    + this.getEngine().getContextPath();
        } else {
            PageLink = _scheme + "://" + _servername
                    + this.getEngine().getContextPath();
        }
        return PageLink;
    }


    private Balances_variable getDanpczzk(Balances_variable bsv){
        long fhid = 0;
        double qnet_ar = 0;
        double stad = 0;
        String zhib = "";
        double shangx = 0;
        double xiax = 0;
        String tiaojdw = "";
        String danw = "";
        String koujdw = "";
        String zengjdw = "";
        String jisdw = "";
        double jis = 0;
        double kouj = 0;
        double zengfj = 0;
        long xiaoscl = 0;
        double jiesl = 0;

        double zongkhje_qnet_ar = 0;
        double zongkhje_stad = 0;

        JDBCcon con = new JDBCcon();
        ResultSetList rs = con.getResultSetList("select fh.jingz,fh.shuiftzl,fh.id,zl.qnet_ar ,zl.stad  from fahb fh,zhilb zl where fh.zhilb_id = zl.id and fh.LIE_ID in ("+bsv.getSelIds()+")");
        while(rs.next()){
            jiesl = rs.getDouble("jingz") - rs.getDouble("shuiftzl");
            fhid = rs.getLong("fhid");
            qnet_ar = rs.getDouble("qnet_ar");
            stad = rs.getDouble("stad");
            ResultSetList rsl=con.getResultSetList("select zb.BIANM ,zkk.shangx ,zkk.xiax, tj.BIANM tjbm, dw.BIANM dwbm , kjdw.BIANM kjbm , zjdw.bianm zjbm ,jsdw.bianm jsbm, zkk.jis, zkk.kouj, zkk.ZENGFJ ,zkk.xiaoscl , zkk.jijlx \n" +
                    " from hetb ht , HETJGB jg ,HETZKKB zkk , HETJSXSB jsxs1 , HETJSXSB jsxs2 ,zhibb zb ,\n" +
                    "tiaojb tj ,danwb kjdw ,danwb zjdw,DANWB dw,danwb jsdw\n" +
                    "where ht.ID = jg.HETB_ID and ht.id = zkk.HETB_ID  and jg.HETJSXSB_ID = jsxs1.ID and zkk.HETJSXSB_ID = jsxs2.id\n" +
                    "and zkk.ZHIBB_ID = zb.ID and zkk.TIAOJB_ID = tj.id and zkk.KOUJDW = kjdw.ID(+) and zkk.ZENGFJDW = zjdw.ID(+) and zkk.DANWB_ID =dw.ID and zkk.JISDWID =jsdw.id\n" +
                    "and jsxs1.BIANM = '"+Locale.jiaqpj_jiesxs+"'  and jsxs2.bianm = '"+Locale.danpc_jiesxs+"' and ht.id = "+bsv.getHetb_Id()+"");
            while(rsl.next()){
                zhib = rsl.getString("bianm");//ָ��
                shangx = rsl.getDouble("shangx");//����
                xiax = rsl.getDouble("xiax");//����
                tiaojdw = rsl.getString("tjbm");//����
                danw = rsl.getString("dwbm");//��λ
                koujdw = rsl.getString("kjbm");//�ۼ۵�λ
                zengjdw = rsl.getString("zjbm");//���۵�λ
                jisdw = rsl.getString("jsbm");//������λ
                jis = rsl.getDouble("jis");//����
                kouj = rsl.getDouble("kouj");//�ۼ�
                zengfj = rsl.getDouble("zengfj");//����
                xiaoscl = 8;
                if(rsl.getLong("xiaoscl") != -1){
                    xiaoscl =rsl.getLong("xiaoscl");//С��λ����
                }
                if(zhib.equals("Qnetar")){
                    //����
//					��ֵ��ת���ɴ󿨼���
                    //jijlxĬ�ϲ��ú�˰�ۡ����ۼ۵�λĬ�ϲ��ö�/Ԫ
                    if(!danw.equals("ǧ��ǧ��")){
                        shangx = CustomMaths.Round_New (shangx * 1000 /29.271,0);
                        xiax = CustomMaths.Round_New (xiax * 1000 /29.271,0);
                        qnet_ar = CustomMaths.Round_New (qnet_ar * 1000 /29.271,0);
                    }
                    if(!jisdw.endsWith("ǧ��ǧ��")){
                        jis = CustomMaths.Round_New (jis * 1000 /29.271,0);
                    }
                    if(tiaojdw.indexOf("С��")>=0){
                        //ȡ����ֵ���ۼ�
                        if(qnet_ar < shangx){
                            zongkhje_qnet_ar += CustomMaths.Round_new((qnet_ar-shangx)/jis,Integer.parseInt(String.valueOf(xiaoscl))) * kouj * jiesl;
                        }

                    }else if(tiaojdw.indexOf("����")>=0){
                        //ȡ����ֵ������
                        if(qnet_ar > xiax){
                            zongkhje_qnet_ar += CustomMaths.Round_new((qnet_ar-xiax)/jis,Integer.parseInt(String.valueOf(xiaoscl))) * zengfj * jiesl;
                        }
                    }

                }else if(zhib.equals("Stad")){
                    //���
                    if(tiaojdw.indexOf("С��")>=0){
                        //ȡ����ֵ������
                        if(stad < shangx){
                            zongkhje_stad += CustomMaths.Round_new((shangx - stad)/jis,Integer.parseInt(String.valueOf(xiaoscl))) * zengfj * jiesl;
                        }

                    }else if(tiaojdw.indexOf("����")>=0){
                        //ȡ����ֵ���ۼ�
                        if(stad > xiax){
                            zongkhje_stad += CustomMaths.Round_new((xiax - stad)/jis,Integer.parseInt(String.valueOf(xiaoscl))) * kouj * jiesl;
                        }
                    }

                }
            }
        }

        if(zongkhje_qnet_ar != 0){
            bsv.setBeiz(bsv.getBeiz() + "  ���������ο��˷���:" + CustomMaths.Round_new((0- zongkhje_qnet_ar),2));
        }

        if(zongkhje_stad != 0){
            bsv.setBeiz(bsv.getBeiz() + "  ��ֵ����ο��˷���:" + CustomMaths.Round_new((0 - zongkhje_stad),2));
        }

        //�����ܽ��
        double yingxdj = CustomMaths.Round_new((zongkhje_qnet_ar + zongkhje_stad)/(bsv.getJiessl()), 2);//��˰����
        bsv.setHansmj(CustomMaths.Round_New(bsv.getHansmj() + yingxdj,2));//��˰ú���
        bsv.setJiashj(CustomMaths.round(bsv.getHansmj() * bsv.getJiessl(), 2));//��˰ú��
        bsv.setJiakhj(CustomMaths.Round_new(bsv.getJiashj()/1.17,2));//����˰ú��
        bsv.setJine(bsv.getJiakhj());//���
        bsv.setJiaksk(CustomMaths.Round_New(bsv.getJiashj() - bsv.getJiakhj(),2));//˰��
        bsv.setBuhsmj(CustomMaths.Round_new(bsv.getJiakhj()/bsv.getJiessl(),8));//����˰ú���

        //�ϼƴ�д(ú��ϼƼ����˷Ѻϼ�)
        Money mn = new Money();
        bsv.setHejdx(mn.NumToRMBStr(bsv.getYunzfhj()+bsv.getJiashj()));
        //�ϼ�Сд
        bsv.setHej(bsv.getYunzfhj()+bsv.getJiashj());

        con.Close();
        return bsv;
    }

    private Balances_variable getBaozmkc(Balances_variable bsv){
        JDBCcon con = new JDBCcon();
        long baozl =0;
        double jiakje = 0;
        ResultSetList rsl = con.getResultSetList("select sum(bzm.baozl) baozl,sum(bzm.jiakje) jiakje from\n" +
                "(select gongysb_id ,max(fahrq) maxFahrq,min(fahrq) minFahrq from fahb where lie_id in ("+bsv.getSelIds()+") group by gongysb_id ) fh ,baozmxxb bzm\n" +
                "where fh.GONGYSB_ID = bzm.GONGYSB_ID and fh.maxFahrq >= bzm.ERIQ and fh.minFahrq <= bzm.BRIQ and bzm.JIESB_ID = 0");
        if(rsl.next()){
            baozl = rsl.getLong("baozl");
            jiakje = rsl.getDouble("jiakje");
        }

        if(baozl !=0){
            bsv.setBeiz(bsv.getBeiz() + "  ��װú������" +baozl);
        }
        if(jiakje !=0){
            bsv.setBeiz(bsv.getBeiz() + "  �ӿ۽�" +jiakje);
        }

        double yingxdj = CustomMaths.Round_new((baozl * bsv.getHansmj() + jiakje)/bsv.getJiessl(),2);

        bsv.setHansmj(CustomMaths.Round_New(bsv.getHansmj() - yingxdj,2));//��˰ú���
        bsv.setJiashj(CustomMaths.round(bsv.getHansmj() * bsv.getJiessl(), 2));//��˰ú��
        bsv.setJiakhj(CustomMaths.Round_new(bsv.getJiashj()/1.17,2));//����˰ú��
        bsv.setJine(bsv.getJiakhj());//���
        bsv.setJiaksk(CustomMaths.Round_New(bsv.getJiashj() - bsv.getJiakhj(),2));//˰��
        bsv.setBuhsmj(CustomMaths.Round_new(bsv.getJiakhj()/bsv.getJiessl(),8));//����˰ú���

        //�ϼƴ�д(ú��ϼƼ����˷Ѻϼ�)
        Money mn = new Money();
        bsv.setHejdx(mn.NumToRMBStr(bsv.getYunzfhj()+bsv.getJiashj()));
        //�ϼ�Сд
        bsv.setHej(bsv.getYunzfhj()+bsv.getJiashj());

        con.Close();
        return bsv;
    }
}