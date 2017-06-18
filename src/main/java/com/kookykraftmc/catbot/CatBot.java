package com.kookykraftmc.catbot;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Color;

import com.google.inject.Inject;
import com.kookykraftmc.catbot.listeners.ChatFilter;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id="catbot", name="CatBot", version="1.0",
        description="KKMC's adorable chat plugin.")
public class CatBot
{
    final public static String CHAT_PREFIX = Color.DARK_MAGENTA + "[" + Color.MAGENTA + "CatBot" + Color.DARK_MAGENTA + "]" + Color.WHITE;
    //final public static String CONSOLE_PREFIX = "[CatBot]"; Provided by default
    
    @Inject
    public Game game;

    @Inject
    public Logger log;
    
    @DefaultConfig(sharedRoot = false)
    @Inject
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;
    
    @Listener
    public void onServerStart(GameStartedServerEvent e) //throws IOException
    {
        //ToDo Load config
        /**
        try
        {
            CommentedConfigurationNode node = configLoader.load();
            config = Config.MAPPER.bindToNew().populate(node);
            Config.MAPPER.bind(config).serialize(node);
            configLoader.save(node);
        } catch (ObjectMappingException ex) {
            log.error("Couldn't populate Config!", ex);
        }
        **/
        registerCommands();
        Sponge.getEventManager().registerListeners(this, new ChatFilter(this));

    }
    
    /**
     * Send a message with prefix if necessary
     * @param sender Who to send it to
     * @param msg Message to send
     */
    public static void sendMsg(CommandSource sender, String msg)
    {
        String prefix = CHAT_PREFIX;
        if(sender instanceof ConsoleSource) prefix = "";
        Text txt = Text.builder(prefix + msg).toText();
        sender.sendMessage(txt);
    }
    
    private void registerCommands()
    {
        CommandSpec commandReload = CommandSpec.builder()
                .description(Text.of("Reload CatBot's config."))
                .permission("catbot.command.reload")
                .arguments(GenericArguments.none())
                .executor((sender, args) -> {
                    return CommandResult.success();
                    //ToDo reload config
                })
                .build();
        Sponge.getCommandManager().register(this, commandReload, "catbotreload");
        
        CommandSpec commandPet = CommandSpec.builder()
                .description(Text.of("Meow."))
                //.permission() everyone can use it
                .arguments(GenericArguments.none())
                .executor(new CommandExecutor() {
                    @Override
                    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException
                    {
                        sendMsg(sender, "Meow :3");
                        return CommandResult.success();
                    }
                })
                .build();
        Sponge.getCommandManager().register(this, commandPet, "catbotpet");
        
        
    }
    
}
