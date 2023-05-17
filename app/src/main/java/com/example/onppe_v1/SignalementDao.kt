package com.example.onppe_v1

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface SignalementDao {

    @Insert
    fun addSignalement(vararg signalement: SignalementTransfert)

    @Query("select * from signalements")
    fun getSignalement():List<SignalementTransfert>
}