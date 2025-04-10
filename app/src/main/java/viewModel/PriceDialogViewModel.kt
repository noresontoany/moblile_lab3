package viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PriceDialogViewModel : ViewModel() {
    val dialogState = MutableLiveData<DialogState>()
    val errorMessage = MutableLiveData<String?>()
}