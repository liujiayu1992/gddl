package com.zhiren.dc.feiyglb;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者:tzf
 * 时间:2009-4-21
 * 修改内容:增加电厂树，grid表格增加 电厂 字段，项目和电厂之间的关联 可以改变.
 */
/**
 * @author 曹林
 * 2009-03-18
 *1,弃掉没有使用的收款单位
2，增加了费用项目
3，供应商改成煤矿
 */
/*
 * 修改内容：1、修改Save1(String strchange, Visit visit)方法，当维护电厂级别是3的电厂数据时，对应的下属电厂的数据也跟着更改。
 *    2、去掉工具条上的电厂树，去掉Grid里电厂字段的下拉框。
 * 修改日期：2009-09-16
 * 修改人：尹佳明
 */

public class Feiyglb extends BasePage implements PageValidateListener {
	
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	
	protected void initialize() {
		super.initialize();
		msg = "";
	}

	public IDropDownBean getMeikxxValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			
			((Visit) getPage().getVisit())
				.setDropDownBean2((IDropDownBean) getIMeikxxModels().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setMeikxxValue(IDropDownBean Value) {
		
		if(((Visit) getPage().getVisit()).getDropDownBean2()!=Value){
			
//			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}

	public void setIMeikxxModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getIMeikxxModel() {
		
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			
			getIMeikxxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getIMeikxxModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "select id,mingc from meikxxb order by mingc";
			((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
	/////
	public boolean _shoukdwchange = false;

	private IDropDownBean _ShoukdwValue;

	public IDropDownBean getShoukdwValue() {
		if (_ShoukdwValue == null) {
			_ShoukdwValue = (IDropDownBean) getIShoukdwModels().getOption(0);
		}
		return _ShoukdwValue;
	}

	public void setShoukdwValue(IDropDownBean Value) {
		long id = -2;
		if (_ShoukdwValue != null) {
			id = _ShoukdwValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_shoukdwchange = true;
			} else {
				_shoukdwchange = false;
			}
		}
		_ShoukdwValue = Value;
	}

	private IPropertySelectionModel _IShoukdwModel;

	public void setIShoukdwModel(IPropertySelectionModel value) {
		_IShoukdwModel = value;
	}

	public IPropertySelectionModel getIShoukdwModel() {
		if (_IShoukdwModel == null) {
			getIShoukdwModels();
		}
		return _IShoukdwModel;
	}

	public IPropertySelectionModel getIShoukdwModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select id,quanc from shoukdw order by mingc";
			_IShoukdwModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IShoukdwModel;
	}
	
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData(((Visit) getPage().getVisit()).getString10());
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			GotoFeiyxm(cycle);
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData(((Visit) getPage().getVisit()).getString10());
		}
	}

	private void GotoFeiyxm(IRequestCycle cycle) {
		// TODO 自动生成方法存根
		cycle.activate("Feiyxm");
	}
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);
	}
	
	public void Save1(String strchange, Visit visit) {

		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		String tableName1 = "feiyglb";
		String tableName2 = "feiyxmmkglb";
		String str_Feiyglb_ID = "";
		
		String[] args = visit.getString10().split(","); // 元素1：feiyxmb_id、元素2：费用类别、元素3：电厂名称、元素4：电厂id、元素5：费用名称
		String feiyxmb_id = args[0]; 	// 费用项目表_id
		String diancxxb_id = args[3];	// 电厂id
		
		ResultSetList delrsl = this.getExtGrid().getDeleteResultSet(strchange);
		while(delrsl.next()) {
			// 删除操作
			sql.append("delete from ").append(tableName1).append(" where id = ").append(delrsl.getString("ID")).append(";\n");
			sql.append("delete from ").append(tableName2).append(" where feiyglb_id = ").append(delrsl.getString("ID")).append(";\n");
			
			String sqlFencId = "select id from diancxxb where fuid = " + diancxxb_id;
			ResultSetList rslFencId = con.getResultSetList(sqlFencId);
			while (rslFencId.next()) {
				String sqlFenc = 
					"select fygl1.id\n" +
					"from feiyglb fygl1, (select * from feiyglb fygl where fygl.id = "+ delrsl.getString("ID") +") fygl2\n" + 
					"where fygl1.meikxxb_id = fygl2.meikxxb_id\n" + 
					"  and fygl1.feiylbb_id = fygl2.feiylbb_id\n" + 
					"  and nvl(fygl1.shoukdwb_id, 0) = nvl(fygl2.shoukdwb_id, 0)\n" + 
					"  and fygl1.meikyfxtgys = fygl2.meikyfxtgys\n" + 
					"  and fygl1.diancxxb_id = " + rslFencId.getString("ID");
				ResultSetList rslFenc = con.getResultSetList(sqlFenc);
				
				while (rslFenc.next()) {
					sql.append("delete from ").append(tableName1).append(" where id = ").append(rslFenc.getString("ID")).append(";\n");
					sql.append("delete from ").append(tableName2).append(" where feiyglb_id = ").append(rslFenc.getString("ID")).append(";\n");
				}
			}
		}
		
		ResultSetList mdrsl = this.getExtGrid().getModifyResultSet(strchange);
		while(mdrsl.next()) {
			
			StringBuffer sql2 = new StringBuffer(); // 保存要插入到feiyglb表的values值
			StringBuffer sql3 = new StringBuffer(); // 保存要插入到feiyxmmkglb表的字段
			StringBuffer sql4 = new StringBuffer(); // 保存要插入到feiyxmmkglb表的values值
			
			if("0".equals(mdrsl.getString("ID"))) {
				// 插入操作
				sql.append("insert into ").append(tableName1).append("(id");
				str_Feiyglb_ID = MainGlobal.getNewID(Long.parseLong(diancxxb_id));
				sql2.append(str_Feiyglb_ID);
				sql3.append("insert into ").append(tableName2).append("(id, feiyglb_id, feiyxmb_id, shifsy");
				sql4.append("getnewid(").append(diancxxb_id).append("), ").append(str_Feiyglb_ID).append(", ").append(feiyxmb_id);
				for(int i = 1; i < mdrsl.getColumnCount(); i ++) {
					if(!mdrsl.getColumnNames()[i].equals("SHIFSY")){
						if (!mdrsl.getColumnNames()[i].equals("SHOUKDWB_ID")) {
							sql.append(", ").append(mdrsl.getColumnNames()[i]);
							if(mdrsl.getColumnNames()[i].equals("FEIYLBB_ID")){
								sql2.append(", ").append(MainGlobal.getProperId(this.getIFeiylxModel(),mdrsl.getString(i)));
								continue;
							}
							if(mdrsl.getColumnNames()[i].equals("DIANCXXB_ID")){
								sql2.append(", ").append(diancxxb_id);
								continue;
							}
						} else {
							continue;
						}
					}else{
						sql4.append(", ").append(this.getExtGrid().getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));
						continue;
					}
					sql2.append(", ").append(this.getExtGrid().getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));
				}
				sql.append(") values(").append(sql2).append(");	\n");
				sql3.append(") values(").append(sql4).append(");	\n");
				sql.append(sql3).append("\n");
				
				String sqlFencId = "select id from diancxxb where fuid = " + diancxxb_id;
				ResultSetList rslFencId = con.getResultSetList(sqlFencId);
				while (rslFencId.next()) {
					
					StringBuffer sql2b = new StringBuffer(); // 保存要插入到feiyglb表的values值
					StringBuffer sql3b = new StringBuffer(); // 保存要插入到feiyxmmkglb表的字段
					StringBuffer sql4b = new StringBuffer(); // 保存要插入到feiyxmmkglb表的values值
					
					String sqlFenc = 
						"select fyxm1.id, fyxm1.diancxxb_id\n" +
						"from feiyxmb fyxm1, (select * from feiyxmb where id = "+ feiyxmb_id +") fyxm2\n" + 
						"where fyxm1.feiymcb_id = fyxm2.feiymcb_id\n" + 
						"  and fyxm1.gongs = fyxm2.gongs\n" + 
						"  and fyxm1.shuib = fyxm2.shuib\n" + 
						"  and fyxm1.feiylbb_id = fyxm2.feiylbb_id\n" + 
						"  and fyxm1.juflx = fyxm2.juflx\n" + 
						"  and fyxm1.shuil = fyxm2.shuil\n" + 
						"  and fyxm1.diancxxb_id = " + rslFencId.getString("ID");
					
					ResultSetList rslFenc = con.getResultSetList(sqlFenc);
					while (rslFenc.next()) {
						sql.append("insert into ").append(tableName1).append("(id");
						str_Feiyglb_ID = MainGlobal.getNewID(Long.parseLong(rslFenc.getString("DIANCXXB_ID")));
						sql2b.append(str_Feiyglb_ID );
						sql3b.append("insert into ").append(tableName2).append("(id, feiyglb_id, feiyxmb_id, shifsy");
						sql4b.append("getnewid(").append(rslFenc.getString("DIANCXXB_ID")).append("), ").append(str_Feiyglb_ID).append(", ").append(rslFenc.getString("ID"));
						for(int i = 1; i < mdrsl.getColumnCount(); i ++) {
							if(!mdrsl.getColumnNames()[i].equals("SHIFSY")){
								if (!mdrsl.getColumnNames()[i].equals("SHOUKDWB_ID")) {
									sql.append(", ").append(mdrsl.getColumnNames()[i]);
									if(mdrsl.getColumnNames()[i].equals("FEIYLBB_ID")){
										sql2b.append(", ").append(MainGlobal.getProperId(this.getIFeiylxModel(),mdrsl.getString(i)));
										continue;
									}
									if(mdrsl.getColumnNames()[i].equals("DIANCXXB_ID")) {
										sql2b.append(", ").append(rslFencId.getString("id"));
										continue;
									}
								} else {
									continue;
								}
							}else{
								sql4b.append(", ").append(this.getExtGrid().getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));
								continue;
							}
							sql2b.append(", ").append(this.getExtGrid().getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));
						}
						sql.append(") values(").append(sql2b).append(");	\n");
						sql3b.append(") values(").append(sql4b).append(");	\n");
						sql.append(sql3b).append("\n");
					}
				}
			}else {
				// 更新操作
				sql2.setLength(0);
				sql.append("update ").append(tableName1).append(" set ");
				for (int i = 1; i < mdrsl.getColumnCount(); i ++) {
					if (!mdrsl.getColumnNames()[i].equals("SHIFSY")) {
						if (mdrsl.getColumnNames()[i].equals("MEIKYFXTGYS")) {
							sql.append(mdrsl.getColumnNames()[i]).append(" = ");
							sql.append(this.getExtGrid().getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");
						}
						if (mdrsl.getColumnNames()[i].equals("MEIKXXB_ID")) {
							sql.append(mdrsl.getColumnNames()[i]).append(" = ");
							sql.append(MainGlobal.getProperId(this.getIMeikxxModel(), mdrsl.getString("MEIKXXB_ID"))).append(",");
						}
					} else {
						sql2.append("update ").append(tableName2).append(" set ");
						sql2.append(mdrsl.getColumnNames()[i]).append(" = ");
						sql2.append(this.getExtGrid().getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");
					}
				} 
				sql.deleteCharAt(sql.length()-1);
				sql2.deleteCharAt(sql2.length()-1);
				sql.append(" where id = ").append(mdrsl.getString("ID")).append("; \n");
				sql2.append(" where feiyglb_id = ").append(mdrsl.getString("ID")).append("; \n");
				sql.append(sql2).append("\n");
				
				String sqlFencId = "select id from diancxxb where fuid = " + diancxxb_id;
				ResultSetList rslFencId = con.getResultSetList(sqlFencId);
				
				while (rslFencId.next()) {
					String sqlFenc = 
						"select fygl1.id\n" +
						"from feiyglb fygl1, (select * from feiyglb fygl where fygl.id = "+ mdrsl.getString("ID") +") fygl2\n" + 
						"where fygl1.meikxxb_id = fygl2.meikxxb_id\n" + 
						"  and fygl1.feiylbb_id = fygl2.feiylbb_id\n" + 
						"  and nvl(fygl1.shoukdwb_id, 0) = nvl(fygl2.shoukdwb_id, 0)\n" + 
						"  and fygl1.meikyfxtgys = fygl2.meikyfxtgys\n" + 
						"  and fygl1.diancxxb_id = " + rslFencId.getString("ID");
					ResultSetList rslFenc = con.getResultSetList(sqlFenc);
					
					while (rslFenc.next()) {
						sql2.setLength(0);
						sql.append("update ").append(tableName1).append(" set ");
						for (int i = 1; i < mdrsl.getColumnCount(); i ++) {
							if (!mdrsl.getColumnNames()[i].equals("SHIFSY")) {
								if (mdrsl.getColumnNames()[i].equals("MEIKYFXTGYS")) {
									sql.append(mdrsl.getColumnNames()[i]).append(" = ");
									sql.append(this.getExtGrid().getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");
								}
								if (mdrsl.getColumnNames()[i].equals("MEIKXXB_ID")) {
									sql.append(mdrsl.getColumnNames()[i]).append(" = ");
									sql.append(MainGlobal.getProperId(this.getIMeikxxModel(), mdrsl.getString("MEIKXXB_ID"))).append(",");
								}
							} else {
								sql2.append("update ").append(tableName2).append(" set ");
								sql2.append(mdrsl.getColumnNames()[i]).append(" = ");
								sql2.append(this.getExtGrid().getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");
								continue;
							}
						} 
						sql.deleteCharAt(sql.length()-1);
						sql2.deleteCharAt(sql2.length()-1);
						sql.append(" where id = ").append(rslFenc.getString("ID")).append("; \n");
						sql2.append(" where feiyglb_id = ").append(rslFenc.getString("ID")).append("; \n");
						sql.append(sql2).append("\n");
					}
				}
			}
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
		getSelectData(visit.getString10());
	}
	
	public void getSelectData(String Param) {
	    Visit visit = (Visit) getPage().getVisit();
	    String str_feiylb="";
	    
		JDBCcon con = new JDBCcon();
		
		String[] args = Param.split(","); // 元素1：feiyxmb_id、元素2：费用类别、元素3：电厂名称、元素4：电厂id、元素5：费用名称
		String feiyxmb_id = args[0]; 	// 费用项目表_id
		String feiylb = args[1]; 		// 费用类别
		String diancmc = args[2]; 		// 电厂名称
		String feiymc = args[4]; 		// 费用名称
		
		long ID = Long.parseLong(feiyxmb_id);
		str_feiylb = feiylb;
		try{
			
			String str = 
				"select fygl.id, mkxx.mingc meikxxb_id,\n" +
				"  decode(1, 1, '"+ str_feiylb +"') feiylbb_id,\n" + 
				"  fymc.mingc shoukdwb_id,\n" + 
				"  decode(fygl.meikyfxtgys, 1, '是', 0, '否') meikyfxtgys,\n" + 
				"  decode(fyxmmkgl.shifsy, 1, '是', 0, '否') shifsy,\n" + 
				"  (select mingc from diancxxb where id = fygl.diancxxb_id) diancxxb_id\n" + 
				"from feiyxmb fyxm, feiymcb fymc, feiyxmmkglb fyxmmkgl, feiyglb fygl, meikxxb mkxx, feiylbb fylb\n" + 
				"where fyxm.feiymcb_id = fymc.id\n" + 
				"  and fyxmmkgl.feiyxmb_id = fyxm.id\n" + 
				"  and fyxmmkgl.feiyglb_id = fygl.id\n" + 
				"  and fygl.meikxxb_id = mkxx.id\n" + 
				"  and fygl.feiylbb_id = fylb.id\n" + 
				"  and fyxm.id = "+ ID +"\n" + 
				"order by fyxm.id";
			
		ResultSetList rsl = con.getResultSetList(str);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("feiyglb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("meikxxb_id").setHeader("矿别");		
		egu.getColumn("feiylbb_id").setHeader("费用类别");
		egu.getColumn("shoukdwb_id").setHeader("费用项目");
		egu.getColumn("shoukdwb_id").setDefaultValue(feiymc);
	
		egu.getColumn("meikyfxtgys").setHeader("运费与煤款供应商相同");
		egu.getColumn("shifsy").setHeader("是否使用");
		egu.getColumn("diancxxb_id").setHeader("电厂");
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("diancxxb_id").setDefaultValue(diancmc);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

		egu.getColumn("meikxxb_id").setEditor(new ComboBox());
		String str3 = "select id,mingc from meikxxb where id not in (\n" +
		"select mk.id\n" + 
		"   from feiyglb gl,meikxxb mk,feiyxmmkglb xmgl\n" + 
		"   where gl.meikxxb_id = mk.id\n" + 
		"       and xmgl.feiyglb_id = gl.id\n" + 
//		"       and xmgl.feiyxmb_id = " +((Visit) this.getPage().getVisit()).getString10().substring(0,((Visit) this.getPage().getVisit()).getString10().lastIndexOf(','))+"\n" + 
		"       and xmgl.feiyxmb_id = " + feiyxmb_id +"\n" +
		")\n" + 
		"order by mingc";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId, new IDropDownModel(str3));
		egu.getColumn("meikxxb_id").setReturnId(true);
		
		egu.getColumn("feiylbb_id").setDefaultValue(str_feiylb);
		egu.getColumn("feiylbb_id").setEditor(null);
		
//		egu.getColumn("shoukdwb_id").setDefaultValue("");
		egu.getColumn("shoukdwb_id").setEditor(null);
		egu.getColumn("shoukdwb_id").setWidth(150);
		
		List l = new ArrayList();
		l.add(new IDropDownBean(1, "是"));
		l.add(new IDropDownBean(0, "否"));
		egu.getColumn("meikyfxtgys").setEditor(new ComboBox());
		egu.getColumn("meikyfxtgys").setComboEditor(egu.gridId,
				new IDropDownModel(l));
		egu.getColumn("meikyfxtgys").setDefaultValue("是");
		egu.getColumn("meikyfxtgys").setReturnId(true);
		egu.getColumn("meikyfxtgys").setWidth(180);
		
		List shifsy = new ArrayList();
		shifsy.add(new IDropDownBean(1, "是"));
		shifsy.add(new IDropDownBean(0, "否"));
		egu.getColumn("shifsy").setEditor(new ComboBox());
		egu.getColumn("shifsy").setComboEditor(egu.gridId,
				new IDropDownModel(shifsy));
		egu.getColumn("shifsy").setDefaultValue("是");
		egu.getColumn("shifsy").setReturnId(true);
		egu.getColumn("shifsy").setWidth(60);
		
//		String s="";
//		s = "select id, mingc from diancxxb";
//		if(visit.isFencb()){
//			s="select d.id,d.mingc from diancxxb d where d.fuid="+visit.getDiancxxb_id();
//		}else{
//			s="select d.id,d.mingc from diancxxb d where d.id="+visit.getDiancxxb_id();
//		}
		
		//egu.getColumn("diancxxb_id").setWidth(85);
//		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
//		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(s));
//		egu.getColumn("diancxxb_id").returnId=true;
//		egu.getColumn("diancxxb_id").setDefaultValue(args[2]);
//		egu.getColumn("diancxxb_id").setEditor(null);
//	    if(visit.isFencb()==true){
//		egu.addTbarText("厂别:");
//		ComboBox comb4 = new ComboBox();
//		comb4.setTransform("FencbDropDown");
//		comb4.setId("changb");
//		comb4.setEditable(false);
//		comb4.setLazyRender(true);// 动态绑定
//		comb4.setWidth(100);
//		comb4.setReadOnly(true);
//		egu.addToolbarItem(comb4.getScript());
//		egu.addOtherScript("changb.on('select',function(){document.forms[0].submit();});");
//	      }

//		设置树
//		egu.addTbarText("电厂：");
//		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
//		setTree(etu);
//		egu.addTbarTreeBtn("diancTree"); 
//		egu.addTbarText("-");
		
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
	    egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
	    egu.addToolbarItem("{"
				+ new GridButton("返回",
						"function (){ document.getElementById('ReturnButton').click();}")
						.getScript() + "}");
	    
		setExtGrid(egu);
		rsl.close();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}
//	-----电厂tree
//	private String treeid;
	/*public String getTreeid() {
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}*/
//	public String getTreeid() {
//		String treeid=((Visit) getPage().getVisit()).getString2();
//		if(treeid==null||treeid.equals("")){
//			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
//		}
//		return ((Visit) getPage().getVisit()).getString2();
//	}
//	public void setTreeid(String treeid) {
//		((Visit) getPage().getVisit()).setString2(treeid);
//	}
//	public ExtTreeUtil getTree() {
//		return ((Visit) this.getPage().getVisit()).getExtTree1();
//	}
//	public void setTree(ExtTreeUtil etu) {
//		((Visit) this.getPage().getVisit()).setExtTree1(etu);
//	}
//	public String getTreeHtml() {
//		return getTree().getWindowTreeHtml(this);
//	}
//	public String getTreeScript() {
//		return getTree().getWindowTreeScript();
//	}
	
	//--------------------------------
//	厂别
	public boolean _Fencbchange = false;
	public IDropDownBean getFencbValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getFencbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setFencbValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean5()){
			
			((Visit) getPage().getVisit()).setboolean3(true);
			
		}
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public IPropertySelectionModel getFencbModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

			getFencbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setFencbModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getFencbModels() {

		
			String sql ="select id,mingc from diancxxb d where d.fuid="+
			((Visit) getPage().getVisit()).getDiancxxb_id()+"order by mingc";


			((Visit) getPage().getVisit())
			.setProSelectionModel5(new IDropDownModel(sql, "请选择"));
	return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}


	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
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
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) { 
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setFeiylxValue(null);	//费用类别DropDownBean1
			setIFeiylxModel(null);	//费用类别ProSelectionModel1
			getIFeiylxModels();
			setMeikxxValue(null);	//煤矿单位DropDownBean2
			setIMeikxxModel(null);	//煤矿单位ProSelectionModel2
//			this.setTreeid("");
			
		}
		
		getSelectData(visit.getString10());
	}
	
//	 费用类别
	
	private String getProperValue(IPropertySelectionModel _selectModel,
			long value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();
		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getId() == value) {
				return ((IDropDownBean) _selectModel.getOption(i)).getValue();
			}
		}
		return null;
	}
	public IDropDownBean getFeiylxValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
				.setDropDownBean1((IDropDownBean) getIFeiylxModels().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setFeiylxValue(IDropDownBean Value) {
		
		if(((Visit) getPage().getVisit()).getDropDownBean1()!=Value){
			
//			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean1(Value);
		}
	}

	public void setIFeiylxModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getIFeiylxModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getIFeiylxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getIFeiylxModels() {
		
		
		String sql = "select id,mingc from FEIYLBB order by mingc ";
		((Visit) getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
	private long getProperId(IPropertySelectionModel _selectModel, String value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();

		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getValue().equals(
					value)) {
				return ((IDropDownBean) _selectModel.getOption(i)).getId();
			}
		}
		return -1;
	}
	
}