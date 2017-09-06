package com.zhiren.jt.jihgl.ranlxyjh;

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
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 
*/


public class Ranlxyjhcx extends BasePage implements PageValidateListener {

	// �ж��Ƿ��Ǽ����û�
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// ����

	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// ��˾
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// �糧
	}

	// ��ʼ����
	private Date _BeginriqValue = new Date();

	public Date getBeginriqDate() {
		if (_BeginriqValue == null) {
			_BeginriqValue = new Date();
		}
		return _BeginriqValue;
	}

	public void setBeginriqDate(Date _value) {
		if (_BeginriqValue.equals(_value)) {
			// _BeginriqChange=false;
		} else {
			_BeginriqValue = _value;
			// _BeginriqChange=true;
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
			// this.getSelectData();
		}
	}

	private void Refurbish() {
		// Ϊ "ˢ��" ��ť��Ӵ������
		isBegin = true;
		getSelectData();
	}

	// ******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setNianfValue(null);
//			setYuefValue(null);
			getNianfModels();
//			getYuefModels();
			this.setTreeid(null);
			this.getTree();
			isBegin = true;

		}

		getToolBars();
		this.Refurbish();
	}

	private String RT_HET = "Yuedmjgmxreport";// �¶�ú�۸���ϸ

	private String mstrReportName = "Yuedmjgmxreport";

	public String getPrintTable() {
		if (!isBegin) {
			return "";
		}
		isBegin = false;
		if (mstrReportName.equals(RT_HET)) {
			return getSelectData();
		} else {
			return "�޴˱���";
		}
	}

	public int getZhuangt() {
		return 1;
	}

	private int intZhuangt = 0;

	public void setZhuangt(int _value) {
		intZhuangt = 1;
	}

	private boolean isBegin = false;

	private String getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String strSQL = "";
		_CurrentPage = 1;
		_AllPages = 1;

		JDBCcon cn = new JDBCcon();

		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}

		String zhuangt_sl = "";
		String zhuangt_zl = "";
		String zhuangt_dj = "";

		String strGongsID = "";
		String guoltj = "";
		String notHuiz = "";
		String biaot = "";
		String biaoti = "";
		String group = "";
		String order = "";
		int jib = this.getDiancTreeJib();
		if (jib == 1) {// ѡ����ʱˢ�³����еĵ糧
			strGongsID = " ";
			guoltj = " and dc.id not in(" + Guoldcid() + ")\n";
			biaot = "select decode(grouping(g.mingc)+grouping(f.mingc),2,'�ܼ�',1,f.mingc,'&nbsp;&nbsp;'||g.mingc) as diancmc,\n";
			group = "  group by rollup(f.mingc,g.mingc)\n";

			order = " order by grouping(f.mingc) desc ,max(f.xuh),f.mingc,grouping(g.mingc) desc ,max(g.xuh),g.mingc";

		} else if (jib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and (dc.fuid=  " + this.getTreeid()
					+ " or dc.shangjgsid=" + this.getTreeid() + ")";
			guoltj = " and dc.id not in(" + Guoldcid() + ")\n";
			notHuiz = " having not grouping(f.mingc)=1 ";// ���糧���Ƿֹ�˾ʱ,ȥ�����Ż���

			biaot = "select decode(grouping(g.mingc)+grouping(f.mingc),2,'�ܼ�',1,f.mingc,'&nbsp;&nbsp;'||g.mingc) as diancmc,\n";
			group = "  group by rollup(f.mingc,g.mingc)\n";

			order = " order by grouping(f.mingc) desc ,max(f.xuh),f.mingc,grouping(g.mingc) desc ,max(g.xuh),g.mingc";

		} else if (jib == 3) {// ѡ�糧ֻˢ�³��õ糧
			strGongsID = " and dc.id= " + this.getTreeid();

			if (getBaoblxValue().getValue().equals("�ֳ�����")) {
				notHuiz = " having not  grouping(dc.mingc)=1";
			} else if (getBaoblxValue().getValue().equals("�ֿ����")) {
				biaot = "select decode(grouping(g.mingc)+grouping(dc.mingc),2,'�ܼ�',1,dc.mingc,'&nbsp;&nbsp;'||g.mingc) as diancmc,\n";
				group = "  group by rollup(dc.mingc,g.mingc)\n";

				order = " order by grouping(dc.mingc) desc ,max(dc.xuh),dc.mingc,grouping(g.mingc) desc ,max(g.xuh),g.mingc";

				notHuiz = " having not  grouping(dc.mingc)=1";// ���糧���ǵ糧ʱ,ȥ���ֹ�˾�ͼ��Ż���
			} else {
				notHuiz = " having not  grouping(f.mingc)=1";// ���糧���ǵ糧ʱ,ȥ���ֹ�˾�ͼ��Ż���
			}
		} else if (jib == -1) {
			strGongsID = " and dc.id = "
					+ ((Visit) getPage().getVisit()).getDiancxxb_id();
			guoltj = "";
		}

		long yuefen = getYuefValue().getId();
		long yuefen1 = getYuefValue().getId();
		long yuefen2 = getYuefValue().getId();
		long yuefen3 = getYuefValue().getId();
		long yuefen4 = getYuefValue().getId();
		long nianfen = getNianfValue().getId();
		String nianyue = "";
		if (String.valueOf(yuefen).length() == 1) {
			nianyue = String.valueOf(nianfen) + "0" + String.valueOf(yuefen)
					+ "01";
		} else {
			nianyue = String.valueOf(nianfen) + String.valueOf(yuefen) + "01";
		}

		// �����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String arrFormat[] = null;
		int iFixedRows = 0;// �̶��к�
		int iCol = 0;// ����
		// ��������

		biaoti = "ȼ�����üƻ���";
		// JDBCcon con = new JDBCcon();
//		visit.getRenyjb();	3���糧��2����˾��1������
		String sql="";
		String idpd="";
		String sqq="select  d.jib jib from diancxxb d where d.id="+getTreeid();
		ResultSet rsq = cn.getResultSet(sqq);
//		System.out.println(sqq);
		try {
			while(rsq.next()){
				idpd=rsq.getString("jib");
				
			}
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
        if("3".equals(idpd)){
        	
        	sql=

       		 "select decode(xuh,8,'����',"
					+ "9,'����',10,'����',11,'����',12,'����',15,'����',16,'����',"
					+ "17,'����',18,'����',19,'����',20,'����',21,'����',22,'����',23,"
					+ "'����',24,'����',25,'����',26,'����',27,'����',28,'����',29,'����',30,'����',31,'����'"
					+ ",32,'����',33,'����',34,'����',35,'����',36,'����',37,'����',38,'����',39,'����',40,'����',13,8,14,9,41,10,42,11,xuh) as ���, mingc ,beiz ,nvl(value,0) as �ϼ� ,nvl(y1,0) as һ�·� ,nvl(y2,0) as ���·�,nvl(y3,0) as ���·�,nvl(y4,0) as ���·�,nvl(y5,0) as ���·�,nvl(y6,0) as ���·�,\n"
					+ "nvl(y7,0) as ���·�,nvl(y8,0) as ���·�,nvl(y9,0) as ���·�,nvl(y10,0) as ʮ�·�,nvl(y11,0) as ʮһ�·�,nvl(y12,0) as ʮ���·� from (\n"
					+ "select im.mingc,im.xuh xuh,im.beiz beiz, nvl(sum(rhb.value),0) value,nvl(sum(rhb.y1),0) y1,nvl(sum(rhb.y2),0) y2,nvl(sum(rhb.y3),0) y3,nvl(sum(rhb.y4),0) y4,"
					+ "nvl(sum(rhb.y5),0) y5,nvl(sum(rhb.y6),0) y6,nvl(sum(rhb.y7),0) y7\n"
					+ ",nvl(sum(rhb.y8),0) y8,nvl(sum(rhb.y9),0) y9,nvl(sum(rhb.y10),0) y10 ,nvl(sum(rhb.y11),0) y11 ,nvl(sum(rhb.y12),0) y12\n"
					+ " from ranlxyjhzbb rzb ,ranlxyjhb rhb ,item im,itemsort it\n"
					+ "where rhb.ranlxyjhzbb_id=rzb.id and to_char(rzb.nianf,'yyyy')="
					+ getNianfValue()
					+ " and rzb.diancxxb_id="
					+ getTreeid()
					+ "\n"
					+ "and im.itemsortid=it.id and rhb.zhibmc_item_id=im.id\n"
					+ " and instr(im.diancxxbs_id,to_char(rzb.diancxxb_id))>0 \n"
					+

					"group by (im.xuh,im.mingc,im.beiz)\n"
					+ "order by im.xuh\n" + "\n" + ")\n" + "";
        	
        }else{
        	
        	 sql=

             	"select decode(xuh,8,'����',"
					+ "9,'����',10,'����',11,'����',12,'����',15,'����',16,'����',"
					+ "17,'����',18,'����',19,'����',20,'����',21,'����',22,'����',23,"
					+ "'����',24,'����',25,'����',26,'����',27,'����',28,'����',29,'����',30,'����',31,'����'"
					+ ",32,'����',33,'����',34,'����',35,'����',36,'����',37,'����',38,'����',39,'����',40,'����',13,8,14,9,41,10,42,11,xuh) as ���, mingc ,beiz ,nvl(value,0) as �ϼ� ,nvl(y1,0) as һ�·� ,nvl(y2,0) as ���·�,nvl(y3,0) as ���·�,nvl(y4,0) as ���·�,nvl(y5,0) as ���·�,nvl(y6,0) as ���·�,\n"
					+ "nvl(y7,0) as ���·�,nvl(y8,0) as ���·�,nvl(y9,0) as ���·�,nvl(y10,0) as ʮ�·�,nvl(y11,0) as ʮһ�·�,nvl(y12,0) as ʮ���·� from (\n"
					+ "select im.mingc,im.xuh xuh,im.beiz beiz, nvl(sum(rhb.value),0) value,nvl(sum(rhb.y1),0) y1,nvl(sum(rhb.y2),0) y2,nvl(sum(rhb.y3),0) y3,nvl(sum(rhb.y4),0) y4,"
					+ "nvl(sum(rhb.y5),0) y5,nvl(sum(rhb.y6),0) y6,nvl(sum(rhb.y7),0) y7\n"
					+ ",nvl(sum(rhb.y8),0) y8,nvl(sum(rhb.y9),0) y9,nvl(sum(rhb.y10),0) y10 ,nvl(sum(rhb.y11),0) y11 ,nvl(sum(rhb.y12),0) y12\n"
					+ " from ranlxyjhzbb rzb ,ranlxyjhb rhb ,item im,itemsort it\n"
					+ "where rhb.ranlxyjhzbb_id=rzb.id and to_char(rzb.nianf,'yyyy')="
					+ getNianfValue()
					+ "and im.itemsortid=it.id and rhb.zhibmc_item_id=im.id\n"
					+ "group by (im.xuh,im.mingc,im.beiz)\n"
					+ "order by im.xuh\n" + "\n" + ")\n" + "";
        	 
        }
    	StringBuffer cs = new StringBuffer();
		cs.append("���,ָ������,��λ,�ϼ�,һ�·�,���·�,���·�,���·�,���·�,���·�,���·�,���·�,���·�,ʮ�·�,ʮһ�·�,ʮ���·�");
		ArrHeader = new String[1][16];
		ArrHeader[0] = cs.toString().split(",");
//		
		ArrWidth = new int[] { 30, 100, 60, 80, 50, 50, 50, 50, 50, 50, 50, 50,
				50, 50, 50,50 };
		// arrFormat=new String
		// []{"","","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00"};
//		iFixedRows = 1;
//		iCol = 10;
		// }
		ResultSet rs = cn.getResultSet(sql);
		String biao="";
		String sqlq="select mingc from diancxxb d where id="+getTreeid();
		ResultSet rsr = cn.getResultSet(sqlq);
		try {
			while(rsr.next()){
				biao=rsr.getString("mingc");
			}
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			try {
				rsr.close();
			} catch (SQLException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}
		}		
		// ����
		Table tb = new Table(rs, 1, 0, 1);
		rt.setBody(tb);

		rt.setTitle("���ƺ������������޹�˾"+ intyear + "��"  + biaoti,
				ArrWidth);
		rt
				.setDefaultTitle(1, 3, "���λ:"
						+biao ,
						Table.ALIGN_LEFT);

//		rt.setDefaultTitle(7, 3, "�����:" + intyear + "��" + intMonth + "��",
//				Table.ALIGN_LEFT);
		rt.setDefaultTitle(13, 5, "", Table.ALIGN_RIGHT);//

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.ShowZero = true;

		// rt.body.setColFormat(arrFormat);
		// ҳ��
		rt.body.mergeFixedRow();
		
		rt.body.mergeFixedCols();
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "��ӡ����:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(10, 2, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(13, 3, "�Ʊ�:", Table.ALIGN_LEFT);
		

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();


		return rt.getAllPagesHtml();
	}

	// �õ�ϵͳ��Ϣ�������õı������ĵ�λ����
	public String getBiaotmc() {
		String biaotmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc = "select  mingc from itemsort where bianm='RANLXYJHZB'";
		ResultSet rs = cn.getResultSet(sql_biaotmc);
		try {
			while (rs.next()) {
				biaotmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return biaotmc;

	}

	// �糧����
	public boolean _diancmcchange = false;

	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if (_DiancmcValue == null) {
			_DiancmcValue = (IDropDownBean) getIDiancmcModel().getOption(0);
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

		String sql = "";
		sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);

	}

	// �󱨱�����
	public boolean _Baoblxchange = false;

	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if (_BaoblxValue == null) {
			_BaoblxValue = (IDropDownBean) getIBaoblxModels().getOption(0);
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
		try {
			List fahdwList = new ArrayList();
			fahdwList.add(new IDropDownBean(0, "�ֿ����"));
			fahdwList.add(new IDropDownBean(1, "�ֳ�����"));
			fahdwList.add(new IDropDownBean(2, "�ֳ��ֿ����"));

			_IBaoblxModel = new IDropDownModel(fahdwList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IBaoblxModel;
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

	public Date getYesterday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	public Date getMonthFirstday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
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

	// �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
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


		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),
				"-1".equals(getTreeid()) ? null : getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getIDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));


		ToolbarButton tb = new ToolbarButton(null, "ˢ��",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);

		setToolbar(tb1);

	}

	// ��ѯ�Ƿ����ù��˵糧id
	private String Guoldcid() {
		JDBCcon con = new JDBCcon();
		String dcid = "";
		ResultSetList rsl = con
				.getResultSetList("select zhi from xitxxb where mingc='�����Ϻ������ַ��硢�Ϻ������ȵ硢�Ϻ���������'\n");

		while (rsl.next()) {
			dcid = rsl.getString("zhi");
		}
		con.Close();

		return dcid;
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

	public IDropDownBean getYuefValue() {
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
