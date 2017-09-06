package com.zhiren.dc.monthReport;

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

/**
 * @author yinjm
 * �������³ɱ�����
 */

public class YuecbReport extends BasePage implements PageValidateListener {
	
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
	
//	�ھ�������_��ʼ
	public IDropDownBean getKoujValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean4() == null) {
			if (getKoujModel().getOptionCount() > 0) {
				setKoujValue((IDropDownBean) getKoujModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean4();
	}

	public void setKoujValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean4(LeibValue);
	}

	public IPropertySelectionModel getKoujModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
			setKoujModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setKoujModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(value);
	}

	public void setKoujModels() {
		ArrayList list = new ArrayList();
		list.add(new IDropDownBean(1, "ú��"));
		list.add(new IDropDownBean(2, "�ƻ��ھ�"));
		setKoujModel(new IDropDownModel(list));
	}
//	�ھ�������_����
	
	private boolean _RefurbishChick = false;
	
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}
	
	/**
	 * �жϴ���ĵ糧id�Ƿ����¼��糧
	 * @param con
	 * @param id
	 * @return
	 */
	public boolean hasDianc_id(JDBCcon con, String id) {
		boolean hasDianc_id = false;
		String sql = "select id from diancxxb where fuid = " + id;
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.getRows() > 0) {
			hasDianc_id = true;
		}
		return hasDianc_id;
	}
	
	public String getPrintTable() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		
		String diancxxb_id = "dc.id = " + getTreeid();
		if (visit.isFencb()){
			if (hasDianc_id(con, getTreeid())) {
				diancxxb_id = "dc.fuid = " + getTreeid();
			}
		}
		String kouj = "meikxxb";
		if (getKoujValue().getValue().equals("�ƻ��ھ�")) {
			kouj = "jihkjb";
		}
		
		String hej = "";
		if (visit.isFencb()) {
			hej = 			
				"    select * from (\n" + 
				"        select 2 as mkmc, '�ֹ�˾�ϼ�' as mingc, null as niancjcsl, null as niancjcdj, null as niancjcje, null as caigsl, null as yunj, null as caigfrl,\n" + 
				"              null as caigje, null as kuangj, null as yunzf, null as changnfy, null as biaoml, null as biaomdj, null as rulsl, null as ruldj,\n" + 
				"              null as rulje, null as rulzs, null as rulfrl, null as rulbml, null as meizbmdj, null as youzbmdj, null as zonghbmdj,\n" + 
				"              null as  rezc, null as qithysl, null as qithydj, null as qithyje, null as qimjysl, null as qimjydj, null as qimjyje from dual\n" + 
				"        union all\n" + 
				"        select grouping(kj.mingc) mkmc,\n" + 
				"               decode(kj.mingc, null, '&nbsp;&nbsp;&nbsp;ȼúС��', '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||kj.mingc) mingc, sum(rm.niancjcsl) niancjcsl, sum(rm.niancjcdj) niancjcdj,\n" + 
				"               sum(rm.niancjcje) niancjcje, sum(rm.caigsl) caigsl,\n" + 
				"               sum(rm.yunj) yunj, sum(rm.caigfrl) caigfrl, sum(rm.caigje) caigje, sum(rm.kuangj) kuangj, sum(rm.yunzf) yunzf,\n" + 
				"               sum(rm.changnfy) changnfy, sum(rm.biaoml) biaoml, sum(rm.biaomdj) biaomdj, sum(rm.rulsl) rulsl, sum(rm.ruldj) ruldj,\n" + 
				"               sum(rm.rulje) rulje, sum(rm.rulzs) rulzs, sum(rm.rulfrl) rulfrl, sum(rm.rulbml) rulbml, sum(rm.meizbmdj) meizbmdj,\n" + 
				"               sum(rm.youzbmdj) youzbmdj, sum(rm.zonghbmdj) zonghbmdj, sum(rm.rezc) rezc, sum(rm.qithysl) qithysl, sum(rm.qithydj) qithydj,\n" + 
				"               sum(rm.qithyje) qithyje, sum(rm.qimjysl) qimjysl, sum(rm.qimjydj) qimjydj, sum(rm.qimjyje) qimjyje\n" + 
				"          from yuecbrmb rm, "+ kouj +" kj, diancxxb dc\n" + 
				"         where to_date(to_char(rm.riq, 'yyyy-mm'), 'yyyy-mm') = to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"', 'yyyy-mm')\n" + 
				"           and rm."+ kouj +"_id = kj.id\n" + 
				"           and rm.diancxxb_id = dc.id\n" + 
				"           and "+ diancxxb_id +"\n" + 
				"        group by rollup (kj.mingc)\n" + 
				"        union all\n" + 
				"        select -1 as mkmc, '&nbsp;&nbsp;&nbsp;ȼ��' as mingc, sum(ry.niancjcsl) niancjcsl, sum(ry.niancjcdj) niancjcdj, sum(ry.niancjcje) niancjcje, sum(ry.caigsl) caigsl,\n" + 
				"               sum(ry.yunj) yunj, sum(ry.caigfrl) caigfrl, sum(ry.caigje) caigje, sum(ry.kuangj) kuangj, sum(ry.yunzf) yunzf,\n" + 
				"               sum(ry.changnfy) changnfy, sum(ry.biaoml) biaoml, sum(ry.biaomdj) biaomdj, sum(ry.rulsl) rulsl, sum(ry.ruldj) ruldj,\n" + 
				"               sum(ry.rulje) rulje, sum(ry.rulzs) rulzs, sum(ry.rulfrl) rulfrl, sum(ry.rulbml) rulbml, sum(ry.meizbmdj) meizbmdj,\n" + 
				"               sum(ry.youzbmdj) youzbmdj, sum(ry.zonghbmdj) zonghbmdj, sum(ry.rezc) rezc, sum(ry.qithysl) qithysl, sum(ry.qithydj) qithydj,\n" + 
				"               sum(ry.qithyje) qithyje, sum(ry.qimjysl) qimjysl, sum(ry.qimjydj) qimjydj, sum(ry.qimjyje) qimjyje\n" + 
				"          from yuecbryb ry, diancxxb dc\n" + 
				"         where to_date(to_char(ry.riq, 'yyyy-mm'), 'yyyy-mm') = to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"', 'yyyy-mm')\n" + 
				"           and ry.diancxxb_id = dc.id\n" + 
				"           and "+ diancxxb_id +"\n" + 
				"        order by mkmc desc, mingc) a\n" + 
				"    union all\n";
		}
		
		String sql = 
			"select mingc, rownum as xuh, niancjcsl, niancjcdj, niancjcje, caigsl, yunj, caigfrl, caigje, kuangj+yunzf+changnfy as hej, kuangj, yunzf, changnfy,\n" +
			"       biaoml, biaomdj, rulsl, ruldj, rulje, rulzs, rulfrl, rulbml, meizbmdj, youzbmdj, zonghbmdj, rezc,\n" + 
			"       qithysl, qithydj, qithyje, qimjysl, qimjydj, qimjyje from (\n" + hej +
			"    select * from (\n" + 
			"        select  grouping(d.mingc) mkmc, decode(d.mingc, null, max(dc.mingc), d.mingc) mingc,\n" + 
			"               to_number(decode(grouping(d.mc), 1, null, max(d.niancjcsl))) niancjcsl, to_number(decode(grouping(d.mc), 1, null, max(d.niancjcdj))) niancjcdj,\n" + 
			"               to_number(decode(grouping(d.mc), 1, null, max(d.niancjcje))) niancjcje, to_number(decode(grouping(d.mc), 1, null, max(d.caigsl))) caigsl,\n" + 
			"               to_number(decode(grouping(d.mc), 1, null, max(d.yunj))) yunj, to_number(decode(grouping(d.mc), 1, null, max(d.caigfrl))) caigfrl,\n" + 
			"               to_number(decode(grouping(d.mc), 1, null, max(d.caigje))) caigje, to_number(decode(grouping(d.mc), 1, null, max(d.kuangj))) kuangj,\n" + 
			"               to_number(decode(grouping(d.mc), 1, null, max(d.yunzf))) yunzf, to_number(decode(grouping(d.mc), 1, null, max(d.changnfy))) changnfy,\n" + 
			"               to_number(decode(grouping(d.mc), 1, null, max(d.biaoml))) biaoml, to_number(decode(grouping(d.mc), 1, null, max(d.biaomdj))) biaomdj,\n" + 
			"               to_number(decode(grouping(d.mc), 1, null, max(d.rulsl))) rulsl, to_number(decode(grouping(d.mc), 1, null, max(d.ruldj))) ruldj,\n" + 
			"               to_number(decode(grouping(d.mc), 1, null, max(d.rulje))) rulje, to_number(decode(grouping(d.mc), 1, null, max(d.rulzs))) rulzs,\n" + 
			"               to_number(decode(grouping(d.mc), 1, null, max(d.rulfrl))) rulfrl, to_number(decode(grouping(d.mc), 1, null, max(d.rulbml))) rulbml,\n" + 
			"               to_number(decode(grouping(d.mc), 1, null, max(d.meizbmdj))) meizbmdj, to_number(decode(grouping(d.mc), 1, null, max(d.youzbmdj))) youzbmdj,\n" + 
			"               to_number(decode(grouping(d.mc), 1, null, max(d.zonghbmdj))) zonghbmdj, to_number(decode(grouping(d.mc), 1, null, max(d.rezc))) rezc,\n" + 
			"               to_number(decode(grouping(d.mc), 1, null, max(d.qithysl))) qithysl, to_number(decode(grouping(d.mc), 1, null, max(d.qithydj))) qithydj,\n" + 
			"               to_number(decode(grouping(d.mc), 1, null, max(d.qithyje))) qithyje, to_number(decode(grouping(d.mc), 1, null, max(d.qimjysl))) qimjysl,\n" + 
			"               to_number(decode(grouping(d.mc), 1, null, max(d.qimjydj))) qimjydj, to_number(decode(grouping(d.mc), 1, null, max(d.qimjyje))) qimjyje from (\n" + 
			"            select rm.diancxxb_id, grouping(kj.mingc) mc,\n" + 
			"                   decode(kj.mingc, null, '&nbsp;&nbsp;&nbsp;ȼúС��', '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||kj.mingc) mingc,\n" + 
			"                   sum(rm.niancjcsl) niancjcsl, sum(rm.niancjcdj) niancjcdj, sum(rm.niancjcje) niancjcje, sum(rm.caigsl) caigsl,\n" + 
			"                   sum(rm.yunj) yunj, sum(rm.caigfrl) caigfrl, sum(rm.caigje) caigje, sum(rm.kuangj) kuangj, sum(rm.yunzf) yunzf,\n" + 
			"                   sum(rm.changnfy) changnfy, sum(rm.biaoml) biaoml, sum(rm.biaomdj) biaomdj, sum(rm.rulsl) rulsl, sum(rm.ruldj) ruldj,\n" + 
			"                   sum(rm.rulje) rulje, sum(rm.rulzs) rulzs, sum(rm.rulfrl) rulfrl, sum(rm.rulbml) rulbml, sum(rm.meizbmdj) meizbmdj,\n" + 
			"                   sum(rm.youzbmdj) youzbmdj, sum(rm.zonghbmdj) zonghbmdj, sum(rm.rezc) rezc, sum(rm.qithysl) qithysl, sum(rm.qithydj) qithydj,\n" + 
			"                   sum(rm.qithyje) qithyje, sum(rm.qimjysl) qimjysl, sum(rm.qimjydj) qimjydj, sum(rm.qimjyje) qimjyje\n" + 
			"              from yuecbrmb rm, "+ kouj +" kj, diancxxb dc\n" + 
			"             where to_date(to_char(rm.riq, 'yyyy-mm'), 'yyyy-mm') = to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"', 'yyyy-mm')\n" + 
			"               and rm."+ kouj +"_id = kj.id\n" + 
			"               and rm.diancxxb_id = dc.id\n" + 
			"               and "+ diancxxb_id +"\n" + 
			"            group by rollup(rm.diancxxb_id, kj.mingc)\n" + 
			"            having not (grouping(rm.diancxxb_id)+grouping(kj.mingc)=2)\n" + 
			"            union all\n" + 
			"            select ry.diancxxb_id, -1 as mc, '&nbsp;&nbsp;&nbsp;ú��' as mingc,\n" + 
			"                   sum(ry.niancjcsl) niancjcsl, sum(ry.niancjcdj) niancjcdj, sum(ry.niancjcje) niancjcje, sum(ry.caigsl) caigsl,\n" + 
			"                   sum(ry.yunj) yunj, sum(ry.caigfrl) caigfrl, sum(ry.caigje) caigje, sum(ry.kuangj) kuangj, sum(ry.yunzf) yunzf,\n" + 
			"                   sum(ry.changnfy) changnfy, sum(ry.biaoml) biaoml, sum(ry.biaomdj) biaomdj, sum(ry.rulsl) rulsl, sum(ry.ruldj) ruldj,\n" + 
			"                   sum(ry.rulje) rulje, sum(ry.rulzs) rulzs, sum(ry.rulfrl) rulfrl, sum(ry.rulbml) rulbml, sum(ry.meizbmdj) meizbmdj,\n" + 
			"                   sum(ry.youzbmdj) youzbmdj, sum(ry.zonghbmdj) zonghbmdj, sum(ry.rezc) rezc, sum(ry.qithysl) qithysl, sum(ry.qithydj) qithydj,\n" + 
			"                   sum(ry.qithyje) qithyje, sum(ry.qimjysl) qimjysl, sum(ry.qimjydj) qimjydj, sum(ry.qimjyje) qimjyje\n" + 
			"              from yuecbryb ry, diancxxb dc\n" + 
			"             where to_date(to_char(ry.riq, 'yyyy-mm'), 'yyyy-mm') = to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"', 'yyyy-mm')\n" + 
			"               and ry.diancxxb_id = dc.id\n" + 
			"               and "+ diancxxb_id +"\n" + 
			"             group by ry.diancxxb_id\n" + 
			"            order by diancxxb_id, mc desc, mingc) d, diancxxb dc\n" + 
			"        where d.diancxxb_id = dc.id\n" + 
			"        group by rollup (d.diancxxb_id, d.mc, d.mingc)\n" + 
			"        having (grouping(d.diancxxb_id)+grouping(d.mingc)=0 or grouping(d.diancxxb_id)+grouping(d.mc)=1)\n" + 
			"        order by d.diancxxb_id, grouping(d.mingc) desc, d.mc desc, d.mingc) b)";

		 String ArrHeader[][]=new String[3][31];
		 ArrHeader[0]=new String[] {"��         λ","�к�","���������","���������","���������","�ɹ����������˰��","�ɹ����������˰��","�ɹ����������˰��","�ɹ����������˰��","�ɹ����������˰��","�ɹ����������˰��","�ɹ����������˰��","�ɹ����������˰��","�ɹ����������˰��","�ɹ����������˰��","��¯���","��¯���","��¯���","��¯���","��¯���","��¯���","��¯���","��¯���","��¯���","��ֵ��        ��ǧ��/ǧ�ˣ�","��������","��������","��������","��ĩ����","��ĩ����","��ĩ����"};
		 ArrHeader[1]=new String[] {"��         λ","�к�","����           ���֣�","����   ��Ԫ/�֣�","���    ����Ԫ��","�������֣�","�˾�       �����","��λ��������ǧ��/ǧ�ˣ�","�ɹ�����Ԫ��","�ɹ��۸񵥼ۣ�Ԫ/�֣�","�ɹ��۸񵥼ۣ�Ԫ/�֣�","�ɹ��۸񵥼ۣ�Ԫ/�֣�","�ɹ��۸񵥼ۣ�Ԫ/�֣�","��ú�����֣�","��ú���ۣ�Ԫ/�֣�","�������֣�","����   ��Ԫ/�֣�","���    ����Ԫ��","���𣨶֣�","��λ��������ǧ��/ǧ�ˣ�","��ú��    ���֣�","ú�۱�ú���ۣ�Ԫ/�֣�","���۱�ú���ۣ�Ԫ/�֣�","�ۺϱ�ú���ۣ�Ԫ/�֣�","��ֵ��        ��ǧ��/ǧ�ˣ�","����           ���֣�","����   ��Ԫ/�֣�","���    ����Ԫ��","����          ���֣�","����   ��Ԫ/�֣�","���    ����Ԫ��"};
		 ArrHeader[2]=new String[] {"��         λ","�к�","����           ���֣�","����   ��Ԫ/�֣�","���    ����Ԫ��","�������֣�","�˾�       �����","��λ��������ǧ��/ǧ�ˣ�","�ɹ�����Ԫ��","�ϼ�","���","���ӷ�","���ڷ���","��ú�����֣�","��ú���ۣ�Ԫ/�֣�","�������֣�","����   ��Ԫ/�֣�","���    ����Ԫ��","���𣨶֣�","��λ��������ǧ��/ǧ�ˣ�","��ú��    ���֣�","ú�۱�ú���ۣ�Ԫ/�֣�","���۱�ú���ۣ�Ԫ/�֣�","�ۺϱ�ú���ۣ�Ԫ/�֣�","��ֵ��        ��ǧ��/ǧ�ˣ�","����           ���֣�","����   ��Ԫ/�֣�","���    ����Ԫ��","����          ���֣�","����   ��Ԫ/�֣�","���    ����Ԫ��"};

		 int ArrWidth[]=new int[] {150,30,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52};
		
		ResultSetList rslData =  con.getResultSetList(sql);
		rt.setTitle("�й����Ƽ��Ź�˾ȼ�ϳɱ���������", ArrWidth);
		rt.setBody(new Table(rslData, 3, 0, 1));
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(25);
		rt.body.setHeaderData(ArrHeader);
		
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		
		rt.setDefaultTitle(1, 5, "���Ƶ�λ��"+((Visit)this.getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(14, 4, DateUtil.Formatdate("yyyy��MM��dd��", new Date()), Table.ALIGN_CENTER);
		rt.setDefaultTitle(29, 3, "02��", Table.ALIGN_CENTER);
		
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
	
	public void getSelectData() {
		
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tbr = new Toolbar("tbdiv");
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win, "diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
		setTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getDiancmcModel())
			.getBeanValue(Long.parseLong(getTreeid() == null || "".equals(getTreeid()) ? "-1" : getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null, null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tbr.addText(new ToolbarText("�糧��"));
		tbr.addField(tf);
		tbr.addItem(tb2);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("��ݣ�"));
		ComboBox nf_comb = new ComboBox();
		nf_comb.setWidth(60);
		nf_comb.setListWidth(60);
		nf_comb.setTransform("Nianf");
		nf_comb.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		nf_comb.setLazyRender(true);
		tbr.addField(nf_comb);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("�·ݣ�"));
		ComboBox yf_comb = new ComboBox();
		yf_comb.setWidth(60);
		yf_comb.setListWidth(60);
		yf_comb.setTransform("Yuef");
		yf_comb.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		yf_comb.setLazyRender(true);
		tbr.addField(yf_comb);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("��Ӧ�̣�"));
		ComboBox kouj_comb = new ComboBox();
		kouj_comb.setTransform("Kouj");
		kouj_comb.setWidth(80);
		kouj_comb.setListWidth(100);
//		kouj_comb.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		kouj_comb.setLazyRender(true);
		tbr.addField(kouj_comb);
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
	
//	�糧��_��ʼ
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
//	�糧��_����

	public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			this.setTreeid(null);
			visit.setProSelectionModel2(null); // ���������
			visit.setDropDownBean2(null);
			visit.setProSelectionModel3(null); // �·�������
			visit.setDropDownBean3(null);
			visit.setProSelectionModel4(null); // �ھ�������
			visit.setDropDownBean4(null); 
		}
		getSelectData();
	}
}