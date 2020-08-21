package br.com.alura.agenda.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.List;

import br.com.alura.agenda.R;
import br.com.alura.agenda.asynctask.BuscaTodosTelefonesDoAluno;
import br.com.alura.agenda.asynctask.EditaAlunosTask;
import br.com.alura.agenda.asynctask.SalvaAlunoTask;
import br.com.alura.agenda.database.AgendaDatabase;
import br.com.alura.agenda.database.dao.AlunoDAO;
import br.com.alura.agenda.database.dao.TelefoneDAO;
import br.com.alura.agenda.model.Aluno;
import br.com.alura.agenda.model.Telefone;
import br.com.alura.agenda.model.TipoTelefone;

import static br.com.alura.agenda.ui.activity.ConstantesActivities.CHAVE_ALUNO;

public class FormularioAlunoActivity extends AppCompatActivity {

    private static final String TITULO_APPBAR_NOVO_ALUNO = "Novo aluno";
    private static final String TITULO_APPBAR_EDITA_ALUNO = "Edita aluno";
    private EditText campoNome;
    private EditText campoTelefoneCelular;
    private EditText campoTelefoneFixo;
    private EditText campoEmail;
    private AlunoDAO dao;
    private Aluno aluno;
    private TelefoneDAO telefoneDAO;
    private List<Telefone> todosTelefonesDoAluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_aluno);
        AgendaDatabase database = AgendaDatabase.getInstance(this);
        dao = database.getRoomAlunoDAO();
        telefoneDAO = database.getTelefoneDAO();
        inicializacaoDosCampos();
        carregaAluno();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater()
                .inflate(R.menu.activity_formulario_aluno_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.activity_formulario_aluno_menu_salvar){
            finalizaFormulario();
        }
        return super.onOptionsItemSelected(item);
    }

    private void carregaAluno() {
        Intent dados = getIntent();
        if (dados.hasExtra(CHAVE_ALUNO)) {
            setTitle(TITULO_APPBAR_EDITA_ALUNO);
            aluno = (Aluno) dados.getSerializableExtra(CHAVE_ALUNO);
            preencheCampos();
        } else {
            setTitle(TITULO_APPBAR_NOVO_ALUNO);
            aluno = new Aluno();
        }
    }

    private void preencheCampos() {
        campoNome.setText(aluno.getNome());
        campoEmail.setText(aluno.getEmail());

        preencheCamposTelefone();
    }

    private void preencheCamposTelefone() {
        new BuscaTodosTelefonesDoAluno(telefoneDAO, aluno, telefones ->
        {
            todosTelefonesDoAluno = telefones;
            for (Telefone telefone: todosTelefonesDoAluno){
                if(telefone.getTipoTelefone() == TipoTelefone.FIXO){
                    campoTelefoneFixo.setText(telefone.getNumero());
                }else{
                    campoTelefoneCelular.setText(telefone.getNumero());
                }
            }
        })
        .execute();



    }

    private void finalizaFormulario() {
        preencheAluno();
        Telefone telefoneFixo = getTelefone(campoTelefoneFixo, TipoTelefone.FIXO);
        Telefone telefoneCelular = getTelefone(campoTelefoneCelular, TipoTelefone.CELULAR);

        if (aluno.temIdValido()) {
            editAluno(telefoneFixo, telefoneCelular);

        } else {
            saveAluno(telefoneFixo, telefoneCelular);
        }
    }



    private void saveAluno(Telefone telefoneFixo, Telefone telefoneCelular) {
        new SalvaAlunoTask(telefoneFixo,
                telefoneCelular,
                dao,
                telefoneDAO,
                aluno, this::finish)
        .execute();

    }

    private void editAluno(Telefone telefoneFixo, Telefone telefoneCelular) {
        new EditaAlunosTask(dao,
                telefoneDAO,
                telefoneFixo,
                telefoneCelular,
                aluno,
                todosTelefonesDoAluno,
                this::finish)
        .execute();

    }



    private Telefone getTelefone(EditText campoTelefone, TipoTelefone tipo) {
        String numeroFixo = campoTelefone.getText().toString();
        return new Telefone(numeroFixo, tipo);
    }

    private void inicializacaoDosCampos() {
        campoNome = findViewById(R.id.activity_formulario_aluno_nome);
        campoTelefoneCelular = findViewById(R.id.activity_formulario_aluno_telefone_celular);
        campoTelefoneFixo = findViewById(R.id.activity_formulario_aluno_telefone_fixo);
        campoEmail = findViewById(R.id.activity_formulario_aluno_email);
    }

    private void preencheAluno() {
        String nome = campoNome.getText().toString();
        String email = campoEmail.getText().toString();
        aluno.setNome(nome);
        aluno.setEmail(email);
    }
}
