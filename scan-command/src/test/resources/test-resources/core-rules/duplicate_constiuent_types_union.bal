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

type A record {
    int a;
}|int;

type A2 record {int a;}|int|record {int a;}; // warning
type A3 record {int a;}|record {int a;}|string; // warning
type A4 boolean|(record {int a;} & readonly)|int|record {int a;}|()|record {int a;}|decimal; // warning
type A5 record {|int a;|}|int|record {int a;};

type A6 int|int|int; // warning
type A7 int|boolean|int|string; // warning
type A8 boolean|(int & readonly)|int|(float|decimal)|();
type A9 record {|int a;|}|int|(decimal|int); // warning

int|string moduleLevelvar = 1;
int|int moduleLevelvar2 = 1; // warning
int|string|int moduleLevelvar3 = 1; // warning
int|(string & readonly)|(int)|boolean moduleLevelvar4 = 1; // warning

record {int a;}|int|(record {int a;}) moduleLevelRecordvar = 1; // warning
record {int a;}|record {int a;}|string moduleLevelRecordvar2 = "string"; // warning
boolean|(record {int a;} & readonly)|int|record {int a;}|()|decimal moduleLevelRecordvar3 = 1;
record {|int a;|}|(int & readonly)|record {int a;} moduleLevelRecordvar4 = 1;

int|int|int moduleLevelSimpleVar = 1; // warning
int|boolean|int|string moduleLevelSimpleVar2 = 1; // warning
boolean|(int & readonly)|int|(float|decimal)|() moduleLevelSimpleVar3 = 1;
record {|int a;|}|int|(decimal|int) _ = 1; // warning

public function main() {
    int|string funcLevelvar = 1;
    int|int funcLevelvar2 = 1; // warning.
    int|string|int funcLevelvar3 = 1; // warning.
    int|(string & readonly)|(int)|boolean funcLevelvar4 = 1; // warning.

    record {int a;}|int|record {int a;} funcLevelRecordvar = 1; // warning.
    record {int a;}|record {int a;}|string funcLevelRecordvar2 = "str"; // warning.
    boolean|(record {int a;} & readonly)|int|record {int a;}|()|record {int a;}|decimal funcLevelRecordvar3 = 12d; // warning.
    record {|int a;|}|int|record {int a;} funcLevelRecordvar4 = 3;

    int|int|int funcLevelSimpleVar = 1; // warning
    int|boolean|int|string _ = 1; // warning
    boolean|(int & readonly)|int|(float|decimal)|() _ = 1;
    record {|int a;|}|int|(decimal|int) _ = 1; // warning
}

function test() {
    int|"int" a = 1;
    string|"string" b = "string";
    "string"|(string|"string") b = "string"; // warning
    "a"|string|(int|(decimal|"a")) c = "a"; // warning
    "a"|string|(int|(decimal|record {"a" a;})) d = "a";
    "a"|string|(int|(decimal|record {decimal a;})) e = "a";
    "a"|record {decimal a;}|(int|(decimal|record {decimal a;})) e = "a"; // warning
//    record{}|int|record {} a2 = {}; // Currently there is no warning for this. TODO://
//    record{record{} a;}|int|record {record {} a;} a2 = {}; // Currently there is no warning for this. TODO://
}
