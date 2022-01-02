package com.monitoring.fmonitoringsys.to;

import java.time.LocalDateTime;

/**
 * POJO per mappare le informazioni del file letto
 * 
 * @author amanganiello90
 */
public class InfoFileTO {

	private String path;
	private String name;
	private long byteSize;
	private LocalDateTime lastModify;
	private String hashMd5;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getByteSize() {
		return byteSize;
	}

	public void setByteSize(long byteSize) {
		this.byteSize = byteSize;
	}

	public LocalDateTime getLastModify() {
		return lastModify;
	}

	public void setLastModify(LocalDateTime lastModify) {
		this.lastModify = lastModify;
	}

	public String getHashMd5() {
		return hashMd5;
	}

	public void setHashMd5(String hashMd5) {
		this.hashMd5 = hashMd5;
	}

	@Override
	public String toString() {
		return "InfoFileTO [path=" + path + ", name=" + name + ", byteSize=" + byteSize + ", lastModify=" + lastModify
				+ ", hasMd5=" + hashMd5 + "]";
	}

}
