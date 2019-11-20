package org.techtown.samplerelativelayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsageListAdapter extends RecyclerView.Adapter<UsageListAdapter.ViewHolder> {

    private List<CustomUsageStats> mCustomUsageStatsList = new ArrayList<>();
    private DateFormat mDateFormat = new SimpleDateFormat();
    private DateFormat mTimeFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mPackageName;
        private final TextView mLastTimeUsed;
        private final ImageView mAppIcon;

        public ViewHolder(View v) {
            super(v);
            mPackageName = (TextView) v.findViewById(R.id.textview_package_name);
            mLastTimeUsed = (TextView) v.findViewById(R.id.textview_last_time_used);
            mAppIcon = (ImageView) v.findViewById(R.id.app_icon);
        }

        public TextView getLastTimeUsed() {
            return mLastTimeUsed;
        }

        public TextView getPackageName() {
            return mPackageName;
        }

        public ImageView getAppIcon() {
            return mAppIcon;
        }
    }

    public UsageListAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.usage_row, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
//        if(mCustomUsageStatsList.get(position).usageStats.getTotalTimeInForeground()>=10000) {
//            viewHolder.getPackageName().setText(
//                    mCustomUsageStatsList.get(position).usageStats.getPackageName());
//            long lastTimeUsed = mCustomUsageStatsList.get(position).usageStats.getLastTimeUsed();
//            long foregroundTimeUsed = mCustomUsageStatsList.get(position).usageStats.getTotalTimeInForeground();
//
//            System.out.println(mTimeFormat.format(mCustomUsageStatsList.get(position).usageStats.getTotalTimeInForeground()));
//            //viewHolder.getLastTimeUsed().setText(mDateFormat.format(new Date(lastTimeUsed)));
//            viewHolder.getLastTimeUsed().setText(mTimeFormat.format(mCustomUsageStatsList.get(position).usageStats.getTotalTimeInForeground())+"    마지막사용시각"
//                    + mDateFormat.format(new Date(lastTimeUsed)));
//            viewHolder.getAppIcon().setImageDrawable(mCustomUsageStatsList.get(position).appIcon);
//        }


            viewHolder.getPackageName().setText(
                    mCustomUsageStatsList.get(position).usageStats.getPackageName());
            long lastTimeUsed = mCustomUsageStatsList.get(position).usageStats.getLastTimeUsed();
            long foregroundTimeUsed = mCustomUsageStatsList.get(position).usageStats.getTotalTimeInForeground();

            System.out.println(mTimeFormat.format(mCustomUsageStatsList.get(position).usageStats.getTotalTimeInForeground()));
            //viewHolder.getLastTimeUsed().setText(mDateFormat.format(new Date(lastTimeUsed)));
            viewHolder.getLastTimeUsed().setText(mTimeFormat.format(mCustomUsageStatsList.get(position).usageStats.getTotalTimeInForeground())+"    마지막사용시각"
                    + mDateFormat.format(new Date(lastTimeUsed)));
            viewHolder.getAppIcon().setImageDrawable(mCustomUsageStatsList.get(position).appIcon);

    }

    @Override
    public int getItemCount() {
        return mCustomUsageStatsList.size();
    }

    public void setCustomUsageStatsList(List<CustomUsageStats> customUsageStats) {
        mCustomUsageStatsList = customUsageStats;
    }
}
