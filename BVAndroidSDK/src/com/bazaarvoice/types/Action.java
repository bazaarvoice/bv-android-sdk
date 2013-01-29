package com.bazaarvoice.types;

/**
 * 
 * An enum used for defining an action type for content submission.
 * 
 * <p>
 * Created on 7/9/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 * 
 */
public enum Action {
	/**
	 * Submit the content only for preview.
	 */
	PREVIEW("preview"),

	/**
	 * Submit the content fully to Bazaarvoice.
	 */
	SUBMIT("submit");
	
	private String actionString;
	
	Action(String actionString){
		this.actionString = actionString;
	}
	public String getActionName() {
		return this.actionString	;
	}
}
