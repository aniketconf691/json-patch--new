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
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jackson.jsonpointer.JsonPointerCustom;
import com.github.fge.jsonpatch.JsonPatch;
import com.google.common.collect.Lists;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertEquals;

public final class UnchangedTest {
    private static final ObjectMapper MAPPER = JacksonUtils.newMapper();
    private static final TypeReference<Map<JsonPointerCustom, JsonNode>> TYPE_REF
            = new TypeReference<Map<JsonPointerCustom, JsonNode>>() {
    };

    private final JsonNode testData;

    public UnchangedTest()
            throws IOException {
        final String resource = "/jsonpatch/diff/unchanged.json";
        testData = JsonLoader.fromResource(resource);
    }

    @DataProvider
    public Iterator<Object[]> getTestData()
            throws IOException {
        final List<Object[]> list = Lists.newArrayList();

        for (final JsonNode node : testData)
            list.add(new Object[]{node.get("first"), node.get("second"),
                    MAPPER.readValue(node.get("unchanged").traverse(), TYPE_REF)});

        return list.iterator();
    }

    @Test(dataProvider = "getTestData")
    public void computeUnchangedValuesWorks(final JsonNode first,
                                            final JsonNode second, final Map<JsonPointerCustom, JsonNode> expected) {
        final Map<JsonPointerCustom, JsonNode> actual
                = JsonDiff.getUnchangedValues(first, second);

        assertEquals(actual, expected);
    }

    @Test
    public void testCase1() throws IOException {

        final String resource_one = "/jsonpatch/diff/old_diff_custom.json";
        final JsonNode first = JsonLoader.fromResource(resource_one);

        final String resource_two = "/jsonpatch/diff/new_diff_custom.json";
        final JsonNode second = JsonLoader.fromResource(resource_two);

        JsonPointerCustom pointer = JsonPointerCustom.of("Entitlements");
        Map<JsonPointerCustom, Set<String>> map = new HashMap<>();
        Set<String> set = new HashSet<>();
        set.add("Application Key");
        set.add("Entitlement Type");
        set.add("Entitlement Name");
        map.put(pointer, set);

        JsonDiff.asJsonPatch(first, second, map);


    }

}
