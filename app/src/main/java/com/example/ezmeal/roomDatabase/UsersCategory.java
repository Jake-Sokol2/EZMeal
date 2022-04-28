<<<<<<< HEAD:app/src/main/java/com/example/ezmeal/RoomDatabase/Category.java
=======
package com.example.ezmeal.roomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class UsersCategory
{
    @PrimaryKey
    @NonNull
    @ColumnInfo (name = "category")
    public String category;

    // Room does not support Date variables
    @ColumnInfo (name = "dateRetrieved")
    public long dateRetrieved;

    public UsersCategory(String category, long dateRetrieved)
    {
        this.category = category;
        this.dateRetrieved = dateRetrieved;
    }

    @NonNull
    public String getCategory()
    {
        return category;
    }

    public void setCategory(@NonNull String category)
    {
        this.category = category;
    }
}

>>>>>>> dd0d8037da93aa6844472c72982db51af1786c51:app/src/main/java/com/example/ezmeal/roomDatabase/UsersCategory.java
