import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.lab_3.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import viewModel.PriceDialogViewModel

class PriceDialog : DialogFragment() {

    private val viewModel: PriceDialogViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_price, null)

        val brandSpinner = view.findViewById<Spinner>(R.id.brandSpinner)
        val modelSpinner = view.findViewById<Spinner>(R.id.modelSpinner)
        val yearSpinner = view.findViewById<Spinner>(R.id.yearSpinner)
        val priceText = view.findViewById<TextView>(R.id.priceText)

        var brandSpinnerPressed = -1
        var modelSpinnerPressed = -1
        var yearSpinnerPressed = -1

        viewModel.loadBrands()

        builder.setView(view)
            .setTitle("Узнать цену")
            .setNegativeButton("Закрыть") { dialog, _ -> dialog.dismiss() }

        brandSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                Log.d("brand1", "brandSpinner = " + brandSpinnerPressed.toString() + "position = " + position.toString())
                val brandId = viewModel.brands.value.getOrNull(position)?.codigo ?: return
                if(brandSpinnerPressed != -1) {
                    viewModel.clearModels()
                    modelSpinnerPressed = -1
                    viewModel.clearYears()
                    yearSpinnerPressed = -1
                    viewModel.clearPrice()
                    viewModel.loadModels(brandId)
                }
                brandSpinnerPressed = position
                Log.d("brand2", "brandSpinner = " + brandSpinnerPressed.toString() + "position = " + position.toString())

            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                return
            }
        }


        modelSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val brandId =
                    viewModel.brands.value.getOrNull(brandSpinner.selectedItemPosition)?.codigo
                        ?: return
                val modelCode =
                    viewModel.models.value.getOrNull(position)?.codigo?.toIntOrNull() ?: return

                if (modelSpinnerPressed != -1) {
                    viewModel.clearYears()
                    yearSpinnerPressed = -1
                    viewModel.clearPrice()
                    viewModel.loadYears(brandId, modelCode)
                }
                modelSpinnerPressed = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val brandId = viewModel.brands.value.getOrNull(brandSpinner.selectedItemPosition)?.codigo ?: return
                val modelCode = viewModel.models.value.getOrNull(modelSpinner.selectedItemPosition)?.codigo?.toIntOrNull() ?: return
                val yearCode = viewModel.years.value.getOrNull(position)?.codigo ?: return
                if(yearSpinnerPressed != -1) {
                    viewModel.clearPrice()
                    viewModel.loadPrice(brandId, modelCode, yearCode)
                }
                yearSpinnerPressed = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        lifecycleScope.launch {
            viewModel.brands.collectLatest { brands ->
                brandSpinner.adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    brands.map { it.nome }
                )
            }
        }

        lifecycleScope.launch {
            viewModel.models.collectLatest { models ->
                modelSpinner.adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    models.map { it.nome }
                )
            }
        }

        lifecycleScope.launch {
            viewModel.years.collectLatest { years ->
                yearSpinner.adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    years.map { it.nome }
                )
            }
        }

        lifecycleScope.launch {
            viewModel.price.collectLatest { price ->
                price?.let {
                    priceText.text = "Цена: ${it.Valor}"
                }
            }
        }

        viewModel.loadBrands()

        return builder.create()
    }


    override fun onDestroy() {
        viewModel.cancelJobs()
        Log.d("Job", "canceled")
        super.onDestroy()
    }
}
