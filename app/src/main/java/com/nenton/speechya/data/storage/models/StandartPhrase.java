package com.nenton.speechya.data.storage.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(active = true, nameInDb = "PHRASES")
public class StandartPhrase {

    @Id
    private Long mId;

    @NotNull
    private String phrase;

    @NotNull
    private Long sortPosition;

    /** Used for active entity operations. */
    @Generated(hash = 9933636)
    private transient StandartPhraseDao myDao;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public StandartPhrase(String phrase, Long sortPosition) {
        this.phrase = phrase;
        this.sortPosition = sortPosition;
    }

    public StandartPhrase(StandartPhrase standartPhrase,Long sortPosition){
        this.mId = standartPhrase.getMId();
        this.phrase = standartPhrase.getPhrase();
        this.sortPosition = sortPosition;
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
    @Generated(hash = 1970401058)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getStandartPhraseDao() : null;
    }

    public Long getSortPosition() {
        return this.sortPosition;
    }

    public void setSortPosition(Long sortPosition) {
        this.sortPosition = sortPosition;
    }

    public String getPhrase() {
        return this.phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public Long getMId() {
        return this.mId;
    }

    public void setMId(Long mId) {
        this.mId = mId;
    }

    @Generated(hash = 943666781)
    public StandartPhrase(Long mId, @NotNull String phrase,
            @NotNull Long sortPosition) {
        this.mId = mId;
        this.phrase = phrase;
        this.sortPosition = sortPosition;
    }

    @Generated(hash = 1827165862)
    public StandartPhrase() {
    }
    
}
