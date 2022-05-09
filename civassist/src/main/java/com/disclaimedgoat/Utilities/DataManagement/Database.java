package com.disclaimedgoat.Utilities.DataManagement;

import com.disclaimedgoat.Integrations.Data.UserData;
import com.disclaimedgoat.Utilities.Misc.ClassUtils;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import net.dv8tion.jda.api.entities.Guild;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class Database {

    private static String USERNAME = "";
    private static String PASSWORD = "";
    private static final String URL = "mongodb+srv://%s:%s@civ-assist.gnroy.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
    private static String formatConnectionString() { return String.format(URL, USERNAME, PASSWORD); }

    private static MongoClient client = null;

    private static MongoDatabase serversDatabase = null;
    private static MongoCollection<Document> globalServerCollection = null;


    private static MongoDatabase userDatabase = null;
    private static MongoCollection<Document> basicUserCollection = null;


    public static void init() {

        USERNAME = Environment.getMongoDBUsername();
        PASSWORD = Environment.getMongoDBPassword();

        ConnectionString cs = new ConnectionString(formatConnectionString());

        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(cs)
            .retryWrites(true)
            .build();

        client = MongoClients.create(settings);

        serversDatabase = client.getDatabase("servers");
        globalServerCollection = serversDatabase.getCollection("global");

        userDatabase = client.getDatabase("users");
        basicUserCollection = userDatabase.getCollection("basic");

    }

    public static void addServer(Guild guild) {
        if(guild == null) return;

        String id = guild.getId();
        if(isServerInCollection(id))
            return;

        databaseLog("Creating new database for guild " + guild.getId());

        serversDatabase.createCollection(id);
    }

    private static boolean isServerInCollection(String id) {
        for(String s : serversDatabase.listCollectionNames())
            if(s.equals(id)) return true;
        return false;
    }

    public static MongoCollection<Document> getCollectionFromGuild(Guild guild) {
        return serversDatabase.getCollection(guild.getId());
    }

    public static Document findFirstFromGuild(Guild guild, String field, Object value) {
        return findFirst(getCollectionFromGuild(guild), field, value);
    }

    public static void insertNewGuildDocument(Guild guild, Object obj) {
        getCollectionFromGuild(guild).insertOne(new Document(ClassUtils.mapClass(obj)));
    }

    public static <T> void deleteOneDocument(Guild guild, String fieldName, T value) {
        databaseLog("Deleting document with (" + fieldName + ", " + value + ") value");

        getCollectionFromGuild(guild).deleteOne(eq(fieldName, value));
    }

    public static Document findFirstThroughServerCollections(String field, Object value) {

        for(String collectionName : serversDatabase.listCollectionNames()) {
            MongoCollection<Document> collection = serversDatabase.getCollection(collectionName);

            Document potentialDoc = findFirst(collection, field, value);
            if(potentialDoc != null) return potentialDoc;
        }

        return null;
    }

    public static <T> void updateOneFromGuild(Guild guild, String fieldName, T from, T to) {
        serversDatabase.getCollection(guild.getId()).updateOne(eq(fieldName, from), set(fieldName, to));
    }

    public static <T> void updateOneFromGuildToNewValue(Guild guild, String filterName, T filterValue, String fieldName, T value) {
        serversDatabase.getCollection(guild.getId()).updateOne(eq(filterName, filterValue), set(fieldName, value));
    }

    private static Document findFirst(MongoCollection<Document> collection, String fieldName, Object value) {
        return collection.find(eq(fieldName, value)).first();
    }

    public static void pushUser(UserData user, boolean create) {
        databaseLog("Pushing user " + user.userId + " to database");

        if(create) basicUserCollection.insertOne(new Document(ClassUtils.mapClass(user)));
        else basicUserCollection.replaceOne(eq("userId", user.userId), new Document(ClassUtils.mapClass(user)));
    }

    public static Document getUser(String userId) {
        databaseLog("Getting user from database: " + userId);

        return basicUserCollection.find(eq("userId", userId)).first();
    }

    private static void iterateCollection(MongoCollection<Document> collection, DocumentIterator iterator) {
        FindIterable<Document> iterable = collection.find();

        for (Document document : iterable) {
            iterator.iterate(document);
        }
    }

    public static void iterateCollection(Guild guild, DocumentIterator iterator) {
        iterateCollection(getCollectionFromGuild(guild), iterator);
    }

    public interface DocumentIterator {
        void iterate(Document doc);
    }

    private static void databaseLog(String...content) {
        Logger.globalLog("database", content);
    }
}
