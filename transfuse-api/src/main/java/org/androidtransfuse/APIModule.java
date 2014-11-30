/**
 * Copyright 2013 John Ericksen
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
package org.androidtransfuse;

/**
 * @author John Ericksen
 */

import android.os.Handler;
import android.os.Looper;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.aop.AsynchronousMethodInterceptor;
import org.androidtransfuse.aop.UIThreadMethodInterceptor;
import org.androidtransfuse.scope.ApplicationScope;
import org.androidtransfuse.scope.ConcurrentDoubleLockingScope;

import javax.inject.Singleton;

@TransfuseModule
@BindInterceptors({
    @BindInterceptor(annotation = Asynchronous.class, interceptor = AsynchronousMethodInterceptor.class),
    @BindInterceptor(annotation = UIThread.class, interceptor = UIThreadMethodInterceptor.class)
})
@DefineScopes({
        @DefineScope(annotation = Singleton.class, scope = ConcurrentDoubleLockingScope.class),
        @DefineScope(annotation = TransfuseModule.class, scope = ConcurrentDoubleLockingScope.class),
        @DefineScope(annotation = ApplicationScope.ApplicationScopeQualifier.class, scope = ApplicationScope.class)
})
public class APIModule {

    @Provides
    public Handler getHandler(){
        return new Handler(Looper.getMainLooper());
    }
}
