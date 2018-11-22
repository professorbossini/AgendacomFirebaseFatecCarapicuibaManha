package br.com.bossini.agendacomfirebasefateccarapicuibamanha;

import com.google.firebase.database.Exclude;

public class Contato {
    private String id;
    private String nome, fone, email;

    public Contato (){

    }

    public Contato (String id, String nome, String fone, String email){
        this(nome, fone, email);
        setId(id);
    }

    public Contato (String nome, String fone, String email){
        setNome(nome);
        setFone(fone);
        setEmail(email);
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Contato{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", fone='" + fone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
