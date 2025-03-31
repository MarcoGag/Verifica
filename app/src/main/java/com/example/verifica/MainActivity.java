package com.example.verifica;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText editTextAttivita;
    Button btnRegistra;
    ListView listAttivita;
    SharedPreferences sharedPreferences;
    ArrayList<String> listaAtt;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextAttivita = findViewById(R.id.editTextAttivita);
        btnRegistra = findViewById(R.id.buttonRegistra);
        listAttivita = findViewById(R.id.ListAttivita);

        sharedPreferences =getSharedPreferences("listaAtt", Context.MODE_PRIVATE);
        listaAtt = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaAtt);
        listAttivita.setAdapter(adapter);

        listaAtt = caricaAtt();

        btnRegistra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = String.valueOf(editTextAttivita.getText());
                if(!str.isEmpty()){
                    listaAtt.add(str);
                    adapter.notifyDataSetChanged();
                    save();
                    editTextAttivita.setText("");
                }else{
                    Toast.makeText(MainActivity.this, "inserisci input", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listAttivita.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listaAtt.remove(position);
                adapter.notifyDataSetChanged();
                save();
                Toast.makeText(MainActivity.this,"attività rimossa", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void save(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder string = new StringBuilder();
        for (String str : listaAtt) {
            string.append(str).append(";");
        }
        editor.putString("attività", string.toString());
        editor.apply();
    }

    public ArrayList<String> caricaAtt(){
        String strAtt = sharedPreferences.getString("attività","");
        ArrayList<String> lista = new ArrayList<>();
        if(!strAtt.isEmpty()){
            String[] attivitArray = strAtt.split(";");
            for(String att : attivitArray){
                lista.add(att);
            }
        }
        return lista;
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("lista", listaAtt);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        listaAtt.clear();
        listaAtt.addAll(savedInstanceState.getStringArrayList("lista"));
        adapter.notifyDataSetChanged();
    }
}