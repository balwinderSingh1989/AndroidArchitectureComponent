package er.arch.design.bluerprint.data.database;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;

import er.arch.design.bluerprint.data.dao.UserDao;
import er.arch.design.bluerprint.data.model.User;

/**
 * Created by Balwinder singh on 5/1/2018.
 */

@Database(entities  ={User.class},version = 1,exportSchema = false)
public abstract class GitHubDatabase extends RoomDatabase {
    @NonNull
    abstract public UserDao userDao();
}

