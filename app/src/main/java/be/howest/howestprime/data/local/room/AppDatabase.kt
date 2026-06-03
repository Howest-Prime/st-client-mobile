package be.howest.howestprime.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import be.howest.howestprime.data.local.room.dao.SnackDao
import be.howest.howestprime.data.local.room.dao.TicketDao
import be.howest.howestprime.data.local.room.entity.SnackEntity

@Database(
    entities = [SnackEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun snackDao(): SnackDao
    abstract fun ticketDao(): TicketDao
}