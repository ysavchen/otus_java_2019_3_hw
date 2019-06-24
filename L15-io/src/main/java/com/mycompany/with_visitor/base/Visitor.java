package com.mycompany.with_visitor.base;

import com.mycompany.with_visitor.types.TraversedArray;
import com.mycompany.with_visitor.types.TraversedObject;
import com.mycompany.with_visitor.types.TraversedPrimitive;

public interface Visitor {

    void visit(TraversedArray value);

    void visit(TraversedPrimitive value);

    void visit(TraversedObject value);

}
