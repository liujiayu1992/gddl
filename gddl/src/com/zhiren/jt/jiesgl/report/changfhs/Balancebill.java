package com.zhiren.jt.jiesgl.report.changfhs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
/*
 * ���ߣ���ɳɳ
 * ʱ�䣺2012-4-20
 * ��������Ȫ�糧�ص�ú��Ʊ����ʱ��Ҫ�˷ѿ��˽��㵥�����Ӷ��Ƿ���ʾ�˷ѿ��˽��㵥���жϼ���ʾ����
 */
/*
 * ���ߣ����
 * ʱ�䣺2014-03-07
 * ����������ҳ���ʼ��BUG
 */
public class Balancebill extends BasePage {
	
	private String OraDate(Date _date) {
		if (_date == null) {
			return "to_date('" + DateUtil.Formatdate("yyyy-MM-dd", new Date())
					+ "','yyyy-mm-dd')";
		}
		return "to_date('" + DateUtil.Formatdate("yyyy-MM-dd", _date)
				+ "','yyyy-mm-dd')";
	}
	
//	�����ʽ��
	public String getBianm(String bm){
		if(bm.equals(null)){
			bm = "";
		}
		return bm;
	}
	
//	�Ƿ����ӽ��㵥
	public boolean isZijsd() {
		JDBCcon con = new JDBCcon();
		boolean Flag=false;
		String talbes=((Visit) getPage().getVisit()).getString3();
		String talbe1 = talbes.substring(0,talbes.indexOf(","));
		String talbe2 = talbes.substring(talbes.indexOf(",")+1);
		String bianm = "";
		String sql = "  select js.bianm from "+talbe1+" js where js.fuid in (select id from "+talbe1+" where bianm = '"+this.getJiesbhSel1()+"')\n" +
					 "  union\n" + 
					 "  select jy.bianm from "+talbe2+" jy where jy.fuid in (select id from "+talbe2+" where bianm = '"+this.getJiesbhSel1()+"')\n";
		ResultSetList rs = con.getResultSetList(sql);
		while(rs.next()){
			bianm += rs.getString("bianm")+",";
		}
		if(bianm!=null&&!bianm.equals("")){
			bianm = bianm.substring(0,bianm.length()-1);
			Flag = true;
		}
		
		((Visit) getPage().getVisit()).setString5(bianm);
		rs.close();
		con.Close();
		
		return Flag;
	}
//	�ж��Ƿ���ú�����
	public boolean isMeikjs(){
		
		JDBCcon con = new JDBCcon();
		boolean Flag = false;
		try{
			
			String talbes=((Visit) getPage().getVisit()).getString3();
			String talbe1 = talbes.substring(0,talbes.indexOf(","));
			
			String sql= " select * from "+talbe1+" where bianm='"+this.getJiesbhSel1()+"'";
			ResultSet rs = con.getResultSet(sql);
			if(rs.next()){
				
				Flag = true;
			}
			rs.close();
		}catch(SQLException e){
			
			e.printStackTrace();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		
		return Flag;
	}
	
//	�ж��Ƿ�����Ʊ����
	public boolean isLiangpjs(){
		
		JDBCcon con = new JDBCcon();
		boolean Flag = false;
		try{
			
			String talbes=((Visit) getPage().getVisit()).getString3();
			String talbe1 = talbes.substring(0,talbes.indexOf(","));
			
			String sql= " select jieslx from "+talbe1+" where bianm='"+this.getJiesbhSel1()+"'";
			ResultSet rs = con.getResultSet(sql);
			if(rs.next()){
				if(rs.getString("jieslx").equals("1")){//������Ʊ����ʱ(jiesb.jieslx=1)
				   Flag = true;
				}
			}
			rs.close();
		}catch(SQLException e){
			
			e.printStackTrace();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		
		return Flag;
	}
	
//	���ϵ���ʾ����
	public boolean isShouldsh() {
		
		if(MainGlobal.getXitxx_item("����", "�Ƿ���ʾ���ϵ�", 
				String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()),
				"��").equals("��")) {
			
			this.setShouldsh(true);
		}
		return ((Visit) getPage().getVisit()).getboolean6();
	}
	
	public void setShouldsh(boolean value) {
		
		((Visit) getPage().getVisit()).setboolean6(value);
	}
	
//	�˷ѿ��˽��㵥��ʾ����
	public boolean isYunfKaohdxs() {
		
		if(MainGlobal.getXitxx_item("����", "�Ƿ���ʾ�˷ѿ��˽��㵥", 
				String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()),
				"��").equals("��")) {
			
			this.setYunfKaohdxs(true);
		}
		return ((Visit) getPage().getVisit()).getboolean8();
	}
	
    public void setYunfKaohdxs(boolean value) {
		
		((Visit) getPage().getVisit()).setboolean8(value);
	}

	public boolean isEditable() {
		return ((Visit) getPage().getVisit()).getboolean4();
	}

	public void setEditable(boolean editable) {
		((Visit) getPage().getVisit()).setboolean4(editable);
	}

	public boolean isEditable2() {
		return ((Visit) getPage().getVisit()).getboolean5();
	}

	public void setEditable2(boolean editable) {
		((Visit) getPage().getVisit()).setboolean5(editable);
	}

	// ��ʽ��
	public String format(double dblValue, String strFormat) {
		DecimalFormat df = new DecimalFormat(strFormat);
		return formatq(df.format(dblValue));

	}

	public String formatq(String strValue) {// ��ǧλ�ָ���
		String strtmp = "", xiaostmp = "", tmp = "";
		int i = 3;
		if (strValue.lastIndexOf(".") == -1) {

			strtmp = strValue;
			if (strValue.equals("")) {

				xiaostmp = "";
			} else {

				xiaostmp = ".00";
			}

		} else {

			strtmp = strValue.substring(0, strValue.lastIndexOf("."));

			if (strValue.substring(strValue.lastIndexOf(".")).length() == 2) {

				xiaostmp = strValue.substring(strValue.lastIndexOf(".")) + "0";
			} else {

				xiaostmp = strValue.substring(strValue.lastIndexOf("."));
			}

		}
		tmp = strtmp;

		while (i < tmp.length()) {
			strtmp = strtmp.substring(0, strtmp.length() - (i + (i - 3) / 3))
					+ ","
					+ strtmp.substring(strtmp.length() - (i + (i - 3) / 3),
							strtmp.length());
			i = i + 3;
		}

		return strtmp + xiaostmp;
	}

	// ****************�ж�ҳ���Ƿ��ǵ�һ�ε���**************//
	public int getZhuangt() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

	public void setZhuangt(int _value) {
		((Visit) getPage().getVisit()).setInt1(_value);
	}

	// *****************************************�������ÿ�ʼ******************************************//
	// ****************��������*******************//
	// ��ʼ����
	private Date _BeginriqValue = getMonthFirstday(new Date());

	private boolean _BeginriqChange = false;

	public Date getBeginriqDate() {
		if (_BeginriqValue == null) {
			_BeginriqValue = getMonthFirstday(new Date());
		}
		return _BeginriqValue;
	}

	public void setBeginriqDate(Date _value) {
		if (FormatDate(_BeginriqValue).equals(FormatDate(_value))) {
			_BeginriqChange = false;
		} else {

			_BeginriqValue = _value;
			_BeginriqChange = true;
		}
	}

	// ��������
	private Date _EndriqValue = new Date();

	private boolean _EndriqChange = false;

	public Date getEndriqDate() {
		if (_EndriqValue == null) {
			_EndriqValue = new Date();
		}
		return _EndriqValue;
	}

	public void setEndriqDate(Date _value) {
		if (FormatDate(_EndriqValue).equals(FormatDate(_value))) {
			_EndriqChange = false;
		} else {
			_EndriqValue = _value;
			_EndriqChange = true;
		}
	}
	
	boolean _GongysChange=false;

	// ��˾����
	public IDropDownBean getGongsmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getIGongsmcModels()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setGongsmcValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean2() != null) {

			((Visit) getPage().getVisit()).setDropDownBean2(Value);
			getIDiancmcModels();
		}
	}

	public void setIGongsmcModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getIGongsmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getIGongsmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getIGongsmcModels() {

		String sql = "";
		sql = "select id,mingc from diancxxb where jib=2 order by mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, "ȫ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	// �糧����
	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getIDiancmcModels()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setDiancmcValue(IDropDownBean value) {
		if (value == null) {
			((Visit) getPage().getVisit()).setDropDownBean3(value);
		} else {
			if (((Visit) getPage().getVisit()).getDropDownBean3().getId() != value
					.getId()) {

				((Visit) getPage().getVisit()).setboolean2(true);
				((Visit) getPage().getVisit()).setDropDownBean3(value);
			} else {
				((Visit) getPage().getVisit()).setboolean2(false);
			}
		}
	}

	public void setIDiancmcModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

			getIDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public IPropertySelectionModel getIDiancmcModels() {

		String sql = "";

		if (((Visit) getPage().getVisit()).isGSUser()) {
			// �ֹ�˾
			sql = "select d.id,d.mingc from diancxxb d where fuid="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ " order by d.mingc";
		} else if(((Visit) getPage().getVisit()).isJTUser()){
			// ����
			if (getGongsmcValue().getId() > -1) {

				sql = "select d.id,d.mingc from diancxxb d where fuid="
						+ getGongsmcValue().getId() + " order by d.mingc";
			} else {

				sql = "select d.id,d.mingc from diancxxb d where jib=3 order by d.mingc";
			}
		}else{
//			�糧
			if(((Visit) getPage().getVisit()).isFencb()){
				
//				�ֳ���
				sql="select id,mingc from diancxxb where fuid="
					+((Visit) getPage().getVisit()).getDiancxxb_id()+" order by mingc";
			}else{
				
				sql="select id,mingc from diancxxb where id="+((Visit) getPage().getVisit()).getDiancxxb_id()+"";
			}
		}

		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql, "ȫ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	// ��Ӧ��
	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getIGongysModels()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setGongysValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean4() != value) {

			((Visit) getPage().getVisit()).setboolean2(true);
			((Visit) getPage().getVisit()).setDropDownBean4(value);
		}
	}

	public void setIGongysModel(IPropertySelectionModel value) {
		_GongysChange=true;

		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getIGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getIGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public IPropertySelectionModel getIGongysModels() {

		String sql = "";
		String diancxxb_id="";
		
		if (((Visit) getPage().getVisit()).isDCUser()) {
			// �糧
			
			if(((Visit) getPage().getVisit()).isFencb()){
				
				if(this.getDiancmcValue().getId()>-1){
					
					diancxxb_id=" and diancxxb_id="+String.valueOf(this.getDiancmcValue().getId());
				}else{
					
					diancxxb_id=" and diancxxb.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"";
				}
			}else{
				
				diancxxb_id=" and diancxxb_id="+String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
			}
			
			sql = " select distinct gongysb.id,gongysb.mingc from gongysb,diancxxb,		\n"
				+ " ((select diancxxb_id,gongysb_id from jiesb where jiesrq>="+this.OraDate(this.getBeginriqDate())+"	\n" 
				+ " 	and jiesrq<="+this.OraDate(this.getEndriqDate())+")		\n" 
				+ " union	\n" 
				+ " (select diancxxb_id,gongysb_id from jiesyfb where jiesrq>="+this.OraDate(this.getBeginriqDate())+"	\n" 
				+ " 	and jiesrq<="+this.OraDate(this.getEndriqDate())+")) a 	\n"        
				+ " where gongysb.id=a.gongysb_id		\n" 
				+ " 	and gongysb.leix=1 and gongysb.fuid<>0	and diancxxb.id=a.diancxxb_id	\n"
				+ " 	"+diancxxb_id+"		\n"
				+ " order by mingc ";
		} else if (((Visit) getPage().getVisit()).isGSUser()) {
			// ��˾
			
			if(this.getDiancmcValue().getId()>-1){
				
				diancxxb_id=String.valueOf(this.getDiancmcValue().getId());
			}else{
				
				diancxxb_id=String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
			}
			
			sql = "select distinct gongysb_id,gongysmc from diancjsmkb,gongysb where diancjsmkb.gongysb_id=gongysb.id and gongysb.leix=1 and gongysb.fuid<>0 and diancxxb_id in (select id from diancxxb where fuid="
					+ diancxxb_id
					+ ") order by gongysmc";
			
		} else if (((Visit) getPage().getVisit()).isJTUser()) {
			// ����
			
			if(this.getDiancmcValue().getId()>-1){
				
				diancxxb_id=" and diancxxb_id="+String.valueOf(this.getDiancmcValue().getId());
			}else{
				
				diancxxb_id="";
			}
			
			sql = "select distinct gongysb_id,gongysmc from diancjsmkb,gongysb where diancjsmkb.gongysb_id=gongysb.id and gongysb.leix=1 and gongysb.fuid<>0 order by gongysmc";
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql, "ȫ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	// ��ӡ״̬
	private static IPropertySelectionModel _PrintModel;

	public IPropertySelectionModel getPrintModel() {
		if (_PrintModel == null) {
			getPrintModels();
		}
		return _PrintModel;
	}

	private IDropDownBean _PrintValue;

	public IDropDownBean getPrintValue() {
		if (_PrintValue == null) {
			setPrintValue((IDropDownBean) getPrintModel().getOption(0));
		}
		return _PrintValue;
	}

	private boolean _PrintChange = false;

	public void setPrintValue(IDropDownBean Value) {
		if (_PrintValue == Value) {
			_PrintChange = false;
		} else {
			_PrintValue = Value;
			_PrintChange = true;
		}
	}

	public IPropertySelectionModel getPrintModels() {
		List listPrint = new ArrayList();

		listPrint.add(new IDropDownBean(0, "����ӡ"));
		listPrint.add(new IDropDownBean(1, "�Ѵ�ӡ"));
		listPrint.add(new IDropDownBean(2, "ȫ��"));

		_PrintModel = new IDropDownModel(listPrint);
		return _PrintModel;
	}

	public void setPrintModel(IPropertySelectionModel _value) {
		_PrintModel = _value;
	}

	// ******************�������*****************//
	public IDropDownBean getBianhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			if (getIBianhModel().getOptionCount() > 1) {
				((Visit) getPage().getVisit())
						.setDropDownBean5((IDropDownBean) getIBianhModel()
								.getOption(0));
			} else {
				((Visit) getPage().getVisit())
						.setDropDownBean5((IDropDownBean) getIBianhModel()
								.getOption(0));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setBianhValue(IDropDownBean Value) {
		
		if (((Visit) getPage().getVisit()).getDropDownBean5()!= Value) {
			
			((Visit) getPage().getVisit()).setDropDownBean5(Value);
			((Visit) getPage().getVisit()).setboolean3(true);
		}
	}

	public void setIBianhModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getIBianhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getIBianhModels(((Visit) getPage().getVisit()).getString3());
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public IPropertySelectionModel getIBianhModels(String tables) {

		StringBuffer w_sql = new StringBuffer();
		w_sql.append(" where ");
		String beginriq = "";
		String endriq = "";
		String gongsid = "";
		String diancid = "";
		String diqumc = "";
		String printState = "";
        String js_gongysb_id="";
        String yf_gongysb_id="";
		
		beginriq = " jiesrq>=" + OraDate(_BeginriqValue);// ��ʼ����
		endriq = " and jiesrq<=" + OraDate(_EndriqValue);// ��������
		w_sql.append(beginriq);
		w_sql.append(endriq);

		if (((Visit) getPage().getVisit()).getRenyjb() == 1) {

			if (getGongsmcValue() != null && getGongsmcValue().getId() != -1) {// ��˾����

				gongsid = " and diancxxb_id in (select id from diancxxb where fuid="
						+ getGongsmcValue().getId() + ")";
				w_sql.append(gongsid);
			}

			if (getDiancmcValue() != null && getDiancmcValue().getId() != -1) {// �糧����

				diancid = " and diancxxb_id=" + getDiancmcValue().getId();
				w_sql.append(diancid);
			}

		} else if (((Visit) getPage().getVisit()).getRenyjb() == 2) {

			if (getDiancmcValue() != null && getDiancmcValue().getId() != -1) {// �糧����

				diancid = " and diancxxb_id=" + getDiancmcValue().getId();
				w_sql.append(diancid);
			} else {

				diancid = " and diancxxb_id in (select id from diancxxb where fuid="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id() + ")";
				w_sql.append(diancid);
			}

		} else if (((Visit) getPage().getVisit()).getRenyjb() == 3) {
			
			if(((Visit) getPage().getVisit()).isFencb()){
				
//				���ڵ糧Ĭ��ֵ�ǡ�ȫ�����������getDiancmcValue().getId()Ϊ-1��ȥ��������
				if(getDiancmcValue().getId()!=-1){
					
					diancid = " and diancxxb_id="
						+ this.getDiancmcValue().getId();
				}
				
			}else{
				
				diancid = " and diancxxb_id="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id();
			}
			
			w_sql.append(diancid);
		}

		if (getGongysValue() != null && getGongysValue().getId() != -1) {// ��Ӧ��

			diqumc = " and gongysb_id='" + getGongysValue().getId() + "' ";
			js_gongysb_id=" and js.gongysb_id='" + getGongysValue().getId() + "' ";
			yf_gongysb_id=" and yf.gongysb_id='" + getGongysValue().getId() + "' ";
			w_sql.append(diqumc);
		}

		String sql = "";

		sql = " select id,bianm from ("
				+ " select js.id,js.bianm,js.diancxxb_id,gongysb_id from "+tables.substring(0,tables.lastIndexOf(","))+" js"
				+ w_sql.toString()
				+ " and js.fuid = 0 "+js_gongysb_id
				+ printState
				+ " union "
				+ " select yf.id,yf.bianm,yf.diancxxb_id,gongysb_id from "+tables.substring(tables.lastIndexOf(",")+1)+" yf "
				+ w_sql.toString() + " and yf.jieslx<>1 and yf.fuid = 0" +yf_gongysb_id+ " ) order by bianm";

		setIBianhModel(new IDropDownModel(sql, "��ѡ��"));
		setIBianhModel1(new IDropDownModel(sql, "��ѡ��"));
		sql_jsdNfy=sql;
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	// *****************************************�������ý���******************************************//
	String sql_jsdNfy="";//��ǽ��㵥����ҳ
	private List getJiesbh(){
		JDBCcon con = new JDBCcon();
		List list=new ArrayList();
		ResultSet rs=con.getResultSet(sql_jsdNfy);
		try{
		  while(rs.next()){
			  list.add(rs.getString("bianm"));
		  }
		  rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		  con.Close();
		}
		return list;
	}
	
	public boolean BianhChange1 = false;

	public IDropDownBean getBianhValue1() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			if (getIBianhModel1().getOptionCount() > 1) {
				((Visit) getPage().getVisit())
						.setDropDownBean1((IDropDownBean) getIBianhModel1()
								.getOption(0));
			} else {
				((Visit) getPage().getVisit())
						.setDropDownBean1((IDropDownBean) getIBianhModel1()
								.getOption(0));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setBianhValue1(IDropDownBean Value) {
		if (Value == null) {
			((Visit) getPage().getVisit()).setDropDownBean1(Value);
		} else {
			if (((Visit) getPage().getVisit()).getDropDownBean1().getId() == Value
					.getId()) {
				BianhChange1 = false;
			} else {
				((Visit) getPage().getVisit()).setDropDownBean1(Value);
				BianhChange1 = true;
			}
		}
	}

	// private IPropertySelectionModel _IBianhModel;

	public void setIBianhModel1(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getIBianhModel1() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getIBianhModels(((Visit) getPage().getVisit()).getString3());
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
	
//	����鿴
	public IDropDownBean getbaobmcSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean6((IDropDownBean) getbaobmcSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setbaobmcSelectValue(IDropDownBean Value) {
		if (Value != null && ((Visit) getPage().getVisit()).getDropDownBean6() != null) {
			if (Value.getId() != ((Visit) getPage().getVisit())
					.getDropDownBean6().getId()) {
				((Visit) getPage().getVisit()).setboolean7(true);
			} else {
				((Visit) getPage().getVisit()).setboolean7(false);
			}
			((Visit) getPage().getVisit()).setDropDownBean6(Value);

		}
	}

	public void setbaobmcSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public IPropertySelectionModel getbaobmcSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getbaobmcSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void getbaobmcSelectModels() {
		
		
		List baob=new ArrayList();
		
		if(isMeikjs()){
//			ú�����
			baob.add(new IDropDownBean(-1,"��ѡ��"));
			baob.add(new IDropDownBean(0,"������ϸ"));
			baob.add(new IDropDownBean(1,"���ⵥ"));
			if(MainGlobal.getXitxx_item("����",	"��ʤ������ϸ��ʽ", "0", "��").equals("��")){
				
			}else{
				baob.add(new IDropDownBean(2,"���ֿ����ܸ���"));
			}
			if(isShouldsh()){
				baob.add(new IDropDownBean(3,"���ϵ�"));
			}
			if(isZijsd()){
				baob.add(new IDropDownBean(4,"�ӽ��㵥"));
			}
			baob.add(new IDropDownBean(5,"��������ϸ��"));
			if(isLiangpjs()){
				if(isYunfKaohdxs()){
					baob.add(new IDropDownBean(6,"�˷ѽ��㸽��"));	
				}
			}
		}else{
//			�˷ѽ���
			baob.add(new IDropDownBean(-1,"��ѡ��"));
			baob.add(new IDropDownBean(1,"���ⵥ"));
			if(isZijsd()){
				baob.add(new IDropDownBean(4,"�ӽ��㵥"));
			}
			baob.add(new IDropDownBean(5,"��������ϸ��"));
		}
		
		((Visit) getPage().getVisit())
				.setProSelectionModel6(new IDropDownModel(baob));
	}

	// ***************������Ϣ��******************//
	private String _msg;

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	// ******************��ť����****************//
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _QuedChick = false;

	public void QuedButton(IRequestCycle cycle) {
		_QuedChick = true;
	}

	private boolean _PrintChick = false;

	public void PrintButton(IRequestCycle cycle) {
		_PrintChick = true;
	}
	
////	������ѯ
//	private boolean _YansmxChick = false;
//
//	public void YansmxButton(IRequestCycle cycle) {
//		
//		_YansmxChick = true;
//	}
	
////	���ϵ���ѯ
//	private boolean _ShouldChick = false;
//
//	public void ShouldButton(IRequestCycle cycle) {
//		
//		_ShouldChick = true;
//	}


	public void submit(IRequestCycle cycle) {

		if (_RefurbishChick) {
			_RefurbishChick = false;
			chaxunzt1 = 1;// ��ѯ״̬
			zhuangt = 2;
			// setBianhValue(null);
			// setIBianhModel(null);
			// getIBianhModels();
			getSelectData();
		}
		if (_QuedChick) {
			_QuedChick = false;
			chaxunzt1 = 1;// ��ѯ״̬
			zhuangt = 2;
			Refurbish();
			
		}
		if (_PrintChick) {
			_PrintChick = false;
			PrintState();
		}
		this.setWindowScript("");
//		if(_YansmxChick){
//			_YansmxChick = false;
//			Yansmx();
//		}
		
//		if(_ShouldChick){
//			_ShouldChick = false;
////			��ӡ���ϵ�
//			Print_Should();
//		} 
	}
	
	public String getType(String value){
//		�õ������ڵĽ�������
//		value: ��+;+�ֹ�˾id
		String Type = ",dianc";
		
		if(!value.equals("")){
			
			if(value.indexOf(";")==value.length()){
//				˵��û�зֹ�˾id��˵�����Ƿֹ�˾�ɹ�����
				if(value.indexOf("diancjsmkb")>-1){
					
					Type = ",changf";
				}else if(value.indexOf("kuangfjsmkb")>-1){
					
					Type = ",kuangf";
				}
			}else if(value.indexOf("kuangfjsmkb")>-1){
//				˵������һ��ҳ���Ѿ������ֹ�˾id��˵���ֹ�˾�ɹ�����
				Type = ",fengscg";
			}
		}
		
		return Type;
	}
	
	private void Yansmx(){
		
		if(!this.getJiesbhSel1().equals("")&&!this.getJiesbhSel1().equals("��ѡ��")){
			
			String str = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "var end = url.indexOf(';');"
				+ "url = url.substring(0,end);"
				+ "url = url + '?service=page/' + 'Yansmxreport&lx="
				+((Visit) getPage().getVisit()).getString3()+";"
				+getJiesbhSel1()
				+getType(((Visit) getPage().getVisit()).getString3()+";"
						+((Visit) getPage().getVisit()).getString6())+"';"
				+ " window.open(url,'Yansmx');";
			
			this.setZhuangt(2);
			zhuangt = 2;
			this.setWindowScript(str);
//			((Visit) getPage().getVisit()).setboolean3(true);
		}else{
			
			setMsg("��ѡ����㵥��ţ�");
		}
//		getSelectData();
	}
	
	
	
	private void Jufd(){
		
		if(!this.getJiesbhSel1().equals("")&&!this.getJiesbhSel1().equals("��ѡ��")){
			
			String str = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "var end = url.indexOf(';');"
				+ "url = url.substring(0,end);"
				+ "url = url + '?service=page/' + 'Kuidkkjfd&lx="
				+((Visit) getPage().getVisit()).getString3()+";"+getJiesbhSel1()+"';"
				+ " window.open(url,'Kuidkkjfd');";
			
			this.setZhuangt(2);
			zhuangt = 2;
			this.setWindowScript(str);
//			((Visit) getPage().getVisit()).setboolean3(true);
		}else{
			
			setMsg("��ѡ����㵥��ţ�");
		}
//		getSelectData();
	}
	
	private void Print_Should(){
//		���ϵ�
		if(!this.getJiesbhSel1().equals("")){
			
			String str = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "var end = url.indexOf(';');"
				+ "url = url.substring(0,end);"
				+ "url = url + '?service=page/' + 'Should&lx="+this.getJiesbhSel1()+"';"
				+ " window.open(url,'Should');";
			
			this.setZhuangt(2);
			zhuangt = 2;
			this.setWindowScript(str);
//			((Visit) getPage().getVisit()).setboolean3(true);
		}else{
			setMsg("��ѡ����㵥��ţ�");
		}
	}
	
	private void Guohddy(){
//		���ڷֹ�˾�����϶�û�г�Ƥ����ֻ֧�ֳ���ϵͳ
		JDBCcon con = new JDBCcon();
		String js_ID = "";
		long jieslx=0;	//��ʶ�ý������ڵĽ�������
		String table = "";		 //�ܱ�
		String table1 = "jiesb"; //ú��
		String table2 = "jiesyfb";	//�˷�
		table = ((Visit) getPage().getVisit()).getString3();
		
		if(!table.equals("")){
			
			table1 = table.substring(0,table.indexOf(","));
			table2 = table.substring(table.indexOf(",")+1);
		}
		
		String sql =
			"select * from (\n" +
			"select 1 as xuh,id,jieslx from "+table1+" j where j.bianm='"+getJiesbhSel1()+"'\n" + 
			"union\n" + 
			"select 2 as xuh,id,jieslx from "+table2+" jy where jy.bianm='"+getJiesbhSel1()+"'\n" + 
			")\n" + 
			"order by xuh";


		ResultSetList rs = con.getResultSetList(sql);
		
		while(rs.next()){
			
			if(rs.getInt("xuh")==1
				&&rs.getLong("jieslx")==Locale.liangpjs_feiylbb_id){
//				���
				
				js_ID = rs.getString("ID");
			}else if(rs.getLong("jieslx")!=Locale.liangpjs_feiylbb_id){
				
				js_ID = rs.getString("ID");
			}
			
			jieslx = rs.getLong("jieslx");
		}
		rs.close();
		con.Close();
		if(!js_ID.equals("")){
			String str = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "var end = url.indexOf(';');"
				+ "url = url.substring(0,end);"
				+ "url = url + '?service=page/' + 'Guohd&lx="
				+jieslx+","+js_ID+"';"
				+ " window.open(url,'Guohd');";
			
			this.setZhuangt(2);
			zhuangt = 2;
			this.setWindowScript(str);
		}else{
			setMsg("��ѡ����㵥��ţ�");
		}		
	}
	
	private void Danpcmxd(){
		if(!this.getJiesbhSel1().equals("")){
			String str = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "var end = url.indexOf(';');"
				+ "url = url.substring(0,end);"
				+ "url = url + '?service=page/' + 'Danpcmxd&lx="+((Visit) getPage().getVisit()).getString3()+";"+getJiesbhSel1()+"';"
				+ " window.open(url,'Danpcmxd');";
			
			this.setZhuangt(2);
			zhuangt = 2;
			this.setWindowScript(str);
//			((Visit) getPage().getVisit()).setboolean3(true);
		}else{
			setMsg("��ѡ����㵥��ţ�");
		}		
	}

	private void Zijsd(){
		if(!this.getJiesbhSel1().equals("")){
			String str = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "var end = url.indexOf(';');"
				+ "url = url.substring(0,end);"
				+ "url = url + '?service=page/' + 'Zijsd&lx="+((Visit) getPage().getVisit()).getString3()+"&bm="
				+ ((Visit) getPage().getVisit()).getString5() +"';"
				+ " window.open(url,'Zijsd');";
			
			this.setZhuangt(2);
			zhuangt = 2;
			this.setWindowScript(str);
//			((Visit) getPage().getVisit()).setboolean3(true);
		}else{
			setMsg("��ѡ����㵥��ţ�");
		}		
	}
	
	private void Yunfkhjsd(){
		if(!this.getJiesbhSel1().equals("")){
			String str = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "var end = url.indexOf(';');"
				+ "url = url.substring(0,end);"
				+ "url = url + '?service=page/' + 'Yunfkhjsd&lx="+getJiesbhSel1()+"';"
				+ " window.open(url,'Yunfkhjsd');";
			
			this.setZhuangt(2);
			zhuangt = 2;
			this.setWindowScript(str);
		}else{
			setMsg("��ѡ����㵥��ţ�");
		}		
	}
	
	private void PrintState() {
		JDBCcon con = new JDBCcon();

		String bianh[] = getWhere();
		String where = "";
		for (int i = 0; i < bianh.length; i++) {
			if (where.equals("")) {
				where = "'" + bianh[i] + "'";
			} else {
				where = where + ",'" + bianh[i] + "'";
			}
		}
		// String sql = "update diancjsmkb set gongsdyzt=1 where bianm in
		// ("+where+")";
		// con.getUpdate(sql);
		con.Close();
		setBianhValue(null);
		setIBianhModel(null);
		chaxunzt1 = 1;// ��ѯ״̬
		zhuangt = 2;
		getSelectData();
	}

	private void Refurbish() {
		// Ϊ "ˢ��" ��ť��Ӵ������
		/*isJiesdfy=isJiesdfy();
		if(!isJiesdfy){//��isJiesdfy=false�������㵥����ҳ��ʾ
			//if(_QuedChick){
			    getSelectData_Jiesdfy();
			//}else{
				//getSelectData();
			//}
		}else{
		getSelectData();
		}*/
		getSelectData();
	}

	// ******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		} else {
			visit.setboolean1(false);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())&&
			!visit.getActivePageName().toString().equals("Yansmxreport")&&
			!visit.getActivePageName().toString().equals("Guohd")&&
			!visit.getActivePageName().toString().equals("Kuidkkjfd")&&
			!visit.getActivePageName().toString().equals("Should")&&
			!visit.getActivePageName().toString().equals("Zijsd")&&
			!visit.getActivePageName().toString().equals("Danpcmxd")
			) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setInt1(2);// �ǵ�һ����ʾ
			visit.setString2("");	//���㵥���
//			visit.setString3("");	//���¸�ҳ�洫�ݵĲ���
			zhuangt = visit.getInt1();
			// System.out.println("��һ������zhuangt="+visit.getZhuangt());
			chaxunzt1 = 0;
			// *************�������ÿ�ʼ***************//
			_BeginriqValue = getMonthFirstday(new Date());
			_EndriqValue = new Date();
			visit.setboolean2(false);// ���� ;
			visit.setboolean3(false);// ���㵥
			visit.setboolean4(false);// ��˾��ʾ
			visit.setboolean5(false);// �糧��ʾ
			visit.setboolean6(false);// ���յ���ʾ
			visit.setboolean7(false);// �����ѯ
			visit.setboolean8(false);//�˷ѿ��˽��㵥��ʾ
			setDiancmcValue(null);
			setIDiancmcModel(null);

			((Visit) getPage().getVisit()).setString4("");	//WindowScript(ҳ����ʾ)
			((Visit) getPage().getVisit()).setString5("");	//�ӽ��㵥���
			visit.setString6("");							//�ֹ�˾�ɹ�����ķֹ�˾id
			
//			���ñ��������������ò��ü���Ľ����jiesb,jiesyfb,diancjsmkb,diancjsyfb��������
			if(cycle.getRequestContext().getParameters("lx") !=null) {
				
				((Visit) getPage().getVisit()).setString3("");
				((Visit) getPage().getVisit()).setString3(cycle.getRequestContext().getParameters("lx")[0]);
	        }
			
			if(cycle.getRequestContext().getParameters("jsdwid") !=null){
				
				visit.setString6(cycle.getRequestContext().getParameters("jsdwid")[0]);
			}
			
			// ����
			if (visit.getRenyjb() < 2) {

				visit.setboolean4(true);
				visit.setboolean5(true);
				setGongsmcValue(null);
				setIGongsmcModel(null);
				getIGongsmcModels();
			}
			// �ֹ�˾
			if (visit.getRenyjb() < 3) {
				visit.setboolean5(true);
				setDiancmcValue(null);
				setIDiancmcModel(null);
				getIDiancmcModels();
			}
			
//			�糧
			if(visit.isDCUser()&&visit.isFencb()){
//				���ǵ糧�û����Ƿֳ���
				visit.setboolean5(true);	//��ʾ�糧������
			}

			// ��Ӧ��
			setGongysValue(null);
			setIGongysModel(null);
			getIGongysModels();

			// ��ӡ״̬
			// ���
			setBianhValue(null);
			setIBianhModel(null);
			getIBianhModels(((Visit) getPage().getVisit()).getString3());

			setJiesbhSel(null);
			setJiesbhSel1(null);
			
//			�����ѯ
			setbaobmcSelectValue(null);
			setbaobmcSelectModel(null);
//			getbaobmcSelectModels();

		}
//		ÿ�μ��ظ�ҳ��ʱ�Զ����ҳ������Ϊ��ǰҳ
		visit.setActivePageName(getPageName().toString());
		
		if (visit.getboolean2()) {// ��˾���Ƹ���ʱ
			visit.setboolean2(false);
			setBianhValue(null);
			setIBianhModel(null);
			getIBianhModels(((Visit) getPage().getVisit()).getString3());
		}

		if (_BeginriqChange) {
			_BeginriqChange = false;
			setBianhValue(null);
			setIBianhModel(null);
			getIBianhModels(((Visit) getPage().getVisit()).getString3());
			getIGongysModels();
			
		}

		if (_EndriqChange) {
			_EndriqChange = false;
			setBianhValue(null);
			setIBianhModel(null);
			getIBianhModels(((Visit) getPage().getVisit()).getString3());
			getIGongysModels();
		}
		
		if(_GongysChange){
			getIGongysModels();
			setBianhValue(null);
			setIBianhModel(null);
			getIBianhModels(((Visit) getPage().getVisit()).getString3());
			
		}
		if (_PrintChange) {
			_PrintChange = false;
			setBianhValue(null);
			setIBianhModel(null);
		}
		// if(BianhChange){
		// BianhChange = false;
		// chaxunzt1 = 1;// ��ѯ״̬
		// zhuangt=2;
		// Refurbish();
		// }
		if (visit.getboolean3()) {
			visit.setboolean3(false);
			setbaobmcSelectValue(null);
			setbaobmcSelectModel(null);
//			getbaobmcSelectModels();
			chaxunzt1 = 1;// ��ѯ״̬
			zhuangt = 2;
			Refurbish();
			// }
		}
		if (zhuangt == 1) {// ��Ҫ��
			visit.setInt1(1);
		}
		if (zhuangt == 2) {// ��Ҫ��
			visit.setInt1(2);
		}

		if(visit.getboolean7()){
			visit.setboolean7(false);
			if(getbaobmcSelectValue().getId()==0){//������ϸ
				Yansmx();
			} else if(getbaobmcSelectValue().getId()==1){//���ⵥ
				Guohddy();
			} else if(getbaobmcSelectValue().getId()==2){//���ֿ����ܸ���
				Jufd();
			} else if(getbaobmcSelectValue().getId()==3){//���ϵ�
				Print_Should();
			} else if(getbaobmcSelectValue().getId()==4){//�ӽ��㵥
				Zijsd();
			} else if(getbaobmcSelectValue().getId()==5){//��������ϸ��
				Danpcmxd();
			} else if(getbaobmcSelectValue().getId()==6){//�˷ѿ��˽��㵥
				Yunfkhjsd();
			}
			chaxunzt1=2;
		}
		
//		zhuangt = 1;
		
		// *************�������ý���***************//
	}

	// *****************************������������*****************************//
	private int chaxunzt1 = 0;// ��ѯ״̬

	private int zhuangt = 1;

	public String getJiesbhSel() {

		return ((Visit) getPage().getVisit()).getString1();
	}
	
//	���ڵ���js_begin
	public String getWindowScript(){
		
		return ((Visit) getPage().getVisit()).getString4();
	}
	
	public void setWindowScript(String value){
		
		((Visit) getPage().getVisit()).setString4(value);
	}
//	���ڵ���js_end

	public void setJiesbhSel(String Jiesbhsel) {
		if (((Visit) getPage().getVisit()).getString1() != null) {
			if (((Visit) getPage().getVisit()).getString1().equals(Jiesbhsel)) {
				((Visit) getPage().getVisit()).setboolean3(false);
			} else {
				((Visit) getPage().getVisit()).setboolean3(true);
			}
		} else if (Jiesbhsel != null) {
			if (!Jiesbhsel.equals("")) {
				((Visit) getPage().getVisit()).setboolean3(true);
			}
		}
		((Visit) getPage().getVisit()).setString1(Jiesbhsel);
	}

	public String getJiesbhSel1() {

		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setJiesbhSel1(String Jiesbhsel) {
		if (((Visit) getPage().getVisit()).getString2() != null) {
			if (((Visit) getPage().getVisit()).getString2().equals(Jiesbhsel)) {
				((Visit) getPage().getVisit()).setboolean3(false);
			} else {
				((Visit) getPage().getVisit()).setboolean3(true);
			}
		} else if (Jiesbhsel != null) {
			if (!Jiesbhsel.equals("")) {
				((Visit) getPage().getVisit()).setboolean3(true);
			}
		}
		((Visit) getPage().getVisit()).setString2(Jiesbhsel);
	}

	public String[] getWhere() {// ��ѯ����
		// �������
		String bianh[];

		/*if (getJiesbhSel() != null && !getJiesbhSel().equals("")
				&& !getJiesbhSel().equals("��ѡ��")) {// ������
			bianh = getJiesbhSel().split(",");
		} else {
			bianh = null;
		}*/
		if (getJiesbhSel() != null && !getJiesbhSel().equals("")
				&& !getJiesbhSel().equals("��ѡ��")) {// ������
			bianh = getJiesbhSel().split(",");
		} else if(getJiesbhSel1() != null && !getJiesbhSel1().equals("")
				&& !getJiesbhSel1().equals("��ѡ��")){
			bianh = getJiesbhSel1().split(",");
		}else{
			bianh = null;
		}
		return bianh;
	}

//	private String nvlStr(String strValue) {
//		if (strValue == null) {
//			return "";
//		} else if (strValue.equals("null")) {
//			return "";
//		}
//
//		return strValue;
//	}
//	�ж��Ƿ��ҳ��ʾ���㵥
	boolean isJiesdfy=true;//���㵥��ҳ��ʾ���
	public boolean isJiesdfy(){
		JDBCcon con = new JDBCcon();
		String sql="select zhi from xitxxb where mingc='���㵥��ҳ��ʾ'";
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			if(rsl.getString("zhi").equals("��")){
				isJiesdfy=false;
			}
		}
		rsl.close();
		con.Close();
		return isJiesdfy;
	}
	
	public String getSelectData_Jiesdfy(){//
		Visit visit = (Visit) getPage().getVisit();
			if (chaxunzt1 == 1) {// ��ѯ״̬
				chaxunzt1 = 2;
				return "";
			} else if (chaxunzt1 == 2) {
			List lbh=getJiesbh();
			String bianh="";
		
			Changfjsd jsd = new Changfjsd();
			jsd.setVisit(visit);
			StringBuffer sb = new StringBuffer();
			sb.append("<center>");
			sb.append("<span id='reportpage").append(1).append("' >\n");
			for(int i=0;i<lbh.size();i++){
				bianh=lbh.get(i).toString();
					sb.append(jsd.getChangfjsd(bianh, i,visit.getString3()));
					//k+=jsd.getAllPages();
//					System.out.print(sb);
			}
			sb.append("</span>\n");
			sb.append("</center>");
			setAllPages(1);
			_CurrentPage=1;
			return sb.toString();
			} else {
				return "";
			}
	}

	public String getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		
		if (chaxunzt1 == 1) {// ��ѯ״̬
			chaxunzt1 = 2;
			return "";
		} else if (chaxunzt1 == 2) {
			String bianh[] = getWhere();
			if (bianh != null) {
				if (bianh.length == 0) {
					return "";
				} else {
					visit.setInt1(2);// �ǵ�һ����ʾ
					chaxunzt1 = 0;
					zhuangt = 1;
					int k=0;
					Changfjsd jsd = new Changfjsd();
					jsd.setVisit(visit);
					StringBuffer sb = new StringBuffer();
					if(!isJiesdfy){//���㵥����ҳ��ʾ
						sb.append("<center>");
						sb.append("<span id='reportpage").append(1).append("' >\n");
					}
					
					for (int p = 0; p < bianh.length; p++) {
						sb.append(jsd.getChangfjsd(bianh[p], p,visit.getString3()));
						k+=jsd.getAllPages();
					}
					if(!isJiesdfy){
					    sb.append("</span>\n");
					    sb.append("</center>");
					    setAllPages(1);
					}else{
					    setAllPages(k);
					}
					_CurrentPage=1;
							
					return sb.toString();
				}
			} else {
				return "";
			}
		} else {
			return "";
		}
		
	}

	// ***************************�����ʼ����***************************//
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

	private String RT_HET = "jies";

	private String mstrReportName = "jies";

	public String getPrintTable() {
		boolean isJiesdfy=isJiesdfy();
		
		if(!isJiesdfy){//��isJiesdfy=false�������㵥����ҳ��ʾ
			if((getJiesbhSel() != null && !getJiesbhSel().equals("")
					&& !getJiesbhSel().equals("��ѡ��"))||(getJiesbhSel1() != null 
					&& !getJiesbhSel1().equals("")
					&& !getJiesbhSel1().equals("��ѡ��"))){
				return getSelectData();
			}else{
				return getSelectData_Jiesdfy();
			}
		}else{
		   if (mstrReportName.equals(RT_HET)) {

			   return getSelectData();
		   } else {
			    return "�޴˱���";
		   }
		}
	}

	// ******************************����*******************************//

	// public String getcontext() {
	// return "var context='http://"
	// + this.getRequestCycle().getRequestContext().getServerName()
	// + ":"
	// + this.getRequestCycle().getRequestContext().getServerPort()
	// + ((Visit) getPage().getVisit()).getContextPath() + "';";
	// }

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			// _pageLink = "window.location.target = '_blank';"+_pageLink;
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page����
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.opener=null;self.close();window.parent.close();open('"
					+ getpageLinks() + "','');";
		} else {
			return "";
		}
	}

	public String getTianzdw(String jiesdbh) {
		String Tianzdw = "";
		JDBCcon con = new JDBCcon();
		try {
			String sql = "select quanc from diancxxb where id=(select diancxxb_id from diancjsmkb where bianm='"
					+ jiesdbh + "')";
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				Tianzdw = rs.getString("quanc");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return Tianzdw;
	}

	private String FormatDate(Date _date) {
		if (_date == null) {
			// return MainGlobal.Formatdate("yyyy�� MM�� dd��", new Date());
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}

	public Date getYesterday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	public Date getMonthFirstday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}

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
}