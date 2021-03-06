package com.example.cupcake.model

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.cupcake.R
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_PER_CUPCAKE = 2.00
private const val PRICE_FOR_SOME_DAY_PICKUP = 3.00

class OrderViewModel: ViewModel() {
    private val _quantity = MutableLiveData<Int> ()
    val quantity: LiveData<Int> get() = _quantity

    private val _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> get() = _flavor

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> get() = _date

    private val _price = MutableLiveData<Double>()
    val price: LiveData<String> get() = Transformations.map(_price) {
        NumberFormat.getCurrencyInstance().format(it)
    }

    val dateOptions = getPickupOptions()

    init {
        resetOrder()
    }

    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
        updatePrice()
    }

    fun hasNoFlavorSet(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }

    fun setFlavor(desiredFlavor: String) {
        _flavor.value = desiredFlavor

    }

    private fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()

        repeat(4) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }

        return options
    }


    fun resetOrder() {
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[0]
        _price.value = 0.0
    }

    fun cancelOrder(fragment: Fragment ,navigationAction: Int) {
        resetOrder()
        findNavController(fragment).navigate(navigationAction)
    }

    private fun updatePrice() {
        var calculatedPrice = ((quantity.value ?: 0) * PRICE_PER_CUPCAKE)

        if (_date.value == dateOptions[0]){
            calculatedPrice += PRICE_FOR_SOME_DAY_PICKUP

        }

        _price.value = calculatedPrice

    }

    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        updatePrice()
    }

}