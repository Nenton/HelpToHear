package com.nenton.speechya.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nenton.speechya.R;
import com.nenton.speechya.data.manager.DataManager;
import com.nenton.speechya.data.storage.models.Dialog;
import com.nenton.speechya.data.storage.models.Messages;
import com.nenton.speechya.data.storage.models.StandartPhrase;
import com.nenton.speechya.ui.activities.MainActivity;

import java.util.Date;
import java.util.List;

public class StandartPhraseAdapter extends RecyclerView.Adapter<StandartPhraseAdapter.StandartPhraseViewHolder> {

    protected Context mContext;
    private List<StandartPhrase> mPhrases;

    public StandartPhraseAdapter(List<StandartPhrase> phrases) {
        mPhrases = phrases;
    }

    @Override
    public StandartPhraseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_standart_phrase, parent, false);
        return new StandartPhraseViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(StandartPhraseViewHolder holder, int position) {
        final StandartPhrase standartPhrase = mPhrases.get(position);
        holder.mTextView.setText(standartPhrase.getPhrase());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                DataManager.getInstanse().getPreferencesManager().setCreateDateDialog(date.getTime());
                DataManager.getInstanse().createNewDialog(new Dialog(date,standartPhrase.getPhrase()));
                DataManager.getInstanse().createNewMessage(new Messages(standartPhrase.getPhrase(),false,date,date));
                Intent intent = new Intent(mContext, MainActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPhrases.size();
    }

    public static class StandartPhraseViewHolder extends RecyclerView.ViewHolder {
        protected CardView mCardView;
        protected TextView mTextView;

        public StandartPhraseViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.txt_phrase);
            mCardView = (CardView) itemView.findViewById(R.id.card_view_phrase);
        }
    }
}
