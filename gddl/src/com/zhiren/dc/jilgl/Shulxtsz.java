package com.zhiren.dc.jilgl;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.Radio;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
/**
 * 
 * @author Rock
 *
 */
/*
 * 2009-05-18
 * ����
 * ������ﵥ���á���¼��Ϣ���á�����������á����ݵ�������
 */
/*
 * 2009-05-15
 * ����
 * �����޸�Ĭ�϶����滻�ĳ�ʼ��ֵ����ȷ
 */
/*
 * 2009-02-23
 * ����
 * ����ϵͳ���������ӻ𳵡�����Ĭ�������ʵ�����
 */
/*
 * ���ߣ�������
 * ʱ�䣺2009-05-25
 * �����������Ƿ��Զ����ɲ�����š���ˮ�����Ƿ���ʾ���պĴ��ձ���ú����Լ�������������ĸ�����
 */
public abstract class Shulxtsz extends BasePage {
//	����ҳ����ʾ��Ϣ������
	private String _msg;
	public String getMsg() { 
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
	    _msg = _value;
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
//  ҳ��仯��¼
    private String Change;
    public String getChange() {
    	return Change;
    }
    public void setChange(String change) {
    	Change = change;
    }
//	ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(this.getPageName().toString());
		}
	}
	
	private String getInsertSql(int xuh,String diancxxb_id,String mingc,String zhi,String danw,String leib,int zhuangt,String beiz) {
		String sql = "insert into xitxxb(id,xuh,diancxxb_id,mingc,zhi,danw,leib,zhuangt,beiz) values(getnewid("
			+ diancxxb_id + ")," + xuh + "," + diancxxb_id + ",'" + mingc + "','" + zhi + "','" + danw +"','" 
			+ leib + "'," + zhuangt + ",'" + beiz +"')";
		return sql;
	}
	
	private String getXitxxSql(String zd,String mingc, String danw, String diancxxb_id) {
    	String sdanw = "";
    	if(danw != null) {
    		sdanw = " and danw='"+danw+"'";
    	}
    	String sql = "select "+zd+" from xitxxb where leib='����' and mingc='"+mingc
    	+"' and diancxxb_id=" + diancxxb_id + sdanw;
    	return sql;
    }
	
	private String UpdateXitxxSql(String zd,String zhi,String mingc, String danw, String diancxxb_id) {
		String sdanw = "";
    	if(danw != null) {
    		sdanw = " and danw='"+danw+"'";
    	}
		String sql = "update xitxxb set "+zd+"='"+zhi+"' where leib='����' and mingc='"+mingc
		+"' and diancxxb_id=" + diancxxb_id + sdanw;
		return sql;
	}
//  ȡ�ó�ʼ������
    public String getInitData() {
    	Visit visit = (Visit)this.getPage().getVisit();
    	String initData = "";
    	JDBCcon con = new JDBCcon();
    	
//    	����ȷ������
    	boolean guohdh = true;
    	boolean shenhdh = false;
    	ResultSetList rsl = con.getResultSetList(getXitxxSql("zhi","����ȷ������",null,String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()) {
    		if("����".equals(rsl.getString("zhi"))) {
    			guohdh = true;
    		}else
    			if("���".equals(rsl.getString("zhi"))) {
        			guohdh = false;
        			shenhdh = true;
        		}
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"����ȷ������","����","","����",1,""));
    	}
    	Radio rd1g = new Radio("daoh");
    	rd1g.setBoxLabel("���⼴����");
    	rd1g.setId("rd1g");
    	rd1g.setChecked(guohdh);
    	initData = initData + rd1g.getScript() + "\n";
    	
    	Radio rd1s = new Radio("daoh");
    	rd1s.setBoxLabel("��˼�����");
    	rd1s.setId("rd1s");
    	rd1s.setChecked(shenhdh);
    	initData = initData + rd1s.getScript() + "\n";
    	
//    	�ճ���������
    	boolean sriq = true;
    	boolean eriq = false;
    	rsl = con.getResultSetList(getXitxxSql("zhi","�ճ���������",null,String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()) {
    		if("��������".equals(rsl.getString("zhi"))) {
    			sriq = false;
    			eriq = true;
    		}else
    			if("��ʼ����".equals(rsl.getString("zhi"))) {
    				sriq = true;
    		    	eriq = false;
        		}
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"�ճ���������","��ʼ����","","����",1,""));
    	}
    	
    	Radio rdscrqs = new Radio("scrq");
    	rdscrqs.setBoxLabel("��ʼ����");
    	rdscrqs.setId("rdscrqs");
    	rdscrqs.setChecked(sriq);
    	initData = initData + rdscrqs.getScript() + "\n";
    	
    	Radio rdscrqe = new Radio("scrq");
    	rdscrqe.setBoxLabel("��������");
    	rdscrqe.setId("rdscrqe");
    	rdscrqe.setChecked(eriq);
    	initData = initData + rdscrqe.getScript() + "\n";
    	
//    	�Ժ�����
    	rsl = con.getResultSetList(getXitxxSql("zhi","��ʾ¼��ʱ��",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean xssj = false; 
    	if(rsl.next()) {
    		xssj = "��".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"��ʾ¼��ʱ��","��","","����",1,""));
    	}
    	Checkbox cb2x = new Checkbox();
    	cb2x.setBoxLabel("��ʾ¼��ʱ��");
    	cb2x.setId("cb2x");
    	cb2x.setChecked(xssj);
    	initData = initData + cb2x.getScript() + "\n";
    	
//    	�𳵲�������
    	//1����Ӧ��
    	rsl = con.getResultSetList(getXitxxSql("beiz","��Ӧ��","���˲������",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb3gyssy = true;
    	if(rsl.next()) {
    		cb3gyssy = "ʹ��".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"��Ӧ��","long,gongysb_id","���˲������","����",1,"ʹ��"));
    	}
    	Checkbox cb3gys = new Checkbox();
    	cb3gys.setBoxLabel("��Ӧ��");
    	cb3gys.setId("cb3gys");
    	cb3gys.setChecked(cb3gyssy);
    	initData = initData + cb3gys.getScript() + "\n";
    	
//    	2��ú��
    	rsl = con.getResultSetList(getXitxxSql("beiz","ú��","���˲������",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb3mksy = true;
    	if(rsl.next()) {
    		cb3mksy = "ʹ��".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(2,String.valueOf(visit.getDiancxxb_id()),"ú��","long,meikxxb_id","���˲������","����",1,"ʹ��"));
    	}
    	Checkbox cb3mk = new Checkbox();
    	cb3mk.setBoxLabel("ú��");
    	cb3mk.setId("cb3mk");
    	cb3mk.setChecked(cb3mksy);
    	initData = initData + cb3mk.getScript() + "\n";
    	
//    	3��Ʒ��
    	rsl = con.getResultSetList(getXitxxSql("beiz","Ʒ��","���˲������",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb3pzsy = true;
    	if(rsl.next()) {
    		cb3pzsy = "ʹ��".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(3,String.valueOf(visit.getDiancxxb_id()),"Ʒ��","long,pinzb_id","���˲������","����",1,"ʹ��"));
    	}
    	Checkbox cb3pz = new Checkbox();
    	cb3pz.setBoxLabel("Ʒ��");
    	cb3pz.setId("cb3pz");
    	cb3pz.setChecked(cb3pzsy);
    	initData = initData + cb3pz.getScript() + "\n";
    	
//    	4����վ
    	rsl = con.getResultSetList(getXitxxSql("beiz","��վ","���˲������",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb3fzsy = true;
    	if(rsl.next()) {
    		cb3fzsy = "ʹ��".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(4,String.valueOf(visit.getDiancxxb_id()),"��վ","long,faz_id","���˲������","����",1,"ʹ��"));
    	}
    	Checkbox cb3fz = new Checkbox();
    	cb3fz.setBoxLabel("��վ");
    	cb3fz.setId("cb3fz");
    	cb3fz.setChecked(cb3fzsy);
    	initData = initData + cb3fz.getScript() + "\n";
    	
//    	5���ھ�
    	rsl = con.getResultSetList(getXitxxSql("beiz","�ھ�","���˲������",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb3kjsy = true;
    	if(rsl.next()) {
    		cb3kjsy = "ʹ��".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(5,String.valueOf(visit.getDiancxxb_id()),"�ھ�","long,jihkjb_id","���˲������","����",1,"ʹ��"));
    	}
    	Checkbox cb3kj = new Checkbox();
    	cb3kj.setBoxLabel("�ھ�");
    	cb3kj.setId("cb3kj");
    	cb3kj.setChecked(cb3kjsy);
    	initData = initData + cb3kj.getScript() + "\n";
  
//    	6����������
    	rsl = con.getResultSetList(getXitxxSql("beiz","��������","���˲������",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb3fhrqsy = true;
    	if(rsl.next()) {
    		cb3fhrqsy = "ʹ��".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(6,String.valueOf(visit.getDiancxxb_id()),"��������","date,fahrq","���˲������","����",1,"ʹ��"));
    	}
    	Checkbox cb3fhrq = new Checkbox();
    	cb3fhrq.setBoxLabel("��������");
    	cb3fhrq.setId("cb3fhrq");
    	cb3fhrq.setChecked(cb3fhrqsy);
    	initData = initData + cb3fhrq.getScript() + "\n";
    	
//    	7����������
    	rsl = con.getResultSetList(getXitxxSql("beiz","��������","���˲������",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb3cyrqsy = true;
    	if(rsl.next()) {
    		cb3cyrqsy = "ʹ��".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(7,String.valueOf(visit.getDiancxxb_id()),"��������","date,caiyrq","���˲������","����",1,"ʹ��"));
    	}
    	Checkbox cb3cyrq = new Checkbox();
    	cb3cyrq.setBoxLabel("��������");
    	cb3cyrq.setId("cb3cyrq");
    	cb3cyrq.setChecked(cb3cyrqsy);
    	initData = initData + cb3cyrq.getScript() + "\n";
    	
//    	8������
    	rsl = con.getResultSetList(getXitxxSql("beiz","����","���˲������",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb3ccsy = true;
    	if(rsl.next()) {
    		cb3ccsy = "ʹ��".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(8,String.valueOf(visit.getDiancxxb_id()),"����","string,chec","���˲������","����",1,"ʹ��"));
    	}
    	Checkbox cb3cc = new Checkbox();
    	cb3cc.setBoxLabel("����");
    	cb3cc.setId("cb3cc");
    	cb3cc.setChecked(cb3ccsy);
    	initData = initData + cb3cc.getScript() + "\n";
    	
//    	9������
    	rsl = con.getResultSetList(getXitxxSql("beiz","����","���˲������",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb3cbsy = true;
    	if(rsl.next()) {
    		cb3cbsy = "ʹ��".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(9,String.valueOf(visit.getDiancxxb_id()),"����","long,diancxxb_id","���˲������","����",1,"ʹ��"));
    	}
    	Checkbox cb3cb = new Checkbox();
    	cb3cb.setBoxLabel("����");
    	cb3cb.setId("cb3cb");
    	cb3cb.setChecked(cb3cbsy);
    	initData = initData + cb3cb.getScript() + "\n";
    	
//    	������������
    	//1����Ӧ��
    	rsl = con.getResultSetList(getXitxxSql("beiz","��Ӧ��","���˲������",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb4gyssy = true;
    	if(rsl.next()) {
    		cb4gyssy = "ʹ��".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"��Ӧ��","long,gongysb_id","���˲������","����",1,"ʹ��"));
    	}
    	Checkbox cb4gys = new Checkbox();
    	cb4gys.setBoxLabel("��Ӧ��");
    	cb4gys.setId("cb4gys");
    	cb4gys.setChecked(cb4gyssy);
    	initData = initData + cb4gys.getScript() + "\n";
    	
//    	2��ú��
    	rsl = con.getResultSetList(getXitxxSql("beiz","ú��","���˲������",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb4mksy = true;
    	if(rsl.next()) {
    		cb4mksy = "ʹ��".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(2,String.valueOf(visit.getDiancxxb_id()),"ú��","long,meikxxb_id","���˲������","����",1,"ʹ��"));
    	}
    	Checkbox cb4mk = new Checkbox();
    	cb4mk.setBoxLabel("ú��");
    	cb4mk.setId("cb4mk");
    	cb4mk.setChecked(cb4mksy);
    	initData = initData + cb4mk.getScript() + "\n";
    	
//    	3��Ʒ��
    	rsl = con.getResultSetList(getXitxxSql("beiz","Ʒ��","���˲������",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb4pzsy = true;
    	if(rsl.next()) {
    		cb4pzsy = "ʹ��".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(3,String.valueOf(visit.getDiancxxb_id()),"Ʒ��","long,pinzb_id","���˲������","����",1,"ʹ��"));
    	}
    	Checkbox cb4pz = new Checkbox();
    	cb4pz.setBoxLabel("Ʒ��");
    	cb4pz.setId("cb4pz");
    	cb4pz.setChecked(cb4pzsy);
    	initData = initData + cb4pz.getScript() + "\n";
    	
//    	4����վ
    	rsl = con.getResultSetList(getXitxxSql("beiz","��վ","���˲������",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb4fzsy = true;
    	if(rsl.next()) {
    		cb4fzsy = "ʹ��".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(4,String.valueOf(visit.getDiancxxb_id()),"��վ","long,faz_id","���˲������","����",1,"ʹ��"));
    	}
    	Checkbox cb4fz = new Checkbox();
    	cb4fz.setBoxLabel("��վ");
    	cb4fz.setId("cb4fz");
    	cb4fz.setChecked(cb4fzsy);
    	initData = initData + cb4fz.getScript() + "\n";
    	
//    	5���ھ�
    	rsl = con.getResultSetList(getXitxxSql("beiz","�ھ�","���˲������",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb4kjsy = true;
    	if(rsl.next()) {
    		cb4kjsy = "ʹ��".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(5,String.valueOf(visit.getDiancxxb_id()),"�ھ�","long,jihkjb_id","���˲������","����",1,"ʹ��"));
    	}
    	Checkbox cb4kj = new Checkbox();
    	cb4kj.setBoxLabel("�ھ�");
    	cb4kj.setId("cb4kj");
    	cb4kj.setChecked(cb4kjsy);
    	initData = initData + cb4kj.getScript() + "\n";
  
//    	6����������
    	rsl = con.getResultSetList(getXitxxSql("beiz","��������","���˲������",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb4fhrqsy = true;
    	if(rsl.next()) {
    		cb4fhrqsy = "ʹ��".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(6,String.valueOf(visit.getDiancxxb_id()),"��������","date,fahrq","���˲������","����",1,"ʹ��"));
    	}
    	Checkbox cb4fhrq = new Checkbox();
    	cb4fhrq.setBoxLabel("��������");
    	cb4fhrq.setId("cb4fhrq");
    	cb4fhrq.setChecked(cb4fhrqsy);
    	initData = initData + cb4fhrq.getScript() + "\n";
    	
//    	7����������
    	rsl = con.getResultSetList(getXitxxSql("beiz","��������","���˲������",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb4cyrqsy = true;
    	if(rsl.next()) {
    		cb4cyrqsy = "ʹ��".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(7,String.valueOf(visit.getDiancxxb_id()),"��������","date,caiyrq","���˲������","����",1,"ʹ��"));
    	}
    	Checkbox cb4cyrq = new Checkbox();
    	cb4cyrq.setBoxLabel("��������");
    	cb4cyrq.setId("cb4cyrq");
    	cb4cyrq.setChecked(cb4cyrqsy);
    	initData = initData + cb4cyrq.getScript() + "\n";
    	
//    	8������
    	rsl = con.getResultSetList(getXitxxSql("beiz","����","���˲������",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb4ccsy = true;
    	if(rsl.next()) {
    		cb4ccsy = "ʹ��".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(8,String.valueOf(visit.getDiancxxb_id()),"����","string,chec","���˲������","����",1,"ʹ��"));
    	}
    	Checkbox cb4cc = new Checkbox();
    	cb4cc.setBoxLabel("����");
    	cb4cc.setId("cb4cc");
    	cb4cc.setChecked(cb4ccsy);
    	initData = initData + cb4cc.getScript() + "\n";
    	
//    	9������
    	rsl = con.getResultSetList(getXitxxSql("beiz","����","���˲������",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb4cbsy = true;
    	if(rsl.next()) {
    		cb4cbsy = "ʹ��".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(9,String.valueOf(visit.getDiancxxb_id()),"����","long,diancxxb_id","���˲������","����",1,"ʹ��"));
    	}
    	Checkbox cb4cb = new Checkbox();
    	cb4cb.setBoxLabel("����");
    	cb4cb.setId("cb4cb");
    	cb4cb.setChecked(cb4cbsy);
    	initData = initData + cb4cb.getScript() + "\n";
    	
//    	������㷽ʽ
    	boolean dc = true;
    	rsl = con.getResultSetList(getXitxxSql("zhi","������㷽��",null,String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()) {
    		if("����".equals(rsl.getString("zhi"))) {
    			dc = true;
    		}else
    			dc = false;
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"������㷽��","����","","����",1,"ʹ��"));
    	}
    	Radio rd5fp = new Radio("yuns");
    	rd5fp.setBoxLabel("������������");
    	rd5fp.setId("rd5fp");
    	rd5fp.setChecked(!dc);
    	initData = initData + rd5fp.getScript() + "\n";
    	
    	Radio rd5dc = new Radio("yuns");
    	rd5dc.setBoxLabel("������������");
    	rd5dc.setId("rd5dc");
    	rd5dc.setChecked(dc);
    	initData = initData + rd5dc.getScript() + "\n";    	
//    	�ռƻ����ɲ��� 
    	boolean jh = true;
    	rsl = con.getResultSetList(getXitxxSql("zhi","�ռƻ����ɲ���","����",String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()) {
    		if("��".equals(rsl.getString("zhi"))) {
    			jh = true;
    		}else
    			jh = false;
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"�ռƻ����ɲ���","��","����","����",1,"ʹ��"));
    	}
    	Radio rd6jh = new Radio("cyscd");
    	rd6jh.setBoxLabel("�ƻ�¼��");
    	rd6jh.setId("rd6jh");
    	rd6jh.setChecked(jh);
    	initData = initData + rd6jh.getScript() + "\n";
    	
    	Radio rd6dr = new Radio("cyscd");
    	rd6dr.setBoxLabel("���ݵ���");
    	rd6dr.setId("rd6dr");
    	rd6dr.setChecked(!jh);
    	initData = initData + rd6dr.getScript() + "\n";    	
    	
//    	�𳵲�¼�����Ƿ��Զ����ɲ������
    	boolean huocblbh = false;
    	rsl = con.getResultSetList(getXitxxSql("zhi","��¼�Զ����ɱ��","��",String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()) {
    		if("��".equals(rsl.getString("zhi"))) {
    			huocblbh = true;
    		}else
    			if("��".equals(rsl.getString("zhi"))) {
    				huocblbh = false;
        		}
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"��¼�Զ����ɱ��","��","��","����",1,""));
    	}
    	Radio rd7ybh = new Radio("hcblbh");
    	rd7ybh.setBoxLabel("�Զ����ɱ��");
    	rd7ybh.setId("rdybh");
    	rd7ybh.setChecked(huocblbh);
    	initData = initData + rd7ybh.getScript() + "\n";
    	
    	Radio rd7nbh = new Radio("hcblbh");
    	rd7nbh.setBoxLabel("�ֶ�¼����");
    	rd7nbh.setId("rdnbh");
    	rd7nbh.setChecked(!huocblbh);
    	initData = initData + rd7nbh.getScript() + "\n";
    	
//    	������¼�����Ƿ��Զ����ɲ������
    	boolean qicblbh = true;
    	rsl = con.getResultSetList(getXitxxSql("zhi","��¼�Զ����ɱ��","����",String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()) {
    		if("��".equals(rsl.getString("zhi"))) {
    			qicblbh = true;
    		}else
    			if("��".equals(rsl.getString("zhi"))) {
    				qicblbh = false;
        		}
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"��¼�Զ����ɱ��","��","����","����",1,""));
    	}
    	Radio rd8qybh = new Radio("qcblbh");
    	rd8qybh.setBoxLabel("�Զ����ɱ��");
    	rd8qybh.setId("rdqybh");
    	rd8qybh.setChecked(qicblbh);
    	initData = initData + rd8qybh.getScript() + "\n";
    	
    	Radio rd8qnbh = new Radio("qcblbh");
    	rd8qnbh.setBoxLabel("�ֶ�¼����");
    	rd8qnbh.setId("rdqnbh");
    	rd8qnbh.setChecked(!qicblbh);
    	initData = initData + rd8qnbh.getScript() + "\n";
    	
//    	�𳵺�����¼�복���Ƿ����Զ�����ѡ��
    	boolean huocczdjs = false;
    	rsl = con.getResultSetList(getXitxxSql("zhi","�𳵳����Զ�����",null,String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()){
    		if("��".equals(rsl.getString("zhi"))){
    			huocczdjs = true;
    		}else{
    			huocczdjs = false;
    		}
    	}else{
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"�𳵳����Զ�����","��","","����",1,""));
    	}
    	rsl.close();
    	Radio rd9hycczd = new Radio("hycc");
    	rd9hycczd.setBoxLabel("��������ѡ��");
    	rd9hycczd.setId("hycczd");
    	rd9hycczd.setChecked(huocczdjs);
    	initData = initData + rd9hycczd.getScript() + "\n";
    	
    	Radio rd9hyccsd = new Radio("hycc");
    	rd9hyccsd.setBoxLabel("�����ֶ���д");
    	rd9hyccsd.setId("hyccsd");
    	rd9hyccsd.setChecked(!huocczdjs);
    	initData = initData + rd9hyccsd.getScript() + "\n";
//    	�����������������ֶ�
    	String danw = "maoz-piz";
    	rsl = con.getResultSetList(getXitxxSql("danw","��������������",null,String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()){
    		danw = rsl.getString("danw");
    	}else{
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"��������������","0","jingz","����",1,"ʹ��"));
    	}
    	TextField tx = new TextField();
    	tx.setFieldLabel("���������ֶ�");
    	tx.setId("caiyfzzd");
    	tx.setValue(danw);
    	tx.setWidth(90);
    	initData = initData + tx.getScript() + "\n";
//    	������������������ֵ
    	double fzz = 0.0;
    	rsl = con.getResultSetList(getXitxxSql("zhi","��������������",null,String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()){
    		fzz = rsl.getDouble("zhi");
    	}
    	NumberField nx = new NumberField();
    	nx.setFieldLabel("��������ֵ");
    	nx.setId("caiyfzz");
    	nx.setValue(String.valueOf(fzz));
    	nx.setWidth(90);
    	initData = initData + nx.getScript() + "\n";
//    	ϵͳĬ��������
//    	��
    	double tlysl = 0.012;
    	rsl = con.getResultSetList(getXitxxSql("zhi","Ĭ��������","��",String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()){
    		tlysl = rsl.getDouble("zhi");
    	}else{
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"Ĭ��������","0.012","��","����",1,"ʹ��"));
    	}
    	NumberField hcysl = new NumberField();
    	hcysl.setFieldLabel("��Ĭ��������");
    	hcysl.setId("hcmrysl");
    	hcysl.setDecimalPrecision(3);
    	hcysl.setValue(String.valueOf(tlysl));
    	hcysl.setWidth(90);
    	initData = initData + hcysl.getScript() + "\n";
//    	����
    	double glysl = 0.01;
    	rsl = con.getResultSetList(getXitxxSql("zhi","Ĭ��������","����",String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()){
    		glysl = rsl.getDouble("zhi");
    	}else{
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"Ĭ��������","0.01","����","����",1,"ʹ��"));
    	}
    	NumberField qcysl = new NumberField();
    	qcysl.setFieldLabel("����Ĭ��������");
    	qcysl.setId("qcmrysl");
    	qcysl.setDecimalPrecision(3);
    	qcysl.setValue(String.valueOf(glysl));
    	qcysl.setWidth(90);
    	initData = initData + qcysl.getScript() + "\n";
//   	�������
    	rsl = con.getResultSetList(getXitxxSql("zhi","��˲����޸�����",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean cb6chsy = false;
    	if(rsl.next()) {
    		cb6chsy = "��".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"��˲����޸�����","��","","����",1,"ʹ��"));
    	}
    	Checkbox cb6ch = new Checkbox();
    	cb6ch.setBoxLabel("��˲����޸�����");
    	cb6ch.setId("cb6ch");
    	cb6ch.setChecked(cb6chsy);
    	initData = initData + cb6ch.getScript() + "\n";
//    	��ë���޸�����
    	rsl = con.getResultSetList(getXitxxSql("zhi","��ë�ؿ��޸�",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean hcmzkg = false;
    	if(rsl.next()) {
    		hcmzkg = "��".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"��ë�ؿ��޸�","��","","����",1,"ʹ��"));
    	}
    	Checkbox cbhcmzkg = new Checkbox();
    	cbhcmzkg.setBoxLabel("��ë�ؿ��޸�");
    	cbhcmzkg.setId("hcmzkg");
    	cbhcmzkg.setChecked(hcmzkg);
    	initData = initData + cbhcmzkg.getScript() + "\n";
//    	����ë���޸�����
    	rsl = con.getResultSetList(getXitxxSql("zhi","����ë�ؿ��޸�",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean qcmzkg = false;
    	if(rsl.next()) {
    		qcmzkg = "��".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"����ë�ؿ��޸�","��","","����",1,"ʹ��"));
    	}
    	Checkbox cbqcmzkg = new Checkbox();
    	cbqcmzkg.setBoxLabel("����ë�ؿ��޸�");
    	cbqcmzkg.setId("qcmzkg");
    	cbqcmzkg.setChecked(qcmzkg);
    	initData = initData + cbqcmzkg.getScript() + "\n";
//    	��Ƥ���޸�����
    	rsl = con.getResultSetList(getXitxxSql("zhi","�𳵲����޸�Ƥ��",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean cb6hcsy = false;
    	if(rsl.next()) {
    		cb6hcsy = "��".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"�𳵲����޸�Ƥ��","��","","����",1,"ʹ��"));
    	}
    	Checkbox cb6hc = new Checkbox();
    	cb6hc.setBoxLabel("�𳵲����޸�Ƥ��");
    	cb6hc.setId("cb6hc");
    	cb6hc.setChecked(cb6hcsy);
    	initData = initData + cb6hc.getScript() + "\n";
//    	���������޸�Ƥ��
    	rsl = con.getResultSetList(getXitxxSql("zhi","�������޸�Ƥ��",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean cb6qcsy = false;
    	if(rsl.next()) {
    		cb6qcsy = "��".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"�������޸�Ƥ��","��","","����",1,"ʹ��"));
    	}
    	Checkbox cb6qc = new Checkbox();
    	cb6qc.setBoxLabel("�������޸�Ƥ��");
    	cb6qc.setId("cb6qc");
    	cb6qc.setChecked(cb6qcsy);
    	initData = initData + cb6qc.getScript() + "\n";
//    	����Ϊ��������
    	rsl = con.getResultSetList(getXitxxSql("zhi","����Ϊ�������",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean caiysz1sy = false;
    	if(rsl.next()) {
    		caiysz1sy = "��".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"����Ϊ�������","��","","����",1,"ʹ��"));
    	}
    	Checkbox caiysz1 = new Checkbox();
    	caiysz1.setBoxLabel("����Ϊ�������");
    	caiysz1.setId("caiysz1");
    	caiysz1.setChecked(caiysz1sy);
    	initData = initData + caiysz1.getScript() + "\n";
//    	�������κ�Ϊ��������
    	rsl = con.getResultSetList(getXitxxSql("zhi","�������κ�Ϊ�������",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean jincphcy = false;
    	if(rsl.next()) {
    		jincphcy = "��".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"�������κ�Ϊ�������","��","","����",1,"ʹ��"));
    	}
    	Checkbox caiysz2 = new Checkbox();
    	caiysz2.setBoxLabel("�������κ�Ϊ�������");
    	caiysz2.setId("caiysz2");
    	caiysz2.setChecked(jincphcy);
    	initData = initData + caiysz2.getScript() + "\n";
    	
//    	�����޸�Ĭ�϶����滻
    	rsl = con.getResultSetList(getXitxxSql("zhi","�����޸�Ĭ�϶����滻",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean th = false; 
    	if(rsl.next()) {
    		th = "��".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"�����޸�Ĭ�϶����滻","��","","����",1,"ʹ��"));
    	}
    	Checkbox cb3x = new Checkbox();
    	cb3x.setBoxLabel("�����޸�Ĭ�϶����滻");
    	cb3x.setId("cb3x");
    	cb3x.setChecked(th);
    	initData = initData + cb3x.getScript() + "\n";
    	
//    	������ﵥģʽ
    	String mos = "";
    	rsl = con.getResultSetList(getXitxxSql("zhi","�������ﵥģʽ",null,String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()){
    		mos = rsl.getString("zhi");
    	}else{
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"�������ﵥģʽ","PRINT_GUIGUAN",null,"����",1,"ʹ��"));
    	}
    	TextField t1x = new TextField();
    	t1x.setFieldLabel("�������ﵥģʽ");
    	t1x.setId("qicjjdms");
    	t1x.setValue(mos);
    	t1x.setWidth(120);
    	initData = initData + t1x.getScript() + "\n";
    	
//    	��¼ë���Զ�����
    	rsl = con.getResultSetList(getXitxxSql("zhi","��¼ë���Զ�����",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean mzjs = false; 
    	if(rsl.next()) {
    		mzjs = "��".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"��¼ë���Զ�����","��","��·","����",1,"ʹ��"));
    	}
    	Checkbox cb4x = new Checkbox();
    	cb4x.setBoxLabel("��¼ë���Զ�����");
    	cb4x.setId("cb4x");
    	cb4x.setChecked(mzjs);
    	initData = initData + cb4x.getScript() + "\n";
    	
//    	��¼Ĭ��Ƥ��
    	double mfpz = 0.0;
    	rsl = con.getResultSetList(getXitxxSql("zhi","��¼Ĭ��Ƥ��",null,String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()){
    		mfpz = rsl.getDouble("zhi");
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"��¼Ĭ��Ƥ��","0","��·","����",1,"ʹ��"));
    	}
    	NumberField nx1 = new NumberField();
    	nx1.setFieldLabel("��¼Ĭ��Ƥ��");
    	nx1.setId("mfpz");
    	nx1.setValue(String.valueOf(mfpz));
    	nx1.setWidth(90);
    	initData = initData + nx1.getScript() + "\n";
    	
//    	�ᳵ����Ƿ����ɱ�
    	rsl = con.getResultSetList(getXitxxSql("zhi","�ᳵ����Ƿ����ɱ�",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean jscb = false; 
    	if(rsl.next()) {
    		jscb = "��".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"�ᳵ����Ƿ����ɱ�","��","","����",1,"ʹ��"));
    	}
    	Checkbox cb5x = new Checkbox();
    	cb5x.setBoxLabel("�ᳵ����Ƿ����ɱ�");
    	cb5x.setId("cb5x");
    	cb5x.setChecked(jscb);
    	initData = initData + cb5x.getScript() + "\n";
    	
//    	���ݵ���Ĭ������
    	double mrrq = 0.0;
    	rsl = con.getResultSetList(getXitxxSql("zhi","���ݵ���Ĭ������",null,String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()){
    		mrrq = rsl.getDouble("zhi");
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"���ݵ���Ĭ������","0","","����",1,"ʹ��"));
    	}
    	NumberField nx2 = new NumberField();
    	nx2.setFieldLabel("���ݵ���Ĭ������");
    	nx2.setId("mrrq");
    	nx2.setValue(String.valueOf(mrrq));
    	nx2.setWidth(90);
    	initData = initData + nx2.getScript() + "\n";
    	
//    	�Ƿ��Զ����ɲ������
    	rsl = con.getResultSetList(getXitxxSql("zhi","�Ƿ��Զ����ɲ���",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean cybh = true;
    	if(rsl.next()) {
    		cybh = "��".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1, String.valueOf(visit.getDiancxxb_id()), "�Ƿ��Զ����ɲ���", "��", "", "����", 1, "ʹ��"));
    	}
    	Checkbox cbcybh = new Checkbox();
    	cbcybh.setBoxLabel("�Զ����ɲ���");
    	cbcybh.setId("cbcybh");
    	cbcybh.setChecked(cybh);
    	initData = initData + cbcybh.getScript() + "\n";
    	
//    	�Ƿ���ʾ��ˮ����
    	rsl = con.getResultSetList(getXitxxSql("zhi","��ˮ�����Ƿ���ʾ",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean kskz = false;
    	if(rsl.next()) {
    		kskz = "��".equals(rsl.getString("zhi"));
    	} else {
    		con.getInsert(getInsertSql(1, String.valueOf(visit.getDiancxxb_id()), "��ˮ�����Ƿ���ʾ", "��", "", "����", 1, "ʹ��"));
    	}
    	Checkbox cbkskz = new Checkbox();
    	cbkskz.setBoxLabel("��ʾ��ˮ����");
    	cbkskz.setId("cbkskz");
    	cbkskz.setChecked(kskz);
    	initData = initData + cbkskz.getScript() + "\n";
    	
//    	�պĴ��ձ���Լ����
    	rsl = con.getResultSetList(getXitxxSql("zhi","�պĴ��ձ���ú����Լ",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean shcxy = false;
    	if(rsl.next()) {
    		shcxy = "��".equals(rsl.getString("zhi"));
    	} else {
    		con.getInsert(getInsertSql(1, String.valueOf(visit.getDiancxxb_id()), "�պĴ��ձ���ú����Լ", "��", "", "����", 1, ""));
    	}
    	Checkbox cbshcxy = new Checkbox();
    	cbshcxy.setBoxLabel("��Լ�պĴ��ձ���ú��");
    	cbshcxy.setId("cbshcxy");
    	cbshcxy.setChecked(shcxy);
    	initData = initData + cbshcxy.getScript() + "\n";
    	
//    	��������������
    	rsl = con.getResultSetList(getXitxxSql("zhi","����������",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean azlfl = false;
    	if(rsl.next()) {
    		azlfl = "��".equals(rsl.getString("zhi"));
    	} else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"����������","��",null, "����", 1, "ʹ��"));
    	}
    	Checkbox cbazlfl = new Checkbox();
    	cbazlfl.setBoxLabel("����������");
    	cbazlfl.setId("cbazlfl");
    	cbazlfl.setChecked(azlfl);
    	initData = initData + cbazlfl.getScript() + "\n";
    	
    	return initData;
    }
    
    
//	��ť�¼�����
	private boolean _SaveChick = false;
    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }
    
    public void submit(IRequestCycle cycle) {
    	if (_SaveChick) {
    		_SaveChick = false;
    		Save();
        }
    }
//	�������ĸĶ�
	private void Save() {
		if(getChange()==null || "".equals(getChange())) {
//			��ʾ��Ϣ
			setMsg("�޸�Ϊ��!");
			return ;
		}
		String change[] = getChange().split(";");
		Visit visit = (Visit)this.getPage().getVisit();
    	JDBCcon con = new JDBCcon();
    	for(int i=0 ; i<change.length ; i++) {
    		String ch[] = change[i].split(",");
    		String mingc = ch[0];
    		String zhi = ch[1];
    		if("rd1g".equals(mingc)) {
    			String strzhi = "����";
    			if("true".equals(zhi)) {
    				strzhi = "����";
    			}else {
    				strzhi = "���";
    			}
    			con.getUpdate(UpdateXitxxSql("zhi",strzhi,"����ȷ������",null,String.valueOf(visit.getDiancxxb_id())));
    			continue;
    		}else if("rdscrqs".equals(mingc)) {
    			String strzhi = "��ʼ����";
    			if("true".equals(zhi)) {
    				strzhi = "��ʼ����";
    			}else {
    				strzhi = "��������";
    			}
    			con.getUpdate(UpdateXitxxSql("zhi",strzhi,"�ճ���������",null,String.valueOf(visit.getDiancxxb_id())));
    			continue;
    		}else if("cb2x".equals(mingc)) {
				String strzhi = "��";
				if("true".equals(zhi)) {
					strzhi = "��";
				}else {
					strzhi = "��";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"��ʾ¼��ʱ��",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
    		}else if("cb3gys".equals(mingc)) {
				String strzhi = "ʹ��";
				if("true".equals(zhi)) {
					strzhi = "ʹ��";
				}else {
					strzhi = "��ʹ��";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"��Ӧ��","���˲������",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb3mk".equals(mingc)) {
				String strzhi = "ʹ��";
				if("true".equals(zhi)) {
					strzhi = "ʹ��";
				}else {
					strzhi = "��ʹ��";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"ú��","���˲������",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb3pz".equals(mingc)) {
				String strzhi = "ʹ��";
				if("true".equals(zhi)) {
					strzhi = "ʹ��";
				}else {
					strzhi = "��ʹ��";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"Ʒ��","���˲������",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb3fz".equals(mingc)) {
				String strzhi = "ʹ��";
				if("true".equals(zhi)) {
					strzhi = "ʹ��";
				}else {
					strzhi = "��ʹ��";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"��վ","���˲������",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb3kj".equals(mingc)) {
				String strzhi = "ʹ��";
				if("true".equals(zhi)) {
					strzhi = "ʹ��";
				}else {
					strzhi = "��ʹ��";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"�ھ�","���˲������",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb3fhrq".equals(mingc)) {
				String strzhi = "ʹ��";
				if("true".equals(zhi)) {
					strzhi = "ʹ��";
				}else {
					strzhi = "��ʹ��";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"��������","���˲������",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb3cyrq".equals(mingc)) {
				String strzhi = "ʹ��";
				if("true".equals(zhi)) {
					strzhi = "ʹ��";
				}else {
					strzhi = "��ʹ��";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"��������","���˲������",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb3cc".equals(mingc)) {
				String strzhi = "ʹ��";
				if("true".equals(zhi)) {
					strzhi = "ʹ��";
				}else {
					strzhi = "��ʹ��";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"����","���˲������",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb3cb".equals(mingc)) {
				String strzhi = "ʹ��";
				if("true".equals(zhi)) {
					strzhi = "ʹ��";
				}else {
					strzhi = "��ʹ��";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"����","���˲������",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb4gys".equals(mingc)) {
				String strzhi = "ʹ��";
				if("true".equals(zhi)) {
					strzhi = "ʹ��";
				}else {
					strzhi = "��ʹ��";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"��Ӧ��","���˲������",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb4mk".equals(mingc)) {
				String strzhi = "ʹ��";
				if("true".equals(zhi)) {
					strzhi = "ʹ��";
				}else {
					strzhi = "��ʹ��";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"ú��","���˲������",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb4pz".equals(mingc)) {
				String strzhi = "ʹ��";
				if("true".equals(zhi)) {
					strzhi = "ʹ��";
				}else {
					strzhi = "��ʹ��";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"Ʒ��","���˲������",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb4fz".equals(mingc)) {
				String strzhi = "ʹ��";
				if("true".equals(zhi)) {
					strzhi = "ʹ��";
				}else {
					strzhi = "��ʹ��";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"��վ","���˲������",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb4kj".equals(mingc)) {
				String strzhi = "ʹ��";
				if("true".equals(zhi)) {
					strzhi = "ʹ��";
				}else {
					strzhi = "��ʹ��";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"�ھ�","���˲������",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb4fhrq".equals(mingc)) {
				String strzhi = "ʹ��";
				if("true".equals(zhi)) {
					strzhi = "ʹ��";
				}else {
					strzhi = "��ʹ��";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"��������","���˲������",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb4cyrq".equals(mingc)) {
				String strzhi = "ʹ��";
				if("true".equals(zhi)) {
					strzhi = "ʹ��";
				}else {
					strzhi = "��ʹ��";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"��������","���˲������",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb4cc".equals(mingc)) {
				String strzhi = "ʹ��";
				if("true".equals(zhi)) {
					strzhi = "ʹ��";
				}else {
					strzhi = "��ʹ��";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"����","���˲������",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb4cb".equals(mingc)) {
				String strzhi = "ʹ��";
				if("true".equals(zhi)) {
					strzhi = "ʹ��";
				}else {
					strzhi = "��ʹ��";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"����","���˲������",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("rd5dc".equals(mingc)) {
				String strzhi = "����";
				if("true".equals(zhi)) {
					strzhi = "����";
				}else {
					strzhi = "����";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"������㷽��",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("rd6jh".equals(mingc)) {
				String strzhi = "��";
				if("true".equals(zhi)) {
					strzhi = "��";
				}else {
					strzhi = "��";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"�ռƻ����ɲ���","����",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("caiyfzzd".equals(mingc)){
				con.getUpdate(UpdateXitxxSql("danw",zhi,"��������������",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("caiyfzz".equals(mingc)){
				con.getUpdate(UpdateXitxxSql("zhi",zhi,"��������������",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("rdybh".equals(mingc)) {
				String strzhi = "��";
				if("true".equals(zhi)) {
					strzhi = "��";
				}else {
					strzhi = "��";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"��¼�Զ����ɱ��","��",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("rdqybh".equals(mingc)) {
				String strzhi = "��";
				if("true".equals(zhi)) {
					strzhi = "��";
				}else {
					strzhi = "��";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"��¼�Զ����ɱ��","����",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("hycczd".equals(mingc)) {
				String strzhi = "��";
				if("true".equals(zhi)) {
					strzhi = "��";
				}else {
					strzhi = "��";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"�𳵳����Զ�����",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}
			else if("cb6ch".equals(mingc)) {
				String strzhi = "��";
				if("true".equals(zhi)) {
					strzhi = "��";
				}else {
					strzhi = "��";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"��˲����޸�����",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}
			else if("cb6hc".equals(mingc)) {
				String strzhi = "��";
				if("true".equals(zhi)) {
					strzhi = "��";
				}else {
					strzhi = "��";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"�𳵲����޸�Ƥ��",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}
			else if("hcmzkg".equals(mingc)) {
				String strzhi = "��";
				if("true".equals(zhi)) {
					strzhi = "��";
				}else {
					strzhi = "��";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"��ë�ؿ��޸�",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}
			else if("qcmzkg".equals(mingc)) {
				String strzhi = "��";
				if("true".equals(zhi)) {
					strzhi = "��";
				}else {
					strzhi = "��";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"����ë�ؿ��޸�",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}
			else if("cb6qc".equals(mingc)) {
				String strzhi = "��";
				if("true".equals(zhi)) {
					strzhi = "��";
				}else {
					strzhi = "��";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"�������޸�Ƥ��",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}
			else if("caiysz1".equals(mingc)) {
				String strzhi = "��";
				if("true".equals(zhi)) {
					strzhi = "��";
				}else {
					strzhi = "��";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"����Ϊ�������",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("caiysz2".equals(mingc)) {
				String strzhi = "��";
				if("true".equals(zhi)) {
					strzhi = "��";
				}else {
					strzhi = "��";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"�������κ�Ϊ�������",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("hcmrysl".equals(mingc)){
				con.getUpdate(UpdateXitxxSql("zhi",zhi,"Ĭ��������","��",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("qcmrysl".equals(mingc)){
				con.getUpdate(UpdateXitxxSql("zhi",zhi,"Ĭ��������","����",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb3x".equals(mingc)) {
				String strzhi = "��";
				if("true".equals(zhi)) {
					strzhi = "��";
				}else {
					strzhi = "��";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"�����޸�Ĭ�϶����滻",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("qicjjdms".equals(mingc)){
				//String zd,String zhi,String mingc, String danw, String diancxxb_id
				con.getUpdate(UpdateXitxxSql("zhi",zhi,"�������ﵥģʽ",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb4x".equals(mingc)) {
				String strzhi = "��";
				if("true".equals(zhi)) {
					strzhi = "��";
				}else {
					strzhi = "��";
				}
				//String zd,String zhi,String mingc, String danw, String diancxxb_id
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"��¼ë���Զ�����","��·",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("mfpz".equals(mingc)){
				con.getUpdate(UpdateXitxxSql("zhi",zhi,"��¼Ĭ��Ƥ��","��·",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb5x".equals(mingc)) {
				String strzhi = "��";
				if("true".equals(zhi)) {
					strzhi = "��";
				}else {
					strzhi = "��";
				}
				//String zd,String zhi,String mingc, String danw, String diancxxb_id
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"�ᳵ����Ƿ����ɱ�",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("mrrq".equals(mingc)){
				con.getUpdate(UpdateXitxxSql("zhi",zhi,"���ݵ���Ĭ������",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cbcybh".equals(mingc)) {
    			String strzhi = "��";
    			if("true".equals(zhi)) {
    				strzhi = "��";
    			}else {
    				strzhi = "��";
    			}
    			con.getUpdate(UpdateXitxxSql("zhi",strzhi,"�Ƿ��Զ����ɲ���",null,String.valueOf(visit.getDiancxxb_id())));
    			continue;
			}else if("cbkskz".equals(mingc)) {
				String strzhi = "��";
				if("true".equals(zhi)) {
					strzhi = "��";
				} else {
					strzhi = "��";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"��ˮ�����Ƿ���ʾ",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cbshcxy".equals(mingc)) {
				String strzhi = "��";
				if("true".equals(zhi)) {
					strzhi = "��";
				} else {
					strzhi = "��";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"�պĴ��ձ���ú����Լ",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cbazlfl".equals(mingc)) {
				String strzhi = "��";
				if("true".equals(zhi)) {
					strzhi = "��";
				} else {
					strzhi = "��";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"����������",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}
    	}
    	con.Close();
	}
}
