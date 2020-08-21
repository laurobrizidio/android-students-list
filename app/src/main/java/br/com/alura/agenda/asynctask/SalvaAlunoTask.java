package br.com.alura.agenda.asynctask;

import br.com.alura.agenda.database.dao.AlunoDAO;
import br.com.alura.agenda.database.dao.TelefoneDAO;
import br.com.alura.agenda.model.Aluno;
import br.com.alura.agenda.model.Telefone;

public class SalvaAlunoTask extends BaseAlunoComTelefoneTask{
    private final AlunoDAO dao;
    private final Aluno aluno;
    private final Telefone telefoneFixo;
    private final Telefone telefoneCelular;
    private final TelefoneDAO telefoneDAO;

    public SalvaAlunoTask(Telefone telefoneFixo,
                          Telefone telefoneCelular,
                          AlunoDAO dao,
                          TelefoneDAO telefoneDAO,
                          Aluno aluno,
                          FinalizadaListener listener) {
        super(listener);
        this.dao = dao;
        this.aluno = aluno;
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.telefoneDAO = telefoneDAO;

    }


    @Override
    protected Void doInBackground(Void... voids) {
        int alunoId = dao.salva(aluno).intValue();
        vinculaAlunoComTelefone(alunoId,telefoneFixo,telefoneCelular);
        telefoneDAO.salva(telefoneFixo,telefoneCelular);
        return null;
    }

}
