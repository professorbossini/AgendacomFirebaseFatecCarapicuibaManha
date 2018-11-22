package br.com.bossini.agendacomfirebasefateccarapicuibamanha;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaDeContatosActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference contatosReference;

    private void configuraFirebase (){
        firebaseDatabase = FirebaseDatabase.getInstance();
        contatosReference = firebaseDatabase.getReference("contatos");
    }

    private ListView contatosListView;
    private ArrayAdapter <Contato> contatosAdapter;
    private List <Contato> contatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_contatos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contatosListView = (ListView) findViewById(R.id.contatosListView);
        contatos = new ArrayList<>();
        contatosAdapter = new ArrayAdapter<Contato>(this,
                                    android.R.layout.simple_list_item_1, contatos);
        contatosListView.setAdapter(contatosAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaDeContatosActivity.this,
                                        AdicionaContatoActivity.class);
                startActivity(intent);
            }
        });
        configuraFirebase();
        configuraLongClickListener();
    }

    private void configuraLongClickListener(){
        contatosListView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder apagarAtualizarDialogoBuilder =
                        new AlertDialog.Builder(ListaDeContatosActivity.this);
                apagarAtualizarDialogoBuilder.
                        setNegativeButton(R.string.deletar_contato, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //apagar o cara que sofreu o clique longo
                                Contato contato = contatos.get(position);
                                String id = contato.getId();
                                contatosReference.child(id).removeValue();
                                Toast.makeText(ListaDeContatosActivity.this,
                                        getString(R.string.contato_removido),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).setPositiveButton(R.string.atualizar_contato,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //atualizar o cara que sofreu o clique longo
                                final Contato contato = contatos.get(position);
                                AlertDialog.Builder atualizarBuilder =
                                        new AlertDialog.Builder(ListaDeContatosActivity.this);
                                LayoutInflater inflater = LayoutInflater.from(ListaDeContatosActivity.this);
                                View view = inflater.inflate(R.layout.activity_adiciona_contato, null);
                                atualizarBuilder.setView(view);
                                final AlertDialog atualizarDialogo = atualizarBuilder.create();
                                final EditText nomeEditText = view.findViewById(R.id.nomeEditText);
                                nomeEditText.setText(contato.getNome());
                                final EditText foneEditText = view.findViewById(R.id.foneEditText);
                                foneEditText.setText(contato.getFone());
                                final EditText emailEditText = view.findViewById(R.id.emailEditText);
                                emailEditText.setText(contato.getEmail());
                                FloatingActionButton floatingActionButton =
                                        view.findViewById(R.id.fab);

                                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String nome =
                                                nomeEditText.getEditableText().toString();
                                        String fone =
                                                foneEditText.getEditableText().toString();
                                        String email =
                                                emailEditText.getEditableText().toString();
                                        String id = contato.getId();
                                        Contato atualizarContato = new Contato(id, nome, fone, email);
                                        contatosReference.child(id).setValue(atualizarContato);
                                        Toast.makeText(ListaDeContatosActivity.this,
                                                getString(R.string.atualizar_contato),
                                                Toast.LENGTH_SHORT).show();
                                        atualizarDialogo.cancel();

                                    }
                                });

                                atualizarDialogo.show();
                            }
                        });
                apagarAtualizarDialogoBuilder.create().show();
                return false;
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        contatosReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contatos.clear();
                for (DataSnapshot filho : dataSnapshot.getChildren()){
                    Contato contato = filho.getValue(Contato.class);
                    contato.setId(filho.getKey());
                    contatos.add(contato);
                }
                contatosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ListaDeContatosActivity.this,
                        getString(R.string.erro_firebase),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
