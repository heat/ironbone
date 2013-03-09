/*
 * Copyright 2013 OnezinoGabriel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bluewolfbr.ironbone.model;
import java.util.List;

/**
 * 
 * @author Danilo
 *
 */
public interface Table {

	// Fields
	String getDbName();
	String getClassName();
	String getFieldName();
	
	// Columns
	List<Column> getColumns();
	List<Column> getPks();
	Boolean hasFk();
	List<Column> getFks();
	List<Column> getOwnColumns();
	
	// Relationships
//	Boolean hasImportedTable();
//	List<Table> getImportedTables();
//	Boolean hasExportedTable();
//	List<Table> getExportedTables();
//	Boolean hasCrossReferencedTable();
//	List<Table> getCrossReferencedTables();
	
	
	/*
	 * 
	 * Consult http://www.codeproject.com/Articles/330447/Understanding-Association-Aggregation-and-Composit
	 * and google it! (look for Composition, Aggregation and Association)
	 * 
	 * Following a summary reference:
	 * 
 				Association								Aggregation									Composition
Owner			 No owner								 Single owner								 Single owner
Life time		 Have their own lifetime				 Have their own lifetime					 Owner's life time
Child object	 Child objects all are independent		 Child objects belong to a single parent	 Child objects belong to a single parent
	 *
	 */
	
	Boolean hasComposition();
	List<Table> getCompositionChilds();
	Boolean isComposition();
	Table getCompositionParent();
	
	Boolean hasAggregation();
	List<Table> getAggregationChilds();
	Boolean isAggregation();
	Table getAggregationParent();

	Boolean hasAssociation();
	List<Table> getAssociationChilds();
	Boolean isAssociation();
	Table getAssociationParent();
	
}
