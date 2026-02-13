package com.example.aircraftwar2024.DAO;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 数据访问模式 DA0接口
 */
public interface ScoreDao {

    /**
     * 向源文件写入记录
     * @param score 数据对象
     */
    void addItem(Score score) throws IOException;

    /**
     * 从源文件获取全部对象
     */
    List<Score> GetAllItem() throws IOException;

    /**
     * 对分数进行排序
     */
    void SortByScore(List<Score> scoreList) throws IOException;

    /**
     * 删除文件中的某条记录
     */
    void DeleteItem(int row) throws IOException;
}
