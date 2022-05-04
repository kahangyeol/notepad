package com.example.memo.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface userDao {

    @Query("SELECT * FROM memoTable ")/*WHERE trash = 0*/
    List<User> getAll();

    @Query("SELECT * FROM memoTable WHERE root is null Order by id")                // User 메모 정보만 불러오기
    List<User> getAllFolder();

    @Query("SELECT count(id) FROM memoTable WHERE root is null")                // User 메모 정보만 불러오기
    int getAllFolderCount();

    @Query("SELECT * FROM memoTable WHERE root is not null")                // User 메모 정보만 불러오기
    List<User> getAllMemo();

    @Query("SELECT * FROM memoTable WHERE root = :root and id < 1000")                // User root로 메모 정보만불러오기
    List<User> getAllMemoRoot(String root);

    @Query("SELECT * FROM memoTable WHERE trash = 1")                       // User 휴지통에 있는 정보만 불러오기
    List<User> trashGetAll();

    @Query("SELECT * FROM memoTable WHERE root is null AND id = :id")                // User 메모 정보만 불러오기
    User getFolder(int id);

    @Query("SELECT * from memoTable where folderTitle = :folderTitle")      // User 폴더 제목으로 폴더 정보 불러오기
    User selectFolder(String folderTitle);

    @Query("SELECT * FROM memoTable WHERE root = :root AND memoTitle = :memoTitle AND id = :id") // User 메모 제목과 root, id로 폴더 정보 불러오기
    User selectMemo(String root, String memoTitle,int id);

    @Query("SELECT * from memoTable where memoTitle = :memoTitle and trash = 1")    // User 휴지통에서 메모 제목으로 메모 정보 불러오기
    User selectTrash(String memoTitle);

    @Query("SELECT folderTitle FROM memoTable WHERE root is null")          // String 폴더 제목만 불러오기
    List<String> getAllFolderTitle();

    @Query("SELECT memoTitle FROM memoTable WHERE root is not null")        // String 메모 제목만 불러오기
    List<String> getAllMemoTitle();




/*    @Query("SELECT content FROM memoTable WHERE folderTitle LIKE :folderTitle AND memoTitle LIKE :memoTitle LIMIT 1 ")
    User findcontent(String folderTitle, String memoTitle);*/
    @Query("SELECT star FROM memoTable WHERE id = :id AND root is null")        // int 폴더 즐겨찾기 확인
    int starFolder(int id);

    @Query("SELECT star FROM memoTable WHERE id = :id AND root = :root")        // int 메모 즐겨찾기 확인
    int loadStarMemo(int id,String root);

    @Query("SELECT password FROM memoTable WHERE id = :id AND root = :root")        // int 메모 잠금 확인
    int loadPassWord(int id, String root);

    @Query("SELECT pin FROM memoTable WHERE id = :id AND root is null")        // int 메모 고정 확인
    int loadPin(int id);

    @Query("SELECT pin FROM memoTable WHERE id = :id AND root = :root")        // int 메모 고정 확인
    int loadPinMemo(int id, String root);

    @Query("SELECT count(pin) FROM memoTable WHERE pin = 1 AND root is null")        // int 폴더 고정 갯수
    int pinCount();

    @Query("SELECT count(pin) FROM memoTable WHERE pin = 1 AND root = :root")        // int 폴더 고정 갯수
    int pinCountMemo(String root);

    @Query("SELECT COUNT(id) FROM memoTable where root = :root AND id < 1000 ")        // int 휴지통에 넣은 메모의 root와 일치하는 갯수 확인
    int rollBack (String root);

    @Query("SELECT COUNT(id) FROM memoTable WHERE trash = 1")        // int 휴지통에 갯수 확인
    int trashCount();

    @Query("SELECT * FROM memoTable WHERE trash = 1 AND id = :id")
    User loadTrash(int id);

    @Query("SELECT memoTitle FROM memoTable WHERE root = :root and id = :id")
    String loadMemoTitle(String root, int id);

    @Query("SELECT folderTitle FROM memoTable WHERE id = :id AND root is null")
    String loadFolderTitle(int id);





    @Query("UPDATE memoTable SET password = 1 WHERE id = :id AND root = :root")
    void updatePassWordOn(int id,String root);

    @Query("UPDATE memoTable SET password = 0 WHERE id = :id AND root = :root")
    void updatePassWordOff(int id,String root);

    @Query("UPDATE memoTable SET pin = 1 WHERE id = :id AND root is null")
    void updatePinOnFolder(int id);

    @Query("UPDATE memoTable SET pin = 0 WHERE id = :id AND root is null")
    void updatePinOffFolder(int id);

    @Query("UPDATE memoTable SET pin = 1 WHERE id = :id AND root = :root")
    void updatePinOn(int id,String root);

    @Query("UPDATE memoTable SET pin = 0 WHERE id = :id AND root = :root")
    void updatePinOff(int id,String root);

    @Query("UPDATE memoTable SET star = 1 WHERE id = :id AND root = :root")
    void updateStarOnMemo(int id,String root);

    @Query("UPDATE memoTable SET star = 0 WHERE id = :id AND root = :root")
    void updateStarOffMemo(int id,String root);

    @Query("UPDATE memoTable SET star = 1 WHERE id = :id AND root is null")
    void updateStarOnFolder(int id);

    @Query("UPDATE memoTable SET star = 0 WHERE id = :id AND root is null")
    void updateStarOffFolder(int id);

    @Query("UPDATE memoTable SET memoTitle = :memoTitle, content = :content, editTime = :editTime WHERE id = :id AND root = :root")
    void updateMemo(String memoTitle,String content,int id,String root, String editTime);

    @Query("UPDATE memoTable SET id = :id WHERE folderTitle = :folderTitle")
    void updateId(int id, String folderTitle);

    @Query("UPDATE memoTable SET id = :setId WHERE memoTitle = :memoTitle AND root = :root AND id = :id")
    void updateMemoId(int setId, String memoTitle, String root, int id);

    @Query("UPDATE memoTable SET folderTitle = :folderTitle WHERE root is null and id = :id")
    void updateFolderTitle(String folderTitle,int id);

    @Query("UPDATE memoTable SET root = :folderTitle WHERE root = :root")
    void updateRoot (String folderTitle, String root);

    @Query("UPDATE memoTable SET trash = :trash, id = :trashId WHERE id = :id AND root = :root")
    void updateTrashId (int trash, int trashId, int id, String root);




    @Query("DELETE FROM memoTable WHERE root = :root")
    void delMemo(String root);

    @Query("DELETE FROM memoTable WHERE folderTitle = :folderTitle AND id = :id")
    void delFolder(String folderTitle, int id);

    @Query("DELETE FROM memoTable WHERE id = :id")
    void selectDelFolder(int id);

    @Update
    void update(User user);

    @Insert
    void insertAll(User user);

    @Delete
    void delete(User user);
}
