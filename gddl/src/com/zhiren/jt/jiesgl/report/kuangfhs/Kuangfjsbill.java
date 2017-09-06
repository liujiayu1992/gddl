package com.zhiren.jt.jiesgl.report.kuangfhs;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.main.Visit;
import org.apache.tapestry.contrib.palette.SortMode;

public class Kuangfjsbill extends BasePage {
	
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
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
	
//	��ʽ��
	public String format(double dblValue,String strFormat){
		 DecimalFormat df = new DecimalFormat(strFormat);
		 return formatq(df.format(dblValue));
		 
	}
	
	public String formatq(String strValue){//��ǧλ�ָ���
		String strtmp="",xiaostmp="",tmp="";
		int i=3;
		if(strValue.lastIndexOf(".")==-1){
			
			strtmp=strValue;
			if(strValue.equals("")){
				
				xiaostmp="";
			}else{
				
				xiaostmp=".00";
			}
			
		}else {
			
			strtmp=strValue.substring(0,strValue.lastIndexOf("."));
			
			if(strValue.substring(strValue.lastIndexOf(".")).length()==2){
				
				xiaostmp=strValue.substring(strValue.lastIndexOf("."))+"0";
			}else{
				
				xiaostmp=strValue.substring(strValue.lastIndexOf("."));
			}
			
		}
		tmp=strtmp;
		
		while(i<tmp.length()){
			strtmp=strtmp.substring(0,strtmp.length()-(i+(i-3)/3))+","+strtmp.substring(strtmp.length()-(i+(i-3)/3),strtmp.length());
			i=i+3;
		}
		
		return strtmp+xiaostmp;
	}
//****************�ж�ҳ���Ƿ��ǵ�һ�ε���**************//
	public int getZhuangt() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

	public void setZhuangt(int _value) {
		((Visit) getPage().getVisit()).setInt1(_value);
	}
	
//*****************************************�������ÿ�ʼ******************************************//	
//****************��������*******************//
	public Date getMonthFirstday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE,cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}
	//��ʼ����
	private Date _BeginriqValue =getMonthFirstday(new Date());
	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue=getMonthFirstday(new Date());
		}
		return _BeginriqValue;
	}
	
	public void setBeginriqDate(Date _value) {
		if (FormatDate(_BeginriqValue).equals(FormatDate(_value))) {
			_BeginriqChange=false;
		} else {
			_BeginriqValue = _value;
			_BeginriqChange=true;
		}
	}
	//��������
	private Date _EndriqValue =new Date();
	private boolean _EndriqChange=false;
	public Date getEndriqDate() {
		if (_EndriqValue==null){
			_EndriqValue=new Date();
		}
		return _EndriqValue;
	}
	
	public void setEndriqDate(Date _value) {
		if (FormatDate(_EndriqValue).equals(FormatDate(_value))) {
			_EndriqChange=false;
		} else {
			_EndriqValue = _value;
			_EndriqChange=true;
		}
	}
	
	//��˾����
	public IDropDownBean getGongsmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean2()==null){
			((Visit)getPage().getVisit()).setDropDownBean2((IDropDownBean)getIGongsmcModels().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean2();
	}

	public void setGongsmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean2() != null) {
			
			((Visit)getPage().getVisit()).setDropDownBean2(Value);
			getIDiancmcModels();
		}
	}

	public void setIGongsmcModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getIGongsmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel2() == null) {
			getIGongsmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getIGongsmcModels() {
		
		String sql="";
		sql = "select id,mingc from diancxxb where jib=2 order by mingc";
		((Visit)getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"ȫ��"));
		return ((Visit)getPage().getVisit()).getProSelectionModel2();
	}
	
	//�糧����
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean3()==null){
			((Visit)getPage().getVisit()).setDropDownBean3((IDropDownBean)getIDiancmcModels().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean3();
	}

	public void setDiancmcValue(IDropDownBean value) {

		if(((Visit)getPage().getVisit()).getDropDownBean3()!=value){
			
			((Visit)getPage().getVisit()).setboolean2(true);
			((Visit)getPage().getVisit()).setDropDownBean3(value);
		}
	}

	public void setIDiancmcModel(IPropertySelectionModel value) {

		((Visit)getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel3() == null) {
			
			getIDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel3();
	}

	public IPropertySelectionModel getIDiancmcModels() {
		
		String sql="";
		
		if(((Visit)getPage().getVisit()).getRenyjb()==2){
//			�ֹ�˾
			sql = "select d.id,d.mingc from diancxxb d where fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+" order by d.mingc";
		}else{
//			����
			if(getGongsmcValue().getId()>-1){
				
				sql = "select d.id,d.mingc from diancxxb d where fuid="+getGongsmcValue().getId()+" order by d.mingc";
			}else{
				
				sql = "select d.id,d.mingc from diancxxb d where jib=3 order by d.mingc";
			}
		}
		
		((Visit)getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql,"ȫ��"));
		return ((Visit)getPage().getVisit()).getProSelectionModel3();
	}
	
	//��Ӧ��
	public IDropDownBean getGongysValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean4()==null){
			((Visit)getPage().getVisit()).setDropDownBean4((IDropDownBean)getIGongysModels().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean4();
	}

	public void setGongysValue(IDropDownBean value) {
		
		if(((Visit)getPage().getVisit()).getDropDownBean4()==value){
			
			((Visit)getPage().getVisit()).setboolean2(true);
			((Visit)getPage().getVisit()).setDropDownBean4(value);
		}
	}

	public void setIGongysModel(IPropertySelectionModel value) {

		((Visit)getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getIGongysModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel4() == null) {
			getIGongysModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel4();
	}

	public IPropertySelectionModel getIGongysModels() {
		
		String sql="";
		if(((Visit)getPage().getVisit()).getRenyjb()==3){
//			�糧
			sql = "select distinct gongysb_id,gongysmc from kuangfjsmkb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" order by gongysmc";
		}else if(((Visit)getPage().getVisit()).getRenyjb()==2){
//			��˾
			sql = "select distinct gongysb_id,gongysmc from kuangfjsmkb where diancxxb_id in (select id from diancxxb where fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+") order by gongysmc";
		}else if(((Visit)getPage().getVisit()).getRenyjb()==1){
//			����
			sql = "select distinct gongysb_id,gongysmc from kuangfjsmkb order by gongysmc";
		}
		((Visit)getPage().getVisit()).setProSelectionModel4( new IDropDownModel(sql,"ȫ��"));
		return ((Visit)getPage().getVisit()).getProSelectionModel4();
	}
	
	
//	��ӡ״̬
	private static IPropertySelectionModel _PrintModel;
	
	public IPropertySelectionModel getPrintModel() {
		if (_PrintModel == null) {
			getPrintModels();
		}
		return _PrintModel;
	}

	private IDropDownBean _PrintValue;

	public IDropDownBean getPrintValue() {
		if(_PrintValue==null){
			setPrintValue((IDropDownBean)getPrintModel().getOption(0));
		}
		return _PrintValue;
	}
	
	private boolean _PrintChange=false;
	public void setPrintValue(IDropDownBean Value) {
		if (_PrintValue==Value) {
			_PrintChange = false;
		}else{
			_PrintValue = Value;
			_PrintChange = true;
		}
	}

	public IPropertySelectionModel getPrintModels() {
		List listPrint=new ArrayList();
		
		listPrint.add(new IDropDownBean(0, "����ӡ"));
		listPrint.add(new IDropDownBean(1, "�Ѵ�ӡ"));
		listPrint.add(new IDropDownBean(2, "ȫ��"));
		
		_PrintModel = new IDropDownModel(listPrint);
		return _PrintModel;
	}

	public void setPrintModel(IPropertySelectionModel _value) {
		_PrintModel = _value;
	}
	
//******************�������*****************//
//	private IDropDownBean _BianhValue;
	public IDropDownBean getBianhValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean5()==null){
			if(getIBianhModel().getOptionCount()>1){
				((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean)getIBianhModel().getOption(0));
			}else{
				((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean)getIBianhModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean5();
	}

	public void setBianhValue(IDropDownBean Value) {
		if(Value==null){
			((Visit) getPage().getVisit()).setDropDownBean5(Value);
		}else{
			if(((Visit) getPage().getVisit()).getDropDownBean5().getId()==Value.getId()){
				((Visit) getPage().getVisit()).setboolean3(false);
			}else{
				((Visit) getPage().getVisit()).setDropDownBean5(Value);
				((Visit) getPage().getVisit()).setboolean3(true);
			}
		}
	}

	public void setIBianhModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getIBianhModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel5() == null) {
			getIBianhModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel5();
	}

	public IPropertySelectionModel getIBianhModels() {
		
		StringBuffer w_sql = new StringBuffer();
		w_sql.append(" where ");
		String beginriq="";
		String endriq="";
		String gongsid="";
		String diancid="";
		String diqumc="";
		String printState="";
		
		beginriq=" jiesrq>="+OraDate(_BeginriqValue);//��ʼ����
		endriq=" and jiesrq<="+OraDate(_EndriqValue);//��������
		w_sql.append(beginriq);
		w_sql.append(endriq);
		
		if(((Visit)getPage().getVisit()).getRenyjb()==1){
			
			if(getGongsmcValue()!=null && getGongsmcValue().getId()!=-1){//��˾����
				
				gongsid=" and diancxxb_id in (select id from diancxxb where fuid="+getGongsmcValue().getId()+")";
				w_sql.append(gongsid);
			}
			
			if(getDiancmcValue()!=null && getDiancmcValue().getId()!=-1){//�糧����
				
				diancid=" and diancxxb_id="+getDiancmcValue().getId();
				w_sql.append(diancid);
			}
			
			
		}else if(((Visit)getPage().getVisit()).getRenyjb()==2){
			
			if(getDiancmcValue()!=null && getDiancmcValue().getId()!=-1){//�糧����
				
				diancid=" and diancxxb_id="+getDiancmcValue().getId();
				w_sql.append(diancid);
			}else{
				
				diancid=" and diancxxb_id in (select id from diancxxb where fuid="+getDiancmcValue().getId()+")";
				w_sql.append(diancid);
			}
			
			
		}else if(((Visit)getPage().getVisit()).getRenyjb()==3){
			
			diancid=" and diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id();
			w_sql.append(diancid);
		}
		
		if(getGongysValue()!=null && getGongysValue().getId()!=-1){//��Ӧ��
			
			diqumc=" and gongysb_id='"+getGongysValue().getId()+"'";
			w_sql.append(diqumc);
		}
		
		String sql="";
		
		sql = " select id,bianm from ("
			+ " select js.id,js.bianm,js.diancxxb_id,gongysb_id from kuangfjsmkb js" +  w_sql.toString() + printState
			+ " union "
			+ " select yf.id,yf.bianm,yf.diancxxb_id,gongysb_id from kuangfjsyfb yf " + w_sql.toString() + " and yf.jieslx<>1"    
			+ " ) order by bianm";
		
		setIBianhModel(new IDropDownModel(sql,"��ѡ��"));
		setIBianhModel1(new IDropDownModel(sql,"��ѡ��"));
		return ((Visit)getPage().getVisit()).getProSelectionModel5();
	}
	
//	********************************************************************
	public boolean BianhChange1 = false;
	public IDropDownBean getBianhValue1() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			if(getIBianhModel1().getOptionCount()>1){
				((Visit) getPage().getVisit()).setDropDownBean1((IDropDownBean)getIBianhModel1().getOption(0));
			}else{
				((Visit) getPage().getVisit()).setDropDownBean1((IDropDownBean)getIBianhModel1().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setBianhValue1(IDropDownBean Value) {
		if(Value==null){
			((Visit) getPage().getVisit()).setDropDownBean1(Value);
		}else{
			if(((Visit) getPage().getVisit()).getDropDownBean1().getId()==Value.getId()){
				BianhChange1 = false;
			}else{
				((Visit) getPage().getVisit()).setDropDownBean1(Value);
				BianhChange1 = true;
			}
		}
	}
//	private IPropertySelectionModel _IBianhModel;

	public void setIBianhModel1(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getIBianhModel1() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getIBianhModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}
//**********���㵥����****************
	
	private IDropDownBean _MingcValue;

	public IDropDownBean getMingcValue() {
		if(_MingcValue==null){
			
			_MingcValue=((IDropDownBean)getIMingcModel().getOption(0));
		}
		return _MingcValue;
	}

	public void setMingcValue(IDropDownBean Value) {
		_MingcValue = Value;
	}
	
	private IPropertySelectionModel _IMingcModel;

	public void setIMingcModel(IPropertySelectionModel value) {
		_IMingcModel = value;
	}

	public IPropertySelectionModel getIMingcModel() {
		if (_IMingcModel == null) {
			getIMingcModels();
		}
		return _IMingcModel;
	}

	public IPropertySelectionModel getIMingcModels() {
		
		List listMingc=new ArrayList();
		listMingc.add(new IDropDownBean(0, "ȼ�ϲɹ���ⵥ"));
		listMingc.add(new IDropDownBean(1, "ȼ�ϲɹ����ⵥ"));
		
		_IMingcModel = new IDropDownModel(listMingc);
		return _IMingcModel;
	}
	
//
	
//*****************************************�������ý���******************************************//		
	
//***************������Ϣ��******************//
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
//******************��ť����****************//
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
	
	public void submit(IRequestCycle cycle) {

		if(_RefurbishChick){
			_RefurbishChick=false;
			chaxunzt1 = 1;// ��ѯ״̬
			zhuangt=2;
			setBianhValue(null);
			setIBianhModel(null);
			getIBianhModels();
		}
		if (_QuedChick) {
			chaxunzt1 = 1;// ��ѯ״̬
			zhuangt=2;
			Refurbish();
			_QuedChick = false;
		}
		if(_PrintChick){
			_PrintChick=false;
			PrintState();
		}
	}

	private void PrintState(){
		JDBCcon con=new JDBCcon();
		String bianh[] = getWhere();
		String where="";
		for(int i=0;i<bianh.length;i++){
			if(where.equals("")){
				where = "'"+bianh[i]+"'";
			}else{
				where = where+",'"+bianh[i]+"'";
			}
		}
		String sql = "update kuangfjsmkb set gongsdyzt=1 where bianh in ("+where+")";
		con.getUpdate(sql);

		con.Close();
		setBianhValue(null);
		setIBianhModel(null);
		chaxunzt1 = 1;// ��ѯ״̬
		zhuangt=2;
		getSelectData();
	}
	
	
	private void Refurbish() {
		// Ϊ "ˢ��" ��ť��Ӵ������
		 getSelectData();
	}
//******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		} else {
			visit.setboolean1(false);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setInt1(2);//�ǵ�һ����ʾ
			visit.setString2("");
			zhuangt=visit.getInt1();
//			System.out.println("��һ������zhuangt="+visit.getZhuangt());
			chaxunzt1 = 0;
//*************�������ÿ�ʼ***************//
			_BeginriqValue = getMonthFirstday(new Date());
			_EndriqValue = new Date();
			visit.setboolean2(false);//����	;
			visit.setboolean3(false);//���㵥
			visit.setboolean4(false);//��˾��ʾ
			visit.setboolean5(false);//�糧��ʾ
			//��˾
			if(visit.getRenyjb()<2){
				
				visit.setboolean4(true);
				visit.setboolean5(true);
				setGongsmcValue(null);
				setIGongsmcModel(null);
				getIGongsmcModels();
			}
			//�糧
			if(visit.getRenyjb()<3){
				
				visit.setboolean5(true);
				setDiancmcValue(null);
				setIDiancmcModel(null);
				getIDiancmcModels();
			}
			
			//��Ӧ��
			setGongysValue(null);
			setIGongysModel(null);
			getIGongysModels();
			

			//���
			setBianhValue(null);
			setIBianhModel(null);
			getIBianhModels();
			
			
			setJiesbhSel(null);
			setJiesbhSel1(null);
		}
		if(visit.getboolean2()){//�����仯ʱ
			setBianhValue(null);
			setIBianhModel(null);
			getIBianhModels();
		}

		if(_EndriqChange){
			_EndriqChange=false;
			setBianhValue(null);
			setIBianhModel(null);
			getIBianhModels();
		}
		if(_BeginriqChange){
			_BeginriqChange=false;
			setBianhValue(null);
			setIBianhModel(null);
			getIBianhModels();
		}
		if(_PrintChange){
			_PrintChange=false;
			setBianhValue(null);
			setIBianhModel(null);
		}
//		if(_JiesbhSelchange){
//			_JiesbhSelchange=false;
//			setJiesbhSel1(null);
//			getJiesbhSel1();
//		}
		if(visit.getboolean3()){
        	visit.setboolean3(false);
//			if(getJiesbhSel()!=null){
				chaxunzt1 = 1;// ��ѯ״̬
				zhuangt=2;
				Refurbish();
//			}
        }
		if(zhuangt==1){//��Ҫ��
			visit.setInt1(1);
		}
		if(zhuangt==2){//��Ҫ��
			visit.setInt1(2);
		}
		zhuangt=1;
 //*************�������ý���***************//
		
	}

//*****************************������������*****************************//
	private int chaxunzt1 = 0;// ��ѯ״̬
	private int zhuangt =1;
	
	private boolean _JiesbhSelchange;
	public String getJiesbhSel() {
		
		return ((Visit)getPage().getVisit()).getString1();
	}
	public void setJiesbhSel(String Jiesbhsel) {
		if(((Visit)getPage().getVisit()).getString1()!=null){
			if(((Visit)getPage().getVisit()).getString1().equals(Jiesbhsel)){
				((Visit)getPage().getVisit()).setboolean3(false);
			}else{
				((Visit)getPage().getVisit()).setboolean3(true);
			}
		}else if(Jiesbhsel!=null){
			if(!Jiesbhsel.equals("")){
				((Visit)getPage().getVisit()).setboolean3(true);
			}
		}
		((Visit)getPage().getVisit()).setString1(Jiesbhsel);
	}
	
	public String getJiesbhSel1() {
		
		return ((Visit)getPage().getVisit()).getString2();
	}
	public void setJiesbhSel1(String Jiesbhsel) {
		if(((Visit)getPage().getVisit()).getString2()!=null){
			if(((Visit)getPage().getVisit()).getString2().equals(Jiesbhsel)){
				((Visit)getPage().getVisit()).setboolean3(false);
			}else{
				((Visit)getPage().getVisit()).setboolean3(true);
			}
		}else if(Jiesbhsel!=null){
			if(!Jiesbhsel.equals("")){
				((Visit)getPage().getVisit()).setboolean3(true);
			}
		}
		((Visit)getPage().getVisit()).setString2(Jiesbhsel);
	}
	
	public String[] getWhere() {//��ѯ����
		String bianh[];
		
		if(getJiesbhSel()!=null && !getJiesbhSel().equals("") && !getJiesbhSel().equals("��ѡ��")){//������
			bianh=getJiesbhSel().split(",");
		}else{
			bianh=null;
		}
		return bianh;
	}
	
	public String getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		if (chaxunzt1 == 1) {// ��ѯ״̬
			chaxunzt1 = 2;
			return "";
		} else if (chaxunzt1 == 2) {
			String bianh[]=getWhere();
			if(bianh!=null){
				if(bianh.length==0){
					return "";
				}else{
					visit.setInt1(2);//�ǵ�һ����ʾ
					chaxunzt1=0;
					zhuangt=1;
					Kuangfjsd jsd = new Kuangfjsd();
					StringBuffer sb = new StringBuffer();
					_CurrentPage=1;
					setAllPages(bianh.length);
					jsd.setAllPages(bianh.length);
					for(int p=0;p<bianh.length;p++){
						sb.append(jsd.getKuangfjsd(bianh[p],p));
					}
					return sb.toString();
				}
			}else{
				return "";
			}
		}else {
			return "";
		}
	}
//***************************�����ʼ����***************************//
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
		if (mstrReportName.equals(RT_HET)) {
			return getSelectData();
		} else {
			return "�޴˱���";
		}
	}
	
//******************************����*******************************//
	
//	public String getcontext() {
//		return "var  context='http://"
//				+ this.getRequestCycle().getRequestContext().getServerName()
//				+ ":"
//				+ this.getRequestCycle().getRequestContext().getServerPort()
//				+ ((Visit) getPage().getVisit()).getContextPath() + "';";
//	}

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
	
	public String getTianzdw(long diancxxb_id) {
		String Tianzdw="";
		JDBCcon con=new JDBCcon();
		try{
			String sql="select quanc from gongsxxb where id="+diancxxb_id;
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				Tianzdw=rs.getString("quanc");
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return Tianzdw;
	}
	
	private String FormatDate(Date _date) {
		if (_date == null) {
//			return MainGlobal.Formatdate("yyyy�� MM�� dd��", new Date());
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
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