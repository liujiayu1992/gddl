package com.zhiren.dc.chengbgl.baob;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Chengbyb extends BasePage {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
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
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	public int getAllPages() {
		return _AllPages;
	}
	public void setAllPages(int _value) {
		_AllPages= _value;
	}

	public IPropertySelectionModel getNianfModel() {
        if (((Visit)this.getPage().getVisit()).getProSelectionModel3() == null) {
            getNianfModels();
        }
        return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
    }
	
	public void setNianfModel(IPropertySelectionModel _value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(_value);
    }
	
	public void getNianfModels() {
        List listNianf = new ArrayList();
        int i;
        for (i = 2004; i <= DateUtil.getYear(new Date())+1; i++) {
            listNianf.add(new IDropDownBean(i, String.valueOf(i)));
        }
        setNianfModel(new IDropDownModel(listNianf));
    }
    
	
    public IDropDownBean getNianfValue() {
        if (((Visit)this.getPage().getVisit()).getDropDownBean3() == null) {
            int _nianf = DateUtil.getYear(new Date());
            int _yuef = DateUtil.getMonth(new Date());
            if (_yuef == 1) {
                _nianf = _nianf - 1;
            }
            for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
                Object obj = getNianfModel().getOption(i);
                if (_nianf == ((IDropDownBean) obj).getId()) {
                	setNianfValue((IDropDownBean) obj);
                    break;
                }
            }
        }
        return ((Visit)this.getPage().getVisit()).getDropDownBean3();
    }
	
    public void setNianfValue(IDropDownBean Value) {
    	((Visit)this.getPage().getVisit()).setDropDownBean3(Value);
    }

    

    

	// 月份下拉框
	
	public IPropertySelectionModel getYuefModel() {
	    if (((Visit)this.getPage().getVisit()).getProSelectionModel4() == null) {
	        getYuefModels();
	    }
	    return ((Visit)this.getPage().getVisit()).getProSelectionModel4();
	}
	public void setYuefModel(IPropertySelectionModel _value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel4(_value);
    }
	
	public void getYuefModels() {
        List listYuef = new ArrayList();
        for (int i = 1; i < 13; i++) {
            listYuef.add(new IDropDownBean(i, String.valueOf(i)));
        }
        setYuefModel(new IDropDownModel(listYuef));
    }
	
	
	public IDropDownBean getYuefValue() {
	    if (((Visit)this.getPage().getVisit()).getDropDownBean4() == null) {
	        int _yuef = DateUtil.getMonth(new Date());
	        if (_yuef == 1) {
	            _yuef = 12;
	        } else {
	            _yuef = _yuef - 1;
	        }
	        for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
	            Object obj = getYuefModel().getOption(i);
	            if (_yuef == ((IDropDownBean) obj).getId()) {
	            	setYuefValue((IDropDownBean) obj);
	                break;
	            }
	        }
	    }
	    return ((Visit)this.getPage().getVisit()).getDropDownBean4();
	}
	
	public void setYuefValue(IDropDownBean Value) {
		((Visit)this.getPage().getVisit()).setDropDownBean4(Value);
	}

//	页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	public boolean getRaw() {
		return true;
	}
//	厂别下拉框
	public IDropDownBean getChangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getChangbModel().getOptionCount()>0) {
				setChangbValue((IDropDownBean)getChangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setChangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getChangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setChangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setChangbModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if(visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id());
		}else {
			sb.append("select id,mingc from diancxxb where id="+visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}
//	获取相关的SQL
	/*
	 * 将修约方法由round 改为round_new 并用参数控制发热量的小数位
	 * 修改时间：2008-12-05
	 * 修改人：王磊
	 */
	public StringBuffer getBaseSql() {
		Visit visit = (Visit) this.getPage().getVisit();
		long intYuef = getYuefValue().getId();
		String riq = getNianfValue().getValue() + "-" + (intYuef<10?"0"+intYuef:""+intYuef) + "-01";
		String CurrentDate = DateUtil.FormatOracleDate(riq);
		StringBuffer sb = new StringBuffer();
		sb.append("select decode(grouping(j.mingc),1,'总计',j.mingc) jihkj,\n")
		.append("decode(grouping(g.mingc)+grouping(j.mingc),1,'合计',g.mingc) gongysmc,\n")
		.append("decode(grouping(p.mingc)+grouping(g.mingc)+grouping(j.mingc),1,'小计',p.mingc) pinz,\n")
		.append("y.mingc, nvl(b.fenx,'') fenx, sum(s.jingz) jingz,\n")
		.append("round_new(nvl(decode(sum(s.jingz),0,0,sum(s.jingz*b.meij)/sum(s.jingz)),0),2) meij,\n")
		.append("round_new(nvl(decode(sum(s.jingz),0,0,sum(s.jingz*b.meijs)/sum(s.jingz)),0),2) meijs,\n")
		.append("round_new(nvl(decode(sum(s.jingz),0,0,sum(s.jingz*b.yunj)/sum(s.jingz)),0),2) yunj,\n")
		.append("round_new(nvl(decode(sum(s.jingz),0,0,sum(s.jingz*b.yunjs)/sum(s.jingz)),0),2) yunjs,\n")
		.append("round_new(nvl(decode(sum(s.jingz),0,0,sum(s.jingz*b.jiaohqzf)/sum(s.jingz)),0),2) jiaohqzf,\n")
		.append("round_new(nvl(decode(sum(s.jingz),0,0,sum(s.jingz*b.daozzf)/sum(s.jingz)),0),2) daozzf,\n")
		.append("round_new(nvl(decode(sum(s.jingz),0,0,sum(s.jingz*b.zaf)/sum(s.jingz)),0),2) zaf,\n")
		.append("round_new(nvl(decode(sum(s.jingz),0,0,sum(s.jingz*b.qit)/sum(s.jingz)),0),2) qit,\n")
		.append("round_new(nvl(decode(sum(s.jingz),0,0,sum(s.jingz*b.qnet_ar)/sum(s.jingz)),0),"+visit.getFarldec()+") qner_ar,\n")
		.append("round_new(nvl(decode(sum(s.jingz),0,0,sum(s.jingz*b.biaomdj)/sum(s.jingz)),0),2) biaomdj,\n")
		.append("round_new(nvl(decode(sum(s.jingz),0,0,sum(s.jingz*b.buhsbmdj)/sum(s.jingz)),0),2) buhsbmdj\n")
		.append("from yuercbmdj b, yuetjkjb t, yueslb s, gongysb g, jihkjb j, pinzb p, yunsfsb y\n")
		.append("where t.id = s.yuetjkjb_id and t.id = b.yuetjkjb_id and b.fenx = s.fenx\n")
		.append("and t.gongysb_id = g.id and t.jihkjb_id = j.id and t.pinzb_id = p.id and t.yunsfsb_id = y.id\n")
		.append("and t.diancxxb_id = ").append(visit.getDiancxxb_id())
		.append(" and t.riq = ").append(CurrentDate).append("\n")
		.append("group by rollup (b.fenx,j.mingc,g.mingc,p.mingc,y.mingc)\n")
		.append("having not  (grouping(b.fenx) = 1 or grouping(p.mingc)+grouping(y.mingc) = 1)\n")
		.append("order by grouping(j.mingc) desc,max(j.xuh),j.mingc,grouping(g.mingc) desc ,max(g.xuh),g.mingc,\n")
		.append("grouping(p.mingc) desc ,max(p.xuh),p.mingc,grouping(y.mingc) desc ,y.mingc,b.fenx\n");

/*		这个是直接从ruccb表中取出来的
 * 		sb.append("select decode(grouping(j.mingc),1,'总计',j.mingc) jihkj,\n")
		.append("decode(grouping(g.mingc)+grouping(j.mingc),1,'合计',g.mingc) gongysmc,\n")
		.append("decode(grouping(m.mingc)+grouping(g.mingc)+grouping(j.mingc),1,'小计',m.mingc) meikdwmc,\n")
		.append("c.fenx, sum(c.jingz) jingz,\n")
		.append("round(nvl(decode(sum(c.jingz),0,0,sum(meij*jingz)/sum(jingz)),0),2) meij,\n")
		.append("round(nvl(decode(sum(c.jingz),0,0,sum(meijs*jingz)/sum(jingz)),0),2) meijs,\n")
		.append("round(nvl(decode(sum(c.jingz),0,0,sum(yunj*jingz)/sum(jingz)),0),2) yunj,\n")
		.append("round(nvl(decode(sum(c.jingz),0,0,sum(yunjs*jingz)/sum(jingz)),0),2) yunjs,\n")
		.append("round(nvl(decode(sum(c.jingz),0,0,sum(jiaohqzf*jingz)/sum(jingz)),0),2) jiaohqzf,\n")
		.append("round(nvl(decode(sum(c.jingz),0,0,sum(zaf*jingz)/sum(jingz)),0),2) zaf,\n")
		.append("round(nvl(decode(sum(c.jingz),0,0,sum(daozzf*jingz)/sum(jingz)),0),2) daozzf,\n")
		.append("round(nvl(decode(sum(c.jingz),0,0,sum(qitfy*jingz)/sum(jingz)),0),2) qitfy,\n")
		.append("round(nvl(decode(sum(c.jingz),0,0,sum(qnet_ar*jingz)/sum(jingz)),0),2) qnet_ar,\n")
		.append("round(nvl(decode(sum(c.jingz),0,0,sum(biaomdj*jingz)/sum(jingz)),0),2) biaomdj,\n")
		.append("round(nvl(decode(sum(c.jingz),0,0,sum(buhsbmdj*jingz)/sum(jingz)),0),2) buhsbmdj\n")
		.append("from chengbyb c, gongysb g, meikxxb m, jihkjb j\n")
		.append("where c.gongysb_id = g.id and c.meikxxb_id = m.id\n")
		.append("and c.jihkjb_id = j.id and c.diancxxb_id = ").append(visit.getDiancxxb_id()).append("\n")
		.append("and c.riq = ").append(CurrentDate).append("\n")
		.append("group by rollup(c.fenx,j.mingc,g.mingc,m.mingc)\n")
		.append("having not  grouping(fenx) = 1\n")
		.append("order by grouping(j.mingc) desc,max(j.xuh),j.mingc,grouping(g.mingc) desc ,max(g.xuh),g.mingc,\n")
		.append("grouping(m.mingc) desc,max(m.xuh),m.mingc,c.fenx\n");*/
		return sb;
	}
	
//	刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		if(visit.isFencb()) {
			tb1.addText(new ToolbarText("厂别:"));
			ComboBox changbcb = new ComboBox();
			changbcb.setTransform("ChangbSelect");
			changbcb.setWidth(130);
			changbcb.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
			tb1.addField(changbcb);
			tb1.addText(new ToolbarText("-"));
		}
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		//nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		//yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	/*
	 * 将表头信息Locale.jingz_fahb改为Locale.laimsl_fahb
	 * 修改时间：2008-12-05
	 * 修改人：王磊
	 */
	public String getPrintTable(){
		JDBCcon con = new JDBCcon();
		String riq = getNianfValue().getValue() + " 年 " +getYuefValue().getValue()+" 月";
		ResultSet rs = con.getResultSet(getBaseSql(),
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		if (rs == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult);
			setMsg(ErrorMessage.NullResult);
			return "";
		}
		Report rt = new Report();

		String[][] ArrHeader = new String[][] {{ Locale.jihkjb_id_fahb, Locale.gongysb_id_fahb, 
			Locale.pinzb_id_fahb, Locale.yunsfsb_id_fahb, Locale.MRtp_fenx, Locale.laimsl_fahb, Locale.meij_chengbrb, 
			Locale.meijs_chengbrb, Locale.yunj_chengbrb, Locale.yunjs_chengbrb, Locale.jiaohqzf_chengbrb, 
			Locale.daozzf_chengbrb, Locale.zaf_chengbrb, Locale.qitfy_chengbrb, Locale.Qnet_ar_chengbrb, Locale.biaomdj_chengbrb, Locale.buhsbmdj_chengbrb} };

		int[] ArrWidth = new int[] { 80, 100, 60, 60, 50, 70, 50, 50, 50, 50, 80, 50, 70, 50, 50, 50, 80 };

		rt.setTitle("成本月报", ArrWidth);
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 4, "制表单位：" + ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 5, "报表日期：" + riq,
				Table.ALIGN_CENTER);
		rt.setDefaultTitle(12, 4, "单位：吨、车", Table.ALIGN_RIGHT);

		String[] arrFormat = new String[] { "", "", "", "", "", "", "0.00", "0.00",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "" };

		rt.setBody(new Table(rs, 1, 0, 4));
		//rt.body.setColAlign(0, Table.ALIGN_CENTER);r
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
//		rt.body.setCellVAlign(i, j, intAlign)
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setColFormat(arrFormat);
		rt.body.setPageRows(40);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();

//		rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 4, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 5, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(12, 4, "制表：", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
//		rt.footer.setRowHeight(1, 1);
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);

		return rt.getAllPagesHtml();// ph;
	}
//	工具栏使用的方法
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
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setChangbValue(null);
			setChangbModel(null);
			setNianfValue(null);
			getNianfModels();
			setYuefValue(null);
			getYuefModels();
			getSelectData();
		}
	}
	
//	按钮的监听事件
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

//	表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}
//	页面登陆验证
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
