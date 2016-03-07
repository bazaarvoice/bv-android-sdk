/*
Large portions of this file are copyright by the authors of Jackson under the Apache 2.0 or LGPL license.

The implementation was derived from the Jackson class 'org.codehaus.jackson.impl.WriterBasedGenerator' and
modified for Rison.
*/

package com.bazaarvoice.bvandroidsdk.jackson.rison;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.io.NumberOutput;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * {@link com.fasterxml.jackson.core.JsonGenerator} that outputs Rison content using a {@link java.io.Writer}
 * which handles character encoding.
 */
public final class RisonGenerator
        extends GeneratorBase
{
    /**
     * Enumeration that defines all configurable features for Rison generators.
     */
    public enum Feature {
        /**
         * Whether to assume that the top-level value being encoded is an object,
         * and therefore to omit the containing '(' and ')'.
         *<p>
         * Default setting is false.
         */
        O_RISON(false),

        /**
         * Whether to assume that the top-level value being encoded is an array,
         * and therefore to omit the containing '!(' and ')'.
         *<p>
         * Default setting is false.
         */
        A_RISON(false)
        ;

        final boolean _defaultState;

        final int _mask;

        /**
         * Method that calculates bit set (flags) of all features that
         * are enabled by default.
         */
        public static int collectDefaults()
        {
            int flags = 0;
            for (Feature f : values()) {
                if (f.enabledByDefault()) {
                    flags |= f.getMask();
                }
            }
            return flags;
        }

        private Feature(boolean defaultState) {
            _defaultState = defaultState;
            _mask = (1 << ordinal());
        }

        public boolean enabledByDefault() { return _defaultState; }

        public int getMask() { return _mask; }
    }

    final protected static int SHORT_WRITE = 32;

    /*
   /**********************************************************
   /* Configuration
   /**********************************************************
    */

    final protected IOContext _ioContext;

    final protected Writer _writer;

    final protected int _risonFeatures;

    protected SerializableString _rootValueSeparator
            = DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR;

    /*
    /**********************************************************
    /* Output buffering
    /**********************************************************
     */

    /**
     * Intermediate buffer in which contents are buffered before
     * being written using {@link #_writer}.
     */
    protected char[] _outputBuffer;

    /**
     * Pointer to the first buffered character to output
     */
    protected int _outputHead = 0;

    /**
     * Pointer to the position right beyond the last character to output
     * (end marker; may point to position right beyond the end of the buffer)
     */
    protected int _outputTail = 0;

    /**
     * End marker of the output buffer; one past the last valid position
     * within the buffer.
     */
    protected int _outputEnd;

    /*
    /**********************************************************
    /* Life-cycle
    /**********************************************************
     */

    public RisonGenerator(IOContext ctxt, int jsonFeatures, int risonFeatures, ObjectCodec codec,
                          Writer w)
    {
        super(jsonFeatures, codec);
        _ioContext = ctxt;
        _writer = w;
        _risonFeatures = risonFeatures;
        _outputBuffer = ctxt.allocConcatBuffer();
        _outputEnd = _outputBuffer.length;
    }

    @Override
    public Version version() {
        return ModuleVersion.instance.version();
    }

    /*
   /**********************************************************
   /* Overridden configuration methods
   /**********************************************************
    */

    @Override
    public Object getOutputTarget() {
        return _writer;
    }

    private boolean isRisonEnabled(Feature feature) {
        return (_risonFeatures & feature.getMask()) != 0;
    }

    @Override
    public JsonGenerator setRootValueSeparator(SerializableString sep) {
        _rootValueSeparator = sep;
        return this;
    }

    /*
   /**********************************************************
   /* Overridden methods
   /**********************************************************
    */

    @Override
    public void writeFieldName(String name)  throws IOException, JsonGenerationException
    {
        int status = _writeContext.writeFieldName(name);
        if (status == JsonWriteContext.STATUS_EXPECT_VALUE) {
            _reportError("Can not write a field name, expecting a value");
        }
        _writeFieldName(name, (status == JsonWriteContext.STATUS_OK_AFTER_COMMA));
    }

    @Override
    public void writeFieldName(SerializableString name)
            throws IOException, JsonGenerationException
    {
        // Object is a value, need to verify it's allowed
        int status = _writeContext.writeFieldName(name.getValue());
        if (status == JsonWriteContext.STATUS_EXPECT_VALUE) {
            _reportError("Can not write a field name, expecting a value");
        }
        _writeFieldName(name, (status == JsonWriteContext.STATUS_OK_AFTER_COMMA));
    }

    /*
   /**********************************************************
   /* Output method implementations, structural
   /**********************************************************
    */

    private boolean omitArrayWrappers(JsonWriteContext writeContext) {
        return writeContext.inRoot() && isRisonEnabled(Feature.A_RISON);
    }

    private boolean omitObjectWrappers(JsonWriteContext writeContext) {
        return writeContext.inRoot() && isRisonEnabled(Feature.O_RISON);
    }

    @Override
    public void writeStartArray() throws IOException, JsonGenerationException
    {
        _verifyValueWrite("start an array");
        if (!omitArrayWrappers(_writeContext)) {
            if ((_outputTail + 1) >= _outputEnd) {
                _flushBuffer();
            }
            _outputBuffer[_outputTail++] = '!';
            _outputBuffer[_outputTail++] = '(';
        }
        _writeContext = _writeContext.createChildArrayContext();
    }

    @Override
    public void writeEndArray() throws IOException, JsonGenerationException
    {
        if (!_writeContext.inArray()) {
            _reportError("Current context not an ARRAY but "+_writeContext.getTypeDesc());
        }
        _writeContext = _writeContext.getParent();
        if (!omitArrayWrappers(_writeContext)) {
            if (_outputTail >= _outputEnd) {
                _flushBuffer();
            }
            _outputBuffer[_outputTail++] = ')';
        }
    }

    @Override
    public void writeStartObject() throws IOException, JsonGenerationException
    {
        _verifyValueWrite("start an object");
        if (!(omitObjectWrappers(_writeContext))) {
            if (_outputTail >= _outputEnd) {
                _flushBuffer();
            }
            _outputBuffer[_outputTail++] = '(';
        }
        _writeContext = _writeContext.createChildObjectContext();
    }

    @Override
    public void writeEndObject() throws IOException, JsonGenerationException
    {
        if (!_writeContext.inObject()) {
            _reportError("Current context not an object but "+_writeContext.getTypeDesc());
        }
        _writeContext = _writeContext.getParent();
        if (!omitObjectWrappers(_writeContext)) {
            if (_outputTail >= _outputEnd) {
                _flushBuffer();
            }
            _outputBuffer[_outputTail++] = ')';
        }
    }

    protected void _writeFieldName(String name, boolean commaBefore)
            throws IOException, JsonGenerationException
    {
        // for fast+std case, need to output up to 2 chars, comma, quote
        if ((_outputTail + 1) >= _outputEnd) {
            _flushBuffer();
        }
        if (commaBefore) {
            _outputBuffer[_outputTail++] = ',';
        }

        if (IdentifierUtils.isIdStrict(name)) {
            _writeRaw(name);
            return;
        }

        // we know there's room for at least one more char
        _outputBuffer[_outputTail++] = '\'';
        // The beef:
        _writeString(name);
        // and closing quotes; need room for one more char:
        if (_outputTail >= _outputEnd) {
            _flushBuffer();
        }
        _outputBuffer[_outputTail++] = '\'';
    }

    public void _writeFieldName(SerializableString name, boolean commaBefore)
            throws IOException, JsonGenerationException
    {
        // Can't take advantage of SerializableString.asQuotedChars() because JSON and Rison
        // have different rules for quoting characters within strings.
        _writeFieldName(name.getValue(), commaBefore);
    }

    /*
    /**********************************************************
    /* Output method implementations, textual
    /**********************************************************
     */

    @Override
    public void writeString(String text)
            throws IOException, JsonGenerationException
    {
        _verifyValueWrite("write text value");
        if (text == null) {
            _writeNull();
            return;
        }
        if (_outputTail >= _outputEnd) {
            _flushBuffer();
        }

        if (IdentifierUtils.isIdStrict(text)) {
            _writeRaw(text);
            return;
        }

        _outputBuffer[_outputTail++] = '\'';
        _writeString(text);
        // And finally, closing quotes
        if (_outputTail >= _outputEnd) {
            _flushBuffer();
        }
        _outputBuffer[_outputTail++] = '\'';
    }

    @Override
    public void writeString(char[] text, int offset, int len)
            throws IOException, JsonGenerationException
    {
        _verifyValueWrite("write text value");

        if (IdentifierUtils.isIdStrict(text, offset, len)) {
            _writeRaw(text, offset, len);
            return;
        }

        if (_outputTail >= _outputEnd) {
            _flushBuffer();
        }
        _outputBuffer[_outputTail++] = '\'';
        _writeString(text, offset, len);
        // And finally, closing quotes
        if (_outputTail >= _outputEnd) {
            _flushBuffer();
        }
        _outputBuffer[_outputTail++] = '\'';
    }

    @Override
    public void writeString(SerializableString sstr)
            throws IOException, JsonGenerationException
    {
        // Can't take advantage of SerializableString.asQuotedChars() because JSON and Rison
        // have different rules for quoting characters within strings.
        writeString(sstr.getValue());
    }

    @Override
    public void writeRawUTF8String(byte[] text, int offset, int length)
            throws IOException, JsonGenerationException
    {
        // could add support for buffering if we really want it...
        _reportUnsupportedOperation();
    }

    @Override
    public void writeUTF8String(byte[] text, int offset, int length)
            throws IOException, JsonGenerationException
    {
        // could add support for buffering if we really want it...
        _reportUnsupportedOperation();
    }

    /*
   /**********************************************************
   /* Output method implementations, unprocessed ("raw")
   /**********************************************************
    */

    @Override
    public void writeRaw(String text)
            throws IOException, JsonGenerationException
    {
        _reportUnsupportedOperation();
    }

    @Override
    public void writeRaw(String text, int start, int len)
            throws IOException, JsonGenerationException
    {
        _reportUnsupportedOperation();
    }

    @Override
    public void writeRaw(char[] text, int offset, int len)
            throws IOException, JsonGenerationException
    {
        _reportUnsupportedOperation();
    }

    @Override
    public void writeRaw(char c)
            throws IOException, JsonGenerationException
    {
        _reportUnsupportedOperation();
    }

    /*
    /**********************************************************
    /* Output method implementations, base64-encoded binary
    /**********************************************************
     */

    @Override
    public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len)
            throws IOException, JsonGenerationException
    {
        _verifyValueWrite("write binary value");
        // Starting quotes
        if (_outputTail >= _outputEnd) {
            _flushBuffer();
        }
        _outputBuffer[_outputTail++] = '\'';
        _writeBinary(b64variant, data, offset, offset+len);
        // and closing quotes
        if (_outputTail >= _outputEnd) {
            _flushBuffer();
        }
        _outputBuffer[_outputTail++] = '\'';
    }

    /*
    /**********************************************************
    /* Output method implementations, primitive
    /**********************************************************
     */

    @Override
    public void writeNumber(int i)
            throws IOException, JsonGenerationException
    {
        _verifyValueWrite("write number");
        if (_cfgNumbersAsStrings) {
            _writeQuotedInt(i);
            return;
        }
        // up to 10 digits and possible minus sign
        if ((_outputTail + 11) >= _outputEnd) {
            _flushBuffer();
        }
        _outputTail = NumberOutput.outputInt(i, _outputBuffer, _outputTail);
    }

    private void _writeQuotedInt(int i) throws IOException {
        if ((_outputTail + 13) >= _outputEnd) {
            _flushBuffer();
        }
        _outputBuffer[_outputTail++] = '\'';
        _outputTail = NumberOutput.outputInt(i, _outputBuffer, _outputTail);
        _outputBuffer[_outputTail++] = '\'';
    }

    @Override
    public void writeNumber(long l)
            throws IOException, JsonGenerationException
    {
        _verifyValueWrite("write number");
        if (_cfgNumbersAsStrings) {
            _writeQuotedLong(l);
            return;
        }
        if ((_outputTail + 21) >= _outputEnd) {
            // up to 20 digits, minus sign
            _flushBuffer();
        }
        _outputTail = NumberOutput.outputLong(l, _outputBuffer, _outputTail);
    }

    private void _writeQuotedLong(long l) throws IOException {
        if ((_outputTail + 23) >= _outputEnd) {
            _flushBuffer();
        }
        _outputBuffer[_outputTail++] = '\'';
        _outputTail = NumberOutput.outputLong(l, _outputBuffer, _outputTail);
        _outputBuffer[_outputTail++] = '\'';
    }

    @Override
    public void writeNumber(BigInteger value)
            throws IOException, JsonGenerationException
    {
        _verifyValueWrite("write number");
        if (value == null) {
            _writeNull();
        } else if (_cfgNumbersAsStrings) {
            _writeQuotedRaw(value);
        } else {
            _writeRaw(value.toString());
        }
    }


    @Override
    public void writeNumber(double d)
            throws IOException, JsonGenerationException
    {
        if (_cfgNumbersAsStrings ||
                // [JACKSON-139]
                (((Double.isNaN(d) || Double.isInfinite(d))
                        && isEnabled(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS)))) {
            writeString(Double.toString(d));
            return;
        }
        // What is the max length for doubles? 40 chars?
        _verifyValueWrite("write number");
        _writeRaw(formatDouble(d));
    }

    @Override
    public void writeNumber(float f)
            throws IOException, JsonGenerationException
    {
        if (_cfgNumbersAsStrings ||
                // [JACKSON-139]
                (((Float.isNaN(f) || Float.isInfinite(f))
                        && isEnabled(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS)))) {
            writeString(Float.toString(f));
            return;
        }
        // What is the max length for floats?
        _verifyValueWrite("write number");
        _writeRaw(formatFloat(f));
    }

    @Override
    public void writeNumber(BigDecimal value)
            throws IOException, JsonGenerationException
    {
        // Don't really know max length for big decimal, no point checking
        _verifyValueWrite("write number");
        if (value == null) {
            _writeNull();
        } else if (_cfgNumbersAsStrings) {
            _writeQuotedRaw(formatDecimal(value));
        } else {
            _writeRaw(formatDecimal(value));
        }
    }

    @Override
    public void writeNumber(String encodedValue)
            throws IOException, JsonGenerationException
    {
        _verifyValueWrite("write number");
        if (_cfgNumbersAsStrings) {
            _writeQuotedRaw(formatDecimal(encodedValue));
        } else {
            _writeRaw(formatDecimal(encodedValue));
        }
    }

    private void _writeQuotedRaw(Object value) throws IOException
    {
        if (_outputTail >= _outputEnd) {
            _flushBuffer();
        }
        _outputBuffer[_outputTail++] = '\'';
        _writeRaw(value.toString());
        if (_outputTail >= _outputEnd) {
            _flushBuffer();
        }
        _outputBuffer[_outputTail++] = '\'';
    }

    @Override
    public void writeBoolean(boolean state)
            throws IOException, JsonGenerationException
    {
        _verifyValueWrite("write boolean value");
        if ((_outputTail + 1) >= _outputEnd) {
            _flushBuffer();
        }
        _outputBuffer[_outputTail++] = '!';
        _outputBuffer[_outputTail++] = state ? 't' : 'f';
    }

    @Override
    public void writeNull()
            throws IOException, JsonGenerationException
    {
        _verifyValueWrite("write null value");
        _writeNull();
    }

    /*
    /**********************************************************
    /* Implementations for other methods
    /**********************************************************
     */

    @Override
    protected final void _verifyValueWrite(String typeMsg)
            throws IOException, JsonGenerationException
    {
        int status = _writeContext.writeValue();
        if (status == JsonWriteContext.STATUS_EXPECT_NAME) {
            _reportError("Can not "+typeMsg+", expecting field name");
        }
        char c;
        switch (status) {
            case JsonWriteContext.STATUS_OK_AFTER_COMMA:
                c = ',';
                break;
            case JsonWriteContext.STATUS_OK_AFTER_COLON:
                c = ':';
                break;
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                if (_rootValueSeparator != null) {
                    _writeRaw(_rootValueSeparator.getValue());
                }
                return;
            case JsonWriteContext.STATUS_OK_AS_IS:
            default:
                return;
        }
        if (_outputTail >= _outputEnd) {
            _flushBuffer();
        }
        _outputBuffer[_outputTail++] = c;
    }

    /*
    /**********************************************************
    /* Low-level output handling
    /**********************************************************
     */

    @Override
    public void flush()
            throws IOException
    {
        _flushBuffer();
        if (_writer != null) {
            if (isEnabled(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM)) {
                _writer.flush();
            }
        }
    }

    @Override
    public void close()
            throws IOException
    {
        super.close();

        /* 05-Dec-2008, tatu: To add [JACKSON-27], need to close open
         *   scopes.
         */
        // First: let's see that we still have buffers...
        if (_outputBuffer != null
                && isEnabled(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT)) {
            while (true) {
                JsonStreamContext ctxt = getOutputContext();
                if (ctxt.inArray()) {
                    writeEndArray();
                } else if (ctxt.inObject()) {
                    writeEndObject();
                } else {
                    break;
                }
            }
        }
        _flushBuffer();

        /* 25-Nov-2008, tatus: As per [JACKSON-16] we are not to call close()
         *   on the underlying Reader, unless we "own" it, or auto-closing
         *   feature is enabled.
         *   One downside: when using UTF8Writer, underlying buffer(s)
         *   may not be properly recycled if we don't close the writer.
         */
        if (_writer != null) {
            if (_ioContext.isResourceManaged() || isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET)) {
                _writer.close();
            } else  if (isEnabled(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM)) {
                // If we can't close it, we should at least flush
                _writer.flush();
            }
        }
        // Internal buffer(s) generator has can now be NEXUS_USERd as well
        _NEXUS_USERBuffers();
    }

    @Override
    protected void _NEXUS_USERBuffers()
    {
        char[] buf = _outputBuffer;
        if (buf != null) {
            _outputBuffer = null;
            _ioContext.NEXUS_USERConcatBuffer(buf);
        }
    }

    /*
    /**********************************************************
    /* Internal methods, low-level writing; text, default
    /**********************************************************
     */

    private void _writeRaw(String text)
            throws IOException, JsonGenerationException
    {
        // Nothing to check, can just output as is
        int len = text.length();
        int room = _outputEnd - _outputTail;

        if (room == 0) {
            _flushBuffer();
            room = _outputEnd - _outputTail;
        }
        // But would it nicely fit in? If yes, it's easy
        if (room >= len) {
            text.getChars(0, len, _outputBuffer, _outputTail);
            _outputTail += len;
        } else {
            _writeRawLong(text);
        }
    }

    private void _writeRaw(char[] text, int offset, int len)
            throws IOException, JsonGenerationException
    {
        // Only worth buffering if it's a short write?
        if (len < SHORT_WRITE) {
            int room = _outputEnd - _outputTail;
            if (len > room) {
                _flushBuffer();
            }
            System.arraycopy(text, offset, _outputBuffer, _outputTail, len);
            _outputTail += len;
            return;
        }
        // Otherwise, better just pass through:
        _flushBuffer();
        _writer.write(text, offset, len);
    }

    private void _writeRawLong(String text)
            throws IOException, JsonGenerationException
    {
        int room = _outputEnd - _outputTail;
        // If not, need to do it by looping
        text.getChars(0, room, _outputBuffer, _outputTail);
        _outputTail += room;
        _flushBuffer();
        int offset = room;
        int len = text.length() - room;

        while (len > _outputEnd) {
            int amount = _outputEnd;
            text.getChars(offset, offset+amount, _outputBuffer, 0);
            _outputHead = 0;
            _outputTail = amount;
            _flushBuffer();
            offset += amount;
            len -= amount;
        }
        // And last piece (at most length of buffer)
        text.getChars(offset, offset+len, _outputBuffer, 0);
        _outputHead = 0;
        _outputTail = len;
    }

    private void _writeString(String text)
            throws IOException, JsonGenerationException
    {
        /* One check first: if String won't fit in the buffer, let's
         * segment writes. No point in extending buffer to huge sizes
         * (like if someone wants to include multi-megabyte base64
         * encoded stuff or such)
         */
        final int len = text.length();
        if (len > _outputEnd) { // Let's reserve space for entity at begin/end
            _writeLongString(text);
            return;
        }

        // Ok: we know String will fit in buffer ok
        // But do we need to flush first?
        if ((_outputTail + len) > _outputEnd) {
            _flushBuffer();
        }
        text.getChars(0, len, _outputBuffer, _outputTail);

        _writeString2(len);
    }

    private void _writeString2(final int len)
            throws IOException, JsonGenerationException
    {
        // And then we'll need to verify need for escaping etc:
        int end = _outputTail + len;

        output_loop:
        while (_outputTail < end) {
            // Fast loop for chars not needing escaping
            escape_loop:
            while (true) {
                char c = _outputBuffer[_outputTail];
                if (c == '!' || c == '\'') {
                    break escape_loop;
                }
                if (++_outputTail >= end) {
                    break output_loop;
                }
            }

            // Ok, bumped into something that needs escaping.
            /* First things first: need to flush the buffer.
             * Inlined, as we don't want to lose tail pointer
             */
            int flushLen = (_outputTail - _outputHead);
            if (flushLen > 0) {
                _writer.write(_outputBuffer, _outputHead, flushLen);
            }
            /* In any case, tail will be the new start, so hopefully
             * we have room now.  prepend the '!' to escape the character (Rison)
             */
            _outputHead = _prependOrWrite(_outputBuffer, _outputTail, '!');
            _outputTail++;  // advance past the char-to-be-escaped
        }
    }

    /**
     * Method called to write "long strings", strings whose length exceeds
     * output buffer length.
     */
    private void _writeLongString(String text)
            throws IOException, JsonGenerationException
    {
        // First things first: let's flush the buffer to get some more room
        _flushBuffer();

        // Then we can write
        final int textLen = text.length();
        int offset = 0;
        do {
            int max = _outputEnd;
            int segmentLen = ((offset + max) > textLen)
                    ? (textLen - offset) : max;
            text.getChars(offset, offset+segmentLen, _outputBuffer, 0);
            _writeSegment(segmentLen);
            offset += segmentLen;
        } while (offset < textLen);
    }

    /**
     * Method called to output textual context which has been copied
     * to the output buffer prior to call. If any escaping is needed,
     * it will also be handled by the method.
     *<p>
     * Note: when called, textual content to write is within output
     * buffer, right after buffered content (if any). That's why only
     * length of that text is passed, as buffer and offset are implied.
     */
    private void _writeSegment(int end)
            throws IOException, JsonGenerationException
    {
        int ptr = 0;
        int start = ptr;

        output_loop:
        while (ptr < end) {
            // Fast loop for chars not needing escaping
            char c;
            while (true) {
                c = _outputBuffer[ptr];
                if (c == '!' || c == '\'') {
                    break;
                }
                if (++ptr >= end) {
                    break;
                }
            }

            // Ok, bumped into something that needs escaping.
            /* First things first: need to flush the buffer.
             * Inlined, as we don't want to lose tail pointer
             */
            int flushLen = (ptr - start);
            if (flushLen > 0) {
                _writer.write(_outputBuffer, start, flushLen);
                if (ptr >= end) {
                    break output_loop;
                }
            }
            // So; either try to prepend (most likely), or write directly:
            start = _prependOrWrite(_outputBuffer, ptr, '!');
            ptr++;  // advance past the char-to-be-escaped

            // Flush the last 1 or 2 characters if this is the last time though the loop
            if (ptr == end) {
                _writer.write(_outputBuffer, start, ptr - start);
                break output_loop;
            }
        }
    }

    /**
     * This method called when the string content is already in
     * a char buffer, and need not be copied for processing.
     */
    private void _writeString(char[] text, int offset, int len)
            throws IOException, JsonGenerationException
    {
        /* Let's just find longest spans of non-escapable
        * content, and for each see if it makes sense
        * to copy them, or write through
        */
        len += offset; // -> len marks the end from now on
        while (offset < len) {
            int start = offset;

            while (true) {
                char c = text[offset];
                if (c == '!' || c == '\'') {
                    break;
                }
                if (++offset >= len) {
                    break;
                }
            }

            // Short span? Better just copy it to buffer first:
            int newAmount = offset - start;
            if (newAmount < SHORT_WRITE) {
                // Note: let's reserve room for escaped char (up to 6 chars)
                if ((_outputTail + newAmount) > _outputEnd) {
                    _flushBuffer();
                }
                if (newAmount > 0) {
                    System.arraycopy(text, start, _outputBuffer, _outputTail, newAmount);
                    _outputTail += newAmount;
                }
            } else { // Nope: better just write through
                _flushBuffer();
                _writer.write(text, start, newAmount);
            }
            // Was this the end?
            if (offset >= len) { // yup
                break;
            }
            // Nope, need to escape the char.
            char c = text[offset++];
            _appendCharacterEscape('!', c);
        }
    }

    /*
   /**********************************************************
   /* Internal methods, low-level writing; binary
   /**********************************************************
    */

    protected void _writeBinary(Base64Variant b64variant, byte[] input, int inputPtr, final int inputEnd)
            throws IOException, JsonGenerationException
    {
        // Encoding is by chunks of 3 input, 4 output chars, so:
        int safeInputEnd = inputEnd - 3;
        // Let's also reserve room for possible lf char each round
        int safeOutputEnd = _outputEnd - 6;
        int chunksBeforeLF = b64variant.getMaxLineLength() >> 2;

        // Ok, first we loop through all full triplets of data:
        while (inputPtr <= safeInputEnd) {
            if (_outputTail > safeOutputEnd) { // need to flush
                _flushBuffer();
            }
            // First, mash 3 bytes into lsb of 32-bit int
            int b24 = ((int) input[inputPtr++]) << 8;
            b24 |= ((int) input[inputPtr++]) & 0xFF;
            b24 = (b24 << 8) | (((int) input[inputPtr++]) & 0xFF);
            _outputTail = b64variant.encodeBase64Chunk(b24, _outputBuffer, _outputTail);
            if (--chunksBeforeLF <= 0) {
                // note: RISON does not escape newlines
                _outputBuffer[_outputTail++] = '\n';
                chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
            }
        }

        // And then we may have 1 or 2 leftover bytes to encode
        int inputLeft = inputEnd - inputPtr; // 0, 1 or 2
        if (inputLeft > 0) { // yes, but do we have room for output?
            if (_outputTail > safeOutputEnd) { // don't really need 6 bytes but...
                _flushBuffer();
            }
            int b24 = ((int) input[inputPtr++]) << 16;
            if (inputLeft == 2) {
                b24 |= (((int) input[inputPtr]) & 0xFF) << 8;
            }
            _outputTail = b64variant.encodeBase64Partial(b24, inputLeft, _outputBuffer, _outputTail);
        }
    }

    /*
    /**********************************************************
    /* Internal methods, low-level writing, other
    /**********************************************************
     */

    private void _writeNull() throws IOException
    {
        if ((_outputTail + 1) >= _outputEnd) {
            _flushBuffer();
        }
        _outputBuffer[_outputTail++] = '!';
        _outputBuffer[_outputTail++] = 'n';
    }

    /*
   /**********************************************************
   /* Internal methods, low-level writing, escapes
   /**********************************************************
    */

    /**
     * Method called to try to either prepend character escape at front of
     * given buffer; or if not possible, to write it out directly.
     *
     * @return Pointer to start of prepended entity (if prepended); or 'ptr'
     *   if not.
     */
    private int _prependOrWrite(char[] buffer, int ptr, char esc)
            throws IOException, JsonGenerationException
    {
        if (ptr > 0) { // fits, just prepend
            buffer[--ptr] = esc;
        } else { // won't fit, write
            _writer.write(esc);
        }
        return ptr;
    }

    /**
     * Method called to append escape sequence for given character, at the
     * end of standard output buffer; or if not possible, write out directly.
     */
    private void _appendCharacterEscape(char esc, char ch)
            throws IOException, JsonGenerationException
    {
        if ((_outputTail + 1) >= _outputEnd) {
            _flushBuffer();
        }
        _outputBuffer[_outputTail++] = esc;
        _outputBuffer[_outputTail++] = ch;
    }

    protected final void _flushBuffer() throws IOException
    {
        int len = _outputTail - _outputHead;
        if (len > 0) {
            int offset = _outputHead;
            _outputTail = _outputHead = 0;
            _writer.write(_outputBuffer, offset, len);
        }
    }

    private String formatDouble(double d) {
        return Double.toString(d).toLowerCase();
    }

    private String formatFloat(float f) {
        return Float.toString(f).toLowerCase();
    }

    private String formatDecimal(Object number) {
        return number.toString().toLowerCase();
    }
}
