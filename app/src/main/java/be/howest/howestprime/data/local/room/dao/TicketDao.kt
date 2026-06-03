package be.howest.howestprime.data.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import be.howest.howestprime.data.local.room.entity.TicketEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {

    @Query("SELECT * FROM tickets")
    fun getAllTickets(): Flow<List<TicketEntity>>

    @Query("SELECT * FROM tickets WHERE dateTime > date('now')")
    fun getUpcomingTickets(): Flow<List<TicketEntity>>
}