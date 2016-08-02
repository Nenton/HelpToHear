package com.nenton.speechya.data.storage.models;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MESSAGES".
*/
public class MessagesDao extends AbstractDao<Messages, Long> {

    public static final String TABLENAME = "MESSAGES";

    /**
     * Properties of entity Messages.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Message = new Property(1, String.class, "message", false, "MESSAGE");
        public final static Property WhoWrite = new Property(2, boolean.class, "whoWrite", false, "WHO_WRITE");
        public final static Property IdDialog = new Property(3, java.util.Date.class, "idDialog", false, "ID_DIALOG");
        public final static Property DateMessage = new Property(4, java.util.Date.class, "dateMessage", false, "DATE_MESSAGE");
    };

    private DaoSession daoSession;

    private Query<Messages> dialog_MMessagesQuery;

    public MessagesDao(DaoConfig config) {
        super(config);
    }
    
    public MessagesDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MESSAGES\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"MESSAGE\" TEXT NOT NULL ," + // 1: message
                "\"WHO_WRITE\" INTEGER NOT NULL ," + // 2: whoWrite
                "\"ID_DIALOG\" INTEGER NOT NULL ," + // 3: idDialog
                "\"DATE_MESSAGE\" INTEGER NOT NULL UNIQUE );"); // 4: dateMessage
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MESSAGES\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Messages entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getMessage());
        stmt.bindLong(3, entity.getWhoWrite() ? 1L: 0L);
        stmt.bindLong(4, entity.getIdDialog().getTime());
        stmt.bindLong(5, entity.getDateMessage().getTime());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Messages entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getMessage());
        stmt.bindLong(3, entity.getWhoWrite() ? 1L: 0L);
        stmt.bindLong(4, entity.getIdDialog().getTime());
        stmt.bindLong(5, entity.getDateMessage().getTime());
    }

    @Override
    protected final void attachEntity(Messages entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Messages readEntity(Cursor cursor, int offset) {
        Messages entity = new Messages( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // message
            cursor.getShort(offset + 2) != 0, // whoWrite
            new java.util.Date(cursor.getLong(offset + 3)), // idDialog
            new java.util.Date(cursor.getLong(offset + 4)) // dateMessage
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Messages entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setMessage(cursor.getString(offset + 1));
        entity.setWhoWrite(cursor.getShort(offset + 2) != 0);
        entity.setIdDialog(new java.util.Date(cursor.getLong(offset + 3)));
        entity.setDateMessage(new java.util.Date(cursor.getLong(offset + 4)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Messages entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Messages entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "mMessages" to-many relationship of Dialog. */
    public List<Messages> _queryDialog_MMessages(java.util.Date idDialog) {
        synchronized (this) {
            if (dialog_MMessagesQuery == null) {
                QueryBuilder<Messages> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.IdDialog.eq(null));
                dialog_MMessagesQuery = queryBuilder.build();
            }
        }
        Query<Messages> query = dialog_MMessagesQuery.forCurrentThread();
        query.setParameter(0, idDialog);
        return query.list();
    }

}