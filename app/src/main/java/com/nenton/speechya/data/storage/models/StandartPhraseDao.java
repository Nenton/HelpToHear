package com.nenton.speechya.data.storage.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PHRASES".
*/
public class StandartPhraseDao extends AbstractDao<StandartPhrase, Long> {

    public static final String TABLENAME = "PHRASES";

    /**
     * Properties of entity StandartPhrase.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property MId = new Property(0, Long.class, "mId", true, "_id");
        public final static Property Phrase = new Property(1, String.class, "phrase", false, "PHRASE");
        public final static Property SortPosition = new Property(2, long.class, "sortPosition", false, "SORT_POSITION");
    };

    private DaoSession daoSession;


    public StandartPhraseDao(DaoConfig config) {
        super(config);
    }
    
    public StandartPhraseDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PHRASES\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: mId
                "\"PHRASE\" TEXT NOT NULL ," + // 1: phrase
                "\"SORT_POSITION\" INTEGER NOT NULL );"); // 2: sortPosition
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PHRASES\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, StandartPhrase entity) {
        stmt.clearBindings();
 
        Long mId = entity.getMId();
        if (mId != null) {
            stmt.bindLong(1, mId);
        }
        stmt.bindString(2, entity.getPhrase());
        stmt.bindLong(3, entity.getSortPosition());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, StandartPhrase entity) {
        stmt.clearBindings();
 
        Long mId = entity.getMId();
        if (mId != null) {
            stmt.bindLong(1, mId);
        }
        stmt.bindString(2, entity.getPhrase());
        stmt.bindLong(3, entity.getSortPosition());
    }

    @Override
    protected final void attachEntity(StandartPhrase entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public StandartPhrase readEntity(Cursor cursor, int offset) {
        StandartPhrase entity = new StandartPhrase( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // mId
            cursor.getString(offset + 1), // phrase
            cursor.getLong(offset + 2) // sortPosition
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, StandartPhrase entity, int offset) {
        entity.setMId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPhrase(cursor.getString(offset + 1));
        entity.setSortPosition(cursor.getLong(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(StandartPhrase entity, long rowId) {
        entity.setMId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(StandartPhrase entity) {
        if(entity != null) {
            return entity.getMId();
        } else {
            return null;
        }
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}