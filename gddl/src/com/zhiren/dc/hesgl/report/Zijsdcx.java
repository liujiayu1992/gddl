package com.zhiren.dc.hesgl.report;

import java.util.ArrayList;
import java.util.Date;

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

/**
 * @author yinjm
 * �������ӽ��㵥��ѯ
 */

public class Zijsdcx extends BasePage implements PageValidateListener {
	
	boolean meikjsbh; // ��ʶ�Ƿ���ˢ��ҳ��ʱ���ع�ú�������������������ݣ�trueΪ�ǣ�falseΪ��
	
	public boolean isMeikjsbh() {
		return meikjsbh;
	}

	public void setMeikjsbh(boolean meikjsbh) {
		this.meikjsbh = meikjsbh;
	}
	
	boolean yunfjsbh; // ��ʶ�Ƿ���ˢ��ҳ��ʱ���ع��˷ѽ�����������������ݣ�trueΪ�ǣ�falseΪ��
	
	public boolean isYunfjsbh() {
		return yunfjsbh;
	}

	public void setYunfjsbh(boolean yunfjsbh) {
		this.yunfjsbh = yunfjsbh;
	}
	
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
	public IDropDownBean getNianfValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getNianfModel().getOptionCount() > 0) {
				for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
					if (DateUtil.getYear(new Date()) == ((IDropDownBean)getNianfModel().getOption(i)).getId()) {
						setNianfValue((IDropDownBean)getNianfModel().getOption(i));
						break;
					}
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}

	public void setNianfValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getNianfModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			getNianfModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	
	public void setNianfModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void getNianfModels() {
		String sql = "select nf.yvalue, nf.ylabel from nianfb nf";
		setNianfModel(new IDropDownModel(sql));
	}
//	���������_����
	
//	�·�������_��ʼ
	public IDropDownBean getYuefValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getYuefModel().getOptionCount() > 0) {
				for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
					if (DateUtil.getMonth(new Date()) == ((IDropDownBean)getYuefModel().getOption(i)).getId()) {
						setYuefValue((IDropDownBean)getYuefModel().getOption(i));
						break;
					}
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	
	public void setYuefValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getYuefModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			getYuefModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	
	public void setYuefModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	
	public void getYuefModels() {
		String sql = "select mvalue, mlabel from yuefb";
		setYuefModel(new IDropDownModel(sql));
	}
//	�·�������_����
	
//	��������������_��ʼ
	public IDropDownBean getJieslxValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean4() == null) {
			if (getJieslxModel().getOptionCount() > 0) {
				setJieslxValue((IDropDownBean) getJieslxModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean4();
	}

	public void setJieslxValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean4(LeibValue);
	}

	public IPropertySelectionModel getJieslxModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
			getJieslxModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setJieslxModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getJieslxModels() {
		ArrayList list = new ArrayList();
//		list.add(new IDropDownBean(1, "��Ʊ����"));
		list.add(new IDropDownBean(2, "ú�����"));
		list.add(new IDropDownBean(3, "�˷ѽ���"));
		setJieslxModel(new IDropDownModel(list));
	}
//	��������������_����
	
//	���䵥λ������_��ʼ
	public IDropDownBean getYunsdwValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean5() == null) {
			if (getYunsdwModel().getOptionCount() > 0) {
				setYunsdwValue((IDropDownBean) getYunsdwModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean5();
	}

	public void setYunsdwValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean5(LeibValue);
	}

	public IPropertySelectionModel getYunsdwModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel5() == null) {
			getYunsdwModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel5();
	}

	public void setYunsdwModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel5(value);
	}

	public void getYunsdwModels() {
		String sql = "select rownum as id, shoukdw from (select distinct yfzb.shoukdw from jiesyfzb yfzb order by yfzb.shoukdw)";
		setYunsdwModel(new IDropDownModel(sql, "ȫ��"));
	}
//	���䵥λ������_����
	
//	�տλ������_��ʼ
	public IDropDownBean getShoukdwValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean6() == null) {
			if (getShoukdwModel().getOptionCount() > 0) {
				setShoukdwValue((IDropDownBean) getShoukdwModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean6();
	}

	public void setShoukdwValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean6(LeibValue);
	}

	public IPropertySelectionModel getShoukdwModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel6() == null) {
			getShoukdwModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel6();
	}

	public void setShoukdwModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel6(value);
	}

	public void getShoukdwModels() {
		String sql = "select rownum as id, shoukdw from (select distinct jszb.shoukdw from jieszb jszb order by jszb.shoukdw)";
		setShoukdwModel(new IDropDownModel(sql, "ȫ��"));
	}
//	�տλ������_����
	
//	ú�������������_��ʼ
	public IDropDownBean getMeikjsbhValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean7() == null) {
			if (getMeikjsbhModel().getOptionCount() > 0) {
				setMeikjsbhValue((IDropDownBean) getMeikjsbhModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean7();
	}

	public void setMeikjsbhValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean7(LeibValue);
	}

	public IPropertySelectionModel getMeikjsbhModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel7() == null) {
			getMeikjsbhModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel7();
	}

	public void setMeikjsbhModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel7(value);
	}

	public void getMeikjsbhModels() {
		
		String gongysOrMeik = "";
		if (!getTreeid().equals("0")) {
			gongysOrMeik = " and (jszb.gongysb_id = "+ getTreeid() +" or jszb.meikxxb_id = "+ getTreeid() +")";
		}
		
		String shoukdw = "";
		if (!getShoukdwValue().getValue().equals("ȫ��")) {
			shoukdw = " and jszb.shoukdw = '"+ getShoukdwValue().getValue() +"'";
		}
		
		String sql = 
			"select rownum num, bianm\n" +
			"  from (select distinct jszb.bianm\n" + 
			"          from jieszb jszb\n" + 
			"         where to_char(jszb.jiesrq, 'yyyy-mm') =\n" + 
			"               to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm')\n" + gongysOrMeik + shoukdw + ") order by bianm";
		setMeikjsbhModel(new IDropDownModel(sql, "ȫ��"));
	}
//	ú�������������_����
	
	
//	�˷ѽ�����������_��ʼ
	public IDropDownBean getYunfjsbhValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean8() == null) {
			if (getYunfjsbhModel().getOptionCount() > 0) {
				setYunfjsbhValue((IDropDownBean) getYunfjsbhModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean8();
	}

	public void setYunfjsbhValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean8(LeibValue);
	}

	public IPropertySelectionModel getYunfjsbhModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel8() == null) {
			getYunfjsbhModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel8();
	}

	public void setYunfjsbhModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel8(value);
	}

	public void getYunfjsbhModels() {
		
		String gongysOrMeik = "";
		if (!getTreeid().equals("0")) {
			gongysOrMeik = " and (yfzb.gongysb_id = "+ getTreeid() +" or yfzb.meikxxb_id = "+ getTreeid() +")";
		}
		
		String yunsdw = "";
		if (!getYunsdwValue().getValue().equals("ȫ��")) {
			yunsdw = " and yfzb.shoukdw = '"+ getYunsdwValue().getValue() +"'";
		}
		
		String sql = 
			"select rownum num, bianm\n" +
			"  from (select distinct yfzb.bianm\n" + 
			"          from jiesyfzb yfzb\n" + 
			"         where to_char(yfzb.jiesrq, 'yyyy-mm') =\n" + 
			"               to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm')\n" + gongysOrMeik + yunsdw + ") order by bianm";
		setYunfjsbhModel(new IDropDownModel(sql, "ȫ��"));
	}
//	�˷ѽ�����������_����
	
	private boolean _RefurbishChick = false;
	
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
//	ȡ��ú����㵥��֣���ȡ���˷ѽ��㵥���
	private boolean _QuxcfClick = false;
	
	public void QuxcfButton(IRequestCycle cycle) {
		_QuxcfClick = true;
	}
	
//	ȡ����Ʊ���㵥���
	private boolean _QuxlpcfClick = false;
	
	public void QuxlpcfButton(IRequestCycle cycle) {
		_QuxlpcfClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
		if (_QuxlpcfClick) {
			_QuxlpcfClick = false;
			quxlpcf();
		}
		if (_QuxcfClick) {
			_QuxcfClick = false;
			quxcf();
		}
	}
	
	public String getPrintTable() {
		
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		String[][] ArrHeader = null;
		int[] ArrWidth = null;
		String sql = "";
		String allPagesHtml = "";
		
		String gongysOrMeik = "";
		if (!getTreeid().equals("0")) {
			if (getJieslxValue().getValue().equals("�˷ѽ���")) {
				gongysOrMeik = "and (yfzb.gongysb_id = "+ getTreeid() +" or yfzb.meikxxb_id = "+ getTreeid() +")";
			} else if (getJieslxValue().getValue().equals("ú�����")) {
				gongysOrMeik = "and (jszb.gongysb_id = "+ getTreeid() +" or jszb.meikxxb_id = "+ getTreeid() +")";
			}
		}
		
		String shoukdw = "";
		if (!getShoukdwValue().getValue().equals("ȫ��")) {
			shoukdw = "and jszb.shoukdw = '"+ getShoukdwValue().getValue() +"'";
		}
		
		String yunsdw = "";
		if (!getYunsdwValue().getValue().equals("ȫ��")) {
			yunsdw = "and yfzb.shoukdw = '"+ getYunsdwValue().getValue() +"'";
		}
		
		if (getJieslxValue().getValue().equals("�˷ѽ���")) {
			
			String yunfjsbh = "";
			if (!getYunfjsbhValue().getValue().equals("ȫ��")) {
				yunfjsbh = " and yfzb.bianm = '"+ getYunfjsbhValue().getValue() +"'";
			}
			
			sql = 
				"select /*grouping(ycgj.gysmc) gm, grouping(ycgj.mkmc) km, grouping(yf.zhongchh) hh,*/\n" +
				" decode(grouping(ycgj.gysmc), 1, '�ܼ�', ycgj.gysmc) gysmc,\n" + 
				" decode(grouping(ycgj.gysmc) + grouping(ycgj.mkmc),\n" + 
				"        1,\n" + 
				"        '�ϼ�',\n" + 
				"        2,\n" + 
				"        '�ܼ�',\n" + 
				"        ycgj.mkmc) mkmc,\n" + 
				" decode(grouping(ycgj.gysmc) + grouping(ycgj.mkmc) + grouping(yf.bianm), 1, 'С��', 2, '�ϼ�', 3, '�ܼ�', yf.bianm) bianm,\n" +
				" decode(grouping(ycgj.gysmc) + grouping(ycgj.mkmc) + grouping(yf.zhongchh),\n" + 
				"        1,\n" + 
				"        'С��',\n" + 
				"        2,\n" + 
				"        '�ϼ�',\n" + 
				"        3,\n" + 
				"        '�ܼ�',\n" + 
				"        yf.zhongchh) zhongchh,\n" + 
				" sum(yf.ches) ches,\n" + 
				" sum(yf.jiessl) jiessl,\n" + 
				" max(yf.yunj) yunju,\n" + 
				" max(yf.hansdj) as yunjia,\n" + 
				" sum(round_new(yf.jiessl * yf.hansdj, 2)) as yunfei,\n" + 
				" sum(yf.ches) as xiecs,\n" + 
				" sum(yf.xiecf) xiecf,\n" + 
				" sum(round_new(yf.jiessl * yf.hansdj + decode(yf.xiecf, null, 1, yf.xiecf), 2)) as hej,\n" + 
				" sum(ycgj.jiessl) as ycgj_jiessl,\n" + 
				" sum(round_new(ycgj.jiessl * yf.hansdj, 2)) as ycgj_yunf,\n" + 
				" sum(ycgj.xiecf) as ycgj_xiecf,\n" + 
				" sum(round_new(ycgj.jiessl * yf.hansdj + ycgj.xiecf, 2)) as ycgj_jine,\n" + 
				" sum(dtyc.jiessl) jiessl,\n" + 
				" sum(round_new(dtyc.jiessl * yf.hansdj, 2)) as dtgj_yunf,\n" + 
				" sum(dtyc.xiecf) as dtyc_xiecf,\n" + 
				" sum(round_new(dtyc.jiessl * yf.hansdj + dtyc.xiecf, 2)) as dtyc_jine\n" + 
				"  from (select gys.mingc gysmc,\n" + 
				"               mk.mingc mkmc,\n" + 
				"               yfzb.jiessl,\n" + 
				"               yfzb.xiecf,\n" + 
				"               yfzb.jiesrq,\n" + 
				"               yfzb.jieslx,\n" + 
				"               yfzb.bianm,\n" + 
				"               yfzb.zhongchh,\n" + 
				"               yfzb.danw,\n" + 
				"               yfzb.chaifbl,\n" + 
				"               yfzb.gongysb_id,\n" + 
				"               yfzb.meikxxb_id\n" + 
				"          from jiesyfzb yfzb, gongysb gys, meikxxb mk, jiesyfb yf\n" + 
				"         where to_char(yfzb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm') "+ gongysOrMeik +"\n" + 
				"           and yfzb.gongysb_id = gys.id\n" + 
				"           and yfzb.meikxxb_id = mk.id\n" + yunsdw + yunfjsbh + "\n" +
				"           and yfzb.bianm = yf.bianm\n" + 
				"           and yfzb.danw = '���ǹ���') ycgj,\n" + 
				"\n" + 
				"       (select gys.mingc gycmc,\n" + 
				"               mk.mingc mkmc,\n" + 
				"               yfzb.jiessl,\n" + 
				"               yfzb.xiecf,\n" + 
				"               yfzb.jiesrq,\n" + 
				"               yfzb.jieslx,\n" + 
				"               yfzb.bianm,\n" + 
				"               yfzb.zhongchh,\n" + 
				"               yfzb.danw,\n" + 
				"               yfzb.chaifbl,\n" + 
				"               yfzb.gongysb_id,\n" + 
				"               yfzb.meikxxb_id\n" + 
				"          from jiesyfzb yfzb, gongysb gys, meikxxb mk\n" + 
				"         where to_char(yfzb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm') "+ gongysOrMeik +"\n" + 
				"           and yfzb.gongysb_id = gys.id\n" + 
				"           and yfzb.meikxxb_id = mk.id\n" + yunsdw + yunfjsbh +"\n" +
				"           and yfzb.danw = '��������') dtyc,\n" + 
				"       jiesyfb yf\n" + 
				" where ycgj.bianm = dtyc.bianm\n" + 
				"   and ycgj.bianm = yf.bianm\n" + 
				"   and dtyc.bianm = yf.bianm\n" + 
				" group by rollup(ycgj.gysmc, ycgj.mkmc, yf.bianm, yf.zhongchh)\n" +
				" having not grouping(yf.bianm) + grouping(yf.zhongchh) = 1";
			
//			��ȡ��ͬ���
			String hetbh_sql = 
				"select ysht.hetbh\n" +
				"  from hetys ysht\n" + 
				" where ysht.id in\n" + 
				"       (select distinct yfzb.hetb_id\n" + 
				"          from jiesyfzb yfzb, gongysb gys, meikxxb mk, jiesyfb yf\n" + 
				"         where to_char(yfzb.jiesrq, 'yyyy-mm') =\n" + 
				"               to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm')\n" + 
				"           and yfzb.gongysb_id = gys.id\n" + 
				"           and yfzb.meikxxb_id = mk.id\n" + 
				"           and yfzb.bianm = yf.bianm)";
			
			ResultSetList hetbh_rsl = con.getResultSetList(hetbh_sql);
			String hetbh = "";
			while (hetbh_rsl.next()) {
				hetbh += hetbh_rsl.getString("hetbh") + ",&nbsp;";
			}
			
//			��ȡ��ֱ���
			String chaifbl_sql = 
				"select yfzb.danw, max(yfzb.chaifbl) chaifbl\n" +
				"  from jiesyfzb yfzb, gongysb gys, meikxxb mk, jiesyfb yf\n" + 
				" where to_char(yfzb.jiesrq, 'yyyy-mm') =\n" + 
				"       to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm')\n" + 
				"   and yfzb.gongysb_id = gys.id\n" + 
				"   and yfzb.meikxxb_id = mk.id\n" + 
				"   and yfzb.bianm = yf.bianm\n" + 
				"group by yfzb.danw\n" + 
				"order by yfzb.danw";
			
			ResultSetList chaifbl_rsl = con.getResultSetList(chaifbl_sql);
			String chaifbl_dtyc = "";
			String chaifbl_ycgj = "";
			while (chaifbl_rsl.next()) {
				if (chaifbl_rsl.getString("danw").equals("��������")) {
					chaifbl_dtyc = chaifbl_rsl.getString("chaifbl") + "%";
				} else {
					chaifbl_ycgj = chaifbl_rsl.getString("chaifbl") + "%";
				}
			}
			
			ArrHeader = new String[2][20];
			ArrHeader[0] = new String[]{"��ú��λ","����","������","жú<br>�ص�","����","����ú��<br>(��)","�˾�<br>(Km)","�˼�<br>(Ԫ/��)","�˷�<br>(Ԫ)","ж����","ж����","�ϼ�",
				"���ǹ��ʷ�̯���"+chaifbl_ycgj,"���ǹ��ʷ�̯���"+chaifbl_ycgj,"���ǹ��ʷ�̯���"+chaifbl_ycgj,"���ǹ��ʷ�̯���"+chaifbl_ycgj, 
				"�������Ƿ�̯���"+chaifbl_dtyc, "�������Ƿ�̯���"+chaifbl_dtyc, "�������Ƿ�̯���"+chaifbl_dtyc, "�������Ƿ�̯���"+chaifbl_dtyc};
			ArrHeader[1] = new String[]{"��ú��λ","����","������","жú<br>�ص�","����","����ú��<br>(��)","�˾�<br>(Km)","�˼�<br>(Ԫ/��)","�˷�<br>(Ԫ)","��ж<br>����","���<br>(Ԫ)","�ϼ�",
				"ú��<br>(��)","�˷�<br>(Ԫ)","ж����<br>(Ԫ)","���<br>(Ԫ)", "ú��<br>(��)", "�˷�<br>(Ԫ)", "ж����<br>(Ԫ)", "���<br>(Ԫ)"};
			ArrWidth = new int[] {95, 75, 80, 40, 30, 50, 35, 50, 65, 35, 40, 65, 45, 60, 45, 65, 45, 60, 45, 65};
			
			ResultSetList rslData =  con.getResultSetList(sql);
			rt.setBody(new Table(rslData, 2, 0, 4));
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(23);
			rt.body.setHeaderData(ArrHeader);
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			
			for(int i = 2; i < rt.body.getRows(); i ++){
				rt.body.merge(i, 2, i, 4);
			}
			rt.body.merge(rt.body.getRows(), 1, rt.body.getRows(), 4);
			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			rt.body.setColAlign(3, Table.ALIGN_CENTER);
			rt.body.setColAlign(4, Table.ALIGN_CENTER);
			
			if (!yunsdw.equals("")) {
				yunsdw = "��" + getYunsdwValue().getValue();
			}
			rt.setTitle("���ǵ糧"+ yunsdw +"��·����ú��(��ʵ����)���˷Ѻ˶Ա�", ArrWidth);
			rt.setDefaultTitle(1, 2, "��ѯʱ�䣺"+ getNianfValue().getValue() +"��"+ getYuefValue().getValue() +"��", Table.ALIGN_CENTER);
			if (!hetbh.equals("")) {
				rt.setDefaultTitle(4, 12, "��ͬ��ţ�" + hetbh.substring(0, hetbh.lastIndexOf(",")), Table.ALIGN_CENTER);
			}
			rt.setDefaultTitle(16, 4, "���䵥λ��" + getYunsdwValue().getValue(), Table.ALIGN_CENTER);
			
			hetbh_rsl.close();
			chaifbl_rsl.close();
			rslData.close();
			
			_CurrentPage = 1;
			_AllPages = 1;
			if (_AllPages == 0) {
				_CurrentPage = 0;
			}
			if(rt.body.getPages() > 0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			allPagesHtml = rt.getAllPagesHtml();
			
		} else if (getJieslxValue().getValue().equals("ú�����")) {
			
			String meikjsbh = "";
			if (!getMeikjsbhValue().getValue().equals("ȫ��")) {
				meikjsbh = " and jszb.bianm = '"+ getMeikjsbhValue().getValue() +"'";
			}
			
			sql = 
				"select /*grouping(ycgj.ysfs) ysfs, grouping(ycgj.gysmc) gmc, grouping(ycgj.mkmc) mkmc, grouping(js.bianm) bm, grouping(to_char(js.yansksrq, 'dd')||'-'||to_char(js.yansjzrq, 'dd')||'��') riq,*/\n" +
				"       decode(grouping(ycgj.ysfs), 1, '����ȼ���ܼ�', ycgj.ysfs) ysfs,\n" + 
				"       decode(grouping(ycgj.ysfs) + grouping(ycgj.gysmc), 1, ycgj.ysfs||'�ܼ�', 2, '����ȼ���ܼ�', ycgj.gysmc) gysmc,\n" + 
				"       decode(grouping(ycgj.ysfs) + grouping(ycgj.gysmc) + grouping(ycgj.mkmc), 1, '�ϼ�', 2, ycgj.ysfs||'�ܼ�', 3, '����ȼ���ܼ�', ycgj.mkmc) mkmc,\n" + 
				"       decode(grouping(ycgj.ysfs) + grouping(ycgj.gysmc) + grouping(ycgj.mkmc) + grouping(js.bianm), 1, 'С��', 2, '�ϼ�', 3, ycgj.ysfs||'�ܼ�', 4, '����ȼ���ܼ�', js.bianm) bianm,\n" +
				"       decode(grouping(ycgj.ysfs) + grouping(ycgj.gysmc) + grouping(ycgj.mkmc) + grouping(to_char(js.yansksrq, 'dd')||'-'||to_char(js.yansjzrq, 'dd')||'��'), 1, 'С��', 2, '�ϼ�', 3, ycgj.ysfs||'�ܼ�', 4, '����ȼ���ܼ�', to_char(js.yansksrq, 'dd')||'-'||to_char(js.yansjzrq, 'dd')||'��') as gongmsj,\n" + 
				"       max(js.yunj) yunju,\n" + 
				"       sum(js.ches) ches,\n" + 
				"       sum(getjiesdzb('jiesb', js.id, '��������', 'changf')) as yanssl,\n" + 
				"       sum(js.koud) koud,\n" + 
				"       sum(js.jiessl) jiessl,\n" + 
				"       round_new(sum(getjiesdzb('jiesb', js.id, '��������', 'changf') * js.jieslf) / sum(getjiesdzb('jiesb', js.id, '��������', 'changf')), 2) as jieslf,\n" + 
				"       round_new(sum(getjiesdzb('jiesb', js.id, '��������', 'changf') * js.jiesrl) / sum(getjiesdzb('jiesb', js.id, '��������', 'changf')), 0) as  jiesrl,\n" + 
				"       round_new(sum(js.jiessl * js.hetj) / decode(sum(js.jiessl), 0, 1, sum(js.jiessl)), 2) as hetj,\n" + 
				"       '' as yirzjkdj, --����ֵ��(+)��(-)����(Ԫ/��)\n" + 
				"       round_new(sum(js.jiessl * js.hansdj) / decode(sum(js.jiessl), 0, 1, sum(js.jiessl)), 2) as hansdj,\n" + 
				"       sum(js.hansmk) hansmk,\n" + 
				"       sum(ycgj.jiessl) ycgj_jiessl,\n" + 
				"       sum(round_new(ycgj.jiessl * js.hansdj, 2)) as ycgj_jine,\n" + 
				"       sum(dtyc.jiessl) dtyc_jiessl,\n" + 
				"       sum(round_new(dtyc.jiessl * js.hansdj, 2)) as dtyc_jine\n" + 
				"  from (select ysfs.mingc ysfs, gys.mingc gysmc, mk.mingc mkmc, jszb.*\n" + 
				"          from jieszb jszb, gongysb gys, meikxxb mk, yunsfsb ysfs\n" + 
				"         where to_char(jszb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm')\n" + gongysOrMeik + "\n" +
				"           and jszb.gongysb_id = gys.id\n" + 
				"           and jszb.meikxxb_id = mk.id\n" + 
				"           and jszb.yunsfsb_id = ysfs.id\n" + shoukdw + meikjsbh + "\n" +
				"           and jszb.danw = '���ǹ���') ycgj,\n" + 
				"\n" + 
				"       (select ysfs.mingc ysfs, gys.mingc gysmc, mk.mingc mkmc, jszb.*\n" + 
				"          from jieszb jszb, gongysb gys, meikxxb mk, yunsfsb ysfs\n" + 
				"         where to_char(jszb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm')\n" + gongysOrMeik + "\n" +
				"           and jszb.gongysb_id = gys.id\n" + 
				"           and jszb.meikxxb_id = mk.id\n" + 
				"           and jszb.yunsfsb_id = ysfs.id\n" + shoukdw + meikjsbh + "\n" +
				"           and jszb.danw = '��������') dtyc,\n" + 
				"       jiesb js\n" + 
				" where ycgj.bianm = dtyc.bianm\n" + 
				"   and ycgj.bianm = js.bianm\n" + 
				"   and dtyc.bianm = js.bianm\n" + 
				"group by rollup (ycgj.ysfs, ycgj.gysmc, ycgj.mkmc, js.bianm, to_char(js.yansksrq, 'dd')||'-'||to_char(js.yansjzrq, 'dd')||'��')\n" +
				"having not grouping(js.bianm) + grouping(to_char(js.yansksrq, 'dd')||'-'||to_char(js.yansjzrq, 'dd')||'��') = 1";
			
//			��ȡ��ͬ���
			String hetbh_sql = 
				"select ht.hetbh\n" +
				"  from hetb ht\n" + 
				" where ht.id in\n" + 
				"       (select jszb.hetb_id\n" + 
				"          from jieszb jszb, gongysb gys, meikxxb mk, jiesb js\n" + 
				"         where to_char(jszb.jiesrq, 'yyyy-mm') =\n" + 
				"               to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm')\n" + 
				"           and jszb.gongysb_id = gys.id\n" + 
				"           and jszb.meikxxb_id = mk.id\n" + 
				"           and jszb.bianm = js.bianm)";
			
			ResultSetList hetbh_rsl = con.getResultSetList(hetbh_sql);
			String hetbh = "";
			while (hetbh_rsl.next()) {
				hetbh += hetbh_rsl.getString("hetbh") + ",&nbsp;";
			}
			
//			��ȡ��ֱ���
			String chaifbl_sql = 
				"select jszb.danw, max(jszb.chaifbl) chaifbl\n" +
				"  from jieszb jszb, gongysb gys, meikxxb mk, jiesb js\n" + 
				" where to_char(jszb.jiesrq, 'yyyy-mm') =\n" + 
				"       to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm')\n" + 
				"   and jszb.gongysb_id = gys.id\n" + 
				"   and jszb.meikxxb_id = mk.id\n" + 
				"   and jszb.bianm = js.bianm\n" + 
				"group by jszb.danw\n" + 
				"order by jszb.danw";

			ResultSetList chaifbl_rsl = con.getResultSetList(chaifbl_sql);
			String chaifbl_dtyc = "";
			String chaifbl_ycgj = "";
			while (chaifbl_rsl.next()) {
				if (chaifbl_rsl.getString("danw").equals("��������")) {
					chaifbl_dtyc = chaifbl_rsl.getString("chaifbl") + "%";
				} else {
					chaifbl_ycgj = chaifbl_rsl.getString("chaifbl") + "%";
				}
			}
			
			ArrHeader = new String[2][20];
			ArrHeader[0] = new String[]{"����<br>��ʽ","��ú��λ","���","������","��úʱ��","�˾�<br>(Km)","����","��ú��(��)","����<br>(��)","������<br>(��)","St,ad<br>(%)","Qnet,ar(��/��)","��ͬ����<br>(Ԫ/��)","����ֵ<br>���۵���<br>(Ԫ/��)","�����<br>(Ԫ/��)","���(Ԫ)","���ǹ��ʷ�̯"+chaifbl_ycgj,"���ǹ��ʷ�̯"+chaifbl_ycgj,"�������Ƿ�̯"+chaifbl_dtyc,"�������Ƿ�̯"+chaifbl_dtyc};
			ArrHeader[1] = new String[]{"����<br>��ʽ","��ú��λ","���","������","��úʱ��","�˾�<br>(Km)","����","��ú��(��)","����<br>(��)","������<br>(��)","St,ad<br>(%)","Qnet,ar(��/��)","��ͬ����<br>(Ԫ/��)","����ֵ<br>���۵���<br>(Ԫ/��)","�����<br>(Ԫ/��)","���(Ԫ)","ú��(��)","���(Ԫ)","ú��(��)","���(Ԫ)"};
			ArrWidth = new int[] {35, 95, 75, 80, 55, 32, 32, 55, 45, 55, 38, 38, 40, 35, 40, 70, 65, 70, 65, 70};
			
			ResultSetList rslData = con.getResultSetList(sql);
			rt.setBody(new Table(rslData, 2, 0, 5));
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(20);
			rt.body.setHeaderData(ArrHeader);
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			
			for(int i = 2; i < rt.body.getRows(); i ++){
				rt.body.merge(i, 2, i, 5);
			}
			rt.body.merge(rt.body.getRows(), 1, rt.body.getRows(), 5);
			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			rt.body.setColAlign(3, Table.ALIGN_CENTER);
			rt.body.setColAlign(4, Table.ALIGN_CENTER);
			rt.body.setColAlign(5, Table.ALIGN_CENTER);
			
			if (!shoukdw.equals("")) {
				shoukdw = "��" + getShoukdwValue().getValue();
			}
			rt.setTitle("���ǵ糧"+ shoukdw +"ú�����˶Ա�", ArrWidth);
			rt.setDefaultTitle(1, 2, "��ѯʱ�䣺"+ getNianfValue().getValue() +"��"+ getYuefValue().getValue() +"��", Table.ALIGN_CENTER);
			if (!hetbh.equals("")) {
				rt.setDefaultTitle(3, 17, "��ͬ��ţ�" + hetbh.substring(0, hetbh.lastIndexOf(",")), Table.ALIGN_RIGHT);
			}
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 6, "ȷ��˫����"+((Visit)this.getPage().getVisit()).getDiancqc()+"ȼ�ϲ�<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+getShoukdwValue().getValue(), Table.ALIGN_LEFT);
			rt.setDefautlFooter(7, 4, "ͳ��Ա��", Table.ALIGN_LEFT);
			rt.setDefautlFooter(11, 2, "���Ա��", Table.ALIGN_CENTER);
			rt.setDefautlFooter(16, 4, "�˶����ڣ�", Table.ALIGN_LEFT);
			
			hetbh_rsl.close();
			chaifbl_rsl.close();
			rslData.close();

			_CurrentPage = 1;
			_AllPages = 1;
			if (_AllPages == 0) {
				_CurrentPage = 0;
			}
			if(rt.body.getPages() > 0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			allPagesHtml = rt.getAllPagesHtml();
		}

		con.Close();
		return allPagesHtml;
	}
	
	/**
	 * ȡ��ú����㵥��֣���ȡ���˷ѽ��㵥���
	 */
	public void quxcf() {
		
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		if (getJieslxValue().getValue().equals("ú�����")) {
			sbsql.append("delete from jieszb jszb where jszb.bianm = '"+ getMeikjsbhValue().getValue() +"';\n");
		} else {
			sbsql.append("delete from jiesyfzb yfzb where yfzb.bianm = '"+ getYunfjsbhValue().getValue() +"';");
		}
		sbsql.append("end;");
		
		if (con.getUpdate(sbsql.toString()) == 1) {
			setMsg("ȡ����ֳɹ���");
			this.setMeikjsbh(true); // ��ʶ�Ƿ���ˢ��ҳ��ʱ���ع�ú�������������������ݣ�trueΪ�ǣ�falseΪ��
			this.setYunfjsbh(true); // ��ʶ�Ƿ���ˢ��ҳ��ʱ���ع��˷ѽ�����������������ݣ�trueΪ�ǣ�falseΪ��
		} else {
			setMsg("ȡ�����ʧ�ܣ�");
		}
		con.Close();
	}
	
	/**
	 * ȡ����Ʊ���㵥���
	 */
	public void quxlpcf() {
		
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		if (getJieslxValue().getValue().equals("ú�����")) {
			sbsql.append("delete from jieszb jszb where jszb.bianm = '"+ getMeikjsbhValue().getValue() +"';\n");
			sbsql.append("delete from jiesyfzb yfzb where yfzb.bianm = '"+ getMeikjsbhValue().getValue() +"';");
		} else {
			sbsql.append("delete from jieszb jszb where jszb.bianm = '"+ getYunfjsbhValue().getValue() +"';\n");
			sbsql.append("delete from jiesyfzb yfzb where yfzb.bianm = '"+ getYunfjsbhValue().getValue() +"';");
		}
		sbsql.append("end;");
		
		if (con.getUpdate(sbsql.toString()) == 1) {
			setMsg("ȡ����ֳɹ���");
			this.setMeikjsbh(true); // ��ʶ�Ƿ���ˢ��ҳ��ʱ���ع�ú�������������������ݣ�trueΪ�ǣ�falseΪ��
			this.setYunfjsbh(true); // ��ʶ�Ƿ���ˢ��ҳ��ʱ���ع��˷ѽ�����������������ݣ�trueΪ�ǣ�falseΪ��
		} else {
			setMsg("ȡ�����ʧ�ܣ�");
		}
		con.Close();
	}
	
	public void getSelectData() {
		
		Toolbar tbr = new Toolbar("tbdiv");
		
		tbr.addText(new ToolbarText("��ݣ�"));
		ComboBox cbx_nf = new ComboBox();
		cbx_nf.setTransform("Nianf");
		cbx_nf.setWidth(55);
		cbx_nf.setListWidth(55);
		cbx_nf.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		tbr.addField(cbx_nf);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("�·ݣ�"));
		ComboBox cbx_yf = new ComboBox();
		cbx_yf.setTransform("Yuef");
		cbx_yf.setWidth(45);
		cbx_yf.setListWidth(45);
		cbx_yf.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		tbr.addField(cbx_yf);
		tbr.addText(new ToolbarText("-"));
		
		String condition = "";
		if (getJieslxValue().getValue().equals("��Ʊ����")) {
			condition = "jieszb jszb, jiesyfzb yfzb & " +
				"and gys.id = jszb.gongysb_id and gys.id = yfzb.gongysb_id " +
				"and to_char(jszb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm') " +
				"and to_char(yfzb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm') && " +
				"and gys.id = jszb.gongysb_id and m.id = jszb.meikxxb_id and gys.id = yfzb.gongysb_id and m.id = yfzb.meikxxb_id " +
				"and to_char(jszb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm') " +
				"and to_char(yfzb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm') ";
		} else if (getJieslxValue().getValue().equals("ú�����")) {
			condition = "jieszb jszb & " +
				"and gys.id = jszb.gongysb_id and to_char(jszb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm') &&" +
				"and gys.id = jszb.gongysb_id and m.id = jszb.meikxxb_id and to_char(jszb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm') ";
		} else if (getJieslxValue().getValue().equals("�˷ѽ���")) {
			condition = "jiesyfzb yfzb & " +
				"and gys.id = yfzb.gongysb_id and to_char(yfzb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm') &&" +
				"and gys.id = yfzb.gongysb_id and m.id = yfzb.meikxxb_id and to_char(yfzb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm') ";
		}
		
		ExtTreeUtil etu = new ExtTreeUtil("gongysTree", ExtTreeUtil.treeWindowCheck_gongys_jieszb, 
			((Visit) this.getPage().getVisit()).getDiancxxb_id(), getTreeid(), condition, false);
		setTree(etu);
		
		TextField tf = new TextField();
		tf.setId("gongysTree_text");
		tf.setWidth(90);
		
		ToolbarButton btn = new ToolbarButton(null, null,"function(){gongysTree_window.show();}");
		btn.setIcon("ext/resources/images/list-items.gif");
		btn.setCls("x-btn-icon");
		btn.setMinWidth(20);
		tbr.addText(new ToolbarText("������λ��"));
		tbr.addField(tf);
		tbr.addItem(btn);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("�������ͣ�"));
		ComboBox jieslx = new ComboBox();
		jieslx.setWidth(80);
		jieslx.setTransform("Jieslx");
		jieslx.setId("jieslx");
		jieslx.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		jieslx.setLazyRender(true);
		jieslx.setEditable(false);
		tbr.addField(jieslx);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("�տλ��"));
		ComboBox combx = new ComboBox();
		combx.setWidth(90);
		combx.setListWidth(250);
		if (getJieslxValue().getValue().equals("ú�����")) {
			combx.setTransform("Shoukdw");
			combx.setId("shoukdw");
		} else {
			combx.setTransform("Yunsdw");
			combx.setId("yunsdw");
		}
		combx.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		combx.setLazyRender(true);
		combx.setEditable(false);
		tbr.addField(combx);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("�����ţ�"));
		ComboBox jiesbh = new ComboBox();
		jiesbh.setWidth(90);
		jiesbh.setListWidth(150);
		if (getJieslxValue().getValue().equals("ú�����")) {
			jiesbh.setTransform("Meikjsbh");
		} else {
			jiesbh.setTransform("Yunfjsbh");
		}
		jiesbh.setId("jsbh");
		jiesbh.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		jiesbh.setLazyRender(true);
		jiesbh.setEditable(false);
		tbr.addField(jiesbh);
		tbr.addText(new ToolbarText("-"));
		
		ToolbarButton chaifbtn = new ToolbarButton("chaif_Button", "ȡ�����", getButtonHandler("test"));
		chaifbtn.setIcon(SysConstant.Btn_Icon_Cancel);
		tbr.addItem(chaifbtn);
		tbr.addText(new ToolbarText("-"));
		
		ToolbarButton tbrtn = new ToolbarButton(null, "��ѯ", "function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Search);
		tbr.addItem(tbrtn);
		setToolbar(tbr);
	}
	
	/**
	 * ����"ȡ�����"��ť��handler�����Ҫȡ����ֵĽ��㵥Ϊ��Ʊ���㵥ʱִ��quxlpcf()������
	 * ���Ϊú����㵥���˷ѽ��㵥����ôִ��quxcf()������
	 * @param buttonName
	 * @return
	 */
	public String getButtonHandler(String buttonName) {
		
		JDBCcon con = new JDBCcon();
		String str = "    	 document.getElementById('QuxcfButton').click();";
		
		if (getJieslxValue().getValue().equals("ú�����")) {
			ResultSetList jieslx_rsl = con.getResultSetList("select distinct jszb.jieslx from jieszb jszb where jszb.bianm = '"+ getMeikjsbhValue().getValue() +"'");
			if (jieslx_rsl.next()) {
				if (jieslx_rsl.getString("jieslx").equals("1")) {
					str = 					
					"    Ext.MessageBox.confirm('��ʾ��Ϣ','�ý��㵥Ϊ��Ʊ���㣬�Ƿ�Ҳȡ������˷ѽ��㵥��',\n" + 
					"        function(btn){\n" + 
					"            if(btn == 'yes'){\n" + 
					"                document.getElementById('QuxlpcfButton').click();\n" + 
					"                Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...'," +
					"                width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n" + 
					"            };\n" + 
					"        }\n" + 
					"    );";
				}
			}
			jieslx_rsl.close();
		} else {
			ResultSetList jieslx_rsl = con.getResultSetList("select distinct yfzb.jieslx from jiesyfzb yfzb where yfzb.bianm = '"+ getYunfjsbhValue().getValue() +"'");
			if (jieslx_rsl.next()) {
				if (jieslx_rsl.getString("jieslx").equals("1")) {
					str = 					
					"    Ext.MessageBox.confirm('��ʾ��Ϣ','�ý��㵥Ϊ��Ʊ���㣬�Ƿ�Ҳȡ�����ú����㵥��',\n" + 
					"        function(btn){\n" + 
					"            if(btn == 'yes'){\n" + 
					"                document.getElementById('QuxlpcfButton').click();\n" + 
					"                Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...'," +
					"                width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n" + 
					"            };\n" + 
					"        }\n" + 
					"    );";
				}
			}
			jieslx_rsl.close();
		}
		
		String handler = 
			"function(){\n" +
			"    if(jsbh.getRawValue()=='ȫ��'){\n" + 
			"        Ext.MessageBox.alert('��ʾ��Ϣ','��ѡһ�������ţ�');\n" + 
			"    }else{\n" + 
					str + 
			"    }\n" + 
			"}";
		
		con.Close();
		return handler;
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
	
//	��Ӧ����_��ʼ
	public String getTreeid() {
		if(((Visit) getPage().getVisit()).getString2() == null || ((Visit) getPage().getVisit()).getString2().equals("")){
			((Visit) getPage().getVisit()).setString2("0");
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		if(!((Visit) getPage().getVisit()).getString2().equals(treeid)){
			((Visit) getPage().getVisit()).setString2(treeid);
			this.setMeikjsbh(true); // ��ʶ�Ƿ���ˢ��ҳ��ʱ���ع�ú�������������������ݣ�trueΪ�ǣ�falseΪ��
			this.setYunfjsbh(true); // ��ʶ�Ƿ���ˢ��ҳ��ʱ���ع��˷ѽ�����������������ݣ�trueΪ�ǣ�falseΪ��
		}
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
//	��Ӧ����_����

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
			visit.setActivePageName(getPageName().toString());
			this.setTreeid(null);
			visit.setProSelectionModel2(null); // ���������
			visit.setDropDownBean2(null);
			visit.setProSelectionModel3(null); // �·�������
			visit.setDropDownBean3(null);
			visit.setProSelectionModel4(null); // ��������������
			visit.setDropDownBean4(null);
			visit.setProSelectionModel5(null); // ���䵥λ������
			visit.setDropDownBean5(null);
			visit.setProSelectionModel6(null); // �տλ������
			visit.setDropDownBean6(null);
			visit.setProSelectionModel7(null); // ú�������������
			visit.setDropDownBean7(null);
			visit.setProSelectionModel8(null); // �˷ѽ�����������
			visit.setDropDownBean8(null);
		}
		if (this.isMeikjsbh()) {
			visit.setProSelectionModel7(null);
			visit.setDropDownBean7(null);
			getMeikjsbhModels();
			this.setMeikjsbh(false); // ��ʶ�Ƿ���ˢ��ҳ��ʱ���ع�ú�������������������ݣ�trueΪ�ǣ�falseΪ��
		}
		if (this.isYunfjsbh()) {
			visit.setProSelectionModel8(null);
			visit.setDropDownBean8(null);
			getYunfjsbhModels();
			this.setYunfjsbh(false); // ��ʶ�Ƿ���ˢ��ҳ��ʱ���ع��˷ѽ�����������������ݣ�trueΪ�ǣ�falseΪ��
		}
		getSelectData();
	}

}