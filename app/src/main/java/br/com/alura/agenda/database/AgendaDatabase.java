package br.com.alura.agenda.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import br.com.alura.agenda.database.converter.ConversorCalendar;
import br.com.alura.agenda.database.dao.AlunoDAO;
import br.com.alura.agenda.model.Aluno;

import static br.com.alura.agenda.database.AgendaMigrations.ALL_MIGRATIONS;

@Database(entities = {Aluno.class},version = 5,exportSchema = false)
@TypeConverters({ConversorCalendar.class})
public abstract class AgendaDatabase extends RoomDatabase {

    public static final String AGENDA_DB = "agenda.db";


    public abstract AlunoDAO getRoomAlunoDAO();

    public static AgendaDatabase getInstance(Context context){
        return Room.databaseBuilder(context, AgendaDatabase.class, AGENDA_DB)
                .allowMainThreadQueries()
                .addMigrations(ALL_MIGRATIONS)
                .build();
    }

}
