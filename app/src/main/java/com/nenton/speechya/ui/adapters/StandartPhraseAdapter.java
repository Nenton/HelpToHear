package com.nenton.speechya.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nenton.speechya.R;
import com.nenton.speechya.data.chronos.ChronosCreate;
import com.nenton.speechya.data.manager.DataManager;
import com.nenton.speechya.data.storage.models.Dialog;
import com.nenton.speechya.data.storage.models.Messages;
import com.nenton.speechya.data.storage.models.StandartPhrase;
import com.nenton.speechya.ui.activities.MainActivity;

import java.util.Date;
import java.util.List;

public class StandartPhraseAdapter extends RecyclerView.Adapter<StandartPhraseAdapter.PhraseViewHolder> {

    private Context mContext;
    private List<StandartPhrase> mPhrases;

    /**
     * Create adapter for phrase
     * @param phrases list phrase for adapter
     */
    public StandartPhraseAdapter(List<StandartPhrase> phrases) {
        mPhrases = phrases;
    }

    /**
     * Create view
     * @return create view phrase
     */
    @Override
    public PhraseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_standart_phrase, parent, false);
        return new PhraseViewHolder(convertView);
    }

    /**
     * Bind item view on current position
     * @param holder view item
     * @param position position item is list
     */
    @Override
    public void onBindViewHolder(PhraseViewHolder holder, int position) {
        final StandartPhrase standartPhrase = mPhrases.get(position);
        holder.mTextView.setText(standartPhrase.getPhrase());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                DataManager.getInstanse().getPreferencesManager().setCreateDateDialog(date.getTime());
                DataManager.getInstanse().getChronosConnector().runOperation(new ChronosCreate(new Dialog(date.getTime(), standartPhrase.getPhrase())),true);
                DataManager.getInstanse().getChronosConnector().runOperation(new ChronosCreate(new Messages(standartPhrase.getPhrase(), false, date.getTime(), date.getTime())),true);
                Intent intent = new Intent(mContext, MainActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * Get size list
     * @return size list
     */
    @Override
    public int getItemCount() {
        return mPhrases.size();
    }

    /**
     * Holder view phrase
     */
    public static class PhraseViewHolder extends RecyclerView.ViewHolder {

        private CardView mCardView;
        private TextView mTextView;

        /**
         * Initialization all view on item
         * @param itemView item in list
         */
        public PhraseViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.txt_phrase);
            mCardView = (CardView) itemView.findViewById(R.id.card_view_phrase);
        }
    }
}