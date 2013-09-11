Suller
===========

Suller is currently comprised of SullerJ, an extension to SolrJ with richer bindings.

Features
-----------
**Supported features** not present in SolrJ:
* Polymorphic queries.
* Embedded objects (like JPA's `@Embeddable`), and collections of those.
* Extensible data types. Some provided with SullerJ out of the box (GIS `Position`, `BigDecimal`, etc) but also user-defined.
* (Optional) Session semantics, preserving object identity.
* Support for arbitrary collection and array types.
* Data types mapped to several Solr fields.


Features available in **SolrJ but not in SullerJ** yet:
* Map support using dynamic fields (as in `"*_s"`).
* Getter/Setter access (instead of direct field access).

Features **planned**:
* Entity relationships.
* Custom boost values.
* Streamed imports.
* Collection support using dynamic fields (as in `"*_s"`).

Maven
-----------
Add the following dependency:
```xml
<dependency>
  <groupId>com.iotake.suller</groupId>
  <artifactId>iotake-sullerj</artifactId>
  <version>${iotake.suller.version}</version>
</dependency>
```

Snapshots and pre-release artifacts are made available in Sonatype repository:
```xml
<repository>
  <id>sonatype</id>
  <name>Sonatype OSS Repository</name>
  <url>http://oss.sonatype.org/content/groups/public</url>
</repository>
```

For GIS support use the following dependency:
```xml
<dependency>
  <groupId>com.iotake.suller</groupId>
  <artifactId>iotake-sullerj-gis</artifactId>
  <version>${iotake.suller.version}</version>
</dependency>
```

GIS dependencies can be found in the OpenGeo repository:
```xml
<repository>
  <id>repo.opengeo.org</id>
  <name>OpenGeo Maven Repository</name>
  <url>http://repo.opengeo.org</url>
</repository>
```

Spring
-----------
TBD

Document Definition
-----------
The bare minimum is to annotate document classes with `@SolrDocument` and to annotate their ids with `@SolrId`, but here is the full list of things you can do:

* `@SolrDocument`: Indicates that a class is a Solr document.

        ```java
        @SolrDocument
        public class MyDocument {...}
        ```

* `@SolrEmbeddable`: Indicates that a class is embeddable, its fields will be the containing document class.

        ```java
        @SolrEmbeddable
        public class MyEmbeddable {...}
        
        @SolrDocument
        public class MyDocument {
          private MyEmbeddable attribute;
          ...
        }
        ```

* `@SolrEnumerated`: Allows to choose to map Java enums using ordinal numbers or names. If the annotation is not specified EnumType.NAME is assumed.

        ```java
        public enum MyEnum {...}
        
        @SolrDocument
        public class MyDocument {
          @SolrEnumerated(EnumType.ORDINAL)
          private MyEnum attribute;
           ...
        }
        ```

* `@SolrId`: Defines the id field. It is mandatory to define exactly one id field per document class, but it could be inherited from a superclass.

        ```java
        @SolrDocument
        public class MyDocument {
          @SolrId
          private long attribute;
          ...
        }
        ```

* `@SolrName`: Allows to chose an alternate name for documents and fields. See: Naming.
 
        ```java
        @SolrName("MyDoc")
        @SolrDocument
        public class MyDocument {
          @SolrName("attr")
          private String attribute;
          ...
        }
        ```
* `@SolrReadable`: Allows to set fields as non-readable. If the annotation is not specified the field is assumed to be readable.
 
        ```java
        @SolrDocument
        public class MyDocument {
          @SolrReadable(false)
          private String attribute;
          ...
        }
        ```

* `@SolrTarget`: Allows to define a specific implementation class for a field type while having the field declared as a superclass or interface.
        ```java
        @SolrDocument
        public class MyDocument {
          @SolrTarget(String.class)
          private CharSequence attribute;
          ...
        }
        ```

* `@SolrTargetCollection`: Allows to define a specific implementation class for a collection field type while having the field declared as a superclass or interface. If missing the collection creator registered for the declared type is used. See: Collections.
 
        ```java
        @SolrDocument
        public class MyDocument {
          @SolrTargetCollection(ArrayList.class)
          private List<String> attribute;
          ...
        }
        ```

* `@SolrTransient`: Causes the field to be ignored by the binder.
 
        ```java
        @SolrDocument
        public class MyDocument {
          @SolrTransient
          private String attribute;
          ...
        }
        ```

* `@SolrWritable`: Allows to set fields as non-writable. If the annotation is not specified the field is assumed to be writable.

        ```java
        @SolrDocument
        public class MyDocument {
          @SolrWritable(false)
          private String attribute;
          ...
        }
        ```

Naming
-----------
TBD

Sessions
-----------
TBD

Value types
-----------
TBD

Collections
-----------
TBD
