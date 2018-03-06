package com.semioe.api.vo;

import java.io.Serializable;

public class DeviceItemVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8071029497048942150L;
	private String deviceItemId;
	private String deviceItemName;

	public String getDeviceItemId() {
		return deviceItemId;
	}

	public void setDeviceItemId(String deviceItemId) {
		this.deviceItemId = deviceItemId;
	}

	public String getDeviceItemName() {
		return deviceItemName;
	}

	public void setDeviceItemName(String deviceItemName) {
		this.deviceItemName = deviceItemName;
	}

}