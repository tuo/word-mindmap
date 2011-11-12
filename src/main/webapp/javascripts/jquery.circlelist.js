/*
 * circlelist jQuery Plugin v1.0.0
 *
 * Copyright (c) 2011 kissrobber
 * Licensed under the MIT license.
 */
jQuery(function(){
	var is_string = function(v){
		return typeof v === 'string';
	};
	var to_array = function(v){
		if(v instanceof Array){
			return v;
		} else if(v){
			return [v];
		} else {
			return [];
		}
	};
	
	var DATA = "circlelist";
    jQuery.circlelist = function(me, options){
    	var me = jQuery(me), tmp;
    	if(typeof options === "string"){
			var args = Array.prototype.slice.call(arguments, 2);
			if(publics[options]){
			} else {
				throw "method missing. circlelist#" + options;
			}
			var ret = publics[options].apply(me, args);

			if(/^get_/.test(options)){
				return ret;
			} else {
	    		return me;
			}
    	}
    	options = jQuery.extend({
				values:[],
				translate:function(v){return v},
				item_width:null,
				speed:1000,
				reverse:false,
				start: 0,
				angle: false,
				angle_offset: 0
			}, options);
		options.elms = {};
		options.values_in_circle = [];
		
		if(options.values_in_circle.length == 0){
			var tmp = me.get(0).tagName.toUpperCase();
			if(tmp == 'UL' || tmp == 'OL'){
				jQuery('li', me).each(function(i, v){
					options.values.push(jQuery(v).hide().css('list-style', 'none').get(0));
				});
			}
		}
		if(options.item_width == null){
			options.item_width = me.width() / 5;
		}
		options.size = parseInt(me.width() / options.item_width * 2.5, 10);
		options.r = me.width() / 2 * 1;
		options.values_in_circle = [];
		jQuery.data(me.get(0), DATA, options);
		if(options.values.length > 0){
			options.values_in_circle = options.values.length > options.size ? 
				options.values.slice(options.values.length - options.size) : Array.prototype.slice.call(options.values, 0);
			if(options.values_in_circle.length > 0){
				jQuery.each(options.values_in_circle, function(i, v){
					append.call(me, v);
				});
			}
		}

		animate_to_start_position.call(me, options.values_in_circle);
		return this;
   };
    
    var to_elm = function(v){
    	if(is_string(v)){
    		var vars = jQuery.data(this.get(0), DATA);
    		return vars.elms[v];
    	} else {
    		return v;
    	}
    };
    
    var append = function(v){
    	var vars = jQuery.data(this.get(0), DATA);
		if(is_string(v)){
			tmp = vars.translate(v);
			vars.elms[v] = tmp;
			v = tmp;
		}
		jQuery(v).css('position', 'absolute').show();
		this.append(v);
    };
    
	var delete_elm = function(v){
		var vars = jQuery.data(this.get(0), DATA);
		if(is_string(v)){
			if(vars.elms[v]){
				vars.elms[v].remove();
				delete vars.elms[v];
			}
		} else {
			jQuery(v).hide();
		}
	};
				
	var animate_step = function(params) {
		var me = this;
		for(var i in params) {
			this[i] = params[i]
		}
		this.dir = this.dir || 1;
		while(this.start > this.end && this.dir > 0) {
			this.start -= 360
		}
		while(this.start < this.end && this.dir < 0) {
			this.start += 360
		}
		return function(now, fx){
			var css = me.calc_css(1 - fx.pos);
			$(fx.elem).css(css);
		};
	};
	animate_step.prototype.calc_css = function(p) {
		var a = this.start * (p) + this.end * (1 - (p));
		a = a * Math.PI / 180;
		var x = parseInt(Math.sin(a) * this.radius + this.center[0], 10);
		var y = parseInt(Math.cos(a) * this.radius + this.center[1], 10);
		var ret = {
			'top' : y + "px",
			'left' : x + "px"
		};
		/*
		if(this.angle){
			var deg = "rotate(" + (-1 * a * 180 / Math.PI + this.angle_offset + 180) + "deg)";
			ret["WebkitTransform"] = deg;
			ret["MozTransform"] = deg;
			ret["OTransform"] = deg;
			ret["msTransform"] = deg;
		}
		*/
		return ret;
	};
    var animate_to_start_position = function(values){
		var me = this;
		var vars = jQuery.data(this.get(0), DATA);
		var width = me.width();
		jQuery.each(values, function(i, v) {
			var v = values[values.length - i -1];
			rotate_one.call(me, v, 0, i, 1);
		});
	};
	
	var rotate = function(s){
		var me = this;
		var vars = jQuery.data(this.get(0), DATA);
		var step = s || 1;
		var width = me.width();
		jQuery.each(vars.values_in_circle, function(i, v) {
			v = vars.values_in_circle[vars.values_in_circle.length - i - 1];
			rotate_one.call(me, v, i, step, 1);
		});
	};
	
	var rotate_one = function(value, i, step, d){
		var me = this;
		var vars = jQuery.data(this.get(0), DATA);
		
		var elm = jQuery(to_elm.call(me, value));
		var width = me.width();
		var cb = null;
		var direction = vars.reverse ? -1 : 1;
		d = d * direction;
		if(i >= vars.size - step){
			cb = function(){
				delete_elm.call(me, value);
			};
		}
		var zindex = elm.css('z-index');
		if(zindex == "auto"){
			zindex = 0;
		}
		elm.animate({'z-index':zindex},
			{
				duration: vars.speed,
				complete: cb,
				step : new animate_step({
					center : [width / 2 - vars.item_width / 2, width / 2 - vars.item_width / 2],
					radius : vars.r,
					start : direction* ((360 / vars.size * i)) + vars.start,
					end : direction* ((360 / vars.size * Math.min(i + step, vars.size))) + vars.start,
					dir : d,
					angle : vars.angle,
					angle_offset : vars.angle_offset
				})
			}
		);
	};
	
	//public methods
    var publics = {
    	add:function(value){
    		var me = this;
	    	var vars = jQuery.data(this.get(0), DATA);
			var values_t = to_array(value);
			var values = [];
			var cnt = 0;
			var add_cnt, tmp, tmps;
			
			jQuery.each(values_t, function(i, v){
				if(jQuery.inArray(v, vars.values) == -1){
					values.push(v);
					vars.values.push(v);
				}
			});
			if(values.length == 0){
				return;
			}
			
			rotate.call(me, values.length);
			
			while(vars.values_in_circle.length > 0 && ((vars.values_in_circle.length + values.length) > vars.size)){
				cnt++;
				vars.values_in_circle.shift();
			}
			add_cnt = Math.min(values.length, vars.size);
			values = vars.values.slice(vars.values.length - add_cnt);
			
			jQuery.each(values, function(i, v){
				vars.values_in_circle.push(v);
				append.call(me, v);
			});
			animate_to_start_position.call(me, values);
		},
		remove:function(datas){
			
    		var me = this;
	    	var vars = jQuery.data(this.get(0), DATA);
			var i, j, tmp, cnt, data, length;
			datas = to_array(datas);

			for(i = 0; i < datas.length; i++){
				tmp = jQuery.inArray(datas[i], vars.values);
				if(tmp >= 0){
					vars.values.splice(tmp, 1);
				} else {
					throw "error. no data found.";
				}
			}

			//animation
			cnt = 0;
			length = vars.values_in_circle.length;
			j = 0;
			var deletes = [];
			
			for(i = 0; i < length; i++){
				data = vars.values_in_circle[vars.values_in_circle.length - i -1];
				if(jQuery.inArray(data, datas) == -1){
					if(cnt > 0){
						rotate_one.call(me, data, i, -cnt, -1);
					}
				} else {
					cnt++;
					deletes.push(data);
				}
			}
			
			jQuery.each(deletes, function(i, v){
				vars.values_in_circle.splice(jQuery.inArray(v, vars.values_in_circle), 1);
				delete_elm.call(me, v)
			});
			var add_cnt = Math.min(vars.size - vars.values_in_circle.length, vars.values.length - vars.values_in_circle.length);
			datas = vars.values.slice(vars.values.length - vars.values_in_circle.length - add_cnt, vars.values.length - vars.values_in_circle.length);
			if(vars.values_in_circle.length < vars.size && vars.values_in_circle.length < vars.values.length){
				jQuery.each(datas, function(i, v){
					v = datas[datas.length - i - 1];
					if(jQuery.inArray(v, vars.values_in_circle) == -1){
						vars.values_in_circle.unshift(v);
						append.call(me, v);
						rotate_one.call(me, v, vars.size - 1, vars.values_in_circle.length - vars.size, -1);
						return (vars.values_in_circle.length < vars.size);
					}
				});
			}
		},
		get_displayed:function(){
			var vars = jQuery.data(this.get(0), DATA);
			return vars.values_in_circle;
		},
		get:function(){
			var vars = jQuery.data(this.get(0), DATA);
			return vars.values;
		}
    };
    
    jQuery.fn.circlelist = function() {
    	var tmp = Array.prototype.slice.call(arguments);
    	if(typeof tmp[0] === 'string' && /^get_/.test(tmp[0])){
			var tmp2 = tmp.slice();
			tmp2.unshift(this);
    		return jQuery.circlelist.apply(this, tmp2);
    	} else {
			return this.each(function() {
				var tmp2 = tmp.slice();
				tmp2.unshift(this);
	           jQuery.circlelist.apply(this, tmp2);
	        });
    	}
    };
	
});