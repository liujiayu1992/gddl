package com.zhiren.dc.huaygl.meizycbg_hy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;


public class Meizycbg_hy extends BasePage implements PageValidateListener {
	
	private String zhilb_id; // 存放前个页面传过来的zhilb_id
	
	private String dianc_id; // 存放前个页面传过来的dianc_id
	
	public String getDianc_id() {
		return dianc_id;
	}

	public void setDianc_id(String dianc_id) {
		this.dianc_id = dianc_id;
	}

	public String getZhilb_id() {
		return zhilb_id;
	}

	public void setZhilb_id(String zhilb_id) {
		this.zhilb_id = zhilb_id;
	}

	private String _msg;

	public void setMsg(String _value) {
		this._msg = MainGlobal.getExtMessageBox(_value, false);
	}
	
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	protected void initialize() {
		this._msg = "";
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
	
	public boolean getRaw() {
		return true;
	}
	
	public String getPrintTable() {
		
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		
	    String bianm="";
	    String mk ="";
	    _CurrentPage=0;
	    _AllPages = 0;
	    
	    StringBuffer ssff=new StringBuffer("");

		String sSql=

			"select * from (\n" +
			"select zlls.id zlls_zhilb_id, getHuaybh4zl(zlls.id) bianm, max(mkxx.mingc) mkmc, max(fh.daohrq) daoh,\n" + 
			"         round_new(avg(zlls.mt), 1) mt,Formatxiaosws(round_new(avg(zlls.stad),2),2) stad,\n" + 
			"        round_new(avg(zlls.aad),2) aad,\n"
			+ " round_new(avg(zlls.qnet_ar),2)*1000 qnet_ar,\n"+
			"        round_new(avg(zlls.qnet_ar) * 1000 / 4.1816,0) frl\n" + 
			"      from zhilb zlls, fahb fh, meikxxb mkxx\n" + 
			"      where\n" + 
			"       fh.zhilb_id = zlls.id\n" + 
			"      and zlls.huaysj= to_date('"+getBRiq()+"','yyyy-mm-dd')\n"+
			"        and fh.meikxxb_id = mkxx.id\n" + 
			"        and fh.daohrq = (to_date('"+getBRiq()+"','yyyy-mm-dd')-2)\n" + 
//			"        and fh.daohrq = (to_date('"+getBRiq()+"','yyyy-mm-dd'))\n" + 
			"        and fh.diancxxb_id="+getTreeid()+"\n" + 
			"      group by zlls.id, mkxx.mingc\n" + 
			" order by bianm     )";

		
		
		ResultSetList rs = con.getResultSetList(sSql);
//		System.out.println(sSql);
		
//		设置页数和当前页数
		if(rs.getRows()>1){
			if(Math.round((rs.getRows()%6))!=0){
//				if(Math.round((rs.getRows()%5))!=0){
				
				_AllPages=Math.round((rs.getRows()/6))+1;
			}else{
				
				_AllPages=Math.round((rs.getRows()/6));
			}
		}else{
			
			_AllPages = 0;
		}
		if((_CurrentPage==-1&&rs.getRows()>0)||(_CurrentPage==0&&rs.getRows()>0)){
			_CurrentPage=1;
		}else if(_CurrentPage>1){
			
			_CurrentPage = getCurrentPage();
		}
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		
		
		
		StringBuffer ssb_1=new StringBuffer();//1
		StringBuffer ssb_2=new StringBuffer();//2
		StringBuffer ssb_3=new StringBuffer();//3
		StringBuffer ssb_4=new StringBuffer();//4
		StringBuffer ssb_5=new StringBuffer();//5
		StringBuffer ssb_6=new StringBuffer();//6
		StringBuffer ssb_7=new StringBuffer();//7
		StringBuffer ssb_8=new StringBuffer();//8
		StringBuffer ssb_9=new StringBuffer();//9
		StringBuffer ssb_10=new StringBuffer();//10
		StringBuffer ssb_11=new StringBuffer();//11
		StringBuffer ssb_12=new StringBuffer();//12
		StringBuffer ssb_13=new StringBuffer();//13
		StringBuffer ssb_14=new StringBuffer();//14
		StringBuffer ssb_15=new StringBuffer();//15
		ssb_1.append("主要指标@主要指标@设计值");
		ssb_2.append("主要指标@ 主要指标@设计值");
		ssb_3.append("水分Mar（％）@ 水分Mar（％）@ 5.67±3");
		ssb_4.append("灰分Aad（％）@ 灰分Aad（％）@ 17.99±4");
		ssb_5.append("全硫St，ad（％）@ 全硫St，ad（％）@  0.34");
		ssb_6.append("发热量Qnet，ar<br>J/g（卡/克）@ 发热量Qnet，arJ/g（卡/克）@  25539±1674");
		ssb_7.append("监<br>督<br>评<br>判@ 亏卡；<br>设备磨损增加；<br>影响锅炉燃烧@  有影响");
		ssb_8.append("监<br>督<br>评<br>判@ 亏卡；<br>设备磨损增加；<br>影响锅炉燃烧@  严重");
		ssb_9.append("监<br>督<br>评<br>判@ 受热面腐蚀增加@  有影响");
		ssb_10.append("监<br>督<br>评<br>判@ 受热面腐蚀增加@  严重");
		ssb_11.append("监<br>督<br>评<br>判@ 亏卡；<br>影响输煤系统的安全运行@ 有影响");
		ssb_12.append("监<br>督<br>评<br>判@ 亏卡；<br>影响输煤系统的安全运行@  严重");
		ssb_13.append("监<br>督<br>建<br>议@ 一:加强锅炉燃烧监控@  一:加强锅炉燃烧监控");
		ssb_14.append("监<br>督<br>建<br>议@ 二:加强配煤@ 二:加强配煤");
		ssb_15.append("监<br>督<br>建<br>议@ 三:危害严重<br>立即停止购买该煤种@  三:危害严重<br>立即停止购买该煤种");
		String daohrq="";
		while(rs.next()){
			//把需用的数据赋值给不同的StringBuffer，然后通过StringBuffer进行给ArrBody传值
			ssb_1.append("@异&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;常&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;煤&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;    样");
			ssb_2.append("@"+rs.getString("mkmc")+"<br>"+rs.getString("bianm"));
			if(rs.getDouble("mt")>10){
				
				ssb_3.append("@"+rs.getDouble("mt"));
			}else{
				ssb_3.append("@"+" ");
			}
			if(rs.getDouble("aad")>25){
				
				ssb_4.append("@"+rs.getDouble("aad"));
			}else{
				ssb_4.append("@"+" ");
			}
			if(rs.getDouble("stad")>0.5){
				
				ssb_5.append("@"+rs.getString("stad"));
			}else{
				ssb_5.append("@"+" ");
			}
			if(rs.getDouble("frl")<5500){//发热量Qnet，ar J/g（卡/克）
				
				ssb_6.append("@"+rs.getString("qnet_ar")+"<br>("+rs.getLong("frl")+")");
			}else{ 
				ssb_6.append("@"+" ");
			}
			if(rs.getDouble("aad")>25&&rs.getDouble("aad")<=30){
				
				ssb_7.append("@"+"√");
				ssb_8.append("@"+" ");
			}else if(rs.getDouble("aad")>30){
				ssb_7.append("@"+" ");
				ssb_8.append("@"+"√");
			}else{
				ssb_7.append("@"+" ");
				ssb_8.append("@"+" ");
			}
			if(rs.getDouble("stad")>0.5&&rs.getDouble("stad")<=1){
				
				
				ssb_9.append("@"+"√");
				ssb_10.append("@"+" ");
			}else if(rs.getDouble("stad")>1){
				ssb_9.append("@"+ " ");
				ssb_10.append("@"+"√");
				
			}else{
				ssb_9.append("@"+" ");
				ssb_10.append("@"+" ");
			}
			if(rs.getDouble("mt")>10&&rs.getDouble("mt")<=15){
				
				
				ssb_11.append("@"+"√");
				ssb_12.append("@"+" ");
			}else if(rs.getDouble("mt")>15){

				ssb_11.append("@"+" ");
				ssb_12.append("@"+"√");
				
			}else{

				ssb_11.append("@"+" ");
				ssb_12.append("@"+" ");
				
			}
			if(rs.getDouble("mt")>15||rs.getDouble("frl")<4800){
				
				ssb_13.append("@"+"√");
			}else{
				ssb_13.append("@"+" ");
			}
			if(rs.getDouble("aad")>30||rs.getDouble("stad")>0.6||rs.getDouble("frl")<5300){
				
				ssb_14.append("@"+"√");
			}else{
				
				ssb_14.append("@"+" ");
			}
			if(rs.getDouble("aad")>40||rs.getDouble("stad")>1.5){
				
				ssb_15.append("@"+"√");
			}else{
				ssb_15.append("@"+" ");
			}
			daohrq=rs.getDateString("daoh").replaceAll("-", ".");//
		}
		String huayy = ""; // 存入化验员信息
		_CurrentPage=1;
		for(int a=0;a<_AllPages;a++){
		String[][] ArrBody =new String[15][3] ;
		String[][] ArrBody1 =new String[15][3] ;
		if (getCurrentPage()==1) {//查询出来1条到5条之间&&rs.getRows()<=2
			if(rs.getRows()/6>=1&&Math.round((rs.getRows()%6))!=0){//第一页但是还有数据//5
				ArrBody= new String[15][6+3];//5
			}else if(rs.getRows()/2>=1&&Math.round((rs.getRows()%6))==0){//就是正好一页//5
				ArrBody= new String[15][6+3];//5
			}else if(rs.getRows()/6==0){//5
				
				ArrBody1= new String[15][rs.getRows()+3];
			}
		}
		if (getCurrentPage() < _AllPages&&getCurrentPage()!=1&&rs.getRows()>=7 ) {// 查询出来大于5条并且不是最后一页
			ArrBody = new String[15][9];
			ArrBody1 = new String[15][9];
		}
		if (getCurrentPage() == _AllPages && getCurrentPage() != 1) {// 查询出来大于5条并且是最后一页
			// ArrWidth1 = new int[3+Math.round((rs.getRows()%5))];
			if(Math.round((rs.getRows()%6))==0){
				
				ArrBody = new String[15][3+6];//5
				ArrBody1 = new String[15][3+6];//5
			}
			if(Math.round((rs.getRows()%6))!=0){
				ArrBody = new String[15][3+Math.round((rs.getRows()%6))];//5
				ArrBody1 = new String[15][3+Math.round((rs.getRows()%6))];//5
			}
		}

		if (rs.getRows() > 0) {
			
			if (getCurrentPage()==1&&rs.getRows()<7) {//查询出来1条到5条之间
				ArrBody[0] = ssb_1.toString().split("@");
				
				ArrBody[1] = ssb_2.toString().split("@");
				
				ArrBody[2] = ssb_3.toString().split("@");
				
				ArrBody[3] = ssb_4.toString().split("@");//mt==mar
				
				ArrBody[4] = ssb_5.toString().split("@");
			
				ArrBody[5] = ssb_6.toString().split("@");
//				
			
				ArrBody[6] = ssb_7.toString().split("@");
				
				ArrBody[7] = ssb_8.toString().split("@");
				
				ArrBody[8] = ssb_9.toString().split("@");
				ArrBody[9] = ssb_10.toString().split("@");
//				
				ArrBody[10] = ssb_11.toString().split("@");
				
				ArrBody[11] = ssb_12.toString().split("@");
//				
//				
				ArrBody[12] = ssb_13.toString().split("@");
//				
//				
				ArrBody[13] = ssb_14.toString().split("@");
				
				ArrBody[14] = ssb_15.toString().split("@");
			}
			
			if (getCurrentPage() < _AllPages&&getCurrentPage()!=1  ) {// 查询出来大于5条并且不是最后一页
				ArrBody1[0] = ssb_1.toString().split("@");
				
//				ArrBody[0]=(ssb_1.toString().split("@", 3).toString()+ssb_1.toString().split("@", 5)).split(",");
				ArrBody1[1] = ssb_2.toString().split("@");
				
				ArrBody1[2] = ssb_3.toString().split("@");
				
				ArrBody1[3] = ssb_4.toString().split("@");//mt==mar
				
				ArrBody1[4] = ssb_5.toString().split("@");
			
				ArrBody1[5] = ssb_6.toString().split("@");
//				
			
				ArrBody1[6] = ssb_7.toString().split("@");
				
				ArrBody1[7] = ssb_8.toString().split("@");
				
				ArrBody1[8] = ssb_9.toString().split("@");
				ArrBody1[9] = ssb_10.toString().split("@");
//				
				ArrBody1[10] = ssb_11.toString().split("@");
				
				ArrBody1[11] = ssb_12.toString().split("@");
//				
//				
				ArrBody1[12] = ssb_13.toString().split("@");
//				
//				
				ArrBody1[13] = ssb_14.toString().split("@");
				
				ArrBody1[14] = ssb_15.toString().split("@");
				for(int i=0;i<ArrBody[0].length;i++){
//						System.out.println(ArrBody[0].length);
					if(i>=3){
						ArrBody[0][i]=ArrBody1[0][i+(getCurrentPage()-1)*6];//5
						ArrBody[1][i]=ArrBody1[1][i+(getCurrentPage()-1)*6];
						ArrBody[2][i]=ArrBody1[2][i+(getCurrentPage()-1)*6];
						ArrBody[3][i]=ArrBody1[3][i+(getCurrentPage()-1)*6];
						ArrBody[4][i]=ArrBody1[4][i+(getCurrentPage()-1)*6];
						ArrBody[5][i]=ArrBody1[5][i+(getCurrentPage()-1)*6];
						ArrBody[6][i]=ArrBody1[6][i+(getCurrentPage()-1)*6];
						ArrBody[7][i]=ArrBody1[7][i+(getCurrentPage()-1)*6];
						ArrBody[8][i]=ArrBody1[8][i+(getCurrentPage()-1)*6];
						ArrBody[9][i]=ArrBody1[9][i+(getCurrentPage()-1)*6];
						ArrBody[10][i]=ArrBody1[10][i+(getCurrentPage()-1)*6];
						ArrBody[11][i]=ArrBody1[11][i+(getCurrentPage()-1)*6];
						ArrBody[12][i]=ArrBody1[12][i+(getCurrentPage()-1)*6];
						ArrBody[13][i]=ArrBody1[13][i+(getCurrentPage()-1)*6];
						ArrBody[14][i]=ArrBody1[14][i+(getCurrentPage()-1)*6];
					}
						if(i<3){
							ArrBody[0][i]=ArrBody1[0][i];
							ArrBody[1][i]=ArrBody1[1][i];
							ArrBody[2][i]=ArrBody1[2][i];
							ArrBody[3][i]=ArrBody1[3][i];
							ArrBody[4][i]=ArrBody1[4][i];
							ArrBody[5][i]=ArrBody1[5][i];
							ArrBody[6][i]=ArrBody1[6][i];
							ArrBody[7][i]=ArrBody1[7][i];
							ArrBody[8][i]=ArrBody1[8][i];
							ArrBody[9][i]=ArrBody1[9][i];
							ArrBody[10][i]=ArrBody1[10][i];
							ArrBody[11][i]=ArrBody1[11][i];
							ArrBody[12][i]=ArrBody1[12][i];
							ArrBody[13][i]=ArrBody1[13][i];
							ArrBody[14][i]=ArrBody1[14][i];
						}

//					}
				}
			}
			
			if (getCurrentPage() < _AllPages&&getCurrentPage()==1 ) {// 查询出来大于5条并且不是最后一页
				ArrBody1[0] = ssb_1.toString().split("@");
				
//				ArrBody[0]=(ssb_1.toString().split("@", 3).toString()+ssb_1.toString().split("@", 5)).split(",");
				ArrBody1[1] = ssb_2.toString().split("@");
				
				ArrBody1[2] = ssb_3.toString().split("@");
				
				ArrBody1[3] = ssb_4.toString().split("@");//mt==mar
				
				ArrBody1[4] = ssb_5.toString().split("@");
			
				ArrBody1[5] = ssb_6.toString().split("@");
//				
			
				ArrBody1[6] = ssb_7.toString().split("@");
				
				ArrBody1[7] = ssb_8.toString().split("@");
				
				ArrBody1[8] = ssb_9.toString().split("@");
				ArrBody1[9] = ssb_10.toString().split("@");
//				
				ArrBody1[10] = ssb_11.toString().split("@");
				
				ArrBody1[11] = ssb_12.toString().split("@");
				
				ArrBody1[12] = ssb_13.toString().split("@");
			
				ArrBody1[13] = ssb_14.toString().split("@");
				
				ArrBody1[14] = ssb_15.toString().split("@");
				
				for(int i=0;i<ArrBody[0].length;i++){
//						System.out.println(ArrBody[0].length+"getCurrentPage() < _AllPages&&getCurrentPage()==1 ");//5
						ArrBody[0][i]=ArrBody1[0][i];//+(getCurrentPage()-1)*2
						if(i>=3){
						ArrBody[1][i]=ArrBody1[1][i];
						ArrBody[2][i]=ArrBody1[2][i];
						ArrBody[3][i]=ArrBody1[3][i];
						ArrBody[4][i]=ArrBody1[4][i];
						ArrBody[5][i]=ArrBody1[5][i];
						ArrBody[6][i]=ArrBody1[6][i];
						ArrBody[7][i]=ArrBody1[7][i];
						ArrBody[8][i]=ArrBody1[8][i];
						ArrBody[9][i]=ArrBody1[9][i];
						ArrBody[10][i]=ArrBody1[10][i];
						ArrBody[11][i]=ArrBody1[11][i];
						ArrBody[12][i]=ArrBody1[12][i];
						ArrBody[13][i]=ArrBody1[13][i];
						ArrBody[14][i]=ArrBody1[14][i];
						}
						if(i<3){
							ArrBody[0][i]=ArrBody1[0][i];
							ArrBody[1][i]=ArrBody1[1][i];
							ArrBody[2][i]=ArrBody1[2][i];
							ArrBody[3][i]=ArrBody1[3][i];
							ArrBody[4][i]=ArrBody1[4][i];
							ArrBody[5][i]=ArrBody1[5][i];
							ArrBody[6][i]=ArrBody1[6][i];
							ArrBody[7][i]=ArrBody1[7][i];
							ArrBody[8][i]=ArrBody1[8][i];
							ArrBody[9][i]=ArrBody1[9][i];
							ArrBody[10][i]=ArrBody1[10][i];
							ArrBody[11][i]=ArrBody1[11][i];
							ArrBody[12][i]=ArrBody1[12][i];
							ArrBody[13][i]=ArrBody1[13][i];
							ArrBody[14][i]=ArrBody1[14][i];
						}

				}
			}
			
			if (getCurrentPage() == _AllPages && getCurrentPage() != 1) {// 查询出来大于5条并且是最后一页
				
				for(int i=0;i<ArrBody[0].length;i++){
					ArrBody1[0] = ssb_1.toString().split("@");
					
					ArrBody1[1] = ssb_2.toString().split("@");
					
					ArrBody1[2] = ssb_3.toString().split("@");
					
					ArrBody1[3] = ssb_4.toString().split("@");//mt==mar
					
					ArrBody1[4] = ssb_5.toString().split("@");
				
					ArrBody1[5] = ssb_6.toString().split("@");
			
					ArrBody1[6] = ssb_7.toString().split("@");
					
					ArrBody1[7] = ssb_8.toString().split("@");
					
					ArrBody1[8] = ssb_9.toString().split("@");
					ArrBody1[9] = ssb_10.toString().split("@");
				
					ArrBody1[10] = ssb_11.toString().split("@");
					
					ArrBody1[11] = ssb_12.toString().split("@");
				
					ArrBody1[12] = ssb_13.toString().split("@");
				
					ArrBody1[13] = ssb_14.toString().split("@");
					
					ArrBody1[14] = ssb_15.toString().split("@");
					if(i>=3){
						
						ArrBody[0][i]=ArrBody1[0][i+(getCurrentPage()-1)*6];//5
						ArrBody[1][i]=ArrBody1[1][i+(getCurrentPage()-1)*6];
						ArrBody[2][i]=ArrBody1[2][i+(getCurrentPage()-1)*6];
						ArrBody[3][i]=ArrBody1[3][i+(getCurrentPage()-1)*6];
						ArrBody[4][i]=ArrBody1[4][i+(getCurrentPage()-1)*6];
						ArrBody[5][i]=ArrBody1[5][i+(getCurrentPage()-1)*6];
						ArrBody[6][i]=ArrBody1[6][i+(getCurrentPage()-1)*6];
						ArrBody[7][i]=ArrBody1[7][i+(getCurrentPage()-1)*6];
						ArrBody[8][i]=ArrBody1[8][i+(getCurrentPage()-1)*6];
						ArrBody[9][i]=ArrBody1[9][i+(getCurrentPage()-1)*6];
						ArrBody[10][i]=ArrBody1[10][i+(getCurrentPage()-1)*6];
						ArrBody[11][i]=ArrBody1[11][i+(getCurrentPage()-1)*6];
						ArrBody[12][i]=ArrBody1[12][i+(getCurrentPage()-1)*6];
						ArrBody[13][i]=ArrBody1[13][i+(getCurrentPage()-1)*6];
						ArrBody[14][i]=ArrBody1[14][i+(getCurrentPage()-1)*6];
					}
					if(i<3){
						ArrBody[0][i]=ArrBody1[0][i];
						ArrBody[1][i]=ArrBody1[1][i];
						ArrBody[2][i]=ArrBody1[2][i];
						ArrBody[3][i]=ArrBody1[3][i];
						ArrBody[4][i]=ArrBody1[4][i];
						ArrBody[5][i]=ArrBody1[5][i];
						ArrBody[6][i]=ArrBody1[6][i];
						ArrBody[7][i]=ArrBody1[7][i];
						ArrBody[8][i]=ArrBody1[8][i];
						ArrBody[9][i]=ArrBody1[9][i];
						ArrBody[10][i]=ArrBody1[10][i];
						ArrBody[11][i]=ArrBody1[11][i];
						ArrBody[12][i]=ArrBody1[12][i];
						ArrBody[13][i]=ArrBody1[13][i];
						ArrBody[14][i]=ArrBody1[14][i];
					}
				}
			}

		} else {
			return null;
		}
		

		int[] ArrWidth= new int[9];
		int[] ArrWidth1 = new int[3 + rs.getRows()];
//		System.out.println(getCurrentPage()+"getCurrentPage()"+_AllPages+"_AllPages");
		if (getCurrentPage()==1) {//查询出来1条到5条之间
			 ArrWidth1 = new int[3 + rs.getRows()];
			 for (int i = 0; i < 3 + rs.getRows(); i++) {
					if(i==0||i==2){
		        		
		        		ArrWidth1[i]=65;
		        	}else if(i==1){
		        		
		        		ArrWidth1[i]=100;
		        	}else{
		        		ArrWidth1[i]=480/rs.getRows();
		        	}
		        	
		        }
			 ArrWidth=ArrWidth1;
		}
		
		
		if(getCurrentPage() < _AllPages&&getCurrentPage()!=1){//查询出来大于5条并且不是最后一页
			 ArrWidth1 = new int[9];//8
			 for (int i = 0; i < 9; i++) {//8
					if(i==0||i==2){
		        		
		        		ArrWidth1[i]=65;
		        	}else if(i==1){
		        		
		        		ArrWidth1[i]=100;
		        	}else{
		        		ArrWidth1[i]=480/6;//8
		        	}
		        	
		        }
			 ArrWidth=ArrWidth1;
		}
		
		if(getCurrentPage() < _AllPages&&getCurrentPage()==1){//查询出来大于5条并且不是最后一页
			 ArrWidth1 = new int[9];//8
			 for (int i = 0; i < 9; i++) {//8
//				 System.out.println(ArrWidth1[i]=480/2);
					if(i==0||i==2){
		        		
		        		ArrWidth1[i]=65;
		        	}else if(i==1){
		        		
		        		ArrWidth1[i]=100;
		        	}else{
		        		ArrWidth1[i]=480/6;//5
		        	}
		        	
		        }
			 ArrWidth=ArrWidth1;
		}
		
		
		if(getCurrentPage() == _AllPages&&getCurrentPage()!=1&&rs.getRows()>=7){//查询出来大于5条并且是最后一页
			if(Math.round((rs.getRows()%6))==0){
				
				ArrWidth1 = new int[3+6];//5
				
				for (int i = 0; i < 3 + 6; i++) {//5
					if(i==0||i==2){
						
						ArrWidth1[i]=65;
					}else if(i==1){
						
						ArrWidth1[i]=100;
					}else{
						ArrWidth1[i]=480/6;//5
					}

					}
					ArrWidth = ArrWidth1;
				}
				if (Math.round((rs.getRows() % 6)) != 0) {
					ArrWidth1 = new int[3 + Math.round((rs.getRows() % 6))];// 5

					for (int i = 0; i < 3 + Math.round((rs.getRows() % 6)); i++) {// 5
						if (i == 0 || i == 2) {

							ArrWidth1[i] = 65;
						} else if (i == 1) {

							ArrWidth1[i] = 100;
						} else {
							ArrWidth1[i] = 480 / Math.round((rs.getRows() % 6));// 5
						}

					}
					ArrWidth = ArrWidth1;
				}
		}
		
		

		
		rt.setTitle("<font face='宋体' size='6'>煤质异常报告</font>" +
				"<table width='740'><tr>&nbsp;</tr><tr width='600'>" +
				"<td width='100'>&nbsp;</td><td align='left' colspan='1'><font face='宋体' size='4'>主  报：沈总&nbsp;&nbsp;孟总&nbsp;&nbsp; 徐总&nbsp;&nbsp; 金总&nbsp;&nbsp; 杨总&nbsp;&nbsp; 畅总 </font>  </td>" +
				"<td align='right'>  <font face='宋体' size='4'>  紧急程度： 紧急</font></td><td>&nbsp;</td></tr>" +
				"<tr width='600' height='20'><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>"+
				"<tr width='600'><td width='100'>&nbsp;</td><td align='left' colspan='4'><font face='宋体' size='4'>抄 送: &nbsp;&nbsp;一期:  发电部  策划部  安监部  燃料部  机务设备部  仪电设备部</font></td> <td>&nbsp;</td></tr>" +
				"<tr width='600'><td width='100'>&nbsp;</td><td align='left' colspan='4'><font face='宋体' size='4'>  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 二期:  发电部  综合经营部  设备部  安监部</font></td><td>&nbsp;</td></tr>" +
				"<tr width='600' height='20'><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>"+
				"<tr width='600'><td width='100'>&nbsp;</td><td align='left'><font face='宋体' size='4'>报告人： 环保与技术监督部 </font> </td><td align='right'>  <font face='宋体' size='4'> 制表时间:"+getBRiq().replaceAll("-", ".")+"</font></td><td>&nbsp;</td></tr>" +
				"<tr width='600'><td width='100'>&nbsp;</td><td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; " +
				" </td><td align='right'><font face='宋体' size='4'> 入厂时间:"
				+daohrq
				+
				"</font></td></tr>" +
				"<tr width='600' height='30'><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>"+
				"</table>" +
				""
				, ArrWidth);
//		rt.setDefaultTitle(7, 2, getBRiq().replaceFirst("-", "年").replaceFirst("-", "月")+"日", Table.ALIGN_RIGHT);
				rt.setDefaultTitle(1, 3, "一、煤质异常情况及监督建议", Table.ALIGN_LEFT);
		rt.setBody(new Table(ArrBody, 0, 0, 0));//new Table(ArrBody, 0, 0, 0)//new Table(17,9)
//				rt.setBody(new Table(15,rs.getRows()+3));
		rt.body.setWidth(ArrWidth);
		
		
		//...
		if(getCurrentPage()==1&&rs.getRows()<7){//6
			
			rt.body.setCells(1, 1, 15,8, Table.PER_ALIGN, Table.ALIGN_CENTER);
			
			for(int i=1;i<=rt.body.getRows();i++){
				if(i==15){
					rt.body.setRowHeight(i, 50);
				}else{
					rt.body.setRowHeight(i, 50);
				}
				
				
			}
			
			for(int i = 1; i <= 15; i ++) {
				rt.body.setRowCells(i, Table.PER_FONTSIZE, 9);
			}
			
			rt.body.mergeCell(1, 1, 2, 2);
			rt.body.setCells(1, 1, 2, 2, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.mergeCell(3, 1, 3, 2);
			rt.body.mergeCell(4, 1, 4, 2);
			rt.body.mergeCell(5, 1, 5, 2);
			rt.body.mergeCell(6, 1, 6, 2);
			
			rt.body.mergeCell(7, 1, 12, 1);
			rt.body.mergeCell(13, 1, 15, 1);
			rt.body.mergeCell(13, 2, 13, 3);
			rt.body.mergeCell(14, 2, 14, 3);
			rt.body.mergeCell(15, 2, 15, 3);
			rt.body.mergeCell(1, 3, 2, 3);
			rt.body.mergeCell(7, 2, 8, 2);
			rt.body.mergeCell(9, 2, 10, 2);
			rt.body.mergeCell(11, 2, 12, 2);
			rt.body.setCells(1, 3, 2, 3, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.setCells(3, 1, 6, 2, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.setCells(7, 1, 15, 1, Table.PER_ALIGN, Table.ALIGN_CENTER);
			if(rs.getRows()>=1){
				
				rt.body.mergeCell(1, 4, 1, rs.getRows()+3);//rs.getRows()-5*（当前页数-1）+3
				rt.body.setCells(1, 4, 1, rs.getRows()+3, Table.PER_ALIGN, Table.ALIGN_CENTER);
				rt.body.setCells(3, 4, 6, rs.getRows()+3, Table.PER_ALIGN, Table.ALIGN_RIGHT);
				
				rt.body.setCells(7, 4, 15, rs.getRows()+3, Table.PER_ALIGN, Table.ALIGN_CENTER);
			}
		}
		
		if (getCurrentPage() < _AllPages && getCurrentPage() != 1&&rs.getRows()>=7) {// 查询出来大于5条并且不是最后一页

			rt.body.setCells(1, 1, 15, 8, Table.PER_ALIGN, Table.ALIGN_CENTER);

			for (int i = 1; i <= rt.body.getRows(); i++) {
				if (i == 15) {
					rt.body.setRowHeight(i, 50);
				} else {
					rt.body.setRowHeight(i, 50);
				}

			}

			for (int i = 1; i <= 15; i++) {
				rt.body.setRowCells(i, Table.PER_FONTSIZE, 9);
			}

			rt.body.mergeCell(1, 1, 2, 2);
			rt.body.setCells(1, 1, 2, 2, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.mergeCell(3, 1, 3, 2);
			rt.body.mergeCell(4, 1, 4, 2);
			rt.body.mergeCell(5, 1, 5, 2);
			rt.body.mergeCell(6, 1, 6, 2);

			rt.body.mergeCell(7, 1, 12, 1);
			rt.body.mergeCell(13, 1, 15, 1);
			rt.body.mergeCell(13, 2, 13, 3);
			rt.body.mergeCell(14, 2, 14, 3);
			rt.body.mergeCell(15, 2, 15, 3);
			rt.body.mergeCell(1, 3, 2, 3);
			rt.body.mergeCell(7, 2, 8, 2);
			rt.body.mergeCell(9, 2, 10, 2);
			rt.body.mergeCell(11, 2, 12, 2);
			rt.body.setCells(1, 3, 2, 3, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.setCells(3, 1, 6, 2, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.setCells(7, 1, 15, 1, Table.PER_ALIGN, Table.ALIGN_CENTER);
//			if (rs.getRows() >= 1) {
//
				rt.body.mergeCell(1, 4, 1, 6 + 3);// rs.getRows()-5*（当前页数-1）+3//5
				rt.body.setCells(1, 4, 1, 6 + 3, Table.PER_ALIGN,//5
						Table.ALIGN_CENTER);
				rt.body.setCells(3, 4, 6, 6 + 3, Table.PER_ALIGN,//5
						Table.ALIGN_CENTER);

				rt.body.setCells(7, 4, 15, 6 + 3, Table.PER_ALIGN,//5
						Table.ALIGN_CENTER);
//			}
		}
		
		if (getCurrentPage() < _AllPages && getCurrentPage() == 1&&rs.getRows()>=7) {// 查询出来大于5条并且不是最后一页

			rt.body.setCells(1, 1, 15, 9, Table.PER_ALIGN, Table.ALIGN_CENTER);

			for (int i = 1; i <= rt.body.getRows(); i++) {
				if (i == 15) {
					rt.body.setRowHeight(i, 50);
				} else {
					rt.body.setRowHeight(i, 50);
				}

			}

			for (int i = 1; i <= 15; i++) {
				rt.body.setRowCells(i, Table.PER_FONTSIZE, 9);
			}

			rt.body.mergeCell(1, 1, 2, 2);
			rt.body.setCells(1, 1, 2, 2, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.mergeCell(3, 1, 3, 2);
			rt.body.mergeCell(4, 1, 4, 2);
			rt.body.mergeCell(5, 1, 5, 2);
			rt.body.mergeCell(6, 1, 6, 2);

			rt.body.mergeCell(7, 1, 12, 1);
			rt.body.mergeCell(13, 1, 15, 1);
			rt.body.mergeCell(13, 2, 13, 3);
			rt.body.mergeCell(14, 2, 14, 3);
			rt.body.mergeCell(15, 2, 15, 3);
			rt.body.mergeCell(1, 3, 2, 3);
			rt.body.mergeCell(7, 2, 8, 2);
			rt.body.mergeCell(9, 2, 10, 2);
			rt.body.mergeCell(11, 2, 12, 2);
			rt.body.setCells(1, 3, 2, 3, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.setCells(3, 1, 6, 2, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.setCells(7, 1, 15, 1, Table.PER_ALIGN, Table.ALIGN_CENTER);
			if (rs.getRows() >= 1) {

				rt.body.mergeCell(1, 4, 1, 6 + 3);// rs.getRows()-5*（当前页数-1）+3//5
				rt.body.setCells(1, 4, 1, 6 + 3, Table.PER_ALIGN,//5
						Table.ALIGN_CENTER);
				rt.body.setCells(3, 4, 6, 6 + 3, Table.PER_ALIGN,//5
						Table.ALIGN_CENTER);

				rt.body.setCells(7, 4, 15, 6 + 3, Table.PER_ALIGN,//5
						Table.ALIGN_CENTER);
			}
		}
		
		if(getCurrentPage() == _AllPages&&getCurrentPage()!=1&&rs.getRows()>=7){//查询出来大于5条并且是最后一页
			
			rt.body.setCells(1, 1, 15, 8, Table.PER_ALIGN, Table.ALIGN_CENTER);

			for (int i = 1; i <= rt.body.getRows(); i++) {
				if (i == 15) {
					rt.body.setRowHeight(i, 50);
				} else {
					rt.body.setRowHeight(i, 50);
				}

			}

			for (int i = 1; i <= 15; i++) {
				rt.body.setRowCells(i, Table.PER_FONTSIZE, 9);
			}

			rt.body.mergeCell(1, 1, 2, 2);
			rt.body.setCells(1, 1, 2, 2, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.mergeCell(3, 1, 3, 2);
			rt.body.mergeCell(4, 1, 4, 2);
			rt.body.mergeCell(5, 1, 5, 2);
			rt.body.mergeCell(6, 1, 6, 2);
//rt.body.setPageRows(20);
			rt.body.mergeCell(7, 1, 12, 1);
			rt.body.mergeCell(13, 1, 15, 1);
			rt.body.mergeCell(13, 2, 13, 3);
			rt.body.mergeCell(14, 2, 14, 3);
			rt.body.mergeCell(15, 2, 15, 3);
			rt.body.mergeCell(1, 3, 2, 3);
			rt.body.mergeCell(7, 2, 8, 2);
			rt.body.mergeCell(9, 2, 10, 2);
			rt.body.mergeCell(11, 2, 12, 2);
			rt.body.setCells(1, 3, 2, 3, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.setCells(3, 1, 6, 2, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.setCells(7, 1, 15, 1, Table.PER_ALIGN, Table.ALIGN_CENTER);
			if (rs.getRows() >= 1&&(rs.getRows()%6)!=0) {
				rt.body.mergeCell(1, 4, 1, Math.round((rs.getRows()%6)) + 3);// rs.getRows()-5*（当前页数-1）+3//5
				rt.body.setCells(1, 4, 1, Math.round((rs.getRows()%6)) + 3, Table.PER_ALIGN,//5
						Table.ALIGN_CENTER);
				rt.body.setCells(3, 4, 6, Math.round((rs.getRows()%6)) + 3, Table.PER_ALIGN,//5
						Table.ALIGN_CENTER);

				rt.body.setCells(7, 4, 15, Math.round((rs.getRows()%6)) + 3, Table.PER_ALIGN,//5
						Table.ALIGN_CENTER);
			}
			
			if (rs.getRows() >= 1&&(rs.getRows()%6)==0) {
								rt.body.mergeCell(1, 4, 1, 6 + 3);// rs.getRows()-5*（当前页数-1）+3//5
								rt.body.setCells(1, 4, 1, 6 + 3, Table.PER_ALIGN,//5
										Table.ALIGN_CENTER);
								rt.body.setCells(3, 4, 6, 6 + 3, Table.PER_ALIGN,//5
										Table.ALIGN_CENTER);

								rt.body.setCells(7, 4, 15, 6 + 3, Table.PER_ALIGN,//5
										Table.ALIGN_CENTER);
							}
		}
		//整个页面居中对齐
		for(int colAlign=1;colAlign<=rt.body.getCols();colAlign++){
			rt.body.setColAlign(colAlign, Table.ALIGN_CENTER);
		}
		rt.createDefautlFooter(ArrWidth);
		
		ssff.append(rt.getAllPagesHtml((_CurrentPage-1)));
		_CurrentPage++;
		}
		
		
		if (rs.getRows() == 0) {
			_AllPages = 0;

			_CurrentPage = 0;

		} else {
			_AllPages = _CurrentPage - 1;

			_CurrentPage = 1;
		}
		
		rs.close();
		con.Close();
//		System.out.println(ssff.toString());
		return ssff.toString();//rt.getAllPagesHtml();
		
	}
//	 刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("入厂日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRiq", "Form0");// 与html页中的id绑定,并自动刷新
		dfb.setId("riq");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText("-"));

		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree"
				,""+visit.getDiancxxb_id(),"forms[0]",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("-"));

		
		ToolbarButton rbtn = new ToolbarButton(null, "刷新",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		// tb1.addText(new ToolbarText("<marquee width=300
		// scrollamount=2></marquee>"));
		
		

		
		setToolbar(tb1);
	}
//	
	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) this.getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (treeid != null) {
			if (!treeid.equals(this.treeid)) {
				((TextField) getToolbar().getItem("diancTree_text"))
						.setValue(((IDropDownModel) getDiancmcModel())
								.getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}

	private IPropertySelectionModel _IDiancModel;

	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	private boolean _DiancmcChange = false;

	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getDiancmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == Value) {
			_DiancmcChange = false;
		} else {
			_DiancmcChange = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean10(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel10(_value);
	}
//	 工具栏使用的方法
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	
	// 日期是否变化
	private boolean riqchange = false;
//	绑定日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		if (this.briq != null) {
			if (!this.briq.equals(briq))
				riqchange = true;
		}
//		this.riq = riq;
		
		this.briq = briq;
	}



	/*
	 * 从数据库查询出来的车皮号字符串可能很长，从而影响到报表宽度的正常显示，
	 * 这里给车皮号字符串加上<br>(换行符)来处理这一问题。
	 */
	public String getCheph(String strCheph) {
		String[] arr = strCheph.split(",");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i ++) {
			sb.append(arr[i] + ",");
			if (sb.toString().split(",").length % 10 == 0) {
				sb.append("<br>");
			}
		}
		if (sb.toString().endsWith("<br>")) {
			return sb.toString();
		} else {
			return sb.substring(0, sb.length() - 1);
		}
	}
	
	// 按钮的监听事件
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
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
	


	
	public String getTreeScript() {
//		System.out.print(((Visit) this.getPage().getVisit()).getDefaultTree().getScript());
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
		
	}
	

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(this.getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));

			
			visit.setDropDownBean10(null);
			visit.setProSelectionModel10(null);
			visit.setDefaultTree(null);
			setTreeid(null);
		}
		if (riqchange) {
			riqchange = false;
			
			
		}
		
		getSelectData();
	}
	
}
