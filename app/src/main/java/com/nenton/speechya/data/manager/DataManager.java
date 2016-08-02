package com.nenton.speechya.data.manager;

import android.content.Context;

import com.nenton.speechya.data.storage.models.DaoSession;
import com.nenton.speechya.data.storage.models.Dialog;
import com.nenton.speechya.data.storage.models.DialogDao;
import com.nenton.speechya.data.storage.models.Messages;
import com.nenton.speechya.data.storage.models.MessagesDao;
import com.nenton.speechya.data.storage.models.StandartPhrase;
import com.nenton.speechya.data.storage.models.StandartPhraseDao;
import com.nenton.speechya.utils.SpeechApplication;

import java.util.Date;
import java.util.List;

public class DataManager {

    private static DataManager INSTANCE = null;
    private Context mContext;
    private PreferencesManager mPreferencesManager;
    private DaoSession mDaoSession;

    public DataManager() {
        this.mPreferencesManager = new PreferencesManager();
        this.mContext = SpeechApplication.getContext();
        this.mDaoSession = SpeechApplication.getDaoSession();
    }

    /**
     * @return New datamanager or this
     */
    public static DataManager getInstanse() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    /**
     * @return Current preferences manager
     */
    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    public Context getContext() {
        return mContext;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public void createNewDialog(Dialog dialog){
        mDaoSession.insertOrReplace(dialog);
    }

    public void createNewMessage(Messages message){
        mDaoSession.insertOrReplace(message);
    }

    public List<Dialog> getDialogs() {
    return mDaoSession.queryBuilder(Dialog.class)
            .where(DialogDao.Properties.LastMessage.notEq(""))
            .orderDesc(DialogDao.Properties.LastEditDate)
            .build()
            .list();
    }

    public List<Messages> getMessages() {
        return mDaoSession.queryBuilder(Messages.class)
                .where(MessagesDao.Properties.IdDialog.eq(new Date(getPreferencesManager().getCreateDateDialog())))
                .orderAsc(MessagesDao.Properties.DateMessage)
                .build()
                .list();
    }


    public List<StandartPhrase> getStandartPhrase() {
        return mDaoSession.queryBuilder(StandartPhrase.class)
                .orderAsc(StandartPhraseDao.Properties.SortPosition)
                .build()
                .list();
    }

    public void updateDialog (String lastMessage, Date lastUpdate){
        Dialog dialog =  mDaoSession.queryBuilder(Dialog.class)
                .where(DialogDao.Properties.DateCreateDialog.eq(mPreferencesManager.getCreateDateDialog()))
                .build()
                .unique();
        mDaoSession.update(new Dialog(dialog,lastMessage,lastUpdate));
    }

    public void deleteDialog(long id) {
        mDaoSession.queryBuilder(Dialog.class)
                .where(DialogDao.Properties.Id.eq(id))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    public void deleteStandartPhrase(long mId) {
        mDaoSession.queryBuilder(StandartPhrase.class)
                .where(StandartPhraseDao.Properties.MId.eq(mId))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    public void updatePhrase(long id,long sortPosition){
        StandartPhrase standartPhrase = mDaoSession.queryBuilder(StandartPhrase.class)
                .where(StandartPhraseDao.Properties.MId.eq(id))
                .build()
                .unique();
        mDaoSession.update(new StandartPhrase(standartPhrase,sortPosition));
    }

//    public StandartPhrase isEmptyStandartPhrase(){
//        return mDaoSession.queryBuilder(StandartPhrase.class)
//                .build()
//                .unique();
//    }

    public void setStandartPhrase(List<StandartPhrase> standartPhrase){
        List<StandartPhrase> unique = mDaoSession.queryBuilder(StandartPhrase.class)
                .build()
                .list();
        if (unique.size() == 0) {
            for (int i = 0; i < standartPhrase.size(); i++) {
                mDaoSession.insertOrReplace(standartPhrase.get(i));
            }
        }
    }

//    public void deleteUser(String query) {
//        try {
//            mDaoSession.queryBuilder(User.class)
//                    .where(UserDao.Properties.RemoteId.eq(query))
//                    .buildDelete()
//                    .executeDeleteWithoutDetachingEntities();
//            mDaoSession.queryBuilder(Repositories.class)
//                    .where(RepositoriesDao.Properties.UserRemoteId.eq(query))
//                    .buildDelete()
//                    .executeDeleteWithoutDetachingEntities();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
