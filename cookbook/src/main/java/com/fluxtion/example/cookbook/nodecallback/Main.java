package com.fluxtion.example.cookbook.nodecallback;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.callback.CallBackNode;

import java.util.List;

/**
 * Example that demonstrates invoking instances in a graph by direct function calls and triggering a graph processing
 * cycle. Dependencies of the callback node are triggered from invoking a normal java method, no events are sent to
 * the event processor.
 * <p>
 * The graph is a simple one:
 * <pre>
 *
 *     RED_PARTY                   BLUE_PARTY                  GREEN_PARTY
 *       |                             |                            |
 *       |                             |                            |
 *       |                             |                            |
 *       \_____________________________|____________________________/
 *                                     |
 *                                     |
 *                                     |
 *                                ElectionTracker
 * </pre>
 * <p>
 * Each {@link CandidateVoteHandler} handles the same type of generic arguments, if these were passed to the containing
 * event processor to handle all the CandidateVoteHandlers would be triggered. Using the {@link com.fluxtion.runtime.callback.CallBackNode}
 * as a base class allows the CandidateVoteHandler's to receive normal function calls and then trigger a processing cycle,
 * by calling {@link CallBackNode#fireCallback()} ()}
 */
public class Main {

    public static void main(String[] args) throws NoSuchFieldException {
        var voteProcessor = Fluxtion.compile(c -> c.addNode(new ElectionTracker(List.of(
                new CandidateVoteHandler("Red_party"),
                new CandidateVoteHandler("Blue_party"),
                new CandidateVoteHandler("Green_party")
        ))));
        voteProcessor.init();

        //get the nodes by name from the graph
        CandidateVoteHandler redPartyNode = voteProcessor.getNodeById("Red_party");
        CandidateVoteHandler bluePartyNode = voteProcessor.getNodeById("Blue_party");
        CandidateVoteHandler greenPartyNode = voteProcessor.getNodeById("Green_party");

        //invoke functions directly on nodes - creates a
        redPartyNode.voteCountUpdate(new VoteData<>(25), 1);
        bluePartyNode.voteCountUpdate(new VoteData<>(12), 2);
        bluePartyNode.voteCountUpdate(new VoteData<>(19), 3);
        bluePartyNode.voteCountUpdate(new VoteData<>(50), 4);
        redPartyNode.newStory(new VoteData<>("red alert!!"), 5);
        greenPartyNode.newStory(new VoteData<>("green and gone :("), 6);
        greenPartyNode.voteCountUpdate(new VoteData<>(2), 1);
    }

}
