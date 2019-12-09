package ch.fhnw.edu.cpib.errors;

import ch.fhnw.edu.cpib.scanner.Ident;

public class CannotAssignToConstError extends Exception{
	private static final long serialVersionUID = 1L;
	
	public CannotAssignToConstError () {
		
	}
	
	public CannotAssignToConstError(String errorMessage) {
		super(errorMessage);
	}
	
	public CannotAssignToConstError(Ident ident) {
		super(setupMessage(ident));
	}
	
	public CannotAssignToConstError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public CannotAssignToConstError(Ident ident, Throwable cause) {
		super(setupMessage(ident), cause);
	}

	public CannotAssignToConstError(Throwable cause) {
		super(cause);
	} 
	
	private static String setupMessage(Ident ident) {
		return "Cannot assign value to already initialized const [" + ident.getIdent() + "]!";
	}
}
