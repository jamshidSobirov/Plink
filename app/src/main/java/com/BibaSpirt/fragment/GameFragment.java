package com.BibaSpirt.fragment;

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
import androidx.core.content.res.ResourcesCompat;

import com.BibaSpirt.DialogClick;
import com.BibaSpirt.MyDialog;
import com.BibaSpirt.R;
import com.BibaSpirt.bubble.BubbleColor;
import com.BibaSpirt.bubble.BubbleManager;
import com.BibaSpirt.bubble.Player;
import com.BibaSpirt.engine.GameEngine;
import com.BibaSpirt.inputcontroller.BasicInputController;

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

    public GameFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        view.findViewById(R.id.btnPause).setOnClickListener(
                v -> {
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
                }
        );

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
        // Init bubble
        bubbleArray = new char[][]{
                {'o', 'y', 'r', 'r', 'd', 'y', 'r', 'd',},
                {'g', 'y', 'g', 'g', 'd', 'y', 'y', 'o',},
                {'y', 'b', 'o', 'b', 'y', 'b', 'd', 'r',},
                {'r', 'y', 'r', 'o', 'r', 'y', 'r', 'y',},
                {'y', 'b', 'b', 'y', 'd', 'g', 'b', 'o',},
                {'d', 'y', 'r', 'y', 'd', 'y', 'r', 'y',},
                {'y', 'd', 'g', 'r', 'd', 'r', 'y', 'o',},
                {'y', 'b', 'r', 'b', 'y', 'b', 'r', 'r',},
                {'0', '0', '0', '0', '0', '0', '0', '0',}};

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                bubbleArray[i][j] = bubbleColours[new Random().nextInt(6)];
            }
        }
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
//        mGameEngine.setSoundManager(getMainActivity().getSoundManager());

//        // Init bubble

        // When the bubble is added exceed the max row, app will throw index out of bound exception,
        // so the better way is to set a fix row number and show a line at bottom, and when player
        // add bubble over the line, the game is over
        BubbleManager bubbleManager = new BubbleManager(mGameEngine, bubbleArray, this);

        // Add all the object to engine
        mGameEngine.addGameObject(new Player(mGameEngine, bubbleManager, this), 2);
        // mGameEngine.addGameObject(new FPSCounter(mGameEngine), 0);
        mGameEngine.startGame();
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

    public void addNewBubbleLine() {
        char[][] newBubble = new char[9][8];
        char[] bubbleColours = {'b', 'r', 'y', 'o', 'g', 'd'};

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                newBubble[i][j] = bubbleColours[new Random().nextInt(6)];
            }
        }

        bubbleArray = newBubble;
        startGame();

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

                if (s > 2)
                    mp.start();

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
        ball1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), getBalls(ballPos)[1], null));
        ball2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), getBalls(ballPos)[2], null));
        ball3.setImageDrawable(ResourcesCompat.getDrawable(getResources(), getBalls(ballPos)[3], null));
        ball4.setImageDrawable(ResourcesCompat.getDrawable(getResources(), getBalls(ballPos)[4], null));
        ball5.setImageDrawable(ResourcesCompat.getDrawable(getResources(), getBalls(ballPos)[5], null));
    }


    private Integer[] getBalls(int pos) {
        Integer[] list = new Integer[6];
        list[0] = R.drawable.ball1;
        list[1] = R.drawable.ball2;
        list[2] = R.drawable.ball3;
        list[3] = R.drawable.ball4;
        list[4] = R.drawable.ball5;
        list[5] = R.drawable.ball6;

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
