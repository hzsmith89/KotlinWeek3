package taxipark

import kotlin.math.floor
import kotlin.math.nextDown
import kotlin.math.roundToInt

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
    allDrivers.filter { driver ->  trips.none {it.driver == driver}}.toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
        allPassengers
            .filter { passenger ->
                trips.count{passenger in it.passengers } >= minTrips
            }.toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
        allPassengers.filter {  passenger ->
            trips.count{it.driver == driver && passenger in it.passengers} > 1
        }.toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    val (discount, fullFare) = trips.partition { it.discount is Double}
    return allPassengers.filter { passenger ->
            discount.count{it.passengers.contains(passenger)} >
                    fullFare.count{it.passengers.contains(passenger)}
    }.toSet()
}

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    return trips
        .groupBy { it.duration / 10 * 10..it.duration / 10 * 10 + 9 }
        .toList()
        .maxByOrNull { it.second.size }
        ?.first
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    val totalCost = trips.sumByDouble { it.cost }
    val topDriverNumber = floor(allDrivers.size * .2).toInt()
    val driverIncomeMap = mutableMapOf<Driver, Double>()

    trips.groupBy { it.driver }
        .forEach { (driver, trips) ->
            driverIncomeMap[driver] = trips.sumByDouble { it.cost }
        }

    var sum = 0.0
    var x = 0

    driverIncomeMap
        .toList()
        .sortedBy { (_, value) -> value }
        .reversed()
        .toMap()
        .forEach { (_, income) ->
            if (x < topDriverNumber) {
                sum += income
                x++
            }
        }

    return (sum / totalCost >= .8)
}