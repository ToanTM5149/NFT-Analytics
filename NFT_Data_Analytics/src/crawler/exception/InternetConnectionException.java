package crawler.exception;

@SuppressWarnings("serial")
public class InternetConnectionException extends Exception{

	public InternetConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public InternetConnectionException(String message) {
		super(message);
	}
}
