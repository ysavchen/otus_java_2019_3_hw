package com.mycompany.base;

import com.mycompany.types.*;

public interface Visitor {

    void visit(TraversedArray value);

    void visit(TraversedPrimitive value);

    void visit(TraversedObject value);

    void visit(TraversedString value);

    void visit(TraversedPrimitiveWrapper value);
}
