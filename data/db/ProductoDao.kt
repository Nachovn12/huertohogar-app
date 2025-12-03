package data.db

import androidx.room.*

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos")
    suspend fun getAll(): List<ProductoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(productos: List<ProductoEntity>)

    @Query("DELETE FROM productos")
    suspend fun clearAll()
}
