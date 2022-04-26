package com.example.memo.Room;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "memoTable")
public class User implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int a;

    @ColumnInfo
    public int id;

    @ColumnInfo(name = "folderTitle")
    public String folderTitle;

    @ColumnInfo(name = "memoTitle")
    public String memoTitle;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "trash")
    public int trash;

    @ColumnInfo(name = "root")
    public String root;

    @ColumnInfo(name = "star")
    public int star;

    @ColumnInfo(name = "password")
    public int password;

    @ColumnInfo(name = "pin")
    public int pin;

    public User (int id,String folderTitle, int trash,String memoTitle, String content,String root,int star, int password, int pin){
        this.id = id;
        this.folderTitle = folderTitle;
        this.trash = trash;
        this.memoTitle = memoTitle;
        this.content = content;
        this.root = root;
        this.star = star;
        this.password = password;
        this.pin = pin;
    }

    /*protected User(Parcel in) {
        a = in.readInt();
        id = in.readInt();
        folderTitle = in.readString();
        memoTitle = in.readString();
        content = in.readString();
        trash = in.readByte() != 0;
        root = in.readString();
    }*/

/*
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
*/

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFolderTitle() {
        return folderTitle;
    }

    public void setFolderTitle(String folderTitle) {
        this.folderTitle = folderTitle;
    }

    public String getMemoTitle() {
        return memoTitle;
    }

    public void setMemoTitle(String memoTitle) {
        this.memoTitle = memoTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTrash() {
        return trash;
    }

    public void setTrash(int trash) {
        this.trash = trash;
    }

    public String getRoot(){ return root; }

    public void setRoot(){ this.root = root; }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(a);
        dest.writeInt(id);
        dest.writeString(folderTitle);
        dest.writeString(memoTitle);
        dest.writeString(content);
        dest.writeInt(trash);
        dest.writeString(root);
    }
        protected User(Parcel in) {
        a = in.readInt();
        id = in.readInt();
        folderTitle = in.readString();
        memoTitle = in.readString();
        content = in.readString();
        trash = in.readInt();
        root = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
