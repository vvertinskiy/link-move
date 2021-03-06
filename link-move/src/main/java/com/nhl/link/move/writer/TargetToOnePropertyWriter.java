package com.nhl.link.move.writer;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.SelectById;
import org.apache.cayenne.reflect.ToOneProperty;
import org.apache.cayenne.util.Util;

/**
 * @since 1.4
 */
public class TargetToOnePropertyWriter implements TargetPropertyWriter {

	private ToOneProperty property;

	public TargetToOnePropertyWriter(ToOneProperty property) {
		this.property = property;
	}

	@Override
	public boolean write(DataObject target, Object value) {

		// TODO: the strategy depends on the value of
		// 'property.getRelationship().isSourceIndependentFromTargetChange()'
		// e.g. for the master side of a propagated PK, this property should be
		// handled via a PK writer (?)

		boolean updated = false;

		Object oldValue = property.readProperty(target);
		DataObject newValue = resolveRelatedObject(target.getObjectContext(), value);

		if (!Util.nullSafeEquals(oldValue, newValue)) {
			property.setTarget(target, newValue, true);
			updated = true;
		}

		return updated;
	}

	private DataObject resolveRelatedObject(ObjectContext context, Object relatedId) {
		return relatedId != null ? (DataObject) SelectById.query(property.getTargetDescriptor().getObjectClass(),
				relatedId).selectOne(context) : null;
	}
}
