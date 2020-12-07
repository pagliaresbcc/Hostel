package br.com.hostel.model.exceptions;

 public class CustomerNotFoundException extends Exception{
	 
	private static final long serialVersionUID = 1L;
	
	private String name;
	 private String lastName;
		
	 public CustomerNotFoundException(String message, String name, String lastName) {
			super(message);
			this.name = name;
			this.lastName = lastName;
	 }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
