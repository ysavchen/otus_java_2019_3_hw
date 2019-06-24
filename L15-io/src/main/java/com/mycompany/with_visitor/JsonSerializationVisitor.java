package com.mycompany.with_visitor;

import com.mycompany.with_visitor.base.Visitor;
import com.mycompany.with_visitor.types.TraversedArray;
import com.mycompany.with_visitor.types.TraversedObject;
import com.mycompany.with_visitor.types.TraversedPrimitive;

import javax.json.JsonObjectBuilder;

public class JsonSerializationVisitor implements Visitor {

    private final JsonObjectBuilder builder;

    JsonSerializationVisitor(JsonObjectBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void visit(TraversedArray value) {

    }

    @Override
    public void visit(TraversedPrimitive value) {

    }

    @Override
    public void visit(TraversedObject value) {

    }

}
