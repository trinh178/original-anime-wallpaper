package com.samuigroup.originalanimewallpaper.views.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.samuigroup.originalanimewallpaper.BaseContract;

import java.util.ArrayList;

public class TagsMACTVAdapter extends ArrayAdapter<String> {
    // Presenter
    private BaseContract.Presenter presenter;
    //
    private ArrayList<String> suggestions;
    // Search optimum
    private AsyncTask<String, Void, Void> timeOutTask;

    public TagsMACTVAdapter(Context context, BaseContract.Presenter presenter) {
        super(context, android.R.layout.simple_list_item_1, new ArrayList<String>());
        this.presenter = presenter;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }
    private Filter mFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (timeOutTask != null) {
                timeOutTask.cancel(true);
            }
            if (constraint != null) {
                timeOutTask = new AsyncTask<String, Void, Void>() {
                    @Override
                    protected Void doInBackground(String... strings) {
                        // Timeoff
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // If was cancelled during timeoff
                        if (isCancelled()) return null;

                        // Load
                        presenter.onTypeSearch(strings[0]);
                        //
                        return null;
                    }
                }.execute(constraint.toString());

                //
                return new FilterResults(); //*
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results != null) {
                ArrayList<String> suggestions = (ArrayList<String>) results.values;
                if (results.count > 0) {
                    clear();
                    for (String s: suggestions) {
                        add(s);
                    }
                    notifyDataSetChanged();
                }
            }
        }

        /*
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            SpannableString text = new SpannableString(resultValue.toString());
            text.setSpan(new BackgroundColorSpan(Color.GREEN), 0, text.length(), 0);
            return text;
        }
        */
    };
}
