package se.itello.contest.ceres.teamx;


import se.itello.contest.ceres.api.*;
import se.itello.contest.ceres.api.ShipCommand;
import se.itello.contest.ceres.api.GameState;
import se.itello.contest.ceres.api.Brain;

import javax.swing.text.html.parser.Entity;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class HobbitBrain implements Brain {

    private int entListSize = Integer.MAX_VALUE;
    private EntityState packet;

    public Collection<ShipCommand> commandsToSend(GameState state) {

        Collection<ShipCommand> commands = new Collection<ShipCommand>();

        List<EntityState> enemyShips = new ArrayList<>(state.getEnemyStates());

        List<EntityState> asteroids = new ArrayList<>(state.getAsteroidStates());
        List<EntityState> asteroids_ML = new ArrayList<EntityState>();
        List<EntityState> asteroids_S = new ArrayList<EntityState>();

        sortAsteroids(asteroids, asteroids_ML, asteroids_S);

        List<EntityState> entitiesInRadius = findThingsInZone(5.0, state.getShipState(), asteroids, enemyShips);



        return Collections.singleton(ShipCommand.THRUST);



    }


    private List<EntityState> findThingsInZone(double radius, ShipState ship, List<EntityState> asteroids,
                                               List<EntityState> enemyShips) {

        List<EntityState> inRadius = new ArrayList<>();
        List<EntityState> allEntityStates = new ArrayList<>(enemyShips).addAll(asteroids);

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

    // Note: Sqrt not needed as the distances will still have same relations
    private double calculateDistance(Position a, Position b) {
        return Math.sqrt(Math.pow((b.getX() - a.getX()), 2.0) +  Math.pow((b.getY() - a.getY()), 2.0));
    }

    public String name() {
        return "Frodo";
    }
}
