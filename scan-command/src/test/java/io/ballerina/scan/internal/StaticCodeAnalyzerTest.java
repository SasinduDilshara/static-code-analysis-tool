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
        Assert.assertEquals(issues.size(), 2);
        Issue issue = issues.get(0);
        Assert.assertEquals(issue.source(), Source.BUILT_IN);
        LineRange location = issue.location().lineRange();
        Assert.assertEquals(location.fileName(), documentName);
        Assert.assertEquals(location.startLine().line(), 18);
        Assert.assertEquals(location.startLine().offset(), 24);
        Assert.assertEquals(location.endLine().line(), 18);
        Assert.assertEquals(location.endLine().offset(), 46);
        Rule rule = issue.rule();
        Assert.assertEquals(rule.id(), "ballerina:1");
        Assert.assertEquals(rule.numericId(), 1);
        Assert.assertEquals(rule.description(), "Avoid checkpanic");
        Assert.assertEquals(rule.kind(), RuleKind.CODE_SMELL);

        issue = issues.get(1);
        Assert.assertEquals(issue.source(), Source.BUILT_IN);
        location = issue.location().lineRange();
        Assert.assertEquals(location.fileName(), documentName);
        Assert.assertEquals(location.startLine().line(), 22);
        Assert.assertEquals(location.startLine().offset(), 17);
        Assert.assertEquals(location.endLine().line(), 22);
        Assert.assertEquals(location.endLine().offset(), 39);
        rule = issue.rule();
        Assert.assertEquals(rule.id(), "ballerina:1");
        Assert.assertEquals(rule.numericId(), 1);
        Assert.assertEquals(rule.description(), "Avoid checkpanic");
        Assert.assertEquals(rule.kind(), RuleKind.CODE_SMELL);
    }

    @Test(description = "test duplicate constiuent with union analyzer")
    void testDuplicateConstiuentWithUnionAnalyzer() {
        String documentName = "duplicate_constiuent_types_union.bal";
        Document document = loadDocument(documentName);
        ScannerContextImpl scannerContext = new ScannerContextImpl(List.of(
                CoreRule.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES.rule()));
        StaticCodeAnalyzer staticCodeAnalyzer = new StaticCodeAnalyzer(document, scannerContext);
        staticCodeAnalyzer.analyze();

        List<Issue> issues = scannerContext.getReporter().getIssues();
        Assert.assertEquals(issues.size(), 26);

        assertIssue(issues.get(0), documentName, 20, 8, 20, 43, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(1), documentName, 21, 8, 21, 46, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(2), documentName, 22, 8, 22, 91, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(3), documentName, 25, 8, 25, 19, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(4), documentName, 26, 8, 26, 30, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(5), documentName, 28, 8, 28, 43, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(6), documentName, 31, 0, 31, 7, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(7), documentName, 32, 0, 32, 14, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(8), documentName, 33, 0, 33, 37, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(9), documentName, 35, 0, 35, 37, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(10), documentName, 36, 0, 36, 38, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(11), documentName, 40, 0, 40, 11, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(12), documentName, 41, 0, 41, 22, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(13), documentName, 43, 0, 43, 35, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(14), documentName, 47, 4, 47, 11, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(15), documentName, 48, 4, 48, 18, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(16), documentName, 49, 4, 49, 41, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(17), documentName, 51, 4, 51, 39, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(18), documentName, 52, 4, 52, 42, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(19), documentName, 53, 4, 53, 87, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(20), documentName, 56, 4, 56, 15, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(21), documentName, 57, 4, 57, 26, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(22), documentName, 59, 4, 59, 39, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(23), documentName, 65, 4, 65, 30, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(24), documentName, 66, 4, 66, 34, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(25), documentName, 69, 4, 69, 63, "ballerina:2", 2,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_UNION_TYPES, RuleKind.CODE_SMELL);
    }

    @Test(description = "test duplicate constiuent with intersection analyzer")
    void testDuplicateConstiuentWithIntersectionAnalyzer() {
        String documentName = "duplicate_constiuent_types_intersection.bal";
        Document document = loadDocument(documentName);
        ScannerContextImpl scannerContext = new ScannerContextImpl(List.of(
                CoreRule.NO_DUPLICATE_CONSTITUENTS_IN_INTERSECTION_TYPES.rule()));
        StaticCodeAnalyzer staticCodeAnalyzer = new StaticCodeAnalyzer(document, scannerContext);
        staticCodeAnalyzer.analyze();

        List<Issue> issues = scannerContext.getReporter().getIssues();
        Assert.assertEquals(issues.size(), 3);
        assertIssue(issues.get(0), documentName, 20, 8, 20, 38, "ballerina:3", 3,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_INTERSECTION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(1), documentName, 21, 8, 21, 36, "ballerina:3", 3,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_INTERSECTION_TYPES, RuleKind.CODE_SMELL);
        assertIssue(issues.get(2), documentName, 22, 8, 22, 38, "ballerina:3", 3,
                Constants.RuleDescription.NO_DUPLICATE_CONSTITUENTS_IN_INTERSECTION_TYPES, RuleKind.CODE_SMELL);
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
