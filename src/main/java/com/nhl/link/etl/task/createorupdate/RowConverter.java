package com.nhl.link.etl.task.createorupdate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nhl.link.etl.Row;
import com.nhl.link.etl.RowAttribute;

/**
 * Converts source {@link Row} to a map containing row column values by ETL
 * target key.
 * 
 * @since 1.3
 */
public class RowConverter {

	private static final RowConverter instance = new RowConverter();

	public static RowConverter instance() {
		return instance;
	}

	private RowConverter() {
		// private noop constructor
	}

	public List<Map<String, Object>> convert(List<Row> rows) {

		List<Map<String, Object>> translated = new ArrayList<>(rows.size());

		for (Row r : rows) {
			translated.add(convert(r));
		}

		return translated;
	}

	private Map<String, Object> convert(Row source) {

		// reusing template for new values
		Map<String, Object> translated = new HashMap<>();

		for (RowAttribute key : source.attributes()) {
			translated.put(key.targetName(), source.get(key));
		}

		return translated;
	}
}
