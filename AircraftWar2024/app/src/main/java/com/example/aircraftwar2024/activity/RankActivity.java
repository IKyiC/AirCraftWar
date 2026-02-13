package com.example.aircraftwar2024.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aircraftwar2024.DAO.Score;
import com.example.aircraftwar2024.DAO.ScoreDao;
import com.example.aircraftwar2024.DAO.ScoreDaoImpl;
import com.example.aircraftwar2024.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RankActivity extends AppCompatActivity {
    String time;
    ScoreDao scoreDao = null;
    Score score = new Score();
    List<Score> user = new ArrayList<>();
    int overscore;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        TextView textView = findViewById(R.id.textView_rankingList);
        switch (OfflineActivity.gameType) {
            case(1):
                scoreDao = new ScoreDaoImpl(this, "simple_data.txt");
                textView.setText("简单模式排行榜");
                break;
            case(2):
                scoreDao = new ScoreDaoImpl(this, "medium_data.txt");
                textView.setText("普通模式排行榜");
                break;
            case(3):
                scoreDao = new ScoreDaoImpl(this, "hard_data.txt");
                textView.setText("困难模式排行榜");
                break;
            default:
                System.out.println("ERROR!");
        }

        Button return_btn = (Button) findViewById(R.id.return_btn);

        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RankActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        overscore = getIntent().getIntExtra("user_score", 0);
        time = getIntent().getStringExtra("user_date");
        inputname();

    }


    public void inputname() {
        EditText edit = new EditText(this);
        edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)}); //不超过10个字符的过滤器

        AlertDialog.Builder input_builder = new AlertDialog.Builder(this);
        input_builder.setTitle("请输入用户名")
                     .setIcon(android.R.drawable.ic_dialog_info)
                     .setView(edit);

        input_builder.setPositiveButton("确定", (dialogInterface, i) -> {
            String user_name = edit.getText().toString();
            if(user_name.isEmpty()) {
                Toast.makeText(this, "输入为空", Toast.LENGTH_SHORT).show();
            }
            else{
                score.SetScore(overscore);
                score.SetDate(time);
                score.SetName(user_name);
                try {
                    scoreDao.addItem(score);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    showList();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            }
        });
        input_builder.setNegativeButton("取消", (dialogInterface, i) -> {
            Toast.makeText(this, "未输入用户名，本次数据不保存", Toast.LENGTH_SHORT).show();
            try {
                showList();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        input_builder.show();

    }
    public void showList() throws IOException {
        try {
            user = scoreDao.GetAllItem();
        }catch (IOException e) {
            e.printStackTrace();
        }
        ListView listView = findViewById(R.id.list);
        List<Map<String, String>> maps = new LinkedList<>();
        int i = 1;
        for(Score userScore : user) {
            Map<String, String> map_element = new HashMap<>();
            map_element.put("rank", i+"");
            map_element.put("name", userScore.GetName());
            map_element.put("score", String.valueOf(userScore.GetScore()));
            map_element.put("time", userScore.GetDate());
            maps.add(map_element);
            i++;
        }

        SimpleAdapter adapter = new SimpleAdapter(this,
                maps, R.layout.ranking_item,
                new String[] {"rank", "name", "score", "time"},
                new int[] {R.id.rank, R.id.name, R.id.score, R.id.time});

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int row, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RankActivity.this);
                builder.setTitle("确定删除？");
                builder.setMessage("您确定删除该条信息吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                maps.remove(row);
                                update(maps);
                                try {
                                    user.remove(row);
                                    scoreDao.SortByScore(user);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                adapter.notifyDataSetChanged();

                            }

                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    // 更新排名
    private void update(List<Map<String, String>> data) {
        for (int i = 0; i < data.size(); i++) {
            Map<String, String> item = data.get(i);
            item.put("rank", String.valueOf(i + 1));
        }
    }
}
