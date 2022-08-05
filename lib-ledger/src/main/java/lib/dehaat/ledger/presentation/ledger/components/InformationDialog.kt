package lib.dehaat.ledger.presentation.ledger.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import lib.dehaat.ledger.R
import lib.dehaat.ledger.resources.text14Sp

@SuppressLint("UnrememberedMutableState")
@Preview(
    name = "InformationDialog Preview",
    showBackground = true
)
@Composable
fun InformationDialogPreview() {
    InformationDialog(title = "Here is a dummy information")
}

@Composable
fun InformationDialog(
    title: String,
    dismissDialog: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = {
            dismissDialog()
        }
    ) {
        Card(
            shape = RoundedCornerShape(4.dp),
            elevation = 8.dp
        ) {
            Column(Modifier.padding(8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_cancel),
                        contentDescription = "",
                        tint = Color.Black,
                        modifier = Modifier.clickable {
                            dismissDialog()
                        },
                    )
                }

                Text(
                    modifier = Modifier.padding(8.dp),
                    text = title,
                    style = text14Sp(),
                    color = Color.Black
                )
            }
        }
    }
}
