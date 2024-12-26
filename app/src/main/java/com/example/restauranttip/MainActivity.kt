package com.example.restauranttip

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.restauranttip.components.InputField
import com.example.restauranttip.ui.theme.LightPurple
import com.example.restauranttip.ui.theme.RestaurantTipTheme
import com.example.restauranttip.util.calculateTotalTip
import com.example.restauranttip.util.totalPerPerson
import com.example.restauranttip.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Myapp{
                MainContent()
            }

        }
    }
}

@Composable
fun Myapp(content: @Composable () -> Unit){
    RestaurantTipTheme {

            Surface(color = MaterialTheme.colorScheme.background) {
                MainContent()
            }
        }}


@Composable
fun TopHeader(totalPerPerson: Double = 0.0) {
    Surface(modifier = Modifier
        .padding(20.dp)
        .fillMaxWidth()
        .height(150.dp),
        color = LightPurple, shape = RoundedCornerShape(corner = CornerSize(20.dp)) )  {
        Column(modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            val total = "%.2f".format(totalPerPerson)
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.headlineMedium

            )
            Text(
                text = "$$total",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )

        }
    }

}
@Preview
@Composable
fun MainContent(){
    BillForm(){ billAmt ->
        Log.d("AMT", "MainContent: $billAmt")

    }
}




@Composable
fun BillForm(modifier: Modifier = Modifier,
             onValChange: (String)->Unit = {}
) {
    val totalPerPersonState = remember {
        mutableStateOf(0.00)
    }
    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val splitBillState = remember {
        mutableIntStateOf(1)
    }

    val sliderPositionState = remember {
        mutableFloatStateOf(0f)
    }
    val tipPercentageState = remember {
        mutableIntStateOf(0)
    }


    val range = IntRange(start = 1, endInclusive =100 )

    val tipAmountState = remember {
        mutableDoubleStateOf(0.00)
    }
fun calculateTotalPerPerson(){
    totalPerPersonState.value =  totalPerPerson(bill = totalBillState.value.toDouble(), tipAmount = tipAmountState.value, splitCount = splitBillState.value )
}

    Column(modifier = Modifier.padding(horizontal = 15.dp)) {
        TopHeader(totalPerPersonState.value)
        Surface(
            modifier = Modifier
                .padding(horizontal = 1.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(8.dp)),
            border = BorderStroke(width = 2.dp, color = Color.LightGray)
        ) {
            Column(
                modifier = Modifier.padding(6.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                InputField(valueState = totalBillState, labelId = "Enter Bill", enabled = true, onValueChange =  {
                    totalBillState.value = it.trim()
                    calculateTotalPerPerson()
                },
                    isSingleLine = true, onAction = KeyboardActions {
                        if (!validState) return@KeyboardActions


                        if (keyboardController != null) {
                            keyboardController.hide()
                            calculateTotalPerPerson()
                        }

                    }
                )
                if(validState) {

                Row(
                    modifier = Modifier.padding(3.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "split",
                        modifier = Modifier.align(
                            alignment = Alignment.CenterVertically
                        )
                    )
                    Spacer(modifier = Modifier.width(120.dp))
                    // minus button
                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        RoundIconButton(
                            imageVector = Icons.Default.Remove,
                            onClick = {
                                if (splitBillState.value > 1) {
                                    splitBillState.value -= 1
                                    totalPerPersonState.value =  totalPerPerson(bill = totalBillState.value.toDouble(), tipAmount = tipAmountState.value, splitCount = splitBillState.value )

                                }
                            }
                        )

                        Text(
                            text = "${splitBillState.value}",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 9.dp, end = 9.dp)
                        )

                        RoundIconButton(
                            imageVector = Icons.Default.Add,
                            onClick = {
                                if(splitBillState.value < range.last){ splitBillState.value += 1
                                    totalPerPersonState.value =  totalPerPerson(bill = totalBillState.value.toDouble(), tipAmount = tipAmountState.value, splitCount = splitBillState.value )
                                }
                            })
                    }
                }// row 1 split buttons
                //tiprow
                Row(modifier = Modifier.padding(horizontal = 3.dp, vertical = 12.dp)) {
                    Text(
                        text = "Tip",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(200.dp))
                    Text(text = String.format("$%.2f", tipAmountState.value))

                }
                //slider column contains the slider and the percentage of the slider
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "${tipPercentageState.value}%")
                    Spacer(modifier = Modifier.height(14.dp))
                    //slider
                    Slider(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                        steps = 5,
                        value = sliderPositionState.value,
                        onValueChange = { newVal ->
                            sliderPositionState.value = newVal

                            // Dynamically calculate tipPercentage inside this block
                            tipPercentageState.value= (newVal * 100).toInt()

                            // Update tipAmountState based on the new tipPercentage
                            tipAmountState.value =
                                calculateTotalTip(
                                    totalBill = totalBillState.value.toDoubleOrNull() ?: 0.0,
                                    tipPercentage = tipPercentageState.value
                                )
                            val totalBill = totalBillState.value.toDoubleOrNull() ?: 0.0
                            totalPerPersonState.value =  totalPerPerson(bill = totalBill, tipAmount = tipAmountState.value, splitCount = splitBillState.value )


                            Log.d("DEBUG", "Slider Value: $newVal, Tip Percentage: ${tipPercentageState.value}, Tip Amount: ${tipAmountState.doubleValue}")
                        }
                    )


                }

                            }
                            else {
                                Box(){}
                            }
            }

        }
    }

}

//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    RestaurantTipTheme {
//        Myapp {
//            Text(text = "Hello again")
//        }
//    }
//}