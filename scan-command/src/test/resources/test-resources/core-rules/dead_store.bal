int moduleLevel1 = 3;
int moduleLevel2 = 3;

record {int moduleLevel3;} {moduleLevel3} = {moduleLevel3: 1};
int moduleLevel4 = 1;
[int, string] [moduleLevel5, moduleLevel6] = [1, "2"];
var moduleLevel7 = [1, 2].map((ma4) => ());

function testSimpleLocalVariableDecl() {
    int localVar2 = 1;
    int localVar3 = localVar2; // warning
    localVar2 = 2; // warning
}

function testLocalVarDecl() {
    moduleLevel1 = 3;
    int localVar1; // warning
    int localVar2 = 1;
    int localVar3 = localVar2; // warning
    localVar2 = 2; // warning
    [int, string] [localVar4, localVar5] = [1, "2"]; // warning * 2
    record {int localVar6; string localVar7;} {localVar6, localVar7} = {localVar6: 1, localVar7: "a"}; // warning * 2
    var localVar8 = [1, 2].map((a4) => ()); // warning
}

function testLocalVarDecl2() {
    moduleLevel1 = 3;
    int moduleLevel1; // warning

    moduleLevel2 = 10;

    int localVar1 = 1;
    int localVar2 = 2;
    localVar2 = localVar1;
    _ = dummyFunction(localVar2);

    int localVar3 = 1;
    int localVar4 = 2;
    localVar4 = localVar3;
    localVar4 = 12;
    _ = dummyFunction(localVar4);
    localVar3 = 10; // warning

    int localVar5 = 1;
    int localVar6 = 2;
    localVar6 = localVar5;
    _ = dummyFunction(localVar6);
    localVar5 = 10; // warning
    localVar6 = 10;
    localVar6 = 12;
    localVar6 = 13; // warning
}

function testLocalVarDeclWithMappingBindingpattern() {
    record {int localVar1; int localVar2;}
            {localVar1, localVar2} = {localVar1: 1, localVar2: 5};
    {localVar1, localVar2} = {localVar1: 1, localVar2: localVar1}; // warning
    _ = dummyFunction(localVar2);

    record {int localVar3; int localVar4;}
            {localVar3, localVar4} = {localVar3: 1, localVar4: 5};
    {localVar3, localVar4} = {localVar3: 1, localVar4: localVar3};
    _ = dummyFunction(localVar4);
    {localVar3, localVar4} = {localVar3: 1, localVar4: localVar3}; // warning * 2
}

function testLocalVarDeclWithListBindingpattern() {
   [int, int] [localVar1, localVar2] = [1, 5];
   [localVar1, localVar2] = [1, localVar1]; // warning
   _ = dummyFunction(localVar2);

   [int, int] [localVar3, localVar4] = [1, 5];
   [localVar3, localVar4] = [localVar4, localVar3];
   _ = dummyFunction(localVar4);
   [localVar3, localVar4] = [localVar4, localVar3]; // warning * 2 TODO: Since this swap variables, Is this a code smell?

   [int, int] [localVar5, localVar6] = [1, 5];
   [localVar5, localVar6] = [localVar6, localVar5]; // warning TODO: Since this swap variables, Is this a code smell?
   _ = dummyFunction(localVar6);

   [int, int] [localVar7, localVar8] = [1, 5];
   [localVar7, localVar8] = [4, 5]; // warning * 2
}

function dummyFunction(any a) returns int {
    if (a is int) {
        return 2;
    }
    return 1;
}

type SampleError error<record {|int code; string reason;|}>;

[int, string] [a, b] = [1, ""];
record {int localVar3; int localVar4;} {localVar3, localVar4} = {localVar3: 1, localVar4: 5};

function testErrorBindingpatternVarDecl() {
    SampleError e = error("Transaction Failure", error("Database Error"), code = 20,reason = "deadlock condition");
    var error(code = code, reason = reason) = e;
    error(code = code, reason = reason) = e;
    string a = code.toString() + reason.toString();
    _ = dummyFunction(a);

    var error(code = code2, reason = reason2) = e;
    error(code = code2, reason = reason2) = e; // warning * 2
}
