package com.zhiren.jt.meiysgl.rucsl;

import java.sql.ResultSet;
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
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;

import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


public class Rucsl extends BasePage implements PageValidateListener{
    private String _msg;

    public void setMsg(String _value) {
        _msg = _value;
    }

    public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }

    protected void initialize() {
        super.initialize();
        _msg = "";
    }

    public int getEditTableRow() {
        return ((Visit) getPage().getVisit()).getInt1();
    }

    public void setEditTableRow(int _value) {
        ((Visit) getPage().getVisit()).setInt1(_value);
    }

    private Date _daohrq;

    public Date getDaohrq() {
        if (_daohrq == null) {
            _daohrq = new Date();
        }
        return _daohrq;
    }

    public void setDaohrq(Date value) {
        _daohrq = value;
    }

    private void Refurbish() {
        getSelectData();
    }

    /**
     * Insert 添加的时候 选择默认到站、厂别、计划口径、车别、到货日期。
     * 
     */
    private void Insert() {
        List _value = getEditValues();
        if (_value == null) {
            _value = new ArrayList();
        }
        if (_value.isEmpty()) {
            Rucslbean bp;// = new Baipxxbean();
            String daoz = "";
            String changb = "";
            String jihkjb = "";
            if (getIDaoz_idModel() != null) {
                if (getIDaoz_idModel().getOptionCount() > 1) {
                    daoz = ((IDropDownBean) getIDaoz_idModel().getOption(1))
                            .getValue();
                    // bp.setDaoz_id(((IDropDownBean)_IDaoz_idModel.getOption(1)).getValue());
                }
            }
        	JDBCcon con = new JDBCcon();
        	String sql="select mingc from diancxxb where id="+((Visit) getPage().getVisit()).getDiancxxb_id();
            ResultSet rs = con.getResultSet(sql);
        	try{
	        	if(rs.next()){
	        		changb = rs.getString("mingc");
	        	}
	        	rs.close();
        	}catch(Exception e){
        		setMsg("获取厂别信息失败！请在基础信息管理中添加厂别信息！");
        		e.printStackTrace();
        		return;
        	}
            if (getIChangbb_idModel() != null && "".equals(changb)) {
                if (getIChangbb_idModel().getOptionCount() > 1) {
                    changb = ((IDropDownBean) getIChangbb_idModel()
                            .getOption(1)).getValue();
                    // bp.setChangbb_id(((IDropDownBean)_IChangbb_idModel.getOption(1)).getValue());
                }
            }
            if (getIJihkjb_idModel() != null) {
                if (getIJihkjb_idModel().getOptionCount() > 0) {
                    jihkjb = ((IDropDownBean) getIJihkjb_idModel().getOption(0))
                            .getValue();
                    // bp.setJihkjb_id(((IDropDownBean)_IJihkjb_idModel.getOption(0)).getValue());
                }
            }

            if (getRows() > 0) {
                for (int m = 0; m < getRows(); m++) {
                    bp = new Rucslbean(_value.size() + 1);
                    
                    bp.setDaohrq(getDaohrq());
                    bp.setDaoz_id(daoz);
                    bp.setDiancxxb_id(changb);
                    if (m == 0) {
                        bp.setJihkjb_id(jihkjb);
                    }
                    _value.add(bp);
                }
            } else if (getRows() == 0) {
                bp = new Rucslbean(_value.size() + 1);
              
                bp.setDaohrq(getDaohrq());
                bp.setDaoz_id(daoz);
                bp.setDiancxxb_id(changb);
                bp.setJihkjb_id(jihkjb);
                _value.add(bp);
            }
        } else {
            Rucslbean bp = (Rucslbean) _value.get(_value.size() - 1);
            // String fahdw = bp.getFahdwb_id();
            // String meikxx = bp.getMeikxxb_id();
            // String faz = bp.getFaz_id();
            // String pinz = bp.getRanlpzb_id();
            // Date fahrq = bp.getFahrq();
            Date daohrq = bp.getDaohrq();
            String jihkj = bp.getJihkjb_id();
            // String cheph = bp.getCheph();
            String daoz = bp.getDaoz_id();
           
            String chec = bp.getChec();
            String yunsfs=bp.getYunsfsb_id();
            String pinz=bp.getRanlpzb_id();
            String changb = bp.getDiancxxb_id();
            if (getRows() > 0) {
                for (int m = 0; m < getRows(); m++) {
                    Rucslbean bptmp = new Rucslbean(_value.size() + 1);
                    // bptmp.setFahdwb_id(fahdw);
                    // bptmp.setMeikxxb_id(meikxx);
                    // bptmp.setFaz_id(faz);
                    // bptmp.setRanlpzb_id(pinz);
                    // bptmp.setFahrq(fahrq);
                    bptmp.setDaohrq(daohrq);
                    if (m == 0) {
                        bptmp.setJihkjb_id(jihkj);
                    }
                    bptmp.setDaoz_id(daoz);
                    bptmp.setYunsfsb_id(yunsfs);
                    bptmp.setRanlpzb_id(pinz);
                    bptmp.setChec(chec);
                    bptmp.setDiancxxb_id(changb);
                    _value.add(bptmp);
                }
            } else if (getRows() == 0) {
                Rucslbean bptmp = new Rucslbean(_value.size() + 1);
                // bptmp.setFahdwb_id(fahdw);
                // bptmp.setMeikxxb_id(meikxx);
                // bptmp.setFaz_id(faz);
                // bptmp.setRanlpzb_id(pinz);
                // bptmp.setFahrq(fahrq);
                bptmp.setDaohrq(daohrq);
                bptmp.setJihkjb_id(jihkj);
                bptmp.setDaoz_id(daoz);
               
               
                bptmp.setChec(chec);
                bptmp.setDiancxxb_id(changb);
                _value.add(bptmp);
            }
        }
        setEditTableRow(0);
        setEditValues(_value);
        setRows(1);
    }

    /**
     * 删除fahbtmp 中的一条记录
     * 
     */
    private void Delete() {
    	List _value = getEditValues();
    	if (_value != null && !_value.isEmpty()) {
    		JDBCcon con = new JDBCcon();
        	con.setAutoCommit(false);
//	    	int size = _value.size();
	    	for(int introw=0;introw<_value.size();introw++){
	    		if(((Rucslbean) _value.get(introw)).getFlag()){
	                int flag = con.getDelete(" Delete  From fahbtmp Where guohsj is null and id="
	                        + ((Rucslbean) _value.get(introw)).getId());
	                if(flag==-1){
	                	setMsg("删除数据失败！");
	                	con.rollBack();
	                	getSelectData();
	                	return;
	                }else
	                	if(flag==0){
	                		if(((Rucslbean) _value.get(introw)).getId()!=0){
	                			setMsg("该组数据中包含已经检斤的车皮！\n请将检斤数据回退后再次删除！");
		                		getSelectData();
		                		con.rollBack();
		                		return;
	                		}
	                	}
	                _value.remove(introw--);
	    		}
	    	}
	    	setEditTableRow(getEditValues().size() - 1);
            for (int m = 0; m < getEditValues().size(); m++) {
                ((Rucslbean) _value.get(m)).setXuh(m + 1);
            }
            con.commit();
    	}
    }

    /**
     * 保存 循环页面数据List 判断如果是新增 插入一条记录 如果是修改 更新一条记录
     */
    private void Save() {
       List _list = ((Visit) getPage().getVisit()).getList1();
        if (_list == null || _list.isEmpty()) {
            setMsg("记录不存在！");
            return;
        }
        JDBCcon con = new JDBCcon();
        con.setAutoCommit(false);
        try {
            long diancxxb_id = ((Visit) getPage().getVisit()).getDiancxxb_id();
            long fahdwb_id;
            long meikxxb_id;
            long meijb_id;
            long ranlpzb_id;
            long faz_id;
            long daoz_id;
            long jihkjb_id;
            long changbb_id;
            String fahrqstr;
            String daohrqstr;
            String chephstr;
            double pizd;
            double biaozd;
            String Chec;
            String Daozch;
            String Piaojh;
            String Yuanmkdw;
            String Yunsdw;
            String Beiz;
            String lury = ((Visit) getPage().getVisit()).getRenymc();
            for (int i = 0; i < _list.size(); i++) {
                int j = i + 1;
                long _id = ((Rucslbean) _list.get(i)).getId();
                String Sql = "";
                
               //判断煤矿地区是否为空
               
                fahdwb_id = ((IDropDownModel)getIFahdwb_idModel()).getBeanId(((Rucslbean) _list.get(i)).getFahdwb_id());
                
                System.out.println(fahdwb_id);
                if (fahdwb_id == -1) {
                    setMsg("第" + j + "行缺少发货单位！");
                    System.out.println("第" + j + "行缺少发货单位！");
                    con.rollBack();
                    return;
                }
                //判断煤矿是否为空
                IDropDownModel mkModels = getAllMeikxxbModels(fahdwb_id);
                meikxxb_id = mkModels.getBeanId(((Rucslbean) _list.get(i)).getMeikxxb_id());
                if (meikxxb_id == -1) {
                    setMsg("第" + j + "行缺少煤矿单位！");
                    System.out.println("第" + j + "行缺少煤矿单位！");
                    con.rollBack();
                    return;
                }
                //判断煤价表是否有值
                meijb_id=(long)((Rucslbean) _list.get(i)).getMeijb_id();
                if (meijb_id==-1) {
                	meijb_id =0;
                }
                //判断发站是否为空
                faz_id = ((IDropDownModel)getIFaz_idModel()).getBeanId( ((Rucslbean) _list.get(i)).getFaz_id());
                if (faz_id == -1) {
                    setMsg("第" + j + "缺少发站！");
                    System.out.println("第" + j + "缺少发站！");
                    con.rollBack();
                    return;
                }
                //判断燃料品种是否为空
                ranlpzb_id =((IDropDownModel)getIRanlpzb_idModel()).getBeanId(((Rucslbean) _list.get(i)).getRanlpzb_id());
                if (ranlpzb_id == -1) {
                    setMsg("第" + j + "缺少燃料品种！");
                    System.out.println("第" + j + "缺少燃料品种！");
                    con.rollBack();
                    return;
                }
                //判断到站是否为空
                daoz_id = ((IDropDownModel)getIRanlpzb_idModel()).getBeanId( ((Rucslbean) _list.get(i)).getDaoz_id());
                 if (daoz_id == -1) {
                    setMsg("第" + j + "缺少到站！");
                    System.out.println("第" + j + "缺少到站！");
                    con.rollBack();
                    return;
                }
                 //根据煤矿得到计划口径并判断计划口径表是否为空
                jihkjb_id =((IDropDownModel)getIRanlpzb_idModel()).getBeanId(((Rucslbean) _list.get(i)).getJihkjb_id());
                if (jihkjb_id == -1) {
                    String jhkjSql = "select jihkjb_id from meikxxb where jihkjb_id <>0 and jihkjb_id<>-1 and id="
                            + meikxxb_id;
                    ResultSet jhkj = con.getResultSet(jhkjSql);
                    try {
                        if (jhkj.next()) {
                            jihkjb_id = jhkj.getLong("jihkjb_id");
                        }
                        jhkj.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        con.rollBack();
                        setMsg("数据出现异常！");
                        return;
                    }
                }
                if (jihkjb_id == -1) {
                    setMsg("第" + j + "行缺少计划口径！");
                    System.out.println("第" + j + "行缺少计划口径！");
                    con.rollBack();
                    return;
                }
                //判断长别是否为空
                changbb_id =((IDropDownModel)this.getIChangbb_idModel()).getBeanId(((Rucslbean) _list.get(i)).getDiancxxb_id());	
                if (changbb_id == -1) {
                    setMsg("第" + j + "行缺少厂别!");
                    System.out.println("第" + j + "行缺少厂别!");
                    con.rollBack();
                    return;
                }
                //判断发货日期是否为空
                Date fhrqDate = ((Rucslbean) _list.get(i)).getFahrq();
                if (fhrqDate == null || "".equals(fhrqDate)) {
                    setMsg("第" + j + "行缺少发货日期!");
                    System.out.println("第" + j + "行缺少发货日期!");
                    con.rollBack();
                    return;
                }
                fahrqstr = DateUtil.FormatDate(((Rucslbean) _list.get(i)).getFahrq());
                if (fahrqstr == null || "".equals(fahrqstr)) {
                    setMsg("第" + j + "行缺少发货日期!");
                    System.out.println("第" + j + "行缺少发货日期!");
                    con.rollBack();
                    return;
                }
                //判断到货日期是否为空
                daohrqstr = DateUtil.FormatTime(((Rucslbean) _list.get(i)) .getDaohrq());
                if (daohrqstr == null || "".equals(daohrqstr)) {
                    setMsg("第" + j + "行缺少到货日期!");
                    System.out.println("第" + j + "行缺少到货日期!");
                    con.rollBack();
                    return;
                }
               
                pizd = ((Rucslbean) _list.get(i)).getPiz();
                biaozd = ((Rucslbean) _list.get(i)).getBiaoz();
                if (getReg("数量", "BIAOZ", biaozd, con)) {
                    setMsg("第" + j + "行标重超出合理范围!");
                    System.out.println("第" + j + "行标重超出合理范围!");
                    con.rollBack();
                    return;
                }
                Chec = ((Rucslbean) _list.get(i)).getChec();
                if (Chec == null) {
                    Chec = "";
                }
                double maoz=((Rucslbean) _list.get(i)).getMaoz();
                double piz=((Rucslbean) _list.get(i)).getPiz();
                double jingz=((Rucslbean) _list.get(i)).getJingz();
                double biaoz=((Rucslbean) _list.get(i)).getJingz();
                double yingk=((Rucslbean) _list.get(i)).getYingk();
                double yuns=((Rucslbean) _list.get(i)).getYuns();
                double yunsl=((Rucslbean) _list.get(i)).getYunsl();
                double koud=((Rucslbean) _list.get(i)).getKoud();
                double koussl=((Rucslbean) _list.get(i)).getKoussl();
                double kouz=((Rucslbean) _list.get(i)).getKouz();
                double meigzzl=((Rucslbean) _list.get(i)).getMeigzzl();
                double ches=((Rucslbean) _list.get(i)).getChes();
                double tiaozbz=((Rucslbean) _list.get(i)).getTiaozbz();
                
                
                Beiz = ((Rucslbean) _list.get(i)).getBeiz();
                if (Beiz == null) {
                    Beiz = "";
                }
                if (_id == 0) {
                		String ID=MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id());
                    Sql = " insert  into fahb (ID,YUANID,DIANCXXB_ID,FAHDWB_ID,MEIKXXB_ID,MEIJB_ID,FAHRQ,DAOHRQ,CHEC,FAZ_ID,DAOZ_ID,JIHKJB_ID," +
                    		"MAOZ,PIZ,JINGZ,BIAOZ,YINGK,YUNS,YUNSL,KOUD,KOUSSL,KOUZ,MEIGZZL,CHES,TIAOZBZ,RUZRQ,YANSBH,ZHILB_ID,JIESB_ID,LIE_ID," +
                    		"RANLPZB_ID,YUANDZ_ID,YUANSHDWB_ID,KUANGFZLB_ID,KUANGFJS_ID,SHENHB_SHENHID,BEIZ,YUNSFSB_ID) values("
                            +  ID
                            + ","
                            +  ID
                            + ","
                            + diancxxb_id
                            + ","
                            + fahdwb_id
                            + ","
                            + meikxxb_id
                            + ","
                            + meijb_id
                            + ",to_date('"
                            + fahrqstr
                            + "','yyyy-mm-dd'),to_date('"
                            + daohrqstr
                            + "','yyyy-mm-dd hh24:mi:ss'),'"
                            + Chec
                            + "',"
                            + faz_id
                            + ","
                            + daoz_id
                            + ","
                            + jihkjb_id
                            + ","
                            + maoz
                            + ","
                            + piz
                            + ","
                            + jingz
                            + ","
                            + biaoz
                            + ","
                            + yingk
                            + ","
                            + yuns
                            + ","
                            + yunsl
                            + ","
                            + koud
                            + ","
                            + koussl
                            + ","
                            + kouz
                            + ","
                            + meigzzl
                            + ","
                            + ches
                            + ","
                            + ""
                            + ","
                            + ""
                            + ","
                            + ""
                            + ","
                            + 0
                            + ","
                            + 0
                            + ","
                            + 0
                            + ","
                            + ranlpzb_id
                            + ","
                            + daoz_id
                            + ","
                            + diancxxb_id
                            + ","
                            + ""
                            + ","
                            + ""
                            + ","
                            + ""
                            + ","
                            + Beiz
                            + ","
                            + ((IDropDownModel)this.getYunsfsb_idModel()).getBeanId(((Rucslbean) _list.get(i)).getYunsfsb_id())
                            + ")";
                    System.out.println("插入"+Sql);
                    int flag = con.getInsert(Sql);
                    if(flag == -1) {
                    	setMsg("数据插入失败！");
                    	System.out.println("Baipxx row:437 数据插入失败！");
                    	con.rollBack();
                    	return;
                    }
                } else {
                    
                        Sql = " update   fahb  set  DIANCXXB_ID="
                                + diancxxb_id
                                + ",FAHDWB_ID="
                                + fahdwb_id
                                + ",MEIKXXB_ID="
                                + meikxxb_id
                                + ",FAHRQ=to_date('"
                                + fahrqstr
                                + "','yyyy-mm-dd'),DAOHRQ=to_date('"
                                + daohrqstr
                                + "','yyyy-mm-dd hh24:mi:ss'),CHEC='"
                                + Chec
                                + "',FAZ_ID="
                                + faz_id
                                + ",DAOZ_ID="
                                + daoz_id
                                + ",JIHKJB_ID="
                                + jihkjb_id
                                + ",BIAOZ="
                                + biaoz
                                + ",YINGK="
                                + yingk
                                + ",YUNS="
                                + yuns
                                + ",KOUD="
                                + koud
                                + ",CHES="
                                + ches
                                + ",YUNSFSB_ID="
                                + ((IDropDownModel)this.getYunsfsb_idModel()).getBeanId(((Rucslbean) _list.get(i)).getYunsfsb_id())
                                + ",RANLPZB_ID="
                                + ranlpzb_id
                                +",BEIZ='"
                                + Beiz + "' where id=" + _id;
                        System.out.print("更新:"+Sql);
                        int flag = con.getUpdate(Sql);
                        if(flag == -1) {
                        	setMsg("数据更新失败！");
                        	System.out.println("Baipxx row:498 数据更新失败！");
                        	con.rollBack();
                        	return;
                        
                    }
                }
            }
            con.commit();
        } catch (Exception e) {
            setMsg("发生异常!");
            System.out.println("Baipxx row:507 发生异常");
            con.rollBack();
            return;
        } finally {
            con.Close();
        }
        getSelectData();
        setMsg("存储成功！");
        
    }

    private boolean _RefurbishChick = false;

    public void RefurbishButton(IRequestCycle cycle) {
        _RefurbishChick = true;
    }

    private boolean _InsertChick = false;

    public void InsertButton(IRequestCycle cycle) {
        _InsertChick = true;
    }

    private boolean _DeleteChick = false;

    public void DeleteButton(IRequestCycle cycle) {
        _DeleteChick = true;
    }

    private boolean _SaveChick = false;

    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }

    public void submit(IRequestCycle cycle) {
        if (_RefurbishChick) {
            _RefurbishChick = false;
            Refurbish();
        }
        if (_InsertChick) {
            _InsertChick = false;
            Insert();
        }
        if (_DeleteChick) {
            _DeleteChick = false;
            Delete();
        }
        if (_SaveChick) {
            _SaveChick = false;
            Save();
        }
    }

    private Rucslbean _EditValue;

    public List getEditValues() {
        return ((Visit) getPage().getVisit()).getList1();
    }

    public void setEditValues(List editList) {
        ((Visit) getPage().getVisit()).setList1(editList);
    }

    public Rucslbean getEditValue() {
        return _EditValue;
    }

    public void setEditValue(Rucslbean EditValue) {
        _EditValue = EditValue;
    }

    /**
     * 刷新 刷新现有核对标志为0 并且到货日期 为用户所选时间的 fahbtmp 数据在页面显示
     * 
     */
    public void getSelectData() {
        List _editvalues = new ArrayList();
        JDBCcon con = new JDBCcon();
        try {
            getIFahdwb_idModels();
            getIMeikxxb_idModels();
            getIChebModels();
            getIRanlpzb_idModels();
            getIChangbb_idModels();
            getIJihkjb_idModels();
            getIFaz_idModels();
            getIDaoz_idModels();
            getIYuandz_idModels();
            getIYuanshdwb_idModels();
            /*
            String daohrq;
            if (getDaohrq() != null) {
                daohrq = DateUtil.FormatTime(getDaohrq());
            } else {
                daohrq = DateUtil.FormatTime(new Date());
            }*/
         /*   String sql = "select f.*,dq.meikdqmc as meikdq,k.meikdwmc as meikdw,"
                    + "pz.pinz as pinz,cb.mingc as changb,kj.mingc as kouj, "
                    + "(select cz.jianc from chezxxb cz where id = f.faz_id) as faz,"
                    + "(select cz.jianc from chezxxb cz where id = f.daoz_id) as daoz"
                    + " from fahbtmp f, meikdqb dq,meikxxb k,ranlpzb pz,changbb cb,jihkjb kj"
                    + " where hedbz=0 and "
                    + " f.meikxxb_id = k.id(+) and k.meikdqb_id =dq.id(+) and f.ranlpzb_id = pz.id(+) "
                    + " and f.changbb_id = cb.id(+) and f.jihkjb_id = kj.id(+) and "
                    + "daohrq=to_date('"
                    + daohrq
                    + "','yyyy-mm-dd hh24:mi:ss')  order by f.id ";
                    */
            String sql=" select f.* ,g.mingc as meikdq,m.mingc as meikdw,p.mingc as pinz,j.mingc as kouj,"
					  +"(select cz.mingc from chezxxb cz where id = f.faz_id) as faz,"
					  +"(select cz.mingc from chezxxb cz where id = f.daoz_id) as daoz"
					  +" from fahb f,gongysb g,meikxxb m,pinzb p,jihkjb j"
					  +" where f.fahdwb_id=g.id(+)"
					  +" and   f.meikxxb_id=m.id(+)"
					  +" and f.ranlpzb_id=p.id(+)"
					  +" and f.jihkjb_id=j.id(+)"
					  +" and f.daohrq=to_date('2007-12-24', 'yyyy-mm-dd')"
					  +" order by f.id";
           // System.out.println(sql);
            ResultSet rs = con.getResultSet(sql);
            int i = 1;
            while (rs.next()) {
            	int xuh=i;
            	int id=rs.getInt("id");
            	String diancxxb_id=((IDropDownModel)this.getIChangbb_idModel()).getBeanValue(rs.getInt("diancxxb_id"));
                String fahdwb_id = ((IDropDownModel)this.getIFahdwb_idModel()).getBeanValue(rs.getInt("fahdwb_id"));
                String meikxxb_id = ((IDropDownModel)this.getIMeikxxb_idModel()).getBeanValue(rs.getInt("meikxxb_id"));
                double meijb_id = rs.getDouble("meijb_id");
                Date fahrq = rs.getDate("FAHRQ");
                Date daohrq = rs.getDate("DAOHRQ");
                String chec = rs.getString("chec");
                String faz_id =((IDropDownModel)this.getIFaz_idModel()).getBeanValue(rs.getInt("faz_id"));
                String daoz_id =((IDropDownModel)this.getIDaoz_idModel()).getBeanValue(rs.getInt("daoz_id"));
                String jihkjb_id = ((IDropDownModel)this.getIJihkjb_idModel()).getBeanValue(rs.getInt("jihkjb_id"));
                double maoz=rs.getDouble("MAOZ");
                double piz = rs.getDouble("PIZ");
                double jingz=rs.getDouble("jingz");
                double biaoz=rs.getDouble("biaoz");
                double yingk=rs.getDouble("yingk");
                double yuns=rs.getDouble("yuns");
                double yunsl=rs.getDouble("yunsl");
                double koud=rs.getDouble("koud");
                double koussl=rs.getDouble("koussl");
                double kouz=rs.getDouble("kouz");
                double meigzzl=rs.getDouble("meigzzl");
                double ches=rs.getDouble("ches");
                double tiaozbz=rs.getDouble("tiaozbz");
                Date ruzrq=rs.getDate("ruzrq");
                String yansbh=rs.getString("yansbh");
                double  zhilb_id = rs.getDouble("ZHILB_ID");
                double jiesb_id=rs.getDouble("jiesb_id");
                double lie_id=rs.getDouble("lie_id");
                String yunsfs=((IDropDownModel)this.getYunsfsb_idModel()).getBeanValue(rs.getInt("yunsfsb_id"));
                String ranlpzb_id =((IDropDownModel)this.getIRanlpzb_idModel()).getBeanValue(rs.getInt("ranlpzb_id"));
                double  yuandz_id =rs.getDouble("yuandz_id");
                String yuanshdwb_id =((IDropDownModel)this.getIYuanshdwb_idModel()).getBeanValue(rs.getInt("yuanshdwb_id"));
                double kuangfzlb_id=rs.getDouble("kuangfzlb_id");
                double kuangfjs_id=rs.getDouble("kuangfjs_id");
                double shenhb_shenhid=rs.getDouble("shenhb_shenhid");
                String beiz = rs.getString("BEIZ");
             
                
                _editvalues.add(new Rucslbean(xuh,id,  diancxxb_id,  fahdwb_id,  
                		meikxxb_id, meijb_id, fahrq,  daohrq,  chec,  faz_id,
                		 daoz_id, jihkjb_id,  maoz,  piz, 
                		 jingz,  biaoz,  yingk,  yuns,  yunsl,
                		 koud,  koussl,  kouz,  meigzzl, 
                		 ches,  tiaozbz,  ruzrq,  yansbh, 
                		 zhilb_id,  jiesb_id,  lie_id,  yunsfs, 
                		 ranlpzb_id,  yuandz_id,  yuanshdwb_id,  kuangfzlb_id, 
                		 kuangfjs_id,  shenhb_shenhid,  beiz));
                i++;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        ((Visit) getPage().getVisit()).setList1(_editvalues);

    }

    // / 下拉框设置开始
    // 发货单位下拉框
    public IDropDownBean getFahdwb_idValue() {
        return ((Visit) getPage().getVisit()).getDropDownBean1();
    }

    private boolean Fahdwchange = false;

    public void setFahdwb_idValue(IDropDownBean value) {
        if (getFahdwb_idValue() != null) {
            if (!getFahdwb_idValue().equals(value)) {
                Fahdwchange = true;
            }
        }
        ((Visit) getPage().getVisit()).setDropDownBean1(value);
    }

    public IPropertySelectionModel getIFahdwb_idModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
            getIFahdwb_idModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel1();
    }

    public void setIFahdwb_idModel(IPropertySelectionModel value) {
        ((Visit) getPage().getVisit()).setProSelectionModel1(value);
    }

    public void getIFahdwb_idModels() {
        String sql = " select g.id,g.mingc from gongysb g ,gongysdcglb gd where gd.gongysb_id=g.id and gd.diancxxb_id="+((Visit) getPage().getVisit()).getDiancxxb_id()+" order by g.xuh ";
        setIFahdwb_idModel(new IDropDownModel(sql,null));
    }

    // 煤矿单位下拉框
    private boolean meikdwchange = false;

    public IDropDownBean getMeikxxb_idValue() {
        return ((Visit) getPage().getVisit()).getDropDownBean2();
    }

    public void setMeikxxb_idValue(IDropDownBean value) {
        if (getMeikxxb_idValue() != null) {
            if (!getMeikxxb_idValue().equals(value)) {
                meikdwchange = true;
            }
        }
        ((Visit) getPage().getVisit()).setDropDownBean2(value);
    }

    public IPropertySelectionModel getIMeikxxb_idModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
            getIMeikxxb_idModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }

    public void setIMeikxxb_idModel(IPropertySelectionModel value) {
        ((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public void getIMeikxxb_idModels() {
        long id = -2;
        if (getFahdwb_idValue() != null) {
            id = getFahdwb_idValue().getId();
        }
       /* String sql = "select m.id, m.mingc from meikxxb m,gongysmkglb gm,gongysb g  where m.leix = '煤' "+
        	" and gm.meikxxb_id=m.id and gm.gongysb_id=g.id"+
        	" and g.id =" + id;*/
        String sql="select m.id, m.mingc from meikxxb m where m.leix='煤'";
             
        setIMeikxxb_idModel(new IDropDownModel(sql,null));
    }

    // 所有煤矿下拉框
    public IDropDownModel getAllMeikxxbModels(long gongsybid) {
        String sql = "select m.id,m.mingc  from gongysmkglb g,meikxxb m where g.meikxxb_id=m.id and g.gongysb_id="+gongsybid;
        return new IDropDownModel(sql);
    }

    // 车别下拉框
    private String _ChebValue;

    public String getChebValue() {
        return _ChebValue;
    }

    public void setChebValue(String Value) {
        _ChebValue = Value;
    }

    private static IPropertySelectionModel _IChebModel;

    public void setIChebModel(IPropertySelectionModel value) {
        _IChebModel = value;
    }

    public IPropertySelectionModel getIChebModel() {
        if (_IChebModel == null) {
            getIChebModels();
        }
        return _IChebModel;
    }

    public IPropertySelectionModel getIChebModels() {
        List listCheb = new ArrayList();
        listCheb.add(new IDropDownBean(-1, "请选择"));
        listCheb.add(new IDropDownBean(0, "路车"));
        listCheb.add(new IDropDownBean(1, "自备车"));
        _IChebModel = new IDropDownModel(listCheb);
        return _IChebModel;
    }

    // 燃料品种下拉框

    public IDropDownBean getRanlpzb_idValue() {
        return ((Visit) getPage().getVisit()).getDropDownBean3();
    }

    public void setRanlpzb_idValue(IDropDownBean value) {
        ((Visit) getPage().getVisit()).setDropDownBean3(value);
    }

    public IPropertySelectionModel getIRanlpzb_idModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
            getIRanlpzb_idModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel3();
    }

    public void setIRanlpzb_idModel(IPropertySelectionModel value) {
        ((Visit) getPage().getVisit()).setProSelectionModel3(value);
    }

    public void getIRanlpzb_idModels() {
        String sql = "select id ,mingc from pinzb order by xuh";// where
        // leix ='煤'
        setIRanlpzb_idModel(new IDropDownModel(sql,null));
    }

    // 厂别下拉框
    public IDropDownBean getChangbb_idValue() {
        return ((Visit) getPage().getVisit()).getDropDownBean4();
    }

    public void setChangbb_idValue(IDropDownBean value) {
        ((Visit) getPage().getVisit()).setDropDownBean4(value);
    }

    public IPropertySelectionModel getIChangbb_idModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
            getIChangbb_idModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }

    public void setIChangbb_idModel(IPropertySelectionModel value) {
        ((Visit) getPage().getVisit()).setProSelectionModel4(value);
    }

    public void getIChangbb_idModels() {
        String sql = "select id ,mingc  from diancxxb where id="+((Visit) getPage().getVisit()).getDiancxxb_id() +"";
        setIChangbb_idModel(new IDropDownModel(sql));
    }

    // 计划口径表下拉框

    public IDropDownBean getJihkjb_idValue() {
        return ((Visit) getPage().getVisit()).getDropDownBean5();
    }

    public void setJihkjb_idValue(IDropDownBean value) {
        ((Visit) getPage().getVisit()).setDropDownBean5(value);
    }

    public IPropertySelectionModel getIJihkjb_idModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
            getIJihkjb_idModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel5();
    }

    public void setIJihkjb_idModel(IPropertySelectionModel value) {
        ((Visit) getPage().getVisit()).setProSelectionModel5(value);
    }

    public void getIJihkjb_idModels() {
        String sql = "select id ,mingc as kouj  from jihkjb order by xuh";
        setIJihkjb_idModel(new IDropDownModel(sql,null));
    }

    // 发站下拉框
    public IDropDownBean getFaz_idValue() {
        return ((Visit) getPage().getVisit()).getDropDownBean6();
    }

    public void setFaz_idValue(IDropDownBean value) {
        ((Visit) getPage().getVisit()).setDropDownBean6(value);
    }

    public IPropertySelectionModel getIFaz_idModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
            getIFaz_idModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel6();
    }

    public void setIFaz_idModel(IPropertySelectionModel value) {
        ((Visit) getPage().getVisit()).setProSelectionModel6(value);
    }

    public void getIFaz_idModels() {
        String sql = " select id,mingc  from chezxxb order by xuh ";
        setIFaz_idModel(new IDropDownModel(sql,null));
    }

    // 到站下拉框
    public IDropDownBean getDaoz_idValue() {
        return ((Visit) getPage().getVisit()).getDropDownBean7();
    }

    public void setDaoz_idValue(IDropDownBean value) {
        ((Visit) getPage().getVisit()).setDropDownBean7(value);
    }

    public void setIDaoz_idModel(IPropertySelectionModel value) {
        ((Visit) getPage().getVisit()).setProSelectionModel7(value);
    }

    public IPropertySelectionModel getIDaoz_idModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {
            getIDaoz_idModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel7();
    }

    public void getIDaoz_idModels() {
       String sql = "select id ,mingc   from chezxxb where id in (select  dd.chezxxb_id  from diancxxb d ,diancdzb dd where dd.diancxxb_id=d.id and d.id= "
                + ((Visit) getPage().getVisit()).getDiancxxb_id() + ")";
    	//System.out.println(sql);
        setIDaoz_idModel(new IDropDownModel(sql,null));
    }

    // 原到站下拉框
    public IDropDownBean getYuandz_idValue() {
        return ((Visit) getPage().getVisit()).getDropDownBean8();
    }

    public void setYuandz_idValue(IDropDownBean value) {
        ((Visit) getPage().getVisit()).setDropDownBean8(value);
    }

    public IPropertySelectionModel getIYuandz_idModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
            getIYuandz_idModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel8();
    }

    public void setIYuandz_idModel(IPropertySelectionModel value) {
        ((Visit) getPage().getVisit()).setProSelectionModel8(value);
    }

    public void getIYuandz_idModels() {
        String sql = "select id ,mingc   from chezxxb order by xuh";
        setIYuandz_idModel(new IDropDownModel(sql));
    }

    // 原收货单位下拉框
    public IDropDownBean getYuanshdwb_idValue() {
        return ((Visit) getPage().getVisit()).getDropDownBean9();
    }

    public void setYuanshdwb_idValue(IDropDownBean value) {
        ((Visit) getPage().getVisit()).setDropDownBean9(value);
    }

    public IPropertySelectionModel getIYuanshdwb_idModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel9() == null) {
            getIYuanshdwb_idModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel9();
    }

    public void setIYuanshdwb_idModel(IPropertySelectionModel value) {
        ((Visit) getPage().getVisit()).setProSelectionModel9(value);
    }

    public void getIYuanshdwb_idModels() {
        String sql = "select id ,mingc  from diancxxb where id="+((Visit) getPage().getVisit()).getDiancxxb_id();
        //System.out.print(sql);
        setIYuanshdwb_idModel(new IDropDownModel(sql));
    }
    // 运输方式下拉框
    public IDropDownBean getYunsfsb_idValue() {
        return ((Visit) getPage().getVisit()).getDropDownBean10();
    }

    public void setYunsfsb_idValue(IDropDownBean value) {
        ((Visit) getPage().getVisit()).setDropDownBean10(value);
    }

    public IPropertySelectionModel getYunsfsb_idModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {
            getYunsfsb_idModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel10();
    }

    public void setYunsfsb_idModel(IPropertySelectionModel value) {
        ((Visit) getPage().getVisit()).setProSelectionModel10(value);
    }

    public void getYunsfsb_idModels() {
        String sql = "select id,mingc from yunsfsb order by id desc ";
        setYunsfsb_idModel(new IDropDownModel(sql,null));
    }
    // /下拉框设置结束
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

    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
        Visit visit = (Visit) getPage().getVisit();
       
        if (!visit.getActivePageName().toString().equals(
                this.getPageName().toString())) {
            // 在此添加，在页面第一次加载时需要置为空的变量或方法
            visit.setActivePageName(getPageName().toString());
            visit.setList1(null);
            _daohrq=null;
            getSelectData();
            setEditTableRow(-1);
            visit.setProSelectionModel1(null);
            visit.setProSelectionModel2(null);
            visit.setProSelectionModel3(null);
            visit.setProSelectionModel4(null);
            visit.setProSelectionModel5(null);
            visit.setProSelectionModel6(null);
            visit.setProSelectionModel7(null);
            visit.setProSelectionModel8(null);
            visit.setProSelectionModel9(null);
            visit.setProSelectionModel10(null);
            visit.setDropDownBean1(null);
            visit.setDropDownBean2(null);
            visit.setDropDownBean3(null);
            visit.setDropDownBean4(null);
            visit.setDropDownBean5(null);
            visit.setDropDownBean6(null);
            visit.setDropDownBean7(null);
            visit.setDropDownBean8(null);
            visit.setDropDownBean9(null);
            visit.setDropDownBean10(null);
        }
        if (Fahdwchange) {
            Fahdwchange = false;
            getIMeikxxb_idModels();
        }
        if (meikdwchange) {
            meikdwchange = false;
            getIFaz_idModels();
        }
    }

    private int _rows = 1;

    public int getRows() {
        return _rows;
    }

    public void setRows(int value) {
        _rows = value;
    }

    public String getArrayScript() {
    	/*
        JDBCcon con = new JDBCcon();
        StringBuffer array = new StringBuffer();
        StringBuffer sbSql = new StringBuffer();
        long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
        try {
            array.append(" drop = new Array();\n ");
            array.append(" drop1 = new Array();\n");
            sbSql.append("select 0,g.id,g.mingc,nvl(g.piny,0) pinysy,g.xuh from gongysb g ,gongysdcglb gd,gongysmkglb gm where gd.diancxxb_id="+ diancid+" and gm.gongysb_id=gd.id order by g.xuh ");
            ResultSet sfrs = con.getResultSet(sbSql.toString());
            int i = 0;
            while (sfrs.next()) {
                array.append("drop1[");
                array.append(i++);
                array.append("] = new Array(\"");
                array.append(sfrs.getString(1));
                array.append("\",\"");
                array.append(sfrs.getString(2));
                array.append("\",\"");
                array.append(sfrs.getString(3));
                array.append("\",\"");
                array.append(sfrs.getString(4));
                array.append("\");\n");
            }
            sfrs.close();
            array.append(" drop[0] = new Array('FAHDWB_ID',drop1);\n");
            array.append(" drop2 = new Array();\n");
            sbSql = new StringBuffer();
            sbSql.append(" select distinct g.id,m.id,m.mingc,nvl(m.piny,0) pinysy");
			sbSql.append(" from meikxxb m,gongysb g,gongysmkglb gm,diancxxb d,gongysdcglb gd");
			sbSql.append(" where gm.meikxxb_id=m.id ");
			sbSql.append("		  and gm.gongysb_id=g.id");
			sbSql.append("		  and gd.gongysb_id=g.id");
			sbSql.append("	  and gd.diancxxb_id="+diancid +"");
            ResultSet kwjrs = con.getResultSet(sbSql.toString());
            i = 0;
            while (kwjrs.next()) {
                array.append("drop2[");
                array.append(i++);
                array.append("] = new Array(\"");
                array.append(kwjrs.getString(1));
                array.append("\",\"");
                array.append(kwjrs.getString(2));
                array.append("\",\"");
                array.append(kwjrs.getString(3));
                array.append("\",\"");
                array.append(kwjrs.getString(4));
                array.append("\");\n");
            }
            kwjrs.close();
            array.append(" drop[1] = new Array('MEIKXXB_ID',drop2);\n");
            array.append(" drop3 = new Array();\n");
            sbSql = new StringBuffer();
//            sbSql
//                    .append("select m.id,c.id,c.jianc from chezxxb c,meikxxb m,changkglb g where g.zhuangt=1 and g.diancxxb_id= "
//                            + diancid
//                            + " and g.meikxxb_id = m.id and m.quesfz = c.jianc");
            sbSql.append("select k.meikxxb_id,c.id,c.jianc,0 as pinysy ");
            sbSql.append(" from kuangzglb k,chezxxb c,changkglb g ");
            sbSql.append("where k.quesfz_id = c.id and g.zhuangt = 1 and ");
            sbSql.append(" g.meikxxb_id = k.meikxxb_id and g.diancxxb_id = ");
            sbSql.append(diancid);
            sbSql.append(" union select k.meikxxb_id,c.id,c.jianc,0 ");
            sbSql.append(" from kuangzglb k,chezxxb c,changkglb g ");
            sbSql.append("where k.qitfz_id = c.id and g.zhuangt = 1 and ");
            sbSql.append(" g.meikxxb_id = k.meikxxb_id and g.diancxxb_id = ");
            sbSql.append(diancid);
            ResultSet mkrs = con.getResultSet(sbSql.toString());
            i = 0;
            while (mkrs.next()) {
                array.append("drop3[");
                array.append(i++);
                array.append("] = new Array(\"");
                array.append(mkrs.getString(1));
                array.append("\",\"");
                array.append(mkrs.getString(2));
                array.append("\",\"");
                array.append(mkrs.getString(3));
                array.append("\",\"");
                array.append(mkrs.getString(4));
                array.append("\");\n");
            }
            mkrs.close();
            array.append(" drop[2] = new Array(\"FAZ_ID\",drop3);\n");
            ResultSet wsrs = con
                    .getResultSet("select jiesbz,mingc from jicxxb where leix= '位数' and diancxxb_id="
                            + diancid);
            int chepws = 7;
            int biaozws = 2;
            while (wsrs.next()) {
                if ("车皮号".equals(wsrs.getString("mingc"))) {
                    chepws = wsrs.getInt("jiesbz");
                }
                if ("标重".equals(wsrs.getString("mingc"))) {
                    biaozws = wsrs.getInt("jiesbz");
                }
            }
            wsrs.close();
            array.append("var chehws = " + chepws + ";\n");
            array.append("var biaozws = " + biaozws + ";\n");
            ResultSet mkkj = con.getResultSet("select m.meikdwmc, j.mingc "
                    + "from meikxxb m, jihkjb j, changkglb c "
                    + " where m.jihkjb_id = j.id(+) and m.id = c.meikxxb_id "
                    + " and c.zhuangt = 1 and c.diancxxb_id = " + diancid);
            i = 0;
            array.append("mkkj = new Array();\n");
            while (mkkj.next()) {
                array.append("mkkj[");
                array.append(i++);
                array.append("] = new Array(\"");
                array.append(mkkj.getString(1));
                array.append("\",\"");
                array.append(mkkj.getString(2));
                array.append("\");\n");
            }
            mkkj.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        con.Close();
       
        return array.toString();
         */
    	return  "";
    }

    private boolean getReg(String leix, String zhibmc, double shuz, JDBCcon con) {
        boolean flag = false;
//        JDBCcon con = new JDBCcon();
        String sql = "select id from shuzhlfwb where duixmc='" + zhibmc
                + "' and leix='" + leix + "' and diancxxb_id ="
                + ((Visit) getPage().getVisit()).getDiancxxb_id();
        try {
            ResultSet rs = con.getResultSet(sql);
            if (rs.next()) {
                sql = "select * from shuzhlfwb where id=" + rs.getLong("id")
                        + " and helsx>=" + shuz + " and helxx<=" + shuz
                        + " and diancxxb_id ="
                        + ((Visit) getPage().getVisit()).getDiancxxb_id();
                if (!con.getHasIt(sql)) {
                    flag = true;
                }
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            con.rollBack();
            setMsg("数值合理范围判断时出现异常！");
        } 
        return flag;
    }
}