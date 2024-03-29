package gg.scenarios.vesta.commands;

import gg.scenarios.vesta.Vesta;
import gg.scenarios.vesta.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DiscordCommand implements CommandExecutor {

    private final Vesta vesta;

    public DiscordCommand(Vesta vesta) {
        this.vesta = vesta;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatUtil.format("&a&LDISCORD: &f&a&ohttps://discord.gg/cdG3Wjd"));
        return false;
    }
}
