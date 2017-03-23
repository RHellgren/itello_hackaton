package se.itello.contest.ceres.teamx;


import javafx.geometry.Pos;
import se.itello.contest.ceres.api.*;
import se.itello.contest.ceres.api.ShipCommand;
import se.itello.contest.ceres.api.GameState;
import se.itello.contest.ceres.api.Brain;

import java.util.*;

public class HobbitBrain implements Brain {

    private int entListSize = Integer.MAX_VALUE;
    private EntityState packet;
    private Position polestar = new Position(0.0, 0.0);


    public Collection<ShipCommand> commandsToSend(GameState state) {
        ShipState ship = state.getShipState();
        return Collections.singleton(ShipCommand.THRUST);

    }


    // Note: Sqrt not needed as the distances will still have same relations
    private double calculateDistance(Position a, Position b) {
        return Math.pow((b.getX() - a.getX()), 2.0) +  Math.pow((b.getY() - a.getY()), 2.0);
    }




    private EntityState getClosestEnemy(GameState state) {
        ShipState ship = state.getShipState();
        List<EntityState> enemies = new ArrayList<>(state.getEnemyStates());
        int closestEnemy = -1;
        double closestDistance = Double.MAX_VALUE;
        for (int i=0; i<enemies.size(); i++) {
            if(calculateDistance(ship.getPosition(), enemies.get(i).getPosition()) < closestDistance) {
                closestDistance = calculateDistance(ship.getPosition(), enemies.get(i).getPosition());
                closestEnemy = i;
            }
        }

        return enemies.get(closestEnemy);
    }


    public String name() {
        return "Frodo";
    }
}
