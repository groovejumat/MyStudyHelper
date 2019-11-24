package org.techtown.samplerelativelayout;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private ArrayList<Todo> items;

    TodoAdapter(ArrayList<Todo> items) {
        this.items = items;
        Log.e("LOG", "아이템이 가득찬 상태일 때의 갯수 확인." + items.size());
//        itemsFull = new ArrayList<>(items);
//        Log.e("LOG", "아이템이 가득찬 상태일 때의 갯수 확인." + itemsFull.size());
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

    public class ViewHolder extends RecyclerView.ViewHolder{

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
        }
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.ViewHolder viewHolder, int position) {
        Todo item = items.get(position);
        viewHolder.docontent.setText(item.getTodo());
        viewHolder.checkbox.setChecked(item.isCheckbox());
    }
}
