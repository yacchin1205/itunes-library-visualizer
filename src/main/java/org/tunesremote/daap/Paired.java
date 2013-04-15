package org.tunesremote.daap;

import java.net.InetAddress;

public class Paired {

	private InetAddress target;

	private String code;

	public Paired(InetAddress target, String code) {
		if (target == null || code == null) {
			throw new IllegalArgumentException();
		}
		this.target = target;
		this.code = code;
	}

	public InetAddress getTarget() {
		return target;
	}

	public String getCode() {
		return code;
	}

	@Override
	public String toString() {
		return "Paired [target=" + target + ", code=" + code + "]";
	}

}
