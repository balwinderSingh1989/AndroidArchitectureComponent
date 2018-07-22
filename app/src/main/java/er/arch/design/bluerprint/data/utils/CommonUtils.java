/*
 * Copyright (C) 2015-2017 Lukoh Nam, goForer
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

package er.arch.design.bluerprint.data.utils;

import android.content.Context;
import android.widget.Toast;
import javax.inject.Inject;
import er.arch.design.bluerprint.di.scope.ApplicationContext;

public class CommonUtils {
    @Inject
    public
    void showToastMessage(@ApplicationContext Context context, String text, int duration) {
        Toast.makeText(context, text, duration).show();
    }
}
