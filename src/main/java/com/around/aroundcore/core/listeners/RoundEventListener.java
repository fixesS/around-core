package com.around.aroundcore.core.listeners;

import com.around.aroundcore.core.events.OnActivateRoundEvent;
import com.around.aroundcore.core.events.OnDeactivateRoundEvent;
import com.around.aroundcore.core.events.OnTeamRewardingEvent;
import com.around.aroundcore.core.services.TeamRewardingService;
import com.around.aroundcore.database.models.round.Round;
import com.around.aroundcore.database.services.round.RoundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RoundEventListener {
    private final TeamRewardingService teamRewardingService;
    private final RoundService roundService;

    @EventListener
    @Async
    public void handleRewardingTeam(OnTeamRewardingEvent event) {
        teamRewardingService.rewardTeamsForRound(event.getRound());
    }

    @EventListener
    public void handleActivationEvent(OnActivateRoundEvent event) {
        Round round = event.getRound();
        round.activate();
        roundService.save(round);
    }
    @EventListener
    public void handleDeactivationEvent(OnDeactivateRoundEvent event) {
        Round round = event.getRound();
        round.deactivate();
        roundService.save(round);
    }
}
