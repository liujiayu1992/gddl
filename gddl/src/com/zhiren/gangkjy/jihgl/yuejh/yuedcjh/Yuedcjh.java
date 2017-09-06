package com.zhiren.gangkjy.jihgl.yuejh.yuedcjh;

/**
 * @author ����
 */


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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.*;

public class Yuedcjh extends BasePage implements PageValidateListener {
	public List gridColumns;
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
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

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		if(getChange() == null || "".equals(getChange())){
			setMsg("error,�޸ļ�¼Ϊ�գ�");
			return;
		}
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		int flag =0;
		con.setAutoCommit(false);
//		StringBuffer sql = new StringBuffer("begin \n");
		
		String sqldel ="";
		StringBuffer sb = new StringBuffer("begin \n");
		//ɾ��
		ResultSetList rsldel = getExtGrid().getDeleteResultSet(getChange());
		while (rsldel.next()) {
			sqldel ="delete from yuedcjhb where id = "+rsldel.getString(0)+";\n";
			sb.append(sqldel);
		}
		
		
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		
		String sql ="";

		while (rsl.next()) {
			
		
		
					int id = rsl.getInt("id");
					sql += "update yuedcjhb set "
						+ " riq = to_date('" +rsl.getString("riq")+"','yyyy-mm-dd')"
						+ ",xiemcs = " + rsl.getString("xiemcs")
						+ ", xiemds=" + rsl.getString("xiemds")
						+ ",zhuangccm = '" + rsl.getString("zhuangccm")
						+ "',zhuangcds = " + rsl.getString("zhuangcds")
						+ " ,duiccm = " + rsl.getString("duiccm")
						+ "  where id = " + id + ";\n";
				
		}
		
			sql += "end;\n";
			sb.append(sql);
			flag = con.getUpdate(sb.toString());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
						+ sql);
				setMsg(ErrorMessage.DeleteDatabaseFail);
				return;
			}
			if (flag !=-1){
				setMsg("����ɹ���");
			}
		
		rsldel.close();
		rsl.close();
		con.Close();
		getSelectData();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _CreateChick = false;
	public void CreateButton(IRequestCycle cycle) {
		_CreateChick = true;
	}
	private boolean _CopyLast = false;
	public void CopyLast(IRequestCycle cycle) {
		_CopyLast = true;
	}

	private boolean _RefurbishChick = false;
    public void RefurbishButton(IRequestCycle cycle) {
        _RefurbishChick = true;
    }
    private void Refurbish() {
            //Ϊ  "ˢ��"  ��ť��Ӵ������
    	getSelectData();
    }

	public void submit(IRequestCycle cycle) {
		Visit visit = (Visit) this.getPage().getVisit();
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_CreateChick) {
			_CreateChick = false;
			createData(); //���ɵ�������
			getSelectData();
		}
		if(_CopyLast){
			_CopyLast = false;
			copyData();//������������
			getSelectData();
		}
		if (_RefurbishChick) {
            _RefurbishChick = false;
            Refurbish();
        }

	}
	public void copyData(){
		Visit v = (Visit)getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
//		 ����������ݺ��·�������
		long intyear;
		String StrMonth = "";
		String laststr = "";
		long intMonth;
		int yues =0;
		int copynew = 0;
		long lastMonth = 1;
		String strdata = "";
		String strdata1 = "";
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
			if(intMonth >1){
				lastMonth = intMonth -1;
			}
		} else { 
			intMonth = getYuefValue().getId();
			if(intMonth >1){
				lastMonth = intMonth -1;
			}
		}
		
		
		
		if (intMonth < 10) {  //�õ�����
			
			StrMonth = "0" + intMonth;
			
		} else {
			StrMonth = "" + intMonth;
		}
		
		if (lastMonth < 10) {  //�õ�ǰ��
			
			laststr= "0" + lastMonth;
			
		} else {
			laststr = "" + lastMonth;
		}
		strdata1 = intyear + "-" + laststr+"-01";
		strdata = intyear + "-" +StrMonth+"-01";
		
		String sql = "";
		
		// ����Ϊ2��ʱ����Ҫ��ǰ��ȡ��ǰ29��28�ŵ����ݴ��뵱�£�����Ҫ��������
		if(intMonth == 2){
			if((intyear%4 == 0)&&((intyear%100 != 0)&&(intyear%400 == 0))) { 
				yues = 29;
				copynew = 0;
			}else{
				yues= 28;
				copynew = 0;
			}
		//����Ϊ4 6 9 11��ʱ����Ҫ��ǰ��ȡ��30�������ݴ��뵱�£�����Ҫ��������
		}else if(intMonth == 4||intMonth == 6||intMonth == 9||intMonth == 11){
			yues = 30;
			copynew = 1;
		
		//����Ϊ1��ʱ��ֱ���������ݣ�ȫ��ΪĬ��ֵ	
		}else if(intMonth == 1){
			createData();
			return ;
			
		//����Ϊ3��ʱ���жϣ��������Ϊ�����ȡ29����������������ȡ28����¼ ��¼2��3����¼	
		}else if(intMonth == 3){
			if((intyear%4 == 0)&&((intyear%100 != 0)&&(intyear%400 == 0))) { 
				yues = 29;
				copynew = 2;
			}else{
				yues = 28;
				copynew = 3;
			}
		//����Ϊ 5 7 10 12ʱ����Ҫ��ǰ��ȡ30����¼������1����¼	
		}else if(intMonth == 5||intMonth == 7||intMonth == 10||intMonth == 12 ){
			yues = 30;
			copynew = 1;
		//����Ϊ8��ʱ����Ҫ��ǰ��ȡ31����¼������Ҫ�����¼
		}else{
			yues = 31;
			copynew = 0;
		}
		sql = 
			"select y.id id,\n" +
			"       y.diancxxb_id diancxxb,\n" + 
			"       to_char(y.riq,'yyyy-mm-dd') riq,\n" + 
			"       y.meicb_id meicb_id,\n" + 
			"       y.xiemcs xiemcs,\n" + 
			"       y.xiemds xiemds,\n" + 
			"       nvl(y.zhuangccm,'') zhuangccm,\n" + 
			"       y.zhuangcds zhuangcds,\n" + 
			"       y.duiccm duiccm,\n" + 
			"       y.leib leib,\n" + 
			"       y.zhuangt zhuangt,\n" + 
			"       y.beiz beiz" +
			"  from (select *\n" + 
			"          from yuedcjhb\n" + 
			"         where riq >= to_date('"+strdata1+"', 'yyyy-mm-dd')\n" + 
			"           and riq < to_date('"+strdata+"', 'yyyy-mm-dd')\n" + 
			"         order by riq) y\n" + 
			" where rownum <= "+yues+" \n";
		StringBuffer sb = new StringBuffer("begin \n");
		sb.append(deleteData());
		try{
		
		int market = 0;
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			rsl.beforefirst();
		
		while(rsl.next()){
			market++ ;
			long new_id = Long.parseLong(MainGlobal.getNewID(v.getDiancxxb_id()));
			sb.append("insert into yuedcjhb(id,diancxxb_id,riq,meicb_id,xiemcs,xiemds,zhuangccm,zhuangcds,duiccm,leib,zhuangt)");
			sb.append(" values(").append(new_id).append(",").append(v.getDiancxxb_id());
			sb.append(",add_months(to_date('").append(rsl.getString("riq")).append("','yyyy-mm-dd'),1),0,").append(rsl.getInt("xiemcs"));
			sb.append(",").append(rsl.getDouble("xiemds")).append(",'").append(rsl.getString("zhuangccm"));
			sb.append("',").append(rsl.getDouble("zhuangcds")).append(",").append(rsl.getDouble("duiccm"));
			sb.append(",").append(rsl.getInt("leib")).append(",").append(rsl.getInt("zhuangt"));
			sb.append("); \n");
			
			}
		}else{
			createData();
			return;
		}
		if(copynew >0){
			for(int i= 1;i<=copynew;i++){
				long new_id = Long.parseLong(MainGlobal.getNewID(v.getDiancxxb_id()));
				sb.append("insert into yuedcjhb(id,diancxxb_id,riq,meicb_id,xiemcs,xiemds,zhuangccm,zhuangcds,duiccm,leib,zhuangt)");
				sb.append(" values(").append(new_id).append(",").append(v.getDiancxxb_id());
				sb.append(",to_date('").append(strdata).append("-").append(market+i).append(",0,");
				sb.append("0,0.0,'',0.0,0.0,0,1); \n");
				
			}
			
		}
		sb.append(" end ; \n");
		con.getInsert(sb.toString());
		con.commit();
		}catch(Exception e){
			con.rollBack();
			setMsg("����ǰ������ʧ�ܣ�");
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
	}

	public String deleteData(){
		long intyear;
		String StrMonth = "";
		long intMonth;

		long nextMonth = 0;
		String  monthstr;
		String strdate;
		String strdate1;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
			nextMonth = intMonth +1;
		} else { 
			intMonth = getYuefValue().getId();
			nextMonth = intMonth +1;
		}
		if(intMonth <12){
			if (intMonth < 9) {

				StrMonth = "0" + intMonth;
				nextMonth = intMonth + 1;
				monthstr = "0" + nextMonth;
			}else if(intMonth == 9){
				StrMonth = "0"+ intMonth;
				nextMonth = intMonth + 1;
				monthstr = "" + nextMonth;
				
			}else {
				StrMonth = "" + intMonth;
				nextMonth = intMonth + 1;
				monthstr = "" + nextMonth;
			}
			strdate = intyear+"-"+StrMonth+"-01";
			strdate1 = intyear + "-" +monthstr +"-01";
			
		}else{
			strdate = intyear+"-"+intMonth+"-01";
			strdate1 = (intyear+1) +"-01-01";
		}
		
		String ss = "delete from yuedcjhb where riq>=to_date('"+strdate+"','yyyy-mm-dd') and " +
					" riq<to_date('"+strdate1+"','yyyy-mm-dd');" ;
		
		return ss;
	}

	public void createData() {	
//		 ����������ݺ��·�������
		long intyear;
		String StrMonth = "";
		long intMonth;
		int yues =0;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else { 
			intMonth = getYuefValue().getId();
		}
		if(intMonth == 2){
			if((intyear%4 == 0)&&((intyear%100 != 0)&&(intyear%400 == 0))) { 
				yues = 29;
			}else{
				yues= 28;
			}
		}else if(intMonth == 4||intMonth == 6||intMonth == 9||intMonth == 11){
			yues = 30;
		}else{
			yues = 31;
		}
		
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
			
		} else {
			StrMonth = "" + intMonth;
			
		}
		int xiemcs = 0;
		double xiemds = 0.0;

		double zhuangcds = 0.0;
		double duiccm = 0.0;
		Visit v = (Visit)getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer("begin \n");
		sb.append(deleteData());
		try{
		for(int i = 1;i<= yues ;i++){
			long new_id = Long.parseLong(MainGlobal.getNewID(v.getDiancxxb_id()));
			if(i < 10){
			sb.append("insert into yuedcjhb(id,diancxxb_id,riq,meicb_id,xiemcs,xiemds,zhuangccm,zhuangcds,duiccm,leib,zhuangt)");
			sb.append(" values(").append(new_id).append(",").append(v.getDiancxxb_id());
			sb.append(",").append("to_date('").append(intyear).append("-").append(StrMonth);
			sb.append("-0").append(i).append("','yyyy-mm-dd'),0,").append(xiemcs).append(",").append(xiemds);
			sb.append(",''").append(",").append(zhuangcds).append(",");
			sb.append(duiccm).append(",0,1); \n");
			}else{
				sb.append("insert into yuedcjhb(id,diancxxb_id,riq,meicb_id,xiemcs,xiemds,zhuangccm,zhuangcds,duiccm,leib,zhuangt)");
				sb.append(" values(").append(new_id).append(",").append(v.getDiancxxb_id());
				sb.append(",").append("to_date('").append(intyear).append("-").append(StrMonth);
				sb.append("-").append(i).append("','yyyy-mm-dd'),0,").append(xiemcs).append(",").append(xiemds);
				sb.append(",''").append(",").append(zhuangcds).append(",");
				sb.append(duiccm).append(",0,1);\n");
			}
		}
		sb.append("end ; \n");
		con.getInsert(sb.toString());
		con.commit();
		}catch(Exception e){
			con.rollBack();
			setMsg("��������ʧ�ܣ�");
			e.printStackTrace();
		}finally{
			con.Close();
		}
	  
		
		
	}
	
	public void getSelectData() {
		
	
		
//		����������ݺ��·�������
		long intyear;
		String StrMonth = "";
		long intMonth;
		long nextMonth;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else { 
			intMonth = getYuefValue().getId();
		}
		/*String strdate;
		String strdate1;
		String monthstr;
		//���·�С��12��ʱ���õ����µ�һ����¸��µ�һ���ֵ��
		//���·ݵ���12��ʱ���õ�12�µ�һ������һ���ֵ��
		if(intMonth <12){
			if (intMonth < 9) {

				StrMonth = "0" + intMonth;
				nextMonth = intMonth + 1;
				monthstr = "0" + nextMonth;
			} else {
				StrMonth = "" + intMonth;
				nextMonth = intMonth + 1;
				monthstr = "" + nextMonth;
			}
			strdate = intyear+"-"+StrMonth+"-01";
			strdate1 = intyear + "-" +monthstr +"-01";
			
		}else{
			strdate = intyear+"-"+intMonth+"-01";
			strdate1 = intyear +"-" +intMonth +"-31";
		}*/

		

		
		Visit visit = (Visit) getPage().getVisit();
		String sql1 = "";
	
		JDBCcon con = new JDBCcon();
		
			
			
			 /*sql1= 
				 "select y.id          id,\n" +
				 "       y.diancxxb_id diancxxb_id,\n" + 
				 "       y.riq         riq,\n" + 
				 "       y.xiemcs      xiemcs,\n" + 
				 "       y.xiemds      xiemds,\n" + 
				 "       y.zhuangccm   zhuangccm,\n" + 
				 "       y.zhuangcds   zhuangcds,\n" + 
				 "       y.duiccm  duiccm \n"+
				 "  from yuedcjhb y \n" + 
				 " where  y.riq >= to_date('"+intyear+"-"+intMonth+"-01"+"', 'yyyy-mm-dd')-1\n" +
				 "and y.riq <add_months( to_date('"+intyear+"-"+intMonth+"-01"+"', 'yyyy-mm-dd'),1)";*/

     String shijq=intyear+"-"+intMonth+"-1";
   sql1="  select -1         id,\n"+
     "nvl(y.diancxxb_id,"+visit.getDiancxxb_id()+") diancxxb_id,\n"+
     "nvl(y.riq,dch.riq)         riq,\n"+
     "nvl(y.xiemcs,0)      xiemcs,\n"+
     "nvl(y.xiemds,0)      xiemds,\n"+
     "nvl(y.zhuangccm,'')   zhuangccm,\n"+
     "nvl(y.zhuangcds,0)   zhuangcds,\n"+
      " dch.kucl   duiccm\n"+
     "from yuedcjhb y ,\n"+
     "(\n"+
     "   select sum(kucl) kucl,to_date('"+shijq+"','yyyy-mm-dd')-1 riq from\n"+ 
     "   duowkcb dw1,\n"+
     "    (select meicb_id,max(shij)shij from duowkcb where shij<to_date('"+shijq+"','yyyy-mm-dd') and leib<>3   group by meicb_id)  gd\n"+
     "     ,meicb mei\n"+
     "      where dw1.meicb_id=gd.meicb_id and dw1.shij=gd.shij and mei.id=dw1.meicb_id and mei.diancxxb_id="+visit.getDiancxxb_id()+"\n"+
     " )  dch\n"+
     "where  y.riq(+)=dch.riq\n"+
	  "union\n"+
	 "select y.id          id,\n"+
	 "      y.diancxxb_id diancxxb_id,\n"+
	 "      y.riq         riq,\n"+
	   "    y.xiemcs      xiemcs,\n"+
	   "    y.xiemds      xiemds,\n"+
	   "    y.zhuangccm   zhuangccm,\n"+
	   "    y.zhuangcds   zhuangcds,\n"+
	   "    y.duiccm  duiccm \n"+
	 " from yuedcjhb y \n"+
	" where  y.riq >= to_date('"+shijq+"', 'yyyy-mm-dd')\n"+
"	and y.riq < add_months(to_date('"+shijq+"', 'yyyy-mm-dd'),1)\n";

		ResultSetList rsl = con.getResultSetList(sql1);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);

		//����ҳ����
		//egu.setWidth(Locale.Grid_DefaultWidth);
		//����GRID�Ƿ���Ա༭
		
		egu.addPaging(0);//�������ݲ���ҳ
		egu.setTableName("yuedcjhb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("riq").setHeader("����");
		egu.getColumn("riq").setWidth(100);
		egu.getColumn("riq").setEditor(null);
//		egu.getColumn("xiemcs").setHeader("жú����");
//		egu.getColumn("xiemcs").setWidth(80);
//		egu.getColumn("xiemds").setHeader("жú����");
//		egu.getColumn("xiemds").setWidth(80);
        egu.getColumn("zhuangccm").setHeader("װ������");
//	    egu.getColumn("zhuangccm").setWidth(80);
//		egu.getColumn("zhuangcds").setHeader("װ������");
//		egu.getColumn("zhuangcds").setWidth(80);
		egu.getColumn("duiccm").setHeader("�ѳ���ú");
		egu.getColumn("duiccm").setWidth(70);
		
		egu.getColumn("zhuangccm").setEditor(new ComboBox());
		egu.getColumn("zhuangccm").setComboEditor(egu.gridId, new IDropDownModel(" select id,mingc from luncxxb order by mingc"));
		egu.getColumn("zhuangccm").setDefaultValue("���ú�");
		
//		 ������
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
		egu.addTbarText("-");// ���÷ָ���
		

		
		//ˢ�°�ť
		GridButton gbtr = new GridButton("ˢ��","function(){document.getElementById('RefurbishButton').click();}");
		gbtr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbtr);
		
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
//		���ɰ�ť
		GridButton gbt = new GridButton("����","function(){Ext.MessageBox.alert('','���ɻὫ��������������ݸ���');document.getElementById('CreateButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbt);
		
		//������������
//		gbt = new GridButton("������������","function(){document.getElementById('CopyLast').click();}");
//		gbt.setIcon(SysConstant.Btn_Icon_Copy);
//		egu.addTbarBtn(gbt);
		
		StringBuffer sb = new StringBuffer();
		String Headers = 

			"		 [\n" +
			"         {header:'<table><tr><td width=6 align=center></td></tr></table>', align:'center',rowspan:2},\n" + 
			"         {header:'ID', align:'center',rowspan:2},\n" + 
			"         {header:'DIANCXXB_ID', align:'center',rowspan:2},\n" +
			"		  {header:'����',align:'center',rowspan:2},\n" + 
			"         {header:'жú', colspan:2},\n" + 
			"         {header:'װ��', colspan:2},\n" +
			"		  {header:'�ѳ���<br>����',align:'center',rowspan:2}" + 
			"        ],\n" + 
			"        [\n" + 
			"	      {header:'<table><tr><td width=80 align=center style=border:0>����</td></tr></table>', align:'center'},\n" + 
			"         {header:'<table><tr><td width=80 align=center style=border:0>����</td></tr></table>', align:'center'},\n" + 
			"         {header:'<table><tr><td width=80 align=center style=border:0>����</td></tr></table>', align:'center'},\n" + 
			"	      {header:'<table><tr><td width=70 align=center style=border:0>����</td></tr></table>', align:'center'}\n" + 
			"        ]"; 

		sb.append(Headers);
		egu.setHeaders(sb.toString());
		egu.setPlugins("new Ext.ux.plugins.XGrid()");
//		---------------ҳ��js�ļ��㿪ʼ------------------------------------------
		StringBuffer sbjs = new StringBuffer();
		sbjs.append("gridDiv_grid.on('afteredit', function(e){");
		//�����ۼƹ�ú		
		sbjs.append("if(e.field=='ZHUANGCCM'){" +
					"var chuanm=e.record.get('ZHUANGCCM');"+
					"var records=gridDiv_ds.getRange(e.row);"+
					"for (var i=0;i<records.length;i++)" +
					"	{records[i].set('ZHUANGCCM',chuanm);}}\n");
		
		 String Jsscript="if(e.field=='DUICCM'||e.field=='XIEMDS'||e.field=='ZHUANGCDS'){\n"+
		 " var currentrownumber=gridDiv_ds.indexOf(gridDiv_sm.getSelected())\n"+
          "if(e.field=='XIEMDS'||e.field=='ZHUANGCDS')\n"+
           " e.record.set('DUICCM',Number(gridDiv_ds.getAt(currentrownumber-1).get('DUICCM'))+Number(e.record.get('XIEMDS'))-Number(e.record.get('ZHUANGCDS')));\n"+
          "if(currentrownumber+1<=gridDiv_ds.getTotalCount()-1){ \n"+
			"				 var records = gridDiv_ds.getRange(currentrownumber+1,gridDiv_ds.getTotalCount()-1);\n"+
 			"			  for(var i=0;i<records.length;i++){\n"+
		"		              records[i].set('DUICCM',Number(e.record.get('DUICCM'))+Number(records[i].get('XIEMDS'))-Number(records[i].get('ZHUANGCDS')));\n"+
		"		              }\n"+
	     "      }\n"+

        " }\n";
		sbjs.append(Jsscript);
		sbjs.append("});");
		
		egu.addOtherScript(sbjs.toString());
	    String	beforeScript=" gridDiv_grid.on('beforeedit',function(e){\n"+ 
							    "if(Number(e.record.get('ID'))<0){\n"+
							    "    e.cancel=true;\n"+
							    " }\n"+
							   

						     "});\n";

		egu.addOtherScript(beforeScript);
//		---------------ҳ��js�������--------------------------
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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			getSelectData();
		}
	}

//	���ڿؼ�
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
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
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


	public IDropDownBean getYuefValue(){
		if (_YuefValue == null) {
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
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
}




