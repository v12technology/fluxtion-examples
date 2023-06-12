package com.fluxtion.example.cookbook.nodecallback;

import com.fluxtion.runtime.annotations.OnParentUpdate;
import com.fluxtion.runtime.annotations.OnTrigger;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Node class that is part of an {@link com.fluxtion.runtime.EventProcessor} graph.Methods
 * annotated with {@link OnTrigger} and {@link OnParentUpdate} annotations receive callbacks during a graph cycle.
 *
 * @param candidateVoteHandlers
 */
public record ElectionTracker(List<CandidateVoteHandler> candidateVoteHandlers) {
    @OnParentUpdate
    public void updatedCandidateStatus(CandidateVoteHandler candidateVoteHandler) {
        System.out.println("update for:" + candidateVoteHandler.getName());
    }

    @OnTrigger
    public boolean printLatestResults() {
        String result = candidateVoteHandlers.stream()
                .map(Objects::toString)
                .collect(Collectors.joining("\n\t", "\t", "\n\n"));
        System.out.println(result);
        return true;
    }
}
