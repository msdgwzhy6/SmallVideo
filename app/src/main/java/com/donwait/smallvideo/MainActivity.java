package com.donwait.smallvideo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_CODE_PERMISSION_FIRST_USE = 10001;
    private final int REQUEST_CODE_PERMISSION_SETTING = 10002;
    private final int REQUEST_CODE_PERMISSION_MULTI = 10003;

    private boolean isShowAlbumButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_record_video1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowAlbumButton = false;
                checkPermissions(REQUEST_CODE_PERMISSION_MULTI);
            }
        });

        findViewById(R.id.btn_record_video2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowAlbumButton = true;
                checkPermissions(REQUEST_CODE_PERMISSION_MULTI);
            }
        });

        checkPermissions(REQUEST_CODE_PERMISSION_FIRST_USE);
    }

    private void checkPermissions(int requestCode) {
        AndPermission.with(this)
                .requestCode(requestCode)
                .permission(Permission.PHONE, Permission.CAMERA, Permission.STORAGE, Permission.LOCATION, Permission.MICROPHONE)
                .callback(permissionListener)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(MainActivity.this, rationale).show();
                    }
                })
                .start();
    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            if (requestCode == REQUEST_CODE_PERMISSION_MULTI) {
                recordVideo();
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            if (requestCode == 10001) {
                if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, deniedPermissions)) {
                    AndPermission.defaultSettingDialog(MainActivity.this, REQUEST_CODE_PERMISSION_SETTING).show();
                }
            }
        }
    };

    private void recordVideo() {
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra("isShowAlbumButton", isShowAlbumButton);
        startActivity(intent);
    }

}
