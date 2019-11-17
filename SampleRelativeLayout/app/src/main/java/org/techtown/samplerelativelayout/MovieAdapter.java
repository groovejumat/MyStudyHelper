package org.techtown.samplerelativelayout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> implements Filterable  {

    private ArrayList<Movie> items;
    private ArrayList<Movie> itemsFull;

    //필터 사용을 위해 추가된 메서드
    MovieAdapter(ArrayList<Movie> items) {
        this.items = items;
        //여기서 값을 못넣는다.//
        Log.e("LOG", "아이템이 가득찬 상태일 때의 갯수 확인." + items.size());
        itemsFull = new ArrayList<>(items);
        Log.e("LOG", "아이템이 가득찬 상태일 때의 갯수 확인." + itemsFull.size());
    }

    public void SearchDataSet (ArrayList<Movie> items){
        Log.e("LOG", "색인 데이터 리스트 변경." + items.size());
        itemsFull=items;
    }


    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void setItems(ArrayList<Movie> items) {
        this.items = items;
    }

    public void addItems() {
        Movie movieitem = new Movie("https://i.ytimg.com/vi/5-mWvUR7_P0/maxresdefault.jpg",
                "action", "Ant Man", "this movie open in 2015.06");
        this.items.add(movieitem);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivMovie;
        TextView tvTitle, tvContent, tvGenre;

        ViewHolder(View itemView) {
            super(itemView);

            ivMovie = itemView.findViewById(R.id.iv_item_movie);

            tvTitle = itemView.findViewById(R.id.tv_item_movie_title);
            tvContent = itemView.findViewById(R.id.tv_item_movie_content);
            //tvGenre = itemView.findViewById(R.id.tv_item_movie_genre);

            //ivMovie.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        Log.v("태그", String.valueOf(pos)+"가 호출됌.");
                        notifyItemChanged(pos);

                        String url = items.get(pos).getContent();
                        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                        context.startActivity(intent);

//                        if(mListener!=null){
//                            mListener.onItemClcik(v,pos);
                        //}
                    }
                }
            });
        }
    }


    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder viewHolder, int position) {

        Movie item = items.get(position);
        Glide.with(viewHolder.itemView.getContext())
                .load(item.getUrl())
                .into(viewHolder.ivMovie);


//        if (item.getUrl().isEmpty()) {
//            Glide.with(viewHolder.itemView.getContext())
//                    .load(item.getUrl())
//                    .into(viewHolder.ivMovie);
//            viewHolder.ivMovie.setVisibility(View.GONE);
//
//        }
//        else {
//            Glide.with(viewHolder.itemView.getContext())
//                    .load(item.getUrl())
//                    .into(viewHolder.ivMovie);
//            //viewHolder.ivMovie.setVisibility(View.GONE);
//        }

        viewHolder.tvTitle.setText(item.getTitle());
        viewHolder.tvContent.setText(item.getContent());
        // viewHolder.tvGenre.setText(item.getGenre());
    }

    //필터를 위해서 추가된 메서드
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Movie> filteredList = new ArrayList<Movie>();
            //Log.e("LOG", String.valueOf(filteredList.size()));
            Log.e("LOG", String.valueOf(filteredList.size()));

            if (constraint == null || constraint.length() == 0) {
                Log.e("LOG", "[비교작업을 시작합니다.]");
                filteredList.addAll(itemsFull);
                Log.e("LOG", String.valueOf(filteredList.size()));
            }
            else {
                Log.e("LOG", "[비교작업을 시작합니다.]");
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Movie item : itemsFull) {
                    Log.e("LOG", "내용을 비교합니다." + item.getTitle());
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        Log.e("LOG", String.valueOf(filteredList.size()));
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            Log.e("LOG", "검색 결과 반환A");
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items.clear();
            items.addAll((ArrayList<Movie>) results.values);
            Log.e("LOG", "검색 결과 반환B");
            notifyDataSetChanged();
        }
    };
}
