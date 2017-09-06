var CommPort = "1";
var BaudRate = "4800";
var DataBits = "8";
var StopBits = "1";
var ParitySetting = "n";
var Jilhh = "A";
var DataMode = 1;
var isTest = 0;

//var CurrFile ;

function ResetMsCom(){
	if(isTest==0){
//	alert(CommPort + ","+ BaudRate + ","+ DataBits +"," + ParitySetting +"," + StopBits + "," + Jilhh + "," + DataMode);
		MSComm1.CommPort= CommPort;
		MSComm1.Settings=""+ BaudRate + ","+ ParitySetting +"," + DataBits +"," + StopBits + "";
		MSComm1.InputLen=0   ;   
		if(!MSComm1.PortOpen){
			MSComm1.PortOpen=true;
			document.getElementById("DTfont").style.color="yellow";
		}
		setTimeout('getData();',500);
	}else{
		document.getElementById("DTfont").innerHTML = "测试<br>状态";
	}
}
function setParam4Set(){
	var filePath = "D:/zhiren/shul/汽车衡参数设置";
	setParam(filePath);
}
function setParam(filePath){
	var fso, fd, fc, f;
	fso = new ActiveXObject("Scripting.FileSystemObject");
	/*if(fso.FolderExists(filePath)){
		fd = fso.GetFolder(filePath);		
		fc = new Enumerator(fd.files);
		for (; !fc.atEnd(); fc.moveNext()){
			if(fso.FileExists(fc.item())){
				if(!isNaN(curfile)){
					if(fc.item() == curfile){
						continue;
					}
				}
				curfile = fc.item();
				f = fso.OpenTextFile(curfile, 1, false);	
			}	   					
		}
	}*/
	if(fso.FileExists(filePath)){
		f = fso.OpenTextFile(filePath, 1, false);
		var line = 0;
		while (!f.AtEndOfStream){
			switch (line++){
				case 0:CommPort = f.ReadLine().split("=")[1];break;
				case 1:BaudRate = f.ReadLine().split("=")[1];break;
				case 2:DataBits = f.ReadLine().split("=")[1];break;
				case 3:StopBits = f.ReadLine().split("=")[1];break;
				case 4:ParitySetting = f.ReadLine().split("=")[1];break;
				case 5:Jilhh = f.ReadLine().split("=")[1];break;
				case 6:DataMode = f.ReadLine().split("=")[1];break;
				case 7:isTest = f.ReadLine().split("=")[1];break;
				default : break;
			}
			if(line > 10)break;
		}
		f.Close();
	}
}

body.onload =function(){
	document.getElementById("dongtzl").value=0.0;
	document.getElementById("DTfont").style.color="red";
	document.getElementById("caijzl").value=0.0;
	setParam4Set();
	ResetMsCom();
}

function getData(){
	if(MSComm1.InBufferCount>7){
		var Instring = MSComm1.Input;
		var str = Instring;
		var Stemp="";
		var temp1="";
		var temp2="";
		var zhongl = 0;
		switch(DataMode){
//			数据原型< +0265000>;
			case "1": str = Instring.substring(1, 7);
				zhongl = parseInt(str, 10);
				break;
//        	数据原型<025000000000> 实际值为25.000
			case "10": str = Instring.substring(0, 32);
				for(var i=0;i<=32;i++){
					if(str.substr(i,1)==' '){
						temp2=str.substring(i+1,i+10);
						break;
					}
				}
				zhongl = parseInt(temp2,10);
				zhongl=eval(zhongl/1000);
				break;

//			case "10": str = Instring.substring(0, 6);
//        		zhongl = parseInt(str,10);
//        		break;
//			数据原型<=005.5300> 实际值为35.5        		
			case "20": str = Instring.substring(1, 9);
				for(var i = 8;i>= 0;i--){
					Stemp = Stemp + str.substr(i,1);
				}
				zhongl=Stemp*1000;
				break;
			case "30": str = Instring.substring(3, 14);
				zhongl= parseInt(str ,10);
				break;
			case "40": str = Instring.substring(4, 10);
        		zhongl = parseInt(str,10);
        		break;
        	case "50": str = Instring.substring(4, 9);
        		zhongl = parseInt(str,10);
        		break;
//			数据原型<=00935  > 实际值为53.90
//			数据原型< =00935 > 实际值为53.90
			case "60": str = Instring.substring(0, 16);
				for(var i=0;i<=7;i++){
					if(str.substr(i,1)=='='){
						temp1=str.substring(i+1,i+6);
					}
				}
				for(var i =(temp1.length-1);i>= 0;i--){
					if(temp1.substr(i,1)==' '||temp1.substr(i,1)=='='){
						Stemp=Stemp+'0';
					}else 
						Stemp = Stemp + temp1.substr(i,1);
				}
				zhongl=parseInt(Stemp,10);
				zhongl=eval(zhongl);
				break;
		}
		if(isNaN(zhongl)){
			zhongl = 0.0;
		}
		var zl = eval(zhongl/1000);
		document.getElementById("dongtzl").value = zl;
		
        var Lastzl = eval(document.all.item("Lastzl").value);
        if(zl > 0){
        	if(Lastzl == zl){
        		document.getElementById("DTfont").style.color = '#00FF00';
				document.all.item("CaijButton").disabled = false;
        	}else{
        		document.all.item("CaijButton").disabled = true;
				document.getElementById("DTfont").style.color = 'yellow';
				document.all.item("Lastzl").value = zl;
        	}
        }else{
    		document.all.item("CaijButton").disabled = false;
        	document.getElementById("DTfont").style.color = 'yellow';
        }
	}
	setTimeout('getData();',500);
}
function caij(Mode, DataIndex){
//	设置汽车衡测试参数
	if(isTest==2){
		document.all.item("caijzl").value = eval(Math.round(Math.random()*1000))/100+20;
		isTest = 1;
	}else
		if(isTest==1){
			document.all.item("caijzl").value = eval(Math.round(Math.random()*1000))/100+60;
			isTest = 2;
		}else
			document.all.item("caijzl").value = eval(document.all.item("dongtzl").value);
	
	if(Ext.isReady){
		var rec;
		if(Mode == "sel"){
			rec = grid.getSelectionModel().getSelected();
			if(rec == null){
				Ext.MessageBox.alert('提示信息','请选中采集皮重的车皮');
				return;
			}
		}else
			rec = grid.getStore().getAt(0);
		rec.set(DataIndex,eval(document.all.item("caijzl").value));
		if(DataIndex=="MAOZ"){
			rec.set("ZHONGCHH",Jilhh);
		}else
		if(DataIndex=="PIZ"){
			rec.set("QINGCHH",Jilhh);
		}
	}
	return true;
}
//检毛检皮在一个页面的采集按钮触发事件
function caij1(){
//	设置汽车衡测试参数
	if(isTest==2){
		document.all.item("caijzl").value = eval(Math.round(Math.random()*1000))/100+20;
		isTest = 1;
	}else
		if(isTest==1){
			document.all.item("caijzl").value = eval(Math.round(Math.random()*1000))/100+60;
			isTest = 2;
		}else
			document.all.item("caijzl").value = eval(document.all.item("dongtzl").value);
	
	if(Ext.isReady){
		var rec;
		if(DataIndex=="MAOZ"){
			if(Mode == "sel")
				rec = grid.getSelectionModel().getSelected();
			else
				rec = grid.getStore().getAt(0);
			rec.set("ZHONGCHH",Jilhh);
		}else
		if(DataIndex=="PIZ"){
			if(Mode == "sel")
				rec = gridp.getSelectionModel().getSelected();
			else
				rec = gridp.getStore().getAt(0);
			rec.set("QINGCHH",Jilhh);
		}
		rec.set(DataIndex,eval(document.all.item("caijzl").value));
	}
	return true;
}