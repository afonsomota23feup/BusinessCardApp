package com.feup.pesi.businesscard;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.feup.pesi.businesscard.activity.AddUserActivity;
import com.feup.pesi.businesscard.activity.BusinessCardActivity;
import com.feup.pesi.businesscard.activity.UserDetailsActivity;
import com.feup.pesi.businesscard.adapter.UserAdapter;
import com.feup.pesi.businesscard.helper.DBHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUsers;
    private UserAdapter userAdapter;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);

        // Busca os usuários da base de dados
        ArrayList<User> userList = (ArrayList<User>) dbHelper.getAllUsers();

        userAdapter = new UserAdapter(userList);
        recyclerViewUsers.setAdapter(userAdapter);

        userAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(long userId) {
                Intent intent = new Intent(MainActivity.this, UserDetailsActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        userAdapter.setOnItemLongClickListener(new UserAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                // Handle item long click
                showPopupMenu(recyclerViewUsers, position);
            }
        });

        Button btAdicionarUsuario = findViewById(R.id.btnAddUser);
        btAdicionarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Adicionar Usuários", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AddUserActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showPopupMenu(View view, final int position) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.menu_user_options);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(android.view.MenuItem item) {
                switch (item.getItemId()) {

                    case 1:
                        // Opção para editar o usuário
                        editUser(position);
                        return true;
                    case 2:
                        // Opção para visualizar o e-business card
                        viewBusinessCard(position);
                        return true;
                    default:
                        return false;
                }
            }
        });

        popupMenu.show();
    }

    private void editUser(int position) {
        // Implemente a lógica para editar o usuário
        Toast.makeText(MainActivity.this, "Editar usuário: " + userAdapter.getUser(position).getName(), Toast.LENGTH_SHORT).show();
    }

    private void viewBusinessCard(int position) {
        // Implemente a lógica para visualizar o e-business card
        Toast.makeText(MainActivity.this, "Ver Business Card de: " + userAdapter.getUser(position).getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, BusinessCardActivity.class);
        startActivity(intent);
    }
}