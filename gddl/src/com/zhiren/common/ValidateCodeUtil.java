package com.zhiren.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ValidateCodeUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成方法存根
		String osName = System.getProperty("os.name");
		String hashCode = "";
		String MacAddr = "";
		try{
			if(osName.indexOf("unknow") > -1){
	        	hashCode = InetAddress.getLocalHost().getHostName();
	        	System.out.println("ValidateCode : " + hashCode);
	        }else{
	        	String line;
				Process process = Runtime.getRuntime().exec("ipconfig /all");
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(process.getInputStream()));
				while ((line = bufferedReader.readLine()) != null) {
					if (line.indexOf("Physical Address") != -1) {
						if (line.indexOf(":") != -1) {
							MacAddr = line.substring(line.indexOf(":") + 2);
							hashCode = getValidateCode(MacAddr);
							System.out.println("ValidateCode : " + hashCode);
						}
					}
				}
				process.waitFor();
	        }
		}catch(UnknownHostException uhe){
        	uhe.printStackTrace();
        }
        catch(IOException ioe){
        	ioe.printStackTrace();
        }
        catch(InterruptedException ie){
        	ie.printStackTrace();
        }
        catch(Exception e){
        	e.printStackTrace();
        }
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in)); 
		System.out.println("请输入网卡地址(按回车退出)："); 
		try {
			MacAddr = br.readLine();
			if(MacAddr==null || "".equals(MacAddr)){
				return;
			}
			hashCode = getValidateCode(MacAddr);
			System.out.println("ValidateCode : " + hashCode);
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			System.exit(0);
		}
	}
	public static String getValidateCode(String MacAddr){
		String hashCode;
		String MacAddrs = "";
		if(MacAddr.indexOf("-")>-1){
			String pas[] = MacAddr.split("-");
			for(int i=0;i<pas.length;i++){
				MacAddrs += pas[i];
			}
		}else{
			MacAddrs = MacAddr;
		}
		hashCode = String.valueOf(Long.parseLong(MacAddrs,16));
		return hashCode;
	}
}
