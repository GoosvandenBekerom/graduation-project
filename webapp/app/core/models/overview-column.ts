import {Resource} from './resource';

export class OverviewColumn extends Resource {
  name: string;
  queryColumn: string;
  visible: boolean;
  sequenceIndex: number;
  sortEnabled: boolean;
  sortIndex: number;
  sortAscending: boolean;
  jdbcType: JdbcType;
  jdbcTypeName: string;
  /**
   * Indicates if dropdown filter should be shown
   */
  dropdownFilter: boolean;
  /**
   * Comma separated list of items to show in dropdown
   */
  dropdownFilterValues: string;
}

/**
 * All JDBC Types and their respective number as of Java 8 (java.sql)
 */
export enum JdbcType {
  BIT = -7,
  TINYINT = -6,
  SMALLINT = 5,
  INTEGER = 4,
  BIGINT = -5,
  FLOAT = 6,
  REAL = 7,
  DOUBLE = 8,
  NUMERIC = 2,
  DECIMAL = 3,
  CHAR = 1,
  VARCHAR = 12,
  LONGVARCHAR = -1,
  DATE = 91,
  TIME = 92,
  TIMESTAMP = 93,
  BINARY = -2,
  VARBINARY = -3,
  LONGVARBINARY = -4,
  NULL = 0,
  OTHER = 1111,
  JAVA_OBJECT = 2000,
  DISTINCT = 2001,
  STRUCT = 2002,
  ARRAY = 2003,
  BLOB = 2004,
  CLOB = 2005,
  REF = 2006,
  DATALINK = 70,
  BOOLEAN = 16,
  ROWID = -8,
  NCHAR = -15,
  NVARCHAR = -9,
  LONGNVARCHAR = -16,
  NCLOB = 2011,
  SQLXML = 2009,
  REF_CURSOR = 2012,
  TIME_WITH_TIMEZONE = 2013,
  TIMESTAMP_WITH_TIMEZONE = 2014
}
