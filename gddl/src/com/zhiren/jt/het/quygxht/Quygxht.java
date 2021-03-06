package com.zhiren.jt.het.quygxht;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.zhiren.jt.het.hetmb.Fahxxbean;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/** 
 * @author caolin2
 *
 */
public class Quygxht extends BasePage implements PageValidateListener{
	private String getDanwglStr(){;//指标单位关联数组
	return ((Visit) getPage().getVisit()).getString3();
	}
	private void setDanwglStr(String value){;//指标单位关联数组
		 ((Visit) getPage().getVisit()).setString3(value);
	}
	public String getpageLink() {
		return " var context='"
		+ MainGlobal.getHomeContext(this)+ "';"+getDanwglStr();
	}
	public void setTabbarSelect(int value) {
		((Visit) getPage().getVisit()).setInt1(value);
	}
	public int getTabbarSelect() {
		return ((Visit) getPage().getVisit()).getInt1();
	}
	public boolean isXinjht(){
		return ((Visit) getPage().getVisit()).getboolean5();
	}
	public void setXinjht(boolean value){
		((Visit) getPage().getVisit()).setboolean5(value);
	}
	
	//功能区 新建模板  打开  保存  选择流程	模板名称
	//模板名称下拉框
    public IDropDownBean getmobmcSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean1()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean1((IDropDownBean)getmobmcSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean1();
    }
 
    public void setmobmcSelectValue(IDropDownBean Value) {
    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean1()!=null){
	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean1().getId()){
	    		((Visit) getPage().getVisit()).setboolean6(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean6(false);
	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean1(Value);
    	}
    }
    public void setmobmcSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel1(value);
    }

    public IPropertySelectionModel getmobmcSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
            getmobmcSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel1();
    }

    public void getmobmcSelectModels() {
    	  String sql = 
          	"select id,mingc " +
          	"from hetb_mb " +
          	"where diancxxb_id in(" +
          	"select id from diancxxb\n" +
          	"start with id=" + ((Visit) getPage().getVisit()).getDiancxxb_id()+
          	"connect by prior fuid= id)";
        ((Visit) getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql,"")) ;
        return ;
    }
    //合同
    public IDropDownBean gethetSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
    		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)gethetSelectModel().getOption(0));
    	}
        return  ((Visit) getPage().getVisit()).getDropDownBean2();
    }

    public void sethetSelectValue(IDropDownBean Value) {
    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean2()!=null){
	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean2().getId()){
	    		((Visit) getPage().getVisit()).setboolean1(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean1(false);
	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean2(Value);
    	}
    }
    public void sethetSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public IPropertySelectionModel gethetSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
            gethetSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }

    public void gethetSelectModels() {
        String sql = 
        	"select id,hetb.hetbh\n" +
        	"from hetb where leib=1 and fuid=0 and liucztb_id=0  and to_char(hetb.qiandrq,'YYYY')="+getNianfValue().getId()+" and (diancxxb_id="+((Visit) getPage().getVisit()).getDiancxxb_id()+" or fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+")";
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"新建合同")) ;
        return ;
    }
    //新模板名称
    public String getmobmc(){
    	return ((Visit) getPage().getVisit()).getString1();
    }
    public void setmobmc(String value){
    	((Visit) getPage().getVisit()).setString1(value);
    }
    //合同信息
    public Hetxxbean gethetxxbean(){
    	if(((Visit) getPage().getVisit()).getObject1()==null){
    		((Visit) getPage().getVisit()).setObject1(new Hetxxbean());
    	}
    	return (Hetxxbean)((Visit) getPage().getVisit()).getObject1();
    }
    public void sethetxxbean(Hetxxbean value){
    	((Visit) getPage().getVisit()).setObject1(value);
    }
    //计划口径
    public IDropDownBean getJihxzValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean3()==null){
    		 ((Visit) getPage().getVisit()).setDropDownBean3((IDropDownBean)getIJihxzModel().getOption(0));
    	}
        return  ((Visit) getPage().getVisit()).getDropDownBean3();
    }

    public void setJihxzValue(IDropDownBean Value) {
    	((Visit) getPage().getVisit()).setDropDownBean3(Value);
    }


    public void setIJihxzModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel3(value);
    }

    public IPropertySelectionModel getIJihxzModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
            getIJihxzModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel3();
    }

    public void getIJihxzModels() {
        String sql = 
        	"select id,mingc\n" +
        	"from jihkjb";
        ((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql,"")) ;
        return ;
    }
    //合同数量信息
    private int _editTableRows = -1;
	public int getEditTableRows() {
		return _editTableRows;
	}
	public void setEditTableRows(int _value) {
		_editTableRows = _value;
	}
	public List geteditValuess(){
		if(((Visit) getPage().getVisit()).getList1()==null){
			((Visit) getPage().getVisit()).setList1(new ArrayList());
		}
		return ((Visit) getPage().getVisit()).getList1();
	}
	private Fahxxbean editValues1;
	public Fahxxbean geteditValues1(){
		return editValues1;
	}
	public void seteditValues1(Fahxxbean value){
		this.editValues1=value;
	}
	 //运输方式
    public IDropDownBean getyunsfsValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
    		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getyunsfsModel().getOption(0));
    	}
        return  ((Visit) getPage().getVisit()).getDropDownBean4();
    }

    public void setyunsfsValue(IDropDownBean Value) {
    	((Visit) getPage().getVisit()).setDropDownBean4(Value);
    }


    public void setyunsfsModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel4(value);
    }

    public IPropertySelectionModel getyunsfsModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
            getyunsfsModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }

    public void getyunsfsModels() {
        String sql = 
        	"select id,mingc\n" +
        	"from yunsfsb";
        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(sql,"")) ;
        return ;
    }
    //
    // 燃料品种
    public IDropDownBean getRanlpzb_idValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean5()==null){
    		 ((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean)getRanlpzb_idModel().getOption(0));
    	}
        return  ((Visit) getPage().getVisit()).getDropDownBean5();
    }

    public void setRanlpzb_idValue(IDropDownBean Value) {
    	((Visit) getPage().getVisit()).setDropDownBean5(Value);
    }


    public void setRanlpzb_idModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel5(value);
    }

    public IPropertySelectionModel getRanlpzb_idModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
            getRanlpzb_idModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel5();
    }

    public void getRanlpzb_idModels() {
        String sql = 
        	"select id,mingc\n" +
        	"from pinzb ";//where pinzb.zhangt=1
        ((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(sql,"")) ;
        return ;
    }
    //
//  车站
    public IDropDownBean getchezValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean6()==null){
    		 ((Visit) getPage().getVisit()).setDropDownBean6((IDropDownBean)getchezModel().getOption(0));
    	}
        return  ((Visit) getPage().getVisit()).getDropDownBean6();
    }

    public void setchezValue(IDropDownBean Value) {
    	((Visit) getPage().getVisit()).setDropDownBean6(Value);
    }


    public void setchezModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel6(value);
    }

    public IPropertySelectionModel getchezModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
            getchezModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel6();
    }

    public void getchezModels() {
        String sql = 
        	"select id,mingc\n" +
        	"from chezxxb\n" + 
        	"order by mingc";
        ((Visit) getPage().getVisit()).setProSelectionModel6(new IDropDownModel(sql,"")) ;
        return ;
    }
    //
// 收货人
    public IDropDownBean getshouhrValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean7()==null){
    		 ((Visit) getPage().getVisit()).setDropDownBean7((IDropDownBean)getshouhrModel().getOption(0));
    	}
        return  ((Visit) getPage().getVisit()).getDropDownBean7();
    }

    public void setshouhrValue(IDropDownBean Value) {
    	((Visit) getPage().getVisit()).setDropDownBean7(Value);
    }


    public void setshouhrModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel7(value);
    }

    public IPropertySelectionModel getshouhrModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {
            getshouhrModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel7();
    }

//    public void getshouhrModels() {
//        String sql = 
//        	"select id,mingc\n" +
//        	"from diancxxb";
//        ((Visit) getPage().getVisit()).setProSelectionModel7(new IDropDownModel(sql,"")) ;
//        return ;
//    }
    public void getshouhrModels() {
   	 String sql = 
        	"select id,mingc,jib\n" +
        	"from(\n" + 
        	" select id,mingc,0 as jib\n" + 
        	" from diancxxb\n" + 
        	" where id="+ ((Visit) getPage().getVisit()).getDiancxxb_id()+"\n" + 
        	" union\n" + 
        	" select *\n" + 
        	" from(\n" + 
        	" select id,mingc,level as jib\n" + 
        	"  from diancxxb\n" + 
        	" start with fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"\n" + 
        	" connect by fuid=prior id\n" + 
        	" order SIBLINGS by  xuh)\n" + 
        	" )\n" + 
        	" order by jib";
        List dropdownlist = new ArrayList();
        dropdownlist.add(new IDropDownBean(0, ""));
		JDBCcon con = new JDBCcon();
		try {
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String mc = rs.getString("mingc");
				int jib=rs.getInt("jib");
				String nbsp=String.valueOf((char)0xA0);
				for(int i=0;i<jib;i++){
					mc=nbsp+nbsp+nbsp+nbsp+mc;
				}
				dropdownlist.add(new IDropDownBean(id, mc));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
        ((Visit) getPage().getVisit()).setProSelectionModel7(new IDropDownModel(dropdownlist)) ;
        return ;
   }
    //质量
    private int _editTableRowz = -1;
	public int getEditTableRowz() {
		return _editTableRowz;
	}
	public void setEditTableRowz(int _value) {
		_editTableRowz = _value;
	}
	public List geteditValuesz(){
		if(((Visit) getPage().getVisit()).getList2()==null){
			((Visit) getPage().getVisit()).setList2(new ArrayList());
		}
		return ((Visit) getPage().getVisit()).getList2();
	}
	public Zhilyqbean editValuez;
	public Zhilyqbean geteditValuez(){
		return this.editValuez;
	}
	public void seteditValuez(Zhilyqbean value){
		this.editValuez=value;
	}
	//指标
	 	public IDropDownBean getZHIBValue() {
	    	if( ((Visit) getPage().getVisit()).getDropDownBean8()==null){
	    		 ((Visit) getPage().getVisit()).setDropDownBean8((IDropDownBean)getZHIBModel().getOption(0));
	    	}
	        return  ((Visit) getPage().getVisit()).getDropDownBean8();
	    }

	    public void setZHIBValue(IDropDownBean Value) {
	    	((Visit) getPage().getVisit()).setDropDownBean8(Value);
	    }


	    public void setZHIBModel(IPropertySelectionModel value) {
	    	((Visit) getPage().getVisit()).setProSelectionModel8(value);
	    }

	    public IPropertySelectionModel getZHIBModel() {
	        if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
	            getZHIBModels();
	        }
	        return ((Visit) getPage().getVisit()).getProSelectionModel8();
	    }

	    public void getZHIBModels() {
	        String sql = 
	        	"select id,mingc\n" +
	        	"from zhibb";
	        ((Visit) getPage().getVisit()).setProSelectionModel8(new IDropDownModel(sql,"")) ;
	        return ;
	    }
	    //条件
	    public IDropDownBean getTIAOJValue() {
	    	if( ((Visit) getPage().getVisit()).getDropDownBean9()==null){
	    		 ((Visit) getPage().getVisit()).setDropDownBean9((IDropDownBean)getTIAOJModel().getOption(0));
	    	}
	        return  ((Visit) getPage().getVisit()).getDropDownBean9();
	    }

	    public void setTIAOJValue(IDropDownBean Value) {
	    	((Visit) getPage().getVisit()).setDropDownBean9(Value);
	    }


	    public void setTIAOJModel(IPropertySelectionModel value) {
	    	((Visit) getPage().getVisit()).setProSelectionModel9(value);
	    }

	    public IPropertySelectionModel getTIAOJModel() {
	        if (((Visit) getPage().getVisit()).getProSelectionModel9() == null) {
	            getTIAOJModels();
	        }
	        return ((Visit) getPage().getVisit()).getProSelectionModel9();
	    }

	    public void getTIAOJModels() {
	        String sql = 
	        	"select id,mingc\n" +
	        	"from tiaojb";
	        ((Visit) getPage().getVisit()).setProSelectionModel9(new IDropDownModel(sql,"")) ;
	        return ;
	    }
	    //单位
	    public IDropDownBean getDANWValue() {
	    	if( ((Visit) getPage().getVisit()).getDropDownBean10()==null){
	    		 ((Visit) getPage().getVisit()).setDropDownBean10((IDropDownBean)getDANWModel().getOption(0));
	    	}
	        return  ((Visit) getPage().getVisit()).getDropDownBean10();
	    }

	    public void setDANWValue(IDropDownBean Value) {
	    	((Visit) getPage().getVisit()).setDropDownBean10(Value);
	    }


	    public void setDANWModel(IPropertySelectionModel value) {
	    	((Visit) getPage().getVisit()).setProSelectionModel10(value);
	    }

	    public IPropertySelectionModel getDANWModel() {
	        if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {
	            getDANWModels();
	        }
	        return ((Visit) getPage().getVisit()).getProSelectionModel10();
	    }

	    public void getDANWModels() {
	        String sql = 
	        	"select id,mingc\n" +
	        	"from danwb";
	        ((Visit) getPage().getVisit()).setProSelectionModel10(new IDropDownModel(sql,"")) ;
	        return ;
	    }
	    //增扣价格
	    private int _editTableRowj = -1;
		public int getEditTableRowj() {
			return _editTableRowj;
		}
		public void setEditTableRowj(int _value) {
			_editTableRowj = _value;
		}
		public List geteditValuesj(){
			if(((Visit) getPage().getVisit()).getList3()==null){
				((Visit) getPage().getVisit()).setList3(new ArrayList());
			}
			return ((Visit) getPage().getVisit()).getList3();
		}
		public Zengkkbean editValuej;
		public Zengkkbean geteditValuej(){
			return this.editValuej;
		}
		public void seteditValuej(Zengkkbean value){
			this.editValuej=value;
		}
		 //价格
	    private int _editTableRowg= -1;
		public int getEditTableRowg() {
			return _editTableRowg;
		}
		public void setEditTableRowg(int _value) {
			_editTableRowg = _value;
		}
		public List geteditValuesg(){
			if(((Visit) getPage().getVisit()).getList4()==null){
				((Visit) getPage().getVisit()).setList4(new ArrayList());
			}
			return ((Visit) getPage().getVisit()).getList4();
		}
		public jijbean editValueg;
		public jijbean geteditValueg(){
			return this.editValueg;
		}
		public void seteditValueg(jijbean value){
			this.editValueg=value;
		}
		 public IDropDownBean getzhilxmjSelectValue() {
		    	if( ((Visit) getPage().getVisit()).getDropDownBean11()==null){
		    		 ((Visit) getPage().getVisit()).setDropDownBean11((IDropDownBean)getzhilxmjSelectModel().getOption(0));
		    	}
		        return  ((Visit) getPage().getVisit()).getDropDownBean11();
		    }

		    public void setzhilxmjSelectValue(IDropDownBean Value) {
		    	((Visit) getPage().getVisit()).setDropDownBean11(Value);
		    }


		    public void setzhilxmjSelectModel(IPropertySelectionModel value) {
		    	((Visit) getPage().getVisit()).setProSelectionModel11(value);
		    }

		    public IPropertySelectionModel getzhilxmjSelectModel() {
		        if (((Visit) getPage().getVisit()).getProSelectionModel11() == null) {
		            getzhilxmjSelectModels();
		        }
		        return ((Visit) getPage().getVisit()).getProSelectionModel11();
		    }

		    public void getzhilxmjSelectModels() {
		        String sql = 
		        	"select id,mingc\n" +
		        	"from zhibb\n" + 
		        	"where zhibb.leib=1";
		        ((Visit) getPage().getVisit()).setProSelectionModel11(new IDropDownModel(sql,"")) ;
		        return ;
		    }
		    //结算用条件
		    public IDropDownBean gettiaojjSelectValue() {
		    	if( ((Visit) getPage().getVisit()).getDropDownBean12()==null){
		    		 ((Visit) getPage().getVisit()).setDropDownBean12((IDropDownBean)gettiaojjSelectModel().getOption(0));
		    	}
		        return  ((Visit) getPage().getVisit()).getDropDownBean12();
		    }

		    public void settiaojjSelectValue(IDropDownBean Value) {
		    	((Visit) getPage().getVisit()).setDropDownBean12(Value);
		    }


		    public void settiaojjSelectModel(IPropertySelectionModel value) {
		    	((Visit) getPage().getVisit()).setProSelectionModel12(value);
		    }

		    public IPropertySelectionModel gettiaojjSelectModel() {
		        if (((Visit) getPage().getVisit()).getProSelectionModel12() == null) {
		            gettiaojjSelectModels();
		        }
		        return ((Visit) getPage().getVisit()).getProSelectionModel12();
		    }

		    public void gettiaojjSelectModels() {
		        String sql = 
		        	"select id,mingc\n" +
		        	"from tiaojb\n" + 
		        	"where tiaojb.leib=1";
		        ((Visit) getPage().getVisit()).setProSelectionModel12(new IDropDownModel(sql,"")) ;
		        return ;
		    }

		    //价格单位
		    public IDropDownBean getjiagValue() {
		    	if( ((Visit) getPage().getVisit()).getDropDownBean13()==null){
		    		 ((Visit) getPage().getVisit()).setDropDownBean13((IDropDownBean)getjiagModel().getOption(0));
		    	}
		        return  ((Visit) getPage().getVisit()).getDropDownBean13();
		    }

		    public void setjiagValue(IDropDownBean Value) {
		    	((Visit) getPage().getVisit()).setDropDownBean13(Value);
		    }


		    public void setjiagModel(IPropertySelectionModel value) {
		    	((Visit) getPage().getVisit()).setProSelectionModel13(value);
		    }

		    public IPropertySelectionModel getjiagModel() {
		        if (((Visit) getPage().getVisit()).getProSelectionModel13() == null) {
		            getjiagModels();
		        }
		        return ((Visit) getPage().getVisit()).getProSelectionModel13();
		    }

		    public void getjiagModels() {
		        String sql = 
		        	"select id,mingc\n" +
		        	"from danwb\n"+
		        	"where danwb.zhibb_id=0";//zhibb_id=0为价格单位
		        ((Visit) getPage().getVisit()).setProSelectionModel13(new IDropDownModel(sql,"")) ;
		        return ;
		    }

//			  结算用指标单位
		    public IDropDownBean getzhibdwSelectValue() {
		    	if( ((Visit) getPage().getVisit()).getDropDownBean14()==null){
		    		 ((Visit) getPage().getVisit()).setDropDownBean14((IDropDownBean)getzhibdwSelectModel().getOption(0));
		    	}
		        return  ((Visit) getPage().getVisit()).getDropDownBean14();
		    }

		    public void setzhibdwSelectValue(IDropDownBean Value) {
		    	((Visit) getPage().getVisit()).setDropDownBean14(Value);
		    }


		    public void setzhibdwSelectModel(IPropertySelectionModel value) {
		    	((Visit) getPage().getVisit()).setProSelectionModel14(value);
		    }

		    public IPropertySelectionModel getzhibdwSelectModel() {
		        if (((Visit) getPage().getVisit()).getProSelectionModel14() == null) {
		            getzhibdwSelectModels();
		        }
		        return ((Visit) getPage().getVisit()).getProSelectionModel14();
		    }

		    public void getzhibdwSelectModels() {//zhibb_id=
		        String sql = 
		        	"select id,mingc " +
		        	"from danwb\n" ;
		        ((Visit) getPage().getVisit()).setProSelectionModel14(new IDropDownModel(sql,"")) ;
		        return ;
		    }
//			 合同结算方式表
		    public IDropDownBean getjiesfsgSelectValue() {
		    	if( ((Visit) getPage().getVisit()).getDropDownBean15()==null){
		    		 ((Visit) getPage().getVisit()).setDropDownBean15((IDropDownBean)getjiesfsgSelectModel().getOption(0));
		    	}
		        return  ((Visit) getPage().getVisit()).getDropDownBean15();
		    }

		    public void setjiesfsgSelectValue(IDropDownBean Value) {
		    	((Visit) getPage().getVisit()).setDropDownBean15(Value);
		    }


		    public void setjiesfsgSelectModel(IPropertySelectionModel value) {
		    	((Visit) getPage().getVisit()).setProSelectionModel15(value);
		    }

		    public IPropertySelectionModel getjiesfsgSelectModel() {
		        if (((Visit) getPage().getVisit()).getProSelectionModel15() == null) {
		            getjiesfsgSelectModels();
		        }
		        return ((Visit) getPage().getVisit()).getProSelectionModel15();
		    }

		    public void getjiesfsgSelectModels() {
		        String sql = 
		        	"select id,mingc\n" + 
		        	"from hetjsfsb";
		        ((Visit) getPage().getVisit()).setProSelectionModel15(new IDropDownModel(sql,"")) ;
		        return ;
		    }
//			合同计价方式
		    public IDropDownBean getjijfsgSelectValue() {
		    	if( ((Visit) getPage().getVisit()).getDropDownBean16()==null){
		    		 ((Visit) getPage().getVisit()).setDropDownBean16((IDropDownBean)getjijfsgSelectModel().getOption(0));
		    	}
		        return  ((Visit) getPage().getVisit()).getDropDownBean16();
		    }

		    public void setjijfsgSelectValue(IDropDownBean Value) {
		    	((Visit) getPage().getVisit()).setDropDownBean16(Value);
		    }


		    public void setjijfsgSelectModel(IPropertySelectionModel value) {
		    	((Visit) getPage().getVisit()).setProSelectionModel16(value);
		    }

		    public IPropertySelectionModel getjijfsgSelectModel() {
		        if (((Visit) getPage().getVisit()).getProSelectionModel16() == null) {
		            getjijfsgSelectModels();
		        }
		        return ((Visit) getPage().getVisit()).getProSelectionModel16();
		    }
		    public void getjijfsgSelectModels() {
		    	String sql="select id,mingc\n" +
		    	"from hetjjfsb";
		        ((Visit) getPage().getVisit()).setProSelectionModel16(new IDropDownModel(sql,"")) ;
		        return ;
		    }
//			  小数处理
		    public IDropDownBean getxiaoswcljSelectValue() {
		    	if( ((Visit) getPage().getVisit()).getDropDownBean17()==null){
		    		 ((Visit) getPage().getVisit()).setDropDownBean17((IDropDownBean)getxiaoswcljSelectModel().getOption(0));
		    	}
		        return  ((Visit) getPage().getVisit()).getDropDownBean17();
		    }

		    public void setxiaoswcljSelectValue(IDropDownBean Value) {
		    	((Visit) getPage().getVisit()).setDropDownBean17(Value);
		    }


		    public void setxiaoswcljSelectModel(IPropertySelectionModel value) {
		    	((Visit) getPage().getVisit()).setProSelectionModel17(value);
		    }

		    public IPropertySelectionModel getxiaoswcljSelectModel() {
		        if (((Visit) getPage().getVisit()).getProSelectionModel17() == null) {
		            getxiaoswcljSelectModels();
		        }
		        return ((Visit) getPage().getVisit()).getProSelectionModel17();
		    }

		    public void getxiaoswcljSelectModels() {
		    	 List list=new ArrayList();
			      list.add(new IDropDownBean(0," "));
			      list.add(new IDropDownBean(1,"进位"));
			      list.add(new IDropDownBean(2,"舍去"));
			      list.add(new IDropDownBean(3,"四舍五入"));
			      list.add(new IDropDownBean(4,"四舍五入(0.1)"));
			      list.add(new IDropDownBean(5,"四舍五入(0.01)"));
			      
		        ((Visit) getPage().getVisit()).setProSelectionModel17(new IDropDownModel(list)) ;
		        return ;
		    }
		   //文字
		   String buffer;
		   public String getWenz(){
			   return buffer;
		   }
		   public void setWenz(String value){
			   this.buffer=value;
		   }
		   //合同供方
		   public IDropDownBean getgongfValue() {
		    	if( ((Visit) getPage().getVisit()).getDropDownBean18()==null){
		    		 ((Visit) getPage().getVisit()).setDropDownBean18((IDropDownBean)getgongfModel().getOption(0));
		    	}
		        return  ((Visit) getPage().getVisit()).getDropDownBean18();
		    }

		    public void setgongfValue(IDropDownBean Value) {
			    if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean18()!=null){
			    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean18().getId()){
			    		((Visit) getPage().getVisit()).setboolean2(true);
			    	}else{
			    		((Visit) getPage().getVisit()).setboolean2(false);
			    	}
			    	((Visit) getPage().getVisit()).setDropDownBean18(Value);
			    }
		    }


		    public void setgongfModel(IPropertySelectionModel value) {
		    	((Visit) getPage().getVisit()).setProSelectionModel18(value);
		    }

		    public IPropertySelectionModel getgongfModel() {
		        if (((Visit) getPage().getVisit()).getProSelectionModel18() == null) {
		            getgongfModels();
		        }
		        return ((Visit) getPage().getVisit()).getProSelectionModel18();
		    }

		    public void getgongfModels() {
		    	String sql=
		    		"select id,mingc\n" +
		    		"from gongysb";
		        ((Visit) getPage().getVisit()).setProSelectionModel18(new IDropDownModel(sql,"")) ;
		        return ;
		    }
		  //需方
		    public IDropDownBean getxufValue() {
		    	if( ((Visit) getPage().getVisit()).getDropDownBean19()==null){
		    		 ((Visit) getPage().getVisit()).setDropDownBean19((IDropDownBean)getxufModel().getOption(0));
		    	}
		        return  ((Visit) getPage().getVisit()).getDropDownBean19();
		    }

		    public void setxufValue(IDropDownBean Value) {
		    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean19()!=null){
			    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean19().getId()){
			    		((Visit) getPage().getVisit()).setboolean3(true);
			    	}else{
			    		((Visit) getPage().getVisit()).setboolean3(false);
			    	}
			    	((Visit) getPage().getVisit()).setDropDownBean19(Value);
			    }
		    }


		    public void setxufModel(IPropertySelectionModel value) {
		    	((Visit) getPage().getVisit()).setProSelectionModel19(value);
		    }

		    public IPropertySelectionModel getxufModel() {
		        if (((Visit) getPage().getVisit()).getProSelectionModel19() == null) {
		            getxufModels();
		        }
		        return ((Visit) getPage().getVisit()).getProSelectionModel19();
		    }

//		    public void getxufModels() {//显示该用户单位下所有孩子
//		    	String sql=
//		    		"select id,mingc " +
//		    		"from diancxxb\n" + 
//		    		"where diancxxb.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+" or id="+((Visit) getPage().getVisit()).getDiancxxb_id();
//		        ((Visit) getPage().getVisit()).setProSelectionModel19(new IDropDownModel(sql,"")) ;
//		        return ;
//		    }
		    public void getxufModels() {//显示该用户单位下所有孩子
		    	String sql=
		    		"select id,mingc " +
		    		"from diancxxb\n" + 
		    		"where  id="+((Visit) getPage().getVisit()).getDiancxxb_id();
		        ((Visit) getPage().getVisit()).setProSelectionModel19(new IDropDownModel(sql,"")) ;
		        return ;
		    }
		    //供方
			   public IDropDownBean getshijgfSelectValue() {
			    	if( ((Visit) getPage().getVisit()).getDropDownBean20()==null){
			    		 ((Visit) getPage().getVisit()).setDropDownBean20((IDropDownBean)getshijgfSelectModel().getOption(0));
			    	}
			        return  ((Visit) getPage().getVisit()).getDropDownBean20();
			    }

			   public void setshijgfSelectValue(IDropDownBean Value) {
			    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean20()!=null){
				    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean20().getId()){
				    		((Visit) getPage().getVisit()).setboolean7(true);
				    	}else{
				    		((Visit) getPage().getVisit()).setboolean7(false);
				    	}
				    	((Visit) getPage().getVisit()).setDropDownBean20(Value);
				    }
			    	((Visit) getPage().getVisit()).setDropDownBean20(Value);
			    }


			    public void setshijgfSelectModel(IPropertySelectionModel value) {
			    	((Visit) getPage().getVisit()).setProSelectionModel20(value);
			    }

			    public IPropertySelectionModel getshijgfSelectModel() {
			        if (((Visit) getPage().getVisit()).getProSelectionModel20() == null) {
			            getshijgfSelectModels();
			        }
			        return ((Visit) getPage().getVisit()).getProSelectionModel20();
			    }

			    public void getshijgfSelectModels() {
			    	String sql=
			    		"select id,mingc\n" +
			    		"from gongysb";
			        ((Visit) getPage().getVisit()).setProSelectionModel20(new IDropDownModel(sql,"")) ;
			        return ;
			    }
		    //加权方式
			   public IDropDownBean getjiaqfsgSelectValue() {
			    	if( ((Visit) getPage().getVisit()).getDropDownBean21()==null){
			    		 ((Visit) getPage().getVisit()).setDropDownBean21((IDropDownBean)getjiaqfsgSelectModel().getOption(0));
			    	}
			        return  ((Visit) getPage().getVisit()).getDropDownBean21();
			    }

			    public void setjiaqfsgSelectValue(IDropDownBean Value) {
			    	((Visit) getPage().getVisit()).setDropDownBean21(Value);
			    }


			    public void setjiaqfsgSelectModel(IPropertySelectionModel value) {
			    	((Visit) getPage().getVisit()).setProSelectionModel21(value);
			    }

			    public IPropertySelectionModel getjiaqfsgSelectModel() {
			        if (((Visit) getPage().getVisit()).getProSelectionModel21() == null) {
			            getjiaqfsgSelectModels();
			        }
			        return ((Visit) getPage().getVisit()).getProSelectionModel21();
			    }

			    public void getjiaqfsgSelectModels() {
			    	String sql=
			    		"select id,mingc\n" + 
			    		"from hetjsxsb";
			        ((Visit) getPage().getVisit()).setProSelectionModel21(new IDropDownModel(sql,"")) ;
			        return ;
			    }
			    //拒付亏吨运费
			   public IDropDownBean getyingdkfgSelectValue() {
			    	if( ((Visit) getPage().getVisit()).getDropDownBean22()==null){
			    		 ((Visit) getPage().getVisit()).setDropDownBean22((IDropDownBean)getyingdkfgSelectModel().getOption(0));
			    	}
			        return  ((Visit) getPage().getVisit()).getDropDownBean22();
			    }

			    public void setyingdkfgSelectValue(IDropDownBean Value) {
			    	((Visit) getPage().getVisit()).setDropDownBean22(Value);
			    }


			    public void setyingdkfgSelectModel(IPropertySelectionModel value) {
			    	((Visit) getPage().getVisit()).setProSelectionModel22(value);
			    }

			    public IPropertySelectionModel getyingdkfgSelectModel() {
			        if (((Visit) getPage().getVisit()).getProSelectionModel22() == null) {
			            getyingdkfgSelectModels();
			        }
			        return ((Visit) getPage().getVisit()).getProSelectionModel22();
			    }

			    public void getyingdkfgSelectModels() {
			    	List list=new ArrayList();
			    	list.add(new IDropDownBean(0,"") );
			    	list.add(new IDropDownBean(1,"是") );
			    	list.add(new IDropDownBean(2,"否") );
			        ((Visit) getPage().getVisit()).setProSelectionModel22(new IDropDownModel(list)) ;
			        return ;
			    }
			    //年份
				public IDropDownBean getNianfValue() {
					if (((Visit) getPage().getVisit()).getDropDownBean23() == null) {
						((Visit) getPage().getVisit()).setDropDownBean23(getIDropDownBean(getNianfModel(),new Date().getYear()+1900));
					}
					return ((Visit) getPage().getVisit()).getDropDownBean23();
				}
				public void setNianfValue(IDropDownBean Value) {
					if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean23()!=null){
						if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean23().getId()){
				    		((Visit) getPage().getVisit()).setboolean4(true);
				    	}else{
				    		((Visit) getPage().getVisit()).setboolean4(false);
				    	}
						 ((Visit) getPage().getVisit()).setDropDownBean23(Value);
					}
				}
				public IPropertySelectionModel getNianfModel() {
					if (((Visit) getPage().getVisit()).getProSelectionModel23() == null) {
						getNianfModels();
					}
					return ((Visit) getPage().getVisit()).getProSelectionModel23();
				}
			    public void getNianfModels() {
					List listNianf = new ArrayList();
					int i;
					for (i = 2004; i <=DateUtil.getYear(new Date())+2; i++) {
						listNianf.add(new IDropDownBean(i, String.valueOf(i)));
					}
					 ((Visit) getPage().getVisit()).setProSelectionModel23(new IDropDownModel(listNianf)) ;
				}
			    //煤矿
				   public IDropDownBean getMeiksselectValue() {
				    	if( ((Visit) getPage().getVisit()).getDropDownBean24()==null){
				    		 ((Visit) getPage().getVisit()).setDropDownBean24((IDropDownBean)getMeiksselectModel().getOption(0));
				    	}
				        return  ((Visit) getPage().getVisit()).getDropDownBean24();
				    }

				    public void setMeiksselectValue(IDropDownBean Value) {
				    	((Visit) getPage().getVisit()).setDropDownBean24(Value);
				    }


				    public void setMeiksselectModel(IPropertySelectionModel value) {
				    	((Visit) getPage().getVisit()).setProSelectionModel24(value);
				    }

				    public IPropertySelectionModel getMeiksselectModel() {
				        if (((Visit) getPage().getVisit()).getProSelectionModel24() == null) {
				            getMeiksselectModels();
				        }
				        return ((Visit) getPage().getVisit()).getProSelectionModel24();
				    }

				    public void getMeiksselectModels() {
				    	String sql=
				    		"select meikxxb.id,meikxxb.mingc\n" +
				    		"from meikxxb,gongysmkglb\n" + 
				    		"where meikxxb.id=gongysmkglb.meikxxb_id\n" + 
				    		"and gongysmkglb.gongysb_id="+getshijgfSelectValue().getId();

				        ((Visit) getPage().getVisit()).setProSelectionModel24(new IDropDownModel(sql,"")) ;
				        return ;
				    }
    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
        Visit visit = (Visit) getPage().getVisit();
        if (!visit.getActivePageName().toString().equals(  this.getPageName().toString())) {
            // 在此添加，在页面第一次加载时需要置为空的变量或方法
            visit.setActivePageName(getPageName().toString());
            visit.setString1("");
            visit.setString2("");
            visit.setString3("");
            ((Visit) getPage().getVisit()).setDropDownBean1(null);
            ((Visit) getPage().getVisit()).setDropDownBean2(null);
            ((Visit) getPage().getVisit()).setDropDownBean3(null);
            ((Visit) getPage().getVisit()).setDropDownBean4(null);
            ((Visit) getPage().getVisit()).setDropDownBean5(null);
            ((Visit) getPage().getVisit()).setDropDownBean6(null);
            ((Visit) getPage().getVisit()).setDropDownBean7(null);
            ((Visit) getPage().getVisit()).setDropDownBean8(null);
            ((Visit) getPage().getVisit()).setDropDownBean9(null);
            ((Visit) getPage().getVisit()).setDropDownBean10(null);
            ((Visit) getPage().getVisit()).setDropDownBean11(null);
            ((Visit) getPage().getVisit()).setDropDownBean12(null);
            ((Visit) getPage().getVisit()).setDropDownBean13(null);
            ((Visit) getPage().getVisit()).setDropDownBean14(null);
            ((Visit) getPage().getVisit()).setDropDownBean15(null);
            ((Visit) getPage().getVisit()).setDropDownBean16(null);
            ((Visit) getPage().getVisit()).setDropDownBean17(null);
            ((Visit) getPage().getVisit()).setDropDownBean18(null);
            ((Visit) getPage().getVisit()).setDropDownBean19(null);
            ((Visit) getPage().getVisit()).setDropDownBean20(null);
            ((Visit) getPage().getVisit()).setDropDownBean21(null);
            ((Visit) getPage().getVisit()).setDropDownBean22(null);
            ((Visit) getPage().getVisit()).setDropDownBean23(null);
            ((Visit) getPage().getVisit()).setDropDownBean24(null);
            
            ((Visit) getPage().getVisit()).setProSelectionModel1(null);
            ((Visit) getPage().getVisit()).setProSelectionModel2(null);
            ((Visit) getPage().getVisit()).setProSelectionModel3(null);
            ((Visit) getPage().getVisit()).setProSelectionModel4(null);
            ((Visit) getPage().getVisit()).setProSelectionModel5(null);
            ((Visit) getPage().getVisit()).setProSelectionModel6(null);
            ((Visit) getPage().getVisit()).setProSelectionModel7(null);
            ((Visit) getPage().getVisit()).setProSelectionModel8(null);
            ((Visit) getPage().getVisit()).setProSelectionModel9(null);
            ((Visit) getPage().getVisit()).setProSelectionModel10(null);
            ((Visit) getPage().getVisit()).setProSelectionModel11(null);
            ((Visit) getPage().getVisit()).setProSelectionModel12(null);
            ((Visit) getPage().getVisit()).setProSelectionModel13(null);
            ((Visit) getPage().getVisit()).setProSelectionModel14(null);
            ((Visit) getPage().getVisit()).setProSelectionModel15(null);
            ((Visit) getPage().getVisit()).setProSelectionModel16(null);
            ((Visit) getPage().getVisit()).setProSelectionModel17(null);
            ((Visit) getPage().getVisit()).setProSelectionModel18(null);
            ((Visit) getPage().getVisit()).setProSelectionModel19(null);
            ((Visit) getPage().getVisit()).setProSelectionModel20(null);
            ((Visit) getPage().getVisit()).setProSelectionModel21(null);
            ((Visit) getPage().getVisit()).setProSelectionModel22(null);
            ((Visit) getPage().getVisit()).setProSelectionModel23(null);
            ((Visit) getPage().getVisit()).setProSelectionModel24(null);
            
            ((Visit) getPage().getVisit()).setObject1(null);
            ((Visit) getPage().getVisit()).setList1(null);
            ((Visit) getPage().getVisit()).setList2(null);
            ((Visit) getPage().getVisit()).setList3(null);
            ((Visit) getPage().getVisit()).setList4(null);
            setTabbarSelect(0);
            setXinjht(true);
            getDanwGL();
        }
        if(((Visit) getPage().getVisit()).getboolean1()){//选择合同
        	getXuanzht();
        }
        if(((Visit) getPage().getVisit()).getboolean2()){//合同供方
        	getGongf();
        }
        if(((Visit) getPage().getVisit()).getboolean7()){//真实供方
        	getMeiksselectModels();
        }
        if(((Visit) getPage().getVisit()).getboolean3()){//需方
        	getXuf();
        }
        if(((Visit) getPage().getVisit()).getboolean4()){//年份刷新合同
        	gethetSelectModels();
        }
        if(((Visit) getPage().getVisit()).getboolean6()){//加载模板
        	gethetmb(getmobmcSelectValue().getId());
        }
    }
    private void getDanwGL(){
    	String sql="";
    	List list=new ArrayList();
    	StringBuffer Tem=new StringBuffer();
    	JDBCcon con=new JDBCcon();
    	sql=
		"select zhibb_id zhibid,danwb.id danwid,danwb.mingc,zhibb.mingc\n" +
		"from danwb,zhibb\n" + 
		"where danwb.zhibb_id=zhibb.id and zhibb_id<>0\n" + 
		"order by zhibb_id";
    	ResultSet rs=con.getResultSet(sql);
    	try{
    		while(rs.next()){
        	 	String[] gl=new String[4];
        		gl[0]=rs.getString(1);
        		gl[1]=rs.getString(2);
        		gl[2]=rs.getString(3);
        		gl[3]=rs.getString(4);
        		list.add(gl);
        	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		con.Close();
    	}
    	Tem.append("var zhib_danw=new Array();");
    	for(int i=0;i<list.size();i++){
    		Tem.append("zhib_danw["+i+"]=new Array("+((String[])list.get(i))[0]+","+((String[])list.get(i))[1]+",'"+((String[])list.get(i))[2]+"','"+((String[])list.get(i))[3]+"');");//+
    	}
    	setDanwglStr(Tem.toString());
    }
//	页面判定方法
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
   //模板管理
//	 	private boolean _XinjButton = false;
//
//	    public void XinjButton(IRequestCycle cycle) {
//	    	_XinjButton = true;
//	    }
//
//	    private boolean _DakButton = false;
//
//	    public void DakButton(IRequestCycle cycle) {
//	    	_DakButton = true;
//	    }

	    private boolean _ShancButton = false;

	    public void ShancButton(IRequestCycle cycle) {
	    	_ShancButton = true;
	    }
	    private boolean _BaocButton = false;

	    public void BaocButton(IRequestCycle cycle) {
	    	_BaocButton = true;
	    }
	    private boolean TijButton = false;

	    public void TijButton(IRequestCycle cycle) {
	    	TijButton = true;
	    }
	//数量
	    private boolean _InsertButtons = false;

	    public void InsertButtons(IRequestCycle cycle) {
	    	_InsertButtons = true;
	    }
	    private boolean _DeleteButtons = false;

	    public void DeleteButtons(IRequestCycle cycle) {
	    	_DeleteButtons = true;
	    }
	//质量
	    private boolean _InsertButtonz = false;

	    public void InsertButtonz(IRequestCycle cycle) {
	    	_InsertButtonz = true;
	    }
	    private boolean _DeleteButtonz = false;

	    public void DeleteButtonz(IRequestCycle cycle) {
	    	_DeleteButtonz = true;
	    }
	//增扣价格
	    private boolean _InsertButtonj = false;

	    public void InsertButtonj(IRequestCycle cycle) {
	    	_InsertButtonj = true;
	    }
	    private boolean _DeleteButtonj = false;

	    public void DeleteButtonj(IRequestCycle cycle) {
	    	_DeleteButtonj = true;
	    }
	    //	  价格
	    private boolean _InsertButtong = false;

	    public void InsertButtong(IRequestCycle cycle) {
	    	_InsertButtong = true;
	    }
	    private boolean _DeleteButtong = false;

	    public void DeleteButtong(IRequestCycle cycle) {
	    	_DeleteButtong = true;
	    }
	    public void submit(IRequestCycle cycle) {
	    	//模板管理操作
//	        if (_XinjButton) {
//	        	_XinjButton = false;
//	        	Xinj();
//	        }
//	        if (_DakButton) {
//	        	_DakButton = false;
//	        	Dak();
//	        }
	    	if (TijButton) {
		        	TijButton = false;
		        	Tij();
		    }
	        if (_ShancButton) {
	        	_ShancButton = false;
	        	Shanc();
	        }
	        if (_BaocButton) {
	        	_BaocButton = false;
	        	Baoc();
	        }
	        //数量
	        if (_InsertButtons) {
	        	_InsertButtons = false;
	        	Inserts();
	        }
	        if (_DeleteButtons) {
	        	_DeleteButtons = false;
	        	Deletes();
	        }
	        // 质量
	        if (_InsertButtonz) {
	        	_InsertButtonz = false;
	        	Insertz();
	        }
	        if (_DeleteButtonz) {
	        	_DeleteButtonz = false;
	        	Deletez();
	        }
	        //价格
	        if (_InsertButtonj) {
	        	_InsertButtonj = false;
	        	Insertj();
	        }
	        if (_DeleteButtonj) {
	        	_DeleteButtonj = false;
	        	Deletej();
	        }
	        //价格
	        if (_InsertButtong) {
	        	_InsertButtong = false;
	        	Insertg();
	        }
	        if (_DeleteButtong) {
	        	_DeleteButtong = false;
	        	Deleteg();
	        }
	    }
	    private void Tij(){
	    	Baoc();//提交先进行保存工作
	    	String sql="";
	    	long hetb_id=gethetxxbean().getId();
	    	JDBCcon con=new JDBCcon();
	    	//提交后进入相应流程的初始状态（xuh＝1）
	    	long chuszt=0;
	    	sql="select  liucztb.id\n" +
	    	"from hetb,hetb_mb,liucztb\n" + 
	    	"where hetb.hetb_mb_id=hetb_mb.id and hetb_mb.liucb_id=liucztb.liucb_id and xuh=1 and hetb.id="+hetb_id;
	    	ResultSet rs=con.getResultSet(sql);
	    	try{
	    		if(rs.next()){
		    		chuszt=rs.getLong("id");
		    	}
	    	}catch(Exception e){
	    		e.printStackTrace();
	    		return;
	    	}
	    	sql="update hetb set liucztb_id=" +chuszt
	    	+" where hetb.id="+hetb_id;
	    	con.getUpdate(sql);
	    	con.Close();
	    	gethetSelectModels();
	    	Xinj();
	    }
	    private void Xinj(){
	    	//合同信息
	    	 setXinjht(true);
	    	 Hetxxbean bean=gethetxxbean();
	    	 setmobmcSelectValue(getIDropDownBean(getmobmcSelectModel(),-1));
	    	 setJihxzValue(getIDropDownBean(getIJihxzModel(),-1));
	    	 setshijgfSelectValue(getIDropDownBean(getshijgfSelectModel(),-1));
	    	 ((Visit) getPage().getVisit()).setDropDownBean19(getIDropDownBean(getxufModel(),-1));
	    	 ((Visit) getPage().getVisit()).setDropDownBean18(getIDropDownBean(getgongfModel(),-1));
	    	 bean.setGONGFDBGH("");
	    	 bean.setGONGFDH("");
	    	 bean.setGONGFDWDZ("");
	    	 bean.setGONGFDWMC("");
	    	 bean.setGONGFFDDBR("");
	    	 bean.setGONGFKHYH("");
	    	 bean.setGongfsh("");
	    	 bean.setGONGFWTDLR("");
	    	 bean.setGONGFYZBM("");
	    	 bean.setGONGFZH("");
	    	 bean.setGuoqsj(null);
	    	 bean.setHetbh("");
//	    	 bean.setHetyj("");
	    	 bean.setQianddd("");
	    	 bean.setQiandsj(null);
	    	 bean.setShengxsj(null);
	    	 bean.setXUFDBGH("");
	    	 bean.setXUFDH("");
	    	 bean.setXUFDWDZ("");
	    	 bean.setXUFDWMC("");
	    	 bean.setXUFFDDBR("");
	    	 bean.setXUFKHYH("");
	    	 bean.setXufsh("");
	    	 bean.setXUFWTDLR("");
	    	 bean.setXUFYZBM("");
	    	 bean.setXUFZH("");
	    	 setFahr("");
	    	//数量信息
	    	geteditValuess().clear();
	    	//质量信息
	    	geteditValuesz().clear();
	    	//价格信息
	    	geteditValuesj().clear();
	    	//文字信息
	    	setWenz("");
	    }
	    private void Dak(long hetmb_id){
	    	 setXinjht(false);
	    	  String sql="";
	    	  JDBCcon con=new JDBCcon();
	    try{
	    	//合同信息
	         sql=
	        	"select ID,HETBH,QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR," +
	        	"GONGFWTDLR,GONGFDBGH,GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR," + 
	        	"XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,HETGYSBID,GONGYSB_ID,QISRQ,GUOQRQ," + 
	        	"JIHKJB_ID,diancxxb_id,hetb_mb_id,meikmcs " + 
	        	"from hetb" +
	        	" where ID="+hetmb_id;
	         ResultSet rs=con.getResultSet(sql);
	         if(rs.next()){
	    	 Hetxxbean bean=gethetxxbean();
	    	 setJihxzValue(getIDropDownBean(getIJihxzModel(),rs.getLong("JIHKJB_ID")));
//	    	 setmobmc(rs.getString("MINGC"));
	    	 setshijgfSelectValue(getIDropDownBean(getshijgfSelectModel(),rs.getLong("GONGYSB_ID")));
	    	 ((Visit) getPage().getVisit()).setDropDownBean19(getIDropDownBean(getxufModel(),rs.getLong("diancxxb_id")));
	    	 ((Visit) getPage().getVisit()).setDropDownBean18(getIDropDownBean(getgongfModel(),rs.getLong("HETGYSBID")));
//	    	 setmobmcSelectValue(getIDropDownBean(getmobmcSelectModel(),rs.getLong("hetb_mb_id")));
	    	 bean.setGONGFDBGH(rs.getString("GONGFDBGH"));
	    	 bean.setGONGFDH(rs.getString("GONGFDH"));
	    	 bean.setGONGFDWDZ(rs.getString("GONGFDWDZ"));
	    	 bean.setGONGFDWMC(rs.getString("GONGFDWMC"));
	    	 bean.setGONGFFDDBR(rs.getString("GONGFFDDBR"));
	    	 bean.setGONGFKHYH(rs.getString("GONGFKHYH"));
	    	 bean.setGongfsh(rs.getString("Gongfsh"));
	    	 bean.setGONGFWTDLR(rs.getString("GONGFWTDLR"));
	    	 bean.setGONGFYZBM(rs.getString("GONGFYZBM"));
	    	 bean.setGONGFZH(rs.getString("GONGFZH"));
	    	 bean.setGuoqsj(rs.getDate("GUOQRQ"));
	    	 bean.setHetbh(rs.getString("Hetbh"));
//	    	 bean.setHetyj(rs.getString("Hetyj"));
	    	 bean.setQianddd(rs.getString("Qianddd"));
	    	 bean.setQiandsj(rs.getDate("QIANDRQ"));
	    	 bean.setShengxsj(rs.getDate("QISRQ"));
	    	 bean.setXUFDBGH(rs.getString("XUFDBGH"));
	    	 bean.setXUFDH(rs.getString("XUFDH"));
	    	 bean.setXUFDWDZ(rs.getString("XUFDWDZ"));
	    	 bean.setXUFDWMC(rs.getString("XUFDWMC"));
	    	 bean.setXUFFDDBR(rs.getString("XUFFDDBR"));
	    	 bean.setXUFKHYH(rs.getString("XUFKHYH"));
	    	 bean.setXufsh(rs.getString("Xufsh"));
	    	 bean.setXUFWTDLR(rs.getString("XUFWTDLR"));
	    	 bean.setXUFYZBM(rs.getString("XUFYZBM"));
	    	 bean.setXUFZH(rs.getString("XUFZH"));
	    	 setFahr(rs.getString("meikmcs"));
	         }
	    	//数量信息
	    	 List list=geteditValuess();
	    	 list.clear();
	    	 sql=
	    		 "select aa.hetb_id,aa.id,y1, y2,y3,y4, y5, y6, y7, y8,\n" +
	    		 " y9, y10,y11,y12,pinzb.mingc pinz,yunsfsb.mingc yunsfs,faz.mingc faz,daoz.mingc daoz\n" + 
	    		 ",diancxxb.mingc shouhr\n" + 
	    		 "from(\n" + 
	    		 "\n" + 
	    		 "select a.hetb_id,a.id,a.pinzb_id,a.yunsfsb_id,a.faz_id,a.daoz_id,a.diancxxb_id,y1.hetl as y1,y2.hetl as y2,y3.hetl as y3,y4.hetl as y4,y5.hetl as y5,y6.hetl as y6,y7.hetl as y7,y8.hetl as y8,\n" + 
	    		 "y9.hetl as y9,y10.hetl as y10,y11.hetl as y11,y12.hetl as y12\n" + 
	    		 "from\n" + 
	    		 "    (select hetb_id,id,to_char(max(hetslb.riq),'MM')Y,max(hetl)hetl,hetslb.pinzb_id,hetslb.yunsfsb_id,hetslb.faz_id,hetslb.daoz_id,hetslb.diancxxb_id\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where hetb_id="+hetmb_id+"\n" + 
	    		 "    group by hetb_id,id,hetslb.pinzb_id,hetslb.yunsfsb_id,hetslb.faz_id,hetslb.daoz_id,hetslb.diancxxb_id\n" + 
	    		 "    )a,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,1 as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='01'and hetslb.hetb_id="+hetmb_id+")y1,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,2 as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='02'and hetslb.hetb_id="+hetmb_id+")y2,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,3 as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='03'and hetslb.hetb_id="+hetmb_id+")y3,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,4 as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='04'and hetslb.hetb_id="+hetmb_id+")y4,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,5 as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='05'and hetslb.hetb_id="+hetmb_id+")y5,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,6as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='06'and hetslb.hetb_id="+hetmb_id+")y6,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,7 as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='07'and hetslb.hetb_id="+hetmb_id+")y7,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,8 as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='08'and hetslb.hetb_id="+hetmb_id+")y8,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,9 as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='09'and hetslb.hetb_id="+hetmb_id+")y9,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,10 as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='10'and hetslb.hetb_id="+hetmb_id+")y10,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,11 as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='11'and hetslb.hetb_id="+hetmb_id+")y11,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,12 as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='12'and hetslb.hetb_id="+hetmb_id+")y12\n" + 
	    		 "\n" + 
	    		 "------------------------------------\n" + 
	    		 "where a.hetb_id=y1.hetb_id(+) and a.id=y1.id(+)\n" + 
	    		 "and a.hetb_id=y2.hetb_id(+) and a.id=y2.id(+)\n" + 
	    		 "and a.hetb_id=y3.hetb_id(+) and a.id=y3.id(+)\n" + 
	    		 "and a.hetb_id=y4.hetb_id(+) and a.id=y4.id(+)\n" + 
	    		 "and a.hetb_id=y5.hetb_id(+) and a.id=y5.id(+)\n" + 
	    		 "and a.hetb_id=y6.hetb_id(+) and a.id=y6.id(+)\n" + 
	    		 "and a.hetb_id=y7.hetb_id(+) and a.id=y7.id(+)\n" + 
	    		 "and a.hetb_id=y8.hetb_id(+) and a.id=y8.id(+)\n" + 
	    		 "and a.hetb_id=y9.hetb_id(+) and a.id=y9.id(+)\n" + 
	    		 "and a.hetb_id=y10.hetb_id(+) and a.id=y10.id(+)\n" + 
	    		 "and a.hetb_id=y11.hetb_id(+) and a.id=y11.id(+)\n" + 
	    		 "and a.hetb_id=y12.hetb_id(+) and a.id=y12.id(+)\n" + 
	    		 ")aa,pinzb,yunsfsb,chezxxb faz,chezxxb daoz,diancxxb\n" + 
	    		 "where  aa.pinzb_id=pinzb.id and aa.yunsfsb_id=yunsfsb.id\n" + 
	    		 "and faz.id=aa.faz_id and aa.daoz_id=daoz.id and diancxxb.id=aa.diancxxb_id";
	    	 ResultSet rs1=con.getResultSet(sql);
	    	 while(rs1.next()){
	    		 String pinz=rs1.getString("pinz");
	    		 String yunsfs=rs1.getString("yunsfs");
	    		 String faz=rs1.getString("faz");
	    		 String daoz=rs1.getString("daoz");
	    		 String shouhr=rs1.getString("shouhr");
	    		 long id=rs1.getLong("id");
	    		 long Y1=rs1.getLong("Y1");
	    		 long Y2=rs1.getLong("Y2");
	    		 long Y3=rs1.getLong("Y3");
	    		 long Y4=rs1.getLong("Y4");
	    		 long Y5=rs1.getLong("Y5");
	    		 long Y6=rs1.getLong("Y6");
	    		 long Y7=rs1.getLong("Y7");
	    		 long Y8=rs1.getLong("Y8");
	    		 long Y9=rs1.getLong("Y9");
	    		 long Y10=rs1.getLong("Y10");
	    		 long Y11=rs1.getLong("Y11");
	    		 long Y12=rs1.getLong("Y12");
	    		 long hej=Y1+Y2+Y3+Y4+Y5+Y6+Y7+Y8+Y9+Y10+Y11+Y12;
	    		 list.add(new Fahxxbean(id,pinz,yunsfs,faz,daoz,shouhr,hej,Y1,Y2,Y3,Y4,Y5,Y6,Y7,Y8,Y9,Y10,Y11,Y12));
	    	 }
	    	//质量信息
	    	 list= geteditValuesz();
	    	 list.clear();
	    	 sql=
	    		 "select hetzlb.id,zhibb.mingc zhib,tiaojb.mingc tiaoj,shangx,xiax,danwb.mingc danw\n" +
	    		 "from hetzlb,zhibb,tiaojb,danwb\n" + 
	    		 "where hetzlb.zhibb_id=zhibb.id and hetzlb.tiaojb_id=tiaojb.id and hetzlb.danwb_id=danwb.id and hetzlb.hetb_id="+hetmb_id;
	    	 ResultSet rs2=con.getResultSet(sql);
	    	 int i=0;
	    	 while(rs2.next()){
	    		 String zhib=rs2.getString("zhib");
	    		 String tiaoj=rs2.getString("tiaoj");
	    		 double shangx=rs2.getDouble("shangx");
	    		 double xiax=rs2.getDouble("xiax");
	    		 String danw=rs2.getString("danw");
	    		 long id=rs2.getLong("id");
	    		 list.add(new Zhilyqbean(++i,id,zhib,tiaoj,shangx,xiax,danw));
	    	 }
//	    	价格信息
	    	 list = geteditValuesg();
	    	 list.clear();
	    	 sql=
	    		 "select hetjgb.ID,zhibb.mingc zhibmc,tiaojb.mingc tiaojmc,SHANGX,XIAX,zhibdw.mingc zhibdwmc,JIJ,jijdw.mingc jijdwmc,\n" +
	    		 "hetjjfsb.mingc hetjjfsmc ,hetjsfsb.mingc hetjsfsmc,hetjsxsb.mingc hetjsxsmc,YUNJ,yunjdw.mingc yunjdwmc,decode(YINGDKF,1,'是',2,'否','')YINGDKF,\n" + 
	    		 "yunsfsb.mingc yunsfsmc,ZUIGMJ\n" + 
	    		 "from hetjgb,zhibb,tiaojb,danwb zhibdw,danwb jijdw,hetjsfsb,hetjsxsb,hetjjfsb,danwb yunjdw,yunsfsb\n" + 
	    		 "where hetjgb.zhibb_id=zhibb.id and hetjgb.tiaojb_id=tiaojb.id and hetjgb.danwb_id=zhibdw.id\n" + 
	    		 "and hetjgb.jijdwid=jijdw.id and hetjgb.hetjsfsb_id=hetjsfsb.id(+) and hetjgb.hetjsxsb_id=hetjsxsb.id(+)\n" + 
	    		 "and hetjgb.yunjdw_id=yunjdw.id(+) and hetjgb.yunsfsb_id=yunsfsb.id(+) and hetjgb.hetjjfsb_id=hetjjfsb.id(+) and hetjgb.hetb_id="+hetmb_id;
	    	 ResultSet rs3=con.getResultSet(sql);
	    	 i=0;
	    	 while(rs3.next()){
	    		 long id=rs3.getLong("id");
	    		 String zhibmc=rs3.getString("zhibmc");
	    		 String tiaojmc=rs3.getString("tiaojmc");
	    		 double XIAX=rs3.getDouble("XIAX");
	    		 double SHANGX=rs3.getDouble("SHANGX");
	    		 String zhibdwmc=rs3.getString("zhibdwmc");
	    		 double JIJ=rs3.getDouble("JIJ");
	    		 String jijdwmc=rs3.getString("jijdwmc");
	    		 String hetjjfsmc=rs3.getString("hetjjfsmc");
	    		 String hetjsfsmc=rs3.getString("hetjsfsmc");
	    		 String hetjsxsmc=rs3.getString("hetjsxsmc");
	    		 double YUNJ=rs3.getDouble("YUNJ");
	    		 String yunjdwmc=rs3.getString("yunjdwmc");
	    		 String YINGDKF=rs3.getString("YINGDKF");
	    		 String yunsfsmc=rs3.getString("yunsfsmc");
	    		 double ZUIGMJ=rs3.getDouble("ZUIGMJ");
	    		 list.add(new jijbean(++i,id,zhibmc,tiaojmc,SHANGX,XIAX,zhibdwmc,JIJ,jijdwmc,hetjsfsmc,hetjjfsmc,hetjsxsmc,YUNJ,
	    				 yunjdwmc ,YINGDKF,yunsfsmc,ZUIGMJ));
	    	 }
//	    	增扣款信息
	    	 list = geteditValuesj();
	    	 list.clear();
	    	 sql=
	    		 "select z.ID,zhibb.mingc zhibmc,tiaojb.mingc tiaojmc,z.SHANGX,z.XIAX,zhibdw.mingc zhibdwmc,JIS,jisdw.mingc jisdwmc,\n" + 
	    		 "KOUJ,koujdw.mingc koujdwmc,ZENGFJ,zengfjdw.mingc zengfjdwmc,\n" + 
	    		 "decode(XIAOSCL,1,'进位',2,'舍去',3,'四舍五入',4,'四舍五入(0.1)',5,'四舍五入(0.01)','')XIAOSCL,JIZZKJ,JIZZB\n" + 
	    		 ",CANZXM.Mingc canzxmmc,CANZXMDW.Mingc canzxmdwmc,CANZSX,CANZXX,hetjsxsb.mingc hetjsxsmc,yunsfsb.mingc yunsfsmc,z.BEIZ\n" + 
	    		 "from hetzkkb z,zhibb,tiaojb,danwb zhibdw,danwb jisdw,danwb koujdw,danwb zengfjdw,zhibb canzxm,danwb canzxmdw,hetjsxsb,yunsfsb\n" + 
	    		 "where z.zhibb_id=zhibb.id  and z.tiaojb_id=tiaojb.id and z.danwb_id=zhibdw.id and z.jisdwid=jisdw.id(+) and z.koujdw=koujdw.id(+)\n" + 
	    		 "and z.zengfjdw=zengfjdw.id(+) and z.canzxm=canzxm.id(+) and z.canzxmdw=canzxmdw.id(+) and z.hetjsxsb_id=hetjsxsb.id(+)\n" + 
	    		 "and z.yunsfsb_id=yunsfsb.id(+) and z.hetb_id="+hetmb_id;
	    	 ResultSet rs5=con.getResultSet(sql);
	    	 i=0;
	    	 while(rs5.next()){
	    		 long id=rs5.getLong("id");
	    		 String zhibmc=rs5.getString("zhibmc");
	    		 String tiaojmc=rs5.getString("tiaojmc");
	    		 double XIAX=rs5.getDouble("XIAX");
	    		 double SHANGX=rs5.getDouble("SHANGX");
	    		 String zhibdwmc=rs5.getString("zhibdwmc");
	    		 double JIS=rs5.getDouble("JIS");
	    		 String jisdwmc=rs5.getString("jisdwmc");
	    		 double KOUJ=rs5.getDouble("KOUJ");
	    		 String koujdwmc=rs5.getString("koujdwmc");
	    		 double ZENGFJ=rs5.getDouble("ZENGFJ");
	    		 String zengfjdwmc=rs5.getString("zengfjdwmc");
	    		 String XIAOSCL=rs5.getString("XIAOSCL");
	    		 double JIZZKJ=rs5.getDouble("JIZZKJ");
	    		 double JIZZB=rs5.getDouble("JIZZB");
	    		 String canzxmmc=rs5.getString("canzxmmc");
	    		 String canzxmdwmc=rs5.getString("canzxmdwmc");
	    		 double CANZSX=rs5.getDouble("CANZSX");
	    		 double CANZXX=rs5.getDouble("CANZXX");
	    		 String hetjsxsmc=rs5.getString("hetjsxsmc");
	    		 String yunsfsmc=rs5.getString("yunsfsmc");
	    		 String BEIZ=rs5.getString("BEIZ");
	    		 list.add(new Zengkkbean(id,++i,zhibmc,tiaojmc,SHANGX,XIAX,zhibdwmc,JIS,jisdwmc,KOUJ,koujdwmc,ZENGFJ,zengfjdwmc,
	    				 XIAOSCL ,JIZZKJ,JIZZB,canzxmmc,canzxmdwmc,CANZSX,CANZXX,hetjsxsmc,yunsfsmc,BEIZ));
	    	 }

	    	//文字信息
	    	 sql= "select id,wenznr\n" +
	    	 "from hetwzb\n" + 
	    	 "where hetb_id="+hetmb_id;
	    	 ResultSet rs4=con.getResultSet(sql);
	    	 if(rs4.next()){
	    		 setWenz(rs4.getString("wenznr"));
	    	 }
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		 con.Close();
    	}
	 }
	    private void getGongf(){
	    	Hetxxbean htxxbean = gethetxxbean();
	    	JDBCcon con=new JDBCcon(); 
			String sql = 
				"select quanc,danwdz,YOUZBM,shuih,FADDBR,WEITDLR,KAIHYH,ZHANGH,DIANH\n" +
				"from gongysb where id="+getgongfValue().getId();
			ResultSet rs = con.getResultSet(sql);
			try {
				if (rs.next()) {
					String Gongfdn = rs.getString("DIANH");
					String Gongfdwdz = rs.getString("danwdz");
					String Gongfdwmc = rs.getString("quanc");
					String Gongffddbr = rs.getString("FADDBR");
					String Gongfzh = rs.getString("ZHANGH");
					String Gongfkhyh = rs.getString("KAIHYH");
					String Gongfyzbm = rs.getString("YOUZBM");
					String Gongfwtdlr = rs.getString("WEITDLR");
					String shuih = rs.getString("shuih");
					htxxbean.setGONGFDH(Gongfdn);
					htxxbean.setGONGFDWDZ(Gongfdwdz);
					htxxbean.setGONGFDWMC(Gongfdwmc);
					htxxbean.setGONGFFDDBR(Gongffddbr);
					htxxbean.setGONGFZH(Gongfzh);
					htxxbean.setGONGFKHYH(Gongfkhyh);
					htxxbean.setGONGFYZBM(Gongfyzbm);
					htxxbean.setGONGFWTDLR(Gongfwtdlr);
					htxxbean.setGongfsh(shuih);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			con.Close();
//			发货人同时相同
			setshijgfSelectValue(getIDropDownBean(getshijgfSelectModel(),getgongfValue().getId()));
			getMeiksselectModels();
	    }
	    private void getXuf(){
	    	Hetxxbean htxxbean = gethetxxbean();
	    	JDBCcon con=new JDBCcon(); 
	    	String sql="select quanc,diz,YOUZBM,shuih,FADDBR,WEITDLR,KAIHYH,ZHANGH,DIANH\n" +
	    	"from diancxxb where id="+getxufValue().getId();
			ResultSet rs = con.getResultSet(sql);
			try {
				if (rs.next()) {
					String XUFDH = rs.getString("DIANH");
					String XUFDWDZ = rs.getString("DIZ");
					String XUFDWMC = rs.getString("QUANC");
					String XUFFDDBR = rs.getString("FADDBR");
					String XUFZH = rs.getString("ZHANGH");
					String XUFKHYH = rs.getString("KAIHYH");
					String XUFYZBM = rs.getString("YOUZBM");
					String XUFWTDLR = rs.getString("WEITDLR");
					htxxbean.setXUFDH(XUFDH);
					htxxbean.setXUFDWDZ(XUFDWDZ);
					htxxbean.setXUFDWMC(XUFDWMC);
					htxxbean.setXUFFDDBR(XUFFDDBR);
					htxxbean.setXUFZH(XUFZH);
					htxxbean.setXUFKHYH(XUFKHYH);
					htxxbean.setXUFYZBM(XUFYZBM);
					htxxbean.setXUFWTDLR(XUFWTDLR);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			con.Close();
	    }
	    private void getXuanzht(){
	    	 if(gethetSelectValue().getId()==-1){
	    		 Xinj();
	    	 }else{
	    		 Dak(gethetSelectValue().getId());
	    		
	    	 }
	    }
		private void Shanc(){
			String sql="";
			JDBCcon con=new JDBCcon();
			long hetb_id=gethetSelectValue().getId();
			//删除合同信息表
			con.setAutoCommit(false);
			try{
				sql=
				"delete\n" +
				"from hetb\n" + 
				"where id="+hetb_id;
				con.getDelete(sql);
				sql=
					"delete\n" +
					"from hetslb\n" + 
					"where hetslb.hetb_id="+hetb_id;
				con.getDelete(sql);
				sql=
					"delete \n" +
					"from hetzlb\n" + 
					"where hetzlb.hetb_id="+hetb_id;
				con.getDelete(sql);
				sql=
					"delete \n" +
					"from hetjgb\n" + 
					"where hetjgb.hetb_id="+hetb_id;
				con.getDelete(sql);
				sql=
					"delete\n" +
					"from hetwzb\n" + 
					"where hetwzb.hetb_id="+hetb_id;
				con.getDelete(sql);
				con.commit();
			
			}catch(Exception e){
				e.printStackTrace();
				con.rollBack();
			}
			 Xinj();
			 gethetSelectModels();
		}
		private void Baoc(){
		    JDBCcon con = new JDBCcon();
		    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		    Hetxxbean bean=gethetxxbean();
		    String sql="";
		    if(gethetSelectValue().getId()==-1){//如果是新合同
	//	    	插入（新增）保存
	//	    	保存合同信息（包括供方需方）
		    	long hetb_id =Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
				sql=
					"insert into hetb(ID,DIANCXXB_ID,HETBH,QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR,GONGFWTDLR," +
					"GONGFDBGH,GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR,XUFDH,XUFDBGH,XUFKHYH,XUFZH," +
					"XUFYZBM,XUFSH,HETGYSBID,GONGYSB_ID,QISRQ,GUOQRQ,JIHKJB_ID,hetb_mb_id,liucgzid,leib,meikmcs)\n" +
					"values(" 
					 +hetb_id
					 +","
					 + getxufValue().getId()
					 +",'"
					 +bean.getHetbh()
					 +"',to_date('"
					 +format.format(bean.getQiandsj())
					 +"','YYYY-MM-DD'),'"
					 +bean.getQianddd()
					 +"','"
					 +bean.getGONGFDWMC()
					 +"','"
					 +bean.getGONGFDWDZ()
					 +"','"
					 +bean.getGONGFDH()
					 +"','"
					 +bean.getGONGFFDDBR()
					 +"','"
					 +bean.getGONGFWTDLR()
					 +"','"
					 +bean.getGONGFDBGH()
					 +"','"
					 +bean.getGONGFKHYH()
					 +"','"
					 +bean.getGONGFZH()
					 +"','"
					 +bean.getGONGFYZBM()
					 +"','"
					 +bean.getGongfsh()
					 +"','"
					 +bean.getXUFDWMC()
					 +"','"
					 +bean.getXUFDWDZ()
					 +"','"
					 +bean.getXUFFDDBR()
					 +"','"
					 +bean.getXUFWTDLR()
					 +"','"
					 +bean.getXUFDH()
					 +"','"
					 +bean.getXUFDBGH()
					 +"','"
					 +bean.getXUFKHYH()
					 +"','"
					 +bean.getXUFZH()
					 +"','"
					 +bean.getXUFYZBM()
					 +"','"
					 +bean.getXufsh()
					 +"',"
					 +getgongfValue().getId()
					 +","
					 +getshijgfSelectValue().getId()
					 +",to_date('"
					 +format.format(bean.getShengxsj())
					 +"','YYYY-MM-DD'),to_date('"
					 +format.format(bean.getGuoqsj())
					 +"','YYYY-MM-DD'),"
					 +getJihxzValue().getId()
					 +","
					 +getmobmcSelectValue().getId()
//					 +","
//					 +0//结算形式和结算方式表有待于拓展暂不考虑
//					 +","
//					 +0
//					 +","
//					 +0
					 +","
					 +0//流程跟踪表id
					 +","
					 +1//类别1为区域分公司
					 +",'"
					 + getFahr()
					 +"')";
				con.getInsert(sql);
				bean.setId(hetb_id);
				//保存数量
				 List list=geteditValuess();
				 long[] Y=new long[12];
				 for (int i = 0; i < list.size(); i++) {// 遍历容器
		             String Pinz = ((Fahxxbean) list.get(i)).getPinz();
		             String Yunsfs = ((Fahxxbean) list.get(i)).getYunsfs();
		             String Faz = ((Fahxxbean) list.get(i)).getFaz();
		             String Daoz=((Fahxxbean) list.get(i)).getDaoz();
		             String Shouhr = ((Fahxxbean) list.get(i)).getShouhr();
		             Shouhr=Shouhr.substring(Shouhr.lastIndexOf(";")+1,Shouhr.length());
		              ((Fahxxbean) list.get(i)).setShouhr(Shouhr);
		              Y[0]=((Fahxxbean) list.get(i)).getY1();
		              Y[1]=((Fahxxbean) list.get(i)).getY2();
		              Y[2]=((Fahxxbean) list.get(i)).getY3();
		              Y[3]=((Fahxxbean) list.get(i)).getY4();
		              Y[4]=((Fahxxbean) list.get(i)).getY5();
		              Y[5]=((Fahxxbean) list.get(i)).getY6();
		              Y[6]=((Fahxxbean) list.get(i)).getY7();
		              Y[7]= ((Fahxxbean) list.get(i)).getY8();
		              Y[8]=((Fahxxbean) list.get(i)).getY9();
		              Y[9]=((Fahxxbean) list.get(i)).getY10();
		              Y[10]=((Fahxxbean) list.get(i)).getY11();
		              Y[11]= ((Fahxxbean) list.get(i)).getY12();
		         	long hetslb_id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
		             for(int j=0;j<12;j++){
		            	 if(Y[j]!=0){
		            		 sql =
			                	 "insert into hetslb(ID,PINZB_ID,YUNSFSB_ID,FAZ_ID,DAOZ_ID,DIANCXXB_ID,RIQ,HETL,HETB_ID)values(" +
			                	 +hetslb_id
			                	 +",(select id from pinzb where mingc='"
			                	 +Pinz
			                	 +"'),(select id from yunsfsb where mingc='"
			                	 +Yunsfs
			                	 +"'),(select id from chezxxb where mingc='"
			                	 +Faz
			                	 +"'),(select id from chezxxb where mingc='"
			                	 +Daoz
			                	 +"'),(select id from diancxxb where mingc='"
			                	 +Shouhr
			                	 +"'),to_date('"
			                	 +String.valueOf(bean.getShengxsj().getYear()+1900)+(j+1)
			                	 +"','YYYYMM'),"
			                	 +Y[j]
			                	 +","
			                	 +hetb_id
			                	 +")";
			                 con.getInsert(sql);
		            	 }
		             }
		         }
				//保存质量
				list= geteditValuesz();
		         for (int i = 0; i < list.size(); i++) {// 遍历容器
		        	 long hetzlb_id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
		             String zhib = ((Zhilyqbean) list.get(i)).getMingc();
		             String tiaoj = ((Zhilyqbean) list.get(i)).getTiaoj();
		             double shangx = ((Zhilyqbean) list.get(i)).getShangx();
		             double xiax=((Zhilyqbean) list.get(i)).getXiax();
		             String danw = ((Zhilyqbean) list.get(i)).getDanw();
	                 sql="insert into hetzlb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,HETB_ID)values("
	    			 +hetzlb_id
	    			 +",(select id from zhibb where mingc='"
	    			 +zhib
	    			 +"'),(select id from tiaojb where mingc='"
	    			 +tiaoj
	    			 +"'),"
	    			 +shangx
	    			 +","
	    			 +xiax
	    			 +",(select max(id) from danwb where mingc='"
	    			 +danw
	                 +"'),"
	                 +hetb_id
	                 +")";
	                 con.getInsert(sql);
		         }
				//保存价格
		        list = geteditValuesg();
		         for (int i = 0; i < list.size(); i++) {// 遍历容器
		             String zhibb_id = ((jijbean) list.get(i)).getZhibb_id();
		             String tiaojb_id = ((jijbean) list.get(i)).getTiaojb_id();
		             double shangx = ((jijbean) list.get(i)).getShangx();
		             double xiax=((jijbean) list.get(i)).getXiax();
		             String zhibdw = ((jijbean) list.get(i)).getDanwb_id();
		             double jij = ((jijbean) list.get(i)).getJij();
		             String jijdw = ((jijbean) list.get(i)).getJijdwid();
		             String hetjsfsb_id = ((jijbean) list.get(i)).getHetjsfsb_id();
		             String Hetjsxsb_id = ((jijbean) list.get(i)).getHetjsxsb_id();
		             double yunj = ((jijbean) list.get(i)).getYunj();
		             String yunjdw = ((jijbean) list.get(i)).getYunjdw_id();
		             String yingdkf = ((jijbean) list.get(i)).getYingdkf();
		             String Yunsfsb_id = ((jijbean) list.get(i)).getYunsfsb_id();
		             double zuigmj = ((jijbean) list.get(i)).getZuigmj();
		             String hetjjfsb_id = ((jijbean) list.get(i)).getHetjjfsb_id();
		             sql=
		            	 "insert into hetjgb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIJ,JIJDWID,HETJSFSB_ID,HETJSXSB_ID,YUNJ,YUNJDW_ID,\n" +
		            	 "YINGDKF,YUNSFSB_ID,ZUIGMJ,HETB_ID,HETJJFSB_ID) values(" 
		            	 +"xl_xul_id.nextval"
		            	 +",(select id from zhibb where mingc='"
		            	 +zhibb_id
		            	 +"'),(select id from tiaojb where mingc='"
		            	 +tiaojb_id
		            	 +"'),"
		            	 +shangx
		            	 +","
		            	 +xiax
		            	 +",(select max(id) from danwb where mingc='"
		            	 +zhibdw
		            	 +"'),"
		            	 +jij
		            	 +",(select max(id) from danwb where mingc='"
		            	 +jijdw
		            	 +"'),(select id from hetjsfsb where mingc='"
		            	 +hetjsfsb_id
		            	 +"'),(select id from hetjsxsb where mingc='"
		            	 +Hetjsxsb_id
		            	 +"'),"
		            	 +yunj
		            	 +",(select max(id) from danwb where mingc='"
		            	 +yunjdw
		            	 +"'),"
		            	 +getProperId(getyingdkfgSelectModel(),yingdkf)
		            	 +",(select id from yunsfsb where mingc='"
		            	 +Yunsfsb_id
		            	 +"'),"
		            	 +zuigmj
		            	 +","
		            	 +hetb_id
		            	 +",(select id from hetjjfsb where mingc='"
		            	 +hetjjfsb_id
		            	 +"'))";
	                 con.getInsert(sql);
		         }
		         //	保存增扣价格
			        list = geteditValuesj();
			         for (int i = 0; i < list.size(); i++) {// 遍历容器
			             String zhibb_id = ((Zengkkbean) list.get(i)).getZHIBB_ID();
			             String tiaojb_id = ((Zengkkbean) list.get(i)).getTIAOJB_ID();
			             double shangx=((Zengkkbean) list.get(i)).getSHANGX();
			             double xiax = ((Zengkkbean) list.get(i)).getXIAX();
			             String zhibdw = ((Zengkkbean) list.get(i)).getDANWB_ID();
			             double jis = ((Zengkkbean) list.get(i)).getJIS();
			             String jisdwb_id = ((Zengkkbean) list.get(i)).getJISDWID();
			             double kouj = ((Zengkkbean) list.get(i)).getKOUJ();
			             String  koujdw = ((Zengkkbean) list.get(i)).getKOUJDW();
			             double zengfj = ((Zengkkbean) list.get(i)).getZENGFJ();
			             String zengfjdw = ((Zengkkbean) list.get(i)).getZENGFJDW();
			             String xiaoscl = ((Zengkkbean) list.get(i)).getXIAOSCL();
			             double jizzb = ((Zengkkbean) list.get(i)).getJIZZB();
			             double jizzkj = ((Zengkkbean) list.get(i)).getJIZZKJ();
			             String canzxm = ((Zengkkbean) list.get(i)).getCANZXM();
			             String canzxmdw = ((Zengkkbean) list.get(i)).getCANZXMDW();
			             double canzsx = ((Zengkkbean) list.get(i)).getCANZSX();
			             double canzxx = ((Zengkkbean) list.get(i)).getCANZXX();
			             String hetjsxsb_id = ((Zengkkbean) list.get(i)).getJIESXXB_ID();
			             String yunsfsb_id = ((Zengkkbean) list.get(i)).getYUNSFSB_ID();
			             String beiz = ((Zengkkbean) list.get(i)).getBEIZ();
			             sql="insert into hetzkkb( ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIS,JISDWID,KOUJ,KOUJDW,ZENGFJ," +
			             		"ZENGFJDW,XIAOSCL,JIZZKJ,JIZZB,CANZXM,CANZXMDW,CANZSX,CANZXX,HETJSXSB_ID,YUNSFSB_ID,BEIZ,HETB_ID)" +
			             		"values(" 
			             		 +"xl_xul_id.nextval"
			             		 +",(select id from zhibb where mingc='"
				            	 +zhibb_id
				            	 +"'),(select id from tiaojb where mingc='"
				            	 +tiaojb_id
				            	 +"'),"
				            	 +shangx
				            	 +","
				            	 +xiax
				            	 +",(select max(id) from danwb where mingc='"
				            	 +zhibdw
				            	 +"'),"
				            	 +jis
				            	 +",(select max(id) from danwb where mingc='"
				            	 +jisdwb_id
				            	 +"'),"
				            	 +kouj
				            	 +",(select max(id) from danwb where mingc='"
				            	 +koujdw
				            	 +"'),"
				            	 +zengfj
				            	 +",(select max(id) from danwb where mingc='"
				            	 +zengfjdw
				            	 +"'),"
				            	 + getProperId(getxiaoswcljSelectModel(),xiaoscl)
				            	 +","
				            	 +jizzkj
				            	 +","
				            	 +jizzb
				            	 +",(select id from zhibb where mingc='"
				            	 +canzxm
				            	 +"'),(select max(id) from danwb where mingc='"
				            	 +canzxmdw
				            	 +"'),"
				            	 +canzsx
				            	 +","
				            	 +canzxx
				            	 +",(select id from hetjsxsb where mingc='"
				            	 +hetjsxsb_id
				            	 +"'),(select id from yunsfsb where mingc='"
				            	 +yunsfsb_id
				            	 +"'),'"
				            	 +beiz
				            	 +"',"
				            	 +hetb_id
			             		 +")";
		                 con.getInsert(sql);
			         }
				//保存文字
		         long hetwzb_id =Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
		         sql=
		        	 "insert into hetwzb(id,wenznr,hetb_id)values("
		        	 +hetwzb_id
		        	 +",'"
		        	 +getWenz()
		        	 +"',"
		        	 +hetb_id
		        	 +")";
		         con.getInsert(sql);
		         //保存后改变合同下拉框，并使选择框定位到新合同名称
		         gethetSelectModels();
		         sethetSelectValue(getIDropDownBean(gethetSelectModel(),hetb_id));
		    }else{//如果不是新建合同则是已经存在的合同，这时进行更新操作
		    	//保存合同信息（包括供方需方）
		    	long hetb_id=gethetSelectValue().getId();
		    	sql="update hetb set HETBH='" + bean.getHetbh()
		    	+"',QIANDRQ=to_date('" + format.format(bean.getQiandsj())
		    	+"','YYYY-MM-DD'),QIANDDD='" + bean.getQianddd()
		    	+"',GONGFDWMC='" + bean.getGONGFDWMC()
		    	+"',GONGFDWDZ='" + bean.getGONGFDWDZ()
		    	+"',GONGFDH='" + bean.getGONGFDH()
		    	+"',GONGFFDDBR='" + bean.getGONGFFDDBR()
		    	+"',GONGFWTDLR='" + bean.getGONGFWTDLR()
		    	+"',GONGFDBGH='" + bean.getGONGFDBGH()
		    	+"',GONGFKHYH='" + bean.getGONGFKHYH()
		    	+"',GONGFZH='" + bean.getGONGFZH()
		    	+"',GONGFYZBM='" + bean.getGONGFYZBM()
		    	+"',GONGFSH='" + bean.getGongfsh()
		    	+"',XUFDWMC='" + bean.getXUFDWMC()
		    	+"',XUFDWDZ='" + bean.getXUFDWDZ()
		    	+"',XUFFDDBR='" + bean.getXUFFDDBR()
		    	+"',XUFWTDLR='" + bean.getXUFWTDLR()
		    	+"',XUFDH='" + bean.getXUFDH()
		    	+"',XUFDBGH='" + bean.getXUFDBGH()
		    	+"',XUFKHYH='" + bean.getXUFKHYH()
		    	+"',XUFZH='" + bean.getXUFZH()
		    	+"',XUFYZBM='" + bean.getXUFYZBM()
		    	+"',XUFSH='" + bean.getXufsh()
		    	+"',HETGYSBID=" + getgongfValue().getId()
		    	+",GONGYSB_ID=" + getshijgfSelectValue().getId()
		    	+",QISRQ=to_date('" + format.format(bean.getShengxsj())
		    	+"','YYYY-MM-DD'),GUOQRQ=to_date('" + format.format(bean.getGuoqsj())
		    	+"','YYYY-MM-DD'),JIHKJB_ID=" + getJihxzValue().getId()
//		    	+",HETJSFSB_ID=" +0// 暂时为空
//		    	+",HETJSXSB_ID=" + 0
//		    	+",HETYJ=" + 0
//		    	+",MINGC='"+getmobmc()
		    	+",diancxxb_id="
		    	+getxufValue().getId()
		    	+",meikmcs='"+ getFahr()
		    	+"' where id="+hetb_id;
		    	con.getUpdate(sql);

		    	//保存数量
		    	List list=geteditValuess();
				 long[] Y=new long[12];
				 for (int i = 0; i < list.size(); i++) {// 遍历容器
					 long hetslb_id=((Fahxxbean) list.get(i)).getId();
		              String Pinz = ((Fahxxbean) list.get(i)).getPinz();
		              String Yunsfs = ((Fahxxbean) list.get(i)).getYunsfs();
		              String Faz = ((Fahxxbean) list.get(i)).getFaz();
		              String Daoz=((Fahxxbean) list.get(i)).getDaoz();
		              String Shouhr = ((Fahxxbean) list.get(i)).getShouhr();
		              Shouhr=Shouhr.substring(Shouhr.lastIndexOf(";")+1,Shouhr.length());
		              ((Fahxxbean) list.get(i)).setShouhr(Shouhr);
		              Y[0]=((Fahxxbean) list.get(i)).getY1();
		              Y[1]=((Fahxxbean) list.get(i)).getY2();
		              Y[2]=((Fahxxbean) list.get(i)).getY3();
		              Y[3]=((Fahxxbean) list.get(i)).getY4();
		              Y[4]=((Fahxxbean) list.get(i)).getY5();
		              Y[5]=((Fahxxbean) list.get(i)).getY6();
		              Y[6]=((Fahxxbean) list.get(i)).getY7();
		              Y[7]= ((Fahxxbean) list.get(i)).getY8();
		              Y[8]=((Fahxxbean) list.get(i)).getY9();
		              Y[9]=((Fahxxbean) list.get(i)).getY10();
		              Y[10]=((Fahxxbean) list.get(i)).getY11();
		              Y[11]= ((Fahxxbean) list.get(i)).getY12();
		              if(hetslb_id==0){
		            	  hetslb_id =Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
			              for(int j=0;j<12;j++){
			            	 if(Y[j]!=0){
			            		 sql =
				                	 "insert into hetslb(ID,PINZB_ID,YUNSFSB_ID,FAZ_ID,DAOZ_ID,DIANCXXB_ID,RIQ,HETL,HETB_ID)values(" +
				                	 +hetslb_id
				                	 +",(select id from pinzb where mingc='"
				                	 +Pinz
				                	 +"'),(select id from yunsfsb where mingc='"
				                	 +Yunsfs
				                	 +"'),(select id from chezxxb where mingc='"
				                	 +Faz
				                	 +"'),(select id from chezxxb where mingc='"
				                	 +Daoz
				                	 +"'),(select id from diancxxb where mingc='"
				                	 +Shouhr
				                	 +"'),to_date('"
				                	 +String.valueOf(bean.getShengxsj().getYear()+1900)+(j+1)
				                	 +"','YYYYMM'),"
				                	 +Y[j]
				                	 +","
				                	 +hetb_id
				                	 +")";
				                 con.getInsert(sql);
			            	 }
			             }
		              }else{//对于非新增的记录
		            	  sql=
		            		  "delete\n" +
		            		  "from hetslb\n" + 
		            		  "where id="+hetslb_id;
		            	  con.getDelete(sql);
		            	  for(int j=0;j<12;j++){
		            		  if(Y[j]!=0){
			            		 sql =
				                	 "insert into hetslb(ID,PINZB_ID,YUNSFSB_ID,FAZ_ID,DAOZ_ID,DIANCXXB_ID,RIQ,HETL,HETB_ID)values(" +
				                	 +hetslb_id
				                	 +",(select id from pinzb where mingc='"
				                	 +Pinz
				                	 +"'),(select id from yunsfsb where mingc='"
				                	 +Yunsfs
				                	 +"'),(select id from chezxxb where mingc='"
				                	 +Faz
				                	 +"'),(select id from chezxxb where mingc='"
				                	 +Daoz
				                	 +"'),(select id from diancxxb where mingc='"
				                	 +Shouhr
				                	 +"'),to_date('"
				                	 +String.valueOf(bean.getShengxsj().getYear()+1900)+(j+1)
				                	 +"','YYYYMM'),"
				                	 +Y[j]
				                	 +","
				                	 +hetb_id
				                	 +")";
				                 con.getInsert(sql);
				            }
				          }
		              }
		         	  
		         }
		    	//保存质量
				 list= geteditValuesz();
		         for (int i = 0; i < list.size(); i++) {// 遍历容器
		        	 long hetzlb_id = ((Zhilyqbean) list.get(i)).getId(); //
		             String zhib = ((Zhilyqbean) list.get(i)).getMingc();
		             String tiaoj = ((Zhilyqbean) list.get(i)).getTiaoj();
		             double shangx = ((Zhilyqbean) list.get(i)).getShangx();
		             double xiax=((Zhilyqbean) list.get(i)).getXiax();
		             String danw = ((Zhilyqbean) list.get(i)).getDanw();
		             if(hetzlb_id==0){
		            	 hetzlb_id=Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
		            	 sql="insert into hetzlb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,HETB_ID)values("
			    			 +hetzlb_id
			    			 +",(select id from zhibb where mingc='"
			    			 +zhib
			    			 +"'),(select id from tiaojb where mingc='"
			    			 +tiaoj
			    			 +"'),"
			    			 +shangx
			    			 +","
			    			 +xiax
			    			 +",(select max(id) from danwb where mingc='"
			    			 +danw
			                 +"'),"
			                 +hetb_id
			                 +")";
			                 con.getInsert(sql);
			                 ((Zhilyqbean) list.get(i)).setId(hetzlb_id);
		             }else{
		            	 sql=
		            		 "update hetzlb " 
		            		 +"set zhibb_id=(select id from zhibb where mingc='" + zhib
		            		 +"'),tiaojb_id=(select id from tiaojb where mingc='" +tiaoj
		            		 +"'),shangx=" + shangx
		            		 +",xiax=" + xiax
		            		 +",danwb_id=(select max(id) from danwb where mingc='" + danw
		            		 +"')where id="+hetzlb_id;
		            	 con.getUpdate(sql);
		             }
	                
		         } 
//		       保存价格
		         list = geteditValuesg();
		         for (int i = 0; i < list.size(); i++) {// 遍历容器
		        	 	 long hetjgb_id =  ((jijbean) list.get(i)).getId();
		        	  	 String zhibb_id = ((jijbean) list.get(i)).getZhibb_id();
			             String tiaojb_id = ((jijbean) list.get(i)).getTiaojb_id();
			             double shangx = ((jijbean) list.get(i)).getShangx();
			             double xiax=((jijbean) list.get(i)).getXiax();
			             String zhibdw = ((jijbean) list.get(i)).getDanwb_id();
			             double jij = ((jijbean) list.get(i)).getJij();
			             String jijdw = ((jijbean) list.get(i)).getJijdwid();
			             String hetjsfsb_id = ((jijbean) list.get(i)).getHetjsfsb_id();
			             String Hetjsxsb_id = ((jijbean) list.get(i)).getHetjsxsb_id();
			             double yunj = ((jijbean) list.get(i)).getYunj();
			             String yunjdw = ((jijbean) list.get(i)).getYunjdw_id();
			             String yingdkf = ((jijbean) list.get(i)).getYingdkf();
			             String Yunsfsb_id = ((jijbean) list.get(i)).getYunsfsb_id();
			             double zuigmj = ((jijbean) list.get(i)).getZuigmj();
			             String hetjjfsb = ((jijbean) list.get(i)).getHetjjfsb_id();
		             if(hetjgb_id==0){
		            	 hetjgb_id=Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
		            	 sql=
			            	 "insert into hetjgb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIJ,JIJDWID,HETJSFSB_ID,HETJSXSB_ID,YUNJ,YUNJDW_ID,\n" +
			            	 "YINGDKF,YUNSFSB_ID,ZUIGMJ,HETB_ID,HETJJFSB_ID) values(" 
			            	 +"xl_xul_id.nextval"
			            	 +",(select id from zhibb where mingc='"
			            	 +zhibb_id
			            	 +"'),(select id from tiaojb where mingc='"
			            	 +tiaojb_id
			            	 +"'),"
			            	 +shangx
			            	 +","
			            	 +xiax
			            	 +",(select max(id) from danwb where mingc='"
			            	 +zhibdw
			            	 +"'),"
			            	 +jij
			            	 +",(select max(id) from danwb where mingc='"
			            	 +jijdw
			            	 +"'),(select id from hetjsfsb where mingc='"
			            	 +hetjsfsb_id
			            	 +"'),(select id from hetjsxsb where mingc='"
			            	 +Hetjsxsb_id
			            	 +"'),"
			            	 +yunj
			            	 +",(select max(id) from danwb where mingc='"
			            	 +yunjdw
			            	 +"'),"
			            	 +getProperId(getyingdkfgSelectModel(),yingdkf)
			            	 +",(select id from yunsfsb where mingc='"
			            	 +Yunsfsb_id
			            	 +"'),"
			            	 +zuigmj
			            	 +","
			            	 +hetb_id
			            	 +",(select id from hetjjfsb where mingc='"
			            	 +hetjjfsb
			            	 +"'))";
		                 con.getInsert(sql);
		                 ((jijbean) list.get(i)).setId(hetjgb_id);
		             }else{
		            	 sql=
		            		 "update hetjgb set ZHIBB_ID=(select id from zhibb where mingc='"+zhibb_id+
		            		 "'),TIAOJB_ID=(select id from tiaojb where mingc='" + tiaojb_id+
		            		 "'),SHANGX=" + shangx+
		            		 ",XIAX=" + xiax+
		            		 ",DANWB_ID=(select max(id) from danwb where mingc='" + zhibdw+
		            		 "'),JIJ=" +jij+ 
		            		 ",JIJDWID=(select max(id) from danwb where mingc='" +jijdw+
		            		 "'),HETJSFSB_ID=(select id from hetjsfsb where mingc='" +hetjsfsb_id+ 
		            		 "'),HETJSXSB_ID=(select id from hetjsxsb where mingc='" + Hetjsxsb_id+
		            		 "'),YUNJ=" + yunj+
		            		 ",YUNJDW_ID=(select max(id) from danwb where mingc='" + yunjdw+
		            		 "'),YINGDKF=" + getProperId(getyingdkfgSelectModel(),yingdkf)+
		            		 ",YUNSFSB_ID=(select id from yunsfsb where mingc='" + Yunsfsb_id+
		            		 "'),ZUIGMJ=" + zuigmj+
		            		 ",HETJJFSB_ID=(select id from hetjjfsb where mingc='" + hetjjfsb+
		            		 "')where hetjgb.id="+hetjgb_id;
		            	 con.getUpdate(sql);
		             }
		         }
		         //保存增扣款
		         list = geteditValuesj();
		         for (int i = 0; i < list.size(); i++) {// 遍历容器
	        	 	 long hetzkkb_id =  ((Zengkkbean) list.get(i)).getId();
	        	     String zhibb_id = ((Zengkkbean) list.get(i)).getZHIBB_ID();
		             String tiaojb_id = ((Zengkkbean) list.get(i)).getTIAOJB_ID();
		             double shangx=((Zengkkbean) list.get(i)).getSHANGX();
		             double xiax = ((Zengkkbean) list.get(i)).getXIAX();
		             String zhibdw = ((Zengkkbean) list.get(i)).getDANWB_ID();
		             double jis = ((Zengkkbean) list.get(i)).getJIS();
		             String jisdwb_id = ((Zengkkbean) list.get(i)).getJISDWID();
		             double kouj = ((Zengkkbean) list.get(i)).getKOUJ();
		             String  koujdw = ((Zengkkbean) list.get(i)).getKOUJDW();
		             double zengfj = ((Zengkkbean) list.get(i)).getZENGFJ();
		             String zengfjdw = ((Zengkkbean) list.get(i)).getZENGFJDW();
		             String xiaoscl = ((Zengkkbean) list.get(i)).getXIAOSCL();
		             double jizzb = ((Zengkkbean) list.get(i)).getJIZZB();
		             double jizzkj = ((Zengkkbean) list.get(i)).getJIZZKJ();
		             String canzxm = ((Zengkkbean) list.get(i)).getCANZXM();
		             String canzxmdw = ((Zengkkbean) list.get(i)).getCANZXMDW();
		             double canzsx = ((Zengkkbean) list.get(i)).getCANZSX();
		             double canzxx = ((Zengkkbean) list.get(i)).getCANZXX();
		             String hetjsxsb_id = ((Zengkkbean) list.get(i)).getJIESXXB_ID();
		             String yunsfsb_id = ((Zengkkbean) list.get(i)).getYUNSFSB_ID();
		             String beiz = ((Zengkkbean) list.get(i)).getBEIZ();
		             if(hetzkkb_id==0){
		            	 hetzkkb_id=Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
		            	  sql="insert into hetzkkb( ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIS,JISDWID,KOUJ,KOUJDW,ZENGFJ," +
		             		"ZENGFJDW,XIAOSCL,JIZZKJ,JIZZB,CANZXM,CANZXMDW,CANZSX,CANZXX,HETJSXSB_ID,YUNSFSB_ID,BEIZ,HETB_ID)" +
		             		"values(" 
		             		 +"xl_xul_id.nextval"
		             		 +",(select id from zhibb where mingc='"
			            	 +zhibb_id
			            	 +"'),(select id from tiaojb where mingc='"
			            	 +tiaojb_id
			            	 +"'),"
			            	 +shangx
			            	 +","
			            	 +xiax
			            	 +",(select max(id) from danwb where mingc='"
			            	 +zhibdw
			            	 +"'),"
			            	 +jis
			            	 +",(select max(id) from danwb where mingc='"
			            	 +jisdwb_id
			            	 +"'),"
			            	 +kouj
			            	 +",(select max(id) from danwb where mingc='"
			            	 +koujdw
			            	 +"'),"
			            	 +zengfj
			            	 +",(select max(id) from danwb where mingc='"
			            	 +zengfjdw
			            	 +"'),"
			            	 + getProperId(getxiaoswcljSelectModel(),xiaoscl)
			            	 +","
			            	 +jizzkj
			            	 +","
			            	 +jizzb
			            	 +",(select id from zhibb where mingc='"
			            	 +canzxm
			            	 +"'),(select max(id) from danwb where mingc='"
			            	 +canzxmdw
			            	 +"'),"
			            	 +canzsx
			            	 +","
			            	 +canzxx
			            	 +",(select id from hetjsxsb where mingc='"
			            	 +hetjsxsb_id
			            	 +"'),(select id from yunsfsb where mingc='"
			            	 +yunsfsb_id
			            	 +"'),'"
			            	 +beiz
			            	 +"',"
			            	 +hetb_id
		             		 +")";
	                 con.getInsert(sql);
		                 con.getInsert(sql);
		                 ((Zengkkbean) list.get(i)).setId(hetzkkb_id);
		             }else{
		            	 sql=
		            		 "update hetzkkb set ZHIBB_ID=(select id from zhibb where mingc='" +zhibb_id+
		            		 "'),TIAOJB_ID=(select id from tiaojb where mingc='" +tiaojb_id+
		            		 "'),SHANGX=" + shangx+
		            		 ",XIAX=" + xiax+
		            		 ",DANWB_ID=(select max(id) from danwb where mingc='" +zhibdw+ 
		            		 "'),JIS=" + jis+
		            		 ",JISDWID=(select max(id) from danwb where mingc='" +jisdwb_id+
		            		 "'),KOUJ=" + kouj+
		            		 ",KOUJDW=(select max(id) from danwb where mingc='" + koujdw+
		            		 "'),ZENGFJ=" + zengfj+
		            		 ",ZENGFJDW=(select max(id) from danwb where mingc='" + zengfjdw+
		            		 "'),XIAOSCL=" + getProperId(getxiaoswcljSelectModel(),xiaoscl)+
		            		 ",JIZZKJ=" + jizzkj+
		            		 ",JIZZB=" + jizzb+
		            		 ",CANZXM=(select id from zhibb where mingc='" + canzxm+
		            		 "'),CANZXMDW=(select max(id) from danwb where mingc='" + canzxmdw+
		            		 "'),CANZSX=" + canzsx+
		            		 ",CANZXX=" + canzxx+
		            		 ",HETJSXSB_ID=(select id from hetjsxsb where mingc='" + hetjsxsb_id+
		            		 "'),YUNSFSB_ID=(select id from yunsfsb where mingc='" + yunsfsb_id+
		            		 "'),BEIZ='" + beiz+
		            		 "'where id="+hetzkkb_id;
		            	 con.getUpdate(sql);
		             }
		         }
		    	//保存文字
		         sql=
		        	 "update hetwzb\n" +
		        	 "set wenznr='" + getWenz()+
		        	 "'where hetb_id="+hetb_id;
		         con.getUpdate(sql);
		         gethetSelectModels();
		         sethetSelectValue(getIDropDownBean(gethetSelectModel(),hetb_id));
		         bean.setId(hetb_id);
		    }
		}
		private void Inserts(){
			 List _value = geteditValuess();
		     _value.add(new Fahxxbean("",""));//缺省到站,收货人
		}
		private void Deletes(){
			List _value = geteditValuess();
	        if (_editTableRows != -1) {
	            long id = ((Fahxxbean) _value.get(_editTableRows)).getId();
	            if (id != 0) {
	                JDBCcon con = new JDBCcon();
	                String sql = "delete from hetslb where id=" + id;
	                con.getDelete(sql);
	                con.Close();
	            }
	            _value.remove(_editTableRows);
	        }
		}
		private void Insertz(){
			 List _value = geteditValuesz();
		     _value.add(new Zhilyqbean(_value.size() + 1));
		}
		private void Deletez(){
			List _value = geteditValuesz();
	        if (_editTableRowz != -1) {
	            long id = ((Zhilyqbean) _value.get(_editTableRowz)).getId();
	            if (id != 0) {
	                JDBCcon con = new JDBCcon();
	                String sql = "delete from hetzlb where id=" + id;
	                con.getDelete(sql);
	                con.Close();
	            }
	            _value.remove(_editTableRowz);
	            int c = _value.size();
	            for (int a = _editTableRowz; a < c; a++) {
	                ((Zhilyqbean) _value.get(a))
	                        .setXuh(((Zhilyqbean) _value.get(a)).getXuh() - 1);
	            }
	        }
		}
		private void Insertj(){
			 List _value = geteditValuesj();
		     _value.add(new Zengkkbean(_value.size() + 1));
		}
		private void Deletej(){
			List _value = geteditValuesj();
	        if (_editTableRowj != -1) {
	            long id = ((Zengkkbean) _value.get(_editTableRowj)).getId();
	            if (id != 0) {
	                JDBCcon con = new JDBCcon();
	                String sql = "delete from hetzkkb where id=" + id;
	                con.getDelete(sql);
	                con.Close();
	            }
	            _value.remove(_editTableRowj);
	            int c = _value.size();
	            for (int a = _editTableRowj; a < c; a++) {
	                ((Zengkkbean) _value.get(a))
	                        .setXuh(((Zengkkbean) _value.get(a)).getXuh() - 1);
	            }
	        }
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
	    private void Insertg(){
			 List _value = geteditValuesg();
		     _value.add(new jijbean(_value.size() + 1));
		}
		private void Deleteg(){
			List _value = geteditValuesg();
	        if (_editTableRowg != -1) {
	            long id = ((jijbean) _value.get(_editTableRowg)).getId();
	            if (id != 0) {
	                JDBCcon con = new JDBCcon();
	                String sql = "delete from hetjgb where id=" + id;
	                con.getDelete(sql);
	                con.Close();
	            }
	            _value.remove(_editTableRowg);
	            int c = _value.size();
	            for (int a = _editTableRowg; a < c; a++) {
	                ((jijbean) _value.get(a))
	                        .setXuh(((jijbean) _value.get(a)).getXuh() - 1);
	            }
	        }
		}
//	    private String getProperValue(IPropertySelectionModel _selectModel,
//	            int value) {
//	        int OprionCount;
//	        OprionCount = _selectModel.getOptionCount();
//	        for (int i = 0; i < OprionCount; i++) {
//	            if (((IDropDownBean) _selectModel.getOption(i)).getId() == value) {
//	                return ((IDropDownBean) _selectModel.getOption(i)).getValue();
//	            }
//	        }
//	        return null;
//	    }
	    private IDropDownBean getIDropDownBean(IPropertySelectionModel model,long id) {
	        int OprionCount;
	        OprionCount = model.getOptionCount();
	        for (int i = 0; i < OprionCount; i++) {
	            if (((IDropDownBean) model.getOption(i)).getId() == id) {
	                return (IDropDownBean) model.getOption(i);
	            }
	        }
	        return null;
	    }
		private void gethetmb(long hetmb_id){
			 String sql="";
	    	 JDBCcon con=new JDBCcon();
	    try{
	    	//合同信息
	         sql=
	        	"select ID,HETBH,QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR," +
	        	"GONGFWTDLR,GONGFDBGH,GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR," + 
	        	"XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,HETGYSBID,GONGYSB_ID,QISRQ,GUOQRQ," + 
	        	"JIHKJB_ID,LIUCB_ID,MINGC,diancxxb_id " + 
	        	"from hetb_mb" +
	        	" where ID="+hetmb_id;
	         ResultSet rs=con.getResultSet(sql);
	         if(rs.next()){
	    	 Hetxxbean bean=gethetxxbean();
//	    	 sethetSelectValue(getIDropDownBean(gethetSelectModel(),rs.getLong("LIUCB_ID")));
	    	 setJihxzValue(getIDropDownBean(getIJihxzModel(),rs.getLong("JIHKJB_ID")));
//	    	 setmobmc(rs.getString("MINGC"));
//	    	 setshijgfSelectValue(getIDropDownBean(getshijgfSelectModel(),rs.getLong("GONGYSB_ID")));
//	    	 setxufValue(getIDropDownBean(getxufModel(),rs.getLong("diancxxb_id")));
//	    	 setgongfValue(getIDropDownBean(getgongfModel(),rs.getLong("HETGYSBID")));
	    	 bean.setGONGFDBGH(rs.getString("GONGFDBGH"));
	    	 bean.setGONGFDH(rs.getString("GONGFDH"));
	    	 bean.setGONGFDWDZ(rs.getString("GONGFDWDZ"));
	    	 bean.setGONGFDWMC(rs.getString("GONGFDWMC"));
	    	 bean.setGONGFFDDBR(rs.getString("GONGFFDDBR"));
	    	 bean.setGONGFKHYH(rs.getString("GONGFKHYH"));
	    	 bean.setGongfsh(rs.getString("Gongfsh"));
	    	 bean.setGONGFWTDLR(rs.getString("GONGFWTDLR"));
	    	 bean.setGONGFYZBM(rs.getString("GONGFYZBM"));
	    	 bean.setGONGFZH(rs.getString("GONGFZH"));
//	    	 bean.setGuoqsj(rs.getDate("GUOQRQ"));
	    	 bean.setHetbh(rs.getString("Hetbh"));
//	    	 bean.setHetyj(rs.getString("Hetyj"));
	    	 bean.setQianddd(rs.getString("Qianddd"));
//	    	 bean.setQiandsj(rs.getDate("QIANDRQ"));
//	    	 bean.setShengxsj(rs.getDate("QISRQ"));
	    	 bean.setXUFDBGH(rs.getString("XUFDBGH"));
	    	 bean.setXUFDH(rs.getString("XUFDH"));
	    	 bean.setXUFDWDZ(rs.getString("XUFDWDZ"));
	    	 bean.setXUFDWMC(rs.getString("XUFDWMC"));
	    	 bean.setXUFFDDBR(rs.getString("XUFFDDBR"));
	    	 bean.setXUFKHYH(rs.getString("XUFKHYH"));
	    	 bean.setXufsh(rs.getString("Xufsh"));
	    	 bean.setXUFWTDLR(rs.getString("XUFWTDLR"));
	    	 bean.setXUFYZBM(rs.getString("XUFYZBM"));
	    	 bean.setXUFZH(rs.getString("XUFZH"));
	         }
	    	//数量信息
	    	 List list=geteditValuess();
	    	 list.clear();
	    	 sql=
	    		 "select aa.hetb_id,aa.id,y1, y2,y3,y4, y5, y6, y7, y8,\n" +
	    		 " y9, y10,y11,y12,pinzb.mingc pinz,yunsfsb.mingc yunsfs,faz.mingc faz,daoz.mingc daoz\n" + 
	    		 ",diancxxb.mingc shouhr\n" + 
	    		 "from(\n" + 
	    		 "\n" + 
	    		 "select a.hetb_id,a.id,a.pinzb_id,a.yunsfsb_id,a.faz_id,a.daoz_id,a.diancxxb_id,y1.hetl as y1,y2.hetl as y2,y3.hetl as y3,y4.hetl as y4,y5.hetl as y5,y6.hetl as y6,y7.hetl as y7,y8.hetl as y8,\n" + 
	    		 "y9.hetl as y9,y10.hetl as y10,y11.hetl as y11,y12.hetl as y12\n" + 
	    		 "from\n" + 
	    		 "    (select hetb_id,id,to_char(max(hetslb.riq),'MM')Y,max(hetl)hetl,hetslb.pinzb_id,hetslb.yunsfsb_id,hetslb.faz_id,hetslb.daoz_id,hetslb.diancxxb_id\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where hetb_id="+hetmb_id+"\n" + 
	    		 "    group by hetb_id,id,hetslb.pinzb_id,hetslb.yunsfsb_id,hetslb.faz_id,hetslb.daoz_id,hetslb.diancxxb_id\n" + 
	    		 "    )a,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,1 as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='01'and hetslb.hetb_id="+hetmb_id+")y1,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,2 as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='02'and hetslb.hetb_id="+hetmb_id+")y2,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,3 as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='03'and hetslb.hetb_id="+hetmb_id+")y3,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,4 as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='04'and hetslb.hetb_id="+hetmb_id+")y4,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,5 as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='05'and hetslb.hetb_id="+hetmb_id+")y5,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,6as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='06'and hetslb.hetb_id="+hetmb_id+")y6,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,7 as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='07'and hetslb.hetb_id="+hetmb_id+")y7,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,8 as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='08'and hetslb.hetb_id="+hetmb_id+")y8,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,9 as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='09'and hetslb.hetb_id="+hetmb_id+")y9,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,10 as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='10'and hetslb.hetb_id="+hetmb_id+")y10,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,11 as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='11'and hetslb.hetb_id="+hetmb_id+")y11,\n" + 
	    		 "    ----------\n" + 
	    		 "    (select hetb_id,id,12 as y,hetl\n" + 
	    		 "    from hetslb\n" + 
	    		 "    where to_char(riq,'MM')='12'and hetslb.hetb_id="+hetmb_id+")y12\n" + 
	    		 "\n" + 
	    		 "------------------------------------\n" + 
	    		 "where a.hetb_id=y1.hetb_id(+) and a.id=y1.id(+)\n" + 
	    		 "and a.hetb_id=y2.hetb_id(+) and a.id=y2.id(+)\n" + 
	    		 "and a.hetb_id=y3.hetb_id(+) and a.id=y3.id(+)\n" + 
	    		 "and a.hetb_id=y4.hetb_id(+) and a.id=y4.id(+)\n" + 
	    		 "and a.hetb_id=y5.hetb_id(+) and a.id=y5.id(+)\n" + 
	    		 "and a.hetb_id=y6.hetb_id(+) and a.id=y6.id(+)\n" + 
	    		 "and a.hetb_id=y7.hetb_id(+) and a.id=y7.id(+)\n" + 
	    		 "and a.hetb_id=y8.hetb_id(+) and a.id=y8.id(+)\n" + 
	    		 "and a.hetb_id=y9.hetb_id(+) and a.id=y9.id(+)\n" + 
	    		 "and a.hetb_id=y10.hetb_id(+) and a.id=y10.id(+)\n" + 
	    		 "and a.hetb_id=y11.hetb_id(+) and a.id=y11.id(+)\n" + 
	    		 "and a.hetb_id=y12.hetb_id(+) and a.id=y12.id(+)\n" + 
	    		 ")aa,pinzb,yunsfsb,chezxxb faz,chezxxb daoz,diancxxb\n" + 
	    		 "where  aa.pinzb_id=pinzb.id and aa.yunsfsb_id=yunsfsb.id\n" + 
	    		 "and faz.id=aa.faz_id and aa.daoz_id=daoz.id and diancxxb.id=aa.diancxxb_id";
	    	 ResultSet rs1=con.getResultSet(sql);
	    	 while(rs1.next()){
	    		 String pinz=rs1.getString("pinz");
	    		 String yunsfs=rs1.getString("yunsfs");
	    		 String faz=rs1.getString("faz");
	    		 String daoz=rs1.getString("daoz");
	    		 String shouhr=rs1.getString("shouhr");
	    		 long id=rs1.getLong("id");
	    		 long Y1=rs1.getLong("Y1");
	    		 long Y2=rs1.getLong("Y2");
	    		 long Y3=rs1.getLong("Y3");
	    		 long Y4=rs1.getLong("Y4");
	    		 long Y5=rs1.getLong("Y5");
	    		 long Y6=rs1.getLong("Y6");
	    		 long Y7=rs1.getLong("Y7");
	    		 long Y8=rs1.getLong("Y8");
	    		 long Y9=rs1.getLong("Y9");
	    		 long Y10=rs1.getLong("Y10");
	    		 long Y11=rs1.getLong("Y11");
	    		 long Y12=rs1.getLong("Y12");
	    		 long hej=Y1+Y2+Y3+Y4+Y5+Y6+Y7+Y8+Y9+Y10+Y11+Y12;
	    		 list.add(new Fahxxbean(id,pinz,yunsfs,faz,daoz,shouhr,hej,Y1,Y2,Y3,Y4,Y5,Y6,Y7,Y8,Y9,Y10,Y11,Y12));
	    	 }
	    	//质量信息
	    	 list= geteditValuesz();
	    	 list.clear();
	    	 sql=
	    		 "select hetzlb.id,zhibb.mingc zhib,tiaojb.mingc tiaoj,shangx,xiax,danwb.mingc danw\n" +
	    		 "from hetzlb,zhibb,tiaojb,danwb\n" + 
	    		 "where hetzlb.zhibb_id=zhibb.id and hetzlb.tiaojb_id=tiaojb.id and hetzlb.danwb_id=danwb.id and hetzlb.hetb_id="+hetmb_id;
	    	 ResultSet rs2=con.getResultSet(sql);
	    	 int i=0;
	    	 while(rs2.next()){
	    		 String zhib=rs2.getString("zhib");
	    		 String tiaoj=rs2.getString("tiaoj");
	    		 double shangx=rs2.getDouble("shangx");
	    		 double xiax=rs2.getDouble("xiax");
	    		 String danw=rs2.getString("danw");
	    		 long id=rs2.getLong("id");
	    		 list.add(new Zhilyqbean(++i,id,zhib,tiaoj,shangx,xiax,danw));
	    	 }
//	    	价格信息
	    	 list = geteditValuesg();
	    	 list.clear();
	    	 sql=
	    		 "select hetjgb.ID,zhibb.mingc zhibmc,tiaojb.mingc tiaojmc,SHANGX,XIAX,zhibdw.mingc zhibdwmc,JIJ,jijdw.mingc jijdwmc,\n" +
	    		 "hetjjfsb.mingc hetjjfsmc ,hetjsfsb.mingc hetjsfsmc,hetjsxsb.mingc hetjsxsmc,YUNJ,yunjdw.mingc yunjdwmc,decode(YINGDKF,1,'是',2,'否','')YINGDKF,\n" + 
	    		 "yunsfsb.mingc yunsfsmc,ZUIGMJ\n" + 
	    		 "from hetjgb,zhibb,tiaojb,danwb zhibdw,danwb jijdw,hetjsfsb,hetjsxsb,hetjjfsb,danwb yunjdw,yunsfsb\n" + 
	    		 "where hetjgb.zhibb_id=zhibb.id and hetjgb.tiaojb_id=tiaojb.id and hetjgb.danwb_id=zhibdw.id\n" + 
	    		 "and hetjgb.jijdwid=jijdw.id and hetjgb.hetjsfsb_id=hetjsfsb.id(+) and hetjgb.hetjsxsb_id=hetjsxsb.id(+)\n" + 
	    		 "and hetjgb.yunjdw_id=yunjdw.id(+) and hetjgb.yunsfsb_id=yunsfsb.id(+) and hetjgb.hetjjfsb_id=hetjjfsb.id(+) and hetjgb.hetb_id="+hetmb_id;
	    	 ResultSet rs3=con.getResultSet(sql);
	    	 i=0;
	    	 while(rs3.next()){
	    		 long id=rs3.getLong("id");
	    		 String zhibmc=rs3.getString("zhibmc");
	    		 String tiaojmc=rs3.getString("tiaojmc");
	    		 double XIAX=rs3.getDouble("XIAX");
	    		 double SHANGX=rs3.getDouble("SHANGX");
	    		 String zhibdwmc=rs3.getString("zhibdwmc");
	    		 double JIJ=rs3.getDouble("JIJ");
	    		 String jijdwmc=rs3.getString("jijdwmc");
	    		 String hetjjfsmc=rs3.getString("hetjjfsmc");
	    		 String hetjsfsmc=rs3.getString("hetjsfsmc");
	    		 String hetjsxsmc=rs3.getString("hetjsxsmc");
	    		 double YUNJ=rs3.getDouble("YUNJ");
	    		 String yunjdwmc=rs3.getString("yunjdwmc");
	    		 String YINGDKF=rs3.getString("YINGDKF");
	    		 String yunsfsmc=rs3.getString("yunsfsmc");
	    		 double ZUIGMJ=rs3.getDouble("ZUIGMJ");
	    		 list.add(new jijbean(++i,id,zhibmc,tiaojmc,SHANGX,XIAX,zhibdwmc,JIJ,jijdwmc,hetjsfsmc,hetjjfsmc,hetjsxsmc,YUNJ,
	    				 yunjdwmc ,YINGDKF,yunsfsmc,ZUIGMJ));
	    	 }
//	    	增扣款信息
	    	 list = geteditValuesj();
	    	 list.clear();
	    	 sql=
	    		 "select z.ID,zhibb.mingc zhibmc,tiaojb.mingc tiaojmc,z.SHANGX,z.XIAX,zhibdw.mingc zhibdwmc,JIS,jisdw.mingc jisdwmc,\n" + 
	    		 "KOUJ,koujdw.mingc koujdwmc,ZENGFJ,zengfjdw.mingc zengfjdwmc,\n" + 
	    		 "decode(XIAOSCL,1,'进位',2,'舍去',3,'四舍五入',4,'四舍五入(0.1)',5,'四舍五入(0.01)','')XIAOSCL,JIZZKJ,JIZZB\n" + 
	    		 ",CANZXM.Mingc canzxmmc,CANZXMDW.Mingc canzxmdwmc,CANZSX,CANZXX,hetjsxsb.mingc hetjsxsmc,yunsfsb.mingc yunsfsmc,z.BEIZ\n" + 
	    		 "from hetzkkb z,zhibb,tiaojb,danwb zhibdw,danwb jisdw,danwb koujdw,danwb zengfjdw,zhibb canzxm,danwb canzxmdw,hetjsxsb,yunsfsb\n" + 
	    		 "where z.zhibb_id=zhibb.id  and z.tiaojb_id=tiaojb.id and z.danwb_id=zhibdw.id and z.jisdwid=jisdw.id(+) and z.koujdw=koujdw.id(+)\n" + 
	    		 "and z.zengfjdw=zengfjdw.id(+) and z.canzxm=canzxm.id(+) and z.canzxmdw=canzxmdw.id(+) and z.hetjsxsb_id=hetjsxsb.id(+)\n" + 
	    		 "and z.yunsfsb_id=yunsfsb.id(+) and z.hetb_id="+hetmb_id;
	    	 ResultSet rs5=con.getResultSet(sql);
	    	 i=0;
	    	 while(rs5.next()){
	    		 long id=rs5.getLong("id");
	    		 String zhibmc=rs5.getString("zhibmc");
	    		 String tiaojmc=rs5.getString("tiaojmc");
	    		 double XIAX=rs5.getDouble("XIAX");
	    		 double SHANGX=rs5.getDouble("SHANGX");
	    		 String zhibdwmc=rs5.getString("zhibdwmc");
	    		 double JIS=rs5.getDouble("JIS");
	    		 String jisdwmc=rs5.getString("jisdwmc");
	    		 double KOUJ=rs5.getDouble("KOUJ");
	    		 String koujdwmc=rs5.getString("koujdwmc");
	    		 double ZENGFJ=rs5.getDouble("ZENGFJ");
	    		 String zengfjdwmc=rs5.getString("zengfjdwmc");
	    		 String XIAOSCL=rs5.getString("XIAOSCL");
	    		 double JIZZKJ=rs5.getDouble("JIZZKJ");
	    		 double JIZZB=rs5.getDouble("JIZZB");
	    		 String canzxmmc=rs5.getString("canzxmmc");
	    		 String canzxmdwmc=rs5.getString("canzxmdwmc");
	    		 double CANZSX=rs5.getDouble("CANZSX");
	    		 double CANZXX=rs5.getDouble("CANZXX");
	    		 String hetjsxsmc=rs5.getString("hetjsxsmc");
	    		 String yunsfsmc=rs5.getString("yunsfsmc");
	    		 String BEIZ=rs5.getString("BEIZ");
	    		 list.add(new Zengkkbean(id,++i,zhibmc,tiaojmc,SHANGX,XIAX,zhibdwmc,JIS,jisdwmc,KOUJ,koujdwmc,ZENGFJ,zengfjdwmc,
	    				 XIAOSCL ,JIZZKJ,JIZZB,canzxmmc,canzxmdwmc,CANZSX,CANZXX,hetjsxsmc,yunsfsmc,BEIZ));
	    	 }

	    	//文字信息
	    	 sql= "select id,wenznr\n" +
	    	 "from hetwzb\n" + 
	    	 "where hetb_id="+hetmb_id;
	    	 ResultSet rs4=con.getResultSet(sql);
	    	 if(rs4.next()){
	    		 setWenz(rs4.getString("wenznr"));
	    	 }
   	}catch(Exception e){
   		e.printStackTrace();
   	}finally{
   		 con.Close();
   	}
		}
	    public String getFahr(){
	    	if(((Visit) getPage().getVisit()).getString2()==null){
	    		((Visit) getPage().getVisit()).setString2("");
	    	}
	    	return ((Visit) getPage().getVisit()).getString2();
	    }
	    public void setFahr(String value){
	    	((Visit) getPage().getVisit()).setString2(value);
	    }
}
