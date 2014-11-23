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
package org.androidtransfuse.plugins;

import org.androidtransfuse.Plugin;
import org.androidtransfuse.Plugins;
import org.androidtransfuse.annotations.TransfuseModule;

/**
 * @author John Ericksen
 */
@TransfuseModule
@Plugins({
        @Plugin(ApplicationPlugin.class),
        @Plugin(ServicePlugin.class),
        @Plugin(ActivityPlugin.class),
        @Plugin(FragmentPlugin.class),
        @Plugin(BroadcastReceiverPlugin.class),
        @Plugin(SystemServicePlugin.class),
        @Plugin(SharedPreferencesPlugin.class),
        @Plugin(ResourcesPlugin.class),
        @Plugin(ModulePlugin.class)
})
public class PluginModule {
}
