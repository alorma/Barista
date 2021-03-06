package com.schibsted.spain.barista.sample;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.PerformException;
import com.schibsted.spain.barista.BaristaRule;
import com.schibsted.spain.barista.permission.PermissionGranter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.schibsted.spain.barista.BaristaClickActions.click;

public class PermissionGranterTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Rule
  public BaristaRule<RuntimePermissionActivity> activityRule = BaristaRule.create(RuntimePermissionActivity.class);

  @Before
  public void setUp() throws Exception {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
      throw new IllegalStateException("This test needs to run on a device with Android 23 or above");
    }
    if (hasNeededPermission(InstrumentationRegistry.getTargetContext(), RuntimePermissionActivity.SOME_PERMISSION)) {
      throw new IllegalStateException("This test expects you to not have the permission granted. Remember to clear data.");
    }
  }

  @Test(expected = PerformException.class)
  public void fails_when_using_permission() throws Exception {
    activityRule.launchActivity();

    click(R.id.use_permission_button);
  }

  @Test
  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
  public void works_after_granting_permission() throws Exception {
    activityRule.launchActivity();

    click(R.id.request_permission_button);

    PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.READ_CONTACTS);

    click(R.id.use_permission_button);
  }

  private static boolean hasNeededPermission(Context context, String permissionNeeded) {
    int permissionStatus = context.checkPermission(permissionNeeded, android.os.Process.myPid(), android.os.Process.myUid());
    return permissionStatus == PackageManager.PERMISSION_GRANTED;
  }
}
