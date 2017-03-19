package se.itello.contest.ceres.teamx;

import se.itello.contest.ceres.api.*;
import se.itello.contest.ceres.api.ShipCommand;
import se.itello.contest.ceres.api.GameState;

import java.util.Collection;
import java.util.Collections;

public class FullThrottleBrain implements Brain {

    public Collection<ShipCommand> commandsToSend(GameState state) {
        return Collections.singleton(ShipCommand.THRUST);
    }

    public String name() {
        return "Full Throttle";
    }
}
