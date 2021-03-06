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
package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.analysis.astAnalyzer.ASTAnalysis;
import org.androidtransfuse.gen.scopeBuilder.ScopeAspectFactory;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;
import org.androidtransfuse.model.InjectionSignature;
import org.androidtransfuse.util.matcher.Matcher;
import org.androidtransfuse.util.matcher.Matchers;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class InjectionNodeBuilderRepository {

    private final Map<Matcher<InjectionSignature>, InjectionNodeBuilder> typeQualifierBindings = new HashMap<Matcher<InjectionSignature>, InjectionNodeBuilder>();
    private final Map<ASTType, ScopeAspectFactory> scopeVariableBuilderMap = new HashMap<ASTType, ScopeAspectFactory>();
    private final Map<ASTType, ASTType> scopeAnnotations = new HashMap<ASTType, ASTType>();
    private final Map<InjectionSignature, ASTType> scoping = new HashMap<InjectionSignature, ASTType>();
    private final Map<ASTType, ASTType> interceptorAnnotationMap = new HashMap<ASTType, ASTType>();
    private final Set<ASTAnalysis> analysisSet = new HashSet<ASTAnalysis>();
    private final ASTClassFactory astClassFactory;

    @Inject
    public InjectionNodeBuilderRepository(ASTClassFactory astClassFactory) {
        this.astClassFactory = astClassFactory;
    }

    public InjectionNodeBuilderRepository(Set<ASTAnalysis> analysisSet, ASTClassFactory astClassFactory) {
        this.astClassFactory = astClassFactory;
        this.analysisSet.addAll(analysisSet);
    }

    public void putAnnotation(Class<?> viewClass, InjectionNodeBuilder viewVariableBuilder) {
        putSignatureMatcher(Matchers.annotated().byType(astClassFactory.getType(viewClass)).build(), viewVariableBuilder);
    }

    public void putType(Class<?> clazz, InjectionNodeBuilder variableBuilder) {
        putType(astClassFactory.getType(clazz), variableBuilder);
    }

    public void putType(ASTType type, InjectionNodeBuilder variableBuilder) {
        putType(new InjectionSignature(type), variableBuilder);
    }

    public void putType(InjectionSignature injectionSignature, InjectionNodeBuilder variableBuilder) {
        putSignatureMatcher(Matchers.signature(injectionSignature), variableBuilder);
    }

    public void putSignatureMatcher(Matcher<InjectionSignature> matcher, InjectionNodeBuilder variableBuilder) {
        this.typeQualifierBindings.put(matcher, variableBuilder);
    }

    public Map<Matcher<InjectionSignature>, InjectionNodeBuilder> getTypeQualifierBindings() {
        return typeQualifierBindings;
    }

    public Set<ASTType> getScopes() {
        return scopeAnnotations.keySet();
    }

    public boolean containsScope(ASTAnnotation scopeAnnotation) {
        return scopeAnnotations.containsKey(scopeAnnotation.getASTType());
    }

    public Map<ASTType, ASTType> getScopeAnnotations(){
        return scopeAnnotations;
    }

    public ScopeAspectFactory getScopeAspectFactory(ASTType scopeType) {
        return scopeVariableBuilderMap.get(scopeType);
    }

    public void putScopeAspectFactory(ASTType scopeAnnotation, ASTType scopeType, ScopeAspectFactory scopeAspectFactory) {
        scopeVariableBuilderMap.put(scopeAnnotation, scopeAspectFactory);
        scopeAnnotations.put(scopeAnnotation, scopeType);
    }

    private Map<ASTType, ScopeAspectFactory> getScopeVariableBuilderMap() {
        return scopeVariableBuilderMap;
    }

    private Map<InjectionSignature, ASTType> getScoping() {
        return scoping;
    }

    public void putScoped(InjectionSignature type, ASTType scope) {
        scoping.put(type, scope);
    }

    public ASTType getScoped(InjectionSignature type) {
        return scoping.get(type);
    }

    public void putInterceptor(ASTType annotationType, ASTType interceptor) {
        interceptorAnnotationMap.put(annotationType, interceptor);
    }

    public ASTType getInterceptor(ASTType annotationType) {
        return interceptorAnnotationMap.get(annotationType);
    }

    public boolean isInterceptor(ASTAnnotation annotation) {
        return interceptorAnnotationMap.containsKey(annotation.getASTType());
    }

    public Set<ASTAnalysis> getAnalysisRepository() {
        return analysisSet;
    }

    private Map<ASTType, ASTType> getInterceptorAnnotationMap() {
        return interceptorAnnotationMap;
    }

    public void addRepository(InjectionNodeBuilderRepository repository){
        this.typeQualifierBindings.putAll(repository.getTypeQualifierBindings());
        this.scopeVariableBuilderMap.putAll(repository.getScopeVariableBuilderMap());
        this.scopeAnnotations.putAll(repository.getScopeAnnotations());
        this.scoping.putAll(repository.getScoping());
        this.interceptorAnnotationMap.putAll(repository.getInterceptorAnnotationMap());
    }
}
