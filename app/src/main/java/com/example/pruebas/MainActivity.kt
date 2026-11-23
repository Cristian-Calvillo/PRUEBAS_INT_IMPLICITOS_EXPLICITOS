
package com.example.pruebas

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.pruebas.ui.theme.PruebasTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"
    private var keepSplash = true

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { keepSplash }
        ShowToast("Funcion onCreate()")

        lifecycleScope.launch {
            delay(3000)
            keepSplash = false
        }

        enableEdgeToEdge()
        setContent {
            PruebasTheme {
                ViewContainer()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        ShowToast("Funcion onStart()")
    }

    override fun onRestart() {
        super.onRestart()
        ShowToast("Funcion onRestart()")
    }

    override fun onResume() {
        super.onResume()
        ShowToast("Funcion onResume()")
    }

    override fun onPause() {
        ShowToast("Funcion onPause()")
        super.onPause()
    }

    override fun onStop() {
        ShowToast("Funcion onStop()")
        super.onStop()
    }

    override fun onDestroy() {
        Log.i(TAG, "Funcion onDestroy()")
        super.onDestroy()
    }

    private fun ShowToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        Log.i(TAG, message)
    }
}

fun abrirCorreo(context: Context) {
    val correo = "23150690@aguascalientes.tecnm.mx"
    val asunto = "Práctica de Android"
    val mensaje = "Hola, este correo fue creado desde mi aplicación."

    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:${Uri.encode(correo)}")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(correo))
        putExtra(Intent.EXTRA_SUBJECT, asunto)
        putExtra(Intent.EXTRA_TEXT, mensaje)
    }

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            context,
            "No se encontró una aplicación de correo",
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun abrirSMS(context: Context) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("smsto:4492576778")
        putExtra(
            "sms_body",
            "Hola, este mensaje fue creado desde mi aplicación Android."
        )
    }

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            context,
            "No se encontró una aplicación de SMS",
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun abrirMaps(context: Context) {
    val ubicacion = "Instituto Tecnológico de Aguascalientes"
    val uri = Uri.parse("geo:0,0?q=${Uri.encode(ubicacion)}")
    val intent = Intent(Intent.ACTION_VIEW, uri)

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            context,
            "No se encontró una aplicación de mapas",
            Toast.LENGTH_SHORT
        ).show()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, locale = "es-rMX")
@Composable
fun ViewContainer() {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    var showScaffoldBars by rememberSaveable {
        mutableStateOf(true)
    }

    var isFavorite by rememberSaveable {
        mutableStateOf(false)
    }

    val snackbarMessage = stringResource(R.string.home_fab_text)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    "Menú de Navegación",
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            topBar = {
                if (showScaffoldBars) {
                    TopAppBar(
                        title = {
                            Text(
                                text = stringResource(
                                    R.string.pantalla_principal
                                )
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            titleContentColor = Color.White,
                            navigationIconContentColor = Color.White,
                            actionIconContentColor = Color.White
                        ),
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        drawerState.open()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Abrir menú",
                                    tint = Color.White
                                )
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = {
                                    isFavorite = !isFavorite
                                    Toast.makeText(
                                        context,
                                        if (isFavorite) {
                                            "Agregado a favoritos"
                                        } else {
                                            "Eliminado de favoritos"
                                        },
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "Marcar como favorito",
                                    tint = if (isFavorite) {
                                        Color.Red
                                    } else {
                                        Color.White
                                    }
                                )
                            }

                            IconButton(
                                onClick = {
                                    val intent = Intent(
                                        context,
                                        SecondActivity::class.java
                                    )
                                    context.startActivity(intent)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Abrir segunda actividad",
                                    tint = Color.White
                                )
                            }

                            MenuOpciones()
                        }
                    )
                }
            },
            bottomBar = {
                if (showScaffoldBars) {
                    BottomAppBar(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.primary
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            text = stringResource(R.string.bottom_app_bar),
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                }
            },
            floatingActionButton = {
                if (showScaffoldBars) {
                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = snackbarMessage
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = snackbarMessage
                        )
                    }
                }
            }
        ) { innerPadding ->
            ContentView(
                modifier = Modifier.padding(innerPadding),
                showScaffoldBars = showScaffoldBars,
                onToggleScaffoldBars = {
                    showScaffoldBars = !showScaffoldBars
                }
            )
        }
    }
}

@Composable
fun ContentView(
    modifier: Modifier = Modifier,
    showScaffoldBars: Boolean = true,
    onToggleScaffoldBars: () -> Unit = {}
) {
    var counter by rememberSaveable {
        mutableIntStateOf(0)
    }

    val imageList: List<Int> = listOf(
        R.drawable.crb,
        R.drawable.img1,
        R.drawable.ia,
        R.drawable.tecnl,
        R.drawable.img2
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Red)
    ) {
        item {
            Button(
                onClick = onToggleScaffoldBars,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = if (showScaffoldBars) {
                        "Ocultar Scaffold (Barras)"
                    } else {
                        "Mostrar Scaffold (Barras)"
                    }
                )
            }

            Image(
                painter = painterResource(id = R.drawable.andriodlogo),
                contentDescription = stringResource(
                    R.string.image_description
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Agregar a favoritos",
                    tint = Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            counter++
                        }
                )

                Text(
                    text = counter.toString(),
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Text(
                text = stringResource(R.string.image_description),
                fontSize = 24.sp,
                color = Color.Blue,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(R.string.materia),
                fontSize = 24.sp,
                color = Color.Blue,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(R.string.nombre),
                fontSize = 25.sp,
                color = Color.Magenta,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(R.string.apellido),
                fontSize = 24.sp,
                color = Color.Blue,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                items(imageList) { imageId ->
                    Image(
                        painter = painterResource(id = imageId),
                        contentDescription = "Imagen de aplicaciones móviles",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(width = 200.dp, height = 100.dp)
                            .padding(5.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MenuOpciones() {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Opciones",
                tint = Color.White
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Enviar correo") },
                onClick = {
                    expanded = false
                    abrirCorreo(context)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null
                    )
                }
            )

            DropdownMenuItem(
                text = { Text("Enviar SMS") },
                onClick = {
                    expanded = false
                    abrirSMS(context)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Sms,
                        contentDescription = null
                    )
                }
            )

            DropdownMenuItem(
                text = { Text("Google Maps") },
                onClick = {
                    expanded = false
                    abrirMaps(context)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null
                    )
                }
            )
        }
    }
}
