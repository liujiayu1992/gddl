package com.zhiren.gangkjy.jiexdy.xiemgl;

import java.sql.ResultSet;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

 
public class Xiemxxcx extends BasePage {

	public boolean getRaw() {
		return true;
	}

	// �����û���ʾ
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
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

	public String getPrintTable() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		Visit visit=(Visit)this.getPage().getVisit();
		
		String strDiancID = "";
		if(visit.isJTUser()){
			strDiancID = "";
		}else if(visit.isGSUser()){
			strDiancID = " and (dc.id="+visit.getDiancxxb_id()+" or dc.fuid="+visit.getDiancxxb_id()+")";
		}else{
			strDiancID = " and dc.id="+visit.getDiancxxb_id();
		}
		
		String sqlRenwbb ="select k.fhdw fhdw,k.pinz pinz,k.daohrq daohrq,k.faz faz,k.chec chec,k.ches ches,k.biaoz biaoz\n" +
			 "       ,k.maoz maoz,k.piz piz,k.fahrq fahrq,k.guohsj guohsj  from\n" + 
			 "(select grouping(g.mingc) as g1, grouping(p.mingc) as p1,grouping(f.daohrq) d1,\n" + 
			 "       decode(grouping(g.mingc),1,'�ܼ�',g.mingc) as fhdw,\n" + 
			 "       decode(grouping(f.daohrq)-grouping(g.mingc),1,'С��',p.mingc) pinz,\n" + 
			 "        to_char(f.daohrq,'yyyy-mm-dd') daohrq,\n" + 
			 "        c.mingc faz,f.chec chec, sum(f.ches) ches,\n" + 
			 "        sum(round_new(f.biaoz,0)) biaoz ,\n" + 
			 "        sum(round_new(f.maoz,0)) maoz,\n" + 
			 "        sum(round_new(f.piz,0)) piz,\n" + 
			 "        to_char(f.fahrq,'yyyy-mm-dd') fahrq," +
			 "		  to_char(f.guohsj,'yyyy-mm-dd HH24:mi:ss') guohsj\n" + 
			 "        from vwpinz p,fahb f,vwfahr g,vwchez c,diancxxb dc\n" + 
			 "        where f.gongysb_id=g.id and f.pinzb_id=p.id and f.faz_id=c.id\n" + 
			 "             and f.diancxxb_id=dc.id and \n" + 
			 "             f.daohrq >= to_date('"+this.getBeginRiq()+"','yyyy-mm-dd')\n" + 
			 "             and f.daohrq < to_date('"+this.getEndRiq()+"','yyyy-mm-dd')+1 and f.yewlxb_id=1 \n" + 
			 strDiancID+
			 "             group by rollup(g.mingc,p.mingc,f.daohrq,c.mingc,f.chec,f.fahrq,f.guohsj)\n" + 
			 "             having grouping(p.mingc)=1 or grouping(f.guohsj)=0" +
			 "		 order by g1 desc,g.mingc,p1 desc,p.mingc,d1 desc,f.daohrq) k";
		
		ResultSet rs = con.getResultSet(sqlRenwbb);
		if (rs == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult);
			setMsg(ErrorMessage.NullResult);
			return "";
		}
//		/********************���ñ�ͷ********************/
		String ArryHeader[][]=new String[1][11];
		ArryHeader[0]=new String[]{"������","Ʒ��","��������","��վ","����","����","Ʊ��(��)","ë��(��)","Ƥ��","��������","��������"};
		//���ñ��
		int ArryWidth[]=new int[11];
		ArryWidth=new int[]{100,40,70,60,45,45,45,45,45,80,120};
		rt.setTitle("жú��Ϣ��ѯ", ArryWidth);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 4, "�Ʊ�λ: "+visit.getDiancqc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 7, "�������ڣ�"+getBeginRiq() + " �� " + getEndRiq(), Table.ALIGN_RIGHT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);
		
		String[] strFormat = new String[]{"","","","","","","","","","",""};
		
		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArryWidth);
		rt.body.setHeaderData(ArryHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedCol(1);
		rt.body.mergeFixedCol(2);
		rt.body.setColFormat(strFormat);
		
		rt.body.setColAlign(1, Table.ALIGN_LEFT);
		rt.body.setColAlign(2, Table.ALIGN_LEFT);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_LEFT);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_CENTER);
		rt.body.setColAlign(11, Table.ALIGN_CENTER);
		
		rt.createDefautlFooter(ArryWidth);

		rt.setDefautlFooter(1, 3, "��ӡ����:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
//		rt.setDefautlFooter(6, 2, "����:", Table.ALIGN_LEFT);
//		rt.setDefautlFooter(11, 3, "���:", Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);
		

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
//		rt.body.setRowHeight(43);

		return rt.getAllPagesHtml();

	}

	// ��ť�ļ����¼�
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// ����ť�ύ����
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getPrintTable();
		}
	}

	
	public void getSelectData(){
	    Toolbar tb1 = new Toolbar("tbdiv");
	    tb1.addText(new ToolbarText("��������:"));
	    DateField renwkssj = new DateField();
		renwkssj.setValue(this.getBeginRiq());
		renwkssj.setId("BeginRiq");
		renwkssj.Binding("RIQ", "");
		tb1.addField(renwkssj);
		
		tb1.addText(new ToolbarText("��:"));
		DateField renwjssj = new DateField();
		renwjssj.setValue(this.getEndRiq());
		renwjssj.setId("EndRiq");
		renwjssj.Binding("RIQ1","");// ��htmlҳ�е�id��,���Զ�ˢ��
		tb1.addField(renwjssj);
			
		
//     ���ð�ť
		ToolbarButton rbtn = new ToolbarButton(null, "��ѯ",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		setToolbar(tb1);
	}
	
	

	// ������ʹ�õķ���
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		((DateField) getToolbar().getItem("BeginRiq")).setValue(getBeginRiq());
		((DateField) getToolbar().getItem("EndRiq")).setValue(getEndRiq());
		return getToolbar().getRenderScript();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setString2("");
			getSelectData();
		}

	}
	

	// ������
	 public String getBeginRiq() {
			if (((Visit) this.getPage().getVisit()).getString1() == null || ((Visit) this.getPage().getVisit()).getString1().equals("")) {
				
				((Visit) this.getPage().getVisit()).setString1(DateUtil.FormatDate(new Date()));
			}
			return ((Visit) this.getPage().getVisit()).getString1();
		}
	   public void setBeginRiq(String riq) {
				((Visit) this.getPage().getVisit()).setString1(riq);
		}
	   
	   public String getEndRiq() {
			if (((Visit) this.getPage().getVisit()).getString2() == null || ((Visit) this.getPage().getVisit()).getString2().equals("")) {
				
				((Visit) this.getPage().getVisit()).setString2(DateUtil.FormatDate(new Date()));
			}
			return ((Visit) this.getPage().getVisit()).getString2();
		}
	  public void setEndRiq(String riq) {
				((Visit) this.getPage().getVisit()).setString2(riq);
		}
	  
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
			
}
