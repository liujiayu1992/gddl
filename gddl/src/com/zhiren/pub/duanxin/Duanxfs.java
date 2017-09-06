package com.zhiren.pub.duanxin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import cn.com.chundi.sendsms.Sms;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Duanxfs extends BasePage {
	// 界面用户提示
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

	// 接收人员选择组件
	public SortMode getSort() {
		return SortMode.USER;
	}

	public IPropertySelectionModel getXuanzryModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			setXuanzryModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setXuanzryModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public List getXuanzrySelected() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setXuanzrySelected(List ChepSelect) {
		((Visit) getPage().getVisit()).setList1(ChepSelect);
	}

	private void setXuanzryModels() {
		setXuanzrySelected(null);
		Visit v = (Visit) getPage().getVisit();
		List _XuanzryList = new ArrayList();
		JDBCcon con = new JDBCcon();
		String sql = "select distinct id, quanc from renyxxb where diancxxb_id="+v.getDiancxxb_id()+ " and yiddh is not null";
		ResultSetList rs = con.getResultSetList(sql);
		rs = con.getResultSetList(sql);
		while (rs.next()) {
			_XuanzryList.add(new IDropDownBean(rs.getLong("id"), rs
					.getString("quanc")));
		}
		con.Close();
		setXuanzryModel(new IDropDownModel(_XuanzryList));
	}

	// 表格使用的方法
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	// 工具栏使用的方法
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

	// 页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setboolean1(false);
			visit.setString1(null);
			visit.setString2(null);
			setXuanzryModel(null);
			setXuanzrySelected(null);

		}

	}

	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {

		if (_Sendsmss) {
			Sendsmss();
		}
	}

	private boolean _Sendsmss = false;

	public void Sendsmss(IRequestCycle cycle) {
		_Sendsmss = true;
	}

	// 页面登陆验证
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

	public void setReny(String _value) {
		((Visit) getPage().getVisit()).setString1(_value);
	}

	public String getReny() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setSendmsg(String _value) {
		((Visit) getPage().getVisit()).setString2(_value);
	}

	public String getSendmsg() {
		return ((Visit) getPage().getVisit()).getString2();
	}

	private void Sendsmss() {
		String _sms = getSendmsg();
		if (_sms.equals("")) {
			setMsg("请输入短信内容");
			return;
		}
		String _id = "";
		String _reny = getReny();
		if (getXuanzrySelected().size() == 0) {
			if (_reny.equals("")) {
				setMsg("请选择接收短信的人员");
			}
		}
		String[] _renys;
		if (_reny.length() > 0) {
			_reny = ";" + _reny;
		}
		_reny = _reny.replaceAll("，", ",");
		_reny = _reny.replaceAll(";", ",");
		_reny = _reny.replaceAll("；", ",");
		_reny = _reny.replaceAll("、", ",");
		_renys = _reny.split(",");
		for (int i = 0; i < getXuanzrySelected().size(); i++) {
			if (i == (getXuanzrySelected().size() - 1)) {
				_id = _id
						+ ((IDropDownBean) (getXuanzrySelected().get(i)))
								.getId();
			} else {
				_id = _id
						+ ((IDropDownBean) (getXuanzrySelected().get(i)))
								.getId() + ",";
			}
		}
		JDBCcon con = new JDBCcon();
		int _sendsucc = 0;
		try {
			Sms sms1 = new Sms();
			if (!_id.equals("")) {
				String _sql = "select yiddh from renyxxb where id in (" + _id
						+ ")";
				ResultSet res = con.getResultSet(_sql);
				while (res.next()) {
					boolean sendsucc = sms1.sendmsg(_sms, res
							.getString("yiddh"));
					if (!sendsucc && res.getString("yiddh").length() == 11) {
						_sendsucc++;
					} else {
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			for (int i = 0; i < _renys.length; i++) {
				if (!_renys[i].equals("")
						&& _renys[i].toString().length() == 11) {
					System.out.println(_renys[i]);
					boolean sendsucc = sms1.sendmsg(_sms, _renys[i]);
					if (!sendsucc) {
						_sendsucc++;
					} else {
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			con.Close();
			if (_sendsucc > 1) {
				setMsg("共有：" + _sendsucc + "消息发送错误");
			}
		}
	}
}
