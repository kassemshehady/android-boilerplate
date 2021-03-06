package com.bpellow.android.boilerplate.activity.model;

import java.io.Reader;
import java.util.Date;

import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class Item {
    String content;
    Boolean favorited;
    Date favorited_at;
    Integer id;

    public Item() {}

    public static Item fromJSON(Reader stream) {
        try {
            Gson gson = new GsonBuilder()
            	.registerTypeAdapter(Date.class, new DateFormat().new DateDeserializer())
                .create();
            return gson.fromJson(stream, Item.class);
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Item fromJSON(JsonObject json) {
        try {
            if (json == null) throw new JsonParseException("invalid json");            
            Gson gson = new GsonBuilder()
            	.registerTypeAdapter(Date.class, new DateFormat().new DateDeserializer())
                .create();
            return gson.fromJson(json, Item.class);
        } catch (JsonParseException e) {
        	throw new RuntimeException(e);
        }
    }
    
    public static Item fromCursor(Cursor c) {
    	return fromCursor(c, true);
    }
    
    public static Item fromCursor(Cursor c, Boolean close) {
    	if (c.getCount() == 0) {
    		if (close) {
    			c.close();
    		}
    		return null;
    	}
    	
    	Item item = new Item();
    	int col1 = c.getColumnIndex("content");
    	int col2 = c.getColumnIndex("favorited");
    	int col3 = c.getColumnIndex("favorited_at");
    	int col4 = c.getColumnIndex("_id");
    	item.content = c.getString(col1);
    	item.id = c.getInt(col4);
    	item.favorited = (c.getInt(col2) == 1) ? true : false;
    	if (!c.isNull(col3)) {
    		item.favorited_at = new Date(c.getLong(col3));
    	}
    	if (close) {
    		c.close();
    	}
    	return item;
    }
    
    public String getContent() {
        return content;
    }
    public Boolean getFavorited() {
    	return favorited;
    }
    public Date getFavoritedAt() {
    	return favorited_at;
    }
    public String getFavoritedAtAsString() {
    	return (favorited_at == null) ? "unknown" : favorited_at.toLocaleString();
    }
    public Integer getId() {
    	return id;
    }

}
