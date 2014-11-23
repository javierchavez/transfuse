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
package org.androidtransfuse.analysis;

import org.androidtransfuse.ConfigurationRepository;
import org.androidtransfuse.DescriptorBuilder;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
@Singleton
public class ConfigurationRepositoryImpl implements ConfigurationRepository{

    private final List<DescriptorBuilder> descriptorBuilders = new ArrayList<DescriptorBuilder>();

    public List<DescriptorBuilder> getDescriptorBuilders() {
        return descriptorBuilders;
    }

    @Override
    public void add(DescriptorBuilder descriptorBuilder) {
        descriptorBuilders.add(descriptorBuilder);
    }
}
