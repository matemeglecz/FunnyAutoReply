package com.funnyautoreply.data

import androidx.room.*

@Dao
interface MessageDao {
    @Query("SELECT * FROM message")
    fun getAll(): List<Message>

    @Insert
    fun insert(shoppingItems: Message): Long

    @Update
    fun update(shoppingItem: Message)

    /*@Delete
    fun deleteItem(shoppingItem: ShoppingItem)*/
}