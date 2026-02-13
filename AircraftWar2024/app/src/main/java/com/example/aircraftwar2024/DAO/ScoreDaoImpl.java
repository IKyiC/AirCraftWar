package com.example.aircraftwar2024.DAO;

import android.content.Context;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class ScoreDaoImpl implements ScoreDao{

    private List<Score> scoreList = new ArrayList<>();
    private String fileName;
    private Context context;

    public ScoreDaoImpl(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
        //打开文件的输出流
        try(ObjectInputStream ois = new ObjectInputStream(context.openFileInput(this.fileName))){
            //用序列化的方式进行写入和写出
            scoreList = (List<Score>)ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addItem(Score score) throws IOException {
        scoreList.add(score);
        SortByScore(this.scoreList);
    }

    @Override
    public List<Score> GetAllItem() throws IOException {
        return scoreList;
    }

    @Override
    public void SortByScore(List<Score> scoreList) throws IOException {
        //正则表达式形式比较a和b分数的大小，return为负数则a在b之前
        //对列表进行排序
        scoreList.sort((b, a) -> {
            return a.GetScore() - b.GetScore();
        });

        //对文件重新排序
        ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput(this.fileName, Context.MODE_PRIVATE));
        oos.writeObject(scoreList);  //this.scoreList会随着文件流改变的，最开始使用了一个readObject方法
        oos.close();
    }

    @Override
    //按行删除
    public void DeleteItem(int row) throws IOException {
        scoreList.remove(row);
        //重新对数据进行排序和文件进行重写
        SortByScore(this.scoreList);

    }
}



