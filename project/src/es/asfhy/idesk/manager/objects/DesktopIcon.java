package es.asfhy.idesk.manager.objects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import es.asfhy.idesk.manager.ManagerMain;
import es.asfhy.idesk.manager.tables.Icon;

public class DesktopIcon {
	public static DesktopIcon create(String fileName) {
		return new DesktopIcon(fileName, false);
	}
	
	private String	fileName;
	private Icon	iconTable	= new Icon();
	
	private DesktopIcon(String fileName, boolean check) {
		this.fileName = fileName;
		iconTable.setCommands(new String[2]);
		iconTable.getCommands()[0] = "";
		iconTable.getCommands()[1] = "idesktool " + fileName;
	}
	
	public DesktopIcon(String fileName) throws FileNotFoundException {
		this.fileName = fileName;
		File aux = new File(ManagerMain.ideskFolder, fileName);
		if (aux.exists())
			addProperties(new Scanner(aux));
	}
	
	public synchronized String getFileName() {
		return fileName;
	}
	
	public synchronized void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public void addProperties(Scanner scan) {
		iconTable.addProperties(scan);
	}
	
	public String getCaption() {
		return iconTable.getCaption();
	}
	
	public int hashCode() {
		return iconTable.hashCode();
	}
	
	public String[] getCommands() {
		return iconTable.getCommands();
	}
	
	public Integer getHeight() {
		return iconTable.getHeight();
	}
	
	public String getIcon() {
		return iconTable.getIcon();
	}
	
	public String getTableString() {
		return iconTable.getTableString();
	}
	
	public String getTooltipCaption() {
		return iconTable.getTooltipCaption();
	}
	
	public Integer getWidth() {
		return iconTable.getWidth();
	}
	
	public Integer getX() {
		return iconTable.getX();
	}
	
	public Integer getY() {
		return iconTable.getY();
	}
	
	public void setCaption(String caption) {
		iconTable.setCaption(caption);
	}
	
	public void setCommands(String[] commands) {
		iconTable.setCommands(commands);
	}
	
	public void setHeight(Integer height) {
		iconTable.setHeight(height);
	}
	
	public void setIcon(String icon) {
		iconTable.setIcon(icon);
	}
	
	public void setTooltipCaption(String tooltipCaption) {
		iconTable.setTooltipCaption(tooltipCaption);
	}
	
	public void setWidth(Integer width) {
		iconTable.setWidth(width);
	}
	
	public void setX(Integer x) {
		iconTable.setX(x);
	}
	
	public void setY(Integer y) {
		iconTable.setY(y);
	}
	
	public boolean equals(Object obj) {
		return iconTable.equals(obj);
	}
	
	public String toString() {
		return iconTable.toString();
	}
	
	public void save() throws IOException {
		FileWriter writer = new FileWriter(new File(ManagerMain.ideskFolder, fileName));
		writer.write(getTableString());
		writer.flush();
		writer.close();
	}
	
	public boolean delete() throws IOException {
		File f = new File(ManagerMain.ideskFolder, fileName);
		boolean out = false;
		if (f.exists())
			out = f.delete();
		else
			out = true;
		return out;
	}
}
