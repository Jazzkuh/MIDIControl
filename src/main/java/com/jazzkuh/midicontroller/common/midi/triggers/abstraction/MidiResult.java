package com.jazzkuh.midicontroller.common.midi.triggers.abstraction;

import lombok.Getter;

public record MidiResult(@Getter byte noteValue, @Getter byte pressedValue) {
}
