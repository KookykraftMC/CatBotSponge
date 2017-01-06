package com.kookykraftmc.catbot.listeners;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;

import com.kookykraftmc.catbot.CatBot;

public class ChatFilter
{
    final static public Random rdm = new Random();
    static Logger log;
    static public CatBot plugin;
    static public HashSet<String> badWords = new HashSet<String>(Arrays.asList("\\bl(a|@)g+(y|ing|s)?\\b","dick","fuck","\\b(bull)?shitt?(ing|y)?\\b","penis","vagina","fag","\\brape","slut","hitler","\\b(jack)?ass(holes?|lick|wipe)?\\b","arse(hole)?","bitch","whore","nigg(er|a)","bastard","bea?stiality","negro","retard","\\bcum\\b","cunt","dildo","bollocks?","\\bwank","jizz","piss"));
    static public String denyMsg;
    static public List<String> replacingWords = Arrays.asList("meow","hiss","purr");
    
    public ChatFilter(CatBot cb)
    {
        plugin = cb;
        loadCfg();
        log = cb.log;
    }

    @Listener
    public void onChat(MessageChannelEvent e)
    {
        //ToDon't filter if sender has permission to bypass
        boolean isFiltered = false;
        if(!(e instanceof MessageChannelEvent.Chat)) return;
        Text msg = e.getMessage();
        List<Text> msgs = e.getMessage().getChildren();
        Text.Builder newMessageBuilder = Text.builder();
        for(Text msgPart:msgs)
        {
            String msgPlain = msgPart.toPlain();
            String filteredMsg = filter(msgPlain);
            if(!msgPlain.equals(filteredMsg))
            {
                isFiltered = true;
                msgPart = Text.builder(filteredMsg).toText();
            }
            newMessageBuilder.append(msgPart);
        }
        if(isFiltered)
        {
            e.setMessage(newMessageBuilder.build());
            log.info("Original message: " + msg.toPlain());
        //ToDo: Send player denyMsg when their message is filtered
        }
    }
    
    /**
     * Filters message to replace bad words
     * 
     * @param message Message to filter
     * @return Filtered message
     */
    public String filter(String message)
    {
            for (String bad:badWords)
            {
                if(message.toLowerCase().matches("(?iu).*" + bad + ".*"))
                {
                    String replacingWord = replacingWords.get(rdm.nextInt(replacingWords.size()));
                    message = message.replaceAll("(?iu)" + bad,replacingWord);
                }
            }
            return message;
    }
    
    public void loadCfg()
    {
        //ToDo: Make it load from config
    }
    
}
