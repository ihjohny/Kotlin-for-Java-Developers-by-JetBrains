package taxipark

import java.util.Arrays.sort

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
/*        allDrivers.filter { driver -> trips.none { it.driver == driver } }.toSet()*/
        allDrivers.minus(trips.map { it.driver })


/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
        allPassengers.filter { passenger ->
            minTrips <= trips.count {
                it.passengers.contains(passenger)
            }
        }.toSet()
/*    trips
            .flatMap(Trip::passengers)
            .groupBy { it: Passenger -> it }
            .filterValues { group -> group.size >= minTrips}
            .keys*/

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
        trips
                .filter { trip -> trip.driver == driver }
                .flatMap(Trip::passengers)
                .groupBy { passenger -> passenger }
                .filterValues { it.size > 1 }
                .keys
/*        allPassengers.filter { passenger ->
            trips.count { it.driver == driver && passenger in it.passengers } > 1
        }.toSet()*/

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    val (tripsWithDiscount, tripsWithoutDiscount) = trips.partition { it.discount != null }
    return allPassengers
            .filter { passenger ->
                tripsWithDiscount.count { passenger in it.passengers } >
                        tripsWithoutDiscount.count { passenger in it.passengers }
            }
            .toSet()
}

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
/*    var count = IntArray(100000)
    for (trip in trips) {
        count[trip.duration]++
    }
    var max = 0
    var sum = 0
    var uperBound = 0
    for (i in count.indices) {
        if(i%10 == 0 || i == 0) {
            if(sum > max) {
                max = sum
                uperBound = i - 1
            }
            sum = 0
        }
        sum += count[i]
    }
    if(uperBound < 9)
        return null
    return (uperBound - 9)..(uperBound)*/
    return trips
            .groupBy {
                val start = it.duration / 10 * 10
                val end = start + 9
                start..end
            }
            .toList()
            .maxBy { (_, group)-> group.size
            }?.first
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
/*    var incomes = DoubleArray(allDrivers.size)
    var totalIncome= 0.0
    for (trip in trips) {
        totalIncome += trip.cost
        allDrivers.forEachIndexed { index, driver ->
            if(driver.name == trip.driver.name) {
                incomes[index] += trip.cost
            }
        }
    }
    sort(incomes)
    var driverPercent = (allDrivers.size * .2).toInt()
    var incomePercent = totalIncome * .8
    var costs = 0.0
    for (i in allDrivers.size-1 downTo  (allDrivers.size  - driverPercent)){
        costs += incomes[i]
    }
    if(trips.isEmpty()) {
       return false
    }
    return costs >= incomePercent*/
    if (trips.isEmpty()) {
        return false
    }
    val totalIncome = trips.sumByDouble { it.cost }
    val sortedDrivesIncome: List<Double> =
            trips
                    .groupBy { it.driver }
                    .map { (_, tripsByDriver) -> tripsByDriver.sumByDouble { it.cost } }
                    .sortedDescending()

    val numberOfTopDriver = (0.2 * allDrivers.size).toInt()
    val incomeByTopDriver = sortedDrivesIncome
            .take(numberOfTopDriver)
            .sum()
    return incomeByTopDriver >= 0.8 * totalIncome
}