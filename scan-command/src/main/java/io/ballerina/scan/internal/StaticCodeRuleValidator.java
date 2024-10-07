package io.ballerina.scan.internal;

import io.ballerina.compiler.api.symbols.IntersectionTypeSymbol;
import io.ballerina.compiler.api.symbols.TypeDescKind;
import io.ballerina.compiler.api.symbols.TypeSymbol;
import io.ballerina.compiler.syntax.tree.IntersectionTypeDescriptorNode;
import io.ballerina.compiler.syntax.tree.Node;
import io.ballerina.compiler.syntax.tree.ParameterizedTypeDescriptorNode;
import io.ballerina.compiler.syntax.tree.ParenthesisedTypeDescriptorNode;
import io.ballerina.compiler.syntax.tree.SyntaxKind;
import io.ballerina.compiler.syntax.tree.TypeDescriptorNode;
import io.ballerina.compiler.syntax.tree.UnionTypeDescriptorNode;

import java.util.HashSet;

public class StaticCodeRuleValidator {
    public static boolean isDuplicateConstituentTypesForUnionType(Node[] typeDescs) {
        return isDuplicateConstituentTypesForUnionType(typeDescs, new HashSet<>());
    }

    public static boolean isDuplicateConstituentTypesForUnionType(Node[] typeDescs,
                                          HashSet<TypeDesNodeSignature> constituentTypeSignatures) {
        for (Node type : typeDescs) {
            if (type instanceof TypeDescriptorNode typeDescriptorNode) {
                if (type instanceof ParenthesisedTypeDescriptorNode parenthesisedTypeDescriptorNode) {
                    boolean result = isDuplicateConstituentTypesForUnionType(
                            new Node[]{parenthesisedTypeDescriptorNode.typedesc()}, constituentTypeSignatures);
                    if (result) {
                        return true;
                    }
                    continue;
                }
                if (typeDescriptorNode instanceof UnionTypeDescriptorNode unionTypeDescriptorNode) {
                    boolean result = isDuplicateConstituentTypesForUnionType(
                            new Node[]{unionTypeDescriptorNode.leftTypeDesc(), unionTypeDescriptorNode.rightTypeDesc()},
                            constituentTypeSignatures
                    );
                    if (result) {
                        return true;
                    }
                    continue;
                }
                boolean result = constituentTypeSignatures.add(
                        new TypeDesNodeSignature(typeDescriptorNode.toString().trim(), typeDescriptorNode.kind()));
                if (!result) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isDuplicateConstituentTypesForIntersectionType(Node[] typeDescs) {
        return isDuplicateConstituentTypesForIntersectionType(typeDescs, new HashSet<>());
    }

    public static boolean isDuplicateConstituentTypesForIntersectionType(Node[] typeDescs,
                                         HashSet<TypeDesNodeSignature> constituentTypeSignatures) {
        for (Node type : typeDescs) {
            if (type instanceof TypeDescriptorNode typeDescriptorNode) {
                if (type instanceof ParenthesisedTypeDescriptorNode parenthesisedTypeDescriptorNode) {
                    boolean result = isDuplicateConstituentTypesForIntersectionType(
                            new Node[]{parenthesisedTypeDescriptorNode.typedesc()}, constituentTypeSignatures);
                    if (result) {
                        return true;
                    }
                    continue;
                }
                if (typeDescriptorNode instanceof IntersectionTypeDescriptorNode intersectionTypeDescriptorNode) {
                    boolean result = isDuplicateConstituentTypesForIntersectionType(
                            new Node[]{intersectionTypeDescriptorNode.leftTypeDesc(),
                                    intersectionTypeDescriptorNode.rightTypeDesc()},
                            constituentTypeSignatures
                    );
                    if (result) {
                        return true;
                    }
                    continue;
                }
                boolean result = constituentTypeSignatures.add(
                        new TypeDesNodeSignature(typeDescriptorNode.toString().trim(), typeDescriptorNode.kind()));
                if (!result) {
                    return true;
                }
            }
        }
        return false;
    }
}

class TypeDesNodeSignature {
    private final String stringValue;
    private final SyntaxKind kind;

    public TypeDesNodeSignature(String signature, SyntaxKind typeDescriptorNode) {
        this.stringValue = signature;
        this.kind = typeDescriptorNode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TypeDesNodeSignature other) {
            return this.stringValue.equals(other.stringValue) && this.kind.equals(other.kind);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return stringValue.hashCode() + kind.hashCode();
    }
}
