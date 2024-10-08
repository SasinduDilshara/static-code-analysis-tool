/*
 *  Copyright (c) 2024, WSO2 LLC. (https://www.wso2.com).
 *
 *  WSO2 LLC. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied. See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package io.ballerina.scan.internal;

import io.ballerina.compiler.api.SemanticModel;
import io.ballerina.compiler.api.symbols.FunctionSymbol;
import io.ballerina.compiler.syntax.tree.CheckExpressionNode;
import io.ballerina.compiler.syntax.tree.FunctionCallExpressionNode;
import io.ballerina.compiler.syntax.tree.FunctionDefinitionNode;
import io.ballerina.compiler.syntax.tree.ModulePartNode;
import io.ballerina.compiler.syntax.tree.NameReferenceNode;
import io.ballerina.compiler.syntax.tree.Node;
import io.ballerina.compiler.syntax.tree.NodeVisitor;
import io.ballerina.compiler.syntax.tree.SyntaxKind;
import io.ballerina.compiler.syntax.tree.SyntaxTree;
import io.ballerina.projects.Document;
import io.ballerina.scan.ScannerContext;

/**
 * {@code StaticCodeAnalyzer} contains the logic to perform core static code analysis on Ballerina documents.
 *
 * @since 0.1.0
 * */
class StaticCodeAnalyzer extends NodeVisitor {
    private final Document document;
    private final SyntaxTree syntaxTree;
    private final ScannerContext scannerContext;
    private final SemanticModel semanticModel;

    StaticCodeAnalyzer(Document document, ScannerContextImpl scannerContext) {
        this.document = document;
        this.syntaxTree = document.syntaxTree();
        this.scannerContext = scannerContext;
        semanticModel = document.module().getCompilation().getSemanticModel();
    }

    void analyze() {
        this.visit((ModulePartNode) syntaxTree.rootNode());
    }

    /**
     * Visits check expressions in a Ballerina document and perform static code analysis.
     *
     * @param checkExpressionNode node that represents a check expression
     */
    @Override
    public void visit(CheckExpressionNode checkExpressionNode) {
        if (checkExpressionNode.checkKeyword().kind().equals(SyntaxKind.CHECKPANIC_KEYWORD)) {
            scannerContext.getReporter().reportIssue(document, checkExpressionNode.location(),
                    CoreRule.AVOID_CHECKPANIC.rule());
        }
    }

    @Override
    public void visit(FunctionCallExpressionNode functionCallExpressionNode) {
        checkForSelfRecursions(functionCallExpressionNode);
    }

    private void checkForSelfRecursions(FunctionCallExpressionNode functionCallExpressionNode) {
        NameReferenceNode callerName = functionCallExpressionNode.functionName();
        if (callerName.kind().equals(SyntaxKind.SIMPLE_NAME_REFERENCE)) {
            Node parentNode = functionCallExpressionNode.parent();
            while (parentNode != null) {
                if (parentNode instanceof FunctionDefinitionNode parentFunctionNode) {
                    semanticModel.symbol(parentFunctionNode).ifPresent(functionSymbol -> {
                        
                    });
                }
                parentNode = parentNode.parent();
            }
        }
    }
}
