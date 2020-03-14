package test;

import ctd.util.annotation.RpcService;
import ctd.util.message.MessageCenter;

public class TestSub {
	
	@RpcService
	public void onSubscribeMessage(Object message){
		System.out.println("get message:" + message);
	}
	
	public void sub(){
		MessageCenter.subscribe("platform.guys", this);
	}
	
	public void say(){
//		System.out.println(AppDomainContext.getRpcServerWorkUrl());
//		System.out.println(AppDomainContext.getRpcServerHost());
		MessageCenter.pub("platform.guys", "aaa");
	}

}
