package com.fluxtion.example.cookbook.nodecallback;

import com.fluxtion.runtime.callback.CallBackNode;
import lombok.Getter;

/**
 * This class extends {@link CallBackNode} which allows it to trigger a graph process cycle from an internal function
 * using the {@link CallBackNode#triggerGraphCycle()} with this node as the root of the processing cycle.
 */
public class CandidateVoteHandler extends CallBackNode {

    private int updateId;
    private String lastNewsStory = "";
    private int totalVotes;

    public CandidateVoteHandler(String name) {
        super(name);
    }

    public void newStory(VoteData<String> argument, int updateId) {
        this.updateId = updateId;
        this.lastNewsStory = argument.value();
        super.triggerGraphCycle();
    }

    public void voteCountUpdate(VoteData<Integer> argument, int updateId) {
        this.updateId = updateId;
        this.totalVotes += argument.value();
        super.triggerGraphCycle();
    }

    @Override
    public String toString() {
        return getName() +
                ", totalVotes=" + totalVotes +
                ", updateId=" + updateId +
                ", lastNewsStory='" + lastNewsStory + '\''
                ;
    }
}
