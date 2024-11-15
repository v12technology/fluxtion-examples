package com.fluxtion.example.cookbook.nodecallback;

import com.fluxtion.runtime.callback.CallBackNode;
import com.fluxtion.runtime.node.NamedNode;
import lombok.Getter;

/**
 * This class extends {@link CallBackNode} which allows it to trigger a graph process cycle from an internal function
 * using the {@link CallBackNode#fireCallback()} ()} with this node as the root of the processing cycle.
 */
public class CandidateVoteHandler extends CallBackNode implements NamedNode {

    private int updateId;
    private String lastNewsStory = "";
    private int totalVotes;
    private final String name;

    public CandidateVoteHandler(String name) {
        this.name = name;
    }

    public void newStory(VoteData<String> argument, int updateId) {
        this.updateId = updateId;
        this.lastNewsStory = argument.value();
        fireCallback();
    }

    public void voteCountUpdate(VoteData<Integer> argument, int updateId) {
        this.updateId = updateId;
        this.totalVotes += argument.value();
        fireCallback();
    }

    @Override
    public String toString() {
        return getName() +
                ", totalVotes=" + totalVotes +
                ", updateId=" + updateId +
                ", lastNewsStory='" + lastNewsStory + '\''
                ;
    }

    @Override
    public String getName() {
        return name;
    }
}
