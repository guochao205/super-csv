package org.supercsv.cellprocessor;

import java.text.DecimalFormat;

import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.ClassCastInputCSVException;
import org.supercsv.util.CSVContext;

/**
 * Converts a double into a formatted string using the {@link DecimalFormat} class and the default locale. This is
 * useful, when you need to show numbers with a specific number of digits.
 * <p>
 * Please be aware that the constructors that use <tt>DecimalFormat</tt> are not thread-safe, so it is generally better
 * to use the constructors that accept a date format String.
 * <p>
 * In the format string, the following characters are defined as : <br>
 * 
 * <pre>
 * 0   - means Digit
 * #   - means Digit, zero shows as absent (works only as zero padding on the right hand side of the number)
 * .   - means Decimal separator or monetary decimal separator
 * -   - means Minus sign
 * ,   - means Grouping separator
 * </pre>
 * 
 * <br>
 * If you want to convert from a String to a decimal, use the {@link ParseDouble} or {@link ParseBigDecimal} processor.
 * 
 * @since 1.50
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class FmtNumber extends CellProcessorAdaptor implements DoubleCellProcessor, LongCellProcessor {
	
	/** the decimal format string */
	protected String decimalFormat;
	
	/** the decimal format object - not thread safe */
	protected DecimalFormat formatter;
	
	/**
	 * Constructs a new <tt>FmtNumber</tt> processor, which converts a double into a formatted string using the supplied
	 * decimal format String. This constructor is thread-safe.
	 * 
	 * @param decimalFormat
	 *            the decimal format String (see {@link DecimalFormat})
	 */
	public FmtNumber(final String decimalFormat) {
		super();
		this.decimalFormat = decimalFormat;
	}
	
	/**
	 * Constructs a new <tt>FmtNumber</tt> processor, which converts a double into a formatted string using the supplied
	 * decimal format String, then calls the next processor in the chain. This constructor is thread-safe.
	 * 
	 * @param decimalFormat
	 *            the decimal format String (see {@link DecimalFormat})
	 * @param next
	 *            the next processor in the chain
	 */
	public FmtNumber(final String decimalFormat, final StringCellProcessor next) {
		super(next);
		this.decimalFormat = decimalFormat;
	}
	
	/**
	 * Constructs a new <tt>FmtNumber</tt> processor, which converts a double into a formatted string using the supplied
	 * decimal format. This constructor is not thread-safe.
	 * 
	 * @param formatter
	 *            the DecimalFormat
	 */
	public FmtNumber(final DecimalFormat formatter) {
		super();
		this.formatter = formatter;
	}
	
	/**
	 * Constructs a new <tt>FmtNumber</tt> processor, which converts a double into a formatted string using the supplied
	 * decimal format, then calls the next processor in the chain. This constructor is not thread-safe.
	 * 
	 * @param formatter
	 *            the DecimalFormat
	 * @param next
	 *            the next processor in the chain
	 */
	public FmtNumber(final DecimalFormat formatter, final StringCellProcessor next) {
		super(next);
		this.formatter = formatter;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context, this);
		
		if( !(value instanceof Number) ) {
			throw new ClassCastInputCSVException("the value '" + value + "' is not of type Number", context, this);
		}
		
		// create a new DecimalFormat if one is not supplied
		DecimalFormat decimalformatter = (formatter == null) ? new DecimalFormat(decimalFormat) : formatter;
		final String result = decimalformatter.format(value);
		return next.execute(result, context);
	}
}
