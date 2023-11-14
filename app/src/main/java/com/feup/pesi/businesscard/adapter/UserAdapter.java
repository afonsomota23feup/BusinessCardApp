package com.feup.pesi.businesscard.adapter;

// ...

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.feup.pesi.businesscard.R;
import com.feup.pesi.businesscard.User;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(long userId); // Modifica o tipo do parâmetro
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);

        // Configurar o clique curto
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    User clickedUser = userList.get(adapterPosition);
                    onItemClickListener.onItemClick(clickedUser.getId());
                }
            }
        });

        // Configurar o clique longo
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION && onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(adapterPosition);
                }
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public User getUser(int position) {
        return userList.get(position);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewUserName;
        private TextView textViewUserEmail;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewUserEmail = itemView.findViewById(R.id.textViewUserEmail);
        }

        public void bind(User user) {
            textViewUserName.setText(user.getName());
            textViewUserEmail.setText(user.getEmailAddress());
        }
    }
}
