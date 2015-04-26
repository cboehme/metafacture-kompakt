/*
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <http://unlicense.org/>
 */
package org.culturegraph.workshops.mfkompakt;

import org.culturegraph.mf.formeta.formatter.FormatterStyle;
import org.culturegraph.mf.stream.converter.FormetaEncoder;
import org.culturegraph.mf.stream.converter.xml.MarcXmlHandler;
import org.culturegraph.mf.stream.converter.xml.XmlDecoder;
import org.culturegraph.mf.stream.pipe.StreamLogger;
import org.culturegraph.mf.stream.sink.ObjectWriter;
import org.culturegraph.mf.stream.source.FileOpener;

/**
 * Main class of the Metafacture Kompakt application.
 *
 * @author Christoph Böhme
 *
 */
public final class MetafactureKompakt {

	public static void main(final String[] args) {
		// Create modules:
		final FileOpener opener = new FileOpener();
		final XmlDecoder decoder = new XmlDecoder();
		final MarcXmlHandler marcHandler = new MarcXmlHandler();
		final StreamLogger logger = new StreamLogger();
		final FormetaEncoder encoder = new FormetaEncoder();
		encoder.setStyle(FormatterStyle.MULTILINE);
		final ObjectWriter<String> writer = new ObjectWriter<String>("output.txt");

		// Connect modules to form a processing pipeline:
		opener
				.setReceiver(decoder)
				.setReceiver(marcHandler)
				.setReceiver(logger)
				.setReceiver(encoder)
				.setReceiver(writer);

		// Feed the input data into the pipeline:
		opener.process("persons_marcxml.xml");
		opener.closeStream();
	}

}
