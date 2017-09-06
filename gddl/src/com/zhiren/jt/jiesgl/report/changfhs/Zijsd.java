package com.zhiren.jt.jiesgl.report.changfhs;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.zhiren.common.DateUtil;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
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
import com.zhiren.main.Visit;
import org.apache.tapestry.contrib.palette.SortMode;

public class Zijsd extends BasePage {
	
	private String OraDate(Date _date) {
		if (_date == null) {
			return "to_date('" + DateUtil.Formatdate("yyyy-MM-dd", new Date())
					+ "','yyyy-mm-dd')";
		}
		return "to_date('" + DateUtil.Formatdate("yyyy-MM-dd", _date)
				+ "','yyyy-mm-dd')";
	}
	
	public boolean isEditable() {
		return ((Visit) getPage().getVisit()).getboolean4();
	}

	public void setEditable(boolean editable) {
		((Visit) getPage().getVisit()).setboolean4(editable);
	}

	public boolean isEditable2() {
		return ((Visit) getPage().getVisit()).getboolean5();
	}

	public void setEditable2(boolean editable) {
		((Visit) getPage().getVisit()).setboolean5(editable);
	}

	// ��ʽ��
	public String format(double dblValue, String strFormat) {
		DecimalFormat df = new DecimalFormat(strFormat);
		return formatq(df.format(dblValue));

	}

	public String formatq(String strValue) {// ��ǧλ�ָ���
		String strtmp = "", xiaostmp = "", tmp = "";
		int i = 3;
		if (strValue.lastIndexOf(".") == -1) {

			strtmp = strValue;
			if (strValue.equals("")) {

				xiaostmp = "";
			} else {

				xiaostmp = ".00";
			}

		} else {

			strtmp = strValue.substring(0, strValue.lastIndexOf("."));

			if (strValue.substring(strValue.lastIndexOf(".")).length() == 2) {

				xiaostmp = strValue.substring(strValue.lastIndexOf(".")) + "0";
			} else {

				xiaostmp = strValue.substring(strValue.lastIndexOf("."));
			}

		}
		tmp = strtmp;

		while (i < tmp.length()) {
			strtmp = strtmp.substring(0, strtmp.length() - (i + (i - 3) / 3))
					+ ","
					+ strtmp.substring(strtmp.length() - (i + (i - 3) / 3),
							strtmp.length());
			i = i + 3;
		}

		return strtmp + xiaostmp;
	}

	// ****************�ж�ҳ���Ƿ��ǵ�һ�ε���**************//
//	public int getZhuangt() {
//		return ((Visit) getPage().getVisit()).getInt1();
//	}
//
//	public void setZhuangt(int _value) {
//		((Visit) getPage().getVisit()).setInt1(_value);
//	}

	// *****************************************�������ÿ�ʼ******************************************//

	// ��ӡ״̬
	private static IPropertySelectionModel _PrintModel;

	public IPropertySelectionModel getPrintModel() {
		if (_PrintModel == null) {
			getPrintModels();
		}
		return _PrintModel;
	}

	private IDropDownBean _PrintValue;

	public IDropDownBean getPrintValue() {
		if (_PrintValue == null) {
			setPrintValue((IDropDownBean) getPrintModel().getOption(0));
		}
		return _PrintValue;
	}

	private boolean _PrintChange = false;

	public void setPrintValue(IDropDownBean Value) {
		if (_PrintValue == Value) {
			_PrintChange = false;
		} else {
			_PrintValue = Value;
			_PrintChange = true;
		}
	}

	public IPropertySelectionModel getPrintModels() {
		List listPrint = new ArrayList();

		listPrint.add(new IDropDownBean(0, "����ӡ"));
		listPrint.add(new IDropDownBean(1, "�Ѵ�ӡ"));
		listPrint.add(new IDropDownBean(2, "ȫ��"));

		_PrintModel = new IDropDownModel(listPrint);
		return _PrintModel;
	}

	public void setPrintModel(IPropertySelectionModel _value) {
		_PrintModel = _value;
	}

	
	// ***************������Ϣ��******************//
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

	public void submit(IRequestCycle cycle) {
	
	}
	

//	private void PrintState() {
//		JDBCcon con = new JDBCcon();
//
//		String bianh[] = getWhere();
//		String where = "";
//		for (int i = 0; i < bianh.length; i++) {
//			if (where.equals("")) {
//				where = "'" + bianh[i] + "'";
//			} else {
//				where = where + ",'" + bianh[i] + "'";
//			}
//		}
//		// String sql = "update diancjsmkb set gongsdyzt=1 where bianm in
//		// ("+where+")";
//		// con.getUpdate(sql);
//		con.Close();
//		getSelectData();
//	}


	// ******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		} else {
			visit.setboolean1(false);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			// *************�������ÿ�ʼ***************//
			visit.setboolean2(false);// ���� ;
			visit.setboolean3(false);// ���㵥

			((Visit) getPage().getVisit()).setString4("");	//WindowScript(ҳ����ʾ)
			
			if(cycle.getRequestContext().getParameters("lx") !=null) {
				((Visit) getPage().getVisit()).setString3("");
				((Visit) getPage().getVisit()).setString3(cycle.getRequestContext().getParameters("lx")[0]);
	        }
			if(cycle.getRequestContext().getParameters("bm") !=null) {
				((Visit) getPage().getVisit()).setString2("");
				((Visit) getPage().getVisit()).setString2(cycle.getRequestContext().getParameters("bm")[0]);
	        }
		}

		// if(BianhChange){
		// BianhChange = false;
		// chaxunzt1 = 1;// ��ѯ״̬
		// zhuangt=2;
		// Refurbish();
		// }
//		if (zhuangt == 1) {// ��Ҫ��
//			visit.setInt1(1);
//		}
//		if (zhuangt == 2) {// ��Ҫ��
//			visit.setInt1(2);
//		}

		
		// *************�������ý���***************//
	}

	// *****************************������������*****************************//
//	private int chaxunzt1 = 0;// ��ѯ״̬
//
//	private int zhuangt = 1;

//	���ڵ���js_begin
	public String getWindowScript(){
		
		return ((Visit) getPage().getVisit()).getString4();
	}
	
	public void setWindowScript(String value){
		
		((Visit) getPage().getVisit()).setString4(value);
	}

	private String nvlStr(String strValue) {
		if (strValue == null) {
			return "";
		} else if (strValue.equals("null")) {
			return "";
		}

		return strValue;
	}

	public String getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String bianh[] = visit.getString2().split(",");
		if (bianh != null) {
			if (bianh.length == 0) {
				return "";
			} else {
				visit.setInt1(2);// �ǵ�һ����ʾ
				int k=0;
				Changfjsd jsd = new Changfjsd();
				StringBuffer sb = new StringBuffer();
//				setAllPages(bianh.length);
//				jsd.setAllPages(bianh.length);
				for (int p = 0; p < bianh.length; p++) {
					sb.append(jsd.getChangfjsd(bianh[p], p,visit.getString3()));
					k+=jsd.getAllPages();
				}
				setAllPages(k);
				_CurrentPage=1;
				return sb.toString();
			}
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

	private String RT_HET = "jies";

	private String mstrReportName = "jies";

	public String getPrintTable() {
		if (mstrReportName.equals(RT_HET)) {

			return getSelectData();
		} else {
			return "�޴˱���";
		}
	}

	// ******************************����*******************************//

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			// _pageLink = "window.location.target = '_blank';"+_pageLink;
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

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.opener=null;self.close();window.parent.close();open('"
					+ getpageLinks() + "','');";
		} else {
			return "";
		}
	}

	public String getTianzdw(String jiesdbh) {
		String Tianzdw = "";
		JDBCcon con = new JDBCcon();
		try {
			String sql = "select quanc from diancxxb where id=(select diancxxb_id from diancjsmkb where bianm='"
					+ jiesdbh + "')";
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				Tianzdw = rs.getString("quanc");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return Tianzdw;
	}

	private String FormatDate(Date _date) {
		if (_date == null) {
			// return MainGlobal.Formatdate("yyyy�� MM�� dd��", new Date());
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
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
}