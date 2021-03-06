/**
 * Copyright 2011-2015 John Ericksen
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
package org.androidtransfuse.listeners;

import android.view.Menu;
import android.view.MenuItem;

/**
 * Defines the `Activity` `Menu` related Call-Through methods.  Each defined method represents a method in the corresponding
 * `Activity` class.
 *
 * Only one Call-Through component per type may be defined per injection graph.
 *
 * @author John Ericksen
 */
public interface ActivityMenuComponent {

    @CallThrough
    boolean onCreateOptionsMenu(Menu menu);

    @CallThrough
    boolean onPrepareOptionsMenu(Menu menu);

    @CallThrough
    boolean onOptionsItemSelected(MenuItem menuItem);

    @CallThrough
    boolean onMenuOpened(int featureId, Menu menu);

    @CallThrough
    void onOptionsMenuClosed(Menu menu);


}
