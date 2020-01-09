package org.ganymede.simpletotoList;


import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    ItemClickListener itemClickListener;
    TextView item_title, item_description, item_timeline, item_status;

    public ListItemViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

        item_title = (TextView)itemView.findViewById(R.id.item_title);
        item_description = (TextView)itemView.findViewById(R.id.item_description);
        item_timeline = (TextView)itemView.findViewById(R.id.item_timeline);
        item_status = (TextView) itemView.findViewById(R.id.item_status);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(),"DELETE");

    }
}

public class  ListItemAdapter extends RecyclerView.Adapter<ListItemViewHolder> {
   MainTodoActivity mainTodoActivity;
   List<ToDo> todoList;
    private ListItemViewHolder holder;

    public ListItemAdapter(MainTodoActivity mainTodoActivity, List<ToDo> todoList) {
        this.mainTodoActivity = mainTodoActivity;
        this.todoList = todoList;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mainTodoActivity.getBaseContext());
        View view = inflater.inflate(R.layout.list_item,parent,false);

        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {

        holder.item_title.setText(todoList.get(position).getId());
        holder.item_description.setText(todoList.get(position).getDescription());
        holder.item_timeline.setText(todoList.get(position).getTimeline());
        holder.item_status.setText(todoList.get(position).getStatus());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                //when select item, data will auto set edit text view
                mainTodoActivity.title.setText(todoList.get(position).getTitle());
                mainTodoActivity.description.setText(todoList.get(position).getDescription());
                mainTodoActivity.timeline.setText(todoList.get(position).getTimeline());
                mainTodoActivity.status.setText(todoList.get(position).getStatus());


                mainTodoActivity.isUpdate=true; //setflag is update = true
                mainTodoActivity.idUpdate = todoList.get(position).getId();

                //set for item

            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }
}
