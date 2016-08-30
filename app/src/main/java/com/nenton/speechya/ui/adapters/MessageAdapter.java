package com.nenton.speechya.ui.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nenton.speechya.R;
import com.nenton.speechya.data.manager.DataManager;
import com.nenton.speechya.data.storage.models.Messages;
import com.nenton.speechya.ui.activities.MainActivity;
import com.nenton.speechya.ui.fragments.FragmentDialogDeleteMessage;
import com.nenton.speechya.ui.fragments.FragmentDialogResizeMessage;
import com.nenton.speechya.utils.ConstantManager;

import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import ru.yandex.speechkit.Error;
import ru.yandex.speechkit.Recognizer;
import ru.yandex.speechkit.Synthesis;
import ru.yandex.speechkit.Vocalizer;
import ru.yandex.speechkit.VocalizerListener;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private Context mContext;
    private List<Messages> mMessages;
    private Vocalizer vocalizer;
    private FragmentManager mFragmentManager;
    private MainActivity mMainActivity;

    /**
     * Create message adapter
     * @param messages list messages
     * @param mainActivity activity make adapter
     * @param fragmentManager fragment manager
     */
    public MessageAdapter(List<Messages> messages, MainActivity mainActivity, FragmentManager fragmentManager) {
        mMessages = messages;
        mFragmentManager = fragmentManager;
        mMainActivity = mainActivity;
    }

    /**
     * Create view
     * @return create view message
     */
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(convertView);
    }

    /**
     * Bind item view on current position
     * @param holder view item
     * @param position position item is list
     */
    @Override
    public void onBindViewHolder(final MessageViewHolder holder, final int position) {
        final Messages message = mMessages.get(position);
        holder.mMessage.setText(message.getMessage());
        setupClick(holder, message, position);
        setupLayoutParamsForEveryMessage(holder, message);
    }

    /**
     * Setup click on view
     * @param holder holder
     * @param message message
     * @param position position
     */
    private void setupClick(final MessageViewHolder holder, final Messages message, final int position) {
        holder.mMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentDialogResizeMessage resizeMessage = new FragmentDialogResizeMessage();
                resizeMessage.setMessage(message.getMessage(),mMainActivity,mContext);
                resizeMessage.show(mFragmentManager, resizeMessage.getClass().getName());
            }
        });

        holder.mMessage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(ConstantManager.STRING_EMPTY, message.getMessage());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mContext, ConstantManager.STRING_MESSAGE_COPY, Toast.LENGTH_LONG).show();
                return true;
            }
        });

        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentDialogDeleteMessage deleteMessage = new FragmentDialogDeleteMessage();
                deleteMessage.setMessage(message, MessageAdapter.this, position);
                deleteMessage.show(mFragmentManager, deleteMessage.getClass().getName());
            }
        });
    }

    /**
     * Setup current layout params for messages who write message
     * @param holder holder
     * @param message messages
     */
    private void setupLayoutParamsForEveryMessage(final MessageViewHolder holder, final Messages message) {
        if (message.getWhoWrite()) {
            holder.mSay.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((int) mContext.getResources().getDimension(R.dimen.spacing_null)
                    , (int) mContext.getResources().getDimension(R.dimen.spacing_small_8)
                    , (int) mContext.getResources().getDimension(R.dimen.spacing_medial_24)
                    , (int) mContext.getResources().getDimension(R.dimen.spacing_null)
            );
            holder.mRelativeLayout.setLayoutParams(layoutParams);
            holder.mLineMessage.setCardBackgroundColor(mContext.getResources().getColor(R.color.message_guest));
        } else {
            holder.mSay.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((int) mContext.getResources().getDimension(R.dimen.spacing_medial_24)
                    , (int) mContext.getResources().getDimension(R.dimen.spacing_small_8)
                    , (int) mContext.getResources().getDimension(R.dimen.spacing_null)
                    , (int) mContext.getResources().getDimension(R.dimen.spacing_null)
            );
            holder.mRelativeLayout.setLayoutParams(layoutParams);
            holder.mLineMessage.setCardBackgroundColor(mContext.getResources().getColor(R.color.message_user));
            holder.mSay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setupVocalizer(holder,message);
                }
            });
        }
    }

    /**
     * Setup vocalizer
     * @param holder holder
     * @param message message
     */
    private void setupVocalizer(final MessageViewHolder holder, Messages message ) {
        holder.mProgressWheel.setVisibility(View.VISIBLE);
        resetVocalizer();
        vocalizer = Vocalizer.createVocalizer(DataManager.getInstanse().getPreferencesManager().getLanguageVocalizer(), message.getMessage(), true, DataManager.getInstanse().getPreferencesManager().getSpeechVoice());
        vocalizer.setListener(new VocalizerListener() {
            @Override
            public void onSynthesisBegin(Vocalizer vocalizer) {
            }

            @Override
            public void onSynthesisDone(Vocalizer vocalizer, Synthesis synthesis) {
            }

            @Override
            public void onPlayingBegin(Vocalizer vocalizer) {
                resetRecognizer();
            }

            @Override
            public void onPlayingDone(Vocalizer vocalizer) {
                holder.mProgressWheel.setVisibility(View.GONE);
                if (mMainActivity.getBoolean()) {
                    createAndStartRecognizer();
                }
            }

            @Override
            public void onVocalizerError(Vocalizer vocalizer, Error error) {
                holder.mProgressWheel.setVisibility(View.GONE);
            }
        });
        vocalizer.start();
    }

    /**
     * Get size list
     * @return size list
     */
    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public Messages getMessage(int position){
        return mMessages.get(position);
    }

    /**
     * Delete message on position
     * @param position position message in list
     */
    public void deleteMessage(int position) {
        mMessages.remove(position);
        this.notifyDataSetChanged();
    }

    /**
     * Cancel vocalizer
     */
    private void resetVocalizer() {
        if (vocalizer != null) {
            vocalizer.cancel();
            vocalizer = null;
        }
    }

    /**
     * Start record voice
     */
    private void createAndStartRecognizer() {
        if (ContextCompat.checkSelfPermission(mContext, RECORD_AUDIO) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mMainActivity, new String[]{RECORD_AUDIO}, MainActivity.REQUEST_PERMISSION_CODE);
        } else {
            resetRecognizer();
            mMainActivity.setRecognizer(Recognizer.create(DataManager.getInstanse().getPreferencesManager().getLanguageRecognizer(), Recognizer.Model.NOTES, mMainActivity));
            mMainActivity.getRecognizer().start();
        }
    }

    /**
     * Cancel recognizer
     */
    private void resetRecognizer() {
        if (mMainActivity.getRecognizer() != null) {
            mMainActivity.getRecognizer().cancel();
            mMainActivity.setRecognizer(null);
        }
    }

    /**
     * Holder view phrase
     */
    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView mMessage;
        private ImageView mSay;
        private CardView mLineMessage;
        private CircularProgressBar mProgressWheel;
        private RelativeLayout mRelativeLayout;
        private ImageView mDelete;

        /**
         * Initialization all view on item
         * @param itemView item in list
         */
        public MessageViewHolder(View itemView) {
            super(itemView);
            mMessage = (TextView) itemView.findViewById(R.id.txt_message);
            mSay = (ImageView) itemView.findViewById(R.id.say_txt);
            mLineMessage = (CardView) itemView.findViewById(R.id.line_message);
            mProgressWheel = (CircularProgressBar) itemView.findViewById(R.id.progressBar);
            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_message);
            mDelete = (ImageView) itemView.findViewById(R.id.delete);
        }
    }
}