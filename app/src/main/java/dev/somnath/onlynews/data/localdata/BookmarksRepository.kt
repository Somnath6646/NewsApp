package dev.somnath.onlynews.data.localdata

import dev.somnath.onlynews.model.Article

class BookmarksRepository(private val dao: BookmarkDAO){

    val bookmarks = dao.getAllBookmarks()

    suspend fun insertArticleIntoBookmarks(bookmark: Bookmarks){
        dao.insertArticle(bookmark)
    }

    suspend fun deleteArticlefromBookmarks(bookmark: Bookmarks){
        dao.deleteArticle(bookmark)
    }

    suspend fun deleteALlArticleFromBookmarks(){
        dao.deleteAll()
    }
}