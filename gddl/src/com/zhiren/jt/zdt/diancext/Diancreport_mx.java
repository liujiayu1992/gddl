package com.zhiren.jt.zdt.diancext;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.Money;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;

import org.apache.tapestry.contrib.palette.SortMode;


public class Diancreport_mx  extends BasePage implements PageValidateListener{
	
//	 �ж��Ƿ��Ǽ����û�
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// ����

	} 

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// ��˾
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// �糧
	}
	
//	 ��ʼ����
	private Date _BeginriqValue = new Date();

	private boolean _BeginriqChange = false;

	public Date getBeginriqDate() {
		if (_BeginriqValue == null) {
			_BeginriqValue = new Date();
		}
		return _BeginriqValue;
	}

	public void setBeginriqDate(Date _value) {
		if (DateUtil.Formatdate("yyyy-MM-dd", _BeginriqValue).equals(
				DateUtil.Formatdate("yyyy-MM-dd", _value))) {
			_BeginriqChange = false;
		} else {
			_BeginriqValue = _value;
			_BeginriqChange = true;
		}
	}
	
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
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
	}
	
	private void Refurbish() {
        //Ϊ "ˢ��" ��ť��Ӵ������
		isBegin=true;
		getSelectData();
	}

//******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setNianfValue(null);
			setYuefValue(null);
			getNianfModels();
			getYuefModels();
			setBaoblxValue(null);
			getIBaoblxModels();
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			isBegin=true;
			this.getSelectData();
		}
		if(nianfchanged){
			nianfchanged=false;
			Refurbish();
		}
		if(yuefchanged){
			yuefchanged=false;
			Refurbish();
		}
		if(_Baoblxchange){
			_Baoblxchange=false;
			Refurbish();
		}
		
		visit.setLong1(0);
		if (cycle.getRequestContext().getParameter("diancxxb_id") != null&&!cycle.getRequestContext().getParameter("diancxxb_id").equals("-1")) {
				visit.setLong1(Long.parseLong(cycle.getRequestContext().getParameter("diancxxb_id")));
			}
		Refurbish();
	}
	
	private String RT_HET="Ranlsckb";
	private String mstrReportName="Ranlsckb";//ȼ�������챨
	
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
		if (mstrReportName.equals(RT_HET)){
			return getSelectData();
		}else{
			return "�޴˱���";
		}
	}
	
	public int getZhuangt() {
		return 1;
	}

	private int intZhuangt=0;
	public void setZhuangt(int _value) {
		intZhuangt=1;
	}

	private boolean isBegin=false;
	private String getSelectData(){
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		
		JDBCcon cn = new JDBCcon();
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		int iFixedRows=0;//�̶��к�
		int iCol=0;//����
		
		String diancmc="";
		String diancjc="";
		String tongxdz="";
		String diancdm="";
		String chanqxz="";
		String ranlgs="";
		String suosejgs="";
		String zhongndm="";
		String faddbr="";
		String suosdw="";
		String youzbm="";
		String kaihyh="";
		String zhangh="";
		String shuih="";
		String dianh="";
		String zhuangjts="";
		String rongltais="";
		long rijhm=0;
		long nianhml=0;
		
		String xingm_zjl="";
		String guddh_zjl="";
		String shouj_zjl="";
		String email_zjl="";
		String xingm_zgld="";
		String guddh_zgld="";
		String shouj_zgld="";
		String email_zgld="";
		String xingm_bmzg="";
		String guddh_bmzg="";
		String shouj_bmzg="";
		String email_bmzg="";
		String zongj="";
		String xitwb="";
		String shih="";
		String chuanz="";
		String dianclb="";
		String yunsfs="";
		String daoz="";
		String daozlj="";
		String jiexfs="";
		String jiexnl="";
		String caiyfs="";
		String jilfs="";
		String daog="";
		String rijhm_str="";
		String ninhml_str="";
		
		
		long diancxxb_id=((Visit) getPage().getVisit()).getLong1();
		
			
				strSQL = "select dc.quanc,dc.mingc as jianc,dc.jitbm,dc.bianm,dc.diz,cq.mingc as chanqxz,di.mingc as ranlgs,\n"
				+ "       sj.mingc as shangjgs,dc.faddbr,dc.kaihyh,dc.zhangh,dc.dianh,dc.youzbm,dc.shuih,jz.tais,\n"
				+ "       JizgcInfo(dc.id) as jizgc,dc.rijhm,dc.rijhm*365 as nianhml,\n"
				+ "       dc.yunsfs,c.mingc as daoz,c1.mingc as daog,lj.mingc as daozlj,dc.jiexfs,dc.jiexnl,dc.caiyfs,dc.jilfs,\n"
				+ "       lxr.xingm_zjl,lxr.guddh_zjl,lxr.shouj_zjl,lxr.email_zjl,\n"
				+ "       lxr.xingm_zgld,lxr.guddh_zgld,lxr.shouj_zgld,lxr.email_zgld,lxr.xingm_bmzg,lxr.guddh_bmzg,\n"
				+ "       lxr.shouj_bmzg,lxr.email_bmzg,dc.zongj,dc.xitzjh,dc.shihzjh,dc.chuanz,lb.mingc as dianclbb,'' as suosdw\n"
				+ " from diancxxb dc ,chanqxzb cq,diancxxb di,diancxxb sj,vwjizxx jz,chezxxb c,dianclbb lb,lujxxb lj,chezxxb c1,\n"
				+ "     (select l.diancxxb_id, max(decode(l.lianxrzwb_id,1,l.xingm,'')) as xingm_zjl,\n"
				+ "       max(decode(l.lianxrzwb_id,1,l.guddh,'')) as guddh_zjl,\n"
				+ "       max(decode(l.lianxrzwb_id,1,l.shouj,'')) as shouj_zjl,max(decode(l.lianxrzwb_id,1,l.email,'')) as email_zjl,\n"
				+ "       max(decode(l.lianxrzwb_id,2,l.xingm,'')) as xingm_zgld,max(decode(l.lianxrzwb_id,2,l.guddh,'')) as guddh_zgld,\n"
				+ "       max(decode(l.lianxrzwb_id,2,l.shouj,'')) as shouj_zgld,max(decode(l.lianxrzwb_id,2,l.email,'')) as email_zgld,\n"
				+ "       max(decode(l.lianxrzwb_id,3,l.xingm,'')) as xingm_bmzg,max(decode(l.lianxrzwb_id,3,l.guddh,'')) as guddh_bmzg,\n"
				+ "       max(decode(l.lianxrzwb_id,3,l.shouj,'')) as shouj_bmzg,max(decode(l.lianxrzwb_id,3,l.email,'')) as email_bmzg\n"
				+ "      from lianxrb l\n"
				+ "      group by l.diancxxb_id) lxr\n"
				+ " where dc.chanqxzb_id=cq.id(+)\n"
				+ " and dc.fuid=di.id(+)\n"
				+ " and dc.shangjgsid=di.id(+)\n"
				+ " and dc.fuid=sj.id(+)\n"
				+ " and dc.fuid=sj.id(+)\n"
				+ " and dc.id=jz.diancxxb_id(+)\n"
				+ " and dc.daoz=c.id(+)\n"
				+ " and dc.jib=3\n"
				+ " and dc.id=lxr.diancxxb_id(+)\n"
				+ " and dc.dianclbb_id=lb.id(+)\n"
				+ " and c.lujxxb_id=lj.id(+)\n"
				+ " and dc.daog=c1.id(+) and dc.id="+diancxxb_id+"\n";
				


				ResultSet rs = cn.getResultSet(strSQL);
				try {
					if(rs.next()){
						diancmc = rs.getString("quanc");
						diancjc = rs.getString("jianc");
						tongxdz = rs.getString("diz");
						diancdm = rs.getString("jitbm");
						chanqxz = rs.getString("chanqxz");
						ranlgs = rs.getString("ranlgs");
						suosejgs = rs.getString("shangjgs");
						zhongndm = rs.getString("bianm");
						faddbr = rs.getString("faddbr");
						suosdw = rs.getString("suosdw");
						youzbm = rs.getString("youzbm");
						kaihyh = rs.getString("kaihyh");
						zhangh = rs.getString("zhangh");
						shuih = rs.getString("shuih");
						dianh = rs.getString("dianh");
						zhuangjts = rs.getString("tais");
						rongltais = rs.getString("jizgc");
						rijhm = rs.getLong("rijhm");
						rijhm_str=String.valueOf(rijhm);
						nianhml = rs.getLong("nianhml");
						ninhml_str=String.valueOf(nianhml);
						
						xingm_zjl = rs.getString("xingm_zjl");
						guddh_zjl = rs.getString("guddh_zjl");
						shouj_zjl = rs.getString("shouj_zjl");
						email_zjl = rs.getString("email_zjl");
						xingm_zgld = rs.getString("xingm_zgld");
						guddh_zgld = rs.getString("guddh_zgld");
						shouj_zgld = rs.getString("shouj_zgld");
						email_zgld = rs.getString("email_zgld");
						xingm_bmzg = rs.getString("xingm_bmzg");
						guddh_bmzg = rs.getString("guddh_bmzg");
						shouj_bmzg = rs.getString("shouj_bmzg");
						email_bmzg =rs.getString("email_bmzg");
						
						
						zongj = rs.getString("zongj");
						xitwb = rs.getString("xitzjh");
						shih = rs.getString("shihzjh");
						chuanz = rs.getString("chuanz");
						dianclb = rs.getString("dianclbb");
						yunsfs =rs.getString("yunsfs");
						daoz = rs.getString("daoz");
						daozlj = rs.getString("daozlj") ;
						jiexfs = rs.getString("jiexfs");
						jiexnl = rs.getString("jiexnl");
						caiyfs = rs.getString("caiyfs");
						jilfs = rs.getString("jilfs");
						daog = rs.getString("daog");
						
						
					}
				} catch (SQLException e) {
					// TODO �Զ����� catch ��
					e.printStackTrace();
				}
				
				
			

				 ArrHeader=new String[18][9];
				 ArrHeader[0]=new String[] {"�糧��Ϣ","�糧��Ϣ","�糧��Ϣ","�糧��Ϣ","�糧��Ϣ","�糧��Ϣ","�糧��Ϣ","�糧��Ϣ","�糧��Ϣ"};
				 ArrHeader[1]=new String[] {"�糧����:",diancmc,"","","","","","�糧���:",diancjc};
				 ArrHeader[2]=new String[] {"ͨѶ��ַ:",tongxdz,"","","","","","�糧����:",diancdm};
				 ArrHeader[3]=new String[] {"��Ȩ����:",chanqxz,"����ȼ�Ϲ�˾","����ȼ�Ϲ�˾",ranlgs,"����������˾",suosejgs,"���ܴ���:",zhongndm};
				 ArrHeader[4]=new String[] {"����������:",faddbr,"","","","��������",suosdw,"��������:",youzbm};
				 ArrHeader[5]=new String[] {"��������:",kaihyh,"","","","�˺�:",zhangh,"","",};
				 ArrHeader[6]=new String[] {"˰��:",shuih,"","","","�绰:",dianh,"","",};
				 ArrHeader[7]=new String[] {"�糧��ģ","�糧��ģ","�糧��ģ","�糧��ģ","�糧��ģ","�糧��ģ","�糧��ģ","�糧��ģ","�糧��ģ"};
				 ArrHeader[8]=new String[] {"װ��̨��:",zhuangjts,"����*̨",rongltais,"","�վ���ú(��)",rijhm_str,"���ú��(��)",ninhml_str,};
				 ArrHeader[9]=new String[] {"��ϵ��ʽ","��ϵ��ʽ","��ϵ��ʽ","��ϵ��ʽ","��ϵ��ʽ","��ϵ��ʽ","��ϵ��ʽ","��ϵ��ʽ","��ϵ��ʽ"};
				 ArrHeader[10]=new String[] {"��ϵ��ְ��","����","�绰","�绰","�ֻ�","�ֻ�","Email","Email","Email",};
				 ArrHeader[11]=new String[] {"�ܾ���:",xingm_zjl,guddh_zjl,"",shouj_zjl,"",email_zjl,"","",};
				 ArrHeader[12]=new String[] {"ȼ�������쵼:",xingm_zgld,guddh_zgld,"",shouj_zgld,"",email_zgld,"","",};
				 ArrHeader[13]=new String[] {"ȼ�ϲ�������:",xingm_bmzg,guddh_bmzg,"",shouj_bmzg,"",email_bmzg,"","",};
				 ArrHeader[14]=new String[] {"�糧�ܻ�:",zongj,"ϵͳ΢��",xitwb,"�л�",shih,"����",chuanz,"",};
				 ArrHeader[15]=new String[] {"����","����","����","����","����","����","����","����","����"};
				 ArrHeader[16]=new String[] {"�糧���","���䷽ʽ","��վ","��վ·��","��ж��ʽ","��ж����","������ʽ","������ʽ","����"};
				 ArrHeader[17]=new String[] {dianclb,yunsfs,daoz,daozlj,jiexfs,jiexnl,caiyfs,jilfs,daog};
							
							
				 
				 
				 ArrWidth=new int[] {95,85,65,65,85,85,65,85,100};
				 iFixedRows=1;
				
				 
			
			
			
			
			 
			// ����
			
			rt.setBody(new Table(ArrHeader,0,0,0));
			rt.setTitle("�糧��Ϣ��", ArrWidth);
			rt.setDefaultTitle(1, 2, "��λ:"+this.getDiancmc(), Table.ALIGN_LEFT);
			
	
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(100);
			//rt.body.mergeFixedRow();
			//rt.body.mergeFixedCols();
			rt.body.ShowZero = false;
			
			
		    rt.body.mergeCell(1,1,1,9);
		    rt.body.setCellAlign(1, 1, Table.ALIGN_CENTER);
		    rt.body.mergeCell(2,2,2,7);
		    rt.body.mergeCell(3,2,3,7);
		    rt.body.mergeCell(4,3,4,4);
		    rt.body.mergeCell(5,2,5,5);
		    rt.body.mergeCell(6,2,6,5);
		    rt.body.mergeCell(6,7,6,9);
		    rt.body.mergeCell(7,2,7,5);
		    rt.body.mergeCell(7,7,7,9);
		    rt.body.mergeCell(8,1,8,9);
		    rt.body.setCellAlign(8, 1, Table.ALIGN_CENTER);
		    rt.body.mergeCell(9,4,9,5);
		    rt.body.mergeCell(10,1,10,9);
		    rt.body.setCellAlign(10, 1, Table.ALIGN_CENTER);
		    rt.body.mergeCell(11,3,11,4);
		    rt.body.mergeCell(11,5,11,6);
		    rt.body.mergeCell(11,7,11,9);
		    rt.body.mergeCell(12,3,12,4);
		    rt.body.mergeCell(12,5,12,6);
		    rt.body.mergeCell(12,7,12,9);
		    rt.body.mergeCell(13,3,13,4);
		    rt.body.mergeCell(13,5,13,6);
		    rt.body.mergeCell(13,7,13,9);
		    rt.body.mergeCell(14,3,14,4);
		    rt.body.mergeCell(14,5,14,6);
		    rt.body.mergeCell(14,7,14,9);
		    rt.body.mergeCell(15,8,15,9);
		    rt.body.mergeCell(16,1,16,9);
		    rt.body.setCellAlign(16, 1, Table.ALIGN_CENTER);
		    for (int i=1;i<=9;i++){
		    	rt.body.setColAlign(i, Table.ALIGN_CENTER);
			    
		    }
		   
		    
		    
			
			//ҳ�� 
			  rt.createDefautlFooter(ArrWidth);
			  rt.setDefautlFooter(1,1,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
			 
		
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();

			return rt.getAllPagesHtml();
	}
	//�õ����ӹ����ĵ糧����
	public String getDiancmc(){
		String diancmc="";
		JDBCcon cn = new JDBCcon();
		long diancid=((Visit) getPage().getVisit()).getLong1();
		String sql_diancmc="select d.quanc from diancxxb d where d.id="+diancid;
		ResultSet rs=cn.getResultSet(sql_diancmc);
		try {
			while(rs.next()){
				 diancmc=rs.getString("quanc");
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

//	�糧����
	public boolean _diancmcchange = false;
	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if(_DiancmcValue==null){
			_DiancmcValue=(IDropDownBean)getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		
		String sql="";
		sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);
		

	}
	
	
//	�������
	public boolean _meikdqmcchange = false;
	private IDropDownBean _MeikdqmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if(_MeikdqmcValue==null){
			_MeikdqmcValue=(IDropDownBean)getIMeikdqmcModels().getOption(0);
		}
		return _MeikdqmcValue;
	}

	public void setMeikdqmcValue(IDropDownBean Value) {
		long id = -2;
		if (_MeikdqmcValue != null) {
			id = _MeikdqmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_meikdqmcchange = true;
			} else {
				_meikdqmcchange = false;
			}
		}
		_MeikdqmcValue = Value;
	}

	private IPropertySelectionModel _IMeikdqmcModel;

	public void setIMeikdqmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIMeikdqmcModel() {
		if (_IMeikdqmcModel == null) {
			getIMeikdqmcModels();
		}
		return _IMeikdqmcModel;
	}

	public IPropertySelectionModel getIMeikdqmcModels() {
		JDBCcon con = new JDBCcon();
		try{

		String sql="";
		sql = "select id,mingc from gongysb order by mingc";
		_IMeikdqmcModel = new IDropDownModel(sql);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IMeikdqmcModel;
	}
	
//	�󱨱�����
	public boolean _Baoblxchange = false;
	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if(_BaoblxValue==null){
			_BaoblxValue=(IDropDownBean)getIBaoblxModels().getOption(0);
		}
		return _BaoblxValue;
	}

	public void setBaoblxValue(IDropDownBean Value) {
		long id = -2;
		if (_BaoblxValue != null) {
			id = _BaoblxValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Baoblxchange = true;
			} else {
				_Baoblxchange = false;
			}
		}
		_BaoblxValue = Value;
	}

	private IPropertySelectionModel _IBaoblxModel;

	public void setIBaoblxModel(IPropertySelectionModel value) {
		_IBaoblxModel = value;
	}

	public IPropertySelectionModel getIBaoblxModel() {
		if (_IBaoblxModel == null) {
			getIBaoblxModels();
		}
		return _IBaoblxModel;
	}

	public IPropertySelectionModel getIBaoblxModels() {
		JDBCcon con = new JDBCcon();
		try{
		List fahdwList = new ArrayList();
		fahdwList.add(new IDropDownBean(0,"�ֳ�����"));
		_IBaoblxModel = new IDropDownModel(fahdwList);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
	}
	
//	���
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
	public boolean nianfchanged = false;
	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2000; i <= DateUtil.getYear(new Date())+1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}
	
	/**
	 * �·�
	 */
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
	public boolean yuefchanged = false;
	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			yuefchanged = true;
		}
		_YuefValue = Value;
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


	public SortMode getSort() {
		return SortMode.USER;
	}
	
	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
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

	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}
	
//	***************************�����ʼ����***************************//
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
		
	public Date getYesterday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE,-1);
		return cal.getTime();
	}
	
	public Date getMonthFirstday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE,cal.getActualMinimum(Calendar.DATE));
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
	 //	ҳ���ж�����
    public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}

	
	
	

//	�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getTreeDiancmc(String diancmcId) {
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
	
	
	
	
	

}