package com.example.onppe_v1

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SignalementDao {

    @Insert
    fun addSignalement(vararg signalement: SignalementTransfert)

    @Query("select * from signalements")
    fun getSignalement():List<SignalementTransfert>

    @Query("select * from signalements where upload=0")
    fun getSignalementToSynchronize():List<SignalementTransfert>

    @Update
    fun updateSignalements(team: List<SignalementTransfert>?)

    @Query("SELECT * FROM signalements WHERE signalementId = :id")
    fun getSignalementById(id: Int): SignalementTransfert

    @Update
    fun updateSynchronize(sign: SignalementTransfert)
}