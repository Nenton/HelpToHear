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
import com.nenton.speechya.ui.activities.MainActivity;

import java.util.Date;
import java.util.List;

public class DialogsAdapter extends RecyclerView.Adapter<DialogsAdapter.DialogsViewHolder> {

    protected Context mContext;
    private List<Dialog> mPhrases;

    public DialogsAdapter(List<Dialog> phrases) {
        mPhrases = phrases;
    }

    @Override
    public DialogsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog, parent, false);
        return new DialogsViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(DialogsViewHolder holder, int position) {
        final Dialog dialog = mPhrases.get(position);
        holder.mTextDate.setText(dialog.getLastEditDate().toString());
        holder.mTextLastMessage.setText(dialog.getLastMessage());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = dialog.getDateCreateDialog();
                DataManager.getInstanse().getPreferencesManager().setCreateDateDialog(date.getTime());
                Intent intent = new Intent(mContext, MainActivity.class);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPhrases.size();
    }

    public static class DialogsViewHolder extends RecyclerView.ViewHolder {

        protected TextView mTextDate;
        protected TextView mTextLastMessage;
        protected CardView mCardView;

        public DialogsViewHolder(View itemView) {
            super(itemView);
            mTextLastMessage = (TextView) itemView.findViewById(R.id.last_message_dialog);
            mTextDate = (TextView) itemView.findViewById(R.id.date_last_message_dialog);
            mCardView = (CardView) itemView.findViewById(R.id.card_view_dialog);
        }
    }
}
