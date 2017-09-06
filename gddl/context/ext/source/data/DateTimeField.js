Ext.form.DateTimeField = Ext.extend(Ext.form.TriggerField,  {
    format : "m/d/Y H:i:s",
    altFormats : "m/d/Y|m-d-y|m-d-Y|m/d|m-d|md|mdy|mdY|d|Y-m-d",
    disabledDays : null,
    disabledDaysText : "Disabled",
    disabledDates : null,
    disabledDatesText : "Disabled",
    minValue : null,
    maxValue : null,
    minText : "The date in this field must be equal to or after {0}",
    maxText : "The date in this field must be equal to or before {0}",
    invalidText : "{0} is not a valid date - it must be in the format {1}",
    triggerClass : 'x-form-date-trigger',

        defaultAutoCreate : {tag: "input", type: "text", size: "10", autocomplete: "off"},

    initComponent : function(){
        Ext.form.DateTimeField.superclass.initComponent.call(this);
        if(typeof this.minValue == "string"){
            this.minValue = this.parseDate(this.minValue);
        }
        if(typeof this.maxValue == "string"){
            this.maxValue = this.parseDate(this.maxValue);
        }
        this.ddMatch = null;
        if(this.disabledDates){
            var dd = this.disabledDates;
            var re = "(?:";
            for(var i = 0; i < dd.length; i++){
                re += dd[i];
                if(i != dd.length-1) re += "|";
            }
            this.ddMatch = new RegExp(re + ")");
        }
    },

        validateValue : function(value){
        value = this.formatDate(value);
        if(!Ext.form.DateTimeField.superclass.validateValue.call(this, value)){
            return false;
        }
        if(value.length < 1){              return true;
        }
        var svalue = value;
        value = this.parseDate(value);
        if(!value){
            this.markInvalid(String.format(this.invalidText, svalue, this.format));
            return false;
        }
        var time = value.getTime();
        if(this.minValue && time < this.minValue.getTime()){
            this.markInvalid(String.format(this.minText, this.formatDate(this.minValue)));
            return false;
        }
        if(this.maxValue && time > this.maxValue.getTime()){
            this.markInvalid(String.format(this.maxText, this.formatDate(this.maxValue)));
            return false;
        }
        if(this.disabledDays){
            var day = value.getDay();
            for(var i = 0; i < this.disabledDays.length; i++) {
            	if(day === this.disabledDays[i]){
            	    this.markInvalid(this.disabledDaysText);
                    return false;
            	}
            }
        }
        var fvalue = this.formatDate(value);
        if(this.ddMatch && this.ddMatch.test(fvalue)){
            this.markInvalid(String.format(this.disabledDatesText, fvalue));
            return false;
        }
        return true;
    },

            validateBlur : function(){
        return !this.menu || !this.menu.isVisible();
    },


    getValue : function(){
     	var v = this.parseDate(Ext.form.DateTimeField.superclass.getValue.call(this));
    	return this.formatDate(v)
    },

    setValue : function(date){
        Ext.form.DateTimeField.superclass.setValue.call(this, this.formatDate(this.parseDate(date)));
    },

        parseDate : function(value){
        if(!value || value instanceof Date){
            return value;
        }
        var v = Date.parseDate(value, this.format);
        if(!v && this.altFormats){
            if(!this.altFormatsArray){
                this.altFormatsArray = this.altFormats.split("|");
            }
            for(var i = 0, len = this.altFormatsArray.length; i < len && !v; i++){
                v = Date.parseDate(value, this.altFormatsArray[i]);
            }
        }
        return v;
    },

        onDestroy : function(){
        if(this.wrap){
            this.wrap.remove();
        }
        Ext.form.DateTimeField.superclass.onDestroy.call(this);
    },

        formatDate : function(date){
        return (!date || !(date instanceof Date)) ?
               date : date.dateFormat(this.format);
    },

        menuListeners : {
        select: function(m, d){
            this.setValue(d);
        },
        show : function(){             this.onFocus();
        },
        hide : function(){
            this.focus.defer(10, this);
            var ml = this.menuListeners;
            this.menu.un("select", ml.select,  this);
            this.menu.un("show", ml.show,  this);
            this.menu.un("hide", ml.hide,  this);
        }
    },

            onTriggerClick : function(){
        if(this.disabled){
            return;
        }
        if(this.menu == null){
            this.menu = new Ext.menu.DateTimeMenu();
        }
        Ext.apply(this.menu.picker,  {
            minDate : this.minValue,
            maxDate : this.maxValue,
            disabledDatesRE : this.ddMatch,
            disabledDatesText : this.disabledDatesText,
            disabledDays : this.disabledDays,
            disabledDaysText : this.disabledDaysText,
            format : this.format,
            minText : String.format(this.minText, this.formatDate(this.minValue)),
            maxText : String.format(this.maxText, this.formatDate(this.maxValue))
        });
        this.menu.on(Ext.apply({}, this.menuListeners, {
            scope:this
        }));
        var v = this.getValue();
		if(v){
			v = Date.parseDate(v, this.format);
		}	
        this.menu.picker.setValue(v || new Date());
        //this.menu.picker.setValue(this.getValue() || new Date());
        this.menu.show(this.el, "tl-bl?");
    },

    beforeBlur : function(){
        var v = this.parseDate(this.getRawValue());
        if(v){
            this.setValue(v);
        }
    }

});
Ext.reg('datetimefield', Ext.form.DateTimeField);

Ext.menu.DateTimeMenu = function(config){
    Ext.menu.DateTimeMenu.superclass.constructor.call(this, config);
    this.plain = true;
    var di = new Ext.menu.DateTimeItem(config);
    this.add(di);
    this.picker = di.picker;
    this.relayEvents(di, ["select"]);
    this.on('beforeshow', function(){
        if(this.picker){
            this.picker.hideMonthPicker(true);
        }
    }, this);
};
Ext.extend(Ext.menu.DateTimeMenu, Ext.menu.Menu, {
    cls:'x-date-menu'
});

Ext.menu.DateTimeItem = function(config){
    Ext.menu.DateTimeItem.superclass.constructor.call(this, new Ext.DateTimePicker(config), config);

    this.picker = this.component;
    this.addEvents('select');

    this.picker.on("render", function(picker){
        picker.getEl().swallowEvent("click");
        picker.container.addClass("x-menu-date-item");
    });
    this.picker.on("select", this.onSelect, this);
};

Ext.extend(Ext.menu.DateTimeItem, Ext.menu.Adapter, {
        onSelect : function(picker, date){
        this.fireEvent("select", this, date, picker);
        Ext.menu.DateTimeItem.superclass.handleClick.call(this);
    }
});

Ext.DateTimePicker = Ext.extend(Ext.Component, {
    todayText : "OK",
    okText : "&#160;OK&#160;",
    cancelText : "Cancel",
    todayTip : "{0} (Spacebar)",
    minDate : null,
    maxDate : null,
    minText : "This date is before the minimum date",
    maxText : "This date is after the maximum date",
    format : "m/d/Y H:i:s",
    disabledDays : null,
    disabledDaysText : "",
    disabledDatesRE : null,
    disabledDatesText : "",
    constrainToViewport : true,
    monthNames : Date.monthNames,
    dayNames : Date.dayNames,
    nextText: 'Next Month (Control+Right)',
    prevText: 'Previous Month (Control+Left)',
    monthYearText: 'Choose a month (Control+Up/Down to move years)',

    startDay : 0,

    initComponent : function(){
        Ext.DateTimePicker.superclass.initComponent.call(this);
//add for datetimepicker
    this.hourel = document.createElement("SELECT");
    this.minuteel = document.createElement("SELECT");
    this.secondel = document.createElement("SELECT");
        this.value = this.value ?
                 this.value.clearTime() : new Date().clearTime();

        this.addEvents(
            'select'
        );
        if(this.handler){
            this.on("select", this.handler,  this.scope || this);
        }
        this.initDisabledDays();
    },


    initDisabledDays : function(){
        if(!this.disabledDatesRE && this.disabledDates){
            var dd = this.disabledDates;
            var re = "(?:";
            for(var i = 0; i < dd.length; i++){
                re += dd[i];
                if(i != dd.length-1) re += "|";
            }
            this.disabledDatesRE = new RegExp(re + ")");
        }
    },


    setValue : function(value){
        var old = this.value;

//add for datetimepicker
        if(this.hourel.options.length>0) this.hourel.selectedIndex = value.getHours();
       	if(this.minuteel.options.length>0) this.minuteel.selectedIndex = value.getMinutes();
       	if(this.secondel.options.length>0) this.secondel.selectedIndex = value.getSeconds();
        this.value = value;
        if(this.el){
            this.update(this.value);
        }
    },


    getValue : function(){
        return this.value;
    },


    focus : function(){
        if(this.el){
            this.update(this.activeDate);
        }
    },


    onRender : function(container, position){
        var m = [
             '<table cellspacing="0" width="200px">',
                '<tr><td class="x-date-left"><a href="#" title="', this.prevText ,'">&#160;</a></td><td class="x-date-middle" align="center"></td><td class="x-date-right"><a href="#" title="', this.nextText ,'">&#160;</a></td></tr>',
                '<tr><td colspan="3"><table class="x-date-inner" cellspacing="0"><thead><tr>'];
        var dn = this.dayNames;
        for(var i = 0; i < 7; i++){
            var d = this.startDay+i;
            if(d > 6){
                d = d-7;
            }
            m.push("<th><span>", dn[d].substr(0,1), "</span></th>");
        }
        m[m.length] = "</tr></thead><tbody><tr>";
        for(var i = 0; i < 42; i++) {
            if(i % 7 == 0 && i != 0){
                m[m.length] = "</tr><tr>";
            }
            m[m.length] = '<td><a href="#" hidefocus="on" class="x-date-date" tabIndex="1"><em><span></span></em></a></td>';
        }
        m[m.length] = '</tr></tbody></table></td></tr>';
//DateTime add start
        m[m.length] = '<tr><td colspan="3" class="x-date-time" align="center" style="font-size:15px">';
        m[m.length] = '<table cellspacing="0"><tr><td class="x-date-hour"></td><td>:</td><td class="x-date-minute"></td><td>:</td><td class="x-date-second"></td></tr></table>';
        m[m.length] = '</td></tr>';
//DateTime add start
        m[m.length] = '<tr><td colspan="3" class="x-date-bottom" align="center"></td></tr></table><div class="x-date-mp"></div>';

        var el = document.createElement("div");
        el.className = "x-date-picker";
        el.innerHTML = m.join("");

        container.dom.insertBefore(el, position);

        this.el = Ext.get(el);
        this.eventEl = Ext.get(el.firstChild);

        new Ext.util.ClickRepeater(this.el.child("td.x-date-left a"), {
            handler: this.showPrevMonth,
            scope: this,
            preventDefault:true,
            stopDefault:true
        });

        new Ext.util.ClickRepeater(this.el.child("td.x-date-right a"), {
            handler: this.showNextMonth,
            scope: this,
            preventDefault:true,
            stopDefault:true
        });

        this.eventEl.on("mousewheel", this.handleMouseWheel,  this);

        this.monthPicker = this.el.down('div.x-date-mp');
        this.monthPicker.enableDisplayMode('block');

        var kn = new Ext.KeyNav(this.eventEl, {
            "left" : function(e){
                e.ctrlKey ?
                    this.showPrevMonth() :
                    this.update(this.activeDate.add("d", -1));
            },

            "right" : function(e){
                e.ctrlKey ?
                    this.showNextMonth() :
                    this.update(this.activeDate.add("d", 1));
            },

            "up" : function(e){
                e.ctrlKey ?
                    this.showNextYear() :
                    this.update(this.activeDate.add("d", -7));
            },

            "down" : function(e){
                e.ctrlKey ?
                    this.showPrevYear() :
                    this.update(this.activeDate.add("d", 7));
            },

            "pageUp" : function(e){
                this.showNextMonth();
            },

            "pageDown" : function(e){
                this.showPrevMonth();
            },

            "enter" : function(e){
                e.stopPropagation();
                return true;
            },

            scope : this
        });

        this.eventEl.on("click", this.handleDateClick,  this, {delegate: "a.x-date-date"});

        this.eventEl.addKeyListener(Ext.EventObject.SPACE, this.selectToday,  this);

        this.el.unselectable();

        this.cells = this.el.select("table.x-date-inner tbody td");
        this.textNodes = this.el.query("table.x-date-inner tbody span");
//add start
        var tdhourel = this.el.child("td.x-date-hour");
        	tdhourel.appendChild(this.hourel);
			//小时
			for(var i=0;i<24;i++){
    			var houroption = document.createElement("OPTION");
    			houroption.value=i;
    			houroption.text=i;
    			this.hourel.options.add(houroption);
    		}
			//将当前小时为选择
    		this.hourel.options[this.value.getHours()].selected=true;

        	var tdminuteel = this.el.child("td.x-date-minute");
        	tdminuteel.appendChild(this.minuteel);
			//分钟
        	for(var i=0;i<60;i++){
        		var minuteOption = document.createElement("OPTION");
        		minuteOption.value=i;
        		minuteOption.text=i;
        		this.minuteel.options.add(minuteOption);
        	}
		//将当前分设为选择
        this.minuteel.options[this.value.getMinutes()].selected=true;
        
        var tdsecondel = this.el.child("td.x-date-second");
        	tdsecondel.appendChild(this.secondel);
        	for(var i=0;i<60;i++){
        		var secondOption = document.createElement("OPTION");
        		secondOption.value=i;
        		secondOption.text=i;
        		this.secondel.options.add(secondOption);
        	}
        this.secondel.options[this.value.getSeconds()].selected=true;
//add end

        this.mbtn = new Ext.Button({
            text: "&#160;",
            tooltip: this.monthYearText,
            renderTo: this.el.child("td.x-date-middle", true)
        });

        this.mbtn.on('click', this.showMonthPicker, this);
        this.mbtn.el.child(this.mbtn.menuClassTarget).addClass("x-btn-with-menu");


        var today = (new Date()).dateFormat(this.format);
        var todayBtn = new Ext.Button({
            renderTo: this.el.child("td.x-date-bottom", true),
            text: String.format(this.todayText, today),
            tooltip: String.format(this.todayTip, today),
            handler: this.selectToday,
            scope: this
        });

        if(Ext.isIE){
            this.el.repaint();
        }
        this.update(this.value);
    },

    createMonthPicker : function(){
        if(!this.monthPicker.dom.firstChild){
            var buf = ['<table border="0" cellspacing="0">'];
            for(var i = 0; i < 6; i++){
                buf.push(
                    '<tr><td class="x-date-mp-month"><a href="#">', this.monthNames[i].substr(0, 3), '</a></td>',
                    '<td class="x-date-mp-month x-date-mp-sep"><a href="#">', this.monthNames[i+6].substr(0, 3), '</a></td>',
                    i == 0 ?
                    '<td class="x-date-mp-ybtn" align="center"><a class="x-date-mp-prev"></a></td><td class="x-date-mp-ybtn" align="center"><a class="x-date-mp-next"></a></td></tr>' :
                    '<td class="x-date-mp-year"><a href="#"></a></td><td class="x-date-mp-year"><a href="#"></a></td></tr>'
                );
            }
            buf.push(
                '<tr class="x-date-mp-btns"><td colspan="4"><button type="button" class="x-date-mp-ok">',
                    this.okText,
                    '</button><button type="button" class="x-date-mp-cancel">',
                    this.cancelText,
                    '</button></td></tr>',
                '</table>'
            );
            this.monthPicker.update(buf.join(''));
            this.monthPicker.on('click', this.onMonthClick, this);
            this.monthPicker.on('dblclick', this.onMonthDblClick, this);

            this.mpMonths = this.monthPicker.select('td.x-date-mp-month');
            this.mpYears = this.monthPicker.select('td.x-date-mp-year');

            this.mpMonths.each(function(m, a, i){
                i += 1;
                if((i%2) == 0){
                    m.dom.xmonth = 5 + Math.round(i * .5);
                }else{
                    m.dom.xmonth = Math.round((i-1) * .5);
                }
            });
        }
    },

    showMonthPicker : function(){
        this.createMonthPicker();
        var size = this.el.getSize();
        this.monthPicker.setSize(size);
        this.monthPicker.child('table').setSize(size);

        this.mpSelMonth = (this.activeDate || this.value).getMonth();
        this.updateMPMonth(this.mpSelMonth);
        this.mpSelYear = (this.activeDate || this.value).getFullYear();
        this.updateMPYear(this.mpSelYear);

        this.monthPicker.slideIn('t', {duration:.2});
    },

    updateMPYear : function(y){
        this.mpyear = y;
        var ys = this.mpYears.elements;
        for(var i = 1; i <= 10; i++){
            var td = ys[i-1], y2;
            if((i%2) == 0){
                y2 = y + Math.round(i * .5);
                td.firstChild.innerHTML = y2;
                td.xyear = y2;
            }else{
                y2 = y - (5-Math.round(i * .5));
                td.firstChild.innerHTML = y2;
                td.xyear = y2;
            }
            this.mpYears.item(i-1)[y2 == this.mpSelYear ? 'addClass' : 'removeClass']('x-date-mp-sel');
        }
    },

    updateMPMonth : function(sm){
        this.mpMonths.each(function(m, a, i){
            m[m.dom.xmonth == sm ? 'addClass' : 'removeClass']('x-date-mp-sel');
        });
    },

    selectMPMonth: function(m){

    },

    onMonthClick : function(e, t){
        e.stopEvent();
        var el = new Ext.Element(t), pn;
        if(el.is('button.x-date-mp-cancel')){
            this.hideMonthPicker();
        }
        else if(el.is('button.x-date-mp-ok')){
            this.update(new Date(this.mpSelYear, this.mpSelMonth, (this.activeDate || this.value).getDate()));
            this.hideMonthPicker();
        }
        else if(pn = el.up('td.x-date-mp-month', 2)){
            this.mpMonths.removeClass('x-date-mp-sel');
            pn.addClass('x-date-mp-sel');
            this.mpSelMonth = pn.dom.xmonth;
        }
        else if(pn = el.up('td.x-date-mp-year', 2)){
            this.mpYears.removeClass('x-date-mp-sel');
            pn.addClass('x-date-mp-sel');
            this.mpSelYear = pn.dom.xyear;
        }
        else if(el.is('a.x-date-mp-prev')){
            this.updateMPYear(this.mpyear-10);
        }
        else if(el.is('a.x-date-mp-next')){
            this.updateMPYear(this.mpyear+10);
        }
    },

    onMonthDblClick : function(e, t){
        e.stopEvent();
        var el = new Ext.Element(t), pn;
        if(pn = el.up('td.x-date-mp-month', 2)){
            this.update(new Date(this.mpSelYear, pn.dom.xmonth, (this.activeDate || this.value).getDate()));
            this.hideMonthPicker();
        }
        else if(pn = el.up('td.x-date-mp-year', 2)){
            this.update(new Date(pn.dom.xyear, this.mpSelMonth, (this.activeDate || this.value).getDate()));
            this.hideMonthPicker();
        }
    },

    hideMonthPicker : function(disableAnim){
        if(this.monthPicker){
            if(disableAnim === true){
                this.monthPicker.hide();
            }else{
                this.monthPicker.slideOut('t', {duration:.2});
            }
        }
    },


    showPrevMonth : function(e){
        this.update(this.activeDate.add("mo", -1));
    },


    showNextMonth : function(e){
        this.update(this.activeDate.add("mo", 1));
    },


    showPrevYear : function(){
        this.update(this.activeDate.add("y", -1));
    },


    showNextYear : function(){
        this.update(this.activeDate.add("y", 1));
    },


    handleMouseWheel : function(e){
        var delta = e.getWheelDelta();
        if(delta > 0){
            this.showPrevMonth();
            e.stopEvent();
        } else if(delta < 0){
            this.showNextMonth();
            e.stopEvent();
        }
    },


    handleDateClick : function(e, t){
        e.stopEvent();
        if(t.dateValue && !Ext.fly(t.parentNode).hasClass("x-date-disabled")){
        	var dt = new Date(t.dateValue);
     	    dt.setHours(this.hourel.options[this.hourel.selectedIndex].value);
            dt.setMinutes(this.minuteel.options[this.minuteel.selectedIndex].value);
            dt.setSeconds(this.secondel.options[this.secondel.selectedIndex].value);
 	    	this.setValue(dt);
            //this.fireEvent("select", this, this.value);
        }
    },


    selectToday : function(){
        this.value.setHours(this.hourel.options[this.hourel.selectedIndex].value);
        this.value.setMinutes(this.minuteel.options[this.minuteel.selectedIndex].value);
        this.value.setSeconds(this.secondel.options[this.secondel.selectedIndex].value);
        this.fireEvent("select", this, this.value);
    },


    update : function(date){
        var vd = this.activeDate;
        this.activeDate = date;
        if(vd && this.el){
            var t = new Date(date).clearTime().getTime();
            if(vd.getMonth() == date.getMonth() && vd.getFullYear() == date.getFullYear()){
                this.cells.removeClass("x-date-selected");
                this.cells.each(function(c){
                   if(c.dom.firstChild.dateValue == t){
                       c.addClass("x-date-selected");
                       setTimeout(function(){
                            try{c.dom.firstChild.focus();}catch(e){}
                       }, 50);
                       return false;
                   }
                });
                return;
            }
        }
        var days = date.getDaysInMonth();
        var firstOfMonth = date.getFirstDateOfMonth();
        var startingPos = firstOfMonth.getDay()-this.startDay;

        if(startingPos <= this.startDay){
            startingPos += 7;
        }

        var pm = date.add("mo", -1);
        var prevStart = pm.getDaysInMonth()-startingPos;

        var cells = this.cells.elements;
        var textEls = this.textNodes;
        days += startingPos;


        var day = 86400000;
        var d = (new Date(pm.getFullYear(), pm.getMonth(), prevStart)).clearTime();
        var today = new Date().getTime();
        var tToday = new Date().clearTime().getTime();
        var sel = date.getTime();
        var tSel = new Date(date).clearTime().getTime();
        var min = this.minDate ? this.minDate : Number.NEGATIVE_INFINITY;
        var max = this.maxDate ? this.maxDate : Number.POSITIVE_INFINITY;
        var ddMatch = this.disabledDatesRE;
        var ddText = this.disabledDatesText;
        var ddays = this.disabledDays ? this.disabledDays.join("") : false;
        var ddaysText = this.disabledDaysText;
        var format = this.format;

        var setCellClass = function(cal, cell){
            cell.title = "";
            var t = d.getTime();
            cell.firstChild.dateValue = t;
            //alert(d+":"+t+" : "+tSel+" : "+tToday+":"+(t==tSel)+":"+(t==tToday));
            if(t == tToday){
                cell.className += " x-date-today";
                cell.title = cal.todayText;
            }
            if(t == tSel){
                cell.className += " x-date-selected";
                setTimeout(function(){
                    try{cell.firstChild.focus();}catch(e){}
                }, 50);
            }

            if(t < min) {
                cell.className = " x-date-disabled";
                cell.title = cal.minText;
                return;
            }
            if(t > max) {
                cell.className = " x-date-disabled";
                cell.title = cal.maxText;
                return;
            }
            if(ddays){
                if(ddays.indexOf(d.getDay()) != -1){
                    cell.title = ddaysText;
                    cell.className = " x-date-disabled";
                }
            }
            if(ddMatch && format){
                var fvalue = d.dateFormat(format);
                if(ddMatch.test(fvalue)){
                    cell.title = ddText.replace("%0", fvalue);
                    cell.className = " x-date-disabled";
                }
            }
        };

        var i = 0;
        for(; i < startingPos; i++) {
            textEls[i].innerHTML = (++prevStart);
            d.setDate(d.getDate()+1);
            cells[i].className = "x-date-prevday";
            setCellClass(this, cells[i]);
        }
        for(; i < days; i++){
            intDay = i - startingPos + 1;
            textEls[i].innerHTML = (intDay);
            d.setDate(d.getDate()+1);
            cells[i].className = "x-date-active";
            setCellClass(this, cells[i]);
        }
        var extraDays = 0;
        for(; i < 42; i++) {
             textEls[i].innerHTML = (++extraDays);
             d.setDate(d.getDate()+1);
             cells[i].className = "x-date-nextday";
             setCellClass(this, cells[i]);
        }

        this.mbtn.setText(this.monthNames[date.getMonth()] + " " + date.getFullYear());
		
		/*
        if(!this.internalRender){
            var main = this.el.dom.firstChild;
            var w = main.offsetWidth;
            this.el.setWidth(w + this.el.getBorderWidth("lr"));
            Ext.fly(main).setWidth(w);
            this.internalRender = true;



            if(Ext.isOpera && !this.secondPass){
                main.rows[0].cells[1].style.width = (w - (main.rows[0].cells[0].offsetWidth+main.rows[0].cells[2].offsetWidth)) + "px";
                this.secondPass = true;
                this.update.defer(10, this, [date]);
            }
        }
		*/
    }
});
Ext.reg('datetimepicker', Ext.DateTimePicker);
