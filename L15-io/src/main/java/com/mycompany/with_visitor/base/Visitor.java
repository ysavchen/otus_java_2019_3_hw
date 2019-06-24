package com.mycompany.with_visitor.base;

import com.mycompany.with_visitor.types.*;

public interface Visitor {

    void visit(TraversedArray value);

    void visit(TraversedPrimitive value);

    void visit(TraversedObject value);

    void visit(TraversedString value);

    void visit(TraversedPrimitiveWrapper value);
}
