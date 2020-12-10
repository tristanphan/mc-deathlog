package com.tristanphan;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public final class DeathLog extends JavaPlugin implements Listener {

    public File folder;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        folder = new File(this.getDataFolder().getPath());
        if (!(folder.exists())) folder.mkdirs();


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        getMessage(event.getEntity(), false, event.getDeathMessage(), "Death");
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        getMessage(event.getPlayer(), true, null, "Logout");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("deathlog.access")) return false;

        if (args.length > 0) {
            File playerFile = new File(folder, args[0] + ".log");
            if (!sender.hasPermission("deathlog.all")) return false;
            if (!playerFile.exists()) {
                sender.sendMessage("§6§lDL §r§3> §l§cCannot find log file");
                return true;
            }

            try {
                Scanner reader = new Scanner(playerFile);
                boolean send = false;
                while (reader.hasNextLine()) {
                    String nextLine = reader.nextLine();
                    if (!nextLine.isEmpty()) send = true;
                    if (nextLine.contains("[")) nextLine = "§f§l" + nextLine;
                    else nextLine = "§3§l" + nextLine.replace(":",":§r§b");
                    if (send) sender.sendMessage("§6§lDL §r§3> " + nextLine);
                }
            } catch (FileNotFoundException ignored) { }
            return true;
        }

        File playerFile = new File(folder, ((Player) sender).getUniqueId() + ".log");
        if (!command.getName().equalsIgnoreCase("dl")) return false;
        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
            } catch (Exception ignored) {
            }
        }
        try {
            Scanner reader = new Scanner(playerFile);
            boolean send = false;
            while (reader.hasNextLine()) {
                String nextLine = reader.nextLine();
                if (!nextLine.isEmpty()) send = true;
                if (nextLine.contains("[")) nextLine = "§f§l" + nextLine;
                else nextLine = "§3§l" + nextLine.replace(":",":§r§b");
                if (nextLine.contains("Player")) continue;
                if (send) sender.sendMessage("§6§lDL §r§3> " + nextLine);
            }
        } catch (FileNotFoundException ignored) { }

        return true;
    }

    public void getMessage(Player player, Boolean hasHealth, String deathMessage, String action) {

        String header = String.format("[%s]", action);

        Location location = player.getLocation();
        location.getX();
        String printer = String.format(
                "%s\n" +
                "Player: %s\n" +
                        "Time: %s\n" +
                        "Location: %s / %s / %s",
                header,
                player.getDisplayName(),
                currentTime(),
                round(location.getX()),
                round(location.getY()),
                round(location.getZ())
        );

        if (hasHealth) {
            printer += String.format(
                    "\nHealth: %s hearts",
                    Math.round(player.getHealth()) / 2
            );
        } else if (deathMessage != null) {
            printer += String.format(
                    "\nDeath Message: %s",
                    deathMessage
            );
        }

        File playerFile = new File(folder, player.getUniqueId() + ".log");
        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
            } catch (Exception ignored) {
            }
        }

        try {
            FileWriter writer = new FileWriter(playerFile.getPath(), true);
            writer.write("\n\n" + printer);
            writer.close();
        } catch (Exception ignored) {
        }

        System.out.println(System.lineSeparator() + printer);
    }

    public Double round(Double number) {
        int step1 = (int) Math.round(number * 100);
        return (double) step1 / 100;
    }

    public String currentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss a");
        return dtf.format(LocalDateTime.now(ZoneId.of("America/Los_Angeles")));
    }

}
