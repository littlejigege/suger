package com.mobile.utils

import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.app.*
import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.app.usage.NetworkStatsManager
import android.app.usage.UsageStatsManager
import android.appwidget.AppWidgetManager
import android.bluetooth.BluetoothManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.*
import android.content.RestrictionsManager
import android.content.pm.ShortcutManager
import android.hardware.ConsumerIrManager
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.hardware.display.DisplayManager
import android.hardware.fingerprint.FingerprintManager
import android.hardware.input.InputManager
import android.hardware.usb.UsbManager
import android.location.LocationManager
import android.media.AudioManager
import android.media.midi.MidiManager
import android.media.projection.MediaProjectionManager
import android.media.session.MediaSessionManager
import android.media.tv.TvInputManager
import android.net.ConnectivityManager
import android.net.nsd.NsdManager
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pManager
import android.nfc.NfcManager
import android.os.*
import android.os.health.SystemHealthManager
import android.os.storage.StorageManager
import android.print.PrintManager
import android.support.annotation.RequiresApi
import android.telecom.TelecomManager
import android.telephony.CarrierConfigManager
import android.telephony.TelephonyManager
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.CaptioningManager
import android.view.inputmethod.InputMethodManager
import com.mobile.utils.Utils.Companion.app

/**
 * Created by jimji on 2017/9/16.
 */
inline val accessibilityManager: AccessibilityManager
    get() = Utils.app.getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager


/** Returns the AccountManager instance. **/
inline val accountManager: AccountManager
    get() = Utils.app.getSystemService(ACCOUNT_SERVICE) as AccountManager


/** Returns the ActivityManager instance. **/
inline val activityManager: ActivityManager
    get() = Utils.app.getSystemService(ACTIVITY_SERVICE) as ActivityManager


/** Returns the AlarmManager instance. **/
inline val alarmManager: AlarmManager
    get() = Utils.app.getSystemService(ALARM_SERVICE) as AlarmManager


/** Returns the AppOpsManager instance. **/
inline val appOpsManager: AppOpsManager
    get() = Utils.app.getSystemService(APP_OPS_SERVICE) as AppOpsManager


/** Returns the AudioManager instance. **/
inline val audioManager: AudioManager
    get() = Utils.app.getSystemService(AUDIO_SERVICE) as AudioManager


/** Returns the BatteryManager instance. **/
inline val batteryManager: BatteryManager
    get() = Utils.app.getSystemService(BATTERY_SERVICE) as BatteryManager


/** Returns the BluetoothManager instance. **/
inline val bluetoothManager: BluetoothManager
    get() = Utils.app.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager


/** Returns the CameraManager instance. **/
inline val cameraManager: CameraManager
    get() = Utils.app.getSystemService(CAMERA_SERVICE) as CameraManager


/** Returns the CaptioningManager instance. **/
inline val captioningManager: CaptioningManager
    get() = Utils.app.getSystemService(CAPTIONING_SERVICE) as CaptioningManager


/** Returns the CarrierConfigManager instance. **/
inline val carrierConfigManager: CarrierConfigManager
    get() = Utils.app.getSystemService(CARRIER_CONFIG_SERVICE) as CarrierConfigManager


/** Returns the ClipboardManager instance. **/
inline val clipboardManager: android.text.ClipboardManager
    get() = Utils.app.getSystemService(CLIPBOARD_SERVICE) as android.text.ClipboardManager


/** Returns the ConnectivityManager instance. **/
inline val connectivityManager: ConnectivityManager
    get() = Utils.app.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager


/** Returns the ConsumerIrManager instance. **/
inline val consumerIrManager: ConsumerIrManager
    get() = Utils.app.getSystemService(CONSUMER_IR_SERVICE) as ConsumerIrManager


/** Returns the DevicePolicyManager instance. **/
inline val devicePolicyManager: DevicePolicyManager
    get() = Utils.app.getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager


/** Returns the DisplayManager instance. **/
inline val displayManager: DisplayManager
    get() = Utils.app.getSystemService(DISPLAY_SERVICE) as DisplayManager


/** Returns the DownloadManager instance. **/
inline val downloadManager: DownloadManager
    get() = Utils.app.getSystemService(DOWNLOAD_SERVICE) as DownloadManager


/** Returns the FingerprintManager instance. **/
inline val fingerprintManager: FingerprintManager
    get() = Utils.app.getSystemService(FINGERPRINT_SERVICE) as FingerprintManager


/** Returns the HardwarePropertiesManager instance. **/
inline val hardwarePropertiesManager: HardwarePropertiesManager
    get() = Utils.app.getSystemService(HARDWARE_PROPERTIES_SERVICE) as HardwarePropertiesManager


/** Returns the InputManager instance. **/
inline val inputManager: InputManager
    get() = Utils.app.getSystemService(INPUT_SERVICE) as InputManager


/** Returns the InputMethodManager instance. **/
inline val inputMethodManager: InputMethodManager
    get() = Utils.app.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager


/** Returns the KeyguardManager instance. **/
inline val keyguardManager: KeyguardManager
    get() = Utils.app.getSystemService(KEYGUARD_SERVICE) as KeyguardManager


/** Returns the LocationManager instance. **/
inline val locationManager: LocationManager
    get() = Utils.app.getSystemService(LOCATION_SERVICE) as LocationManager


/** Returns the MediaProjectionManager instance. **/
inline val mediaProjectionManager: MediaProjectionManager
    @RequiresApi(21) get() = Utils.app.getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager


/** Returns the MediaSessionManager instance. **/
inline val mediaSessionManager: MediaSessionManager
    @RequiresApi(21) get() = Utils.app.getSystemService(MEDIA_SESSION_SERVICE) as MediaSessionManager


/** Returns the MidiManager instance. **/
inline val midiManager: MidiManager
    @RequiresApi(23) get() = Utils.app.getSystemService(MIDI_SERVICE) as MidiManager


/** Returns the NetworkStatsManager instance. **/
inline val networkStatsManager: NetworkStatsManager
    @RequiresApi(23) get() = Utils.app.getSystemService(NETWORK_STATS_SERVICE) as NetworkStatsManager


/** Returns the NfcManager instance. **/
inline val nfcManager: NfcManager
    get() = Utils.app.getSystemService(NFC_SERVICE) as NfcManager


/** Returns the NotificationManager instance. **/
inline val notificationManager: NotificationManager
    get() = Utils.app.getSystemService(NOTIFICATION_SERVICE) as NotificationManager


/** Returns the NsdManager instance. **/
inline val nsdManager: NsdManager
    get() = Utils.app.getSystemService(NSD_SERVICE) as NsdManager


/** Returns the PowerManager instance. **/
inline val powerManager: PowerManager
    get() = Utils.app.getSystemService(POWER_SERVICE) as PowerManager


/** Returns the PrintManager instance. **/
inline val printManager: PrintManager
    get() = Utils.app.getSystemService(PRINT_SERVICE) as PrintManager


/** Returns the RestrictionsManager instance. **/
inline val restrictionsManager: RestrictionsManager
    @RequiresApi(21) get() = Utils.app.getSystemService(RESTRICTIONS_SERVICE) as RestrictionsManager


/** Returns the SearchManager instance. **/
inline val searchManager: SearchManager
    get() = Utils.app.getSystemService(SEARCH_SERVICE) as SearchManager


/** Returns the SensorManager instance. **/
inline val sensorManager: SensorManager
    get() = Utils.app.getSystemService(SENSOR_SERVICE) as SensorManager


/** Returns the ShortcutManager instance. **/
inline val shortcutManager: ShortcutManager
    @RequiresApi(25) get() = Utils.app.getSystemService(SHORTCUT_SERVICE) as ShortcutManager


/** Returns the StorageManager instance. **/
inline val storageManager: StorageManager
    get() = Utils.app.getSystemService(STORAGE_SERVICE) as StorageManager


/** Returns the SystemHealthManager instance. **/
inline val systemHealthManager: SystemHealthManager
    @RequiresApi(24) get() = Utils.app.getSystemService(SYSTEM_HEALTH_SERVICE) as SystemHealthManager


/** Returns the TelecomManager instance. **/

inline val telecomManager: TelecomManager
    @RequiresApi(21) get() = Utils.app.getSystemService(TELECOM_SERVICE) as TelecomManager


/** Returns the TelephonyManager instance. **/
inline val telephonyManager: TelephonyManager
    get() = Utils.app.getSystemService(TELEPHONY_SERVICE) as TelephonyManager


/** Returns the TvInputManager instance. **/
inline val tvInputManager: TvInputManager
    @RequiresApi(21) get() = Utils.app.getSystemService(TV_INPUT_SERVICE) as TvInputManager


/** Returns the UiModeManager instance. **/
inline val uiModeManager: UiModeManager
    get() = Utils.app.getSystemService(UI_MODE_SERVICE) as UiModeManager


/** Returns the UsageStatsManager instance. **/
inline val usageStatsManager: UsageStatsManager
    @RequiresApi(22) get() = Utils.app.getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager


/** Returns the UsbManager instance. **/
inline val usbManager: UsbManager
    get() = Utils.app.getSystemService(USB_SERVICE) as UsbManager


/** Returns the UserManager instance. **/
inline val userManager: UserManager
    get() = Utils.app.getSystemService(USER_SERVICE) as UserManager


/** Returns the WallpaperManager instance. **/
inline val wallpaperManager: WallpaperManager
    get() = Utils.app.getSystemService(WALLPAPER_SERVICE) as WallpaperManager


/** Returns the WifiManager instance. **/
inline val wifiManager: WifiManager
    @SuppressLint("WifiManagerLeak")
    get() = Utils.app.getSystemService(WIFI_SERVICE) as WifiManager


/** Returns the WifiP2pManager instance. **/
inline val wifiP2pManager: WifiP2pManager
    get() = Utils.app.getSystemService(WIFI_P2P_SERVICE) as WifiP2pManager


/** Returns the WindowManager instance. **/
inline val windowManager: WindowManager
    get() = Utils.app.getSystemService(WINDOW_SERVICE) as WindowManager