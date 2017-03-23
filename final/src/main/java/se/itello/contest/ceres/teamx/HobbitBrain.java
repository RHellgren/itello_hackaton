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

    private int entListSize = Integer.MAX_VALUE;
    private EntityState packet;
    private Position middle = null;

    public Collection<ShipCommand> commandsToSend(GameState state) {
        if(middle == null)
            middle = new Position(state.getUniverseInfo().getWidth()/2,state.getUniverseInfo().getHeight()/2);
        return driveTowardsPosition(state, middle);
        /*
        Collection<ShipCommand> commands = new ArrayList<>();

        List<EntityState> enemyShips = new ArrayList<>(state.getEnemyStates());

        List<EntityState> asteroids = new ArrayList<>(state.getAsteroidStates());
        List<EntityState> asteroids_ML = new ArrayList<>();
        List<EntityState> asteroids_S = new ArrayList<>();

        sortAsteroids(asteroids, asteroids_ML, asteroids_S);

        List<EntityState> entitiesInRadius = findThingsInZone(5.0, state.getShipState(), asteroids, enemyShips);


        return Collections.singleton(ShipCommand.THRUST);*/
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
                return Collections.singleton(null);
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

    private List<EntityState> findThingsInZone(double radius, ShipState ship, List<EntityState> asteroids,
                                               List<EntityState> enemyShips) {

        List<EntityState> inRadius = new ArrayList<>();
        List<EntityState> allEntityStates = new ArrayList<>(enemyShips);
        allEntityStates.addAll(asteroids);

        Iterator<EntityState> iterator = allEntityStates.iterator();

        while(iterator.hasNext()) {
            EntityState state = iterator.next();
            if(calculateDistance(state.getPosition(), ship.getPosition()) <= radius) {
                inRadius.add(state);
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
