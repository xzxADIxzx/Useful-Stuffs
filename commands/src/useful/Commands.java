package useful;

import arc.struct.*;
import arc.util.*;
import arc.util.CommandHandler.*;
import arc.util.serialization.*;
import arc.util.serialization.Jval.*;
import mindustry.gen.*;

import static mindustry.Vars.*;

public class Commands {

    public static final CommandHandler clientHandler = netServer.clientCommands;
    public static final ObjectMap<String, Command> clientCommands = new ObjectMap<>();

    private static Jval commandsData = Jval.newObject();

    private static final String DEFAULT_PARAMS = "";
    private static final String DEFAULT_DESCRIPTION = "No description provided";

    public static void load() {
        var file = dataDirectory.child("commands.json");
        if (file.exists()) {
            commandsData = Jval.read(file.readString());

            Cooldowns.defaultCooldown(commandsData.getLong("defaultCooldown", Cooldowns.defaultCooldown));
            Cooldowns.restrictAdmins(commandsData.getBool("restrictAdmins", Cooldowns.restrictAdmins));
        }
    }

    public static Command create(String name) {
        return new Command(name);
    }

    public static Command admin(String name) {
        return new Command(name).admin(true);
    }

    public static Command hidden(String name) {
        return new Command(name).hidden(true);
    }

    public static Command getCommand(String name) {
        return clientCommands.get(name);
    }

    public static Seq<Command> getCommands(boolean admin) {
        return clientHandler.getCommandList().map(command -> clientCommands.get(command.text, new Command(command.text)
                        .params(command.paramText)
                        .description(command.description)))
                .retainAll(command -> !command.admin || admin)
                .removeAll(command -> command.hidden);
    }

    public static class Command {
        public final String name;
        public boolean enabled = true;

        public String params;
        public String description;

        public long cooldown;
        public boolean admin, hidden, welcomeMessage;

        public CommandRunner<Player> runner;

        public Command(String name) {
            this.name = name;
        }

        public Command enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Command params(String params) {
            this.params = params;
            return this;
        }

        public Command description(String description) {
            this.description = description;
            return this;
        }

        // Sets the cooldown of this command
        public Command cooldown(long cooldown) {
            this.cooldown = cooldown;
            return this;
        }

        // Makes this command admin-only
        public Command admin(boolean admin) {
            this.admin = admin;
            return this;
        }

        // Makes this command hidden
        public Command hidden(boolean hidden) {
            this.hidden = hidden;
            return this;
        }

        // Makes this command appear in the welcome message
        public Command welcomeMessage(boolean welcomeMessage) {
            this.welcomeMessage = welcomeMessage;
            return this;
        }

        // Registers this command
        public void register(CommandRunner<Player> runner) {
            this.runner = runner;

            // Load data from JSON
            if (commandsData.has(name)) {
                var data = commandsData.get(name);

                enabled = data.getBool("enabled", enabled);
                params = data.getString("params", params);
                description = data.getString("description", description);
                cooldown = data.getLong("cooldown", cooldown);
                admin = data.getBool("admin", admin);
                hidden = data.getBool("hidden", hidden);
                welcomeMessage = data.getBool("welcomeMessage", welcomeMessage);
            }

            if (!enabled) return;

            // Find default params if not set
            if (params == null)
                params = Bundle.getDefault("commands." + name + ".params", DEFAULT_PARAMS);

            // Find default description if not set
            if (description == null)
                description = Bundle.getDefault("commands." + name + ".description", DEFAULT_DESCRIPTION);

            clientHandler.<Player>register(name, params, description, (args, player) -> {
                if (admin && !player.admin) {
                    Bundle.send(player, "commands.permission-denied", name);
                    return;
                }

                if (Cooldowns.canRun(player, name)) {
                    runner.accept(args, player);
                    Cooldowns.run(player, name, cooldown);
                } else {
                    Bundle.send(player, "commands.cooldown", name, Bundle.formatDuration(cooldown));
                }
            });

            // Save this command for later use
            clientCommands.put(name, this);
        }

        // Returns the localised params (Or the default ones, if localised cannot be found)
        public String params(Player player) {
            return Bundle.get("commands." + name + ".params", params, player);
        }

        // Returns the localised description (Or the default one, if localised cannot be found)
        public String description(Player player) {
            return Bundle.get("commands." + name + ".description", description, player);
        }

        public void run(String[] args, Player player) {
            runner.accept(args, player);
        }
    }
}
