package com.zhiren.sms;

// SendMessage.java - 金笛短信服务器 v3.2 发送示例.
// www.sendsms.cn

import cn.sendsms.*;
import cn.sendsms.modem.*;
import cn.sendsms.OutboundMessage;

import java.util.*;
import java.io.*;
import java.net.*;


public class JinDiSms{
	private Service srv;
	private OutboundMessage msg;
	
	public OutboundMessage  getMsg(){
		return msg;
	}
		
	//开始短信服务，传入串口号字符串 COM1
	public boolean Start(String strCom){
		OutboundNotification outboundNotification = new OutboundNotification();
		srv = new Service();
		SerialModemGateway gateway = new SerialModemGateway( "jindi", strCom, 9600, "Wavecom", "M1306B");

		gateway.setInbound(true);
		gateway.setOutbound(true);
		gateway.setSimPin("0000");
		srv.setOutboundNotification(outboundNotification);
		
		try {
			srv.addGateway(gateway);
		} catch (GatewayException e) {
			e.printStackTrace();
		}
		
		try {
			srv.startService();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (GatewayException e) {
			e.printStackTrace();
		} catch (SMSLibException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	//停止服务
	public boolean Stop(){
		try {
			srv.stopService();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (GatewayException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	//strNumbe 手机号
	//strMessage 短信息
	public boolean SendMessage(String strNumber,String strMessage){
		boolean blnIsSend=false;
		// 发送短信
		msg = new OutboundMessage(strNumber, strMessage);
		msg.setEncoding(Message.MessageEncodings.ENCUCS2);
		msg.setStatusReport(true);

		try {
			blnIsSend=srv.sendMessage(msg);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (GatewayException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return blnIsSend;
	}

	public class OutboundNotification implements IOutboundMessageNotification{
		public void process(String gatewayId, OutboundMessage msg){
			System.out.println("状态报告: " + gatewayId);
			System.out.println(msg);
		}
	}
}
