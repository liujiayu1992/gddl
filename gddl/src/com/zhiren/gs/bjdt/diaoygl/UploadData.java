package com.zhiren.gs.bjdt.diaoygl;

import java.io.File;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.text.DecimalFormat;

import com.zhiren.common.tools.FtpUpload;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.tools.FtpCreatTxt;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public class UploadData {

	private Date m_date;//日期
	private String m_msg;//提示消息
	
	public Date getDate(){
		return this.m_date;
	}
	public void setDate(Date mdate){
		this.m_date = mdate;
	}
	
	public String getMsg(){
		return this.m_msg;
	}
	public void setMsg(String msg){
		this.m_msg=msg;
	}
	
	private String serverIp;
	public void setServerIp(String ip){
		serverIp = ip;
	}
	public String getServerIp(){
		return this.serverIp;
	}
	private String serverUserName;
	public void setServerUserName(String username){
		serverUserName = username;
	}
	public String getServerUserName(){
		return this.serverUserName;
	}
	private String serverPassWord;
	public void setServerPassWord(String password){
		serverPassWord = password;
	}
	public String getServerPassWord(){
		return this.serverPassWord;
	}
	
	public void getServerInfo(){
		String sql = "select xt.mingc,xt.zhi from xitxxb xt where xt.mingc in ('上级服务器IP','上级服务器用户名','上级服务器密码')";
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		for(int i=0;rsl.next();i++){
			if(rsl.getString("mingc").equals("上级服务器IP")){
				setServerIp(rsl.getString("zhi"));
			}
			if(rsl.getString("mingc").equals("上级服务器用户名")){
				setServerUserName(rsl.getString("zhi"));
			}
			if(rsl.getString("mingc").equals("上级服务器密码")){
				setServerPassWord(rsl.getString("zhi"));
			}
		}
		rsl.close();
		con.Close();
	}
	
	
	private Date WeekFistDate(Date date,int next){//得到当前日期所在周
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -(next+1));
		return  cal.getTime();
	}
	private void getUploadFtp(String tableName,boolean shangcfs)
    {
		int num = 0;
		FtpUpload fu = new FtpUpload();
        if(tableName == ""){
//            setMsg("没有数据可上报！");
        	return;
        }else{
            try{
                String upload = "";
                
                if(getServerIp()==null || getServerIp().equals("")){
                	getServerInfo();
                }
                
                

//                String ip = "10.81.4.214";
//                String ip = "219.141.254.214";
//                String username = "111700";
//                String password = "111700";
                String filepath = "";
                String FileName[]=tableName.split(",");
                num = FileName.length;
                fu.connectServer(getServerIp(), getServerUserName(), getServerPassWord(), filepath);
                for(int i=0;i<FileName.length;i++){
	                String filename = "C://Ribsc//" + FileName[i] + ".txt";
	                upload = fu.upload(filename,  FileName[i] + ".txt" + "\n");
	                
	                System.out.println("/***************************"+FileName[i]+"日报上报成功！******************/"+(new Date()));
                }
            }
            catch(Exception e)
            {
            	if(shangcfs){//手动上传
                	setMsg("网络出现问题，上报失败!");
                }else{
            		setMsg("");
            	}
                System.out.println("/**********************  网络出现问题，数据上报失败!  ***************/"+(new Date()));
                e.printStackTrace();
                return;
            }finally{
            	fu.closeConnect();
            	upDatezt(shangcfs);
            }
            if(shangcfs){//手动上传
            	setMsg("上报成功!");
            }else{
        		setMsg("");
        	}
            System.out.println("/********************** 数据上报成功 *************/"+(new Date()));
            upDatezt(shangcfs);
        }
    }
	
	private void upDatezt(boolean shangcfs){
		
		JDBCcon con = new JDBCcon();
		try{
			if(shangcfs){
				String riq = DateUtil.Formatdate("yyyy-MM-dd",getDate());
				String sql = "update fenkshcrb set shangbzt=1 where riq=to_date('" 
						   + riq+"','yyyy-mm-dd') and shangbzt=0 ";
				con.getUpdate(sql);
				
			}else{
				String riq = DateUtil.Formatdate("yyyy-MM-dd",new Date());
				for(int i=0;i<10;i++){
					
					String sql = "update fenkshcrb set shangbzt=1 where riq=to_date('" 
							   +riq+"','yyyy-mm-dd')-"+(i+1)+" and shangbzt=0 ";
					con.getUpdate(sql);
				}
			}
		}catch(Exception e){
			System.out.println("/********************* 数据状态更新失败 **************/"+(new Date()));
			e.printStackTrace();
		}finally{
			con.Close();
		}
	}
	public void ShangcTXTFile(boolean shangcfs)
    {
        String UploadFilename="";
        Date riq = getDate();

        if(!(new File("c://Ribsc")).isDirectory())
            (new File("c://Ribsc")).mkdir();
        JDBCcon con = new JDBCcon();
        FtpCreatTxt ct = new FtpCreatTxt();
        StringBuffer fileline = new StringBuffer();
        
        String filedata = "";
        String strdate = "";
        String diancbm = "";
        double hetjh = 0;
        double dangrgm = 0;
        double leijgm = 0;
        double dangr = 0;
        double leij = 0;
        double dangrhy = 0;
        double leijhy = 0;
        double kuc = 0;
        int hasdata = 0;
        String sql = "select gs.bianm from diancxxb gs where gs.jib=2 ";
        try
        {
            ResultSetList rs = con.getResultSetList(sql);
            while(rs.next())
            {
                String gsbm = rs.getString("bianm");
                if(shangcfs){
                	String FileName = "HC" + gsbm.substring(0, 1) + gsbm.substring(3) + FormatDate(riq).substring(8, 10);
//	                String FileName = "TEST_HC" + gsbm.substring(0, 1) + gsbm.substring(3) + FormatDate(riq).substring(8, 10);
	                ct.CreatTxt("c://Ribsc/" + FileName + ".txt");
	                
	                String datasql = 
	                	"select to_char(max(h.riq),'yyyymmdd') as riq,decode(grouping(dc.bianm),1,max(gs.bianm),dc.bianm) as diancbm,\n" +
	                	"       sum(dh.hej*10000) as hetjh,sum(h.dangrgm) as dangrgm,sum(lj.dangrgm) as leijgm,\n" + 
	                	"       sum(h.dangrgm) as dangr,sum(lj.dangrgm) as leij,sum(h.haoyqkdr) as dangrhy,sum(lj.haoyqkdr) as leijhy,\n" + 
	                	"       sum(h.kuc) as kuc\n" + 
	                	"  from shouhcrbb h,diancxxb gs,dianckjpxb px,\n" + 
	                	"       (select dc.id,dc.mingc,dc.bianm,dc.zhengccb,dc.rijhm from diancxxb dc where dc.jib=3) dc,\n" + 
	                	"     (select dh.diancxxb_id,sum(dh.hej) as hej from niancgjhb dh\n" + 
	                	"       where dh.riq=First_day(" + OraDate(riq) + ") group by dh.diancxxb_id) dh,\n" + 
	                	"     (select h.diancxxb_id,sum(h.dangrgm) as dangrgm,sum(h.haoyqkdr) as haoyqkdr from shouhcrbb h\n" + 
	                	"       where h.riq>=First_day(" + OraDate(riq) + ")\n" + 
	                	"         and h.riq<=" + OraDate(riq) + " group by h.diancxxb_id) lj\n" + 
	                	"  where h.diancxxb_id=dc.id and h.diancxxb_id=dh.diancxxb_id(+) and h.diancxxb_id=lj.diancxxb_id\n" + 
	                	"    and dc.id=px.diancxxb_id and gs.jib=2 and px.shujsbzt=1 and px.kouj='月报'\n" + 
	                	"    and h.riq=" + OraDate(riq) + "\n" + 
	                	"  group by rollup(dc.bianm)  order by grouping(dc.bianm) desc,max(px.xuh)";

	                
	                ResultSetList rsdata = con.getResultSetList(datasql);
	                if(! con.getHasIt(datasql.toString()))
	                {
	                    (new File("c://Ribsc/" + FileName + ".txt")).delete();
	                    continue;
	                } else
	                {
	                    while(rsdata.next())
	                    {
	                        filedata = "";
	                        fileline.setLength(0);
	                        strdate = rsdata.getString("riq");
	                        diancbm = rsdata.getString("diancbm");
	                        hetjh = rsdata.getDouble("hetjh");
	                        dangrgm = rsdata.getDouble("dangrgm");
	                        leijgm = rsdata.getDouble("leijgm");
	                        dangr = rsdata.getDouble("dangr");
	                        leij = rsdata.getDouble("leij");
	                        dangrhy = rsdata.getDouble("dangrhy");
	                        leijhy = rsdata.getDouble("leijhy");
	                        kuc = rsdata.getDouble("kuc");
	                        fileline.append(getStr(8, strdate));
	                        fileline.append(getStr(6, diancbm));
	                        fileline.append(getNum(8, 0, String.valueOf(Math.round(hetjh))));
	                        fileline.append(getNum(8, 0, String.valueOf(Math.round(dangrgm))));
	                        fileline.append(getNum(8, 0, String.valueOf(Math.round(leijgm))));
	                        fileline.append(getNum(8, 0, String.valueOf(Math.round(dangr))));
	                        fileline.append(getNum(8, 0, String.valueOf(Math.round(leij))));
	                        fileline.append(getNum(8, 0, String.valueOf(Math.round(dangrhy))));
	                        fileline.append(getNum(8, 0, String.valueOf(Math.round(leijhy))));
	                        fileline.append(getNum(8, 0, String.valueOf(Math.round(kuc))));
	                        filedata = fileline.toString();
							
							ct.aLine(filedata);//写入行数据
							hasdata++;
	                    }
	                    ct.finish();
	                    rsdata.close();
	                }
	                if(hasdata>0){
	                	if(UploadFilename.equals("")){
		                	UploadFilename = FileName;
		                }else{
		                	UploadFilename = UploadFilename +"," +FileName;
		                }
	                }
                }else{
	                for(int i=0;i<7;i++){//7天以内的数据
	                	
	            		riq = WeekFistDate(new Date(),i);
		                String FileName = "HC" + gsbm.substring(0, 1) + gsbm.substring(3) + FormatDate(riq).substring(8, 10);
//		                String FileName = "TEST_HC" + gsbm.substring(0, 1) + gsbm.substring(3) + FormatDate(riq).substring(8, 10);
		                ct.CreatTxt("c://Ribsc/" + FileName + ".txt");
		                String datasql = 
		                		"select to_char(max(h.riq),'yyyymmdd') as riq,decode(grouping(dc.bianm),1,max(gs.bianm),dc.bianm) as diancbm,\n" +
			                	"       sum(dh.hej*10000) as hetjh,sum(h.dangrgm) as dangrgm,sum(lj.dangrgm) as leijgm,\n" + 
			                	"       sum(h.dangrgm) as dangr,sum(lj.dangrgm) as leij,sum(h.haoyqkdr) as dangrhy,sum(lj.haoyqkdr) as leijhy,\n" + 
			                	"       sum(h.kuc) as kuc\n" + 
			                	"  from shouhcrbb h,diancxxb gs,dianckjpxb px,\n" + 
			                	"       (select dc.id,dc.mingc,dc.bianm,dc.zhengccb,dc.rijhm from diancxxb dc where dc.jib=3) dc,\n" + 
			                	"     (select dh.diancxxb_id,sum(dh.hej) as hej from niancgjhb dh\n" + 
			                	"       where dh.riq=First_day(" + OraDate(riq) + ") group by dh.diancxxb_id) dh,\n" + 
			                	"     (select h.diancxxb_id,sum(h.dangrgm) as dangrgm,sum(h.haoyqkdr) as haoyqkdr from shouhcrbb h\n" + 
			                	"       where h.riq>=First_day(" + OraDate(riq) + ")\n" + 
			                	"         and h.riq<=" + OraDate(riq) + " group by h.diancxxb_id) lj\n" + 
			                	"  where h.diancxxb_id=dc.id and h.diancxxb_id=dh.diancxxb_id(+) and h.diancxxb_id=lj.diancxxb_id\n" + 
			                	"    and dc.id=px.diancxxb_id and gs.jib=2 and px.shujsbzt=1 and px.kouj='月报' \n" + 
			                	"    and h.riq=" + OraDate(riq) + "\n" + 
			                	"  group by rollup(dc.bianm)  order by grouping(dc.bianm) desc,max(px.xuh)";

		                ResultSetList rsdata = con.getResultSetList(datasql);
		                if(! con.getHasIt(datasql.toString()))
		                {
		                    (new File("c://Ribsc/" + FileName + ".txt")).delete();
		                    continue;
		                } else
		                {
		                    while(rsdata.next())
		                    {
		                        filedata = "";
		                        fileline.setLength(0);
		                        strdate = rsdata.getString("riq");
		                        diancbm = rsdata.getString("diancbm");
		                        hetjh = rsdata.getDouble("hetjh");
		                        dangrgm = rsdata.getDouble("dangrgm");
		                        leijgm = rsdata.getDouble("leijgm");
		                        dangr = rsdata.getDouble("dangr");
		                        leij = rsdata.getDouble("leij");
		                        dangrhy = rsdata.getDouble("dangrhy");
		                        leijhy = rsdata.getDouble("leijhy");
		                        kuc = rsdata.getDouble("kuc");
		                        fileline.append(getStr(8, strdate));
		                        fileline.append(getStr(6, diancbm));
		                        fileline.append(getNum(8, 0, String.valueOf(Math.round(hetjh))));
		                        fileline.append(getNum(8, 0, String.valueOf(Math.round(dangrgm))));
		                        fileline.append(getNum(8, 0, String.valueOf(Math.round(leijgm))));
		                        fileline.append(getNum(8, 0, String.valueOf(Math.round(dangr))));
		                        fileline.append(getNum(8, 0, String.valueOf(Math.round(leij))));
		                        fileline.append(getNum(8, 0, String.valueOf(Math.round(dangrhy))));
		                        fileline.append(getNum(8, 0, String.valueOf(Math.round(leijhy))));
		                        fileline.append(getNum(8, 0, String.valueOf(Math.round(kuc))));
		                        filedata = fileline.toString();
								
								ct.aLine(filedata);//写入行数据
								hasdata++;
		                    }
		                    ct.finish();
		                    rsdata.close();
		                }
		                if(hasdata>0){
		                	if(UploadFilename.equals("")){
			                	UploadFilename = FileName;
			                }else{
			                	UploadFilename = UploadFilename +"," +FileName;
			                }
		                }
	                }
                }
                
            }
//            ct.finish();
            if(UploadFilename.length()<=0){
            	System.out.println("/********************** 没有数据或当日数据已上报! *********************/" + (new Date()));
            	if(shangcfs){
            		setMsg("没有数据或当日数据已上报!");
            	}else{
            		setMsg("");
            	}
            	return;
            }
            
            rs.close();
        }
        catch(Exception e)
        {
        	if(shangcfs){
        		setMsg("数据上报失败!");
        	}else{
        		setMsg("");
        	}
        	System.out.println("/************************** 数据上报失败 *****************/" + (new Date()));
            e.printStackTrace();
            return;
        }
        finally
        {
            con.Close();	
        }
        getUploadFtp(UploadFilename,shangcfs);
//        return;
    }
	private String getStr(int weis,String str){
		StringBuffer Str_zf = new StringBuffer();
		if(str==null || str.equals("")){
			for (int i=0;i<weis;i++){
				Str_zf.append(" ");
			}
		}else{
			char[] Str=str.toCharArray();
			int Str_lenght=Str.length;
			
			for (int j=0;j<Str_lenght;j++){
				String Strs=""+Str[j];
				Str_zf.append(Strs);
			}
			int cha=0;
			if (Str_lenght!=weis){
				cha=weis-Str_lenght;
				for (int i=0;i<cha;i++){
					Str_zf.append(" ");
				}
			}
		}
//		System.out.println(Str_zf.toString());
		return Str_zf.toString();
	}
	private String getNum(int weis,int xiaos,String Number){//得到位数及数符串
		StringBuffer Str_zf = new StringBuffer();
		String str="";
		str=Number;
		if(str.equals("") ){
			for (int j=0;j<weis-xiaos-2;j++){
				String Strs="";
				Str_zf.append(Strs);
			}
			Str_zf.append(0.);
			for (int j=0;j<xiaos;j++){
				Str_zf.append(0);
			}
		}else{
			int zhengsw=0; 
			if(xiaos!=0){//带小数位的
				String[] c=str.split("\\.");
				String strs1=c[0];//整数位
				char[] Str1=strs1.toCharArray();//整数位
				String Strs2=c[1];//小数位
				char[] Str2=Strs2.toCharArray();//小数位
				//录入整数位
				zhengsw=weis-xiaos-1;
				if (Str1.length!=zhengsw){
					int cha=zhengsw-Str1.length;
					for (int i=0;i<cha;i++){
						Str_zf.append(" ");
					}
				}
				for (int j=0;j<Str1.length;j++){
					String Strs=""+Str1[j];
					Str_zf.append(Strs);
				}
				//录入小数位
				Str_zf.append(".");
				if(Str2.length!=xiaos){
					for (int j=0;j<Str2.length;j++){
						String Strs=""+Str2[j];
						Str_zf.append(Strs);
					}
					for (int j=0;j<xiaos-Str2.length;j++){
						Str_zf.append(0);
					}
				}else{
					for (int j=0;j<Str2.length;j++){
						String Strs=""+Str2[j];
						Str_zf.append(Strs);
					}
				}
			}else{//不带小数位
				char[] Str=str.toCharArray();
				int Str_lenght=Str.length;
				int cha=0;
				if (Str_lenght!=weis){//补空格
					cha=weis-Str_lenght;
					for (int i=0;i<cha;i++){
						Str_zf.append(" ");
					}
				}
				for (int j=0;j<Str_lenght;j++){//录入数据
					String Strs=""+Str[j];
					Str_zf.append(Strs);
				}
			}
		}

//		System.out.println(Str_zf.toString());
		return Str_zf.toString();
	}
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	
}
