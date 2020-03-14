package test;

import ctd.mvc.controller.util.MVCSessionListener;
import ctd.util.annotation.RpcService;
import ctd.util.message.MessageCenter;

public class TestPub {
	
	@RpcService
	public void onSubscribeMessage(Object message){
		System.out.println("get message:" + message);
	}
	
	public void pub(){
//		MessageCenter.pub("platform.guys", "Hey, Judy.");
		System.out.println(MVCSessionListener.getUsers());
	}

}
