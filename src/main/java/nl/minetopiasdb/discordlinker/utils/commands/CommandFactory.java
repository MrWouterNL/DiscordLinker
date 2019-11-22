package nl.minetopiasdb.discordlinker.utils.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import nl.minetopiasdb.discordlinker.utils.data.ConfigUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CommandFactory {

    private static CommandFactory instance = null;
    private static HashMap<Command, BotCommand> commands = new HashMap<Command, BotCommand>();

    public static CommandFactory getInstance() {
        if (instance == null) {
            instance = new CommandFactory();
        }
        return instance;
    }

    public void registerCommand(String command, BotCommand executor) {
        commands.keySet().forEach(cmd -> {
            if (cmd.getName().equalsIgnoreCase(command)) {
                return;
            }
        });
        commands.put(new Command(ConfigUtils.getInstance().getPrefix() + command), executor);
    }

    public Command excecute(String fullProvided, Message msg, MessageReceivedEvent event) {
        String[] args = fullProvided.split(" ");
        if (args.length == 0) {
            return null;
        }
        ArrayList<String> tempArgs = new ArrayList<>(Arrays.asList(args));
        tempArgs.remove(0);
        args = tempArgs.toArray(new String[0]);
        for (Command cmd : commands.keySet()) {
            if (cmd.getName().equalsIgnoreCase(fullProvided.split(" ")[0])) {
                commands.get(cmd).excecute(cmd, args, msg, event);
                return cmd;
            }
        }
        return null;
    }
}
