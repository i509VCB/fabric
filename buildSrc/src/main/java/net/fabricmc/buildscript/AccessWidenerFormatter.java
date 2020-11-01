package net.fabricmc.buildscript;

import java.io.BufferedReader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.diffplug.spotless.FormatterStep;

import net.fabricmc.accesswidener.AccessWidener;
import net.fabricmc.accesswidener.AccessWidenerReader;

public final class AccessWidenerFormatter {
	public static FormatterStep create() {
		return create(FormatOptions.create());
	}

	public static FormatterStep create(FormatOptions format) {
		Objects.requireNonNull(format, "Format cannot be null");

		return FormatterStep.createNeverUpToDate("access-widener", (rawUnix) -> Objects.requireNonNull(AccessWidenerFormatter.format(format, rawUnix)));
	}

	private static String format(FormatOptions format, String rawUnix) throws Exception {
		// Verify the access widener file is actually usable.
		// We do not format invalid files.
		try (final BufferedReader reader = new BufferedReader(new StringReader(rawUnix))) {
			new AccessWidenerReader(new AccessWidener()).read(reader);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Unable to format access widener at %s since it is invalid");
		}

		final String[] lines = rawUnix.split("\n");
		// Allocate initial capacity of original file
		final List<String> outputLines = new ArrayList<>(lines.length);
		final String indent = createIndent(format);

		for (String line : lines) {
			// Preserve empty lines
			if (line.isEmpty()) {
				outputLines.add(line);
				continue;
			}

			// Preserve header
			if (line.startsWith("accessWidener")) {
				if (!line.startsWith("accessWidener\tv1")) {
					throw new Exception("Cannot format unsupported version access widener");
				}

				final String[] parts = line.split("\\s+");

				// reformat preserving namespace
				outputLines.add(String.format("accessWidener%sv1%s%s", indent, indent, parts[2]));
				continue;
			}

			final int commentStart = line.indexOf("#");

			// If the comment starts on line then continue
			if (commentStart == 0) {
				outputLines.add(line);
				continue;
			}

			String[] parts;
			String comment;

			if (commentStart != -1) {
				parts = line.substring(0, commentStart).split("\\s+");
				comment = line.substring(commentStart);
			} else {
				parts = line.split("\\s+");
				comment = null;
			}

			switch (parts.length) {
			case 3:
				// 3 parts is always a class
				switch (parts[0]) {
				case "accessible":
				case "extendable":
					// <access>[INDENT]class[INDENT]<className>
					String reformatted = String.format("%s%sclass%s%s", parts[0], indent, indent, parts[2]);

					// End of line comments
					if (comment != null) {
						reformatted += indent;
						reformatted += comment;
					}

					outputLines.add(reformatted);

					break;
				default:
					throw new Exception(String.format("Invalid class access modifier \"%s\"", parts[0]));
				}

				break;
			case 5:
				switch (parts[0]) {
				case "mutable": // Always a field
					outputLines.add(reformat(parts, "field", indent, comment));
					break;
				case "extendable": // Always a method
					outputLines.add(reformat(parts, "method", indent, comment));
					break;
				case "accessible": // Ambiguous: needs a further check
					switch (parts[1]) {
					case "field":
					case "method":
						outputLines.add(reformat(parts, parts[1], indent, comment));
						break;
					default:
						throw new Exception(String.format("Invalid type for accessible modifier \"%s\"", parts[1]));
					}

					break;
				default:
					throw new RuntimeException();
				}

				break;
			default:
				throw new Exception("Invalid access widener entry");
			}
		}

		StringBuilder output = null;

		for (String line : outputLines) {
			if (output == null) {
				output = new StringBuilder(line);
				continue;
			}

			output.append("\n");

			if (!line.isEmpty()) {
				output.append(line);
			}
		}

		if (output != null) {
			System.out.println(output.toString());
			return output.toString();
		}

		return null;
	}

	private static String reformat(String[] parts, String type, String indent, String comment) {
		// <access>[INDENT]method[INDENT]<className>[INDENT]<methodName>[INDENT]<methodDesc>
		String reformatted = String.format("%s%s%s%s%s%s%s%s%s", parts[0], indent, type, indent, parts[2], indent, parts[3], indent, parts[4]);

		// End of line comments
		if (comment != null) {
			reformatted += indent;
			reformatted += comment;
		}

		return reformatted;
	}

	private static String createIndent(FormatOptions format) {
		if (format.useTabs) {
			return "\t";
		}

		return " ".repeat(Math.max(0, format.indentSize));
	}

	private AccessWidenerFormatter() {
	}

	// Spotless raises hell if this is not serializable
	public static final class FormatOptions implements Serializable {
		private boolean useTabs = true;
		private int indentSize = 4;

		public static FormatOptions create() {
			return new FormatOptions();
		}

		private FormatOptions() {
		}

		public FormatOptions useTabs() {
			this.useTabs = true;
			return this;
		}

		public FormatOptions useSpaces() {
			return this.useSpaces(4);
		}

		public FormatOptions useSpaces(int size) {
			if (size <= 0) {
				throw new IllegalArgumentException("Space size must be greater than 0");
			}

			this.useTabs = false;
			this.indentSize = size;
			return this;
		}
	}
}
