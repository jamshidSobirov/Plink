package com.plink6746.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.plink6746.DialogClick;
import com.plink6746.MyDialog;
import com.plink6746.R;
import com.plink6746.bubble.BubbleColor;
import com.plink6746.bubble.BubbleManager;
import com.plink6746.bubble.Player;
import com.plink6746.engine.GameEngine;
import com.plink6746.engine.RedLine;
import com.plink6746.inputcontroller.BasicInputController;
import com.plink6746.utils.ImageUtils;

import java.util.Random;

public class GameFragment extends BaseFragment implements Observers {
    SharedPreferences sharedPreferences;
    TextView tvScore;
    int score = 0;
    int ballPos = 0;
    int bubbleAddTIme = 20;
    char[][] bubbleArray;
    private GameEngine mGameEngine;
    ImageView ball1, ball2, ball3, ball4, ball5;
    MediaPlayer mp, mp2;
    private String difficulty;
    private long lastBubbleMove = 0;
    private int currentStep = 0;

    public GameFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get difficulty from arguments
        if (getArguments() != null) {
            difficulty = getArguments().getString("difficulty", "easy");
        } else {
            difficulty = "easy";
        }

        // Adjust game parameters based on difficulty
        switch (difficulty) {
            case "hard":
                bubbleAddTIme = 10; // Faster bubble addition
                break;
            case "medium":
                bubbleAddTIme = 15; // Medium bubble addition speed
                break;
            default: // "easy"
                bubbleAddTIme = 20; // Default bubble addition time
                break;
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        initBubble();
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        tvScore = view.findViewById(R.id.tvScore);
        tvScore.setText(getString(R.string.score) + " " + 0);

        sharedPreferences.edit().putInt("gamesPlayed", 0).putInt("gamesPlayed", sharedPreferences.getInt("gamesPlayed", 0) + 1).apply();

        view.findViewById(R.id.btnMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp2.start();
                requireActivity().finish();
            }
        });

        view.findViewById(R.id.btnPause).setOnClickListener(v -> {
            mGameEngine.pauseGame();
            MyDialog dialog = new MyDialog(requireActivity(), "pause", new DialogClick() {
                @Override
                public void onMenuClick() {
                    requireActivity().finish();
                }

                @Override
                public void onBtnClick() {
                    mGameEngine.resumeGame();
                }
            });
            dialog.setCancelable(false);
            dialog.show();
            mp2.start();
        });

        ball1 = view.findViewById(R.id.ball1);
        ball2 = view.findViewById(R.id.ball2);
        ball3 = view.findViewById(R.id.ball3);
        ball4 = view.findViewById(R.id.ball4);
        ball5 = view.findViewById(R.id.ball5);

        mp = MediaPlayer.create(requireContext(), R.raw.combination_complete);
        mp2 = MediaPlayer.create(requireContext(), R.raw.button_pressed);

    }

    private void initBubble() {
        char[] bubbleColours = {'b', 'r', 'y', 'o', 'g', 'd'};
        // Init bubble with triangular pattern
        bubbleArray = new char[14][11];

        // Fill with '0' first (empty spaces)
        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 11; j++) {
                bubbleArray[i][j] = '0';
            }
        }

        // Create upward triangular pattern
        Random random = new Random();
        int triangleHeight = 9;  // Height of 9 rows
        int maxWidth = 11;  // Maximum width of the grid

        for (int row = 0; row < triangleHeight; row++) {
            int actualRow = triangleHeight - row - 1;  // Start from bottom row
            // Calculate width to increase by roughly 1.4 bubbles per row to reach 11 in 9 rows
            int width = Math.min(maxWidth, (int) (row * 1.4 + 1));
            int startCol = (maxWidth - width) / 2;  // Center the bubbles
            int endCol = startCol + width;    // Calculate width for this row

            for (int col = startCol; col < endCol; col++) {
                bubbleArray[actualRow][col] = bubbleColours[random.nextInt(6)];
            }
        }
    }

    public void addNewBubbleLine() {
        char[][] newBubble = new char[14][11];
        char[] bubbleColours = {'b', 'r', 'y', 'o', 'g', 'd'};
        Random random = new Random();

        // Fill with '0' first
        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 11; j++) {
                newBubble[i][j] = '0';
            }
        }

        // Create upward triangular pattern
        int triangleHeight = 9;  // Height of 9 rows
        int maxWidth = 11;  // Maximum width of the grid

        for (int row = 0; row < triangleHeight; row++) {
            int actualRow = triangleHeight - row - 1;  // Start from bottom row
            // Calculate width to increase by roughly 1.4 bubbles per row to reach 11 in 9 rows
            int width = Math.min(maxWidth, (int) (row * 1.4 + 1));
            int startCol = (maxWidth - width) / 2;  // Center the bubbles
            int endCol = startCol + width;    // Calculate width for this row

            for (int col = startCol; col < endCol; col++) {
                newBubble[actualRow][col] = bubbleColours[random.nextInt(6)];
            }
        }

        bubbleArray = newBubble;
        startGame();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    protected void onLayoutCompleted() {
        startGame();
    }

    private void startGame() {
        // Init engine
        mGameEngine = new GameEngine(getMainActivity(), getView().findViewById(R.id.game_view), this);
        mGameEngine.setInputController(new BasicInputController(mGameEngine));

        // Reset bubble movement timer and step
        lastBubbleMove = System.currentTimeMillis();
        currentStep = 0;

        // When the bubble is added exceed the max row, app will throw index out of bound exception,
        // so the better way is to set a fix row number and show a line at bottom, and when player
        // add bubble over the line, the game is over
        BubbleManager bubbleManager = new BubbleManager(mGameEngine, bubbleArray, this);

        // Calculate the Y position for the red line at the bottom of the 14th row
        float intervalY = mGameEngine.mScreenWidth * 0.85f / 11f * 0.75f / mGameEngine.mPixelFactor;
        float redLineY = 13 * intervalY; // Position at row 13 (14th row, 0-based)

        // Add the red line
        mGameEngine.addGameObject(new RedLine(mGameEngine, redLineY), 1);

        // Add all the object to engine
        mGameEngine.addGameObject(new Player(mGameEngine, bubbleManager, this), 2);

        // Start bubble movement check
        new Thread(() -> {
            while (mGameEngine != null && mGameEngine.isRunning()) {
                if (!mGameEngine.isPaused()) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastBubbleMove >= bubbleAddTIme * 1000) { // Convert to milliseconds
                        lastBubbleMove = currentTime;
                        requireActivity().runOnUiThread(() -> moveBubblesDown());
                    }
                }
                try {
                    Thread.sleep(100); // Check every 100ms
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        mGameEngine.startGame();
    }

    private void moveBubblesDown() {
        currentStep++;

        // Create new array with one row shifted down
        char[][] newArray = new char[14][11];

        // Fill with '0' first
        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 11; j++) {
                newArray[i][j] = '0';
            }
        }

        // Move everything down one row
        for (int i = 13; i > 0; i--) {
            System.arraycopy(bubbleArray[i - 1], 0, newArray[i], 0, 11);
        }

        // Add new bubbles at the top row
        char[] bubbleColours = {'b', 'r', 'y', 'o', 'g', 'd'};
        Random random = new Random();
        
        // Add a bubble to each column in the top row
        for (int col = 0; col < 11; col++) {
            newArray[0][col] = bubbleColours[random.nextInt(6)];
        }

        bubbleArray = newArray;

        // Check for game over - check row 13 (where red line is)
        boolean gameOver = false;
        for (int col = 0; col < 11; col++) {
            if (bubbleArray[13][col] != '0') {
                gameOver = true;
                break;
            }
        }

        if (gameOver) {
            observeGameOver(true);
        }
        
        // Restart the game with new bubble positions
        startGame();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGameEngine != null && mGameEngine.isRunning() && mGameEngine.isPaused()) {
            mGameEngine.resumeGame();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGameEngine.isRunning() && !mGameEngine.isPaused()) {
            mGameEngine.pauseGame();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGameEngine.stopGame();
    }

    @Override
    public boolean onBackPressed() {
        getMainActivity().finish();
        return true;
    }

    @Override
    public void observeGameOver(Boolean gameOver) {
        requireActivity().runOnUiThread(new Runnable() {
            public void run() {
                MyDialog dialog = new MyDialog(requireActivity(), "gameOver", new DialogClick() {
                    @Override
                    public void onMenuClick() {
                        requireActivity().finish();
                    }

                    @Override
                    public void onBtnClick() {
                        requireActivity().recreate();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
            }
        });

    }

    @Override
    public void shotBubbleColor(BubbleColor shotBubbleColor) {
        ballPos = 0;
        switch (shotBubbleColor) {
            case BLUE:
                ballPos = 1;
                break;
            case RED:
                ballPos = 2;
                break;
            case YELLOW:
                ballPos = 3;
                break;
            case ORANGE:
                ballPos = 4;
                break;
            case BLACK:
                ballPos = 5;
                break;
            case GREEN:
                ballPos = 0;
                break;
        }
        placeTheBalls(ballPos);
    }

    @Override
    public void observeScore(int s) {
//        if (ballPos > 5) {
//            ballPos = 0;
//        }

        requireActivity().runOnUiThread(new Runnable() {
            public void run() {
//                placeTheBalls(ballPos);

                if (s > 2) mp.start();

                switch (s) {
                    case 0:
                    case 1:
                    case 2:
                        score += 0;
                        break;
                    case 3:
                        score += 10;
                        break;
                    case 4:
                        score += 20;
                        break;
                    case 5:
                        score += 40;
                        break;
                    case 6:
                        score += 80;
                        break;
                    case 7:
                        score += 100;
                        break;
                    case 8:
                        score += 120;
                        break;
                    case 9:
                        score += 140;
                        break;
                    case 10:
                        score += 160;
                        break;
                    default:
                        score += 200;

                }
                if (score > sharedPreferences.getInt("bestScore", 0)) {
                    sharedPreferences.edit().putInt("bestScore", score).apply();
                }

                sharedPreferences.edit().putInt("lastGameResult", score).apply();

                tvScore.setText(getString(R.string.score) + " " + score);
//                ballPos++;
            }
        });
    }

    @Override
    public void ballsFinished() {
        addNewBubbleLine();
    }

    private void placeTheBalls(int ballPos) {
        ball1.setImageDrawable(ImageUtils.getScaledDrawable(requireContext(), getBalls(ballPos)[1]));
        ball2.setImageDrawable(ImageUtils.getScaledDrawable(requireContext(), getBalls(ballPos)[2]));
        ball3.setImageDrawable(ImageUtils.getScaledDrawable(requireContext(), getBalls(ballPos)[3]));
        ball4.setImageDrawable(ImageUtils.getScaledDrawable(requireContext(), getBalls(ballPos)[4]));
        ball5.setImageDrawable(ImageUtils.getScaledDrawable(requireContext(), getBalls(ballPos)[5]));
    }


    private Integer[] getBalls(int pos) {
        Integer[] list = new Integer[6];
        list[0] = R.drawable.ball_blue;
        list[1] = R.drawable.ball_red;
        list[2] = R.drawable.ball_yellow;
        list[3] = R.drawable.ball_orange;
        list[4] = R.drawable.ball_green;
        list[5] = R.drawable.ball_black;

        Integer[] temp = new Integer[6];
        for (int i = pos, j = 0; i < 6; i++, j++) {
            temp[j] = list[i];
        }
        for (int i = 0, j = 6 - pos; i < pos; i++, j++) {
            temp[j] = list[i];
        }

        return temp;
    }


}
