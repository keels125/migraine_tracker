package hu.ait.android.keely.migrainetracker.Data;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * Store the food list using Sugar ORM
 */
public class Food extends SugarRecord<Food> implements Serializable {



    public Food() {

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

    private Date date;

    private String desc;

    public Food(String desc, Date date) {
        this.desc = desc;
        this.date = date;
    }
}
