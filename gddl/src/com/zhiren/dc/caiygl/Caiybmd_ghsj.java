package com.zhiren.dc.caiygl;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.request.RequestContext;

public class Caiybmd_ghsj extends BasePage
{
  private String REPORT_NAME_CAIYBMD_TIEL = "Tiel";
  private String REPORT_NAME_CAIYBMD_GONGL = "Gongl";
  private String msg = "";
  private String tbmsg;
  private int _CurrentPage = -1;
  private int _AllPages = -1;
  private boolean riqchange = false;
  private String riq;
  private String Change;
  private String mstrReportName = "";
  private boolean blnIsBegin = false;
  private boolean _RefurbishChick = false;

  public String getMsg()
  {
    return this.msg;
  }

  public void setMsg(String msg) {
    this.msg = MainGlobal.getExtMessageBox(msg, false);
  }

  protected void initialize()
  {
    super.initialize();
    setMsg("");
    setTbmsg(null);
  }

  public String getTbmsg()
  {
    return this.tbmsg;
  }

  public void setTbmsg(String tbmsg) {
    this.tbmsg = tbmsg;
  }

  public int getCurrentPage()
  {
    return this._CurrentPage;
  }

  public void setCurrentPage(int _value) {
    this._CurrentPage = _value;
  }

  public int getAllPages()
  {
    return this._AllPages;
  }

  public void setAllPages(int _value) {
    this._AllPages = _value;
  }

  public String getRiq()
  {
    return this.riq;
  }

  public void setRiq(String riq) {
    if ((this.riq != null) && 
      (!(this.riq.equals(riq))))
      this.riqchange = true;

    this.riq = riq;
  }

  public String getChange()
  {
    return this.Change;
  }

  public void setChange(String change) {
    this.Change = change;
  }

  public boolean getRaw() {
    return true;
  }

  public IDropDownBean getHengdValue()
  {
    if ((((Visit)getPage().getVisit()).getDropDownBean1() == null) && 
      (getHengdModel().getOptionCount() > 0)) {
      setHengdValue((IDropDownBean)getHengdModel().getOption(0));
    }

    return ((Visit)getPage().getVisit()).getDropDownBean1();
  }

  public void setHengdValue(IDropDownBean value) {
    ((Visit)getPage().getVisit()).setDropDownBean1(value);
  }

  public IPropertySelectionModel getHengdModel() {
    if (((Visit)getPage().getVisit()).getProSelectionModel1() == null)
      setHengdModels();

    return ((Visit)getPage().getVisit()).getProSelectionModel1();
  }

  public void setHengdModel(IPropertySelectionModel value) {
    ((Visit)getPage().getVisit()).setProSelectionModel1(value);
  }

  public void setHengdModels()
  {
    StringBuffer sb = new StringBuffer();

    sb.append
      ("select a.id,a.guohsj from  (select  distinct  g.id,to_char(g.guohsj, 'yyyy-mm-dd hh24:mi:ss') guohsj  from guohb g,chepb c,fahb f\nwhere g.id=c.guohb_id and c.fahb_id=f.id and to_char(g.guohsj, 'yyyy-mm-dd') = '" + 
      getRiq() + "'\n" + 
      "and f.yunsfsb_id=1)a");

    setHengdModel(new IDropDownModel(sb.toString()));
  }

  public void getSelectData()
  {
    Toolbar tb1 = new Toolbar("tbdiv");
    tb1.addText(new ToolbarText("过衡时间:"));
    DateField df = new DateField();
    df.setValue(getRiq());
    df.Binding("Riq", "Form0");
    df.setId("guohsj");
    tb1.addField(df);
    tb1.addText(new ToolbarText("-"));
    if (this.mstrReportName.equals(this.REPORT_NAME_CAIYBMD_TIEL)) {
      ComboBox hengdcb = new ComboBox();
      hengdcb.setTransform("HengdSelect");
      hengdcb.setWidth(130);
      hengdcb.setListeners
        ("select:function(own,rec,index){Ext.getDom('HengdSelect').selectedIndex=index}");
      tb1.addField(hengdcb);
      tb1.addText(new ToolbarText("-"));
    }
    ToolbarButton rbtn = new ToolbarButton(null, "查询", 
      "function(){document.getElementById('RefurbishButton').click();}");
    rbtn.setIcon("imgs/btnicon/search.gif");
    tb1.setWidth("bodyWidth");
    tb1.addItem(rbtn);
    tb1.addFill();
    tb1.addText(
      new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
    setToolbar(tb1);
  }

  public String getPrintTable()
  {
    if (!(this.blnIsBegin))
      return "";

    this.blnIsBegin = false;
    if (this.mstrReportName.equals(this.REPORT_NAME_CAIYBMD_TIEL))
      return getTielcyd();
    if (this.mstrReportName.equals(this.REPORT_NAME_CAIYBMD_GONGL))
      return getGonglcyd();

    return "无此报表";
  }

  private String getTielcyd()
  {
    JDBCcon con = new JDBCcon();
    StringBuffer sb = new StringBuffer();
    long hengdid = -1L;
    String guohsj = "";
    if (getHengdValue() != null) {
      hengdid = getHengdValue().getId();
      guohsj = getHengdValue().getValue();
    }

    sb.append
      ("select distinct y.id from fahb f,caiyb y,\n").append
      ("(select * from chepb where guohb_id = ").append
      (hengdid).append
      (
      " ) c where f.id = c.fahb_id and f.zhilb_id = y.zhilb_id and f.yunsfsb_id = 1");
    ResultSetList rsl = con.getResultSetList(sb.toString());
    String[][] bianms = new String[rsl.getRows()][2];
    while (rsl.next()) {
      bianms[rsl.getRow()][0] = rsl.getString("id");
      sb.delete(0, sb.length());
      sb.append
        (
        "select y.leib||':'||z.bianm bm from zhuanmb z,yangpdhb y where z.zhuanmlb_id = (select id from zhuanmlb where jib = 1)").append
        (
        " and y.zhilblsb_id = z.zhillsb_id and y.caiyb_id =").append
        (rsl.getString("id"));
      ResultSetList rbm = con.getResultSetList(sb.toString());
      sb.delete(0, sb.length());
      while (rbm.next())
        sb.append(rbm.getString("bm")+",<br>").append("\n");

      bianms[rsl.getRow()][1] = sb.toString();
    }

    sb.delete(0, sb.length());
    sb.append
      ("select b.caiybm, rownum as xuh, b.cheph, b.biaoz, b.yingd, b.kuid, b.caiysj\n  from (select y.id caiybm,\n                c.cheph,\n               c.biaoz,\n               c.yingd,\n               c.yingd - c.yingk kuid,\n               '' as caiysj\n          from fahb f,\n               caiyb y,\n               (select *\n                  from chepb\n                 where guohb_id =\n" + 
      hengdid + 
      ") c\n" + 
      "         where f.id = c.fahb_id\n" + 
      "           and f.zhilb_id = y.zhilb_id\n" + 
      "           and f.yunsfsb_id = 1\n" + 
      "         order by c.xuh) b");
    rsl = con.getResultSetList(sb.toString());
    while (rsl.next())
      for (int i = 0; i < bianms.length; ++i)
        if (rsl.getString("caiybm").equals(bianms[i][0])) {
          rsl.setString("caiybm", bianms[i][1]);
          break;
        }


    rsl.beforefirst();
    String[][] ArrHeader = { { "采样编码", "序号", 
      "车号", "票重", "盈吨", 
      "亏吨", "采样时间" } };

    int[] ArrWidth = { 120, 40, 100, 90, 90, 90, 90 };
    Report rt = new Report();
    rt.setTitle("采 样 编 码 单", ArrWidth);
    rt.setDefaultTitle(1, 5, "打印时间：" + DateUtil.FormatDateTime(new Date()), 
      -1);
    rt.setDefaultTitle(6, 2, "过衡时间：" + guohsj, 
      -1);
    rt.setBody(new Table(rsl, 1, 0, 1));
    rt.body.setWidth(ArrWidth);
    rt.body.setHeaderData(ArrHeader);
    rt.body.setPageRows(40);
    rt.body.mergeFixedCols();
    rt.body.mergeFixedRow();
    rt.createFooter(1, ArrWidth);
    rt.setDefautlFooter(1, 3, "采样人员：", -1);
    con.Close();
    if (rt.body.getPages() > 0) {
      setCurrentPage(1);
      setAllPages(rt.body.getPages());
    }
    rt.body.setRowHeight(21);

    return rt.getAllPagesHtml();
  }

  private String getGonglcyd()
  {
    JDBCcon con = new JDBCcon();
    StringBuffer sb = new StringBuffer();

    sb.append
      ("select distinct y.id from fahb f,caiyb y,\n").append
      (
      "(select * from chepb where to_char(lursj,'yyyy-mm-dd') = '").append
      (getRiq()).append
      (
      "' ) c where f.id = c.fahb_id and f.zhilb_id = y.zhilb_id and f.yunsfsb_id = 2");
    ResultSetList rsl = con.getResultSetList(sb.toString());
    String[][] bianms = new String[rsl.getRows()][2];
    while (rsl.next()) {
      bianms[rsl.getRow()][0] = rsl.getString("id");
      sb.delete(0, sb.length());
      sb.append
        (
        "select y.leib||':'||z.bianm bm from zhuanmb z,yangpdhb y where z.zhuanmlb_id = (select id from zhuanmlb where jib = 1)").append
        (
        " and y.zhilblsb_id = z.zhillsb_id and y.caiyb_id =").append
        (rsl.getString("id"));
      ResultSetList rbm = con.getResultSetList(sb.toString());
      sb.delete(0, sb.length());
      while (rbm.next())
        sb.append(rbm.getString("bm")).append("\n");

      bianms[rsl.getRow()][1] = sb.toString();
    }

    sb.delete(0, sb.length());
    sb.append
      ("select b.caiybm, rownum as xuh, b.cheph, b.biaoz, b.yingd, b.kuid, b.caiysj\n  from (select y.id caiybm,\n               c.cheph,\n               c.biaoz,\n               c.yingd,\n               c.yingd - c.yingk kuid,\n               '' as caiysj\n          from fahb f,\n               caiyb y,\n               (select *\n                  from chepb\n                 where to_char(lursj,'yyyy-mm-dd') ='" + 
      getRiq() + 
      "') c\n" + 
      "         where f.id = c.fahb_id\n" + 
      "           and f.zhilb_id = y.zhilb_id\n" + 
      "           and f.yunsfsb_id = 2\n" + 
      "         order by y.id,c.xuh) b");
    rsl = con.getResultSetList(sb.toString());
    while (rsl.next())
      for (int i = 0; i < bianms.length; ++i)
        if (rsl.getString("caiybm").equals(bianms[i][0])) {
          rsl.setString("caiybm", bianms[i][1]);
          break;
        }


    rsl.beforefirst();
    String[][] ArrHeader = { { "采样编码", "序号", 
      "车号", "票重", "盈吨", 
      "亏吨", "采样时间" } };

    int[] ArrWidth = { 100, 50, 100, 90, 90, 90, 90 };
    Report rt = new Report();
    rt.setTitle("采 样 编 码 单", ArrWidth);
    rt.setDefaultTitle(1, 5, "打印时间：" + DateUtil.FormatDateTime(new Date()), 
      -1);
    rt.setBody(new Table(rsl, 1, 0, 1));
    rt.body.setWidth(ArrWidth);
    rt.body.setHeaderData(ArrHeader);
    rt.body.setPageRows(40);
    rt.body.mergeFixedCols();
    rt.body.mergeFixedRow();
    rt.createFooter(1, ArrWidth);
    rt.setDefautlFooter(1, 3, "采样人员：", -1);
    con.Close();
    if (rt.body.getPages() > 0) {
      setCurrentPage(1);
      setAllPages(rt.body.getPages());
    }
    rt.body.setRowHeight(21);

    return rt.getAllPagesHtml();
  }

  public Toolbar getToolbar()
  {
    return ((Visit)getPage().getVisit()).getToolbar();
  }

  public void setToolbar(Toolbar tb1) {
    ((Visit)getPage().getVisit()).setToolbar(tb1);
  }

  public String getToolbarScript() {
    if (getTbmsg() != null) {
      getToolbar().deleteItem();
      getToolbar().addText(
        new ToolbarText("<marquee width=300 scrollamount=2>" + 
        getTbmsg() + "</marquee>"));
    }
    ((DateField)getToolbar().getItem("guohsj")).setValue(getRiq());
    return getToolbar().getRenderScript();
  }

  public void beginResponse(IMarkupWriter writer, IRequestCycle cycle)
  {
    Visit visit = (Visit)getPage().getVisit();
    if (!(visit.getActivePageName().toString().equals(
      getPageName().toString())))
    {
      visit.setActivePageName(getPageName().toString());
      visit.setList1(null);
      visit.setboolean1(false);
      visit.setString1(null);
      setRiq(DateUtil.FormatDate(new Date()));
      setHengdValue(null);
      setHengdModel(null);
    }

    if (this.riqchange) {
      this.riqchange = false;
      setHengdValue(null);
      setHengdModel(null);
      setTbmsg(null);
    }
    if (cycle.getRequestContext().getParameters("lx") != null) {
      visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
      this.mstrReportName = visit.getString1();
      getSelectData();
    }
    else if (visit.getString1().equals("")) {
      this.mstrReportName = visit.getString1();
    }

    this.blnIsBegin = true;
  }

  public void RefurbishButton(IRequestCycle cycle)
  {
    this._RefurbishChick = true;
  }

  public void submit(IRequestCycle cycle)
  {
    if (this._RefurbishChick) {
      this._RefurbishChick = false;
      getSelectData();
    }
  }

  public void pageValidate(PageEvent arg0)
  {
    String PageName = arg0.getPage().getPageName();
    String ValPageName = Login.ValidateLogin(arg0.getPage());
    if (!(PageName.equals(ValPageName))) {
      ValPageName = Login.ValidateAdmin(arg0.getPage());
      if (!(PageName.equals(ValPageName))) {
        IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        throw new PageRedirectException(ipage);
      }
    }
  }
}