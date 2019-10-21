package com.example.efahrtenbuchapp.helper;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class ErrorHelper {
	public static void addErrorMessage(String text) {
		FacesMessage message = new FacesMessage("Error: " + text);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}	
	public static void addErrorMsgForCompontent(UIComponent component, String text) {
		FacesMessage message = new FacesMessage(text);
		FacesContext.getCurrentInstance().addMessage(component.getClientId(), message);
	}
}
