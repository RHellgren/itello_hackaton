package se.itello.contest.ceres.teamx;

import se.itello.contest.ceres.api.*;
import se.itello.contest.ceres.api.ShipCommand;
import se.itello.contest.ceres.api.GameState;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by Daredevil on 2017-03-14.
 */
public class SuperSmartBrain implements Brain {
    public Collection<ShipCommand> commandsToSend(GameState state) {
        UniverseInfo universe_info = state.getUniverseInfo();
        ShipState ship_state = state.getShipState();
        EntityState next_entity = state.getAllEntityStates().iterator().next();
        EntityState next_contract = state.getContractStates().iterator().next();

        // Prints out what we know
        System.out.println("_____");
        System.out.println("Universe height: " + universe_info.getHeight() + " Universe width: " + universe_info.getWidth());
        System.out.println();
        System.out.println("Ship cartesian: " + ship_state.getPosition() + " Ship polar: " + cartesian_to_polar(ship_state.getPosition()));
        System.out.println("Ship rotation: " + ship_state.getRotation() + " Ship velocity: " + ship_state.getVelocity());
        System.out.println("Ship size: " + ship_state.getSize());
        System.out.println();
        System.out.println("(next) Entity cartesian: " + next_entity.getPosition() + " (next) Entity polar: " + cartesian_to_polar(next_entity.getPosition()));
        System.out.println("(next) Entity size: " + next_entity.getSize());
        System.out.println();
        System.out.println("(next) Contract cartesian: " + next_contract.getPosition() + " (next) Contract Polar: " + cartesian_to_polar(next_contract.getPosition()));
        System.out.println("(next) Contract size: " + next_contract.getSize());
        System.out.println("^^^^");
        System.out.println();

        /*
            Vad jag kan se så verkar next_entity === next_contract just nu, möjligt att det är tänkt att inkludera andra
            entities i kommande levels.

            Märkte från körningen av level1 att koordinaterna för skeppet inte verkar överensstämma med kontraktens koordinater,
            exempelvis så verkar första kontraktet [som ligger på (100.0,100.0)] plockas upp när skeppet passerar ~(100.0, 67.0),
            min teori är att koordinaterna överensstämmer med centrum på bägge och att kollisionen sker när deras yttre skal korsas.

            Skeppets hastighet verkar vara konstant ökande, vilket inte är så konstigt med tanke på 'THRUST', lyckas dock inte
            hitta något kommando som inte ökar hastigheten till nästa runda.
         */

        // Dummy example of how the ship could be controlled, doesn't work unfortunately :-(
        ShipCommand command;
        if (ship_state.getPosition().getX() - next_entity.getPosition().getX() > 10)
            command = ShipCommand.TURN_PORT;
        else if (next_entity.getPosition().getX() - ship_state.getPosition().getX() > 10)
            command = ShipCommand.TURN_STARBOARD;
        else
            command = ShipCommand.THRUST;

        return Collections.singleton(command);
    }

    /*
        Not sure but I think this needs to be here? Probably needs a cooler name tho :-D
     */
    public String name() {
        return "Super Smart";
    }

    /*
        Converts polar coordinates (r,phi) to cartesian coordinates (x,y)
        TODO: Double check the math please!
     */
    private Position polar_to_cartesian(Position coordinates) {
        double cartesian_x = coordinates.getX()*Math.cos(coordinates.getY());
        double cartesian_y = coordinates.getX()*Math.sin(coordinates.getY());
        return new Position(cartesian_x,cartesian_y);
    }

    /*
        Converts cartesian coordinates (x,y) to polar coordinates (r,phi)
        TODO: Double check the math please!
     */
    private Position cartesian_to_polar(Position coordinates) {
        double polar_r = Math.sqrt((coordinates.getX()*coordinates.getX())+(coordinates.getY()*coordinates.getY()));
        double polar_phi = Math.atan(coordinates.getY()/coordinates.getX());
        return new Position(polar_r,polar_phi);
    }

    /*
        Just regular old Pythagoras
     */
    private double distance_between_coords(Position first_coords, Position second_coords) {
        double diff_x = first_coords.getX() - second_coords.getX();
        double diff_y = first_coords.getY() - second_coords.getY();
        return Math.sqrt((diff_x*diff_x) + (diff_y*diff_y));
    }

    /*
        TODO: Vector subtraction, not sure how or why it's needed yet
     */
}
