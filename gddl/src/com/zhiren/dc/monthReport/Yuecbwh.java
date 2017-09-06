package com.zhiren.dc.monthReport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author yinjm
 * �������³ɱ�ά��
 */

public class Yuecbwh extends BasePage implements PageValidateListener {
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		msg = "";
	}
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
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
	
	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	
//	"����"��ť
	private boolean _CreateClick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
		}
		if (_CreateClick) {
			_CreateClick = false;
			createData();
		}
	}
	
	public void createData() {
		
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		String sql = 
			"select fh.meikxxb_id, fh.jihkjb_id, sum(fh.laimsl) caigsl,\n" +
			"       round_new(sum(fh.laimsl * ls.rez) / sum(decode(fh.laimsl, 0, 1, fh.laimsl)), 0) caigfrl,\n" + 
			"       round_new(sum(fh.laimsl * (ls.meij + ls.yunf + ls.zaf + ls.fazzf + ls.ditf)) / sum(decode(fh.laimsl, 0, 1, fh.laimsl)), 2) as caigje,\n" + 
			"       round_new(sum(fh.laimsl * ls.meij) / sum(decode(fh.laimsl, 0, 1, fh.laimsl)), 2) as kuangj,\n" + 
			"       round_new(sum(fh.laimsl * (ls.fazzf + ls.ditf)) / sum(decode(fh.laimsl, 0, 1, fh.laimsl)), 2) yunzf,\n" + 
			"       round_new(sum(fh.laimsl * ls.zaf) / sum(decode(fh.laimsl, 0, 1, fh.laimsl)), 2) changnfy,\n" + 
			"       round_new(sum(fh.laimsl * fh.jingz) / sum(decode(fh.laimsl, 0, 1, fh.laimsl)), 2) biaoml,\n" + 
			"       round_new(sum(fh.laimsl * round_new((ls.meij + ls.yunf + ls.zaf + ls.fazzf+ls.ditf) * 7000 / ls.rez, 2)) / sum(decode(fh.laimsl, 0, 1, fh.laimsl)), 2) biaomdj\n" + 
			"  from guslsb ls, fahb fh\n" + 
			" where ls.id in (select max(ls.id)\n" + 
			"                   from fahb fh, guslsb ls\n" + 
			"                  where to_date(to_char(fh.daohrq, 'yyyy-mm'), 'yyyy-mm') = to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"', 'yyyy-mm')\n" + 
			"                    and ls.fahb_id = fh.id\n" + 
			"                  group by ls.fahb_id)\n" + 
			"   and ls.fahb_id = fh.id\n" + 
			"group by fh.meikxxb_id, fh.jihkjb_id\n" + 
			"order by fh.meikxxb_id";

		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.getRows() > 0) {
			sbsql.append("delete from yuecbrmb rm where to_char(rm.riq, 'yyyy-mm') = '"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"';\n");
			while(rsl.next()) {
				sbsql.append("insert into yuecbrmb(id, diancxxb_id, meikxxb_id, jihkjb_id, riq, xuh, caigsl, caigfrl, caigje, kuangj, yunzf, changnfy, biaoml, biaomdj) values(")
				.append("getnewid(").append(getTreeid()).append("), ").append(getTreeid()).append(", ")
				.append(rsl.getString("meikxxb_id")).append(", ").append(rsl.getString("jihkjb_id")).append(", ")
				.append("to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd')").append(", ")
				.append("(select max(xuh)+1 from yuecbrmb)").append(", ").append(rsl.getString("caigsl")).append(", ")
				.append(rsl.getString("caigfrl")).append(", ").append(rsl.getString("caigje")).append(", ")
				.append(rsl.getString("kuangj")).append(", ").append(rsl.getString("yunzf")).append(", ")
				.append(rsl.getString("changnfy")).append(", ").append(rsl.getString("biaoml")).append(", ")
				.append(rsl.getString("biaomdj")).append(");\n");
			}
			sbsql.append("end;");
			con.getUpdate(sbsql.toString());
		} else {
			setMsg("ͬ��û�����ݣ��޷����ɣ�");
		}
		rsl.close();
		con.Close();
	}
	
	public void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			if (visit.getString1().equals("ranm")) {
				sbsql.append("delete from yuecbrmb where id = ").append(delrsl.getString("id")).append(";\n");
			} else {
				sbsql.append("delete from yuecbryb where id = ").append(delrsl.getString("id")).append(";\n");
			}
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			if("0".equals(mdrsl.getString("id"))) {
				if (visit.getString1().equals("ranm")) {
					sbsql.append("insert into yuecbrmb(id, diancxxb_id, meikxxb_id, jihkjb_id, riq, xuh, niancjcsl, niancjcdj, niancjcje, caigsl, yunj, caigfrl, \n" +
							"caigje, kuangj, yunzf, changnfy, biaoml, biaomdj, rulsl, ruldj, rulje, rulzs, rulfrl, rulbml, meizbmdj, youzbmdj, zonghbmdj, rezc,\n" + 
							"qithysl, qithydj, qithyje, qimjysl, qimjydj, qimjyje) values(").append("getnewid(").append(getTreeid()).append("), ").append(getTreeid())
							.append(", ").append((getExtGrid().getColumn("meikxxb_id").combo).getBeanId(mdrsl.getString("meikxxb_id")))
							.append(", ").append((getExtGrid().getColumn("jihkjb_id").combo).getBeanId(mdrsl.getString("jihkjb_id")))
							.append(", to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), ").append(mdrsl.getString("xuh"))
							.append(", ").append(getSqlValue(mdrsl.getString("niancjcsl"))).append(", ").append(getSqlValue(mdrsl.getString("niancjcdj")))
							.append(", ").append(getSqlValue(mdrsl.getString("niancjcje"))).append(", ").append(getSqlValue(mdrsl.getString("caigsl")))
							.append(", ").append(getSqlValue(mdrsl.getString("yunj"))).append(", ").append(getSqlValue(mdrsl.getString("caigfrl")))
							.append(", ").append(getSqlValue(mdrsl.getString("caigje"))).append(", ").append(getSqlValue(mdrsl.getString("kuangj")))
							.append(", ").append(getSqlValue(mdrsl.getString("yunzf"))).append(", ").append(getSqlValue(mdrsl.getString("changnfy")))
							.append(", ").append(getSqlValue(mdrsl.getString("biaoml"))).append(", ").append(getSqlValue(mdrsl.getString("biaomdj")))
							.append(", ").append(getSqlValue(mdrsl.getString("rulsl"))).append(", ").append(getSqlValue(mdrsl.getString("ruldj")))
							.append(", ").append(getSqlValue(mdrsl.getString("rulje"))).append(", ").append(getSqlValue(mdrsl.getString("rulzs")))
							.append(", ").append(getSqlValue(mdrsl.getString("rulfrl"))).append(", ").append(getSqlValue(mdrsl.getString("rulbml")))
							.append(", ").append(getSqlValue(mdrsl.getString("meizbmdj"))).append(", ").append(getSqlValue(mdrsl.getString("youzbmdj")))
							.append(", ").append(getSqlValue(mdrsl.getString("zonghbmdj"))).append(", ").append(getSqlValue(mdrsl.getString("rezc")))
							.append(", ").append(getSqlValue(mdrsl.getString("qithysl"))).append(", ").append(getSqlValue(mdrsl.getString("qithydj")))
							.append(", ").append(getSqlValue(mdrsl.getString("qithyje"))).append(", ").append(getSqlValue(mdrsl.getString("qimjysl")))
							.append(", ").append(getSqlValue(mdrsl.getString("qimjydj"))).append(", ").append(getSqlValue(mdrsl.getString("qimjyje")))
							.append(");\n");
				} else {
					sbsql.append("insert into yuecbryb(id, diancxxb_id, riq, niancjcsl, niancjcdj, niancjcje, caigsl, yunj, caigfrl, \n" +
							"caigje, kuangj, yunzf, changnfy, biaoml, biaomdj, rulsl, ruldj, rulje, rulzs, rulfrl, rulbml, meizbmdj, youzbmdj, zonghbmdj, rezc,\n" + 
							"qithysl, qithydj, qithyje, qimjysl, qimjydj, qimjyje) values(").append("getnewid(").append(getTreeid()).append("), ").append(getTreeid())
							.append(", to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd') ")
							.append(", ").append(getSqlValue(mdrsl.getString("niancjcsl"))).append(", ").append(getSqlValue(mdrsl.getString("niancjcdj")))
							.append(", ").append(getSqlValue(mdrsl.getString("niancjcje"))).append(", ").append(getSqlValue(mdrsl.getString("caigsl")))
							.append(", ").append(getSqlValue(mdrsl.getString("yunj"))).append(", ").append(getSqlValue(mdrsl.getString("caigfrl")))
							.append(", ").append(getSqlValue(mdrsl.getString("caigje"))).append(", ").append(getSqlValue(mdrsl.getString("kuangj")))
							.append(", ").append(getSqlValue(mdrsl.getString("yunzf"))).append(", ").append(getSqlValue(mdrsl.getString("changnfy")))
							.append(", ").append(getSqlValue(mdrsl.getString("biaoml"))).append(", ").append(getSqlValue(mdrsl.getString("biaomdj")))
							.append(", ").append(getSqlValue(mdrsl.getString("rulsl"))).append(", ").append(getSqlValue(mdrsl.getString("ruldj")))
							.append(", ").append(getSqlValue(mdrsl.getString("rulje"))).append(", ").append(getSqlValue(mdrsl.getString("rulzs")))
							.append(", ").append(getSqlValue(mdrsl.getString("rulfrl"))).append(", ").append(getSqlValue(mdrsl.getString("rulbml")))
							.append(", ").append(getSqlValue(mdrsl.getString("meizbmdj"))).append(", ").append(getSqlValue(mdrsl.getString("youzbmdj")))
							.append(", ").append(getSqlValue(mdrsl.getString("zonghbmdj"))).append(", ").append(getSqlValue(mdrsl.getString("rezc")))
							.append(", ").append(getSqlValue(mdrsl.getString("qithysl"))).append(", ").append(getSqlValue(mdrsl.getString("qithydj")))
							.append(", ").append(getSqlValue(mdrsl.getString("qithyje"))).append(", ").append(getSqlValue(mdrsl.getString("qimjysl")))
							.append(", ").append(getSqlValue(mdrsl.getString("qimjydj"))).append(", ").append(getSqlValue(mdrsl.getString("qimjyje")))
							.append(");\n");
				}
			} else {
				if (visit.getString1().equals("ranm")) {
					sbsql.append("update yuecbrmb set")
					.append(" meikxxb_id = ").append((getExtGrid().getColumn("meikxxb_id").combo).getBeanId(mdrsl.getString("meikxxb_id"))).append(", ")
					.append(" jihkjb_id = ").append((getExtGrid().getColumn("jihkjb_id").combo).getBeanId(mdrsl.getString("jihkjb_id"))).append(", ")
					.append(" xuh = ").append(getSqlValue(mdrsl.getString("xuh"))).append(", ");
				} else {
					sbsql.append("update yuecbryb set");
				}
				sbsql.append(" niancjcsl = ").append(getSqlValue(mdrsl.getString("niancjcsl"))).append(", ")
				.append(" niancjcdj = ").append(getSqlValue(mdrsl.getString("niancjcdj"))).append(", ")
				.append(" niancjcje = ").append(getSqlValue(mdrsl.getString("niancjcje"))).append(", ")
				.append(" caigsl = ").append(getSqlValue(mdrsl.getString("caigsl"))).append(", ")
				.append(" yunj = ").append(getSqlValue(mdrsl.getString("yunj"))).append(", ")
				.append(" caigfrl = ").append(getSqlValue(mdrsl.getString("caigfrl"))).append(", ")
				.append(" caigje = ").append(getSqlValue(mdrsl.getString("caigje"))).append(", ")
				.append(" kuangj = ").append(getSqlValue(mdrsl.getString("kuangj"))).append(", ")
				.append(" yunzf = ").append(getSqlValue(mdrsl.getString("yunzf"))).append(", ")
				.append(" changnfy = ").append(getSqlValue(mdrsl.getString("changnfy"))).append(", ")
				.append(" biaoml = ").append(getSqlValue(mdrsl.getString("biaoml"))).append(", ")
				.append(" biaomdj = ").append(getSqlValue(mdrsl.getString("biaomdj"))).append(", ")
				.append(" rulsl = ").append(getSqlValue(mdrsl.getString("rulsl"))).append(", ")
				.append(" ruldj = ").append(getSqlValue(mdrsl.getString("ruldj"))).append(", ")
				.append(" rulje = ").append(getSqlValue(mdrsl.getString("rulje"))).append(", ")
				.append(" rulzs = ").append(getSqlValue(mdrsl.getString("rulzs"))).append(", ")
				.append(" rulfrl = ").append(getSqlValue(mdrsl.getString("rulfrl"))).append(", ")
				.append(" rulbml = ").append(getSqlValue(mdrsl.getString("rulbml"))).append(", ")
				.append(" meizbmdj = ").append(getSqlValue(mdrsl.getString("meizbmdj"))).append(", ")
				.append(" youzbmdj = ").append(getSqlValue(mdrsl.getString("youzbmdj"))).append(", ")
				.append(" zonghbmdj = ").append(getSqlValue(mdrsl.getString("zonghbmdj"))).append(", ")
				.append(" rezc = ").append(getSqlValue(mdrsl.getString("rezc"))).append(", ")
				.append(" qithysl = ").append(getSqlValue(mdrsl.getString("qithysl"))).append(", ")
				.append(" qithydj = ").append(getSqlValue(mdrsl.getString("qithydj"))).append(", ")
				.append(" qithyje = ").append(getSqlValue(mdrsl.getString("qithyje"))).append(", ")
				.append(" qimjysl = ").append(getSqlValue(mdrsl.getString("qimjysl"))).append(", ")
				.append(" qimjydj = ").append(getSqlValue(mdrsl.getString("qimjydj"))).append(", ")
				.append(" qimjyje = ").append(getSqlValue(mdrsl.getString("qimjyje")))
				.append(" where id = ").append(mdrsl.getString("id")).append(";\n");
			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
	}
	
	public void getSelectData() {
		
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		
		String sql = 
			"select ry.id, ry.niancjcsl, ry.niancjcdj, ry.niancjcje, ry.caigsl, ry.yunj,\n" +
			"       ry.caigfrl, ry.caigje, ry.kuangj, ry.yunzf, ry.changnfy, ry.biaoml, ry.biaomdj, ry.rulsl, ry.ruldj,\n" + 
			"       ry.rulje, ry.rulzs, ry.rulfrl, ry.rulbml, ry.meizbmdj, ry.youzbmdj, ry.zonghbmdj, ry.rezc, ry.qithysl,\n" + 
			"       ry.qithydj, ry.qithyje, ry.qimjysl, ry.qimjydj, ry.qimjyje from yuecbryb ry" +
			" where ry.riq = to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-1', 'yyyy-mm-dd')";
		
		if (visit.getString1().equals("ranm")) { // �ж��Ƿ�Ϊȼú
			sql = 
				"select rm.id, rm.xuh, mk.mingc meikxxb_id, kj.mingc jihkjb_id, rm.niancjcsl, rm.niancjcdj, rm.niancjcje, rm.caigsl, rm.yunj,\n" +
				"       rm.caigfrl, rm.caigje, rm.kuangj, rm.yunzf, rm.changnfy, rm.biaoml, rm.biaomdj, rm.rulsl, rm.ruldj,\n" + 
				"       rm.rulje, rm.rulzs, rm.rulfrl, rm.rulbml, rm.meizbmdj, rm.youzbmdj, rm.zonghbmdj, rm.rezc, rm.qithysl,\n" + 
				"       rm.qithydj, rm.qithyje, rm.qimjysl, rm.qimjydj, rm.qimjyje from yuecbrmb rm, meikxxb mk, jihkjb kj\n" + 
				" where rm.meikxxb_id = mk.id and rm.jihkjb_id = kj.id " +
				"   and rm.riq = to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-1', 'yyyy-mm-dd')" +
				"order by rm.xuh";
		}
		
		ResultSetList rsl =  con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.getColumn("id").setFixed(true);
		if (visit.getString1().equals("ranm")) {
			egu.getColumn("xuh").setHeader("�к�");
			egu.getColumn("xuh").setWidth(60);
			egu.getColumn("xuh").setFixed(true);
			egu.getColumn("meikxxb_id").setHeader("ú��");
			egu.getColumn("meikxxb_id").setFixed(true);
			egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
			egu.getColumn("jihkjb_id").setFixed(true);
		}
		egu.getColumn("niancjcsl").setHeader("����(��)");
		egu.getColumn("niancjcsl").setFixed(true);
		egu.getColumn("niancjcdj").setHeader("����(Ԫ/��)");
		egu.getColumn("niancjcdj").setFixed(true);
		egu.getColumn("niancjcje").setHeader("���(��Ԫ)");
		egu.getColumn("niancjcje").setFixed(true);
		egu.getColumn("caigsl").setHeader("����(��)");
		egu.getColumn("caigsl").setFixed(true);
		egu.getColumn("yunj").setHeader("�˾�(����)");
		egu.getColumn("yunj").setFixed(true);
		egu.getColumn("caigfrl").setHeader("��λ������(ǧ��/ǧ��)");
		egu.getColumn("caigfrl").setFixed(true);
		egu.getColumn("caigje").setHeader("�ɹ����(��Ԫ)");
		egu.getColumn("caigje").setFixed(true);
		egu.getColumn("kuangj").setHeader("���");
		egu.getColumn("kuangj").setFixed(true);
		egu.getColumn("yunzf").setHeader("���ӷ�");
		egu.getColumn("yunzf").setFixed(true);
		egu.getColumn("changnfy").setHeader("���ڷ���");
		egu.getColumn("changnfy").setFixed(true);
		egu.getColumn("biaoml").setHeader("��ú��(��)");
		egu.getColumn("biaoml").setFixed(true);
		egu.getColumn("biaomdj").setHeader("��ú����(Ԫ/��)");
		egu.getColumn("biaomdj").setFixed(true);
		egu.getColumn("rulsl").setHeader("����(��)");
		egu.getColumn("rulsl").setFixed(true);
		egu.getColumn("ruldj").setHeader("����(Ԫ/��)");
		egu.getColumn("ruldj").setFixed(true);
		egu.getColumn("rulje").setHeader("���(��Ԫ)");
		egu.getColumn("rulje").setFixed(true);
		egu.getColumn("rulzs").setHeader("����(��)");
		egu.getColumn("rulzs").setFixed(true);
		egu.getColumn("rulfrl").setHeader("��λ������(ǧ��/ǧ��)");
		egu.getColumn("rulfrl").setFixed(true);
		egu.getColumn("rulbml").setHeader("��ú��(��)");
		egu.getColumn("rulbml").setFixed(true);
		egu.getColumn("meizbmdj").setHeader("ú�۱�ú����(Ԫ/��)");
		egu.getColumn("youzbmdj").setHeader("���۱�ú����(Ԫ/��)");
		egu.getColumn("meizbmdj").setFixed(true);
		egu.getColumn("zonghbmdj").setHeader("�ۺϱ�ú����(Ԫ/��)");
		egu.getColumn("zonghbmdj").setFixed(true);
		egu.getColumn("rezc").setHeader("��ֵ��(ǧ��/ǧ��)");
		egu.getColumn("rezc").setFixed(true);
		egu.getColumn("qithysl").setHeader("����(��)");
		egu.getColumn("qithysl").setFixed(true);
		egu.getColumn("qithydj").setHeader("����(Ԫ/��)");
		egu.getColumn("qithydj").setFixed(true);
		egu.getColumn("qithyje").setHeader("���(��Ԫ)");
		egu.getColumn("qithyje").setFixed(true);
		egu.getColumn("qimjysl").setHeader("����(��)");
		egu.getColumn("qimjysl").setFixed(true);
		egu.getColumn("qimjydj").setHeader("����(Ԫ/��)");
		egu.getColumn("qimjydj").setFixed(true);
		egu.getColumn("qimjyje").setHeader("���(��Ԫ)");
		egu.getColumn("qimjyje").setFixed(true);
		
		if (visit.getString1().equals("ranm")) {
			egu.getColumn("meikxxb_id").setEditor(new ComboBox());
			egu.getColumn("meikxxb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id, mingc from meikxxb order by mingc"));
			
			egu.getColumn("jihkjb_id").setEditor(new ComboBox());
			egu.getColumn("jihkjb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id, mingc from jihkjb order by mingc"));
		}
		
		egu.addTbarText("�糧��");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc, 
			((Visit)this.getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		egu.addTbarText("��ݣ�");
		ComboBox nf_comb = new ComboBox();
		nf_comb.setWidth(60);
		nf_comb.setTransform("Nianf");
		nf_comb.setId("Nianf");
		nf_comb.setLazyRender(true);
		nf_comb.setEditable(true);
		egu.addToolbarItem(nf_comb.getScript());
//		egu.addOtherScript("Nianf.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		egu.addTbarText("�·ݣ�");
		ComboBox yf_comb = new ComboBox();
		yf_comb.setWidth(60);
		yf_comb.setTransform("Yuef");
		yf_comb.setId("Yuef");
		yf_comb.setLazyRender(true);
		yf_comb.setEditable(true);
		egu.addToolbarItem(yf_comb.getScript());
//		egu.addOtherScript("Yuef.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("ˢ��", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		
		if (visit.getString1().endsWith("ranm")) {
			String condition = 
				"var xuhMrcd = gridDiv_ds.getModifiedRecords();\n" +
				"for(var i = 0; i< xuhMrcd.length; i++){\n" + 
				"    if(xuhMrcd[i].get('XUH') == '' || xuhMrcd[i].get('XUH') == null){\n" + 
				"        Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ��� ����Ϊ��');\n" + 
				"        return;\n" + 
				"    }\n" + 
				"}";
			egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton", condition);
		} else {
			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		}
		
		
		if (visit.getString1().equals("ranm")) {
			egu.addTbarText("-");
			GridButton createButton = new GridButton("����", getButtonHandler(con, "CreateButton"), SysConstant.Btn_Icon_Copy);
			egu.addTbarBtn(createButton);
		}
		
		String ranm = "";
		if (visit.getString1().equals("ranm")) {
			ranm = 
			"    {header:'<table><tr><td width=40 align=center style=border:0>���</td></tr></table>',align:'center', rowspan:2},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>ú��</td></tr></table>',align:'center', rowspan:2},\n" + 
			"    {header:'<table><tr><td width=80 align=center style=border:0>�ƻ��ھ�</td></tr></table>',align:'center', rowspan:2},\n";
		}
		
		String headers = // ���б�ͷ��ʽ
			
			"[\n" +
			"    {header:'<table><tr><td width=5 align=center></td></tr></table>', align:'center', rowspan:2},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>ID</td></tr></table>',align:'center', rowspan:2},\n" + ranm +
			"    {header:'<table><tr><td width=240 align=center style=border:0>���������</td></tr></table>', align:'center', colspan:3},\n" + 
			"    {header:'<table><tr><td width=600 align=center style=border:0>�ɹ����(����˰)</td></tr></table>', align:'center', colspan:9},\n" +
			"    {header:'<table><tr><td width=600 align=center style=border:0>��¯���</td></tr></table>', align:'center', colspan:9},\n" + 
			"    {header:'<table><tr><td width=80 align=center style=border:0>��ֵ��</td></tr></table>', align:'center', rowspan:2},\n" +
			"    {header:'<table><tr><td width=240 align=center style=border:0>��������</td></tr></table>', align:'center', colspan:3},\n" +
			"    {header:'<table><tr><td width=240 align=center style=border:0>��ĩ����</td></tr></table>', align:'center', colspan:3}\n" + 
			"],\n" + 
			"[\n" + 
			"    {header:'<table><tr><td width=80 align=center style=border:0>����<br>(��)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>����<br>(Ԫ/��)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>���<br>(��Ԫ)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>����<br>(��)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>�˾�<br>(����)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>��λ������<br>(ǧ��/ǧ��)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>�ɹ����<br>(��Ԫ)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>���</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>���ӷ�</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>���ڷ���</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>��ú��<br>(��)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>��ú����<br>(Ԫ/��)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>����<br>(��)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>����<br>(Ԫ/��)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>���<br>(��Ԫ)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>����<br>(��)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>��λ������<br>(ǧ��/ǧ��)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>��ú��<br>(��)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>ú�۱�ú����<br>(Ԫ/��)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>���۱�ú����<br>(Ԫ/��)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>��ú����<br>(Ԫ/��)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>����<br>(��)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>����<br>(Ԫ/��)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>���<br>(��Ԫ)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>����<br>(��)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>����<br>(Ԫ/��)</td></tr></table>', align:'center'},\n" +
			"    {header:'<table><tr><td width=80 align=center style=border:0>���<br>(��Ԫ)</td></tr></table>', align:'center'}\n" +
			"]";
		
		egu.setHeaders(headers);
		egu.setPlugins("new Ext.ux.plugins.XGrid()");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(25);
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}
	
	/**
	 * �����ҳ����ȡ����ֵΪNull���ǿմ�����ô�����ݿⱣ���ֶε�Ĭ��ֵ
	 * @param value
	 * @return
	 */
	public String getSqlValue(String value) {
		return value == null || "".equals(value) ? "default" : value;
	}
	
	/**
	 * ���ش������ڵ��ϸ��·ݣ����ص����ڸ�ʽΪ"yyyy-mm"
	 * @param date
	 * @return
	 */
	public static String getLastMonth(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		if(date == null){
			date = sdf.format(new Date());
		}
		try {
			Date tempdate = sdf.parse(date);
			int month = DateUtil.getMonth(tempdate);
			int year =DateUtil.getYear(tempdate);
			if(month == 1){
				year -= 1;
				month = 12;
			}else{
				month -= 1;
			}
			return year+"-"+month;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * ����"����ͬ��"��ť��handler�������"����ͬ��"��ťʱ�жϵ�ǰ�����Ƿ������ݣ�
	 * �����������ô������ʾ�Ƿ�Ҫ����ԭ�����ݣ����û����ô������ʾ��Ϣ��
	 * ֱ�ӽ���"����ͬ��"������
	 * @return
	 */
	public String getButtonHandler(JDBCcon con, String buttonName) {
		
		String handler = 
			"function (){\n" +
			"    document.getElementById('"+ buttonName +"').click();\n" + 
			"}";
		
		String sql = "select rm.id from yuecbrmb rm where to_date(to_char(rm.riq, 'yyyy-mm'), 'yyyy-mm') = to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"', 'yyyy-mm')";
		ResultSetList rsl = con.getResultSetList(sql);
		
		if (rsl.next()) {
			handler = 
				"function (){\n" +
				"    Ext.MessageBox.confirm('��ʾ��Ϣ','�����ݽ��Ḳ��ԭ�����ݣ��Ƿ������',\n" + 
				"        function(btn){\n" + 
				"            if(btn == 'yes'){\n" + 
				"                document.getElementById('"+ buttonName +"').click();\n" + 
				"                Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...'," +
				"                width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n" + 
				"            };\n" + 
				"        }\n" + 
				"    );\n" + 
				"}";
		}
		rsl.close();
		return handler;
	}
	
//	�糧��_��ʼ
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
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
//	�糧��_����

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	public String getGridScript() {
		return getExtGrid().getGridScript();
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

		String reportType = cycle.getRequestContext().getParameter("lx");
		if (reportType != null) {
			visit.setString1(reportType);
			visit.setProSelectionModel2(null); // ���������
			visit.setDropDownBean2(null);
			visit.setProSelectionModel3(null); // �·�������
			visit.setDropDownBean3(null);
		}
		
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			this.setTreeid(null);
			visit.setProSelectionModel2(null); // ���������
			visit.setDropDownBean2(null);
			visit.setProSelectionModel3(null); // �·�������
			visit.setDropDownBean3(null);
		}
		getSelectData();
	}

}