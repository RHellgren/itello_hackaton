package se.itello.contest.ceres.teamx;

import se.itello.contest.ceres.api.*;
import se.itello.contest.ceres.api.ShipCommand;
import se.itello.contest.ceres.api.GameState;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class HobbitBrain implements Brain {

    public Collection<ShipCommand> commandsToSend(GameState state) {return Collections.singleton(ShipCommand.THRUST);}

    public String name() {
        return "Full Throttle";
    }

    private EntityState findClosestEntity(GameState state) {
        Iterator<EntityState> entities_iter = state.getContractStates().iterator();
        Position shipPosition = state.getShipState().getPosition();

        EntityState tempEntity, closestEntity = null;
        double tempDistance, closestDistance = Double.MAX_VALUE;

        while(entities_iter.hasNext()) {
            tempEntity = entities_iter.next();
            tempDistance = calculateDistance(shipPosition, tempEntity.getPosition());

            if (tempDistance < closestDistance) {
                closestDistance = tempDistance;
                closestEntity = tempEntity;
            }
        }

        return closestEntity;
    }

    // Note: Sqrt not needed as the distances will still have same relations
    private double calculateDistance(Position a, Position b) {
        return Math.pow((b.getX() - a.getX()), 2.0) +  Math.pow((b.getY() - a.getY()), 2.0);
    }
}

