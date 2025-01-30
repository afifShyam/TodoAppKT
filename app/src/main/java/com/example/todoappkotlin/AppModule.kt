package com.example.todoappkotlin
import android.content.Context
import androidx.room.Room
import com.example.todoappkotlin.data.TodoDao
import com.example.todoappkotlin.data.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TodoDatabase {
        return Room.databaseBuilder(
            context,
            TodoDatabase::class.java,
            "todo_database"
        ).fallbackToDestructiveMigration() // This prevents crashes on version changes
            .build()
    }

    @Provides
    @Singleton
    fun provideTodoDao(database: TodoDatabase): TodoDao {
        return database.todoDao()
    }
}