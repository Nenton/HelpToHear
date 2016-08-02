package com.nenton.speechya.ui.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nenton.speechya.R;
import com.nenton.speechya.data.storage.models.Messages;

import java.util.List;

import ru.yandex.speechkit.Error;
import ru.yandex.speechkit.Synthesis;
import ru.yandex.speechkit.Vocalizer;
import ru.yandex.speechkit.VocalizerListener;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> implements VocalizerListener {


    protected Context mContext;
    private List<Messages> mMessages;
    private Vocalizer vocalizer;

    public MessageAdapter(List<Messages> messages) {
        mMessages = messages;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {

        final Messages message = mMessages.get(position);
        holder.mMessage.setText(message.getMessage());
        if (message.getWhoWrite()) {
            holder.mSay.setVisibility(View.GONE);
            CardView.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((int) mContext.getResources().getDimension(R.dimen.spacing_small_8)
                    , (int) mContext.getResources().getDimension(R.dimen.spacing_small_8)
                    , (int) mContext.getResources().getDimension(R.dimen.spacing_medium_32)
                    , (int) mContext.getResources().getDimension(R.dimen.spacing_small_8)
            );
            holder.mLineMessage.setLayoutParams(layoutParams);
//            holder.mLineMessage.setsetBackground();
            holder.mLineMessage.setCardBackgroundColor(mContext.getResources().getColor(R.color.message_guest));
        } else {
            holder.mSay.setVisibility(View.VISIBLE);
            CardView.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((int) mContext.getResources().getDimension(R.dimen.spacing_medium_32)
                    , (int) mContext.getResources().getDimension(R.dimen.spacing_small_8)
                    , (int) mContext.getResources().getDimension(R.dimen.spacing_small_8)
                    , (int) mContext.getResources().getDimension(R.dimen.spacing_small_8)
            );
            holder.mLineMessage.setLayoutParams(layoutParams);
            holder.mLineMessage.setCardBackgroundColor(mContext.getResources().getColor(R.color.message_user));
            holder.mSay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetVocalizer();
                    vocalizer = Vocalizer.createVocalizer(Vocalizer.Language.RUSSIAN, message.getMessage(), true, Vocalizer.Voice.ERMIL);
                    vocalizer.setListener(MessageAdapter.this);
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

    @Override
    public void onSynthesisBegin(Vocalizer vocalizer) {
// TODO: 28.07.2016 statusBar
    }

    @Override
    public void onSynthesisDone(Vocalizer vocalizer, Synthesis synthesis) {
        // TODO: 28.07.2016 зеленый фон проигрываемого сообщения
    }

    @Override
    public void onPlayingBegin(Vocalizer vocalizer) {

    }

    @Override
    public void onPlayingDone(Vocalizer vocalizer) {

    }

    @Override
    public void onVocalizerError(Vocalizer vocalizer, Error error) {
        resetVocalizer();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        protected TextView mMessage;
        protected ImageView mSay;
        protected CardView mLineMessage;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mMessage = (TextView) itemView.findViewById(R.id.txt_message);
            mSay = (ImageView) itemView.findViewById(R.id.say_txt);
            mLineMessage = (CardView) itemView.findViewById(R.id.line_message);
        }

    }

}


