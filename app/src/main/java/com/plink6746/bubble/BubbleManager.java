package com.plink6746.bubble;

import com.plink6746.engine.GameEngine;
import com.plink6746.fragment.Observers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BubbleManager {

    private final GameEngine mGameEngine;
    private final Bubble[][] mBubbleArray;
    private final int mCol, mRow;
    private final float mBubbleWidth;
    Observers observers;
    int poppedBalls = 0;

    private final ArrayList<Bubble> mDeleteList = new ArrayList<>();

    public BubbleManager(GameEngine gameEngine, char[][] charArray) {
        mGameEngine = gameEngine;
        mCol = charArray[0].length;
        mRow = charArray.length;

        mBubbleArray = new Bubble[mRow][mCol];
        // Calculate bubble width to fill screen width, but make bubbles smaller
        float totalWidth = gameEngine.mScreenWidth * 0.85f;  // Use 85% of screen width
        mBubbleWidth = (totalWidth / (mCol + 0.5f)) / gameEngine.mPixelFactor;

        initBubble(charArray);
    }

    public BubbleManager(GameEngine gameEngine, char[][] charArray, Observers observers) {
        mGameEngine = gameEngine;
        mCol = charArray[0].length;
        mRow = charArray.length;

        mBubbleArray = new Bubble[mRow][mCol];
        // Calculate bubble width to fill screen width, but make bubbles smaller
        float totalWidth = gameEngine.mScreenWidth * 0.85f;  // Use 85% of screen width
        mBubbleWidth = (totalWidth / (mCol + 0.5f)) / gameEngine.mPixelFactor;
        this.observers = observers;

        initBubble(charArray);
    }

    private void initBubble(char[][] charArray) {
        float intervalX = mBubbleWidth * 0.80f;  // Reduced horizontal spacing to fit all columns
        float intervalY = mBubbleWidth * 0.75f;  // Also reduced vertical spacing slightly

        // Add bubble to array
        for (int i = 0; i < mRow; i++) {
            for (int j = 0; j < mCol; j++) {
                // Init new bubble
                Bubble bubble = new Bubble(mGameEngine, i, j, getBubbleColor(charArray[i][j]));
                bubble.mX = j * intervalX;
                bubble.mY = i * intervalY;

                // We adjust x interval at odd row
                if ((i % 2) != 0) {
                    bubble.mX += intervalX / 2f;
                }

                // Add to array and engine
                mBubbleArray[i][j] = bubble;
                mGameEngine.addGameObject(bubble, 1);
            }
        }

        // Add adjacent bubble to edges
        for (int i = 0; i < mRow; i++) {
            for (int j = 0; j < mCol; j++) {
                Bubble bubble = mBubbleArray[i][j];
                if (i < mRow - 1) {
                    bubble.mEdges.add(mBubbleArray[i + 1][j]);   // Down
                }
                if (i > 0) {
                    bubble.mEdges.add(mBubbleArray[i - 1][j]);   // Up
                }
                if (j < mCol - 1) {
                    bubble.mEdges.add(mBubbleArray[i][j + 1]);   // Right
                }
                if (j > 0) {
                    bubble.mEdges.add(mBubbleArray[i][j - 1]);   // Left
                }

                if ((i % 2) == 0) {
                    if (i < mRow - 1 && j > 0) {
                        bubble.mEdges.add(mBubbleArray[i + 1][j - 1]);   // Bottom left
                    }
                    if (i > 0 && j > 0) {
                        bubble.mEdges.add(mBubbleArray[i - 1][j - 1]);   // Top left
                    }
                } else {
                    if (i < mRow - 1 && j < mCol - 1) {
                        bubble.mEdges.add(mBubbleArray[i + 1][j + 1]);   // Bottom right
                    }
                    if (i > 0 && j < mCol - 1) {
                        bubble.mEdges.add(mBubbleArray[i - 1][j + 1]);   // Top right
                    }
                }
            }
        }
    }

    private BubbleColor getBubbleColor(char color) {
        switch (color) {
            case 'b':
                return BubbleColor.BLUE;
            case 'r':
                return BubbleColor.RED;
            case 'y':
                return BubbleColor.YELLOW;
            case 'o':
                return BubbleColor.ORANGE;
            case 'g':
                return BubbleColor.GREEN;
            case 'd':
                return BubbleColor.BLACK;
            case '0':
                return BubbleColor.BLANK;
        }
        return BubbleColor.BLANK;
    }

    public void addBubble(Player player, Bubble bubble) throws ArrayIndexOutOfBoundsException {
        int row = bubble.mRow;
        int col = bubble.mCol;
        Bubble newBubble;
        int previousAmount = 0;
        int finalAmount = 0;
        int score = 0;

        previousAmount = countTheRemovedBalls();

        if (player.mY > bubble.mY + mBubbleWidth / 2) {   // Player collide bubble at bottom

            if (player.mX >= bubble.mX) {   // Player collide bubble at right bottom

                if (row % 2 == 0) {
                    newBubble = mBubbleArray[row + 1][col];
                } else {
                    newBubble = mBubbleArray[row + 1][col + 1];
                }

                // Check odd or even row

            } else {   // Player collide bubble at left bottom

                // Check odd or even row
                if (row % 2 == 0) {
                    newBubble = mBubbleArray[row + 1][col - 1];
                } else {
                    newBubble = mBubbleArray[row + 1][col];
                }
            }

        } else {   // Player collide bubble at side

            if (player.mX >= bubble.mX) {   // Player collide bubble at right side
                newBubble = mBubbleArray[row][col + 1];
            } else {   // Player collide bubble at left side
                newBubble = mBubbleArray[row][col - 1];
            }

        }

        // Add new bubble and set color
        newBubble.setBubbleColor(player.mBubbleColor);

        // Remove same color bubble
        popBubble(newBubble);

        // Remove floater
        popFloater();

        finalAmount = countTheRemovedBalls();

        score = finalAmount - previousAmount + 1;

        observers.observeScore(score);

        // Check for game over - if any bubble is in row 13
        for (int j = 0; j < mCol; j++) {
            if (mBubbleArray[13][j].mBubbleColor != BubbleColor.BLANK) {
                observers.observeGameOver(true);
                return;
            }
        }
    }

    private void popBubble(Bubble bubble) {
        poppedBalls = 0;
        // Search same color bubble with bfs
        bfs(bubble, bubble.mBubbleColor);

        // Update bubble after bfs
        int size = mDeleteList.size();

        poppedBalls += size;

        for (Bubble b : mDeleteList) {
            // We set it to blank if 3 bubble match
            if (size >= 3) {
                b.setBlankBubble();
            }

            // Reset depth
            b.mDepth = -1;
        }
        mDeleteList.clear();
    }

    private void bfs(Bubble root, BubbleColor color) {
        Queue<Bubble> queue = new LinkedList<>();
        root.mDepth = 0;
        queue.offer(root);

        while (queue.size() > 0) {
            Bubble currentBubble = queue.poll();
            mDeleteList.add(currentBubble);
            for (Bubble b : currentBubble.mEdges) {
                // Unvisited bubble
                if (b.mDepth == -1 && b.mBubbleColor == color) {
                    b.mDepth = currentBubble.mDepth + 1;
                    queue.offer(b);
                }
            }
        }
    }

    private void popFloater() {
        // We start dfs from root

        for (int i = 0; i < mCol; i++) {
            Bubble bubble = mBubbleArray[0][i];   // Bubble at row 0
            if (bubble.mBubbleColor != BubbleColor.BLANK) {
                // Search attached bubble with dfs
                dfs(bubble);
            }
        }

        // Update bubble after dfs
        for (int i = 0; i < mRow; i++) {
            for (int j = 0; j < mCol; j++) {
                Bubble bubble = mBubbleArray[i][j];
                // We remove floater if bubble undiscovered
                if (!bubble.mDiscover) {
                    bubble.setBlankBubble();
                } else {
                    // Reset discover
                    bubble.mDiscover = false;
                }
            }
        }
    }

    private void dfs(Bubble bubble) {
        bubble.mDiscover = true;
        for (Bubble b : bubble.mEdges) {
            if (!b.mDiscover && b.mBubbleColor != BubbleColor.BLANK) {
                dfs(b);
            }
        }
    }

    private int countTheRemovedBalls() {
        int counter = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 8; j++) {
                Bubble bubble = mBubbleArray[i][j];
                if (bubble.mBubbleColor.toString().equals("BLANK")) {
                    counter++;// Bubble at row 0
                }
            }
        }
        return counter;
    }
}
