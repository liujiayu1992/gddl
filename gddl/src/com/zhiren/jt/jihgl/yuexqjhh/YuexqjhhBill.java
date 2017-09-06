package com.zhiren.jt.jihgl.yuexqjhh;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
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
import com.zhiren.common.SysConstant;
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

public class YuexqjhhBill extends BasePage implements PageValidateListener {

	private String _msg;

	public void setMsg(String _value) {
		this._msg = MainGlobal.getExtMessageBox(_value, false);
	}
	
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	protected void initialize() {
		this._msg = "";
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
	
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
//	���������_��ʼ
	public boolean nianfchanged;
	
	private IDropDownBean _NianfValue;
	
	private static IPropertySelectionModel _NianfModel;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null||_NianfValue.equals("")) {
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}
	
	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
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
//	���������_����
	
//	�·�������_��ʼ
	public boolean Changeyuef = false;
	
	private IDropDownBean _YuefValue;

	private static IPropertySelectionModel _YuefModel;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj).getId()) {
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
	
	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}
	
	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}
//	�·�������_����
	
	private boolean _RefurbishChick = false;
	
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}
	
	public String getPrintTable() {
		
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		Report rt = new Report();
		String sql = "";
		
		String zhuangt="";
		if(visit.isShifsh()==true){
			if(visit.getRenyjb()==3){
				zhuangt="";
			}else if(visit.getRenyjb()==2){
				zhuangt=" and (yx.zhuangt=1 or yx.zhuangt=2)";
			}else if(visit.getRenyjb()==1){
				zhuangt=" and yx.zhuangt=2";
			}
		}
		
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
		
//		���·���1λ��ʱ��ǰ�油0������1��ת����01��
		String StrMonth = "";
		if(intMonth<10){
			StrMonth="0"+intMonth;
		}else{
			StrMonth=""+intMonth;
		}
		
		String child_dcid = "select id from diancxxb where fuid = " + getTreeid();
		
//		��һ����������£�ͨ���糧��id�ж�ҳ���ϲ�ѯ�����ܵ糧�������ӵ糧
		if (Shujpd(con, child_dcid) != 0) { // ҳ���ϲ�ѯ�����ܵ糧
			
//			�ж��ܵ糧�Ƿ�������
			String zhongc = 
				"select (sum(yx.fadl) + sum(yx.fadmh) + sum(yx.fadml) +\n" +
				"       sum(yx.gongrl) + sum(yx.gongrmh) + sum(yx.gongrml) +\n" + 
				"       sum(yx.sunh) + sum(yx.qity) + sum(yx.yuekc) +\n" + 
				"       sum(yx.yuemkc) + sum(yx.xuqsl) + sum(yx.rulrz)) as total_value\n" + 
				"  from yuexqjhh yx, diancxxb dc\n" + 
				" where yx.diancxxb_id = dc.id\n" + 
				"   and yx.riq = to_date('"+ intyear +"-"+ StrMonth +"-01', 'yyyy-mm-dd')\n" + 
				"   and yx.diancxxb_id = " + getTreeid();
			
			if (hasValue(con, zhongc, "total_value")) { // �ܵ糧������
				sql  =
					"select sj.lanc,sj.mingc,sj.danw,sj.zhi\n" +
					"from (\n" + 
					"select 1 as lanc,'������' as mingc,yx.fadl as zhi,'��ǧ��ʱ' as danw,yx.zhuangt\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" +
					"union\n" +
					"select 2 as lanc,'�����ú��' as mingc,yx.fadmh as zhi,'��/ǧ��ʱ' as danw,yx.zhuangt\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" +
					"union\n" +
					"select 3 as lanc,'�����ú��' as mingc,yx.fadml as zhi,'��' as danw,yx.zhuangt\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
					"union\n" +
					"select 4 as lanc,'������' as mingc,yx.gongrl as zhi,'�򼪽�' as danw,yx.zhuangt\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
					"union\n" +
					"select 5 as lanc,'���ȱ�ú��' as mingc,yx.gongrmh as zhi,'ǧ��/����' as danw,yx.zhuangt\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
					"union\n" +
					"select 6 as lanc,'���ȱ�ú��' as mingc,yx.gongrml as zhi,'��' as danw,yx.zhuangt\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
					"union\n" +
					"select 7 as lanc,'�������' as mingc,yx.sunh as zhi,'��' as danw,yx.zhuangt\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
					"union\n" +
					"select 8 as lanc,'������' as mingc,yx.qity as zhi,'��' as danw,yx.zhuangt\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
					"union\n" +
					"select 9 as lanc,'�³���' as mingc,yx.yuekc as zhi,'��' as danw,yx.zhuangt\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
					"union\n" +
					"select 10 as lanc,'��ĩ���' as mingc,yx.yuemkc as zhi,'��' as danw,yx.zhuangt\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
					"union\n" +
					"select 11 as lanc,'��������' as mingc,yx.xuqsl as zhi,'��' as danw,yx.zhuangt\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd')  and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
					"union\n" +
					"select 12 as lanc,'��¯��ֵ' as mingc,yx.rulrz as zhi,'�׽�/ǧ��' as danw,yx.zhuangt\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd')  and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
					") sj order by sj.lanc";
			} else { // �ܵ糧û���ݣ�ȡ�ӵ糧�ĺϼ�����
				sql  =
					"select sj.lanc,sj.mingc,sj.danw,sj.zhi\n" +
					"from (\n" + 
					"select 1 as lanc,'������' as mingc, sum(yx.fadl) as zhi,'��ǧ��ʱ' as danw\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id in ("+ child_dcid +")\n" +
					"union\n" +
					"select 2 as lanc,'�����ú��' as mingc, sum(yx.fadmh) as zhi,'��/ǧ��ʱ' as danw\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id in ("+ child_dcid +")\n" +
					"union\n" +
					"select 3 as lanc,'�����ú��' as mingc, sum(yx.fadml) as zhi,'��' as danw\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id in ("+ child_dcid +")\n" + 
					"union\n" +
					"select 4 as lanc,'������' as mingc, sum(yx.gongrl) as zhi,'�򼪽�' as danw\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id in ("+ child_dcid +")\n" + 
					"union\n" +
					"select 5 as lanc,'���ȱ�ú��' as mingc, sum(yx.gongrmh) as zhi,'ǧ��/����' as danw\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id in ("+ child_dcid +")\n" + 
					"union\n" +
					"select 6 as lanc,'���ȱ�ú��' as mingc, sum(yx.gongrml) as zhi,'��' as danw\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id in ("+ child_dcid +")\n" + 
					"union\n" +
					"select 7 as lanc,'�������' as mingc, sum(yx.sunh) as zhi,'��' as danw\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id in ("+ child_dcid +")\n" + 
					"union\n" +
					"select 8 as lanc,'������' as mingc, sum(yx.qity) as zhi,'��' as danw\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id in ("+ child_dcid +")\n" + 
					"union\n" +
					"select 9 as lanc,'�³���' as mingc, sum(yx.yuekc) as zhi,'��' as danw\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id in ("+ child_dcid +")\n" + 
					"union\n" +
					"select 10 as lanc,'��ĩ���' as mingc, sum(yx.yuemkc) as zhi,'��' as danw\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id in ("+ child_dcid +")\n" + 
					"union\n" +
					"select 11 as lanc,'��������' as mingc, sum(yx.xuqsl) as zhi,'��' as danw\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd')  and yx.diancxxb_id in ("+ child_dcid +")\n" + 
					"union\n" +
					"select 12 as lanc,'��¯��ֵ' as mingc, sum(yx.rulrz) as zhi,'�׽�/ǧ��' as danw\n" + 
					"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd')  and yx.diancxxb_id in ("+ child_dcid +")\n" + 
					") sj order by sj.lanc";
			}
		} else { // ҳ���ϲ�ѯ�����ӵ糧
			sql  =
				"select sj.lanc,sj.mingc,sj.danw,sj.zhi\n" +
				"from (\n" + 
				"select 1 as lanc,'������' as mingc,yx.fadl as zhi,'��ǧ��ʱ' as danw,yx.zhuangt\n" + 
				"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" +
				"union\n" +
				"select 2 as lanc,'�����ú��' as mingc,yx.fadmh as zhi,'��/ǧ��ʱ' as danw,yx.zhuangt\n" + 
				"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" +
				"union\n" +
				"select 3 as lanc,'�����ú��' as mingc,yx.fadml as zhi,'��' as danw,yx.zhuangt\n" + 
				"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
				"union\n" +
				"select 4 as lanc,'������' as mingc,yx.gongrl as zhi,'�򼪽�' as danw,yx.zhuangt\n" + 
				"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
				"union\n" +
				"select 5 as lanc,'���ȱ�ú��' as mingc,yx.gongrmh as zhi,'ǧ��/����' as danw,yx.zhuangt\n" + 
				"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
				"union\n" +
				"select 6 as lanc,'���ȱ�ú��' as mingc,yx.gongrml as zhi,'��' as danw,yx.zhuangt\n" + 
				"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
				"union\n" +
				"select 7 as lanc,'�������' as mingc,yx.sunh as zhi,'��' as danw,yx.zhuangt\n" + 
				"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
				"union\n" +
				"select 8 as lanc,'������' as mingc,yx.qity as zhi,'��' as danw,yx.zhuangt\n" + 
				"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
				"union\n" +
				"select 9 as lanc,'�³���' as mingc,yx.yuekc as zhi,'��' as danw,yx.zhuangt\n" + 
				"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
				"union\n" +
				"select 10 as lanc,'��ĩ���' as mingc,yx.yuemkc as zhi,'��' as danw,yx.zhuangt\n" + 
				"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
				"union\n" +
				"select 11 as lanc,'��������' as mingc,yx.xuqsl as zhi,'��' as danw,yx.zhuangt\n" + 
				"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd')  and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
				"union\n" +
				"select 12 as lanc,'��¯��ֵ' as mingc,yx.rulrz as zhi,'�׽�/ǧ��' as danw,yx.zhuangt\n" + 
				"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd')  and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
				") sj order by sj.lanc";
		}
		
		String[][] ArrHeader = new String[1][4];
		ArrHeader[0] = new String[]{"����", "��Ŀ", "��λ", "�ƻ�"};
		int[] ArrWidth = new int[] {40, 200, 70, 90};
		
		ResultSetList rslData = con.getResultSetList(sql);
		
		rt.setTitle("�����üƻ���ѯ", ArrWidth);
		rt.setBody(new Table(rslData, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.ShowZero = true;
		rt.body.setColCells(1, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setColCells(2, Table.PER_ALIGN, Table.ALIGN_LEFT);
		rt.body.setColCells(3, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ��"+((Visit)this.getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 2, "��ѯʱ�䣺"+getNianfValue()+"��"+getYuefValue()+"��", Table.ALIGN_RIGHT);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "��ӡ���ڣ�"+DateUtil.Formatdate("yyyy��MM��dd��", new Date()), Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 1, "��ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 1, "�Ʊ�", Table.ALIGN_LEFT);
		
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		if(rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rslData.close();
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	private void getSelectData() {
		
		Visit visit = (Visit)this.getPage().getVisit();
		
		Toolbar tbr = new Toolbar("tbdiv");
		
		tbr.addText(new ToolbarText("��ݣ�"));
		ComboBox nf_cbx = new ComboBox();
		nf_cbx.setTransform("NIANF");
		nf_cbx.setWidth(60);
		tbr.addField(nf_cbx);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("�·ݣ�"));
		ComboBox yf_cbx = new ComboBox();
		yf_cbx.setTransform("YUEF");
		yf_cbx.setWidth(60);
		tbr.addField(yf_cbx);
		tbr.addText(new ToolbarText("-"));
		
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
		setTree(dt);
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
		tbr.addText(new ToolbarText("�糧��"));
		tbr.addField(tf);
		tbr.addItem(tb2);
		tbr.addText(new ToolbarText("-"));
		
		ToolbarButton tbrtn = new ToolbarButton(null, "��ѯ", "function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Search);
		tbr.addItem(tbrtn);
		setToolbar(tbr);
	}
	
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
	
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id, mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}

	public DefaultTree getTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public void setTree(DefaultTree etu) {
		((Visit) this.getPage().getVisit()).setDefaultTree(etu);
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
	/**
	 * �ж��ܵ糧�Ƿ������ݣ������ݷ���ture��û���ݷ���false
	 * @param con
	 * @param sql
	 * @param colName �ֶ���
	 * @return
	 */
	public boolean hasValue(JDBCcon con, String sql, String colName) {
		
		boolean hasValue = false;
		
		ResultSetList rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString(colName).equals("0")) {
				hasValue = false;
			} else {
				hasValue = true;
			}
		}
		return hasValue;
	}
	
	/**
	 * �ж����������ݿ����Ƿ���ڣ����ڷ��������������ڷ���0
	 * @param con
	 * @param sql
	 * @return
	 */
	public int Shujpd(JDBCcon con,String sql){
		return JDBCcon.getRow(con.getResultSet(sql));
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
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().equals(getPageName())) {
			visit.setActivePageName(getPageName());
			this.setTreeid(null);
			setNianfValue(null);
			setYuefValue(null);
		}
		getSelectData();
	}
}