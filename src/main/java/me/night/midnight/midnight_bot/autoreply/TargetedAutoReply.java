package me.night.midnight.midnight_bot.autoreply;

import net.dv8tion.jda.api.entities.Message;

import java.util.List;

/**
 * An auto reply that functions like {@link SimpleAutoReply}, except it also only triggers
 * on specified message senders.
 */
public class TargetedAutoReply extends SimpleAutoReply {
    private List<Long> userIds;
    private boolean ignoreMode;

    /**
     * Creates a new TargetedAutoReply
     * @param triggers       The strings that trigger this SimpleAutoReply
     * @param responses      The possible responses the bot can give
     * @param ignoreCase     Whether to ignore case
     * @param checkWholeWord Whether to check for a whole word
     * @param useReply       Whether to send the response as a reply
     * @param userIds        The list of user IDs to use for this autoreply
     * @param ignoreMode     If true, TargetedAutoReply can only trigger on users whose IDs are not specified in userIds.
     *                       Otherwise, it only triggers on users whose IDs are specified in userIds
     */
    public TargetedAutoReply(List<String> triggers, List<String> responses, boolean ignoreCase, boolean checkWholeWord, boolean useReply,
                             List<Long> userIds, boolean ignoreMode) {
        super(triggers, responses, ignoreCase, checkWholeWord, useReply);
        this.userIds = userIds;
        this.ignoreMode = ignoreMode;
    }

    /**
     * Creates a new TargetedAutoReply from a given SimpleAutoReply
     * @param simpleAutoReply The SimpleAutoReply to upgrade
     * @param userIds        The list of user IDs to use for this autoreply
     * @param ignoreMode     If true, TargetedAutoReply can only trigger on users whose IDs are not specified in userIds.
     *                       Otherwise, it only triggers on users whose IDs are specified in userIds
     */
    public TargetedAutoReply(SimpleAutoReply simpleAutoReply, List<Long> userIds, boolean ignoreMode) {
        super(simpleAutoReply);
        this.userIds = userIds;
        this.ignoreMode = ignoreMode;
    }

    @Override
    public boolean checkConditions(Message msg) {
        long userId = msg.getAuthor().getIdLong();

        // Check if user ID matches up
        if ((ignoreMode && !userIds.contains(userId))
                || (!ignoreMode && userIds.contains(userId)))
            return super.checkConditions(msg);
        else return false;
    }
}
