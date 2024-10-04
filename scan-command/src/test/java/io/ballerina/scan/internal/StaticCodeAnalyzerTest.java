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

import io.ballerina.projects.Document;
import io.ballerina.projects.Module;
import io.ballerina.projects.Project;
import io.ballerina.projects.directory.SingleFileProject;
import io.ballerina.scan.BaseTest;
import io.ballerina.scan.Issue;
import io.ballerina.scan.Rule;
import io.ballerina.scan.RuleKind;
import io.ballerina.scan.Source;
import io.ballerina.scan.utils.Constants;
import io.ballerina.tools.text.LineRange;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.util.List;

/**
 * Core analyzer tests.
 *
 * @since 0.1.0
 */
public class StaticCodeAnalyzerTest extends BaseTest {
    private final Path coreRuleBalFiles = testResources.resolve("test-resources").resolve("core-rules");

    private Document loadDocument(String documentName) {
        Project project = SingleFileProject.load(coreRuleBalFiles.resolve(documentName));
        Module defaultModule = project.currentPackage().getDefaultModule();
        return defaultModule.document(defaultModule.documentIds().iterator().next());
    }

    @Test(description = "test checkpanic analyzer")
    void testCheckpanicAnalyzer() {
        String documentName = "rule_checkpanic.bal";
        Document document = loadDocument(documentName);
        ScannerContextImpl scannerContext = new ScannerContextImpl(List.of(CoreRule.AVOID_CHECKPANIC.rule()));
        StaticCodeAnalyzer staticCodeAnalyzer = new StaticCodeAnalyzer(document, scannerContext);
        staticCodeAnalyzer.analyze();
        List<Issue> issues = scannerContext.getReporter().getIssues();
        Assert.assertEquals(issues.size(), 1);
        Issue issue = issues.get(0);
        Assert.assertEquals(issue.source(), Source.BUILT_IN);
        LineRange location = issue.location().lineRange();
        Assert.assertEquals(location.fileName(), documentName);
        Assert.assertEquals(location.startLine().line(), 20);
        Assert.assertEquals(location.startLine().offset(), 17);
        Assert.assertEquals(location.endLine().line(), 20);
        Assert.assertEquals(location.endLine().offset(), 39);
        Rule rule = issue.rule();
        Assert.assertEquals(rule.id(), "ballerina:1");
        Assert.assertEquals(rule.numericId(), 1);
        Assert.assertEquals(rule.description(), "Avoid checkpanic");
        Assert.assertEquals(rule.kind(), RuleKind.CODE_SMELL);
    }

    @Test(description = "test dead store analyzer")
    void testDeadStoreAnalyzerAnalyzer() {
        String documentName = "dead_store.bal";
        Document document = loadDocument(documentName);
        ScannerContextImpl scannerContext = new ScannerContextImpl(List.of(CoreRule.DEAD_STORE.rule()));
        StaticCodeAnalyzer staticCodeAnalyzer = new StaticCodeAnalyzer(document, scannerContext);
        staticCodeAnalyzer.analyze();
        List<Issue> issues = scannerContext.getReporter().getIssues();

        Assert.assertEquals(issues.size(), 25);
        assertIssue(issues.get(0), documentName, 11, 4, 11, 13, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(1), documentName, 10, 8, 10, 17, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(2), documentName, 21, 47, 21, 56, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(3), documentName, 21, 58, 21, 67, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(4), documentName, 20, 19, 20, 28, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(5), documentName, 20, 30, 20, 39, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(6), documentName, 22, 8, 22, 17, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(7), documentName, 19, 4, 19, 13, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(8), documentName, 18, 8, 18, 17, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(9), documentName, 16, 8, 16, 17, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(10), documentName, 50, 4, 50, 13, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(11), documentName, 47, 4, 47, 13, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(12), documentName, 27, 8, 27, 20, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(13), documentName, 41, 4, 41, 13, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(14), documentName, 63, 16, 63, 25, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(15), documentName, 63, 5, 63, 14, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(16), documentName, 56, 5, 56, 14, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(17), documentName, 81, 4, 81, 13, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(18), documentName, 74, 15, 74, 24, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(19), documentName, 77, 4, 77, 13, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(20), documentName, 81, 15, 81, 24, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(21), documentName, 74, 4, 74, 13, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(22), documentName, 68, 4, 68, 13, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(23), documentName, 104, 24, 104, 40, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
        assertIssue(issues.get(24), documentName, 104, 10, 104, 22, "ballerina:5", 5,
                Constants.RuleDescription.DEAD_STORE, RuleKind.CODE_SMELL);
    }

    @Test(description = "test dead store analyzer with clauses")
    void testDeadStoreAnalyzerAnalyzerWithClauses() {
        String documentName = "dead_stores_including_clauses.bal";
        Document document = loadDocument(documentName);
        ScannerContextImpl scannerContext = new ScannerContextImpl(List.of(CoreRule.DEAD_STORE.rule()));
        StaticCodeAnalyzer staticCodeAnalyzer = new StaticCodeAnalyzer(document, scannerContext);
        staticCodeAnalyzer.analyze();
        List<Issue> issues = scannerContext.getReporter().getIssues();

        String s = "";
        issues.sort((o1, o2) -> o1.location().lineRange().startLine().line() - o2.location().lineRange().startLine().line());
        for (int i = 0; i < issues.size(); i++) {
            Issue issue = issues.get(i);
            LineRange location = issue.location().lineRange();
            int a = location.startLine().line();
            s += a + 1 + ", ";
        }
        String t = s;


        Assert.assertEquals(issues.size(), 1);
    }

    void assertIssue(Issue issue, String documentName, int startLine, int startOffset, int endLine, int endOffset,
                     String ruleId, int numericId, String description, RuleKind ruleKind) {
        Assert.assertEquals(issue.source(), Source.BUILT_IN);
        LineRange location = issue.location().lineRange();
        Assert.assertEquals(location.fileName(), documentName);
        Assert.assertEquals(location.startLine().line(), startLine);
        Assert.assertEquals(location.startLine().offset(), startOffset);
        Assert.assertEquals(location.endLine().line(), endLine);
        Assert.assertEquals(location.endLine().offset(), endOffset);
        Rule rule = issue.rule();
        Assert.assertEquals(rule.id(), ruleId);
        Assert.assertEquals(rule.numericId(), numericId);
        Assert.assertEquals(rule.description(), description);
        Assert.assertEquals(rule.kind(), ruleKind);
    }
}
