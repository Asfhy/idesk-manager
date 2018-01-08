package es.asfhy.idesk.manager.utils;

import java.util.Arrays;
import java.util.List;

import es.asfhy.idesk.manager.enums.MouseButton;
import es.asfhy.idesk.manager.enums.MouseEvent;

public class EventConfig {
	private boolean shift = false;
	private boolean ctrl = false;
	private boolean alt = false;
	private MouseButton button = null;
	private MouseEvent event = null;

	public EventConfig(boolean s, boolean c, boolean a, MouseButton btn, MouseEvent evt) {
		shift = s;
		ctrl = c;
		alt = a;
		button = btn;
		event = evt;
	}

	public EventConfig(String propValue) {
		List<String> parts = Arrays.asList(propValue.split(" "));
		shift = parts.contains("shift");
		ctrl = parts.contains("ctrl");
		alt = parts.contains("alt");
		for (int x = 0; button == null && x < MouseButton.values().length; x++)
			if (parts.contains(MouseButton.values()[x].toString()))
				button = MouseButton.values()[x];
		for (int x = 0; event == null && x < MouseEvent.values().length; x++)
			if (parts.contains(MouseEvent.values()[x].toString()))
				event = MouseEvent.values()[x];
	}

	@Override
	public synchronized String toString() {
		StringBuilder out = new StringBuilder();
		if (shift)
			out.append("shift ");
		if (ctrl)
			out.append("ctrl ");
		if (alt)
			out.append("alt ");
		out.append(button).append(" ").append(event);
		return out.toString();
	}

	public synchronized boolean isShift() {
		return shift;
	}

	public synchronized void setShift(boolean shift) {
		this.shift = shift;
	}

	public synchronized boolean isCtrl() {
		return ctrl;
	}

	public synchronized void setCtrl(boolean ctrl) {
		this.ctrl = ctrl;
	}

	public synchronized boolean isAlt() {
		return alt;
	}

	public synchronized void setAlt(boolean alt) {
		this.alt = alt;
	}

	public synchronized MouseButton getButton() {
		return button;
	}

	public synchronized void setButton(MouseButton button) {
		this.button = button;
	}

	public synchronized MouseEvent getEvent() {
		return event;
	}

	public synchronized void setEvent(MouseEvent event) {
		this.event = event;
	}
}
