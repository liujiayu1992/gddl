package com.zhiren.dtrlgs.pubclass;

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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class BalanceFmis extends BasePage {
//		绑定日期
//		private String briq;
//
//		public String getBRiq() {
//			return briq;
//		}
//
//		public void setBRiq(String briq) {
//			this.briq = briq;
//		}
		
//		绑定日期
		private String eriq;

		public String getERiq() {
			return eriq;
		}

		public void setERiq(String eriq) {
			this.eriq = eriq;
		}
		public boolean getRaw() {
			return true;
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

		private String _msg = "";

		public void setMsg(String _value) {
			 _msg = MainGlobal.getExtMessageBox(_value,false);
		}

		public String getMsg() {
			if (_msg == null) {
				_msg = "";
			}
			return _msg;
		}
		
		private void SendData(){
			FmisInterface fi=new FmisInterface() ;
			if(getLeixDropDownValue()!=null){
				setMsg(fi.JsFmis(DateUtil.getDate(getERiq()),getLeixDropDownValue().getId()));
			}
		}
		
		private boolean blnIsBegin = false;
		private String RT_FMISSJJK = "fmissjjk";
		private String mstrReportName = "fmissjjk";

		public String getPrintTable() {
			if (mstrReportName.equals(RT_FMISSJJK)) {
				return getPrintData();
			} else {
				return "无此报表";
			}
		}
		
		public String getPrintData() {
			
			JDBCcon cn = new JDBCcon();
			ResultSet rs;
			Report rt = new Report();
		    String ArrHeader[][] = new String[1][];
		    int ArrWidth[];
		    String ArrFormat[];
			String sql = "";
		    try{
		    	if(getLeixDropDownValue()!=null && getLeixDropDownValue().getId()==FmisInterface.CAIG_DATA){//矿方结算数据
		    		
		    		sql = "select * from ((select dc.mingc,js.bianm as jiesdh,to_char(js.ruzrq, 'yyyy-mm-dd') as ruzrq,js.fapbh as beiz,gy.bianm as gongysbm,\n"
						+ "        (js.hansmk - js.shuik) as meik,js.shuik as shuik,js.hansmk as jiashj,js.shoukdw, \n"
						+"		   nvl((select gongf from jieszbsjb where jiesdid=js.id and zhibb_id=1),0) as gongfsl,\n"
						+"		   nvl((select gongf from jieszbsjb where jiesdid=js.id and zhibb_id=2),0) as gongfrl,\n"
						+"		   nvl((select gongf from jieszbsjb where jiesdid=js.id and zhibb_id=3),0) as gongflf,\n"
						+ "       js.yuanshr as yuanshdw,js.xianshr as xianshdw,to_char(js.fahjzrq, 'yyyy-mm-dd') as daohrq\n"
						+ "  from kuangfjsmkb js, diancxxb dc, gongysb gy\n"
						+ " where js.diancxxb_id = dc.id and js.shoukdw = gy.quanc \n"
						+ "   and js.ruzrq >= "+DateUtil.FormatOracleDate(getERiq())+" and js.ruzrq < "+DateUtil.FormatOracleDate(getERiq())+"+1 ) \n"
						+ " union\n"
						+ " (select dc.mingc,yj.bianm as jiesdh,to_char(yj.ruzrq, 'yyyy-mm-dd') as ruzrq,yj.fapbh as beiz,gy.bianm as gongysbm,\n"
						+ "         (yj.hansyf - yj.shuik) as meik,yj.shuik as shuik,yj.hansyf as jiashj,yj.shoukdw, \n"
						+"			(select gongf from jieszbsjb where jiesdid=mk.id and zhibb_id=1) as gongfsl,\n"
						+"			(select gongf from jieszbsjb where jiesdid=mk.id and zhibb_id=2) as gongfrl,\n"
						+"			(select gongf from jieszbsjb where jiesdid=mk.id and zhibb_id=3) as gongflf,\n"
						+ "         yj.yuanshr as yuanshdw,yj.xianshr as xianshdw, to_char(yj.fahjzrq, 'yyyy-mm-dd') as daohrq \n"
						+ "    from kuangfjsyfb yj,kuangfjsmkb mk, diancxxb dc, gongysb gy \n"
						+ "   where yj.diancxxb_id = dc.id and yj.kuangfjsmkb_id=mk.id and yj.shoukdw = gy.quanc \n"
						+ "     and yj.ruzrq >= "+DateUtil.FormatOracleDate(getERiq())+" and yj.ruzrq< "+DateUtil.FormatOracleDate(getERiq())+"+1 )) \n"
						+ "   order by mingc,jiesdh";

		    		
		    		
		    		ArrHeader[0] = new String[] {"电厂名称","结算单编号","入账日期","发票编号","供应商编码","煤款","税款","价税合计","收款单位","供方数量","供方热量","供方硫分","原收货单位","现收货单位","到货日期"};
		    		ArrWidth = new int[] {85,70,70,80,70,80,80,80,180,70,70,50,110,110,70};
		    		ArrFormat = new String[]{"","","","","","0.00","0.00","0.00","","","","0.00","","",""};
		    		
		    	}else{//厂方结算数据  js.shulzjbz as buhsdj
		    		sql = "select dc.mingc,js.bianm as jiesbh,js.fapbh,to_char(js.ruzrq, 'yyyy-mm-dd') as ruzrq,gs.bianm as gongysbm,js.hansmk as fapje,js.shuik as shuije,js.buhsmk as buhsje, \n"
						+"	      decode(js.guohl, 0, 0, round((js.hansmk - js.shuik) / js.guohl, 4)) as buhsdj,js.guohl as shul,(kf.hansmk - kf.shuik) + (nvl(yf.hansyf, 0) - nvl(yf.shuik, 0)) as chengbje,\n"
						+"		  dc.quanc as shouhdw,decode(gy.bianm, null, '', 'F' || gy.bianm) as diqbm,nvl((select hetbz from jieszbsjb where jiesdid=js.id and zhibb_id=1),0) as hetsl, \n "
						+"		  nvl((select hetbz from jieszbsjb where jiesdid=js.id and zhibb_id=2),0) as hetrl,nvl((select hetbz from jieszbsjb where jiesdid=js.id and zhibb_id=3),0) as hetlf, \n "
						+"		  js.jiessl,nvl((select jies from jieszbsjb where jiesdid=js.id and zhibb_id=2),0) as jiesrz,nvl((select jies from jieszbsjb where jiesdid=js.id and zhibb_id=3),0) as jieslf \n "
						+"	 from diancjsmkb  js,kuangfjsmkb kf,kuangfjsyfb yf, diancxxb dc,diancxxb gs,gongysb gy "
						+"  where js.diancxxb_id = dc.id and kf.diancjsmkb_id(+) = js.id and js.id = yf.diancjsmkb_id(+) and js.shoukdw = gs.quanc  and js.gongysb_id = gy.id "
						+"	　and js.ruzrq>="+DateUtil.FormatOracleDate(getERiq())+" and js.ruzrq<"+DateUtil.FormatOracleDate(getERiq())+"+1 order by dc.mingc desc ";	
		    		
		    		ArrHeader[0]=new String[] {"电厂名称","结算单编号","发票编号","入账日期","供应商编码","发票金额","税金额","不含税金额","不含税<br>单价","数量","成本金额","收款单位","地区编码","合同数量","合同热值","合同硫份","结算数量","结算热值","结算硫份"};
		    		ArrWidth = new int[] {90,100,60,70,70,80,80,80,50,60,80,90,50,60,60,50,50,50,50};
		    		ArrFormat = new String[]{"","","","","","0.00","0.00","0.00","0.0000","","0.00","","","","","0.00","","","0.00"};
		    	}
			    
			    rs = cn.getResultSet(sql);
//				设置页标题
			    if(getLeixDropDownValue().getId()==FmisInterface.CAIG_DATA){
			    	rt.setTitle(getERiq()+" 采购结算数据",ArrWidth);
			    }else if (getLeixDropDownValue().getId()==FmisInterface.XIAOS_DATA){
			    	rt.setTitle(getERiq()+" 销售结算数据",ArrWidth);
			    }else{
			    	rt.setTitle(getERiq()+" 采购、销售结算数据",ArrWidth);
			    }
			    
			//数据
			    rt.setBody(new Table(rs,1,0,5));
			    rt.body.setHeaderData(ArrHeader);//表头数据
			    rt.body.setWidth(ArrWidth);
			    rt.body.setRowHeight(1, 36);
			    rt.body.setColFormat(ArrFormat);
			    rt.body.setPageRows(24);
			    if(getLeixDropDownValue()!=null && getLeixDropDownValue().getId()==FmisInterface.XIAOS_DATA){
			    	rt.body.setColAlign(12,Table.ALIGN_LEFT);
				    rt.body.setColAlign(13,Table.ALIGN_LEFT);
			    }else{
			    	rt.body.setColAlign(9,Table.ALIGN_LEFT);
				    rt.body.setColAlign(15,Table.ALIGN_LEFT);
				    rt.body.setColAlign(13,Table.ALIGN_LEFT);
				    rt.body.setColAlign(14,Table.ALIGN_LEFT);
			    }
			    rt.body.mergeFixedRow();
			    rt.body.mergeFixedCol(1);
				// 设置页数
				_CurrentPage = 1;
				_AllPages = rt.body.getPages();
				if (_AllPages == 0) {
			    	  _CurrentPage = 0;
				}
				rs.close();
				return rt.getAllPagesHtml();
				
		    }catch(Exception e){
		    	e.printStackTrace();
		    }finally{
		    	cn.Close();
		    }
		    return "";
	  }
		
		private boolean _FmisClick = false;

		public void FmisButton(IRequestCycle cycle) {
			_FmisClick = true;
		}
		
		private boolean _QueryClick = false;
		public void QueryButton(IRequestCycle cycle) {
			_QueryClick = true;
		}
		public void submit(IRequestCycle cycle) {
			if (_QueryClick) {
				_QueryClick = false;
				setMsg("");
			}
			if (_FmisClick) {
				_FmisClick = false;
				SendData();
			}
		}

		private void getToolbars(){
			Toolbar tb1 = new Toolbar("tbdiv");
			
			//日期
			tb1.addText(new ToolbarText("日期:"));
			DateField dfe = new DateField();
			dfe.setValue(getERiq());
			dfe.Binding("ERIQ", "");// 与html页中的id绑定,并自动刷新
			dfe.setId("rqe");
			tb1.addField(dfe);

			tb1.addText(new ToolbarText("-"));
			
			tb1.addText(new ToolbarText("结算类型:"));
			ComboBox cb2 = new ComboBox();
			cb2.setTransform("LeixDropDown");
			cb2.setEditable(true);
			cb2.setWidth(100);
			tb1.addField(cb2);
			
			tb1.addText(new ToolbarText("-"));
			
			ToolbarButton tb = new ToolbarButton(null,"查询","function(){document.Form0.submit();}");
			tb.setIcon(SysConstant.Btn_Icon_Print);
			tb1.addItem(tb);
			
			tb1.addText(new ToolbarText("-"));
			
			String scr=
				"function(){document.getElementById('FmisButton').click();\n" +
				"Ext.MessageBox.show(\n" + 
				"    {msg:'正在处理数据,请稍后...',\n" + 
				"     progressText:'处理中...',\n" + 
				"     width:300,\n" + 
				"     wait:true,\n" + 
				"     waitConfig: {interval:200},\n" + 
				"     icon:Ext.MessageBox.INFO\n" + 
				"     }\n" + 
				");" +
//				"Ext.MessageBox.alert('提示信息','成功传入FMIS接口');"+
				"}";
			ToolbarButton tbb2 = new ToolbarButton(null,"传FMIS接口",scr);
			tb1.addItem(tbb2);
			setToolbar(tb1);
		}
		
		public Toolbar getToolbar() {
			return ((Visit)this.getPage().getVisit()).getToolbar();
		}
		public void setToolbar(Toolbar tb1) {
			((Visit)this.getPage().getVisit()).setToolbar(tb1);
		}
		public String getToolbarScript() {
			return getToolbar().getRenderScript();
		}
		
	    // 结算方
	    public IDropDownBean getLeixDropDownValue() {
	    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
	   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getLeixDropDownModel().getOption(0));
	    	}
	       return  ((Visit) getPage().getVisit()).getDropDownBean4();
	    }
	    public void setLeixDropDownValue(IDropDownBean Value) {
		    	((Visit) getPage().getVisit()).setDropDownBean4(Value);
	    }
	    public void setLeixDropDownModel(IPropertySelectionModel value) {
	    	((Visit) getPage().getVisit()).setProSelectionModel4(value);
	    }
	    public IPropertySelectionModel getLeixDropDownModel() {
	        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
	            getLeixDropDownModels();
	        }
	        return ((Visit) getPage().getVisit()).getProSelectionModel4();
	    }
	    public void getLeixDropDownModels() {
	    	StringBuffer sql=new StringBuffer();
	    	sql.append("select "+FmisInterface.CAIG_DATA+" id,'采购' mingc from dual union\n");
	    	sql.append("select "+FmisInterface.XIAOS_DATA+" id,'销售' mingc from dual \n");
//	    	sql.append(" union select 3 id,'全部' mingc from dual ");
	        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(sql.toString()));
	        return ;
	    }
	    
	    private String[] getLeix(){
	    	String[] a=new String[3];
	    	if(getLeixDropDownValue().getId()==FmisInterface.CAIG_DATA){
	    		a[0]="kuangfjsmkb";
	    		a[1]="kuangfjsyfb";
	    		a[2]="采购";//矿方
	    	}else if(getLeixDropDownValue().getId()==FmisInterface.XIAOS_DATA){
	    		a[0]="diancjsmkb";
	    		a[1]="diancjsyfb";
	    		a[2]="销售";//厂方
	    	}
	    	return a;
	    }
	    
	    public String getTianzdw(long diancxxb_id) {//填制单位
			String Tianzdw="";
			JDBCcon con=new JDBCcon();
			try{
				String sql="select quanc from diancxxb where id="+diancxxb_id;
				ResultSet rs=con.getResultSet(sql);
				if(rs.next()){
					
					Tianzdw=rs.getString("quanc");
				}
				rs.close();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				con.Close();
			}
			return Tianzdw;
		}
		
		
		public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
			Visit visit = (Visit) getPage().getVisit();
			visit.setboolean1(false);
			if (visit.getRenyID() == -1) {
				 visit.setboolean1(true);
				 return;
			}
			
			if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
				visit.setActivePageName(getPageName().toString());
				String rq=DateUtil.FormatDate(new Date());
//				this.setBRiq(rq);
				this.setERiq(rq);
				visit.setList1(null);
				visit.setDropDownBean4(null);
				visit.setProSelectionModel4(null);
				
			}
			if(getERiq()==null){
				String rq=DateUtil.FormatDate(new Date());
				this.setERiq(rq);
			}
			getToolbars();
//			if(cycle.getRequestContext().getParameter("lx")!=null){
//				mstrReportName = cycle.getRequestContext().getParameter("lx");
//			}
			blnIsBegin = true;
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
		
		public String getPageHome() {
			if (((Visit) getPage().getVisit()).getboolean1()) {
				return "window.location = '" + MainGlobal.getHomeContext(this)
						+ "';";
			} else {
				return "";
			}
		}
}
