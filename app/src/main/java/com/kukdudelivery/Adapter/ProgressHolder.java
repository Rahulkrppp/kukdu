package com.kukdudelivery.Adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.kukdudelivery.R;


class ProgressHolder extends RecyclerView.ViewHolder {
    private ProgressBar mProgressBar;

    ProgressHolder(final View itemView) {
        super(itemView);
        mProgressBar = itemView.findViewById(R.id.mProgressBar);

    }
}