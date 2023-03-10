/*
 * Copyright (c) 2014, Francis Galiegue (fgaliegue@gmail.com)
 *
 * This software is dual-licensed under:
 *
 * - the Lesser General Public License (LGPL) version 3.0 or, at your option, any
 *   later version;
 * - the Apache Software License (ASL) version 2.0.
 *
 * The text of this file and of both licenses is available at the root of this
 * project or, if you have the jar distribution, in directory META-INF/, under
 * the names LGPL-3.0.txt and ASL-2.0.txt respectively.
 *
 * Direct link to the sources:
 *
 * - LGPL 3.0: https://www.gnu.org/licenses/lgpl-3.0.txt
 * - ASL 2.0: http://www.apache.org/licenses/LICENSE-2.0.txt
 */

package com.github.fge.jsonpatch.diff;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.jsonpointer.JsonPointerCustom;
import com.github.fge.jsonpatch.AddOperation;
import com.github.fge.jsonpatch.CopyOperation;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.github.fge.jsonpatch.MoveOperation;
import com.github.fge.jsonpatch.RemoveOperation;
import com.github.fge.jsonpatch.ReplaceOperation;

import java.util.Iterator;
import java.util.Map;

final class DiffOperation {
    private final Type type;
    /* An op's "from", if any */
    private final JsonPointerCustom from;
    /* An op's "value", if any */
    private  JsonNode value = null;
    /* An op's "path", if any */
    private final JsonPointerCustom path;

    /* Value displaced by this operation, if any */
    private final JsonNode oldValue;


    static DiffOperation add(final JsonPointerCustom path,
                             final JsonNode value) {
        return new DiffOperation(Type.ADD, null, null, path, value);
    }

    static DiffOperation copy(final JsonPointerCustom from,
                              final JsonPointerCustom path, final JsonNode value) {
        return new DiffOperation(Type.COPY, from, null, path,
                value);
    }

    static DiffOperation move(final JsonPointerCustom from,
                              final JsonNode oldValue, final JsonPointerCustom path,
                              final JsonNode value) {
        return new DiffOperation(Type.MOVE, from, oldValue, path,
                value);
    }

    static DiffOperation remove(final JsonPointerCustom from,
                                final JsonNode oldValue) {
        return new DiffOperation(Type.REMOVE, from, oldValue, null, null);
    }

    static DiffOperation replace(final JsonPointerCustom from,
                                 final JsonNode oldValue, final JsonNode value) {
        return new DiffOperation(Type.REPLACE, from, oldValue, null,
                value);
    }

//    private DiffOperation(final Type type, final JsonPointerCustom from,
//                          final JsonNode oldValue, final JsonPointerCustom path,
//                          final JsonNode value) {
//        this.type = type;
//        this.from = from;
//        this.oldValue = oldValue;
//        this.path = path;
//        this.value = value;
//    }

    public DiffOperation(final Type type, final JsonPointerCustom from,
                         final JsonNode oldValue, final JsonPointerCustom path,
                         final JsonNode value) {
        this.type = type;
        this.from = from;
        this.oldValue = oldValue;
        this.path = path;
        this.value = value;
    }

    Type getType() {
        return type;
    }

    JsonPointerCustom getFrom() {
        return from;
    }

    JsonNode getOldValue() {
        return oldValue;
    }

    JsonPointerCustom getPath() {
        return path;
    }

    JsonNode getValue() {
        return value;
    }

    JsonPatchOperation asJsonPatchOperation() {
        return type.toOperation(this);
    }

    enum Type {
        ADD {
            @Override
            JsonPatchOperation toOperation(final DiffOperation op) {
                return new AddOperation(op.path, op.value);
            }
        },
        COPY {
            @Override
            JsonPatchOperation toOperation(final DiffOperation op) {
                return new CopyOperation(op.from, op.path);
            }
        },
        MOVE {
            @Override
            JsonPatchOperation toOperation(final DiffOperation op) {
                return new MoveOperation(op.from, op.path);
            }
        },
        REMOVE {
            @Override
            JsonPatchOperation toOperation(final DiffOperation op) {
                ObjectNode valueLocator = getValueLocator(op);
                return new RemoveOperation(op.from, valueLocator);
            }
        },
        REPLACE {
            @Override
            JsonPatchOperation toOperation(final DiffOperation op) {
                ObjectNode valueLocator = getValueLocator(op);
                return new ReplaceOperation(op.from, op.oldValue, valueLocator);
            }
        },
        ;

        abstract JsonPatchOperation toOperation(final DiffOperation op);
    }

    private static ObjectNode getValueLocator(final DiffOperation op) {
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object> valueLocatorMap = mapper.convertValue(value, new TypeReference<Map<String, Object>>() {
//        });
//        ObjectNode valueLocator = new ObjectMapper().createObjectNode();
//        Iterator<Map.Entry<String, Object>> iterator = valueLocatorMap.entrySet().iterator();
//        int i = 0;
//        while (i < 3) {
//            Map.Entry<String, Object> entry = iterator.next();
//            String currentKey = entry.getKey();
//            valueLocator.set(currentKey, op.oldValue.get(currentKey));
//            i++;
//        }
        op.oldValue.get("ll");
        return null;
    }
}
