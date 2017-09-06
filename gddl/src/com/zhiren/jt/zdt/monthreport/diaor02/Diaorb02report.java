package com.zhiren.jt.zdt.monthreport.diaor02;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;

import org.apache.tapestry.contrib.palette.SortMode;
     
/* 
* ʱ�䣺2009-06-19
* ���ߣ� sy
* �޸����ݣ��޸�sql����ͷ���������е糧��ȡ������gongysb�Ĺ���
*			
*/ 

/* 
* ʱ�䣺2009-06-26
* ���ߣ� sy
* �޸����ݣ���ͷ�����������������ֵ�ĵ糧��������û�����鲻�����µ����ݣ������ۼƲ��ۼƱ�������
*                    
*			
*/ 
/* 
* ʱ�䣺2009-07-21
* ���ߣ� ll
* �޸����ݣ�1���޸ı�����⡣
* 		   
*/ 
/* 
* ʱ�䣺2009-07-22
* ���ߣ� sy
* �޸����ݣ�ȥ������״̬������������01��02��һ�¡�
* 		   �ж�xitxxb�й�������������ʾ���С������֡������ȵ糧
*/ 
public class Diaorb02report  extends BasePage implements PageValidateListener{
	
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
	
	//��ʼ����
	private Date _BeginriqValue = new Date();
//	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue = new Date();
		}
		return _BeginriqValue;
	}
	
	public void setBeginriqDate(Date _value) {
		if (_BeginriqValue.equals(_value)) {
//			_BeginriqChange=false;
		} else {
			_BeginriqValue = _value;
//			_BeginriqChange=true;
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
			this.setTreeid(null);
			this.getTree();
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean11(null);
			visit.setProSelectionModel11(null);
			isBegin=true;
			
		}
	
	
		getToolBars() ;
		Refurbish();
	}
	
	private String RT_HET="Fadbmdjqkbreport";//�����ú���۱�
	private String mstrReportName="Fadbmdjqkbreport";
	
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
		
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		String zhuangt="";
			if(visit.getRenyjb()==3){
				zhuangt="";
			}else if(visit.getRenyjb()==2){
				zhuangt=" and (y.zhuangt=1 or y.zhuangt=2)";
			}else if(visit.getRenyjb()==1){
				zhuangt=" and y.zhuangt=2";
			}
		long intyear;
		if (getNianfValue() == null){
			intyear=DateUtil.getYear(new Date());
		}else{
			intyear=getNianfValue().getId();
		}
		long intMonth;
		if(getYuefValue() == null){
			intMonth = DateUtil.getMonth(new Date());
		}else{
			intMonth = getYuefValue().getId();
		}

		String strGongsID = "";
		String  notHuiz="";
		String guoltj="1=1";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";
			guoltj="  max(dc.id) not in("+Guoldcid()+")\n";
			
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
//			strGongsID = "  and dc.fuid=  " +this.getTreeid();
			strGongsID = "  and (dc.fuid= "+this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
			notHuiz=" and not grouping(f.mingc)=1 ";//���糧���Ƿֹ�˾ʱ,ȥ�����Ż���
			guoltj=" max(dc.id) not in("+Guoldcid()+")\n";
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();
			notHuiz=" and not  grouping(dc.mingc)=1";//���糧���ǵ糧ʱ,ȥ���ֹ�˾�ͼ��Ż���
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
			guoltj="";
		}
		
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		
		int iFixedRows=0;//�̶��к�
		int iCol=0;//����
		
		
		String strt="";
		String strw="";
		//��������
		if(getBaoblxValue().getValue().equals("�ֳ�")){		
			strt="select decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ϼ�',1,'&nbsp;&nbsp;'||dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as mingc \n";
			strw="group by rollup(dc.fgsmc,dc.mingc) \n"
				+"order by grouping(dc.fgsmc)desc,min(dc.fgsxh),grouping(dc.mingc)desc,min(dc.xuh) \n";
		}else if(getBaoblxValue().getValue().equals("�ֿ�")){
			strt="select decode(grouping(dc.fgsmc)+grouping(gy.dqmc),2,'�ϼ�',1,'&nbsp;&nbsp;'||dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||gy.dqmc) as mingc \n";
			strw="group by rollup(dc.fgsmc,(gy.dqmc,dc.fgsid,gy.dqid)) \n"
				+"order by grouping(dc.fgsmc)desc,min(dc.fgsxh),grouping(gy.dqmc)desc,min(gy.xuh)";
		}else if(getBaoblxValue().getValue().equals("�ֳ��ֿ�")){
			strt="select decode(grouping(dc.fgsmc)+grouping(dc.mingc)+grouping(gy.dqmc),3,'�ܼ�',2,dc.fgsmc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||gy.dqmc) as mingc \n";
			strw="group by rollup(dc.fgsmc,dc.mingc,gy.dqmc)  \n"
				+"order by grouping(dc.fgsmc) desc,max(dc.fgsxh) ,dc.fgsmc, grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc, grouping(gy.dqmc) desc,max(gy.dqxh) ,gy.dqmc";
		}else if(getBaoblxValue().getValue().equals("�ֿ�ֳ�")){
			strt="select decode(grouping(dc.fgsmc)+grouping(dc.mingc)+grouping(gy.dqmc),3,'�ܼ�',2,gy.dqmc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as mingc \n";
			strw="group by rollup(gy.dqmc,dc.fgsmc,dc.mingc)  \n"
				+"order by  grouping(gy.dqmc) desc,max(gy.dqxh) ,gy.dqmc,grouping(dc.fgsmc) desc,max(dc.fgsxh) ,dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc ";
		} 
				strSQL=
					strt+
					",sum(byjh.laimsl) as jihlml_by\n" +
					"         ,sum(ljjh.laimsl) as jihlml_lj\n" + 
					"         ,sum(byss.laimsl) as shislml_by\n" + 
					"         ,sum(ljss.laimsl) as shislml_lj\n" + 
					"         ,sum(byss.laimsl)-sum(byjh.laimsl) as kuiq_by\n" + 
					"         ,sum(ljss.laimsl)-sum(ljjh.laimsl) as kuiq_lj\n" + 
					"          ,DECODE(sum(byjh.laimsl),0,0,round(sum(byss.laimsl)/sum(byjh.laimsl),2))*100 as daohl_by\n" + 
					"          ,DECODE(sum(ljjh.laimsl),0,0,round(sum(ljss.laimsl)/sum(ljjh.laimsl),2))*100 as daohl_by\n" + 
					"from vwdianc dc,vwgongys gy,\n" + 
					"( select dc.id as diancxxb_id,cg.gongysb_id\n"+
				      "      from yuecgjhb cg ,vwdianc  dc where cg.riq>=to_date('"+intyear+"-01-01','yyyy-mm-dd')\n"+
				       "     and cg.riq<=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')  and dc.id=cg.diancxxb_id(+)\n"+
				    " union\n "+
				    " (select dc.id as diancxxb_id,tj.gongysb_id as gongysb_id from yueslb sl,yuetjkjb tj,vwdianc dc\n"+
				    "        where sl.yuetjkjb_id=tj.id  and sl.fenx='����'  and dc.id=tj.diancxxb_id(+) \n" +
				    "and tj.riq>=to_date('"+intyear+"-01-01','yyyy-mm-dd')  and tj.riq<=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')) )a,\n" +
					"     (select  cg.diancxxb_id as diancxxb_id,cg. gongysb_id as gongysb_id,sum(cg.yuejhcgl) as laimsl \n" +
					" from yuecgjhb cg,diancxxb dc,gongysb gy  where cg.diancxxb_id=dc.id and cg.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" +
					"and cg.gongysb_id=gy.id    group by (cg.diancxxb_id,cg. gongysb_id))byjh,\n" +

					"        (select cg.diancxxb_id as diancxxb_id,cg. gongysb_id as gongysb_id,sum(cg.yuejhcgl) as laimsl from yuecgjhb cg,diancxxb dc,gongysb gy\n" + 
					"         where cg.diancxxb_id=dc.id and cg.gongysb_id=gy.id "+strGongsID+"  \n" + 
					"              and cg.riq>=to_date('"+intyear+"-01-01','yyyy-mm-dd') and cg.riq<=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + 
					"        group by (cg.diancxxb_id,cg. gongysb_id) \n" + 
					"       )ljjh,\n" + 
					"       (select tj.diancxxb_id as diancxxb_id,tj.gongysb_id as gongysb_id,sum(sl.laimsl) as laimsl  from yueslb sl,yuetjkjb tj,diancxxb dc,gongysb gy\n" + 
					"        where sl.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and tj.gongysb_id=gy.id "+strGongsID+"  \n" + 
					"             and sl.fenx='����' and tj.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + 
					"      group by (tj.diancxxb_id,tj.gongysb_id) \n" + 
					"       )byss,\n" + 
					"    (  select tj.diancxxb_id as diancxxb_id,tj.gongysb_id as gongysb_id,sum(sl.laimsl) as laimsl  from yueslb sl,yuetjkjb tj,diancxxb dc,gongysb gy\n" + 
					"       where sl.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and tj.gongysb_id=gy.id "+strGongsID+" \n" + 
					"              and sl.fenx='����' and tj.riq>=to_date('"+intyear+"-01-01','yyyy-mm-dd') and tj.riq<=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + 
					"      group by (tj.diancxxb_id,tj.gongysb_id) \n" + 
					"       )ljss\n" + 
					"  where dc.id=a.diancxxb_id and gy.id=a.gongysb_id" +
					"  and a.diancxxb_id= byjh.diancxxb_id(+) and a.gongysb_id=byjh.gongysb_id(+)\n" +
					"and a.diancxxb_id=byss.diancxxb_id(+)  and a.gongysb_id=byss.gongysb_id(+)\n" +
					"and a.diancxxb_id=ljjh.diancxxb_id(+)  and a.gongysb_id=ljjh.gongysb_id(+)\n" +
					" and a.diancxxb_id=ljss.diancxxb_id(+)  and a.gongysb_id=ljss.gongysb_id(+)\n" +

					"having  "+guoltj+"\n"

                    +strw;	
				
				 ArrHeader =new String[2][9];
				 ArrHeader[0]=new String[] {"��λ","�ƻ�(��)","�ƻ�(��)","ʵ��(��)","ʵ��(��)","��Ƿ(��)","��Ƿ(��)","������%","������%"};
				 ArrHeader[1]=new String[] {"��λ","����","�ۼ�","����","�ۼ�","����","�ۼ�","����","�ۼ�"};

				 ArrWidth =new int[] {150,80,80,80,80,80,80,80,80};

				 
			//System.out.println(strSQL);
			ResultSet rs = cn.getResultSet(strSQL);
			 
			// ����
			
			Table tb = new Table(rs,2, 0, 1);
			rt.setBody(tb);
			
			rt.setTitle(getBiaotmc()+intyear+"��"+intMonth+"�µ�ȼ02��", ArrWidth);
			rt.setDefaultTitle(1, 2, "���λ(����):"+((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(3, 2, "�����:"+intyear+"��"+intMonth+"��", Table.ALIGN_LEFT);
			rt.setDefaultTitle(6, 2, "��λ:��", Table.ALIGN_RIGHT);
			rt.setDefaultTitle(8, 2, "��ȼ02��", Table.ALIGN_RIGHT);
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(18);
			rt.body.setHeaderData(ArrHeader);// ��ͷ����
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.ShowZero = false;

			tb.setColAlign(2, Table.ALIGN_CENTER);
			if(rt.body.getRows()>2){
				rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
			}
			
			//ҳ�� 
			 rt.createDefautlFooter(ArrWidth);
			  rt.setDefautlFooter(1,3,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			  rt.setDefautlFooter(4,2,"���:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(7,2,"�Ʊ�:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
			 
		
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();

			return rt.getAllPagesHtml();
	}
	
	//�õ�ϵͳ��Ϣ�������õı������ĵ�λ����
	public String getBiaotmc(){
		String biaotmc="";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc="select  zhi from xitxxb where mingc='������ⵥλ����'";
		ResultSet rs=cn.getResultSet(sql_biaotmc);
		try {
			while(rs.next()){
				 biaotmc=rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}
			
		return biaotmc;
		
	}
//	��ѯ�Ƿ����ù��˵糧id
	private String  Guoldcid(){
		JDBCcon con=new JDBCcon();
		String dcid="";
		ResultSetList rsl=con.getResultSetList("select zhi from xitxxb where mingc='�����Ϻ������ַ��硢�Ϻ������ȵ硢�Ϻ���������'\n");
		
		 while(rsl.next()){
			 dcid=rsl.getString("zhi");
		 }
	    con.Close();
			
		return dcid;
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
			int _nianf = DateUtil.getYear(new Date());
            int _yuef = DateUtil.getMonth(new Date());
            if (_yuef == 1) {
                _nianf = _nianf - 1;
            }
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
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
			 int _yuef = DateUtil.getMonth(new Date());
		        if (_yuef == 1) {
		            _yuef = 12;
		        } else {
		            _yuef = _yuef - 1;
		        }
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
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

//	 ��������
	public boolean _Baoblxchange = false;
	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean11()==null){
			((Visit)getPage().getVisit()).setDropDownBean11((IDropDownBean)getIBaoblxModels().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean11();
	}

	public void setBaoblxValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit)getPage().getVisit()).getDropDownBean11() != null) {
			id = ((Visit)getPage().getVisit()).getDropDownBean11().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Baoblxchange = true;
			} else {
				_Baoblxchange = false;
			}
		}
		((Visit)getPage().getVisit()).setDropDownBean11(Value);
	}

	private IPropertySelectionModel _IBaoblxModel;

	public void setIBaoblxModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel11(value);
	}

	public IPropertySelectionModel getIBaoblxModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel11() == null) {
			getIBaoblxModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel11();
	}

	public IPropertySelectionModel getIBaoblxModels() {
		
		List List = new ArrayList();
		List.add(new IDropDownBean(0,"�ֳ�"));
		List.add(new IDropDownBean(1,"�ֿ�"));
		List.add(new IDropDownBean(2,"�ֳ��ֿ�"));
		List.add(new IDropDownBean(3,"�ֿ�ֳ�"));
		((Visit)getPage().getVisit()).setProSelectionModel11(new IDropDownModel(List));
		
		return ((Visit)getPage().getVisit()).getProSelectionModel11();
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

	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		
		
		tb1.addText(new ToolbarText("���:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		
		

		tb1.addText(new ToolbarText("�·�:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
		
		
		
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("ͳ�ƿھ�:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("BAOBLX");
		cb.setWidth(80);
		cb.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(cb);
		
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
		
		
	}
	
	
	
	
	
	
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

	private String treeid;

	/*public String getTreeid() {
		if (treeid == null || "".equals(treeid)) {
			return "-1";
		}
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

	
}