package ch.fhnw.edu.cpib.parser.util;

import ch.fhnw.edu.cpib.scanner.enumerations.Types;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CastChecker {

    private static Map<Types, List<Types>> possibleCasts = new HashMap<>();

    static {
        // INT32 -> INT32:  possible
        // INT32 -> NAT32:  possible
        // INT32 -> BOOL:   not possible
        possibleCasts.put(Types.INT32, Arrays.asList(Types.INT32, Types.NAT32));

        // NAT32 -> INT32:  possible
        // NAT32 -> NAT32:  possible
        // NAT32 -> BOOL:   not possible
        possibleCasts.put(Types.NAT32, Arrays.asList(Types.INT32, Types.NAT32));

        // BOOL -> INT32:   not possible
        // BOOL -> NAT32:   not possible
        // BOOL -> BOOL:    not possible
        possibleCasts.put(Types.BOOL, Arrays.asList());
    }

    public static boolean isCastable(Types from, Types to) {
        return possibleCasts.get(from).contains(to);
    }

}
