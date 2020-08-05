package br.com.alura.agenda.database.converter;

import androidx.room.TypeConverter;

import java.util.Calendar;


public class ConversorCalendar {

    @TypeConverter
    public Long toLong(Calendar calendar){
        return calendar.getTimeInMillis();
    }

    @TypeConverter
    public Calendar toCalendar(Long longNumber){
        Calendar moment = Calendar.getInstance();
        if(longNumber != null){
            moment.setTimeInMillis(longNumber);
        }

        return moment;
    }

}
