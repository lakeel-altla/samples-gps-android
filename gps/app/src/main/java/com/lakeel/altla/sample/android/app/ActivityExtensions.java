package com.lakeel.altla.sample.android.app;

import android.Manifest;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import com.lakeel.altla.sample.android.ManifestExtensions;

public final class ActivityExtensions {

	public static void requestPermissionToAccessFineLocation(@NonNull Activity activity, int requestCode) {
		ActivityCompat.requestPermissions(
				activity, ManifestExtensions.PERMISSIONS_ACCESS_FINE_LOCATION, requestCode);
	}

	public static boolean shouldShowRequestPermissionRationaleToAccessFineLocation(@NonNull Activity activity) {
		return ActivityCompat.shouldShowRequestPermissionRationale(
				activity, Manifest.permission.ACCESS_FINE_LOCATION);
	}

	private ActivityExtensions() {
	}
}
