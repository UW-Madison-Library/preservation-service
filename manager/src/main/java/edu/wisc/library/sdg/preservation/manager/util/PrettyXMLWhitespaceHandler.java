/*
 * Copyright 2011, 2012 Odysseus Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.wisc.library.sdg.preservation.manager.util;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Package-private helper used by {@link PrettyXMLStreamWriter}
 * to handle pretty printing state and insert indentation and newline characters events.
 *
 * Copied from: https://github.com/beckchr/staxon/blob/master/core/src/main/java/de/odysseus/staxon/xml/util/PrettyXMLWhitespaceHandler.java
 *
 * Modified by UW-Madison developers on 9/17/2020. The package has been changed and the event writer and related code has been removed.
 */
class PrettyXMLWhitespaceHandler {

    /**
     * Whitespace writer.
     */
    private static abstract class WhitespaceWriter {
        abstract void add(String event) throws XMLStreamException;
    }

    private static final int MAX_DEPTH = 64;

    private final String newline;
    private final String[] indent;
    private final WhitespaceWriter writer;

    private int depth = 0;
    private boolean text = false;
    private boolean leaf = false;

    /**
     * Create whitespace handler for an {@link XMLStreamWriter}.
     *
     * @param writer
     *            stream writer
     * @param indentation
     *            line indentation
     * @param newline
     *            line separator
     */
    PrettyXMLWhitespaceHandler(final XMLStreamWriter writer, String indentation, String newline) {
        this(indentation, newline, new WhitespaceWriter() {
            @Override
            public void add(String event) throws XMLStreamException {
                writer.writeCharacters(event);
            }
        });
    }

    private PrettyXMLWhitespaceHandler(String indentation, String newline, WhitespaceWriter writer) {
        this.newline = newline;
        this.indent = new String[MAX_DEPTH];
        this.writer = writer;

        /*
         * initialize indentation whitespace events
         */
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < indent.length; i++) {
            indent[i] = builder.toString();
            builder.append(indentation);
        }
    }

    private void preStructure() throws XMLStreamException {
        if (text) {
            text = false;
        } else if (depth > 0) {
            writer.add(newline);
            writer.add(indent[depth]);
        }
    }

    private void postComment_PI() throws XMLStreamException {
        leaf = false;
        if (depth == 0) {
            writer.add(newline);
        }
    }

    void preStartDocument() throws XMLStreamException {
        preStructure();
    }

    void postStartDocument() throws XMLStreamException {
        postComment_PI();
    }

    void preComment() throws XMLStreamException {
        preStructure();
    }

    void postComment() throws XMLStreamException {
        postComment_PI();
    }

    void preProcessingInstruction() throws XMLStreamException {
        preStructure();
    }

    void postProcessingInstruction() throws XMLStreamException {
        postComment_PI();
    }

    void preStartElement() throws XMLStreamException {
        preStructure();
    }

    void postStartElement() throws XMLStreamException {
        depth++;
        leaf = true;
    }

    void preEndElement() throws XMLStreamException {
        depth--;
        if (text) {
            text = false;
        } else if (!leaf) {
            writer.add(newline);
            if (depth > 0) {
                writer.add(indent[depth]);
            }
        }
    }

    void postEndElement() throws XMLStreamException {
        leaf = false;
        if (depth == 0) {
            writer.add(newline);
        }
    }

    void preEmptyElement() throws XMLStreamException {
        preStructure();
    }

    void postEmptyElement() throws XMLStreamException {
        leaf = false;
    }

    void preCharacters() {
        text = true;
    }

    void postCharacters() {
    }

    void writeNewline() throws XMLStreamException {
        writer.add(newline);
    }

}
