https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package assignment2;

import java.util.*;

public class Dynamic {

    /**
     * Returns the least cost that can be incurred by your company over the
     * k = hourlyVolume.length hours (i.e hour 0 to hour k-1) that you are
     * in charge of the pump. Given that a full service concluded the hour
     * before you were placed in charge of the system (i.e finished one hour
     * before hour 0), given parameters hourlyVolume, fullServiceCapacity,
     * regularServiceCapacity and minorServiceCapacity
     *
     * (See handout for details)
     *
     * This method must be implemented using an efficient bottom-up dynamic programming
     * solution to the problem (not memoised)
     *
     * @require the arrays hourlyVolume, fullServiceCapacity, regularServiceCapacity
     * and minorServiceCapacity are not null, and do not contain null values. Each
     * of the values in all arrays are non-negative (greater than or equal to 0).
     * fullServiceCapacity.length > 0, regularServiceCapacity.length > 0,
     * minorServiceCapacity.length > 0
     *
     * @ensure Returns the least cost that can be incurred by your company over the
     * k = hourlyVolume.length hours (i.e hour 0 to hour k-1) that you are
     * in charge of the pump. Given that a full service concluded the hour
     * before you were placed in charge of the system (i.e finished one hour
     * before hour 0), given parameters hourlyVolume, fullServiceCapacity,
     * regularServiceCapacity and minorServiceCapacity
     */
    public static int optimalLossDynamic(int[] hourlyVolume,
            int[] fullServiceCapacity, int [] regularServiceCapacity, int[] minorServiceCapacity) {

        // service hours
        int[] service_time = new int[]{4, 2, 1};

        int maxN = hourlyVolume.length;

        // dp table, represents the minimum cost
        // dp[currentHour, hourSinceLastService, lastService]
        // lastService: 0-full, 1-reg, 2-minor
        int[][][] dp = new int[maxN + 1][maxN + 1][3];

        for(int currentHour = maxN - 1; currentHour >= 0; currentHour--) {
            int targetVolume = hourlyVolume[currentHour];
            for(int hoursSinceService = currentHour; hoursSinceService >= 0; hoursSinceService--) {
                for(int serviceType = 2; serviceType >= 0; serviceType--) {
                    // cost of different choices
                    int[] cost = new int[]{targetVolume, targetVolume, targetVolume, targetVolume};

                    // 0-2: services
                    for(int currentService = 0; currentService < 3; currentService++) {
                        int len = service_time[currentService];
                        for(int i = 1; i < len && i+currentHour < maxN; i++) // service cost
                            cost[currentService] += hourlyVolume[currentHour + i];
                        if(currentHour + len < maxN) //  rest of the cost
                            cost[currentService] += dp[currentHour+len][0][currentService];
                    }

                    // 3: no service
                    switch (serviceType) {
                    case 0: // full
                        if(hoursSinceService < fullServiceCapacity.length)
                            cost[3] -= fullServiceCapacity[hoursSinceService];
                        break;
                    case 1: // regular
                        if(hoursSinceService < regularServiceCapacity.length)
                            cost[3] -= regularServiceCapacity[hoursSinceService];
                        break;
                    case 2: // minor
                        if(hoursSinceService < minorServiceCapacity.length)
                            cost[3] -= minorServiceCapacity[hoursSinceService];
                        break;
                    }
                    cost[3] = cost[3] > 0 ? cost[3] : 0;
                    if(currentHour+1 < maxN)
                        cost[3] += dp[currentHour+1][hoursSinceService+1][serviceType];

                    // find min
                    int min_cost = cost[0];
                    for(int i = 1; i < 4; i++) {
                        if(cost[i] < min_cost) {
                            min_cost = cost[i];
                        }
                    }
                    dp[currentHour][hoursSinceService][serviceType] = min_cost;
                }
            }
        }

        return dp[0][0][0];
    }


    /**
     * Returns a schedule of the services that should take place on each of the k
     * = hourlyVolume.length hours that you are in charge of the pump, that guarantees
     * that the least possible cost will be incurred by your company over these k
     * hours (given parameters hourlyVolume, fullServiceCapacity, regularServiceCapacity
     * and minorServiceCapacity)
     *
     * The schedule should be an array of services of length k, where for each array index
     * i, for 0 <= i < k, the value of the array at index i should be the service that is in
     * progress at that hour (Service.FULL_SERVICE, Service.REGULAR_SERVICE, Service.MINOR_SERVICE)
     * if there is a service or null if there is no service taking place at that time.
     *
     * For example, with a k value of 8, the return value
     * [null, null, REGULAR_SERVICE, REGULAR_SERVICE, null, null, MINOR_SERVICE, null]
     * represents a schedule where a regular service is conducted that takes place through the
     * third and fourth hours (hours 2 and 3) and a minor service is conducted in the seventh hour
     * (hour 6) and no services are conducted during the other hours.
     *
     * You should assume that a full service was completed the hour before you took control
     * of the pump (i.e 1 hour before hour 0)
     *
     * (See handout for details.)
     *
     * This method must be implemented using an efficient bottom-up dynamic programming solution
     * to the problem (not memoised)
     *
     * @require the arrays hourlyVolume, fullServiceCapacity, regularServiceCapacity
     * and minorServiceCapacity are not null, and do not contain null values. Each
     * of the values in all arrays are non-negative (greater than or equal to 0).
     * fullServiceCapacity.length > 0, regularServiceCapacity.length > 0,
     * minorServiceCapacity.length > 0
     *
     * @ensure Returns a schedule of the services that should take place on each of the k
     * = hourlyVolume.length hours that you are in charge of the pump, that guarantees
     * that the least possible cost will be incurred by your company over these k
     * hours (given parameters hourlyVolume, fullServiceCapacity, regularServiceCapacity
     * and minorServiceCapacity)
     */
    public static Service[] optimalServicesDynamic(int[] hourlyVolume,
            int[] fullServiceCapacity, int [] regularServiceCapacity, int[] minorServiceCapacity) {

        // service hours
        int[] service_time = new int[]{4, 2, 1, 1};

        int maxN = hourlyVolume.length;

        // dp table, represents the minimum cost
        // dp[currentHour, hourSinceLastService, lastService]
        // lastService: 0-full, 1-reg, 2-minor
        int[][][] dp = new int[maxN + 1][maxN + 1][3];
        // service chosen if available, 4 means no service
        int[][][] action = new int[maxN][maxN][3];

        for(int currentHour = maxN - 1; currentHour >= 0; currentHour--) {
            int targetVolume = hourlyVolume[currentHour];
            for(int hoursSinceService = currentHour; hoursSinceService >= 0; hoursSinceService--) {
                for(int serviceType = 2; serviceType >= 0; serviceType--) {
                    // cost of different choices
                    int[] cost = new int[]{targetVolume, targetVolume, targetVolume, targetVolume};

                    // 0-2: services
                    for(int currentService = 0; currentService < 3; currentService++) {
                        int len = service_time[currentService];
                        for(int i = 1; i < len && i+currentHour < maxN; i++) // service cost
                            cost[currentService] += hourlyVolume[currentHour + i];
                        if(currentHour + len < maxN) //  rest of the cost
                            cost[currentService] += dp[currentHour+len][0][currentService];
                    }

                    // 3: no service
                    switch (serviceType) {
                    case 0: // full
                        if(hoursSinceService < fullServiceCapacity.length)
                            cost[3] -= fullServiceCapacity[hoursSinceService];
                        break;
                    case 1: // regular
                        if(hoursSinceService < regularServiceCapacity.length)
                            cost[3] -= regularServiceCapacity[hoursSinceService];
                        break;
                    case 2: // minor
                        if(hoursSinceService < minorServiceCapacity.length)
                            cost[3] -= minorServiceCapacity[hoursSinceService];
                        break;
                    }
                    cost[3] = cost[3] > 0 ? cost[3] : 0;
                    if(currentHour+1 < maxN)
                        cost[3] += dp[currentHour+1][hoursSinceService+1][serviceType];

                    // find min
                    int min_cost = cost[0];
                    int choice = 0;
                    for(int i = 1; i < 4; i++) {
                        if(cost[i] < min_cost) {
                            min_cost = cost[i];
                            choice = i;
                        }
                    }
                    dp[currentHour][hoursSinceService][serviceType] = min_cost;
                    action[currentHour][hoursSinceService][serviceType] = choice;
                }
            }
        }

        // for(int i = 0; i < maxN; i++) {
        //     for(int j = 0; j < maxN; j++) {
        //         System.out.printf("{%3d %3d %3d} ", dp[i][j][0], dp[i][j][1], dp[i][j][2]);
        //     }
        //     System.out.println();
        // }
        // for(int i = 0; i < maxN; i++) {
        //     for(int j = 0; j < maxN; j++) {
        //         System.out.printf("{%d %d %d} ", action[i][j][0], action[i][j][1], action[i][j][2]);
        //     }
        //     System.out.println();
        // }

        Service[] result = new Service[maxN];
        for(int i = 0; i < maxN; i++)
            result[i] = null;

        int cur = 0;
        int lastService = 0;
        int timeSinceService = 0;
        while(cur < maxN) {
            int nextService = action[cur][timeSinceService][lastService];
            System.out.printf("%d: %d\t", cur, nextService);
            if(nextService < 3) {
                if(nextService == 0) { // record service
                    for(int i = 0; i < service_time[nextService] && cur+i < maxN; i++) {
                        result[cur+i] = Service.FULL_SERVICE;
                    }
                } else if(nextService == 1) {
                    for(int i = 0; i < service_time[nextService] && cur+i < maxN; i++) {
                        result[cur+i] = Service.REGULAR_SERVICE;
                    }
                } else if(nextService == 2) {
                    for(int i = 0; i < service_time[nextService] && cur+i < maxN; i++) {
                        result[cur+i] = Service.MINOR_SERVICE;
                    }
                }
                timeSinceService = 0;
            }
            cur += service_time[nextService];
            timeSinceService++;
            if(nextService < 3)
                lastService = nextService;
        }
        return result;
    }

}
