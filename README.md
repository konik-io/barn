# Konik 

is a easy to use open source implementation of the ZUGFeRD data model including various enhancements. 

## Features 
 - Easy and understandable API to create and read invoices.
 - Extensive documentation with examples.
 - Logical invoice validation done with JSR303
 - Validation of invoices against ZUGFeRD profiles.
 - Validation of invoices against specific country invoice regulations.
 - Easy way of invoice appending or extracting in and from PDFs.
 - Multiple PDF Carriages from different vendors. Choose your favorite one.

## Barn modules 

 - ```konik``` Contains the ZUGFeRD data model. 
 - ```itext-carriage```	Combines Konik with iText. Allows attaching or extracting XML invoices to PDF.
 - ```distribution``` Zip distribution package. For those who are not using maven.
 - ```project``` content related to the project like Scripts, Formatting rules, dictionary.  (Not a Maven module). 
 

For more information please visit http://konik.io


Continuous Integration: [![Build Status](http://ci.konik.io/job/konik/badge/icon)](http://ci.konik.io/job/konik/)
