package com.zhiren.jt.het.gongzlgl;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.util.*;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.JDBCcon;
import com.zhiren.main.Visit;
import com.zhiren.jt.het.renyjs.Renyjsbean;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.MainGlobal;

public class Gongzlgl extends BasePage {
//点击行索引
    private int _editTableRow = -1;// 编辑框中选中的行

    public int getEditTableRow() {
        return _editTableRow;
    }

    public void setEditTableRow(int _value) {
        _editTableRow = _value;
    }
    private int _editTableRowM = -1;// 编辑框中选中的行

    public int getEditTableRowM() {
        return _editTableRowM;
    }

    public void setEditTableRowM(int _value) {
        _editTableRowM = _value;
    }
    private int _editTableRowT = -1;// 编辑框中选中的行

    public int getEditTableRowT() {
        return _editTableRowT; 
    }
    public void setEditTableRowT(int value) {
        _editTableRowT=value;
    }
    private int _editTableRowL = -1;// 编辑框中选中的行

    public int getEditTableRowL() {
        return _editTableRowL;
    }
    public void setEditTableRowL(int _value) {
        _editTableRowL = _value;
    }
    private String meg;// msg

    public String getMeg() {
        return meg;
    }
    private int _editTableRowH = -1;// 编辑框中选中的行

    public int getEditTableRowH() {
        return _editTableRowH;
    }

    public void setEditTableRowH(int _value) {
        _editTableRowH = _value;
    }
    private int _editTableRowD = -1;// 编辑框中选中的行

    public int getEditTableRowD() {
        return _editTableRowD;
    }

    public void setEditTableRowD(int _value) {
        _editTableRowD = _value;
    }
    protected void initialize() {
        meg = "";
    }

    public void setMeg(String meg) {
        this.meg = meg;
    }
//点击按钮
//流程类别表
    private boolean _SaveChick = false;

    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }

    private boolean _DeleteChick = false;

    public void DeleteButton(IRequestCycle cycle) {
        _DeleteChick = true;
    }

    private boolean _InsertChick = false;

    public void InsertButton(IRequestCycle cycle) {
        _InsertChick = true;
    }
    private boolean RefurbishButton = false;

    public void RefurbishButton(IRequestCycle cycle) {
    	RefurbishButton = true;
    }
//流程表
    private boolean _SaveChickM = false;

    public void SaveButtonM(IRequestCycle cycle) {
        _SaveChickM = true;
    }

    private boolean _DeleteChickM = false;

    public void DeleteButtonM(IRequestCycle cycle) {
        _DeleteChickM = true;
    }

    private boolean _InsertChickM = false;

    public void InsertButtonM(IRequestCycle cycle) {
        _InsertChickM = true;
    }
    private boolean RefurbishButtonM = false;

    public void RefurbishButtonM(IRequestCycle cycle) {
    	RefurbishButtonM = true;
    }
    //状态与流程
    private boolean _SaveChickT = false;

    public void SaveButtonT(IRequestCycle cycle) {
        _SaveChickT = true;
    }

    private boolean _DeleteChickT = false;

    public void DeleteButtonT(IRequestCycle cycle) {
        _DeleteChickT = true;
    }

    private boolean _InsertChickT = false;

    public void InsertButtonT(IRequestCycle cycle) {
        _InsertChickT = true;
    }
    private boolean RefurbishButtonT = false;

    public void RefurbishButtonT(IRequestCycle cycle) {
    	RefurbishButtonT = true;
    }
//  类别状态
    private boolean _SaveChickL = false;

    public void SaveButtonL(IRequestCycle cycle) {
        _SaveChickL = true;
    }

    private boolean _DeleteChickL = false;

    public void DeleteButtonL(IRequestCycle cycle) {
        _DeleteChickL = true;
    }

    private boolean _InsertChickL = false;

    public void InsertButtonL(IRequestCycle cycle) {
        _InsertChickL = true;
    }
    private boolean RefurbishButtonL = false;

    public void RefurbishButtonL(IRequestCycle cycle) {
    	RefurbishButtonL = true;
    }
    //角色
    private boolean _SaveChickH = false;

    public void SaveButtonH(IRequestCycle cycle) {
        _SaveChickH = true;
    }

    private boolean _DeleteChickH = false;

    public void DeleteButtonH(IRequestCycle cycle) {
        _DeleteChickH = true;
    }

    private boolean _InsertChickH = false;

    public void InsertButtonH(IRequestCycle cycle) {
        _InsertChickH = true;
    }
    private boolean RefurbishButtonH = false;

    public void RefurbishButtonH(IRequestCycle cycle) {
    	RefurbishButtonH = true;
    }
//DZ
    private boolean _SaveChickD = false;

    public void SaveButtonD(IRequestCycle cycle) {
        _SaveChickD = true;
    }

    private boolean _DeleteChickD= false;

    public void DeleteButtonD(IRequestCycle cycle) {
        _DeleteChickD = true;
    }

    private boolean _InsertChickD= false;

    public void InsertButtonD(IRequestCycle cycle) {
        _InsertChickD = true;
    }
    private boolean RefurbishButtonD = false;

    public void RefurbishButtonD(IRequestCycle cycle) {
    	RefurbishButtonD = true;
    }
    public void submit(IRequestCycle cycle) {
    	//流程类别
    	if (_SaveChick) {
            _SaveChick = false;
            Save();
        }
        if (_DeleteChick) {
            _DeleteChick = false;
            Delete();
        }
        if (_InsertChick) {
            _InsertChick = false;
            Insert();
        }
        if (RefurbishButton) {
        	RefurbishButton = false;
        	getSelectData();
        }
        //流程命称表
        if (_SaveChickM) {
            _SaveChickM = false;
            SaveM();
        }
        if (_DeleteChickM) {
            _DeleteChickM= false;
            DeleteM();
        }
        if (_InsertChickM) {
            _InsertChickM = false;
            InsertM();
        }
        if (RefurbishButtonM) {
        	RefurbishButtonM = false;
        	getSelectDataM();
        }
        //
        
        // tijiao
        if (_SaveChickH) {
            _SaveChickH = false;
            SaveH();
        }
        if (_DeleteChickH) {
            _DeleteChickH = false;
            DeleteH();
        }
        if (_InsertChickH) {
            _InsertChickH = false;
            InsertH();
        }
        if (RefurbishButtonH) {
        	RefurbishButtonH = false;
        	getSelectDataH();
        }
        //状态与流程
        if (_SaveChickT) {
            _SaveChickT = false;
            SaveT();
        }
        if (_DeleteChickT) {
            _DeleteChickT = false;
            DeleteT();
        }
        if (_InsertChickT) {
            _InsertChickT = false;
            InsertT();
        }
        if (RefurbishButtonT) {
        	RefurbishButtonT = false;
        	getSelectDataT();
        }
        //状态与类别
        if (_SaveChickL) {
            _SaveChickL = false;
            SaveL();
        }
        if (_DeleteChickL) {
            _DeleteChickL = false;
            DeleteL();
        }
        if (_InsertChickL) {
            _InsertChickL = false;
            InsertL();
        }
        if (RefurbishButtonL) {
        	RefurbishButtonL = false;
        	getSelectDataL();
        }
        //DZ
        //
        if (_SaveChickD) {
            _SaveChickD = false;
            SaveD();
        }
        if (_DeleteChickD) {
            _DeleteChickD= false;
            DeleteD();
        }
        if (_InsertChickD) {
            _InsertChickD = false;
            InsertD();
        }
        if (RefurbishButtonD) {
        	RefurbishButtonD = false;
        	getSelectDataD();
        }
    }
//下拉框：
//流程类别
    private Liuclbbean _EditValue;

    public List getEditValues() {
        return ((Visit) getPage().getVisit()).getList5();
    }

    public void setEditValues(List editList) {
        ((Visit) getPage().getVisit()).setList5(editList);
    }

    public Liuclbbean getEditValue() {
        return _EditValue;
    }

    public void setEditValue(Liuclbbean EditValue) {
        _EditValue = EditValue;
    }

    

    private String _Renyxxb_idValue;

    public String getRenyxxb_idValue() {
        return _Renyxxb_idValue;
    }

    public void setRenyxxb_idValue(String Value) {
        _Renyxxb_idValue = Value;
    }

    private IPropertySelectionModel _IRenyxxb_idModel;

    public void setIRenyxxb_idModel(IPropertySelectionModel value) {
        _IRenyxxb_idModel = value;
    }

    public IPropertySelectionModel getIRenyxxb_idModel() {
        if (_IRenyxxb_idModel == null) {
            getIRenyxxb_idModels();
        }
        return _IRenyxxb_idModel;
    }

    public IPropertySelectionModel getIRenyxxb_idModels() {
        String sql = "select id, mingc from renyxxb";
        _IRenyxxb_idModel = new IDropDownModel(sql,"请选择");
        return _IRenyxxb_idModel;
    }

    private String _Juesb_idValue;

    public String getJuesb_idValue() {
        return _Juesb_idValue;
    }

    public void setJuesb_idValue(String Value) {
        _Juesb_idValue = Value;
    }

    private IPropertySelectionModel _IJuesb_idModel;

    public void setIJuesb_idModel(IPropertySelectionModel value) {
        _IJuesb_idModel = value;
    }

    public IPropertySelectionModel getIJuesb_idModel() {
        if (_IJuesb_idModel == null) {
            getIJuesb_idModels();
        }
        return _IJuesb_idModel;
    }

    public IPropertySelectionModel getIJuesb_idModels() {
        String sql = 
        	"select id,mingc\n" +
        	"from liucjsb";

        _IJuesb_idModel = new IDropDownModel(sql,"请选择");
        return _IJuesb_idModel;
    }
//流程命

    public String getliuclbb_idValue() {
        return  ((Visit) getPage().getVisit()).getString1();
    }

    public void setliuclbb_idValue(String Value) {
    	((Visit) getPage().getVisit()).setString1(Value);
    }


    public void setliuclbb_idModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel1(value);
    }

    public IPropertySelectionModel getliuclbb_idModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
            getliuclbb_idModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel1();
    }

    public void getliuclbb_idModels() {
        String sql = " select id,mingc from liuclbb";
        ((Visit) getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql,"请选择")) ;
        return ;
    }
    //状态与流程
    public IDropDownBean getLiucb_IDValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
      		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)getLiucb_IDModel().getOption(0));
     		}
        return  ((Visit) getPage().getVisit()).getDropDownBean2();
    }

    public void setLiucb_IDValue(IDropDownBean Value) {
    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean2()!=null){
	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean2().getId()){
	    		((Visit) getPage().getVisit()).setboolean2(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean2(false);
	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean2(Value);
    	}
    }


    public void setLiucb_IDModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public IPropertySelectionModel getLiucb_IDModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
            getLiucb_IDModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }

    public void getLiucb_IDModels() {
        String sql = " select id,mingc from liucb";
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"请选择")) ;
        return ;
    }
    //流程状态表
    public IDropDownBean getzhuangt_idValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean3()==null){
     		 ((Visit) getPage().getVisit()).setDropDownBean3((IDropDownBean)getzhuangt_idModel().getOption(0));
    		}
       return  ((Visit) getPage().getVisit()).getDropDownBean3();
    }

    public void setzhuangt_idValue(IDropDownBean Value) {
    	((Visit) getPage().getVisit()).setDropDownBean3(Value);
    }


    public void setzhuangt_idModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel3(value);
    }

    public IPropertySelectionModel getzhuangt_idModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
            getzhuangt_idModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel3();
    }

    public void getzhuangt_idModels() {
        String sql =
        	"select liucztb.id,leibztb.mingc\n" +
        	"from liucztb,leibztb\n" + 
        	" where liucztb.leibztb_id=leibztb.id and liucztb.liucb_id="+
        	getliucSelectValue().getId();
        ((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql,"请选择"));
        return ;
    }
    //类别状态表
    public IDropDownBean getleibSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean5()==null){
    		 ((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean)getleibSelectModel().getOption(0));
   		}
      return  ((Visit) getPage().getVisit()).getDropDownBean5();
    }

    public void setleibSelectValue(IDropDownBean Value) {
    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean5()!=null){
	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean5().getId()){
	    		((Visit) getPage().getVisit()).setboolean3(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean3(false);
	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean5(Value);
    	}
    }
    public void setleibSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel5(value);
    }

    public IPropertySelectionModel getleibSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
            getleibSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel5();
    }

    public void getleibSelectModels() {
        String sql =
        	"select id,mingc\n" +
        	"from liuclbb";
        ((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(sql,"请选择"));
        return ;
    }
    
    //类别状态表
    public IDropDownBean getleibztSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean6()==null){
     		 ((Visit) getPage().getVisit()).setDropDownBean6((IDropDownBean)getleibztSelectModel().getOption(0));
    		}
       return  ((Visit) getPage().getVisit()).getDropDownBean6();
    }

    public void setleibztSelectValue(IDropDownBean Value) {
    	((Visit) getPage().getVisit()).setDropDownBean6(Value);
    }


    public void setleibztSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel6(value);
    }

    public IPropertySelectionModel getleibztSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
            getleibztSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel6();
    }

    public void getleibztSelectModels() {
        String sql =
        	"select leibztb.id,leibztb.mingc\n" +
        	"from leibztb,liucb\n" + 
        	"where leibztb.liuclbb_id=liucb.liuclbb_id and  liucb.id=" +getLiucb_IDValue().getId() +
        	" union\n" + 
        	"select leibztb.id ,leibztb.mingc\n" + 
        	"from leibztb\n" + 
        	"where leibztb.id=0 or leibztb.id=1";
  ((Visit) getPage().getVisit()).setProSelectionModel6(new IDropDownModel(sql,"请选择"));
        return ;
    }
    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
        Visit visit = (Visit) getPage().getVisit();
        getIJuesb_idModels();
        if (!visit.getActivePageName().toString().equals(
                this.getPageName().toString())) {
            // 在此添加，在页面第一次加载时需要置为空的变量或方法
            visit.setActivePageName(getPageName().toString());
            visit.setList6(null);
            visit.setList5(null);
            visit.setList1(null);
            visit.setList2(null);
            visit.setList3(null);
            visit.setList4(null);
            visit.setProSelectionModel1(null);
            visit.setProSelectionModel2(null);
            visit.setProSelectionModel3(null);
            visit.setProSelectionModel4(null);
            visit.setProSelectionModel5(null);
            visit.setProSelectionModel6(null);
            visit.setDropDownBean4(null);
            visit.setDropDownBean3(null);
            visit.setDropDownBean2(null);
            visit.setDropDownBean1(null);
            visit.setDropDownBean5(null);
            visit.setDropDownBean6(null);
            setTabbarSelect(0);
            getSelectData();
            getSelectDataL();
            getSelectDataH();
//            getSelectDataT();
            getSelectDataM();
//            getSelectDataD();
        }
        if(visit.getboolean1()){//流程动作
        	 getSelectDataD();
        	 getzhuangt_idModels();
        }
        if(visit.getboolean2()){//流程状态
	       	 getSelectDataT();
	         getleibztSelectModels();
        }
        if(visit.getboolean3()){//类别
	       	 getSelectDataL();
       }
    }

    public String getpageLink() {
        return " var context='"
                + MainGlobal.getHomeContext(this) + "';";
    }
    private String getProperValue(IPropertySelectionModel _selectModel,
            int value) {
        int OprionCount;
        OprionCount = _selectModel.getOptionCount();
        for (int i = 0; i < OprionCount; i++) {
            if (((IDropDownBean) _selectModel.getOption(i)).getId() == value) {
                return ((IDropDownBean) _selectModel.getOption(i)).getValue();
            }
        }
        return null;
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
    // ///////////////////
    public void setTabbarSelect(int value) {
        ((Visit) getPage().getVisit()).setInt1(value);
    }

    public int getTabbarSelect() {
        return ((Visit) getPage().getVisit()).getInt1();
    }
//表list
//流程状态
   

    private Liucztbbean _EditValueT;

    public List getEditValuesT() {
       
        if (((Visit) getPage().getVisit()).getList2() == null) {
        	 List list = new ArrayList();
            ((Visit) getPage().getVisit()).setList2(list);
        }
        return ((Visit) getPage().getVisit()).getList2();
    }

    public Liucztbbean getEditValueT() {
        return _EditValueT;
    }

    public void setEditValueT(Liucztbbean EditValue) {
        _EditValueT = EditValue;
    }
    //类别状态
    private Liucztbbean _EditValueL;

    public List getEditValuesL() {
       
        if (((Visit) getPage().getVisit()).getList6() == null) {
        	 List list = new ArrayList();
            ((Visit) getPage().getVisit()).setList6(list);
        }
        return ((Visit) getPage().getVisit()).getList6();
    }

    public Liucztbbean getEditValueL() {
        return _EditValueL;
    }

    public void setEditValueL(Liucztbbean EditValue) {
        _EditValueL = EditValue;
    }
//流程动作
    private Liucdzbbean _EditValueD;

    public List getEditValuesD() {
        if (((Visit) getPage().getVisit()).getList3() == null) {
        	  List list = new ArrayList();
            ((Visit) getPage().getVisit()).setList3(list);
        }
        return ((Visit) getPage().getVisit()).getList3();
    }

    public Liucdzbbean getEditValueD() {
        return _EditValueD;
    }

    public void setEditValueD(Liucdzbbean EditValue) {
        _EditValueD = EditValue;
    }
//流程名表 
    private Liucbbean _EditValueM;
    public Liucbbean getEditValueM() {
        return _EditValueM;
    }

    public void setEditValueM(Liucbbean EditValueM) {
    	_EditValueM = EditValueM;
    }
    public List geteditValuesM() {
        if (((Visit) getPage().getVisit()).getList1() == null) {
        	 List list = new ArrayList();
            ((Visit) getPage().getVisit()).setList1(list);
        }
        return ((Visit) getPage().getVisit()).getList1();
    }
    private Liucjsbbean _EditValueH;

    public List getEditValuesH() {
        if (((Visit) getPage().getVisit()).getList4() == null) {
        	  List list = new ArrayList();
            ((Visit) getPage().getVisit()).setList4(list);
        }
        return ((Visit) getPage().getVisit()).getList4();
    }

    public Liucjsbbean getEditValueH() {
        return _EditValueH;
    }

    public void setEditValueH(Liucjsbbean EditValue) {
        _EditValueH = EditValue;
    }
//流程状态
    private void SaveT() {// 链表的使用
        // 开始 对提交流程表解析,结果放在:juesbs,juesbx中
    	 String sql;
         JDBCcon con = new JDBCcon();
         List _value = getEditValuesT();
         for (int i = 0; i < _value.size(); i++) {// 遍历容器
             long mid = ((Liucztbbean) _value.get(i)).getM_id();
             String mingc = ((Liucztbbean) _value.get(i)).getMingc();
             int xuh=((Liucztbbean) _value.get(i)).getPaixh();
             
             if (mid == 0) {
                 mid = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
                 sql = "insert into liucztb ( id,xuh,liucb_id,leibztb_id)" + "values("
                         + mid + "," + xuh+","+ getLiucb_IDValue().getId()+ ","+getProperId(getleibztSelectModel(),mingc)+")";
                 con.getInsert(sql);
                 ((Liucztbbean) _value.get(i)).setM_id(mid);
             } else {
                 sql = "update liucztb set leibztb_id=" + getProperId(getleibztSelectModel(),mingc) 
                 +","
                 +"xuh="+xuh
                 + " where id=" + mid;
                 con.getUpdate(sql);
             }
         }
         con.Close();
    }

    private void DeleteT() {
        List _value = getEditValuesT();
        if (_editTableRowT != -1) {
            long id = ((Liucztbbean) _value.get(_editTableRowT)).getM_id();
            if (id != 0) {
                JDBCcon con = new JDBCcon();
                String sql = "delete from liucztb where id=" + id;
                con.getDelete(sql);
                con.Close();
            }
            _value.remove(_editTableRowT);
            int c = _value.size();
            for (int a = _editTableRowT; a < c; a++) {
                ((Liucztbbean) _value.get(a))
                        .setXuh(((Liucztbbean) _value.get(a)).getXuh() - 1);
            }
        }
    }

    private void InsertT() {
        List list = getEditValuesT();
        list.add(new Liucztbbean(list.size() + 1));
    }
    public void getSelectDataT() {
        int i = 0;
        List _editvalues = getEditValuesT();
        _editvalues.clear();
        JDBCcon JDBCcon = new JDBCcon();
        try {
            // getIRenyxxb_idModels();
            // getIJuesb_idModels();
            String sql = 
            	"select liucztb.id,leibztb.mingc,xuh\n" +
            	"from liucztb,leibztb\n" + 
            	"where liucztb.leibztb_id =leibztb.id and liucztb.liucb_id="
            	+getLiucb_IDValue().getId()+" order by xuh";

            ResultSet rs = JDBCcon.getResultSet(sql);
            while (rs.next()) {
                long mid = rs.getLong("ID");
                String mingc = rs.getString("mingc");
                int xuh = rs.getInt("xuh");
                _editvalues.add(new Liucztbbean(mid, mingc,xuh, ++i));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCcon.Close();
        }
        _editTableRowT = -1;
    }
//  类别状态
    private void SaveL() {// 链表的使用
        // 开始 对提交流程表解析,结果放在:juesbs,juesbx中
    	 String sql;
         JDBCcon con = new JDBCcon();
         List _value = getEditValuesL();
         for (int i = 0; i < _value.size(); i++) {// 遍历容器
             long mid = ((Liucztbbean) _value.get(i)).getM_id();
             String mingc = ((Liucztbbean) _value.get(i)).getMingc();
             int xuh=((Liucztbbean) _value.get(i)).getPaixh();
             
             if (mid == 0) {
                 mid = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
                 sql = "insert into leibztb ( id,liuclbb_id,mingc)" + "values("
                         + mid + "," + getleibSelectValue().getId()+ ",'"+mingc+"')";
                 con.getInsert(sql);
                 ((Liucztbbean) _value.get(i)).setM_id(mid);
             } else {
                 sql = "update leibztb set mingc='" + mingc 
                 + "' where id=" + mid;
                 con.getUpdate(sql);
             }
         }
         con.Close();
    }

    private void DeleteL() {
        List _value = getEditValuesL();
        if (_editTableRowL != -1) {
            long id = ((Liucztbbean) _value.get(_editTableRowL)).getM_id();
            if (id != 0) {
                JDBCcon con = new JDBCcon();
                String sql = "delete from leibztb where id=" + id;
                con.getDelete(sql);
                con.Close();
            }
            _value.remove(_editTableRowL);
            int c = _value.size();
            for (int a = _editTableRowL; a < c; a++) {
                ((Liucztbbean) _value.get(a))
                        .setXuh(((Liucztbbean) _value.get(a)).getXuh() - 1);
            }
        }
    }

    private void InsertL() {
        List list = getEditValuesL();
        list.add(new Liucztbbean(list.size() + 1));
    }
    public void getSelectDataL() {
        int i = 0;
        List _editvalues = getEditValuesL();
        _editvalues.clear();
        JDBCcon JDBCcon = new JDBCcon();
        try {
            // getIRenyxxb_idModels();
            // getIJuesb_idModels();
            String sql = 
            	"select id,mingc\n" +
            	"from leibztb\n" + 
            	"where liuclbb_id="+getleibSelectValue().getId();
            ResultSet rs = JDBCcon.getResultSet(sql);
            while (rs.next()) {
                long mid = rs.getLong("ID");
                String mingc = rs.getString("mingc");
                _editvalues.add(new Liucztbbean(mid, mingc,1, ++i));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCcon.Close();
        }
        _editTableRowL = -1;
    }
    //流程角色
    public void getSelectDataH() {
        int i = 0;
        List _editvalues = getEditValuesH();
        _editvalues.clear();
        JDBCcon JDBCcon = new JDBCcon();
        try {
            getIRenyxxb_idModels();
            // getIJuesb_idModels();
            String sql = "select id,mingc,beiz from liucjsb";
            ResultSet rs = JDBCcon.getResultSet(sql);
            while (rs.next()) {
                long mid = rs.getLong("ID");
            String mingc = rs.getString("mingc");
            String beiz = rs.getString("beiz");
                _editvalues.add(new Liucjsbbean(mid, mingc, beiz, ++i));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCcon.Close();
        }
        _editTableRowH = -1;
    }



    private void SaveH() {
        String sql;
        JDBCcon con = new JDBCcon();
        List _value = getEditValuesH();
        for (int i = 0; i < _value.size(); i++) {// 遍历容器
            long mid = ((Liucjsbbean) _value.get(i)).getId();
            String Mingc = ((Liucjsbbean) _value.get(i)).getMingc();
            String Beiz = ((Liucjsbbean) _value.get(i)).getBeiz();
            if (mid == 0) {
                mid = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
                sql = "insert into liucjsb (id,mingc,beiz )"
                        + "values(" + mid + ",'" + Mingc + "','" + Beiz
                        + "')";
                con.getInsert(sql);
            } else {
                sql = "update liucjsb set mingc='" + Mingc + "',beiz='"
                        + Beiz + "'  where id=" + mid;
                con.getUpdate(sql);
            }
        }
        con.Close();
        getSelectDataH();
    }

    private void DeleteH() {
        List _value = getEditValuesH();
        if (_editTableRowH != -1) {
            long id = ((Liucjsbbean) _value.get(_editTableRowH)).getId();
            if (id != 0) {
                JDBCcon con = new JDBCcon();
                String sql = "delete from liucjsb where id=" + id;
                con.getDelete(sql);
                con.Close();
            }
            _value.remove(_editTableRowH);
            int c = _value.size();
            for (int a = _editTableRowH; a < c; a++) {
                ((Liucjsbbean) _value.get(a))
                        .setXuh(((Liucjsbbean) _value.get(a)).getXuh() - 1);
            }
        }
    }

    private void InsertH() {
        List list = getEditValuesH();
        list.add(new Liucjsbbean(list.size() + 1));
    }

//  流程类别
    private void Save() {
        String sql;
        JDBCcon con = new JDBCcon();
        List _value = getEditValues();
        for (int i = 0; i < _value.size(); i++) {// 遍历容器
            long mid = ((Liuclbbean) _value.get(i)).getId();
            String mingc = ((Liuclbbean) _value.get(i)).getMingc();
            if (mid == 0) {
                mid =  Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
                sql = "insert into liuclbb (ID,MINGC)" + "values("
                        + mid + ",'" + mingc + "')";
                con.getInsert(sql);
                ((Liuclbbean) _value.get(i)).setId(mid);
            } else {
                sql = "update liuclbb set mingc='" + mingc + "' where id=" + mid;
                con.getUpdate(sql);
            }
        }
        con.Close();
    }

    private void Delete() {
        String sql = "";
        JDBCcon con = new JDBCcon();
        List _value = getEditValues();
        if (_editTableRow != -1) {
        	if(((Liuclbbean)_value.get(_editTableRow)).getId()!=0){
        		sql="delete from liuclbb where id="+((Liuclbbean)_value.get(_editTableRow)).getId();
        		con.getDelete(sql);
        	}
        		_value.remove(_editTableRow);
        		int c = _value.size();
                for (int i = _editTableRow; i < c; i++) {
                    ((Liuclbbean) _value.get(i))  .setXuh(((Liuclbbean) _value.get(i)).getXuh() - 1);
                }
        }
    }

    private void Insert() {
        // 为 "添加" 按钮添加处理程序
        // List _list =((Visit) getPage().getVisit()).getList1();
        // ((Juesbean) _list.get(i)).getXXX();
        List _value = getEditValues();
        if (_value == null) {
            _value = new ArrayList();
        }
        _value.add(new Liuclbbean(0, _value.size() + 1, ""));
        setEditValues(_value);
        _editTableRow = -1;
    }
    public void getSelectData() {
        int i = 0;
        List _editvalues = new ArrayList();
        JDBCcon JDBCcon = new JDBCcon();
        try {

            String sql =
            	"select *\n" +
            	"from liuclbb";

            ResultSet rs = JDBCcon.getResultSet(sql);
            while (rs.next()) {
                long mid = rs.getLong("ID");
                String mmingc = rs.getString("MINGC");
                // double mjib=rs.getDouble("JIB");
                // double mshenhlx=rs.getDouble("SHENHLX");
                _editvalues.add(new Liuclbbean(mid, ++i, mmingc));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCcon.Close();
        }
        if (_editvalues == null) {
            _editvalues.add(new Liuclbbean());
        }
        _editTableRow = -1;
        setEditValues(_editvalues);
        return ;
    }
    
    //流程表
    private void SaveM() {
        String sql;
        JDBCcon con = new JDBCcon();
        List _value = geteditValuesM();
        for (int i = 0; i < _value.size(); i++) {// 遍历容器
            long mid = ((Liucbbean) _value.get(i)).getId();
            String mingc = ((Liucbbean) _value.get(i)).getMingc();
            String liuclbm = ((Liucbbean) _value.get(i)).getLiuclbb_id();
            
            if (mid == 0) {
                mid =  Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
                sql = "insert into liucb (ID,MINGC,liuclbb_id)" + "values("
                        + mid + ",'" + mingc+"',(select id from liuclbb where mingc='"+ liuclbm+ "'))";
                con.getInsert(sql);
                ((Liucbbean) _value.get(i)).setId(mid);
            } else {
                sql = "update liucb set mingc='" + mingc 
                +"',liuclbb_id=(select id from liuclbb where mingc='"+liuclbm
                + "') where id=" + mid;
                con.getUpdate(sql);
            }
        }
        con.Close();
    }

    private void DeleteM() {
        String sql = "";
        JDBCcon con = new JDBCcon();
        List _value =geteditValuesM();
        if (_editTableRowM != -1) {
        	if(((Liucbbean)_value.get(_editTableRowM)).getId()!=0){
        		sql="delete from liucb where id="+((Liucbbean)_value.get(_editTableRowM)).getId();
        		con.getDelete(sql);
        	}
        		_value.remove(_editTableRowM);
        		int c = _value.size();
                for (int i = _editTableRowM; i < c; i++) {
                    ((Liucbbean) _value.get(i))  .setXuh(((Liucbbean) _value.get(i)).getXuh() - 1);
                }
        }
        _editTableRowM = -1;
    }

    private void InsertM() {
        // 为 "添加" 按钮添加处理程序
        // List _list =((Visit) getPage().getVisit()).getList1();
        // ((Juesbean) _list.get(i)).getXXX();
        List _value =geteditValuesM();
        _value.add(new Liucbbean(0, _value.size() + 1, "",""));
        _editTableRowM = -1;
    }
    public void getSelectDataM() {
        int i = 0;
        List _editvalues =geteditValuesM();
        _editvalues.clear();
        JDBCcon JDBCcon = new JDBCcon();
        try {

            String sql ="select liucb.id,liucb.mingc,liuclbb.mingc leibm\n" +
			"from liucb,liuclbb\n" + 
			"where liucb.liuclbb_id=liuclbb.id";


            ResultSet rs = JDBCcon.getResultSet(sql);
            while (rs.next()) {
                long mid = rs.getLong("ID");
                String mmingc = rs.getString("MINGC");
                String leibm = rs.getString("leibm");
                
                // double mjib=rs.getDouble("JIB");
                // double mshenhlx=rs.getDouble("SHENHLX");
                _editvalues.add(new Liucbbean(mid, ++i, mmingc,leibm));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCcon.Close();
        }
        _editTableRowM = -1;
        return ;
    }
    //动作表

//  流程类别
    private void SaveD() {
        String sql;
        JDBCcon con = new JDBCcon();
        List _value = getEditValuesD();
        for (int i = 0; i < _value.size(); i++) {// 遍历容器
            long Liucdzjsb_id = ((Liucdzbbean) _value.get(i)).getLiucdzjsb_id();
            long liucdzb_id = ((Liucdzbbean) _value.get(i)).getLiucdzb_id();
            String jues=((Liucdzbbean) _value.get(i)).getJues();
            String qianuzt=((Liucdzbbean) _value.get(i)).getQianqzt();
            String houjzt=((Liucdzbbean) _value.get(i)).getHoujzt();
            String caozm=((Liucdzbbean) _value.get(i)).getCaozmc();
            String dongz=((Liucdzbbean) _value.get(i)).getDongz();
            if (liucdzb_id == 0) {//暂时只考虑一对一关系没有考虑多对多的关系，没有增加判断是否已经存在。
            	liucdzb_id =  Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
                sql = 
                	"insert into liucdzb(id,liucztqqid,liuczthjid,mingc,dongz)values(" 
                	 + liucdzb_id+","+getProperId(getzhuangt_idModel(),qianuzt)
                	 +","+getProperId(getzhuangt_idModel(),houjzt)
                	 +",'"+caozm  +"','" +dongz
                	 +"')";
                
               
                con.getInsert(sql);
                //
                Liucdzjsb_id =  Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
                sql = 
                	"insert into liucdzjsb(id,liucdzb_id,liucjsb_id)values(" 
                	+ Liucdzjsb_id
                	+","
                	+liucdzb_id
                	+",(select id from liucjsb where liucjsb.mingc='"
                	+jues
                	+"'))";
                con.getInsert(sql);
                ((Liucdzbbean) _value.get(i)).setLiucdzb_id(liucdzb_id);
                ((Liucdzbbean) _value.get(i)).setLiucdzjsb_id(Liucdzjsb_id);
                
            } else {
                sql = "update liucdzb set liucztqqid=" + getProperId(getzhuangt_idModel(),qianuzt)
                +",liuczthjid="
                +getProperId(getzhuangt_idModel(),houjzt)
                +",mingc='"
                +caozm  +"',dongz='"+dongz
                + "' where id=" + liucdzb_id;
                con.getUpdate(sql);
                
                sql = "update liucdzjsb set liucjsb_id=(select id from liucjsb where liucjsb.mingc='" + jues 
                + "') where id=" + Liucdzjsb_id;
                con.getUpdate(sql);
            }
        }
        con.Close();
    }

    private void DeleteD() {
        String sql = "";
        JDBCcon con = new JDBCcon();
        List _value = getEditValuesD();
        if (_editTableRowD != -1) {
        	if(((Liucdzbbean)_value.get(_editTableRowD)).getLiucdzjsb_id()!=0){//暂时没考虑多对多的关系，如果是那样还要判断
        		sql="delete\n" +
        		"from liucdzb\n" + 
        		"where liucdzb.id="+((Liucdzbbean)_value.get(_editTableRowD)).getLiucdzb_id();
        		con.getDelete(sql);
        		sql="delete \n" +
        		"from liucdzjsb\n" + 
        		"where liucdzjsb.id="+((Liucdzbbean)_value.get(_editTableRowD)).getLiucdzjsb_id();
        		con.getDelete(sql);

        	}
        		_value.remove(_editTableRowD);
        		int c = _value.size();
                for (int i = _editTableRowD; i < c; i++) {
                    ((Liucdzbbean) _value.get(i))  .setXuh(((Liucdzbbean) _value.get(i)).getXuh() - 1);
                }
        }
    }

    private void InsertD() {
        // 为 "添加" 按钮添加处理程序
        // List _list =((Visit) getPage().getVisit()).getList1();
        // ((Juesbean) _list.get(i)).getXXX();
        List _value = getEditValuesD();
        _value.add(new Liucdzbbean(_value.size() + 1,0 , 0));
        _editTableRowD = -1;
    }
    public void getSelectDataD() {
        int i = 0;
        List _editvalues =getEditValuesD();
        _editvalues.clear();
        JDBCcon JDBCcon = new JDBCcon();
        try {

            String sql =
            "select liucdzb.id liucdzbid,liucdzjsb.id liucdzjsbid,liucjsb.mingc,leibztb1.mingc qianqzt,leibztb2.mingc houjzt,liucdzb.mingc caozmc,liucdzb.dongz \n" +
            "from liucdzb,liucdzjsb,liucjsb,liucztb liucztb1,liucztb liucztb2,leibztb leibztb1,leibztb leibztb2\n" + 
            "where liucztb1.liucb_id="+getliucSelectValue().getId()+" and liucdzb.id=liucdzjsb.liucdzb_id and liucjsb.id=liucdzjsb.liucjsb_id and liucztb1.id=liucdzb.liucztqqid\n" + 
            "and liucztb2.id=liucdzb.liuczthjid and leibztb1.id=liucztb1.leibztb_id and leibztb2.id=liucztb2.leibztb_id\n" + 
            "order by liucdzbid";

            ResultSet rs = JDBCcon.getResultSet(sql);
            while (rs.next()) {
                long liucdzbid = rs.getLong("liucdzbid");
                long liucdzjsbid = rs.getLong("liucdzjsbid");
                String mmingc = rs.getString("mingc");
                String qianqzt = rs.getString("qianqzt");
                String houjzt = rs.getString("houjzt");
                String caozmc = rs.getString("caozmc");
                String dongz = rs.getString("dongz");
                _editvalues.add(new Liucdzbbean(++i,liucdzbid , liucdzjsbid,mmingc,qianqzt,houjzt,caozmc,dongz));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCcon.Close();
        }
        getzhuangt_idModels();//选择流程所对应的状态
        _editTableRowD = -1;
        return ;
    }
////  
    public IDropDownBean getliucSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getliucSelectModel().getOption(0));
  		}
       return  ((Visit) getPage().getVisit()).getDropDownBean4();
    }
 
    public void setliucSelectValue(IDropDownBean Value) {
    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean4()!=null){
	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean4().getId()){
	    		((Visit) getPage().getVisit()).setboolean1(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean1(false);
	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean4(Value);
    	}
    }
    public void setliucSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel4(value);
    }

    public IPropertySelectionModel getliucSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
            getliucSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }

    public void getliucSelectModels() {
       String sql=
    	   "select id,mingc from liucb";
        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(sql,"请选择")) ;
        return ;
    }
    
}
