package crawler.exception;

@SuppressWarnings("serial")
public class CrawlTimeoutException extends Exception{

	public CrawlTimeoutException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public CrawlTimeoutException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
