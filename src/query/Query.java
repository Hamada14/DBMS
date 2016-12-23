package query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Query {

	private static final Logger logger = LogManager.getLogger(Query.class);

	protected boolean correctSyntax;
	protected boolean operationDone;
	protected String LogMessage;
	protected long startTime, endTime;

	public Query(boolean correctSyntax, String LogMessage) {
		this.correctSyntax = correctSyntax;
		this.LogMessage = LogMessage;
		logger.info(LogMessage);
		startTime = System.nanoTime();
		if (!correctSyntax) {
			setOperationDone();
		}
	}

	public boolean checkOperationFail() {
		return (!correctSyntax) || (!operationDone);
	}

	public boolean checkSyntaxFail() {
		return (!correctSyntax);
	}

	public void setLogMessage(String LogMessage) {
		this.LogMessage = LogMessage;
		logger.info(LogMessage);
	}

	public String getLogMessage() {
		return LogMessage;
	}

	public void setOperationDone() {
		if (!operationDone) {
			operationDone = true;
			endTime = System.nanoTime();
		}
	}

	public long getOperationTime() throws InvalidOperation {
		if (!operationDone) {
			throw new InvalidOperation();
		}
		return endTime - startTime;
	}
}
