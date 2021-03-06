package com.nenton.speechya.data.storage.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(active = true, nameInDb = "MESSAGES")
public class Messages {

    @Id
    private Long id;

    @NotNull
    private String message;

    @NotNull
    private Boolean whoWrite;

    @NotNull
    private long idDialog;

    @Unique
    @NotNull
    private long dateMessage;

    /** Used for active entity operations. */
    @Generated(hash = 1949213313)
    private transient MessagesDao myDao;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Create message
     * @param message current message
     * @param whoWrite author message
     * @param idDialog id dialog current message
     * @param dateMessage date create message
     */
    public Messages(String message, Boolean whoWrite, long idDialog, long dateMessage) {
        this.message = message;
        this.whoWrite = whoWrite;
        this.idDialog = idDialog;
        this.dateMessage = dateMessage;
    }

    @Generated(hash = 231920587)
    public Messages(Long id, @NotNull String message, @NotNull Boolean whoWrite,
            long idDialog, long dateMessage) {
        this.id = id;
        this.message = message;
        this.whoWrite = whoWrite;
        this.idDialog = idDialog;
        this.dateMessage = dateMessage;
    }

    @Generated(hash = 826815580)
    public Messages() {
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 338114405)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMessagesDao() : null;
    }

    public long getDateMessage() {
        return this.dateMessage;
    }

    public void setDateMessage(long dateMessage) {
        this.dateMessage = dateMessage;
    }


    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdDialog(long idDialog) {
        this.idDialog = idDialog;
    }

    public long getIdDialog() {
        return this.idDialog;
    }

    public Boolean getWhoWrite() {
        return this.whoWrite;
    }

    public void setWhoWrite(Boolean whoWrite) {
        this.whoWrite = whoWrite;
    }
    


}
