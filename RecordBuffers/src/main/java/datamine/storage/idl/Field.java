/**
 * Copyright (C) 2015 Turn Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package datamine.storage.idl;

import java.util.EnumSet;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.gson.GsonBuilder;

import datamine.storage.idl.type.FieldType;

/**
 * Definition of one field in Datamine
 * @author tliu
 * @author yqi
 */
public class Field implements Element {
	
	public static final int DERIVED_FIELD_ID = 0;
	
	public enum Constraint {
		REQUIRED, ASC_SORTED, DES_SORTED, OPTIONAL, FREQUENTLY_USED, DERIVED
	}
	
	private final int id;
	private final String name;
	private final FieldType type;
	private final Object defaultValue;
	private final EnumSet<Constraint> constraints;
			
	private Field(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.type = builder.type;
		this.defaultValue = builder.defaultValue;
		this.constraints = getContraintEnumSet(
				builder.isRequired, builder.isDesSorted, 
				builder.isAscSorted, builder.isCommonlyUsed,
				builder.isDerived);
	}

	public static class Builder {
		// required
		private final int id;
		private final String name;
		private final FieldType type;
		
		// optional
		private Object defaultValue = null;
		private boolean isRequired = false;
		private boolean isDesSorted = false;
		private boolean isAscSorted = false;
		private boolean isCommonlyUsed = false;
		private boolean isDerived = false;
		
		private Builder(int id, String name, FieldType type) {
			this.id = id;
			this.name = name;
			this.type = type;
		}
		
		public Builder withDefaultValue(Object dVal) {
			this.defaultValue = dVal;
			return this;
		}
		
		public Builder isRequired(boolean isRequired) {
			this.isRequired = isRequired;
			return this;
		}
		
		public Builder isDesSorted(boolean isDesSorted) {
			this.isDesSorted = isDesSorted;
			return this;
		}
		
		public Builder isAscSorted(boolean isAscSorted) {
			this.isAscSorted = isAscSorted;
			return this;
		}
		
		public Builder isFrequentlyUsed(boolean isOR) {
			this.isCommonlyUsed = isOR;
			return this;
		}
		
		public Builder isDerived(boolean isDerived) {
			this.isDerived = isDerived;
			return this;
		}
		
		public Field build() {
			return new Field(this);
		}
	}
	
	public static Builder newBuilder(int id, String name, FieldType type) {
		return new Builder(id, name, type);
	}

	/**
	 * Get a set of constraints based on the input
	 * 
	 * @param isRequired true if it is a required field
	 * @param isDesSorted true if the field is used for record sorting
	 * @param isAscSorted true if the sorting is in the ascending order (only valid when isSorted=true)
	 * @return a set of constraints based on the input
	 */
	public static EnumSet<Constraint> getContraintEnumSet(boolean isRequired,
			boolean isDesSorted, boolean isAscSorted, boolean isFrequentlyUsed, 
			boolean isDerived) {
		
		Preconditions.checkArgument(!(isDesSorted && isAscSorted), 
				"No way to keep ASC sorting and DES sorting at the same time");
		
		EnumSet<Constraint> ret;
		//1. required or not?
		if (isRequired) {
			ret = EnumSet.of(Constraint.REQUIRED);
		} else {
			ret = EnumSet.of(Constraint.OPTIONAL);
		}
		//2. sorted? if so, asc or des?
		if (isDesSorted) {
			ret.add(Constraint.DES_SORTED);
		}
		
		if (isAscSorted) {
			ret.add(Constraint.ASC_SORTED);
		}
		
		//3. hasRef?
		if (isFrequentlyUsed) {
			ret.add(Constraint.FREQUENTLY_USED);
		}
		
		//4. isDerived?
		if (isDerived) {
			ret.add(Constraint.DERIVED);
		}
		
		return ret;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public FieldType getType() {
		return type;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public EnumSet<Constraint> getConstraints() {
		return constraints;
	}
	
	public boolean isRequired() {
		return constraints.contains(Constraint.REQUIRED);
	}

	public boolean isDesSortKey() {
		return constraints.contains(Constraint.DES_SORTED);
	}
	
	public boolean isFrequentlyUsed () {
		return constraints.contains(Constraint.FREQUENTLY_USED);
	}
	
	public boolean isDerived() {
		return constraints.contains(Constraint.DERIVED);
	}
	
	public boolean isAscSortKey() {
		return constraints.contains(Constraint.ASC_SORTED);
	}
	
	public boolean isSortKey() {
		return isAscSortKey() || isDesSortKey();
	}
	
	@Override
	public String toString() {
		return new GsonBuilder().setPrettyPrinting().create().toJson(this);
	}
	
	@Override
	public void accept(ElementVisitor visitor) {
		visitor.visit(this);
	}
	
	public boolean equalToDefaultValue(Object value) {
		if (value != null && defaultValue != null && 
			defaultValue.getClass().equals(value.getClass())) {
			return Objects.equal(defaultValue, value);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((constraints == null) ? 0 : constraints.hashCode());
		result = prime * result
				+ ((defaultValue == null) ? 0 : defaultValue.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Field other = (Field) obj;
		if (constraints == null) {
			if (other.constraints != null)
				return false;
		} else if (!constraints.equals(other.constraints))
			return false;
		if (defaultValue == null) {
			if (other.defaultValue != null)
				return false;
		} else if (!defaultValue.equals(other.defaultValue))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
