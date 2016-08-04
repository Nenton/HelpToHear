package com.nenton.speechya.data.storage.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(active = true, nameInDb = "DIALOGS")
public class Dialog {

    @Id
    private Long id;

    @NotNull
    @Unique
    private long dateCreateDialog;

    @NotNull
    private String lastMessage;

    @NotNull
    private long lastEditDate;

    @ToMany(joinProperties = {
            @JoinProperty(name = "dateCreateDialog",referencedName = "idDialog")
    })
    private List<Messages> mMessages;

    /** Used for active entity operations. */
    @Generated(hash = 1814831748)
    private transient DialogDao myDao;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public Dialog(long dateCreateDialog) {
        this.dateCreateDialog = dateCreateDialog;
        this.lastEditDate = dateCreateDialog;
        this.lastMessage = "";
    }

    public Dialog(long dateCreateDialog,String lastMessage) {
        this.dateCreateDialog = dateCreateDialog;
        this.lastEditDate = dateCreateDialog;
        this.lastMessage = lastMessage;
    }

    public Dialog(Dialog dialog, String lastMessage, long lastEditDate){
        this.id = dialog.getId();
        this.lastMessage = lastMessage;
        this.lastEditDate = lastEditDate;
        this.dateCreateDialog = dialog.getDateCreateDialog();
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

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 2035901497)
    public synchronized void resetMMessages() {
        mMessages = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 680490771)
    public List<Messages> getMMessages() {
        if (mMessages == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MessagesDao targetDao = daoSession.getMessagesDao();
            List<Messages> mMessagesNew = targetDao._queryDialog_MMessages(dateCreateDialog);
            synchronized (this) {
                if(mMessages == null) {
                    mMessages = mMessagesNew;
                }
            }
        }
        return mMessages;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 20542)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDialogDao() : null;
    }

    public long getLastEditDate() {
        return this.lastEditDate;
    }

    public void setLastEditDate(long lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public String getLastMessage() {
        return this.lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getDateCreateDialog() {
        return this.dateCreateDialog;
    }

    public void setDateCreateDialog(long dateCreateDialog) {
        this.dateCreateDialog = dateCreateDialog;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 581793868)
    public Dialog(Long id, long dateCreateDialog, @NotNull String lastMessage,
            long lastEditDate) {
        this.id = id;
        this.dateCreateDialog = dateCreateDialog;
        this.lastMessage = lastMessage;
        this.lastEditDate = lastEditDate;
    }

    @Generated(hash = 679371034)
    public Dialog() {
    }

}
