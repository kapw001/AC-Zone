package com.digitfellas.ac.dashboard.salesman;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digitfellas.ac.R;
import com.digitfellas.ac.data.model.response.Tasks;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyTasksPaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<Tasks.Data.Datum> movies;
    private Context context;
    private OnItemClickListener onItemClickListener;

    private boolean isLoadingAdded = false;

    public MyTasksPaginationAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        movies = new ArrayList<>();
    }

    public List<Tasks.Data.Datum> getMovies() {
        return movies;
    }

    public void setMovies(List<Tasks.Data.Datum> movies) {
        this.movies = movies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.row_item_my_tasks, parent, false);
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final Tasks.Data.Datum datum = movies.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                MovieVH movieVH = (MovieVH) holder;

                movieVH.name.setText(datum.getName() + "");


                if (datum.getRemarks() != null && datum.getRemarks().length() > 0) {
                    movieVH.remarks.setText(datum.getRemarks() + "");
                } else {
                    movieVH.remarks.setText("No remarks");
                }

                movieVH.txtDate.setText(datum.getTaskDate() + "");

                String day = getDayFullNameByDate(datum.getTaskDate());

                movieVH.txtDay.setText(day + "");


                if (onItemClickListener != null) {

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            onItemClickListener.OnItemClick(v, datum, position);
                        }
                    });

                }


                break;
            case LOADING:
//                Do nothing
                break;
        }

    }

    private String getDayFullNameByDate(String d) {
        if (d != null && d.length() > 0) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                Date date = format.parse(d);
                Format formatter = new SimpleDateFormat("EEEE");
                String s = formatter.format(date);
                return s;
            } catch (ParseException e) {
//            e.printStackTrace();
                return "";
            }
        } else return "";
    }


    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movies.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(Tasks.Data.Datum mc) {
        movies.add(mc);
        notifyItemInserted(movies.size() - 1);

    }


    public void clearAllData() {
        if (movies != null && movies.size() > 0) {
            movies.clear();
            notifyDataSetChanged();
        }

    }


    public void addAll(List<Tasks.Data.Datum> mcList) {

        movies.removeAll(mcList);
        notifyDataSetChanged();

        for (Tasks.Data.Datum mc : mcList) {
            add(mc);
        }
    }

    public void remove(Tasks.Data.Datum city) {
        int position = movies.indexOf(city);
        if (position > -1) {
            movies.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Tasks.Data.Datum());
    }

    public void removeLoadingFooterCheck() {

        if (isLoadingAdded) {
            isLoadingAdded = false;

            if (movies.size() > 0) {

                int position = movies.size() - 1;
                Tasks.Data.Datum item = getItem(position);

                if (item != null) {
                    movies.remove(position);
                    notifyItemRemoved(position);
                }
            }
        }
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        if (movies.size() > 0) {

            int position = movies.size() - 1;
            Tasks.Data.Datum item = getItem(position);

            if (item != null) {
                movies.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    public Tasks.Data.Datum getItem(int position) {
        return movies.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class MovieVH extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_day)
        TextView txtDay;
        @BindView(R.id.txt_date)
        TextView txtDate;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.remarks)
        TextView remarks;

        public MovieVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


    public interface OnItemClickListener {

        void OnItemClick(View view, Tasks.Data.Datum datum, int position);
    }


}