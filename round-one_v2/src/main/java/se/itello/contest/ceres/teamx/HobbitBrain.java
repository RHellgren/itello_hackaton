package se.itello.contest.ceres.teamx;

import se.itello.contest.ceres.api.*;
import se.itello.contest.ceres.api.ShipCommand;
import se.itello.contest.ceres.api.GameState;

import java.util.Collection;
import java.util.Iterator;
import java.util.Collections;

public class HobbitBrain implements Brain {

    public Collection<ShipCommand> commandsToSend(GameState state) {

        EntityState packet = findClosestEntity(state);
        System.out.println("Packet: " + packet.getPosition().getX() + ", " + packet
                .getPosition().getY());
        ShipState ship = state.getShipState();

        double xdist = packet.getPosition().getX() - ship.getPosition().getX();
        double ydist = packet.getPosition().getY() - ship.getPosition().getY();

        double angle = Math.toDegrees(Math.atan(ydist/xdist));


        if (xdist >= 0){
            if (ydist < 0){
                angle = 360 + angle;
            }
        } else {
            angle = 180 + angle;
        }


        if (Math.abs(angle - ship.getRotation()) < 5){
            if (ship.getVelocity().getSpeed() > 50){
                return Collections.singleton(null);
            } else {
                return Collections.singleton(ShipCommand.THRUST);
            }
        } else {
            return Collections.singleton(ShipCommand.TURN_PORT);
        }

    }

    public String name() {
        return "Takin the hobbits to Isengard";
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

