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
package org.androidtransfuse.gen.variableBuilder.resource;

import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.variableDecorator.TypedExpressionFactory;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.util.AndroidLiterals;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class AnimationResourceExpressionBuilder implements ResourceExpressionBuilder {

    private static final String LOAD_ANIMATION = "loadAnimation";

    private final ClassGenerationUtil generationUtil;
    private final InjectionNode applicationInjectionNode;
    private final InjectionExpressionBuilder injectionExpressionBuilder;
    private final TypedExpressionFactory typedExpressionFactory;

    @Inject
    public AnimationResourceExpressionBuilder(/*@Assisted*/ InjectionNode applicationInjectionNode,
                                              ClassGenerationUtil generationUtil,
                                              InjectionExpressionBuilder injectionExpressionBuilder, TypedExpressionFactory typedExpressionFactory) {
        this.generationUtil = generationUtil;
        this.applicationInjectionNode = applicationInjectionNode;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.typedExpressionFactory = typedExpressionFactory;
    }

    @Override
    public TypedExpression buildExpression(InjectionBuilderContext context, JExpression resourceIdExpr) {
        TypedExpression applicationVar = injectionExpressionBuilder.buildVariable(context, applicationInjectionNode);

        //AnimationUtils.loadAnimation(application, id);
        JExpression expression = generationUtil.ref(AndroidLiterals.ANIMATION_UTILS).staticInvoke(LOAD_ANIMATION).arg(applicationVar.getExpression()).arg(resourceIdExpr);
        return typedExpressionFactory.build(AndroidLiterals.ANIMATION, expression);
    }
}
