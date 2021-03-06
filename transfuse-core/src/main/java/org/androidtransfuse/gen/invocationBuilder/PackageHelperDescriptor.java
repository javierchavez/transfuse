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
package org.androidtransfuse.gen.invocationBuilder;

import org.androidtransfuse.adapter.PackageClass;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class PackageHelperDescriptor {

    private final PackageClass name;
    private final Map<ConstructorCall, String> constructorMapping = new HashMap<ConstructorCall, String>();
    private final Map<FieldReference, String> fieldSetMapping = new HashMap<FieldReference, String>();
    private final Map<FieldReference, String> fieldGetMapping = new HashMap<FieldReference, String>();
    private final Map<MethodCall, String> methodCallMapping = new HashMap<MethodCall, String>();

    public PackageHelperDescriptor(PackageClass name) {
        this.name = name;
    }

    public PackageClass getName() {
        return name;
    }

    public Map<ConstructorCall, String> getConstructorMapping() {
        return constructorMapping;
    }

    public Map<FieldReference, String> getFieldSetMapping() {
        return fieldSetMapping;
    }

    public Map<FieldReference, String> getFieldGetMapping() {
        return fieldGetMapping;
    }

    public Map<MethodCall, String> getMethodCallMapping() {
        return methodCallMapping;
    }
}
