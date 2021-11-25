package me.night.midnight.midnight_bot.autoreply;

import me.night.midnight.midnight_bot.core.Utilities;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

/**
 * A class for simple automated bot replies. If any of a given trigger exists in a message, a random
 * response is sent from the bot.
 */
public class SimpleAutoReply extends SimpleAutomatedResponse {
    // The strings that trigger this SimpleAutoReply
    private List<String> triggers;

    // The possible responses the bot can give
    private List<String> responses;

    // Whether to ignore case
    private boolean ignoreCase;

    // Whether to check for a whole word
    private boolean checkWholeWord;

    // Whether to send the response as a reply
    private boolean useReply;

    /**
     * Creates a new SimpleAutoReply
     * @param triggers The strings that trigger this SimpleAutoReply
     * @param responses The possible responses the bot can give
     * @param ignoreCase Whether to ignore case
     * @param checkWholeWord Whether to check for a whole word
     * @param useReply Whether to send the response as a reply
     */
    public SimpleAutoReply(List<String> triggers, List<String> responses, boolean ignoreCase, boolean checkWholeWord, boolean useReply) {
        this.triggers = triggers;
        this.responses = responses;
        this.ignoreCase = ignoreCase;
        this.checkWholeWord = checkWholeWord;
        this.useReply = useReply;
    }

    /**
     * Copy constructor.
     * @param simpleAutoReply The SimpleAutoReply to copy.
     */
    public SimpleAutoReply(SimpleAutoReply simpleAutoReply) {
        this.triggers = simpleAutoReply.triggers;
        this.responses = simpleAutoReply.responses;
        this.ignoreCase = simpleAutoReply.ignoreCase;
        this.checkWholeWord = simpleAutoReply.checkWholeWord;
        this.useReply = simpleAutoReply.useReply;
    }

    @Override
    /**
     * Checks to see if the message contains the trigger
     */
    public boolean checkConditions(Message msg) {
        String message = msg.getContentRaw();

        // Check if case should be  ignored
        if (ignoreCase)
            message = message.toLowerCase();

        for (String s : triggers) {
            if (ignoreCase)
                s = s.toLowerCase();

            if (checkWholeWord) {
                // Check if trigger is the message
                if (message.equals(s)) return true;

                // Check if trigger is in the middle of the message
                if (message.contains(" " + s + " ")) return true;

                // Check if trigger is at the beginning or end of the message
                if (message.contains(s + " ")) return true;
                if (message.contains(" " + s)) return true;

                // Message does not have trigger
            } else if (message.contains(s)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getResponse() {
        return Utilities.getRandomFrom(responses);
    }
}
