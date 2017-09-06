package com.zhiren.dc.rulgl.rulmzlcx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * ���ߣ�yangzl
 * ʱ�䣺2010-03-24
 */

/*
 * ���ߣ�yangzl
 * �޸�ʱ�䣺2010-03-26
 * ������
 * 		����û�м�¼ʱ��Ȼ��ʾ�����¼����¼Ϊ��
 */

public class RulmzlcxReport extends BasePage implements PageValidateListener {

	// ��Ϣ��
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, true);
	}

	protected void initialize() {
		super.initialize();
		_pageLink = "";
		setMsg(null);
	}

	// ��ť�¼�����ˢ�¡�
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = true;
			Refurbish();
		}
	}

	// ҳ���ʼ��ˢ���¼�
	private void Refurbish() {
		// Ϊ "ˢ��" ��ť��Ӵ������
		getSelectData();
	}

	public String getPrintTable() {
		return getSelectData();
	}

	private String getSelectData() {
		String strSQL = "";
		_CurrentPage = 1;
		_AllPages = 1;
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon cn = new JDBCcon();

		// ����
		ResultSetList jizrs = cn
				.getResultSetList("select mingc from jizfzb where diancxxb_id="+this.getTreeid()+" order by xuh");

		// ������
		int jizs = jizrs.getRows();

		if(jizs<=0){
			this.setMsg("'��ǰ�糧û�л���,�޷���ʾ����'");
			return "";
		}
		
		// ���
		int ArrWidth[] = new int[1 + (jizs * 2)];
		// ��ͷ����
		int headLength = 1 + (jizs * 2);
		// ��ͷ����
		String ArrHeader[][] = new String[2][headLength];
		ArrHeader[0][0] = "ʱ��";
		ArrHeader[1][0] = "ϵͳ";

		// ������������
		String jizmcs[] = new String[jizs];
		int i = 0;
		while (jizrs.next()) {
			jizmcs[i] = jizrs.getString("mingc");
			i++;
		}
		jizrs.close();

		int width=jizs<=4?600/(2*jizs+1):770/(2*jizs+1);
		
		// ��̬���������ͷ����Ԫ����
		for (int j = 0; j < headLength; j++) {
			ArrWidth[j] = width;
			if ((j + 1) < (jizs + 1)) {
				ArrHeader[0][(j + 1)] = "St,ad(%)";
				ArrHeader[1][(j + 1)] = jizmcs[j];
			} else if ((j + 1) < headLength) {
				ArrHeader[0][(j + 1)] = "Qnet,ar(��/��)";
				ArrHeader[1][(j + 1)] = jizmcs[(j - jizs)];
			}
		}

		String riq = this.getNianfValue().getValue() + "-"
				+ this.getYuefValue().getValue();

		//����������ʾ30������ݣ�����tmp��ʱ���洢01��30��
		StringBuffer sql = new StringBuffer();
		
		sql.append("select decode(t.day,null,'����ƽ��',t.day) ri,");

		for (int j = 0; j < 2 * jizs; j++) {
			if (j < jizs) {
				sql.append("		formatxiaosws(round(avg(decode(mingc,'" + jizmcs[j]
						+ "',decode(zhib,'stad',zhi,''),'')),2),2) " + jizmcs[j]
						+ ",\n");
			} else {
				sql.append("		round(avg(decode(mingc,'" + jizmcs[j - jizs]
						+ "',decode(zhib,'qnet',zhi,''),zhi,''))) "
						+ jizmcs[j - jizs] + ",\n");
			}
		}
		sql.deleteCharAt(sql.lastIndexOf(","));
		sql.append(" from (\n");
		sql.append("		select to_char(r.rulrq,'dd') ri,j.mingc,'stad' zhib,case when sum(decode(r.meil,null,0,0,0))=0 then '' else to_char(round(sum(nvl(r.stad,0)*r.meil)/sum(r.meil),2)) end as  zhi\n");
		sql.append("    	from rulmzlb r,jizfzb j\n");
		sql.append("    	where r.jizfzb_id=j.id\n");
		sql.append("          	and r.diancxxb_id=");
		sql.append(this.getTreeid());
		sql.append("\n");
		sql.append("          	and to_char(r.rulrq,'yyyy-mm')='");
		sql.append(riq);
		sql.append("'\n");
		sql.append("    	group by r.rulrq,j.mingc\n");
		sql.append("    union\n");
		sql.append("    	select to_char(r.rulrq,'dd') ri,j.mingc,'qnet' zhib,case when sum(decode(r.meil,null,0,0,0))=0 then '' else to_char(round(sum(nvl(r.qnet_ar,0)*r.meil)*1000/(sum(r.meil)*4.1816))) end as zhi\n");
		sql.append("    	from rulmzlb r,jizfzb j\n");
		sql.append("    	where r.jizfzb_id=j.id\n");
		sql.append("          	and r.diancxxb_id=");
		sql.append(this.getTreeid());
		sql.append("\n");
		sql.append("          	and to_char(r.rulrq,'yyyy-mm')='");
		sql.append(riq);
		sql.append("'\n");
		sql.append("    	group by r.rulrq,j.mingc\n");
		sql.append("    order by ri ) rl,(");
		sql.append("		select to_char(date0, 'dd') day\n");
		sql.append("		from (select trunc(to_date('"+this.getNianfValue().getId()+"','yyyy'), 'yyyy') + rn - 1 date0");
		sql.append("				from (select rownum rn from all_objects where rownum < 366))");
		sql.append("		where to_char(date0, 'mm') = '"+this.getYuefValue().getValue()+"'");
		sql.append(") t");
		sql.append(" where rl.ri(+)=t.day");
		sql.append(" group by rollup(t.day)\n");
		sql.append(" union \n");
		//��Ȩƽ��ֵ
				sql.append("select '',\n");
				sql.append("formatxiaosws(decode(sum(r.meil),0,0,round_new(sum(r.meil*stad)/sum(r.meil),2)),2) as stad,\n");
				sql.append("formatxiaosws(decode(sum(r.meil),0,0,round_new(sum(r.meil*stad)/sum(r.meil),2)),2) as stad,\n");
				sql.append("formatxiaosws(decode(sum(r.meil),0,0,round_new(sum(r.meil*stad)/sum(r.meil),2)),2) as stad,\n");
				sql.append("formatxiaosws(decode(sum(r.meil),0,0,round_new(sum(r.meil*stad)/sum(r.meil),2)),2) as stad,\n");
				sql.append("round(decode(sum(r.meil),0,0,round_new(sum(r.meil*r.qnet_ar)/sum(r.meil),2))*1000/4.1816,0) as qnet_ar,\n");
				sql.append("round(decode(sum(r.meil),0,0,round_new(sum(r.meil*r.qnet_ar)/sum(r.meil),2))*1000/4.1816,0) as qnet_ar,\n");
				sql.append("round(decode(sum(r.meil),0,0,round_new(sum(r.meil*r.qnet_ar)/sum(r.meil),2))*1000/4.1816,0) as qnet_ar,\n");
				sql.append("round(decode(sum(r.meil),0,0,round_new(sum(r.meil*r.qnet_ar)/sum(r.meil),2))*1000/4.1816,0) as qnet_ar\n");
				sql.append(" from rulmzlb r where to_char(r.rulrq, 'yyyy-mm') = '"+this.getNianfValue().getValue()+"-"+this.getYuefValue().getValue()+"'\n");
				sql.append("and r.qnet_ar is not null\n");
				sql.append("and r.meil!=0");


		// ���屨��
		Report rt = new Report();

		ResultSetList rs = cn.getResultSetList(sql.toString());

		// ����
		rt.setBody(new Table(rs, 2, 0, 1));
	
		rt.setTitle( this.getYuefValue().getValue() + "�·�" + "��¯ú��ֵ�����ͳ��", ArrWidth);
		rt.setDefaultTitle(1, 3, "",Table.ALIGN_LEFT);
		rt.body.setCells(1, 1, rt.body.getRows(), ArrHeader[0].length,
				Table.PER_ALIGN, Table.ALIGN_CENTER);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(35);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.ShowZero = true;
//		rt.body.mergeFixedRow();// �ϲ���
//		rt.body.mergeFixedCols();// �Ͳ���

		if (rt.body.getRows() > 1) {
			rt.body.merge(1, 2, 1, 1+jizs);
			rt.body.merge(1, 2+jizs, 1, 1+2*jizs);
		}
		rt.body.setCellValue(rt.body.getRows(), 1, "��Ȩƽ��");
		rt.body.merge(rt.body.getRows(), 2, rt.body.getRows(), 5);
		rt.body.merge(rt.body.getRows(), 6, rt.body.getRows(), 9);
		// ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "��ӡ����:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		// rt.setDefautlFooter(5, 2, "���:", Table.ALIGN_LEFT);
		rt
				.setDefautlFooter(rt.body.cols.length - 2, 2, "�Ʊ�:",
						Table.ALIGN_LEFT);
		rt.setPaper(Report.PAPER_A4);
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		rs.close();
		cn.Close();
		return rt.getAllPagesHtml();
	}

	public String getDiancmc(String id){
		String diancmc="";
		JDBCcon con=new JDBCcon();
		ResultSetList rs=con.getResultSetList("select mingc from diancxxb where id="+id);
		while(rs.next()){
			diancmc=rs.getString("mingc");
		}
		rs.close();
		con.Close();
		return diancmc;
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

	// ***************************�����ʼ����***************************//
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

	// ҳ���ж�����
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

	/**
	 * ҳ�濪ʼʱ��ʼ������
	 */
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel1(null);
			visit.setDropDownBean1(null);
			this.setNianfValue(null);
			this.setNianfModel(null);
			this.setYuefModel(null);
			this.setYuefValue(null);
			visit.setString2(null);		
			getSelectData();
		}
		getToolBars();
	}

	// ���������
	private IPropertySelectionModel _NianfModel;

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
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public void setNianfValue(IDropDownBean Value) {
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

	// �·�������
	private IPropertySelectionModel _YuefModel;

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
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		_YuefValue = Value;
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, i < 10 ? ("0" + i) : String
					.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}

	// ��������
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("���:"));
		ComboBox nf = new ComboBox();
		nf.setWidth(50);
		nf.setTransform("NianfDropDown");
		nf.setId("NianfDropDown");
		nf.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		nf.setLazyRender(true);
		nf.setEditable(true);
		tb1.addField(nf);

		tb1.addText(new ToolbarText("�·�:"));
		ComboBox yf = new ComboBox();
		yf.setWidth(50);
		yf.setTransform("YuefDropDown");
		yf.setId("YuefDropDown");
		yf.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		yf.setLazyRender(true);
		yf.setEditable(true);
		tb1.addField(yf);

		tb1.addText(new ToolbarText("-"));

		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
		visit.setDefaultTree(dt);

		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid() == null || "".equals(getTreeid()) ? "-1"
						: getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);

		ToolbarButton tb = new ToolbarButton(null, "ˢ��",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);
		setToolbar(tb1);
	}

	// �糧����
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	private boolean _DiancmcChange = false;

	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getDiancmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	private String treeid;

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		System.out.println("treeid:" + treeid);
		((Visit) getPage().getVisit()).setString2(treeid);
	}

	public DefaultTree getTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public String getTreeScript() {
		return getTree().getScript();
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

}