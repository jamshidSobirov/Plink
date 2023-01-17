package com.BibaSpirt.fragment;

import com.BibaSpirt.bubble.BubbleColor;

public interface Observers {
    void observeGameOver(Boolean gameOver);
    void observeScore(int score);
    void ballsFinished();
    void shotBubbleColor(BubbleColor bubbleColor);
}
