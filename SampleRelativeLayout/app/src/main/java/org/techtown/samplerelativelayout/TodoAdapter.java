package org.techtown.samplerelativelayout;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private ArrayList<Todo> items;
    private Context mContext;

    //1.컨텍스트 메뉴 구성해서 수정 기능 넣기.

    TodoAdapter(FragmentDate fragmentDate, ArrayList<Todo> items) {
        this.items = items;
        Log.e("LOG", "아이템이 가득찬 상태일 때의 갯수 확인." + items.size());
//        itemsFull = new ArrayList<>(items);
//        Log.e("LOG", "아이템이 가득찬 상태일 때의 갯수 확인." + itemsFull.size());
    }

    public TodoAdapter(Context context, ArrayList<Todo> list) {
        this.items = items;
        mContext = context;

    }


    @NonNull
    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_item_list, parent, false);
        TodoAdapter.ViewHolder viewHolder = new TodoAdapter.ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Todo> items) {
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView docontent;
        CheckBox checkbox;

        ViewHolder(View itemView) {
            super(itemView);

            docontent = itemView.findViewById(R.id.todo_list);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkBox);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        Log.v("태그", String.valueOf(pos)+"가 호출됌.");
                    }
                }
            });

            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = getAdapterPosition() ;
                    Log.e("LOG", "체크박스 리스너가 동작됌.");

                    Log.e("LOG", String.valueOf(isChecked));
                    items.get(pos).setCheckbox(isChecked);
                    Log.e("LOG", "해당 부분이 변경 되어진 것을 확인.");
                    Log.e("LOG", "상태 postion의 값 : " + pos + "체크박스의 상태 : " + items.get(pos).isCheckbox());
                    //notifyDataSetChanged();
                }
            });

        }
    }



    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.ViewHolder viewHolder, final int position) {


        final Todo item = items.get(position);

        Log.e("LOG", "바인드 뷰홀더 동작 발생" + item.isCheckbox() + item.getTodo());

        viewHolder.docontent.setText(item.getTodo());

        viewHolder.checkbox.setChecked(item.isCheckbox());

        //viewHolder.checkbox.setOnCheckedChangeListener(null);

//        viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Log.e("LOG", "체크박스 리스너가 동작됌.");
//
//                Log.e("LOG", String.valueOf(isChecked));
//                    item.setCheckbox(isChecked);
//                Log.e("LOG", "해당 부분이 변경 되어진 것을 확인.");
//                Log.e("LOG", "상태 postion의 값 : " + position + "체크박스의 상태 : " + item.isCheckbox());
//            }
//        });
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private TodoAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final TodoAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}
