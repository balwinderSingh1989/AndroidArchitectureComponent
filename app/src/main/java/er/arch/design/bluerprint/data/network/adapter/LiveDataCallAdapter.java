/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package er.arch.design.bluerprint.data.network.adapter;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;


import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import er.arch.design.bluerprint.data.network.response.ApiResponse;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallAdapter<R> implements CallAdapter<R, LiveData<ApiResponse<R>>> {
    private final Type responseType;
    public LiveDataCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @NonNull
    @Override
    public LiveData<ApiResponse<R>> adapt(@NonNull Call<R> call) {
        return new LiveData<ApiResponse<R>>() {
            @NonNull
            AtomicBoolean started = new AtomicBoolean(false);
            @Override
            protected void onActive() {
                super.onActive();
                if (started.compareAndSet(false, true)) {
                    call.enqueue(new Callback<R>() {
                        @Override
                        public void onResponse(Call<R> call, @NonNull Response<R> response) {
                            postValue(new ApiResponse<>(response));
                        }

                        @Override
                        public void onFailure(Call<R> call, @NonNull Throwable throwable) {
                            postValue(new ApiResponse<R>(throwable));
                        }
                    });
                }
            }
        };
    }
}
