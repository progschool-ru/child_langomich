package org.omich.tool.constants;

import javax.annotation.Nonnull;

/**
 * Строковые константы на все случаи жизни.
 */
final public class Strings
{
	public static final @Nonnull String BOOL_FALSE   = "false"; //$NON-NLS-1$
	public static final @Nonnull String BOOL_TRUE    = "true"; //$NON-NLS-1$

	// Константы для случаев частого использования строки, состоящей из одного символа.
	public static final @Nonnull String CH_BRACE_L   = "{"; //$NON-NLS-1$
	public static final @Nonnull String CH_BRACE_R   = "}"; //$NON-NLS-1$
	public static final @Nonnull String CH_BRACKET_L = "["; //$NON-NLS-1$
	public static final @Nonnull String CH_BRACKET_R = "]";	 //$NON-NLS-1$
	public static final @Nonnull String COLON        = ":"; //$NON-NLS-1$
	public static final @Nonnull String CH_ENTER     = "\n"; //$NON-NLS-1$

	// Константа пустой строки.
	public static final @Nonnull String EMPTY        = ""; //$NON-NLS-1$

	// Константы формата чисел.
	public static final @Nonnull String FORMAT_0     = "%d"; //$NON-NLS-1$
	public static final @Nonnull String FORMAT_00    = "%02d"; //$NON-NLS-1$
}
