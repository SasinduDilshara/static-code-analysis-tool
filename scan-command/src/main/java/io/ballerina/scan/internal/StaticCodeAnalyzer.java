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

import io.ballerina.compiler.syntax.tree.AssignmentStatementNode;
import io.ballerina.compiler.syntax.tree.CaptureBindingPatternNode;
import io.ballerina.compiler.syntax.tree.CompoundAssignmentStatementNode;
import io.ballerina.compiler.syntax.tree.FieldBindingPatternVarnameNode;
import io.ballerina.compiler.syntax.tree.FunctionDefinitionNode;
import io.ballerina.compiler.syntax.tree.ModulePartNode;
import io.ballerina.compiler.syntax.tree.ModuleVariableDeclarationNode;
import io.ballerina.compiler.syntax.tree.NamedArgBindingPatternNode;
import io.ballerina.compiler.syntax.tree.NodeLocation;
import io.ballerina.compiler.syntax.tree.NodeVisitor;
import io.ballerina.compiler.syntax.tree.RestBindingPatternNode;
import io.ballerina.compiler.syntax.tree.SimpleNameReferenceNode;
import io.ballerina.compiler.syntax.tree.SyntaxTree;
import io.ballerina.compiler.syntax.tree.TypedBindingPatternNode;
import io.ballerina.projects.Document;
import io.ballerina.scan.ScannerContext;

import java.util.HashSet;
import java.util.Set;

/**
 * {@code StaticCodeAnalyzer} contains the logic to perform core static code analysis on Ballerina documents.
 *
 * @since 0.1.0
 * */
class StaticCodeAnalyzer extends NodeVisitor {

    private final Document document;
    private final SyntaxTree syntaxTree;
    private final ScannerContext scannerContext;
    private final AnalyzerData analyzerData = new AnalyzerData();

    StaticCodeAnalyzer(Document document, ScannerContextImpl scannerContext) {
        this.document = document;
        this.syntaxTree = document.syntaxTree();
        this.scannerContext = scannerContext;
    }

    void analyze() {
        this.visit((ModulePartNode) syntaxTree.rootNode());
    }

    @Override
    public void visit(ModulePartNode modulePartNode) {
        modulePartNode.members().forEach(member -> member.accept(this));
    }

    @Override
    public void visit(FunctionDefinitionNode functionDefinitionNode) {
        super.visit(functionDefinitionNode);
        reportLocalDeadStores(document, CoreRule.DEAD_STORE);
    }

    private void reportLocalDeadStores(Document document, CoreRule coreRule) {
        this.analyzerData.localVarReferenceSet.forEach(localVar -> {
            scannerContext.getReporter().reportIssue(document, localVar.location, coreRule.rule());
        });
        this.analyzerData.localVarReferenceSet.clear();
        this.analyzerData.localVarDeclarationSet.clear();
    }

    @Override
    public void visit(ModuleVariableDeclarationNode moduleVariableDeclarationNode) {
        analyzerData.moduleLevelVarDecl = true;
        super.visit(moduleVariableDeclarationNode);
        analyzerData.moduleLevelVarDecl = false;
    }

    @Override
    public void visit(TypedBindingPatternNode typedBindingPatternNode) {
        analyzerData.localVarDecl = !analyzerData.moduleLevelVarDecl;
        super.visit(typedBindingPatternNode);
        analyzerData.localVarDecl = false;
    }

    @Override
    public void visit(AssignmentStatementNode assignmentStatementNode) {
        // First evaluate the RHS expression.
        assignmentStatementNode.expression().accept(this);
        this.analyzerData.localVarAssignment = true;
        assignmentStatementNode.varRef().accept(this);
        this.analyzerData.localVarAssignment = false;
    }

    @Override
    public void visit(CompoundAssignmentStatementNode captureBindingPatternNode) {
        // First evaluate the RHS expression.
        captureBindingPatternNode.rhsExpression().accept(this);
        this.analyzerData.localVarAssignment = true;
        captureBindingPatternNode.lhsExpression().accept(this);
        this.analyzerData.localVarAssignment = false;
    }

    @Override
    public void visit(CaptureBindingPatternNode captureBindingPatternNode) {
        this.analyzerData.addVariableToVarDeclarationSet(
                captureBindingPatternNode.variableName().text(),
                captureBindingPatternNode.location(), analyzerData.moduleLevelVarDecl);
    }

    @Override
    public void visit(RestBindingPatternNode restBindingPatternNode) {
        // Rest binding pattern is a variable declaration.
        this.analyzerData.addVariableToVarDeclarationSet(
                restBindingPatternNode.variableName().name().text(),
                restBindingPatternNode.location(), analyzerData.moduleLevelVarDecl);
    }

    @Override
    public void visit(NamedArgBindingPatternNode namedArgBindingPatternNode) {
        // Named arg binding pattern is a variable declaration.
        this.analyzerData.addVariableToVarDeclarationSet(
                namedArgBindingPatternNode.argName().text(),
                namedArgBindingPatternNode.location(), analyzerData.moduleLevelVarDecl);
    }

    @Override
    public void visit(FieldBindingPatternVarnameNode fieldBindingPatternVarnameNode) {
        // Field binding pattern varname is a variable declaration.
        this.analyzerData.addVariableToVarDeclarationSet(
                fieldBindingPatternVarnameNode.variableName().name().text(),
                fieldBindingPatternVarnameNode.location(), analyzerData.moduleLevelVarDecl);
    }

    @Override
    public void visit(SimpleNameReferenceNode simpleNameReferenceNode) {
        String name = simpleNameReferenceNode.name().text();
        if (this.analyzerData.localVarAssignment && analyzerData.localVarDeclarationSet.contains(name)) {
            // This is a local variable assignment.
            this.analyzerData.addVariableToVarDeclarationSet(name,
                    simpleNameReferenceNode.location(), analyzerData.moduleLevelVarDecl);
        } else {
            // This is a variable reference.
            this.analyzerData.localVarReferenceSet.removeIf(localVar -> localVar.name.equals(name));
        }
        simpleNameReferenceNode.name().accept(this);
    }
}

class AnalyzerData {
    public Set<LocalVariableMetadata> localVarReferenceSet = new HashSet<>();
    public Set<String> localVarDeclarationSet = new HashSet<>();
    boolean localVarAssignment = false;
    boolean moduleLevelVarDecl = false;
    boolean localVarDecl = false;

    public void addVariableToVarDeclarationSet(String name, NodeLocation location, boolean isModuleLevelVarDecl) {
        if (this.localVarDecl)  {
            this.localVarDeclarationSet.add(name);
            this.localVarReferenceSet.add(LocalVariableMetadata.from(name, location));
            return;
        }

        if (this.localVarDeclarationSet.contains(name)) {
            this.localVarReferenceSet.removeIf(localVar -> localVar.name.equals(name));
            this.localVarReferenceSet.add(LocalVariableMetadata.from(name, location));
        }

        // Ignore Module level variable declarations and assignments.
    }
}

class LocalVariableMetadata {
    public String name;
    public NodeLocation location;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof String) {
            return this.name.equals(obj);
        }
        if (obj instanceof LocalVariableMetadata) {
            return this.name.equals(((LocalVariableMetadata) obj).name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    public LocalVariableMetadata(String name, NodeLocation location) {
        this.name = name;
        this.location = location;
    }

    public static LocalVariableMetadata from(String name, NodeLocation location) {
        return new LocalVariableMetadata(name, location);
    }
}
