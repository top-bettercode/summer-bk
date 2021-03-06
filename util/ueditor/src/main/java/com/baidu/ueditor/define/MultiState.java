package com.baidu.ueditor.define;

import com.baidu.ueditor.Encoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 多状态集合状态
 * 其包含了多个状态的集合, 其本身自己也是一个状态
 * @author hancong03@baidu.com
 *
 */
public class MultiState implements State {

	private final boolean state;
	private String info = null;
	private final Map<String, Long> intMap = new HashMap<>();
	private final Map<String, String> infoMap = new HashMap<>();
	private final List<String> stateList = new ArrayList<>();
	
	public MultiState ( boolean state ) {
		this.state = state;
	}
	
	public MultiState ( boolean state, String info ) {
		this.state = state;
		this.info = info;
	}
	
	public MultiState ( boolean state, int infoKey ) {
		this.state = state;
		this.info = AppInfo.getStateInfo( infoKey );
	}
	
	@Override
	public boolean isSuccess() {
		return this.state;
	}
	
	public void addState ( State state ) {
		stateList.add( state.toJSONString() );
	}

	/**
	 * 该方法调用无效果
	 */
	@Override
	public void putInfo(String name, String val) {
		this.infoMap.put(name, val);
	}

	@Override
	public String toJSONString() {
		
		String stateVal = this.isSuccess() ? AppInfo.getStateInfo( AppInfo.SUCCESS ) : this.info;
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("{\"state\": \"").append(stateVal).append("\"");
		
		// 数字转换
		Iterator<String> iterator = this.intMap.keySet().iterator();
		
		while ( iterator.hasNext() ) {
			
			stateVal = iterator.next();
			
			builder.append(",\"").append(stateVal).append("\": ").append(this.intMap.get(stateVal));
			
		}
		
		iterator = this.infoMap.keySet().iterator();
		
		while ( iterator.hasNext() ) {
			
			stateVal = iterator.next();
			
			builder.append(",\"").append(stateVal).append("\": \"").append(this.infoMap.get(stateVal))
					.append("\"");
			
		}
		
		builder.append( ", list: [" );
		
		
		iterator = this.stateList.iterator();
		
		while ( iterator.hasNext() ) {
			
			builder.append(iterator.next()).append(",");
			
		}
		
		if ( this.stateList.size() > 0 ) {
			builder.deleteCharAt( builder.length() - 1 );
		}
		
		builder.append( " ]}" );

		return Encoder.toUnicode( builder.toString() );

	}

	@Override
	public void putInfo(String name, long val) {
		this.intMap.put( name, val );
	}

}
