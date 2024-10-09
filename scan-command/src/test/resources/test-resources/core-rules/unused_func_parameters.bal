// Copyright (c) 2024, WSO2 LLC. (https://www.wso2.com).
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import ballerina/http;

function test(int a, int b) returns int {
    return a + b;
}

function test2(int a, int b) returns int => a + b;

function test3(int a, int b, int c) returns int { // warning
    return a + b;
}

function test4(int a, int b, int c) returns int => a + b; // warning

function test5(int a, int b, int c, int d) returns int { // warning * 2
    return test(a, b);
}

function test6(int a, int b, int c, int d) returns int => test(a, b); // warning * 2

function test7(int a, int b, int c = 3) returns int {
    return a + b + c;
}

function test8(int a, int b, int c = 3) returns int { //warning
    return a + b;
}

function test9(int a, int b, int c = 3) returns int => a + b; // warning

function test10(int a, int b, int c = 3) returns int => c + test(a, b);

function test11(int a, int b, int... c) returns string => a.toString(); // warning * 2

function test12(int a, int b, int... c) returns string { // warning * 2
    return a.toString();
}

class A {
    function test(int a, int b) returns int {
        return a + b;
    }

    function test2(int a, int b) returns int => a + b;

    function test3(int a, int b, int c) returns int { // warning
        return a + b;
    }

    function test4(int a, int b, int c) returns int => a + b; // warning

    function test5(int a, int b, int c, int d) returns int { // warning * 2
        return test(a, b);
    }

    function test6(int a, int b, int c, int d) returns int => test(a, b); // warning * 2

    function test7(int a, int b, int c = 3) returns int {
        return a + b + c;
    }

    function test8(int a, int b, int c = 3) returns int { // warning
        return a + b;
    }

    function test9(int a, int b, int c = 3) returns int => a + b; // warning

    function test10(int a, int b, int c = 3) returns int => c + test(a, b);

    function test11(int a, int b, int... c) returns string => a.toString(); // warning * 2

    function test12(int a, int b, int... c) returns string { // warning * 2
        return a.toString();
    }

    function testIncludedParams(*IncludedRecord includedparam) {// warning
        return;
    }

    function testIncludedParams2(*IncludedRecord includedparam) {
        _ = includedparam;
        return;
    }
}

service /a on new http:Listener(8080) {
    resource function get test(int a, int b) returns int {
        return a + b;
    }

    resource function get test2(int a, int b) returns int => a + b;

    resource function get test3(int a, int b, int c) returns int { // warning
        return a + b;
    }

    resource function post test4(int a, int b, int c) returns int => a + b; // warning

    resource function post test5(int a, int b, int c, int d) returns int { // warning * 2
        return test(a, b);
    }

    resource function post test6(int a, int b, int c, int d) returns int => test(a, b); // warning * 2

    resource function post test7(int a, int b, int c = 3) returns int {
        return a + b + c;
    }

    resource function post test8(int a, int b, int c = 3) returns int {  // warning
        return a + b;
    }

    resource function post test9(int a, int b, int c = 3) returns int => a + b; // warning

    resource function post test10(int a, int b, int c = 3) returns int => c + test(a, b);

    resource function post test11(int a, int b, int... c) returns string => a.toString(); // warning * 2

    resource function post test12(int a, int b, int... c) returns string { // warning * 2
        return a.toString();
    }

    function testIncludedParams(*IncludedRecord includedparam) {// warning
        return;
    }

    function testIncludedParams2(*IncludedRecord includedparam) {
        _ = includedparam;
        return;
    }
}

object {} a = object {
    function test(int a, int b) returns int {
        return a + b;
    }

    function test2(int a, int b) returns int => a + b;

    function test3(int a, int b, int c) returns int { // warning
        return a + b;
    }

    function test4(int a, int b, int c) returns int => a + b; // warning

    function test5(int a, int b, int c, int d) returns int { // warning * 2
        return test(a, b);
    }

    function test6(int a, int b, int c, int d) returns int => test(a, b); // warning * 2

    function test7(int a, int b, int c = 3) returns int {
        return a + b + c;
    }

    function test8(int a, int b, int c = 3) returns int { // warning
        return a + b;
    }

    function test9(int a, int b, int c = 3) returns int => a + b; // warning

    function test10(int a, int b, int c = 3) returns int => c + test(a, b);

    function test11(int a, int b, int... c) returns string => a.toString(); // warning * 2

    function test12(int a, int b, int... c) returns string { // warning * 2
        return a.toString();
    }

    function testIncludedParams(*IncludedRecord includedparam) {// warning
        return;
    }

    function testIncludedParams2(*IncludedRecord includedparam) {
        _ = includedparam;
        return;
    }
};

public function main(int a, int b, int c) { // warning
    _ = test(a, c);
    [1,2].forEach(element => ()); // warning
    [1,2].forEach(element => doNothing(element));
}

function doNothing(int a) { // warning
    return;
}

public function t(int a, int b, int c) { // warning * 2
    var fn = function(int a2, int b2) returns int => b; // warning * 2
    int _ = fn(1,2);
}

type IncludedRecord record {int a;};

function testIncludedParams(*IncludedRecord includedparam) {// warning
    return;
}

function testIncludedParams2(*IncludedRecord includedparam) {
    _ = includedparam;
    return;
}
