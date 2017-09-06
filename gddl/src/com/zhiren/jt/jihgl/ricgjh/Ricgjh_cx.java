package com.zhiren.jt.jihgl.ricgjh;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Ricgjh_cx extends BasePage {
	
	/**
	 * ����:���ܱ�
	 * ʱ��:2010-1-19 9:16:28
	 * ����:�չ�ú�ƻ�����
	 */
	private static final String BAOBPZB_GUANJZ = "JILTZ_GJZ";// baobpzb�ж�Ӧ�Ĺؼ���
//	�����û���ʾ
	private String msg="";

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
	
	private int _CurrentPage = -1;
	public int getCurrentPage() {
		return _CurrentPage;
	}
	public void setCurrentPage(int _value) {
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	public int getAllPages() {
		return _AllPages;
	}
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	

	
//	������
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}


//	ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	public boolean getRaw() {
		return true;
	}
//	����������
	public IDropDownBean getChangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getChangbModel().getOptionCount()>0) {
				setChangbValue((IDropDownBean)getChangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setChangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getChangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setChangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setChangbModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if(visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id());
		}else {
			sb.append("select id,mingc from diancxxb where id="+visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}

    




	
	
//	��ȡ������
	public String getRptTitle() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sb=visit.getDiancqc()+"ȼ�����յ�";
		
		return sb;
	}
	
//	ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("����:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		//dfb.setId("guohrqb");
		tb1.addField(dfb);
		
		tb1.addText(new ToolbarText("-"));
		
		
		//��Ӧ��������
		tb1.addText(new ToolbarText("��ú��λ:"));
		ComboBox CB_GONGYS = new ComboBox();
		CB_GONGYS.setTransform("GONGYS");
		CB_GONGYS.setWidth(120);
		CB_GONGYS.setListeners("select:function(){document.Form0.submit();}");
		CB_GONGYS.setEditable(true);
		tb1.addField(CB_GONGYS);
		tb1.addText(new ToolbarText("-"));
		
	

		
//		��������
		tb1.addText(new ToolbarText("жú�ص�:"));
		tb1.addText(new ToolbarText("-"));
		ComboBox hengh = new ComboBox();
		hengh.setTransform("HENGH");
		hengh.setWidth(90);
		hengh.setListeners("select:function(){document.Form0.submit();}");

		tb1.addField(hengh);
		
//		�糧Tree
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid_dc() == null || "".equals(getTreeid_dc()) ? "-1"
						: getTreeid_dc())));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		
		
		

		
		if(visit.isFencb()){
			tb1.addText(new ToolbarText("�糧:"));
			tb1.addField(tf1);
			tb1.addItem(tb3);
			tb1.addText(new ToolbarText("-"));
		}
		
		
		
		
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		

		
		
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	
	
	public String getPrintTable(){
		
		
			return getMeikmx()+getXithz();
		
	}
	
	
	
	
	
	
	
	
	public String getMeikmx(){



		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
		Visit v = (Visit) getPage().getVisit();
		ResultSetList rs=null;
		String[] strFormat=null;
		
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		
		
		long meik=this.getGongysValue().getId();
		String meiktiaoj="";
		if(meik==-1){
			meiktiaoj="";
		}else{
			meiktiaoj=" and g.id= "+meik+"";
		}
		
		
		long hengh=this.getHenghValue().getId();
		String henghTiaoj="";
		if(hengh==0){
			henghTiaoj="";
		}else if(hengh==1){
			henghTiaoj=" and mc.mingc='Aϵͳ'";
		}else if(hengh==2){
			henghTiaoj=" and mc.mingc='Bϵͳ'";
		}else {
			henghTiaoj="";
		}
		
		
	
		Report rt = new Report(); //������
	
			sbsql.setLength(0);
					sbsql.append(

						
							

							"select  decode(ys.mingc,null,'�ܼ�',ys.mingc) as yunsfsb_id,\n" +
							"        decode(grouping(ys.mingc)+grouping(g.mingc),2,'',1,'�ϼ�',g.mingc) as gongysb_id,\n" + 
							"        decode(grouping(ys.mingc)+grouping(g.mingc)+grouping(mk.mingc),3,'',2,'',1,'С��',mk.mingc) as meikmc,\n" + 
							"        decode(grouping(mk.mingc),1,'',mc.mingc) as meicb_id,\n" + 
							"        decode(grouping(mk.mingc),1,'',fl.mingc) as xiemflb_id,\n" + 
							"          decode(grouping(mk.mingc),1,'',max(r.did) ) as did,\n" + 
							"        sum(r.jihl) as jihl,\n" + 
							"        round_new(sum(r.yunj1)/count(*),2) as yunj1,\n" + 
							"        round_new(sum(r.yunj2)/count(*),2) as yunj2,\n" + 
							"        decode(sum(r.jihl),0,0,round_new(sum(nvl(r.rez,0)*r.jihl)/sum(r.jihl),0)) as rez,\n" + 
							"        decode(sum(r.jihl),0,0,round_new(sum(nvl(r.liuf,0)*r.jihl)/sum(r.jihl),2)) as liuf\n" + 
							"from rijhb r,diancxxb dc ,gongysb g ,meikxxb mk,meicb mc,xiemflb fl,yunsfsb ys\n" + 
							"where r.diancxxb_id=dc.id\n" + 
							"and r.gongysb_id=g.id\n" + 
							"and r.meikxxb_id=mk.id\n" + 
							"and r.riq=to_date('"+kais+"','yyyy-mm-dd')\n" + 
							""+meiktiaoj+"\n"+
							""+henghTiaoj+"\n"+
							"and r.meicb_id=mc.id(+)\n" + 
							"and r.xiemflb_id=fl.id(+)\n" + 
							"and r.yunsfsb_id=ys.id\n" + 
							"        group by rollup (ys.mingc,g.mingc,mk.mingc,mc.mingc,fl.mingc)\n" + 
							"        having not (\n" + 
							"        (grouping(ys.mingc)+grouping(g.mingc)+grouping(mk.mingc)+grouping(mc.mingc)+grouping(fl.mingc)=2)\n" + 
							"        or  (grouping(ys.mingc)+grouping(g.mingc)+grouping(mk.mingc)+grouping(mc.mingc)+grouping(fl.mingc)=1)\n" + 
							"        )\n" + 
							"        order by ys.mingc,g.mingc,mk.mingc,mc.mingc,fl.mingc"
	
							
							
							
							
							
							


					);
					rs=con.getResultSetList(sbsql.toString());
					
					
					String ArrHeader[][]=new String[2][11];
		//			1120
					ArrHeader[0]=new String[] {"���䷽ʽ","��ú��λ","����","жú<br>�ص�","жú����","����<br>�ص�","�ƻ���(��)","�˾�(Km)","�˾�(Km)","ú��Ԥ��","ú��Ԥ��"};
					ArrHeader[1]=new String[] {"���䷽ʽ","��ú��λ","����","жú<br>�ص�","жú����","����<br>�ص�","�ƻ���(��)","һ��","����","��λ��ֵ<br>(��/��)","���(%)"};
					
					int ArrWidth[]=new int[] {55,100,90,50,50,55,60,50,50,55,55};
					  strFormat = new String[] {"", "","",
			    				"", "", "", "",  "","" ,"",""};
					// ����
					rt.setTitle(getBRiq()+"��ú�ƻ�",ArrWidth);
					rt.setDefaultTitle(1, 3, "�Ʊ�λ:"+v.getDiancmc(), Table.ALIGN_LEFT);
					rt.setDefaultTitle(10, 2, "��λ:��", Table.ALIGN_RIGHT);
					rt.setBody(new Table(rs, 2, 0, 2));
					rt.body.mergeFixedCols();
					
					rt.body.setColFormat(strFormat);
//					�趨С���еı���ɫ������
					for (int i=3;i<=rt.body.getRows();i++){
						String xiaoj=rt.body.getCellValue(i, 3);
						if(xiaoj.equals("")||xiaoj.equals("С��")){
							//rt.body.setCellValue(i, 10, "");
							for (int j=0;j<rt.body.getCols()+1;j++){
								//rt.body.getCell(i, j).backColor="silver";
								rt.body.getCell(i, j).fontBold=true;
							}
						}
					}
					
					
					rt.body.setWidth(ArrWidth);
					rt.body.setPageRows(100);
					rt.body.setHeaderData(ArrHeader);// ��ͷ����
					rt.createFooter(1, ArrWidth);
//					rt.setDefautlFooter(1, 3, "��ӡ����:"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
//					rt.setDefautlFooter(4, 2, "�Ʊ�:", Table.ALIGN_LEFT);
//					rt.setDefautlFooter(7, 3, "���:", Table.ALIGN_LEFT);
					rt.body.ShowZero = true;
			
		
		
		
		
		
			con.Close();
			for (int i=1;i<=rt.body.getCols();i++){
				rt.body.setColAlign(i, Table.ALIGN_CENTER);
			}
			
			
		
		
			if(rt.body.getPages()>0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(21);
			rt.body.mergeFixedRow();
	     	return rt.getAllPagesHtml();// ph;
	
	
		
	
		
		
	}
	
	
	
	private String getXithz(){




		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
		Visit v = (Visit) getPage().getVisit();
		ResultSetList rs=null;
		String[] strFormat=null;
		
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		
	
		
	
		Report rt = new Report(); //������
	
			sbsql.setLength(0);
					sbsql.append(

						
							


							"select  '��·��úú��Ԥ��' as gongysb_id,mc.mingc,\n" +
							"decode(xm.mingc,null,'С��',xm.mingc) as mingc,\n" + 
							"sum(r.jihl) as jihl,'','','','','',\n" + 
							"round(sum(r.jihl*r.rez)/sum(r.jihl),0) as rez,\n" + 
							"round(sum(r.jihl*r.liuf)/sum(r.jihl),2) as liuf\n" + 
							" from rijhb r ,meicb mc,xiemflb xm\n" + 
							"where r.riq=to_date('"+kais+"','yyyy-mm-dd')\n" + 
							"and r.yunsfsb_id=2\n" + 
							"and r.meicb_id=mc.id\n" + 
							"and r.xiemflb_id=xm.id\n" + 
							"group by rollup (mc.mingc,xm.mingc)\n" + 
							"having not (grouping(mc.mingc)+grouping(xm.mingc)=2)\n" + 
							"order by grouping(mc.mingc),mc.mingc,grouping (xm.mingc) desc ,xm.mingc"

							
							


					);
					rs=con.getResultSetList(sbsql.toString());
					
					
					String ArrHeader[][]=new String[1][11];
		//			1120
					ArrHeader[0]=new String[] {"ϵͳ","","λ��","Ԥ����"," ",""," ",""," ","��Ȩ��ֵ","��Ȩ���"};
					
					int ArrWidth[]=new int[] {55,100,90,50,50,55,60,50,50,55,55};
					  strFormat = new String[] {"", "","",
			    				"", "", "", "",  "","","",""};

         				rt.setBody(new Table(rs, 1, 0, 2));
					rt.body.mergeFixedCols();
					
					rt.body.setColFormat(strFormat);

					
					rt.body.setWidth(ArrWidth);
					rt.body.setPageRows(100);
					rt.body.setHeaderData(ArrHeader);// ��ͷ����
					rt.createFooter(1, ArrWidth);
					rt.setDefautlFooter(1, 3, "��ӡ����:"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
					rt.setDefautlFooter(4, 2, "�Ʊ�:", Table.ALIGN_LEFT);
					rt.setDefautlFooter(7, 2, "���:", Table.ALIGN_LEFT);
					rt.body.ShowZero = true;
			
		
//					�趨С���еı���ɫ������
					for (int i=2;i<=rt.body.getRows();i++){
						String xiaoj=rt.body.getCellValue(i, 3);
						if(xiaoj.equals("С��")){
							//rt.body.setCellValue(i, 10, "");
							for (int j=3;j<rt.body.getCols()+1;j++){
							//	rt.body.getCell(i, j).backColor="silver";
								rt.body.getCell(i, j).fontBold=true;
							}
						}
					}
		
		
		
			con.Close();
			for (int i=1;i<=rt.body.getCols();i++){
				rt.body.setColAlign(i, Table.ALIGN_CENTER);
			}
			
			
		
		
			if(rt.body.getPages()>0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(21);
			rt.body.mergeFixedRow();
	     	return rt.getAllPagesHtml();// ph;
	
	
		
	
		
		
	
	}
	

	
//	-------------------------�糧Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	DefaultTree dc ;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc= etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}
	
//	-------------------------�糧Tree END-------------------------------------------------------------
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
		return getToolbar().getRenderScript();
	}
//	ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			
			
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean8(null);
			visit.setProSelectionModel8(null);
			setChangbValue(null);
			setChangbModel(null);
//			visit.setDefaultTree(null);
//			setDiancmcModel(null);
			getDiancmcModels();
			setTreeid_dc(visit.getDiancxxb_id() + "");
			getSelectData();
		}
	}
	
//	��ť�ļ����¼�
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

//	����ť�ύ����
	public void submit(IRequestCycle cycle) {
		
		if (_RefurbishChick) {
			_RefurbishChick = false;
			
			getSelectData();
		}
	}
//	ҳ���½��֤
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
	
	

	

	
	
//	 жú�ص�
	public IDropDownBean getHenghValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean8((IDropDownBean) getHenghModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setHenghValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean8(Value);
	}

	public void setHenghModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public IPropertySelectionModel getHenghModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
			getHenghModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public void getHenghModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, "ȫ��"));
		list.add(new IDropDownBean(1, "Aϵͳ"));
		list.add(new IDropDownBean(2, "Bϵͳ"));
		
		((Visit) getPage().getVisit()).setProSelectionModel8(new IDropDownModel(list));
		return;
	}
	
	
//	 ��Ӧ��������
	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setGongysValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setGongysModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getGongysModels() {
		
		String sql_gongys = "select id,mingc  from gongysb  where leix=1 order by mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql_gongys,"ȫ��"));
		return;
	}
	
	
}
