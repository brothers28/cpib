package ch.fhnw.edu.cpib.parser.util;

import ch.fhnw.edu.cpib.scanner.enumerations.Types;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CastChecker {

    private static Map<Types, List<Types>> possibleCasts = new HashMap<>();

    static {
        // INT64 -> INT64:  possible
        // INT64 -> NAT64:  possible
        // INT64 -> BOOL:   not possible
        possibleCasts.put(Types.INT64, Arrays.asList(Types.INT64, Types.NAT64));

        // NAT64 -> INT64:  possible
        // NAT64 -> NAT64:  possible
        // NAT64 -> BOOL:   not possible
        possibleCasts.put(Types.NAT64, Arrays.asList(Types.INT64, Types.NAT64));

        // BOOL -> INT64:   not possible
        // BOOL -> NAT64:   not possible
        // BOOL -> BOOL:    not possible
        possibleCasts.put(Types.BOOL, Arrays.asList());
    }

    public static boolean isCastable(Types from, Types to){
        return possibleCasts.get(from).contains(to);
    }

}
