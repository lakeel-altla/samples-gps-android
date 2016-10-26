package com.lakeel.altla.sample.android.content;

import android.Manifest;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import com.lakeel.altla.sample.android.content.pm.PackageManagerExtensions;

public final class ContextExtensions {

	public static boolean isPermissionGranted(@NonNull Context context, @NonNull String... permissions) {
		for (String permission : permissions) {
			if (!isPermissionGranted(context, permission)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isPermissionGranted(@NonNull Context context, @NonNull String permission) {
		return PackageManagerExtensions.isPermissionGranted(
				ContextCompat.checkSelfPermission(context, permission));
	}

	public static int checkSelfPermissionToAccessFineLocation(@NonNull Context context) {
		return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
	}

	public static int checkSelfPermissionToAccessCoarseLocation(@NonNull Context context) {
		return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
	}

	public static boolean isPermissionGrantedToAccessFineLocation(@NonNull Context context) {
		return PackageManagerExtensions.isPermissionGranted(checkSelfPermissionToAccessFineLocation(context));
	}

	public static boolean isPermissionGrantedToAccessCoarseLocation(@NonNull Context context) {
		return PackageManagerExtensions.isPermissionGranted(checkSelfPermissionToAccessCoarseLocation(context));
	}

	private ContextExtensions() {
	}
}
