var BasicValue=500;
function CountKuc(gridDiv_ds,i){
	currec = gridDiv_ds.getAt(i);
	lj = gridDiv_ds.getAt(i+1);
	QICKC = eval(currec.get('QICKC')||0);
	SHOUML = eval(currec.get('SHOUML')||0);
	FADY = eval(currec.get('FADY')||0);
	GONGRY = eval(currec.get('GONGRY')||0);
	QITH = eval(currec.get('QITH')||0);
	SUNH = eval(currec.get('SUNH')||0);
	DIAOCL = eval(currec.get('DIAOCL')||0);
	PANYK = eval(currec.get('PANYK')||0);
	SHUIFCTZ=eval(currec.get('SHUIFCTZ')||0);
	currec.set('KUC',QICKC +SHOUML -FADY -GONGRY -QITH -DIAOCL +PANYK+SHUIFCTZ-SUNH);
	currec.set('JITCS',SUNH);
	
	SUNH = eval(lj.get('SUNH')||0);
	lj.set('KUC',currec.get('KUC'));
	lj.set('JITCS',SUNH);
	
	/*QICKC = eval(lj.get('QICKC')||0);
	SHOUML = eval(lj.get('SHOUML')||0);
	FADY = eval(lj.get('FADY')||0);
	GONGRY = eval(lj.get('GONGRY')||0);
	QITH = eval(lj.get('QITH')||0);
	SUNH = eval(lj.get('SUNH')||0);
	DIAOCL = eval(lj.get('DIAOCL')||0);
	PANYK = eval(lj.get('PANYK')||0);
	SHUIFCTZ=eval(lj.get('SHUIFCTZ')||0);
	lj.set('KUC',currec.get('KUC'));
	lj.set('JITCS',SUNH);*/
}
function CountAllKuc(gridDiv_ds){
	for(i=2;i<gridDiv_ds.getCount();i = i + 2){
		CountKuc(gridDiv_ds,i);
    }
}
//Negative
function CountBasic(gridDiv_ds, DataIndex, Value){
	for(i=2;i<gridDiv_ds.getCount();i=i+2){
		currec = gridDiv_ds.getAt(i);
		lj = gridDiv_ds.getAt(i+1);
		DataValue = eval(currec.get('KUC')||0);
		ChangeValue = 0;
		if(DataValue <= BasicValue){
			if(Value > DataValue){
				ChangeValue = DataValue;
			}else{
				ChangeValue = Value;
			}
			//alert(i + " ----- "+ ChangeValue);
			currec.set(DataIndex,ChangeValue);
			if(DataIndex=='PANYK'||DataIndex=='SHUIFCTZ'){
				currec.set('KUC',DataValue + ChangeValue);
				//lj.set('KUC',DataValue + ChangeValue);
				lj.set('KUC',currec.get('KUC'));
			}else{
				currec.set('KUC',DataValue - ChangeValue);
				//lj.set('KUC',DataValue - ChangeValue);
				lj.set('KUC',currec.get('KUC'));
			}
			lj.set(DataIndex, eval(lj.get(DataIndex)||0) + ChangeValue);
			if(ChangeValue == Value){
				return 0;
			}else{
				Value = Value - ChangeValue;
			}
		}
	}
	return Value;
}
function CountPer(gridDiv_ds, DataIndex, Value, Smkchj){
	Sp = Value;
	for(i=2;i<gridDiv_ds.getCount();i=i+2){
		currec = gridDiv_ds.getAt(i);
		lj = gridDiv_ds.getAt(i+1);
		DataValue = eval(currec.get('KUC')||0);
		kchj = eval(currec.get('QICKC')||0)+eval(currec.get('SHOUML')||0);
		ChangeValue = 0;
		if(DataValue > 0){
			ChangeValue = Math.round(Value * kchj / Smkchj);
			if(ChangeValue > Sp){
				ChangeValue = Sp;
			}
			Sp = Sp - ChangeValue;
			currec.set(DataIndex,ChangeValue);
			if(DataIndex=='PANYK'||DataIndex=='SHUIFCTZ'){
				currec.set('KUC',DataValue + ChangeValue);
				//lj.set('KUC',DataValue + ChangeValue);
				lj.set('KUC',currec.get('KUC'));
			}else{
				currec.set('KUC',DataValue - ChangeValue);
				//lj.set('KUC',DataValue - ChangeValue);
				lj.set('KUC',currec.get('KUC'));
			}
			lj.set(DataIndex, eval(lj.get(DataIndex)||0) + ChangeValue);
		}
	}
	CountSp(gridDiv_ds, DataIndex, Sp);
}
function CountSp(gridDiv_ds, DataIndex, Sp){
	if(Sp > 0){
		for(i=2;i<gridDiv_ds.getCount();i=i+2){
			currec = gridDiv_ds.getAt(i);
			lj = gridDiv_ds.getAt(i+1);
			DataValue = eval(currec.get('KUC')||0);
			if(DataValue > Sp){
				ChangeValue = Sp;
				currec.set(DataIndex,eval(currec.get(DataIndex)||0) + ChangeValue);
				if(DataIndex=='PANYK'||DataIndex=='SHUIFCTZ'){
					currec.set('KUC',DataValue + ChangeValue);
					lj.set(DataIndex, eval(lj.get(DataIndex)||0) + ChangeValue);
					//lj.set('KUC',DataValue + ChangeValue);
					lj.set('KUC',currec.get('KUC'));
				}else{
					currec.set('KUC',DataValue - ChangeValue);
					lj.set(DataIndex, eval(lj.get(DataIndex)||0) + ChangeValue);
					//lj.set('KUC',DataValue - ChangeValue);
					lj.set('KUC',currec.get('KUC'));
				}
				
				break;
			}
		}
	}
}
function CountShc(gridDiv_grid){
	Ext.MessageBox.show({
		msg: '正在计算耗用分配,请等待...',
		progressText: '计算中...',
		width:300,
		wait:true,
		waitConfig: {interval:200},
		icon:Ext.MessageBox.INFO //custom class in msg-box.html
	});
	gridDiv_ds = gridDiv_grid.getStore();
	zrec = gridDiv_ds.getAt(0)
	Qickcsy = Qickcz = eval(zrec.get('QICKC')||0);
	Shoumlsy = Shoumlz = eval(zrec.get('SHOUML')||0);
	Qicsmhsy = Qicsmhz = Qickcz +Shoumlz ;
	Fadysy = Fadyz = eval(zrec.get('FADY')||0);
	Gongrysy = Gongryz = eval(zrec.get('GONGRY')||0);
	Qithsy = Qithz = eval(zrec.get('QITH')||0);
	Sunhsy = Sunhz = eval(zrec.get('SUNH')||0);
	Diaoclsy = Diaoclz = eval(zrec.get('DIAOCL')||0);
	Panyksy = Panykz = eval(zrec.get('PANYK')||0);
	Shuifctzsy=Shuifctzz=eval(zrec.get('SHUIFCTZ')||0);
	Jitcssy=Jitcsz=eval(zrec.get('JITCS')||0);
	Kuczsy = Kuczz = eval(zrec.get('KUC')||0);
	CountAllKuc(gridDiv_ds);
	
	Fadysy = CountBasic(gridDiv_ds, 'FADY', Fadysy);
	if(Fadysy != 0){
		CountPer(gridDiv_ds, 'FADY', Fadysy, Qicsmhsy);
	}
	
	Gongrysy = CountBasic(gridDiv_ds, 'GONGRY', Gongrysy);
	if(Gongrysy != 0){
		CountPer(gridDiv_ds, 'GONGRY', Gongrysy , Qicsmhsy);
	}
	

	Qithsy = CountBasic(gridDiv_ds, 'QITH', Qithsy);
	if(Qithsy != 0){
		CountPer(gridDiv_ds, 'QITH', Qithsy, Qicsmhsy);
	}
	
	Sunhsy = CountBasic(gridDiv_ds, 'SUNH', Sunhsy);
	if(Sunhsy != 0){
		CountPer(gridDiv_ds, 'SUNH', Sunhsy, Qicsmhsy);
	}
	
	for (i=2;i<gridDiv_ds.getCount();i=i+2){
		currec = gridDiv_ds.getAt(i);
		lj = gridDiv_ds.getAt(i+1);
		currec.set('JITCS',eval(currec.get('SUNH')||0));
		lj.set('JITCS',eval(lj.get('SUNH')||0));
	}
//	Jitcssy=CountBasic(gridDiv_ds, 'JITCS', Jitcssy);
//	
//	if(Jitcssy != 0){
//		CountPer(gridDiv_ds, 'JITCS', Jitcssy, Qicsmhsy);
//	}
	
	Shuifctzsy=CountBasic(gridDiv_ds,'SHUIFCTZ',Shuifctzsy);
	if(Shuifctzsy!=0){
		CountPer(gridDiv_ds, 'SHUIFCTZ', Shuifctzsy, Qicsmhsy);
	}
	
	Diaoclsy = CountBasic(gridDiv_ds, 'DIAOCL', Diaoclsy);
	if(Diaoclsy!=0){
		CountPer(gridDiv_ds, 'DIAOCL', Diaoclsy, Qicsmhsy);
	}

	Panyksy = CountBasic(gridDiv_ds, 'PANYK', Panyksy);
	if(Panyksy!=0){
		CountPer(gridDiv_ds, 'PANYK', Panyksy, Qicsmhsy);
	}
	
	_Msg = "计算过程结束！";
	/*
	needManual = false;
	if(Diaoclsy != 0){
		_Msg += "<调出量>";
		needManual = true;
	}
	if(Panyksy != 0){
		_Msg += "<盘盈亏>";
		needManual = true;
	}
	if(needManual){
		_Msg += "未能自动计算，请手动分配！";
	}
	*/
	Ext.MessageBox.hide();
	Ext.MessageBox.alert("提示信息",_Msg);   
}

function validateHy(gridDiv_ds){
	var validate=0;
	validate=validateHydx(gridDiv_ds,'FADY');
	if(validate!=0){
		Ext.MessageBox.alert("提示信息","发电耗用数据不正确！<br>与合计数据相差"+validate);
		return true;
	}
	validate=validateHydx(gridDiv_ds,'GONGRY');
	if(validate!=0){
		Ext.MessageBox.alert("提示信息","供热耗用数据不正确！<br>与合计数据相差"+validate);
		return true;
	}
	validate=validateHydx(gridDiv_ds,'QITH');
	if(validate!=0){
		Ext.MessageBox.alert("提示信息","其它耗用数据不正确！<br>与合计数据相差"+validate);
		return true;
	}
	validate=validateHydx(gridDiv_ds,'SUNH');
	if(validate!=0){
		Ext.MessageBox.alert("提示信息","存损数据不正确！<br>与合计数据相差"+validate);
		return true;
	}
	validate=validateHydx(gridDiv_ds,'DIAOCL');
	if(validate!=0){
		Ext.MessageBox.alert("提示信息","调出量数据不正确！<br>与合计数据相差"+validate);
		return true;
	}
	validate=validateHydx(gridDiv_ds,'PANYK');
	if(validate!=0){
		Ext.MessageBox.alert("提示信息","盘盈亏数据不正确！<br>与合计数据相差"+validate);
		return true;
	}
	validate=validateHydx(gridDiv_ds,'SHUIFCTZ');
	if(validate!=0){
		Ext.MessageBox.alert("提示信息","水分差调整量数据不正确！<br>与合计数据相差"+validate);
		return true;
	}
	validate=validateHydx(gridDiv_ds,'JITCS');
	if(validate!=0){
		Ext.MessageBox.alert("提示信息","计提储损数据不正确！<br>与合计数据相差"+validate);
		return true;
	}
	
	if(validateFuzhi(gridDiv_ds,'KUC')){
		Ext.MessageBox.alert("提示信息","库存数据不正确！<br>库存数据不应为负数");
		return true;
	}
}

function validateFuzhi(gridDiv_ds,Field){
	for(i=2;i<gridDiv_ds.getCount();i=i+2){
		if(gridDiv_ds.getAt(i).get(Field)<0){
			return true;
		}
    }
    return false;
}

function validateHydx(gridDiv_ds,Field){
	var zj = eval(gridDiv_ds.getAt(0).get(Field));
	var dx = 0;
	for(i=2;i<gridDiv_ds.getCount();i=i+2){
		dx = eval(dx + eval(gridDiv_ds.getAt(i).get(Field)));
    }
    //alert(Field + ": " + zj + "----" + dx);
    return eval(zj-dx);
}

