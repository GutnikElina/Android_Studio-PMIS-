package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.CoroutineScope

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AttractionsApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(title: String, drawerState: DrawerState, scope: CoroutineScope) {
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color(0xFF766391),
            titleContentColor = Color.White
        ),
        navigationIcon = {
            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttractionsApp() {
    val topBarTitle = remember { mutableStateOf("Пинск") }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val categoryList = listOf(
        "Музеи",
        "Исторические здания",
        "Церкви",
        "Монастыри",
        "Парки",
        "Памятники",
        "Площади",
        "Театры",
        "Галереи",
        "Фонтаны"
    )
    val mainList = remember { mutableStateOf(getListItemsByCategory(0)) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.pinsk_logo),
                        contentDescription = "Пинск",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .padding(bottom = 8.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "Пинск",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyColumn {
                        items(categoryList.size) { index ->
                            NavigationDrawerItem(
                                label = { Text(categoryList[index]) },
                                selected = topBarTitle.value == categoryList[index],
                                onClick = {
                                    topBarTitle.value = categoryList[index]
                                    mainList.value = getListItemsByCategory(index)
                                    scope.launch { drawerState.close() }
                                }
                            )
                        }
                    }
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    MyTopAppBar(title = topBarTitle.value, drawerState = drawerState, scope = scope)
                }
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    items(mainList.value.size) { index ->
                        MainListItem(item = mainList.value[index])
                    }
                }
            }
        }
    )
}

@Composable
fun MainListItem(item: AttractionItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.name,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

data class AttractionItem(
    val name: String,
    val imageRes: Int
)

fun getListItemsByCategory(index: Int): List<AttractionItem> {
    return when (index) {
        0 -> listOf(
            AttractionItem("Музей Белорусского Полесья", R.drawable.museum),
            AttractionItem("Музей Пинского речного флота", R.drawable.museum2)
        )
        1 -> listOf(
            AttractionItem("Пинский замок", R.drawable.castle)
        )
        2 -> listOf(
            AttractionItem("Собор Святой Варвары", R.drawable.church),
            AttractionItem("Костёл Успения Пресвятой Девы Марии", R.drawable.church2)
        )
        3 -> listOf(
            AttractionItem("Бернардинский монастырь", R.drawable.monastery),
            AttractionItem("Монастырь францисканцев", R.drawable.monastery2)
        )
        4 -> listOf(
            AttractionItem("Парк Победы", R.drawable.park),
            AttractionItem("Парк имени Ленина", R.drawable.park2)
        )
        5 -> listOf(
            AttractionItem("Памятник партизанам Пинска", R.drawable.monument),
            AttractionItem("Памятник Ивану Хруцкому", R.drawable.monument2)
        )
        6 -> listOf(
            AttractionItem("Площадь Ленина", R.drawable.square),
            AttractionItem("Рыночная площадь", R.drawable.square2)
        )
        7 -> listOf(
            AttractionItem("Пинский драматический театр", R.drawable.theater)
        )
        8 -> listOf(
            AttractionItem("Галерея современного искусства", R.drawable.gallery)
        )
        9 -> listOf(
            AttractionItem("Фонтан на площади Ленина", R.drawable.fountain),
            AttractionItem("Фонтан у городского парка", R.drawable.fountain2)
        )
        else -> emptyList()
    }
}
