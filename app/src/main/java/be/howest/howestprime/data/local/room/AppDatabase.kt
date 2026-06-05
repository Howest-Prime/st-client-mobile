package be.howest.howestprime.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import be.howest.howestprime.data.local.room.dao.SnackDao
import be.howest.howestprime.data.local.room.dao.TicketDao
import be.howest.howestprime.data.local.room.entity.SnackEntity
import be.howest.howestprime.data.local.room.entity.TicketEntity

@Database(
    entities = [SnackEntity::class, TicketEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun snackDao(): SnackDao
    abstract fun ticketDao(): TicketDao
}