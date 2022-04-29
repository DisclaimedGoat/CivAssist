package com.disclaimedgoat.Integrations.Data;

import com.disclaimedgoat.Utilities.DataManagement.Database;
import org.bson.Document;

import java.lang.reflect.Field;

public class UserData {

    public String userId = "";
    public String playerName = "";

    private UserData(Document document) {
        for(Field field : getClass().getDeclaredFields()) {
            try {
                Object value = document.get(field.getName());

                field.set(this, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private UserData(String userId) {
        this.userId = userId;
    }

    public static UserData getUser(String id) {
        Document document = Database.getUser(id);
        if(document == null) {
            UserData data = new UserData(id);
            data.pushToDatabase(true);
            return data;
        }
        else return new UserData(document);
    }

    public static boolean isUserRegistered(String id) {
        return Database.getUser(id) != null;
    }

    private void pushToDatabase(boolean create) {
        Database.pushUser(this, create);
    }

    public <T> void setValue(String fieldName, T value) {
        try {
            Field field = getClass().getDeclaredField(fieldName);
            field.set(this, value);

            pushToDatabase(false);

        } catch (NoSuchFieldException e) {
            System.err.println("Unable to set " + fieldName + " to class! Check your spelling");
        } catch (IllegalAccessException e) {
            System.err.println("Unable to set " + fieldName + " to class! Illegal access!");
        } catch (IllegalArgumentException e) {
            System.err.println("Unable to set " + fieldName + " to class! Value is the wrong type!");
        }
    }

}
