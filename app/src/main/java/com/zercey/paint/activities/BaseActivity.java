package com.zercey.paint.activities;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.zercey.paint.R;

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog progress;

//    protected void replaceFragment(Fragment fragment) {
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//
////        transaction.setCustomAnimations(
////                R.anim.slide_in_right,
////                R.anim.slide_out_left,
////                android.R.anim.slide_in_left,
////                android.R.anim.slide_out_right);
//
//        transaction.replace(R.id.frame_layout, fragment);
//
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
//
//    protected void finish() {
//        getActivity().getSupportFragmentManager().popBackStack();
//    }

    protected void hideProgress(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
    }

    protected void showProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }

    protected void showProgressDialog() {
        progress = new ProgressDialog(this);
        progress.setCancelable(false);

        progress.setMessage("Please wait..");

        if (!progress.isShowing()) {
            progress.show();
        }
    }

    protected void hideProgressDialog() {
        if (progress.isShowing()) {
            progress.hide();
        }
    }

    protected void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}