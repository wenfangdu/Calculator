package kt.leonbec.calculator

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.OnClickListener
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

private val STATE_PENDING_OPERATION = "PendingOperation"
private val STATE_OPERAND1 = "Operand1"
private val STATE_OPERAND1_STORED = "Operand1_Stored"

class MainActivity : AppCompatActivity() {

    // Variables to hold the operands and calculation type
    private var operand1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputML = mutableListOf<Button>(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnDot)
        val operationML = mutableListOf<Button>(btnEquals, btnDivide, btnMultiply, btnMinus, btnPlus)

        val inputListener = OnClickListener {
            etNewNumber.append((it as Button).text)
        }

        val operationListener = OnClickListener {
            val operation = (it as Button).text.toString()
            try {
                val value = etNewNumber.text.toString().toDouble()
                performOperation(value, operation)
            } catch (e: NumberFormatException) {
                etNewNumber.setText("")
            }
            pendingOperation = operation
            tvOperation.text = pendingOperation
        }
        inputML.forEach { it.setOnClickListener(inputListener) }
        operationML.forEach { it.setOnClickListener(operationListener) }

        btnNeg.setOnClickListener {
            val value = etNewNumber.text.toString()
            if (value.isEmpty())
                etNewNumber.setText("-")
            else try {
                var doubleValue = value.toDouble()
                doubleValue *= -1
                etNewNumber.setText(doubleValue.toString())
            } catch (e: NumberFormatException) {
                etNewNumber.setText("")
            }
        }

        btnClear.setOnClickListener {
            val value = etResult.text.toString()
            if (value.isNotEmpty()) {
                operand1 = null
                etResult.setText("")
                pendingOperation = ""
                tvOperation.text = pendingOperation
            }
        }
    }

    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null)
            operand1 = value
        else {
            if (pendingOperation == "=")
                pendingOperation = operation

            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) Double.NaN else operand1!! / value
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        etResult.setText(operand1.toString())
        etNewNumber.setText("")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        operand1 = if (savedInstanceState.getBoolean(STATE_OPERAND1_STORED, false))
            savedInstanceState.getDouble(STATE_OPERAND1)
        else null
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION)
        tvOperation.text = pendingOperation
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (operand1 != null) {
            outState.putDouble(STATE_OPERAND1, operand1!!)
            outState.putBoolean(STATE_OPERAND1_STORED, true)
        }
        outState.putString(STATE_PENDING_OPERATION, pendingOperation)
    }
}
