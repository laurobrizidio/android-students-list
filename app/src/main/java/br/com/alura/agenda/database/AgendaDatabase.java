package br.com.alura.agenda.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import br.com.alura.agenda.database.converter.ConversorCalendar;
import br.com.alura.agenda.database.converter.ConversorTipoTelefone;
import br.com.alura.agenda.database.dao.AlunoDAO;
import br.com.alura.agenda.database.dao.TelefoneDAO;
import br.com.alura.agenda.model.Aluno;
import br.com.alura.agenda.model.Telefone;

import static br.com.alura.agenda.database.AgendaMigrations.ALL_MIGRATIONS;

@Database(entities = {Aluno.class, Telefone.class},version = 6,exportSchema = false)
@TypeConverters({ConversorCalendar.class, ConversorTipoTelefone.class})
public abstract class AgendaDatabase extends RoomDatabase {


    public abstract AlunoDAO getRoomAlunoDAO();
    public abstract TelefoneDAO getTelefoneDAO();
    public static final String AGENDA_DB = "agenda.db";

    public static AgendaDatabase getInstance(Context context){

        return Room.databaseBuilder(context, AgendaDatabase.class, AGENDA_DB)
//                .allowMainThreadQueries()
                .addMigrations(ALL_MIGRATIONS)
                .build();
    }
}
