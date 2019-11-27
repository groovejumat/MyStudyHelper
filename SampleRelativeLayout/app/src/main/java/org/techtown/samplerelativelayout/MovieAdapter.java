package org.techtown.samplerelativelayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> implements Filterable  {

    private ArrayList<Movie> items;
    private ArrayList<Movie> itemsFull;
    Context mcontext;

    //필터 사용을 위해 추가된 메서드
    MovieAdapter(ArrayList<Movie> items) {
        this.items = items;
        //여기서 값을 못넣는다.//
        Log.e("LOG", "아이템이 가득찬 상태일 때의 갯수 확인." + items.size());
        itemsFull = new ArrayList<>(items);
        Log.e("LOG", "아이템이 가득찬 상태일 때의 갯수 확인." + itemsFull.size());
    }

    public void SearchDataReSet (ArrayList<Movie> items){
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

//            int pos = getAdapterPosition() ;
//            if (items.get(pos).getUrl().isEmpty()) {
//                ivMovie.setVisibility(View.GONE);
//            }

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


//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    //////액션바에서 내용 추가하기 기능 생성//////
//                    //Toast.makeText(MovieAd, "추가버튼을 클릭", Toast.LENGTH_SHORT).show();
//                    //커스텀 다이얼로그 생성 후 띄우기//
//                    final Dialog dialog2 = new Dialog(mcontext);
//                    dialog2.setContentView(R.layout.postdialog);
//                    final EditText text1 = (EditText) dialog2.findViewById(R.id.image_url);
//                    final EditText text2 = (EditText) dialog2.findViewById(R.id.post_name);
//                    final EditText text3 = (EditText) dialog2.findViewById(R.id.url_link);
//
//                    Button button1 = (Button) dialog2.findViewById(R.id.cancel);
//                    button1.setOnClickListener(new View.OnClickListener(){
//                        @Override
//                        public void onClick(View view) {
//
//                            dialog2.dismiss();   //다이얼로그를 닫는 메소드입니다.
//                        }
//                    });
//
//                    Button button2 = (Button) dialog2.findViewById(R.id.ok);
//                    button2.setOnClickListener(new View.OnClickListener(){
//                        @Override
//                        public void onClick(View view) {
//                            String image_url = text1.getText().toString();
//                            String post_name = text2.getText().toString();
//                            String url_link = text3.getText().toString();
//                            if(url_link.contains("https://")) {
//                                Movie m = new Movie(image_url, "의미없음", post_name, url_link);
//                                items.add(m);
//                                //참조용 데이터 리스트도 변경됌.
//                                //adapter.SearchDataReSet(items);
//
//                                dialog2.dismiss();
//                            }//등록 작업을 진행 해줍니다.
//                            else {
//                                //Toast.makeText(MovieAdapter.this, "url 양식이 맞지않습니다. 다시 확인해 주세요.", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//
//                    dialog2.show();
//                    return true;
//                }
//            });
        }
    }


    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder viewHolder, int position) {

        Movie item = items.get(position);
//        Glide.with(viewHolder.itemView.getContext())
//                .load(item.getUrl())
//                .into(viewHolder.ivMovie);

        if (item.getUrl().isEmpty()) {
            Glide.with(viewHolder.itemView.getContext())
                    .load(item.getUrl()).asBitmap()
                    .into(viewHolder.ivMovie);
            Log.e("LOG", position + "   뷰홀더에 바인딩 될 이미지의 링크 확인하세요" + item.getUrl());
            viewHolder.ivMovie.setVisibility(View.GONE);
        }
        else {
            Glide.with(viewHolder.itemView.getContext())
                    .load(item.getUrl()).asBitmap()
                    .into(viewHolder.ivMovie);
            Log.e("LOG", position + "   뷰홀더에 바인딩 될 이미지의 링크 확인하세요" + item.getUrl());
            //viewHolder.ivMovie.setVisibility(View.GONE);
        }

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
