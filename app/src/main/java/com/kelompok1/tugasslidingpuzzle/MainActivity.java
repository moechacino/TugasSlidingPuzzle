package com.kelompok1.tugasslidingpuzzle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GridLayout gridLayout;
    private Button[][] buttons;
    private List<String> puzzlePieces;
    private int emptyRow, emptyCol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout = findViewById(R.id.gridLayout);
        buttons = new Button[4][4]; // Grid 4x4
        puzzlePieces = new ArrayList<>(Arrays.asList(
                "A", "B", "C", "D",
                "E", "F", "G", "H",
                "I", "J", "K", "L",
                "M", "N", "O", ""
        ));

        Collections.shuffle(puzzlePieces);

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                buttons[row][col] = new Button(this);
                buttons[row][col].setText(puzzlePieces.get(row * 4 + col));
                buttons[row][col].setOnClickListener(this);
                gridLayout.addView(buttons[row][col]);

                if (puzzlePieces.get(row * 4 + col).isEmpty()) {
                    emptyRow = row;
                    emptyCol = col;
                }
            }
        }

        Button shuffleButton = findViewById(R.id.shuffleButton);
        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shufflePuzzle();
            }
        });

        Button cheatButton = findViewById(R.id.cheatButton);
        cheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cheat();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Button clickedButton = (Button) v;
        String buttonText = clickedButton.getText().toString();
        int row = -1, col = -1;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (buttons[i][j] == clickedButton) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }

        if (isAdjacent(emptyRow, emptyCol, row, col)) {
            buttons[emptyRow][emptyCol].setText(buttonText);
            buttons[row][col].setText("");
            emptyRow = row;
            emptyCol = col;
        }

        checkWin();
    }

    private boolean isAdjacent(int emptyRow, int emptyCol, int row, int col) {
        int rowDiff = Math.abs(emptyRow - row);
        int colDiff = Math.abs(emptyCol - col);

        return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1);
    }

    private void shufflePuzzle() {
        Collections.shuffle(puzzlePieces);

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                buttons[row][col].setText(puzzlePieces.get(row * 4 + col));
                if (puzzlePieces.get(row * 4 + col).isEmpty()) {
                    emptyRow = row;
                    emptyCol = col;
                }
            }
        }
    }

    private void checkWin() {
        List<String> currentOrder = new ArrayList<>();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                currentOrder.add(buttons[row][col].getText().toString());
            }
        }

        List<String> winOrder = Arrays.asList(
                "A", "B", "C", "D",
                "E", "F", "G", "H",
                "I", "J", "K", "L",
                "M", "N", "O", ""
        );

        if (currentOrder.equals(winOrder)) {
            showWinDialog();
        }
    }

    private void showWinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.TransparentDialog);
        View dialogView = getLayoutInflater().inflate(R.layout.win_dialog, null);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();

        TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
        TextView dialogMessage = dialogView.findViewById(R.id.dialogMessage);
        Button okButton = dialogView.findViewById(R.id.okButton);

        dialogTitle.setText("Aanjaay Kamu Menang!");
        dialogMessage.setText("Selamat! Anda telah menyelesaikan puzzle.");

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shufflePuzzle();
                alertDialog.dismiss();
            }
        });
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.gravity = Gravity.TOP;

        alertDialog.show();
    }

    private void cheat() {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                buttons[row][col].setText(String.valueOf((char) ('A' + row * 4 + col)));
            }
        }
        buttons[3][3].setText("");
        checkWin();
    }
}
