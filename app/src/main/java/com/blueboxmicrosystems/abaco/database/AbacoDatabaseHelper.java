package com.blueboxmicrosystems.abaco.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MARAUJO on 11/4/2016.
 */

public class AbacoDatabaseHelper extends SQLiteOpenHelper {

    String sqlConfigurationCreate = "" +
            "CREATE TABLE main.configuration (\n" +
            "    id             INTEGER \n" +
            "                   UNIQUE\n" +
            "                   NOT NULL,\n" +
            "    sync    INTEGER DEFAULT 0,\n" +
            "    name   TEXT    UNIQUE\n" +
            "                   NOT NULL\n" +
            ");\n" +
            "";

    String sqlAccountCategoryCreate = "" +
            "CREATE TABLE main.account_category (\n" +
            "    id     INTEGER PRIMARY KEY AUTOINCREMENT\n" +
            "                   UNIQUE\n" +
            "                   NOT NULL,\n" +
            "    sync    INTEGER DEFAULT 0,\n" +
            "    name   TEXT    UNIQUE\n" +
            "                   NOT NULL\n" +
            ");\n" +
            "";

    String sqlAccountTypeCreate = "" +
            "CREATE TABLE main.account_type (\n" +
            "    id                  INTEGER PRIMARY KEY AUTOINCREMENT\n" +
            "                        UNIQUE\n" +
            "                        NOT NULL,\n" +
            "    sync         INTEGER DEFAULT 0,\n" +
            "    account_category_id INTEGER DEFAULT 0,\n" +
            "    name   TEXT    UNIQUE\n" +
            "                   NOT NULL\n" +
            ");\n" +
            "";

    String sqlAccountCreate = "" +
            "CREATE TABLE main.account (\n" +
            "    id               INTEGER PRIMARY KEY AUTOINCREMENT\n" +
            "                            UNIQUE,\n" +
            "    sync             INTEGER DEFAULT 0,\n" +
            "    account_type_id  INTEGER DEFAULT 0,\n" +
            "    name             TEXT    NOT NULL\n" +
            "                            DEFAULT ''\n" +
            "                            UNIQUE,\n" +
            "    description      TEXT,\n" +
            "    initial_balance  REAL    DEFAULT 0,\n" +
            "    amount_limit     REAL    DEFAULT 0,\n" +
            "    pay_day          INTEGER DEFAULT 0,\n" +
            "    expire_month     INTEGER DEFAULT 0,\n" +
            "    expire_year      INTEGER DEFAULT 0,\n" +
            "    currency_country TEXT,\n" +
            "    icon             TEXT,\n" +
            "    color            TEXT\n" +
            ");\n" +
            "";

    String sqlTransactionCategoryCreate = "" +
            "CREATE TABLE main.transaction_category (\n" +
            "    id                  INTEGER NOT NULL\n" +
            "                                PRIMARY KEY AUTOINCREMENT\n" +
            "                                UNIQUE,\n" +
            "    sync                INTEGER DEFAULT 0,\n" +
            "    transaction_type_id INTEGER NOT NULL\n" +
            "                                DEFAULT 0\n" +
            "                                REFERENCES transaction_type (id),\n" +
            "    name                TEXT    NOT NULL\n" +
            "                                DEFAULT ''\n" +
            "                                UNIQUE,\n" +
            "    description         TEXT    DEFAULT '',\n" +
            "    icon                INTEGER DEFAULT 0,\n" +
            "    color               INTEGER DEFAULT 0,\n" +
            "    system              INTEGER DEFAULT (0) \n" +
            ");" +
            "";

    String sqlTagCreate = "" +
            "CREATE TABLE main.tag (\n" +
            "    id                  INTEGER NOT NULL\n" +
            "                                PRIMARY KEY AUTOINCREMENT\n" +
            "                                UNIQUE,\n" +
            "    sync                INTEGER DEFAULT 0,\n" +
            "    name                TEXT    NOT NULL\n" +
            "                                UNIQUE,\n" +
            "    description         TEXT,\n" +
            "    icon                TEXT,\n" +
            "    color               INTEGER DEFAULT 0xfff06292\n" +
            ");\n" +
            "";

    String sqlTransactionDetailCreate = "" +
            "CREATE TABLE main.transaction_detail (\n" +
            "    id                       INTEGER PRIMARY KEY AUTOINCREMENT\n" +
            "                                  UNIQUE\n" +
            "                                  NOT NULL,\n" +
            "    sync                    INTEGER DEFAULT 0,\n" +
            "    transaction_header_id   INTEGER REFERENCES transaction_header (id),\n" +
            "    account_id              INTEGER REFERENCES account (id) \n" +
            "                            NOT NULL,\n" +
            "    transaction_category_id INTEGER NOT NULL\n" +
            "                            REFERENCES category (id),\n" +
            "    tag_id                  INTEGER,\n" +
            "    amount                  REAL  NOT NULL\n" +
            "                                  DEFAULT (0) \n" +
            ");" +
            "";

    String sqlTransactionHeaderCreate = "" +
            "CREATE TABLE main.transaction_header (\n" +
            "    id                  INTEGER PRIMARY KEY AUTOINCREMENT\n" +
            "                                UNIQUE\n" +
            "                                NOT NULL,\n" +
            "    sync                INTEGER DEFAULT 0,\n" +
            "    transaction_type_id INTEGER REFERENCES transaction_type (id) \n" +
            "                                NOT NULL,\n" +
            "    datetime            INTEGER DEFAULT (datetime('now') ),\n" +
            "    completed           INTEGER DEFAULT (0),\n" +
            "    note                TEXT,\n" +
            "    image               TEXT,\n" +
            "    location            TEXT\n" +
            ");\n" +
            "";

    String sqlTransactionTypeCreate = "" +
            "CREATE TABLE main.transaction_type (\n" +
            "    id             INTEGER PRIMARY KEY AUTOINCREMENT\n" +
            "                   UNIQUE\n" +
            "                   NOT NULL,\n" +
            "    sync           INTEGER DEFAULT 0,\n" +
            "    name   TEXT    UNIQUE\n" +
            "                   NOT NULL,\n" +
            "    system INTEGER DEFAULT (0) \n" +
            ");\n" +
            "";

    public AbacoDatabaseHelper(Context contexto, String nombre, CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlConfigurationCreate);
        db.execSQL(sqlAccountCategoryCreate);
        db.execSQL(sqlAccountTypeCreate);
        db.execSQL(sqlAccountCreate);
        db.execSQL(sqlTransactionCategoryCreate);
        db.execSQL(sqlTagCreate);
        db.execSQL(sqlTransactionDetailCreate);
        db.execSQL(sqlTransactionHeaderCreate);
        db.execSQL(sqlTransactionTypeCreate);

        this.defaultTableRecords(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //Se elimina la versión anterior de las tablas temporales
        db.execSQL("DROP TABLE IF EXISTS temp.configuration");
        db.execSQL("DROP TABLE IF EXISTS temp.account_category");
        db.execSQL("DROP TABLE IF EXISTS temp.account_type");
        db.execSQL("DROP TABLE IF EXISTS temp.account");
        db.execSQL("DROP TABLE IF EXISTS temp.transaction_category");
        db.execSQL("DROP TABLE IF EXISTS temp.tag");
        db.execSQL("DROP TABLE IF EXISTS temp.transaction_header");
        db.execSQL("DROP TABLE IF EXISTS temp.transaction_detail");
        db.execSQL("DROP TABLE IF EXISTS temp.transaction_type");

        // Se pasan los valores actuales de cada tabla a una nueva tabla temporal
        db.execSQL("CREATE TEMPORARY TABLE temp.configuration AS  SELECT * FROM main.configuration;");
        db.execSQL("CREATE TEMPORARY TABLE temp.account_category AS  SELECT * FROM main.account_category;");
        db.execSQL("CREATE TEMPORARY TABLE temp.account_type AS  SELECT * FROM main.account_type;");
        db.execSQL("CREATE TEMPORARY TABLE temp.account AS  SELECT * FROM main.account;");
        db.execSQL("CREATE TEMPORARY TABLE temp.transaction_category AS  SELECT * FROM main.transaction_category;");
        db.execSQL("CREATE TEMPORARY TABLE temp.tag AS  SELECT * FROM main.tag;");
        db.execSQL("CREATE TEMPORARY TABLE temp.transaction_header AS  SELECT * FROM main.transaction_header;");
        db.execSQL("CREATE TEMPORARY TABLE temp.transaction_detail AS  SELECT * FROM main.transaction_detail;");
        db.execSQL("CREATE TEMPORARY TABLE temp.transaction_type AS  SELECT * FROM main.transaction_type;");

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS main.configuration");
        db.execSQL("DROP TABLE IF EXISTS main.account_category");
        db.execSQL("DROP TABLE IF EXISTS main.account_type");
        db.execSQL("DROP TABLE IF EXISTS main.account");
        db.execSQL("DROP TABLE IF EXISTS main.transaction_category");
        db.execSQL("DROP TABLE IF EXISTS main.tag");
        db.execSQL("DROP TABLE IF EXISTS main.transaction_header");
        db.execSQL("DROP TABLE IF EXISTS main.transaction_detail");
        db.execSQL("DROP TABLE IF EXISTS main.transaction_type");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlConfigurationCreate);
        db.execSQL(sqlAccountCategoryCreate);
        db.execSQL(sqlAccountTypeCreate);
        db.execSQL(sqlAccountCreate);
        db.execSQL(sqlTransactionCategoryCreate);
        db.execSQL(sqlTagCreate);
        db.execSQL(sqlTransactionDetailCreate);
        db.execSQL(sqlTransactionHeaderCreate);
        db.execSQL(sqlTransactionTypeCreate);

        //Se pasan los valores de las tablas temporales a las nuevas tablas
        db.execSQL("insert into main.configuration " +
                "         (id,sync,name) " +
                "   select id,sync,name " +
                "     from temp.configuration;");
        db.execSQL("insert into main.account_category" +
                "         (id,sync,name) " +
                "   select id,sync,name " +
                "     from temp.account_category;");
        db.execSQL("insert into main.account_type" +
                "         (id,sync,account_category_id,name) " +
                "   select id,sync,account_category_id,name " +
                "     from temp.account_type;");
        db.execSQL("insert into main.account" +
                "          (id,sync,account_type_id,description,initial_balance,amount_limit,pay_day,expire_month,expire_year,currency_country,icon,color)" +
                "    select id,sync,account_type_id,description,initial_balance,amount_limit,pay_day,expire_month,expire_year,currency_country,icon,color " +
                "      from temp.account;");
        db.execSQL("insert into main.transaction_category" +
                "          (id,sync,transaction_type_id,name,description,icon,color,system)" +
                "    select id,sync,transaction_type_id,name,description,icon,color,system " +
                "      from temp.transaction_category;");
        db.execSQL("insert into main.tag" +
                "         (id,sync,name,description,icon,color) " +
                "   select id,sync,name,description,icon,color " +
                "     from temp.tag;");
        db.execSQL("insert into main.transaction_detail" +
                "         (id,sync,transaction_header_id,account_id,transaction_category_id,tag_id,amount) " +
                "   select id,sync,transaction_header_id,account_id,transaction_category_id,tag_id,amount " +
                "     from temp.transaction_detail;");
        db.execSQL("insert into main.transaction_header" +
                "         (id,sync,transaction_type_id,datetime,completed,note,image,location) " +
                "   select id,sync,transaction_type_id,datetime,completed,note,image,location " +
                "     from temp.transaction_header;");
        db.execSQL("insert into main.transaction_type" +
                "         (id,sync,name,system) " +
                "   select id,sync,name,system " +
                "     from temp.transaction_type;");

    }

    private void defaultTableRecords(SQLiteDatabase db){
        db.execSQL("insert into configuration (id,name) values (1,'');");

        db.execSQL("insert into account_category (id,name) values (1,'DEB');");
        db.execSQL("insert into account_category (id,name) values (2,'CREDIT');");

        db.execSQL("insert into account_type (id,name,account_category_id) values (1,'WALLET',1);");
        db.execSQL("insert into account_type (id,name,account_category_id) values (2,'SAVING',1);");
        db.execSQL("insert into account_type (id,name,account_category_id) values (3,'LOAN',2);");
        db.execSQL("insert into account_type (id,name,account_category_id) values (4,'CREDIT CARD',2);");

        db.execSQL("insert into transaction_type (id,name,system) values (1,'ACCOUNT TRANSFER',1);");
        db.execSQL("insert into transaction_type (id,name,system) values (2,'INCOME',0);");
        db.execSQL("insert into transaction_type (id,name,system) values (3,'EXPENSE',0);");
    }
}
