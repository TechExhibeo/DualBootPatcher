/*
 * Copyright (C) 2015  Andrew Gunnerson <andrewgunnerson@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.chenxiaolong.dualbootpatcher.patcher;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.chenxiaolong.dualbootpatcher.R;
import com.github.chenxiaolong.dualbootpatcher.patcher.PatchFileItemViewHolder
        .PatchFileItemViewClickListener;

import java.util.List;

public class PatchFileItemAdapter extends RecyclerView.Adapter<PatchFileItemViewHolder> implements PatchFileItemViewClickListener {
    private Context mContext;
    private List<PatchFileItem> mItems;
    private PatchFileItemClickListener mListener;

    public interface PatchFileItemClickListener {
        void onPatchFileItemClicked(PatchFileItem item);
    }

    public PatchFileItemAdapter(Context context, List<PatchFileItem> items,
                                PatchFileItemClickListener listener) {
        mContext = context;
        mItems = items;
        mListener = listener;
    }

    @Override
    public PatchFileItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_v7_patch_file_item, parent, false);
        return new PatchFileItemViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(PatchFileItemViewHolder holder, int position) {
        PatchFileItem item = mItems.get(position);

        holder.vTitle.setText(item.displayName);
        holder.vSubtitle1.setText(mContext.getString(
                R.string.patcher_card_subtitle_target, item.device.getId(), item.romId));

        // Clickability
        holder.vCard.setClickable(item.state == PatchFileState.QUEUED);

        switch (item.state) {
        case QUEUED:
        case PENDING:
        case CANCELLED:
        case COMPLETED:
            holder.vSubtitle2.setVisibility(View.VISIBLE);
            if (item.state == PatchFileState.QUEUED) {
                holder.vSubtitle2.setText(R.string.patcher_card_subtitle_queued);
            } else if (item.state == PatchFileState.PENDING) {
                holder.vSubtitle2.setText(R.string.patcher_card_subtitle_pending);
            } else if (item.state == PatchFileState.CANCELLED) {
                holder.vSubtitle2.setText(R.string.patcher_card_subtitle_cancelled);
            } else if (item.state == PatchFileState.COMPLETED) {
                if (item.successful) {
                    holder.vSubtitle2.setText(R.string.patcher_card_subtitle_succeeded);
                } else {
                    holder.vSubtitle2.setText(mContext.getString(
                            R.string.patcher_card_subtitle_failed, item.errorCode));
                }
            }
            holder.vProgress.setVisibility(View.GONE);
            holder.vProgressPercentage.setVisibility(View.GONE);
            holder.vProgressFiles.setVisibility(View.GONE);
            break;
        case IN_PROGRESS:
            holder.vSubtitle2.setVisibility(View.GONE);
            holder.vProgress.setVisibility(View.VISIBLE);
            holder.vProgressPercentage.setVisibility(View.VISIBLE);
            holder.vProgressFiles.setVisibility(View.VISIBLE);
            break;
        }

        // Normalize progress to 0-1000000 range to prevent integer overflow
        final int normalize = 1000000;
        double percentage;
        int value;
        int max;
        if (item.maxBytes == 0) {
            percentage = 0;
            value = 0;
            max = 0;
        } else {
            percentage = (double) item.bytes / item.maxBytes;
            value = (int) (percentage * normalize);
            max = normalize;
        }
        holder.vProgress.setMax(max);
        holder.vProgress.setProgress(value);
        holder.vProgress.setIndeterminate(item.maxBytes == 0);

        // Percentage progress text
        holder.vProgressPercentage.setText(String.format("%.1f%%", 100.0 * percentage));

        // Files progress
        holder.vProgressFiles.setText(mContext.getString(R.string.overall_progress_files,
                Long.toString(item.files), Long.toString(item.maxFiles)));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onPatchFileItemClicked(int position) {
        if (mListener != null) {
            mListener.onPatchFileItemClicked(mItems.get(position));
        }
    }
}