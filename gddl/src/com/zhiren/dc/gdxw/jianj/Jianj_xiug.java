package com.zhiren.dc.gdxw.jianj;

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
import com.zhiren.common.IDropDownBean;
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
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.dc.gdxw.jianj.gdxw_qcjj_hp;
import com.zhiren.dc.huaygl.Caiycl;
import com.zhiren.dc.jilgl.Jilcz;


/*作者:wzb
 *日期:2010-7-5 14:31:10
 *描述:检斤修改功能调整
 * 
 */

/*作者:wzb
 *日期:2010-6-12 9:58:38
 *描述:宣威发电每一车回皮的时候生成采样编码和分样时的桶号,如果回皮以后发现煤矿搞错了,没有修改界面,修改煤矿的同时
 *分样号和质量id同时要修改
 * 
 */

public class Jianj_xiug extends BasePage implements PageValidateListener {
	
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
	
	private String riq; // 保存日期
	
	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}

	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	
    
	
//	煤矿单位下拉框_开始
	public IDropDownBean getMeikdwValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getMeikdwModel().getOptionCount() > 0) {
				setMeikdwValue((IDropDownBean) getMeikdwModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setMeikdwValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(LeibValue);
	}

	public IPropertySelectionModel getMeikdwModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			getMeikdwModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setMeikdwModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
	}

	public void getMeikdwModels() {
		String sql = "select mk.id, mk.mingc from meikxxb mk order by mk.mingc";
		setMeikdwModel(new IDropDownModel(sql, "请选择"));
	}
//	煤矿单位下拉框_结束
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
			
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
			
		}
		this.getSelectData();
	}
	
	public void save() {

		/*保存思路:
		 * 一:修改扣吨
		 *    思路:直接修改chepbtmp表中的扣吨字段,修改完成后重新计算票重字段(票重=毛重-皮重-扣吨),
		 *         然后根据修改行的zhilb_id查找gdxw_cy表中所对应的zhilb_id
		 *         再修改gdxw_cy中汇总的扣吨字段,同时也要修改gdxw_cy的净重字段,
		 *    潜在问题:当修改扣吨时,如果恰好造成该煤样不够制样标准(500吨一制样),程序也不改变gdxw_cy
		 *            表的zhuangt字段,因为我们不知道修改扣吨是在制样之前修改还是在制样之后修改.
		 * 二:修改车号
		 *    思路:直接修改chepbtmp表中的车号字段,对分样没有影响.
		 *    
		 * 三:修改煤矿名称
		 *    思路:1.先找到修改前该煤矿所对应的gdxw_cy表中的数据,在gdxw_cy表中毛重累计,皮重累计,扣吨累计
		 *           净重累计字段上扣掉该煤矿的毛重,皮重,扣吨和净重,(说明:因为修改煤矿名称造成该煤样不够
		 *           500吨的制样标准时,并不改变该煤样的状态字段.)
		 *         2.再根据修改后的煤矿名称和该车的车型,去gdxw_cy表中查找当天是否有该煤矿zhuang=0的不满足
		 *           制样条件的zhilb_id,找到后更新chepbtmp表中该车对应的zhilb_id和fahbtmp_id,并同时更新
		 *           gdxw_cy表中的毛重,皮重,扣吨,净重字段,
		 *         3.如果在gdxw_cy表中未找到该煤矿,该车型,并且zhuangt=0的数据,呢么就在gdxw_cy表中查找该
		 *           煤矿,该车型,zhuangt=1的,并且累计净重最小的zhilb_id,然后更新chepbtmp表中该车对应的
		 *           zhilb_id和fahbtmp_id,并同时更新,也更新gdxw_cy表中的毛重,皮重,扣吨,净重字段,
		 *         4.如果在gdxw_cy表中找不到该煤矿,该车型所对应的数据,那么去掉车型条件,只查找该煤矿当天在
		 *           gdxw_cy表中是否有数据,如果有数据,呢么利用gdxw_cy表中的zhilb_id,更新chepbtmp表中的
		 *           zhilb_id和fahbtmp_id,同时更新gdxw_cy表中的累计
		 *         5.如果该煤矿当天就没有煤,在gdxw_cy表中就查找不到一条数据,呢么重新生成桶号,制样号等信息
		 * 
		 * 
		 */
		
		
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		long zhilb_id=0;
		int flag = 0;
		StringBuffer sbsql = new StringBuffer("begin\n");
		String riq =this.getRiq();
		String find_caiyb="";
		long StrZhilb_id=0;
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		if(mdrsl.next()) {
				
				
//				判断车型
				String Qufdxc="select zhi,beiz from xitxxb x where x.mingc='汽车500吨采样是否区分大小车' and zhuangt=1";
				ResultSetList Daxcrs = con.getResultSetList(Qufdxc); 
				double Dx_jingz=0.0;
				if(Daxcrs.next()){
					 Dx_jingz=Daxcrs.getDouble("beiz");
				}
				Daxcrs.close();
				String chex="";
				double jingz=mdrsl.getDouble("maoz")-mdrsl.getDouble("piz")-mdrsl.getDouble("koud");
				if(jingz>=Dx_jingz){
					chex=" and cy.chex=0";
				}else{
					chex=" and cy.chex=1";
				}
				
				
			
				String meikdwmc="";
				double maoz1=0.0;
				double piz1=0.0;
				double koud1=0.0;
				double jingz1=0.0;
				String sql5="select zhilb_id,meikdwmc,maoz,piz,koud from chepbtmp where id="+mdrsl.getLong("id");
				ResultSetList rs1=con.getResultSetList(sql5);
				if(rs1.next()){
					 zhilb_id=rs1.getLong(0);
					 meikdwmc=rs1.getString(1);
					 maoz1=rs1.getDouble(2);
					 piz1=rs1.getDouble(3);
					 koud1=rs1.getDouble(4);
					 jingz1=maoz1-piz1-koud1;
					
					
				}
			   
				//if(!mdrsl.getString("meikdwmc").equals(meikdwmc)){
					//修改原煤矿所对应的gdxw_cy表中的累计值
					sbsql.append("update gdxw_cy cy set cy.maoz=cy.maoz-"+maoz1+",cy.piz=" +
							"cy.piz-"+piz1+",cy.koud=cy.koud-"+koud1+",cy.jingz=" +
							"cy.jingz-"+jingz1+" where cy.zhilb_id="+zhilb_id+";\n");
					
					
					find_caiyb="select cy.zhilb_id from gdxw_cy cy where cy.meikdwmc='"+mdrsl.getString("meikdwmc")+"'" +
							""+chex+" and cy.zhuangt=0 and to_char(cy.shengcrq,'yyyy-mm-dd')='"+riq+"'";
				
					 ResultSetList rs2=con.getResultSetList(find_caiyb);
					if(rs2.next()){//条件:煤矿名称,车型,zhuangt=0
						StrZhilb_id=rs2.getLong("zhilb_id");
						//更新chepbtmp
						sbsql.append("update chepbtmp c set c.gongysmc='"+mdrsl.getString("meikdwmc")+"', c.meikdwmc='"+mdrsl.getString("meikdwmc")+"', c.cheph='" +
								""+mdrsl.getString("cheph")+"',c.meicb_id=(select max(id) from meicb where mingc='"+mdrsl.getString("meicb_id")+"'),c.meigy='"+mdrsl.getString("meigy")+"'," +
										"c.koud="+mdrsl.getDouble("koud")+",c.zhilb_id="+StrZhilb_id+"," +
										"c.fahbtmp_id="+StrZhilb_id+" where c.id="+mdrsl.getLong("id")+";\n");
						//更新gdxw_cy
						sbsql.append("update gdxw_cy cy set cy.maoz=cy.maoz+"+mdrsl.getDouble("maoz")+",cy.piz=" +
								"cy.piz+"+mdrsl.getDouble("piz")+",cy.koud=cy.koud+"+mdrsl.getDouble("koud")+",cy.jingz=" +
								"cy.jingz+"+mdrsl.getDouble("jingz")+" where cy.zhilb_id="+StrZhilb_id+";\n");
						
					
						
						rs2.close();
					}else{
						find_caiyb="select zhilb_id from gdxw_cy cy where cy.meikdwmc='"+mdrsl.getString("meikdwmc")+"'" +
						""+chex+" and cy.zhuangt=1 and to_char(cy.shengcrq,'yyyy-mm-dd')='"+riq+"' order by cy.jingz";
						rs2.close();
					    rs2=con.getResultSetList(find_caiyb);
						if(rs2.next()){//条件:煤矿,车型,zhuangt=1，并且净重最小一个
							StrZhilb_id=rs2.getLong("zhilb_id");
							//更新chepbtmp
							sbsql.append("update chepbtmp c set c.gongysmc='"+mdrsl.getString("meikdwmc")+"',c.meikdwmc='"+mdrsl.getString("meikdwmc")+"', c.cheph='" +
									""+mdrsl.getString("cheph")+"',c.meicb_id=(select max(id) from meicb where mingc='"+mdrsl.getString("meicb_id")+"'),c.meigy='"+mdrsl.getString("meigy")+"'," +
											"c.koud="+mdrsl.getDouble("koud")+",c.zhilb_id="+StrZhilb_id+"," +
											"c.fahbtmp_id="+StrZhilb_id+" where c.id="+mdrsl.getLong("id")+";\n");
							//更新gdxw_cy
							sbsql.append("update gdxw_cy cy set cy.maoz=cy.maoz+"+mdrsl.getDouble("maoz")+",cy.piz=" +
									"cy.piz+"+mdrsl.getDouble("piz")+",cy.koud=cy.koud+"+mdrsl.getDouble("koud")+",cy.jingz=" +
									"cy.jingz+"+mdrsl.getDouble("jingz")+" where cy.zhilb_id="+StrZhilb_id+";\n");
							
							rs2.close();
						}else{
							
							find_caiyb="select zhilb_id from gdxw_cy cy where cy.meikdwmc='"+mdrsl.getString("meikdwmc")+"'  " +
							" and cy.zhuangt=1 and to_char(cy.shengcrq,'yyyy-mm-dd')='"+riq+"' order by cy.jingz";
							
							rs2.close();
						    rs2=con.getResultSetList(find_caiyb);
							if(rs2.next()){//条件:煤矿,zhuang=1
								
								StrZhilb_id=rs2.getLong("zhilb_id");
								//更新chepbtmp
								sbsql.append("update chepbtmp c set c.gongysmc='"+mdrsl.getString("meikdwmc")+"', c.meikdwmc='"+mdrsl.getString("meikdwmc")+"', c.cheph='" +
										""+mdrsl.getString("cheph")+"',c.meicb_id=(select max(id) from meicb where mingc='"+mdrsl.getString("meicb_id")+"'),c.meigy='"+mdrsl.getString("meigy")+"'," +
												"c.koud="+mdrsl.getDouble("koud")+",c.zhilb_id="+StrZhilb_id+"," +
												"c.fahbtmp_id="+StrZhilb_id+" where c.id="+mdrsl.getLong("id")+";\n");
								//更新gdxw_cy
								sbsql.append("update gdxw_cy cy set cy.maoz=cy.maoz+"+mdrsl.getDouble("maoz")+",cy.piz=" +
										"cy.piz+"+mdrsl.getDouble("piz")+",cy.koud=cy.koud+"+mdrsl.getDouble("koud")+",cy.jingz=" +
										"cy.jingz+"+mdrsl.getDouble("jingz")+" where cy.zhilb_id="+StrZhilb_id+";\n");
								rs2.close();
								
							}else{
								
								//如果要修改后的煤矿当天没有来煤,则新生成桶号,转码表,质量临时表,质量表等
								flag = countCaiy(con, 259,mdrsl.getString("meikdwmc"), mdrsl.getDouble("maoz"), mdrsl.getDouble("piz"), mdrsl.getDouble("koud"),mdrsl.getString("id"));
								
							}
							
						}
						
						
						
						
					}
					
					
				}
				
			
				
				
				
				
				
			//}
		
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		
		
		
//		更新完原煤矿累计值后检查净重,如果净重小于5吨,删除gdxw_cy表该数据
		find_caiyb="select jingz from gdxw_cy where zhilb_id="+zhilb_id;
		ResultSetList rs3=con.getResultSetList(find_caiyb);
		if(rs3.next()){
			double  zhilb_jingz=rs3.getDouble("jingz");
			if(zhilb_jingz<5){
				find_caiyb="delete gdxw_cy where zhilb_id="+zhilb_id;
				
				flag=con.getUpdate(find_caiyb);
				if(flag == -1){
					WriteLog.writeErrorLog(this.getClass().getName() + 
							"\nSQL:" + find_caiyb +"删除gdxw_cy中的zhilb_id失败!");
					setMsg(this.getClass().getName() + ":更新更新gdxw_cy中的zhilb_id失败!");
					
				}
			}
		}
		
		
		rs3.close();
		// 判断更新后是否达到了按数量分组的条件, 如果达到分组条件,更新gdxw_cy表的zhuangt字段
		 find_caiyb = "select zhi,danw from xitxxb where  mingc ='采样按数量分组' and zhuangt=1 and leib= '数量'";
		rs3=con.getResultSetList(find_caiyb);
		double Fenzl = 0;
		if(rs3.next()){
			Fenzl =rs3.getDouble("ZHI");
		}
		find_caiyb="select jingz from gdxw_cy where zhilb_id="+StrZhilb_id;
		rs3=con.getResultSetList(find_caiyb);
		if(rs3.next()){
			if(rs3.getDouble("jingz") > Fenzl){
				find_caiyb= "update gdxw_cy set zhuangt = 1 where zhilb_id ="+ StrZhilb_id;
				flag=con.getUpdate(find_caiyb);
				if(flag == -1){
					WriteLog.writeErrorLog(this.getClass().getName() + 
							"\nSQL:" + find_caiyb +"删除gdxw_cy中的zhilb_id失败!");
					setMsg(this.getClass().getName() + ":更新更新gdxw_cy中的zhilb_id失败!");
					
				}
				
			}
		}
		
		
		mdrsl.close();
		con.Close();
		this.setMsg("修改成功!");
	
	}
	
	public void getSelectData() {
		

		Visit visit = (Visit) getPage().getVisit();
		String tiaoj="";
		String meikmc=this.getMeikdwValue().getValue();
		//System.out.println(meikmc);
		if(meikmc.equals("请选择")){
			tiaoj="";
		}else{
			tiaoj= "and c.meikdwmc='"+meikmc+"'";
		}
			
		JDBCcon con = new JDBCcon();
		String sql = "select c.id,c.zhilb_id,c.fahbtmp_id,c.meikdwmc,c.cheph,c.maoz,c.piz,c.koud," +
		"(c.maoz-c.piz-c.koud) as jingz,c.biaoz,m.mingc as meicb_id,c.meigy,to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss') as zhongcsj," +
		"to_char(c.qingcsj,'yyyy-mm-dd hh24:mi:ss') as qingcsj,c.zhongcjjy,c.qingcjjy\n" +
		" from chepbtmp c,meicb m\n" + 
		"where c.daohrq=to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
		"and c.fahb_id=0 and c.meicb_id=m.id(+) \n" + 
		""+tiaoj+"\n"+
		"order by c.meikdwmc,c.zhongcsj";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
//		 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
//		设置该grid不进行分页
		egu.addPaging(0);
		
		
		egu.getColumn("id").setEditor(null);
		egu.getColumn("zhilb_id").setEditor(null);
		egu.getColumn("zhilb_id").setHidden(true);
		egu.getColumn("fahbtmp_id").setEditor(null);
		egu.getColumn("fahbtmp_id").setHidden(true);
		
		
		egu.getColumn("meikdwmc").setHeader("煤矿单位");
		
		
		ComboBox cmk= new ComboBox();
		egu.getColumn("meikdwmc").setEditor(cmk);
		cmk.setEditable(true);
		String mkSql="select id,piny || '-' ||mingc from meikxxb order by xuh";
		egu.getColumn("meikdwmc").setComboEditor(egu.gridId, new
		IDropDownModel(mkSql));
		
		egu.getColumn("cheph").setHeader("车号");
		egu.getColumn("cheph").setWidth(80);
		
		egu.getColumn("maoz").setHeader("毛重");
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("maoz").setWidth(60);
		
		egu.getColumn("piz").setHeader("皮重");
		egu.getColumn("piz").setEditor(null);
		egu.getColumn("piz").setWidth(60);
		
		egu.getColumn("koud").setHeader("扣吨");
		egu.getColumn("koud").setWidth(60);
		
		egu.getColumn("jingz").setHeader("净重");
		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("jingz").setWidth(60);
		
		egu.getColumn("biaoz").setHeader("票重");
		//egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("biaoz").setWidth(60);
		
		egu.getColumn("meicb_id").setHeader("卸煤地点");
		egu.getColumn("meicb_id").setWidth(100);
		
		egu.getColumn("meigy").setHeader("煤管员");
		egu.getColumn("meigy").setWidth(60);
		
		egu.getColumn("zhongcsj").setHeader("重衡时间");
		egu.getColumn("zhongcsj").setEditor(null);
		egu.getColumn("zhongcsj").setWidth(120);
		
		egu.getColumn("qingcsj").setHeader("轻车时间");
		egu.getColumn("qingcsj").setEditor(null);
		egu.getColumn("qingcsj").setWidth(120);
		
		egu.getColumn("zhongcjjy").setHeader("重衡检斤员");
		egu.getColumn("zhongcjjy").setEditor(null);
		egu.getColumn("zhongcjjy").setWidth(80);
		
		egu.getColumn("qingcjjy").setHeader("轻衡检斤员");
		egu.getColumn("qingcjjy").setEditor(null);
		egu.getColumn("qingcjjy").setWidth(80);
		
		
//		设置煤场下拉框
		ComboBox cmc= new ComboBox();
		egu.getColumn("meicb_id").setEditor(cmc); 
		cmc.setEditable(true);
		String mcSql="select id,mingc from meicb order by xuh";
		egu.getColumn("meicb_id").setComboEditor(egu.gridId, new IDropDownModel(mcSql));
//      验煤员对应验煤章下拉框
		ComboBox ymz= new ComboBox();
		egu.getColumn("meigy").setEditor(ymz);
		ymz.setEditable(true);
		String ymy = "select id ,zhiw from renyxxb r where r.bum='验煤员' order by r.zhiw";
		egu.getColumn("meigy").setComboEditor(egu.gridId,new IDropDownModel(ymy));

		
		
		egu.addTbarText("过衡日期：");
		DateField df = new DateField();
		df.setValue(getRiq());
		df.setId("riq");
		df.Binding("Riq", "");
		egu.addToolbarItem(df.getScript());
		egu.addOtherScript("riq.on('change',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		
		
		egu.addTbarText("煤矿单位：");
		ComboBox comb = new ComboBox();
		comb.setWidth(120);
		comb.setListWidth(150);
		comb.setTransform("Meikdw");
		comb.setId("Meikdw");
		comb.setLazyRender(true);
		comb.setEditable(true);
		egu.addToolbarItem(comb.getScript());
		egu.addOtherScript("Meikdw.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		
		
		
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		
		

	
	
		setExtGrid(egu);
		rsl.close();
		con.Close();
	
	}
	
	/**
	 * 如果在页面上取到的值为Null或是空串，那么向数据库保存字段的默认值
	 * @param value
	 * @return
	 */
	public String getSqlValue(String value) {
		return value == null || "".equals(value) ? "default" : value;
	}
	
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setProSelectionModel3(null); // 煤矿单位下拉框
			visit.setDropDownBean3(null);
			
			
		}
		getSelectData();
	}
	
	
	
	
	//新增采样
	private int countCaiy(JDBCcon con, long diancxxb_id,String meikdwmc,
			double maoz, double piz, double koud,String chepbtmp_id){
		Visit visit = (Visit) this.getPage().getVisit();
		String Qufdxc="select zhi,beiz from xitxxb x where x.mingc='汽车500吨采样是否区分大小车' and zhuangt=1 and diancxxb_id="+visit.getDiancxxb_id()+"";
		ResultSetList Daxcrs = con.getResultSetList(Qufdxc); 
		String sql="";
		boolean Bool_Qufdxc=false;//是否区分大小车采样
		boolean Bool_dac=false;  //是否是大车
		if(Daxcrs.next()){
			String Dx_zhi=Daxcrs.getString("zhi");
			double Dx_jingz=Daxcrs.getDouble("beiz");
			//500吨分样是否区分大小车
			if(Dx_zhi.equals("是")){
				Bool_Qufdxc=true;
				String chex="";
				//如果maoz-piz-koud>=Dx_jingz ,是大车,否则是小车,(Dx_jingz是45吨),大车chex=0,小车chex=1
				if(maoz-piz-koud>=Dx_jingz){
					Bool_dac=true;
					chex=" and chex=0";
				}else{
					chex=" and chex=1";
				}
				 sql = "select * from gdxw_cy where diancxxb_id = " + diancxxb_id +
				" and meikdwmc = '" + meikdwmc + "' and zhuangt = 0 and shengcrq>=to_date('"+this.getRiq()+"','yyyy-mm-dd') and shengcrq<to_date('"+this.getRiq()+"','yyyy-mm-dd')+1  "+chex+""; 
			}else{
//				判断采样表中有没有未结束的采样信息(zhuangt = 0)
				 sql = "select * from gdxw_cy where diancxxb_id = " + diancxxb_id +
				" and meikdwmc = '" + meikdwmc + "' and zhuangt = 0 and shengcrq>=to_date('"+this.getRiq()+"','yyyy-mm-dd') and shengcrq<to_date('"+this.getRiq()+"','yyyy-mm-dd')+1 ";
			}
		}
		
		
		
 
		ResultSetList rs = con.getResultSetList(sql); 
		String id = "";
		String sql1="";
		int flag = 0;
		if(rs.next()){
			
			//直接生成新的采样信息,不用更新以前的采样信息
			
		}else{
//			如果为新增采样
			id = MainGlobal.getNewID(diancxxb_id);
//			新增采样表、质量表
			long zhilb_id = Jilcz.getZhilbid(con, null, DateUtil.getDate(this.getRiq()), visit.getDiancxxb_id());
//			生成采样、制样、化验编号
			flag = Caiycl.CreatBianh(con, zhilb_id, visit.getDiancxxb_id());
			if(flag == -1){
				WriteLog.writeErrorLog(this.getClass().getName() + 
						"\n构造采样失败!");
				setMsg(this.getClass().getName() + ":构造采样失败!");
				return flag;
			}
			
			
//			查找未使用的存样位置
			
			sql = 
			
			"select * from cunywzb where id in\n" +
			"(select id from cunywzb where zhuangt = 1\n" + 
			"minus\n" + 
			"select cunywzb_id from caiyb where zhilb_id in\n" + 
			"(select zhilb_id from gdxw_cy where  \n" +
			" shengcrq>=to_date('"+this.getRiq()+"','yyyy-mm-dd') and shengcrq<=to_date('"+this.getRiq()+"','yyyy-mm-dd')+1))\n" + 
			"and rownum = 1";
			rs = con.getResultSetList(sql);
			if(rs.next()){
//				更新采样表中存样位置
				sql = "update caiyb set cunywzb_id = " + rs.getString("id") + 
				" where zhilb_id = " + zhilb_id;
//             更新chepbtmp中的zhilb_id
				 sql1="update chepbtmp set gongysmc='"+meikdwmc+"',meikdwmc='"+meikdwmc+"', zhilb_id="+zhilb_id+",fahbtmp_id="+zhilb_id+" where id="+chepbtmp_id+"";
				con.getUpdate(sql);
				con.getUpdate(sql1);
			}else{
				WriteLog.writeErrorLog(this.getClass().getName() + 
				"\n存样位置已满,请添加存样位置!");
				setMsg(this.getClass().getName() + ":存样位置已满,请添加存样位置!");
				return -1;
			}
//			long 
			double jingz=maoz-piz-koud;
			//是否区分大小车采样
			if(Bool_Qufdxc){
				//如果是大车,chex=0,否认如果是小车chex=1
				if(Bool_dac){
					sql = "insert into gdxw_cy(id, diancxxb_id, meikdwmc, maoz, zhilb_id,zhuangt,piz,koud,jingz,shengcrq,chex,zhiyrq) " +
					"values("+id+"," + diancxxb_id +
					",'" + meikdwmc + "'," + maoz + "," + zhilb_id+ ",0,"+piz+","+koud+","+jingz+",to_date('"+getRiq()+"','yyyy-mm-dd'),0,to_date('"+getRiq()+"','yyyy-mm-dd'))";
				}else{
					sql = "insert into gdxw_cy(id, diancxxb_id, meikdwmc, maoz, zhilb_id,zhuangt,piz,koud,jingz,shengcrq,chex,zhiyrq) " +
					"values("+id+"," + diancxxb_id +
					",'" + meikdwmc + "'," + maoz + "," + zhilb_id+ ",0,"+piz+","+koud+","+jingz+",to_date('"+getRiq()+"','yyyy-mm-dd'),1,to_date('"+getRiq()+"','yyyy-mm-dd'))";
				}
				
			}else{
				sql = "insert into gdxw_cy(id, diancxxb_id, meikdwmc, maoz, zhilb_id,zhuangt,piz,koud,jingz,shengcrq,chex,zhiyrq) " +
				"values("+id+"," + diancxxb_id +
				",'" + meikdwmc + "'," + maoz + "," + zhilb_id+ ",0,"+piz+","+koud+","+jingz+",to_date('"+getRiq()+"','yyyy-mm-dd'),0,to_date('"+getRiq()+"','yyyy-mm-dd'))";
			}
			
			flag = con.getInsert(sql);
			if(flag == -1){
				WriteLog.writeErrorLog(this.getClass().getName() + 
				"\n更新gdxw_cy信息失败!");
				setMsg(this.getClass().getName() + ":更新gdxw_cy信息失败!");
				return flag;
			}
		}
		rs.close();
		return flag;
	}
}