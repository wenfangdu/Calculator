package kt.leonbec.calculator

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.OnClickListener
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //Variables to hold the operands and calculation type
    private var operand1: Double? = null
    private var operand2 = 0.0
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputML = mutableListOf<Button>(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnDot)
        val operationML = mutableListOf<Button>(btnEquals, btnDivide, btnMultiply, btnMinus, btnPlus)

        val inputListener = OnClickListener {
            if (it is Button)
                etNewNumber.append(it.text)
        }

        val operationListener = OnClickListener {
            if (it is Button) {
                val operation = it.text.toString()
                val value = etNewNumber.text.toString()
                if (value.isNotBlank())
                    performOperation(value, operation)
                pendingOperation = operation
                tvOperation.text = pendingOperation
            }
        }
        inputML.forEach { it.setOnClickListener(inputListener) }
        operationML.forEach { it.setOnClickListener(operationListener) }
    }

    private fun performOperation(value: String, operation: String) {
        if (operand1 == null)
            operand1 = value.toDouble()
        else {
            operand2 = value.toDouble()
            if (pendingOperation == "=")
                pendingOperation = operation

            when (pendingOperation) {
                "=" -> operand1 = operand2
                "/" -> operand1 = if (operand2 == 0.0) Double.NaN else operand1!! / operand2
                "*" -> operand1 = operand1!! * operand2
                "-" -> operand1 = operand1!! - operand2
                "+" -> operand1 = operand1!! + operand2
            }
            etResult.setText(operand1.toString())
            etNewNumber.setText("")
        }
    }
}
