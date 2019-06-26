package com.mycompany;

import com.mycompany.base.Visitor;

import javax.json.JsonValue;

public interface JsonVisitor extends Visitor {
    /**
     * @return resulting JsonValue after visiting the object
     */
    JsonValue getJsonValue();
}
