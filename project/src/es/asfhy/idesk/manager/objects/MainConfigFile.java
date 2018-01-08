package es.asfhy.idesk.manager.objects;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import es.asfhy.idesk.manager.ManagerMain;
import es.asfhy.idesk.manager.tables.Actions;
import es.asfhy.idesk.manager.tables.Config;

public class MainConfigFile {
	private Config configTable = new Config();
	private Actions actionsTable = new Actions();

	public MainConfigFile() throws FileNotFoundException {
		if (ManagerMain.ideskrc.exists() && ManagerMain.ideskrc.canRead()) {
			Scanner scan = new Scanner(ManagerMain.ideskrc);
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				if (line == null)
					continue;
				if (line.trim().startsWith("table") && line.trim().endsWith("Config")) {
					configTable.addProperties(scan);
				} else if (line.trim().startsWith("table") && line.trim().endsWith("Actions")) {
					actionsTable.addProperties(scan);
				}
			}
		}
	}

	@Override
	public synchronized String toString() {
		StringBuilder out = new StringBuilder();
		out.append(configTable.getTableString()).append("\n");
		out.append(actionsTable.getTableString()).append("\n");
		return out.toString();
	}

	public synchronized Config getConfigTable() {
		return configTable;
	}

	public synchronized void setConfigTable(Config configTable) {
		this.configTable = configTable;
	}

	public synchronized Actions getActionsTable() {
		return actionsTable;
	}

	public synchronized void setActionsTable(Actions actionsTable) {
		this.actionsTable = actionsTable;
	}

	public void save() throws IOException {
		FileWriter writer = new FileWriter(ManagerMain.ideskrc);
		writer.write(configTable.getTableString());
		writer.write("\n");
		writer.write(actionsTable.getTableString());
		writer.write("\n");
		writer.flush();
		writer.close();
	}
}
