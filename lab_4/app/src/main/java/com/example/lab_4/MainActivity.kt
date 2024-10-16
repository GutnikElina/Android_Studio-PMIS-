package com.example.lab_4

import android.Manifest
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lab_4.ui.theme.Lab_4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab_4Theme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Column(Modifier.padding(8.dp)) {
        NavHost(
            navController, startDestination = NavRoutes.Home.route,
            modifier = Modifier.weight(1f)
        ) {
            composable(NavRoutes.Home.route) { Greeting() }
            composable(NavRoutes.Lists.route) { Lists(navController) }
            composable(NavRoutes.ImageScreen.route) { ImageScreen() }
            composable(NavRoutes.ImagePickerScreen.route) { ImagePickerScreen(navController) } // Новый экран
        }
        BottomNavigationBar(navController = navController)
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        NavBarItems.BarItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = navItem.image,
                        contentDescription = navItem.title
                    )
                },
                label = {
                    Text(text = navItem.title)
                }
            )
        }
    }
}

object NavBarItems {
    val BarItems = listOf(
        BarItem(
            title = "Home",
            image = Icons.Filled.Home,
            route = NavRoutes.Home.route
        ),
        BarItem(
            title = "Lists",
            image = Icons.Filled.Info,
            route = NavRoutes.Lists.route
        ),
        BarItem(
            title = "Images",
            image = Icons.Filled.AccountBox,
            route = NavRoutes.ImageScreen.route
        ),
        BarItem(
            title = "Pick Image",
            image = Icons.Filled.AccountBox,
            route = NavRoutes.ImagePickerScreen.route
        )
    )
}

data class BarItem(
    val title: String,
    val image: ImageVector,
    val route: String
)

@Composable
fun Lists(navController: NavController) {
    val configuration = LocalConfiguration.current
    val bankData = listOf(
        "Приорбанк" to "Иван Иванов",
        "Белинвестбанк" to "Александр Петров",
        "Альфа-Банк" to "Мария Смирнова",
        "БеларусБанк" to "Екатерина Сидорова"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))
                )
            )
            .padding(16.dp)
    ) {
        Text(
            text = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) "Список банков" else "Банки",
            fontSize = 24.sp,
            color = Color(0xFF0D47A1),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(bankData) { (bank, fio) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Банк: $bank",
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                            Text(
                                text = "ФИО: $fio",
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = { navController.navigate(NavRoutes.ImageScreen.route) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64B5F6)),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text("Перейти к изображению", color = Color.White)
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    var showName by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE1F5FE), Color(0xFF81D4FA))
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (showName) {
                    Text(
                        text = stringResource(R.string.full_name),
                        fontSize = 20.sp,
                        color = Color(0xFF0277BD),
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    Text(
                        text = "Нажмите кнопку, чтобы показать ФИО",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { showName = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF64B5F6)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(text = "Показать ФИО", color = Color.White)
            }

            Button(
                onClick = { showName = false },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEF5350)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Скрыть", color = Color.White)
            }
        }
    }
}

@Composable
fun ImageScreen() {
    var alpha by remember { mutableStateOf(1f) }
    val animatedAlpha by animateFloatAsState(targetValue = alpha)

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.cat_image),
            contentDescription = null,
            modifier = Modifier
                .size(if (isPortrait) 200.dp else 300.dp)
                .alpha(animatedAlpha)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { alpha = if (alpha == 1f) 0.5f else 1f }) {
            Text("Изменить прозрачность")
        }
    }
}

@Composable
fun ImagePickerScreen(navController: NavController) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var capturedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showPermissionDialog by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                selectedImageUri = uri
                capturedImageBitmap = null
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            if (bitmap != null) {
                capturedImageBitmap = bitmap
                selectedImageUri = null
            }
        }
    )

    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            cameraLauncher.launch()
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { galleryLauncher.launch("image/*") },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Выбрать изображение из галереи")
        }

        Button(
            onClick = {
                when {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PermissionChecker.PERMISSION_GRANTED -> {
                        cameraLauncher.launch()
                    }
                    else -> {
                        showPermissionDialog = true
                    }
                }
            }
        ) {
            Text("Создать снимок")
        }

        capturedImageBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Captured Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }

        selectedImageUri?.let { uri ->
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("Разрешение на использование камеры") },
            text = { Text("Это приложение требует доступ к камере для создания снимков.") },
            confirmButton = {
                Button(onClick = {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                    showPermissionDialog = false
                }) {
                    Text("Разрешить")
                }
            },
            dismissButton = {
                Button(onClick = { showPermissionDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object Lists : NavRoutes("lists")
    object ImageScreen : NavRoutes("image_screen")
    object ImagePickerScreen : NavRoutes("image_picker_screen")
}

