package com.zhiren.dc.report;

import java.awt.Color;
import java.awt.GradientPaint;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.data.category.CategoryDataset;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Chart;
import com.zhiren.report.ChartData;

/*
 * ʱ�䣺2013��06��29��
 * ���ߣ����
 * �޸����ݣ������ӷ��۱�ú����  ���ӡ��ۼ�ֵ������״ͼ
 *          �޸�sql������GETZAFLJJE��RIQ,DIANCXXB_ID������
 * 
 */
public class Yuedzf_char  extends BasePage implements PageValidateListener {

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
	}
		
	private boolean _QueryClick = false;
	
	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}
	
	public void submit(IRequestCycle cyele){
		if(_QueryClick){
			_QueryClick = false;
			this.getPrintChar();
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
	
	public boolean getRaw() {
		return true;
	}
	
	//��ͼ
	public String getPrintChar(){
		Visit visit = (Visit)this.getPage().getVisit();
		/*String Sql=
			"SELECT TO_CHAR(ZB.RIQ, 'mm') || '��' RIQ,\n" +
			"       NVL('�ӷ��۱�ú���ۣ�Ԫ/�֣�', '') LEIX,\n" + 
			"       ROUND(DECODE(NVL(ZB.BZML, 0), 0, 0, ZF.JINE / ZB.BZML), 2) ZHI\n" + 
			"  FROM (SELECT RIQ, SUM(RULMZBZML + RULYZBZML + RULQZBZML) BZML\n" + 
			"          FROM YUEZBB\n" + 
			"         WHERE TRUNC(RIQ, 'yyyy') = DATE '"+getYearValue().getValue()+"-01-01'\n" + 
			"           AND FENX = '����'\n" + 
			"           AND DIANCXXB_ID = "+visit.getDiancxxb_id()+"\n" + 
			"         GROUP BY RIQ) ZB,\n" + 
			"       (SELECT RIQ, SUM(JINE) JINE\n" + 
			"          FROM ZAFB\n" + 
			"         WHERE TRUNC(RIQ, 'yyyy') = DATE '"+getYearValue().getValue()+"-01-01'\n" + 
			"           AND DIANCXXB_ID = "+visit.getDiancxxb_id()+"\n" + 
			"         GROUP BY RIQ) ZF\n" + 
			" WHERE ZF.RIQ(+) = ZB.RIQ\n" + 
			" ORDER BY ZB.RIQ";*/

		String Sql = 
			"SELECT TO_CHAR(ZB.RIQ, 'mm') || '��' RIQ,\n" +
			"\n" + 
			"ZB.FENX LEIX,\n" + 
			"ROUND(DECODE(NVL(ZB.BZML, 0), 0, 0, ZF.JINE / ZB.BZML), 2) ZHI\n" + 
			"FROM (SELECT RIQ, SUM(RULMZBZML + RULYZBZML + RULQZBZML) BZML, FENX\n" + 
			"FROM YUEZBB\n" + 
			"WHERE TRUNC(RIQ, 'yyyy') = DATE '"+getYearValue().getValue()+"-01-01'\n" + 
			"\n" + 
			"AND DIANCXXB_ID = "+visit.getDiancxxb_id()+"\n" +
			"GROUP BY RIQ, FENX) ZB,\n" + 
			"\n" + 
			"(SELECT RIQ, SUM(JINE) JINE, NVL('����', '') FENX\n" + 
			"FROM ZAFB\n" + 
			"WHERE TRUNC(RIQ, 'yyyy') = DATE '"+getYearValue().getValue()+"-01-01'\n" + 
			"AND DIANCXXB_ID = "+visit.getDiancxxb_id()+"\n" +
			"GROUP BY RIQ\n" + 
			"UNION ALL\n" + 
			"SELECT RIQ, GETZAFLJJE(RIQ, "+visit.getDiancxxb_id()+") JINE, NVL('�ۼ�', '') FENX\n" + 
			"FROM ZAFB\n" + 
			"WHERE TRUNC(RIQ, 'yyyy') = DATE '"+getYearValue().getValue()+"-01-01'\n" + 
			"AND DIANCXXB_ID = "+visit.getDiancxxb_id()+"\n" + 
			"GROUP BY RIQ) ZF\n" + 
			"WHERE ZF.RIQ(+) = ZB.RIQ\n" + 
			"AND ZF.FENX(+) = ZB.FENX\n" + 
			"ORDER BY ZB.RIQ, ZB.FENX";

		JDBCcon con = new JDBCcon();
		ResultSetList rstmp=con.getResultSetList(Sql);
		
		
		//��������ͼ	
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//ͼƬ������ɫ
		CategoryDataset dataset = cd.getRsDataChart(rstmp, "RIQ","LEIX","ZHI");//rs��¼����������ͼƬ��Ҫ������
		ct.intDigits=0;				//	��ʾС��λ��
		ct.barItemMargin=-0.05;
		ct.chartBackgroundPaint=gp;
		ct.barItemMargin=-0.05;
		ct.barLabelsFontbln = true;
		ct.showXvalue = true;
		ct.showYvalue = true;
//		��б��ʾX�������
		ct.xTiltShow = true;		
		ct.showLegend = true;
//		���ò���������BAR����ʾ���ݵ���ʾλ��
		ct.barfontPlace=ItemLabelAnchor.OUTSIDE1;
//		������������BAR����ʾ���ݵ���ʾλ��
		ct.barfontPlaceNormal=ItemLabelAnchor.OUTSIDE1;		
//		����Bar�������
		ct.MaximumBarWidth=0.05;
//		�������ݾ������Ӷ��˵ľ���
		ct.outSide=20;
//		�������ֵ���б�ȡ�0:ˮƽ
		ct.barfontTilt=0;
		
		return ct.ChartBar3D(getPage(), dataset,  getYearValue().getValue()+"�곧���ӷ��۱�ú����",  800,  400);
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
	
	
	  //���������
	  public IDropDownBean getYearValue() {
	    if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
	      ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getYearModel().getOption(DateUtil.getYear(new Date())-2008));
	    }
	    return ((Visit) getPage().getVisit()).getDropDownBean4();
	  }
	  public void setYearValue(IDropDownBean Value) {
	    if(Value!=((Visit) getPage().getVisit()).getDropDownBean4()){
	      ((Visit) getPage().getVisit()).setDropDownBean4(Value);
	    }
	  }
	  public void setYearModel(IPropertySelectionModel value) {
	    ((Visit) getPage().getVisit()).setProSelectionModel4(value);
	  }
	  public IPropertySelectionModel getYearModel() {
	    if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
	      getYearModels();
	    }
	    return ((Visit) getPage().getVisit()).getProSelectionModel4();
	  }
	  public void getYearModels() {
	    StringBuffer sql=new StringBuffer();
	    int i=0;
	    for(i=0;i<=DateUtil.getYear(new Date())-2008;i++){
	      sql.append("select "+i+" id,2008+"+i+" mingc from dual union\n");
	    }
	    sql.append("select "+i+" id,2008+"+i+" mingc from dual\n");
	    ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(sql.toString()));
	  }
	  
		 private void getToolbars() {
			Toolbar tb1 = new Toolbar("tbdiv");
			
			tb1.addText(new ToolbarText("���:"));
			ComboBox year = new ComboBox();
			year.setTransform("YearDropDown");
			year.setEditable(true);
			year.setWidth(80);
			tb1.addField(year);
			tb1.addText(new ToolbarText("-"));
			
			//���ˢ�°�ť
			ToolbarButton tb = new ToolbarButton(null, "��ѯ","function(){document.Form0.submit();}");
			tb.setIcon(SysConstant.Btn_Icon_Search);
			tb1.addItem(tb);
			
			setToolbar(tb1);															
		 }
	  
		 //	ҳ���ʼ��	
		 public void beginResponse(IMarkupWriter write , IRequestCycle cycle){
			Visit visit = (Visit)this.getPage().getVisit();
			if(!visit.getActivePageName().equals(this.getPageName().toString())){
				//�ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
				visit.setActivePageName(this.getPageName().toString());
				visit.setList1(null);
				setYearModel(null);
				setYearValue(null);
				getPrintChar();
			}
		getToolbars(); 
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

}