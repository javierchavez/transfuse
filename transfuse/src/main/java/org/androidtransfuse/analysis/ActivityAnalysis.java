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

import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.adapter.element.ASTElementFactory;
import org.androidtransfuse.adapter.element.ASTTypeBuilderVisitor;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.experiment.ComponentDescriptorImpl;
import org.androidtransfuse.experiment.ScopesGeneration;
import org.androidtransfuse.experiment.generators.*;
import org.androidtransfuse.gen.GeneratorFactory;
import org.androidtransfuse.gen.componentBuilder.ListenerRegistrationGenerator;
import org.androidtransfuse.gen.componentBuilder.NonConfigurationInstanceGenerator;
import org.androidtransfuse.gen.variableBuilder.*;
import org.androidtransfuse.intentFactory.ActivityIntentFactoryStrategy;
import org.androidtransfuse.model.InjectionSignature;
import org.androidtransfuse.scope.ApplicationScope;
import org.androidtransfuse.tomove.ComponentDescriptor;
import org.androidtransfuse.util.AndroidLiterals;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.lang.model.type.TypeMirror;

import static org.androidtransfuse.util.TypeMirrorUtil.getTypeMirror;

/**
 * Activity related Analysis
 *
 * @author John Ericksen
 */
public class ActivityAnalysis implements Analysis<ComponentDescriptor> {

    private final InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory;
    private final AnalysisContextFactory analysisContextFactory;
    private final ASTElementFactory astElementFactory;
    private final ProviderInjectionNodeBuilderFactory providerInjectionNodeBuilder;
    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private final InjectionBindingBuilder injectionBindingBuilder;
    private final ObservesExpressionGenerator.ObservesExpressionGeneratorFactory observesExpressionGeneratorFactory;
    private final ViewInjectionNodeBuilder viewVariableBuilder;
    private final ExtraInjectionNodeBuilder extraInjectionNodeBuilder;
    private final SystemServiceBindingInjectionNodeBuilder systemServiceBindingInjectionNodeBuilder;
    private final ResourceInjectionNodeBuilder resourceInjectionNodeBuilder;
    private final PreferenceInjectionNodeBuilder preferenceInjectionNodeBuilder;
    private final Provider<ActivityManifestEntryGenerator> manifestGeneratorProvider;
    private final LayoutGenerator layoutGenerator;
    private final LayoutHandlerGenerator layoutHandlerGenerator;
    private final WindowFeatureGenerator windowFeatureGenerator;
    private final GeneratorFactory generatorFactory;
    private final ListenerRegistrationGenerator.ListerRegistrationGeneratorFactory listerRegistrationGeneratorFactory;
    private final NonConfigurationInstanceGenerator.NonconfigurationInstanceGeneratorFactory nonConfigurationInstanceGeneratorFactory;
    private final OnCreateInjectionGenerator.InjectionGeneratorFactory onCreateInjectionGeneratorFactory;
    private final ScopesGeneration.ScopesGenerationFactory scopesGenerationFactory;
    private final ComponentAnalysis componentAnalysis;

    @Inject
    public ActivityAnalysis(InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory,
                            AnalysisContextFactory analysisContextFactory,
                            ASTElementFactory astElementFactory,
                            ProviderInjectionNodeBuilderFactory providerInjectionNodeBuilder,
                            ASTTypeBuilderVisitor astTypeBuilderVisitor,
                            InjectionBindingBuilder injectionBindingBuilder,
                            ObservesExpressionGenerator.ObservesExpressionGeneratorFactory observesExpressionGeneratorFactory,
                            ViewInjectionNodeBuilder viewVariableBuilder,
                            ExtraInjectionNodeBuilder extraInjectionNodeBuilder,
                            SystemServiceBindingInjectionNodeBuilder systemServiceBindingInjectionNodeBuilder,
                            ResourceInjectionNodeBuilder resourceInjectionNodeBuilder,
                            PreferenceInjectionNodeBuilder preferenceInjectionNodeBuilder,
                            Provider<ActivityManifestEntryGenerator> manifestGeneratorProvider,
                            LayoutGenerator layoutGenerator,
                            LayoutHandlerGenerator layoutHandlerGenerator,
                            WindowFeatureGenerator windowFeatureGenerator,
                            GeneratorFactory generatorFactory,
                            ListenerRegistrationGenerator.ListerRegistrationGeneratorFactory listerRegistrationGeneratorFactory,
                            NonConfigurationInstanceGenerator.NonconfigurationInstanceGeneratorFactory nonConfigurationInstanceGeneratorFactory,
                            OnCreateInjectionGenerator.InjectionGeneratorFactory onCreateInjectionGeneratorFactory,
                            ScopesGeneration.ScopesGenerationFactory scopesGenerationFactory,
                            ComponentAnalysis componentAnalysis) {
        this.injectionNodeBuilderRepositoryFactory = injectionNodeBuilderRepositoryFactory;
        this.analysisContextFactory = analysisContextFactory;
        this.astElementFactory = astElementFactory;
        this.providerInjectionNodeBuilder = providerInjectionNodeBuilder;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.observesExpressionGeneratorFactory = observesExpressionGeneratorFactory;
        this.viewVariableBuilder = viewVariableBuilder;
        this.extraInjectionNodeBuilder = extraInjectionNodeBuilder;
        this.systemServiceBindingInjectionNodeBuilder = systemServiceBindingInjectionNodeBuilder;
        this.resourceInjectionNodeBuilder = resourceInjectionNodeBuilder;
        this.preferenceInjectionNodeBuilder = preferenceInjectionNodeBuilder;
        this.manifestGeneratorProvider = manifestGeneratorProvider;
        this.layoutGenerator = layoutGenerator;
        this.layoutHandlerGenerator = layoutHandlerGenerator;
        this.windowFeatureGenerator = windowFeatureGenerator;
        this.generatorFactory = generatorFactory;
        this.listerRegistrationGeneratorFactory = listerRegistrationGeneratorFactory;
        this.nonConfigurationInstanceGeneratorFactory = nonConfigurationInstanceGeneratorFactory;
        this.onCreateInjectionGeneratorFactory = onCreateInjectionGeneratorFactory;
        this.scopesGenerationFactory = scopesGenerationFactory;
        this.componentAnalysis = componentAnalysis;
    }

    public ComponentDescriptor analyze(ASTType input) {

        Activity activityAnnotation = input.getAnnotation(Activity.class);
        PackageClass activityClassName;
        ComponentDescriptor activityDescriptor;

        if (input.extendsFrom(AndroidLiterals.ACTIVITY)) {
            //vanilla Android activity
            PackageClass activityPackageClass = input.getPackageClass();
            activityClassName = componentAnalysis.buildComponentPackageClass(input, activityPackageClass.getClassName(), "Activity");
            activityDescriptor = new ComponentDescriptorImpl(input, null, activityClassName);
        } else {
            //generated Android activity
            activityClassName = componentAnalysis.buildComponentPackageClass(input, activityAnnotation.name(), "Activity");

            TypeMirror type = getTypeMirror(activityAnnotation, "type");

            ASTType activityType = type == null || type.toString().equals("java.lang.Object") ? AndroidLiterals.ACTIVITY : type.accept(astTypeBuilderVisitor, null);

            AnalysisContext context = analysisContextFactory.buildAnalysisContext(buildVariableBuilderMap(activityType));

            activityDescriptor = new ComponentDescriptorImpl(input, activityType, activityClassName, context);

            componentAnalysis.buildDescriptor(activityDescriptor, activityType, Activity.class);

            activityDescriptor.getGenerators().add(layoutGenerator);
            activityDescriptor.getGenerators().add(layoutHandlerGenerator);
            activityDescriptor.getGenerators().add(windowFeatureGenerator);
            activityDescriptor.getGenerators().add(scopesGenerationFactory.build(getASTMethod("onCreate", AndroidLiterals.BUNDLE)));
            activityDescriptor.getGenerators().add(onCreateInjectionGeneratorFactory.build(getASTMethod("onCreate", AndroidLiterals.BUNDLE), input));
            //extra intent factory
            activityDescriptor.getGenerators().add(generatorFactory.buildStrategyGenerator(ActivityIntentFactoryStrategy.class));
            //listener registration
            activityDescriptor.getGenerators().add(listerRegistrationGeneratorFactory.build(getASTMethod("onCreate", AndroidLiterals.BUNDLE)));
            //non configuration instance update
            activityDescriptor.getGenerators().add(nonConfigurationInstanceGeneratorFactory.build(getASTMethod("onCreate", AndroidLiterals.BUNDLE)));

            componentAnalysis.setupGenerators(activityDescriptor, activityType, Activity.class);

            activityDescriptor.getGenerators().add(observesExpressionGeneratorFactory.build(
                    getASTMethod("onCreate", AndroidLiterals.BUNDLE),
                    getASTMethod("onResume"),
                    getASTMethod("onPause")));
        }

        //add manifest elements
        activityDescriptor.getGenerators().add(manifestGeneratorProvider.get());

        return activityDescriptor;
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(ASTType activityType) {

        InjectionNodeBuilderRepository injectionNodeBuilderRepository = componentAnalysis.setupInjectionNodeBuilderRepository(activityType, Activity.class);

        ASTType applicationScopeType = astElementFactory.getType(ApplicationScope.ApplicationScopeQualifier.class);
        ASTType applicationProvider = astElementFactory.getType(ApplicationScope.ApplicationProvider.class);
        injectionNodeBuilderRepository.putType(AndroidLiterals.APPLICATION, providerInjectionNodeBuilder.builderProviderBuilder(applicationProvider));
        injectionNodeBuilderRepository.putType(AndroidLiterals.CONTEXT, injectionBindingBuilder.buildThis(AndroidLiterals.CONTEXT));
        injectionNodeBuilderRepository.putScoped(new InjectionSignature(AndroidLiterals.APPLICATION), applicationScopeType);

        injectionNodeBuilderRepository.putType(AndroidLiterals.ACTIVITY, injectionBindingBuilder.buildThis(AndroidLiterals.ACTIVITY));

        while(!activityType.equals(AndroidLiterals.ACTIVITY) && activityType.inheritsFrom(AndroidLiterals.ACTIVITY)){
            injectionNodeBuilderRepository.putType(activityType, injectionBindingBuilder.buildThis(activityType));
            activityType = activityType.getSuperClass();
        }

        injectionNodeBuilderRepository.putAnnotation(Extra.class, extraInjectionNodeBuilder);
        injectionNodeBuilderRepository.putAnnotation(Resource.class, resourceInjectionNodeBuilder);
        injectionNodeBuilderRepository.putAnnotation(SystemService.class, systemServiceBindingInjectionNodeBuilder);
        injectionNodeBuilderRepository.putAnnotation(Preference.class, preferenceInjectionNodeBuilder);
        injectionNodeBuilderRepository.putAnnotation(View.class, viewVariableBuilder);

        injectionNodeBuilderRepository.addRepository(
                injectionNodeBuilderRepositoryFactory.buildApplicationInjections());

        injectionNodeBuilderRepository.addRepository(
                injectionNodeBuilderRepositoryFactory.buildModuleConfiguration());

        return injectionNodeBuilderRepository;

    }

    private ASTMethod getASTMethod(String methodName, ASTType... args) {
        return getASTMethod(AndroidLiterals.ACTIVITY, methodName, args);
    }

    private ASTMethod getASTMethod(ASTType type, String methodName, ASTType... args) {
        return astElementFactory.findMethod(type, methodName, args);
    }
}