package io.ballerina.scan.internal;

import io.ballerina.compiler.api.symbols.IntersectionTypeSymbol;
import io.ballerina.compiler.api.symbols.TypeDescKind;
import io.ballerina.compiler.api.symbols.TypeSymbol;
import io.ballerina.compiler.api.symbols.UnionTypeSymbol;

import java.util.HashSet;

public class StaticCodeRuleValidator {
    public static boolean isDuplicateConstituentTypesForUnionType(TypeSymbol typeSymbol) {
        if (typeSymbol.typeKind() != TypeDescKind.UNION) {
            return false;
        }
        UnionTypeSymbol unionTypeSymbol = (UnionTypeSymbol) typeSymbol;
        HashSet<String> constituentTypeSignatures = new HashSet<>();
        for (TypeSymbol constituentType : unionTypeSymbol.memberTypeDescriptors()) {
            if (!constituentTypeSignatures.add(constituentType.signature())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDuplicateConstituentTypesForIntersectionType(TypeSymbol typeSymbol) {
        if (typeSymbol.typeKind() != TypeDescKind.INTERSECTION) {
            return false;
        }
        IntersectionTypeSymbol intersectionTypeSymbol = (IntersectionTypeSymbol) typeSymbol;
        HashSet<String> constituentTypeSignatures = new HashSet<>();
        for (TypeSymbol constituentType : intersectionTypeSymbol.memberTypeDescriptors()) {
            if (!constituentTypeSignatures.add(constituentType.signature())) {
                return true;
            }
        }
        return false;
    }
}
