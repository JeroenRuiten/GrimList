package io.github.ferusgrim.GrimList;

import net.gravitydevelopment.updater.Updater;
import net.gravitydevelopment.updater.Updater.ReleaseType;

public class UpdateManager {
	private static final int ID = 65828;
	public static boolean update = false;
	public static String name = "";
	public static ReleaseType type = null;
	public static String version = "";
	public static String link = "";

	public static void Start(GrimList grimList) {
		Updater updater = new Updater(grimList, ID, grimList.jarFile, Updater.UpdateType.NO_DOWNLOAD, false);
		update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
		name = updater.getLatestName();
		version = updater.getLatestGameVersion();
		type = updater.getLatestType();
		link = updater.getLatestFileLink();
	}
}
