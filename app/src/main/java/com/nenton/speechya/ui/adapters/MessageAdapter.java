package com.nenton.speechya.ui.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nenton.speechya.R;
import com.nenton.speechya.data.storage.models.Messages;
import com.nenton.speechya.ui.fragments.FragmentDialogResizeMessage;

import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import ru.yandex.speechkit.Error;
import ru.yandex.speechkit.Synthesis;
import ru.yandex.speechkit.Vocalizer;
import ru.yandex.speechkit.VocalizerListener;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {


    protected Context mContext;
    private List<Messages> mMessages;
    private Vocalizer vocalizer;
    private FragmentManager mFragmentManager;

    public MessageAdapter(List<Messages> messages,onGetFragmentManager fragmentManager) {
        mMessages = messages;
        mFragmentManager = fragmentManager.getFragmentManager();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder holder, int position) {

        final Messages message = mMessages.get(position);
        holder.mMessage.setText(message.getMessage());
        holder.mMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentDialogResizeMessage resizeMessage = new FragmentDialogResizeMessage();
                resizeMessage.setMessage(message.getMessage());
                resizeMessage.show(mFragmentManager,resizeMessage.getClass().getName());
            }
        });
        if (message.getWhoWrite()) {
            holder.mSay.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((int) mContext.getResources().getDimension(R.dimen.spacing_small_8)
                    , (int) mContext.getResources().getDimension(R.dimen.spacing_small_8)
                    , (int) mContext.getResources().getDimension(R.dimen.spacing_medium_32)
                    , (int) mContext.getResources().getDimension(R.dimen.spacing_small_8)
            );
            holder.mRelativeLayout.setLayoutParams(layoutParams);
            holder.mLineMessage.setCardBackgroundColor(mContext.getResources().getColor(R.color.message_guest));
        } else {
            holder.mSay.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((int) mContext.getResources().getDimension(R.dimen.spacing_medium_32)
                    , (int) mContext.getResources().getDimension(R.dimen.spacing_small_8)
                    , (int) mContext.getResources().getDimension(R.dimen.spacing_small_8)
                    , (int) mContext.getResources().getDimension(R.dimen.spacing_small_8)
            );
            holder.mRelativeLayout.setLayoutParams(layoutParams);
            holder.mLineMessage.setCardBackgroundColor(mContext.getResources().getColor(R.color.message_user));
            holder.mSay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mProgressWheel.setVisibility(View.VISIBLE);
                    resetVocalizer();
                    vocalizer = Vocalizer.createVocalizer(Vocalizer.Language.RUSSIAN, message.getMessage(), true, Vocalizer.Voice.ERMIL);
                    vocalizer.setListener(new VocalizerListener() {
                        @Override
                        public void onSynthesisBegin(Vocalizer vocalizer) {

                        }

                        @Override
                        public void onSynthesisDone(Vocalizer vocalizer, Synthesis synthesis) {

                        }

                        @Override
                        public void onPlayingBegin(Vocalizer vocalizer) {

                        }

                        @Override
                        public void onPlayingDone(Vocalizer vocalizer) {
                            holder.mProgressWheel.setVisibility(View.GONE);
                        }

                        @Override
                        public void onVocalizerError(Vocalizer vocalizer, Error error) {
                            holder.mProgressWheel.setVisibility(View.GONE);
                        }
                    });
                    vocalizer.start();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    private void resetVocalizer() {
        if (vocalizer != null) {
            vocalizer.cancel();
            vocalizer = null;
        }
    }

    // TODO: 28.07.2016 зеленый фон проигрываемого сообщения


    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        protected TextView mMessage;
        protected ImageView mSay;
        protected CardView mLineMessage;
        protected CircularProgressBar mProgressWheel;
        protected RelativeLayout mRelativeLayout;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mMessage = (TextView) itemView.findViewById(R.id.txt_message);
            mSay = (ImageView) itemView.findViewById(R.id.say_txt);
            mLineMessage = (CardView) itemView.findViewById(R.id.line_message);
            mProgressWheel = (CircularProgressBar) itemView.findViewById(R.id.progressBar);
            mRelativeLayout = (RelativeLayout)itemView.findViewById(R.id.relative_message);
        }

    }

    public interface onGetFragmentManager{
        FragmentManager getFragmentManager();
    }

}


