package hu.ait.android.keely.migrainetracker.Data;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Keely on 5/6/15.
 */
public class Migraine extends SugarRecord<Migraine> implements Serializable {

    /*Store the Migraine list using Sugar ORM*/

    public Migraine(){

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDur() {
        return dur;
    }

    public void setDur(String dur) {
        this.dur = dur;
    }

    private Date date;

    private String desc;
    private String dur;

    public Migraine(String desc, String dur, Date date){
        this.date=date;

        this.desc=desc;
        this.dur=dur;
    }

    

}
