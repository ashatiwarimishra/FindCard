package com.singtel.findcard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FindCardActivity extends AppCompatActivity implements FindCardAdapter.OnCardClickedListener {

    private FindCardViewModel mViewModel;
    private RecyclerView rvMazeList;
    private FindCardAdapter mAdapter;
    private Button btnResetGame;
    private TextView txtStepCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewModel = new ViewModelProvider(this).get(FindCardViewModel.class);
        initView();
        initListener();
        mViewModel.prepareMazeGrid();
    }

    private void initListener() {
        mViewModel.observeMazeNumberListLiveData().observe(this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> integers) {
                setMazeAdapter(integers);
            }
        });

        mViewModel.observeRefreshMazeLiveData().observe(this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> validSelectionList) {
                mAdapter.refreshValidSelection(validSelectionList);
            }
        });

        mViewModel.observeGameCompletionLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer step) {
                showGameCompletionDialog(step);
            }
        });

        mViewModel.observeStepCountLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer stepCount) {
                txtStepCount.setText(getString(R.string.step, String.valueOf(stepCount)));
            }
        });

        btnResetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.prepareMazeGrid();
            }
        });
    }

    private void initView() {
        rvMazeList = findViewById(R.id.rv_maze);
        btnResetGame = findViewById(R.id.btn_reset);
        txtStepCount = findViewById(R.id.txt_step_count);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        rvMazeList.setLayoutManager(gridLayoutManager);
    }

    private void setMazeAdapter(List<Integer> mazeNumberList) {
        mAdapter = new FindCardAdapter(mazeNumberList,null, this);
        rvMazeList.setAdapter(mAdapter);
    }

    @Override
    public void onCardClicked(Card card) {
        mViewModel.onCardClicked(card);
    }

    private void showGameCompletionDialog(Integer step) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.congratulation));
        builder.setMessage(getString(R.string.win_message, String.valueOf(step)));
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mViewModel.prepareMazeGrid();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
