/**
 * Copyright (C) 2016 Turn Inc. (yan.qi@turn.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package datamine.storage.recordbuffers.example.model;

import datamine.storage.api.RecordMetadataInterface;
import datamine.storage.idl.Field;
import datamine.storage.idl.type.FieldType;
import datamine.storage.idl.type.PrimitiveType;
import datamine.storage.idl.type.FieldTypeFactory;



/**
 * DO NOT CHANGE! Auto-generated code
 */
public enum FirstLevelNestedTableMetadata implements RecordMetadataInterface {

	INT_REQUIRED_COLUMN((short)1, "int_required_column", FieldTypeFactory.getPrimitiveType(PrimitiveType.INT32), true, null, false, false, false, false),
	NESTED_TABLE_COLUMN((short)2, "nested_table_column", FieldTypeFactory.getListType(FieldTypeFactory.getGroupType(SecondLevelNestedTableMetadata.class)), false, null, false, false, false, false),
	STRING_DERIVED_COLUMN((short)0, "string_derived_column", FieldTypeFactory.getPrimitiveType(PrimitiveType.STRING), false, (String)"Unknown", false, false, false, true),
;

	static final short version = 1;
	static final String name = "first_level_nested_table";
	private Field field;

	private FirstLevelNestedTableMetadata(short id, String name, FieldType type, 
		boolean isRequired, Object defaultValue, boolean isDesSorted, 
		boolean isAscSorted, boolean isFrequentlyUsed, boolean isDerived) {
		field = Field.newBuilder(id, name, type).
				withDefaultValue(defaultValue).
				isRequired(isRequired).
				isDesSorted(isDesSorted).
				isAscSorted(isAscSorted).
				isFrequentlyUsed(isFrequentlyUsed).
				isDerived(isDerived).
				build();
	}

	@Override
	public Field getField() { 
		return field; 
	}

	@Override
	public short getVersion() { 
		return version; 
	}

	@Override
	public String getTableName() { 
		return name; 
	}
}


