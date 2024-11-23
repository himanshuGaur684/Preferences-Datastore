package gaur.himanshu.preferencesdatastore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gaur.himanshu.preferencesdatastore.ui.theme.PreferencesDatastoreTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val preferencesDataStore by lazy { PreferencesDataStore(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PreferencesDatastoreTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding), preferencesDataStore)
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    preferencesDataStore: PreferencesDataStore
) {

    val key = remember {
        mutableStateOf("")
    }
    val value = remember { mutableStateOf("") }
    val storedInfo = remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(value = key.value, onValueChange = {
            key.value = it
        }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(12.dp))

        TextField(value = value.value, onValueChange = {
            value.value = it
        }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = storedInfo.value, style = MaterialTheme.typography.headlineLarge)

        Button(onClick = {
            scope.launch(Dispatchers.IO) {
                preferencesDataStore.saveString(key.value, value.value)
            }
        }) {
            Text("Save")
        }

        Button(onClick = {
            scope.launch(Dispatchers.IO) {
                storedInfo.value = preferencesDataStore.getString(key.value).toString()
            }
        }) {
            Text(text = "Get info")
        }
    }
}
