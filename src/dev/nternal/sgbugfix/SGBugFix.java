package dev.nternal.sgbugfix;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import me.wazup.survivalgames.SurvivalGames;

public class SGBugFix extends JavaPlugin implements Listener {

	// If you're wondering,
	//
	// "Wouldn't making SurvivalGames.getInstance().grace a java.util.Set
	// fix this problem entirely?"
	//
	// then you're correct, it absolutely would. :D

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);

		Plugin sg_ = getServer().getPluginManager().getPlugin("SurvivalGames");
		if (sg_ != null) {
			SurvivalGames.getInstance().grace = new CustomArrayList<String>();
			getLogger().info("SurvivalGames Fix Injected");
		} else {
			getLogger().warning("Failed to inject SurvivalGames Fix");
			this.setEnabled(false);
		}
	}

	/*
	 * Not really necessary. Just here as a failsafe.
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		List<String> worlds = this.getConfig().getStringList("ClearOnTeleportToWorld");
		if (worlds != null && worlds.contains(e.getTo().getWorld().getName())) {
			Plugin sg_ = this.getServer().getPluginManager().getPlugin("SurvivalGames");
			if (sg_ != null) {
				ArrayList<String> grace = SurvivalGames.getInstance().grace;
				int i = 0;
				while (grace.contains(e.getPlayer().getName())) {
					grace.remove(e.getPlayer().getName());
					if (i++ > 100) {
						this.getLogger().warning("Took over 100 attempts to remove a name from survivalgames");
						break;
					}
				}
			}
		}
	}

	/*
	 * "Isn't this just a Set?" Yes..
	 */
	public static class CustomArrayList<T> extends ArrayList<T> {
		@Override
		public boolean add(T t) {
			if (super.contains(t)) {
				return false;
			}
			return super.add(t);
		}

		private static final long serialVersionUID = 8605005363877020040L;
	}

}
