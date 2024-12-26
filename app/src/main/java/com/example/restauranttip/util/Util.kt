package com.example.restauranttip.util

fun calculateTotalTip(totalBill: Double, tipPercentage: Int): Double {
    return if (totalBill >= 1 && totalBill.toString().isNotEmpty())
        (totalBill * tipPercentage)/ 100 else 0.0
}

fun totalPerPerson(bill: Double, tipAmount: Double, splitCount: Int): Double {
    return if (bill >= 1 && splitCount > 0)
        String.format("%.2f", (bill + tipAmount) / splitCount).toDouble()
    else 0.0
}
