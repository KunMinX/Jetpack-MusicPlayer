/*
 * Copyright 2018-present KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kunminx.architecture.ui.page;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.kunminx.architecture.BaseApplication;


/**
 * Create by KunMinX at 19/7/11
 */
public abstract class BaseFragment extends DataBindingFragment {

  private ViewModelProvider mFragmentProvider;
  private ViewModelProvider mActivityProvider;
  private ViewModelProvider mApplicationProvider;

  protected <T extends ViewModel> T getFragmentScopeViewModel(@NonNull Class<T> modelClass) {
    if (mFragmentProvider == null) {
      mFragmentProvider = new ViewModelProvider(this);
    }
    return mFragmentProvider.get(modelClass);
  }

  protected <T extends ViewModel> T getActivityScopeViewModel(@NonNull Class<T> modelClass) {
    if (mActivityProvider == null) {
      mActivityProvider = new ViewModelProvider(mActivity);
    }
    return mActivityProvider.get(modelClass);
  }

  protected <T extends ViewModel> T getApplicationScopeViewModel(@NonNull Class<T> modelClass) {
    if (mApplicationProvider == null) {
      mApplicationProvider = new ViewModelProvider(
              (BaseApplication) mActivity.getApplicationContext());
    }
    return mApplicationProvider.get(modelClass);
  }

  protected NavController nav() {
    return NavHostFragment.findNavController(this);
  }

  protected void toggleSoftInput() {
    InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
  }

  protected void openUrlInBrowser(String url) {
    Uri uri = Uri.parse(url);
    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    startActivity(intent);
  }

  protected void showLongToast(String text) {
    Toast.makeText(mActivity.getApplicationContext(), text, Toast.LENGTH_LONG).show();
  }

  protected void showShortToast(String text) {
    Toast.makeText(mActivity.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
  }

  protected void showLongToast(int stringRes) {
    showLongToast(mActivity.getApplicationContext().getString(stringRes));
  }

  protected void showShortToast(int stringRes) {
    showShortToast(mActivity.getApplicationContext().getString(stringRes));
  }
}
