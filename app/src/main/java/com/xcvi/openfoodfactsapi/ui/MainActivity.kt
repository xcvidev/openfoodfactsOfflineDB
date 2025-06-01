package com.xcvi.openfoodfactsapi.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.xcvi.openfoodfactsapi.ui.theme.OpenFoodFactsApiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            OpenFoodFactsApiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
                    val navController = rememberNavController()
                    val viewModel: FoodViewModel = hiltViewModel()

                    if (cameraPermissionState.status.isGranted) {
                        NavHost(
                            modifier = Modifier.padding(innerPadding),
                            navController = navController,
                            startDestination = "main_screen"
                        ) {
                            composable("main_screen") {
                                MainScreen(
                                    navController = navController,
                                    viewModel = viewModel
                                )
                            }
                            composable("camera_screen") {
                                CameraScreen(
                                    onSuccess = { barcode, barcodeScanner ->
                                        viewModel.scan(barcode)
                                        barcodeScanner?.close()
                                        navController.popBackStack("main_screen", false)
                                    },
                                    onGoBack = {
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }

                    } else if (cameraPermissionState.status.shouldShowRationale) {
                        Text("Camera Permission permanently denied")
                    } else {
                        SideEffect {
                            cameraPermissionState.run { launchPermissionRequest() }
                        }
                        Text("No Camera Permission")
                    }
                }
            }
        }
    }
}

