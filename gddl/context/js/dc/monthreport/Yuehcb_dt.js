var BasicValue=500;
function CountKuc(gridDiv_ds,i){
	currec = gridDiv_ds.getAt(i);
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
}
function CountAllKuc(gridDiv_ds){
	for(i=2;i<gridDiv_ds.getCount();i++){
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
				lj.set('KUC',DataValue + ChangeValue);
			}else{
				currec.set('KUC',DataValue - ChangeValue);
				lj.set('KUC',DataValue - ChangeValue);
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

function sjClear(gridDiv_ds, DataIndex){
	for(i=2;i<gridDiv_ds.getCount();i=i+2){
		currec = gridDiv_ds.getAt(i);
		lj = gridDiv_ds.getAt(i+1);
		currec.set(DataIndex,0);
		lj.set(DataIndex, 0);
	}
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
			if(currec.get('YMINGC')=='铁路'){
				if(DataIndex=='FADY'){
					ChangeValue = eval(currec.get('SHOUML')||0);
				}
			}
			if(ChangeValue > Sp){
				ChangeValue = Sp;
			}
			Sp = Sp - ChangeValue;
			currec.set(DataIndex,ChangeValue);
			if(DataIndex=='PANYK'||DataIndex=='SHUIFCTZ'){
				currec.set('KUC',DataValue + ChangeValue);
				lj.set('KUC',DataValue + ChangeValue);
			}else{
				currec.set('KUC',DataValue - ChangeValue);
				lj.set('KUC',DataValue - ChangeValue);
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
					lj.set('KUC',DataValue + ChangeValue);
				}else{
					currec.set('KUC',DataValue - ChangeValue);
					lj.set('KUC',DataValue - ChangeValue);
				}
				lj.set(DataIndex, eval(lj.get(DataIndex)||0) + ChangeValue);
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
	
	sjClear(gridDiv_ds, 'FADY');
	sjClear(gridDiv_ds, 'GONGRY');
	sjClear(gridDiv_ds, 'QITH');
	sjClear(gridDiv_ds, 'SUNH');
	sjClear(gridDiv_ds, 'JITCS');
	sjClear(gridDiv_ds, 'SHUIFCTZ');
	sjClear(gridDiv_ds, 'DIAOCL');
	sjClear(gridDiv_ds, 'PANYK');
	
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
	if(validateHydx(gridDiv_ds,'FADY')){
		Ext.MessageBox.alert("提示信息","发电耗用数据不正确！");
		return true;
	}
	if(validateHydx(gridDiv_ds,'GONGRY')){
		Ext.MessageBox.alert("提示信息","供热耗用数据不正确！");
		return true;
	}
	if(validateHydx(gridDiv_ds,'QITH')){
		Ext.MessageBox.alert("提示信息","其它耗用数据不正确！");
		return true;
	}
	if(validateHydx(gridDiv_ds,'SUNH')){
		Ext.MessageBox.alert("提示信息","存损数据不正确！");
		return true;
	}
	if(validateHydx(gridDiv_ds,'DIAOCL')){
		Ext.MessageBox.alert("提示信息","调出量数据不正确！");
		return true;
	}
	if(validateHydx(gridDiv_ds,'PANYK')){
		Ext.MessageBox.alert("提示信息","盘盈亏数据不正确！");
		return true;
	}
	if(validateHydx(gridDiv_ds,'SHUIFCTZ')){
		Ext.MessageBox.alert("提示信息","水分差调整量数据不正确！");
		return true;
	}
	if(validateHydx(gridDiv_ds,'JITCS')){
		Ext.MessageBox.alert("提示信息","计提储损数据不正确！");
		return true;
	}
}
function validateHydx(gridDiv_ds,Field){
	var zj = eval(gridDiv_ds.getAt(0).get(Field));
	var dx = 0;
	for(i=2;i<gridDiv_ds.getCount();i=i+2){
		dx = eval(dx + eval(gridDiv_ds.getAt(i).get(Field)));
    }
    //alert(Field + ": " + zj + "----" + dx);
    return zj!=dx;
}
