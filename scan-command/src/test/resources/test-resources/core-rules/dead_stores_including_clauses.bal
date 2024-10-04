//int moduleVar1 = 3;
//error moduleVar2 = error("Transaction Failure", error("Database Error"), code = 20, reason = "deadlock condition");
//int moduleLevel3 = 3;
//int _ = 5;
//
//function testClauses() {
//    do {
//        check dummyFunction2();
//    } on fail error e {
//        moduleVar2 = e;
//    }
//
//    do {
//        check dummyFunction2();
//    } on fail error moduleVar2 {
//        _ = dummyFunction(moduleVar2);
//    }
//
//    do {
//        check dummyFunction2();
//    } on fail error moduleVar2 { // warning
//
//    }
//
//    int _ = let var x = 4 in 2 * x * moduleVar1;
//    int _ = let var moduleVar1 = 4 in 2 * moduleVar1;
//    int _ = let var x = 4 in 2 * moduleVar1; // warning
//
//    int _ = 3;
//}
//
//function testOnLoopsWithListBindingpattern() {
//    foreach [int, int] [a, b] in [[1, 2], [3, 4]] {
//        moduleVar1 = a;
//        _ = dummyFunction(b);
//    }
//
//    foreach [int, int] [a, b] in [[1, 2], [3, 4]] { // warning * 2
//
//    }
//
//    foreach [int, int] [moduleVar1, b] in [[1, 2], [3, 4]] { // warning
//        _ = dummyFunction(b);
//    }
//
//    foreach [int, int] [_, b] in [[1, 2], [3, 4]] {
//        _ = dummyFunction(b);
//    }
//
//    from [int, int] [a, b] in [[1, 2], [3, 4]]
//        let int x = 1
//        do {
//            moduleVar1 = a;
//            _ = dummyFunction(b, x);
//        };
//
//    from [int, int] [a, b] in [[1, 2], [3, 4]]
//        let int moduleVar1 = 1 // warning
//        do {
//            _ = dummyFunction(b, a);
//        };
//
//    from [int, int] [a, b] in [[1, 2], [3, 4]] // warning * 2
//        do {
//        };
//
//    from [int, int] [moduleVar1, b] in [[1, 2], [3, 4]] // warning
//        let int x = 1 // warning
//        do {
//            _ = dummyFunction(b);
//        };
//}
//
//function testOnLoopsWithMappingBindingpattern() {
//    foreach record{int a; int b;} {a, b} in [{a: 1, b: 2}, {a: 3, b: 4}] {
//        moduleVar1 = a;
//        _ = dummyFunction(b);
//    }
//
//    foreach record{int a; int b;} {a, b} in [{a: 1, b: 2}, {a: 3, b: 4}] { // warning * 2
//
//    }
//
//    foreach record{int moduleVar1; int b;} {moduleVar1, b} in [{moduleVar1: 1, b: 2}, {moduleVar1: 3, b: 4}] { // warning
//        _ = dummyFunction(b);
//    }
//
//    foreach record{int b;} {b} in [{a: 1, b: 2}, {a: 3, b: 4}] {
//        _ = dummyFunction(b);
//    }
//
//    from record{int a; int b;} {a, b} in [{a: 1, b: 2}, {a: 3, b: 4}]
//        let int x = 1
//        do {
//            moduleVar1 = a;
//            _ = dummyFunction(b, x);
//        };
//
//    from record{int a; int b;} {a, b} in [{a: 1, b: 2}, {a: 3, b: 4}]
//        let int moduleVar1 = 1 // warning
//        do {
//            _ = dummyFunction(b, a);
//        };
//
//    from record{int a; int b;} {a, b} in [{a: 1, b: 2}, {a: 3, b: 4}] // warning * 2
//        do {
//        };
//
//    from record{int a; int b;} {a, b} in [{a: 1, b: 2}, {a: 3, b: 4}] // warning
//        let int x = 1 // warning
//        do {
//            _ = dummyFunction(b);
//        };
//
//    _ = from record{int a; int b;} {a, b} in [{a: 1, b: 2}, {a: 3, b: 4}]
//        let int x = 1
//        where b <= 3 && x == 4
//        select 1;
//
//    _ = from record {int id; string name;} person in [{id: 1, name: "John"}, {id: 2, name: "Doe"}]
//       join record {int id; string name;} dept in [{id: 1, name: "John"}, {id: 2, name: "Doe"}]
//       on person.id equals dept.id
//       select {
//           id: person.id
//       };
//
//    _ = from record {int id; string name;} person in [{id: 1, name: "John"}, {id: 2, name: "Doe"}]
//       join record {int id; string name;} dept in [{id: 1, name: "John"}, {id: 2, name: "Doe"}] // warning
//       on person.id equals 1
//       select {
//           id: person.id
//       };
//
//    _ = from record {int id; string name;} person in [{id: 1, name: "John"}, {id: 2, name: "Doe"}]
//       join record {int id; string name;} moduleVar1 in [{id: 1, name: "John"}, {id: 2, name: "Doe"}] // warning
//       on person.id equals 1
//       select {
//
//       };
//
//    _ = from record {int id; string name;} person in [{id: 1, name: "John"}, {id: 2, name: "Doe"}]
//       join record {int id; string name;} dept in [{id: 1, name: "John"}, {id: 2, name: "Doe"}]
//       on person.id equals 1
//       select {
//           id: dept.id
//       };
//}
//
//function testOnVarBindingpattern() {
//    foreach var [a, b] in [[1, 2], [3, 4]] {
//        moduleVar1 = a;
//        _ = dummyFunction(b);
//    }
//
//    foreach var [a, b] in [[1, 2], [3, 4]] { // warning * 2
//
//    }
//
//    foreach var [moduleVar1, b] in [[1, 2], [3, 4]] { // warning
//        _ = dummyFunction(b);
//    }
//
//    from var [a, b] in [[1, 2], [3, 4]]
//        let int x = 1
//        do {
//            moduleVar1 = a;
//            _ = dummyFunction(b, x);
//        };
//
//    from var [a, b] in [[1, 2], [3, 4]]
//        let int moduleVar1 = 1 // warning
//        do {
//            _ = dummyFunction(b, a);
//        };
//
//    from var [a, b] in [[1, 2], [3, 4]] // warning * 2
//        do {
//        };
//}
//
//function testMatchClause(any|error e) returns string {
//    match e {
//        var error(localVar9) => {
//            return <string>localVar9 + " is an error";
//        }
//    }
//    match e {
//        var error(localVar9) => { // warning
//            return "" + " is an error";
//        }
//
//        [var a, var b] => {
//            _ = dummyFunction(a, b);
//        }
//
//        [var a, var b, var c] => { // warning
//            _ = dummyFunction(a, b);
//        }
//
//        [var a] => { // warning
//            int d; // warning
//        }
//
//        {a: var a} => {
//            _ = dummyFunction(a);
//        }
//
//        {x: var a, y: var b, ...var rest} => { // warning
//            _ = dummyFunction(a, b);
//        }
//
//        {z: var a, b: var b, ...var rest} => { // warning
//            _ = dummyFunction(a, b, rest);
//        }
//
//        var a if a is decimal => {
//
//        }
//
//        2 => {
//            int a = 5;
//            a = 6; // warning
//        }
//
//        _ => {
//            int a = 5;
//            a = 6; // warning
//        }
//    }
//    return "No match";
//}
//
//function dummyFunction(any|error... a) returns [any|error...] {
//    return a;
//}
//
//function dummyFunction2() returns error? {
//}


function test(int e) {
    foreach int a in [1,2] {

    }

    from int a in [1,2] select a;

    from int a in [1,2]
    do {

    };

    do {
        check dummyFunction2();
    } on fail error moduleVar2 { // warning

    }

    match e {
        var error(localVar9) => {
            return <string>localVar9 + " is an error";
        }
    }
}