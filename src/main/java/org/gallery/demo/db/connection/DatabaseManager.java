package org.gallery.demo.db.connection;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlite3.SQLitePlugin;

public class DatabaseManager {

    private static Jdbi jdbi;

    public DatabaseManager() {
        jdbi = Jdbi.create("jdbc:sqlite:database")
                .installPlugin(new SQLitePlugin());
    }

    public Jdbi getJdbiConnection() {
        return jdbi;
    }

}
