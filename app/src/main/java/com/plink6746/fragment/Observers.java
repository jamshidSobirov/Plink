package com.plink6746.fragment;

import com.plink6746.bubble.BubbleColor;

public interface Observers {
    void observeGameOver(Boolean gameOver);
    void observeScore(int score);
    void ballsFinished();
    void shotBubbleColor(BubbleColor bubbleColor);
}
