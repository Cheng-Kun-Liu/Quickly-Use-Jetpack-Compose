package com.laomuji1999.compose.core.logic.location

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.core.location.LocationListenerCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.laomuji1999.compose.core.logic.common.Log
import com.laomuji1999.compose.core.logic.common.dispatchers.IoCoroutineScope
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * 至少需要 ACCESS_COARSE_LOCATION 权限才能使用
 * 两种方式获取定位
 * 1. FusedLocationProviderClient
 * 2. LocationManager
 * FusedLocationProviderClient 的准确性更高,但没有ACCESS_FINE_LOCATION权限时无法主动获取定位,只能被动接收位置,经常会出现null.
 * LocationManager 的准确性较低,但可以指定获取什么类型的定位,作为备选方案.
 * 只有在 FusedLocationProviderClient 无法获取定位的情况才会使用 LocationManager
 */
class DefaultLocator @Inject constructor(
    private val application: Application,
    @param:IoCoroutineScope val coroutineScope: CoroutineScope,
) : Locator {

    companion object {
        private const val TAG = "tag_locator"
    }

    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private val locationManager =
        application.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    override fun hasLocatorProvider(): Boolean {
        return hasGpsProvider() || hasNetworkProvider()
    }

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): LocatorResult {
        val priority = if (hasFineLocationPermission()) {
            Log.debug(TAG, "use PRIORITY_HIGH_ACCURACY")
            Priority.PRIORITY_HIGH_ACCURACY
        } else if (hasCoarseLocationPermission()) {
            Log.debug(TAG, "use PRIORITY_BALANCED_POWER_ACCURACY")
            Priority.PRIORITY_BALANCED_POWER_ACCURACY
        } else {
            Log.debug(TAG, "no permission")
            return LocatorResult.PermissionDenied
        }
        if (!hasLocatorProvider()) {
            Log.debug(TAG, "hasLocatorProvider false")
            return LocatorResult.ProvidersDisabled
        }
        return try {
            withTimeout(30 * 1000) {
                suspendCancellableCoroutine { cont ->
                    fusedLocationProviderClient.getCurrentLocation(
                        CurrentLocationRequest.Builder().apply {
                            setPriority(priority)
                            setDurationMillis(6 * 1000)
                            setMaxUpdateAgeMillis(0)
                        }.build(), CancellationTokenSource().token
                    ).apply {
                        fusedLocationProviderClient.locationAvailability.addOnSuccessListener {
                            if (it.isLocationAvailable) {
                                Log.debug(TAG, "use fusedLocationProviderClient")
                                Log.debug(TAG, "isComplete $isComplete")
                                if (isComplete) {
                                    if (isSuccessful) {
                                        Log.debug(TAG, "successful, use result")
                                        cont.safeResume(LocatorResult.Success(result))
                                    } else {
                                        Log.debug(TAG, "unsuccessful, use locationManager")
                                        requestSingleLocation(cont)
                                    }
                                } else {
                                    addOnSuccessListener { result ->
                                        if (result != null) {
                                            Log.debug(TAG, "onSuccess $result")
                                            cont.safeResume(LocatorResult.Success(result))
                                        } else {
                                            Log.debug(TAG, "onSuccess null, use locationManager")
                                            requestSingleLocation(cont)
                                        }
                                    }
                                    addOnFailureListener {
                                        Log.debug(TAG, "onFailure, use locationManager")
                                        requestSingleLocation(cont)
                                    }
                                    addOnCanceledListener {
                                        Log.debug(TAG, "onCancel, return null")
                                        cont.safeResume(LocatorResult.Cancelled)
                                    }
                                }
                            } else {
                                Log.debug(TAG, "isLocationAvailable is false, use locationManager")
                                requestSingleLocation(cont)
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            if (e is TimeoutCancellationException) {
                Log.debug(TAG, "timeout, return null")
            } else {
                Log.debug(TAG, "${e.message}")
            }
            LocatorResult.Error(e)
        }
    }

    private fun hasGpsProvider() = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    private fun hasNetworkProvider() =
        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    private fun hasPassiveProvider() =
        locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)

    private fun hasCoarseLocationPermission() =
        ContextCompat.checkSelfPermission(application, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED

    private fun hasFineLocationPermission() =
        ContextCompat.checkSelfPermission(application, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED

    private fun <T> CancellableContinuation<T>.safeResume(value: T) {
        if (isActive) {
            try {
                resume(value)
            }catch (ignored: Exception){
                //resume后isActive会变为false,按道理不会走到这里,这里做一个兜底
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestSingleLocation(
        cont: CancellableContinuation<LocatorResult>
    ) {
        coroutineScope.launch {
            val locationListener = object : LocationListenerCompat {
                private var isCalled = false
                override fun onLocationChanged(location: Location) {
                    if (isCalled) {
                        Log.debug(TAG, "requestSingleLocation ignore")
                        return
                    }
                    isCalled = true
                    Log.debug(TAG, "requestSingleLocation $location")
                    cont.safeResume(LocatorResult.Success(location))
                    locationManager.removeUpdates(this)
                }
            }

            cont.invokeOnCancellation {
                locationManager.removeUpdates(locationListener)
            }

            if (hasGpsProvider()) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0f, locationListener, Looper.getMainLooper()
                )
                delay(3000)
            }

            if (hasNetworkProvider()) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0,
                    0f,
                    locationListener,
                    Looper.getMainLooper()
                )
                delay(5000)
            }

            if (hasPassiveProvider()) {
                locationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER,
                    0,
                    0f,
                    locationListener,
                    Looper.getMainLooper()
                )
            }

            //让协程保持存活
            while (cont.isActive){
                delay(500)
            }
        }
    }
}