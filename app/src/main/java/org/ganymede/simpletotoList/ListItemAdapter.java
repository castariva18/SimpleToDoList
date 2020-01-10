package org.ganymede.simpletotoList;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ganymede.simpletotoList.Model.ToDo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    ItemClickListener itemClickListener;
    TextView itemTitle, itemDescription, itemStatus, itemTimeline;

    public ListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

        itemTitle = (TextView) itemView.findViewById(R.id.item_title);
        itemDescription = (TextView) itemView.findViewById(R.id.item_description);
        itemStatus = (TextView) itemView.findViewById(R.id.item_status);
        itemTimeline = (TextView) itemView.findViewById(R.id.item_timeline);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select action");
        contextMenu.add(0, 0, getAdapterPosition(), "DELETE");

    }
}

public class ListItemAdapter extends RecyclerView.Adapter<ListItemViewHolder>{
    MainTodoActivity mainTodoActivity;
    List<ToDo> todoList;

    public ListItemAdapter(MainTodoActivity mainTodoActivity, List<ToDo> todoList) {
        this.mainTodoActivity = mainTodoActivity;
        this.todoList = todoList;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mainTodoActivity.getBaseContext());
        View view = inflater.inflate(R.layout.list_item,parent,false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {
        holder.itemTitle.setText(todoList.get(position).getTitle());
        holder.itemDescription.setText(todoList.get(position).getDescription());
        holder.itemTimeline.setText(todoList.get(position).getTimeline());
        holder.itemStatus.setText(todoList.get(position).getStatus());

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
