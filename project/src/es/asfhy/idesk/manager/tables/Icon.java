package es.asfhy.idesk.manager.tables;

import java.util.Arrays;
import java.util.Scanner;

public class Icon {
	public static int _int(String in) {
		return Integer.parseInt(in);
	}

	private String caption = null;
	private String tooltipCaption = null;
	private String icon = null;
	private Integer width = null;
	private Integer height = null;
	private Integer x = null;
	private Integer y = null;

	private String commands[] = null;

	public synchronized void addProperties(Scanner scan) {
		String line = null;
		String parts[] = null;
		String cmds[] = new String[1024];
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
				if (propName.equalsIgnoreCase("Caption"))
					caption = propValue;
				else if (propName.equalsIgnoreCase("ToolTip.Caption"))
					tooltipCaption = propValue;
				else if (propName.equalsIgnoreCase("Icon"))
					icon = propValue;
				else if (propName.equalsIgnoreCase("Width"))
					width = _int(propValue);
				else if (propName.equalsIgnoreCase("Height"))
					height = _int(propValue);
				else if (propName.equalsIgnoreCase("X"))
					x = _int(propValue);
				else if (propName.equalsIgnoreCase("Y"))
					y = _int(propValue);
				else if (propName.equalsIgnoreCase("Command")) {
					maxCmd = 0;
					cmds[0] = propValue;
				} else if (propName.startsWith("Command[")) {
					int index = Integer.parseInt(propName.replaceAll("[\\D]", ""));
					maxCmd = Math.max(index, maxCmd);
					cmds[index] = propValue;
				}
			}
		} while (scan.hasNextLine() && !line.trim().startsWith("end"));
		if (maxCmd >= 0) {
			commands = new String[maxCmd + 1];
			for (int x = 0; x <= maxCmd; x++)
				commands[x] = cmds[x];
		} else {
			commands = null;
		}
	}

	public synchronized String getCaption() {
		return caption;
	}

	public synchronized String[] getCommands() {
		return commands;
	}

	public synchronized Integer getHeight() {
		return height;
	}

	public synchronized String getIcon() {
		return icon;
	}

	public synchronized String getTableString() {
		StringBuilder out = new StringBuilder("table Icon\n");
		out.append("\tCaption: ").append(caption).append("\n");
		if (tooltipCaption != null && tooltipCaption.trim().length() != 0)
			out.append("\tToolTip.Caption: ").append(tooltipCaption).append("\n");
		out.append("\tIcon: ").append(icon).append("\n");
		if (width != null)
			out.append("\tWidth: ").append(width).append("\n");
		if (height != null)
			out.append("\tHeight: ").append(height).append("\n");
		if (x != null)
			out.append("\tX: ").append(x).append("\n");
		if (y != null)
			out.append("\tY: ").append(y).append("\n");
		for (int x = 0; commands != null && x < commands.length; x++)
			out.append("\tCommand[").append(x).append("]: ").append(commands[x]).append("\n");
		return out.append("end").toString();
	}

	public synchronized String getTooltipCaption() {
		return tooltipCaption;
	}

	public synchronized Integer getWidth() {
		return width;
	}

	public synchronized Integer getX() {
		return x;
	}

	public synchronized Integer getY() {
		return y;
	}

	public synchronized void setCaption(String caption) {
		this.caption = caption;
	}

	public synchronized void setCommands(String[] commands) {
		this.commands = commands;
	}

	public synchronized void setHeight(Integer height) {
		this.height = height;
	}

	public synchronized void setIcon(String icon) {
		this.icon = icon;
	}

	public synchronized void setTooltipCaption(String tooltipCaption) {
		this.tooltipCaption = tooltipCaption;
	}

	public synchronized void setWidth(Integer width) {
		this.width = width;
	}

	public synchronized void setX(Integer x) {
		this.x = x;
	}

	public synchronized void setY(Integer y) {
		this.y = y;
	}
}
