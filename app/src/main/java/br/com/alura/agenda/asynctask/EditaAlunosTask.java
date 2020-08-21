package br.com.alura.agenda.asynctask;

import java.util.List;

import br.com.alura.agenda.database.dao.AlunoDAO;
import br.com.alura.agenda.database.dao.TelefoneDAO;
import br.com.alura.agenda.model.Aluno;
import br.com.alura.agenda.model.Telefone;
import br.com.alura.agenda.model.TipoTelefone;

public class EditaAlunosTask extends BaseAlunoComTelefoneTask{
    private final AlunoDAO dao;
    private final TelefoneDAO telefoneDAO;
    private final Telefone telefoneFixo;
    private final Telefone telefoneCelular;
    private final Aluno aluno;
    private final List<Telefone> todosTelefonesDoAluno;

    public EditaAlunosTask(AlunoDAO dao,
                           TelefoneDAO telefoneDAO,
                           Telefone telefoneFixo,
                           Telefone telefoneCelular,
                           Aluno aluno,
                           List<Telefone> todosTelefonesDoAluno,
                           FinalizadaListener listener) {
        super(listener);
        this.dao = dao;
        this.telefoneDAO = telefoneDAO;
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.aluno = aluno;
        this.todosTelefonesDoAluno = todosTelefonesDoAluno;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        dao.edita(aluno);
        atualizaIds(telefoneFixo, telefoneCelular);
        telefoneDAO.atualiza(telefoneFixo,telefoneCelular);
        return null;
    }

    private void atualizaIds(Telefone telefoneFixo, Telefone telefoneCelular) {

        vinculaAlunoComTelefone(aluno.getId(), telefoneCelular,telefoneFixo);

        for (Telefone telefone :
                todosTelefonesDoAluno) {
            if(telefone.getTipoTelefone() == TipoTelefone.FIXO) {
                telefoneFixo.setId(telefone.getId());
            }else{
                telefoneCelular.setId(telefone.getId());
            }
        }
    }


}
