package com.lakeel.altla.sample.android.gms.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import com.lakeel.altla.sample.android.content.ContextExtensions;

public final class LocationPermissions {

	private static final String[] PERMISSIONS = {
			Manifest.permission.ACCESS_FINE_LOCATION,
//			Manifest.permission.ACCESS_COARSE_LOCATION
	};

	public static boolean isPermissionsGranted(@NonNull Context context) {
		return ContextExtensions.isPermissionGranted(
				context,
				PERMISSIONS);
	}

	public static void requestPermissions(@NonNull Activity activity, int requestCode) {
		ActivityCompat.requestPermissions(
				activity,
				PERMISSIONS,
				requestCode);
	}

	public static boolean isRequestedPermissionsGranted(@NonNull int[] grantResults) {
		if (grantResults.length != PERMISSIONS.length) {
			return false;
		}

		for (int grantResult : grantResults) {
			if (grantResult != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}

		return true;
	}

	public static boolean shouldShowRequestPermissionRationale(@NonNull Activity activity) {
		for (String permission : PERMISSIONS) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
				return true;
			}
		}

		return false;
	}

	private LocationPermissions() {
	}
}
