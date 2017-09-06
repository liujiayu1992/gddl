package com.zhiren.dc.gdxw.jianj;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.huaygl.Caiycl;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:tzf
 * 时间:2009-12-27
 * 修改内容:增加锁定行 和颜色标记操作
 */
/*
 * 作者：王磊
 * 时间：2009-09-23
 * 描述:选择分样桶的时候排除(3天内)制样的采样桶号
 */
/*
 * 作者：王磊
 * 时间：2009-09-22 
 * 描述：增加煤管员编辑框 alter table chepbtmp add meigy varchar2(30) null
 */

/*
 * 作者：王总兵
 * 时间：2009-09-26
 * 描述：gdxw_cy增加生成时间字段 alter table gdxw_cy add shengcrq date null
 */
/*
 * 作者：王总兵
 * 时间：2009-10-16
 * 描述：gdxw_cy增加车型字段,0是大车,1是小车,大小车的判断标准是净重是否大于45吨
 *  alter table gdxw_cy add chex number(1) null;
 */
/**
 * @author Rock
 * @since 2009.09.14
 * @version 1.0
 * @discription 国电宣威汽车衡过衡
 */
public class gdxw_qcjj extends BasePage implements PageValidateListener {
	//private final static String Customkey_mz = "gdxw_qcjj_mz";
	// 界面用户提示
	private String msg = "";
	private int abc=0;
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public IDropDownModel getYunsdwModel() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sql = "select id,mingc from yunsdwb where diancxxb_id="
				+ visit.getDiancxxb_id();
		return new IDropDownModel(sql);
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	//拒收
	private boolean _JusChick = false;

	public void JusButton(IRequestCycle cycle) {
		_JusChick = true;
	}


	private boolean ZSuodzt(JDBCcon con,String cheph) {
		boolean issuod = false;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select cl.islocked from chelxxb cl where cl.cheph='");
		sql.append(cheph).append("'");
		ps = con.getPresultSet(sql.toString());
		try {
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getString("islocked").equals("1")) {
					issuod = true;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return issuod;
	}
	
	
	
	public String formatXiaos(int size,double number){
		StringBuffer formatString = new StringBuffer("0");
		  if(size>0){
		   formatString.append(".");
		  }
		  for (int i = 0; i < size; i++) {
		   formatString.append("#");
		  }
		  DecimalFormat df = new DecimalFormat(formatString.toString());
		 return df.format(number);
	}
	

	
	private boolean IsMaozXiangd(JDBCcon con,double maoz,String zhongchh) {
    /*	判断当前衡器上车辆的毛重是否与当前衡器上一车的毛重相等,如果当然车辆的毛重与上一车的相等,有可能是过衡人员捣鬼
		用上一车的毛重顶替当然车辆的毛重*/
		boolean isxiangd = false;//是否相等
		double mao1=Double.parseDouble(formatXiaos(1,maoz));
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		//sysdate-0.002 相当于当前日期减3分钟
		sql.append("select cl.maoz from chepbtmp cl where  zhongcsj>=sysdate-0.002 and cl.zhongchh='");
		sql.append(zhongchh).append("' order by zhongcsj desc");
		ps = con.getPresultSet(sql.toString());
		try {
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getDouble("maoz")==mao1) {
					isxiangd = true;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return isxiangd;
	}
	
	private boolean IsChongfgh(JDBCcon con,String id) {
		boolean ischongfgh = true;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select  c.meikdwmc  from chepbtmp c where c.id=");
		sql.append(id).append("");
		ps = con.getPresultSet(sql.toString());
		try {
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getString("meikdwmc")==null) {
					ischongfgh = false;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return ischongfgh;
	}
	
	
	
	private boolean Koudlsd(JDBCcon con,String meikdwmc) {
		boolean isKoudl = false;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from meikkdlsdb sd where  zhuangt=1 and sd.meikdwmc='");
		sql.append(meikdwmc).append("'");
		ps = con.getPresultSet(sql.toString());
		try {
			rs = ps.executeQuery();
			while (rs.next()) {
				
					isKoudl = true;
				
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return isKoudl;
	}
	
	
	
	
	
	private void Save() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有做出任何更改哦！");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(this.getClass().getName() + "\n"
							+ ErrorMessage.NullResult + "\n");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		int flag = 0;
		
		
		
		
		if (rsl.next()) {
			
//			判断是否重复过衡,当这个车在其它衡器上已经过衡,在这个衡器上因为没刷新,操作员又选中该车进行过衡操作
			if(IsChongfgh(con,rsl.getString("id"))){
				setMsg("选择车号错误,该车号不存在,请刷新!");
				con.Close();
				return;
			}
			
			if (ZSuodzt(con,rsl.getString("cheph"))) {
				setMsg("该车已被锁定,操作结束!");
				con.Close();
				return;
			}
			
			if (IsMaozXiangd(con,rsl.getDouble("maoz"),rsl.getString("zhongchh"))) {
				setMsg("该车毛重与该衡器上一车毛重相同,请换其它衡器过衡或等待3分钟!");
				con.Close();
				return;
			}
			
			
			if (Koudlsd(con,rsl.getString("meikdwmc"))) {//扣吨率锁定判断
				setMsg("------"+rsl.getString("meikdwmc")+"----当日累计扣吨率已经超过系统设置值,系统已自动禁止其过重衡");
				con.Close();
				return;
			}
			long diancxxb_id = 0;
			if (visit.isFencb()) {
				diancxxb_id = (getExtGrid().getColumn("dcmc").combo)
						.getBeanId(rsl.getString("dcmc"));
			} else {
				diancxxb_id = visit.getDiancxxb_id();
			}
			String meikdwmc = rsl.getString("meikdwmc");
			double maoz = rsl.getDouble("maoz");
			double maoz1=maoz;
			String zhongcjjy=rsl.getString("zhongcjjy");
			String zhongchh=rsl.getString("zhongchh");
			long id=rsl.getLong("id");
			String beiz="";
			
			
		
			
			String sql="update chepbtmp set diancxxb_id="+diancxxb_id+",gongysmc='"+meikdwmc+"',meikdwmc='"+meikdwmc+"',maoz="+maoz+"" +
					",zhongcjjy='"+zhongcjjy+"',zhongchh='"+zhongchh+"',zhongcsj=sysdate,beiz='"+beiz+"'" +
					"  where id="+id+"";
			flag = con.getUpdate(sql);
			if(flag == -1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(this.getClass().getName() + 
						"\nSQL:" + sql +"更新车皮表TMP失败!");
				setMsg(this.getClass().getName() + ":更新车皮表TMP失败!");
				return;
			}
			
		}
		con.commit();
		con.Close();
		setMsg("毛重保存成功");
	}
	

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			init();
		}
		if (_JusChick) {
			_JusChick = false;
			Jus();
			init();
		}
		
		
	}

	private void Jus() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有做出任何更改哦！");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
	
		int flag = 0;
		String sql="update chepbtmp set isjus=1 ,jussj=sysdate,jusry='"+visit.getRenymc()+"'  where id="+getChange()+"";
		flag = con.getUpdate(sql);
		if(flag == -1){
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(this.getClass().getName() + 
						"\nSQL:" + sql +"更新车皮表TMP失败!");
			setMsg(this.getClass().getName() + ":更新车皮表TMP失败!");
			return;
		}
			
		
		con.commit();
		con.Close();
		setMsg("拒收成功");
	}

	public void setChepid(String fahids) {
		((Visit) this.getPage().getVisit()).setString1(fahids);
	}



	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sqldc = "and d.id = " + visit.getDiancxxb_id();
		String dcmcdef = visit.getDiancmc();
		String dcsql = "select id,mingc from diancxxb where fuid="
			+ visit.getDiancxxb_id();
		ResultSetList rsl;
//		如果是一厂多制
		if(visit.isFencb()){
			sqldc = "and d.fuid = " + visit.getDiancxxb_id();
//			取得默认电厂名称
			rsl = con.getResultSetList(dcsql);
			if(rsl.next()){
				dcmcdef = rsl.getString("mingc");
			}
			rsl.close();
		}
//		设置车头号默认值
		String sql = 
			"select c.id,c.cheph, d.mingc dcmc, c.meikdwmc,  0 as maoz, nvl('" +
			visit.getRenymc()+"','') as zhongcjjy,'' as zhongchh,to_char(c.lursj,'yyyy-mm-dd hh24:mi:ss') as lursj\n" + 
			"from chepbtmp c, diancxxb d, yunsfsb y\n" + 
			"where c.diancxxb_id = d.id and c.zhongcsj is null\n" + 
			"and c.yunsfs = y.mingc\n" + 
			"and c.isjus =0\n" + 
			"and y.id = " + SysConstant.YUNSFS_QIY + "\n" +
			sqldc + "\n" +
			"order by c.lursj ";
		rsl = con.getResultSetList(sql);
//		如果没有取到默认数据
		if(rsl == null){
			
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
//		设置GRID可编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
//		设置该grid不进行分页
		egu.addPaging(0);
		
//		设置列名
		egu.getColumn("dcmc").setHeader(Locale.diancxxb_id_fahb);
		egu.getColumn("meikdwmc").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("zhongcjjy").setHeader(Locale.zhongcjjy_chepb);
		egu.getColumn("zhongchh").setHeader(Locale.zhongchh_chepb);
		egu.getColumn("lursj").setCenterHeader("采样时间");
//		设置列宽
		egu.getColumn("dcmc").setWidth(70);
		egu.getColumn("meikdwmc").setWidth(170);
		egu.getColumn("cheph").setWidth(110);
		egu.getColumn("maoz").setWidth(80);
		egu.getColumn("zhongcjjy").setWidth(100);
		egu.getColumn("zhongchh").setWidth(90);
		egu.getColumn("lursj").setWidth(200);
//		设置列是否可编辑
		egu.getColumn("cheph").setEditor(null);
		egu.getColumn("lursj").setEditor(null);
		egu.getColumn("zhongcjjy").setEditor(null);
		egu.getColumn("zhongchh").setEditor(null);
		
		egu.getColumn("zhongcjjy").setHidden(true);
		
//		设置列的上下限
		egu.getColumn("maoz").editor.setAllowBlank(false);
		sql = "select * from shuzhlfwb where leib ='数量' and mingc = '汽车衡毛重' and diancxxb_id = "
				+ visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			egu.getColumn("maoz").editor.setMinValue(rsl.getString("xiax"));
			egu.getColumn("maoz").editor.setMaxValue(rsl.getString("shangx"));
		}
		rsl.close();

//		设置分厂别电厂下拉框
		if (visit.isFencb()) {
			ComboBox dc = new ComboBox();
			egu.getColumn("dcmc").setEditor(dc);
			dc.setEditable(true);
			egu.getColumn("dcmc").setComboEditor(egu.gridId,
					new IDropDownModel(dcsql));
		} else {
			egu.getColumn("dcmc").setHidden(true);
			egu.getColumn("dcmc").setEditor(null);
		}
//		设置煤矿下拉框
		ComboBox cmk= new ComboBox();
		egu.getColumn("meikdwmc").setEditor(cmk); cmk.setEditable(true);
		String mkSql="select id,piny || '-' ||mingc from meikxxb order by xuh";
		egu.getColumn("meikdwmc").setComboEditor(egu.gridId, new
		IDropDownModel(mkSql));
		
		GridButton refurbish = new GridButton("刷新",
		"function (){document.getElementById('RefurbishButton').click();}");
			refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addTbarText("-");// 设置分隔符
		// 原来保存按钮
		//egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		//新保存
		//GridButton gbs = new GridButton("保存1", getBtnHandlerScript());
		
		
		
		/*String Gongysstr="function (){"+
		"if(!gridDiv_sm.hasSelection()){Ext.MessageBox.alert('提示','请先点击要返回供货单位编码的数据行！');return; };"+
   		" var url = 'http://'+document.location.host+document.location.pathname;"+
        "var end = url.indexOf(';');"+
		"url = url.substring(0,end);"+
		"var cheh=gridDiv_sm.getSelected().get('CHEPH')+',';"+
		"var meik=gridDiv_sm.getSelected().get('MEIKDWMC')+',';"+
		"var maoz=gridDiv_sm.getSelected().get('MAOZ');"+
		"var linjc=cheh+meik+maoz;"+
   	    "url = url + '?service=page/' + 'gdxw_qcjj_son&tiaoj='+linjc;\n"+
   	    "var rewin =  window.showModalDialog(url,'newWin','dialogWidth=1000px;dialogHeight=300px;');\n" +
  
   	   "if   (rewin != null){gridDiv_sm.getSelected().set('GONGHDWBM',rewin.bianh);}" +
   	    "" +
   	    "}";
	
		egu.addToolbarItem("{"+new GridButton("新保存",""+Gongysstr+"").getScript()+"}");*/
		
		GridButton gbs = new GridButton("保存毛重",GridButton.ButtonType_Save,
				"gridDiv", egu.getGridColumns(), "SaveButton");
		egu.addTbarBtn(gbs);
		egu.addTbarText("-");// 设置分隔符
		egu.addTbarText("->");// 设置分隔符
		
		
		
		
		int caiy=0;
		int zhongc=0;
		int kongc=0;
		double jingz=0.0;
		int jus=0;
		//采样车数
		sql="select count(*) as caiy from chepbtmp c\n" +
			"where c.lursj>=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')\n" +
			" and c.lursj<to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')+1";
		rsl = con.getResultSetList(sql);
		if (rsl.next()){
			caiy=rsl.getInt("caiy");
		}
		//过重磅车数
		sql="select count(*) as zhongc from chepbtmp c\n" +
			"where c.zhongcsj>=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')\n" + 
			"and c.zhongcsj<to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')+1 \n"+
			"and c.maoz>0\n" + 
			"and c.zhongcsj is not null";
		rsl = con.getResultSetList(sql);
		if (rsl.next()){
			zhongc=rsl.getInt("zhongc");
		}
		//回皮车数,净重
		sql="select count(*) as huip,sum(c.maoz-c.piz-c.koud) as jingz from chepbtmp c\n" +
			"where c.qingcsj>=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')\n" + 
			"and c.qingcsj<to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')+1 \n"+
			"and c.piz>0\n" + 
			"and c.qingcsj is not null";
		rsl = con.getResultSetList(sql);
		if (rsl.next()){
			kongc=rsl.getInt("huip");
			jingz=rsl.getDouble("jingz");
		}
		//拒收车数
		sql="select count(*) as jus from chepbtmp c\n" +
		"where c.jussj>=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')\n" +
		" and c.jussj<to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')+1"+
		" and c.isjus=1";
		rsl = con.getResultSetList(sql);
		if (rsl.next()){
			jus=rsl.getInt("jus");
		}
		
		egu.addTbarText(
				"今日:&nbsp;采样:"+caiy+"车," +
				"&nbsp;过重:"+zhongc+" 车,&nbsp;" +
				"过空:"+kongc+" 车,&nbsp;拒收:"+jus+"车,&nbsp;今日来煤:"+jingz+"吨" +
				"&nbsp;");
		
//		拒收
		egu.addTbarText("-");// 设置分隔符
		String sPwHandler = "function(){"
			+"if(gridDiv_sm.getSelected() == null){"
			+"	 Ext.MessageBox.alert('提示信息','请选中车号进行拒收');"
			+"	 return;"
			+"}"
			+"var grid_rcd = gridDiv_sm.getSelected();"
		
			+"Ext.MessageBox.confirm('提示信息','拒收车号:&nbsp;&nbsp;&nbsp;'+ grid_rcd.get('CHEPH')+'&nbsp;&nbsp;&nbsp;确认吗?',function(btn){"
			+"	 if(btn == 'yes'){"
			+"		    grid_history = grid_rcd.get('ID');"
			+"			var Cobj = document.getElementById('CHANGE');"
			+"			Cobj.value = grid_history;"
			+"			document.getElementById('JusButton').click();"
			+"	       	}"
			+"	  })"
			+"}";
		egu.addTbarBtn(new GridButton("拒收",sPwHandler));
		egu.addTbarText("-");// 设置分隔符
		egu.addOtherScript("gridDiv_grid.on('rowclick',function(){Mode='sel';DataIndex='MAOZ';  });");
		egu.addOtherScript("gridDiv_grid.on('beforeedit',function(e){ "
				+"if(e.row!=row_maoz_index){ e.cancel=true; } \n"
				+"if(e.field == 'MAOZ'){e.cancel=true;}});\n");
		
		egu.addOtherScript("function  gridDiv_save(rec){if(confirm('是否保存车号:   '+rec.get('CHEPH') + '     煤矿: ' + rec.get('MEIKDWMC')+'        ')){return \"\";}else{return \"return\";}};");
		
		egu.addOtherScript(" gridDiv_grid.on('cellclick',function(grid,rowIndex,columnIndex,e){ \n " +
				" if(columnIndex==2){ \n" +
				" row_maoz_index=rowIndex;\n"+
				" gridDiv_grid.getView().refresh();\n"+
				" gridDiv_grid.getView().getRow(rowIndex).style.backgroundColor=\"red\";} \n"+
				" else { \n" +
//				" gridDiv_grid.getView().focusRow(row_maoz_index); \n" +
				" } \n"+
				" }); \n");
		
		egu.addOtherScript(" gridDiv_grid.addListener('afteredit',function(e){\n " +
				" gridDiv_grid.getView().getRow(e.row).style.backgroundColor=\"red\"; \n" +
				"} ); \n");
		setExtGrid(egu);
		/*  皮重grid初始化  */
		
		sql = "select c.id,c.cheph, c.meikdwmc,c.maoz, c.zhongcjjy , \n" +
			" to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi') as zhongcsj,to_char(c.lursj,'yyyy-mm-dd hh24:mi') as lursj " +
			"  from chepbtmp c\n" + 
			"where c.zhongcsj is not null and c.qingcsj is null and c.isjus =0  order by c.zhongcsj desc ";
		rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu1 = new ExtGridUtil("gridDivPiz", rsl);
//		设置页面可编辑
		egu1.setGridType(ExtGridUtil.Gridstyle_Edit);
//		设置页面宽度
		egu1.setWidth(Locale.Grid_DefaultWidth);
//		设置该grid不进行分页
		egu1.addPaging(0);
//		设置列名
		egu1.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu1.getColumn("meikdwmc").setHeader(Locale.meikxxb_id_fahb);
		egu1.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu1.getColumn("lursj").setHeader("采样时间");
		egu1.getColumn("zhongcsj").setHeader("重车时间");
		egu1.getColumn("zhongcjjy").setHeader("重车检斤员");
		
//		设置列宽
		egu1.getColumn("cheph").setWidth(110);
		egu1.getColumn("meikdwmc").setWidth(170);
		egu1.getColumn("maoz").setWidth(80);
		egu1.getColumn("lursj").setWidth(180);
		egu1.getColumn("zhongcsj").setWidth(180);
		egu1.getColumn("zhongcjjy").setWidth(100);

//		设置列是否可编辑
		egu1.getColumn("meikdwmc").setEditor(null);
		egu1.getColumn("lursj").setEditor(null);
		egu1.getColumn("cheph").setEditor(null);
		egu1.getColumn("maoz").setEditor(null);
		egu1.getColumn("zhongcsj").setEditor(null);
		egu1.getColumn("zhongcjjy").setEditor(null);
		
	
		egu1.addOtherScript(" if(gridDivPiz_ds.getCount()>=1){ gridDivPiz_grid.getView().getRow(0).style.backgroundColor=\"red\"; }\n");
		setPizGrid(egu1);
		con.Close();
	}


	

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public ExtGridUtil getPizGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setPizGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridScriptPiz() {
		if (getPizGrid() == null) {
			return "";
		}
		return getPizGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	public String getGridPizHtml() {
		if (getPizGrid() == null) {
			return "";
		}
		return getPizGrid().getHtml();
	}

	public DefaultTree getDefaultTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public void setDefaultTree(DefaultTree dftree1) {
		((Visit) this.getPage().getVisit()).setDefaultTree(dftree1);
	}

	public String getTreeScript() {
		if(getDefaultTree()==null){
			return "";
		}
		// System.out.print(((Visit)
		// this.getPage().getVisit()).getDefaultTree().getScript());
		return getDefaultTree().getScript();
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

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setGongysModels() {
		String sql = "select id,mingc from gongysb order by xuh,mingc";
		setGongysModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getMeikModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setMeikModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setMeikModels() {
		String sql = "select id,mingc from meikxxb order by xuh,mingc";
		setMeikModel(new IDropDownModel(sql));
	}

	private void setJianjdmodel() {
		JDBCcon con = new JDBCcon();
		String sql = "select zhi from xitxxb where mingc='汽车衡检斤单模式' and zhuangt = 1 and diancxxb_id ="
				+ ((Visit) this.getPage().getVisit()).getDiancxxb_id();
		ResultSetList rsl = con.getResultSetList(sql);
		String model = "";
		if (rsl.next()) {
			model = rsl.getString("zhi");
		}
		rsl.close();
		((Visit) this.getPage().getVisit()).setString15(model);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setString1(null);
			init();
		}
	}

	private void init() {
		setGongysModels();
		setMeikModels();
		setJianjdmodel();
		setExtGrid(null);
		setPizGrid(null);
		setDefaultTree(null);
		getSelectData();
	}
}