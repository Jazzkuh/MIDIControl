package com.jazzkuh.midicontroller.common.triggers.abstraction;

import lombok.Getter;

public record MidiResult(@Getter byte noteValue, @Getter byte pressedValue) {
}
