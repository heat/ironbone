package model;
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
