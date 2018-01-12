package com.example.zbyszek.stackmoney2

import org.junit.Test

import org.junit.Assert.*
import com.example.zbyszek.stackmoney2.model.User
import org.junit.After
import android.arch.persistence.room.Room
import android.media.VolumeShaper
import android.support.test.InstrumentationRegistry
import org.junit.Before
import android.support.test.runner.AndroidJUnit4
import com.example.zbyszek.stackmoney2.helpers.HashUtils
import com.example.zbyszek.stackmoney2.model.account.AccountSQL
import com.example.zbyszek.stackmoney2.model.category.CategorySQL
import com.example.zbyszek.stackmoney2.model.operation.Operation
import com.example.zbyszek.stackmoney2.sql.AppDatabase
import com.example.zbyszek.stackmoney2.sql.dao.AccountDAO
import com.example.zbyszek.stackmoney2.sql.dao.CategoryDAO
import com.example.zbyszek.stackmoney2.sql.dao.OperationDAO
import com.example.zbyszek.stackmoney2.sql.dao.UserDAO
import org.hamcrest.CoreMatchers.equalTo
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import java.io.IOException


///**
// * Example local unit test, which will execute on the development machine (host).
// *
// * See [testing documentation](http://d.android.com/tools/testing).
// */
//class ExampleUnitTest {
//    @Test
//    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)
//    }
//}
@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private var mUserDao: UserDAO? = null
    private var mCategoryDao: CategoryDAO? = null
    private var mAccountDao: AccountDAO? = null
    private var mOperationDao: OperationDAO? = null
    private var mDb: AppDatabase? = null

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        mUserDao = mDb!!.userDAO()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        mDb!!.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        val user = User("roomTests", HashUtils.sha256("roomPassword"))
        mUserDao!!.insertUser(user)
        val byName = mUserDao!!.getUser("roomTests", HashUtils.sha256("roomPassword"))
        byName!!.id = 0
        assertEquals(byName, user)
    }

    @Test
    @Throws(Exception::class)
    fun writeCategory() {
        val categoryName = "TestCategory"
        val category = CategorySQL(1, 5, 5, null, true, true, categoryName)
        mCategoryDao!!.insertCategorySQL(category)
        val byNames = mCategoryDao!!.getAllUserCategoriesSQL(1)
        val categoryByName = byNames.firstOrNull { it.name == categoryName }
        categoryByName!!.id = 0
        assertEquals(categoryByName, category)
    }

    @Test
    @Throws(Exception::class)
    fun writeAccount() {
        val accountName = "TestAccount"
        val account = AccountSQL(1, 5, null, accountName)
        mAccountDao!!.insertAccountSQL(account)
        val byNames = mAccountDao!!.getAllUserAccountsSQL(1)
        val accountByNames = byNames.firstOrNull { it.name == accountName }
        accountByNames!!.id = 0
        assertEquals(accountByNames, account)
    }

    @Test
    @Throws(Exception::class)
    fun writeOperation() {
        val operationTitle = "TestOperation"
        val operation = Operation(1, 1, 1, operationTitle, 20, true, true, "Desc", "2017-08-08")
        mOperationDao!!.insertOperation(operation)
        val byNames = mOperationDao!!.getAllUserOperations(1)
        val operationByName = byNames.firstOrNull { it.title == operationTitle }
        operationByName!!.id = 0
        assertEquals(operationByName, operation)
    }

    @Test
    @Throws(Exception::class)
    fun checkIfAnyColors() {
        val colors = mDb!!.colorDAO().getAllColors()
        assertTrue(colors.count() > 0)
    }

    @Test
    @Throws(Exception::class)
    fun checkIfAnyIcons() {
        val icons = mDb!!.iconDAO().getAllIcons()
        assertTrue(icons.count() > 0)
    }

    @Test
    @Throws(Exception::class)
    fun writeCategoryCopy() {
        val accountName = "TestAccount"
        val account = AccountSQL(1, 5, null, accountName)
        try {
            mAccountDao!!.insertAccountSQL(account)
            mAccountDao!!.insertAccountSQL(account)
        } catch (e: Exception){
            assert(true)
        } finally {
            assert(false)
        }
    }

    @Test
    @Throws(Exception::class)
    fun writeAccountCopy() {
        val categoryName = "TestAccount"
        val category = CategorySQL(1, 5, 5, null, true, true, categoryName)
        try {
            mCategoryDao!!.insertCategorySQL(category)
            mCategoryDao!!.insertCategorySQL(category)
        } catch (e: Exception){
            assert(true)
        } finally {
            assert(false)
        }
    }
}