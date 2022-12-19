package com.glebalekseevjk.yasmrhomework.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.glebalekseevjk.yasmrhomework.data.local.model.RouterDbModel

@Dao
interface RouterDao {
    @Query("SELECT * FROM RouterDbModel WHERE bssid = :bssid")
    fun get(bssid: Long): RouterDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(todoItemDbModel: RouterDbModel)

    @Query("DELETE FROM RouterDbModel WHERE bssid = :bssid")
    fun delete(bssid: Long)

    @Query("SELECT * FROM RouterDbModel")
    fun getAll(): LiveData<List<RouterDbModel>>

    @Query("DELETE FROM RouterDbModel")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(vararg router: RouterDbModel)

    @Transaction
    fun replaceAll(routerList: List<RouterDbModel>) {
        deleteAll()
        addAll(*routerList.toTypedArray())
    }
}