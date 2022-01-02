package com.monitoring.fmonitoringsys.to;

import java.util.List;

/**
 * POJO per mappare il risultato dell'invocazione delle api rest
 * 
 * @author amanganiello90
 */
public class ResultTO {

	private String message;

	private List<InfoFileTO> files;

	public ResultTO() {
	}

	public ResultTO(String message, List<InfoFileTO> files) {
		this.message = message;
		this.files = files;

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<InfoFileTO> getFiles() {
		return files;
	}

	public void setFiles(List<InfoFileTO> files) {
		this.files = files;
	}

	@Override
	public String toString() {
		return "ResultTO [message=" + message + ", files=" + files + "]";
	}

}
