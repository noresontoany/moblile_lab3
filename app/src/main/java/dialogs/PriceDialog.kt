package dialogs

import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import viewModel.PriceDialogViewModel

class PriceDialog : DialogFragment() {
    private val viewModel : PriceDialogViewModel by viewModel()
}