package com.zhiren.shanxdted.jilrb;

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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * 作者:tzf
 * 时间:2009-11-20
 * 内容:修改 已经过皮时  才取净重  要不为0
 */
/*
 * 作者:tzf
 * 时间:2009-07-06
 * 内容:过衡日报和收煤日报
 */
public class Jilrb extends BasePage implements PageValidateListener {

	/*
	 * 时间:2009-03-19 作者:童忠付 内容:对过衡日报增加盈亏字段，当运输方式是汽运时，要在xitxxb中查找对应计算的小数点保留位数，
	 * 并且增加电厂字段，进行统计 相应的数据库配置说明已经落实，具体请查看
	 */
	private static final int RPTTYPE_GH_ALL = 1;

	private static final int RPTTYPE_GH_HUOY = 2;

	private static final int RPTTYPE_GH_QIY = 3;

	private static final int RPTTYPE_SB_ALL = 4;

	private static final int RPTTYPE_SB_HUOY = 5;

	private static final int RPTTYPE_SB_QIY = 6;


	private static final String BAOBPZB_GUANJZ = "GUOHRB_GJZ_ER_ED";// baobpzb中对应的关键字
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

	// 绑定日期
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}

	// 页面变化记录
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

	
	  private boolean hasDianc(String id){ 
			JDBCcon con=new JDBCcon();
			boolean mingc= false;
			String sql="select mingc from diancxxb where fuid = " + id;
			ResultSetList rsl=con.getResultSetList(sql);
			if(rsl.next()){
				mingc=true;
			}
			rsl.close();
			return mingc;
		}
	  
	// 获取相关的SQL
	
	public StringBuffer getBaseSqlStr(){
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		
		String diancid="";
		if (getTreeid() != null && !"".equals(getTreeid())
				&& !"0".equals(getTreeid())) {
			
			if(hasDianc(getTreeid())){
				diancid = " where d.fgsid = " + getTreeid();
			} else {
				diancid = " where d.id ="+ getTreeid();
			}
		}
		
		String yunsdw="";
		if(this.getMeikid()!=null && !this.getMeikid().equals("") && !this.getMeikid().equals("0")){
			yunsdw=" and y.id="+this.getMeikid();
		}
		
		String meik="";
		String fahgl="";
		if(this.getMeikuangid()!=null && !this.getMeikuangid().equals("") && !this.getMeikuangid().equals("0")){
			meik=" where id="+this.getMeikuangid();
			fahgl=" and f.meikxxb_id="+this.getMeikuangid();
		}
		
		String sql1 = "";
		String ghrq="";//过衡日期
		String sbrq="";//收煤日期
		switch (visit.getInt1()) {
		case RPTTYPE_GH_ALL:
			ghrq=" and cp.zhongcsj>="+DateUtil.FormatOracleDate(this.getRiq())+" and cp.zhongcsj<"+DateUtil.FormatOracleDate(this.getRiq())+"+1 ";
			break;
		case RPTTYPE_GH_HUOY:
			ghrq=" and cp.zhongcsj>="+DateUtil.FormatOracleDate(this.getRiq())+" and cp.zhongcsj<"+DateUtil.FormatOracleDate(this.getRiq())+"+1 ";
			sql1+=" and f.yunsfsb_id = "+SysConstant.YUNSFS_HUOY+" \n";
			break;
		case RPTTYPE_GH_QIY:
			ghrq=" and cp.zhongcsj>="+DateUtil.FormatOracleDate(this.getRiq())+" and cp.zhongcsj<"+DateUtil.FormatOracleDate(this.getRiq())+"+1 ";
			sql1+=" and f.yunsfsb_id = "+SysConstant.YUNSFS_QIY+" \n";
			break;
		case RPTTYPE_SB_ALL:
			sbrq="   and f.daohrq="+DateUtil.FormatOracleDate(this.getRiq());
			break;
		case RPTTYPE_SB_HUOY:
			sbrq="   and f.daohrq="+DateUtil.FormatOracleDate(this.getRiq());
			sql1+=" and f.yunsfsb_id = "+SysConstant.YUNSFS_HUOY+" \n";
			break;
		case RPTTYPE_SB_QIY:
			sbrq="  and f.daohrq="+DateUtil.FormatOracleDate(this.getRiq());
			sql1+=" and f.yunsfsb_id = "+SysConstant.YUNSFS_QIY+" \n";
			break;
		default:
			
			break;
		}
		
		
		sb.append(" select decode(grouping(fx.dc),1,'总计',fx.dc) dc,\n");
		sb.append(" decode(grouping(fx.dc)+grouping(fx.gys),1,'合计',fx.gys) gys,\n");
		sb.append(" decode(grouping(fx.dc)+grouping(fx.gys)+grouping(fx.mk),1,'小计',fx.mk) mk,\n");
		sb.append(" cx.ysdw,\n");
		sb.append(" fx.pinz,fx.faz,\n");
		
		sb.append(" sum(round_new(cx.biaoz,"+visit.getShuldec()+")) biaoz,\n");
		sb.append(" sum(round_new(cx.jingz,"+visit.getShuldec()+")) jingz,\n");
		sb.append(" sum(round_new(cx.yuns,"+visit.getShuldec()+"))  yuns,\n");
		
		sb.append(" sum(round_new(cx.yingk,"+visit.getShuldec()+")) yingk,\n");
		sb.append(" sum(round_new(cx.zongkd,"+visit.getShuldec()+")) zongkd,\n");
//		sb.append(" sum(cx.ches) ches\n");
		sb.append(" sum(fx.ches) ches\n");
		
		sb.append("  from \n");
		sb.append(" (\n");
		sb.append(" select fh.id ,gh.mingc gys,m.mingc mk,c.mingc faz,p.mingc pinz,dh.mingc dc,fh.daohrq,fh.ches  from \n");
		
		sb.append(" (select f.id,f.diancxxb_id ,f.gongysb_id,f.meikxxb_id,f.faz_id,f.pinzb_id,f.daohrq,f.ches  from    fahb  f where 1=1 \n");
		sb.append( sbrq+" "+sql1+fahgl+"  ) fh,\n");
		sb.append(" \n");
		
		sb.append(" (select g.id,g.mingc  from  gongysb g )  gh,\n");
		sb.append(" (select  d.id,d.mingc from  vwdianc d "+diancid+" ) dh,\n");
		sb.append(" pinzb p, chezxxb c,(select id,mingc from meikxxb "+meik+" ) m\n");
		
		sb.append(" where fh.diancxxb_id=dh.id and fh.gongysb_id=gh.id and fh.meikxxb_id=m.id and fh.faz_id=c.id  and fh.pinzb_id=p.id \n");
		sb.append(" ) fx,\n");
		
		sb.append(" (select cp.fahb_id,sum(cp.maoz) maoz,sum(cp.piz) piz,sum(decode(cp.QINGCSJ,null,0,cp.biaoz)) biaoz," +
				"sum(cp.yuns) yuns,sum(cp.yingk) yingk,sum(cp.zongkd) zongkd,sum( decode(cp.QINGCSJ,null,0,cp.maoz-cp.piz-cp.zongkd) ) jingz," +
				" count(*) ches,y.mingc ysdw\n");
		sb.append(" from chepb cp,yunsdwb y  where cp.yunsdwb_id=y.id(+) "+yunsdw+ghrq+" group by cp.fahb_id,y.mingc ) cx where fx.id=cx.fahb_id \n");
		
		sb.append(" group by grouping sets((),(fx.dc),(fx.dc,fx.gys),\n");
		sb.append(" (fx.dc,fx.gys,fx.mk,cx.ysdw),(fx.dc,fx.gys,fx.mk,cx.ysdw,fx.pinz,fx.faz))\n");
		sb.append("  having grouping(fx.dc)+grouping(fx.gys)+grouping(fx.mk)+grouping(cx.ysdw)-grouping(fx.pinz)-grouping(fx.faz)>-2 \n");
		
		sb.append(" order by grouping(fx.dc) desc,fx.dc desc,grouping(fx.gys) desc,fx.gys asc,grouping(fx.mk) desc,cx.ysdw \n");
		sb.append(" \n");
		sb.append(" \n");
		sb.append(" \n");
		
		
		
		return sb;
	}

	// 获取表表标题
	public String getRptTitle() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sb;
		switch (visit.getInt1()) {
		case RPTTYPE_GH_ALL:
			sb = Locale.RptTitle_Jilrb_GH_All;
			break;
		case RPTTYPE_GH_HUOY:
			sb = Locale.RptTitle_Jilrb_GH_Huoy;
			break;
		case RPTTYPE_GH_QIY:
			sb = Locale.RptTitle_Jilrb_GH_Qiy;
			break;
		case RPTTYPE_SB_ALL:
			sb = Locale.RptTitle_Jilrb_SB_All;
			break;
		case RPTTYPE_SB_HUOY:
			sb = Locale.RptTitle_Jilrb_SB_Huoy;
			break;
		case RPTTYPE_SB_QIY:
			sb = Locale.RptTitle_Jilrb_SB_Qiy;
			break;
		default:
			sb = Locale.RptTitle_Jilrb_GH_All;
			break;
		}
		return sb;
	}

	// 获取表日期
	public String getRiqTitle() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sb;
		switch (visit.getInt1()) {
		case RPTTYPE_GH_ALL:
			sb = "过衡日期";
			break;
		case RPTTYPE_GH_HUOY:
			sb = "过衡日期";
			break;
		case RPTTYPE_GH_QIY:
			sb = "过衡日期";
			break;
		case RPTTYPE_SB_ALL:
			sb = "到货日期";
			break;
		case RPTTYPE_SB_HUOY:
			sb = "到货日期";
			break;
		case RPTTYPE_SB_QIY:
			sb = "到货日期";
			break;
		default:
			sb = "到货日期";
			break;
		}
		return sb;
	}

	
//	获取煤矿
	
	private StringBuffer getMKSql(){
		Visit visit=(Visit)this.getPage().getVisit();
		StringBuffer bf=new StringBuffer();
		
		bf.append(" select distinct * from ( \n");
		bf.append(" select 0 id,'全部' mingc,0 jib from dual\n");
		bf.append(" union\n");
		bf.append(" select id ,mingc,1 jib from meikxxb");
		bf.append(" ) \n");
		bf.append(" order by id,mingc\n");
		
		return bf;
	}
	
	private String meikuangid = "";
	public String getMeikuangid() {
		if (meikuangid.equals("")) {

			meikuangid = "0";
		}
		return meikuangid;
	}
	public void setMeikuangid(String meikuangid) {
		if(meikuangid!=null) {
			if(!meikuangid.equals(this.meikuangid)) {
				((TextField)getToolbar().getItem("meikuangTree_text")).setValue
				(((IDropDownModel)this.getMeiKuangDropDownModel()).getBeanValue(Long.parseLong(meikuangid)));
				this.getMeikuangtr().getTree().setSelectedNodeid(meikuangid);
			}
		}
		this.meikuangid = meikuangid;
	}

	

	public IPropertySelectionModel getMeiKuangDropDownModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
            getMeiKuangDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }
	public void setMeiKuangDropDownModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel4(value);
    }
    public void getMeiKuangDropDownModels() {
    	String sql="";
    	
    	sql+="  select 0 id, '全部' mingc from dual union select id ,mingc from meikxxb";
//        setMeiKuangDropDownModel(new IDropDownModel(sql,"全部")) ;
    	setMeiKuangDropDownModel(new IDropDownModel(sql)) ;
        return ;
    }
    
    DefaultTree meikuangtr;
	
	public DefaultTree getMeikuangtr() {
		return meikuangtr;
	}
	public void setMeikuangtr(DefaultTree etu) {
		meikuangtr=etu;
	}

	public String getMeikuangtrScriptMK() {
		return this.getMeikuangtr().getScript();
	}
    //---------------
	// 刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText(getRiqTitle() + ":"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		df.setId("guohrq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		
		
		

		//煤矿
		
		DefaultTree gystree=new DefaultTree();
		gystree.setTree_window(this.getMKSql().toString(), "meikuangTree", visit.getDiancxxb_id()+"", "forms[0]", this.getMeikuangid(), this.getMeikuangid());
		this.setMeikuangtr(gystree);
		TextField tf1 = new TextField();
		tf1.setId("meikuangTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel)getMeiKuangDropDownModel()).getBeanValue(Long.parseLong(getMeikuangid()==null||"".equals(getMeikuangid())?"-1":getMeikuangid())));
		
		ToolbarButton tb5 = new ToolbarButton(null,null,"function(){meikuangTree_window.show();}");
		tb5.setIcon("ext/resources/images/list-items.gif");
		tb5.setCls("x-btn-icon");
		tb5.setMinWidth(20);

		tb1.addText(new ToolbarText("煤矿:"));
		tb1.addField(tf1);
		tb1.addItem(tb5);
		
		tb1.addText(new ToolbarText("-"));
		
		
		
		
//运输单位--------------------
		
		DefaultTree mktree=new DefaultTree();
		mktree.setTree_window(this.getDTsql().toString(), "meikTree", visit.getDiancxxb_id()+"", "forms[0]", this.getMeikid(), this.getMeikid());
		this.setTree(mktree);
		
		TextField tfmk=new TextField();
		tfmk.setId("meikTree_text");
		tfmk.setWidth(100);
		tfmk.setValue(((IDropDownModel)this.getMeikModel()).getBeanValue(Long
				.parseLong(this.getMeikid() == null || "".equals(this.getMeikid()) ? "-1"
						: this.getMeikid())));
		
		

		ToolbarButton tb4 = new ToolbarButton(null, null,
				"function(){meikTree_window.show();}");
		tb4.setIcon("ext/resources/images/list-items.gif");
		tb4.setCls("x-btn-icon");
		tb4.setMinWidth(20);
		
		tb1.addText(new ToolbarText("运输单位"));
		tb1.addField(tfmk);
		tb1.addItem(tb4);
		
		tb1.addText(new ToolbarText("-"));
	//-------------------------
		
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "forms[0]", getTreeid(),
				getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getGongysDropDownModel())
				.getBeanValue(Long.parseLong(getTreeid() == null
						|| "".equals(getTreeid()) ? "-1" : getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf);
		tb1.addItem(tb2);

		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "查询",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText(
				"<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}

	
//	------------煤矿----------

	private String meikid = "";
		public String getMeikid() {
			if (meikid.equals("")) {

				meikid = "0";
			}
			return meikid;
		}
		public void setMeikid(String meikid) {
			if(meikid!=null) {
				if(!meikid.equals(this.meikid)) {
					((TextField)getToolbar().getItem("meikTree_text")).setValue
					(((IDropDownModel)this.getMeikModel()).getBeanValue(Long.parseLong(meikid)));
					this.getTree().getTree().setSelectedNodeid(meikid);
				}
			}
			this.meikid = meikid;
		}
	
		
		
		
		//获得运输单位 树形结构sql
		private StringBuffer getDTsql(){
			
			Visit visit=(Visit)this.getPage().getVisit();
			StringBuffer bf=new StringBuffer();
			
			String s=" union \n select id ,mingc,1 jib from yunsdwb ";
			switch (visit.getInt1()) {
			case RPTTYPE_GH_ALL:
				break;
			case RPTTYPE_GH_HUOY:
				s=" ";
				break;
			case RPTTYPE_GH_QIY:
				break;
			case RPTTYPE_SB_ALL:
				break;
			case RPTTYPE_SB_HUOY:
				s=" ";
				break;
			case RPTTYPE_SB_QIY:
				break;
			default:
				
				break;
			}
			
			
			bf.append(" select distinct * from ( \n");
			bf.append(" select 0 id,'全部' mingc,0 jib from dual\n");
			bf.append(s);
			bf.append(" ) \n");
			bf.append(" order by id,mingc\n");
			
			return bf;
		}
		
		
		DefaultTree mktr;
		
		public DefaultTree getTree() {
			return mktr;
		}
		public void setTree(DefaultTree etu) {
			mktr=etu;
		}

		public String getTreeScriptMK() {
			return this.getTree().getScript();
		}
		
		


		public IPropertySelectionModel getMeikModel() {
			if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
				getMeikModels();
			}
			return ((Visit) getPage().getVisit()).getProSelectionModel3();
		}

		public void setMeikModel(IPropertySelectionModel _value) {
			((Visit) getPage().getVisit()).setProSelectionModel3(_value);
		}

		public void getMeikModels() {
			String sql = "select 0 id,'全部' mingc from dual union select id,mingc  from yunsdwb ";
			setMeikModel(new IDropDownModel(sql));
		}
		
		//-------------------------------------------------
		
		
		
	public String getPrintTable() {
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
//		System.out.println(getBaseSqlStr().toString());
		ResultSetList rstmp = con.getResultSetList(getBaseSqlStr().toString());
		Report rt = new Report();
		ResultSetList rs = null;
		String[][] ArrHeader = null;
		String[] strFormat = null;
		int[] ArrWidth = null;
		String[] Zidm = null;
		StringBuffer sb = new StringBuffer();
		sb
				.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='"
						+ BAOBPZB_GUANJZ + "' order by xuh");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl.getRows() > 0) {
			ArrWidth = new int[rsl.getRows()];
			strFormat = new String[rsl.getRows()];
			String biaot = rsl.getString(0, 1);
			String[] Arrbt = biaot.split("!@");
			ArrHeader = new String[Arrbt.length][rsl.getRows()];
			Zidm = new String[rsl.getRows()];
			rs = new ResultSetList();
			while (rsl.next()) {
				Zidm[rsl.getRow()] = rsl.getString("zidm");
				ArrWidth[rsl.getRow()] = rsl.getInt("kuand");
				strFormat[rsl.getRow()] = rsl.getString("format") == null ? ""
						: rsl.getString("format");
				String[] title = rsl.getString("biaot").split("!@");
				for (int i = 0; i < title.length; i++) {
					ArrHeader[i][rsl.getRow()] = title[i];
				}
			}
			rs.setColumnNames(Zidm);
			while (rstmp.next()) {
				rs.getResultSetlist().add(rstmp.getArrString(Zidm));
			}
			rstmp.close();
			rsl.close();
			rsl = con
					.getResultSetList("select biaot from baobpzb where guanjz='"
							+ BAOBPZB_GUANJZ + "'");
			String Htitle = "";
			while (rsl.next()) {
				Htitle = rsl.getString("biaot");
			}
			rt.setTitle(Htitle, ArrWidth);
			rsl.close();
		} else {
			rs = rstmp;

			ArrHeader = new String[][] { {Locale.diancxxb_id_fahb, Locale.gongysb_id_fahb,
					Locale.meikxxb_id_fahb,Locale.yunsdw_id_chepb,Locale.pinzb_id_fahb, Locale.faz_id_fahb,
					 Locale.biaoz_fahb,
					Locale.jingz_fahb, Locale.yingk_fahb,
					Locale.yuns_fahb, Locale.zongkd_fahb, Locale.ches_fahb } };

			ArrWidth = new int[] { 100, 120, 70,70, 50,  50, 50, 50, 50, 50, 50 };

			rt.setTitle(getRptTitle(), ArrWidth);
		}
		ArrWidth = new int[] { 100, 120, 70, 70,50, 50,  50, 50, 50, 50, 50 };
		rt.setTitle(getRptTitle(), ArrWidth);
		rt.title.fontSize = 10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 3, "制表单位："
				+ ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 4, "报表日期：" + getRiq(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 4, "单位：吨、车", Table.ALIGN_RIGHT);

		// String[] arrFormat = new String[] { "", "", "", "", "", "", "",
		// "", "", "", "", "" };

		rt.setBody(new Table(rs, 1, 0, 3));
		// rt.body.setColAlign(0, Table.ALIGN_CENTER);r
		// rt.body.setColAlign(1, Table.ALIGN_CENTER);
		// rt.body.setColAlign(2, Table.ALIGN_CENTER);
		// rt.body.setColAlign(3, Table.ALIGN_CENTER);
		// rt.body.setColAlign(4, Table.ALIGN_CENTER);
		// rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		// rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		// rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		// rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		// rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		// rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		// rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);

		// rt.body.setCellVAlign(i, j, intAlign)
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		// rt.body.setColFormat(arrFormat);
		rt.body.setPageRows(40);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();

		// rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期："
				+ DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(8, 2, "制表：", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize = 10;
		// rt.footer.setRowHeight(1, 1);
		con.Close();
		if (rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		RPTInit.getInsertSql(v.getDiancxxb_id(), getBaseSqlStr().toString(), rt,
				getRptTitle(), "" +BAOBPZB_GUANJZ);
		return rt.getAllPagesHtml();// ph;
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
		String reportType = cycle.getRequestContext().getParameter("lx");
//		System.out.println(reportType+"----");
		if (reportType != null) {
			visit.setInt1(Integer.parseInt(reportType));
			
			visit.setActivePageName("");
			
			
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			if (reportType == null) {
				visit.setInt1(RPTTYPE_GH_ALL);
			}
			
			this.meikid="";
			this.meikuangid="";
			this.getMeikModels();
			this.getMeiKuangDropDownModels();
			this.getGongysDropDownModels();
//			
		
			
			getSelectData();
		}
		
		
	}

	// 按钮的监听事件
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
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

	// 获取供应商
	public IPropertySelectionModel getGongysDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getGongysDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setGongysDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public void getGongysDropDownModels() {
		// String sql="select d.id,d.mingc from diancxxb d where d.id="+((Visit)
		// getPage().getVisit()).getDiancxxb_id()+" or d.fuid="+((Visit)
		// getPage().getVisit()).getDiancxxb_id();
		String sql = "select d.id,d.mingc from diancxxb d ";
		setGongysDropDownModel(new IDropDownModel(sql, "全部"));
		return;
	}

	//	工具栏使用的方法
	//工具栏使用的方法
	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) this.getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (treeid != null) {
			if (!treeid.equals(this.treeid)) {
				((TextField) getToolbar().getItem("diancTree_text"))
						.setValue(((IDropDownModel) getGongysDropDownModel())
								.getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}

	public String getTreeScript() {
		//	System.out.print(((Visit) this.getPage().getVisit()).getDefaultTree().getScript());
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	//	-------------------------电厂Tree-----------------------------------------------------------------

}
