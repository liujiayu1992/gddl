package com.zhiren.jt.jiesgl.report.pub;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;


public class Jiesdcx_hs extends BasePage {


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

	/*
	 * 
	 * 
	 */

	public String getPrintTable() {
		_CurrentPage=1;
		_AllPages=1;
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		String shoukdw = "";
		String kaihyh = "";
		String zhangh = "";
		String bianh = "";
		String jiesrq = "";
		String laimksrq="";
		String laimjzrq="";
		
		String JIESBID = visit.getString1();
		//�ж��Ƿ����ӽ��㵥
		String sql_id="select id from jiesb where fuid="+JIESBID;
		ResultSetList rsjs=cn.getResultSetList (sql_id);
		if(rsjs.getRows()>0){
			JIESBID="";
			while(rsjs.next()) {
			
				JIESBID+=""+rsjs.getString("ID")+",";
				
			}
			JIESBID=JIESBID.substring(0, JIESBID.length()-1);
		}
		
	
		String sql_xx = "select max(j.shoukdw) as shoukdw ,max(j.kaihyh) as kaihyh,max(j.zhangh) as zhangh," +
				"max(j.jiesrq) as jiesrq, min(j.yansksrq) as yansksrq,max(j.yansjzrq) as yansjzrq " +
				" from jiesb j where id in ("+JIESBID+")";
		ResultSetList rs2=cn.getResultSetList (sql_xx);
	
		
			while(rs2.next()){
				 shoukdw = rs2.getString("shoukdw");
				 kaihyh = rs2.getString("kaihyh");
				 zhangh = rs2.getString("zhangh");
				 jiesrq=rs2.getDateString("jiesrq");
				 laimksrq=rs2.getDateString("yansksrq");
				 laimjzrq=rs2.getDateString("yansjzrq");
			 }
			
	
			//ȥ���ܸ��ӷѺ;ܸ��˷�,���¼��㲻��˰ú��,˰��
			
		String sql=

			"select decode(j.bianm,null,'�ϼ�',j.bianm) as bianm,max(j.faz) as faz,Round_new(sum(j.jiesrl*j.jiessl)/sum(j.jiessl)*4.1816/1000,2) as farl,\n" +
			"sum(j.ches) as ches,sum(j.jiessl) as jiessl,Round_new(sum(j.hansdj*j.jiessl)/sum(j.jiessl),2) as hansdj,\n" + 
			"sum((j.hansmk-nvl(yf.kuidjfyf,0)-nvl(yf.kuidjfzf,0))- round_new((j.hansmk-nvl(yf.kuidjfyf,0)-nvl(yf.kuidjfzf,0))/1.17*0.17,2))  as buhsmk,\n" + 
			"sum(round_new((j.hansmk-nvl(yf.kuidjfyf,0)-nvl(yf.kuidjfzf,0))/1.17*0.17,2))  as shuik,\n" + 
			"sum(nvl(yf.guotyf,0)) as yunzf,\n" + 
			"sum(j.hansmk-nvl(yf.kuidjfyf,0)-nvl(yf.kuidjfzf,0)+nvl(yf.guotyf,0)) as jehj,\n" + 
			"sum(j.yufkje) as bencycjk,\n" + 
			"sum(j.hansmk-nvl(yf.kuidjfyf,0)-nvl(yf.kuidjfzf,0)+nvl(yf.guotyf,0)-j.yufkje)as shijfk\n" + 
			"from jiesb j ,jiesyfb yf\n" + 
			"where j.bianm=yf.bianm(+)\n" + 
			"and j.id in ("+JIESBID+")\n" + 
			"group by rollup (j.bianm) order by j.bianm";


	
		
		String ArrHeader[][]=new String[2][12];
		ArrHeader[0]=new String[] {"�տλ",shoukdw,shoukdw,shoukdw,shoukdw,"������",kaihyh,kaihyh,kaihyh,"�ʺ�",zhangh,zhangh};
		ArrHeader[1]=new String[] {"���յ����","վ��","��ֵ","����","����","����","�ۿ�","˰��","���ӷ�","���ϼ�","����Ӧ����","����ʵ�ʸ���"};

		int ArrWidth[]=new int[] {100,50,45,45,50,50,70,70,70,70,80,80};


		ResultSetList rs = cn.getResultSetList(sql);
		//ResultSet rs = cn.getResultSet(strSQL.toString());

		// ����
		
		Table tb=new Table(rs, 2, 0, 0);
		rt.setBody(tb);
		

//		����15��
		int iLastRow=rt.body.getRows();
		int iNewlastRow=15;
		if  (iLastRow<15){
			rt.body.AddTableRow(15-iLastRow);
			//�ϼ��Ƶ����
			for (int i=1 ;i<=rt.body.getCols(); i++){
				rt.body.setCellValue(iNewlastRow-1, i, rt.body.getCellValue(iLastRow,i));
				rt.body.setCellValue(iLastRow, i, "");
			}
			rt.body.setCellValue(iNewlastRow, 1, "��ע");
			rt.body.merge(15, 2, 15, 12);
			rt.body.setCellValue(15, 2, "��ú����"+laimksrq+"��"+laimjzrq);
			
		}
		
		
		rt.setTitle(visit.getDiancqc()+"ȼ�Ͻ��㵥", ArrWidth);
		rt.setDefaultTitle(9, 4, "���:"+((Visit) getPage().getVisit()).getString4(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(1, 4, "����:"+jiesrq, Table.ALIGN_LEFT);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
//		rt.body.mergeFixedRowCol();
		rt.body.mergeCell(1,2,1,5);
		rt.body.mergeCell(1,7,1,9);
		rt.body.mergeCell(1,11,1,12);
		rt.body.ShowZero=true;
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_RIGHT);
		rt.body.setColAlign(4, Table.ALIGN_RIGHT);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
		rt.body.setCellAlign(15, 2, Table.ALIGN_LEFT);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 12, "��˾�ܾ���&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
									"�ܻ��ʦ��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
									"�������ܣ�&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
									"�����ƣ�&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
									"�����쵼��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
									"�����ˣ�&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
									"���ˣ�",Table.ALIGN_LEFT);
		
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
			return rt.getAllPagesHtml();
//		
	}
	

	public void getSelectData() {
		// Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		ToolbarButton rbtn = new ToolbarButton(null, "����",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Return);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText(
				"<marquee width=300 scrollamount=2></marquee>"));
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
		if (getTbmsg() != null) {
			getToolbar().deleteItem();
			getToolbar().addText(
					new ToolbarText("<marquee width=300 scrollamount=2>"
							+ getTbmsg() + "</marquee>"));
		}
		return getToolbar().getRenderScript();
	}

	// ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
//			visit.setString10(visit.getActivePageName());
			//visit.setActivePageName(getPageName().toString());
			setTbmsg(null);
		}

		getSelectData();
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
			Visit visit = (Visit) getPage().getVisit();
			cycle.activate("Ranljsd");
		}
	}

	// ҳ���½��֤
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