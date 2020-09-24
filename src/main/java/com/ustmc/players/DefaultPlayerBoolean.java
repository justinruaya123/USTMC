package com.ustmc.players;

public enum DefaultPlayerBoolean {

	IS_VANISHED("vanished", false);

	private final String index;
	private final boolean value;

	DefaultPlayerBoolean(String configIndex, boolean defaultValue) {
		this.index = configIndex;
		this.value = defaultValue;
	}

	public String getConfigIndex() {
		return index;
	}

	public boolean getValue() {
		return value;
	}

}
