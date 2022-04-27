package com.fulldive.wallet.database


import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<in T> {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun create(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<T>)

    @Update
    fun update(obj: T)

    @Delete
    fun delete(obj: T)
}