package com.zhiren.jt.zdt.rulgl.rulrcmrz;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.zhiren.common.ResultSetList;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * ���ߣ�������
 * ʱ�䣺2010-01-29 
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */

public class Rulrcmrz extends BasePage {
	public boolean getRaw() {
		return true;
	}
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

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getHetltj();
	}
	//  
	private String getHetltj() {
		JDBCcon con = new JDBCcon();
		StringBuffer buffer = new StringBuffer("");

		
		buffer.append(
				"select decode(grouping(ruc.ruc_riq),1,'�ۼ�', to_char(ruc.ruc_riq,'yyyy-mm-dd')) as riq,\n" +
				"       rul.rul_banz,\n" + 
				"       sum(rul.rul_meil) as rulml,\n" + 
				"       sum(rul.zongml) as rulzl,\n" + 
				"       decode(sum(rul_meil),0,0,round(sum(rul_meil*rul.ru_lrl)/sum(rul_meil))) as rurl,\n" + 
				"       decode(sum(rul.zongml),0,0,round(sum(rul.zongml*rul.rul_jqrl)/sum(rul.zongml))) as rujqrl,\n" + 
				"       --rul.rul_jqrl,\n" + 
				"       sum(ruc.ruc_lml) as rucml,\n" + 
				"       --ruc.ruc_rl,\n" + 
				"       decode(sum(ruc_lml),0,0,round(sum(ruc_lml*ruc.ruc_rl)/sum(ruc_lml))) as rcrl,\n" + 
				"       decode(sum(rul.zongml),0,0,round(sum(rul.zongml*rul.rul_jqrl)/sum(rul.zongml))-\n" + 
				"       decode(sum(ruc_lml),0,0,round(sum(ruc_lml*ruc.ruc_rl)/sum(ruc_lml)))) as rezc,\n" + 
				"       --ruc.ruc_rl-rul.ru_lrl as rizc,\n" + 
				"       decode(sum(rul_meil),0,0,round(sum(rul_meil*rul.rul_star)/sum(rul_meil),2)) as rulstar,\n" + 
				"       decode(sum(rul.zongml),0,0,round(sum(rul.zongml*rul.rul_jqstar)/sum(rul.zongml),2)) as ruljqstar,\n" + 
				"       --rul.rul_star,\n" + 
				"       --rul.rul_jqstar,\n" + 
				"       --ruc.ruc_star,\n" + 
				"       decode(sum(ruc_lml),0,0,round(sum(ruc_lml*ruc.ruc_star)/sum(ruc_lml),2)) as rulstar,\n" + 
				"    '' as beiz \n" +
				"  from (select bz.xuh,\n" + 
				"               rlz.rulrq as rul_riq,\n" + 
				"               bz.mingc as rul_banz,\n" + 
				"               rlz.meil as rul_meil,\n" + 
				"               round(rlz.qnet_ar * 1000 / 4.1816, 0) as ru_lrl,\n" + 
				"               zml.zongml,\n" + 
				"               round(zml.ruljqrl / 0.0041816, 0) as rul_jqrl,\n" + 
				"               round(rlz.stad * (100 - rlz.mt) / (100 - rlz.mad), 2) as rul_star,\n" + 
				"               round(ruljqstar,2) as rul_jqstar\n" + 
				"          from rulmzlb rlz,\n" + 
				"               rulbzb bz,\n" + 
				"               (select rulrq,\n" + 
				"                       sum(meil) as zongml,\n" + 
				"                       decode(sum(meil),\n" + 
				"                              0,\n" + 
				"                              0,\n" + 
				"                              sum(meil * nvl(qnet_ar, 0)) / sum(meil)) as ruljqrl,\n" + 
				"                       decode(sum(meil),\n" + 
				"                              0,\n" + 
				"                              0,\n" + 
				"                              sum(meil * nvl(stad * (100 - mt) / (100 - mad), 0)) / sum(meil)) as ruljqstar\n" + 
				"                  from rulmzlb\n" + 
				"                 where rulrq between to_date('" + this.getRiq1() + "', 'yyyy-mm-dd')\n" + 
				"                             and to_date('" + this.getRiq2() + "', 'yyyy-mm-dd')\n" +
				"                 group by rulrq) zml\n" + 
				"         where rlz.rulrq between to_date('" + this.getRiq1() + "', 'yyyy-mm-dd')\n" + 
				"               and to_date('" + this.getRiq2() + "', 'yyyy-mm-dd')\n" + 
				"           and zml.rulrq = rlz.rulrq\n" + 
				"           and rlz.rulbzb_id = bz.id\n" + 
				") rul,\n" + 
				"\n" + 
				"       (select sum(fh.laimsl) as ruc_lml,\n" + 
				"               fh.daohrq as ruc_riq,\n" + 
				"               decode(sum(fh.laimsl),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round(sum(fh.laimsl * nvl(zl.qnet_ar, 0)) /\n" + 
				"                            sum(fh.laimsl) / 0.0041816,\n" + 
				"                            0)) as ruc_rl,\n" + 
				"\n" + 
				"               decode(sum(fh.laimsl),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round(sum(fh.laimsl * nvl(zl.star, 0)) / sum(fh.laimsl),\n" + 
				"                            2)) as ruc_star\n" + 
				"\n" + 
				"          from fahb fh, zhilb zl\n" + 
				"         where fh.zhilb_id = zl.id\n" + 
				"           and fh.daohrq between to_date('" + this.getRiq1() + "', 'yyyy-mm-dd')\n" + 
				"           and to_date('" + this.getRiq2() + "', 'yyyy-mm-dd')\n" + 
				"         group by fh.daohrq\n" + 
				"         ) ruc\n" + 
				" where rul.rul_riq = ruc.ruc_riq\n" + 
				"group by rollup(\n" +
				"      ruc.ruc_riq,\n" + 
				"      rul.xuh,\n" + 
				"      rul.rul_banz\n" + 
				"      )\n" + 
				"      having grouping(ruc.ruc_riq)+grouping(rul_banz)=0\n" + 
				"     or grouping(ruc.ruc_riq)+grouping(rul_banz)=2\n" + 
				"order by ruc.ruc_riq,rul.xuh"

		);

		ResultSet rs = con.getResultSet(buffer,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		ArrHeader = new String[1][13];
		ArrWidth = new int[] {80,100,60,70,70,110,90,100,90,90,90,90,60};
		ArrHeader[0] = new String[] { "����", "��¯ú����ʱ��", "��¯����<br>��","����¯����<br>��","��¯ú��ֵ<br>cal/kg","��¯ú��Ȩƽ����ֵ<br>cal/kg","�볧��ú��<br>��","�볧úƽ����ֵ<br>cal/kg","��ֵ��<br>cal/kg","��¯ú������<br>%","��¯ú����ƽ��<br>%","�볧ú����ƽ��<br>%","��ע"};
//		ArrHeader[1] = new String[] { "����", "��¯ú����ʱ��", "��","�ܶ�","cal/kg","kcal/kg","��","cal/kg","star","star","star","��ע"};
		rt.setBody(new Table(rs, 1, 0, 1));
		//
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.setTitle("�� ¯ �� �� ú �� ֵ", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 5, "�Ʊ�λ:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 8, "��λ:(��)" ,Table.ALIGN_RIGHT);
//		rt.setDefaultTitle(6, 2, "���:"+getNianfValue().getId(),Table.ALIGN_RIGHT);
//		rt.body.setPageRows(rt.PAPER_COLROWS);
		
		rt.body.setColFormat(4, "0");
		rt.body.setColFormat(7, "0.00");
		rt.body.setColFormat(10, "0.00");
		rt.body.setColFormat(11, "0.00");
		rt.body.setColFormat(12, "0.00");
		
		
		for (int i=1; i<=rt.body.getCols();i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		
//		rt.body.mergeCol(4);
//		rt.body.mergeCol(6);
//		rt.body.mergeCol(7);
//		rt.body.mergeCol(8);
//		rt.body.mergeCol(9);
//		rt.body.mergeCol(11);
//		rt.body.mergeCol(12);
		
		int sPos = 1;
		int ePos = 1;
		double dblRulzl = 0;
		double dblRuczl = 0;
		for (int i=1;i<=rt.body.getRows();) {
			sPos = i;
			ePos = i;
			for (int j=sPos;i<=rt.body.getRows();j++) {
				if (i==j) continue; 
				if (rt.body.getCell(i, 1).value.equals(rt.body.getCell(j, 1).value)) {
					ePos = j;				
				} else {
					i = ePos;
					if (sPos!=ePos) {
						rt.body.mergeCell(sPos, 4, ePos, 4);
						rt.body.mergeCell(sPos, 6, ePos, 6);
						rt.body.mergeCell(sPos, 7, ePos, 7);
						rt.body.mergeCell(sPos, 8, ePos, 8);
						rt.body.mergeCell(sPos, 9, ePos, 9);
						rt.body.mergeCell(sPos, 11, ePos, 11);
						rt.body.mergeCell(sPos, 12, ePos, 12);
						rt.body.mergeCell(sPos, 13, ePos, 13);
						dblRulzl = dblRulzl + ("".equals(rt.body.getCell(sPos, 4).value)? 0 :Double.parseDouble(rt.body.getCell(sPos, 4).value));
						dblRuczl = dblRuczl + ("".equals(rt.body.getCell(sPos, 7).value)? 0 :Double.parseDouble(rt.body.getCell(sPos, 7).value));
					}
					break;
				}
			}
			i++;
		}
		if ("�ۼ�".equals(rt.body.getCell(rt.body.getRows(),1).value)) {
			rt.body.setCellValue(rt.body.getRows(), 4, "" + CustomMaths.Round_New(dblRulzl, 2));
			rt.body.setCellValue(rt.body.getRows(), 7, "" + CustomMaths.Round_New(dblRuczl, 2));
		}
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.body.ShowZero=true;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 13, "��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();

	}
	

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}
	boolean riqichange = false;
	private String riq1;
	public String getRiq1() {
		if (riq1 == null || riq1.equals("")) {
			riq1 = DateUtil.FormatDate(new Date());
		}
		return riq1;
	}
	public void setRiq1(String riq) {
		if (this.riq1 != null && !this.riq1.equals(riq)) {
			this.riq1 = riq;
			riqichange = true;
		}
	}
	
	private String riq2;
	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}
	public void setRiq2(String riq) {
		if (this.riq2!= null && !this.riq2.equals(riq)) {
			this.riq2 = riq;
			riqichange = true;
		}
	}
	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("��¯����:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("RIQ1");
		tb1.addField(df);
		
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("RIQ2");
		tb1.addField(df1);
		ToolbarButton tb = new ToolbarButton(null, "ˢ��","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		setToolbar(tb1);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDefaultTree(null);
			setTreeid(null);
		}
		
		//begin��������г�ʼ������
		visit.setString1(null);

			String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
			if (pagewith != null) {

				visit.setString1(pagewith);
			}
		//	visit.setString1(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
		getToolbars();
		blnIsBegin = true;

	}
    // ��Ӧ��
	
    public IDropDownBean getGongysDropDownValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)getGongysDropDownModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean2();
    }
    public void setGongysDropDownValue(IDropDownBean Value) {
//    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean2()!=null){
//	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean2().getId()){
//	    		((Visit) getPage().getVisit()).setBooleanData1(true);
//	    	}else{
//	    		((Visit) getPage().getVisit()).setBooleanData1(false);
//	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean2(Value);
//    	}
    }
    public void setGongysDropDownModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public IPropertySelectionModel getGongysDropDownModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
            getGongysDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }
    public void getGongysDropDownModels() {
    	String sql="select id,mingc\n" +
    	"from gongysb\n" + 
    	"where gongysb.fuid=0";
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"ȫ��")) ;
        return ;
    }
    //���
	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit()).setDropDownBean3(getIDropDownBean(getNianfModel(),new Date().getYear()+1900));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}
	public void setNianfValue(IDropDownBean Value) {
//		if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean3()!=null){
//			if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean3().getId()){
//	    		((Visit) getPage().getVisit()).setBooleanData2(true);
//	    	}else{
//	    		((Visit) getPage().getVisit()).setBooleanData2(false);
//	    	}
			 ((Visit) getPage().getVisit()).setDropDownBean3(Value);
//		}
	}
	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
    public void getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = DateUtil.getYear(new Date())-2; i <= DateUtil.getYear(new Date())+2; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		 ((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(listNianf)) ;
	}
    private IDropDownBean getIDropDownBean(IPropertySelectionModel model,long id) {
        int OprionCount;
        OprionCount = model.getOptionCount();
        for (int i = 0; i < OprionCount; i++) {
            if (((IDropDownBean) model.getOption(i)).getId() == id) {
                return (IDropDownBean) model.getOption(i);
            }
        }
        return null;
    }
    //����
    public IDropDownBean getLeixSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getLeixSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean4();
    }
    public void setLeixSelectValue(IDropDownBean Value) {
//    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean4()!=null){
//	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean4().getId()){
//	    		((Visit) getPage().getVisit()).setBooleanData1(true);
//	    	}else{
//	    		((Visit) getPage().getVisit()).setBooleanData1(false);
//	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean4(Value);
//    	}
    }
    public void setLeixSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel4(value);
    }

    public IPropertySelectionModel getLeixSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
            getLeixSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }
    public void getLeixSelectModels() {
        List list=new ArrayList();
        list.add(new IDropDownBean(1,"�ֳ��ֿ�"));
        list.add(new IDropDownBean(2,"�ֿ�ֳ�"));
        list.add(new IDropDownBean(3,"�ֳ�"));
        list.add(new IDropDownBean(4,"�ֿ�"));
        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(list)) ;
        return ;
    }

    private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
    public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	�糧����
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}
	
	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}
	
	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		 setDiancmcModel(new IDropDownModel(sql)) ;
		 return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

		public void setDiancmcModel(IPropertySelectionModel _value) {
			((Visit)getPage().getVisit()).setProSelectionModel1(_value);
		}
	//
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}
