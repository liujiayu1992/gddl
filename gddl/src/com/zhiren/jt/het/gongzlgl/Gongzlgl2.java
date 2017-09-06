package com.zhiren.jt.het.gongzlgl;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.util.*;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.JDBCcon;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;

public class Gongzlgl2 extends BasePage {
//点击行索引
    private int _editTableRow = -1;// 编辑框中选中的行

    public int getEditTableRow() {
        return _editTableRow;
    }

    public void setEditTableRow(int _value) {
        _editTableRow = _value;
    }
	 public String getpageLink() {
	        return " var context='"
	                + MainGlobal.getHomeContext(this) + "';";
	    }
	 // ///////////////////
	    public void setTabbarSelect(int value) {
	        ((Visit) getPage().getVisit()).setInt1(value);
	    }

	    public int getTabbarSelect() {
	        return ((Visit) getPage().getVisit()).getInt1();
	    }
    private String meg;// msg

    public String getMeg() {
        return meg;
    }

    protected void initialize() {
        meg = "";
    }

    public void setMeg(String meg) {
        this.meg = meg;
    }

    public void submit(IRequestCycle cycle) {
    	//流程类别
    	if (_SaveChickLeib) {
            _SaveChickLeib = false;
            SaveLeib();
            getSelectDataLeib();
        }
        //流程命称表
        if (_SaveChickLiuc) {
            _SaveChickLiuc = false;
            SaveLiuc();
            getSelectDataLiuc();
        }
        //类别状态
        if (_SaveChickLeibzt) {
        	_SaveChickLeibzt = false;
            SaveLeibzt();
            getSelectDataLeibzt();
        }
        //状态与流程
        if (_SaveChickLiuczt) {
        	_SaveChickLiuczt = false;
        	SaveLiuczt();
            getSelectDataLiuczt();
        }
        // 流程角色
        if (_SaveChickLiucjs) {
        	_SaveChickLiucjs = false;
        	SaveLiucjs();
        	getSelectDataLiucjs();
        }

        //流程动作
        if (_SaveChickLiucdz) {
        	_SaveChickLiucdz = false;
        	SaveLiucdz();
        	getSelectDataLiucdz();
        }
       
    }



    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
        Visit visit = (Visit) getPage().getVisit();

        if (!visit.getActivePageName().toString().equals(
                this.getPageName().toString())) {
            // 在此添加，在页面第一次加载时需要置为空的变量或方法
            visit.setActivePageName(getPageName().toString());
            setTabbarSelect(0);
            getSelectDataLeib();
            getSelectDataLiucjs();
            getSelectDataLiuc();

			setLeibSelectValue(null);
			getLeibSelectModels();

			setLiucSelectValue(null);
			getLiucSelectModels();

		    setLiucdzSelectValue(null);
			getLiucdzSelectModels();
        }
        getSelectDataLeibzt();
        getSelectDataLiuczt();
        getSelectDataLiucdz();
    }

//***********************类别************
    public void getSelectDataLeib() {
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList("select id,mingc from liuclbb");
		ExtGridUtil egu = new ExtGridUtil("gridDiv_leib", rsl);
		// //设置表名称用于保存
		egu.setHeight(490);
		egu.frame = false;
		egu.setTableName("liuclbb");
		// /设置显示列名称
		egu.getColumn("id").setHeader("id");
		egu.getColumn("mingc").setHeader("名称");
//		// //设置列宽度
		egu.getColumn("mingc").setWidth(70);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// //设置分页行数（缺省25行可不设）
		egu.addPaging(25);
		// /设置按钮
		egu.addToolbarButton(GridButton.ButtonType_Insert,"InsertButtonLeib");
		egu.addToolbarButton(GridButton.ButtonType_Delete,"DeleteButtonLeib");
		egu.addToolbarButton(GridButton.ButtonType_Save,"SaveButtonLeib");
		setExtGrid_Leib(egu);
		con.Close();
	}
	//类别
	public ExtGridUtil getExtGrid_Leib() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid_Leib(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript_Leib() {
		return getExtGrid_Leib().getGridScript();
	}

	public String getGridHtml_Leib() {
		return getExtGrid_Leib().getHtml();
	}
	
	private void SaveLeib() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _SaveChickLeib = false;

	public void SaveButtonLeib(IRequestCycle cycle) {
		_SaveChickLeib = true;
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
	//
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

//	***********************流程************
	 public void getSelectDataLiuc() {
			JDBCcon con = new JDBCcon();
			ResultSetList rsll = con
					.getResultSetList("select liucb.id,liucb.mingc,liuclbb.mingc as liuclbb_id " +
							"from liucb,liuclbb " +
							"where liucb.liuclbb_id=liuclbb.id");
			ExtGridUtil egul = new ExtGridUtil("gridDiv_liuc", rsll);
			// //设置表名称用于保存
			egul.setHeight(460);
			egul.frame = false;
			egul.setTableName("liucb");
			// /设置显示列名称
			egul.getColumn("id").setHeader("id");
			egul.getColumn("mingc").setHeader("名称");
			egul.getColumn("liuclbb_id").setHeader("类别");
			egul.getColumn("liuclbb_id").setEditor(new ComboBox());
			egul.getColumn("liuclbb_id").setComboEditor(egul.gridId,
					new IDropDownModel("select id, mingc from liuclbb"));
			egul.setGridType(ExtGridUtil.Gridstyle_Edit);
			// //设置分页行数（缺省25行可不设）
			egul.addPaging(25);

			// /设置按钮
			egul.addToolbarButton(GridButton.ButtonType_Insert, "InsertButtonLiuc");
			egul.addToolbarButton(GridButton.ButtonType_Delete, "DeleteButtonLiuc");
			egul.addToolbarButton(GridButton.ButtonType_Save,"SaveButtonliuc");
			setExtGrid_Liuc(egul);
			con.Close();
		}
	 
		//流程
		public ExtGridUtil getExtGrid_Liuc() {
			return ((Visit) this.getPage().getVisit()).getExtGrid2();
		}

		public void setExtGrid_Liuc(ExtGridUtil extgrid) {
			((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
		}

		public String getGridScript_Liuc() {
			return getExtGrid_Liuc().getGridScript();
		}

		public String getGridHtml_Liuc() {
			return getExtGrid_Liuc().getHtml();
		}
		private void SaveLiuc() {
			Visit visit = (Visit) this.getPage().getVisit();
			visit.getExtGrid2().Save(getChange(), visit);
		}

		private boolean _SaveChickLiuc = false;

		public void SaveButtonLiuc(IRequestCycle cycle) {
			_SaveChickLiuc = true;
		}
//  *************************************   
//  ************************类别状态***********
	    public void getSelectDataLeibzt() {
			JDBCcon con = new JDBCcon();
			ResultSetList rsl = con
					.getResultSetList("select id,mingc,liuclbb_id from leibztb where liuclbb_id="+getLeibSelectValue().getId());
			ExtGridUtil eguzt = new ExtGridUtil("gridDiv_leibzt", rsl);
			// //设置表名称用于保存
			eguzt.setHeight(460);
			eguzt.frame = false;
			eguzt.setTableName("leibztb");
			// /设置显示列名称
			eguzt.getColumn("id").setHeader("id");
			eguzt.getColumn("mingc").setHeader("名称");
			eguzt.getColumn("liuclbb_id").setHeader("liuclbb_id");
			eguzt.getColumn("liuclbb_id").setHidden(true);
			eguzt.getColumn("liuclbb_id").setDefaultValue(getLeibSelectValue().getId()+"");
//			// //设置列宽度
			eguzt.getColumn("mingc").setWidth(100);
			eguzt.setGridType(ExtGridUtil.Gridstyle_Edit);
			// //设置分页行数（缺省25行可不设）
			eguzt.addPaging(25);
			//------------工具-------------------

			eguzt.addTbarText("类别:");
			ComboBox comb3 = new ComboBox();
			comb3.setTransform("LeibSelect");
			comb3.setId("leibid");
			comb3.setEditable(true);
			comb3.setLazyRender(true);// 动态绑定
			comb3.setWidth(130);
			eguzt.addToolbarItem(comb3.getScript());
			eguzt.addOtherScript("leibid.on('select',function(){document.forms[0].submit();});");
			// /设置按钮
			eguzt.addToolbarButton(GridButton.ButtonType_Insert,"InsertButtonLeibzt");
			eguzt.addToolbarButton(GridButton.ButtonType_Delete,"DeleteButtonLeibzt");
			eguzt.addToolbarButton(GridButton.ButtonType_Save,"SaveButtonLeibzt");
			setExtGrid_Leibzt(eguzt);
			con.Close();
		}
		//类别状态
		public ExtGridUtil getExtGrid_Leibzt() {
			return ((Visit) this.getPage().getVisit()).getExtGrid3();
		}

		public void setExtGrid_Leibzt(ExtGridUtil extgrid) {
			((Visit) this.getPage().getVisit()).setExtGrid3(extgrid);
		}

		public String getGridScript_Leibzt() {
			return getExtGrid_Leibzt().getGridScript();
		}

		public String getGridHtml_Leibzt() {
			return getExtGrid_Leibzt().getHtml();
		}
		
		private void SaveLeibzt() {
			Visit visit = (Visit) this.getPage().getVisit();
			visit.getExtGrid3().Save(getChange(), visit);
		}

		private boolean _SaveChickLeibzt = false;

		public void SaveButtonLeibzt(IRequestCycle cycle) {
			_SaveChickLeibzt = true;
		}
//-------------------类别下拉框---------
		public boolean _leibSelectchange = false;

		private IDropDownBean _LeibSelectValue;
		
		public IDropDownBean getLeibSelectValue() {
			if (_LeibSelectValue == null) {
				_LeibSelectValue = (IDropDownBean) getLeibSelectModel().getOption(0);
			}
			return _LeibSelectValue;
		}

		public void setLeibSelectValue(IDropDownBean Value) {
			long id = -2;
			if (_LeibSelectValue != null) {
				id = _LeibSelectValue.getId();
			}
			if (Value != null) {
				if (Value.getId() != id) {
					_leibSelectchange = true;
				} else {
					_leibSelectchange = false;
				}
			}
			_LeibSelectValue = Value;
		}

		private IPropertySelectionModel _LeibSelectModel;

		public void setLeibSelectModel(IPropertySelectionModel value) {
			_LeibSelectModel = value;
		}

		public IPropertySelectionModel getLeibSelectModel() {
			if (_LeibSelectModel == null) {
				getLeibSelectModels();
			}
			return _LeibSelectModel;
		}

		public IPropertySelectionModel getLeibSelectModels() {
			JDBCcon con = new JDBCcon();
			try {

				String 	sql = "select id,mingc from liuclbb";
				_LeibSelectModel = new IDropDownModel(sql);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				con.Close();
			}
			return _LeibSelectModel;
		}
//	  *************************************   
//	  ************************流程状态***********
	    public void getSelectDataLiuczt() {
			JDBCcon con = new JDBCcon();
			ResultSetList rsl = con
					.getResultSetList("select liucztb.id,leibztb.mingc as leibztb_id,liucb_id,xuh\n" +
								"from liucztb,leibztb\n" + 
								"where liucztb.leibztb_id =leibztb.id and liucztb.liucb_id=" +getLiucSelectValue().getId()+ 
								" order by xuh"
							);
			ExtGridUtil egulczt = new ExtGridUtil("gridDiv_liuczt", rsl);
			// //设置表名称用于保存
			egulczt.setHeight(465);
			egulczt.frame = false;
			egulczt.setTableName("liucztb");
			// /设置显示列名称
			egulczt.getColumn("id").setHeader("id");
			egulczt.getColumn("leibztb_id").setHeader("类别状态");
			egulczt.getColumn("xuh").setHeader("序号");
			egulczt.getColumn("leibztb_id").setEditor(new ComboBox());
			egulczt.getColumn("leibztb_id").setComboEditor(egulczt.gridId,
					new IDropDownModel("select id, mingc from leibztb"));
			egulczt.getColumn("liucb_id").setHeader("liucb_id");
			egulczt.getColumn("liucb_id").setHidden(true);
			egulczt.getColumn("liucb_id").setDefaultValue(getLiucSelectValue().getId()+"");
//			// //设置列宽度
			egulczt.setGridType(ExtGridUtil.Gridstyle_Edit);
			// //设置分页行数（缺省25行可不设）
			egulczt.addPaging(25);
			//------------工具-------------------
			egulczt.addTbarText("流程:");
			ComboBox comb3 = new ComboBox();
			comb3.setTransform("LiucSelect");
			comb3.setId("liucid");
			comb3.setEditable(true);
			comb3.setLazyRender(true);// 动态绑定
			comb3.setWidth(130);
			egulczt.addToolbarItem(comb3.getScript());
			egulczt.addOtherScript("liucid.on('select',function(){document.forms[0].submit();});");
			// /设置按钮
			egulczt.addToolbarButton(GridButton.ButtonType_Insert,"InsertButtonLiuczt");
			egulczt.addToolbarButton(GridButton.ButtonType_Delete,"DeleteButtonLiuczt");
			egulczt.addToolbarButton(GridButton.ButtonType_Save,"SaveButtonLiuczt");
			setExtGrid_Liuczt(egulczt);
			con.Close();
		}
		//流程状态
		public ExtGridUtil getExtGrid_Liuczt() {
			return ((Visit) this.getPage().getVisit()).getExtGrid4();
		}

		public void setExtGrid_Liuczt(ExtGridUtil extgrid) {
			((Visit) this.getPage().getVisit()).setExtGrid4(extgrid);
		}

		public String getGridScript_Liuczt() {
			return getExtGrid_Liuczt().getGridScript();
		}

		public String getGridHtml_Liuczt() {
			return getExtGrid_Liuczt().getHtml();
		}
		
		private void SaveLiuczt() {
			Visit visit = (Visit) this.getPage().getVisit();
			visit.getExtGrid4().Save(getChange(), visit);
		}

		private boolean _SaveChickLiuczt = false;

		public void SaveButtonLiuczt(IRequestCycle cycle) {
			_SaveChickLiuczt = true;
		}
//-------------------流程下拉框---------
		public boolean _liucSelectchange = false;

		private IDropDownBean _LiucSelectValue;
		
		public IDropDownBean getLiucSelectValue() {
			if (_LiucSelectValue == null) {
				_LiucSelectValue = (IDropDownBean) getLiucSelectModels().getOption(0);
			}
			return _LiucSelectValue;
		}

		public void setLiucSelectValue(IDropDownBean Value) {
			long id = -2;
			if (_LiucSelectValue != null) {
				id = _LiucSelectValue.getId();
			}
			if (Value != null) {
				if (Value.getId() != id) {
					_liucSelectchange = true;
				} else {
					_liucSelectchange = false;
				}
			}
			_LiucSelectValue = Value;
		}

		private IPropertySelectionModel _LiucSelectModel;

		public void setLiucSelectModel(IPropertySelectionModel value) {
			_LiucSelectModel = value;
		}

		public IPropertySelectionModel getLiucSelectModel() {
			if (_LiucSelectModel == null) {
				getLiucSelectModels();
			}
			return _LiucSelectModel;
		}

		public IPropertySelectionModel getLiucSelectModels() {
			JDBCcon con = new JDBCcon();
			try {

				String 	sql = "select id,mingc from liucb";
				_LiucSelectModel = new IDropDownModel(sql);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				con.Close();
			}
			return _LiucSelectModel;
		}
//	  ************************************* 
//		***********************流程角色************
	    public void getSelectDataLiucjs() {
			JDBCcon con = new JDBCcon();
			ResultSetList rsl = con
					.getResultSetList("select id,mingc,beiz from liucjsb");
			ExtGridUtil egulcjs = new ExtGridUtil("gridDiv_liucjs", rsl);
			// //设置表名称用于保存
			egulcjs.setHeight(460);
			egulcjs.frame = false;
			egulcjs.setTableName("liucjsb");
			// /设置显示列名称
			egulcjs.getColumn("id").setHeader("id");
			egulcjs.getColumn("mingc").setHeader("名称");
			egulcjs.getColumn("beiz").setHeader("备注");
//			// //设置列宽度
			egulcjs.getColumn("mingc").setWidth(70);
			egulcjs.setGridType(ExtGridUtil.Gridstyle_Edit);
			// //设置分页行数（缺省25行可不设）
			egulcjs.addPaging(25);
			// /设置按钮
			egulcjs.addToolbarButton(GridButton.ButtonType_Insert,"InsertButtonLiucjs");
			egulcjs.addToolbarButton(GridButton.ButtonType_Delete,"DeleteButtonLiucjs");
			egulcjs.addToolbarButton(GridButton.ButtonType_Save,"SaveButtonLiucjs");
			setExtGrid_Liucjs(egulcjs);
			con.Close();
		}
		//角色
		public ExtGridUtil getExtGrid_Liucjs() {
			return ((Visit) this.getPage().getVisit()).getExtGrid5();
		}

		public void setExtGrid_Liucjs(ExtGridUtil extgrid) {
			((Visit) this.getPage().getVisit()).setExtGrid5(extgrid);
		}

		public String getGridScript_Liucjs() {
			return getExtGrid_Liucjs().getGridScript();
		}

		public String getGridHtml_Liucjs() {
			return getExtGrid_Liucjs().getHtml();
		}
		
		private void SaveLiucjs() {
			Visit visit = (Visit) this.getPage().getVisit();
			visit.getExtGrid5().Save(getChange(), visit);
		}

		private boolean _SaveChickLiucjs = false;

		public void SaveButtonLiucjs(IRequestCycle cycle) {
			_SaveChickLiucjs = true;
		}
//**************************************************************
//		  ************************流程动作***********
	    public void getSelectDataLiucdz() {
			JDBCcon con = new JDBCcon();
			ResultSetList rsl = con
					.getResultSetList(
							"select lcdz.id as id ,lcdzjs.id as liucdzjsb_id,lcjs.mingc as mingc,leibztb1.mingc as liucztqqid,leibztb2.mingc as liuczthjid,lcdz.mingc as caozmc\n" +
							"         from liucdzb lcdz,liucdzjsb lcdzjs,liucjsb lcjs,\n" + 
							"         liucztb liucztb1,liucztb liucztb2,leibztb leibztb1,leibztb leibztb2\n" + 
							"         where lcdzjs.liucdzb_id=lcdz.id and lcdzjs.liucjsb_id=lcjs.id\n" + 
							"         and liucztb1.liucb_id="+getLiucdzSelectValue().getId()+"\n" + 
							"         and liucztb1.id=lcdz.liucztqqid\n" + 
							"		  and liucztb2.id=lcdz.liuczthjid and leibztb1.id=liucztb1.leibztb_id and leibztb2.id=liucztb2.leibztb_id\n" + 
							"         order by  lcdz.id"
							);
			ExtGridUtil egulcdz = new ExtGridUtil("gridDiv_liucdz", rsl);
			// //设置表名称用于保存
			egulcdz.setHeight(460);
			egulcdz.frame = false;
			egulcdz.setTableName("liucdzb");
			// /设置显示列名称
			egulcdz.getColumn("id").setHeader("id");
			egulcdz.getColumn("liucdzjsb_id").setHeader("liucdzjsb_id");
			egulcdz.getColumn("liucdzjsb_id").setHidden(true);
			egulcdz.getColumn("liucdzjsb_id").setEditor(null);
			egulcdz.getColumn("mingc").setHeader("角色");
			egulcdz.getColumn("mingc").setEditor(new ComboBox());
			egulcdz.getColumn("mingc").setComboEditor(egulcdz.gridId,
					new IDropDownModel("select id, mingc from liucjsb"));
			egulcdz.getColumn("liucztqqid").setHeader("前驱状态");
			egulcdz.getColumn("liucztqqid").setEditor(new ComboBox());
			egulcdz.getColumn("liucztqqid").setComboEditor(egulcdz.gridId, 
					new IDropDownModel(
							"select liucztb.id,leibztb.mingc\n" +
				        	"from liucztb,leibztb\n" + 
				        	" where liucztb.leibztb_id=leibztb.id and liucztb.liucb_id="+
				        	getLiucdzSelectValue().getId()));
			egulcdz.getColumn("liuczthjid").setHeader("后继状态");
			egulcdz.getColumn("liuczthjid").setEditor(new ComboBox());
			egulcdz.getColumn("liuczthjid").setComboEditor(egulcdz.gridId, 
					new IDropDownModel("select liucztb.id,leibztb.mingc\n" +
				        	"from liucztb,leibztb\n" + 
				        	" where liucztb.leibztb_id=leibztb.id and liucztb.liucb_id="+
				        	getLiucdzSelectValue().getId()));
			egulcdz.getColumn("caozmc").setHeader("操作名");
//			// //设置列宽度
			egulcdz.setGridType(ExtGridUtil.Gridstyle_Edit);
			// //设置分页行数（缺省25行可不设）
			egulcdz.addPaging(25);
			//------------工具-------------------
			egulcdz.addTbarText("流程:");
			ComboBox comb3 = new ComboBox();
			comb3.setTransform("LiucdzSelect");
			comb3.setId("liucid");
			comb3.setEditable(true);
			comb3.setLazyRender(true);// 动态绑定
			comb3.setWidth(130);
			egulcdz.addToolbarItem(comb3.getScript());
			egulcdz.addOtherScript("liucid.on('select',function(){document.forms[0].submit();});");
			// /设置按钮
			egulcdz.addToolbarButton(GridButton.ButtonType_Insert,"InsertButtonLiucdz");
			egulcdz.addToolbarButton(GridButton.ButtonType_Delete,"DeleteButtonLiucdz");
			egulcdz.addToolbarButton(GridButton.ButtonType_Save,"SaveButtonLiucdz");
			setExtGrid_Liucdz(egulcdz);
			con.Close();
		}
		//流程动作
		public ExtGridUtil getExtGrid_Liucdz() {
			return ((Visit) this.getPage().getVisit()).getExtGrid6();
		}

		public void setExtGrid_Liucdz(ExtGridUtil extgrid) {
			((Visit) this.getPage().getVisit()).setExtGrid6(extgrid);
		}

		public String getGridScript_Liucdz() {
			return getExtGrid_Liucdz().getGridScript();
		}

		public String getGridHtml_Liucdz() {
			return getExtGrid_Liucdz().getHtml();
		}
		
		private void SaveLiucdz() {
			JDBCcon con = new JDBCcon();
			Visit visit = (Visit) this.getPage().getVisit();
			ResultSetList drsl = visit.getExtGrid6().getDeleteResultSet(getChange());
			StringBuffer sql = new StringBuffer("begin \n");
			while (drsl.next()) {
				sql.append("delete from ").append("liucdzb").append(" where id =")
				.append(drsl.getLong("id")).append(";\n");
				sql.append("delete from ").append("liucdzjsb").append(" where id =")
				.append(drsl.getLong("liucdzjsb_id")).append(";\n");
			}
			
			ResultSetList rsl = visit.getExtGrid6().getModifyResultSet(getChange());
		
			while (rsl.next()) {
				long liucdzb_id=0;
				long liucdzjsb_id= 0;
				
				if ("0".equals(rsl.getString("ID"))) {
					liucdzb_id =  Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
					  sql.append("insert into liucdzb(id,liucztqqid,liuczthjid,mingc)values("
					  			+ liucdzb_id
					  			+",(select id from (select liucztb.id,leibztb.mingc as mingc from liucztb,leibztb "
					  			+" where liucztb.leibztb_id=leibztb.id and liucztb.liucb_id="+getLiucdzSelectValue().getId()
					  			+")b where b.mingc='"+rsl.getString("liucztqqid")
					  			+"'),(select id from (select liucztb.id,leibztb.mingc as mingc from liucztb,leibztb"
					  			+" where liucztb.leibztb_id=leibztb.id and liucztb.liucb_id="+getLiucdzSelectValue().getId()
					  			+")b where b.mingc='"+rsl.getString("liuczthjid")+"'),'"+rsl.getString("caozmc")
					  			+"');\n");
					  
		             
		            liucdzjsb_id =  Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
		                sql.append( 
		                	"insert into liucdzjsb(id,liucdzb_id,liucjsb_id)values(" 
		                	+ liucdzjsb_id
		                	+","
		                	+liucdzb_id
		                	+",(select id from liucjsb where mingc='"
		                	+rsl.getString("mingc")
		                	+"'));");
				}else {
				  sql.append("update liucdzb set liucztqqid=(select id from (select liucztb.id,leibztb.mingc as mingc from liucztb,leibztb "
					  			+" where liucztb.leibztb_id=leibztb.id and liucztb.liucb_id="+getLiucdzSelectValue().getId()
					  			+")b where b.mingc='" + rsl.getString("liucztqqid")
					  			+"'),liuczthjid=(select id from (select liucztb.id,leibztb.mingc as mingc from liucztb,leibztb"
					  			+" where liucztb.leibztb_id=leibztb.id and liucztb.liucb_id="+getLiucdzSelectValue().getId()
					  			+")b where b.mingc='"+rsl.getString("liuczthjid")
                +"'),mingc='"+rsl.getString("caozmc")
                + "' where id=" + rsl.getLong("id")+";\n");
                                
                sql.append( "update liucdzjsb set liucjsb_id=(select id from liucjsb where mingc='" + rsl.getString("mingc") 
                + "') where id=" + rsl.getLong("liucdzjsb_id")+";\n");
            
				}
			}
			sql.append("end;");
			con.getUpdate(sql.toString());
			
			
			
		}
		private boolean _SaveChickLiucdz = false;

		public void SaveButtonLiucdz(IRequestCycle cycle) {
			_SaveChickLiucdz = true;
		}
		  

//		-------------------流程下拉框---------
		public boolean _liucdzSelectchange = false;

		private IDropDownBean _LiucdzSelectValue;
		
		public IDropDownBean getLiucdzSelectValue() {
			if (_LiucdzSelectValue == null) {
				_LiucdzSelectValue = (IDropDownBean) getLiucdzSelectModels().getOption(0);
			}
			return _LiucdzSelectValue;
		}

		public void setLiucdzSelectValue(IDropDownBean Value) {
			long id = -2;
			if (_LiucdzSelectValue != null) {
				id = _LiucdzSelectValue.getId();
			}
			if (Value != null) {
				if (Value.getId() != id) {
					_liucdzSelectchange = true;
				} else {
					_liucdzSelectchange = false;
				}
			}
			_LiucdzSelectValue = Value;
		}

		private IPropertySelectionModel _LiucdzSelectModel;

		public void setLiucdzSelectModel(IPropertySelectionModel value) {
			_LiucdzSelectModel = value;
		}

		public IPropertySelectionModel getLiucdzSelectModel() {
			if (_LiucdzSelectModel == null) {
				getLiucdzSelectModels();
			}
			return _LiucdzSelectModel;
		}

		public IPropertySelectionModel getLiucdzSelectModels() {
			JDBCcon con = new JDBCcon();
			try {

				String 	sql = "select id,mingc from liucb";
				_LiucdzSelectModel = new IDropDownModel(sql);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				con.Close();
			}
			return _LiucdzSelectModel;
		}
}
