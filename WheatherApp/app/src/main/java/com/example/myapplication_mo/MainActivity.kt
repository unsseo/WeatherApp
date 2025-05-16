import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication_mo.R
import com.example.myapplication_mo.ui.theme.MyApplication_moTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplication_moTheme {
                MainScreen()
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {
    var showPopup by remember { mutableStateOf(false) }

    val today = remember{
        java.time.LocalDate.now().format(
            java.time.format.DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
        )
    }

    // 팝업이 나타나면 6초 뒤에 사라지게 함
    if (showPopup) {
        LaunchedEffect(Unit) {
            delay(6000)
            showPopup = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        
        Text(
            text =today,
            color = Color(0xFF80BA80),
            modifier=Modifier
                .align(Alignment.TopCenter)
                .padding(top =32.dp),
            style = MaterialTheme.typography.titleMedium
        )

        Button(
            onClick = { showPopup = true },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp)
        ) {
            Text("미세먼지 농도 확인")
        }

        if (showPopup) {
            DustLevelPopup(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun DustLevelPopup(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(242.dp)
            .height(134.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.dust_level_group),
            contentDescription = "팝업 배경",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text("미세먼지 농도 기준", color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text("0~30: 좋음\n31~80: 보통\n81~150: 나쁨\n151~: 매우 나쁨", color = Color.Black)
        }
    }
}
