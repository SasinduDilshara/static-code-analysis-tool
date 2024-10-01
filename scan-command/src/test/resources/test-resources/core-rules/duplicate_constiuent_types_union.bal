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
}|int; // no warning

type A2 record {int a;}|int|record {int a;};
type A3 record {int a;}|record {int a;}|string;
type A4 boolean|(record {int a;} & readonly)|int|record {int a;}|()|record {int a;}|decimal;
type A5 record {|int a;|}|int|record {int a;};

type A6 int|int|int;
type A7 int|boolean|int|string;
type A8 boolean|(int & readonly)|int|(float|decimal)|();  // no warning
type A9 record {|int a;|}|int|(decimal|int);

int|string moduleLevelvar = 1;
int|int moduleLevelvar2 = 1;
int|string|int moduleLevelvar3 = 1;
int|(string & readonly)|(int)|boolean moduleLevelvar4 = 1;

record {int a;}|int|(record {int a;}) moduleLevelRecordvar = 1;
record {int a;}|record {int a;}|string moduleLevelRecordvar2 = "string";
boolean|(record {int a;} & readonly)|int|record {int a;}|()|decimal moduleLevelRecordvar3 = 1;  // no warning
record {|int a;|}|(int & readonly)|record {int a;} moduleLevelRecordvar4 = 1;

int|int|int moduleLevelSimpleVar = 1;
int|boolean|int|string moduleLevelSimpleVar2 = 1;
boolean|(int & readonly)|int|(float|decimal)|() moduleLevelSimpleVar3 = 1; // no warning
record {|int a;|}|int|(decimal|int) _ = 1;

public function main() {
    int|string funcLevelvar = 1;
    int|int funcLevelvar2 = 1;
    int|string|int funcLevelvar3 = 1;
    int|(string & readonly)|(int)|boolean funcLevelvar4 = 1;

    record {int a;}|int|record {int a;} funcLevelRecordvar = 1;
    record {int a;}|record {int a;}|string funcLevelRecordvar2 = "str";
    boolean|(record {int a;} & readonly)|int|record {int a;}|()|record {int a;}|decimal funcLevelRecordvar3 = 12d;
    record {|int a;|}|int|record {int a;} funcLevelRecordvar4 = 3;

    int|int|int funcLevelSimpleVar = 1;
    int|boolean|int|string _ = 1;
    boolean|(int & readonly)|int|(float|decimal)|() _ = 1; // no warning
    record {|int a;|}|int|(decimal|int) _ = 1;
}
