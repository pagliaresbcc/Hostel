package  br.com.hostel.model.exceptions;

public class RoomNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private int number;
	
	public RoomNotFoundException(String message, int number) {
		super(message);
		this.number = number;
	}
	
	public int getNumber( ) {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}

}
