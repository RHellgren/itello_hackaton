package se.itello.contest.ceres.teamx;


import se.itello.contest.ceres.api.*;
import se.itello.contest.ceres.api.ShipCommand;
import se.itello.contest.ceres.api.GameState;
import se.itello.contest.ceres.api.Brain;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class HobbitBrain implements Brain {
    private int index = -1;
    private Position target;
    private EntityState packet;
    private ArrayList<Position> nodes;

    public Collection<ShipCommand> commandsToSend(GameState state) {
        if(nodes == null) {
            nodes = new ArrayList<>();
            addNodes(state);
        }
        if(target == null || calculateDistance(state.getShipState().getPosition(), target) < 10) {
            target = chooseTarget(state);
        }

        Collection<ShipCommand> commands = new ArrayList<>();

        List<EntityState> entitiesInRadius = findThingsInZone(150.0, state);

        // Shoot all small asteroids
        for(int i = 0; i < entitiesInRadius.size(); i++) {
            if (entitiesInRadius.get(i).getSize() == 75) {
                if(calculateDistance(entitiesInRadius.get(i).getPosition(), state.getShipState().getPosition()) > 100)
                    break;
                Collection<ShipCommand> result = driveTowardsPosition(state, entitiesInRadius.get(i).getPosition());
                if (result != null) {
                    commands.addAll(driveTowardsPosition(state, entitiesInRadius.get(i).getPosition()));
                    commands.add(ShipCommand.SHOOT);
                    return commands;
                }
            }
            else { // large/medium asteroid
                return driveTowardsPosition(state,
                        findOppositePosition(state.getShipState().getPosition(), entitiesInRadius.get(i).getPosition()));
            }
        }

        return driveTowardsPosition(state, target);
    }

    private Position findOppositePosition(Position shipPosition, Position evilPosition) {
        double result_x = 0.0;
        double result_y = 0.0;

        result_x = shipPosition.getX() + (shipPosition.getX() - evilPosition.getX());
        result_y = shipPosition.getY() + (shipPosition.getY() - evilPosition.getY());

        return new Position(result_x, result_y);

    }

    private void addNodes(GameState state) {
        nodes.add(new Position(3*state.getUniverseInfo().getWidth()/4,3*state.getUniverseInfo().getHeight()/4));
        nodes.add(new Position(state.getUniverseInfo().getWidth()/4,3*state.getUniverseInfo().getHeight()/4));
        nodes.add(new Position(3*state.getUniverseInfo().getWidth()/4,state.getUniverseInfo().getHeight()/4));
        nodes.add(new Position(state.getUniverseInfo().getWidth()/4,state.getUniverseInfo().getHeight()/4));
    }

    private Position chooseTarget(GameState state) {
        index = (index + 1)  % nodes.size();
        return nodes.get(index);
    }

    private Collection<ShipCommand> rotateTowardsPosition(GameState state, Position target) {
        ShipState ship = state.getShipState();
        double xdist = target.getX() - ship.getPosition().getX();
        double ydist = target.getY() - ship.getPosition().getY();

        double angle = Math.toDegrees(Math.atan(ydist/xdist));


        if (xdist >= 0){
            if (ydist < 0){
                angle = 360 + angle;
            }
        } else {
            angle = 180 + angle;
        }


        if (Math.abs(angle - ship.getRotation()) < 5 && ship.getVelocity().getSpeed() > 150){
            return null;
        } else if (angle - ship.getRotation() > 0){
            return Collections.singleton(ShipCommand.TURN_PORT);
        } else {
            return Collections.singleton(ShipCommand.TURN_STARBOARD);
        }
    }

    private Collection<ShipCommand> driveTowardsPosition(GameState state, Position target) {
        ShipState ship = state.getShipState();
        double xdist = target.getX() - ship.getPosition().getX();
        double ydist = target.getY() - ship.getPosition().getY();

        double angle = Math.toDegrees(Math.atan(ydist/xdist));


        if (xdist >= 0){
            if (ydist < 0){
                angle = 360 + angle;
            }
        } else {
            angle = 180 + angle;
        }


        if (Math.abs(angle - ship.getRotation()) < 5){
            if (ship.getVelocity().getSpeed() > 100){
                return null;
            } else {
                return Collections.singleton(ShipCommand.THRUST);
            }
        } else if (angle - ship.getRotation() > 0){
            return Collections.singleton(ShipCommand.TURN_PORT);
        } else {
            return Collections.singleton(ShipCommand.TURN_STARBOARD);
        }
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

    private List<EntityState> findThingsInZone(double radius, GameState state) {

        List<EntityState> inRadius = new ArrayList<>();
        ShipState ship = state.getShipState();

        List<EntityState> enemyShips = new ArrayList<>(state.getEnemyStates());
        List<EntityState> asteroids = new ArrayList<>(state.getAsteroidStates());

        List<EntityState> allEntityStates = new ArrayList<>(enemyShips);
        allEntityStates.addAll(asteroids);

        Iterator<EntityState> iterator = allEntityStates.iterator();

        while(iterator.hasNext()) {
            EntityState next = iterator.next();
            if(calculateDistance(next.getPosition(), ship.getPosition()) <= radius) {
                inRadius.add(next);
            }
        }

        return inRadius;

    }

    private void sortAsteroids(List<EntityState> asteroids, List<EntityState> asteroids_ML,
                               List<EntityState> asteroids_S) {

        Iterator<EntityState> asteroids_iter = asteroids.iterator();

        while(asteroids_iter.hasNext()) {
            EntityState asteroid = asteroids_iter.next();
            if (asteroid.getSize() > 30) {
                asteroids_ML.add(asteroid);
            }
            else {
                asteroids_S.add(asteroid);
            }
        }

    }

    private double calculateDistance(Position a, Position b) {
        return Math.sqrt(Math.pow((b.getX() - a.getX()), 2.0) +  Math.pow((b.getY() - a.getY()), 2.0));
    }

    public String name() {
        return "Frodo";
    }
}
