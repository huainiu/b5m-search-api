package com.b5m.exception;

public class InvokeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7982553558401584289L;

	public InvokeException(){
		super();
	}
	
	public InvokeException(String message){
		super(message);
	}
	
	public InvokeException(String message, Throwable e){
		super(message, e);
	}
	
	public InvokeException(Throwable e){
		super(e);
	}
}
