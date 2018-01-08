package es.asfhy.idesk.manager.tables;

import java.util.Arrays;
import java.util.Scanner;

import es.asfhy.idesk.manager.enums.MouseButton;
import es.asfhy.idesk.manager.enums.MouseEvent;
import es.asfhy.idesk.manager.utils.EventConfig;

public class Actions {
	private EventConfig lock = null;
	private EventConfig reload = null;
	private EventConfig drag = null;
	private EventConfig endDrag = null;
	private EventConfig execute[] = null;

	public Actions() {
		lock = new EventConfig(false, true, false, MouseButton.right, MouseEvent.doubleClk);
		reload = new EventConfig(false, false, false, MouseButton.middle, MouseEvent.doubleClk);
		drag = new EventConfig(false, false, false, MouseButton.left, MouseEvent.hold);
		endDrag = new EventConfig(false, false, false, MouseButton.left, MouseEvent.singleClk);
		execute = new EventConfig[2];
		execute[0] = new EventConfig(false, false, false, MouseButton.left, MouseEvent.doubleClk);
		execute[1] = new EventConfig(false, false, false, MouseButton.right, MouseEvent.doubleClk);
	}

	public String getTableString() {
		StringBuilder out = new StringBuilder("table Actions\n");
		if (lock != null)
			out.append("\tLock: ").append(lock).append("\n");
		if (reload != null)
			out.append("\tReload: ").append(reload).append("\n");
		if (drag != null)
			out.append("\tDrag: ").append(drag).append("\n");
		if (endDrag != null)
			out.append("\tEndDrag: ").append(endDrag).append("\n");
		for (int x = 0; execute != null && x < execute.length; x++)
			out.append("\tExecute[").append(x).append("]: ").append(execute[x]).append("\n");
		return out.append("end").toString();
	}

	public synchronized EventConfig getLock() {
		return lock;
	}

	public synchronized void setLock(EventConfig lock) {
		this.lock = lock;
	}

	public synchronized EventConfig getReload() {
		return reload;
	}

	public synchronized void setReload(EventConfig reload) {
		this.reload = reload;
	}

	public synchronized EventConfig getDrag() {
		return drag;
	}

	public synchronized void setDrag(EventConfig drag) {
		this.drag = drag;
	}

	public synchronized EventConfig getEndDrag() {
		return endDrag;
	}

	public synchronized void setEndDrag(EventConfig endDrag) {
		this.endDrag = endDrag;
	}

	public synchronized EventConfig[] getExecute() {
		return execute;
	}

	public synchronized void setExecute(EventConfig[] execute) {
		this.execute = execute;
	}

	public synchronized void addProperties(Scanner scan) {
		String line = null;
		String parts[] = null;
		EventConfig cmds[] = new EventConfig[1024];
		Arrays.fill(cmds, null);
		int maxCmd = -1;
		do {
			line = scan.nextLine();
			if (line == null)
				continue;
			line = line.trim();
			parts = line.split(":", 2);
			if (parts.length == 2 && parts[1] != null && parts[1].trim().length() != 0) {
				String propName = parts[0].trim();
				String propValue = parts[1].trim();
				if (propName.equalsIgnoreCase("Lock"))
					lock = new EventConfig(propValue);
				else if (propName.equalsIgnoreCase("Reload"))
					reload = new EventConfig(propValue);
				else if (propName.equalsIgnoreCase("Drag"))
					drag = new EventConfig(propValue);
				else if (propName.equalsIgnoreCase("EndDrag"))
					endDrag = new EventConfig(propValue);
				else if (propName.equalsIgnoreCase("Execute")) {
					maxCmd = 0;
					cmds[0] = new EventConfig(propValue);
				} else if (propName.startsWith("Execute[")) {
					int index = Integer.parseInt(propName.replaceAll("[\\D]", ""));
					maxCmd = Math.max(index, maxCmd);
					cmds[index] = new EventConfig(propValue);
				}
			}
		} while (scan.hasNextLine() && !line.trim().startsWith("end"));
		if (maxCmd >= 0) {
			execute = new EventConfig[maxCmd + 1];
			for (int x = 0; x <= maxCmd; x++)
				execute[x] = cmds[x];
		}
		cmds = null;
	}
}
