package com.example.zbyszek.stackmoney2.sql

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.zbyszek.stackmoney2.model.*
import com.example.zbyszek.stackmoney2.model.account.AccountSQL
import com.example.zbyszek.stackmoney2.model.account.CategorySQL
import com.example.zbyszek.stackmoney2.sql.dao.*

@Database(
        entities = arrayOf(
                Icon::class,
                Color::class,

                User::class,
                Question::class,

                AccountSQL::class,
                CategorySQL::class,
                Operation::class,
                OperationPattern::class
        ),
        version = 3
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun iconDAO() : IconDAO
    abstract fun colorDAO() : ColorDAO

    abstract fun userDAO() : UserDAO
    abstract fun questionDAO() : QuestionDAO

    abstract fun accountDAO() : AccountDAO
    abstract fun categoryDAO() : CategoryDAO
    abstract fun operationDAO() : OperationDAO
    abstract fun operationPatternDAO() : OperationPatternDAO

    companion object {
        private var INSTANCE: AppDatabase? = null
            private set

        fun getInMemoryDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, "database.db")
                        .addCallback(CALLBACK)
                        .build()
            }

            return INSTANCE!!
        }

        private val CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                db.execSQL(
                   "CREATE TRIGGER validate_parent_category_before_insert_category " +
                        "BEFORE INSERT ON categories " +
                        "BEGIN " +
                                "SELECT " +
                                "CASE " +
                                "WHEN NEW.parent_category_id IS NULL " +
                                     "AND LOWER(TRIM(NEW.name)) IN (SELECT LOWER(TRIM(name)) " +
                                                                   "FROM categories " +
                                                                   "WHERE parent_category_id IS NULL " +
                                                                         "AND user_id = NEW.user_id) " +
                                "THEN RAISE " +
                                    "(ABORT, 'Category with that name already exists') " +
                                "WHEN NEW.parent_category_id IS NOT NULL " +
                                     "AND LOWER(TRIM(NEW.name)) IN (SELECT LOWER(TRIM(name)) " +
                                                                   "FROM categories " +
                                                                   "WHERE parent_category_id " +
                                                                   "== NEW.parent_category_id " +
                                                                   "AND user_id = NEW.user_id) " +
                                "THEN RAISE " +
                                    "(ABORT, 'Subcategory with that name already exists') " +
                                "END; " +
                         "END;")

                db.execSQL("CREATE TRIGGER validate_parent_account_before_insert_account BEFORE INSERT ON accounts BEGIN SELECT CASE WHEN NEW.parent_account_id IS NULL AND LOWER(TRIM(NEW.name)) IN (SELECT LOWER(TRIM(name)) FROM accounts WHERE parent_account_id IS NULL AND user_id = NEW.user_id) THEN RAISE (ABORT, 'Account with that name already exists') WHEN NEW.parent_account_id IS NOT NULL AND LOWER(TRIM(NEW.name)) IN (SELECT LOWER(TRIM(name)) FROM accounts WHERE parent_account_id == NEW.parent_account_id AND user_id = NEW.user_id) THEN RAISE (ABORT, 'Subaccount with that name already exists') END; END;")
            }
        }
    }
}