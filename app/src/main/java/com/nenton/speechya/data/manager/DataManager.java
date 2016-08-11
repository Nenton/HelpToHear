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
import com.redmadrobot.chronos.ChronosConnector;

import java.util.Date;
import java.util.List;

public class DataManager {

    private static DataManager INSTANCE = null;
    private Context mContext;
    private PreferencesManager mPreferencesManager;
    private DaoSession mDaoSession;
    private ChronosConnector mChronosConnector;

    public ChronosConnector getChronosConnector() {
        return mChronosConnector;
    }

    /**
     * Create singleton
     */
    public DataManager() {
        this.mPreferencesManager = new PreferencesManager();
        this.mContext = SpeechApplication.getContext();
        this.mDaoSession = SpeechApplication.getDaoSession();
        this.mChronosConnector = SpeechApplication.getChronosConnector();
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

    /**
     * @return Current context
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * @return Current dao session
     */
    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    /**
     * Create new dialog
     * @param dialog new dialog
     */
    public void createNewDialog(Dialog dialog){
        mDaoSession.insertOrReplace(dialog);
    }

    /**
     * Create new message
     * @param message new message
     */
    public void createNewMessage(Messages message){
        mDaoSession.insertOrReplace(message);
    }

    /**
     * Create new phrase
     * @param phrase new phrase
     */
    public void createNewPhrase(StandartPhrase phrase){
        mDaoSession.insertOrReplace(phrase);
    }

    /**
     * @return all dialogs
     */
    public List<Dialog> getDialogs() {
    return mDaoSession.queryBuilder(Dialog.class)
            .where(DialogDao.Properties.LastMessage.notEq(""))
            .orderDesc(DialogDao.Properties.LastEditDate)
            .build()
            .list();
    }

    /**
     * @return all message on current dialog
     */
    public List<Messages> getMessages() {
        return mDaoSession.queryBuilder(Messages.class)
                .where(MessagesDao.Properties.IdDialog.eq((new Date(getPreferencesManager().getCreateDateDialog())).getTime()))
                .orderAsc(MessagesDao.Properties.DateMessage)
                .build()
                .list();
    }

    /**
     * @return all phrase
     */
    public List<StandartPhrase> getStandartPhrase() {
        return mDaoSession.queryBuilder(StandartPhrase.class)
                .orderDesc(StandartPhraseDao.Properties.SortPosition)
                .build()
                .list();
    }

    /**
     * @param lastMessage last message in dialog
     * @param lastUpdate date last message in dialog
     */
    public void updateDialog (String lastMessage, Date lastUpdate){
        Dialog dialog =  mDaoSession.queryBuilder(Dialog.class)
                .where(DialogDao.Properties.DateCreateDialog.eq(mPreferencesManager.getCreateDateDialog()))
                .build()
                .unique();
        mDaoSession.update(new Dialog(dialog,lastMessage,lastUpdate.getTime()));
    }

    /**
     * @param id id phrase
     * @param sortPosition position in list
     */
    public void updatePhrase(long id,long sortPosition){
        StandartPhrase standartPhrase = mDaoSession.queryBuilder(StandartPhrase.class)
                .where(StandartPhraseDao.Properties.MId.eq(id))
                .build()
                .unique();
        mDaoSession.update(new StandartPhrase(standartPhrase,sortPosition));
    }

    /**
     * @param id id dialog in BD
     */
    public void deleteDialog(long id) {
        mDaoSession.queryBuilder(Dialog.class)
                .where(DialogDao.Properties.Id.eq(id))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    /**
     * Delete empty dialogs
     */
    public void deleteDialogs() {
        mDaoSession.queryBuilder(Dialog.class)
                .where(DialogDao.Properties.LastMessage.eq(""))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    /**
     * Delete all message on current id dialog
     * @param idDialog id dialog
     */
    public void deleteMessages(long idDialog) {
        mDaoSession.queryBuilder(Messages.class)
                .where(MessagesDao.Properties.IdDialog.eq(idDialog))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    /**
     * Delete message
     * @param dateMessage date create message
     */
    public void deleteMessage(long dateMessage) {
        mDaoSession.queryBuilder(Messages.class)
                .where(MessagesDao.Properties.DateMessage.eq(dateMessage))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    /**
     * Delete phrase
     * @param mId id phrase
     */
    public void deleteStandartPhrase(long mId) {
        mDaoSession.queryBuilder(StandartPhrase.class)
                .where(StandartPhraseDao.Properties.MId.eq(mId))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }
}