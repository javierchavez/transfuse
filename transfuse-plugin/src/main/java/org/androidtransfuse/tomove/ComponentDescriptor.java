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
package org.androidtransfuse.tomove;

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.MethodSignature;
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.model.InjectionNode;

import java.util.Collection;
import java.util.List;

/**
 * @author John Ericksen
 */
public interface ComponentDescriptor {

    Collection<Generation> getGenerators();

    PackageClass getPackageClass();

    ASTType getTarget();

    AnalysisContext getAnalysisContext();

    ASTType getType();

    List<MethodSignature> getGenerateFirst();

    InjectionNode getRootInjectionNode();

    void setRootInjectionNode(InjectionNode rootInjectionNode);
}
