package com.zhiren.dc.jilgl.baob;

import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.IDropDownModel;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.common.SysConstant;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

/*
 * 时间：2009-04-30
 * 作者：lzp
 * 关键字：数量汽车模块添加运输单位查询
 * 描述：
         根据包头二电的要求，添加一个可以根据时间查询某个矿各个承运单位时间段内的拉煤车数的汇
         总和各个车辆的明细，根据chepb中轻车时间查询。
*/

/*
 * 时间: 2009-11-12
 * 修改人：ww
 * 描述：
 * 		改成按zhongcsj统计
 */

/* 时间: 2010-1-30
* 修改人：cbd
* 描述：
* 		加入以煤矿单位查询的条件
*/
public class Huojj extends BasePage {
    public boolean getRaw() {
        return true;
    }

    private String userName = "";

    public void setUserName(String value) {
        userName = ((Visit) getPage().getVisit()).getRenymc();
    }

    public String getUserName() {
        return userName;
    }

    // private boolean reportShowZero(){
    // return ((Visit) getPage().getVisit()).isReportShowZero();
    // }

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

    // ***************设置消息框******************//
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
    // 绑定日期
    private String briq;

    public String getBRiq() {
        if (briq == null || briq.equals("")) {
            Calendar stra=Calendar.getInstance();
            stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
            stra.add(Calendar.MONTH,-1);
            briq = DateUtil.FormatDate(stra.getTime());
        }
        return briq;
    }
    // 绑定日期
    private String eriq;

    public String getERiq() {
        return eriq;
    }
    public long getDiancxxbId() {

        return ((Visit) getPage().getVisit()).getDiancxxb_id();
    }

    public boolean isJTUser() {
        return ((Visit) getPage().getVisit()).isJTUser();
    }

    // 得到单位全称
    public String getTianzdwQuanc(long gongsxxbID) {
        String _TianzdwQuanc = "";
        JDBCcon cn = new JDBCcon();

        try {
            ResultSet rs = cn.getResultSet(" select quanc from diancxxb where id=" + gongsxxbID);
            while (rs.next()) {
                _TianzdwQuanc = rs.getString("quanc");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cn.Close();
        }
        return _TianzdwQuanc;
    }



    public String getPrintTable() {
        setMsg(null);

                return getYunsdwcx_hz();


        }



    private String getYunsdwcx_hz() {
        JDBCcon cn = new JDBCcon();
        try {
            Report rt = new Report();
            String yunsdw = "";
            String meikdw = "";

            if (getYunsdwValue().getId() == -1) {
                yunsdw = "";
            } else {
                yunsdw = " and yunsdwb_id=" +
                        getYunsdwValue().getId();
            }

            if (getTreeid().equals("0")) {
                meikdw = "";
            } else {
                meikdw = "and meikxxb_id=" + getTreeid();
            }
/*
 * 改成按zhongcsj统计
 */

            StringBuffer sbsql = new StringBuffer();

            sbsql.append("select rownum as xuh,\n" +
                    "       cheph,\n" +
                    "       maoz,\n" +
                    "       piz,\n" +
                    "       jingz,\n" +
                    "      maoz-piz kuangfl,\n" +
                    "       0 changkc,\n" +
                    "       koud,\n" +
                    "       to_char(jianmsj,'yyyy-mm-dd hh24:mi:ss') jianmsj,\n" +
                    "        to_char(jianpsj,'yyyy-mm-dd hh24:mi:ss') jianpsj,\n" +
                    "       chengydw,\n" +
                    "       meikdwmc,\n" +
                    "       meikdqmc,\n" +
                    "       (zb.mingc) pinz\n" +
                    "  from qichjjbtmp qi\n" +
                    "  left join gongysb sb\n" +
                    "    on qi.meikdqb_id = sb.id\n" +
                    "  left join meikxxb mei\n" +
                    "    on qi.meikxxb_id = mei.id\n" +
                    "  left join pinzb zb\n" +
                    "    on qi.ranlpzb_id = zb.id where qi.jianmsj>=to_Date('"+getRiqi()+"','yyyy-mm-dd') and qi.jianmsj<=to_Date('"+getRiq2()+"','yyyy-mm-dd')");
            ResultSet rs = cn.getResultSet(sbsql.toString());
            String ArrHeader[][] = new String[1][14];
            ArrHeader[0] = new String[]{"序号", "车号", "毛重", "皮重", "净重", "矿发量", "厂矿差量", "扣重", "毛重时间", "皮重时间", "运输单位", "矿点", "单位", "品名"};
            int[] ArrWidth = new int[]{30, 50, 50, 50, 50, 50, 50, 50, 130, 130, 150, 60, 170, 30};

            rt.setTitle("汽   车   衡   称   重   记   录" , ArrWidth);
            rt.title.setRowHeight(2, 40);
            rt.title.setRowCells(2, 2, 14);
            rt.title.setRowCells(2, 7, 1);
            rt.setDefaultTitleLeft("日期：" + this.getBRiq(), 4);
            rt.setDefaultTitleRight("单位：吨", 3);
            rt.setBody(new Table(rs, 1, 0, 4));
            rt.body.setWidth(ArrWidth);
            rt.body.setColAlign(4, 1);
            rt.body.setHeaderData(ArrHeader);

            rt.body.ShowZero = false;
            rt.createDefautlFooter(ArrWidth);
            rt.body.setWidth(ArrWidth);
            rt.body.setHeaderData(ArrHeader);
            rt.body.setPageRows(40);
            rt.body.mergeFixedCols();
            rt.body.mergeFixedRow();
            rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
            rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);

            rt.setDefautlFooter(1, 5, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
                    Table.ALIGN_LEFT);
            rt.setDefautlFooter(9, 2, "审核：", Table.ALIGN_LEFT);
            rt.setDefautlFooter(12, 2, "制表:", Table.ALIGN_LEFT);
            _CurrentPage = 1;
            _AllPages = rt.body.getPages();
            if (_AllPages == 0) {
                _CurrentPage = 0;
            }
            cn.Close();
            return rt.getAllPagesHtml();
        }catch (Exception e){
            e.printStackTrace();
            cn.rollBack();
        }finally {
            cn.Close();
        }
        return null;
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
            if(getChaxfsValue().getId()==2)
            {
                getSelectData();
            }else{
                getSelectData1();
            }
        }
    }



    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
        Visit visit = (Visit) getPage().getVisit();

        if (!visit.getActivePageName().toString().equals(
                this.getPageName().toString())){
            visit.setActivePageName(getPageName().toString());

            visit.setProSelectionModel10(null);
            visit.setDropDownBean10(null);
            visit.setProSelectionModel3(null);
            visit.setDropDownBean3(null);
            visit.setProSelectionModel4(null);
            visit.setDropDownBean4(null);
            visit.setProSelectionModel5(null);
            visit.setDropDownBean5(null);
            visit.setProSelectionModel1(null);
            visit.setDropDownBean1(null);
            getGongysDropDownModels();
            getGongysDropDownModel();
            getYunsdwModels() ;
            setYunsdwValue(null) ;
            getYunsdwModel() ;
            getPoundModels();
            setPoundValue(null);
            getPoundModel();
            getChaxfsModels();
            setChaxfsValue(null);
            getChaxfsModel();
            setRiqi(null);
            setRiq2(null);
            visit.setboolean3(false);



        }
        if(getChaxfsValue().getValue().equals("煤矿  运输单位")||getChaxfsValue().getValue().equals(null)){
            getSelectData();
        }else{
            getSelectData1();
        }
    }

    // 绑定日期
    boolean riqichange = false;

    private String riqi;

    public String getRiqi() {
        if (riqi == null || riqi.equals("")) {
          riqi = DateUtil.FormatDate(new Date());
        }
        return riqi;


    }

    public void setRiqi(String riqi) {

        if (this.riqi != null && !this.riqi.equals(riqi)) {
            this.riqi = riqi;
            riqichange = true;
        }

    }

    boolean riq2change = false;

    private String riq2;

    public String getRiq2() {
        if (riq2 == null || riq2.equals("")) {
            riq2 = DateUtil.FormatDate(new Date());
        }
        return riq2;
    }

    public void setRiq2(String riq2) {

        if (this.riq2 != null && !this.riq2.equals(riq2)) {
            this.riq2 = riq2;
            riq2change = true;
        }

    }

    boolean riqchange = false;

    private String riq;

    public String getRiq() {
        if (riq == null || riq.equals("")) {
            riq = DateUtil.FormatDate(new Date());
        }
        return riq;
    }

    public void setRiq(String riq) {

        if (this.riq != null && !this.riq.equals(riq)) {
            this.riq = riq;
            riqchange = true;
        }

    }

    // 卸煤点
    public boolean _Poundchange = false;

    public IDropDownBean getPoundValue() {

        if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
            ((Visit) getPage().getVisit())
                    .setDropDownBean3((IDropDownBean) getPoundModel()
                            .getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean3();
    }

    public void setPoundValue(IDropDownBean Value) {

        if (Value != ((Visit) getPage().getVisit()).getDropDownBean3()) {

            ((Visit) getPage().getVisit()).setboolean3(true);

        }
        ((Visit) getPage().getVisit()).setDropDownBean3(Value);
    }

    public IPropertySelectionModel getPoundModel() {

        if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

            getPoundModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel3();
    }

    public void setPoundModel(IPropertySelectionModel value) {

        ((Visit) getPage().getVisit()).setProSelectionModel3(value);
    }

    public IPropertySelectionModel getPoundModels() {
        JDBCcon con = new JDBCcon();
        List List = new ArrayList();
        try {

            List.add(new IDropDownBean(0, "汇总"));
            List.add(new IDropDownBean(1, "明细"));

        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
        ((Visit) getPage().getVisit())
                .setProSelectionModel3(new IDropDownModel(List));
        return ((Visit) getPage().getVisit()).getProSelectionModel3();
    }
//运输单位

    public boolean _Yunsdwchange = false;

    public IDropDownBean getYunsdwValue() {

        if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
            ((Visit) getPage().getVisit())
                    .setDropDownBean4((IDropDownBean) getYunsdwModel()
                            .getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean4();
    }

    public void setYunsdwValue(IDropDownBean Value) {

        if (Value != ((Visit) getPage().getVisit()).getDropDownBean4()) {

            ((Visit) getPage().getVisit()).setboolean4(true);

        }
        ((Visit) getPage().getVisit()).setDropDownBean4(Value);
    }

    public IPropertySelectionModel getYunsdwModel() {

        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
            getYunsdwModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }

    public void setYunsdwModel(IPropertySelectionModel value) {

        ((Visit) getPage().getVisit()).setProSelectionModel4(value);
    }

    public IPropertySelectionModel getYunsdwModels() {
        JDBCcon con = new JDBCcon();
        String sql="select id,mingc from yunsdwb " ;
        //	"where diancxxb_id= "+((Visit) getPage().getVisit()).getDiancxxb_id();

        ((Visit) getPage().getVisit())
                .setProSelectionModel4(new IDropDownModel(sql,"全部"));
        //setYunsdwModel(new IDropDownModel(sql,"全部"));
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }

    //	获取供应商
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
        String sql="select id,mingc from vwgongysmk where diancxxb_id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
        //setGongysDropDownModel(new IDropDownModel(sql,"全部")) ;
        ((Visit) getPage().getVisit())
                .setProSelectionModel1(new IDropDownModel(sql,"全部"));
        return ;
    }
    //查询方式
    public boolean _Chaxfschange=false;
    public IDropDownBean getChaxfsValue() {

        if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
            ((Visit) getPage().getVisit())
                    .setDropDownBean5((IDropDownBean) getChaxfsModel()
                            .getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean5();
    }

    public void setChaxfsValue(IDropDownBean Value) {

        if (Value != ((Visit) getPage().getVisit()).getDropDownBean5()) {

            ((Visit) getPage().getVisit()).setboolean5(true);

        }
        ((Visit) getPage().getVisit()).setDropDownBean5(Value);
    }

    public IPropertySelectionModel getChaxfsModel() {

        if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

            getChaxfsModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel5();
    }

    public void setChaxfsModel(IPropertySelectionModel value) {

        ((Visit) getPage().getVisit()).setProSelectionModel5(value);
    }

    public IPropertySelectionModel getChaxfsModels() {
        JDBCcon con = new JDBCcon();
        List List = new ArrayList();
        try {

            List.add(new IDropDownBean(2, "煤矿  运输单位"));
            List.add(new IDropDownBean(3, "运输单位  煤矿"));

        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
        ((Visit) getPage().getVisit())
                .setProSelectionModel5(new IDropDownModel(List));
        return ((Visit) getPage().getVisit()).getProSelectionModel5();
    }
    public void getSelectData() {

        Visit visit=(Visit)getPage().getVisit();
        Toolbar tb1 = new Toolbar("tbdiv");

        tb1.addText(new ToolbarText("开始日期:"));
        DateField df = new DateField();
        df.setReadOnly(true);
        df.setValue(this.getRiqi());
        df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
        df.setId("riqI");
        tb1.addField(df);
        tb1.addText(new ToolbarText("-"));

        tb1.addText(new ToolbarText("结束日期:"));
        DateField df1 = new DateField();
        df1.setReadOnly(true);
        df1.setValue(this.getRiq2());
        df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
        df1.setId("riq2");
        tb1.addField(df1);
        //tb1.addText(new ToolbarText("-"));
        // tb1.addText(new ToolbarText("查询方式:"));
        /*ComboBox ch=new ComboBox();
        ch.setTransform("ChaxfsDropDown");
        ch.setId("ChaxfsDropDown");*/
        // ch.setListeners("select :function(combo,newValue,oldValue){document.forms[0].submit();}");
        /*ch.setEditable(true);
        ch.setLazyRender(true);
        ch.setWidth(100);
        tb1.addField(ch);
        tb1.addText(new ToolbarText("-"));*/

        DefaultTree dt = new DefaultTree(DefaultTree.tree_gongys_win,"gongysTree"
                ,""+visit.getDiancxxb_id(),"forms[0]",null,getTreeid());
        visit.setDefaultTree(dt);
        TextField tf = new TextField();
        tf.setId("gongysTree_text");
        tf.setWidth(100);
        tf.setValue(((IDropDownModel)getGongysDropDownModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
       /* ToolbarButton tb2 = new ToolbarButton(null,null,"function(){gongysTree_window.show();}");
        tb2.setIcon("ext/resources/images/list-items.gif");
        tb2.setCls("x-btn-icon");
        tb2.setMinWidth(20);*/
        // tb1.addText(new ToolbarText("单位:"));
        /*tb1.addField(tf);
        tb1.addItem(tb2);
        tb1.addText(new ToolbarText("-"));*/

        // tb1.addText(new ToolbarText("汇总或明细:"));
        // ComboBox gh = new ComboBox();
        //gh.setTransform("PoundDropDown");
        //gh.setEditable(true);
        //gh.setWidth(60);
        //gh.setListeners("select:function(){document.Form0.submit();}");
        //tb1.addField(gh);
        // tb1.addText(new ToolbarText("-"));

        // tb1.addText(new ToolbarText("运输单位:"));
       /* ComboBox ys = new ComboBox();
        ys.setTransform("YunsdwDropDown");
        ys.setEditable(true);
        ys.setWidth(200);*/
        //ys.setListeners("select:function(){document.Form0.submit();}");
        //tb1.addField(ys);



        ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
        rbtn.setIcon(SysConstant.Btn_Icon_Search);
        tb1.addItem(rbtn);
        tb1.addFill();
        tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));

        setToolbar(tb1);

    }
    public void getSelectData1(){
        Visit visit=(Visit)getPage().getVisit();
        Toolbar tb1 = new Toolbar("tbdiv");

        tb1.addText(new ToolbarText("开始日期:"));
        DateField df = new DateField();
        df.setReadOnly(true);
        df.setValue(this.getRiqi());
        df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
        df.setId("riqI");
        tb1.addField(df);
        tb1.addText(new ToolbarText("-"));

        tb1.addText(new ToolbarText("结束日期:"));
        DateField df1 = new DateField();
        df1.setReadOnly(true);
        df1.setValue(this.getRiq2());
        df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
        df1.setId("riq2");
        tb1.addField(df1);
        tb1.addText(new ToolbarText("-"));
        //tb1.addText(new ToolbarText("查询方式:"));
        ComboBox ch=new ComboBox();
        ch.setTransform("ChaxfsDropDown");
        ch.setId("ChaxfsDropDown");
        ch.setListeners("select :function(combo,newValue,oldValue){document.forms[0].submit();}");
        ch.setEditable(true);
        ch.setLazyRender(true);
        ch.setWidth(100);
        tb1.addField(ch);
        tb1.addText(new ToolbarText("-"));
        //运输单位
        //tb1.addText(new ToolbarText("运输单位:"));
        ComboBox ys = new ComboBox();
        ys.setTransform("YunsdwDropDown");
        ys.setEditable(true);
        ys.setWidth(200);
        //ys.setListeners("select:function(){document.Form0.submit();}");
        tb1.addField(ys);

        //tb1.addText(new ToolbarText("汇总或明细:"));
        ComboBox gh = new ComboBox();
        gh.setTransform("PoundDropDown");
        gh.setEditable(true);
        gh.setWidth(60);
        //gh.setListeners("select:function(){document.Form0.submit();}");
        tb1.addField(gh);
        tb1.addText(new ToolbarText("-"));
        //运输单位

        DefaultTree dt = new DefaultTree(DefaultTree.tree_gongys_win,"gongysTree"
                ,""+visit.getDiancxxb_id(),"forms[0]",null,getTreeid());
        visit.setDefaultTree(dt);
        TextField tf = new TextField();
        tf.setId("gongysTree_text");
        tf.setWidth(100);
        tf.setValue(((IDropDownModel)getGongysDropDownModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));

        ToolbarButton tb2 = new ToolbarButton(null,null,"function(){gongysTree_window.show();}");
        tb2.setIcon("ext/resources/images/list-items.gif");
        tb2.setCls("x-btn-icon");
        tb2.setMinWidth(20);
        //tb1.addText(new ToolbarText("单位:"));
        tb1.addField(tf);
        tb1.addItem(tb2);
        tb1.addText(new ToolbarText("-"));


        ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
        rbtn.setIcon(SysConstant.Btn_Icon_Search);
        tb1.addItem(rbtn);
        tb1.addFill();
        tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));

        setToolbar(tb1);
    }

    //	工具栏使用的方法
    private String treeid;
    public String getTreeid() {
        if(treeid==null||treeid.equals("")){
            treeid="0";
        }
        return treeid;
    }
    public void setTreeid(String treeid) {
        if(treeid!=null) {
            if(!treeid.equals(this.treeid)) {
                ((TextField)getToolbar().getItem("gongysTree_text")).setValue
                        (((IDropDownModel)getGongysDropDownModel()).getBeanValue(Long.parseLong(treeid)));
                ((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
            }
        }
        this.treeid = treeid;
    }
    public String getTreeScript() {
        return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
    }


    public Toolbar getToolbar() {
        return ((Visit) this.getPage().getVisit()).getToolbar();
    }

    public void setToolbar(Toolbar tb1) {
        ((Visit) this.getPage().getVisit()).setToolbar(tb1);
    }

    public String getToolbarScript() {
        return getToolbar().getRenderScript();
    }

    public String getcontext() {
        return "";
        // return "var context='http://"
        // + this.getRequestCycle().getRequestContext().getServerName()
        // + ":"
        // + this.getRequestCycle().getRequestContext().getServerPort()
        // + ((Visit) getPage().getVisit()).get() + "';";
    }

    public SortMode getSort() {
        return SortMode.USER;
    }

    private String _pageLink;

    public String getpageLink() {
        if (!_pageLink.equals("")) {
            return _pageLink;
        } else {
            return "";
        }
    }

    // Page方法
    protected void initialize() {
        _pageLink = "";
    }
}
