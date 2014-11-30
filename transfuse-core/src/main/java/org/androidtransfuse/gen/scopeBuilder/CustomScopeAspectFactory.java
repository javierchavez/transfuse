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
package org.androidtransfuse.gen.scopeBuilder;

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.astAnalyzer.ScopeAspect;
import org.androidtransfuse.gen.variableBuilder.VariableFactoryBuilderFactory2;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class CustomScopeAspectFactory implements ScopeAspectFactory {

    private final ASTType scopeKey;
    private final Provider<VariableFactoryBuilderFactory2> variableFactoryBuilderFactoryProvider;

    public CustomScopeAspectFactory(Provider<VariableFactoryBuilderFactory2> variableFactoryBuilderFactoryProvider, ASTType scopeKey) {
        this.variableFactoryBuilderFactoryProvider = variableFactoryBuilderFactoryProvider;
        this.scopeKey = scopeKey;
    }

    @Override
    public ScopeAspect buildAspect(AnalysisContext context) {
        return new ScopeAspect(variableFactoryBuilderFactoryProvider.get().buildScopeVariableBuilder(scopeKey));
    }
}
