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
import com.nenton.speechya.data.manager.DataManager;
import com.nenton.speechya.data.storage.models.Dialog;
import com.nenton.speechya.ui.activities.MainActivity;
import com.nenton.speechya.utils.ConstantManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DialogsAdapter extends RecyclerView.Adapter<DialogsAdapter.DialogsViewHolder> {

    private Context mContext;
    private List<Dialog> mPhrases;

    /**
     * Create dialog adapter
     * @param phrases phrases for adapter
     */
    public DialogsAdapter(List<Dialog> phrases) {
        mPhrases = phrases;
    }

    /**
     * Create view
     * @return create view dialog
     */
    @Override
    public DialogsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog, parent, false);
        return new DialogsViewHolder(convertView);
    }

    /**
     * Bind item view on current position
     * @param holder view item
     * @param position position item is list
     */
    @Override
    public void onBindViewHolder(DialogsViewHolder holder, int position) {

        final Dialog dialog = mPhrases.get(position);

        String format = (new SimpleDateFormat(ConstantManager.STRING_FORMAT_DATE)).format(new Date(dialog.getLastEditDate()));
        holder.mTextDate.setText(format);
        holder.mTextLastMessage.setText(dialog.getLastMessage());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date(dialog.getDateCreateDialog());
                DataManager.getInstanse().getPreferencesManager().setCreateDateDialog(date.getTime());
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
     * Holder view dialog
     */
    public static class DialogsViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextDate;
        private TextView mTextLastMessage;
        private CardView mCardView;

        /**
         * Initialization all view on item
         * @param itemView item in list
         */
        public DialogsViewHolder(View itemView) {
            super(itemView);
            mTextLastMessage = (TextView) itemView.findViewById(R.id.last_message_dialog);
            mTextDate = (TextView) itemView.findViewById(R.id.date_last_message_dialog);
            mCardView = (CardView) itemView.findViewById(R.id.card_view_dialog);
        }
    }
}
