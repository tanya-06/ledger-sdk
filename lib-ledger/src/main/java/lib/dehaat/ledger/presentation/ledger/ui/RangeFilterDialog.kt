package lib.dehaat.ledger.presentation.ledger.ui

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.util.*

@Composable
fun RangeFilterDialog(
    startRange: (Long) -> Unit,
    endRange: (Long) -> Unit
) {
    val date = Date().time
    Toast.makeText(LocalContext.current, date.toString(), Toast.LENGTH_LONG).show()
}
