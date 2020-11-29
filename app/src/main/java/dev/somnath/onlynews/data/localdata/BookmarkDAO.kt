package dev.somnath.onlynews.data.localdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import dev.somnath.onlynews.model.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDAO {
    @Insert
    suspend fun insertArticle(bookmarks: Bookmarks): Long

    @Delete
    suspend fun deleteArticle(bookmarks: Bookmarks)

    @Query("SELECT * FROM Bookmarks ORDER BY  bookmark_id DESC")
    fun getAllBookmarks(): LiveData<List<Bookmarks>>

    @Query("DELETE FROM Bookmarks")
    suspend fun deleteAll()

}