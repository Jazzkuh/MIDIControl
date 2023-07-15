package com.jazzkuh.midicontroller.common.utils.lighting.bulb;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class Bulb {
	private final String name;
	private final String ip;
	private final String[] groups;

	Bulb(String name, String ip, String... groups) {
		this.name = name;
		this.ip = ip;
		this.groups = groups;
	}
}
