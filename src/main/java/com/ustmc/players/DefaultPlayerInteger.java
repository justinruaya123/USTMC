package com.ustmc.players;

public enum DefaultPlayerInteger {

	REPUTATION("reputation", 0);

	private final String index;
	private final int value;

	DefaultPlayerInteger(String configIndex, int defaultValue) {
		this.index = configIndex;
		this.value = defaultValue;
	}

	public String getConfigIndex() {
		return index;
	}

	public int getValue() {
		return value;
	}

}
