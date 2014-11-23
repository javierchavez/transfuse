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

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JVar;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import java.util.Map;

/**
 * @author John Ericksen
 */
public interface ComponentBuilder {

    void build();

    void add(ComponentPartGenerator partGenerator);

    void addLazy(ASTMethod method, ComponentMethodGenerator partGenerator);

    void add(ASTMethod method, GenerationPhase phase, ComponentMethodGenerator partGenerator);

    JDefinedClass getDefinedClass();

    AnalysisContext getAnalysisContext();

    void setScopes(JVar scopes);

    JVar getScopes();

    Map<InjectionNode, TypedExpression> getExpressionMap();
}
