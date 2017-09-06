package com.zhiren.jt.dianc;


import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import org.apache.tapestry.contrib.palette.SortMode;
import com.zhiren.common.ext.ExtTreeUtil;

public class Diancreport extends BasePage {
	public boolean getRaw() {
		return true;
	}
	private String userName="";
	
	public void setUserName(String value) {
		userName=((Visit) getPage().getVisit()).getRenymc();
	}
	public String getUserName() {
		return userName;
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

	private String treeid = "";

	public String getTreeid() {

		if (treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {

		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
	}
	
//	***************������Ϣ��******************//
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
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	private String REPORT_NAME_REZC="rezc";
	
	private String mstrReportName="";
	
	public String getPrintTable(){
		if(mstrReportName.equals(REPORT_NAME_REZC)){
			return getSelectData();
		}else{	
			return "�޴˱���";
		}
	}
	private String getSelectData(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		String str = "";
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and dc.id = " + getTreeid()
						+ " and dc.fuid = " + getTreeid() + "";
			} else {
				str = "and dc.id = " + getTreeid() + "";
			}
		}

		sbsql.append("SELECT dc.bianm, dc.mingc, dc.quanc, \n");
		sbsql.append(" sf.quanc AS shengfb_id,\n");
		sbsql.append(" DECODE ((SELECT mingc\n");
		sbsql.append(" FROM diancxxb");
		sbsql.append(" WHERE ID = dc.fuid), NULL, '',\n");
		sbsql.append("(SELECT mingc\n");
		sbsql.append(" FROM diancxxb\n");
		sbsql.append(" WHERE ID = dc.fuid)) AS fuid,\n");
		sbsql.append("DECODE ((SELECT cz.quanc\n");
		sbsql.append("FROM chezxxb cz, diancdzb dz\n");
		sbsql.append(" WHERE cz.ID = dz.chezxxb_id\n");
		sbsql.append(" AND dz.leib = '��վ'\n");
		sbsql.append(" AND dz.diancxxb_id = dc.ID),NULL, '',\n");
		sbsql.append("(SELECT cz.quanc  FROM chezxxb cz, diancdzb dz WHERE cz.ID = dz.chezxxb_id \n");
		sbsql.append("AND dz.leib = '��վ' AND dz.diancxxb_id = dc.ID)) AS daoz,\n");
		sbsql.append("DECODE ((SELECT cz.quanc FROM chezxxb cz, diancdzb dz\n");
		sbsql.append(" WHERE cz.ID = dz.chezxxb_id AND dz.leib = '�ۿ�' AND dz.diancxxb_id = dc.ID),NULL, '',\n");
		sbsql.append("(SELECT cz.quanc FROM chezxxb cz, diancdzb dz WHERE cz.ID = dz.chezxxb_id AND dz.leib = '�ۿ�'\n");
		sbsql.append("  AND dz.diancxxb_id = dc.ID)) AS daog,\n");
		sbsql.append(" lb.mingc AS dianclbb_id \n");
		sbsql.append(" FROM diancxxb dc, shengfb sf, dianclbb lb\n");
		sbsql.append(" WHERE dc.shengfb_id = sf.ID AND dc.dianclbb_id = lb.ID \n");
		sbsql.append(str);
		
		ResultSet rs=cn.getResultSet(sbsql.toString());
		Report rt=new Report();
		
		//�����ͷ����
		 String ArrHeader[][]=new String[1][8];
		 ArrHeader[0]=new String[] {"����","����","ȫ��","ʡ��","�ϼ���˾","��վ","����","�糧���"};

		
		
		//�п�
		 int ArrWidth[]=new int[] {60,100,240,50,80,60,60,80};



		//����ҳ����
		rt.setTitle("�糧��Ϣ��ϸ��",ArrWidth);
//		rt.setDefaultTitle(1,4,"���λ:"+danwqc,Table.ALIGN_LEFT);
//		rt.setDefaultTitle(6,4,_NianfValue.getId()+"��"+ _YuefValue.getId()+"��",Table.ALIGN_CENTER);
//		rt.setDefaultTitle(11,2,"��Ȼ16-1��",Table.ALIGN_RIGHT);
		
		//����
		rt.setBody(new Table(rs,1,0,0));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);//��ͷ����
		rt.body.mergeFixedRow();
//		rt.body.mergeFixedCol(1);
		rt.body.ShowZero=false;
		
		//ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,8,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
//		rt.setDefautlFooter(4,2,"���:",Table.ALIGN_LEFT);
//		rt.setDefautlFooter(19,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT );
		
		//����ҳ��
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
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
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean3(null);
			visit.setString1("");
			visit.setDefaultTree(null);
			getSelectData();
		}
		
		String[] param = null;
		if(cycle.getRequestContext().getParameters("lx") !=null) {
            param= cycle.getRequestContext().getParameters("lx");
            if(param != null ) {
            	mstrReportName=param[0];
            }
        }
	}
	
	public String getcontext() {
			return "";
//		return "var  context='http://"
//				+ this.getRequestCycle().getRequestContext().getServerName()
//				+ ":"
//				+ this.getRequestCycle().getRequestContext().getServerPort()
//				+ ((Visit) getPage().getVisit()).get() + "';";
	}

	public SortMode getSort() {
		return SortMode.USER;
	}
	
	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page����
	protected void initialize() {
		_pageLink = "";
	}
}

