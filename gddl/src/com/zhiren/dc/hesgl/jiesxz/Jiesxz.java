package com.zhiren.dc.hesgl.jiesxz;
/*
    2009-6-16 ��
	zsj
	��visit�е�String13 ��� fahb_id��������һ��ҳ��
*/
/**
 * ������
 * 2012-08-14
 * �����������������Ӳ��������Ƿ񿼺�ȫˮ����������
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.LogManager;

import com.zhiren.common.ext.*;
import com.zhiren.dc.hesgl.jiesd.Dcbalance;
import org.apache.log4j.Logger;
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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.Locale;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.dc.hesgl.jiesd.Balances;
import com.zhiren.dc.hesgl.jiesd.Balances_variable;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;
import com.zhiren.dc.jilgl.Jilcz;

/**
 * @author ���پ�
 * 2009-10-16
 * ����������ϵͳ�����ӡ�һ�����Ƶ������㡷�������������㳧�� ������
 * �ڽ���ʱ���ݡ�һ�����Ƶ������㡷�����жϣ��г����ֳ����з���������δ�������ݽ��н��㣬
 * ���㵥�е糧ID��Ϊ�������㳧�����������õĵ糧ID��
 * @author ���پ�
 * 2009-10-19
 * �������ڸ�ҳ�����Ӳ���"jsdwid"�����㵥λid����ֵΪͳһ����ʱҪʹ�ú�ͬ���跽id��
 * ���磺Ҫ���ֹ�˾�ɹ������㣬��Ҫʹ�÷ֹ�˾�Ĳɹ��� ͬ���ʺ�ͬ���跽Ӧ���Ƿֹ�˾����ʱҳ�����õĲ���ֵӦΪ�ֹ�˾��id
 * ��������ñ�ʶ�������㵥λ��ʶ���ӵ�string15�У�
 * �����ж�������㵥λΪ�ֹ�˾
 * �����¸�ҳ�棬�ڱ���ʱ�ж�����Ƿֹ�˾���㣬
 * ��ô���������ݴ���"�ֹ�˾�ɹ���"�У��������"�糧�ɹ���"�С�
 * @author ���پ�
 * 2009-10-23
 * ���������ƶ�һ�����Ƶ�ҵ���֧�֣�
 * 1��ҳ�������ӳ����ѡcombox ������ �ܳ����ơ�
 * 2��ҳ�������������㳧combox Ϊ���յĽ��㵥λ��
 * <p>
 * huochaoyuan
 * 2010-01-16������ͬ���������ȡ��sql��ʹ����ϵͳ����ȡ���跽�Ƿֹ�˾�������ͬ
 * @author ���پ�
 * 2011-07-04
 * ��������Ժ����糧�����ƽ��������һ��lie_id��Ӧ���pinzb_id���ڽ���ʱ��Ҫ��¼ҳ��ѡ��Ʒ�֣��������������
 * 1������visit.string19 ��¼Ʒ��id
 * 2������������ Dcbalance�� Jieszbtz ��������ҳ��
 * 2��Dcbalance�� Jieszbtz ��������������Ӧ����
 */
/**
 * @author ���پ�
 * 2009-10-19
 * �������ڸ�ҳ�����Ӳ���"jsdwid"�����㵥λid����ֵΪͳһ����ʱҪʹ�ú�ͬ���跽id��
 * 		���磺Ҫ���ֹ�˾�ɹ������㣬��Ҫʹ�÷ֹ�˾�Ĳɹ��� ͬ���ʺ�ͬ���跽Ӧ���Ƿֹ�˾����ʱҳ�����õĲ���ֵӦΪ�ֹ�˾��id
 * 			��������ñ�ʶ�������㵥λ��ʶ���ӵ�string15�У�
 * 			�����ж�������㵥λΪ�ֹ�˾
 * 			�����¸�ҳ�棬�ڱ���ʱ�ж�����Ƿֹ�˾���㣬
 * 			��ô���������ݴ���"�ֹ�˾�ɹ���"�У��������"�糧�ɹ���"�С�
 * */
/**
 * @author ���پ�
 * 2009-10-23
 * ���������ƶ�һ�����Ƶ�ҵ���֧�֣�
 * 		1��ҳ�������ӳ����ѡcombox ������ �ܳ����ơ�
 * 		2��ҳ�������������㳧combox Ϊ���յĽ��㵥λ��
 * */
/**
 * huochaoyuan
 * 2010-01-16������ͬ���������ȡ��sql��ʹ����ϵͳ����ȡ���跽�Ƿֹ�˾�������ͬ
 */
/**
 * @author ���پ�
 * 2011-07-04
 * ��������Ժ����糧�����ƽ��������һ��lie_id��Ӧ���pinzb_id���ڽ���ʱ��Ҫ��¼ҳ��ѡ��Ʒ�֣��������������
 * 		1������visit.string19 ��¼Ʒ��id
 * 		2������������ Dcbalance�� Jieszbtz ��������ҳ��
 * 		2��Dcbalance�� Jieszbtz ��������������Ӧ����
 * */

/**
 * @author liht
 * 2011-12-29
 * ������������Ϣ��Ʒ�ֹ���String19���½������
 * 		1������visit.string20 ��¼Ʒ��id
 * 		2������������ Dcbalance�� Jieszbtz ��������ҳ��
 * 		2��Dcbalance�� Jieszbtz ��������������Ӧ����
 * */
public class Jiesxz extends BasePage implements PageValidateListener {
    private static Logger logger = org.apache.log4j.LogManager.getLogger(Jiesxz.class);
    private String msg = "";
    private static String sql = "";

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = MainGlobal.getExtMessageBox(msg, false);
    }

    protected void initialize() {
        msg = "";
    }

    private String scjsl = "";

    public String getScjsl() {

        return scjsl;
    }

    public void setScjsl(String value) {

        scjsl = value;
    }

    // ������
    public String getRiq1() {
        if (((Visit) this.getPage().getVisit()).getString5() == null || ((Visit) this.getPage().getVisit()).getString5().equals("")) {

            ((Visit) this.getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
        }
        return ((Visit) this.getPage().getVisit()).getString5();
    }

    public void setRiq1(String riq1) {

        if (((Visit) this.getPage().getVisit()).getString5() != null && !((Visit) this.getPage().getVisit()).getString5().equals(riq1)) {

            ((Visit) this.getPage().getVisit()).setString5(riq1);
            ((Visit) this.getPage().getVisit()).setboolean1(true);
        }
    }


    public String getRiq2() {
        if (((Visit) this.getPage().getVisit()).getString6() == null || ((Visit) this.getPage().getVisit()).getString6().equals("")) {

            ((Visit) this.getPage().getVisit()).setString6(DateUtil.FormatDate(new Date()));
        }
        return ((Visit) this.getPage().getVisit()).getString6();
    }

    public void setRiq2(String riq2) {

        if (((Visit) this.getPage().getVisit()).getString6() != null && !((Visit) this.getPage().getVisit()).getString6().equals(riq2)) {

            ((Visit) this.getPage().getVisit()).setString6(riq2);
            ((Visit) this.getPage().getVisit()).setboolean1(true);
        }

    }

    //	��������ѡ��
    public String getRbvalue() {
        return ((Visit) this.getPage().getVisit()).getString1();
    }

    public void setRbvalue(String rbvalue) {
        ((Visit) this.getPage().getVisit()).setString1(rbvalue);
    }

//	����������ȡֵ(��ѡ)

    public String getChangb() {

        if (((Visit) this.getPage().getVisit()).getString3().equals("")) {

            ((Visit) this.getPage().getVisit()).setString3(
                    ((IDropDownBean) getFencbModel().getOption(0)).getValue());
        }

        return ((Visit) this.getPage().getVisit()).getString3();
    }

    public void setChangb(String changb) {
        ((Visit) this.getPage().getVisit()).setString3(changb);
    }

//	�����㵥λ

    public String getMainjsdw() {

        if (((Visit) this.getPage().getVisit()).getString16().equals("")) {

            ((Visit) this.getPage().getVisit()).setString16(
                    ((IDropDownBean) getFencbModel().getOption(0)).getValue());
        }

        return ((Visit) this.getPage().getVisit()).getString16();
    }

    public void setMainjsdw(String maindw) {
        ((Visit) this.getPage().getVisit()).setString16(maindw);
    }

    //	���䵥λ������
    public String getYunsdw() {
        return ((Visit) this.getPage().getVisit()).getString10();
    }

    public void setYunsdw(String yunsdw) {
        ((Visit) this.getPage().getVisit()).setString10(this.getYunsdwValue().getValue());
    }

    //	Ʒ��������
    private String _pinz = "";

    public String getPinz() {
        return _pinz;
    }

    public void setPinz(String pinz) {
        _pinz = pinz;
    }

//	���ձ��������ȡֵ

    public String getYansbh() {
        return ((Visit) this.getPage().getVisit()).getString4();
    }

    public void setYansbh(String yansbh) {
        ((Visit) this.getPage().getVisit()).setString4(yansbh);
    }

    //	Ԥ���㵥����
    public String getYujsdbm() {
        return ((Visit) this.getPage().getVisit()).getString20();
    }

    public void setYujsdbm(String yujsdbm) {
        ((Visit) this.getPage().getVisit()).setString20(yujsdbm);
    }

    //����۶���ȡֵ
    public String getJieskdl() {
        return ((Visit) this.getPage().getVisit()).getString7();
    }

    public void setJieskdl(String jieskdl) {
        ((Visit) this.getPage().getVisit()).setString7(jieskdl);
    }

    //����ۿ���ȡֵ
    public String getJieskkje() {
        return ((Visit) this.getPage().getVisit()).getString18();
    }

    public void setJieskkje(String jieskkje) {
        ((Visit) this.getPage().getVisit()).setString18(jieskkje);
    }

    //	Qnet,ar����
    private String _Qu = "";

    public String getQu() {
        return _Qu;
    }

    public void setQu(String qu) {

        _Qu = qu;
    }

    //	Qnet,ar����
    private String _Qd = "";

    public String getQd() {
        return _Qd;
    }

    public void setQd(String qd) {
        _Qd = qd;
    }

    //	Std����
    private String _Stdu = "";

    public String getStdu() {
        return _Stdu;
    }

    public void setStdu(String su) {
        _Stdu = su;
    }

    //	Std����
    private String _Stdd = "";

    public String getStdd() {
        return _Stdd;
    }

    public void setStdd(String sd) {
        _Stdd = sd;
    }

    //	Star����
    private String _Staru = "";

    public String getStaru() {
        return _Staru;
    }

    public void setStaru(String su) {
        _Staru = su;
    }

    //	Star����
    private String _Stard = "";

    public String getStard() {
        return _Stard;
    }

    public void setStard(String sd) {
        _Stard = sd;
    }

    //��Ʊ�˶�״̬
    public String getHuopztvalue() {
        return ((Visit) this.getPage().getVisit()).getString9();
    }

    public void setHuopztvalue(String huopztvalue) {
        ((Visit) this.getPage().getVisit()).setString9(huopztvalue);
    }

    // ҳ��仯��¼
    private String Change;

    public String getChange() {
        return Change;
    }

    public void setChange(String change) {
        Change = change;
    }

    private boolean _RefurbishChick = false;

    public void RefurbishButton(IRequestCycle cycle) {
        _RefurbishChick = true;
        if(this.getYunsdwValue().getId()==-1){
            this.getYunsdwModels();
        }
    }

    private void Refurbish() {
        //Ϊ  "ˢ��"  ��ť��Ӵ������
//    	getSelectData();
        ((Visit) getPage().getVisit()).setboolean4(true);    //�����ˢ�¡���ť
    }

    //
    private boolean _JiesChick = false;

    public void JiesButton(IRequestCycle cycle) {
        _JiesChick = true;
    }

    public void submit(IRequestCycle cycle) {
        if (_RefurbishChick) {
            _RefurbishChick = false;
            Refurbish();
        }

        if (_JiesChick) {
            _JiesChick = false;
             GotoJiesd(cycle);


        }
    }

    private void gotoYunfjsd(IRequestCycle cycle) {
        cycle.activate("Yunfjsd");
    }

    private void GotoJiesd(IRequestCycle cycle) {
        try {
            ExtGridUtil egu = this.getExtGrid();
            ResultSetList mdrsl = egu.getModifyResultSet(this.getChange());
            String mstr_lieid = "";
            String mstrchaij_lieid = "";
            String mstr_jieszbtz = "��";
            while (mdrsl.next()) {
                if (getJieslxValue().getId() == Locale.liangpjs_feiylbb_id
                        || getJieslxValue().getId() == Locale.guotyf_feiylbb_id
                        || getJieslxValue().getId() == Locale.dityf_feiylbb_id
                        || getJieslxValue().getId() == Locale.haiyyf_feiylbb_id
                        ) {//��Ʊ���㡢�����˷ѡ�����
                    if (mdrsl.getString("HUOPHDZT").equals("�����")) {
                        mstr_lieid += mdrsl.getString("ID") + ",";
                    } else if (!mdrsl.getString("YIHD_CHES").equals("0")) {
//        			Ҫ��ֵ���
                        if (getHuopztvalue().equals("weiwc")) {
                            mstrchaij_lieid = mdrsl.getString("ID");
                            setChaifl(mstrchaij_lieid);
                            mstr_lieid += ((Visit) getPage().getVisit()).getString8() + ",";
                        } else {
                            this.setMsg("��δ�˶Ի�Ʊ�����ܽ��н���");
                            return;
                        }
                    } else {
//                        if (getHuopztvalue().equals("weiwc")) {
                            mstr_lieid += mdrsl.getString("ID") + ",";
//                        } else {
//                            this.setMsg("��δ�˶Ի�Ʊ�����ܽ��н���");
//                            return;
//                        }
                    }
                } else {
                    mstr_lieid += mdrsl.getString("ID") + ",";
                }
            }
//    	����ѡ�����еõ���һ���ڵ�id
            String gongysb_id = getGongysmlttree_id();

            if (gongysb_id.equals("")) {

                gongysb_id = "0";
            }

            if (gongysb_id.indexOf(",") > -1) {

                gongysb_id = gongysb_id.substring(0, gongysb_id.indexOf(","));
            }

//    	�糧Id��ֵ

//    	���Ӷ�һ�����ơ�һ�����Ƶ�������೧����֧�֣���ϵͳ�����еġ�һ�����������㳧ID�����ɽ��㵥λid
//    		���������������


            if (((Visit) getPage().getVisit()).isFencb()) {
//
                ((Visit) getPage().getVisit()).setLong1(MainGlobal.getProperId(this.getFencbModel(), this.getMainjsdw()));
            } else {

                ((Visit) getPage().getVisit()).setLong1(((Visit) getPage().getVisit()).getDiancxxb_id());
            }

//    	������Lie_Id
            ((Visit) getPage().getVisit()).setString1(mstr_lieid.substring(0, mstr_lieid.lastIndexOf(",")));

//    	����Id
            ((Visit) getPage().getVisit()).setString13(Jiesdcz.getFahb_id_FromLie_id(sql, mstr_lieid.substring(0, mstr_lieid.lastIndexOf(","))));

//    	wangzb,��ͬʹ��,setString21Ҳ��¼����Id,��ֹ�ڽ��㵥����ʱgetString13�ò���fahb_idʱ,������getString21();
            ((Visit) getPage().getVisit()).setString21(Jiesdcz.getFahb_id_FromLie_id(sql, mstr_lieid.substring(0, mstr_lieid.lastIndexOf(","))));

//    	�������ͣ���Ʊ��ú��˷�...��
            ((Visit) getPage().getVisit()).setLong2(this.getJieslxValue().getId());

            //������λid
            ((Visit) getPage().getVisit()).setLong3(Long.parseLong(MainGlobal.getLeaf_ParentNodeId(getTree(), gongysb_id)));

            //����ʱ�Ŀ۶������ò��������Ѽ���������
            if (((Visit) getPage().getVisit()).getString7().trim().equals("")) {

                ((Visit) getPage().getVisit()).setString7("0");
            }

            if (((Visit) getPage().getVisit()).getString18().trim().equals("")) {

                ((Visit) getPage().getVisit()).setString18("0");
            }

//    	����ʱ���ϴν�������Ϊ���ۼ������������ۼ��ã�
            if (this.getScjsl().trim().equals("")) {

                ((Visit) getPage().getVisit()).setString12("0");
            } else {

                ((Visit) getPage().getVisit()).setString12(this.getScjsl());
            }

//    	��¼�½��㵥λ���õ�λ�����Ƿֹ�˾id��Ҳ�����ǵ糧id��רΪ����ͳһ����ʱʹ��
            if (((Visit) getPage().getVisit()).getString15().equals("")) {

                if (((Visit) getPage().getVisit()).isFencb()) {

                    ((Visit) getPage().getVisit()).setString15(String.valueOf(MainGlobal.getProperId(this.getFencbModel(), this.getMainjsdw())));

                } else {

                    ((Visit) getPage().getVisit()).setString15(String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()));
                }
            }

//    	��¼ҳ��ѡ���ȼ��Ʒ��ID
            if (!"".equals(this.getPinz())) {

                try {
                    ((Visit) getPage().getVisit()).setString19(String.valueOf(MainGlobal.getTableId("pinzb", "mingc", this.getPinz())));
                } catch (Exception e) {
                    // TODO �Զ����� catch ��
                    ((Visit) getPage().getVisit()).setString19("0");
                    e.printStackTrace();
                }
            }
//    	��¼Ԥ���㵥�Ľ�����
            if (!this.getYujsdbm().equals("")) {
                ((Visit) getPage().getVisit()).setString20(this.getYujsdbm());
            }
//    	����ѡ�з������hetb_idΪgetHthValue().getId()
            if (setFahb_HetbId(((Visit) getPage().getVisit()).getString1(), this.getHthValue().getId())) {

//    		��Hetb_id����Long8
                ((Visit) getPage().getVisit()).setLong8(this.getHthValue().getId());

//    		Ϊ��ֹgongysb_id=0ȡ�������÷���ʱ�����ؼ���gongysb_id���¸�ֵ�����
                ((Visit) getPage().getVisit()).setLong3(Jiesdcz.getGongysb_id(((Visit) getPage().getVisit()).getString1(),
                        ((Visit) getPage().getVisit()).getLong1(), ((Visit) getPage().getVisit()).getLong3(),
                        ((Visit) getPage().getVisit()).getLong8(), Double.parseDouble(((Visit) getPage().getVisit()).getString7())));

                mstr_jieszbtz = Jiesdcz.getJiessz_item(((Visit) getPage().getVisit()).getLong1(),
                        ((Visit) getPage().getVisit()).getLong3(), this.getHthValue().getId(), Locale.jieszbtz_jies, mstr_jieszbtz);

//    		ú�����н��㵥����ָ�����
                String flag_mstr = MainGlobal.getXitxx_item("����", Locale.jieszbtz_jies, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "��");
                if ("��".equals(flag_mstr)) {
                    String sql = "select leib from feiylbb f where f.id=" + getJieslxValue().getId();
                    JDBCcon con = new JDBCcon();
                    ResultSetList rsl1 = con.getResultSetList(sql);
                    if (rsl1.next()) {
                        if (rsl1.getInt("leib") == 0) {
                            mstr_jieszbtz = flag_mstr;
                        }
                    }
                    con.Close();
                }

                ((Visit) getPage().getVisit()).setString2(mstr_jieszbtz);

//    		���䵥λ��_id
                ((Visit) getPage().getVisit()).setLong9(this.getYunsdwValue().getId());

//    		�ж��Ƿ���Ҫָ�����
                if (mstr_jieszbtz.equals("��")) {
                    if (((Visit) getPage().getVisit()).getString17().trim().equals("tb")) {
//	    			������㷽ʽΪ�������ת�� DCBalance_tb
                        cycle.activate("DCBalance_tb");
                    } else {
                        if (MainGlobal.getXitxx_item("����", "�Ƿ���ʾ����ϵͳ������", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "��").equals("��")) {
//	    				˵����ɽ�����ǵ糧���˷ѽ������⣬��Ҫ��ѡ��Ĺ���ϵͳ(Aϵͳ��Bϵͳ)����������
                            ((Visit) getPage().getVisit()).setString19(getGuohxtValue().getValue());
                        }
                        cycle.activate("DCBalance");
                    }
                } else {
                    if (((Visit) getPage().getVisit()).getString17().trim().equals("tb")) {
//	    			������㷽ʽΪ�������ת�� DCBalance_tb
                        cycle.activate("DCBalance_tb");
                    }
//	    		��Ҫָ�����
//	    		1��ϵͳ��Ϣ�в�ʹ�����ձ��
                    ((Visit) getPage().getVisit()).setString4("");
//	    		2��ϵͳ��Ϣ��ʹ�����ձ��
                    ((Visit) getPage().getVisit()).setString4((this.getYansbh().equals("") ? "" : this.getYansbh()));
                    String s = MainGlobal.getXitxx_item("����", "�����ͬר��", ((Visit) getPage().getVisit()).getDiancxxb_id(), "");
                    if ("��".equals(s)) {
                        JDBCcon con = new JDBCcon();
                        con.setAutoCommit(false);
                        try {
                            String sql0 = "select getnewid(300) as yansbhb_id from dual";
                            //�õ�һ��yansbhb��id
                            ResultSetList rsl0 = con.getResultSetList(sql0);
                            rsl0.next();
                            //�������ձ�ű�
                            String sql = "insert into yansbhb (id,bianm,lie_id) values(" + rsl0.getLong("yansbhb_id") + "," + MainGlobal.getYansbh() + ",'" + ((Visit) getPage().getVisit()).getString1() + "')";

                            con.getInsert(sql);
                            //	    		//����fahb��yansbhb_id
                            String sql1 = "update fahb f set f.yansbhb_id=" + rsl0.getLong("yansbhb_id") + " where lie_id in ("
                                    + ((Visit) getPage().getVisit()).getString1() + ")";
                            con.getUpdate(sql1);
                            //����jieszbsjb
                            mstr_jieszbtz = MainGlobal.getXitxx_item("����", "����������ɷ�ʽ", ((Visit) getPage().getVisit()).getDiancxxb_id(), "maoz-piz-koud");

                            String sql2 = "select sum(" + mstr_jieszbtz + ") from chepb c where c.fahb_id in(select id from fahb f where f.lie_id in ("
                                    + ((Visit) getPage().getVisit()).getString1() + "))";
                            String sql3 = "select round(sum((" + mstr_jieszbtz + ")*qnet_ar)/sum(" + mstr_jieszbtz + "),2) " +
                                    "from chepb c,(select f.id fid,z.* from fahb f,zhilb z " +
                                    "				where  f.zhilb_id=z.id and f.lie_id in ("
                                    + ((Visit) getPage().getVisit()).getString1() + ")) g " +
                                    "where c.fahb_id=g.fid";

                            String sql4 = "select round(sum((" + mstr_jieszbtz + ")*stad)/sum(" + mstr_jieszbtz + "),2) " +
                                    "from chepb c,(select f.id fid,z.* from fahb f,zhilb z " +
                                    "				where  f.zhilb_id=z.id and f.lie_id in ("
                                    + ((Visit) getPage().getVisit()).getString1() + ")) g " +
                                    "where c.fahb_id=g.fid";

                            String sql5 = "select round(sum((" + mstr_jieszbtz + ")*mt)/sum(" + mstr_jieszbtz + "),2) " +
                                    "from chepb c,(select f.id fid,z.* from fahb f,zhilb z " +
                                    "				where  f.zhilb_id=z.id and f.lie_id in ("
                                    + ((Visit) getPage().getVisit()).getString1() + ")) g " +
                                    "where c.fahb_id=g.fid";

                            String sql6 = "select sum(c.biaoz) from fahb f,chepb c where c.fahb_id=f.id and f.lie_id in ("
                                    + ((Visit) getPage().getVisit()).getString1() + ")";

                            String sql7 = "begin \n" +
                                    "insert into jieszbsjb (id,jiesdid,zhibb_id,gongf,changf,jies,yingk,zhejbz,zhejje,zhuangt,yansbhb_id) \n" +
                                    "values (getnewid(300),0,1,(" + sql6 + "),(" + sql2 + "),0,0,0,0,0," + rsl0.getLong("yansbhb_id") + ");\n" +

                                    "insert into jieszbsjb (id,jiesdid,zhibb_id,gongf,changf,jies,yingk,zhejbz,zhejje,zhuangt,yansbhb_id) \n" +
                                    "values (getnewid(300),0,2,0,(" + sql3 + "),0,0,0,0,0," + rsl0.getLong("yansbhb_id") + ");\n" +

                                    "insert into jieszbsjb (id,jiesdid,zhibb_id,gongf,changf,jies,yingk,zhejbz,zhejje,zhuangt,yansbhb_id) \n" +
                                    "values (getnewid(300),0,13,0,(" + sql4 + "),0,0,0,0,0," + rsl0.getLong("yansbhb_id") + ");\n" +

                                    "insert into jieszbsjb (id,jiesdid,zhibb_id,gongf,changf,jies,yingk,zhejbz,zhejje,zhuangt,yansbhb_id) \n" +
                                    "values (getnewid(300),0,6,0,(" + sql5 + "),0,0,0,0,0," + rsl0.getLong("yansbhb_id") + ");\n end;";
                            con.getInsert(sql7);
                            con.commit();
                        } catch (Exception e) {
                            con.rollBack();
                        } finally {
                            con.Close();
                        }

                    }


                    cycle.activate("Jieszbtz");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setChaifl(String mstrchaij_lieid) {
        // TODO �Զ����ɷ������
//		����У��ҵ�Ҫ��ֵ���
        JDBCcon con = new JDBCcon();
        try {

            long lngFahb_id = 0;
            String sql = "select id from fahb f where f.lie_id=" + mstrchaij_lieid;
            ResultSetList rsl = con.getResultSetList(sql);
            while (rsl.next()) {

                lngFahb_id = rsl.getLong("id");
                Chaif(lngFahb_id);
            }

        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
    }

    private void Chaif(long lngFahb_id) {
//		ʵ�ʲ�ַ�����
        JDBCcon con = new JDBCcon();
        try {
            String strNew_FahbId = "";
            StringBuffer sqlbf = new StringBuffer("begin 		\n");

//			1�����ȱ���Ҫ��ֵķ���,�����µķ���
            strNew_FahbId = Jilcz.CopyFahb(con, String.valueOf(lngFahb_id), ((Visit) getPage().getVisit()).getDiancxxb_id());
            sqlbf.append(" update fahb set lie_id=" + strNew_FahbId + " where id=" + strNew_FahbId + "; 	\n");


//			2�������Ѻ˶Ե�chepb��fahb_idΪ�·�����id
            sqlbf.append(" update chepb set fahb_id=" + strNew_FahbId + " where id in (select cp.id from chepb cp,fahb f,danjcpb dj 	\n");
            sqlbf.append(" 		where f.id=cp.fahb_id and dj.chepb_id=cp.id and f.id=" + lngFahb_id + ");	\n");
            sqlbf.append("end;	");

            con.getUpdate(sqlbf.toString());

//			3�������·�����
            Jilcz.updateFahb(con, strNew_FahbId);

//			4�������Ϸ�����
            Jilcz.updateFahb(con, String.valueOf(lngFahb_id));

            if (((Visit) getPage().getVisit()).getString8().equals("")) {

                ((Visit) getPage().getVisit()).setString8(strNew_FahbId);
            } else {

                ((Visit) getPage().getVisit()).setString8("," + strNew_FahbId);
            }

        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
    }

    private boolean setFahb_HetbId(String FahbSelLieId, long HetbId) {
        // TODO �Զ����ɷ������
//		����ѡ�з������hetb_idΪgetHthValue().getId()
        boolean flag = true;
        if (getJieslxValue().getId() == Locale.liangpjs_feiylbb_id
                || getJieslxValue().getId() == Locale.meikjs_feiylbb_id
                ) {
            JDBCcon con = new JDBCcon();
            flag = false;
            String sql = "update fahb set hetb_id=" + HetbId + " where lie_id in (" + FahbSelLieId + ")";
            if (con.getUpdate(sql) >= 0) {

                flag = true;
            }
            con.Close();
        }

        return flag;
    }

    public void getSelectData() {
        Visit visit = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
//		 ********************������************************************************
        String biaoq = "��������";
        String riq = "fahrq";
        String danx = "";
        String danx1 = "";
        String yihd = "";
        String weihd = "";
        String fencb = "";
        String yansbh = "";
        String huophdzt = "";
        String yunsdw = "";
        String pinz = "";
        String yujsdbm = "";
        String shangcjsl = "";    //�ϴν�����
        String tiaoj_returnvalue_changb = "";
        String tiaoj_returnvalue_yansbh = "";
        String tiaoj_returnvalue_huophdzt = "";
        String tiaoj_returnvalue_yunsdw = "";
        String tiaoj_returnvalue_pinz = "";
        String tiaoj_returnvalue_yujsdbm = "";
        String tiaoj_returnvalue_shangcjsl = "";
        String where_rownum = "";
        String jieskdl = " ,{  \n"
                + "	\titems:jieskdl=new Ext.form.Field({	\n"
                + "	\twidth:100,	\n"
                + " \tfieldLabel: '����۶���',\n"
                + " \tselectOnFocus:true,	\n"
                + " \tvalue:document.getElementById('Jieskdl').value,	\n"
                + " \ttransform:'Jieskdl',	\n"
                + " \tlazyRender:true,	\n"
                + "	\ttriggerAction:'all',	\n"
                + " \ttypeAhead:true,	\n"
                + " \tforceSelection:true,	\n"
                + " \teditable:false,	\n"
                + "	\tlisteners:{select:function(own,rec,index){Ext.getDom('Jieskdl').selectedIndex=index}}"
                + "	\t})}";

        String zhi = "��";
        if (getJieslxValue().getValue().equals(Locale.guotyf_feiylbb) || getJieslxValue().getValue().equals(Locale.dityf_feiylbb)) {
            zhi = MainGlobal.getXitxx_item("����", "�Ƿ���ʾ����ϵͳ������", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "��");
        }
//
//        String Qnet =
//        	",{ \n" +
//        	" \t\titems: [{\n" +
//        	" \t\t\txtype:'textfield',\n" +
//        	" \t\t\tfieldLabel:'Qnet,ar(mj/kg)',\n" +
//        	"\t\t\twidth:0\n" +
//        	"\t\t } ]}";
//

        //region EXT
        String jieskkje = " ,{  \n"
                + "	\titems:jieskkje=new Ext.form.Field({	\n"
                + "	\twidth:100,	\n"
                + " \tfieldLabel: '����ۿ���',\n"
                + " \tselectOnFocus:true,	\n"
                + " \tvalue:document.getElementById('Jieskkje').value,	\n"
                + " \ttransform:'Jieskkje',	\n"
                + " \tlazyRender:true,	\n"
                + "	\ttriggerAction:'all',	\n"
                + " \ttypeAhead:true,	\n"
                + " \tforceSelection:true,	\n"
                + " \teditable:false,	\n"
                + "	\tlisteners:{select:function(own,rec,index){Ext.getDom('Jieskkje').selectedIndex=index}}"
                + "	\t})}";

        String Qd =
                ",{\n" +
                        "\t\t\titems:qd=new Ext.form.Field({\n" +
                        "\t\t\twidth:100,\n" +
                        " \t\t\tfieldLabel: 'Qnet,ar(mj/kg)����',\n" +
                        " \t\t\tvalue:document.getElementById('Qd').value,	\n" +
                        " \t\t\tselectOnFocus:true,\n" +
                        " \t\t\ttransform:'Qd',\n" +
                        " \t\t\tlazyRender:true,\n" +
                        "\t\t\ttriggerAction:'all',\n" +
                        " \t\t\ttypeAhead:true,\n" +
                        " \t\t\tforceSelection:true,\n" +
                        " \t\t\teditable:false,\n" +
                        "	listeners:{\n" +
                        "     'change':function(own,rec,index){\n" +
                        "        var obj1 = own.getRawValue();\n" +
                        "      \tvar num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" +
                        "\t\t\t\tif(!MatchNum(obj1 ,num )){\n" +
                        "\t\t\t\t\tqd.setRawValue(0);\n" +
                        "\t\t\t\t}\n" +
                        "      }\n" +
                        "}\n" +
                        "})}\n";

        String Qu =
                ",{\n" +
                        "\t\t\titems:qu=new Ext.form.Field({\n" +
                        "\t\t\twidth:100,\n" +
                        " \t\t\tfieldLabel: 'Qnet,ar(mj/kg)����',\n" +
                        " \t\t\tvalue:document.getElementById('Qu').value,	\n" +
                        " \t\t\tselectOnFocus:true,\n" +
                        " \t\t\ttransform:'Qu',\n" +
                        " \t\t\tlazyRender:true,\n" +
                        "\t\t\ttriggerAction:'all',\n" +
                        " \t\t\ttypeAhead:true,\n" +
                        " \t\t\tforceSelection:true,\n" +
                        " \t\t\teditable:false,\n" +
                        "	listeners:{\n" +
                        "     'change':function(own,rec,index){\n" +
                        "        var obj1 = own.getRawValue();\n" +
                        "      \tvar num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" +
                        "\t\t\t\tif(!MatchNum(obj1 ,num )){\n" +
                        "\t\t\t\t\tqu.setRawValue(0);\n" +
                        "\t\t\t\t}\n" +
                        "      }\n" +
                        "}\n" +
                        "})}\n";

        String Stard =
                ",{\n" +
                        "\t\t\titems:stard=new Ext.form.Field({\n" +
                        "\t\t\twidth:100,\n" +
                        " \t\t\tfieldLabel: 'Star(%)����',\n" +
                        " \t\t\tselectOnFocus:true,\n" +
                        " \t\t\tvalue:document.getElementById('Stard').value,	\n" +
                        " \t\t\ttransform:'Stard',\n" +
                        " \t\t\tlazyRender:true,\n" +
                        "\t\t\ttriggerAction:'all',\n" +
                        " \t\t\ttypeAhead:true,\n" +
                        " \t\t\tforceSelection:true,\n" +
                        " \t\t\teditable:false,\n" +
                        "	listeners:{\n" +
                        "     'change':function(own,rec,index){\n" +
                        "        var obj1 = own.getRawValue();\n" +
                        "      \tvar num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" +
                        "\t\t\t\tif(!MatchNum(obj1 ,num )){\n" +
                        "\t\t\t\t\tstard.setRawValue(0);\n" +
                        "\t\t\t\t}\n" +
                        "      }\n" +
                        "}\n" +
                        "})}\n";

        String Staru =
                ",{\n" +
                        "\t\t\titems:staru=new Ext.form.Field({\n" +
                        "\t\t\twidth:100,\n" +
                        " \t\t\tfieldLabel: 'Star(%)����',\n" +
                        " \t\t\tvalue:document.getElementById('Staru').value,	\n" +
                        " \t\t\tselectOnFocus:true,\n" +
                        " \t\t\ttransform:'Staru',\n" +
                        " \t\t\tlazyRender:true,\n" +
                        "\t\t\ttriggerAction:'all',\n" +
                        " \t\t\ttypeAhead:true,\n" +
                        " \t\t\tforceSelection:true,\n" +
                        " \t\t\teditable:false,\n" +
                        "	listeners:{\n" +
                        "     'change':function(own,rec,index){\n" +
                        "        var obj1 = own.getRawValue();\n" +
                        "      \tvar num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" +
                        "\t\t\t\tif(!MatchNum(obj1 ,num )){\n" +
                        "\t\t\t\t\tstaru.setRawValue(0);\n" +
                        "\t\t\t\t}\n" +
                        "      }\n" +
                        "}\n" +
                        "})}\n";
        String Stdd =
                ",{\n" +
                        "\t\t\titems:stdd=new Ext.form.Field({\n" +
                        "\t\t\twidth:100,\n" +
                        " \t\t\tfieldLabel: 'Std(%)����',\n" +
                        " \t\t\tvalue:document.getElementById('Stdd').value,	\n" +
                        " \t\t\tselectOnFocus:true,\n" +
                        " \t\t\ttransform:'Stdd',\n" +
                        " \t\t\tlazyRender:true,\n" +
                        "\t\t\ttriggerAction:'all',\n" +
                        " \t\t\ttypeAhead:true,\n" +
                        " \t\t\tforceSelection:true,\n" +
                        " \t\t\teditable:false,\n" +
                        "	listeners:{\n" +
                        "     'change':function(own,rec,index){\n" +
                        "        var obj1 = own.getRawValue();\n" +
                        "      \tvar num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" +
                        "\t\t\t\tif(!MatchNum(obj1 ,num )){\n" +
                        "\t\t\t\t\tstdd.setRawValue(0);\n" +
                        "\t\t\t\t}\n" +
                        "      }\n" +
                        "}\n" +
                        "})}\n";

        String Stdu =
                ",{\n" +
                        "\t\t\titems:stdu=new Ext.form.Field({\n" +
                        "\t\t\twidth:100,\n" +
                        " \t\t\tfieldLabel: 'Std(%)����',\n" +
                        " \t\t\tvalue:document.getElementById('Stdu').value,	\n" +
                        " \t\t\tselectOnFocus:true,\n" +
                        " \t\t\ttransform:'Stdu',\n" +
                        " \t\t\tlazyRender:true,\n" +
                        "\t\t\ttriggerAction:'all',\n" +
                        " \t\t\ttypeAhead:true,\n" +
                        " \t\t\tforceSelection:true,\n" +
                        " \t\t\teditable:false,\n" +
                        "	listeners:{\n" +
                        "     'change':function(own,rec,index){\n" +
                        "        var obj1 = own.getRawValue();\n" +
                        "      \tvar num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" +
                        "\t\t\t\tif(!MatchNum(obj1 ,num )){\n" +
                        "\t\t\t\t\tstdu.setRawValue(0);\n" +
                        "\t\t\t\t}\n" +
                        "      }\n" +
                        "}\n" +
                        "})}\n";
        //endregion


        if (getRbvalue().equals("fahrq") || this.getRbvalue().equals("")) {


            biaoq = "��������";
            riq = "fahrq";
            danx = "checked:true ,   \n";
        } else if (getRbvalue().equals("daohrq")) {
            biaoq = "��������";
            riq = "daohrq";
            danx1 = "checked:true ,   \n";

        }
//        if(visit.isFencb()==true){


        //��ͬ���⴦����ѡ�������㵥λΪ�����ͬ���ڻ�����ͬ����ʱ������ѡ����ͬ���ں͹����ͬ����
        StringBuffer listeners = new StringBuffer();
        String dtsqlString = "SELECT ID FROM xitxxb WHERE mingc = '��ͬ�����������⴦��' AND zhi = '��'";
        ResultSetList rslxitxx = con.getResultSetList(dtsqlString);
        if (rslxitxx.next()) {
            listeners.append("   ,listeners:{                                               \n");
            listeners.append("     'select':function(own,rec,index){                        \n");
            listeners.append("        if (own.getValue()== '301'){                          \n");
            listeners.append("            Changb.setValue('�����ͬһ��');                  \n");
            listeners.append("        } else {                                              \n");
            listeners.append("           Changb.setValue('�����ͬ����,�����ͬ����');      \n");
            listeners.append("        }                                                     \n");
            listeners.append("    }                                                         \n");
            listeners.append("   }                                                          \n");
        }
        fencb =
                ",{items:Changb=new Ext.zr.select.Selectcombo({\n" +
                        "   multiSelect:true,\n" +
                        "   width:100,\n" +
                        "   fieldLabel: '����',\n" +
                        "   transform:'FencbDropDown',\n" +
                        "   lazyRender:true,\n" +
                        "   triggerAction:'all',\n" +
                        "   typeAhead:true,\n" +
                        "   forceSelection:true \n" +
                        "})},\n" +
                        "{items:Mainjsdw=new Ext.form.ComboBox({\n" +
                        "\n" +
                        "   width:100,\n" +
                        "   fieldLabel: '�����㵥λ',\n" +
                        "   selectOnFocus:true,\n" +
                        "   transform:'MainjsdwDropDown',\n" +
                        "   lazyRender:true,\n" +
                        "   triggerAction:'all',\n" +
                        "   typeAhead:true,\n" +
                        "   forceSelection:true,\n" +
                        "   editable:false\n" +
                        listeners.toString() +
                        "})}";


        tiaoj_returnvalue_changb = "	document.getElementById('TEXT_CHANGB_VALUE').value=Changb.getRawValue();	\n"
                + "	document.getElementById('TEXT_MAINJSDW_VALUE').value=Mainjsdw.getRawValue();	\n";


        shangcjsl = ",{\n" +
                "      items:scjsl=new Ext.form.Field({\n" +
                "      width:100,\n" +
                "      fieldLabel: '�ϴν�����(��)',\n" +
                "      selectOnFocus:true,\n" +
                "	   value:document.getElementById('Scjsl').value, \n" +
                "      transform:'Scjsl',\n" +
                "      lazyRender:true,\n" +
                "      triggerAction:'all',\n" +
                "      typeAhead:true,\n" +
                "      forceSelection:true,\n" +
                " \t\t\teditable:false,\n" +
                "\t\t\tlisteners:{'change':function(own,rec,index){\n" +
                "\t\t        \t\tvar obj1 = own.getRawValue();\n" +
                "\t\t      \t\t\tvar num = /^[0-9]{0,7}\\.[0-9]{0,4}$|^[0-9]{0,7}$/;\n" +
                "\t\t\t\t\t\tif(!MatchNum(obj1 ,num )){\n" +
                "\t\t\t\t\t\t\tscjsl.setRawValue(0);\n" +
                "\t\t\t\t\t\t}\n" +
                "      \t\t\t\t}\n" +
                "\t\t\t}\n" +
                "})}";

        tiaoj_returnvalue_shangcjsl = " document.getElementById('Scjsl').value=scjsl.getRawValue();	\n";

//       }else{
//        	fencb="";
//        	tiaoj_returnvalue_changb="";
//        }
//    	if(MainGlobal.getXitxx_item("����", Locale.shiyysbh_jies, String.valueOf(visit.isFencb()?MainGlobal.getProperId(getFencbModel(), this.getChangb()):visit.getDiancxxb_id()), "��").equals("��")){
        tiaoj_returnvalue_yansbh = "document.getElementById('TEXT_YANSBH_VALUE').value=yansbh.getRawValue();";
        yansbh = "	,{\n"
                + " \titems:yansbh=new Ext.form.ComboBox({	\n"
                + "	\twidth:100,	\n"
                + " \tfieldLabel: '���ձ��',\n"
                + " \tselectOnFocus:true,	\n"
                + " \ttransform:'YansbhDropDown',	\n"
                + " \tlazyRender:true,	\n"
                + "	\ttriggerAction:'all',	\n"
                + " \ttypeAhead:true,	\n"
                + " \tforceSelection:true,	\n"
                + " \teditable:false,	\n"
                + "	\tlisteners:{select:function(own,rec,index){Ext.getDom('YansbhDropDown').selectedIndex=index}}"
                + "	\t})}";
//			}else{
//				yansbh="";
//			}
//         if(this.getJieslxValue().getId()==Locale.liangpjs_feiylbb_id
//        		 ||this.getJieslxValue().getId()==Locale.guotyf_feiylbb_id){

//        	 yunsdw=",{		\n"
//        		 + " xtype:'textfield',	\n"
//        		 + " fieldLabel:'���䵥λ',	\n"
//        		 + " width:0	\n"
//        		 + "}	\n";

        yunsdw = "	/*,{\n"
                + "items:YunsdwCb=new Ext.form.ComboBox({	\n"
                + "	width:100,	\n"
                + " fieldLabel: '���䵥λ',\n"
                + "	selectOnFocus:true,		\n"
                + "	transform:'YunsdwDropDown',		\n"
                + "	lazyRender:true,		\n"
                + " triggerAction:'all',	\n"
                + " typeAhead:true,		\n"
                + "	forceSelection:true,	\n"
                + "	editable:false		\n"
                + "	})}*/";

        tiaoj_returnvalue_yunsdw = "document.getElementById('TEXT_YUNSDW_VALUE').value=YunsdwCb.getRawValue();";

//        	 pinz=",{		\n"
//        		 + " xtype:'textfield',	\n"
//        		 + " fieldLabel:'Ʒ��',	\n"
//        		 + " width:0	\n"
//        		 + "}	\n";

        pinz = "	,{\n"
                + "items:PinzCb=new Ext.form.ComboBox({	\n"
                + "	width:100,	\n"
                + " fieldLabel: 'Ʒ��',\n"
                + "	selectOnFocus:true,		\n"
                + "	transform:'PinzDropDown',		\n"
                + "	lazyRender:true,		\n"
                + " triggerAction:'all',	\n"
                + " typeAhead:true,		\n"
                + "	forceSelection:true,	\n"
                + "	editable:false		\n"
                + "	})}";

        tiaoj_returnvalue_pinz = "document.getElementById('TEXT_PINZ_VALUE').value=PinzCb.getRawValue();";

        tiaoj_returnvalue_yujsdbm = "document.getElementById('TEXT_YUJSDBM_VALUE').value=yujsdbm.getRawValue();";
        yujsdbm = "	,{\n"
                + " \titems:yujsdbm=new Ext.form.ComboBox({	\n"
                + "	\twidth:100,	\n"
                + "	\tlistWidth:220,	\n"
                + " \tfieldLabel: 'Ԥ���㵥����',\n"
                + " \tselectOnFocus:true,	\n"
                + " \ttransform:'YujsdbmDropDown',	\n"
                + " \tlazyRender:true,	\n"
                + "	\ttriggerAction:'all',	\n"
                + " \ttypeAhead:true,	\n"
                + " \tforceSelection:true,	\n"
                + " \teditable:false,	\n"
                + "	\tlisteners:{select:function(own,rec,index){Ext.getDom('YujsdbmDropDown').selectedIndex=index}}"
                + "	\t})}";

        if (visit.getDiancxxb_id() == 323) {
            if (getHuopztvalue().equals("yiwc")) {
                yihd = "checked:true ,   \n";
            } else if (getHuopztvalue().equals("weiwc") || this.getHuopztvalue().equals("")) {
                weihd = "checked:true ,   \n";
            }
        } else {
            if (getHuopztvalue().equals("yiwc") || this.getHuopztvalue().equals("")) {
                yihd = "checked:true ,   \n";
            } else if (getHuopztvalue().equals("weiwc")) {
                weihd = "checked:true ,   \n";
            }
        }
        huophdzt = " ,{items: [{  \n"
                + " \txtype:'textfield',\n"
                + " \tfieldLabel:'��Ʊ�˶�״̬',\n"
                + "	\twidth:0	\n"
                + " },	\n"
                + " { \n"
                + "     xtype:'radio', \n"
                + "		boxLabel:'�Ѻ˶�', \n"
                + "     anchor:'95%', \n"
                + yihd
                + "     Value:'yiwc', \n"
                + "		name:'r2',\n"
                + "		listeners:{ \n"
//				+ "				'focus':function(r,c){\n"
//				+ "					document.getElementById('huopztvalue').value=r.Value;\n"
//				+ "				},\n"
                + "				'check':function(r,c){ \n"
                + "					var val='weiwc';if(c) val='yiwc';"
                + "					document.getElementById('huopztvalue').value=val;\n"
                + "					\tif(document.getElementById('TEXT_RADIO_HUOPHDZT_VALUE').value==''){	\n"
                + " 				\t	document.getElementById('TEXT_RADIO_HUOPHDZT_VALUE').value=document.getElementById('huopztvalue').value;} \n"
                + "					  }\n"
                + "		} \n"

                + "	},\n"
                + "	{  \n"
                + "     xtype:'radio', \n"
                + "		boxLabel:'δ�˶�',\n"
                + "		Value:'weiwc', \n"
                + "     anchor:'95%',	\n"
                + weihd
                + "		name:'r2'"
                + ",\n"
                + "		listeners:{ \n"
//				+ "				'focus':function(r,c){ \n"
//				+ "					document.getElementById('huopztvalue').value=r.Value;\n"
//				+ "				}, \n"
                +
                "'check':function(r,c){\n" +
                "            var val='yiwc';if(c) val='weiwc';\n" +
                "            document.getElementById('huopztvalue').value=val;\n" +
                "              if(document.getElementById('TEXT_RADIO_HUOPHDZT_VALUE').value==''){\n" +
                "                 document.getElementById('TEXT_RADIO_HUOPHDZT_VALUE').value=document.getElementById('huopztvalue').value;\n" +
                "               }\n" +
                "        }"
                + "		}	\n"
                + "}]}";

        tiaoj_returnvalue_huophdzt = "document.getElementById('TEXT_RADIO_HUOPHDZT_VALUE').value=document.getElementById('huopztvalue').value;";
//         }else{
//        	 huophdzt="";
//         }


        String Strtmpfunction =
                "var form =new Ext.FormPanel({\n" +
                        "\tlabelAlign:'left',buttonAlign:'right',bodyStyle:'padding:5px;',\n" +
                        "    frame:true,labelWidth:115,monitorValid:true,\n" +
                        "    items:[{\n" +
                        "      layout:'column',border:false,labelSeparator:':',\n" +
                        "        defaults:{layout: 'form',border:false,columnWidth:.5},\n" +
                        "        items:[\n" +
                        "              {items: [{\n" +
                        "              xtype:'textfield',\n" +
                        "              fieldLabel:'����ѡ��',\n" +
                        "              width:0\n" +
                        "            },{xtype:'radio',\n" +
                        "                    boxLabel:'��������',\n" +
                        "               anchor:'95%',\n" +
                        danx +
                        "               Value:'fahrq',\n" +
                        "            name:'test',\n" +
                        "            listeners:{\n" +
                        "                  'focus':function(r,c){\n" +
                        "                  document.getElementById('rbvalue').value=r.Value;\n" +
                        "                },\n" +
                        "                'check':function(r,c){\n" +
                        "                    document.getElementById('rbvalue').value=r.Value;\n" +
                        "                    if(document.getElementById('TEXT_RADIO_RQSELECT_VALUE')==''){\n" +
                        "                     document.getElementById('TEXT_RADIO_RQSELECT_VALUE')=document.getElementById('rbvalue').value;}\n" +
                        "                }\n" +
                        "            }\n" +
                        "          },{\n" +
                        "            xtype:'radio',\n" +
                        "            boxLabel:'��������',\n" +
                        "            Value:'daohrq',\n" +
                        "               anchor:'95%',\n" +
                        danx1 +
                        "            name:'test',\n" +
                        "            listeners:{\n" +
                        "                'focus':function(r,c){\n" +
                        "                document.getElementById('rbvalue').value=r.Value;\n" +
                        "            },\n" +
                        "            'check':function(r,c){\n" +
                        "                document.getElementById('rbvalue').value=r.Value;\n" +
                        "                if(document.getElementById('TEXT_RADIO_RQSELECT_VALUE')==''){\n" +
                        "                 document.getElementById('TEXT_RADIO_RQSELECT_VALUE')=document.getElementById('rbvalue').value;}\n" +
                        "            }\n" +
                        "          }\n" +
                        "        }]}\n" +
                        yansbh +
                        yunsdw +
                        pinz +
                        huophdzt +
                        jieskdl +
                        jieskkje +
//	    	Qnet +
                        Qd +
                        Qu +
                        Stdd +
                        Stard +
                        Stdu +
                        Staru +
                        fencb +
                        shangcjsl +
                        yujsdbm +
                        "\n" +
                        "        ]//items\n" +
                        "      }]//items\n" +
                        "});//FormPanel\n"


                        + " win = new Ext.Window({\n"
                        + " el:'hello-win',\n"
                        + "layout:'fit',\n"
                        + "width:500,\n"
                        + "height:420,\n"
                        + "closeAction:'hide',\n"
                        + "plain: true,\n"
                        + "title:'����',\n"
                        + "modal:true,"
                        + "items: [form],\n"

                        + "buttons: [{\n"
                        + "   text:'ȷ��',\n"
                        + "   handler:function(){  \n"
                        + "  		win.hide();\n"
                        + tiaoj_returnvalue_changb + "\n"
                        + tiaoj_returnvalue_yansbh + "\n"
                        + tiaoj_returnvalue_yujsdbm + "\n"
                        + "      	document.getElementById('Jieskdl').value=jieskdl.getRawValue();\n"
                        + "      	document.getElementById('Jieskkje').value=jieskkje.getRawValue();\n"
                        + "      	document.getElementById('Qd').value=qd.getRawValue();\n"
                        + "      	document.getElementById('Qu').value=qu.getRawValue();\n"
                        + "      	document.getElementById('Stdd').value=stdd.getRawValue();\n"
                        + "      	document.getElementById('Stdu').value=stdu.getRawValue();\n"
                        + "      	document.getElementById('Stard').value=stard.getRawValue();\n"
                        + "      	document.getElementById('Staru').value=staru.getRawValue();\n"
                        + tiaoj_returnvalue_shangcjsl + "\n"
                        + tiaoj_returnvalue_huophdzt + "\n"
                        + tiaoj_returnvalue_yunsdw + "\n"
                        + tiaoj_returnvalue_pinz + "\n"
                        + tiaoj_returnvalue_yujsdbm + "\n"
                        + "			document.getElementById('TEXT_RADIO_RQSELECT_VALUE').value=document.getElementById('rbvalue').value;	\n"
                        + " 		document.getElementById('RefurbishButton').click(); \n"
                        + "  	}   \n"
                        + "},{\n"
                        + "   	text: 'ȡ��',\n"
                        + "   	handler: function(){\n"
                        + "		win.hide();\n"
                        + "		document.getElementById('TEXT_CHANGB_VALUE').value='';	\n"
                        + "		document.getElementById('TEXT_MAINJSDW_VALUE').value='';	\n"
                        + "		document.getElementById('TEXT_YANSBH_VALUE').value='';	\n"
                        + "		document.getElementById('TEXT_YUNSDW_VALUE').value='';	\n"
                        + "		document.getElementById('TEXT_PINZ_VALUE').value='';	\n"
                        + "		document.getElementById('TEXT_YUJSDBM_VALUE').value='';	\n"
                        + "		document.getElementById('TEXT_RADIO_HUOPHDZT_VALUE').value='';	\n"
                        + "		document.getElementById('Jieskdl').value='';  \n"
                        + "		document.getElementById('Jieskkje').value='';  \n"
                        + "		document.getElementById('Qd').value='';  \n"
                        + "		document.getElementById('Qu').value='';  \n"
                        + "		document.getElementById('Stdd').value='';  \n"
                        + "		document.getElementById('Stdu').value='';  \n"
                        + "		document.getElementById('Stard').value='';  \n"
                        + "		document.getElementById('Staru').value='';  \n"
                        + "		document.getElementById('Scjsl').value='';  \n"
                        + "   }\n"
                        + "}]\n"

                        + " });win.show();win.hide();";


//      ��ȡ�������ñ��н������ò���
//      	1����������
//        	2�������Ȩ����
//        	3��������ʾָ��
//        	4��������������С��λ
//        	5����������ȡ����ʽ
//        	6��Mt����С��λ
//        	7��Mad����С��λ
//        	8��Aar����С��λ
//        	9��Aad����С��λ
//        	10��Adb����С��λ
//        	11��Vad����С��λ
//        	12��Vdaf����С��λ
//        	13��Stad����С��λ
//        	14��Std����С��λ
//        	15��Had����С��λ
//        	16��Qnetar����С��λ
//        	17��Qbad����С��λ
//        	18��Qgrad����С��λ
//        	19������ָ�����

        String jies_Jssl = "f.biaoz+f.yingk";
        String jies_Jqsl = "jingz";
        String jies_Jsxszb = "Mt,Qnetark,Std";
        String jies_Jsslblxs = "0";
        String jies_Jieslqzfs = "sum(round())";
        String jies_Mtblxs = "1";
        String jies_Madblxs = "2";
        String jies_Aarblxs = "2";
        String jies_Aadblxs = "2";
        String jies_Adblxs = "2";
        String jies_Vadblxs = "2";
        String jies_Vdafblxs = "2";
        String jies_Stadblxs = "2";
        String jies_Starblxs = "2";
        String jies_Stdblxs = "2";
        String jies_Hadblxs = "2";
        String jies_Qnetarblxs = "2";
        String jies_Qbadblxs = "2";
        String jies_Qgradblxs = "2";

        long feiylbb_id = 0;
        String strDiancxxb_id = "0";

        if (getJieslxValue().getId() == Locale.liangpjs_feiylbb_id
                || getJieslxValue().getId() == Locale.guotyf_feiylbb_id
                || getJieslxValue().getId() == Locale.haiyyf_feiylbb_id) {
//				�������ת�����������Ʊ������ǹ���������ôͳͳת��Ϊ�����˷ѡ�
            feiylbb_id = Locale.guotyf_feiylbb_id;
        } else {

            feiylbb_id = getJieslxValue().getId();
        }

        String YunsfsWhere = "";
        if (!((Visit) getPage().getVisit()).getString11().equals("")) {

            if (((Visit) getPage().getVisit()).getString11().equals(Locale.yunsfspy_tiel)) {
//	    		   ��·
                YunsfsWhere = " and f.yunsfsb_id=1 ";
            } else if (((Visit) getPage().getVisit()).getString11().equals(Locale.yunsfspy_gongl)) {
//	    		   ��·
                YunsfsWhere = " and f.yunsfsb_id=2 ";
            } else if (((Visit) getPage().getVisit()).getString11().equals(Locale.yunsfspy_haiy)) {
//	    		   ����
                YunsfsWhere = " and f.yunsfsb_id=3 ";
            } else if (((Visit) getPage().getVisit()).getString11().equals(Locale.yunsfspy_pidc)) {
//	    		   Ƥ����
                YunsfsWhere = " and f.yunsfsb_id=4 ";
            }
        }

        if (((Visit) getPage().getVisit()).getboolean4()) {
//	    	   �����ˢ�¡���ť
            where_rownum = "";
            ((Visit) getPage().getVisit()).setboolean4(false);
        } else {

            where_rownum = "	and rownum=0 \n";
        }
        String sql_tmp = "";
        if (getJieslxValue().getId() == Locale.daozyf_feiylbb_id) {
//	    	   ��װ�˷ѽ���
            //region ��װ�˷ѽ���
            sql =

                    " select f.lie_id as id,decode(g.mingc,null,'�ϼ�',g.mingc) as fahdw,m.mingc as meikdw,		\n" +
                            " 		decode(min(to_char(f.fahrq,'yyyy-MM-dd')),max(to_char(f.fahrq,'yyyy-MM-dd')),	\n" +
                            "       min(to_char(f.fahrq,'yyyy-MM-dd')),\n" +
                            "       min(to_char(f.fahrq,'yyyy-MM-dd'))	\n" +
                            "       ||'--'||max(to_char(f.fahrq,'yyyy-MM-dd'))) as fahrq ," +
                            " 		decode(min(to_char(f.daohrq,'yyyy-MM-dd')),max(to_char(f.daohrq,'yyyy-MM-dd')),	\n" +
                            "       min(to_char(f.daohrq,'yyyy-MM-dd')),\n" +
                            "       min(to_char(f.daohrq,'yyyy-MM-dd'))	\n" +
                            "       ||'--'||max(to_char(f.daohrq,'yyyy-MM-dd'))) as daohrq ," +
                            "       c.mingc as faz,decode(g.mingc,null,'',count(cp.id)) as ches,decode(g.mingc,null,'',sum(cp.biaoz)) as biaoz,	\n" +
                            "		decode(g.mingc,null,'',sum(cp.yingk)) as yingk," +
                            "		decode(g.mingc,null,'',sum(cp.yuns)) as yuns,	\n" +
                            "       decode(g.mingc,null,'',sum(" + Locale.jing_jiesxz + ")) as jingz,decode(g.mingc,null,'',h.hetbh) as hetbh,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*round_new(z.qnet_ar," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + "))) as Qnetar,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(round_new(sum(f.jingz*round_new(z.qnet_ar," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + ")*1000/4.1816,0))) as Qnetark,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.std)/sum(f.jingz)," + jies_Stdblxs + "))) as Std,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.stad)/sum(f.jingz)," + jies_Stadblxs + "))) as Stad,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.star)/sum(f.jingz)," + jies_Starblxs + "))) as Star,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.vdaf)/sum(f.jingz)," + jies_Vdafblxs + "))) as Vdaf,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.mt)/sum(f.jingz)," + jies_Mtblxs + "))) as Mt,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.mad)/sum(f.jingz)," + jies_Madblxs + "))) as Mad,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.aar)/sum(f.jingz)," + jies_Aarblxs + "))) as Aar,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.aad)/sum(f.jingz)," + jies_Aadblxs + "))) as Aad,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.ad)/sum(f.jingz)," + jies_Adblxs + "))) as Ad,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.vad)/sum(f.jingz)," + jies_Vadblxs + "))) as Vad,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.had)/sum(f.jingz)," + jies_Hadblxs + "))) as Had,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*round_new(z.qbad," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + "))) as Qbad,\n" +
                            "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*round_new(z.qgrad," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + "))) as Qgrad\n" +
                            "       from fahb f,gongysb g,meikxxb m,hetb h,chezxxb c,zhilb z,daozcpb cp,yansbhb ys \n" + this.getSupplyTable(riq) + " \n" +
                            "       where f.gongysb_id=g.id and f.meikxxb_id=m.id 						\n" +
                            "       and f.hetb_id=h.id(+) and f.faz_id=c.id 							\n" +
                            "		and f.id=cp.fahb_id 												\n" +
                            "       and f.liucztb_id=1													\n" +
                            "		and f.yansbhb_id = ys.id(+)											\n" +
                            where_rownum +
//	     			���䷽ʽ����
                            YunsfsWhere +
                            this.getWhere(riq) +
                            this.getSupplyWhere() +
                            "       group by rollup(f.lie_id,g.mingc,m.mingc,h.hetbh,c.mingc)	\n" +
                            "       having not (grouping(c.mingc)=1 and grouping(f.lie_id)=0)			\n" +
                            "       order by g.mingc,fahrq";
            //endregion


        } else if (((Visit) getPage().getVisit()).getString11().equals(Locale.yunsfspy_haiy)) {
//	    	   ������䷽ʽ�Ǻ���,��Ҫ����ҳ����ʾ��ֵ������ ���������� ������վ��Ϊ�ۿ� ȥ������

            //region Description
            sql = " select f.lie_id as id,decode(g.mingc,null,'�ϼ�',g.mingc) as fahdw,m.mingc as meikdw,lcxx.mingc as chuanm,f.chec as hangc,		\n" +
                    " 		decode(min(to_char(f.fahrq,'yyyy-MM-dd')),max(to_char(f.fahrq,'yyyy-MM-dd')),	\n" +
                    "       min(to_char(f.fahrq,'yyyy-MM-dd')),\n" +
                    "       min(to_char(f.fahrq,'yyyy-MM-dd'))	\n" +
                    "       ||'--'||max(to_char(f.fahrq,'yyyy-MM-dd'))) as fahrq ," +
                    " 		decode(min(to_char(f.daohrq,'yyyy-MM-dd')),max(to_char(f.daohrq,'yyyy-MM-dd')),	\n" +
                    "       min(to_char(f.daohrq,'yyyy-MM-dd')),\n" +
                    "       min(to_char(f.daohrq,'yyyy-MM-dd'))	\n" +
                    "       ||'--'||max(to_char(f.daohrq,'yyyy-MM-dd'))) as daohrq ," +
                    "       c.mingc as faz,decode(g.mingc,null,'',count(cp.id)) as ches,decode(g.mingc,null,'',sum(f.biaoz)) as biaoz,	\n" + this.getSupplyCol() + "decode(g.mingc,null,'',sum(cp.yingk)) as yingk," +
                    "		decode(g.mingc,null,'',sum(cp.yuns)) as yuns,	\n" +
                    "       decode(g.mingc,null,'',sum(f.jingz)) as jingz,decode(g.mingc,null,'',h.hetbh) as hetbh,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*round_new(z.qnet_ar," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + "))) as Qnetar,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(round_new(sum(f.jingz*round_new(z.qnet_ar," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + ")*1000/4.1816,0))) as Qnetark,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.std)/sum(f.jingz)," + jies_Stdblxs + "))) as Std,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.stad)/sum(f.jingz)," + jies_Stadblxs + "))) as Stad,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.star)/sum(f.jingz)," + jies_Starblxs + "))) as Star,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.vdaf)/sum(f.jingz)," + jies_Vdafblxs + "))) as Vdaf,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.mt)/sum(f.jingz)," + jies_Mtblxs + "))) as Mt,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.mad)/sum(f.jingz)," + jies_Madblxs + "))) as Mad,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.aar)/sum(f.jingz)," + jies_Aarblxs + "))) as Aar,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.aad)/sum(f.jingz)," + jies_Aadblxs + "))) as Aad,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.ad)/sum(f.jingz)," + jies_Adblxs + "))) as Ad,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.vad)/sum(f.jingz)," + jies_Vadblxs + "))) as Vad,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.had)/sum(f.jingz)," + jies_Hadblxs + "))) as Had,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*round_new(z.qbad," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + "))) as Qbad,\n" +
                    "       decode(g.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*round_new(z.qgrad," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + "))) as Qgrad\n" +
                    "       from fahb f,luncxxb lcxx,gongysb g,meikxxb m,hetb h,chezxxb c,zhilb z,chepb cp,yansbhb ys \n" + this.getSupplyTable(riq) + " \n" +
                    "       where f.gongysb_id=g.id and f.meikxxb_id=m.id 						\n" +
                    "       and f.hetb_id=h.id(+) and f.faz_id=c.id 							\n" +
                    "		and f.id=cp.fahb_id 												\n" +
                    "       and f.liucztb_id=1													\n" +
                    "		and f.yansbhb_id = ys.id(+)											\n" +
                    "		and f.luncxxb_id = lcxx.id(+)										\n" +
                    where_rownum +
                    //     			���䷽ʽ����
                    YunsfsWhere +
                    this.getWhere(riq) +
                    this.getSupplyWhere() +
                    "       group by rollup(f.lie_id,g.mingc,m.mingc,lcxx.mingc,f.chec,h.hetbh,c.mingc)	\n" +
                    "       having not (grouping(c.mingc)=1 and grouping(f.lie_id)=0)			\n" +
                    "       order by g.mingc,fahrq";
            //endregion

        } else {
//---------------
            String guohxt = "";
            if (getJieslxValue().getValue().equals(Locale.guotyf_feiylbb) || getJieslxValue().getValue().equals(Locale.dityf_feiylbb)) {
                if (zhi.equals("��")) {
                    guohxt = "	and cp.zhongchh = '" + getGuohxtValue().getValue() + "'\n";
                }
            }

            //����ǹ��������糧����Ȩ����=����-ˮ�ֵ�����
	    	 /*  String fahbtmp="(select * from fahb) f";
	    	   if(MainGlobal.getXitxx_item("����", "���������������Ƿ����ˮ�ֿ��˵���", String.valueOf(visit.getDiancxxb_id()), "��").equals("��")){
	    		   fahbtmp="(select id, yuanid, diancxxb_id, gongysb_id, meikxxb_id, pinzb_id, faz_id, daoz_id,\n" +
  					"jihkjb_id, fahrq, daohrq, hetb_id, zhilb_id, jiesb_id,  yunsfsb_id, chec, maoz, piz,\n" +
  					" jingz - gethetsfkhl(id) jingz, biaoz, yingd, yingk, yuns, yunsl, koud,  kous, kouz, koum, zongkd, sanfsl,\n" +
  					" ches, tiaozbz, yansbhb_id, lie_id, yuandz_id,  yuanshdwb_id, kuangfzlb_id, liucb_id,\n" +
  					" liucztb_id, hedbz, beiz, ruccbb_id, leiid,  ditjsbz, ditjsb_id, lieid, laimsl, laimzl,\n" +
  					"  laimkc, jiancl, yundh, xiemkssj, xiemjssj, zhuanggkssj, zhuanggjssj, chex from fahb) f";
	    	   }*/
//	    	 String jieslx=getJieslxValue().getValue();
//	    	 System.out.println(jieslx);
            sql_tmp =" select f.lie_id as id," ;
                            if(getJieslxValue().getValue().equals("�����˷�")){
                                sql_tmp+=   "decode(y.mingc,null,'�ϼ�',y.mingc) as yunsdw," ;
                            }else{
                                sql_tmp+= "decode(g.mingc,null,'�ϼ�',g.mingc) as fahdw," ;
                            }


            sql_tmp+= "m.mingc as meikdw,	p.mingc pinz,	\n" +
                            " 		decode(min(to_char(f.fahrq,'yyyy-MM-dd')),max(to_char(f.fahrq,'yyyy-MM-dd')),	\n" +
                            "       min(to_char(f.fahrq,'yyyy-MM-dd')),\n" +
                            "       min(to_char(f.fahrq,'yyyy-MM-dd'))	\n" +
                            "       ||'--'||max(to_char(f.fahrq,'yyyy-MM-dd'))) as fahrq ," +
                            " 		decode(min(to_char(f.daohrq,'yyyy-MM-dd')),max(to_char(f.daohrq,'yyyy-MM-dd')),	\n" +
                            "       min(to_char(f.daohrq,'yyyy-MM-dd')),\n" +
                            "       min(to_char(f.daohrq,'yyyy-MM-dd'))	\n" +
                            "       ||'--'||max(to_char(f.daohrq,'yyyy-MM-dd'))) as daohrq ," +
                            "       c.mingc as faz," +
                                    "decode(m.mingc,null,'',count(cp.id)) as ches," +
                                    "decode(m.mingc,null,'',sum(cp.biaoz)) as biaoz,	\n" + this.getSupplyCol() +
                                    "decode(m.mingc,null,'',sum(cp.yingk)) as yingk," +
                            "		decode(m.mingc,null,'',sum(cp.yuns)) as yuns,	\n" +
                            "       decode(m.mingc,null,'',sum(" + Locale.jing_jiesxz + ")) as jingz,decode(m.mingc,null,'',h.hetbh) as hetbh,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*round_new(z.qnet_ar," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + "))) as Qnetar,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(round_new(sum(f.jingz*round_new(z.qnet_ar," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + ")*1000/4.1816,0))) as Qnetark,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.std)/sum(f.jingz)," + jies_Stdblxs + "))) as Std,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.stad)/sum(f.jingz)," + jies_Stadblxs + "))) as Stad,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.star)/sum(f.jingz)," + jies_Starblxs + "))) as Star,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.vdaf)/sum(f.jingz)," + jies_Vdafblxs + "))) as Vdaf,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.mt)/sum(f.jingz)," + jies_Mtblxs + "))) as Mt,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.mad)/sum(f.jingz)," + jies_Madblxs + "))) as Mad,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.aar)/sum(f.jingz)," + jies_Aarblxs + "))) as Aar,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.aad)/sum(f.jingz)," + jies_Aadblxs + "))) as Aad,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.ad)/sum(f.jingz)," + jies_Adblxs + "))) as Ad,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.vad)/sum(f.jingz)," + jies_Vadblxs + "))) as Vad,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.had)/sum(f.jingz)," + jies_Hadblxs + "))) as Had,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*round_new(z.qbad," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + "))) as Qbad,\n" +
                            "       decode(m.mingc,null,'',decode(sum(f.jingz),0,0,round_new(sum(f.jingz*round_new(z.qgrad," + visit.getFarldec() + "))/sum(f.jingz)," + visit.getFarldec() + "))) as Qgrad\n" +
                            "       from fahb f,gongysb g,meikxxb m,hetb h,chezxxb c,zhilb z,chepb cp,yansbhb ys,pinzb p,yunsdwb y\n" + this.getSupplyTable(riq) + " \n" +
                            "       where f.gongysb_id=g.id and f.meikxxb_id=m.id 	 and cp.yunsdwb_id=y.id(+)					\n" +
                            "       and f.hetb_id=h.id(+) and f.faz_id=c.id 							\n" +
                            "		and f.id=cp.fahb_id 												\n" +
                            "       and f.liucztb_id=1													\n" +
                            "		and f.yansbhb_id = ys.id(+)	and f.pinzb_id=p.id										\n" +
                            where_rownum +
//	     			���䷽ʽ����
                            YunsfsWhere +
                            this.getWhere(riq) +
                            this.getSupplyWhere() + guohxt ;


            logger.info("���䵥λid:"+this.getYunsdwValue());
            if(getJieslxValue().getValue().equals("�����˷�")){
                long yunsdw_id=this.getYunsdwValue().getId();
                if(yunsdw_id!=-1){
                    sql_tmp+=" and cp.yunsdwb_id="+yunsdw_id+"\n";
                }

                sql_tmp+=  " group by rollup(f.lie_id,y.mingc,m.mingc,p.mingc,h.hetbh,c.mingc) \n"+
                "       having not (grouping(c.mingc)=1 and grouping(f.lie_id)=0)			\n" +
                        "       order by y.mingc,m.mingc,fahrq";

            }else {
                sql_tmp+= "       group by rollup(f.lie_id,g.mingc,m.mingc,p.mingc,h.hetbh,c.mingc)	\n" +
                "       having not (grouping(c.mingc)=1 and grouping(f.lie_id)=0)			\n" +
                        "       order by g.mingc,fahrq";

            }

            String flag = MainGlobal.getXitxx_item("����", "���������ν���,����������,����chec������", String.valueOf(visit.getDiancxxb_id()), "��");
            if ("��".equals(flag)) {
                sql = "select sj.* from fahb f,(" + sql_tmp + ") sj where f.lie_id(+)=sj.id order by decode(substr(f.chec||'_0',2,9),'0','_999999999',substr(f.chec||'_0',2,9))";
            } else {
                sql = sql_tmp;
            }
        }


        ResultSetList rsl = con.getResultSetList(sql);
        String flag = MainGlobal.getXitxx_item("����", "���������ν���,����������,����chec������", String.valueOf(visit.getDiancxxb_id()), "��");
        if ("��".equals(flag)) {
            sql = sql_tmp;
        }
        ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
        egu.setTableName("fahb");
        egu.setDefaultsortable(false);
        egu.setWidth(Locale.Grid_DefaultWidth + "-5");// ����ҳ��Ŀ��,������������ʱ��ʾ������
        egu.getColumn("id").setHidden(true);
        if(getJieslxValue().getValue().equals("�����˷�")){
            egu.getColumn("yunsdw").setHeader("���䵥λ");
        }else {
            egu.getColumn("fahdw").setHeader(Locale.gongysb_id_fahb);
        }
        egu.getColumn("meikdw").setHeader(Locale.meikxxb_id_fahb);
        egu.getColumn("ches").setHeader("����");
        egu.getColumn("hetbh").setHeader("��ͬ��");
        egu.getColumn("pinz").setHeader("ú��");
        egu.getColumn("fahrq").setHeader("��������");
        egu.getColumn("daohrq").setHeader("��������");
        egu.getColumn("faz").setHeader("��վ");
        egu.getColumn("biaoz").setHeader("Ʊ��");
        egu.getColumn("yingk").setHeader("ӯ��");
        egu.getColumn("yuns").setHeader("����");
        egu.getColumn("jingz").setHeader("����");

        egu.getColumn("Std").setHeader("Std(%)");
        egu.getColumn("Mt").setHeader("Mt(%)");
        egu.getColumn("Mad").setHeader("Mad(%)");
        egu.getColumn("Aar").setHeader("Aar(%)");
        egu.getColumn("Aad").setHeader("Aad(%)");
        egu.getColumn("Ad").setHeader("Ad(%)");
        egu.getColumn("Vad").setHeader("Vad(%)");
        egu.getColumn("Vdaf").setHeader("Vdaf(%)");
        egu.getColumn("Stad").setHeader("Stad(%)");
        egu.getColumn("Star").setHeader("Star(%)");
        egu.getColumn("Had").setHeader("Had(%)");
        egu.getColumn("Qnetar").setHeader("Qnetar(mj/kg)");
        egu.getColumn("Qnetark").setHeader("Qnetar(kcal/kg)");
        egu.getColumn("Qbad").setHeader("Qbad(mj/kg)");
        egu.getColumn("Qgrad").setHeader("Qgrad(mj/kg)");

        egu.getColumn("Std").setHidden(true);
        egu.getColumn("Mt").setHidden(true);
        egu.getColumn("Mad").setHidden(true);
        egu.getColumn("Aar").setHidden(true);
        egu.getColumn("Aad").setHidden(true);
        egu.getColumn("Ad").setHidden(true);
        egu.getColumn("Vad").setHidden(true);
        egu.getColumn("Vdaf").setHidden(true);
        egu.getColumn("Star").setHidden(true);
        egu.getColumn("Stad").setHidden(true);
        egu.getColumn("Had").setHidden(true);
        egu.getColumn("Qnetar").setHidden(true);
        egu.getColumn("Qnetark").setHidden(true);
        egu.getColumn("Qbad").setHidden(true);
        egu.getColumn("Qgrad").setHidden(true);

        if (((Visit) getPage().getVisit()).getString11().equals(Locale.yunsfspy_haiy)) {
//			������䷽ʽ�Ǻ��ˣ�
            egu.getColumn("chuanm").setHeader("����");
            egu.getColumn("hangc").setHeader("����");
            egu.getColumn("faz").setHeader("�ۿ�");
            egu.getColumn("ches").setHidden(true);
        }

//		��ϵͳ��Ϣ���ж�̬���ø��ӵ���ʾָ��
        if (((Visit) getPage().getVisit()).isFencb()) {

            strDiancxxb_id = String.valueOf(MainGlobal.getProperId(this.getFencbModel(), this.getMainjsdw()));
        } else {
            strDiancxxb_id = String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
        }

        String Xitxxvalue = "";
        String Xitxx[] = null;
        Xitxxvalue = MainGlobal.getXitxx_item("����", Locale.jiesxzfjxszb_xitxx,
                strDiancxxb_id, "");

        Xitxx = Xitxxvalue.split(",");

        for (int i = 0; i < Xitxx.length; i++) {

            if (egu.getColumn(Xitxx[i]) != null) {

                egu.getColumn(Xitxx[i]).setHidden(false);
            }
        }
//		��ϵͳ��Ϣ���ж�̬���ø��ӵ���ʾָ��_end


//		ͨ��jiesszb�н���ָ����ʾ�ֶ���Ŀ���ã����ý���ѡ�����ʾָ��
        String xszb[] = jies_Jsxszb.split(",");

        for (int i = 0; i < xszb.length; i++) {

            egu.getColumn(xszb[i]).setHidden(false);
        }

//		ͨ���������������ֶε���ʾ���

        if (this.getJieslxValue().getId() == Locale.liangpjs_feiylbb_id
                || this.getJieslxValue().getId() == Locale.guotyf_feiylbb_id
                || this.getJieslxValue().getId() == Locale.dityf_feiylbb_id
                || this.getJieslxValue().getId() == Locale.haiyyf_feiylbb_id) {
//			��Ʊ����

//			������ʾ�ֶ�
            egu.getColumn("yihd_ches").setHeader("�Ѻ˶Գ���");
            egu.getColumn("yihd_biaoz").setHeader("�Ѻ˶�Ʊ��");
            egu.getColumn("Huophdzt").setHeader("��Ʊ�˶�״̬");
            egu.getColumn("Huophdzt").setHidden(true);
            egu.getColumn("Huophdzt").setRenderer("renderHdzt");

//			�����ֶο��
            egu.getColumn("yihd_ches").setWidth(70);
            egu.getColumn("yihd_biaoz").setWidth(70);
            egu.getColumn("Huophdzt").setWidth(80);
        }

        // �趨�г�ʼ���
//		egu.getColumn("id").setWidth(80);
        if(getJieslxValue().getValue().equals("�����˷�")){
            egu.getColumn("yunsdw").setWidth(100);
        }else {
            egu.getColumn("fahdw").setWidth(100);
        }


        egu.getColumn("meikdw").setWidth(130);
        egu.getColumn("pinz").setWidth(80);
        egu.getColumn("ches").setWidth(50);
        egu.getColumn("hetbh").setWidth(70);
        egu.getColumn("fahrq").setWidth(130);
        egu.getColumn("daohrq").setWidth(130);
        egu.getColumn("faz").setWidth(60);
        egu.getColumn("biaoz").setWidth(60);
        egu.getColumn("yingk").setWidth(50);
        egu.getColumn("yuns").setWidth(50);
        egu.getColumn("jingz").setWidth(60);
        egu.getColumn("Std").setWidth(55);
        egu.getColumn("Star").setWidth(55);
        egu.getColumn("Mt").setWidth(55);
        egu.getColumn("Mad").setWidth(60);
        egu.getColumn("Aar").setWidth(60);
        egu.getColumn("Aad").setWidth(60);
        egu.getColumn("Ad").setWidth(60);
        egu.getColumn("Vad").setWidth(60);
        egu.getColumn("Vdaf").setWidth(60);
        egu.getColumn("Stad").setWidth(60);
        egu.getColumn("Had").setWidth(60);
        egu.getColumn("Qnetar").setWidth(80);
        egu.getColumn("Qnetark").setWidth(80);
        egu.getColumn("Qbad").setWidth(80);
        egu.getColumn("Qgrad").setWidth(100);

        egu.setGridType(ExtGridUtil.Gridstyle_Read);// �趨grid���Ա༭
        egu.addPaging(0);// ���÷�ҳ

        egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
        egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));

        egu.addTbarText(biaoq);
        DateField df = new DateField();
        df.setReadOnly(true);
        df.setValue(this.getRiq1());
        df.Binding("RIQ1", "");// ��htmlҳ�е�id��,���Զ�ˢ��
        df.setId("RIQ1");

        egu.addToolbarItem(df.getScript());

        egu.addTbarText("��:");
        DateField df1 = new DateField();
        df1.setReadOnly(true);
        df1.setValue(this.getRiq2());
        df1.Binding("RIQ2", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
        df1.setId("RIQ2");
        egu.addToolbarItem(df1.getScript());

        egu.addTbarText("-");// ���÷ָ���

        // ������
        String condition = " and " + rb1() + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') and " + rb1() + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') ";

        ExtTreeUtil etu = new ExtTreeUtil("gongysTree",
                ExtTreeUtil.treeWindowCheck_gongys, ((Visit) this.getPage()
                .getVisit()).getDiancxxb_id(), getTreeid(), condition, true);

        String changbAndJiesdwValue = "";
        String RefurbishScript = "document.getElementById('RefurbishButton').click();";
        if (MainGlobal.getXitxx_item("����", "�Ƿ�Ϊɽ���ֹ�˾�ɹ����㵥", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "��").equals("��")) {

            egu.addTbarText("����:");
            ComboBox combcb = new ComboBox();
            combcb.setTransform("DiancDropDown");
            combcb.setId("dianc");
            combcb.setEditable(false);
            combcb.setLazyRender(true);
            combcb.setWidth(80);
            egu.addToolbarItem(combcb.getScript());
            egu.addOtherScript("dianc.on('select',function(){document.forms[0].submit();});");
            egu.addTbarText("-");

            changbAndJiesdwValue = "Mainjsdw.setRawValue(dianc.getRawValue()); Changb.setRawValue(dianc.getRawValue());";
            RefurbishScript = "document.getElementById('TEXT_CHANGB_VALUE').value=Changb.getRawValue(); document.getElementById('RefurbishButton').click();";

            etu = new ExtTreeUtil("gongysTree",
                    ExtTreeUtil.treeWindowCheck_gongys, getFencbValue().getId(), getTreeid(), condition, true);
        }

        egu.addTbarText(Locale.gongysb_id_fahb);
        setTree(etu);
        egu.addTbarTreeBtn("gongysTree");


        egu.addTbarText("-");// ���÷ָ���




        egu.addTbarText("���䵥λ:");
        ComboBox ys = new ComboBox();
        ys.setTransform("YunsdwDropDown");
        ys.setEditable(true);
        ys.setWidth(200);
        //ys.setListeners("select:function(){document.Form0.submit();}");
        egu.addToolbarItem(ys.getScript());

        egu.addTbarText("-");// ���÷ָ���


        //���ձ�Ÿ��ݲ�ͬ�û���������
        //
        egu.addTbarText("��������:");
        ComboBox comb4 = new ComboBox();
        comb4.setTransform("JieslxDropDown");
        comb4.setId("Jieslx");
        comb4.setEditable(false);
        comb4.setLazyRender(true);// ��̬��
        comb4.setWidth(80);
        comb4.setReadOnly(true);
        egu.addToolbarItem(comb4.getScript());
        //egu.addOtherScript("Jieslx.on('select',function(){document.forms[0].submit();});");

        egu.addTbarText("-");
        egu.addTbarText("��ͬ��:");
        ComboBox comb5 = new ComboBox();
        comb5.setTransform("HthDropDown");
        comb5.setId("Heth");
        comb5.setEditable(false);
        comb5.setLazyRender(true);// ��̬��
        comb5.setWidth(100);
        comb5.setListWidth(350);
        comb5.setReadOnly(true);
        egu.addToolbarItem(comb5.getScript());


        if (getJieslxValue().getValue().equals(Locale.guotyf_feiylbb) || getJieslxValue().getValue().equals(Locale.dityf_feiylbb)) {
            if (zhi.equals("��")) {
                egu.addTbarText("-");
                egu.addTbarText("ϵͳ��");
                ComboBox xit_comb = new ComboBox();
                xit_comb.setWidth(80);
                xit_comb.setTransform("Guohxt");
                xit_comb.setId("Guohxt");
                xit_comb.setLazyRender(true);
                xit_comb.setEditable(false);
                egu.addToolbarItem(xit_comb.getScript());
            }
        }

        // �趨�������������Զ�ˢ��
//		egu.addOtherScript("faz.on('select',function(){document.forms[0].submit();});");
        egu.addTbarText("-");// ���÷ָ���
        //egu.addToolbarButton(GridButton.ButtonType_ShowDIV, null);
        egu.addToolbarItem("{" + new GridButton("ˢ��", "function(){" + RefurbishScript + "}").getScript() + "}");
        egu.addTbarText("-");
        egu.addToolbarItem("{" + new GridButton("����", "function(){ if(win){ win.show(this);" + "}"
//				+ " win.show(this);	\n"
                + " \tif(document.getElementById('TEXT_CHANGB_VALUE').value!=''){	\n"
                + "		\tChangb.setRawValue(document.getElementById('TEXT_CHANGB_VALUE').value);	\n"
                + "	\t}	\n"
                + " \tif(document.getElementById('TEXT_MAINJSDW_VALUE').value!=''){	\n"
                + "		\tMainjsdw.setRawValue(document.getElementById('TEXT_MAINJSDW_VALUE').value);	\n"
                + "	\t}	\n" + changbAndJiesdwValue
                + " \tif(document.getElementById('TEXT_YANSBH_VALUE').value!=''){	\n"
                + "		\tyansbh.setRawValue(document.getElementById('TEXT_YANSBH_VALUE').value);	\n"
                + "	\t}	\n"
                + " \tif(document.getElementById('TEXT_YUNSDW_VALUE').value!=''){	\n"
                + "		\tYunsdwCb.setRawValue(document.getElementById('TEXT_YUNSDW_VALUE').value);	\n"
                + "	\t}	\n"
                + " \tif(document.getElementById('TEXT_PINZ_VALUE').value!=''){	\n"
                + "		\tPinzCb.setRawValue(document.getElementById('TEXT_PINZ_VALUE').value);	\n"
                + "	\t}	\n"
                + " \tif(document.getElementById('TEXT_YUJSDBM_VALUE').value!=''){	\n"
                + "		\tyujsdbm.setRawValue(document.getElementById('TEXT_YUJSDBM_VALUE').value);	\n"
                + "	\t}	\n"
                + "}").getScript() + "}");
        egu.addTbarText("-");

        if (((Visit) getPage().getVisit()).getString17().equals("tb")) {
//        	������㷽ʽΪ���ȡ������ʱ�Ժ�ͬ���ж�

            egu.addToolbarButton("����", GridButton.ButtonType_SubmitSel, "JiesButton");

        } else {

            String buttonName = "����";
            if (MainGlobal.getXitxx_item("����", "�Ƿ�Ϊɽ���ֹ�˾�ɹ����㵥", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "��").equals("��")) {
                buttonName = "����";
            }
            egu.addToolbarButton(buttonName, GridButton.ButtonType_SubmitSel_condition, "JiesButton", "if(Heth.getRawValue()=='��ѡ��'&&Jieslx.getRawValue()!='�����˷�'&&Jieslx.getRawValue()!='�����˷�'){	\n	"
                    + " Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ���ͬ��');	\n"
                    + " return ;	\n"
                    + " }");
        }


        egu.addOtherScript("function gridDiv_save(rec){	\n" +
                "	\tif(rec.get('FAHDW')=='�ϼ�'){	\n" +
                "	\treturn 'continue';	\n" +
                "	\t}}");

        // ---------------ҳ��js�ļ��㿪ʼ------------------------------------------
	/*	StringBuffer sb = new StringBuffer();

		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		// sb.append("if (e.row==7){e.cancel=true;}");//�趨��8�в��ɱ༭,�����Ǵ�0��ʼ��
		sb.append(" if(e.field=='GONGYSB_ID'){ e.cancel=true;}");// �趨��Ӧ���в��ɱ༭
		sb.append(" if(e.field=='DIANCXXB_ID'){ e.cancel=true;}");// �趨�糧��Ϣ�в��ɱ༭
		sb.append("});");

		egu.addOtherScript(sb.toString());*/

//      �õ����õļ�Ȩ����Ĭ��Ϊ����
        String jiaq = "";
        String value = MainGlobal.getXitxx_item("����", "����ѡ���Ȩֵ", visit.getDiancxxb_id() + "", "����");
        if (value.equals("Ʊ��")) {
            jiaq = "eval(rec[i].get('BIAOZ'))";
        } else if (value.equals("Ʊ��+ӯ��")) {
            jiaq = "eval(rec[i].get('BIAOZ'))+eval(rec[i].get('YINGK'))";
        } else if (value.equals("����+����")) {
            jiaq = "eval(rec[i].get('JINGZ'))+eval(rec[i].get('YUNS'))";
        } else {
            jiaq = "eval(rec[i].get('JINGZ'))";
        }

        toAllUpperOrLowerCase("");

        egu.addOtherScript("gridDiv_grid.on('rowclick',function(own,row,e){ \n"
                + " \tvar ches=0,biaoz=0,yihd_ches=0,yihd_biaoz=0,yingk=0,yuns=0,jingz=0,std=0, mt=0,mad=0,aar=0,aad=0,ad=0,vad=0,vdaf=0,stad=0,had=0,qnet_ar=0,qbad=0,qgrad_daf=0;	\n"
//				���ü�Ȩ����
                + "var jiaqz = 0;qnetar_jq=0;qnetark_jq = 0;std_jq=0;mt_jq=0;qnetar = 0;qnetark = 0;std1=0;mt1=0;\n"

                + "	var rec = gridDiv_grid.getSelectionModel().getSelections();	\n"
                + " for(var i=0;i<rec.length;i++){ \n"
                + " \tif(''!=rec[i].get('ID')){ \n"
                + "		\tches+=eval(rec[i].get('CHES'));	\n"
                + "		\tbiaoz+=eval(rec[i].get('BIAOZ'));	\n"
                + "		\tyihd_ches+=eval(rec[i].get('YIHD_CHES'));	\n"
                + "		\tyihd_biaoz+=eval(rec[i].get('YIHD_BIAOZ'));	\n"
                + " 	\tyingk+=eval(rec[i].get('YINGK')); \n"
                + " 	\tyuns+=eval(rec[i].get('YUNS'));	\n"
                + " 	\tjingz+=eval(rec[i].get('JINGZ'));	\n"
                + " 	\tstd=eval(rec[i].get('STD'));	\n"
                + "		\tmt=eval(rec[i].get('MT'));"
                + " 	\tmad=eval(rec[i].get('MAD')); \n"
                + " 	\taar=eval(rec[i].get('AAR'));	\n"
                + " 	\taad=eval(rec[i].get('AAD'));	\n"
                + " 	\tad=eval(rec[i].get('AD'));	\n"
                + "		\tvad=eval(rec[i].get('VAD'));"
                + " 	\tvdaf=eval(rec[i].get('VDAF')); \n"
                + " 	\tstad=eval(rec[i].get('STAD'));	\n"
                + " 	\thad=eval(rec[i].get('HAD'));	\n"
                + " 	\tqnet_ar=eval(rec[i].get('QNET_AR'));	\n"
                + "		\tqbad=eval(rec[i].get('QBAD'));"
                + " 	\tqgrad_daf=eval(rec[i].get('QGRAD_DAF')); \n"

                //�����Ȩ����ֵ
                + " 	jiaqz += (" + jiaq + "); \n"
                + " 	qnetar_jq += (eval(rec[i].get('QNETAR'))*(" + jiaq + ")); \n"
//				+ " 	qnetark_jq += (eval(rec[i].get('QNETARK'))*("+jiaq+")); \n"
                + " 	std_jq += (eval(rec[i].get('STD'))*(" + jiaq + ")); \n"
                + " 	mt_jq += (eval(rec[i].get('MT'))*(" + jiaq + ")); \n"


                + " \t}	\n"
                + " }	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('CHES',ches);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('BIAOZ',biaoz);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YINGK',yingk);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YUNS',yuns);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('JINGZ',jingz);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YIHD_CHES',yihd_ches);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YIHD_BIAOZ',yihd_biaoz);	\n"

                //�����ȨֵΪ0���ϼ�Ϊ0
                + " if(jiaqz!=0){	\n"
                + " 	qnetar = eval(qnetar_jq/jiaqz).toFixed(2);	\n"
//				+ " 	qnetark = eval(qnetark_jq/jiaqz);	\n"
                + " 	std1 = eval(std_jq/jiaqz);	\n"
                + " 	mt1 = eval(mt_jq/jiaqz);	\n"
                + " } 	\n"
                + "	qnetark = qnetar*1000/4.1816;\n"
                //��Ȩ��ֵ
                + " gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('QNETAR',qnetar);	\n"
                + " gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('QNETARK',qnetark.toFixed(0) );	\n"
                + " gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('STD',std1.toFixed(2) );	\n"
                + " gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('MT',mt1.toFixed(2) );	\n"

                + " });		\n");


//		��ѡ��ȫѡ�����¼���ѡ����ĺϼ�ֵ
        egu.addOtherScript("gridDiv_grid.on('click',function(){		\n"
                + "		reCountToolbarNum(this);			\n"
                + "});		\n");


//		���¼���ѡ����ĺϼ�ֵ
        egu.addOtherScript(" function reCountToolbarNum(obj){	\n "
                + " \tvar rec;	\n"
                + " \tvar ches=0,biaoz=0,yihd_ches=0,yihd_biaoz=0,yingk=0,yuns=0,jingz=0;	\n"
                //���ü�Ȩ����
                + "var jiaqz = 0;qnetar_jq=0;qnetark_jq = 0;std_jq=0;mt_jq=0;qnetar = 0;qnetark = 0;std1=0;mt1=0;\n"
                + " \trec = obj.getSelectionModel().getSelections();				\n"
                + " \tfor(var i=0;i<rec.length;i++){								\n"
                + " 	\tif(0!=rec[i].get('ID')){									\n"
                + " 		\tches+=eval(rec[i].get('CHES'));						\n"
                + " 		\tbiaoz+=eval(rec[i].get('BIAOZ'));						\n"
                + " 		\tyingk+=eval(rec[i].get('YINGK'));						\n"
                + " 		\tyuns+=eval(rec[i].get('YUNS'));						\n"
                + " 		\tjingz+=eval(rec[i].get('JINGZ'));						\n"
                + " 		\tyihd_ches+=eval(rec[i].get('YIHD_CHES'));				\n"
                + " 		\tyihd_biaoz+=eval(rec[i].get('YIHD_BIAOZ'));			\n"
                //�����Ȩ����ֵ
                + " 	jiaqz += (" + jiaq + "); \n"
                + " 	qnetar_jq += (eval(rec[i].get('QNETAR'))*(" + jiaq + ")); \n"
//				+ " 	qnetark_jq += (eval(rec[i].get('QNETARK'))*("+jiaq+")); \n"
                + " 	std_jq += (eval(rec[i].get('STD'))*(" + jiaq + ")); \n"
                + " 	mt_jq += (eval(rec[i].get('MT'))*(" + jiaq + ")); \n"
                + " 	\t}															\n"
                + " \t}																\n"
                + " if(gridDiv_ds.getCount()>0){									\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('CHES',ches);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('BIAOZ',biaoz);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YINGK',yingk);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YUNS',yuns);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('JINGZ',jingz);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YIHD_CHES',yihd_ches);	\n"
                + " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YIHD_BIAOZ',yihd_biaoz);	\n"
                //�����ȨֵΪ0���ϼ�Ϊ0
                + " if(jiaqz!=0){	\n"
                + " 	qnetar = eval(qnetar_jq/jiaqz).toFixed(2);	\n"
//				+ " 	qnetark = eval(qnetark_jq/jiaqz);	\n"
                + " 	std1 = eval(std_jq/jiaqz);	\n"
                + " 	mt1 = eval(mt_jq/jiaqz);	\n"
                + " } 	\n"
                + "	qnetark = qnetar*1000/4.1816;\n"
                //��Ȩ��ֵ
                + " gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('QNETAR',qnetar);	\n"
                + " gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('QNETARK',qnetark.toFixed(0) );	\n"
                + " gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('STD',std1.toFixed(2) );	\n"
                + " gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('MT',mt1.toFixed(2) );	\n"
                + "	}\n"
                + " \t} \n");

//		��ѡ���ĸ�ֵ
        egu.addOtherScript(
                "gongysTree_treePanel.on(\"checkchange\",function(node,checked){\n" +
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
                        "  },gongysTree_treePanel);\n" +
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
                        "\t\tif(gongysTree_history==\"\"){\n" +
                        "\t\t\tgongysTree_history = history;\n" +
                        " 	   document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" +
                        "\t\t}else{\n" +
                        "\t\t\tvar his = gongysTree_history.split(\";\");\n" +
                        "\t\t\tvar reset = false;\n" +
                        "\t\t\tfor(i=0;i<his.length;i++){\n" +
                        "\t\t\t\tif(his[i].split(\",\")[1] == node.id){\n" +
                        "\t\t\t\t\this[i] = \"\";\n" +
                        "\t\t\t\t\treset = true;\n" +
                        "\t\t\t\t\tbreak;\n" +
                        "\t\t\t\t}\n" +
                        "\t\t\t}\n" +
                        "\t\tif(reset){\n" +
                        "\t\t\t  gongysTree_history = his.join(\";\");\n" +
                        "      	 document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" +
                        "    }else{\n" +
                        "      	 gongysTree_history += history;\n" +
                        "        document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" +
                        "    }\n" +
                        "  }\n" +
                        "\n" +
                        "}");

        // ---------------ҳ��js�������--------------------------
        egu.addOtherScript(Strtmpfunction);
        setExtGrid(egu);
        con.Close();
    }

    //��Сдת��
    public static String toAllUpperOrLowerCase(String fireString) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < fireString.length(); i++) {
            char c = fireString.charAt(i);
            if (Character.isUpperCase(c)) {
                buffer.append(Character.toLowerCase(c));
            } else if (Character.isLowerCase(c)) {
                buffer.append(Character.toUpperCase(c));
            }
        }
        return buffer.toString();
    }

    private String getSupplyTable(String DateType) {
//		Ϊ�˴����ú����������е����
        String supplytable = "";
        long feiylbb_id = 0;
        String where = "";
//      ʹ�����ձ��
        if (this.getJieslxValue().getId() == Locale.liangpjs_feiylbb_id        //        	��Ʊ����
                || this.getJieslxValue().getId() == Locale.guotyf_feiylbb_id    //        	�����˷�
                || this.getJieslxValue().getId() == Locale.dityf_feiylbb_id    //        	�����˷�
                || this.getJieslxValue().getId() == Locale.haiyyf_feiylbb_id    //        	�����˷�
                ) {

            if (getJieslxValue().getId() == Locale.liangpjs_feiylbb_id || getJieslxValue().getId() == Locale.guotyf_feiylbb_id
                    || getJieslxValue().getId() == Locale.haiyyf_feiylbb_id) {

                feiylbb_id = Locale.guotyf_feiylbb_id;
            } else {

                feiylbb_id = getJieslxValue().getId();
            }

            supplytable = ",(select c.id as chepb_id,yfzl.jifzl from chepb c,yunfjszlb yfzl				\n"
                    + "       						where c.id=yfzl.chepb_id																			\n"
                    + "       						and yfzl.feiylbb_id=" + feiylbb_id + ") yfzl,									\n"

                    + " (select distinct cp.id,decode(yfzl.jifzl,null,nvl(cp.biaoz,0),yfzl.jifzl) as yihd_biaoz 			\n"
                    + " 	from chepb cp,danjcpb dj,yunfdjb yf,(select c.id as chepb_id,yfzl.jifzl from chepb c,yunfjszlb yfzl				\n"
                    + "       where c.id=yfzl.chepb_id																			\n"
                    + "       and yfzl.feiylbb_id=" + feiylbb_id + ") yfzl,fahb f,meikxxb m,gongysb g,yansbhb ys,zhilb z 		\n"
                    + " 	where cp.id=dj.chepb_id and f.id=cp.fahb_id 															\n"
                    + "     and f.meikxxb_id=m.id and f.gongysb_id=g.id	and dj.yunfdjb_id=yf.id and yf.feiylbb_id=" + feiylbb_id
                    + " 	and cp.id=yfzl.chepb_id(+) " + this.getWhere(DateType)
                    + " ) yihd,\n" +

                    "	(select distinct cp.id,dj.yunfjsb_id\n" +
                    "                  from chepb cp,yunfdjb yd,danjcpb dj,fahb f,zhilb z,yansbhb ys,\n" +
                    "						gongysb g,meikxxb m\n" +
                    "                  where yd.id=dj.yunfdjb_id\n" +
                    "                        and dj.chepb_id=cp.id\n" +
                    "                        and f.id=cp.fahb_id\n" +
                    "						 and f.gongysb_id=g.id\n" +
                    "						 and f.meikxxb_id=m.id\n" +
                    "                        and yd.feiylbb_id=" + feiylbb_id + "\n" + this.getWhere(DateType) +
                    "     ) djcp";

        }

        return supplytable;
    }

    private String getSupplyWhere() {

        String supplywhere = "";

        if (this.getJieslxValue().getId() == Locale.liangpjs_feiylbb_id            //��Ʊ����
                || this.getJieslxValue().getId() == Locale.guotyf_feiylbb_id        //�����˷�
                || this.getJieslxValue().getId() == Locale.dityf_feiylbb_id        //�����˷�
                || this.getJieslxValue().getId() == Locale.haiyyf_feiylbb_id        //�����˷�
                ) {

            supplywhere = "	    and cp.id=yihd.id(+)\n" +
                    "       and cp.id=djcp.id(+)\n" +
                    "      -- and (djcp.yunfjsb_id is null or djcp.yunfjsb_id=0) \n" +
                    "		and cp.id=yfzl.chepb_id(+) \n ";
        }

        return supplywhere;
    }

    private String getSupplyCol() {
//		�˷ѽ��������
        String supplycol = "";

        if (this.getJieslxValue().getId() == Locale.liangpjs_feiylbb_id
                || this.getJieslxValue().getId() == Locale.guotyf_feiylbb_id
                || this.getJieslxValue().getId() == Locale.dityf_feiylbb_id
                || this.getJieslxValue().getId() == Locale.haiyyf_feiylbb_id
                ) {
//			��Ʊ����
            supplycol = " decode(m.mingc,null,'',count(yihd.id)) as yihd_ches,decode(m.mingc,null,'',sum(nvl(yihd.yihd_biaoz,0))) as yihd_biaoz,	\n" +
                    " decode(m.mingc,null,'',case when (count(cp.id)-count(yihd.id))=0 then '�����' else 'δ���' end) as Huophdzt,	\n";
        }

        return supplycol;
    }

    private String getWhere(String DateType) {

        String where = "";

        //��Ӧ��
//		2008-12-19���޸ģ�����û�û���������������г�ȫ���ķ�����Ϣ
        if (!this.getYansbh().equals("��ѡ��") && !this.getYansbh().equals("")) {

//			�����ձ��
            where = " and f.yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh());

            if (((Visit) getPage().getVisit()).isFencb()) {
//        		һ�����Ƶ�������೧�ж�

                String dcid = MainGlobal.getProperIds(this.getFencbModel(), this.getChangb());

                where += " and f.diancxxb_id in (" + dcid + ")";

//            	where+=" and f.diancxxb_id="+MainGlobal.getProperId(this.getFencbModel(),this.getChangb());
            } else {

                where += " and f.diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id();
            }

            if (getJieslxValue().getId() == Locale.liangpjs_feiylbb_id) {    //��Ʊ����

                where += " and (f.jiesb_id=0 or f.jiesb_id=" + MainGlobal.getProperId(this.getYujsdbmModel(), this.getYujsdbm()) + ")	\n";
            } else if (getJieslxValue().getId() == Locale.meikjs_feiylbb_id) {    //ú�����

                where += " and (f.jiesb_id=0 or f.jiesb_id=" + MainGlobal.getProperId(this.getYujsdbmModel(), this.getYujsdbm()) + ")	\n";
            } else if (getJieslxValue().getId() == Locale.daozyf_feiylbb_id) {

                where += " and cp.jiesb_id=0	\n";
            } else {

                where += " and ((dj.yunfjsb_id=0 or yd.feiylbb_id<>" + getJieslxValue().getId() + " or yd.feiylbb_id is null) or dj.yunfdjb_id is null)	\n";
            }

        } else {

//        	û�����ձ��
            String gongysb_id = this.getGongysmlttree_id();

            if (!(gongysb_id == null || gongysb_id.equals(""))) {
                where += " and (m.id in (" + gongysb_id + ") or g.id in (" + gongysb_id + "))	\n";
            }

            if (getJieslxValue().getId() == Locale.liangpjs_feiylbb_id) {    //��Ʊ����

                where += " and (f.jiesb_id=0 or f.jiesb_id=" + MainGlobal.getProperId(this.getYujsdbmModel(), this.getYujsdbm()) + ") and f.zhilb_id=z.id and z.liucztb_id=1 	\n";
            } else if (getJieslxValue().getId() == Locale.meikjs_feiylbb_id) {    //ú�����

                where += " and (f.jiesb_id=0 or f.jiesb_id=" + MainGlobal.getProperId(this.getYujsdbmModel(), this.getYujsdbm()) + ") and f.zhilb_id=z.id and z.liucztb_id=1	\n";

            } else if (getJieslxValue().getId() == Locale.guotyf_feiylbb_id
                    || getJieslxValue().getId() == Locale.dityf_feiylbb_id
                    || getJieslxValue().getId() == Locale.daozyf_feiylbb_id
                    || getJieslxValue().getId() == Locale.haiyyf_feiylbb_id
                    ) {        //�˷ѽ���
//        		where+=" and (dj.yunfjsb_id=0 or dj.yunfdjb_id is null) and f.zhilb_id=z.id(+) ";
                where += " and f.zhilb_id=z.id and z.liucztb_id=1 \n";
            }

            if (getJieslxValue().getId() == Locale.daozyf_feiylbb_id) {
//        		��װ�˷�
                where += " and cp.jiesb_id=0 \n";
            }

            if (MainGlobal.getProperId(getYunsdwModel(), this.getYunsdw()) > -1) {

                if (getJieslxValue().getId() == Locale.daozyf_feiylbb_id) {
//        			��װ�˷�
                    where += " and cp.yunsdw='" + getYunsdw() + "'";
                } else {

                    where += " and cp.yunsdwb_id=" + MainGlobal.getProperId(getYunsdwModel(), this.getYunsdw());
                }
            }

            //Ʒ��
            if (MainGlobal.getProperId(getPinzModel(), this.getPinz()) > -1) {
                where += " and f.pinzb_id = " + MainGlobal.getProperId(getPinzModel(), this.getPinz()) + "\n";
            }

            //��λ����
//        	����
            if (!this.getQd().equals("") && !this.getQd().equals("0")) {
                where += " and z.qnet_ar>=" + this.getQd() + "\n";
            }
//        	����
            if (!this.getQu().equals("") && !this.getQu().equals("0")) {
                where += " and z.qnet_ar<=" + this.getQu() + "\n";
            }
            //Std
//        	����
            if (!this.getStdd().equals("") && !this.getStdd().equals("0")) {
                where += " and z.std>=" + this.getStdd() + "\n";
            }
//        	����
            if (!this.getStdu().equals("") && !this.getStdu().equals("0")) {
                where += " and z.std<=" + this.getStdu() + "\n";
            }
            //Star
//        	����
            if (!this.getStard().equals("") && !this.getStard().equals("0")) {
                where += " and z.star>=" + this.getStard() + "\n";
            }
//        	����
            if (!this.getStaru().equals("") && !this.getStaru().equals("0")) {
                where += " and z.star<=" + this.getStaru() + "\n";
            }


            if (((Visit) getPage().getVisit()).isFencb()) {

//            	һ�����Ƶ�������೧�ж�
                String yicdzcl = MainGlobal.getProperIds(this.getFencbModel(), this.getChangb());

                where += " and f.diancxxb_id in (" + yicdzcl + ")";

//            	where+=" and f.diancxxb_id="+MainGlobal.getProperId(this.getFencbModel(),this.getChangb());
            } else {

                where += " and f.diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id();
            }

//          �����ж�

            where += " and f." + DateType + " >= to_date('" + getRiq1() + "', 'yyyy-mm-dd')				\n" +
                    "	and f." + DateType + " <= to_date('" + getRiq2() + "', 'yyyy-mm-dd')				\n";


//            ʹ�����ձ��
            if (MainGlobal.getXitxx_item("����", Locale.shiyysbh_jies, String.valueOf(((Visit) getPage().getVisit()).isFencb() ? MainGlobal.getProperIds(this.getFencbModel(), this.getChangb()) : String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id())), "��").equals("��")) {

                where += " and f.yansbhb_id=ys.id(+) ";

                if (this.getTree() != null && getHthValue().getId() != -1) {

//            		if(Jiesdcz.getJiessz_item(((Visit) getPage().getVisit()).isFencb()?MainGlobal.getProperId(this.getFencbModel(),this.getChangb()):((Visit) getPage().getVisit()).getDiancxxb_id(),
//            				Long.parseLong(MainGlobal.getLeaf_ParentNodeId(this.getTree(), this.getTreeid())),this.getHthValue().getId() ,Locale.jieszbtz_jies, "��").equals("��")){
//
//            			//where+=" and ys.liucztbid=1";
//            		}
                }
            }
        }


        return where;
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
            // �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
            visit.setboolean1(true);
            if (visit.getActivePageName().toString().equals("Balance")
                    || visit.getActivePageName().toString().equals("Jieszbtz")
                    || visit.getActivePageName().toString().equals("DCBalance_tb")
                    || visit.getActivePageName().toString().equals("DCBalance")
                    ) {
//				����ǴӺ���Ľ�����ת�����ģ���String15 ��Ϊ�գ�Ӧ����Stirng15��ֵ����Ϊ�Ƿֹ�˾����
                if (!visit.getString15().equals("")) {

                    visit.setboolean1(false);
                }
            }

            visit.setActivePageName(getPageName().toString());

            visit.setString1("");    //Rbvalue��������ѡ��
            visit.setString2("");    //Treeid
            visit.setString3("");    //��
            visit.setString4("");    //���ձ��
            visit.setString5("");    //riq1
            visit.setString6("");    //riq2
            visit.setString7("");    //����۶���
            visit.setString8("");    //�������е���id
            visit.setString9("");    //���ú˶�״̬
            visit.setString10("");    //���䵥λ
            visit.setString12("");    //�ϴν�����
            visit.setString13("");    //fahb_id
            visit.setString14("");    //gongysbmlt_id(��Ӧ�̱�)
            visit.setString16("");    //�����㵥λ
            visit.setString18("");  //����ۿ���
            visit.setString19("");  //ȼ��Ʒ��ID
            visit.setString20("");  //Ԥ���㵥����

            if (visit.getboolean1()) {

                visit.setString15("");    //���㵥λ����������ҳ�����õĽ��㵥λ�������� �糧id �� �ֹ�˾id����
                //�ò���Ҫ������һ��ҳ�棬����ʱ���ж���
                visit.setString17("");    //���㷽ʽ�����������
                visit.setString11("");  //����ҳ����ز���(���䷽ʽ)
            }
//			���������ҳ�淵�ؽ�������ҳ��ʱ�����䷽ʽ��ʧ���޷���ȷ������������ҳ��
            if ("hy".equals(MainGlobal.getXitxx_item("����", "ׯ�Ӻ���ú��������ҳ��bug", String.valueOf(visit.getDiancxxb_id()), ""))) {
                visit.setString11("hy");
            }
            visit.setLong1(0);        //�糧��Ϣ��id
            visit.setLong2(0);        //��������
            visit.setLong3(0);        //��Ӧ�̱�id
            visit.setLong8(0);        //��ͬ��id
            visit.setLong9(0);        //���䵥λ��id
            visit.setList1(null);
            setJieslxValue(null);    //3
            setJieslxModel(null);

            setHthValue(null);        //4
            setHthModel(null);

            setYansbhValue(null);    //2
            setYansbhModel(null);

            setFencbValue(null);    //5
            setFencbModel(null);

            setYunsdwValue(null);    //6
            setYunsdwModel(null);    //6

            setYujsdbmValue(null);    //8
            setYujsdbmModel(null);

            setGuohxtValue(null);    //9
            setGuohxtModel(null);    //9

            setPinzValue(null);        //7
            setPinzModel(null);        //7

            visit.setboolean1(true);    //����change
            visit.setboolean2(false);    //������λchange����������,���䵥λ,Ʒ��
            ((Visit) getPage().getVisit()).setboolean3(false);    //�ֳ���
            ((Visit) getPage().getVisit()).setboolean4(false);    //�ж��Ƿ����ʡ�ˢ�°�ť����ֻ�е����ˢ�¡�����ʾ����

            getJieslxModels();        //3
            getHthModels();            //4
            getYansbhModels();        //2
            getFencbModels();        //5
            logger.info("===begin getYunsdwValue:"+this.getYunsdwValue().getId());
            if(this.getYunsdwValue().getId()==-1){
                 getYunsdwModels();
            }
      //6
            getPinzModels();        //7
            getYujsdbmModels();        //8

//			((Visit) this.getPage().getVisit()).setString11(""); //����ҳ����ز���(���䷽ʽ)
            if (cycle.getRequestContext().getParameters("lx") != null) {

                ((Visit) this.getPage().getVisit()).setString11(cycle.getRequestContext().getParameters("lx")[0].trim());
            }
//			ͳһ����ʱ���õ���ͬ���跽��id������Ƿֹ�˾�ɹ�������Ӧ�Ƿֹ�˾��id������ǵ糧�ɹ�����Ҫ�Ӹò�����
            if (cycle.getRequestContext().getParameters("jsdwid") != null) {

                ((Visit) this.getPage().getVisit()).setString15(cycle.getRequestContext().getParameters("jsdwid")[0].trim());
            }
//			�жϽ���Ľ��㷽ʽ�������� jsfs=tb,������Ϊ�ǽ��㷽ʽ�Ǽ���
            if (cycle.getRequestContext().getParameters("jsfs") != null) {

                ((Visit) this.getPage().getVisit()).setString17(cycle.getRequestContext().getParameters("jsfs")[0].trim());
            }

//			����Ĭ�ϻ�Ʊ�˶�״̬
            setHuopztvalue(MainGlobal.getXitxx_item("����", "ú����������еĻ�Ʊ�˶�״̬Ϊ", String.valueOf(visit.getDiancxxb_id()), "yiwc"));

        }

        if (visit.getboolean1() || visit.getboolean2()) {

            visit.setboolean1(false);
            visit.setboolean2(false);
//			visit.setString9("");
            getHthModels();
            getYansbhModels();
//            getYunsdwModels();
            getPinzModels();
            //this.getPinzModels();
        }
        getYujsdbmModels();
        getSelectData();
    }


    //��������
    public boolean _Jieslxchange = false;

    public IDropDownBean getJieslxValue() {

        if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
            ((Visit) getPage().getVisit())
                    .setDropDownBean3((IDropDownBean) getJieslxModel()
                            .getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean3();
    }

    public void setJieslxValue(IDropDownBean Value) {

        if (Value != ((Visit) getPage().getVisit()).getDropDownBean3()) {

            ((Visit) getPage().getVisit()).setboolean2(true);

        }
        ((Visit) getPage().getVisit()).setDropDownBean3(Value);
    }

    public IPropertySelectionModel getJieslxModel() {

        if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

            getJieslxModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel3();
    }

    public void setJieslxModel(IPropertySelectionModel value) {

        ((Visit) getPage().getVisit()).setProSelectionModel3(value);
    }

    public IPropertySelectionModel getJieslxModels() {

        String sql = "select id, mingc from feiylbb where (leib<2 or leib=3) order by id";

        if (MainGlobal.getXitxx_item("����", "�Ƿ�Ϊɽ���ֹ�˾�ɹ����㵥", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "��").equals("��")) {
            sql = "select id, mingc from feiylbb where mingc = '" + Locale.meikjs_feiylbb + "'";
        }

        ((Visit) getPage().getVisit())
                .setProSelectionModel3(new IDropDownModel(sql));
        return ((Visit) getPage().getVisit()).getProSelectionModel3();
    }

    //  ����ϵͳ������_��ʼ
    public IDropDownBean getGuohxtValue() {
        if (((Visit) this.getPage().getVisit()).getDropDownBean9() == null) {
            if (getGuohxtModel().getOptionCount() > 0) {
                setGuohxtValue((IDropDownBean) getGuohxtModel().getOption(0));
            }
        }
        return ((Visit) this.getPage().getVisit()).getDropDownBean9();
    }

    public void setGuohxtValue(IDropDownBean LeibValue) {
        ((Visit) this.getPage().getVisit()).setDropDownBean9(LeibValue);
    }

    public IPropertySelectionModel getGuohxtModel() {
        if (((Visit) this.getPage().getVisit()).getProSelectionModel9() == null) {
            getGuohxtModels();
        }
        return ((Visit) this.getPage().getVisit()).getProSelectionModel9();
    }

    public void setGuohxtModel(IPropertySelectionModel value) {
        ((Visit) this.getPage().getVisit()).setProSelectionModel9(value);
    }

    public void getGuohxtModels() {
        ArrayList list = new ArrayList();
        list.add(new IDropDownBean(1, "Aϵͳ"));
        list.add(new IDropDownBean(2, "Bϵͳ"));
        setGuohxtModel(new IDropDownModel(list));
    }
//	����������_����

    public String rb1() {
        if (getRbvalue() == null || getRbvalue().equals("")) {


            return "fh.fahrq";


        } else {
            if (getRbvalue().equals("fahrq")) {
                return "fh.fahrq";
            } else return "fh.daohrq";
        }
    }

    // ���ձ��
    public boolean _Yansbhchange = false;

    public IDropDownBean getYansbhValue() {
        if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
            ((Visit) getPage().getVisit())
                    .setDropDownBean2((IDropDownBean) getYansbhModel()
                            .getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean2();
    }

    public void setYansbhValue(IDropDownBean Value) {

        if (Value != ((Visit) getPage().getVisit()).getDropDownBean2()) {

            ((Visit) getPage().getVisit()).setboolean1(true);
        }
        ((Visit) getPage().getVisit()).setDropDownBean2(Value);
    }

    public void setYansbhModel(IPropertySelectionModel value) {
        ((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public IPropertySelectionModel getYansbhModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
            getYansbhModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }

    public IPropertySelectionModel getYansbhModels() {

        String sql = "";

        if (this.getJieslxValue().getId() == 1 || getJieslxValue().getId() == 2) {
//			��Ʊ�����ú�����

            sql = "select distinct y.id,y.bianm from yansbhb y, chepb cp, fahb fh where fh.yansbhb_id=y.id and cp.fahb_id=fh.id  "
                    + " and fh.jiesb_id=0 and fh.diancxxb_id in (" + String.valueOf(((Visit) getPage().getVisit()).isFencb() ? MainGlobal.getProperIds(getFencbModel(),
                    this.getChangb()) : String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id())) + ") order by bianm";
        } else {
//			�˷ѽ���
            sql = "select distinct y.id,y.bianm from yansbhb y, chepb cp, danjcpb dj,fahb fh where fh.yansbhb_id=y.id and cp.id=dj.chepb_id and cp.fahb_id=fh.id "
                    + " and dj.yunfjsb_id=0 and fh.diancxxb_id in (" + String.valueOf(((Visit) getPage().getVisit()).isFencb() ? MainGlobal.getProperIds(getFencbModel(),
                    this.getChangb()) : String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id())) + ") order by bianm";
        }

        ((Visit) getPage().getVisit())
                .setProSelectionModel2(new IDropDownModel(sql, "��ѡ��"));
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }

    //	Ԥ���㵥����begin
    public IDropDownBean getYujsdbmValue() {
        if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
            ((Visit) getPage().getVisit())
                    .setDropDownBean8((IDropDownBean) getYujsdbmModel()
                            .getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean8();
    }

    public void setYujsdbmValue(IDropDownBean Value) {

        if (Value != ((Visit) getPage().getVisit()).getDropDownBean8()) {

            ((Visit) getPage().getVisit()).setboolean1(true);
        }
        ((Visit) getPage().getVisit()).setDropDownBean8(Value);
    }

    public void setYujsdbmModel(IPropertySelectionModel value) {
        ((Visit) getPage().getVisit()).setProSelectionModel8(value);
    }

    public IPropertySelectionModel getYujsdbmModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
            getYujsdbmModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel8();
    }

    public IPropertySelectionModel getYujsdbmModels() {
        String gys_id = getGongysmlttree_id();

        String mstr_gongys_id = "";
//		ѡ���˹�Ӧ��
        if ("".equals(gys_id) || gys_id == null) {
            //ʲô��û��ѡ��,��ȫ����ʱ��
            mstr_gongys_id = "0";
        } else if (gys_id.indexOf(",") == -1) {
            //�������ֻѡ����ú��,�Զ���ú��ת��Ϊ��Ӧ�̵�id
            mstr_gongys_id = MainGlobal.getLeaf_ParentNodeId(this.getTree(), gys_id);
        } else {
            //��ѡ����ú����ѡ���˹�Ӧ��,��ȡ��Ӧ�̵�id
            mstr_gongys_id = MainGlobal.getLeaf_ParentNodeId(this.getTree(),
                    gys_id.substring(0, gys_id.indexOf(",")));
        }
        String sql = "select id,bianm from jiesb where gongysb_id in(" + mstr_gongys_id + ") " +
                " and daibch like '%~%' and is_yujsd=1  and weicdje>0";

        ((Visit) getPage().getVisit())
                .setProSelectionModel8(new IDropDownModel(sql, "��ѡ��"));
        return ((Visit) getPage().getVisit()).getProSelectionModel8();
    }

    //	Ԥ���㵥����end
    //��ͬ��
    public boolean _Hthchange = false;

    public IDropDownBean getHthValue() {
        if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
            ((Visit) getPage().getVisit())
                    .setDropDownBean4((IDropDownBean) getHthModel()
                            .getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean4();
    }

    public void setHthValue(IDropDownBean Value) {

        if (Value != ((Visit) getPage().getVisit()).getDropDownBean4()) {

            ((Visit) getPage().getVisit()).setboolean1(true);
        }
        ((Visit) getPage().getVisit()).setDropDownBean4(Value);
    }

    public void setHthModel(IPropertySelectionModel value) {
        ((Visit) getPage().getVisit()).setProSelectionModel4(value);
    }

    public IPropertySelectionModel getHthModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
            getHthModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }

    public IPropertySelectionModel getHthModels() {
        JDBCcon con = new JDBCcon();
        List List = new ArrayList();
        try {

//			һ��ú����û�й�Ӧ��
//			�����ж�ϵͳ����û�к�ͬ
//			2008-10-17�޸ķ���������ͬδ������ʱ���Խ����Խ��㣬�����ܱ���
//
//			��ͬ�͹�Ӧ�̺�ú��Ĺ�ϵ��Ҫ���ҳ���Ӧ�ĺ�ͬ�����������������

//			�糧�ɹ���
//			1��ú���ͬ
//				1)���跽��hetb�е�diancxxb_id��Ϊ�õ糧�� ��ͬ�����е��ջ��˰����õ糧��
//				2)��δѡ��Ӧ��ʱ��ѡ���ʱ����ڴ��ں�ͬ�����˹���ú �� ѡ��Ĺ�Ӧ��Ϊ��ͬ�ķ�����
//					�� �����еķ����˴�����"hetfhrgysglb"���ұ��е�gongysb_id�ܺͷ��������;
//					��ѡ���˹�Ӧ��ʱ����ͬ�ķ�����Ϊ��ѡ��Ӧ�� �� ��ѡ��Ӧ���� hetfhrgysglb ���к�ͬ��������֮��Ӧ��

//			2�������ͬ(֧��һ�����ƣ��ֳ���ʹ���ܳ��������ͬ)
//				1�������û��ѡ��Ӧ�̣���ô����ʱ����ڵķ��������������ͬ�۸��й涨��ú��
//						���ָ���˹�Ӧ�̣���ô��Ӧ��ú������е�ú����Ϣ�����������ͬ�۸��е�����ʾ��
//				2�����õ糧�����������ͬ�����˷���

//			�ֹ�˾�ɹ���
//			1��ú���ͬ
//				1�����跽��hetb�е�diancxxb_id��Ϊ�÷ֹ�˾��
//				2����δѡ��Ӧ��ʱ��ѡ���ʱ����ڵķ������ڹ�Ӧ��Ϊ��ͬ�ķ�����
//						�� ѡ���ʱ����ڵķ������ڵĹ�Ӧ��Ϊ��ͬ���跽���ֹ�˾���˴���hetb�е��跽��λ(xufdwmc) ��糧��Ϣ���е� string15 Ϊid��ȫ��ƥ�䡣
//						�� �����еķ����˴�����"hetfhrgysglb"�У��ұ��е�gongysb_id�ܺͷ��������;
//					��ѡ��Ӧ��ʱ����ͬ�ķ�����Ϊ��ѡ��Ӧ��
//								�� ��ͬ���跽Ϊ��ѡ�Ĺ�Ӧ�̣��ֹ�˾�� �˴���hetb�е��跽��λ(xufdwmc) ��糧��Ϣ���е� string15 Ϊid��ȫ��ƥ�䡣
//								�� ��ѡ��Ӧ���� hetfhrgysglb ���к�ͬ��������֮��Ӧ��

//			2�������ͬ(֧��һ�����ƣ��ֳ���ʹ���ܳ��������ͬ)
//				1�������û��ѡ��Ӧ�̣���ô����ʱ����ڵķ��������������ͬ�۸��й涨��ú��
//					���ָ���˹�Ӧ�̣���ô��Ӧ��ú������е�ú����Ϣ�����������ͬ�۸��е�����ʾ��
//				2�����÷ֹ�˾�����������ͬ�����˷���

            String strHetspzt = "";
            String sql = "";
            String stryunsdw = "";
            String stryansbh = "";//���ձ���ж�����
            String riq = "fahrq";
            String gongysb_id = this.getGongysmlttree_id();

            if (getRbvalue().equals("fahrq") || this.getRbvalue().equals("")) {


                riq = "fahrq";
            } else if (getRbvalue().equals("daohrq")) {
                riq = "daohrq";
            }

            List.add(new IDropDownBean(-1, "��ѡ��"));

            if (MainGlobal.getProperId(getYunsdwModel(), this.getYunsdw()) > -1) {
//				ѡ�������䵥λ
                if (getJieslxValue().getId() != Locale.daozyf_feiylbb_id) {
//					��װ�˷�
                    stryunsdw = " and ys.mingc='" + getYunsdw() + "'";
                } else {

                    stryunsdw = " and yunsdwb_id=" + MainGlobal.getProperId(getYunsdwModel(), this.getYunsdw());
                }

            } else {

                stryunsdw = "";
            }

            if (MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) > -1) {

                stryansbh = " and f.yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh());
            }

            if (!Jiesdcz.isFengsj(((Visit) getPage().getVisit()).getString15())) {
//				���Ϊ�գ����Ǿ��ǵ糧id��������Ϊ�ǳ����Ĳɹ�������

                if (gongysb_id == null || gongysb_id.equals("")) {    //ȫ��

                    if (((Visit) getPage().getVisit()).isFencb()) {

//						�ֳ���
                        if (getJieslxValue().getId() != Locale.guotyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.haiyyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.daozyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.dityf_feiylbb_id) {
//						�ֹ�˾�ɹ����ֳ���ú�����

                            //��ͬ���⴦����ѡ�������㵥λΪ�����ͬ���ڻ�����ͬ����ʱ������ѡ����ͬ���ں͹����ͬ����
                            StringBuffer hetrqSql = new StringBuffer();
                            String dtsqlString = "SELECT ID FROM xitxxb WHERE mingc = '��ͬ�����������⴦��' AND zhi = '��'";
                            ResultSetList rslxitxx = con.getResultSetList(dtsqlString);

                            if (rslxitxx.next()) {
                                hetrqSql.append("|| '(' || to_char(h.qisrq,'yyyy-mm-dd') || '��' || to_char(h.guoqrq,'yyyy-mm-dd') || ')' AS hetbh");
                            }

                            sql = " select distinct h.id,h.hetbh " + hetrqSql.toString() + " from hetb h,hetslb hs where (h.diancxxb_id in ("
                                    + MainGlobal.getProperIds(getFencbModel(), this.getChangb())
                                    + ") or hs.diancxxb_id in (" + MainGlobal.getProperIds(getFencbModel(), this.getChangb())
                                    + " )) and h.id = hs.hetb_id(+) and h.gongysb_id in ((select distinct gongysb_id from fahb f where ";
                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') and \n ";
                            }

                            sql += " liucztb_id=1 and diancxxb_id in (" + MainGlobal.getProperIds(getFencbModel(), this.getChangb()) + ")  \n"
                                    + stryansbh + " ) union ("
                                    +
                                    "	select distinct h.hetfhr_id as gongysb_id from hetfhrgysglb h\n" +
                                    "       where h.zhuangt=1 and h.gongysb_id in (\n" +
                                    "       	select distinct gongysb_id from fahb f where \n";

                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') and \n ";
                            }


                            sql += " liucztb_id=1 and diancxxb_id in (" + MainGlobal.getProperIds(getFencbModel(), this.getChangb()) + ")  \n"
                                    + stryansbh + " ))) \n";

                            if (!stryansbh.equals("")) {

                                sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                        + "', 'yyyy-mm-dd') order by hetbh ";
                            } else {

                                sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') order by hetbh ";
                            }

                        } else {
//							�ֳ����˷ѽ���
                            sql = " select distinct hetys.id,hetbh from hetysjgb,hetys,yunsdwb ys where hetysjgb.hetys_id=hetys.id and hetys.yunsdwb_id=ys.id \n";

                            if (!stryansbh.equals("")) {

                                sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                        + "', 'yyyy-mm-dd') ";
                            } else {

                                sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                            }

                            sql += " and (meikxxb_id in (select distinct meikxxb_id from fahb f where \n";

                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') \n ";
                            }

                            sql += " and diancxxb_id in (" + MainGlobal.getProperIds(getFencbModel(), this.getChangb()) + ")) or meikxxb_id=0) \n"
                                    + stryunsdw + stryansbh + " and (hetys.diancxxb_id in (" + MainGlobal.getProperIds(getFencbModel(), this.getChangb()) + ") or	\n"
                                    + " hetys.diancxxb_id=(select distinct id from diancxxb where id in (select distinct fuid from diancxxb where id in ("
                                    + MainGlobal.getProperIds(getFencbModel(), this.getChangb()) + ")) and jib=" + SysConstant.JIB_DC + "))  \n"
                                    + " order by hetbh ";
                        }

                    } else {

                        if (getJieslxValue().getId() != Locale.guotyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.haiyyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.daozyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.dityf_feiylbb_id) {
//							���� ú�����
                            sql = "select distinct h.id,h.hetbh from hetb h,hetslb hs where h.id = hs.hetb_id(+) and (h.diancxxb_id="
                                    + ((Visit) getPage().getVisit()).getDiancxxb_id()
                                    + " or hs.diancxxb_id="
                                    + ((Visit) getPage().getVisit()).getDiancxxb_id()
                                    + ") and h.gongysb_id in ((select distinct gongysb_id from fahb f where \n";

                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') and \n ";
                            }


                            sql += " liucztb_id=1 " + stryansbh + ") \n union ( \n"
                                    +
                                    "	select distinct h.hetfhr_id as gongysb_id from hetfhrgysglb h\n" +
                                    "       where h.zhuangt=1 and h.gongysb_id in (\n" +
                                    "       	select distinct gongysb_id from fahb f where \n";

                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') and \n ";
                            }

                            sql += " liucztb_id=1 " + stryansbh + "))) \n";

                            if (!stryansbh.equals("")) {

                                sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                        + "', 'yyyy-mm-dd') \n";
                            } else {

                                sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') \n";
                            }

                            sql += " order by hetbh";

                        } else {
//							���� �˷ѽ���
                            sql = " select distinct hetys.id,hetbh from hetysjgb,hetys,yunsdwb ys where hetysjgb.hetys_id=hetys.id and hetys.yunsdwb_id=ys.id	\n";
                            if (!stryansbh.equals("")) {

                                sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                        + "', 'yyyy-mm-dd') ";
                            } else {

                                sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                            }
                            sql += " and (meikxxb_id in (select distinct meikxxb_id from fahb where \n ";

                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') and\n ";
                            }

                            sql += " liucztb_id=1 and diancxxb_id in (" + ((Visit) getPage().getVisit()).getDiancxxb_id() + ")) or meikxxb_id=0) \n"
                                    + stryunsdw + stryansbh + " and (hetys.diancxxb_id="
                                    + ((Visit) getPage().getVisit()).getDiancxxb_id()
                                    + " or hetys.diancxxb_id in (select id from diancxxb where id in (select fuid from diancxxb where id="
                                    + ((Visit) getPage().getVisit()).getDiancxxb_id() + ") and jib=" + SysConstant.JIB_DC + "))	\n"
                                    + " order by hetbh ";
                        }
                    }

                } else {

                    String mstr_gongys_id = "";
//					ѡ���˹�Ӧ��
                    if (gongysb_id.indexOf(",") == -1) {

                        mstr_gongys_id = MainGlobal.getLeaf_ParentNodeId(this.getTree(), gongysb_id);
                    } else {

                        mstr_gongys_id = MainGlobal.getLeaf_ParentNodeId(this.getTree(),
                                gongysb_id.substring(0, gongysb_id.indexOf(",")));
                    }

                    if (mstr_gongys_id.equals("")) {

                        setMsg("��ú��û�ж�Ӧ�Ĺ�Ӧ����Ϣ��");
                    } else {

                        if (((Visit) getPage().getVisit()).isFencb()) {
//							ѡ���˹�Ӧ�̣��ֳ���
                            if (getJieslxValue().getId() != Locale.guotyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.haiyyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.daozyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.dityf_feiylbb_id) {

                                //��ͬ���⴦����ѡ�������㵥λΪ�����ͬ���ڻ�����ͬ����ʱ������ѡ����ͬ���ں͹����ͬ����
                                StringBuffer hetrqSql = new StringBuffer();
                                String dtsqlString = "SELECT ID FROM xitxxb WHERE mingc = '��ͬ�����������⴦��' AND zhi = '��'";
                                ResultSetList rslxitxx = con.getResultSetList(dtsqlString);

                                if (rslxitxx.next()) {
                                    hetrqSql.append("|| '(' || to_char(h.qisrq,'yyyy-mm-dd') || '��' || to_char(h.guoqrq,'yyyy-mm-dd') || ')' AS hetbh");
                                }

//								��Ʊ��ú��
                                sql = "select distinct h.id,h.hetbh " + hetrqSql + " from hetb h,hetslb hs where h.id=hs.hetb_id(+) and (h.diancxxb_id in ("
                                        + MainGlobal.getProperIds(getFencbModel(), this.getChangb())
                                        + ") or hs.diancxxb_id in ( "
                                        + MainGlobal.getProperIds(getFencbModel(), this.getChangb())
                                        + ")) and (gongysb_id=" + mstr_gongys_id + " or gongysb_id in ("
                                        + "	select distinct h.hetfhr_id as gongysb_id\n" +
                                        "       from hetfhrgysglb h\n" +
                                        "            where h.gongysb_id=" + mstr_gongys_id + "))	\n ";

                                if (!stryansbh.equals("")) {

                                    sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                            + "', 'yyyy-mm-dd') ";
                                } else {

                                    sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                                }
                                sql += " order by hetbh ";

                            } else {
//								ѡ���˹�Ӧ�̣��ֳ����˷ѽ���
                                sql = " select distinct hetys.id,hetbh from hetysjgb,hetys,yunsdwb ys where hetysjgb.hetys_id=hetys.id and hetys.yunsdwb_id=ys.id	\n"
                                        + " and (meikxxb_id in	\n"
                                        + " (select mk.id		\n"
                                        + " from gongysmkglb glb,gongysb gys,meikxxb mk		\n"
                                        + " where gys.id=glb.gongysb_id and mk.id=glb.meikxxb_id	\n"
                                        + " and (gys.id in (" + gongysb_id + ") or mk.id in (" + gongysb_id + "))) or meikxxb_id = 0) and (hetys.diancxxb_id in ("
                                        + MainGlobal.getProperIds(getFencbModel(), this.getChangb())
                                        + ") or hetys.diancxxb_id in (select distinct id from diancxxb where id in (select distinct fuid from diancxxb where id in ("
                                        + MainGlobal.getProperIds(getFencbModel(), this.getChangb()) + ")) and jib = " + SysConstant.JIB_DC + ")) 	\n";
                                if (!stryansbh.equals("")) {

                                    sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                            + "', 'yyyy-mm-dd') ";
                                } else {

                                    sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                                }

                                sql += " order by hetbh ";
                            }

                        } else {
//
                            if (getJieslxValue().getId() != Locale.guotyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.haiyyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.daozyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.dityf_feiylbb_id) {
//							ѡ���˹�Ӧ�̣���������	��Ʊ��ú��
                                sql = "select distinct h.id,h.hetbh from hetb h,hetslb hs where h.id=hs.hetb_id(+) and (h.diancxxb_id="
                                        + ((Visit) getPage().getVisit()).getDiancxxb_id()
                                        + " or hs.diancxxb_id = "
                                        + ((Visit) getPage().getVisit()).getDiancxxb_id()
                                        + " ) and (gongysb_id=" + mstr_gongys_id + " or gongysb_id in ("
                                        + "	select distinct h.hetfhr_id as gongysb_id\n" +
                                        "       from hetfhrgysglb h\n" +
                                        "            where h.gongysb_id=" + mstr_gongys_id + "))	\n ";

                                if (!stryansbh.equals("")) {

                                    sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                            + "', 'yyyy-mm-dd') ";
                                } else {

                                    sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                                }

                                sql += " order by hetbh ";
                            } else {
//								ѡ���˹�Ӧ�̣��������˷ѽ���
                                sql = " select distinct hetys.id,hetbh from hetysjgb,hetys,yunsdwb ys where hetysjgb.hetys_id=hetys.id and hetys.yunsdwb_id=ys.id	\n"
                                        + " and (meikxxb_id in	\n"
                                        + " (select mk.id		\n"
                                        + " from gongysmkglb glb,gongysb gys,meikxxb mk		\n"
                                        + " where gys.id=glb.gongysb_id and mk.id=glb.meikxxb_id	\n"
                                        + " and (gys.id in (" + gongysb_id + ") or mk.id in (" + gongysb_id + "))) or meikxxb_id = 0)	\n"
                                        + " and (hetys.diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id()
                                        + " or hetys.diancxxb_id in (select id from diancxxb where id in (select fuid from diancxxb where id="
                                        + ((Visit) getPage().getVisit()).getDiancxxb_id() + "))) 	\n";

                                if (!stryansbh.equals("")) {

                                    sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                            + "', 'yyyy-mm-dd') ";
                                } else {

                                    sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                                }
                                sql += " order by hetbh ";
                            }
                        }
                    }
                }
            } else if (Jiesdcz.isFengsj(((Visit) getPage().getVisit()).getString15())) {
//				���string15��jiesdwid����Ϊ�գ����Ƿֹ�˾�ɹ�����

                if (gongysb_id == null || gongysb_id.equals("")) {    //ȫ��

                    if (((Visit) getPage().getVisit()).isFencb()) {

//						�ֳ���
                        if (getJieslxValue().getId() != Locale.guotyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.haiyyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.daozyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.dityf_feiylbb_id) {
//							�ֹ�˾��δѡ��Ӧ�̣��ֳ���ú�����

                            String caight = "";
                            if (MainGlobal.getXitxx_item("����", "�Ƿ�Ϊɽ���ֹ�˾�ɹ����㵥", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "��").equals("��")) {
                                caight = "\nand h.leib = 2";
                            }

                            sql = " select distinct h.id,h.hetbh from hetb h where h.diancxxb_id="
                                    + ((Visit) getPage().getVisit()).getString15() + caight
                                    + " and (h.gongysb_id in ((select distinct gongysb_id from fahb f where ";
                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') and \n ";
                            }

                            sql += " liucztb_id=1 and diancxxb_id in (" + MainGlobal.getProperIds(getFencbModel(), this.getChangb()) + ")  \n"
                                    + stryansbh + ") union ("
                                    +
                                    "	select distinct h.hetfhr_id as gongysb_id from hetfhrgysglb h\n" +
                                    "       where h.zhuangt=1 and h.gongysb_id in (\n" +
                                    "       	select distinct gongysb_id from fahb f where \n";

                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') and \n ";
                            }


                            sql += " liucztb_id=1 and diancxxb_id in (" + MainGlobal.getProperIds(getFencbModel(), this.getChangb()) + ")  \n"
                                    + stryansbh + " ))) or h.xufdwmc in (select quanc from diancxxb where id=" + ((Visit) getPage().getVisit()).getString15() + "))\n";

                            if (!stryansbh.equals("")) {

                                sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id="
                                        + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                        + "', 'yyyy-mm-dd') order by hetbh ";
                            } else {

                                sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') order by hetbh ";
                            }

                        } else {
//							�ֹ�˾��δѡ��Ӧ�̣��ֳ����˷ѽ���
                            sql = " select distinct hetys.id,hetbh from hetysjgb,hetys,yunsdwb ys where hetysjgb.hetys_id=hetys.id and hetys.yunsdwb_id=ys.id \n";

                            if (!stryansbh.equals("")) {

                                sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                        + "', 'yyyy-mm-dd') ";
                            } else {

                                sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                            }

                            sql += " and (meikxxb_id in (select distinct meikxxb_id from fahb f where \n ";

                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') \n ";
                            }

                            sql += " and liucztb_id=1 and diancxxb_id in (" + MainGlobal.getProperIds(getFencbModel(), this.getChangb()) + ")) or meikxxb_id=0) \n"
                                    + stryunsdw + stryansbh + " and hetys.diancxxb_id=" + ((Visit) getPage().getVisit()).getString15() + " 	\n"
                                    + " order by hetbh ";
                        }

                    } else {
//						���ֳ���
                        if (getJieslxValue().getId() != Locale.guotyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.haiyyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.daozyf_feiylbb_id
                                && getJieslxValue().getId() != Locale.dityf_feiylbb_id) {
//							�ֹ�˾�ɹ���δѡ��Ӧ�̣����ֳ���ú�����

                            String caight = "";
                            if (MainGlobal.getXitxx_item("����", "�Ƿ�Ϊɽ���ֹ�˾�ɹ����㵥", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "��").equals("��")) {
                                caight = "\nand h.leib = 2";
                            }

                            sql = " select distinct h.id,h.hetbh from hetb h where h.diancxxb_id="
                                    + ((Visit) getPage().getVisit()).getString15() + caight
                                    + " and (h.gongysb_id in ((select distinct gongysb_id from fahb f where ";
                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') and \n ";
                            }

                            sql += " liucztb_id=1 and diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id() + "  \n"
                                    + stryansbh + ") union ("
                                    +
                                    "	select distinct h.hetfhr_id as gongysb_id from hetfhrgysglb h\n" +
                                    "       where h.zhuangt=1 and h.gongysb_id in (\n" +
                                    "       	select distinct gongysb_id from fahb f where \n";

                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') and \n ";
                            }

                            sql += " liucztb_id=1 and diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id() + "  \n"
                                    + stryansbh + " ))) or h.xufdwmc in (select quanc from diancxxb where id=" + ((Visit) getPage().getVisit()).getString15() + "))\n";

                            if (!stryansbh.equals("")) {

                                sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id="
                                        + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                        + "', 'yyyy-mm-dd') order by hetbh ";
                            } else {

                                sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') order by hetbh ";
                            }

                        } else {
//							�ֹ�˾�ɹ���δѡ��Ӧ�̣����ֳ����˷ѽ���
                            sql = " select distinct hetys.id,hetbh from hetysjgb,hetys,yunsdwb ys where hetysjgb.hetys_id=hetys.id and hetys.yunsdwb_id=ys.id	\n";
                            if (!stryansbh.equals("")) {

                                sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                        + "', 'yyyy-mm-dd') ";
                            } else {

                                sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                            }
                            sql += " and (meikxxb_id in (select distinct meikxxb_id from fahb where \n ";

                            if (stryansbh.equals("")) {

                                sql += riq + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd') \n and "
                                        + riq + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd') and\n ";
                            }

                            sql += " liucztb_id=1 and dianxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id() + ") or meikxxb_id = 0) \n"
                                    + stryunsdw + stryansbh + " and hetys.diancxxb_id=" + ((Visit) getPage().getVisit()).getString15() + " 	\n"
                                    + " order by hetbh ";
                        }
                    }

                } else {

                    String mstr_gongys_id = "";
                    if (gongysb_id.indexOf(",") == -1) {

                        mstr_gongys_id = MainGlobal.getLeaf_ParentNodeId(this.getTree(), gongysb_id);
                    } else {

                        mstr_gongys_id = MainGlobal.getLeaf_ParentNodeId(this.getTree(),
                                gongysb_id.substring(0, gongysb_id.indexOf(",")));
                    }

                    if (mstr_gongys_id.equals("")) {

                        setMsg("��ú��û�ж�Ӧ�Ĺ�Ӧ����Ϣ��");
                    } else {

                        if (((Visit) getPage().getVisit()).isFencb()) {

                            if (getJieslxValue().getId() != Locale.guotyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.haiyyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.daozyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.dityf_feiylbb_id) {
//								�ֹ�˾�ɹ���ѡ���˹�Ӧ�̣�һ�����ƣ�ú�����
                                sql = "select distinct h.id,h.hetbh from hetb h where \n"
                                        + " h.diancxxb_id="
                                        + ((Visit) getPage().getVisit()).getString15()
                                        + " and ((gongysb_id=" + mstr_gongys_id + " or gongysb_id in ("
                                        + "	select distinct h.hetfhr_id as gongysb_id\n"
                                        + "       from hetfhrgysglb h\n"
                                        + "            where h.gongysb_id=" + mstr_gongys_id + "))	\n "
                                        + " or h.xufdwmc=(select quanc from diancxxb where id="
                                        + ((Visit) getPage().getVisit()).getString15() + "))";

                                if (!stryansbh.equals("")) {

                                    sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                            + "', 'yyyy-mm-dd') ";
                                } else {

//										sql+=" and guoqrq>=to_date('"+getRiq2()+"', 'yyyy-mm-dd') order by hetbh ";
                                    sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                                }
                                sql += " order by hetbh ";

                            } else {
//								�ֹ�˾�ɹ���ѡ���˹�Ӧ�̣�һ�����ƣ��˷ѽ���
                                sql = " select distinct hetys.id,hetbh from hetysjgb,hetys,yunsdwb ys where hetysjgb.hetys_id=hetys.id and hetys.yunsdwb_id=ys.id	\n"
                                        + " and (meikxxb_id in	\n"
                                        + " (select mk.id		\n"
                                        + " from gongysmkglb glb,gongysb gys,meikxxb mk		\n"
                                        + " where gys.id=glb.gongysb_id and mk.id=glb.meikxxb_id	\n"
                                        + " and (gys.id in (" + gongysb_id + ") or mk.id in (" + gongysb_id + "))) or meikxxb_id=0) \n"
                                        + " and hetys.diancxxb_id="
                                        + ((Visit) getPage().getVisit()).getString15();
                                if (!stryansbh.equals("")) {

                                    sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                            + "', 'yyyy-mm-dd') ";
                                } else {

                                    sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                                }

                                sql += " order by hetbh ";
                            }

                        } else {
//
                            if (getJieslxValue().getId() != Locale.guotyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.haiyyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.daozyf_feiylbb_id
                                    && getJieslxValue().getId() != Locale.dityf_feiylbb_id) {
//								�ֹ�˾�ɹ���ѡ���˹�Ӧ�̣���һ�糧��ú�����
                                sql = "select distinct h.id,h.hetbh from hetb h,hetslb hs where h.id=hs.hetb_id(+) and h.diancxxb_id="
                                        + ((Visit) getPage().getVisit()).getString15()
                                        + " and ((gongysb_id=" + mstr_gongys_id + " or gongysb_id in ("
                                        + "	select distinct h.hetfhr_id as gongysb_id\n"
                                        + "       from hetfhrgysglb h\n"
                                        + "            where h.gongysb_id=" + mstr_gongys_id + "))	\n "
                                        + " or h.xufdwmc in (select quanc from diancxxb where id="
                                        + ((Visit) getPage().getVisit()).getString15() + "))";

                                if (!stryansbh.equals("")) {

                                    sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                            + "', 'yyyy-mm-dd') ";
                                } else {

                                    sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                                }

                                sql += " order by hetbh ";
                            } else {

                                sql = " select distinct hetys.id,hetbh from hetysjgb,hetys,yunsdwb ys where hetysjgb.hetys_id=hetys.id and hetys.yunsdwb_id=ys.id	\n"
                                        + " and (meikxxb_id in	\n"
                                        + " (select mk.id		\n"
                                        + " from gongysmkglb glb,gongysb gys,meikxxb mk		\n"
                                        + " where gys.id=glb.gongysb_id and mk.id=glb.meikxxb_id	\n"
                                        + " and (gys.id in (" + gongysb_id + ") or mk.id in (" + gongysb_id + "))) or meikxxb_id=0) 	\n"
                                        + " and hetys.diancxxb_id=" + ((Visit) getPage().getVisit()).getString15();

                                if (!stryansbh.equals("")) {

                                    sql += " and guoqrq>=to_date('" + MainGlobal.getTableCol("fahb f", "to_char(max(" + riq + "),'yyyy-MM-dd')", " yansbhb_id=" + MainGlobal.getProperId(this.getYansbhModel(), this.getYansbh()) + "")
                                            + "', 'yyyy-mm-dd') ";
                                } else {

                                    sql += " and guoqrq>=to_date('" + getRiq2() + "', 'yyyy-mm-dd') ";
                                }
                                sql += " order by hetbh ";
                            }
                        }
                    }
                }
            }

            ResultSetList rsl = con.getResultSetList(sql);
            if (rsl.getRows() == 0 && !gongysb_id.equals("")) {

                if (!((Visit) getPage().getVisit()).getString17().equals("tb")) {

                    setMsg("�ù�Ӧ����ϵͳ��û�ж�Ӧ�ĺ�ͬ��");
                }
            }

            while (rsl.next()) {

                List.add(new IDropDownBean(rsl.getLong("id"), rsl.getString("hetbh")));
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
        ((Visit) getPage().getVisit())
                .setProSelectionModel4(new IDropDownModel(List));
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }


    // �õ���½�û����ڵ糧���߷ֹ�˾������
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
        } catch (SQLException e) {
            // TODO �Զ����� catch ��
            e.printStackTrace();
        } finally {
            cn.Close();
        }

        return diancmc;

    }

    // �õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����d
    public String getIDropDownDiancmc(String diancmcId) {
        if (diancmcId == null || diancmcId.equals("")) {
            diancmcId = "1";
        }
        String IDropDownDiancmc = "";
        JDBCcon cn = new JDBCcon();

        String sql_diancmc = "select d.mingc from diancxxb d where d.id="
                + diancmcId;
        ResultSet rs = cn.getResultSet(sql_diancmc);
        try {
            while (rs.next()) {
                IDropDownDiancmc = rs.getString("mingc");
            }
        } catch (SQLException e) {
            // TODO �Զ����� catch ��
            e.printStackTrace();
        } finally {
            cn.Close();
        }

        return IDropDownDiancmc;

    }

    //����
    public boolean _Fencbchange = false;

    public IDropDownBean getFencbValue() {

        if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
            ((Visit) getPage().getVisit())
                    .setDropDownBean5((IDropDownBean) getFencbModel()
                            .getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean5();
    }

    public void setFencbValue(IDropDownBean Value) {

        if (Value != ((Visit) getPage().getVisit()).getDropDownBean5()) {

            ((Visit) getPage().getVisit()).setboolean3(true);
        }
        ((Visit) getPage().getVisit()).setDropDownBean5(Value);
    }

    public IPropertySelectionModel getFencbModel() {

        if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

            getFencbModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel5();
    }

    public void setFencbModel(IPropertySelectionModel value) {

        ((Visit) getPage().getVisit()).setProSelectionModel5(value);
    }

    public IPropertySelectionModel getFencbModels() {

//			String sql ="select id,mingc from diancxxb d where d.fuid="+
//			((Visit) getPage().getVisit()).getDiancxxb_id()+"order by mingc";
        JDBCcon con = new JDBCcon();
        String sql =
                "select id,mingc from diancxxb d where d.fuid="
                        + ((Visit) getPage().getVisit()).getDiancxxb_id() + " \n" +
                        "order by xuh,mingc";
        ResultSetList rsl = con.getResultSetList(sql);
        if (rsl.getRows() == 0) {

            sql = "select id,mingc from diancxxb where id=" + ((Visit) getPage().getVisit()).getDiancxxb_id();
        }
        rsl.close();
        con.Close();
        ((Visit) getPage().getVisit())
                .setProSelectionModel5(new IDropDownModel(sql));
        return ((Visit) getPage().getVisit()).getProSelectionModel5();
    }

//	���䵥λ_begin

    public IDropDownBean getYunsdwValue() {
       logger.info ("getYunsdwValue:"+((Visit) getPage().getVisit()).getDropDownBean6());
        if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
            ((Visit) getPage().getVisit())
                    .setDropDownBean6((IDropDownBean) getYunsdwModel()
                            .getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean6();
    }

    public void setYunsdwValue(IDropDownBean Value) {

        if (Value != ((Visit) getPage().getVisit()).getDropDownBean6()) {

            ((Visit) getPage().getVisit()).setboolean2(true);
        }
        ((Visit) getPage().getVisit()).setDropDownBean6(Value);
    }

    public IPropertySelectionModel getYunsdwModel() {

        if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {

            getYunsdwModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel6();
    }

    public void setYunsdwModel(IPropertySelectionModel value) {

        ((Visit) getPage().getVisit()).setProSelectionModel6(value);
    }

    public IPropertySelectionModel getYunsdwModels() {

        String Diancxxb_id = "0";
        String sql = "";
        if (((Visit) getPage().getVisit()).isFencb()) {

            Diancxxb_id = MainGlobal.getProperIds(this.getFencbModel(), this.getChangb());
        } else {

            Diancxxb_id = String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
        }

        if (this.getJieslxValue().getId() == Locale.daozyf_feiylbb_id) {
//			����ǡ���װ�˷ѡ����㣬Ҫ�ӵ�װ��Ƥ��ȡֵ

            sql = "select distinct rownum as id,yunsdw \n" +
                    "		from fahb fh,daozcpb c,zhilb z\n" +
                    "		where fh.id=c.fahb_id \n" +
                    "			and " + rb1() + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd')\n" +
                    "			and " + rb1() + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd')\n" +
                    "			and fh.diancxxb_id in (" + Diancxxb_id + ")	\n" +
                    "			and fh.liucztb_id=1 and z.liucztb_id=1\n" +
                    "			and fh.zhilb_id = z.id" +
                    "		order by c.yunsdw";

        } else {

            sql = "select distinct y.id,y.mingc\n" +
                    "		from fahb fh,chepb c,zhilb z,yunsdwb y\n" +
                    "		where fh.id=c.fahb_id and c.yunsdwb_id=y.id\n" +
                    "			and " + rb1() + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd')\n" +
                    "			and " + rb1() + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd')\n" +
                    "			and fh.diancxxb_id in (" + Diancxxb_id + ")	\n" +
                    "			and fh.liucztb_id=1 and z.liucztb_id=1\n" +
                    "			and fh.zhilb_id = z.id" +
                    "		order by y.mingc";
        }

        ((Visit) getPage().getVisit())
                .setProSelectionModel6(new IDropDownModel(sql, "ȫ��"));
        return ((Visit) getPage().getVisit()).getProSelectionModel6();
    }
//	���䵥λ_end

//	Ʒ��_begin

    public IDropDownBean getPinzValue() {

        if (((Visit) getPage().getVisit()).getDropDownBean7() == null) {
            ((Visit) getPage().getVisit())
                    .setDropDownBean7((IDropDownBean) getPinzModel()
                            .getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean7();
    }

    public void setPinzValue(IDropDownBean Value) {

        if (Value != ((Visit) getPage().getVisit()).getDropDownBean7()) {

            ((Visit) getPage().getVisit()).setboolean2(true);
        }
        ((Visit) getPage().getVisit()).setDropDownBean7(Value);
    }

    public IPropertySelectionModel getPinzModel() {

        if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {

            getPinzModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel7();
    }

    public void setPinzModel(IPropertySelectionModel value) {

        ((Visit) getPage().getVisit()).setProSelectionModel7(value);
    }

    public IPropertySelectionModel getPinzModels() {

        String Diancxxb_id = "0";
        if (((Visit) getPage().getVisit()).isFencb()) {

            Diancxxb_id = MainGlobal.getProperIds(this.getFencbModel(), this.getChangb());
        } else {

            Diancxxb_id = String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
        }

        String sql = "select distinct p.id,p.mingc\n" +
                "               from fahb fh,zhilb z,pinzb p\n" +
                "               where fh.pinzb_id = p.id\n" +
                "                     and " + rb1() + ">=to_date('" + this.getRiq1() + "','yyyy-MM-dd')\n" +
                "                     and " + rb1() + "<=to_date('" + this.getRiq2() + "','yyyy-MM-dd')\n" +
                "					  and fh.diancxxb_id in (" + Diancxxb_id + ")	\n" +
                "                     and fh.zhilb_id=z.id\n" +
                "                     and fh.liucztb_id=1 and z.liucztb_id=1\n" +
                "               order by p.mingc";

        ((Visit) getPage().getVisit())
                .setProSelectionModel7(new IDropDownModel(sql, "ȫ��"));
        return ((Visit) getPage().getVisit()).getProSelectionModel7();
    }
//	Ʒ��_end

    //	tree_id_begin
    private String treeid;

    /*
	 * public String getTreeid() { return treeid; } public void setTreeid(String
	 * treeid) { this.treeid = treeid; }
	 */
    public String getTreeid() {
        // if(treeid==null||treeid.equals("")){
        // ((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit)
        // this.getPage().getVisit()).getDiancxxb_id()));
        // }
        if (((Visit) getPage().getVisit()).getString2() == null || ((Visit) getPage().getVisit()).getString2().equals("")) {

            ((Visit) getPage().getVisit()).setString2("0");
        }
        return ((Visit) getPage().getVisit()).getString2();
    }

    public void setTreeid(String treeid) {

        if (!((Visit) getPage().getVisit()).getString2().equals(treeid)) {

            ((Visit) getPage().getVisit()).setString2(treeid);
            ((Visit) getPage().getVisit()).setboolean2(true);
        }
    }
//	tree_id_end

//	gongysTree_id_begin

    public String getGongysmlttree_id() {

        String gongys_id = "";
        if (((Visit) getPage().getVisit()).getString14() != null
                && !((Visit) getPage().getVisit()).getString14().equals("")) {

            String change[] = ((Visit) getPage().getVisit()).getString14().split(";");
            if (change.length == 1 &&
                    (change[0].indexOf("+") == -1 && change[0].indexOf("-") == -1)) {

                gongys_id = change[0];
            } else {

                for (int i = 0; i < change.length; i++) {
                    if (change[i] == null || "".equals(change[i])) {
                        continue;
                    }
                    String record[] = change[i].split(",", 2);
                    String sign = record[0];
                    String ziyid = record[1];
                    if ("+".equals(sign)) {
                        gongys_id += ziyid + ",";
                    }
                }

                if (!gongys_id.equals("")) {

                    gongys_id = gongys_id.substring(0, gongys_id.lastIndexOf(","));
                }
            }

        }

        if (gongys_id.indexOf(",") == -1) {

            if (gongys_id.equals("")) {

                this.setTreeid("0");
            } else {

                this.setTreeid(MainGlobal.getLeaf_ParentNodeId(this.getTree(), gongys_id));
            }

        } else {

            this.setTreeid(MainGlobal.getLeaf_ParentNodeId(this.getTree(),
                    gongys_id.substring(0, gongys_id.indexOf(","))));
        }

        return gongys_id;
    }

    public void setGongysmlttree_id(String value) {

        ((Visit) getPage().getVisit()).setString14(value);
    }

//	gongysTree_id_end

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

    // �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧

    public String getWunScript() {

        return "";
    }
}