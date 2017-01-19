package com.blueboxmicrosystems.abaco.controller.entiry;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.blueboxmicrosystems.abaco.database.AbacoDatabaseHelper;
import com.blueboxmicrosystems.abaco.model.entity.Tag;

import java.util.ArrayList;

/**
 * Created by MARAUJO on 1/5/2017.
 */

public class TagController {
    private SQLiteDatabase db;

    public TagController(SQLiteDatabase db) {
        this.db = db;
    }

    private ContentValues getContentValues(Tag tag) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("icon", "");
        contentValues.put("name", tag.getName().toString());
        contentValues.put("description", tag.getDescription().toString());
        contentValues.put("color", tag.getColor().toString());
        return contentValues;
    }

    public void create(Tag tag) throws Exception {
        ContentValues contentValues = getContentValues(tag);
        try {
            this.db.insertOrThrow("main.tag", null, contentValues);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void update(Tag tag) throws Exception {
        ContentValues contentValues = getContentValues(tag);
        if (tag == null) {
            throw new Exception("Entity for update is null");
        }
        if (tag.getId() == null) {
            throw new Exception("Entity Id for update is null");
        }
        try {
            this.db.update("main.tag", contentValues, "id=" + tag.getId(), null);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void delete(Tag tag) throws Exception {
        try {
            db.delete("main.tag", "id=?", new String[]{tag.getId().toString()});
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }


    public Tag findById(Integer id)throws Exception {
        Tag tag = null;
        try {
            Cursor c = db.rawQuery("select id,sync,name,description,icon,color from main.tag where id=" + id, null);
            if (c.moveToFirst()) {
                tag = new Tag(c.getInt(0), c.getInt(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(5));
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
        return tag;
    }

    public ArrayList<Tag> findAll() throws Exception {
        ArrayList<Tag> tags = new ArrayList<Tag>();
        try {
            Cursor c = db.rawQuery("select id,sync,name,description,icon,color from main.tag order by id desc", null);
            if (c.moveToFirst()) {
                do {
                    tags.add(new Tag(c.getInt(0), c.getInt(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(5)));
                } while (c.moveToNext());
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
        return tags;
    }
}
