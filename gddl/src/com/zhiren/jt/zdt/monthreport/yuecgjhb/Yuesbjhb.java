package com.zhiren.jt.zdt.monthreport.yuecgjhb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.Field;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yuesbjhb extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
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

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		int flag=visit.getExtGrid1().Save(getChange(), visit);
		if(flag!=-1){
			setMsg(ErrorMessage.SaveSuccessMessage);
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
	public void submit(IRequestCycle cycle) {
		if (_Refreshclick) {
			_Refreshclick = false;
			getSelectData(null);
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData(null);
		}
		if(_CopyButton){
			_CopyButton=false;
			CoypLastYueData();
		}
	}

	public void CoypLastYueData(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		//����������ݺ��·�������
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
		
		String StrMonth1="";
		if(intMonth<10){
			
			StrMonth1="0"+intMonth;
		}else{
			StrMonth1=""+intMonth;
		}
		//-----------------------------------
		String strriq = intyear+"-"+StrMonth1+"-01";
		
		
		//���·���1ʱ�ϸ�����ʾ12
		if(intMonth-1==0){
			intMonth=12;
			intyear=intyear-1;
		}else{
			intMonth=intMonth-1;
		}
		//���·���1��ʱ����ʾ01,
		String StrMonth="";
		if(intMonth<10){
			StrMonth="0"+intMonth;
		}else{
			StrMonth=""+intMonth;
		}
	
		String str_jizzt ="";
		
		if(getLeixDropDownValue()!=null){
			str_jizzt=" and y.jizzt="+getLeixDropDownValue().getId();
		}
		
		String str = "";
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and dc.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();	
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = " and (dc.fuid=  " + this.getTreeid()+ " or dc.shangjgsid=" + this.getTreeid() + ")";
			
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = "and dc.id = " + getTreeid() + "";
			
		}
	  
		String chaxun = 
			"select y.id as id,\n" +
			"       y.riq as riq,\n" + 
			"       dc.mingc as diancxxb_id,\n" + 
			"       g.mingc as gongysb_id,\n" + 
			"       j.mingc as jihkjb_id,\n" + 
			"       ch.mingc as faz_id,\n" + 
			"       che.mingc as daoz_id,\n" + 
			"       ys.mingc as yunsfsb_id,\n" +
			"       y.yuejhcgl as yuejhcgl,\n" + 
			"       y.rez as rez,\n" + 
			"       y.huiff as huiff,\n" +
			"       y.liuf as liuf,\n" +
			"       y.chebjg as chebjg,\n" + 
			"       y.yunf as yunf,\n" + 
			"       y.zaf as zaf,\n" + 
			"       y.daocj as daocj,\n" +
			"       y.biaomdj as biaomdj,\n" +
			"       y.jiakk as jiakk,\n" + 
			"       y.jihddsjysl as jihddsjysl\n ,y.jizzt \n" + 
			"  from yuesbjhb y, gongysb g, chezxxb ch, chezxxb che, vwdianc dc,jihkjb j,yunsfsb ys\n" + 
			" where y.gongysb_id = g.id(+)\n" + 
			"   and y.faz_id = ch.id(+)\n" + 
			"   and y.daoz_id = che.id(+)\n" + 
			"   and y.diancxxb_id = dc.id(+)\n" + 
			"   and y.jihkjb_id = j.id(+)\n" +
			"   and y.yunsfsb_id = ys.id(+)\n" +
			"   and to_char(y.riq,'yyyy-mm') ='" + intyear + "-" + StrMonth+ "'  \n"+
			"    "+str+"  "+ str_jizzt +
			"   order by y.id";
	  
//	System.out.println("����ͬ�ڵ�����:"+copyData);
	ResultSetList rslcopy = con.getResultSetList(chaxun);
	while(rslcopy.next()){
	
		double yuejhcgl=rslcopy.getDouble("yuejhcgl");
		double huiff=rslcopy.getDouble("huiff");
		double liuf=rslcopy.getDouble("liuf");
		double daocj=rslcopy.getDouble("daocj");
		double biaomdj=rslcopy.getDouble("biaomdj");
		double chebjg=rslcopy.getDouble("chebjg");
		double yunf=rslcopy.getDouble("yunf");
		double zaf=rslcopy.getDouble("zaf");
		String rez=rslcopy.getString("rez");
		String jiakk=rslcopy.getString("jiakk");
		String jihddsjysl=rslcopy.getString("jihddsjysl");
		String jizzt=rslcopy.getString("jizzt");
//		Date riq=rslcopy.getDate("riq");
		String _id = MainGlobal.getNewID(((Visit) getPage()
				.getVisit()).getDiancxxb_id());
		String sql="insert into yuesbjhb(id,riq,diancxxb_id,gongysb_id,jihkjb_id,faz_id,daoz_id,yuejhcgl,chebjg,yunf,zaf,rez,jiakk,jihddsjysl,huiff,liuf,daocj,biaomdj,yunsfsb_id,jizzt) values(" +
	//		_id+","+"to_date('"+strriq+"','yyyy-mm-dd')"
			_id+","+"to_date('" +strriq+ "','yyyy-mm-dd')"
			+",(select id from diancxxb where mingc='"+ rslcopy.getString("diancxxb_id")
			+"'),(select id from gongysb where mingc='"+rslcopy.getString("gongysb_id")
			+"'),(select id from jihkjb where mingc='"+rslcopy.getString("jihkjb_id")
			+"'),(select id from chezxxb where mingc='"+rslcopy.getString("faz_id")
			+"'),(select id from chezxxb where mingc='"+rslcopy.getString("daoz_id")
			+"'),"+yuejhcgl+","+chebjg+","+yunf+","+zaf+",'"+rez+"','"+jiakk
			+"','"+jihddsjysl+"',"+huiff+","+liuf+","+daocj+","+biaomdj+",getYunsfsbId('"+rslcopy.getString("yunsfsb_id")+"'),"+jizzt+")";
		con.getInsert(sql);
		con.commit();
	}
    getSelectData(null);
		con.Close();
	}
	
//	private String FormatDate(Date _date) {
//		if (_date == null) {
//			return "";
//		}
//		return DateUtil.Formatdate("yyyy-MM-dd", _date);
//	}

	public void getSelectData(ResultSetList rsl) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		//����������ݺ��·�������
		
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
		//���·���1��ʱ����ʾ01,
		String StrMonth="";
		if(intMonth<10){
			
			StrMonth="0"+intMonth;
		}else{
			StrMonth=""+intMonth;
		}
		//-----------------------------------
		
		String str_jizzt ="";
		
		if(getLeixDropDownValue()!=null){
			str_jizzt=" and y.jizzt="+getLeixDropDownValue().getId();
		}
		
		String str = "";
		
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and dc.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
//			str = "and (d.id = " + getTreeid() + " or d.fuid = " + getTreeid()
//			+ ")";
			str = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = "and dc.id = " + getTreeid() + "";
			
		}
		if(rsl==null){
			String chaxun = 
				"select y.id as id,\n" +
				"       y.riq as riq,\n" + 
				"       dc.mingc as diancxxb_id,\n" + 
				"       g.mingc as gongysb_id,\n" + 
				"       j.mingc as jihkjb_id,\n" + 
				"       ch.mingc as faz_id,\n" + 
				"       che.mingc as daoz_id,\n" + 
				"       ys.mingc as yunsfsb_id,\n" +
				"       y.yuejhcgl as yuejhcgl,\n" + 
				"       y.rez as rez,\n" + 
				"       y.huiff as huiff,\n" +
				"       y.liuf as liuf,\n" +
				"       y.chebjg as chebjg,\n" + 
				"       y.yunf as yunf,\n" + 
				"       y.zaf as zaf,\n" + 
				"       y.daocj as daocj,\n" +
				"       y.biaomdj as biaomdj,\n" +
				"       y.jiakk as jiakk,\n" + 
				"       y.jihddsjysl as jihddsjysl\n ,y.jizzt \n" + 
				"  from yuesbjhb y, gongysb g, chezxxb ch, chezxxb che, diancxxb dc,jihkjb j,yunsfsb ys\n" + 
				" where y.gongysb_id = g.id(+)\n" + 
				"   and y.faz_id = ch.id(+)\n" + 
				"   and y.daoz_id = che.id(+)\n" + 
				"   and y.diancxxb_id = dc.id(+)\n" + 
				"   and y.jihkjb_id = j.id(+)\n" +
				"   and y.yunsfsb_id = ys.id(+)\n" +
				"   and to_char(y.riq,'yyyy-mm') ='" + intyear + "-" + StrMonth+ "'  \n"+
				"    "+str+"  "+ str_jizzt +
				"   order by y.id";
		//System.out.println(chaxun);	
		rsl = con.getResultSetList(chaxun);
	}
	boolean showBtn = false;
	if(rsl.next()){
		rsl.beforefirst();
		showBtn = false;
	}else{
		showBtn = true;
	}
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("yuesbjhb");
   	
	egu.getColumn("riq").setCenterHeader("����");
	egu.getColumn("riq").setHidden(true);
	egu.getColumn("riq").setEditor(null);
	egu.getColumn("diancxxb_id").setCenterHeader("�糧����");
	egu.getColumn("gongysb_id").setCenterHeader("�������");
	egu.getColumn("jihkjb_id").setCenterHeader("�ƻ��ھ�");
	egu.getColumn("faz_id").setCenterHeader("��վ");
	egu.getColumn("daoz_id").setCenterHeader("��վ");
	egu.getColumn("yunsfsb_id").setCenterHeader("���䷽ʽ");
	egu.getColumn("yuejhcgl").setCenterHeader("�¼ƻ�<br>�ɹ���(��)");
	egu.getColumn("rez").setCenterHeader("��ֵ<br>(MJ/Kg)");
	egu.getColumn("huiff").setCenterHeader("�ӷ���%");
	egu.getColumn("liuf").setCenterHeader("���%");
	egu.getColumn("chebjg").setCenterHeader("����۸�<br>(Ԫ/��)");
	egu.getColumn("yunf").setCenterHeader("�˷�<br>(Ԫ/��)");
	egu.getColumn("zaf").setCenterHeader("�ӷ�<br>(Ԫ/��)");
	egu.getColumn("daocj").setCenterHeader("������<br>(Ԫ/��)");
	egu.getColumn("biaomdj").setCenterHeader("��ú����<br>(Ԫ/��)");
	egu.getColumn("jiakk").setCenterHeader("�ӿۿ�<br>(Ԫ/��)");
	egu.getColumn("jiakk").setHidden(true);
	egu.getColumn("jihddsjysl").setCenterHeader("�ƻ�����ʱ<br>��������");
	egu.getColumn("jihddsjysl").setHidden(true);
	
	egu.getColumn("jizzt").setHeader("����״̬");
	egu.getColumn("jizzt").setHidden(true);
	
	//���ò��ɱ༭����ɫ
//	egu.getColumn("daohldy").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
	
	
	//�趨�г�ʼ����
	egu.getColumn("riq").setWidth(80);
	egu.getColumn("gongysb_id").setWidth(120);
	egu.getColumn("diancxxb_id").setWidth(120);
	egu.getColumn("jihkjb_id").setWidth(80);
	egu.getColumn("faz_id").setWidth(80);
	egu.getColumn("daoz_id").setWidth(80);
	egu.getColumn("yunsfsb_id").setWidth(80);
	egu.getColumn("yuejhcgl").setWidth(80);
	egu.getColumn("yuejhcgl").setDefaultValue("0");
	egu.getColumn("rez").setWidth(80);
	egu.getColumn("rez").setDefaultValue("0");
	egu.getColumn("huiff").setWidth(80);
	egu.getColumn("huiff").setDefaultValue("0");
	egu.getColumn("liuf").setWidth(80);
	egu.getColumn("liuf").setDefaultValue("0");
	egu.getColumn("chebjg").setWidth(80);
	egu.getColumn("chebjg").setDefaultValue("0");
	egu.getColumn("yunf").setWidth(80);
	egu.getColumn("yunf").setDefaultValue("0");
	egu.getColumn("zaf").setWidth(80);
	egu.getColumn("zaf").setDefaultValue("0");
	egu.getColumn("daocj").setWidth(80);
	egu.getColumn("daocj").setDefaultValue("0");
	egu.getColumn("biaomdj").setWidth(80);
	egu.getColumn("biaomdj").setEditor(null);
	egu.getColumn("biaomdj").setDefaultValue("0");
	egu.getColumn("jiakk").setWidth(80);
	egu.getColumn("jiakk").setDefaultValue("0");
	egu.getColumn("jihddsjysl").setWidth(120);
	egu.getColumn("jihddsjysl").setDefaultValue("0");
	
	egu.getColumn("jizzt").setWidth(60);
	egu.getColumn("jizzt").setDefaultValue(""+getLeixDropDownValue().getId());
	
	
	
	egu.setGridType(ExtGridUtil.Gridstyle_Edit);//�趨grid���Ա༭
	egu.addPaging(100);//���÷�ҳ
	egu.setWidth(1000);//����ҳ��Ŀ���,�������������ʱ��ʾ������
	
	
	
	//*****************************************����Ĭ��ֵ****************************
	//	�糧������
	int treejib2 = this.getDiancTreeJib();

	if (treejib2 == 1) {// ѡ����ʱˢ�³����еĵ糧
		//egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		ComboBox cb_diancxxb = new ComboBox();
		egu.getColumn("diancxxb_id").setEditor(cb_diancxxb);
		egu.getColumn("diancxxb_id").setDefaultValue("");
		cb_diancxxb.setEditable(true);
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
		egu.getColumn("diancxxb_id").setReturnId(true);
	} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
		//egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		ComboBox cb_diancxxb = new ComboBox();
		egu.getColumn("diancxxb_id").setEditor(cb_diancxxb);
		egu.getColumn("diancxxb_id").setDefaultValue("");
		cb_diancxxb.setEditable(true);
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id,mingc from diancxxb where (fuid="
									+ getTreeid() + " or shangjgsid ="+getTreeid()+") order by mingc"));
		egu.getColumn("diancxxb_id").setReturnId(true);
	} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
		//egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		ComboBox cb_diancxxb = new ComboBox();
		egu.getColumn("diancxxb_id").setEditor(cb_diancxxb);
		egu.getColumn("diancxxb_id").setDefaultValue("");
		cb_diancxxb.setEditable(true);
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc"));
		ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc");			
		String mingc="";
		if(r.next()){
			mingc=r.getString("mingc");
		}
		egu.getColumn("diancxxb_id").setDefaultValue(mingc);
		
	}		
	
	//���õ糧Ĭ�ϵ�վ
	egu.getColumn("daoz_id").setDefaultValue(this.getDiancDaoz());
	//�������ڵ�Ĭ��ֵ,
	egu.getColumn("riq").setDefaultValue(intyear+"-"+StrMonth+"-01");
	
	
	
	//*************************������*****************************************88
	//���ù�Ӧ�̵�������
	//egu.getColumn("gongysb_id").setEditor(new ComboBox());
	ComboBox cb_gongysb = new ComboBox();
	egu.getColumn("gongysb_id").setEditor(cb_gongysb);
	egu.getColumn("gongysb_id").setDefaultValue("");
	cb_gongysb.setEditable(true);
	/*
	 //��糧������Ĺ�Ӧ��
	String GongysSql = "select g.id,g.mingc from diancxxb d,gongysdcglb gd,gongysb  g\n"
				+ "where gd.diancxxb_id=d.id\n"
				+ "and gd.gongysb_id=g.id\n"
				+ "and d.id="+visit.getDiancxxb_id();
	*/
	String GongysSql="select id,mingc from gongysb  where fuid=0 or fuid =-1 order by mingc ";
	egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(GongysSql));
	//���üƻ��ھ���������
	egu.getColumn("jihkjb_id").setEditor(new ComboBox());
	String JihkjSql="select id,mingc from jihkjb order by mingc ";
	egu.getColumn("jihkjb_id").setComboEditor(egu.gridId, new IDropDownModel(JihkjSql));
	//���÷�վ������
	ComboBox cb_faz=new ComboBox();
	egu.getColumn("faz_id").setEditor(cb_faz);
	cb_faz.setEditable(true);
	String fazSql="select id ,mingc from chezxxb c where c.leib='��վ' order by c.mingc";
	egu.getColumn("faz_id").setComboEditor(egu.gridId, new IDropDownModel(fazSql));
	//���õ�վ������
	ComboBox cb_daoz=new ComboBox();
	egu.getColumn("daoz_id").setEditor(cb_daoz);
	cb_daoz.setEditable(true);

	String daozSql="select id,mingc from chezxxb order by mingc";
	egu.getColumn("daoz_id").setComboEditor(egu.gridId, new IDropDownModel(daozSql));
	
	//�������䷽ʽ������
	ComboBox cb_yunsfs=new ComboBox();
	egu.getColumn("yunsfsb_id").setEditor(cb_yunsfs);
	cb_daoz.setEditable(true);

	String yunsfsSql="select id,mingc from yunsfsb order by mingc";
	egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId, new IDropDownModel(yunsfsSql));
	
	
	
	//********************������************************************************
		egu.addTbarText("���:");
		ComboBox comb1=new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");//���Զ�ˢ�°�
		comb1.setLazyRender(true);//��̬��
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("-");// ���÷ָ���
		
		egu.addTbarText("�·�:");
		ComboBox comb2=new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");//���Զ�ˢ�°�
		comb2.setLazyRender(true);//��̬��
		comb2.setWidth(50);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");//���÷ָ���
		//������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree"); 
		
	
		//�趨�������������Զ�ˢ��
		//egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});YUEF.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// ���÷ָ���
		
		/*egu.addTbarText("����״̬:");
		ComboBox comb3=new ComboBox();
		comb3.setTransform("LeixDropDown");
		comb3.setId("LeixDropDown");//���Զ�ˢ�°�
		comb3.setLazyRender(true);//��̬��
		comb3.setWidth(80);
		egu.addToolbarItem(comb3.getScript());
		
		egu.addOtherScript("LeixDropDown.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// ���÷ָ���*/
		
//		ˢ�°�ť
		StringBuffer rsb2 = new StringBuffer();
		rsb2.append("function (){")
		.append(MainGlobal.getExtMessageBox("'����ˢ��'+Ext.getDom('NIANF').value+'��'+Ext.getDom('YUEF').value+'�µ�����,���Ժ�'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr2 = new GridButton("ˢ��",rsb2.toString());
		gbr2.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr2);
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		
		if(showBtn){
			//egu.addToolbarItem("{"+new GridButton("����ͬ�ڼƻ�","function(){document.getElementById('CopyButton').click();}").getScript()+"}");
			StringBuffer cpb = new StringBuffer();
			cpb.append("function(){").append(
					"document.getElementById('CopyButton').click();}");
				GridButton cpr = new GridButton("����ǰ������", cpb.toString());
				cpr.setIcon(SysConstant.Btn_Icon_Copy);
				egu.addTbarBtn(cpr);
			}
			//egu.addTbarText("->");
			//egu.addTbarText("<font color=\"#EE0000\">��λ:�֡�Ԫ/�֡�ǧ��/ǧ��</font>");
	

		
		
//		---------------ҳ��js�ļ��㿪ʼ------------------------------------------
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit',function(e){");
			//sb.append("e.record.set('DAOHLDY',parseFloat(e.record.get('SHIDLDY')==''?0:e.record.get('SHIDLDY'))/parseFloat(e.record.get('JIHLDY')==''?0:e.record.get('JIHLDY'))*100);");
			sb.append("e.record.set('DAOCJ',eval(e.record.get('CHEBJG')||0)+eval(e.record.get('YUNF')||0)+eval(e.record.get('ZAF')||0));");
			sb.append("if (e.record.get('REZ')!=0){ e.record.set('BIAOMDJ',eval(Round(eval(e.record.get('DAOCJ')||0)*29.271/eval(e.record.get('REZ')||0),2)||0));}");
		sb.append("});");
		
		
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		sb.append("if(e.record.get('DIANCXXB_ID')=='�ϼ�'){e.cancel=true;}");//���糧�е�ֵ��"�ϼ�"ʱ,��һ�в������༭
		sb.append("});");
		
		
       //�趨�ϼ��в�����
		sb.append("function gridDiv_save(record){if(record.get('diancxxb_id')=='�ϼ�') return 'continue';}");
		
		
		
		
		egu.addOtherScript(sb.toString());
		//---------------ҳ��js�������--------------------------
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
			// �ڴ����ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.setNianfValue(null);
			this.getNianfModels();
			this.setTreeid(null);
			this.setYuefValue(null);
			this.setMsg(null);
			this.getYuefModels();
			
//			getSelectData(null);
			
		}
//		if(nianfchanged){
//			nianfchanged = false;
//			getSelectData(null);
//		}
//		if(Changeyuef){
//			Changeyuef = false;
//			getSelectData(null);
//		}
		getSelectData(null);
		
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

	public IDropDownBean getYuefValue() {
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
		}finally{
			cn.Close();
		}

		return diancmc;

	}
	//�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
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
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

	}
	
	//�õ��糧��Ĭ�ϵ�վ
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
	
	
//	�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
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
	
//	����
	public IDropDownBean getLeixDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean) getLeixDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setLeixDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setLeixDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getLeixDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getLeixDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getLeixDropDownModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, "���ۻ���"));
		list.add(new IDropDownBean(1, "��������"));
		//list.add(new IDropDownBean(3, "���̱�"));
		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(list));
		return;
	}
}