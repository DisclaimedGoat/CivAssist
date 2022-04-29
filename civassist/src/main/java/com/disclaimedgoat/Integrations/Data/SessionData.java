package com.disclaimedgoat.Integrations.Data;

import com.disclaimedgoat.Utilities.DataManagement.Database;
import net.dv8tion.jda.api.entities.Guild;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class SessionData {

    public String sessionName;
    public ArrayList<String> joinedUsers = new ArrayList<>();
    public ArrayList<String> blacklistedUsers = new ArrayList<>();
    public ArrayList<String> whitelistedUsers = new ArrayList<>();
    public int maxPlayers = 6;

    public String hostId;
    public String channelName;
    public String channelId;

    private final Guild guild;

    public SessionData(Guild guild, Document document) {
        this.guild = guild;

        for(Field field : SessionData.class.getFields()) {
            try {
                Object value = document.get(field.getName());

                if(value instanceof String[])
                    value = Arrays.asList((String[]) value);

                field.set(this, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public SessionData(Guild guild, String sessionName, String hostId, String channelName, String channelId) {
        this(guild, sessionName, 6, hostId, channelName, channelId);
    }

    public SessionData(Guild guild, String sessionName, int maxPlayers, String hostId, String channelName, String channelId) {
        this.guild = guild;
        this.sessionName = sessionName;
        this.maxPlayers = maxPlayers;

        this.hostId = hostId;
        this.channelName = channelName;
        this.channelId = channelId;

        joinedUsers.add(hostId);
    }

    @Nullable
    public static SessionData getBySessionName(Guild guild, String sessionName) {
        Document document = getDocumentBySessionName(guild, sessionName);
        if(document == null) return null;
        else return new SessionData(guild, document);
    }

    @Nullable
    public static SessionData getByChannelId(Guild guild, String channelId) {
        Document document = getDocumentByChannelId(guild, channelId);
        if(document == null) return null;
        else return new SessionData(guild, document);
    }

    public static void deleteSession(SessionData sessionData) {
        Database.deleteOneDocument(sessionData.guild, "sessionName", sessionData.sessionName);
    }

    public static boolean isValidSessionName(Guild guild, String sessionName) {
        return getDocumentBySessionName(guild, sessionName) == null;
    }

    public static boolean pushNew(SessionData sessionData) {
        Document document = getDocumentBySessionName(sessionData.guild, sessionData.sessionName);
        if(document != null) return false;

        Database.insertNewDocument(sessionData.guild, sessionData);
        return true;
    }

    private static Document getDocumentBySessionName(Guild guild, String sessionName)
    { return Database.findFirstFromGuild(guild, "sessionName", sessionName); }

    private static Document getDocumentByChannelId(Guild guild, String channelId)
    { return Database.findFirstFromGuild(guild, "channelId", channelId); }

    private <T> void setValue(String fieldName, T from, T to) {
        Database.updateOneFromGuild(guild, fieldName, from, to);
    }

    private <T> void setArrayFromList(ArrayList<T> list, String fieldName) {
        Database.updateOneFromGuildToNewValue(guild, "sessionName", sessionName, fieldName, list);
    }

    public void setSessionName(String sessionName) {
        setValue("sessionName", this.sessionName, sessionName);
        this.sessionName = sessionName;
    }

    public void sethostId(String hostId) {
        setValue("hostId", this.hostId, hostId);
        this.hostId = hostId;
    }

    public void setChannelName(String channelName) {
        setValue("channelName", this.channelName, channelName);
        this.channelName = channelName;
    }

    public void setChannelId(String channelId) {
        setValue("channelId", this.channelId, channelId);
        this.channelId = channelId;
    }

    public boolean addJoinedUser(String userId) {
        if(joinedUsers.contains(userId)) return false;
        joinedUsers.add(userId);

        setArrayFromList(joinedUsers, "joinedUsers");

        return true;
    }

    public boolean removeJoinedUser(String userId) {
        if(!joinedUsers.contains(userId)) return false;
        joinedUsers.remove(userId);


        setArrayFromList(joinedUsers, "joinedUsers");

        return true;
    }

    public boolean addBlacklistedUser(String userId) {
        if(blacklistedUsers.contains(userId)) return false;
        blacklistedUsers.add(userId);

        setArrayFromList(blacklistedUsers, "blacklistedUsers");

        return true;
    }

    public boolean removeBlacklistedUser(String userId) {
        if(!blacklistedUsers.contains(userId)) return false;
        blacklistedUsers.remove(userId);

        setArrayFromList(blacklistedUsers, "blacklistedUsers");

        return true;
    }

    public boolean addWhitelistedUser(String userId) {
        if(whitelistedUsers.contains(userId)) return false;
        whitelistedUsers.add(userId);

        setArrayFromList(whitelistedUsers, "whitelistedUsers");

        return true;
    }

    public boolean removeWhitelistedUser(String userId) {
        if(!whitelistedUsers.contains(userId)) return false;
        whitelistedUsers.remove(userId);

        setArrayFromList(whitelistedUsers, "whitelistedUsers");

        return true;
    }

    public boolean canAddUser(String userId) {
        if(whitelistedUsers.size() > 0) return whitelistedUsers.contains(userId);
        else return !blacklistedUsers.contains(userId);
    }

    public void setMaxPlayers(int maxPlayers) {
        setValue("maxPlayers", this.maxPlayers, maxPlayers);
        this.maxPlayers = maxPlayers;
    }

    public int getNumberPlayers() {
        return joinedUsers.size();
    }
}
