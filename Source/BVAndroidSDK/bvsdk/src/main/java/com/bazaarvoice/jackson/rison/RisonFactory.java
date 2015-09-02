/*
Portions of this file are copyright by the authors of Jackson under the Apache 2.0 or LGPL license.
*/

package com.bazaarvoice.jackson.rison;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.format.InputAccessor;
import com.fasterxml.jackson.core.format.MatchStrength;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * See the <a href="http://mjtemplate.org/examples/rison.html">Rison spec</a>.
 */
public class RisonFactory extends JsonFactory {

    public final static String FORMAT_NAME_RISON = "Rison";

    final static int DEFAULT_RISON_PARSER_FEATURE_FLAGS = RisonParser.Feature.collectDefaults();

    final static int DEFAULT_RISON_GENERATOR_FEATURE_FLAGS = RisonGenerator.Feature.collectDefaults();

    protected int _risonParserFeatures = DEFAULT_RISON_PARSER_FEATURE_FLAGS;

    protected int _risonGeneratorFeatures = DEFAULT_RISON_GENERATOR_FEATURE_FLAGS;

    public RisonFactory() {
        this(null);
    }

    public RisonFactory(ObjectCodec codec) {
        super(codec);
    }

    @Override
    public RisonFactory copy() {
        _checkInvalidCopy(RisonFactory.class);
        return new RisonFactory(null);
    }

    @Override
    protected Object readResolve() {
        return new RisonFactory(_objectCodec);
    }

    @Override
    public String getFormatName() {
        return FORMAT_NAME_RISON;
    }

    @Override
    public MatchStrength hasFormat(InputAccessor acc) throws IOException {
        return MatchStrength.SOLID_MATCH;  // format detection isn't supported
    }

    @Override
    public Version version() {
        return ModuleVersion.instance.version();
    }

    //
    // Parser configuration
    //

    public final RisonFactory configure(RisonParser.Feature f, boolean state) {
        if (state) {
            enable(f);
        } else {
            disable(f);
        }
        return this;
    }

    public RisonFactory enable(RisonParser.Feature f) {
        _risonParserFeatures |= f.getMask();
        return this;
    }

    public RisonFactory disable(RisonParser.Feature f) {
        _risonParserFeatures &= ~f.getMask();
        return this;
    }

    public boolean isEnabled(RisonParser.Feature f) {
        return (_risonParserFeatures & f.getMask()) != 0;
    }

    //
    // Generator configuration
    //

    public RisonFactory configure(RisonGenerator.Feature f, boolean state) {
        if (state) {
            enable(f);
        } else {
            disable(f);
        }
        return this;
    }

    public RisonFactory enable(RisonGenerator.Feature f) {
        _risonGeneratorFeatures |= f.getMask();
        return this;
    }

    public RisonFactory disable(RisonGenerator.Feature f) {
        _risonGeneratorFeatures &= ~f.getMask();
        return this;
    }

    public final boolean isEnabled(RisonGenerator.Feature f) {
        return (_risonGeneratorFeatures & f.getMask()) != 0;
    }

    //
    // Internal factory methods
    //

    @Override
    protected RisonParser _createParser(InputStream in, IOContext ctxt) throws IOException, JsonParseException {
        return _createJsonParser(in, ctxt);
    }

    @Override
    protected RisonParser _createParser(Reader r, IOContext ctxt) throws IOException, JsonParseException {
        return _createJsonParser(r, ctxt);
    }

    @Override
    protected RisonParser _createParser(byte[] data, int offset, int len, IOContext ctxt) throws IOException, JsonParseException {
        return _createJsonParser(data, offset, len, ctxt);
    }

    @Deprecated
    @Override
    protected RisonParser _createJsonParser(InputStream in, IOContext ctxt) throws IOException, JsonParseException {
        return _createJsonParser(new InputStreamReader(in, "UTF-8"), ctxt);
    }

    @Deprecated
    @Override
    protected RisonParser _createJsonParser(Reader r, IOContext ctxt) throws IOException, JsonParseException {
        return new RisonParser(ctxt, _parserFeatures, _risonParserFeatures, r, _objectCodec,
                _rootCharSymbols.makeChild(isEnabled(Feature.CANONICALIZE_FIELD_NAMES),
                        isEnabled(Feature.INTERN_FIELD_NAMES)));
    }

    @Deprecated
    @Override
    protected RisonParser _createJsonParser(byte[] data, int offset, int len, IOContext ctxt) throws IOException, JsonParseException {
        return _createJsonParser(new ByteArrayInputStream(data, offset, len), ctxt);
    }

    @Override
    protected RisonGenerator _createGenerator(Writer out, IOContext ctxt) throws IOException {
        return _createJsonGenerator(out, ctxt);
    }

    @Deprecated
    @Override
    protected RisonGenerator _createJsonGenerator(Writer out, IOContext ctxt) throws IOException {
        RisonGenerator gen = new RisonGenerator(ctxt, _generatorFeatures, _risonGeneratorFeatures, _objectCodec, out);
        SerializableString rootSep = _rootValueSeparator;
        if (rootSep != DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR) {
            gen.setRootValueSeparator(rootSep);
        }
        return gen;
    }

    @Override
    protected RisonGenerator _createUTF8Generator(OutputStream out, IOContext ctxt) throws IOException {
        return _createUTF8JsonGenerator(out, ctxt);
    }

    @Deprecated
    @Override
    protected RisonGenerator _createUTF8JsonGenerator(OutputStream out, IOContext ctxt) throws IOException {
        return _createJsonGenerator(_createWriter(out, JsonEncoding.UTF8, ctxt), ctxt);
    }
}
